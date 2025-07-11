package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Pharmacist;
import dao.SysAdminDAO;
import model.accounts.PharmacistAccount;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/pharmacists")
public class PharmacistListServlet extends HttpServlet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        SysAdminDAO dao = new SysAdminDAO();
        List<PharmacistAccount> pharmacistList = dao.getAllPharmacists();

        String json = gson.toJson(pharmacistList);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
