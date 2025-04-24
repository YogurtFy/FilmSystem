# encoding: utf-8
import requests
from lxml import etree
import mysql.connector
from datetime import datetime
import time
headers = {
    'User-Agent':
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36'
}

db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '220211',
    'database': 'filmsystem',
    'charset': 'utf8mb4'
}

# 获取安全的第一个文本
def get_first_text(lst):
    try:
        return lst[0].strip()
    except:
        return ""

# 安全的 XPath 提取函数
def safe_xpath(html, xpath_exprs):
    for xpath_expr in xpath_exprs:
        result = html.xpath(xpath_expr)
        if result:
            return get_first_text(result)
    return ""

# 地区处理：存在则返回 ID，不存在则插入
def get_or_create_region_id(cursor, region_name):
    cursor.execute("SELECT region_id FROM regions WHERE region_name = %s", (region_name,))
    result = cursor.fetchone()
    if result:
        return result[0]
    else:
        cursor.execute("INSERT INTO regions (region_name) VALUES (%s)", (region_name,))
        return cursor.lastrowid

def extract_first_region(region_raw):
    try:
        return region_raw.split("/")[0].strip()
    except:
        return region_raw.strip() if region_raw else ""

def get_movie_id(cursor, title, release_date):
    cursor.execute("SELECT id FROM movies WHERE title=%s AND release_date=%s", (title, release_date))
    result = cursor.fetchone()
    return result[0] if result else None

def get_or_create_actor_id(cursor, actor_name, photo_url):
    cursor.execute("SELECT actor_id FROM actors WHERE actor_name = %s", (actor_name,))
    result = cursor.fetchone()
    if result:
        return result[0]
    else:
        cursor.execute("INSERT INTO actors (actor_name, actor_picture) VALUES (%s, %s)", (actor_name, photo_url))
        return cursor.lastrowid

def get_or_create_category_id(cursor, category_name):
    cursor.execute("SELECT actor_id FROM actors WHERE category_name = %s", (category_name,))
    result = cursor.fetchone()
    if result:
        return result[0] 
    else:
        cursor.execute("INSERT INTO actors (category_name) VALUES (%s)", (category_name,))
        return cursor.lastrowid

# 主函数
def getMovieData():
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()
    url = 'https://movie.douban.com/subject/36660929/'
    try:
        res_new = requests.get(url=url, headers=headers)
        res_new.encoding = res_new.apparent_encoding
        html_new = etree.HTML(res_new.text)

        title = safe_xpath(html_new, ['//*[@id="content"]/h1/span[1]/text()'])
        director = safe_xpath(html_new, ['//*[@id="info"]//span[contains(text(),"导演")]/following-sibling::span[1]/a/text()'])
        region = extract_first_region(safe_xpath(html_new, ['//*[@id="info"]//span[contains(text(),"制片国家")]/following-sibling::text()[1]']))
        region_id = get_or_create_region_id(cursor, region)

        language = safe_xpath(html_new, ['//*[@id="info"]//span[contains(text(),"语言")]/following-sibling::text()[1]'])
        year_str = safe_xpath(html_new, [
            '//*[@id="info"]//span[@property="v:initialReleaseDate"]/text()',
            '//*[@id="info"]//span[contains(text(),"上映日期")]/following-sibling::text()[1]'  # 备选方案
        ])

        try:
            first_date_str = year_str.split('/')[0].strip().split('(')[0].strip()
            release_date = datetime.strptime(first_date_str, '%Y-%m-%d').date()
        except:
            release_date = None

        description = safe_xpath(html_new, [
            '//*[@id="link-report-intra"]/span[1]//text()',
            '//*[@id="link-report"]/span[1]//text()'
        ])
        average_rating = safe_xpath(html_new, ['//*[@id="interest_sectl"]//strong[@class="ll rating_num"]/text()'])
        picture = safe_xpath(html_new, ['//*[@id="mainpic"]/a/img/@src'])
        runtime = 0  # 豆瓣页面一般不直接提供，默认为0

        print(title, director, release_date, language)

        insert_query = """
        INSERT INTO movies (title, director, release_date, runtime, language, description, average_rating, picture, region_id)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(insert_query, (title, director, release_date, runtime, language, description, average_rating, picture, region_id))
        movie_id = get_movie_id(cursor, title, release_date)  # 获取当前插入电影 ID

         # ✅ 获取并插入演员信息
        actor_elements = html_new.xpath('//*[@id="info"]//span[contains(text(),"主演")]/following-sibling::span[1]/a')[:5]
        for actor_el in actor_elements:
            actor_name = actor_el.xpath('./text()')[0].strip()
            actor_link = actor_el.xpath('./@href')[0].strip()

            # 请求演员详情页
            try:
                actor_res = requests.get(actor_link, headers=headers, timeout=10)
                actor_res.encoding = actor_res.apparent_encoding
                actor_html = etree.HTML(actor_res.text)
                
                # 豆瓣演员页的头像图片（可能路径略有不同，可根据页面调试）
                photo_url = actor_html.xpath('//*[@id="content"]/div/div[1]/section[1]/div[1]/div[1]/div[1]/img/@src')
                photo_url = photo_url[0].strip() if photo_url else ""

            except Exception as e:
                print(f"获取演员 {actor_name} 照片失败：{e}")
                photo_url = ""

            # 存入数据库
            actor_id = get_or_create_actor_id(cursor, actor_name, photo_url)
            cursor.execute("INSERT IGNORE INTO movie_actors (movie_id, actor_id) VALUES (%s, %s)", (movie_id, actor_id))
            
        
        # 处理类别
        category_list = html_new.xpath('//*[@id="info"]//span[@property="v:genre"]/text()')
        category_list = [cat.strip() for cat in category_list if cat.strip()]
        category_list = list(set(category_list))  # 去重
        for category in category_list:
            category = category.strip()
            print(category)
            if not category:
                continue

            # 插入或获取类别 ID
            cursor.execute("SELECT category_id FROM category WHERE category_name = %s", (category,))
            result = cursor.fetchone()
            if result:
                category_id = result[0]
            else:
                cursor.execute("INSERT INTO category (category_name) VALUES (%s)", (category,))
                category_id = cursor.lastrowid

            # 插入 movie_category 关联表
            cursor.execute("INSERT INTO movie_category (movie_id, category_id) VALUES (%s, %s)", (movie_id, category_id))        
    except Exception as e:
        print("爬取或入库失败：", e)
    
    conn.commit()
    cursor.close()
    conn.close()
    print("数据已存入数据库！")

def main():
    getMovieData()

if __name__ == '__main__':
    main()
