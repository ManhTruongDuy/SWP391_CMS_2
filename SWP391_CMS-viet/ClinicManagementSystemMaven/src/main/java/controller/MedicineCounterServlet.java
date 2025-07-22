package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CounterDAO;
import dao.PrescriptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MedicineCounter;
import model.MedicineOrderRequest;
import model.Prescription;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@WebServlet("/api/medicineCounter")
public class MedicineCounterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        try {
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(req.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid page or pageSize\"}");
            return;
        }
        CounterDAO dao = new CounterDAO();
        List<MedicineCounter> mec = dao.getMedicineCounter(page, pageSize);
        int totalItems = dao.getMedicineCount();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", mec);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try {
            BufferedReader reader = req.getReader();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.sql.Date.class, (com.google.gson.JsonDeserializer<java.sql.Date>) (json, typeOfT, context) -> {
                        try {
                            return java.sql.Date.valueOf(json.getAsString()); // expects yyyy-MM-dd
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .create();

            MedicineOrderRequest order = gson.fromJson(reader, MedicineOrderRequest.class);

            if (order == null || order.getPrescriptionId() == null || order.getItems() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Dữ liệu không hợp lệ.\"}");
                return;
            }

            PrescriptionDAO preDAO = new PrescriptionDAO();
            CounterDAO dao = new CounterDAO();
            Prescription pre = preDAO.getPrescriptionById(Integer.parseInt(order.getPrescriptionId()));
            if(pre != null) {
                long timestamp = System.currentTimeMillis();
                int random = new Random().nextInt(900) + 100;
                Long orderCode = Long.parseLong(timestamp + "" + random);
                int pending = dao.insertInvoice(pre.getMedicineRecord().getPatient().getId(), pre.getId(),
                        order.getItems().stream()
                                .filter(i -> i.getQuantity() > 0 && i.getPrice() != null)
                                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .doubleValue(),
                        "pending",orderCode);
                int pid = dao.insertPrescriptionInvoice(pending, Integer.parseInt(order.getPrescriptionId()));
                dao.insertMedicines(pid, order.getItems());

                resp.setStatus(HttpServletResponse.SC_OK);
                JSONObject responseJson = new JSONObject();
                responseJson.put("message", "Lưu đơn thuốc thành công.");
                responseJson.put("orderCode", orderCode);
                resp.getWriter().write(responseJson.toString());
            }
            else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Khong tim thay prescription.\"}");
            }
            // Trả phản hồi thành công


        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Lỗi xử lý dữ liệu.\"}");
        }
    }
}
