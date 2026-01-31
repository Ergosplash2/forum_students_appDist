package model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;           // USER or ADMIN
    private String userType;       // STUDENT or PROFESSOR
    private String university;     // Required
    private String createdAt;
    
    // Email verification fields
    private boolean emailVerified;
    private String verificationToken;
    private String tokenCreatedAt;
    
    // Optional profile fields
    private String fullName;
    private String specialty;
    private String level;
    private String birthdate;      // DATE type
    private String studentId;      // Can be alphanumeric
    private Integer graduationYear; // INT type
    private String profilePicture;

    // Constructors
    public User() {}

    // Basic constructor (for login)
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Full constructor with all required fields
    public User(int id, String username, String email, String password, 
                String role, String userType, String university, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userType = userType;
        this.university = university;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Email verification getters/setters
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public String getTokenCreatedAt() { return tokenCreatedAt; }
    public void setTokenCreatedAt(String tokenCreatedAt) { this.tokenCreatedAt = tokenCreatedAt; }

    // Optional profile fields
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    // Utility methods
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(this.userType);
    }

    public boolean isProfessor() {
        return "PROFESSOR".equalsIgnoreCase(this.userType);
    }
    
    public String getUserTypeBadgeClass() {
        if (isStudent()) return "badge-student";
        if (isProfessor()) return "badge-professor";
        return "badge-default";
    }
    
    public String getUserTypeDisplay() {
        if (isStudent()) return "Student";
        if (isProfessor()) return "Professor";
        return "User";
    }
}