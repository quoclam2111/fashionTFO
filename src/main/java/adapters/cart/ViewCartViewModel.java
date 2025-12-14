package adapters.cart;

import quanlycart.ViewCartOutputData;
import java.util.ArrayList;
import java.util.List;

public class ViewCartViewModel {
    private List<ViewCartOutputData> cartItems = new ArrayList<>();
    private boolean error = false;
    private String errorCode;
    private String errorMessage;
    private boolean empty = false;
    
    public List<ViewCartOutputData> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<ViewCartOutputData> cartItems) {
        this.cartItems = cartItems;
    }
    
    public boolean isError() {
        return error;
    }
    
    public void setError(boolean error) {
        this.error = error;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean isEmpty() {
        return empty;
    }
    
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}