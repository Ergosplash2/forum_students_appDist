<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${post.title} - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
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
        .post-header {
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
        }
        .post-title {
            font-size: 1.8em;
            color: #2c3e50;
            margin: 0 0 10px 0;
        }
        .post-meta {
            color: #7f8c8d;
            font-size: 0.9em;
            margin-bottom: 15px;
        }
        .post-content {
            line-height: 1.6;
            color: #34495e;
            font-size: 1.1em;
            margin: 25px 0;
            white-space: pre-wrap;
        }
        .post-tags {
            margin: 20px 0;
        }
        .tag {
            display: inline-block;
            background-color: #e8f4fd;
            color: #2980b9;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.9em;
            margin-right: 8px;
            margin-bottom: 8px;
            text-decoration: none;
        }
        .tag:hover {
            background-color: #d4e8fa;
        }
        .comments-section {
            margin-top: 40px;
        }
        .comments-header {
            font-size: 1.4em;
            color: #2c3e50;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }
        .comment {
            border: 1px solid #eee;
            padding: 20px;
            margin-bottom: 15px;
            border-radius: 5px;
            background-color: #fafafa;
        }
        .comment-meta {
            color: #7f8c8d;
            font-size: 0.9em;
            margin-bottom: 10px;
        }
        .comment-content {
            line-height: 1.5;
            color: #34495e;
        }
        .add-comment-form {
            margin-top: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .add-comment-form h3 {
            margin-top: 0;
            color: #2c3e50;
        }
        textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            font-family: Arial, sans-serif;
            min-height: 100px;
            resize: vertical;
            box-sizing: border-box;
        }
        textarea:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        .submit-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 10px;
        }
        .submit-btn:hover {
            background-color: #45a049;
        }
        .no-comments {
            text-align: center;
            padding: 30px;
            color: #7f8c8d;
            font-style: italic;
        }
        .actions {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            display: flex;
            gap: 10px;
        }
        .action-btn {
            padding: 8px 16px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: white;
            color: #333;
            text-decoration: none;
            cursor: pointer;
            font-size: 0.9em;
        }
        .action-btn:hover {
            background-color: #f5f5f5;
        }
        .delete-btn {
            border-color: #e74c3c;
            color: #e74c3c;
        }
        .delete-btn:hover {
            background-color: #e74c3c;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Student Forum</h1>
            <a href="posts" class="back-btn">← Back to All Posts</a>
        </div>
        
        <div class="post-header">
            <h1 class="post-title">${post.title}</h1>
            <div class="post-meta">
                Posted by <strong>${post.username}</strong> on ${post.createdAt}
                | ${post.commentCount} comments
            </div>
            
            <c:if test="${not empty post.tags}">
                <div class="post-tags">
                    <c:forEach items="${post.tags}" var="tag">
                        <a href="#" class="tag">${tag.name}</a>
                    </c:forEach>
                </div>
            </c:if>
        </div>
        
        <div class="post-content">
            ${post.content}
        </div>
        
        <div class="actions">
            <a href="posts" class="action-btn">Back to Forum</a>
            <c:if test="${sessionScope.user.id == post.userId}">
                <a href="editPost?id=${post.id}" class="action-btn">Edit Post</a>
                
                <form method="post" action="deletePost" style="display:inline;">
    			<input type="hidden" name="id" value="${post.id}">
    				<button type="submit" class="action-btn delete-btn"
           				 onclick="return confirm('Are you sure you want to delete this post?')">
        					Delete Post
    				</button>
				</form>

            </c:if>
        </div>
        
        <div class="comments-section">
            <h2 class="comments-header">Comments (${post.commentCount})</h2>
            
            <c:choose>
                <c:when test="${not empty comments}">
                    <c:forEach items="${comments}" var="comment">
                        <div class="comment">
                            <div class="comment-meta">
                                <strong>${comment.username}</strong> - ${comment.createdAt}
                            </div>
                            <div class="comment-content">
                                ${comment.content}
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-comments">
                        No comments yet. Be the first to comment!
                    </div>
                </c:otherwise>
            </c:choose>
            
            <div class="add-comment-form">
                <h3>Add a Comment</h3>
                <form method="post" action="addComment">
                    <input type="hidden" name="postId" value="${post.id}">
                    <textarea name="content" placeholder="Write your comment here..." required></textarea>
                    <button type="submit" class="submit-btn">Post Comment</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>