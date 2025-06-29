package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/payos")
public class PayOSServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String amountStr = request.getParameter("amount");
        if (amountStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing amount\"}");
            return;
        }
        try {
            int amount = Integer.parseInt(amountStr);
            // TODO: Thay thế các thông tin bên dưới bằng thông tin PayOS thực tế của bạn
            String payosApiUrl = "https://api.payos.vn/v1/payment-qr"; // Ví dụ, thay bằng endpoint thực tế
            String apiKey = "c4918efa-f45a-400c-9366-60e8b0ce400a"; // TODO
            String accountId = "ae019f41-d3ea-4478-8a0e-836e499fd3b4"; // TODO
            String orderInfo = "Thanh toan don thuoc";

            // Tạo JSON body gửi lên PayOS
            String jsonInputString = String.format("{\"amount\":%d,\"accountId\":\"%s\",\"orderInfo\":\"%s\"}", amount, accountId, orderInfo);

            URL url = new URL(payosApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey); // Nếu PayOS dùng Bearer token
            conn.setDoOutput(true);

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            Scanner scanner;
            if (code == 200 || code == 201) {
                scanner = new Scanner(conn.getInputStream(), "UTF-8");
            } else {
                scanner = new Scanner(conn.getErrorStream(), "UTF-8");
            }
            StringBuilder responseStr = new StringBuilder();
            while (scanner.hasNextLine()) {
                responseStr.append(scanner.nextLine());
            }
            scanner.close();
            // Parse response để lấy checkoutUrl bằng Gson
            try {
                JsonObject json = JsonParser.parseString(responseStr.toString()).getAsJsonObject();
                String checkoutUrl = null;
                if (json.has("checkoutUrl")) {
                    checkoutUrl = json.get("checkoutUrl").getAsString();
                } else if (json.has("data") && json.getAsJsonObject("data").has("checkoutUrl")) {
                    checkoutUrl = json.getAsJsonObject("data").get("checkoutUrl").getAsString();
                }
                if (checkoutUrl != null) {
                    response.getWriter().write("{\"checkout_url\":\"" + checkoutUrl + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\":\"Không tìm thấy checkoutUrl trong phản hồi PayOS\"}");
                }
            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Lỗi khi phân tích phản hồi PayOS: " + ex.getMessage() + "\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
} 