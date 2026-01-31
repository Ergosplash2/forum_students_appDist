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
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check if already verified
        if (user.isEmailVerified()) {
            response.sendRedirect("posts?success=Your email is already verified!");
            return;
        }
        
        // Get user's email from session
        String email = user.getEmail();
        String username = user.getUsername();
        
        // Generate new token
        UserDAO userDAO = new UserDAO();
        String newToken = userDAO.regenerateVerificationToken(email);
        
        if (newToken != null) {
            // Send verification email
            boolean sent = EmailSender.sendVerificationEmail(email, username, newToken);
            
            if (sent) {
                // Success - show message on current page
                response.sendRedirect("posts?success=Verification email sent! Please check your inbox.");
            } else {
                response.sendRedirect("posts?error=Failed to send verification email. Please try again later.");
            }
        } else {
            response.sendRedirect("posts?error=Unable to resend verification email. Please contact support.");
        }
    }
}