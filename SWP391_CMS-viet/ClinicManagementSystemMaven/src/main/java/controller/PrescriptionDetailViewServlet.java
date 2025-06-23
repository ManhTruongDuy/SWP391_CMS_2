package controller;
//123
import com.google.gson.Gson;
import dao.PrescriptionDetailViewDAO;
import jakarta.servlet.ServletException;
import model.PrescriptionDetailView;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/prescription-detail-views")
public class PrescriptionDetailViewServlet extends HttpServlet {
    private final PrescriptionDetailViewDAO dao = new PrescriptionDetailViewDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String presIdStr = req.getParameter("prescriptionId");
        Integer presId = (presIdStr != null && !presIdStr.isEmpty()) ? Integer.parseInt(presIdStr) : null;
        String patientName = req.getParameter("patientName");
        String sort = req.getParameter("sort"); // "asc" hoặc "desc"
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<PrescriptionDetailView> list = dao.searchPrescriptionDetails(presId, patientName, sort);
        resp.getWriter().write(gson.toJson(list));
    }
}