package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/customer/dashboard", "/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        String path = request.getServletPath();

        // Customer trying to access admin dashboard
        if (path.contains("/admin/") && "CUSTOMER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            return;
        }

        // Admin/staff trying to access customer dashboard
        if (path.contains("/customer/") && !"CUSTOMER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Forward to appropriate dashboard
        if ("CUSTOMER".equals(role)) {
            request.getRequestDispatcher("/WEB-INF/views/customer-dashboard.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
        }
    }
}