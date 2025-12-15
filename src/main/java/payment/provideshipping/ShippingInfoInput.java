package payment.provideshipping;

public class ShippingInfoInput {
    // Order info
    public final String orderId;
    public final String phone;
    
    // Địa chỉ chi tiết (số nhà, tên đường)
    public final String address;
    
    // GHN Address Components - BẮT BUỘC để validate
    public final Integer provinceId;    // ID tỉnh từ GHN
    public final Integer districtId;    // ID quận từ GHN  
    public final String wardCode;       // Mã phường từ GHN
    
    // Optional: Tên để hiển thị (Frontend gửi lên)
    public final String provinceName;   // Tên tỉnh
    public final String districtName;   // Tên quận
    public final String wardName;       // Tên phường

    // Constructor tối thiểu (BẮT BUỘC các field này)
    public ShippingInfoInput(String orderId, String phone, String address, 
                            Integer provinceId, Integer districtId, String wardCode) {
        this.orderId = orderId;
        this.phone = phone;
        this.address = address;
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.wardCode = wardCode;
        this.provinceName = null;
        this.districtName = null;
        this.wardName = null;
    }
    
    // Constructor đầy đủ (có cả tên)
    public ShippingInfoInput(String orderId, String phone, String address, 
                            Integer provinceId, String provinceName,
                            Integer districtId, String districtName,
                            String wardCode, String wardName) {
        this.orderId = orderId;
        this.phone = phone;
        this.address = address;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardCode = wardCode;
        this.wardName = wardName;
    }
    
    /**
     * Lấy địa chỉ đầy đủ để lưu vào database
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder(address);
        if (wardName != null) sb.append(", ").append(wardName);
        if (districtName != null) sb.append(", ").append(districtName);
        if (provinceName != null) sb.append(", ").append(provinceName);
        return sb.toString();
    }
}