package com.cqu.filmsystem.Controller;

import com.cqu.filmsystem.pojo.AiResult;
import com.cqu.filmsystem.pojo.ContentDto;
import com.cqu.filmsystem.pojo.OllamaResult;
import com.cqu.filmsystem.utils.JsonUtils;
import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Controller
public class AiController {
    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private static final String DONE = "[DONE]";
    private static final Integer timeout = 60;

    private static final String AI_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";

    private static final String URL_OLLAMA = "http://localhost:11434/api/generate";
    private static final String MODEL_DEEPSEEK = "deepseek-r1:8b";

    private static final String MODEL_ULTRA = "4.0Ultra";

    @Value("${api.password:}")
    private String apiPassword;

    @GetMapping("/ai")
    public String aiPage(HttpSession session) {
        // 检查用户是否已登录
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "Ai";
    }

    @GetMapping(value = "/ai/chat")
    @ResponseBody
    public void handleSse(String message, String model, HttpServletResponse response, HttpSession session) {
        // 检查用户是否已登录
        if (session.getAttribute("user") == null) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
            } catch (IOException e) {
                logger.error("发送未授权响应失败", e);
            }
            return;
        }
        // 设置SSE响应头
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");
        // 流式输出核心逻辑
        try (PrintWriter pw = response.getWriter()) {
            // 根据选择的模型调用不同的处理方法
            if ("ultra".equals(model)) {
                getAiResult4Ultra(pw, message);
            } else {
                // 默认使用Deepseek模型
                getAiResult4Deepseek(pw, message);
            }
            pw.write("data:end\n\n");
            pw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAiResult4Ultra(PrintWriter pw, String content) throws InterruptedException {

        // 构造符合讯飞API规范的请求体[6](@ref)
        Map<String, Object> params = new HashMap<>();
        params.put("model", "4.0Ultra");
        //result.put("user", "4.0Ultra");

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);

        List<Map> messages = new ArrayList<>();
        messages.add(message);
        params.put("messages", messages);
        params.put("stream", true);
        String jsonParams = JsonUtils.convertObj2Json(params);
        // 添加鉴权头部[7](@ref)
        Request.Builder builder = new Request.Builder().url(AI_URL);
        builder.addHeader("Authorization", " Bearer " + apiPassword);
        builder.addHeader("Accept", "text/event-stream");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = builder.post(body).build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout,
                TimeUnit.SECONDS).build();

        // 实例化EventSource，注册EventSource监听器 -- 创建一个用于处理服务器发送事件的实例，并定义处理事件的回调逻辑
        CountDownLatch eventLatch = new CountDownLatch(1);

        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if (DONE.equals(data)) {
                    return;
                }
                // 解析讯飞API返回的JSON数据[2](@ref)
                String content = getContent(data);
                pw.write("data:" + JsonUtils.convertObj2Json(new ContentDto(content)) + "\n\n");
                pw.flush();
            }

            @Override
            public void onClosed(EventSource eventSource) {
                super.onClosed(eventSource);
                eventLatch.countDown();
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                logger.info("调用接口失败{}", t);
                if (eventLatch != null) {
                    eventLatch.countDown();
                }
            }
        });
        // 与服务器建立连接
        realEventSource.connect(client);
        // await() 方法被调用来阻塞当前线程，直到 CountDownLatch 的计数变为0。
        eventLatch.await();
    }

    private static String getContent(String data) {
        AiResult aiResult = JsonUtils.convertJson2Obj(data, AiResult.class);
        return aiResult.getChoices().get(0).getDelta().getContent();
    }

    private void getAiResult4Deepseek(PrintWriter pw, String message) throws InterruptedException {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("prompt", message);
        params.put("model", MODEL_DEEPSEEK);
        params.put("stream", true);
        String jsonParams = JsonUtils.convertObj2Json(params);
        // 构建HTTP请求
        Request.Builder builder = new Request.Builder().url(URL_OLLAMA);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = builder.post(body).build();
        // 异步发送请求并处理响应
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout,
                TimeUnit.SECONDS).build();
        CountDownLatch eventLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("请求失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try (okhttp3.ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            // 逐行读取响应
                            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream(), StandardCharsets.UTF_8));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                OllamaResult aiResult = JsonUtils.convertJson2Obj(line, OllamaResult.class);
                                if (aiResult.getDone()) {
                                    break;
                                }
                                logger.info(aiResult.getResponse());
                                pw.write("data:" + JsonUtils.convertObj2Json(new ContentDto(aiResult.getResponse())) + "\n\n");
                                pw.flush();
                            }
                            eventLatch.countDown();
                        }
                    }
                } else {
                    logger.error("请求失败", response);
                }
            }
        });
        eventLatch.await();
    }
}
