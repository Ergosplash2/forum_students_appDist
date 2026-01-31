package Controllers;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/deletePost")
public class DeletePostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        model.User user = (model.User) session.getAttribute("user");

        String postIdStr = request.getParameter("id");
        if (postIdStr == null) {
            response.sendRedirect("posts");
            return;
        }

        try {
            int postId = Integer.parseInt(postIdStr);
            PostDAO postDAO = new PostDAO();

            // 🔐 Ownership check
            if (!postDAO.isPostOwner(postId, user.getId())) {
                response.sendRedirect("posts?error=Unauthorized action");
                return;
            }

            boolean deleted = postDAO.deletePost(postId);

            if (deleted) {
                response.sendRedirect("posts?success=Post deleted successfully");
            } else {
                response.sendRedirect("viewPost?id=" + postId + "&error=Delete failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("posts");
        }
    }
}
