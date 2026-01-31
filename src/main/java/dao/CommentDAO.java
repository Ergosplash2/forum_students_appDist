package dao;

import model.Comment;
import java.sql.*;
import java.util.*;

public class CommentDAO {
    
    // Get all comments for a post
    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username FROM comments c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "WHERE c.post_id = ? ORDER BY c.created_at ASC";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setPostId(rs.getInt("post_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setUsername(rs.getString("username"));
                comment.setCreatedAt(rs.getString("created_at"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    
    // Add a new comment
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO comments (content, post_id, user_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, comment.getContent());
            ps.setInt(2, comment.getPostId());
            ps.setInt(3, comment.getUserId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}