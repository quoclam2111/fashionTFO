//package fashionTFO;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.AfterEach;
//import java.util.ArrayList;
//import java.util.List;
//
//import adapters.add.*;
//import adapters.get.*;
//import adapters.update.*;
//import adapters.delete.*;
//import adapters.list.*;
//import quanlynguoidung.them.AddUserUseCase;
//import quanlynguoidung.get.GetUserUseCase;
//import quanlynguoidung.update.UpdateUserUseCase;
//import quanlynguoidung.delete.DeleteUserUseCase;
//import quanlynguoidung.list.ListUsersUseCase;
//import repository.jdbc.UserRepoImpl;
//
//public class TestAddUser {
//    
//    private UserRepoImpl repository;
//    private List<String> createdUserIds = new ArrayList<>();
//    
//    @BeforeEach
//    public void setUp() {
//        repository = new UserRepoImpl();
//        createdUserIds.clear();
//    }
//    
//    @AfterEach
//    public void tearDown() {
//        // Cleanup: Xóa tất cả users được tạo trong test
//        for (String userId : createdUserIds) {
//            try {
//                repository.deleteById(userId);
//            } catch (Exception e) {
//                // Ignore nếu user đã bị xóa
//            }
//        }
//    }
//    
//    // ========== TEST ADD USER ==========
//    
//    @Test
//    public void testAddUser_ValidInput_Success() {
//        // Arrange - Dùng timestamp để tạo unique values
//        long timestamp = System.currentTimeMillis();
//        AddUserInputDTO inputDTO = new AddUserInputDTO();
//        inputDTO.username = "testuser_" + timestamp;
//        inputDTO.password = "password123";
//        inputDTO.fullName = "Test User";
//        inputDTO.email = "test_" + timestamp + "@example.com";
//        inputDTO.phone = String.format("09%08d", timestamp % 100000000); // 10 chữ số
//        inputDTO.address = "123 Test Street";
//        
//        AddUserViewModel viewModel = new AddUserViewModel();
//        AddUserPresenter presenter = new AddUserPresenter(viewModel);
//        AddUserUseCase useCase = new AddUserUseCase(repository, presenter);
//        AddUserController controller = new AddUserController(useCase);
//        
//        // Act
//        controller.execute(inputDTO);
//        
//        // Track để cleanup
//        if (viewModel.userId != null) {
//            createdUserIds.add(viewModel.userId);
//        }
//        
//        // Assert với thông tin debug
//        assertTrue(viewModel.success, "Add user should succeed. Error: " + viewModel.message);
//        assertNotNull(viewModel.userId, "User ID should not be null");
//        assertEquals("Thêm người dùng thành công!", viewModel.message);
//        assertNotNull(viewModel.timestamp);
//    }
//    
//    @Test
//    public void testAddUser_EmptyUsername_Failed() {
//        // Arrange
//        AddUserInputDTO inputDTO = new AddUserInputDTO();
//        inputDTO.username = ""; // Empty username
//        inputDTO.password = "password123";
//        inputDTO.fullName = "Test User";
//        inputDTO.email = "test@example.com";
//        inputDTO.phone = "0123456789";
//        
//        AddUserViewModel viewModel = new AddUserViewModel();
//        AddUserPresenter presenter = new AddUserPresenter(viewModel);
//        AddUserUseCase useCase = new AddUserUseCase(repository, presenter);
//        AddUserController controller = new AddUserController(useCase);
//        
//        // Act
//        controller.execute(inputDTO);
//        
//        // Assert
//        assertFalse(viewModel.success, "Add user should fail with empty username");
//        assertNotNull(viewModel.message);
//    }
//    
//    @Test
//    public void testAddUser_DuplicateEmail_Failed() {
//        // Arrange - Tạo user đầu tiên
//        AddUserInputDTO inputDTO1 = new AddUserInputDTO();
//        inputDTO1.username = "user_first";
//        inputDTO1.password = "password123";
//        inputDTO1.fullName = "First User";
//        inputDTO1.email = "duplicate@example.com"; // Email trùng
//        inputDTO1.phone = "0111111111";
//        
//        AddUserViewModel viewModel1 = new AddUserViewModel();
//        AddUserPresenter presenter1 = new AddUserPresenter(viewModel1);
//        AddUserUseCase useCase1 = new AddUserUseCase(repository, presenter1);
//        AddUserController controller1 = new AddUserController(useCase1);
//        controller1.execute(inputDTO1);
//        
//        if (viewModel1.userId != null) {
//            createdUserIds.add(viewModel1.userId);
//        }
//        
//        // Arrange - Tạo user thứ hai với CÙNG EMAIL
//        AddUserInputDTO inputDTO2 = new AddUserInputDTO();
//        inputDTO2.username = "user_second"; // Username khác
//        inputDTO2.password = "password456";
//        inputDTO2.fullName = "Second User";
//        inputDTO2.email = "duplicate@example.com"; // Email trùng
//        inputDTO2.phone = "0222222222";
//        
//        AddUserViewModel viewModel2 = new AddUserViewModel();
//        AddUserPresenter presenter2 = new AddUserPresenter(viewModel2);
//        AddUserUseCase useCase2 = new AddUserUseCase(repository, presenter2);
//        AddUserController controller2 = new AddUserController(useCase2);
//        
//        // Act
//        controller2.execute(inputDTO2);
//        
//        // Assert
//        assertFalse(viewModel2.success, "Add user should fail with duplicate email");
//        assertTrue(viewModel2.message.contains("Email") || 
//                   viewModel2.message.contains("đã được sử dụng"));
//    }
//    
//    // ========== TEST GET USER ==========
//    
//    @Test
//    public void testGetUser_ValidId_Success() {
//        // Arrange - Tạo user trước
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "getuser_test";
//        addDTO.password = "password123";
//        addDTO.fullName = "Get User Test";
//        addDTO.email = "getuser@example.com";
//        addDTO.phone = "0333333333";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        // Debug: Check if add succeeded
//        if (!addViewModel.success) {
//            System.out.println("❌ Failed to add user: " + addViewModel.message);
//        }
//        assertTrue(addViewModel.success, "Add user should succeed: " + addViewModel.message);
//        
//        String userId = addViewModel.userId;
//        createdUserIds.add(userId);
//        
//        // Arrange - Get user
//        GetUserInputDTO getDTO = new GetUserInputDTO();
//        getDTO.searchBy = "id";
//        getDTO.searchValue = userId;
//        
//        GetUserViewModel getViewModel = new GetUserViewModel();
//        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
//        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
//        GetUserController getController = new GetUserController(getUseCase);
//        
//        // Act
//        getController.execute(getDTO);
//        
//        // Assert
//        assertTrue(getViewModel.success, "Get user should succeed");
//        assertNotNull(getViewModel.user);
//        assertEquals(userId, getViewModel.user.id);
//        assertEquals("getuser_test", getViewModel.user.username);
//        assertEquals("Get User Test", getViewModel.user.fullName);
//    }
//    
//    @Test
//    public void testGetUser_InvalidId_Failed() {
//        // Arrange
//        GetUserInputDTO getDTO = new GetUserInputDTO();
//        getDTO.searchBy = "id";
//        getDTO.searchValue = "INVALID_USER_ID_999";
//        
//        GetUserViewModel getViewModel = new GetUserViewModel();
//        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
//        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
//        GetUserController getController = new GetUserController(getUseCase);
//        
//        // Act
//        getController.execute(getDTO);
//        
//        // Assert
//        assertFalse(getViewModel.success, "Get user should fail with invalid ID");
//        assertNull(getViewModel.user);
//        assertTrue(getViewModel.message.contains("không tìm thấy") || 
//                   getViewModel.message.contains("Không tìm thấy"));
//    }
//    
//    // ========== TEST UPDATE USER ==========
//    
//    @Test
//    public void testUpdateUser_ValidInput_Success() {
//        // Arrange - Tạo user trước
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "updateuser_test";
//        addDTO.password = "password123";
//        addDTO.fullName = "Original Name";
//        addDTO.email = "original@example.com";
//        addDTO.phone = "0444444444";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        String userId = addViewModel.userId;
//        createdUserIds.add(userId);
//        
//        // Arrange - Update user
//        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
//        updateDTO.userId = userId;
//        updateDTO.fullName = "Updated Name";
//        updateDTO.email = "updated@example.com";
//        updateDTO.phone = "0987654321";
//        
//        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
//        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
//        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
//        UpdateUserController updateController = new UpdateUserController(updateUseCase);
//        
//        // Act
//        updateController.execute(updateDTO);
//        
//        // Assert
//        assertTrue(updateViewModel.success, "Update user should succeed");
//        assertNotNull(updateViewModel.updatedUser);
//        assertEquals("Updated Name", updateViewModel.updatedUser.fullName);
//        assertEquals("updated@example.com", updateViewModel.updatedUser.email);
//        assertEquals("0987654321", updateViewModel.updatedUser.phone);
//    }
//    
//    @Test
//    public void testUpdateUser_InvalidUserId_Failed() {
//        // Arrange
//        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
//        updateDTO.userId = "INVALID_USER_ID_999";
//        updateDTO.fullName = "Updated Name";
//        
//        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
//        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
//        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
//        UpdateUserController updateController = new UpdateUserController(updateUseCase);
//        
//        // Act
//        updateController.execute(updateDTO);
//        
//        // Assert
//        assertFalse(updateViewModel.success, "Update should fail with invalid user ID");
//        assertTrue(updateViewModel.message.contains("không tìm thấy") || 
//                   updateViewModel.message.contains("Không tìm thấy"));
//    }
//    
//    // ========== TEST DELETE USER ==========
//    
//    @Test
//    public void testDeleteUser_ValidId_Success() {
//        // Arrange - Tạo user trước
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "deleteuser_test";
//        addDTO.password = "password123";
//        addDTO.fullName = "Delete User Test";
//        addDTO.email = "delete@example.com";
//        addDTO.phone = "0555555555";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        String userId = addViewModel.userId;
//        // Không add vào createdUserIds vì sẽ tự xóa trong test
//        
//        // Arrange - Delete user
//        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
//        deleteDTO.userId = userId;
//        
//        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
//        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
//        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
//        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
//        
//        // Act
//        deleteController.execute(deleteDTO);
//        
//        // Assert
//        assertTrue(deleteViewModel.success, "Delete user should succeed");
//        assertEquals(userId, deleteViewModel.deletedUserId);
//        assertEquals("deleteuser_test", deleteViewModel.deletedUsername);
//        
//        // Verify user không còn tồn tại
//        GetUserInputDTO getDTO = new GetUserInputDTO();
//        getDTO.searchBy = "id";
//        getDTO.searchValue = userId;
//        
//        GetUserViewModel getViewModel = new GetUserViewModel();
//        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
//        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
//        GetUserController getController = new GetUserController(getUseCase);
//        getController.execute(getDTO);
//        
//        assertFalse(getViewModel.success, "User should not exist after deletion");
//    }
//    
//    @Test
//    public void testDeleteUser_InvalidId_Failed() {
//        // Arrange
//        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
//        deleteDTO.userId = "INVALID_USER_ID_999";
//        
//        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
//        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
//        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
//        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
//        
//        // Act
//        deleteController.execute(deleteDTO);
//        
//        // Assert
//        assertFalse(deleteViewModel.success, "Delete should fail with invalid user ID");
//    }
//    
//    // ========== TEST LIST USERS ==========
//    
//    @Test
//    public void testListUsers_AllStatus_Success() {
//        // Arrange - Tạo ít nhất 1 user để test
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "listuser_test";
//        addDTO.password = "password123";
//        addDTO.fullName = "List User Test";
//        addDTO.email = "listuser@example.com";
//        addDTO.phone = "0666666666";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        createdUserIds.add(addViewModel.userId);
//        
//        // Arrange - List users
//        ListUsersInputDTO listDTO = new ListUsersInputDTO();
//        listDTO.statusFilter = "all";
//        listDTO.sortBy = "fullName";
//        listDTO.ascending = true;
//        
//        ListUsersViewModel listViewModel = new ListUsersViewModel();
//        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
//        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
//        ListUsersController listController = new ListUsersController(listUseCase);
//        
//        // Act
//        listController.execute(listDTO);
//        
//        // Assert
//        assertTrue(listViewModel.success, "List users should succeed");
//        assertNotNull(listViewModel.users);
//        assertTrue(listViewModel.totalCount >= 1, "Should have at least 1 user");
//    }
//    
//    @Test
//    public void testListUsers_ActiveOnly_Success() {
//        // Arrange - Tạo user active
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "activeuser_test";
//        addDTO.password = "password123";
//        addDTO.fullName = "Active User Test";
//        addDTO.email = "active@example.com";
//        addDTO.phone = "0777777777";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        createdUserIds.add(addViewModel.userId);
//        
//        // Arrange - List active users
//        ListUsersInputDTO listDTO = new ListUsersInputDTO();
//        listDTO.statusFilter = "active";
//        listDTO.sortBy = "username";
//        listDTO.ascending = true;
//        
//        ListUsersViewModel listViewModel = new ListUsersViewModel();
//        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
//        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
//        ListUsersController listController = new ListUsersController(listUseCase);
//        
//        // Act
//        listController.execute(listDTO);
//        
//        // Assert
//        assertTrue(listViewModel.success, "List users should succeed");
//        assertNotNull(listViewModel.users);
//        
//        // Verify tất cả user trong list đều có status = "active"
//        for (var user : listViewModel.users) {
//            assertEquals("active", user.status, "All users should have active status");
//        }
//    }
//    
//    @Test
//    public void testListUsers_SortByEmail_Descending() {
//        // Arrange - Tạo 2 users để test sorting
//        AddUserInputDTO addDTO1 = new AddUserInputDTO();
//        addDTO1.username = "user_a";
//        addDTO1.password = "password123";
//        addDTO1.fullName = "User A";
//        addDTO1.email = "a@example.com";
//        addDTO1.phone = "0888888888";
//        
//        AddUserViewModel addViewModel1 = new AddUserViewModel();
//        AddUserPresenter addPresenter1 = new AddUserPresenter(addViewModel1);
//        AddUserUseCase addUseCase1 = new AddUserUseCase(repository, addPresenter1);
//        AddUserController addController1 = new AddUserController(addUseCase1);
//        addController1.execute(addDTO1);
//        createdUserIds.add(addViewModel1.userId);
//        
//        AddUserInputDTO addDTO2 = new AddUserInputDTO();
//        addDTO2.username = "user_z";
//        addDTO2.password = "password123";
//        addDTO2.fullName = "User Z";
//        addDTO2.email = "z@example.com";
//        addDTO2.phone = "0999999999";
//        
//        AddUserViewModel addViewModel2 = new AddUserViewModel();
//        AddUserPresenter addPresenter2 = new AddUserPresenter(addViewModel2);
//        AddUserUseCase addUseCase2 = new AddUserUseCase(repository, addPresenter2);
//        AddUserController addController2 = new AddUserController(addUseCase2);
//        addController2.execute(addDTO2);
//        createdUserIds.add(addViewModel2.userId);
//        
//        // Arrange - List with sorting
//        ListUsersInputDTO listDTO = new ListUsersInputDTO();
//        listDTO.statusFilter = "all";
//        listDTO.sortBy = "email";
//        listDTO.ascending = false; // Descending
//        
//        ListUsersViewModel listViewModel = new ListUsersViewModel();
//        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
//        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
//        ListUsersController listController = new ListUsersController(listUseCase);
//        
//        // Act
//        listController.execute(listDTO);
//        
//        // Assert
//        assertTrue(listViewModel.success, "List users should succeed");
//        assertNotNull(listViewModel.users);
//        
//        // Verify thứ tự giảm dần theo email
//        if (listViewModel.users.size() > 1) {
//            for (int i = 0; i < listViewModel.users.size() - 1; i++) {
//                String email1 = listViewModel.users.get(i).email;
//                String email2 = listViewModel.users.get(i + 1).email;
//                assertTrue(email1.compareTo(email2) >= 0, 
//                          "Emails should be in descending order");
//            }
//        }
//    }
//    
//    @Test
//    public void testListUsers_InactiveFilter() {
//        // Arrange - Tạo user và set inactive
//        long timestamp = System.currentTimeMillis();
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "inactive_user_" + timestamp;
//        addDTO.password = "password123";
//        addDTO.fullName = "Inactive User";
//        addDTO.email = "inactive_" + timestamp + "@example.com";
//        addDTO.phone = String.format("09%08d", timestamp % 100000000); // 10 chữ số
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        String userId = addViewModel.userId;
//        createdUserIds.add(userId);
//        
//        // Update status thành inactive
//        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
//        updateDTO.userId = userId;
//        updateDTO.status = "inactive";
//        
//        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
//        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
//        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
//        UpdateUserController updateController = new UpdateUserController(updateUseCase);
//        updateController.execute(updateDTO);
//        
//        // Debug: Check if update succeeded
//        if (!updateViewModel.success) {
//            System.out.println("❌ Failed to update user to inactive: " + updateViewModel.message);
//        }
//        assertTrue(updateViewModel.success, "Update to inactive should succeed: " + updateViewModel.message);
//        
//        // Arrange - List inactive users
//        ListUsersInputDTO listDTO = new ListUsersInputDTO();
//        listDTO.statusFilter = "inactive";
//        listDTO.sortBy = "fullName";
//        listDTO.ascending = true;
//        
//        ListUsersViewModel listViewModel = new ListUsersViewModel();
//        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
//        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
//        ListUsersController listController = new ListUsersController(listUseCase);
//        
//        // Act
//        listController.execute(listDTO);
//        
//        // Debug: Show what we got
//        System.out.println("📊 List result:");
//        System.out.println("   Success: " + listViewModel.success);
//        System.out.println("   Message: " + listViewModel.message);
//        System.out.println("   Total count: " + listViewModel.totalCount);
//        System.out.println("   Filtered count: " + listViewModel.filteredCount);
//        System.out.println("   Users in result: " + listViewModel.users.size());
//        
//        // Assert
//        assertTrue(listViewModel.success, "List should succeed: " + listViewModel.message);
//        assertNotNull(listViewModel.users, "Users list should not be null");
//        assertTrue(listViewModel.filteredCount >= 1, "Should have at least 1 inactive user");
//        
//        // Verify user vừa tạo có trong list
//        boolean foundOurUser = false;
//        for (var user : listViewModel.users) {
//            if (user.id.equals(userId)) {
//                foundOurUser = true;
//                assertEquals("inactive", user.status, "Our user should be inactive");
//            }
//            // Tất cả users trong filtered result phải là inactive
//            assertEquals("inactive", user.status, 
//                "All users in filtered result should be inactive, but found: " + user.status);
//        }
//        
//        assertTrue(foundOurUser, "Our inactive user should be in the filtered list");
//    }
//    
//    // ========== TEST INTEGRATION ==========
//    
//    @Test
//    public void testFullCRUDCycle() {
//        // 1. CREATE
//        AddUserInputDTO addDTO = new AddUserInputDTO();
//        addDTO.username = "crud_test_user";
//        addDTO.password = "password123";
//        addDTO.fullName = "CRUD Test User";
//        addDTO.email = "crud@example.com";
//        addDTO.phone = "0200000000";
//        
//        AddUserViewModel addViewModel = new AddUserViewModel();
//        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
//        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
//        AddUserController addController = new AddUserController(addUseCase);
//        addController.execute(addDTO);
//        
//        assertTrue(addViewModel.success, "Step 1: Create user should succeed");
//        String userId = addViewModel.userId;
//        
//        // 2. READ
//        GetUserInputDTO getDTO = new GetUserInputDTO();
//        getDTO.searchBy = "id";
//        getDTO.searchValue = userId;
//        
//        GetUserViewModel getViewModel = new GetUserViewModel();
//        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
//        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
//        GetUserController getController = new GetUserController(getUseCase);
//        getController.execute(getDTO);
//        
//        assertTrue(getViewModel.success, "Step 2: Read user should succeed");
//        assertEquals("CRUD Test User", getViewModel.user.fullName);
//        
//        // 3. UPDATE
//        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
//        updateDTO.userId = userId;
//        updateDTO.fullName = "Updated CRUD User";
//        
//        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
//        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
//        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
//        UpdateUserController updateController = new UpdateUserController(updateUseCase);
//        updateController.execute(updateDTO);
//        
//        assertTrue(updateViewModel.success, "Step 3: Update user should succeed");
//        assertEquals("Updated CRUD User", updateViewModel.updatedUser.fullName);
//        
//        // 4. DELETE
//        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
//        deleteDTO.userId = userId;
//        
//        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
//        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
//        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
//        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
//        deleteController.execute(deleteDTO);
//        
//        assertTrue(deleteViewModel.success, "Step 4: Delete user should succeed");
//        
//        // 5. VERIFY DELETION
//        GetUserViewModel verifyViewModel = new GetUserViewModel();
//        GetUserPresenter verifyPresenter = new GetUserPresenter(verifyViewModel);
//        GetUserUseCase verifyUseCase = new GetUserUseCase(repository, verifyPresenter);
//        GetUserController verifyController = new GetUserController(verifyUseCase);
//        verifyController.execute(getDTO);
//        
//        assertFalse(verifyViewModel.success, "Step 5: User should not exist after deletion");
//    }
//}



