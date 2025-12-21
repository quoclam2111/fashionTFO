package repository.jdbc;


import repository.DTO.UserDTO;
import repository.user.DeleteUserRepositoryGateway;
import repository.user.GetUserRepositoryGateway;
import repository.user.ListUsersRepositoryGateway;
import repository.user.RegisterRepoGateway;
import repository.user.UpdateUserRepositoryGateway;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation cho tất cả các Repository Gateway
 */
public class UserRepoImpl implements RegisterRepoGateway, GetUserRepositoryGateway, 
                                     UpdateUserRepositoryGateway, DeleteUserRepositoryGateway, ListUsersRepositoryGateway {

    // ==================== ADD USER ====================
    @Override
    public void save(UserDTO dto) {
        String sql = "INSERT INTO users (user_id, username, password_hash, full_name, email, phone, address, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dto.id);
            stmt.setString(2, dto.username);
            stmt.setString(3, dto.password);
            stmt.setString(4, dto.fullName);
            stmt.setString(5, dto.email);
            stmt.setString(6, dto.phone);
            stmt.setString(7, dto.address);
            stmt.setString(8, dto.status);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lưu user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        return existsByQuery(sql, email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
        return existsByQuery(sql, phone);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        return existsByQuery(sql, username);
    }

    private boolean existsByQuery(String sql, String value) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra tồn tại: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return false;
    }

    // ==================== GET USER ====================
    @Override
    public Optional<UserDTO> findById(String id) {
        return findUserByQuery("SELECT * FROM users WHERE user_id = ?", id);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return findUserByQuery("SELECT * FROM users WHERE username = ?", username);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return findUserByQuery("SELECT * FROM users WHERE email = ?", email);
    }

    @Override
    public Optional<UserDTO> findByPhone(String phone) {
        return findUserByQuery("SELECT * FROM users WHERE phone = ?", phone);
    }

    private Optional<UserDTO> findUserByQuery(String sql, String value) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ==================== UPDATE USER ====================
    @Override
    public void update(UserDTO user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, " +
                     "address = ?, status = ?, password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.fullName);
            stmt.setString(2, user.email);
            stmt.setString(3, user.phone);
            stmt.setString(4, user.address);
            stmt.setString(5, user.status);
            stmt.setString(6, user.password);
            stmt.setString(7, user.id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Không tìm thấy user để cập nhật!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByEmailExcludingUser(String email, String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND user_id != ?";
        return existsByQueryExcludingUser(sql, email, userId);
    }

    @Override
    public boolean existsByPhoneExcludingUser(String phone, String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ? AND user_id != ?";
        return existsByQueryExcludingUser(sql, phone, userId);
    }

    private boolean existsByQueryExcludingUser(String sql, String value, String userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra tồn tại: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return false;
    }

    // ==================== DELETE USER ====================
    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Không tìm thấy user để xóa!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
    }

    // ==================== LIST USERS ====================
    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return list;
    }
    
 // ============================================================
 // 📧 OTP VERIFICATION METHODS
 // ============================================================

 @Override
 public void saveOTP(String userId, String otpCode) {
     String sql = "INSERT INTO email_verification (verification_id, user_id, otp_code, expires_at) " +
                  "VALUES (UUID(), ?, ?, DATE_ADD(NOW(), INTERVAL 10 MINUTE))";
     
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         
         ps. setString(1, userId);
         ps.setString(2, otpCode);
         ps.executeUpdate();
         
         System.out.println("✅ Đã lưu OTP cho user: " + userId + " - OTP: " + otpCode);
         
     } catch (SQLException e) {
         System.err.println("❌ Lỗi khi lưu OTP: " + e.getMessage());
         throw new RuntimeException("Error saving OTP", e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB:  " + e.getMessage(), e);
     }
 }

 @Override
 public boolean verifyOTP(String userId, String otpCode) {
     String sql = "SELECT COUNT(*) FROM email_verification " +
                  "WHERE user_id = ? AND otp_code = ?  AND verified_at IS NULL AND expires_at > NOW()";
     
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         
         ps.setString(1, userId);
         ps.setString(2, otpCode);
         ResultSet rs = ps.executeQuery();
         
         if (rs. next() && rs.getInt(1) > 0) {
             System.out.println("✅ OTP hợp lệ cho user: " + userId);
             return true;
         }
         
         System.out.println("⚠️ OTP không hợp lệ hoặc đã hết hạn");
         return false;
         
     } catch (SQLException e) {
         System.err.println("❌ Lỗi khi verify OTP: " + e.getMessage());
         throw new RuntimeException("Error verifying OTP", e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
     }
 }

 @Override
 public void markEmailAsVerified(String userId) {
     String sql1 = "UPDATE users SET status = 'active' WHERE user_id = ?";
     String sql2 = "UPDATE email_verification SET verified_at = NOW() WHERE user_id = ?  AND verified_at IS NULL";
     
     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);
         
         try (PreparedStatement ps1 = conn.prepareStatement(sql1);
              PreparedStatement ps2 = conn.prepareStatement(sql2)) {
             
             ps1.setString(1, userId);
             ps1.executeUpdate();
             
             ps2.setString(1, userId);
             ps2.executeUpdate();
             
             conn.commit();
             System.out.println("✅ Đã xác thực email cho user: " + userId);
             
         } catch (SQLException e) {
             conn.rollback();
             throw e;
         }
         
     } catch (SQLException e) {
         System.err.println("❌ Lỗi khi xác thực email: " + e.getMessage());
         throw new RuntimeException("Error marking email as verified", e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB: " + e. getMessage(), e);
     }
 }

 @Override
 public int getOTPAttempts(String userId) {
     String sql = "SELECT attempts FROM email_verification WHERE user_id = ?  AND verified_at IS NULL ORDER BY created_at DESC LIMIT 1";
     
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         
         ps.setString(1, userId);
         ResultSet rs = ps.executeQuery();
         
         if (rs.next()) {
             return rs.getInt("attempts");
         }
         return 0;
         
     } catch (SQLException e) {
         throw new RuntimeException("Error getting OTP attempts", e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
     }
 }

 @Override
 public void incrementOTPAttempts(String userId) {
     String sql = "UPDATE email_verification SET attempts = attempts + 1 WHERE user_id = ? AND verified_at IS NULL";
     
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         
         ps. setString(1, userId);
         ps.executeUpdate();
         
     } catch (SQLException e) {
         throw new RuntimeException("Error incrementing OTP attempts", e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
     }
 }

    // ==================== HELPER ====================
    private UserDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        UserDTO dto = new UserDTO();
        dto.id = rs.getString("user_id");
        dto.username = rs.getString("username");
        dto.password = rs.getString("password_hash");
        dto.fullName = rs.getString("full_name");
        dto.email = rs.getString("email");
        dto.phone = rs.getString("phone");
        dto.address = rs.getString("address");
        dto.status = rs.getString("status");
        return dto;
    }
}
