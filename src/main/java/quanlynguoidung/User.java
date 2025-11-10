package quanlynguoidung;

import java.util.Date;
import java.util.UUID;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected String fullName;
    protected String email;
    protected String phone;
    protected String address;
    protected String status;

    public User() {}

    public User(String id, String username, String password,
                String fullName, String email, String phone, String address, String status) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status == null ? "active" : status;
    }

    public static Date getCurrentTimestamp() {
        return new Date();
    }

    // Abstract method để validate theo từng use case
    public abstract void validate();

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}