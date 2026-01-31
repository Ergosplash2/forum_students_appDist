<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .container {
            width: 100%;
            max-width: 500px;
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 10px;
        }
        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 30px;
            font-size: 0.9em;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"],
        select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s;
        }
        select {
            cursor: pointer;
            background-color: white;
        }
        input:focus,
        select:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        input.error,
        select.error {
            border-color: #ff4444;
            background-color: #fff0f0;
        }
        .error-message {
            color: #ff4444;
            font-size: 14px;
            margin-top: 5px;
            display: block;
        }
        .general-error {
            background-color: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #c62828;
        }
        .form-note {
            font-size: 13px;
            color: #666;
            margin-top: 5px;
        }
        .required {
            color: #ff4444;
        }
        button {
            width: 100%;
            padding: 14px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 10px;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #45a049;
        }
        button:active {
            transform: translateY(1px);
        }
        .login-link {
            text-align: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            color: #666;
        }
        .login-link a {
            color: #2196F3;
            text-decoration: none;
            font-weight: bold;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
        .user-type-options {
            display: flex;
            gap: 10px;
            margin-top: 8px;
        }
        .user-type-radio {
            flex: 1;
            position: relative;
        }
        .user-type-radio input[type="radio"] {
            position: absolute;
            opacity: 0;
        }
        .user-type-radio label {
            display: block;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 4px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: normal;
        }
        .user-type-radio input[type="radio"]:checked + label {
            border-color: #4CAF50;
            background-color: #e8f5e9;
            color: #2e7d32;
            font-weight: bold;
        }
        .user-type-radio label:hover {
            border-color: #4CAF50;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Join the Student Forum</h2>
        <p class="subtitle">Connect with students and professors</p>
        
        <c:if test="${not empty error}">
            <div class="general-error">
                ${error}
            </div>
        </c:if>
        
        <form method="post" action="register" id="registerForm">
            
            <!-- Username Field -->
            <div class="form-group">
                <label for="username">Username <span class="required">*</span></label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       value="${username}"
                       class="${not empty usernameError ? 'error' : ''}"
                       placeholder="Choose a unique username"
                       required>
                <c:if test="${not empty usernameError}">
                    <span class="error-message">${usernameError}</span>
                </c:if>
                <div class="form-note">At least 3 characters, will be publicly visible</div>
            </div>
            
            <!-- Email Field -->
            <div class="form-group">
                <label for="email">Email <span class="required">*</span></label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="${email}"
                       class="${not empty emailError ? 'error' : ''}"
                       placeholder="your.email@university.dz"
                       required>
                <c:if test="${not empty emailError}">
                    <span class="error-message">${emailError}</span>
                </c:if>
                <div class="form-note">Use your university email if possible</div>
            </div>
            
            <!-- Password Field -->
            <div class="form-group">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       class="${not empty passwordError ? 'error' : ''}"
                       placeholder="Create a strong password"
                       required>
                <c:if test="${not empty passwordError}">
                    <span class="error-message">${passwordError}</span>
                </c:if>
                <div class="form-note">At least 6 characters</div>
            </div>
            
            <!-- Confirm Password Field -->
            <div class="form-group">
                <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                <input type="password" 
                       id="confirmPassword" 
                       name="confirmPassword" 
                       class="${not empty confirmPasswordError ? 'error' : ''}"
                       placeholder="Re-enter your password"
                       required>
                <c:if test="${not empty confirmPasswordError}">
                    <span class="error-message">${confirmPasswordError}</span>
                </c:if>
            </div>
            
            <!-- User Type Field (Radio Buttons) -->
            <div class="form-group">
                <label>I am a <span class="required">*</span></label>
                <div class="user-type-options">
                    <div class="user-type-radio">
                        <input type="radio" 
                               id="student" 
                               name="userType" 
                               value="STUDENT"
                               ${userType == 'STUDENT' or empty userType ? 'checked' : ''}
                               required>
                        <label for="student">🎓 Student</label>
                    </div>
                    <div class="user-type-radio">
                        <input type="radio" 
                               id="professor" 
                               name="userType" 
                               value="PROFESSOR"
                               ${userType == 'PROFESSOR' ? 'checked' : ''}>
                        <label for="professor">👨‍🏫 Professor</label>
                    </div>
                </div>
                <c:if test="${not empty userTypeError}">
                    <span class="error-message">${userTypeError}</span>
                </c:if>
            </div>
            
            <!-- University Field -->
            <div class="form-group">
                <label for="university">University <span class="required">*</span></label>
                <input type="text" 
                       id="university" 
                       name="university" 
                       value="${university}"
                       class="${not empty universityError ? 'error' : ''}"
                       placeholder="e.g., Blida 1 University"
                       required>
                <c:if test="${not empty universityError}">
                    <span class="error-message">${universityError}</span>
                </c:if>
                <div class="form-note">Your university or educational institution</div>
            </div>
            
            <!-- Submit Button -->
            <button type="submit">Create Account</button>
        </form>
        
        <div class="login-link">
            Already have an account? <a href="login">Sign in here</a>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('registerForm');
            const inputs = document.querySelectorAll('input, select');
            
            // Clear error styling when user starts typing
            inputs.forEach(input => {
                input.addEventListener('input', function() {
                    if (this.classList.contains('error')) {
                        this.classList.remove('error');
                        const errorSpan = this.parentNode.querySelector('.error-message');
                        if (errorSpan) {
                            errorSpan.style.display = 'none';
                        }
                    }
                });
            });
            
            // Form validation
            form.addEventListener('submit', function(event) {
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                
                if (password !== confirmPassword) {
                    event.preventDefault();
                    
                    const confirmInput = document.getElementById('confirmPassword');
                    confirmInput.classList.add('error');
                    
                    let errorSpan = confirmInput.parentNode.querySelector('.error-message');
                    if (!errorSpan) {
                        errorSpan = document.createElement('span');
                        errorSpan.className = 'error-message';
                        confirmInput.parentNode.appendChild(errorSpan);
                    }
                    errorSpan.textContent = 'Passwords do not match';
                    errorSpan.style.display = 'block';
                }
            });
        });
    </script>
</body>
</html>