package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.util.ArrayList;
import java.util.List;

import adapters.add.*;
import adapters.get.*;
import adapters.update.*;
import adapters.delete.*;
import adapters.list.*;
import quanlynguoidung.them.AddUserUseCase;
import quanlynguoidung.get.GetUserUseCase;
import quanlynguoidung.update.UpdateUserUseCase;
import quanlynguoidung.delete.DeleteUserUseCase;
import quanlynguoidung.list.ListUsersUseCase;
import repository.jdbc.UserRepoImpl;

/**
 * Test Suite cho User Management System
 * Kiểm tra toàn bộ chức năng CRUD + List của hệ thống quản lý người dùng
 */
public class TestAddUser {
<<<<<<< Updated upstream
    
    private UserRepoImpl repository;
    private List<String> createdUserIds = new ArrayList<>();
    
    /**
     * Setup: Chạy TRƯỚC MỖI test case
     * - Khởi tạo repository mới
     * - Reset danh sách user IDs để cleanup
     */
    @BeforeEach
    public void setUp() {
        repository = new UserRepoImpl();
        createdUserIds.clear();
=======

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

>>>>>>> Stashed changes
    }
    
    /**
     * Teardown: Chạy SAU MỖI test case
     * - Xóa tất cả users được tạo trong test để giữ DB sạch
     * - Tránh ảnh hưởng giữa các test cases
     */
    @AfterEach
    public void tearDown() {
        // Cleanup: Xóa tất cả users được tạo trong test
        for (String userId : createdUserIds) {
            try {
                repository.deleteById(userId);
            } catch (Exception e) {
                // Ignore nếu user đã bị xóa
            }
        }
    }
    
