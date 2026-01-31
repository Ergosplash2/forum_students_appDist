package Controllers;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Post;
import model.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/editPost")
public class EditPostServlet extends HttpServlet {
    
    // Show edit form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String postIdStr = request.getParameter("id");
        if (postIdStr == null) {
            response.sendRedirect("posts");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            PostDAO postDAO = new PostDAO();
            Post post = postDAO.getPostById(postId);
            model.User user = (model.User) session.getAttribute("user");
            
            // Security check
            if (post == null || post.getUserId() != user.getId()) {
                response.sendRedirect("posts");
                return;
            }
            
            // Get all available tags for the form
            List<Tag> allTags = postDAO.getAllTags();
            
            request.setAttribute("post", post);
            request.setAttribute("allTags", allTags);
            request.getRequestDispatcher("/WEB-INF/editPost.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("posts");
        }
    }
    
    // Save changes
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        model.User user = (model.User) session.getAttribute("user");
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String[] tagIds = request.getParameterValues("tags");
        String newTagsInput = request.getParameter("newTags");
        
        // Validation
        if (idStr == null || title == null || content == null ||
            title.trim().isEmpty() || content.trim().isEmpty()) {
            response.sendRedirect("posts");
            return;
        }
        
        try {
            int postId = Integer.parseInt(idStr);
            
            // Create post object
            Post post = new Post();
            post.setId(postId);
            post.setTitle(title.trim());
            post.setContent(content.trim());
            post.setUserId(user.getId());
            
            // Process tags (same logic as AddPostServlet)
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
            
            // Update post in database
            PostDAO postDAO = new PostDAO();
            boolean updated = postDAO.updatePost(post);
            
            if (updated) {
                response.sendRedirect("viewPost?id=" + postId + "&success=Post updated successfully!");
            } else {
                request.setAttribute("error", "Failed to update post");
                request.setAttribute("post", post);
                request.setAttribute("allTags", postDAO.getAllTags());
                request.getRequestDispatcher("/WEB-INF/editPost.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("posts");
        }
    }
}