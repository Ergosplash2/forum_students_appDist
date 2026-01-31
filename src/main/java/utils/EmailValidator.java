package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Email Validator using External API
 * Uses AbstractAPI's free email validation service
 */
public class EmailValidator {
    
    // FREE API - No key needed for basic validation
    // For production, get free API key from: https://www.abstractapi.com/api/email-verification-validation-api
    private static final String API_URL = "https://emailvalidation.abstractapi.com/v1/";
    private static final String API_KEY = "key-here"; // Get free key or use fallback
    
    /**
     * Validates email using external API
     * @param email Email address to validate
     * @return ValidationResult with details
     */
    public static ValidationResult validateEmail(String email) {
        
        // Basic format check first (fallback if API fails)
        if (!isValidFormat(email)) {
            return new ValidationResult(false, "Invalid email format", null);
        }
        
        try {
            // Build API URL
            String urlString = API_URL + "?api_key=" + API_KEY + 
                              "&email=" + URLEncoder.encode(email, "UTF-8");
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 second timeout
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode == 200) {
                // Read response
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                
                boolean isValid = false;
                if (jsonResponse.has("is_valid_format")) {
                    JsonObject formatObj = jsonResponse.getAsJsonObject("is_valid_format");
                    if (formatObj != null && formatObj.has("value")) {
                        isValid = formatObj.get("value").getAsBoolean();
                    }
                }

                
                boolean isDisposable = false;
                if (jsonResponse.has("is_disposable_email")) {
                    JsonObject dispObj = jsonResponse.getAsJsonObject("is_disposable_email");
                    if (dispObj != null && dispObj.has("value")) {
                        isDisposable = dispObj.get("value").getAsBoolean();
                    }
                }

                
                if (isDisposable) {
                    return new ValidationResult(false, "Disposable email addresses are not allowed", null);
                }
                
                if (!isValid) {
                    return new ValidationResult(false, "Email address is not valid", null);
                }
                
                String domain = jsonResponse.has("domain") ? 
                               jsonResponse.get("domain").getAsString() : null;
                
                return new ValidationResult(true, "Email is valid", domain);
                
            } else {
                // API call failed, use fallback validation
                System.out.println("Email API returned code: " + responseCode + ", using fallback validation");
                return fallbackValidation(email);
            }
            
        } catch (Exception e) {
            // API error, use fallback validation
            System.out.println("Email API error: " + e.getMessage() + ", using fallback validation");
            return fallbackValidation(email);
        }
    }
    
    /**
     * Fallback validation (if API fails)
     */
    private static ValidationResult fallbackValidation(String email) {
        if (isValidFormat(email)) {
            // Check for common disposable domains
            String[] disposableDomains = {
                "tempmail.com", "throwaway.email", "guerrillamail.com",
                "10minutemail.com", "mailinator.com", "trashmail.com"
            };
            
            String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
            
            for (String disposable : disposableDomains) {
                if (domain.equals(disposable)) {
                    return new ValidationResult(false, "Disposable email addresses are not allowed", domain);
                }
            }
            
            return new ValidationResult(true, "Email format is valid", domain);
        }
        
        return new ValidationResult(false, "Invalid email format", null);
    }
    
    /**
     * Basic email format validation (regex)
     */
    private static boolean isValidFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // RFC 5322 compliant regex (simplified)
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    /**
     * Validation Result class
     */
    public static class ValidationResult {
        private boolean valid;
        private String message;
        private String domain;
        
        public ValidationResult(boolean valid, String message, String domain) {
            this.valid = valid;
            this.message = message;
            this.domain = domain;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public String getDomain() { return domain; }
    }
}