package Controllers;

import dao.UserDAO;
import model.User;
import utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/resendVerification")
public class ResendVerificationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("login?error=Username is required");
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        
        // Get user by username (need to create this method or use existing)
        // For now, we'll assume user provides email
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            // Show form to enter email
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/resendVerification.jsp")
                   .forward(request, response);
            return;
        }
        
        // Check if already verified
        if (userDAO.isEmailVerified(email)) {
            response.sendRedirect("login?success=Your email is already verified! You can login now.");
            return;
        }
        
        // Generate new token
        String newToken = userDAO.regenerateVerificationToken(email);
        
        if (newToken != null) {
            // Send verification email
            boolean sent = EmailSender.sendVerificationEmail(email, username, newToken);
            
            if (sent) {
                response.sendRedirect("login?success=Verification email sent! Please check your inbox.");
            } else {
                response.sendRedirect("login?error=Failed to send verification email. Please try again later.");
            }
        } else {
            response.sendRedirect("login?error=Unable to resend verification email. Please contact support.");
        }
    }
}