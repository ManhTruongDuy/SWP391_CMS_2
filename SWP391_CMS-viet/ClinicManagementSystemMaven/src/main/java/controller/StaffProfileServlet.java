package controller;

import com.google.gson.Gson;
import dao.StaffProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.StaffProfileDTO;
import dao.DBContext;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/staff/profile")
public class StaffProfileServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("staffId");

        try (PrintWriter out = response.getWriter()) {
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Missing staffId parameter\"}");
                return;
            }

            int staffId = Integer.parseInt(idParam);

            // ✅ Tạo connection từ DBContext
            StaffProfileDAO dao = new StaffProfileDAO(new DBContext().getConnection());
            StaffProfileDTO profile = dao.getProfileById(staffId);

            if (profile == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Staff not found\"}");
            } else {
                out.print(gson.toJson(profile));
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid staffId format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}
