package dao;

import model.Post;
import model.Tag;
import java.sql.*;
import java.util.*;

public class PostDAO {
    
    // Get all posts with username and comment count
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.username, u.user_type, " +
                "(SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) as comment_count " +
                "FROM posts p JOIN users u ON p.user_id = u.id " +
                "ORDER BY p.created_at DESC";
        
        try (Connection c = DBConnection.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setCreatedAt(rs.getString("created_at"));
                post.setCommentCount(rs.getInt("comment_count"));
                post.setUserType(rs.getString("user_type"));
                
                // Get tags for this post
                post.setTags(getTagsForPost(post.getId(), c));
                
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    
    // Get single post by ID
    public Post getPostById(int postId) {
        Post post = null;
        String sql = "SELECT p.*, u.username, u.user_type, " +
                "(SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) as comment_count " +
                "FROM posts p JOIN users u ON p.user_id = u.id " +
                "WHERE p.id = ?";
        
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setCreatedAt(rs.getString("created_at"));
                post.setCommentCount(rs.getInt("comment_count"));
                post.setUserType(rs.getString("user_type"));
                
                // Get tags for this post
                post.setTags(getTagsForPost(postId, c));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }
    
    // Create new post
    public boolean createPost(Post post) {
        
    	 System.out.println("=== DEBUG: Creating post ===");
    	    System.out.println("Title: " + post.getTitle());
    	    System.out.println("Tags count: " + (post.getTags() != null ? post.getTags().size() : 0));
    	    
    	    if (post.getTags() != null) {
    	        for (int i = 0; i < post.getTags().size(); i++) {
    	            Tag tag = post.getTags().get(i);
    	            System.out.println("Tag " + i + ": ID=" + tag.getId() + ", Name=" + tag.getName());
    	        }
    	    }
    	    
    	
    	String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getContent());
                ps.setInt(3, post.getUserId());
                
                int rowsAffected = ps.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Get generated post ID
                    ResultSet rs = ps.getGeneratedKeys();
                    int postId = 0;
                    if (rs.next()) {
                        postId = rs.getInt(1);
                    }
                    
                    // Add tags if any
                    if (post.getTags() != null && !post.getTags().isEmpty()) {
                        addTagsToPost(postId, post.getTags(), conn);
                    }
                    
                    conn.commit(); // Commit transaction
                    return true;
                }
            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    // Helper method to get tags for a post
    private List<Tag> getTagsForPost(int postId, Connection c) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.id, t.name FROM tags t " +
                     "JOIN post_tags pt ON t.id = pt.tag_id " +
                     "WHERE pt.post_id = ?";
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        }
        return tags;
    }
    
    
    
    private void addTagsToPost(int postId, List<Tag> tags, Connection conn) throws SQLException {
        // First, ensure all tags exist in the tags table
        for (Tag tag : tags) {
            // Check if tag exists by name (not ID)
            String checkSql = "SELECT id FROM tags WHERE name = ?";
            int tagId = 0;
            
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, tag.getName());
                ResultSet rs = checkPs.executeQuery();
                
                if (rs.next()) {
                    // Tag exists, get its ID
                    tagId = rs.getInt("id");
                } else {
                    // Insert new tag
                    String insertSql = "INSERT INTO tags (name) VALUES (?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        insertPs.setString(1, tag.getName());
                        insertPs.executeUpdate();
                        ResultSet generatedKeys = insertPs.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            tagId = generatedKeys.getInt(1);
                        }
                    }
                }
                
                // Link tag to post (ignore duplicates)
                if (tagId > 0) {
                    String linkSql = "INSERT IGNORE INTO post_tags (post_id, tag_id) VALUES (?, ?)";
                    try (PreparedStatement linkPs = conn.prepareStatement(linkSql)) {
                        linkPs.setInt(1, postId);
                        linkPs.setInt(2, tagId);
                        linkPs.executeUpdate();
                    }
                }
            }
        }
    }
    
    // Get all available tags
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tags ORDER BY name";
        
        try (Connection c = DBConnection.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }
    
    // Update post
    public boolean updatePost(Post post) {
        String updatePostSql = "UPDATE posts SET title = ?, content = ? WHERE id = ? AND user_id = ?";
        String deleteTagsSql = "DELETE FROM post_tags WHERE post_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement ps1 = conn.prepareStatement(updatePostSql);
                PreparedStatement ps2 = conn.prepareStatement(deleteTagsSql)
            ) {
                ps1.setString(1, post.getTitle());
                ps1.setString(2, post.getContent());
                ps1.setInt(3, post.getId());
                ps1.setInt(4, post.getUserId());

                int rows = ps1.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }

                // Remove old tags
                ps2.setInt(1, post.getId());
                ps2.executeUpdate();

                // Add new tags
                if (post.getTags() != null && !post.getTags().isEmpty()) {
                    addTagsToPost(post.getId(), post.getTags(), conn);
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    

    }
    
    // Delete post
    public boolean deletePost(int postId) {
        String deleteComments = "DELETE FROM comments WHERE post_id = ?";
        String deletePost = "DELETE FROM posts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteComments);
                 PreparedStatement ps2 = conn.prepareStatement(deletePost)) {

                ps1.setInt(1, postId);
                ps1.executeUpdate();

                ps2.setInt(1, postId);
                int rows = ps2.executeUpdate();

                conn.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    
 // In PostDAO.java, add this method:
    public Tag getOrCreateTag(String tagName) {
        String sql = "SELECT id, name FROM tags WHERE name = ?";
        String insertSql = "INSERT INTO tags (name) VALUES (?)";
        
        try (Connection c = DBConnection.getConnection()) {
            // Check if tag exists
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, tagName);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return new Tag(rs.getInt("id"), rs.getString("name"));
                }
            }
            
            // Tag doesn't exist, create it
            try (PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, tagName);
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return new Tag(rs.getInt(1), tagName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isPostOwner(int postId, int userId) {
        String sql = "SELECT 1 FROM posts WHERE id = ? AND user_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
 // ADD THESE METHODS TO YOUR EXISTING PostDAO.java

 // Search posts by multiple criteria
 public List<Post> searchPosts(String keyword, String university, String tag) {
     List<Post> posts = new ArrayList<>();
     StringBuilder sql = new StringBuilder();
     
     sql.append("SELECT DISTINCT p.*, u.username, u.user_type, ");
     sql.append("(SELECT COUNT(*) FROM comments c WHERE c.post_id = p.id) as comment_count ");
     sql.append("FROM posts p ");
     sql.append("JOIN users u ON p.user_id = u.id ");
     
     // Join with tags if tag filter is used
     if (tag != null && !tag.trim().isEmpty()) {
         sql.append("LEFT JOIN post_tags pt ON p.id = pt.post_id ");
         sql.append("LEFT JOIN tags t ON pt.tag_id = t.id ");
     }
     
     sql.append("WHERE 1=1 ");
     
     // Build WHERE clause dynamically
     boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
     boolean hasUniversity = university != null && !university.trim().isEmpty();
     boolean hasTag = tag != null && !tag.trim().isEmpty();
     
     if (hasKeyword) {
         sql.append("AND (p.title LIKE ? OR p.content LIKE ? OR u.username LIKE ?) ");
     }
     
     if (hasUniversity) {
         sql.append("AND u.university LIKE ? ");
     }
     
     if (hasTag) {
         sql.append("AND t.name LIKE ? ");
     }
     
     sql.append("ORDER BY p.created_at DESC");
     
     try (Connection c = DBConnection.getConnection();
          PreparedStatement ps = c.prepareStatement(sql.toString())) {
         
         int paramIndex = 1;
         
         // Set keyword parameters
         if (hasKeyword) {
             String searchPattern = "%" + keyword.trim() + "%";
             ps.setString(paramIndex++, searchPattern); // title
             ps.setString(paramIndex++, searchPattern); // content
             ps.setString(paramIndex++, searchPattern); // username
         }
         
         // Set university parameter
         if (hasUniversity) {
             ps.setString(paramIndex++, "%" + university.trim() + "%");
         }
         
         // Set tag parameter
         if (hasTag) {
             ps.setString(paramIndex++, "%" + tag.trim() + "%");
         }
         
         ResultSet rs = ps.executeQuery();
         
         while (rs.next()) {
             Post post = new Post();
             post.setId(rs.getInt("id"));
             post.setTitle(rs.getString("title"));
             post.setContent(rs.getString("content"));
             post.setUserId(rs.getInt("user_id"));
             post.setUsername(rs.getString("username"));
             post.setUserType(rs.getString("user_type"));
             post.setCreatedAt(rs.getString("created_at"));
             post.setCommentCount(rs.getInt("comment_count"));
             
             // Get tags for this post
             post.setTags(getTagsForPost(post.getId(), c));
             
             posts.add(post);
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     
     return posts;
 }

 // Get all unique universities (for filter dropdown)
 public List<String> getAllUniversities() {
     List<String> universities = new ArrayList<>();
     String sql = "SELECT DISTINCT university FROM users WHERE university IS NOT NULL ORDER BY university";
     
     try (Connection c = DBConnection.getConnection();
          Statement stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {
         
         while (rs.next()) {
             universities.add(rs.getString("university"));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     
     return universities;
 }

}