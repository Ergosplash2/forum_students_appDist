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

@WebServlet("/api/search")
public class SearchAPI extends HttpServlet {
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String keyword = request.getParameter("q");
        String university = request.getParameter("university");
        String tag = request.getParameter("tag");
        
        PostDAO postDAO = new PostDAO();
        List<Post> results = postDAO.searchPosts(keyword, university, tag);
        
        String json = gson.toJson(results);
        
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}