    // ========================================
    // TEST ADD USER (CREATE)
    // ========================================
    
    /**
     * TEST: Thêm user với dữ liệu hợp lệ
     * Expected: Thành công, trả về user ID và message "Thêm người dùng thành công!"
     */
    @Test
    public void testAddUser_ValidInput_Success() {
        // Arrange - Chuẩn bị dữ liệu test
        // Dùng timestamp để tạo unique values, tránh trùng lặp
        long timestamp = System.currentTimeMillis();
        AddUserInputDTO inputDTO = new AddUserInputDTO();
        inputDTO.username = "testuser_" + timestamp;
        inputDTO.password = "password123";
        inputDTO.fullName = "Test User";
        inputDTO.email = "test_" + timestamp + "@example.com";
        inputDTO.phone = String.format("09%08d", timestamp % 100000000); // 10 chữ số
        inputDTO.address = "123 Test Street";
        
        // Khởi tạo controller + use case + presenter + view model
        AddUserViewModel viewModel = new AddUserViewModel();
        AddUserPresenter presenter = new AddUserPresenter(viewModel);
        AddUserUseCase useCase = new AddUserUseCase(repository, presenter);
        AddUserController controller = new AddUserController(useCase);
        
        // Act - Thực thi action
        controller.execute(inputDTO);
        
        // Track để cleanup sau test
        if (viewModel.userId != null) {
            createdUserIds.add(viewModel.userId);
        }
        
        // Assert - Kiểm tra kết quả
        assertTrue(viewModel.success, "Add user should succeed. Error: " + viewModel.message);
        assertNotNull(viewModel.userId, "User ID should not be null");
        assertEquals("Thêm người dùng thành công!", viewModel.message);
        assertNotNull(viewModel.timestamp);
    }
    
