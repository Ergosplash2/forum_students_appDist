package Controllers;

import dao.CommentDAO;
import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Comment;
import model.Post;

@WebServlet("/viewPost")
public class ViewPostServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // Get post ID from URL parameter
        String postIdStr = request.getParameter("id");
        if (postIdStr == null || postIdStr.trim().isEmpty()) {
            response.sendRedirect("posts");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            
            PostDAO postDAO = new PostDAO();
            Post post = postDAO.getPostById(postId);
            
            if (post == null) {
                // Post not found
                response.sendRedirect("posts?error=Post not found");
                return;
            }
            
            // Get comments for this post
            CommentDAO commentDAO = new CommentDAO();
            java.util.List<Comment> comments = commentDAO.getCommentsByPostId(postId);
            
            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/WEB-INF/viewPost.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("posts?error=Invalid post ID");
        }
    }
}