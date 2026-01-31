package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * Email Sender - Sends verification emails using Ethereal.email (testing)
 */
public class EmailSender {
    
    // Ethereal.email SMTP Configuration
    private static final String SMTP_HOST = "smtp.ethereal.email";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "katrina.abernathy45@ethereal.email"; // Your Ethereal username
    private static final String SMTP_PASSWORD = "password-here"; // Your Ethereal password
    
    // From email address (can be anything with Ethereal)
    private static final String FROM_EMAIL = "noreply@studentforum.com";
    
    // Your application URL
    private static final String APP_URL = "http://localhost:9090/SIR";
    
    /**
     * Send verification email to user
     * @param toEmail Recipient email
     * @param username User's username
     * @param token Verification token
     * @return true if sent successfully
     */
    public static boolean sendVerificationEmail(String toEmail, String username, String token) {
        
        try {
            // Setup mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            
            // Create session with Ethereal authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // USE ETHEREAL CREDENTIALS, NOT GMAIL!
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Verify Your Email - Student Forum");
            
            // Build verification URL
            String verificationUrl = APP_URL + "/verifyEmail?token=" + token;
            
            // Create HTML email content
            String htmlContent = buildEmailHTML(username, verificationUrl);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            // Send email
            Transport.send(message);
            
            System.out.println("✅ Verification email sent to: " + toEmail);
            System.out.println("📧 Ethereal Inbox: https://ethereal.email/login");
            System.out.println("👤 Username: " + SMTP_USERNAME);
            System.out.println("🔑 Password: " + SMTP_PASSWORD);
            
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send verification email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Build HTML email content
     */
    private static String buildEmailHTML(String username, String verificationUrl) {
        System.out.println("BUILD EMAIL HTML VERSION 2026-01-06");

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; }
                    .header h1 { margin: 0; font-size: 2em; }
                    .content { padding: 40px; }
                    .content p { color: #555; line-height: 1.6; margin-bottom: 20px; }
                    .button { display: inline-block; background: #4CAF50; color: white; padding: 15px 40px; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 20px 0; }
                    .button:hover { background: #45a049; }
                    .footer { background: #f8f9fa; padding: 20px; text-align: center; color: #777; font-size: 0.9em; }
                    .warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; color: #856404; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>📚 Student Forum</h1>
                        <p>Email Verification Required</p>
                    </div>
                    
                    <div class="content">
                        <h2>Welcome, %s! 🎉</h2>
                        
                        <p>Thank you for registering with Student Forum. To complete your registration and start using the forum, please verify your email address.</p>
                        
                        <p style="text-align: center;">
                            <a href="%s" class="button">Verify My Email</a>
                        </p>
                        
                        <p>Or copy and paste this link into your browser:</p>
                        <p style="word-break: break-all; background: #f8f9fa; padding: 10px; border-radius: 4px; font-family: monospace; font-size: 0.9em;">%s</p>
                        
                        <div class="warning">
                            <strong>⏱️ Important:</strong> This verification link will expire in 24 hours. If you didn't create this account, please ignore this email.
                        </div>
                        
                        <p>After verification, you'll be able to:</p>
                        <ul>
                            <li>📝 Create and reply to posts</li>
                            <li>💬 Comment on discussions</li>
                        </ul>
                        
                        <p>If you have any questions, feel free to contact our support team.</p>
                        
                        <p style="color: #999; font-size: 0.9em; margin-top: 30px;">
                            Best regards,<br>
                            <strong>Student Forum Team</strong>
                        </p>
                    </div>
                    
                    <div class="footer">
                        <p>This is an automated email. Please do not reply to this message.</p>
                        <p>&copy; 2025 Student Forum. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, verificationUrl, verificationUrl);
    }
    
    /**
     * Generate random verification token
     */
    public static String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}