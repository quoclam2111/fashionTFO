package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import adapters.quanlynguoidung.get.*;
import adapters.quanlynguoidung.update.*;
import quanlynguoidung.get.*;
import quanlynguoidung.update.*;
import repository.jdbc.UserRepoImpl;

@WebServlet("/customer/profile")
public class CustomerProfileServlet extends HttpServlet {
    private GetUserController getUserController;
    private GetUserViewModel getUserViewModel;
    
    private UpdateUserController updateController;
    private UpdateUserViewModel updateViewModel;
    
    @Override
    public void init() throws ServletException {
        UserRepoImpl repository = new UserRepoImpl();
        
        // Get user
        getUserViewModel = new GetUserViewModel();
        GetUserPresenter getUserPresenter = new GetUserPresenter(getUserViewModel);
        GetUserUseCase getUserUseCase = new GetUserUseCase(repository, getUserPresenter);
        getUserController = new GetUserController(getUserUseCase);
        
        // Update user
        updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        updateController = new UpdateUserController(updateUseCase);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if (!"CUSTOMER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            handleEditForm(request, response, session);
        } else {
            handleViewProfile(request, response, session);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        handleUpdateProfile(request, response, session);
    }
    
    private void handleViewProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        String userId = (String) session.getAttribute("userId");
        
        GetUserInputDTO dto = new GetUserInputDTO();
        dto.searchBy = "id";
        dto.searchValue = userId;
        
        getUserController.executeWithDTO(dto);
        
        request.setAttribute("viewModel", getUserViewModel);
        request.getRequestDispatcher("/WEB-INF/views/customer-profile.jsp").forward(request, response);
    }
    
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        String userId = (String) session.getAttribute("userId");
        
        GetUserInputDTO dto = new GetUserInputDTO();
        dto.searchBy = "id";
        dto.searchValue = userId;
        
        getUserController.executeWithDTO(dto);
        
        request.setAttribute("viewModel", getUserViewModel);
        request.getRequestDispatcher("/WEB-INF/views/customer-profile-edit.jsp").forward(request, response);
    }
    
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        String userId = (String) session.getAttribute("userId");
        
        UpdateUserInputDTO dto = new UpdateUserInputDTO();
        dto.userId = userId;
        dto.fullName = request.getParameter("fullName");
        dto.email = request.getParameter("email");
        dto.phone = request.getParameter("phone");
        dto.address = request.getParameter("address");
        dto.status = "active"; // Customer không thể tự thay đổi status
        
        // Chỉ cập nhật password nếu user nhập
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            dto.password = password;
        }
        
        updateController.execute(dto);
        
        if (updateViewModel.success) {
            // Cập nhật lại session
            session.setAttribute("fullName", dto.fullName);
            
            response.sendRedirect(request.getContextPath() + "/customer/profile?success=true");
        } else {
            request.setAttribute("error", updateViewModel.message);
            request.setAttribute("viewModel", getUserViewModel);
            request.getRequestDispatcher("/WEB-INF/views/customer-profile-edit.jsp").forward(request, response);
        }
    }
}