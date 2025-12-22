package frameworks.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import payment.provideshipping.GHNAddressValidationService;

@WebServlet("/api/ghn/*")
public class GHNAddressServlet extends HttpServlet {
    
    // ⭐ ĐẶT TOKEN GHN CỦA BẠN Ở ĐÂY
    private static final String GHN_TOKEN = "YOUR_GHN_TOKEN_HERE";
    
    private GHNAddressValidationService ghnService;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        ghnService = new GHNAddressValidationService(GHN_TOKEN);
        objectMapper = new ObjectMapper();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();
        
        try {
            if (pathInfo == null) {
                out.print("{\"error\":\"Invalid endpoint\"}");
                return;
            }
            
            switch (pathInfo) {
                case "/provinces":
                    handleGetProvinces(out);
                    break;
                    
                case "/districts":
                    handleGetDistricts(request, out);
                    break;
                    
                case "/wards":
                    handleGetWards(request, out);
                    break;
                    
                case "/calculate-fee":
                    handleCalculateFee(request, out);
                    break;
                    
                default:
                    out.print("{\"error\":\"Endpoint not found\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void handleGetProvinces(PrintWriter out) throws Exception {
        JsonNode result = ghnService.getProvinces();
        out.print(objectMapper.writeValueAsString(result));
    }
    
    private void handleGetDistricts(HttpServletRequest request, PrintWriter out) throws Exception {
        String provinceIdStr = request.getParameter("province_id");
        
        if (provinceIdStr == null || provinceIdStr.trim().isEmpty()) {
            out.print("{\"error\":\"province_id is required\"}");
            return;
        }
        
        Integer provinceId = Integer.parseInt(provinceIdStr);
        JsonNode result = ghnService.getDistricts(provinceId);
        out.print(objectMapper.writeValueAsString(result));
    }
    
    private void handleGetWards(HttpServletRequest request, PrintWriter out) throws Exception {
        String districtIdStr = request.getParameter("district_id");
        
        if (districtIdStr == null || districtIdStr.trim().isEmpty()) {
            out.print("{\"error\":\"district_id is required\"}");
            return;
        }
        
        Integer districtId = Integer.parseInt(districtIdStr);
        JsonNode result = ghnService.getWards(districtId);
        out.print(objectMapper.writeValueAsString(result));
    }
    
    private void handleCalculateFee(HttpServletRequest request, PrintWriter out) throws Exception {
        String shopIdStr = request.getParameter("shop_id");
        String fromDistrictStr = request.getParameter("from_district");
        String toDistrictStr = request.getParameter("to_district");
        String toWardCode = request.getParameter("to_ward_code");
        String weightStr = request.getParameter("weight");
        
        // Validate
        if (shopIdStr == null || fromDistrictStr == null || toDistrictStr == null || 
            toWardCode == null || weightStr == null) {
            out.print("{\"error\":\"Missing required parameters\"}");
            return;
        }
        
        int shopId = Integer.parseInt(shopIdStr);
        int fromDistrict = Integer.parseInt(fromDistrictStr);
        int toDistrict = Integer.parseInt(toDistrictStr);
        int weight = Integer.parseInt(weightStr);
        
        GHNAddressValidationService.ShippingFeeRequest feeRequest = 
            new GHNAddressValidationService.ShippingFeeRequest(
                shopId, fromDistrict, toDistrict, toWardCode, weight, 2
            );
        
        Integer fee = ghnService.calculateShippingFee(feeRequest);
        
        out.print("{\"fee\":" + fee + "}");
    }
}