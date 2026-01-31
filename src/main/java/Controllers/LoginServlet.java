package Controllers;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check for Remember Me cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String rememberedUsername = null;
            
            for (Cookie cookie : cookies) {
                if ("rememberedUser".equals(cookie.getName())) {
                    rememberedUsername = cookie.getValue();
                    break;
                }
            }
            
            if (rememberedUsername != null) {
                request.setAttribute("rememberedUsername", rememberedUsername);
            }
        }
        
        // Check for success message from registration
        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("success", success);
        }
        
        request.getRequestDispatcher("/WEB-INF/login.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remember");
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);
        
        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());
            
            // Handle Remember Me
            if ("on".equals(rememberMe)) {
                // Create cookie to remember username for 30 days
                Cookie rememberCookie = new Cookie("rememberedUser", username);
                rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days in seconds
                rememberCookie.setPath("/");
                rememberCookie.setHttpOnly(true); // Security: prevent JavaScript access
                response.addCookie(rememberCookie);
            } else {
                // Remove remember cookie if exists
                Cookie rememberCookie = new Cookie("rememberedUser", "");
                rememberCookie.setMaxAge(0); // Delete cookie
                rememberCookie.setPath("/");
                response.addCookie(rememberCookie);
            }
            
            response.sendRedirect("posts");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username); // Preserve username on error
            request.getRequestDispatcher("/WEB-INF/login.jsp")
                   .forward(request, response);
        }
    }
}