    /**
     * TEST: Thêm user với username trống
     * Expected: Thất bại, trả về error message
     */
    @Test
    public void testAddUser_EmptyUsername_Failed() {
        // Arrange
        AddUserInputDTO inputDTO = new AddUserInputDTO();
        inputDTO.username = ""; // ⚠️ Username trống (invalid)
        inputDTO.password = "password123";
        inputDTO.fullName = "Test User";
        inputDTO.email = "test@example.com";
        inputDTO.phone = "0123456789";
        
        AddUserViewModel viewModel = new AddUserViewModel();
        AddUserPresenter presenter = new AddUserPresenter(viewModel);
        AddUserUseCase useCase = new AddUserUseCase(repository, presenter);
        AddUserController controller = new AddUserController(useCase);
        
        // Act
        controller.execute(inputDTO);
        
        // Assert - Phải thất bại
        assertFalse(viewModel.success, "Add user should fail with empty username");
        assertNotNull(viewModel.message);
    }
    
    /**
     * TEST: Thêm user với email đã tồn tại
     * Expected: Thất bại, message chứa "Email đã được sử dụng"
     */
    @Test
    public void testAddUser_DuplicateEmail_Failed() {
        // Arrange - Tạo user đầu tiên (thành công)
        AddUserInputDTO inputDTO1 = new AddUserInputDTO();
        inputDTO1.username = "user_first";
        inputDTO1.password = "password123";
        inputDTO1.fullName = "First User";
        inputDTO1.email = "duplicate@example.com"; // ⚠️ Email này sẽ bị trùng
        inputDTO1.phone = "0111111111";
        
        AddUserViewModel viewModel1 = new AddUserViewModel();
        AddUserPresenter presenter1 = new AddUserPresenter(viewModel1);
        AddUserUseCase useCase1 = new AddUserUseCase(repository, presenter1);
        AddUserController controller1 = new AddUserController(useCase1);
        controller1.execute(inputDTO1);
        
        if (viewModel1.userId != null) {
            createdUserIds.add(viewModel1.userId);
        }
        
        // Arrange - Tạo user thứ hai với CÙNG EMAIL
        AddUserInputDTO inputDTO2 = new AddUserInputDTO();
        inputDTO2.username = "user_second"; // Username khác
        inputDTO2.password = "password456";
        inputDTO2.fullName = "Second User";
        inputDTO2.email = "duplicate@example.com"; // ⚠️ Email trùng với user_first
        inputDTO2.phone = "0222222222";
        
        AddUserViewModel viewModel2 = new AddUserViewModel();
        AddUserPresenter presenter2 = new AddUserPresenter(viewModel2);
        AddUserUseCase useCase2 = new AddUserUseCase(repository, presenter2);
        AddUserController controller2 = new AddUserController(useCase2);
        
        // Act
        controller2.execute(inputDTO2);
        
        // Assert - Phải thất bại vì email trùng
        assertFalse(viewModel2.success, "Add user should fail with duplicate email");
        assertTrue(viewModel2.message.contains("Email") || 
                   viewModel2.message.contains("đã được sử dụng"));
    }
    
