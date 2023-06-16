package com.example.postservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PostServiceHandlerInterceptor implements HandlerInterceptor {

    Logger logger = Logger.getLogger(PostServiceHandlerInterceptor.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a dd/MM/yyyy");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("[PreHandle][" + request + "]" + "[" + request.getMethod()
                + "]" + request.getRequestURI());
        logger.info("Time: "+ dateFormat.format(new Date()));
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
         logger.info("[PostHandle][" + request + "]");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("Responsed !");
    }
}
