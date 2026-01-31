package dao;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.User;

public class UserDAO {
    
    // Login method (updated to include new fields)
    public User login(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username=?";
        
        String hashedPassword = hashPassword(password);
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(hashedPassword)) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setUserType(rs.getString("user_type"));
                    user.setUniversity(rs.getString("university"));
                    user.setCreatedAt(rs.getString("created_at"));
                    
                    // Email verification fields
                    user.setEmailVerified(rs.getBoolean("email_verified"));
                    user.setVerificationToken(rs.getString("verification_token"));
                    user.setTokenCreatedAt(rs.getString("token_created_at"));
                    
                    // Optional profile fields
                    user.setFullName(rs.getString("full_name"));
                    user.setSpecialty(rs.getString("specialty"));
                    user.setLevel(rs.getString("level"));
                    user.setBirthdate(rs.getString("birthdate"));
                    user.setStudentId(rs.getString("student_id"));
                    
                    Integer gradYear = rs.getInt("graduation_year");
                    user.setGraduationYear(rs.wasNull() ? null : gradYear);
                    
                    user.setProfilePicture(rs.getString("profile_picture"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    // Register method (updated with user_type and university)
    public boolean register(String username, String email, String password, 
                           String userType, String university, String verificationToken) {
        String sql = "INSERT INTO users(username, email, password, user_type, university, " +
                     "email_verified, verification_token, token_created_at) " +
                     "VALUES (?, ?, ?, ?, ?, FALSE, ?, NOW())";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashPassword(password));
            ps.setString(4, userType);
            ps.setString(5, university);
            ps.setString(6, verificationToken);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("SQL Error in register: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            
            if (e.getErrorCode() == 1062) {
                System.out.println("Duplicate user detected: " + username + " or " + email);
            }
            return false;
        }
    }
    
    // Get user by ID (for profile view)
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setUserType(rs.getString("user_type"));
                user.setUniversity(rs.getString("university"));
                user.setCreatedAt(rs.getString("created_at"));
                
                // Email verification fields
                user.setEmailVerified(rs.getBoolean("email_verified"));
                
                // Optional fields
                user.setFullName(rs.getString("full_name"));
                user.setSpecialty(rs.getString("specialty"));
                user.setLevel(rs.getString("level"));
                user.setBirthdate(rs.getString("birthdate"));
                user.setStudentId(rs.getString("student_id"));
                
                Integer gradYear = rs.getInt("graduation_year");
                user.setGraduationYear(rs.wasNull() ? null : gradYear);
                
                user.setProfilePicture(rs.getString("profile_picture"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    // Update user profile
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET full_name=?, specialty=?, level=?, " +
                     "birthdate=?, student_id=?, graduation_year=?, university=? " +
                     "WHERE id=?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getSpecialty());
            ps.setString(3, user.getLevel());
            ps.setString(4, user.getBirthdate());
            ps.setString(5, user.getStudentId());
            
            if (user.getGraduationYear() != null) {
                ps.setInt(6, user.getGraduationYear());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            ps.setString(7, user.getUniversity());
            ps.setInt(8, user.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update profile picture path
    public boolean updateProfilePicture(int userId, String picturePath) {
        String sql = "UPDATE users SET profile_picture=? WHERE id=?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, picturePath);
            ps.setInt(2, userId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Verify email using token
    public boolean verifyEmail(String token) {
        String sql = "UPDATE users SET email_verified = TRUE, verification_token = NULL " +
                     "WHERE verification_token = ? AND " +
                     "TIMESTAMPDIFF(HOUR, token_created_at, NOW()) < 24";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, token);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if email is already verified
    public boolean isEmailVerified(String email) {
        String sql = "SELECT email_verified FROM users WHERE email = ?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("email_verified");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Resend verification email (generate new token)
    public String regenerateVerificationToken(String email) {
        String newToken = java.util.UUID.randomUUID().toString();
        String sql = "UPDATE users SET verification_token = ?, token_created_at = NOW() " +
                     "WHERE email = ? AND email_verified = FALSE";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, newToken);
            ps.setString(2, email);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return newToken;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Hash password
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
}