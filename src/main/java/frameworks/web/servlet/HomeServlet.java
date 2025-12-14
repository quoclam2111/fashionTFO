package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import adapters.sanpham.list.ListProductController;
import adapters.sanpham.list.ListProductInputDTO;
import adapters.sanpham.list.ListProductPresenter;
import adapters.sanpham.list.ListProductViewModel;
import quanlysanpham.list.ProductListUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

/**
 * Servlet xử lý trang chủ - hiển thị danh sách sản phẩm từ database
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Setup Architecture Layers (Clean Architecture)
            SanPhamRepositoryImpl repository = new SanPhamRepositoryImpl();
            
            // ViewModel để nhận kết quả
            ListProductViewModel viewModel = new ListProductViewModel();
            
            // Presenter để format dữ liệu
            ListProductPresenter presenter = new ListProductPresenter(viewModel);
            
            // Use Case chứa business logic
            ProductListUseCase useCase = new ProductListUseCase(repository, presenter);
            
            // Controller điều khiển flow
            ListProductController controller = new ListProductController(useCase);
            
            // 2. Tạo request để lấy tất cả sản phẩm PUBLISHED
            ListProductInputDTO inputDTO = new ListProductInputDTO();
            inputDTO.statusFilter = "PUBLISHED"; // Chỉ lấy sản phẩm đang publish
            inputDTO.sortBy = "createdAt"; // Sắp xếp theo thời gian tạo (mới nhất)
            inputDTO.ascending = false; // Giảm dần (mới nhất trước)
            
            // 3. Execute use case
            controller.execute(inputDTO);
            
            // 4. Kiểm tra kết quả
            if (viewModel.success) {
                // Đưa danh sách sản phẩm vào request attribute
                request.setAttribute("products", viewModel.products);
                request.setAttribute("totalCount", viewModel.totalCount);
                request.setAttribute("filteredCount", viewModel.filteredCount);
                
                System.out.println("✅ Loaded " + viewModel.products.size() + " products successfully");
            } else {
                // Nếu có lỗi, set empty list
                request.setAttribute("products", java.util.Collections.emptyList());
                request.setAttribute("errorMessage", viewModel.message);
                
                System.err.println("❌ Failed to load products: " + viewModel.message);
            }
            
            // 5. Forward đến JSP
            request.getRequestDispatcher("/WEB-INF/views/home.jsp")
                   .forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có exception, vẫn forward nhưng với empty list
            request.setAttribute("products", java.util.Collections.emptyList());
            request.setAttribute("errorMessage", "Lỗi khi tải sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/home.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect POST requests to GET
        doGet(request, response);
    }
}