package controller;

import com.google.gson.Gson;
import dao.PharmacistDAO;
import dao.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.AccountPharmacist;
import model.PharmacistDTO;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/pharmacist/profile")
public class PharmacistProfileServlet extends HttpServlet {

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

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            AccountPharmacist pharmacist = (session != null) ?
                    (AccountPharmacist) session.getAttribute("account") : null;

            if (pharmacist == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Not logged in\"}");
                return;
            }

            int accountPharmacistId = pharmacist.getAccount_pharmacist_id();

            PharmacistDAO dao = new PharmacistDAO(new DBContext().getConnection());
            PharmacistDTO profile = dao.getPharmacistProfileByAccountId(accountPharmacistId);

            if (profile == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Pharmacist profile not found\"}");
            } else {
                out.print(gson.toJson(profile));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}
