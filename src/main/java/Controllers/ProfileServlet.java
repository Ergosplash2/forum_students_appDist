package Controllers;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
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
        
        // Get fresh user data from database (in case it was updated)
        UserDAO userDAO = new UserDAO();
        User freshUser = userDAO.getUserById(user.getId());
        
        if (freshUser != null) {
            // Update session with fresh data
            session.setAttribute("user", freshUser);
            request.setAttribute("user", freshUser);
        } else {
            request.setAttribute("user", user);
        }
        
        request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User sessionUser = (User) session.getAttribute("user");
        
        // Get form data
        String fullName = request.getParameter("fullName");
        String university = request.getParameter("university");
        String specialty = request.getParameter("specialty");
        String level = request.getParameter("level");
        String birthdate = request.getParameter("birthdate");
        String studentId = request.getParameter("studentId");
        String graduationYearStr = request.getParameter("graduationYear");
        
        // Create user object with updated data
        User updatedUser = new User();
        updatedUser.setId(sessionUser.getId());
        updatedUser.setFullName(fullName != null && !fullName.trim().isEmpty() ? fullName.trim() : null);
        updatedUser.setUniversity(university != null && !university.trim().isEmpty() ? university.trim() : sessionUser.getUniversity());
        updatedUser.setSpecialty(specialty != null && !specialty.trim().isEmpty() ? specialty.trim() : null);
        updatedUser.setLevel(level != null && !level.trim().isEmpty() ? level : null);
        updatedUser.setBirthdate(birthdate != null && !birthdate.trim().isEmpty() ? birthdate : null);
        updatedUser.setStudentId(studentId != null && !studentId.trim().isEmpty() ? studentId.trim() : null);
        
        // Parse graduation year
        Integer graduationYear = null;
        if (graduationYearStr != null && !graduationYearStr.trim().isEmpty()) {
            try {
                graduationYear = Integer.parseInt(graduationYearStr.trim());
                // Validate year is reasonable
                if (graduationYear < 1900 || graduationYear > 2100) {
                    graduationYear = null;
                }
            } catch (NumberFormatException e) {
                // Invalid year format, ignore
            }
        }
        updatedUser.setGraduationYear(graduationYear);
        
        // Update profile in database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateProfile(updatedUser);
        
        if (success) {
            // Refresh user data in session
            User freshUser = userDAO.getUserById(sessionUser.getId());
            if (freshUser != null) {
                session.setAttribute("user", freshUser);
            }
            
            response.sendRedirect("profile?success=Profile updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update profile. Please try again.");
            request.setAttribute("user", sessionUser);
            request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
        }
    }
}