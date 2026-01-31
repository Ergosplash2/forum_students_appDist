package Controllers;

import dao.UserDAO;
import utils.EmailValidator;
import utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/register.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String userType = request.getParameter("userType");
        String university = request.getParameter("university");
        
        // Preserve form values in case of error
        request.setAttribute("username", username != null ? username : "");
        request.setAttribute("email", email != null ? email : "");
        request.setAttribute("userType", userType != null ? userType : "");
        request.setAttribute("university", university != null ? university : "");
        
        // Error tracking
        boolean hasError = false;
        Map<String, String> errors = new HashMap<>();
        
        // Validate username
        if (username == null || username.trim().isEmpty() || username.length() < 3) {
            errors.put("usernameError", "Username must be at least 3 characters");
            hasError = true;
        }
        
        // Validate email
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("emailError", "Please enter a valid email address");
            hasError = true;
        } else {
            // Use external API to validate email
            EmailValidator.ValidationResult result = EmailValidator.validateEmail(email);
            if (!result.isValid()) {
                errors.put("emailError", result.getMessage());
                hasError = true;
            }
        }
        
        // Validate password
        if (password == null || password.length() < 6) {
            errors.put("passwordError", "Password must be at least 6 characters");
            hasError = true;
        }
        
        // Validate confirm password
        if (confirmPassword == null || !confirmPassword.equals(password)) {
            errors.put("confirmPasswordError", "Passwords do not match");
            hasError = true;
        }
        
        // Validate user type
        if (userType == null || userType.trim().isEmpty()) {
            errors.put("userTypeError", "Please select your role");
            hasError = true;
        } else if (!userType.equals("STUDENT") && !userType.equals("PROFESSOR")) {
            errors.put("userTypeError", "Invalid user type selected");
            hasError = true;
        }
        
        // Validate university
        if (university == null || university.trim().isEmpty()) {
            errors.put("universityError", "University name is required");
            hasError = true;
        } else if (university.trim().length() < 3) {
            errors.put("universityError", "University name must be at least 3 characters");
            hasError = true;
        }
        
        // If validation errors, return to form
        if (hasError) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                request.setAttribute(error.getKey(), error.getValue());
            }
            
            request.setAttribute("error", "Please fix the errors below");
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
            return;
        }
        
        // Register user
        UserDAO userDAO = new UserDAO();
        
        // Generate verification token
        String verificationToken = EmailSender.generateToken();
        
        boolean success = userDAO.register(username, email, password, userType, university, verificationToken);
        
        if (success) {
            // Send verification email
            boolean emailSent = EmailSender.sendVerificationEmail(email, username, verificationToken);
            
            if (emailSent) {
                response.sendRedirect("login?success=Registration successful! Please check your email to verify your account.");
            } else {
                // Account created but email failed
                response.sendRedirect("login?warning=Account created but verification email failed. Please contact support.");
            }
        } else {
            request.setAttribute("error", "Registration failed. Username or email may already exist.");
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
        }
    }
}