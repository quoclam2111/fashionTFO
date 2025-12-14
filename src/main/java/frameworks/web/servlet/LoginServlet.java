package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import adapters.quanlynguoidung.dangnhap.*;
import quanlynguoidung.dangnhap.*;
import repository.jdbc.LoginRepoImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private LoginController controller;
    private LoginViewModel viewModel;

    @Override
    public void init() throws ServletException {
        // Khởi tạo dependencies với LoginRepoImpl của bạn
        LoginRepoImpl repository = new LoginRepoImpl();
        viewModel = new LoginViewModel();
        LoginPresenter presenter = new LoginPresenter(viewModel);
        LoginUseCase useCase = new LoginUseCase(repository, presenter);
        controller = new LoginController(useCase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị trang login
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Tạo DTO và gọi controller
        LoginInputDTO dto = new LoginInputDTO();
        dto.username = username;
        dto.password = password;

        controller.executeWithDTO(dto);

        // Xử lý kết quả
        if (viewModel.success) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", viewModel.userId);
            session.setAttribute("username", viewModel.username);
            session.setAttribute("fullName", viewModel.fullName);
            session.setAttribute("role", viewModel.role);
            session.setAttribute("accountType", viewModel.accountType);

            // Redirect dựa vào role
            if ("CUSTOMER".equals(viewModel.role)) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            } else {
                // ADMIN, MANAGER, STAFF
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            }
        } else {
            request.setAttribute("error", viewModel.message);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}