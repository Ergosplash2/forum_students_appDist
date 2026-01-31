package Controllers;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/verifyEmail")
public class VerifyEmailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Invalid verification link. Token is missing.");
            request.getRequestDispatcher("/WEB-INF/verificationResult.jsp")
                   .forward(request, response);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        boolean verified = userDAO.verifyEmail(token);
        
        if (verified) {
            request.setAttribute("success", true);
            request.setAttribute("message", "Email verified successfully! You can now login to your account.");
        } else {
            request.setAttribute("success", false);
            request.setAttribute("message", "Verification failed. The link may be expired or invalid.");
        }
        
        request.getRequestDispatcher("/WEB-INF/verificationResult.jsp")
               .forward(request, response);
    }
}