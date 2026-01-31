<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .login-container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
        }
        .login-box {
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        input[type="text"]:focus,
        input[type="password"]:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
        }
        .error {
            border-color: #ff4444 !important;
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
            text-align: center;
        }
        .general-success {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #2e7d32;
            text-align: center;
        }
        .login-button {
            width: 100%;
            padding: 14px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 10px;
        }
        .login-button:hover {
            background-color: #45a049;
        }
        .login-button:active {
            transform: translateY(1px);
        }
        .register-link {
            text-align: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            color: #666;
        }
        .register-link a {
            color: #2196F3;
            text-decoration: none;
            font-weight: bold;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
        .remember-me {
            display: flex;
            align-items: center;
            margin-top: 15px;
            margin-bottom: 10px;
            font-size: 14px;
        }
        .remember-me input[type="checkbox"] {
            margin-right: 8px;
            width: auto;
            cursor: pointer;
        }
        .remember-me label {
            margin: 0;
            font-weight: normal;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-box">
            <h2>Welcome Back</h2>
            
            <!-- Success Message (after registration) -->
            <c:if test="${not empty success}">
                <div class="general-success">
                    ✅ ${success}
                </div>
            </c:if>
            
            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="general-error">
                    ❌ ${error}
                    
                    <c:if test="${showResendLink}">
                        <div style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #e74c3c;">
                            <p style="margin: 5px 0; font-size: 0.9em;">Didn't receive the email?</p>
                            <a href="resendVerification?username=${username}" 
                               style="color: white; text-decoration: underline; font-weight: bold;">
                                Resend Verification Email
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:if>
            
            <form method="post" action="login" id="loginForm">
                <!-- Username Field -->
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" 
                           id="username" 
                           name="username" 
                           value="${not empty rememberedUsername ? rememberedUsername : (not empty username ? username : '')}"
                           class="${not empty usernameError ? 'error' : ''}"
                           placeholder="Enter your username"
                           required
                           autofocus>
                    <c:if test="${not empty usernameError}">
                        <span class="error-message">${usernameError}</span>
                    </c:if>
                </div>
                
                <!-- Password Field -->
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           class="${not empty passwordError ? 'error' : ''}"
                           placeholder="Enter your password"
                           required>
                    <c:if test="${not empty passwordError}">
                        <span class="error-message">${passwordError}</span>
                    </c:if>
                </div>
                
                <!-- Remember Me Checkbox -->
                <div class="remember-me">
                    <input type="checkbox" 
                           id="remember" 
                           name="remember"
                           ${not empty rememberedUsername ? 'checked' : ''}>
                    <label for="remember">Remember me for 30 days</label>
                </div>
                
                <!-- Submit Button -->
                <button type="submit" class="login-button">Login</button>
            </form>
            
            <!-- Register Link -->
            <div class="register-link">
                Don't have an account? <a href="register">Sign up here</a>
            </div>
        </div>
    </div>
    
    <!-- JavaScript for better UX -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('loginForm');
            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');
            
            // Clear error styling when user starts typing
            usernameInput.addEventListener('input', clearError);
            passwordInput.addEventListener('input', clearError);
            
            function clearError() {
                if (this.classList.contains('error')) {
                    this.classList.remove('error');
                    const errorSpan = this.parentNode.querySelector('.error-message');
                    if (errorSpan) {
                        errorSpan.style.display = 'none';
                    }
                }
            }
            
            // Simple client-side validation
            form.addEventListener('submit', function(event) {
                let isValid = true;
                
                // Check username
                if (usernameInput.value.trim() === '') {
                    showError(usernameInput, 'Username is required');
                    isValid = false;
                }
                
                // Check password
                if (passwordInput.value.trim() === '') {
                    showError(passwordInput, 'Password is required');
                    isValid = false;
                }
                
                if (!isValid) {
                    event.preventDefault();
                }
            });
            
            function showError(input, message) {
                input.classList.add('error');
                let errorSpan = input.parentNode.querySelector('.error-message');
                
                if (!errorSpan) {
                    errorSpan = document.createElement('span');
                    errorSpan.className = 'error-message';
                    input.parentNode.appendChild(errorSpan);
                }
                
                errorSpan.textContent = message;
                errorSpan.style.display = 'block';
                input.focus();
            }
        });
    </script>
</body>
</html>