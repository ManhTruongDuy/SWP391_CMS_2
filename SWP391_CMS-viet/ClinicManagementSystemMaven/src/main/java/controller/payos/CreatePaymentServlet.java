package controller.payos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import util.PayOSUtil;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;

@WebServlet("/api/create-payment")
public class CreatePaymentServlet extends HttpServlet {
    private final PayOS payOS = PayOSUtil.getPayOS();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Đọc toàn bộ nội dung body của request (dạng JSON) và ghép lại thành một chuỗi
        JSONObject json = new JSONObject(req.getReader().lines().collect(Collectors.joining()));
        Long orderCode = json.getLong("orderCode"); // Lấy giá trị trường "orderCode" từ JSON, kiểu Long
        int amount = json.getInt("amount"); //// Lấy giá trị trường "amount" từ JSON, kiểu int

        ItemData item = ItemData.builder()
                .name("thuốc tổng hợp")
                .quantity(1)
                .price(amount)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("thanh toan ")
                .returnUrl("http://localhost:8080/ClinicManagementSystem_war_exploded/view/pharmacist/paySuccess.html?orderCode=" + orderCode)
                .cancelUrl("http://localhost:8080/ClinicManagementSystem_war_exploded/view/pharmacist/payFail.html")
                .item(item)
                .build();

        CheckoutResponseData result = null;
        try {
            result = payOS.createPaymentLink(paymentData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JSONObject response = new JSONObject();
        response.put("checkoutUrl", result.getCheckoutUrl());

        resp.setContentType("application/json");
        resp.getWriter().write(response.toString());
    }
}
