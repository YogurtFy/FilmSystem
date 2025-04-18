package com.cqu.filmsystem.log;
import com.cqu.filmsystem.Service.SysLogService;
import com.cqu.filmsystem.pojo.Movie;
import com.cqu.filmsystem.pojo.Mylog;
import com.cqu.filmsystem.pojo.Syslog;

import com.cqu.filmsystem.pojo.UserInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 系统日志：切面处理类
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;//将数据写入数据库的操作
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation( com.cqu.filmsystem.pojo.Mylog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        System.out.println("切面。。。。。");
        //保存日志
        Syslog sysLog = new Syslog();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //1.获取切入点所在的方法
        Method method = signature.getMethod();

        //2.获取操作
        Mylog myLog = method.getAnnotation(Mylog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
        }

        //3.获取电影的参数 然后查询电影
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        //4.获取id的值
        Integer id = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if ("id".equals(parameterNames[i])) {
                id = (Integer) args[i];
                break;
            }
        }
        if (id != null) {
            sysLog.setMovie(new Movie())  ;
            sysLog.getMovie().setId(id);
            System.out.println("Accessing method with @Mylog annotation. Method: " + joinPoint.getSignature().getName() + ", ID: " + id);
        } else {
            System.out.println("ID parameter not found or null");
        }

        //5.获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //6.获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //7.获取时间
        sysLog.setCreateDate(new Date());

        //8.获取用户名
        // 从RequestContextHolder中获取ServletRequestAttributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 从ServletRequestAttributes中获取HttpServletRequest
            HttpServletRequest request = attributes.getRequest();
            // 从HttpServletRequest中获取HttpSession
            HttpSession session = request.getSession(false);
            // 现在你可以从session中获取你存储的对象了
            UserInfo user = (UserInfo) session.getAttribute("user");
            // 做你需要的操作 ...
            if(user!=null) {
                //只要存在用户时才存入数据库
                sysLog.setUsername(user.getNickname());
                //先查询是否之前观看过
                List<Syslog> syslogList = sysLogService.selectIsLive(sysLog);
                if (syslogList.size()==0)
                {
                    //调用service保存SysLog实体类到数据库
                    sysLogService.addLog(sysLog);
                    //  存储日志id字段  sysLog.getId()为刚插入这一数据的id
                    // 将数据存储到会话中
                    session.setAttribute("sysLogId", sysLog.getId());
                }
                else
                {
                    //更新时间即可
                    sysLogService.updateTime(sysLog);
                    //  存储日志id字段  sysLog.getId()为刚插入这一数据的id
                    // 将数据存储到会话中
                    Syslog syslog = syslogList.get(0);
                    session.setAttribute("sysLogId", syslog.getId());
                }
            }
        }
    }
}