package controller;

import com.google.gson.Gson;
import dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private final AdminDAO dao = new AdminDAO();
    private final Gson gson = new Gson();

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
                    out.print(gson.toJson(dao.getAllMedicines()));
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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    // Handle POST requests (update price)
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
                    float price = Float.parseFloat(priceParam);

                    boolean success = dao.updateMedicinePriceById(medicineId, price);
                    if (success) {
                        out.print("{\"message\":\"Price updated successfully\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Medicine not found or update failed\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Invalid Medicine ID or price format\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Unknown or missing action\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }


}