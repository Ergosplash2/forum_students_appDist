package Controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.User;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        String path = req.getServletPath();
        
        // Skip filter for public pages
        if (path.equals("/login") || path.equals("/register") || 
            path.equals("/verifyEmail") || path.equals("/resendVerification") ||
            path.equals("/") || path.equals("/index.html") ||
            path.startsWith("/test") || path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // For POST requests to addPost and addComment, check verification
        if (req.getMethod().equalsIgnoreCase("POST")) {
            if (path.equals("/addPost") || path.equals("/addComment")) {
                if (!user.isEmailVerified()) {
                    if (path.equals("/addComment")) {
                        String postId = req.getParameter("postId");
                        res.sendRedirect("viewPost?id=" + postId + "&error=Please verify your email to add comments");
                        return;
                    } else {
                        res.sendRedirect("profile?error=Email verification required to create posts");
                        return;
                    }
                }
            }
        }
        
        // For GET requests to addPost page, check verification
        if (path.equals("/addPost") && req.getMethod().equalsIgnoreCase("GET")) {
            if (!user.isEmailVerified()) {
                res.sendRedirect("profile?error=Email verification required to create posts");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void destroy() {}
}