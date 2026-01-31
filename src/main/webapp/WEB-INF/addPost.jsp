<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create New Post - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
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
        .submit-btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            font-weight: bold;
            width: 100%;
            margin-top: 20px;
        }
        .submit-btn:hover {
            background-color: #45a049;
        }
        .error-message {
            background-color: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #c62828;
        }
        .success-message {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #2e7d32;
        }
        .form-note {
            font-size: 0.9em;
            color: #666;
            margin-top: 5px;
        }
        .char-count {
            text-align: right;
            font-size: 0.85em;
            color: #666;
            margin-top: 5px;
        }
        .title-char-count {
            color: #666;
        }
        .title-char-count.warning {
            color: #f39c12;
        }
        .title-char-count.error {
            color: #e74c3c;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Create New Post</h1>
            <a href="posts" class="back-btn">← Back to Forum</a>
        </div>
        
        <c:if test="${not empty error}">
            <div class="error-message">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="success-message">
                ${success}
            </div>
        </c:if>
        
        <form method="post" action="addPost" id="postForm">
            <div class="form-group">
                <label for="title">Post Title *</label>
                <input type="text" id="title" name="title" 
                       placeholder="What would you like to ask or share?" 
                       required maxlength="200" oninput="updateTitleCharCount()">
                <div class="form-note">Be specific and descriptive</div>
                <div id="titleCharCount" class="title-char-count">0/200 characters</div>
            </div>
            
            <div class="form-group">
                <label for="content">Content *</label>
                <textarea id="content" name="content" 
                          placeholder="Write your post here. You can include questions, code snippets, or any relevant information." 
                          required oninput="updateContentCharCount()"></textarea>
                <div class="form-note">Markdown is supported</div>
                <div id="contentCharCount" class="char-count">0 characters</div>
            </div>
            
            <div class="form-group">
                <label>Tags</label>
                <div class="tags-container">
                    <c:forEach items="${allTags}" var="tag">
                        <div class="tag-option">
                            <input type="checkbox" id="tag_${tag.id}" name="tags" value="${tag.id}">
                            <label for="tag_${tag.id}">${tag.name}</label>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="new-tags-input">
                    <label for="newTags">Create New Tags (comma-separated):</label>
                    <input type="text" id="newTags" name="newTags" 
                           placeholder="e.g., CS301, Homework, UniversityName"
                           oninput="updateNewTagsCharCount()">
                    <div id="newTagsCharCount" class="char-count">0/100 characters</div>
                    <div class="tags-hint">
                        Separate multiple tags with commas. Tags help others find your post.
                        Example: "Java, Servlet, Homework, CS301"
                    </div>
                </div>
            </div>
            
            <button type="submit" class="submit-btn">Publish Post</button>
        </form>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize character counters
            updateTitleCharCount();
            updateContentCharCount();
            updateNewTagsCharCount();
            
            // Auto-capitalize first letter of new tags
            const newTagsInput = document.getElementById('newTags');
            if (newTagsInput) {
                newTagsInput.addEventListener('blur', function() {
                    let tags = this.value.split(',').map(tag => {
                        tag = tag.trim();
                        if (tag.length > 0) {
                            // Capitalize first letter, keep rest as-is
                            return tag.charAt(0).toUpperCase() + tag.slice(1);
                        }
                        return tag;
                    });
                    this.value = tags.join(', ');
                    updateNewTagsCharCount();
                });
            }
            
            
            
            // Form validation
            const form = document.getElementById('postForm');
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
                
                // Optional: Show confirmation for long posts
                if (content.length > 1000) {
                    if (!confirm('Your post is quite long (' + content.length + ' characters). Are you sure you want to publish it?')) {
                        event.preventDefault();
                    }
                }
            });
        });
        
        function updateTitleCharCount() {
            const titleInput = document.getElementById('title');
            const charCount = document.getElementById('titleCharCount');
            const length = titleInput.value.length;
            const maxLength = 200;
            
            charCount.textContent = length + '/' + maxLength + ' characters';
            
            // Update color based on length
            charCount.classList.remove('warning', 'error');
            if (length > maxLength * 0.9) { // Over 90%
                charCount.classList.add('error');
            } else if (length > maxLength * 0.8) { // Over 80%
                charCount.classList.add('warning');
            }
            
            // Enforce max length
            if (length > maxLength) {
                titleInput.value = titleInput.value.substring(0, maxLength);
                updateTitleCharCount();
            }
        }
        
        function updateContentCharCount() {
            const contentInput = document.getElementById('content');
            const charCount = document.getElementById('contentCharCount');
            const length = contentInput.value.length;
            
            charCount.textContent = length + ' characters';
            
            // Optional: Add warning for very long posts
            if (length > 5000) {
                charCount.style.color = '#e74c3c';
            } else if (length > 2000) {
                charCount.style.color = '#f39c12';
            } else {
                charCount.style.color = '#666';
            }
        }
        
        function updateNewTagsCharCount() {
            const newTagsInput = document.getElementById('newTags');
            const charCount = document.getElementById('newTagsCharCount');
            const length = newTagsInput.value.length;
            const maxLength = 100;
            
            charCount.textContent = length + '/' + maxLength + ' characters';
            
            // Update color
            if (length > maxLength) {
                charCount.style.color = '#e74c3c';
                // Truncate if over limit
                newTagsInput.value = newTagsInput.value.substring(0, maxLength);
                updateNewTagsCharCount();
            } else if (length > maxLength * 0.8) {
                charCount.style.color = '#f39c12';
            } else {
                charCount.style.color = '#666';
            }
        }
        
        // Quick tag selection helpers
        function selectAllTags() {
            document.querySelectorAll('.tag-option input[type="checkbox"]').forEach(checkbox => {
                checkbox.checked = true;
                checkbox.nextElementSibling.style.backgroundColor = '#4CAF50';
                checkbox.nextElementSibling.style.color = 'white';
            });
        }
        
        function clearAllTags() {
            document.querySelectorAll('.tag-option input[type="checkbox"]').forEach(checkbox => {
                checkbox.checked = false;
                checkbox.nextElementSibling.style.backgroundColor = '#e8f4fd';
                checkbox.nextElementSibling.style.color = '#2980b9';
            });
        }
        
        // Add these buttons to your form if you want (optional)
        // Add this HTML somewhere in your form if you want quick select buttons:
        // <div style="margin: 10px 0;">
        //     <button type="button" onclick="selectAllTags()" style="padding: 5px 10px; margin-right: 5px;">Select All</button>
        //     <button type="button" onclick="clearAllTags()" style="padding: 5px 10px;">Clear All</button>
        // </div>
    </script>
</body>
</html>