package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.aot.generate.ValueCodeGenerator.Delegate;

import quanlynguoidung.User;
import repository.AddUserRepoGateway;
import repository.DeleteUserRepositoryGateway;
import repository.GetUserRepositoryGateway;
import repository.ListUsersRepositoryGateway;
import repository.UpdateUserRepositoryGateway;
import repository.DTO.UserDTO;


public class UserRepoImpl implements AddUserRepoGateway, ListUsersRepositoryGateway, GetUserRepositoryGateway, UpdateUserRepositoryGateway, DeleteUserRepositoryGateway{

	public void save(UserDTO dto) {
	    String sql = """
	        INSERT INTO users (user_id, username, password_hash, full_name, email, phone, address, status)
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.id == null ? UUID.randomUUID().toString() : dto.id);
            ps.setString(2, dto.username);
            ps.setString(3, dto.password); // mật khẩu đã hash nếu cần
            ps.setString(4, dto.fullName);
            ps.setString(5, dto.email);
            ps.setString(6, dto.phone);
            ps.setString(7, dto.address);
            ps.setString(8, dto.status);
	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            System.out.println("✅ User saved successfully!");
	        } else {
	            System.out.println("⚠️ User not saved!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	 @Override
	    public boolean existsByUsername(String username) {
	        String sql = "SELECT 1 FROM users WHERE username = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setString(1, username);
	            ResultSet rs = ps.executeQuery();
	            return rs.next();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

    @Override
    public boolean existsByPhone(String phone) {
        String sql = "SELECT 1 FROM users WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY full_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                UserDTO dto = mapResultSetToDTO(rs);
                users.add(dto);
            }
            
            System.out.println("✅ Loaded " + users.size() + " users");
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách user: " + e.getMessage());
        }
        
        return users;
    }
    
    // ============================================
    // IMPLEMENT GetUserRepository
    // ============================================
    @Override
    public Optional<UserDTO> findById(String id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                UserDTO dto = mapResultSetToDTO(rs);
                System.out.println("✅ User found: " + dto.username);
                return Optional.of(dto);
            } else {
                System.out.println("⚠️ User not found with id: " + id);
                return Optional.empty();
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm user: " + e.getMessage());
        }
    }
    
    @Override
    public Optional<UserDTO> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                UserDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm user theo username: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<UserDTO> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                UserDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm user theo email: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<UserDTO> findByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                UserDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm user theo phone: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    // ============================================
    // IMPLEMENT UpdateUserRepository
    // ============================================
    @Override
    public void update(UserDTO dto) {
        String sql = """
            UPDATE users 
            SET username = ?, password_hash = ?, full_name = ?, 
                email = ?, phone = ?, address = ?, status = ?
            WHERE user_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dto.username);
            ps.setString(2, dto.password);
            ps.setString(3, dto.fullName);
            ps.setString(4, dto.email);
            ps.setString(5, dto.phone);
            ps.setString(6, dto.address);
            ps.setString(7, dto.status);
            ps.setString(8, dto.id);
            
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✅ User updated successfully: " + dto.username);
            } else {
                System.out.println("⚠️ User not found with id: " + dto.id);
                throw new RuntimeException("USER_NOT_FOUND");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật user: " + e.getMessage());
        }
    }
    
    // ============================================
    // IMPLEMENT DeleteUserRepository
    // ============================================
    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✅ User deleted successfully with id: " + id);
            } else {
                System.out.println("⚠️ User not found with id: " + id);
                throw new RuntimeException("USER_NOT_FOUND");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa user: " + e.getMessage());
        }
    }
    private UserDTO mapResultSetToDTO(ResultSet rs) throws Exception {
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
