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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private RegisterController controller;
    private RegisterViewModel viewModel;

    @Override
    public void init() throws ServletException {
        // Sử dụng UserRepoImpl của bạn
        UserRepoImpl repository = new UserRepoImpl();
        viewModel = new RegisterViewModel();
        RegisterPresenter presenter = new RegisterPresenter(viewModel);
        RegisterUseCase useCase = new RegisterUseCase(repository, presenter);
        controller = new RegisterController(useCase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
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

        if (viewModel.success) {
            request.setAttribute("success", viewModel.message);
            request.setAttribute("registeredUsername", viewModel.username);
            request.getRequestDispatcher("/WEB-INF/views/register-success.jsp").forward(request, response);
        } else {
            request.setAttribute("error", viewModel.message);
            request.setAttribute("formData", dto);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}