package Controllers;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/testReg")
public class TestRegistrationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<h1>Test Registration</h1>");
        out.println("<form method='post'>");
        out.println("Username: <input type='text' name='username'><br>");
        out.println("Email: <input type='email' name='email'><br>");
        out.println("Password: <input type='password' name='password'><br>");
        out.println("<input type='submit' value='Test'>");
        out.println("</form>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        out.println("<h1>Test Results</h1>");
        out.println("<p>Username: " + username + "</p>");
        out.println("<p>Email: " + email + "</p>");
        
        // Test database connection
        UserDAO dao = new UserDAO();
        String token = java.util.UUID.randomUUID().toString();
        
        boolean success = dao.register(username, email, password, "STUDENT", "Test University", token);
        
        if (success) {
            out.println("<p style='color: green;'>✅ Registration SUCCESS in database!</p>");
            
            // Try to login
            model.User user = dao.login(username, password);
            if (user != null) {
                out.println("<p style='color: green;'>✅ Login SUCCESS!</p>");
                out.println("<p>User ID: " + user.getId() + "</p>");
                out.println("<p>Email Verified: " + user.isEmailVerified() + "</p>");
            } else {
                out.println("<p style='color: red;'>❌ Login FAILED!</p>");
            }
        } else {
            out.println("<p style='color: red;'>❌ Registration FAILED in database!</p>");
        }
        
        out.println("<br><a href='testReg'>Test Again</a>");
    }
}