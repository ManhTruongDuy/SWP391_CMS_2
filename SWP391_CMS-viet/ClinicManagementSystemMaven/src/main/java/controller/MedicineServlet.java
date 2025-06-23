package controller;
//123
import dao.MedicineDAO;
import model.Medicine;
import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/api/medicines/*")
public class MedicineServlet extends HttpServlet {
    private MedicineDAO dao;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
                    return json.isJsonNull() ? null : LocalDate.parse(json.getAsString());
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                    return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
                }
            })
            .create();

    @Override
    public void init() {
        dao = new MedicineDAO();
    }

    // Đáp ứng CORS (nếu cần)
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // GET: lấy tất cả thuốc hoặc chi tiết 1 thuốc
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("keyword");
        String categoryIdStr = req.getParameter("categoryId");
        String limitStr = req.getParameter("limit");
        String offsetStr = req.getParameter("offset");

        Integer categoryId = (categoryIdStr != null && !categoryIdStr.isEmpty()) ? Integer.parseInt(categoryIdStr) : null;
        int limit = (limitStr != null && !limitStr.isEmpty()) ? Integer.parseInt(limitStr) : 20; // default 20
        int offset = (offsetStr != null && !offsetStr.isEmpty()) ? Integer.parseInt(offsetStr) : 0; // default 0

        MedicineDAO dao = new MedicineDAO();
        List<Medicine> medicines;

        // Nếu có filter
        if ((keyword != null && !keyword.trim().isEmpty()) || categoryId != null) {
            medicines = dao.searchMedicines(keyword, categoryId, limit, offset);
        } else {
            // LẤY CÓ PHÂN TRANG, KHÔNG DÙNG getAllMedicines() NỮA!
            medicines = dao.getAllMedicines(limit, offset);
        }

        // JSON trả về FE
        String json = gson.toJson(medicines);
        resp.getWriter().write(json);
    }
    // POST: thêm thuốc mới
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        try {
            Medicine med = gson.fromJson(sb.toString(), Medicine.class);
            boolean ok = dao.addMedicine(med);
            if (ok) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(med));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Insert failed\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid body or data: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    // PUT: cập nhật thuốc (theo ID trên url, truyền JSON body)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            try {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = req.getReader()) {
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                }
                Medicine med = gson.fromJson(sb.toString(), Medicine.class);
                med.setMedicine_id(id);
                boolean ok = dao.updateMedicine(med);
                if (ok) {
                    out.print(gson.toJson(med));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Medicine not found or update failed\"}");
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid ID or update failed\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid request\"}");
        }
        out.flush();
    }
    // DELETE: xóa thuốc
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter out = resp.getWriter();
        if (pathInfo != null && pathInfo.split("/").length == 2) {
            try {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
                boolean ok = dao.deleteMedicine(id);
                if (ok) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Medicine not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid ID\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid request\"}");
        }
        out.flush();
    }
}