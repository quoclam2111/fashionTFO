package frameworks.web.servlet;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet. annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet. http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta. servlet.http.HttpSession;

import repository.DTO.OrderDTO;
import repository.jdbc.OrderRepoImpl;

@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {
    
    private OrderRepoImpl orderRepo;
    
    @Override
    public void init() throws ServletException {
        orderRepo = new OrderRepoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"CUSTOMER".equals(role)) {
            response.sendRedirect(request. getContextPath() + "/admin/dashboard");
            return;
        }
        
        // Lấy orderId từ query string
        String orderId = request. getParameter("orderId");
        
        if (orderId != null && !orderId.trim().isEmpty()) {
            // Lấy thông tin đơn hàng
            Optional<OrderDTO> orderOpt = orderRepo.findById(orderId);
            
            if (orderOpt.isPresent()) {
                request.setAttribute("order", orderOpt.get());
            } else {
                request.setAttribute("error", "Không tìm thấy đơn hàng!");
            }
        } else {
            request.setAttribute("error", "Không tìm thấy mã đơn hàng!");
        }

        request.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(request, response);
    }
}