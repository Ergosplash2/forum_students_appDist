package Controllers;

import dao.CommentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Comment;

@WebServlet("/addComment")
public class CommentServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        model.User user = (model.User) session.getAttribute("user");
        String content = request.getParameter("content");
        String postIdStr = request.getParameter("postId");
        
        // Validation
        if (content == null || content.trim().isEmpty() || 
            postIdStr == null || postIdStr.trim().isEmpty()) {
            response.sendRedirect("posts?error=Comment cannot be empty");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            
            Comment comment = new Comment();
            comment.setContent(content.trim());
            comment.setPostId(postId);
            comment.setUserId(user.getId());
            comment.setUsername(user.getUsername());
            
            CommentDAO commentDAO = new CommentDAO();
            boolean success = commentDAO.addComment(comment);
            
            if (success) {
                response.sendRedirect("viewPost?id=" + postId + "&success=Comment added!");
            } else {
                response.sendRedirect("viewPost?id=" + postId + "&error=Failed to add comment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("posts?error=Invalid post ID");
        }
    }
}