package com.everr.reggie_take_out.filter;

import com.alibaba.fastjson.JSON;
import com.everr.reggie_take_out.common.BaseContext;
import com.everr.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description :
 * 检查用户是否已经完成登录
 *
 * @author: everr
 * @date: 2023/03/17 20:10
 **/
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//1. 读取URL，设置不需要处理的路径
        String requestURI = request.getRequestURI();
        log.info("拦截到请求： {}", requestURI);
        String[] urls = new String[]{"/employee/login", "/employee/logout", "/backend/**", "/front/**","/common/**"};
//2. 判断此次请求是否需要处理
        boolean check = check(urls, requestURI);
//3. 如果不需要处理，直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //4判断登录状态 ，如果登录，放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,用户id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            long id =Thread.currentThread().getId();
            log.info("线程id为：{}",id);
            filterChain.doFilter(request, response);
            return;
        }
        ;
            //如过未登录，通过输出流的方式
        log.info("用戶未登錄");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
    }

    /**
     * 路径匹配，检查是否需要过滤
     *
     * @param urls
     * @param requestURI
     * @return
     */

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }

        }
        return false;
    }

}