    // ========================================
    // TEST GET USER (READ)
    // ========================================
    
    /**
     * TEST: Tìm user theo ID hợp lệ
     * Expected: Thành công, trả về đúng thông tin user
     */
    @Test
    public void testGetUser_ValidId_Success() {
        // Arrange - Tạo user trước để test
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "getuser_test";
        addDTO.password = "password123";
        addDTO.fullName = "Get User Test";
        addDTO.email = "getuser@example.com";
        addDTO.phone = "0333333333";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        assertTrue(addViewModel.success, "Add user should succeed: " + addViewModel.message);
        
        String userId = addViewModel.userId;
        createdUserIds.add(userId);
        
        // Arrange - Chuẩn bị tìm kiếm user theo ID
        GetUserInputDTO getDTO = new GetUserInputDTO();
        getDTO.searchBy = "id";
        getDTO.searchValue = userId;
        
        GetUserViewModel getViewModel = new GetUserViewModel();
        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
        GetUserController getController = new GetUserController(getUseCase);
        
        // Act
        getController.execute(getDTO);
        
        // Assert - Phải tìm thấy user với đúng thông tin
        assertTrue(getViewModel.success, "Get user should succeed");
        assertNotNull(getViewModel.user);
        assertEquals(userId, getViewModel.user.id);
        assertEquals("getuser_test", getViewModel.user.username);
        assertEquals("Get User Test", getViewModel.user.fullName);
    }
    
