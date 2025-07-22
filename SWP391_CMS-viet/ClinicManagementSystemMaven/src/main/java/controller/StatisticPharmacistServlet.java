package controller;

import com.google.gson.Gson;
import dao.PrescriptionDetailViewDAO;
import model.MedicineStatistic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "StatisticPharmacistServlet", urlPatterns = {"/api/pharmacist/statistics"})
public class StatisticPharmacistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String dateStr = request.getParameter("date");
        String byMonthStr = request.getParameter("byMonth");
        Date date = null;
        boolean byMonth = false;
        if (dateStr != null && !dateStr.isEmpty()) {
            date = Date.valueOf(dateStr);
        }
        if (byMonthStr != null) {
            byMonth = Boolean.parseBoolean(byMonthStr);
        }
        PrescriptionDetailViewDAO dao = new PrescriptionDetailViewDAO();
        List<MedicineStatistic> list = dao.getMedicineStatistics(date, byMonth);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
} 