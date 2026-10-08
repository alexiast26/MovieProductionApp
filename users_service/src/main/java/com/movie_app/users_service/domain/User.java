package com.movie_app.users_service.domain;

public class User {
    protected Integer id;
    protected String username;
    protected String email;
    protected String phone;
    protected String passwordHash;
    protected UserRole role;

    public User(Integer id, String username, String email, String phone, String passwordHash, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public void authenticate() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