    /**
     * TEST: Tìm user với ID không tồn tại
     * Expected: Thất bại, user = null, message "không tìm thấy"
     */
    @Test
    public void testGetUser_InvalidId_Failed() {
        // Arrange
        GetUserInputDTO getDTO = new GetUserInputDTO();
        getDTO.searchBy = "id";
        getDTO.searchValue = "INVALID_USER_ID_999"; // ⚠️ ID không tồn tại
        
        GetUserViewModel getViewModel = new GetUserViewModel();
        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
        GetUserController getController = new GetUserController(getUseCase);
        
        // Act
        getController.execute(getDTO);
        
        // Assert - Phải thất bại
        assertFalse(getViewModel.success, "Get user should fail with invalid ID");
        assertNull(getViewModel.user);
        assertTrue(getViewModel.message.contains("không tìm thấy") || 
                   getViewModel.message.contains("Không tìm thấy"));
    }
    
    // ========================================
    // TEST UPDATE USER
    // ========================================
    
    /**
     * TEST: Cập nhật user với dữ liệu hợp lệ
     * Expected: Thành công, thông tin user được cập nhật đúng
     */
    @Test
    public void testUpdateUser_ValidInput_Success() {
        // Arrange - Tạo user trước
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "updateuser_test";
        addDTO.password = "password123";
        addDTO.fullName = "Original Name";
        addDTO.email = "original@example.com";
        addDTO.phone = "0444444444";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        String userId = addViewModel.userId;
        createdUserIds.add(userId);
        
        // Arrange - Chuẩn bị update user
        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
        updateDTO.userId = userId;
        updateDTO.fullName = "Updated Name"; // 🔄 Đổi tên
        updateDTO.email = "updated@example.com"; // 🔄 Đổi email
        updateDTO.phone = "0987654321"; // 🔄 Đổi phone
        
        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        UpdateUserController updateController = new UpdateUserController(updateUseCase);
        
        // Act
        updateController.execute(updateDTO);
        
        // Assert - Kiểm tra thông tin đã được cập nhật
        assertTrue(updateViewModel.success, "Update user should succeed");
        assertNotNull(updateViewModel.updatedUser);
        assertEquals("Updated Name", updateViewModel.updatedUser.fullName);
        assertEquals("updated@example.com", updateViewModel.updatedUser.email);
        assertEquals("0987654321", updateViewModel.updatedUser.phone);
    }
    
    /**
     * TEST: Cập nhật user với ID không tồn tại
     * Expected: Thất bại, message "không tìm thấy"
     */
    @Test
    public void testUpdateUser_InvalidUserId_Failed() {
        // Arrange
        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
        updateDTO.userId = "INVALID_USER_ID_999"; // ⚠️ ID không tồn tại
        updateDTO.fullName = "Updated Name";
        
        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        UpdateUserController updateController = new UpdateUserController(updateUseCase);
        
        // Act
        updateController.execute(updateDTO);
        
        // Assert - Phải thất bại
        assertFalse(updateViewModel.success, "Update should fail with invalid user ID");
        assertTrue(updateViewModel.message.contains("không tìm thấy") || 
                   updateViewModel.message.contains("Không tìm thấy"));
    }
    
    // ========================================
    // TEST DELETE USER
    // ========================================
    
    /**
     * TEST: Xóa user với ID hợp lệ
     * Expected: Thành công, user không còn tồn tại trong DB
     */
    @Test
    public void testDeleteUser_ValidId_Success() {
        // Arrange - Tạo user trước
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "deleteuser_test";
        addDTO.password = "password123";
        addDTO.fullName = "Delete User Test";
        addDTO.email = "delete@example.com";
        addDTO.phone = "0555555555";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        String userId = addViewModel.userId;
        // Không add vào createdUserIds vì sẽ tự xóa trong test
        
        // Arrange - Chuẩn bị xóa user
        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
        deleteDTO.userId = userId;
        
        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
        
        // Act
        deleteController.execute(deleteDTO);
        
        // Assert - Kiểm tra xóa thành công
        assertTrue(deleteViewModel.success, "Delete user should succeed");
        assertEquals(userId, deleteViewModel.deletedUserId);
        assertEquals("deleteuser_test", deleteViewModel.deletedUsername);
        
        // Verify user không còn tồn tại
        GetUserInputDTO getDTO = new GetUserInputDTO();
        getDTO.searchBy = "id";
        getDTO.searchValue = userId;
        
        GetUserViewModel getViewModel = new GetUserViewModel();
        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
        GetUserController getController = new GetUserController(getUseCase);
        getController.execute(getDTO);
        
        assertFalse(getViewModel.success, "User should not exist after deletion");
    }
    
