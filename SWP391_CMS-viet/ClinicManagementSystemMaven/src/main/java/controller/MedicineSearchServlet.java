package controller;

import dao.CounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MedicineCounter;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/api/search")
public class MedicineSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usage = request.getParameter("usage");
        CounterDAO medicineDAO = new CounterDAO();

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        if (usage == null || usage.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Thiếu tham số usage\"}");
            return;
        }

        List<MedicineCounter> result = medicineDAO.findByUsage(usage);

        Gson gson = new Gson();
        String json = gson.toJson(result);
        out.print(json);
    }
}
