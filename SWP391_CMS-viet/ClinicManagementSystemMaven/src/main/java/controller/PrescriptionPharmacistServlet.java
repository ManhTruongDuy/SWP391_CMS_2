package controller;

import com.google.gson.Gson;
import dao.PrescriptionViewDAO;
import model.PrescriptionView;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PrescriptionPharmacistServlet", urlPatterns = {"/api/pharmacist/prescriptions"})
public class PrescriptionPharmacistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrescriptionViewDAO dao = new PrescriptionViewDAO();
        // Lấy danh sách đơn thuốc chờ phát
        List<PrescriptionView> list = dao.searchPrescriptionViews(null, null, null, "chờ phát", "desc");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
} 