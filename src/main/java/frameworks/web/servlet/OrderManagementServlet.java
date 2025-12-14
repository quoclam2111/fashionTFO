package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import adapters.quanlydonhang.listorders.*;
import adapters.quanlydonhang.updateorder.*;
import adapters.quanlydonhang.deleteorders.*;
import quanlydonhang.list.ListOrdersUseCase;
import quanlydonhang.update.UpdateOrderUseCase;
import quanlydonhang.delete.DeleteOrderUseCase;
import repository.jdbc.OrderRepoImpl;

@WebServlet("/admin/orders")
public class OrderManagementServlet extends HttpServlet {
    private ListOrdersController listController;
    private ListOrdersViewModel listViewModel;
    
    private UpdateOrderController updateController;
    private UpdateOrderViewModel updateViewModel;
    
    private DeleteOrderController deleteController;
    private DeleteOrderViewModel deleteViewModel;
    
    @Override
    public void init() throws ServletException {
        OrderRepoImpl repository = new OrderRepoImpl();
        
        // List orders
        listViewModel = new ListOrdersViewModel();
        ListOrdersPresenter listPresenter = new ListOrdersPresenter(listViewModel);
        ListOrdersUseCase listUseCase = new ListOrdersUseCase(repository, listPresenter);
        listController = new ListOrdersController(listUseCase);
        
        // Update order
        updateViewModel = new UpdateOrderViewModel();
        UpdateOrderPresenter updatePresenter = new UpdateOrderPresenter(updateViewModel);
        UpdateOrderUseCase updateUseCase = new UpdateOrderUseCase(repository, updatePresenter);
        updateController = new UpdateOrderController(updateUseCase);
        
        // Delete order
        deleteViewModel = new DeleteOrderViewModel();
        DeleteOrderPresenter deletePresenter = new DeleteOrderPresenter(deleteViewModel);
        DeleteOrderUseCase deleteUseCase = new DeleteOrderUseCase(repository, deletePresenter);
        deleteController = new DeleteOrderController(deleteUseCase);
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
        if ("CUSTOMER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("view".equals(action)) {
            handleViewOrder(request, response);
        } else if ("edit".equals(action)) {
            handleEditForm(request, response);
        } else {
            handleListOrders(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            handleUpdateOrder(request, response);
        } else if ("delete".equals(action)) {
            handleDeleteOrder(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
    
    private void handleListOrders(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ListOrdersInputDTO dto = new ListOrdersInputDTO();
        dto.statusFilter = request.getParameter("status") != null ? 
                          request.getParameter("status") : "all";
        dto.sortBy = request.getParameter("sortBy") != null ? 
                    request.getParameter("sortBy") : "orderDate";
        dto.ascending = !"desc".equals(request.getParameter("order"));
        
        listController.execute(dto);
        
        request.setAttribute("viewModel", listViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-orders.jsp").forward(request, response);
    }
    
    private void handleViewOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement view order detail
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
    
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String orderId = request.getParameter("id");
        request.setAttribute("orderId", orderId);
        request.getRequestDispatcher("/WEB-INF/views/admin-order-edit.jsp").forward(request, response);
    }
    
    private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UpdateOrderInputDTO dto = new UpdateOrderInputDTO();
        dto.orderId = request.getParameter("orderId");
        dto.userId = request.getParameter("userId");
        dto.customerName = request.getParameter("customerName");
        dto.customerPhone = request.getParameter("customerPhone");
        dto.customerAddress = request.getParameter("customerAddress");
        dto.totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        dto.status = request.getParameter("status");
        dto.note = request.getParameter("note");
        
        updateController.execute(dto);
        
        if (updateViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?updated=true");
        } else {
            request.setAttribute("error", updateViewModel.message);
            handleEditForm(request, response);
        }
    }
    
    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        DeleteOrderInputDTO dto = new DeleteOrderInputDTO();
        dto.orderId = request.getParameter("orderId");
        
        deleteController.execute(dto);
        
        if (deleteViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?deleted=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=" + deleteViewModel.message);
        }
    }
}