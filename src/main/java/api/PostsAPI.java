package api;

import com.google.gson.Gson;
import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/posts")
public class PostsAPI extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PostDAO postDAO = new PostDAO();
        List<Post> posts = postDAO.getAllPosts();
        
        String json = gson.toJson(posts);
        
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}