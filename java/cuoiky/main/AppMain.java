package cuoiky.main;
import Controller.HomeController;

public class AppMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            HomeController homeController = new HomeController();
            homeController.showHome();
        });
    }
}