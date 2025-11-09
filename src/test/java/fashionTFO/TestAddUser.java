package fashionTFO;

import org.junit.jupiter.api.Test;
import quanlynguoidung.User;
import repository.jdbc.UserRepoImpl;

public class TestAddUser {

    @Test
    public void testSaveUser() {
        UserRepoImpl repo = new UserRepoImpl();

        // Tạo dữ liệu UNIQUE để tránh duplicate
        String uniqueId = java.util.UUID.randomUUID().toString().substring(0, 8);
        String username = "testuser123" + uniqueId;
        String email = "testuser_" + uniqueId + "@example.com";
        String phone = "09" + (int)(Math.random() * 1000000000);

        User user = new User();
        user.setUsername(username);
        user.setPassword("123456");
        user.setFullName("Test User");
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus("active");

        // Gọi save, nếu không throw exception thì test tự xanh
        repo.save(user);
    }
}
