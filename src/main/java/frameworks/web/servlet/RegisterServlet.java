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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private SendOTPController controller;
    private OTPViewModel otpViewModel;  // ⭐ Dùng OTPViewModel

    @Override
    public void init() throws ServletException {
        UserRepoImpl repository = new UserRepoImpl();
        otpViewModel = new OTPViewModel();  // ⭐ Khởi tạo OTPViewModel
        OTPPresenter presenter = new OTPPresenter(otpViewModel);  // ⭐ Dùng OTPPresenter
        RegisterUseCase useCase = new RegisterUseCase(repository, presenter);
        controller = new SendOTPController(useCase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register. jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        RegisterInputDTO dto = new RegisterInputDTO();
        dto.username = request.getParameter("username");
        dto.password = request.getParameter("password");
        dto.fullName = request.getParameter("fullName");
        dto.email = request.getParameter("email");
        dto.phone = request.getParameter("phone");
        dto.address = request.getParameter("address");

        controller.executeWithDTO(dto);

        if (otpViewModel.success) {
            // ✅ Đăng ký thành công → Chuyển sang trang nhập OTP
            request.setAttribute("userId", otpViewModel.userId);
            request.setAttribute("email", dto.email);
            request.setAttribute("otpSent", otpViewModel.otpSent);
            request.setAttribute("message", otpViewModel.message);
            request.getRequestDispatcher("/WEB-INF/views/verify-otp-form.jsp").forward(request, response);
        } else {
            // ❌ Đăng ký thất bại → Hiển thị lỗi
            request.setAttribute("error", otpViewModel.message);
            request.setAttribute("formData", dto);
            request. getRequestDispatcher("/WEB-INF/views/register. jsp").forward(request, response);
        }
    }
}