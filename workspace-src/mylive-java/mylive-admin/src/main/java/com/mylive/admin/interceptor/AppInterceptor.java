package com.mylive.admin.interceptor;

import com.mylive.component.RedisComponent;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.ResponseCodeEnum;
import com.mylive.exception.BusinessException;
import com.mylive.utils.StringTools;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppInterceptor implements HandlerInterceptor {

    private static final String URL_ACCOUNT = "/account";
    private static final String URL_FILE = "/file";

    @Resource
    private RedisComponent redisComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if(null == handler){
            return false;
        }
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if(request.getRequestURI().contains(URL_ACCOUNT)){
            return true;
        }
        String token = request.getHeader(Constants.TOKEN_ADMIN);
        //获取图片时
        if(request.getRequestURI().contains(URL_FILE)){
            token = getTokenFromCookie(request);
        }
        if(StringTools.isEmpty(token)){
           throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        Object sessionObj = redisComponent.getTokenInfo4Admin(token);
        if(null == sessionObj){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        return true;
    }
    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(null == cookies){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Constants.TOKEN_ADMIN.equals(cookie.getName())) {
               return cookie.getValue();
            }
        }
        return null;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
