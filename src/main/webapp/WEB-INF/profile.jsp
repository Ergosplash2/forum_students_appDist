<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile - Student Forum</title>
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
        h1 {
            color: #2c3e50;
            margin: 0;
        }
        .back-btn {
            color: #4CAF50;
            text-decoration: none;
            font-size: 1.1em;
        }
        .back-btn:hover {
            text-decoration: underline;
        }
        .profile-section {
            margin-bottom: 30px;
        }
        .section-title {
            font-size: 1.3em;
            color: #2c3e50;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }
        .info-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        .info-item {
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .info-label {
            font-weight: bold;
            color: #555;
            font-size: 0.9em;
            margin-bottom: 5px;
        }
        .info-value {
            color: #333;
            font-size: 1.1em;
        }
        .info-value.empty {
            color: #999;
            font-style: italic;
        }
        .user-badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 0.85em;
            font-weight: bold;
        }
        .badge-student {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        .badge-professor {
            background-color: #e3f2fd;
            color: #1565c0;
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
        input[type="text"],
        input[type="date"],
        input[type="number"],
        select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }
        input:focus,
        select:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        .form-note {
            font-size: 0.9em;
            color: #666;
            margin-top: 5px;
        }
        .save-btn {
            background-color: #4CAF50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            font-weight: bold;
        }
        .save-btn:hover {
            background-color: #45a049;
        }
        .success-message {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #2e7d32;
        }
        .error-message {
            background-color: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #c62828;
        }
        .readonly-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 30px;
        }
        .edit-section {
            background-color: white;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>My Profile</h1>
            <a href="posts" class="back-btn">← Back to Forum</a>
        </div>
        
        <c:if test="${not empty param.success}">
            <div class="success-message">
                ✅ ${param.success}
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="error-message">
                ❌ ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty param.warning}">
    <div style="background-color: #fff3cd; color: #856404; padding: 12px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #ffc107;">
        ⚠️ ${param.warning}
    </div>
</c:if>

<c:if test="${not user.emailVerified}">
    <div style="background-color: #e8f4fd; border: 1px solid #b6d4fe; border-radius: 4px; padding: 20px; margin-bottom: 30px;">
        <h3 style="color: #0c63e4; margin-top: 0;">📧 Email Verification Required</h3>
        <p style="color: #084298; margin-bottom: 10px;">
            <strong>Your email is not verified.</strong> Please check your inbox for the verification link.
        </p>
        <div style="background-color: white; padding: 15px; border-radius: 4px; margin: 15px 0;">
            <p style="margin: 0 0 10px 0; font-weight: bold;">Without verification, you cannot:</p>
            <ul style="margin: 0; padding-left: 20px;">
                <li>Create new posts</li>
                <li>Comment on discussions</li>
            </ul>
            <p style="margin: 10px 0 0 0; font-weight: bold;">You can still:</p>
            <ul style="margin: 0; padding-left: 20px;">
                <li>Search and browse posts</li>
                <li>Update your profile</li>
                <li>View all content</li>
            </ul>
        </div>
        <p style="margin: 15px 0 0 0;">
    <a href="resendVerification" 
       style="background-color: #0d6efd; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold;">
        Resend Verification Email
    </a>
</p>
    </div>
</c:if>
        
        <!-- Account Information (Read-only) -->
        <div class="readonly-section">
            <h2 class="section-title">Account Information</h2>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">Username</div>
                    <div class="info-value">${user.username}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Email</div>
                    <div class="info-value">${user.email}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Account Type</div>
                    <div class="info-value">
                        <span class="user-badge ${user.userTypeBadgeClass}">
                            ${user.userTypeDisplay}
                        </span>
                    </div>
                </div>
                <div class="info-item">
                    <div class="info-label">Member Since</div>
                    <div class="info-value">${user.createdAt}</div>
                </div>
            </div>
            <div class="form-note">These fields cannot be changed. Contact support if you need to update them.</div>
        </div>
        
        <!-- Editable Profile Information -->
        <div class="edit-section">
            <h2 class="section-title">Profile Details (Optional)</h2>
            
            <form method="post" action="profile">
                
                <!-- Full Name -->
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" 
                           id="fullName" 
                           name="fullName" 
                           value="${user.fullName}"
                           placeholder="e.g., Ahmed Benali">
                    <div class="form-note">Your real name (optional)</div>
                </div>
                
                <!-- University (can be updated) -->
                <div class="form-group">
                    <label for="university">University</label>
                    <input type="text" 
                           id="university" 
                           name="university" 
                           value="${user.university}"
                           placeholder="e.g., Blida 1 University"
                           required>
                    <div class="form-note">Your current university or institution</div>
                </div>
                
                <!-- Specialty/Major -->
                <div class="form-group">
                    <label for="specialty">Specialty / Major</label>
                    <input type="text" 
                           id="specialty" 
                           name="specialty" 
                           value="${user.specialty}"
                           placeholder="e.g., Computer Science, Medicine, Civil Engineering">
                    <div class="form-note">Your field of study or teaching area</div>
                </div>
                
                <!-- Level -->
                <div class="form-group">
                    <label for="level">Level / Year</label>
                    <select id="level" name="level">
                        <option value="">-- Select Level --</option>
                        <option value="Year 1" ${user.level == 'Year 1' ? 'selected' : ''}>Year 1 (Licence 1)</option>
                        <option value="Year 2" ${user.level == 'Year 2' ? 'selected' : ''}>Year 2 (Licence 2)</option>
                        <option value="Year 3" ${user.level == 'Year 3' ? 'selected' : ''}>Year 3 (Licence 3)</option>
                        <option value="Master 1" ${user.level == 'Master 1' ? 'selected' : ''}>Master 1</option>
                        <option value="Master 2" ${user.level == 'Master 2' ? 'selected' : ''}>Master 2</option>
                        <option value="PhD" ${user.level == 'PhD' ? 'selected' : ''}>PhD / Doctorate</option>
                        <option value="Professor" ${user.level == 'Professor' ? 'selected' : ''}>Professor</option>
                    </select>
                    <div class="form-note">Your current academic level</div>
                </div>
                
                <!-- Birthdate (DATE type - satisfies requirement) -->
                <div class="form-group">
                    <label for="birthdate">Birthdate</label>
                    <input type="date" 
                           id="birthdate" 
                           name="birthdate" 
                           value="${user.birthdate}"
                           max="2010-12-31">
                    <div class="form-note">Your date of birth (used for age verification)</div>
                </div>
                
                <!-- Student ID (NUMBER/TEXT type - satisfies requirement) -->
                <div class="form-group">
                    <label for="studentId">Student / Employee ID</label>
                    <input type="text" 
                           id="studentId" 
                           name="studentId" 
                           value="${user.studentId}"
                           placeholder="e.g., 202012345">
                    <div class="form-note">Your student or employee identification number</div>
                </div>
                
                <!-- Graduation Year (NUMBER type - satisfies requirement) -->
                <div class="form-group">
                    <label for="graduationYear">Expected Graduation Year</label>
                    <input type="number" 
                           id="graduationYear" 
                           name="graduationYear" 
                           value="${user.graduationYear}"
                           min="2024" 
                           max="2035"
                           placeholder="e.g., 2026">
                    <div class="form-note">The year you expect to graduate</div>
                </div>
                
                <button type="submit" class="save-btn">Save Profile</button>
            </form>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Set max date for birthdate to today
            const birthdateInput = document.getElementById('birthdate');
            const today = new Date().toISOString().split('T')[0];
            birthdateInput.setAttribute('max', today);
            
         // Check for success/error messages in URL
            const urlParams = new URLSearchParams(window.location.search);
            
            if (urlParams.has('success') || urlParams.has('error') || urlParams.has('warning')) {
                // Messages are already shown by the server-side code
                // Scroll to top to see messages
                window.scrollTo(0, 0);
            }
            
            // Optional: Add AJAX for resend verification (if you want to avoid page reload)
            const resendLink = document.querySelector('a[href="resendVerification"]');
            if (resendLink) {
                resendLink.addEventListener('click', function(e) {
                    // Optional: Show loading state
                    const originalText = this.textContent;
                    this.textContent = 'Sending...';
                    this.style.opacity = '0.7';
                    
                    // The link will still work normally, but we could use AJAX here
                    // For now, let it work normally
                });
            }
            
        });
    </script>
</body>
</html>