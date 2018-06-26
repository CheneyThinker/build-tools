package com.cheney.thinker.filter;

import com.cheney.thinker.utils.CheneyThinkerUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-26
 */
@WebFilter("/CheneyThinker/*")
public class CheneyThinkerFilter implements Filter {

  public void init(FilterConfig filterConfig) {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    try {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      Map<String, Object> map = CheneyThinkerUtils.getMapFromBase64(request.getParameter("data"));
      request.setAttribute("data", map);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
    }
  }

  public void destroy() {
  }

}