    /**
     * TEST: Xóa user với ID không tồn tại
     * Expected: Thất bại
     */
    @Test
    public void testDeleteUser_InvalidId_Failed() {
        // Arrange
        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
        deleteDTO.userId = "INVALID_USER_ID_999"; // ⚠️ ID không tồn tại
        
        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
        
        // Act
        deleteController.execute(deleteDTO);
        
        // Assert - Phải thất bại
        assertFalse(deleteViewModel.success, "Delete should fail with invalid user ID");
    }
    
    // ========================================
    // TEST LIST USERS
    // ========================================
    
    /**
     * TEST: Lấy danh sách tất cả users (không filter status)
     * Expected: Thành công, trả về danh sách users
     */
    @Test
    public void testListUsers_AllStatus_Success() {
        // Arrange - Tạo ít nhất 1 user để test
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "listuser_test";
        addDTO.password = "password123";
        addDTO.fullName = "List User Test";
        addDTO.email = "listuser@example.com";
        addDTO.phone = "0666666666";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        createdUserIds.add(addViewModel.userId);
        
        // Arrange - List users
        ListUsersInputDTO listDTO = new ListUsersInputDTO();
        listDTO.statusFilter = "all"; // 📋 Lấy tất cả (không filter)
        listDTO.sortBy = "fullName";
        listDTO.ascending = true;
        
        ListUsersViewModel listViewModel = new ListUsersViewModel();
        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
        ListUsersController listController = new ListUsersController(listUseCase);
        
        // Act
        listController.execute(listDTO);
        
        // Assert
        assertTrue(listViewModel.success, "List users should succeed");
        assertNotNull(listViewModel.users);
        assertTrue(listViewModel.totalCount >= 1, "Should have at least 1 user");
    }
    
    /**
     * TEST: Lấy danh sách chỉ users có status = "active"
     * Expected: Thành công, tất cả users trả về đều có status = "active"
     */
    @Test
    public void testListUsers_ActiveOnly_Success() {
        // Arrange - Tạo user active
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "activeuser_test";
        addDTO.password = "password123";
        addDTO.fullName = "Active User Test";
        addDTO.email = "active@example.com";
        addDTO.phone = "0777777777";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        createdUserIds.add(addViewModel.userId);
        
        // Arrange - List active users
        ListUsersInputDTO listDTO = new ListUsersInputDTO();
        listDTO.statusFilter = "active"; // 📋 Chỉ lấy active users
        listDTO.sortBy = "username";
        listDTO.ascending = true;
        
        ListUsersViewModel listViewModel = new ListUsersViewModel();
        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
        ListUsersController listController = new ListUsersController(listUseCase);
        
        // Act
        listController.execute(listDTO);
        
        // Assert
        assertTrue(listViewModel.success, "List users should succeed");
        assertNotNull(listViewModel.users);
        
        // Verify tất cả user trong list đều có status = "active"
        for (var user : listViewModel.users) {
            assertEquals("active", user.status, "All users should have active status");
        }
    }
    
    /**
     * TEST: Sắp xếp users theo email giảm dần
     * Expected: Danh sách được sắp xếp đúng thứ tự email Z → A
     */
    @Test
    public void testListUsers_SortByEmail_Descending() {
        // Arrange - Tạo 2 users để test sorting
        AddUserInputDTO addDTO1 = new AddUserInputDTO();
        addDTO1.username = "user_a";
        addDTO1.password = "password123";
        addDTO1.fullName = "User A";
        addDTO1.email = "a@example.com"; // Email nhỏ hơn
        addDTO1.phone = "0888888888";
        
        AddUserViewModel addViewModel1 = new AddUserViewModel();
        AddUserPresenter addPresenter1 = new AddUserPresenter(addViewModel1);
        AddUserUseCase addUseCase1 = new AddUserUseCase(repository, addPresenter1);
        AddUserController addController1 = new AddUserController(addUseCase1);
        addController1.execute(addDTO1);
        createdUserIds.add(addViewModel1.userId);
        
        AddUserInputDTO addDTO2 = new AddUserInputDTO();
        addDTO2.username = "user_z";
        addDTO2.password = "password123";
        addDTO2.fullName = "User Z";
        addDTO2.email = "z@example.com"; // Email lớn hơn
        addDTO2.phone = "0999999999";
        
        AddUserViewModel addViewModel2 = new AddUserViewModel();
        AddUserPresenter addPresenter2 = new AddUserPresenter(addViewModel2);
        AddUserUseCase addUseCase2 = new AddUserUseCase(repository, addPresenter2);
        AddUserController addController2 = new AddUserController(addUseCase2);
        addController2.execute(addDTO2);
        createdUserIds.add(addViewModel2.userId);
        
        // Arrange - List with sorting
        ListUsersInputDTO listDTO = new ListUsersInputDTO();
        listDTO.statusFilter = "all";
        listDTO.sortBy = "email"; // 📋 Sắp xếp theo email
        listDTO.ascending = false; // 📋 Giảm dần (Z → A)
        
        ListUsersViewModel listViewModel = new ListUsersViewModel();
        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
        ListUsersController listController = new ListUsersController(listUseCase);
        
        // Act
        listController.execute(listDTO);
        
        // Assert
        assertTrue(listViewModel.success, "List users should succeed");
        assertNotNull(listViewModel.users);
        
        // Verify thứ tự giảm dần theo email
        if (listViewModel.users.size() > 1) {
            for (int i = 0; i < listViewModel.users.size() - 1; i++) {
                String email1 = listViewModel.users.get(i).email;
                String email2 = listViewModel.users.get(i + 1).email;
                assertTrue(email1.compareTo(email2) >= 0, 
                          "Emails should be in descending order");
            }
        }
    }
    
