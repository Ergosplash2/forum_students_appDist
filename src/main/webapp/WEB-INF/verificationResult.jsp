<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Email Verification - Student Forum</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 40px 20px;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            max-width: 600px;
            width: 100%;
            background: white;
            border-radius: 12px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .header {
            padding: 40px;
            text-align: center;
        }
        .header.success {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
            color: white;
        }
        .header.error {
            background: linear-gradient(135deg, #f44336 0%, #e53935 100%);
            color: white;
        }
        .header h1 {
            margin: 0 0 10px 0;
            font-size: 2.5em;
        }
        .header p {
            margin: 0;
            font-size: 1.2em;
            opacity: 0.9;
        }
        .content {
            padding: 40px;
            text-align: center;
        }
        .icon {
            font-size: 5em;
            margin-bottom: 20px;
        }
        .icon.success {
            color: #4CAF50;
        }
        .icon.error {
            color: #f44336;
        }
        .message {
            font-size: 1.2em;
            color: #555;
            line-height: 1.8;
            margin-bottom: 30px;
        }
        .button {
            display: inline-block;
            padding: 15px 40px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            font-size: 1.1em;
            transition: background 0.3s;
            margin: 10px;
        }
        .button:hover {
            background: #5568d3;
        }
        .button.secondary {
            background: #6c757d;
        }
        .button.secondary:hover {
            background: #5a6268;
        }
        .info-box {
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 20px;
            margin: 30px 0;
            text-align: left;
        }
        .info-box h3 {
            margin-top: 0;
            color: #667eea;
        }
        .info-box ul {
            margin: 10px 0;
            padding-left: 20px;
        }
        .info-box li {
            margin: 8px 0;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <c:choose>
            <c:when test="${success}">
                <!-- Success State -->
                <div class="header success">
                    <h1>✅ Email Verified!</h1>
                    <p>Your account is now active</p>
                </div>
                
                <div class="content">
                    <div class="icon success">🎉</div>
                    
                    <div class="message">
                        ${message}
                    </div>
                    
                    <div class="info-box">
                        <h3>What's Next?</h3>
                        <ul>
                            <li>Login with your username and password</li>
                            <li>Complete your profile (optional)</li>
                            <li>Start creating posts and discussions</li>
                            <li>Connect with students and professors</li>
                        </ul>
                    </div>
                    
                    <a href="login" class="button">Go to Login</a>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Error State -->
                <div class="header error">
                    <h1>❌ Verification Failed</h1>
                    <p>Unable to verify your email</p>
                </div>
                
                <div class="content">
                    <div class="icon error">⚠️</div>
                    
                    <div class="message">
                        ${message}
                    </div>
                    
                    <div class="info-box">
                        <h3>Possible Reasons:</h3>
                        <ul>
                            <li>The verification link has expired (24 hours)</li>
                            <li>The link has already been used</li>
                            <li>The link is invalid or corrupted</li>
                        </ul>
                        
                        <h3 style="margin-top: 20px;">Solutions:</h3>
                        <ul>
                            <li>Request a new verification email</li>
                            <li>Contact support for assistance</li>
                            <li>Try registering again if needed</li>
                        </ul>
                    </div>
                    
                    <a href="register" class="button">Back to Registration</a>
                    <a href="login" class="button secondary">Try Login</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>