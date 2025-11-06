package view.interfaces;

import model.User;

public interface IHomeView {
    void updateUserInterface(User user);
    void showLoginDialog();
    void showRegisterDialog();
    void showMessage(String message);
}