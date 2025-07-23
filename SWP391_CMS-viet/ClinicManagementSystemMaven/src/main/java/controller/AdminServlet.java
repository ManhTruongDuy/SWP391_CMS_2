package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MedicineAdmin;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private final AdminDAO dao = new AdminDAO();
    private final Gson gson;

    public AdminServlet() {
        // Tạo Gson với LocalDateAdapter
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }
    // Handle CORS preflight requests
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // Handle GET requests (fetch all medicines or a specific medicine by ID)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received GET request: " + request.getRequestURI() + "?" + request.getQueryString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            switch (action) {
                case "medicines":
                    List<MedicineAdmin> medicines = dao.getAllMedicines();
                    System.out.println("Medicines retrieved: " + (medicines != null ? medicines.size() : "null"));
                    String jsonResponse = gson.toJson(medicines);
                    System.out.println("JSON response: " + jsonResponse);
                    out.print(jsonResponse);
                    out.flush(); // Đảm bảo dữ liệu được gửi
                    break;
                case "medicineid":
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Medicine ID is missing\"}");
                        return;
                    }
                    try {
                        int medicineId = Integer.parseInt(idParam);
                        Object medicine = dao.getMedicineById(medicineId);
                        System.out.println("Medicine found: " + (medicine != null));
                        if (medicine == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Medicine not found\"}");
                        } else {
                            out.print(gson.toJson(medicine));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Medicine ID format\"}");
                    }
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Unknown or missing action\"}");
                    break;
            }
            System.out.println("Response sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            if ("updatePrice".equals(action)) {
                String idParam = request.getParameter("id");
                String priceParam = request.getParameter("price");

                if (idParam == null || idParam.trim().isEmpty() || priceParam == null || priceParam.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Medicine ID or price is missing\"}");
                    return;
                }

                try {
                    int medicineId = Integer.parseInt(idParam);
                    float newPrice = Float.parseFloat(priceParam);

                    if (newPrice <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Price must be greater than 0\"}");
                        return;
                    }

                    boolean updated = dao.updateMedicinePriceById(medicineId, newPrice);
                    if (updated) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print("{\"message\":\"Price updated successfully\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Medicine not found\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Invalid ID or price format\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Unknown action\"}");
            }
            System.out.println("Response sent successfully");
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }


}