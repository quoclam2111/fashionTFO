package adapters.Home;

import java.util.List;

import adapters.order.OrderFormPayment;
import repository.DTO.ProductVariantDTO;
import Pay.IHomeView;

public class HomeController {
    private IHomeView homeView;
    private SessionManager sessionManager;

    public HomeController() {
        this.sessionManager = SessionManager.getInstance();
        this.homeView = new HomeView(this);
    }

    public void showHome() {
        updateView();
        ((HomeView) homeView).setVisible(true);
    }

    public void handleLogin() {
        homeView.showLoginDialog();
    }

    public void handleRegister() {
        homeView.showRegisterDialog();
    }

    public void handleLogout() {
        sessionManager.logout();
        updateView();
        homeView.showMessage("Đăng xuất thành công!");
    }

    public void performLogin(String email, String password) {
        // TODO: Implement authentication logic with DAO
        // Giả lập đăng nhập thành công
        User user = new User("U001", email, "Test User", User.UserRole.CUSTOMER);
        sessionManager.login(user);
        updateView();
        homeView.showMessage("Đăng nhập thành công!");
    }

    public void performRegister(String email, String password, String fullName, String phone, String address) {
        // TODO: Implement registration logic with DAO
        homeView.showMessage("Đăng ký thành công! Vui lòng đăng nhập.");
    }

    private void updateView() {
        homeView.updateUserInterface(sessionManager.getCurrentUser());
    }

    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    // Handlers cho các chức năng (chưa implement logic)
    public void handleViewProducts() {
        // TODO: Navigate to product list view
        homeView.showMessage("Chức năng: Xem danh sách sản phẩm");
    }

    public void handleViewCart() {
        // TODO: Navigate to cart view
        homeView.showMessage("Chức năng: Xem giỏ hàng");
    }

    public void handleCheckout(List<ProductVariantDTO> cartItems) {
        if (!sessionManager.isLoggedIn()) {
            homeView.showMessage("Vui lòng đăng nhập để thanh toán!");
            return;
        }
        OrderFormPayment.openForm(cartItems);
        homeView.showMessage("Chức năng: Xử lý thanh toán");
    }

    // Admin functions
    public void handleManageProducts() {
        homeView.showMessage("Chức năng Admin: Quản lý sản phẩm");
    }

    public void handleManageUsers() {
        homeView.showMessage("Chức năng Admin: Quản lý người dùng");
    }

    public void handleManageOrders() {
        homeView.showMessage("Chức năng Admin: Quản lý đơn hàng");
    }

    public void handleSearch() {
        homeView.showMessage("Chức năng Admin: Tìm kiếm");
    }
}
