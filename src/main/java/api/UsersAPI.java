package api;

import com.google.gson.JsonObject;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

@WebServlet("/api/users/*")
public class UsersAPI extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Access to all users not allowed");
            error.addProperty("status", 403);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(error.toString());
            return;
        }
        
        try {
            String userIdStr = pathInfo.substring(1);
            int userId = Integer.parseInt(userIdStr);
            
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            
            if (user != null) {
                // Build JSON manually - ONLY include what we want
                JsonObject json = new JsonObject();
                json.addProperty("id", user.getId());
                json.addProperty("username", user.getUsername());
                json.addProperty("userType", user.getUserType());
                json.addProperty("university", user.getUniversity());
                json.addProperty("createdAt", user.getCreatedAt());
                
                response.getWriter().write(json.toString());
            } else {
                JsonObject error = new JsonObject();
                error.addProperty("error", "User not found");
                error.addProperty("status", 404);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(error.toString());
            }
        } catch (NumberFormatException e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Invalid user ID");
            error.addProperty("status", 400);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(error.toString());
        }
    }
}