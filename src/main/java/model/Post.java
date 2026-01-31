package model;

import java.util.List;

public class Post {
    private int id;
    private String title;
    private String content;
    private int userId;
    private String username; // For display
    private String createdAt;
    private List<Tag> tags;
    private int commentCount;
    private String userType;

    // Constructors
    public Post() {}

    public Post(int id, String title, String content, int userId, String username, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            sb.append(tag.getName()).append(",");
        }

        // Remove last comma
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    public String getUserTypeBadgeClass() {
        if ("STUDENT".equals(userType)) return "badge-student";
        if ("PROFESSOR".equals(userType)) return "badge-professor";
        return "";
    }

}