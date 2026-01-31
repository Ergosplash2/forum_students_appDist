package Controllers;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Post;
import model.Tag;

@WebServlet("/addPost")
public class AddPostServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        model.User user = (model.User) session.getAttribute("user");
        if (!user.isEmailVerified()) {
            response.sendRedirect("profile?error=Please verify your email to create posts");
            return;
        }
        
        // Get all available tags for the form
        PostDAO postDAO = new PostDAO();
        List<Tag> allTags = postDAO.getAllTags();
        
        request.setAttribute("allTags", allTags);
        request.getRequestDispatcher("/WEB-INF/addPost.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        model.User user = (model.User) session.getAttribute("user");
        
        if (!user.isEmailVerified()) {
            response.sendRedirect("profile?error=Please verify your email to create posts");
            return;
        }
        
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String[] tagIds = request.getParameterValues("tags"); // Get selected tags
        String newTagsInput = request.getParameter("newTags"); // Get new tags input
        
        
        // Validation
        if (title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            
            request.setAttribute("error", "Title and content are required");
            PostDAO postDAO = new PostDAO();
            request.setAttribute("allTags", postDAO.getAllTags());
            request.getRequestDispatcher("/WEB-INF/addPost.jsp").forward(request, response);
            return;
        }
        
        // Create post object
        Post post = new Post();
        post.setTitle(title.trim());
        post.setContent(content.trim());
        post.setUserId(user.getId());
        post.setUsername(user.getUsername());
        
     // Process tags
        List<Tag> tags = new ArrayList<>();
        
     // 1. Add selected existing tags
        if (tagIds != null && tagIds.length > 0) {
            PostDAO postDAO = new PostDAO();
            List<Tag> allTags = postDAO.getAllTags();
            
            for (String tagIdStr : tagIds) {
                try {
                    int tagId = Integer.parseInt(tagIdStr);
                    // Find the tag in allTags list
                    for (Tag tag : allTags) {
                        if (tag.getId() == tagId) {
                            tags.add(tag);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid tag IDs
                }
            }
        }
            // 2. Add new tags from input
            if (newTagsInput != null && !newTagsInput.trim().isEmpty()) {
                String[] newTagNames = newTagsInput.split(",");
                for (String tagName : newTagNames) {
                    String trimmedName = tagName.trim();
                    if (!trimmedName.isEmpty()) {
                        // Create new tag object (ID will be set when inserted in database)
                        Tag newTag = new Tag();
                        newTag.setName(trimmedName);
                        tags.add(newTag);
                    }
                }
            }
            post.setTags(tags);
        
        
        // Save post to database
        PostDAO postDAO = new PostDAO();
        boolean success = postDAO.createPost(post);
        
        if (success) {
            response.sendRedirect("posts?success=Post created successfully!");
        } else {
            request.setAttribute("error", "Failed to create post. Please try again.");
            request.setAttribute("allTags", postDAO.getAllTags());
            request.getRequestDispatcher("/WEB-INF/addPost.jsp").forward(request, response);
        }
    }
}