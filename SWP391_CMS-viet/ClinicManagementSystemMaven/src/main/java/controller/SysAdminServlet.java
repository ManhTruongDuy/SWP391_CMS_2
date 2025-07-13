package controller;

import com.google.gson.Gson;
import dao.SysAdminDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

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
                case "doctorid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Doctor ID is missing\"}");
                        return;
                    }
                    try {
                        int doctorId = Integer.parseInt(idParam);
                        Object doctor = dao.getDoctorById(doctorId);
                        if (doctor == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Doctor not found\"}");
                        } else {
                            out.print(gson.toJson(doctor));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Doctor ID format\"}");
                    }
                }
                case "managerid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Doctor ID is missing\"}");
                        return;
                    }
                    try {
                        int managerId = Integer.parseInt(idParam);
                        Object manager = dao.getManagerById(managerId);
                        if (manager == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Manager not found\"}");
                        } else {
                            out.print(gson.toJson(manager));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Manager ID format\"}");
                    }
                }
                case "patientid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Patient ID is missing\"}");
                        return;
                    }
                    try {
                        int patientId = Integer.parseInt(idParam);
                        Object patient = dao.getPatientById(patientId);
                        if (patient == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Patient not found\"}");
                        } else {
                            out.print(gson.toJson(patient));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Manager ID format\"}");
                    }
                }
                case "pharmacistid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Pharmacist ID is missing\"}");
                        return;
                    }
                    try {
                        int pharmacistId = Integer.parseInt(idParam);
                        Object pharmacist = dao.getPharmacistById(pharmacistId);
                        if (pharmacist == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Pharmacist not found\"}");
                        } else {
                            out.print(gson.toJson(pharmacist));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Pharmacist ID format\"}");
                    }
                }
                case "sysadminid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"System admin ID is missing\"}");
                        return;
                    }
                    try {
                        int sysadminId = Integer.parseInt(idParam);
                        Object sysadmin = dao.getSysAdminById(sysadminId);
                        if (sysadmin == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"System admin not found\"}");
                        } else {
                            out.print(gson.toJson(sysadmin));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid System admin ID format\"}");
                    }
                }
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
