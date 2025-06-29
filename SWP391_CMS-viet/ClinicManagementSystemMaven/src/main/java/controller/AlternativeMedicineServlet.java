package controller;

import com.google.gson.Gson;
import dao.MedicineDAO;
import model.Medicine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AlternativeMedicineServlet", urlPatterns = {"/api/pharmacist/alternatives", "/api/pharmacist/medicine-by-name"})
public class AlternativeMedicineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getServletPath();
        MedicineDAO dao = new MedicineDAO();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        if ("/api/pharmacist/alternatives".equals(path)) {
            String ingredient = request.getParameter("ingredient");
            List<Medicine> list = dao.findMedicinesByIngredient(ingredient);
            out.print(gson.toJson(list));
        } else if ("/api/pharmacist/medicine-by-name".equals(path)) {
            String name = request.getParameter("name");
            List<Medicine> list = dao.findMedicinesByName(name);
            out.print(gson.toJson(list));
        }
        out.flush();
    }
} 