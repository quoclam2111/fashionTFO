package payment.provideshipping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GHNAddressValidationService {
    private final String apiToken;
    private final String baseUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GHNAddressValidationService(String apiToken) {
        this.apiToken = apiToken;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Lấy danh sách tỉnh/thành phố
     * GET /master-data/province
     */
    public JsonNode getProvinces() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/master-data/province"))
                .header("Token", apiToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    /**
     * Lấy danh sách quận/huyện theo province_id
     * GET /master-data/district?province_id={id}
     */
    public JsonNode getDistricts(Integer provinceId) throws Exception {
        String url = baseUrl + "/master-data/district";
        if (provinceId != null) {
            url += "?province_id=" + provinceId;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Token", apiToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    /**
     * Lấy danh sách phường/xã theo district_id
     * GET /master-data/ward?district_id={id}
     */
    public JsonNode getWards(Integer districtId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/master-data/ward?district_id=" + districtId))
                .header("Token", apiToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    /**
     * Kiểm tra địa chỉ có hợp lệ không bằng cách verify ward_code, district_id
     */
    public GHNAddressValidationResult validateAddress(String wardCode, Integer districtId) {
        try {
            JsonNode wardResponse = getWards(districtId);
            
            if (wardResponse.get("code").asInt() != 200) {
                return new GHNAddressValidationResult(false, "Quận/Huyện không hợp lệ");
            }

            JsonNode data = wardResponse.get("data");
            
            if (data == null || !data.isArray()) {
                return new GHNAddressValidationResult(false, "Không tìm thấy thông tin phường/xã");
            }

            // Kiểm tra wardCode có tồn tại trong danh sách không
            boolean wardExists = false;
            for (JsonNode ward : data) {
                String code = ward.get("WardCode").asText();
                if (code.equals(wardCode)) {
                    wardExists = true;
                    break;
                }
            }

            if (!wardExists) {
                return new GHNAddressValidationResult(false, "Phường/Xã không hợp lệ hoặc không thuộc quận này");
            }

            return new GHNAddressValidationResult(true, null);
            
        } catch (Exception e) {
            return new GHNAddressValidationResult(false, "Lỗi khi xác thực địa chỉ: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem có thể giao hàng đến địa chỉ này không
     * POST /v2/shipping-order/available-services
     */
    public GHNAddressValidationResult checkServiceAvailability(Integer fromDistrict, Integer toDistrict, Integer shopId) {
        try {
            String requestBody = String.format(
                "{\"shop_id\": %d, \"from_district\": %d, \"to_district\": %d}", 
                shopId, fromDistrict, toDistrict
            );
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v2/shipping-order/available-services"))
                    .header("Token", apiToken)
                    .header("ShopId", String.valueOf(shopId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                return new GHNAddressValidationResult(false, "Không thể kiểm tra dịch vụ giao hàng");
            }

            JsonNode root = objectMapper.readTree(response.body());
            
            if (root.get("code").asInt() != 200) {
                return new GHNAddressValidationResult(false, "Không thể kiểm tra dịch vụ giao hàng");
            }

            JsonNode data = root.get("data");
            
            if (data == null || !data.isArray() || data.size() == 0) {
                return new GHNAddressValidationResult(false, "Chưa hỗ trợ giao hàng đến khu vực này");
            }

            return new GHNAddressValidationResult(true, null);
            
        } catch (Exception e) {
            return new GHNAddressValidationResult(false, "Lỗi khi kiểm tra dịch vụ: " + e.getMessage());
        }
    }

    /**
     * Tính phí ship
     * POST /v2/shipping-order/fee
     */
    public Integer calculateShippingFee(ShippingFeeRequest request) throws Exception {
        String requestBody = objectMapper.writeValueAsString(request);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/v2/shipping-order/fee"))
                .header("Token", apiToken)
//                .header("ShopId", String.valueOf(request.shopId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode root = objectMapper.readTree(response.body());
        
        if (root.get("code").asInt() != 200) {
            throw new Exception("Không thể tính phí ship: " + root.get("message").asText());
        }
        
        return root.get("data").get("total").asInt();
    }

    public static class GHNAddressValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public GHNAddressValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static class ShippingFeeRequest {
        public int shop_id;
        public int from_district_id;
        public int to_district_id;
        public String to_ward_code;
        public int weight; // gram
        public int service_type_id; // 2 = Standard
        
        // Constructor
        public ShippingFeeRequest(int shopId, int fromDistrict, int toDistrict, 
                                 String wardCode, int weight, int serviceType) {
            this.shop_id = shopId;
            this.from_district_id = fromDistrict;
            this.to_district_id = toDistrict;
            this.to_ward_code = wardCode;
            this.weight = weight;
            this.service_type_id = serviceType;
        }
    }
}