package Controllers;

import dao.CommentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteComment")
public class DeleteCommentServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        model.User user = (model.User) session.getAttribute("user");
        String commentIdStr = request.getParameter("commentId");
        String postIdStr = request.getParameter("postId"); // To redirect back
        
        if (commentIdStr == null || postIdStr == null) {
            response.sendRedirect("posts");
            return;
        }
        
        try {
            int commentId = Integer.parseInt(commentIdStr);
            int postId = Integer.parseInt(postIdStr);
            
            CommentDAO commentDAO = new CommentDAO();
            
            // Optional: Check ownership before deletion
            // if (!commentDAO.isCommentOwner(commentId, user.getId())) {
            //     response.sendRedirect("viewPost?id=" + postId + "&error=Unauthorized");
            //     return;
            // }
            
            boolean deleted = commentDAO.deleteComment(commentId, user.getId());
            
            if (deleted) {
                response.sendRedirect("viewPost?id=" + postId + "&success=Comment deleted");
            } else {
                response.sendRedirect("viewPost?id=" + postId + "&error=Failed to delete comment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("posts");
        }
    }
}