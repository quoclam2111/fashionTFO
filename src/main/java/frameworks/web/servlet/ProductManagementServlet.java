package frameworks.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import adapters.sanpham.list.*;
import adapters.sanpham.get.*;
import adapters.sanpham.add.*;
import adapters.sanpham.edit.*;
import adapters.sanpham.delete.*;

import quanlysanpham.list.ProductListUseCase;
import quanlysanpham.get.GetProductUseCase;
import quanlysanpham.add.AddProductUseCase;
import quanlysanpham.edit.UpdateProductUseCase;
import quanlysanpham.delete.DeleteProductUseCase;

import repository.jdbc.SanPhamRepositoryImpl;

@WebServlet("/admin/products")
public class ProductManagementServlet extends HttpServlet {
    private ListProductController listController;
    private ListProductViewModel listViewModel;
    
    private GetProductController getController;
    private GetProductViewModel getViewModel;
    
    private AddProductController addController;
    private AddProductViewModel addViewModel;
    
    private UpdateProductController updateController;
    private UpdateProductViewModel updateViewModel;
    
    private DeleteProductController deleteController;
    private DeleteProductViewModel deleteViewModel;
    
    @Override
    public void init() throws ServletException {
        SanPhamRepositoryImpl repository = new SanPhamRepositoryImpl();
        
        // List products
        listViewModel = new ListProductViewModel();
        ListProductPresenter listPresenter = new ListProductPresenter(listViewModel);
        ProductListUseCase listUseCase = new ProductListUseCase(repository, listPresenter);
        listController = new ListProductController(listUseCase);
        
        // Get product
        getViewModel = new GetProductViewModel();
        GetProductPresenter getPresenter = new GetProductPresenter(getViewModel);
        GetProductUseCase getUseCase = new GetProductUseCase(repository, getPresenter);
        getController = new GetProductController(getUseCase);
        
        // Add product
        addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        addController = new AddProductController(addUseCase);
        
        // Update product
        updateViewModel = new UpdateProductViewModel();
        UpdateProductPresenter updatePresenter = new UpdateProductPresenter(updateViewModel);
        UpdateProductUseCase updateUseCase = new UpdateProductUseCase(repository, updatePresenter);
        updateController = new UpdateProductController(updateUseCase);
        
        // Delete product
        deleteViewModel = new DeleteProductViewModel();
        DeleteProductPresenter deletePresenter = new DeleteProductPresenter(deleteViewModel);
        DeleteProductUseCase deleteUseCase = new DeleteProductUseCase(repository, deletePresenter);
        deleteController = new DeleteProductController(deleteUseCase);
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
            handleViewProduct(request, response);
        } else if ("edit".equals(action)) {
            handleEditForm(request, response);
        } else if ("add".equals(action)) {
            handleAddForm(request, response);
        } else {
            handleListProducts(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            handleAddProduct(request, response);
        } else if ("update".equals(action)) {
            handleUpdateProduct(request, response);
        } else if ("delete".equals(action)) {
            handleDeleteProduct(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    private void handleListProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ListProductInputDTO dto = new ListProductInputDTO();
        dto.statusFilter = request.getParameter("status") != null ? 
                          request.getParameter("status") : "all";
        dto.sortBy = request.getParameter("sortBy") != null ? 
                    request.getParameter("sortBy") : "createdAt";
        dto.ascending = !"desc".equals(request.getParameter("order"));
        
        listController.execute(dto);
        
        request.setAttribute("viewModel", listViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-products.jsp").forward(request, response);
    }
    
    private void handleViewProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        
        GetProductInputDTO dto = new GetProductInputDTO();
        dto.productId = productId;
        
        getController.execute(dto);
        
        request.setAttribute("viewModel", getViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-product-detail.jsp").forward(request, response);
    }
    
    private void handleAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin-product-add.jsp").forward(request, response);
    }
    
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        
        GetProductInputDTO dto = new GetProductInputDTO();
        dto.productId = productId;
        
        getController.execute(dto);
        
        request.setAttribute("viewModel", getViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-product-edit.jsp").forward(request, response);
    }
    
    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        AddProductInputDTO dto = new AddProductInputDTO();
        dto.productName = request.getParameter("productName");
        dto.description = request.getParameter("description");
        dto.brandId = request.getParameter("brandId");
        dto.categoryId = request.getParameter("categoryId");
        dto.defaultImage = request.getParameter("defaultImage");
        dto.price = request.getParameter("price");
        dto.discountPrice = request.getParameter("discountPrice");
        dto.stockQuantity = request.getParameter("stockQuantity");
        dto.status = request.getParameter("status");
        
        addController.execute(dto);
        
        if (addViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?added=true");
        } else {
            request.setAttribute("error", addViewModel.message);
            request.setAttribute("formData", dto);
            request.getRequestDispatcher("/WEB-INF/views/admin-product-add.jsp").forward(request, response);
        }
    }
    
    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UpdateProductInputDTO dto = new UpdateProductInputDTO();
        dto.productId = request.getParameter("productId");
        dto.productName = request.getParameter("productName");
        dto.description = request.getParameter("description");
        dto.brandId = request.getParameter("brandId");
        dto.categoryId = request.getParameter("categoryId");
        dto.defaultImage = request.getParameter("defaultImage");
        dto.price = request.getParameter("price");
        dto.discountPrice = request.getParameter("discountPrice");
        dto.stockQuantity = request.getParameter("stockQuantity");
        dto.status = request.getParameter("status");
        
        updateController.execute(dto);
        
        if (updateViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?action=view&id=" + dto.productId + "&success=true");
        } else {
            request.setAttribute("error", updateViewModel.message);
            handleEditForm(request, response);
        }
    }
    
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        DeleteProductInputDTO dto = new DeleteProductInputDTO();
        dto.productId = request.getParameter("productId");
        
        deleteController.execute(dto);
        
        if (deleteViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/products?deleted=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=" + deleteViewModel.message);
        }
    }
}