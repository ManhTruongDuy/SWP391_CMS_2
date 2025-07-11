package controller;

import com.google.gson.Gson;
import dao.SysAdminDAO;
import model.accounts.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/system-admin")
public class SysAdminServlet extends HttpServlet {
    private final SysAdminDAO dao = new SysAdminDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            switch (action) {
                case "pharmacists" -> out.print(gson.toJson(dao.getAllPharmacists()));
                case "doctors"     -> out.print(gson.toJson(dao.getAllDoctors()));
                case "managers"    -> out.print(gson.toJson(dao.getAllManagers()));
                case "sys-admins"  -> out.print(gson.toJson(dao.getAllSysAdmins()));
                case "patients"    -> out.print(gson.toJson(dao.getAllPatients()));
                default -> {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Unknown or missing action\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST not supported.");
    }
}
