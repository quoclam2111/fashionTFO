package adapters.cart;


public class ViewCartInputDTO {
    private final int userId;

    public ViewCartInputDTO(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}