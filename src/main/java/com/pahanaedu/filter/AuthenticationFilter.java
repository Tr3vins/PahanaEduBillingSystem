package com.pahanaedu.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//Filter for authentication.
@WebFilter("/*") // Apply to all URLs
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();

        // Exclude specific URLs from authentication check
        boolean isLoginOrStatic = requestURI.endsWith("/login") ||
                requestURI.endsWith("/login.jsp") ||
                requestURI.endsWith("/register") ||
                requestURI.endsWith("/register.jsp") ||
                requestURI.contains("/css/") ||
                requestURI.contains("/js/") ||
                requestURI.contains("/images/") ||
                requestURI.contains("/tailwind.min.css") ||
                requestURI.contains("/tailwind.js");

        // If user is not logged in AND trying to access a protected resource
        if (session == null || session.getAttribute("currentUser") == null) {
            if (!isLoginOrStatic) {
                // Redirect to login page
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
                return;
            }
        }

        // Continue with the request if authenticated
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}