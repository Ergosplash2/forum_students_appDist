<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search Posts - Student Forum</title>
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
        .back-btn {
            color: #4CAF50;
            text-decoration: none;
            font-size: 1.1em;
        }
        .back-btn:hover {
            text-decoration: underline;
        }
        .search-section {
            background-color: #f8f9fa;
            padding: 25px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .search-title {
            font-size: 1.3em;
            color: #2c3e50;
            margin-bottom: 20px;
        }
        .search-form {
            display: grid;
            gap: 15px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        label {
            font-weight: bold;
            color: #555;
            margin-bottom: 5px;
            font-size: 0.9em;
        }
        input[type="text"],
        select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        input:focus,
        select:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        .search-buttons {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        .search-btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            font-weight: bold;
        }
        .search-btn:hover {
            background-color: #45a049;
        }
        .clear-btn {
            background-color: #6c757d;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .clear-btn:hover {
            background-color: #5a6268;
        }
        .search-results-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 15px;
            background-color: #e8f5e9;
            border-left: 4px solid #4CAF50;
            border-radius: 4px;
        }
        .search-message {
            color: #2e7d32;
            font-weight: bold;
        }
        .result-count {
            color: #666;
            font-size: 0.9em;
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
        .no-results {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
        }
        .no-results h3 {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔍 Search Posts</h1>
            <a href="posts" class="back-btn">← Back to Forum</a>
        </div>
        
        <!-- Search Form -->
        <div class="search-section">
            <h2 class="search-title">Search Criteria</h2>
            <form method="post" action="search" class="search-form">
                <div class="form-row">
                    <!-- Keyword Search -->
                    <div class="form-group">
                        <label for="keyword">Keyword</label>
                        <input type="text" 
                               id="keyword" 
                               name="keyword" 
                               value="${searchKeyword}"
                               placeholder="Search in title, content, or username">
                    </div>
                    
                    <!-- University Filter -->
                    <div class="form-group">
                        <label for="university">University</label>
                        <select id="university" name="university">
                            <option value="">-- All Universities --</option>
                            <c:forEach items="${universities}" var="uni">
                                <option value="${uni}" ${searchUniversity == uni ? 'selected' : ''}>
                                    ${uni}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Tag Filter -->
                    <div class="form-group">
                        <label for="tag">Tag</label>
                        <select id="tag" name="tag">
                            <option value="">-- All Tags --</option>
                            <c:forEach items="${allTags}" var="t">
                                <option value="${t.name}" ${searchTag == t.name ? 'selected' : ''}>
                                    ${t.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <div class="search-buttons">
                    <button type="submit" class="search-btn">🔍 Search</button>
                    <a href="search" class="clear-btn">Clear Filters</a>
                </div>
            </form>
        </div>
        
        <!-- Search Results -->
        <c:if test="${not empty searchMessage}">
            <div class="search-results-header">
                <span class="search-message">${searchMessage}</span>
                <span class="result-count">${resultCount} result(s) found</span>
            </div>
        </c:if>
        
        <!-- Posts List -->
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
                                <span class="user-badge ${post.userTypeBadgeClass}">
                                    ${post.userType}
                                </span>
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
                <div class="no-results">
                    <h3>No posts found</h3>
                    <p>Try adjusting your search criteria or <a href="search">clear filters</a> to see all posts.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>