    /**
     * TEST: Lấy danh sách chỉ users có status = "inactive"
     * Expected: Thành công, tất cả users trả về đều có status = "inactive"
     */
    @Test
    public void testListUsers_InactiveFilter() {
        // Arrange - Tạo user và set inactive
        long timestamp = System.currentTimeMillis();
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "inactive_user_" + timestamp;
        addDTO.password = "password123";
        addDTO.fullName = "Inactive User";
        addDTO.email = "inactive_" + timestamp + "@example.com";
        addDTO.phone = String.format("09%08d", timestamp % 100000000);
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        String userId = addViewModel.userId;
        createdUserIds.add(userId);
        
        // Update status thành inactive
        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
        updateDTO.userId = userId;
        updateDTO.status = "inactive"; // 🔄 Đổi status thành inactive
        
        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        UpdateUserController updateController = new UpdateUserController(updateUseCase);
        updateController.execute(updateDTO);
        
        assertTrue(updateViewModel.success, "Update to inactive should succeed: " + updateViewModel.message);
        
        // Arrange - List inactive users
        ListUsersInputDTO listDTO = new ListUsersInputDTO();
        listDTO.statusFilter = "inactive"; // 📋 Chỉ lấy inactive users
        listDTO.sortBy = "fullName";
        listDTO.ascending = true;
        
        ListUsersViewModel listViewModel = new ListUsersViewModel();
        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
        ListUsersController listController = new ListUsersController(listUseCase);
        
        // Act
        listController.execute(listDTO);
        
        // Assert
        assertTrue(listViewModel.success, "List should succeed: " + listViewModel.message);
        assertNotNull(listViewModel.users, "Users list should not be null");
        assertTrue(listViewModel.filteredCount >= 1, "Should have at least 1 inactive user");
        
        // Verify user vừa tạo có trong list và tất cả đều inactive
        boolean foundOurUser = false;
        for (var user : listViewModel.users) {
            if (user.id.equals(userId)) {
                foundOurUser = true;
                assertEquals("inactive", user.status, "Our user should be inactive");
            }
            assertEquals("inactive", user.status, 
                "All users in filtered result should be inactive");
        }
        
        assertTrue(foundOurUser, "Our inactive user should be in the filtered list");
    }
    
    // ========================================
    // TEST INTEGRATION (Full CRUD Cycle)
    // ========================================
    
    /**
     * TEST TỔNG HỢP: Test toàn bộ vòng đời CRUD của 1 user
     * 1. CREATE - Tạo user mới
     * 2. READ - Đọc thông tin user vừa tạo
     * 3. UPDATE - Cập nhật thông tin user
     * 4. DELETE - Xóa user
     * 5. VERIFY - Xác nhận user đã bị xóa
     * Expected: Tất cả 5 bước đều thành công
     */
    @Test
    public void testFullCRUDCycle() {
        // 1️⃣ CREATE - Tạo user mới
        AddUserInputDTO addDTO = new AddUserInputDTO();
        addDTO.username = "crud_test_user";
        addDTO.password = "password123";
        addDTO.fullName = "CRUD Test User";
        addDTO.email = "crud@example.com";
        addDTO.phone = "0200000000";
        
        AddUserViewModel addViewModel = new AddUserViewModel();
        AddUserPresenter addPresenter = new AddUserPresenter(addViewModel);
        AddUserUseCase addUseCase = new AddUserUseCase(repository, addPresenter);
        AddUserController addController = new AddUserController(addUseCase);
        addController.execute(addDTO);
        
        assertTrue(addViewModel.success, "Step 1: Create user should succeed");
        String userId = addViewModel.userId;
        
        // 2️⃣ READ - Đọc thông tin user vừa tạo
        GetUserInputDTO getDTO = new GetUserInputDTO();
        getDTO.searchBy = "id";
        getDTO.searchValue = userId;
        
        GetUserViewModel getViewModel = new GetUserViewModel();
        GetUserPresenter getPresenter = new GetUserPresenter(getViewModel);
        GetUserUseCase getUseCase = new GetUserUseCase(repository, getPresenter);
        GetUserController getController = new GetUserController(getUseCase);
        getController.execute(getDTO);
        
        assertTrue(getViewModel.success, "Step 2: Read user should succeed");
        assertEquals("CRUD Test User", getViewModel.user.fullName);
        
        // 3️⃣ UPDATE - Cập nhật thông tin user
        UpdateUserInputDTO updateDTO = new UpdateUserInputDTO();
        updateDTO.userId = userId;
        updateDTO.fullName = "Updated CRUD User"; // 🔄 Đổi tên
        
        UpdateUserViewModel updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        UpdateUserController updateController = new UpdateUserController(updateUseCase);
        updateController.execute(updateDTO);
        
        assertTrue(updateViewModel.success, "Step 3: Update user should succeed");
        assertEquals("Updated CRUD User", updateViewModel.updatedUser.fullName);
        
        // 4️⃣ DELETE - Xóa user
        DeleteUserInputDTO deleteDTO = new DeleteUserInputDTO();
        deleteDTO.userId = userId;
        
        DeleteUserViewModel deleteViewModel = new DeleteUserViewModel();
        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
        DeleteUserController deleteController = new DeleteUserController(deleteUseCase);
        deleteController.execute(deleteDTO);
        
        assertTrue(deleteViewModel.success, "Step 4: Delete user should succeed");
        
        // 5️⃣ VERIFY DELETION - Xác nhận user đã bị xóa
        GetUserViewModel verifyViewModel = new GetUserViewModel();
        GetUserPresenter verifyPresenter = new GetUserPresenter(verifyViewModel);
        GetUserUseCase verifyUseCase = new GetUserUseCase(repository, verifyPresenter);
        GetUserController verifyController = new GetUserController(verifyUseCase);
        verifyController.execute(getDTO);
        
        assertFalse(verifyViewModel.success, "Step 5: User should not exist after deletion");
    }
}