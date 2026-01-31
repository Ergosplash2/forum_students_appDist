package Controllers;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Post;

@WebServlet("/posts")
public class ListPostServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        PostDAO postDAO = new PostDAO();
        List<Post> posts = postDAO.getAllPosts();
        
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/posts.jsp").forward(request, response);
    }
}