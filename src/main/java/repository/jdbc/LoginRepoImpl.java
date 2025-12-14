package repository.jdbc;

import repository.DTO.NhanVienDTO;
import repository.DTO.UserDTO;
import repository.user.LoginRepoGateway;
import java.sql.*;

public class LoginRepoImpl implements LoginRepoGateway {
    
    @Override
    public NhanVienDTO findNhanVienByUsername(String username) {
        String sql = "SELECT nv.*, r.role_name " +
                     "FROM nhanvien nv " +
                     "LEFT JOIN roles r ON nv.role_id = r.role_id " +
                     "WHERE nv.username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                NhanVienDTO dto = new NhanVienDTO();
                dto.nhanvienID = rs.getString("nhanvienID");
                dto.username = rs.getString("username");
                dto.password = rs.getString("password_hash");
                dto.fullName = rs.getString("full_name");
                dto.email = rs.getString("email");
                dto.phone = rs.getString("phone");
                dto.status = rs.getString("status");
                dto.roleId = rs.getString("role_id");
                dto.roleName = rs.getString("role_name");
                return dto;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm nhân viên: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public UserDTO findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
        }
        return null;
    }
}