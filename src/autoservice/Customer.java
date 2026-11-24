package autoservice;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final SecureRandom RANDOM = new SecureRandom();
    private int id;
    private String name, phone, email;
    private int completedServices;
    private String passwordSalt = "";
    private String passwordHash = "";

    public Customer(int id, String name, String phone, String email) {
        this(id, name, phone, email, 0);
    }

    public Customer(int id, String name, String phone, String email, int completedServices) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.completedServices = completedServices;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getCompletedServices() { return completedServices; }
    public boolean hasPassword() {
        return passwordSalt != null && !passwordSalt.isEmpty()
                && passwordHash != null && !passwordHash.isEmpty();
    }
    public boolean verifyPassword(char[] password) {
        return hasPassword() && PasswordHasher.verify(password, passwordSalt, passwordHash);
    }

    public void setPassword(char[] password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        passwordSalt = Base64.getEncoder().encodeToString(salt);
        passwordHash = PasswordHasher.hash(password, salt);
    }

    public void restorePassword(String salt, String hash) {
        passwordSalt = salt == null ? "" : salt;
        passwordHash = hash == null ? "" : hash;
    }

    public String getPasswordSalt() { return passwordSalt == null ? "" : passwordSalt; }
    public String getPasswordHash() { return passwordHash == null ? "" : passwordHash; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void addCompletedService() { completedServices++; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Phone: %s | Email: %s | Services: %d",
                id, name, phone, email, completedServices);
    }
}
