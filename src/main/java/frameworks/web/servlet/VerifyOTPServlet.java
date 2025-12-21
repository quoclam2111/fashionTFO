package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import adapters.quanlynguoidung.dangky.*;
import quanlynguoidung.dangky.*;
import repository.jdbc.UserRepoImpl;

@WebServlet("/verify-otp")
public class VerifyOTPServlet extends HttpServlet {
    
    private VerifyOTPController controller;
    private RegisterViewModel viewModel;

    @Override
    public void init() throws ServletException {
        UserRepoImpl repository = new UserRepoImpl();
        viewModel = new RegisterViewModel();
        VerifyOTPPresenter presenter = new VerifyOTPPresenter(viewModel); // ⭐ Dùng presenter riêng
        VerifyOTPUseCase useCase = new VerifyOTPUseCase(repository, presenter);
        controller = new VerifyOTPController(useCase);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String otp = request.getParameter("otp");

        // Gọi controller
        controller.verify(userId, otp);

        if (viewModel.success) {
            // ✅ Xác thực thành công
            request.setAttribute("success", viewModel.message);
            request. getRequestDispatcher("/WEB-INF/views/verify-otp-success.jsp").forward(request, response);
        } else {
            // ❌ Xác thực thất bại
            request.setAttribute("error", viewModel.message);
            request. setAttribute("userId", userId);
            request.getRequestDispatcher("/WEB-INF/views/register-success.jsp").forward(request, response);
        }
    }
}