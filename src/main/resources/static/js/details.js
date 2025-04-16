//支付
$('#payButton').popup({
    popup: $('.payQR.popup'),
    on: 'click',
    position: 'bottom center'
});

//目录初始化
tocbot.init({
    // Where to render the table of contents.
    tocSelector: '.js-toc',
    // Where to grab the headings to build the table of contents.
    contentSelector: '.js-toc-content',
    // Which headings to grab inside of the contentSelector element.
    headingSelector: 'h1, h2, h3',
});

//目录点击展示
$('.toc.button').popup({
    popup: $('.toc-container.popup'),
    on: 'click',
    position: 'left center'
});

$('#toTop-button').click(function () {
    $(window).scrollTo(0, 500);
});


$("#comment-btn").click(function () {

    if($("#contentText").val().length > 0){
        console.log("校验成功");
        $("#con_div").css("border","");
        postData();
    } else {
        $("#con_div").css("border","2px solid red");
        console.log("校验失败");
    }
});

//分页点击
function page(obj) {
    var pageNum = $(obj).attr('pageNum');
    $("#comment-container").load(/*[[@{/font/blog/showAllCommentByBlogId}]]*/"", {
        id: /*[[${blog.id}]]*/1,
        pageNum: pageNum
    });
}

function postData() {
    //品论用户id在控制层获取
    $("#comment-container").load(/*[[@{/font/blog/addComment}]]*/"", {
        "blogId": $("[name='blog.id']").val(),
        "parentCommentId": $("[name='parentComment.id']").val(),
        "content": $("[name='content']").val()
    }, function (response, status, xhr) {

        $("[name='content']").val('');
        $("[name='parentComment.id']").val(-1);
        $("[name='content']").attr("placeholder", "请输入评论信息...");

        //评论成功
        //滚动到评论区
        // $(window).scrollTo($("#comment-container"), 500);
        //清除一些内容
    });
}


//回复
function reply(obj) {
    var commentId = $(obj).data('commentid');
    var nickname = $(obj).data('nickname');
    $("[name='content']").attr("placeholder", "@" + nickname).focus();
    $("[name='parentComment.id']").val(commentId);
    $(window).scrollTo($("#commentForm"), 500);
}


$(document).ready(function() {
    $('#movie-rating').rating(); // 初始化评分组件
    $('#movie-rating').rating('setting', 'onRate', function(value) {
        alert('您给这部电影的评分是：' + value); // 这里可以替换为发送评分到服务器的代码
        // 构造要发送的数据，这里假设您要发送的是评分和电影ID
        var dataToSend = {
            movieId: $('#movie-rating').attr('data-movie-id'), // 假设您的评分组件有一个data-movie-id属性
            rating: value
        };

        // AJAX请求将评分发送到服务器
        $.ajax({
            type: 'POST',                   // 使用POST方法
            url: '/rating/submit',          // 这是处理评分数据的服务器端URL
            data: dataToSend,               // 要发送的数据
            dataType: 'json',               // 期望从服务器返回的数据类型
            success: function(response) {   // 请求成功时的回调函数
                // 这里可以添加您的逻辑处理服务器响应
                alert('评分提交成功!');     // 例如，弹出成功消息
            },
            error: function(xhr, status, error) { // 请求失败时的回调函数
                // 这里可以添加错误处理逻辑
                alert('评分提交失败，请重试。'); // 例如，弹出错误消息
            }
        });
    });
});


document.addEventListener('DOMContentLoaded', function() {
    var video = document.getElementById('videoPlayer');
    // 这里可以添加一些视频控制的JS代码，例如播放、暂停等
    // 示例：暂停和播放视频
    video.addEventListener('click', function() {
        if (video.paused) {
            video.play();
        } else {
            video.pause();
        }
    });
});
