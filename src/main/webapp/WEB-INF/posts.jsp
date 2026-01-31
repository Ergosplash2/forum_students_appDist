<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Forum - Posts</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #4CAF50;
        }
        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .search-btn {
            background-color: #2196F3;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-weight: bold;
        }
        .search-btn:hover {
            background-color: #1976D2;
        }
        .profile-link {
            color: #2196F3;
            text-decoration: none;
            font-weight: bold;
        }
        .profile-link:hover {
            text-decoration: underline;
        }
        .new-post-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-weight: bold;
        }
        .new-post-btn:hover {
            background-color: #45a049;
        }
        .post-card {
            border: 1px solid #ddd;
            padding: 20px;
            margin: 15px 0;
            border-radius: 5px;
            background-color: white;
            transition: box-shadow 0.3s;
        }
        .post-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .post-title {
            font-size: 1.4em;
            color: #2c3e50;
            text-decoration: none;
            margin: 0;
        }
        .post-title:hover {
            color: #4CAF50;
        }
        .post-meta {
            color: #7f8c8d;
            font-size: 0.9em;
            margin: 8px 0;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .post-author {
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .user-badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 0.75em;
            font-weight: bold;
            text-transform: uppercase;
        }
        .badge-student {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        .badge-professor {
            background-color: #e3f2fd;
            color: #1565c0;
        }
        .post-content {
            margin: 15px 0;
            line-height: 1.6;
            color: #34495e;
        }
        .post-tags {
            margin: 10px 0;
        }
        .tag {
            display: inline-block;
            background-color: #e8f4fd;
            color: #2980b9;
            padding: 3px 8px;
            border-radius: 3px;
            font-size: 0.85em;
            margin-right: 5px;
            margin-bottom: 5px;
        }
        .post-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #eee;
        }
        .comment-count {
            color: #7f8c8d;
            font-size: 0.9em;
        }
        .read-more {
            color: #4CAF50;
            text-decoration: none;
            font-weight: bold;
        }
        .read-more:hover {
            text-decoration: underline;
        }
        .no-posts {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
            font-style: italic;
        }
        .logout-btn {
            color: #e74c3c;
            text-decoration: none;
            padding: 5px 10px;
            border: 1px solid #e74c3c;
            border-radius: 3px;
        }
        .logout-btn:hover {
            background-color: #e74c3c;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
    
    <!-- Add this message section -->
    <c:if test="${not empty param.success}">
        <div style="background-color: #d4edda; color: #155724; padding: 12px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #c3e6cb;">
            ✅ ${param.success}
        </div>
    </c:if>
    
    <c:if test="${not empty param.error}">
        <div style="background-color: #f8d7da; color: #721c24; padding: 12px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #f5c6cb;">
            ❌ ${param.error}
        </div>
    </c:if>
    
    <c:if test="${not empty param.warning}">
        <div style="background-color: #fff3cd; color: #856404; padding: 12px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #ffeaa7;">
            ⚠️ ${param.warning}
        </div>
    </c:if>
    
    
        <div class="header">
            <h1>📚 Student Forum</h1>
            <div class="user-info">
    <span>Welcome, <a href="profile" class="profile-link">${sessionScope.user.username}</a>!</span>
    <a href="search" class="search-btn">🔍 Search</a>
    
    <c:choose>
        <c:when test="${sessionScope.user.emailVerified}">
            <a href="addPost" class="new-post-btn">+ New Post</a>
        </c:when>
        <c:otherwise>
            <span class="verification-warning" style="color: #f39c12; font-weight: bold; padding: 5px 10px; background: #fff3cd; border-radius: 4px;">
                ⚠️ Verify to Post
            </span>
        </c:otherwise>
    </c:choose>
    
    <a href="logout" class="logout-btn">Logout</a>
</div>
        </div>
        
        <h2>Recent Posts</h2>
        
        <c:choose>
            <c:when test="${not empty posts}">
                <c:forEach items="${posts}" var="post">
                    <div class="post-card">
                        <h3>
                            <a href="viewPost?id=${post.id}" class="post-title">${post.title}</a>
                        </h3>
                        
                        <div class="post-meta">
                            <span class="post-author">
                                Posted by <strong>${post.username}</strong>
                                <span class="user-badge badge-student">Student</span>
                            </span>
                            <span>•</span>
                            <span>${post.createdAt}</span>
                        </div>
                        
                        <div class="post-content">
                            ${post.content.length() > 300 ? post.content.substring(0,300) : post.content}
                            <c:if test="${post.content.length() > 300}">...</c:if>
                        </div>
                        
                        <c:if test="${not empty post.tags}">
                            <div class="post-tags">
                                <c:forEach items="${post.tags}" var="tag">
                                    <span class="tag">${tag.name}</span>
                                </c:forEach>
                            </div>
                        </c:if>
                        
                        <div class="post-footer">
                            <span class="comment-count">
                                💬 ${post.commentCount} comments
                            </span>
                            <a href="viewPost?id=${post.id}" class="read-more">
                                Read full post →
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-posts">
                    <h3>No posts yet</h3>
                    <p>Be the first to share something with your fellow students!</p>
                    <a href="addPost" class="new-post-btn">Create First Post</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>