<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Post - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
            margin: 0;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
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
        h1 {
            color: #2c3e50;
            margin: 0;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        textarea {
            min-height: 200px;
            resize: vertical;
        }
        input:focus, textarea:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        .tags-container {
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 15px;
            max-height: 200px;
            overflow-y: auto;
            margin-bottom: 15px;
        }
        .tag-option {
            display: inline-block;
            margin: 5px;
        }
        .tag-option input[type="checkbox"] {
            display: none;
        }
        .tag-option label {
            display: inline-block;
            padding: 8px 15px;
            background-color: #e8f4fd;
            color: #2980b9;
            border-radius: 20px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: normal;
            margin: 0;
        }
        .tag-option input[type="checkbox"]:checked + label {
            background-color: #4CAF50;
            color: white;
        }
        .new-tags-input {
            margin-top: 15px;
        }
        .new-tags-input input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
            margin-top: 5px;
        }
        .tags-hint {
            font-size: 0.85em;
            color: #666;
            margin-top: 5px;
            font-style: italic;
        }
        .actions {
            margin-top: 30px;
            display: flex;
            gap: 10px;
        }
        .btn {
            padding: 12px 25px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 16px;
            cursor: pointer;
            border: none;
            font-weight: bold;
        }
        .save-btn {
            background-color: #4CAF50;
            color: white;
        }
        .save-btn:hover {
            background-color: #45a049;
        }
        .cancel-btn {
            background-color: #ddd;
            color: #333;
        }
        .cancel-btn:hover {
            background-color: #ccc;
        }
        .error-message {
            background-color: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #c62828;
        }
        .form-note {
            font-size: 0.9em;
            color: #666;
            margin-top: 5px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <h1>Edit Post</h1>
        <a href="viewPost?id=${post.id}" class="back-btn">← Back to Post</a>
    </div>
    
    <c:if test="${not empty error}">
        <div class="error-message">
            ${error}
        </div>
    </c:if>

    <form method="post" action="editPost">
        <!-- Hidden field: Post ID -->
        <input type="hidden" name="id" value="${post.id}">

        <!-- Title Field -->
        <div class="form-group">
            <label for="title">Post Title *</label>
            <input type="text" 
                   id="title" 
                   name="title"
                   value="${post.title}" 
                   placeholder="What would you like to ask or share?" 
                   required 
                   maxlength="200">
            <div class="form-note">Be specific and descriptive</div>
        </div>

        <!-- Content Field -->
        <div class="form-group">
            <label for="content">Content *</label>
            <textarea id="content" 
                      name="content" 
                      placeholder="Write your post here..." 
                      required>${post.content}</textarea>
            <div class="form-note">Markdown is supported</div>
        </div>

        <!-- Tags Section -->
        <div class="form-group">
            <label>Tags</label>
            <div class="tags-container">
                <c:forEach items="${allTags}" var="tag">
                    <div class="tag-option">
                        <%-- Check if this tag is already assigned to the post --%>
                        <c:set var="isChecked" value="false" />
                        <c:forEach items="${post.tags}" var="postTag">
                            <c:if test="${postTag.id == tag.id}">
                                <c:set var="isChecked" value="true" />
                            </c:if>
                        </c:forEach>
                        
                        <input type="checkbox" 
                               id="tag_${tag.id}" 
                               name="tags" 
                               value="${tag.id}"
                               ${isChecked ? 'checked' : ''}>
                        <label for="tag_${tag.id}">${tag.name}</label>
                    </div>
                </c:forEach>
            </div>
            
            <div class="new-tags-input">
                <label for="newTags">Add New Tags (comma-separated):</label>
                <input type="text" 
                       id="newTags" 
                       name="newTags" 
                       placeholder="e.g., Java, Homework, CS301">
                <div class="tags-hint">
                    Separate multiple tags with commas. Example: "Java, Servlet, Homework"
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="actions">
            <button type="submit" class="btn save-btn">Save Changes</button>
            <a href="viewPost?id=${post.id}" class="btn cancel-btn">Cancel</a>
        </div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Form validation
        const form = document.querySelector('form');
        
        form.addEventListener('submit', function(event) {
            const title = document.getElementById('title').value.trim();
            const content = document.getElementById('content').value.trim();
            
            if (title.length === 0) {
                alert('Please enter a title for your post.');
                event.preventDefault();
                return;
            }
            
            if (content.length === 0) {
                alert('Please enter content for your post.');
                event.preventDefault();
                return;
            }
        });
        
        // Auto-capitalize first letter of new tags
        const newTagsInput = document.getElementById('newTags');
        if (newTagsInput) {
            newTagsInput.addEventListener('blur', function() {
                let tags = this.value.split(',').map(tag => {
                    tag = tag.trim();
                    if (tag.length > 0) {
                        return tag.charAt(0).toUpperCase() + tag.slice(1);
                    }
                    return tag;
                });
                this.value = tags.join(', ');
            });
        }
    });
</script>

</body>
</html>