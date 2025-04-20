package com.cqu.filmsystem.Controller;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RestController
public class ImageProxyController {

    private static final List<String> ALLOWED_DOMAINS = Arrays.asList(
            "img3.doubanio.com",
            "img1.doubanio.com",
            "img2.doubanio.com",
            "img9.doubanio.com", // ✅ 添加支持 img9
            "i0.hdslb.com",
            "i2.hdslb.com",
            "i1.hdslb.com",
            "img.youtube.com",
            "m.media-amazon.com",
            "pyy-filmsystem.oss-cn-chengdu.aliyuncs.com",
            "https://i.pravatar.cc",
            "http://filmsystem.oss-cn-beijing.aliyuncs.com"
    );

    @GetMapping("/img-proxy")
    public ResponseEntity<byte[]> proxyImage(@RequestParam("url") String imageUrl) {
        try {
            if (!isValidDomain(imageUrl)) {
                return serveDefaultImage();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
            headers.set("Referer", getRefererFromUrl(imageUrl));
            headers.set("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
            headers.set("Accept-Language", "zh-CN,zh;q=0.9");
            headers.set("Connection", "keep-alive");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // ✅ 使用支持重定向的 RestTemplate
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    URI.create(imageUrl),
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            MediaType contentType = response.getHeaders().getContentType();
            if (contentType == null || !contentType.getType().equalsIgnoreCase("image")) {
                System.out.println("不是图片，实际内容类型为：" + contentType);
                return serveDefaultImage();
            }

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(contentType);
            return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // ✅ 打印异常信息方便排查
            return serveDefaultImage();
        }
    }

    private boolean isValidDomain(String urlStr) {
        try {
            URL url = new URL(urlStr);
            String host = url.getHost().toLowerCase();
            return ALLOWED_DOMAINS.stream().anyMatch(host::contains);
        } catch (Exception e) {
            return false;
        }
    }

    private String getRefererFromUrl(String url) {
        try {
            int pathIndex = url.indexOf('/', 8);
            return pathIndex > 0 ? url.substring(0, pathIndex) : url;
        } catch (Exception e) {
            return "";
        }
    }

    private ResponseEntity<byte[]> serveDefaultImage() {
        try {
            ClassPathResource defaultImage = new ClassPathResource("static/images/default.jpg");
            byte[] imageBytes = StreamUtils.copyToByteArray(defaultImage.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
