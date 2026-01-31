package Controllers;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Post;
import model.Tag;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // Get search parameters
        String keyword = request.getParameter("keyword");
        String university = request.getParameter("university");
        String tag = request.getParameter("tag");
        
        PostDAO postDAO = new PostDAO();
        
        // Check if any search criteria provided
        boolean hasSearchCriteria = 
            (keyword != null && !keyword.trim().isEmpty()) ||
            (university != null && !university.trim().isEmpty()) ||
            (tag != null && !tag.trim().isEmpty());
        
        List<Post> posts;
        
        if (hasSearchCriteria) {
            // Perform search
            posts = postDAO.searchPosts(keyword, university, tag);
            
            // Set search message
            StringBuilder searchMsg = new StringBuilder("Search results for: ");
            boolean first = true;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                searchMsg.append("\"").append(keyword).append("\"");
                first = false;
            }
            
            if (university != null && !university.trim().isEmpty()) {
                if (!first) searchMsg.append(", ");
                searchMsg.append("University: ").append(university);
                first = false;
            }
            
            if (tag != null && !tag.trim().isEmpty()) {
                if (!first) searchMsg.append(", ");
                searchMsg.append("Tag: ").append(tag);
            }
            
            request.setAttribute("searchMessage", searchMsg.toString());
            request.setAttribute("resultCount", posts.size());
        } else {
            // No search criteria - show all posts
            posts = postDAO.getAllPosts();
        }
        
        // Get filters data for dropdowns
        List<Tag> allTags = postDAO.getAllTags();
        List<String> universities = postDAO.getAllUniversities();
        
        // Set attributes
        request.setAttribute("posts", posts);
        request.setAttribute("allTags", allTags);
        request.setAttribute("universities", universities);
        
        // Preserve search values in form
        request.setAttribute("searchKeyword", keyword != null ? keyword : "");
        request.setAttribute("searchUniversity", university != null ? university : "");
        request.setAttribute("searchTag", tag != null ? tag : "");
        
        // Forward to search results page
        request.getRequestDispatcher("/WEB-INF/search.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST to GET (search should use GET for bookmarkable URLs)
        String keyword = request.getParameter("keyword");
        String university = request.getParameter("university");
        String tag = request.getParameter("tag");
        
        StringBuilder url = new StringBuilder("search?");
        boolean first = true;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            url.append("keyword=").append(java.net.URLEncoder.encode(keyword, "UTF-8"));
            first = false;
        }
        
        if (university != null && !university.trim().isEmpty()) {
            if (!first) url.append("&");
            url.append("university=").append(java.net.URLEncoder.encode(university, "UTF-8"));
            first = false;
        }
        
        if (tag != null && !tag.trim().isEmpty()) {
            if (!first) url.append("&");
            url.append("tag=").append(java.net.URLEncoder.encode(tag, "UTF-8"));
        }
        
        response.sendRedirect(url.toString());
    }
}