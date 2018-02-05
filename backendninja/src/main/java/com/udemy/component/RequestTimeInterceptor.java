package com.udemy.component;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.udemy.repository.LogRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestTimeInterceptor.
 */
@Component("requestTimeInterceptor")
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {
    
    /** The Constant LOG. */
    private static final Log LOG = LogFactory.getLog(RequestTimeInterceptor.class);
    
    /** The log repository. */
    @Autowired
    @Qualifier("logRepository")
    private LogRepository logRepository;
    
    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
     * preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    // PRIMERO
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler) throws Exception{
        
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
     * afterCompletion(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object,
     * java.lang.Exception)
     */
    // SEGUNDO
    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex) throws Exception{
        
        long startTime = (long) request.getAttribute("startTime");
        String url = request.getRequestURL().toString();
        String username = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        }
        logRepository.save(com.udemy.entity.Log.builder()
                .url(url)
                .date(new Date())
                .details(auth.getDetails().toString())
                .username(username)
                .build());
        
        LOG.info("Url to: '" + request.getRequestURL().toString() + "'" + " in '"
                + (System.currentTimeMillis() - startTime) + "'ms");
    }
    
}
