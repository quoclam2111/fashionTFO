package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet. http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import adapters.quanlynguoidung.dangky.*;
import quanlynguoidung.dangky.*;
import repository.jdbc.UserRepoImpl;

@WebServlet("/verify-otp")
public class VerifyOTPServlet extends HttpServlet {
    
    private VerifyOTPController controller;
    private OTPViewModel viewModel;

    @Override
    public void init() throws ServletException {
        UserRepoImpl repository = new UserRepoImpl();
        viewModel = new OTPViewModel();
        VerifyOTPPresenter presenter = new VerifyOTPPresenter(viewModel);
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

        System.out.println("🔍 DEBUG - userId: " + userId);
        System.out.println("🔍 DEBUG - otp: " + otp);

        // Gọi controller
        controller.verify(userId, otp);

        System.out.println("🔍 DEBUG - success: " + viewModel.success);
        System.out.println("🔍 DEBUG - message: " + viewModel.message);

        if (viewModel.success) {
            // ✅ Xác thực thành công
            request.setAttribute("success", viewModel.message);
            request. getRequestDispatcher("/WEB-INF/views/verify-otp-success.jsp").forward(request, response);
        } else {
            // ❌ Xác thực thất bại → Quay lại form nhập OTP
            request.setAttribute("error", viewModel.message);
            request.setAttribute("userId", userId);
            request.setAttribute("remainingAttempts", viewModel. remainingAttempts);
            request.getRequestDispatcher("/WEB-INF/views/verify-otp-form.jsp").forward(request, response);
        }
    }
}