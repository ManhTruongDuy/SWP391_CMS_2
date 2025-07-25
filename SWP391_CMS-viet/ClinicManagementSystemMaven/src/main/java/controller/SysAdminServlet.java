package controller;

import com.google.gson.Gson;
import dao.SysAdminDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import model.AccountPharmacist;
import model.accounts.*;

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
                case "wManager"    -> out.print(gson.toJson(dao.getAllWarehouseManager()));
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
                case "wManagerid"    -> {
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Warehouse manager ID is missing\"}");
                        return;
                    }
                    try {
                        int wManagerID = Integer.parseInt(idParam);
                        Object wMana = dao.getWarehouseManagerById(wManagerID);
                        if (wMana == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Warehouse manager not found\"}");
                        } else {
                            out.print(gson.toJson(wMana));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid Warehouse manager ID format\"}");
                    }
                }
                case "sysadminname" -> {
                    String nameParam = request.getParameter("name");
                    if (nameParam == null || nameParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"System admin name is missing\"}");
                        return;
                    }
                    var list = dao.getSysAdminsByName(nameParam);
                    out.print(gson.toJson(list));
                }
                case "doctorname" -> {
                    String nameParam = request.getParameter("name");
                    if (nameParam == null || nameParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Doctor name is missing\"}");
                        return;
                    }
                    var list = dao.getDoctorsByName(nameParam);
                    out.print(gson.toJson(list));
                }

                case "managername" -> {
                    String nameParam = request.getParameter("name");
                    if (nameParam == null || nameParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Manager name is missing\"}");
                        return;
                    }
                    var list = dao.getManagersByName(nameParam);
                    out.print(gson.toJson(list));
                }

                case "patientname" -> {
                    String nameParam = request.getParameter("name");
                    if (nameParam == null || nameParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Patient name is missing\"}");
                        return;
                    }
                    var list = dao.getPatientsByName(nameParam);
                    out.print(gson.toJson(list));
                }

                case "pharmacistname" -> {
                    String nameParam = request.getParameter("name");
                    if (nameParam == null || nameParam.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Pharmacist name is missing\"}");
                        return;
                    }
                    var list = dao.getPharmacistsByName(nameParam);
                    out.print(gson.toJson(list));
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        switch (action != null ? action.toLowerCase() : "") {
            case "createaccount":
                try (PrintWriter out = resp.getWriter()) {
                    String username = req.getParameter("username");
                    String password = req.getParameter("password");
                    String role = req.getParameter("role");
                    String email = req.getParameter("email");
                    String img = req.getParameter("img");
                    String status = req.getParameter("status");

                    if (username == null || password == null || role == null || email == null || status == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Missing required fields\"}");
                        return;
                    }

                    // Create StaffAccount object
                    StaffAccount sa = new StaffAccount();
                    sa.setUsername(username);
                    sa.setPassword(password); // Hash in production
                    sa.setRole(role);
                    sa.setEmail(email);
                    sa.setImg(img != null ? img : "");
                    sa.setStatus(status);

                    // Insert into AccountStaff
                    boolean result = dao.addAccountStaff(sa);
                    if (result) {
                        out.print("{\"message\":\"Tài khoản được tạo thành công\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        out.print("{\"error\":\"Không thể tạo tài khoản\"}");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
                break;
            case "createpharmacist":
                try (PrintWriter out = resp.getWriter()) {
                    String username = req.getParameter("username");
                    String password = req.getParameter("password");
                    String email = req.getParameter("email");
                    String img = req.getParameter("img");
                    String status = req.getParameter("status");

                    if (username == null || password == null || email == null || status == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Missing required fields\"}");
                        return;
                    }

                    AccountPharmacist ap = new AccountPharmacist();
                    ap.setUsername(username);
                    ap.setPassword(password);
                    ap.setEmail(email);
                    ap.setImg(img != null ? img : "");
                    ap.setStatus(status);

                    boolean result = dao.addAccountPharmacist(ap);
                    if (result) {
                        out.print("{\"message\":\"Tài khoản được tạo thành công\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        out.print("{\"error\":\"Không thể tạo tài khoản\"}");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
                break;
            case "toggle-status":
                try (var reader = req.getReader(); PrintWriter out = resp.getWriter()) {
                    var body = gson.fromJson(reader, java.util.Map.class);
                    String role = (String) body.get("role");
                    String status = (String) body.get("status");

                    if (role == null || status == null || !body.containsKey("id")) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Missing required fields (id, role, status)\"}");
                        return;
                    }

                    int id = ((Double) body.get("id")).intValue(); // Gson parses numbers as Double
                    boolean result = false;

                    switch (role) {
                        case "SysAdmin" -> result = dao.updateSysAdminStatus(id, status);
                        case "Doctor" -> result = dao.updateDoctorStatus(id, status);
                        case "BusinessAdmin" -> result = dao.updateBusinessAdminStatus(id, status);
                        case "Pharmacist" -> result = dao.updatePharmacistStatus(id, status);
                        case "Patient" -> result = dao.updatePatientStatus(id, status);
                        default -> {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.print("{\"error\":\"Vai trò không hợp lệ\"}");
                            return;
                        }
                    }
                    if (result) {
                        out.print("{\"message\":\"Cập nhật trạng thái thành công\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        out.print("{\"error\":\"Không thể cập nhật trạng thái\"}");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing action");
        }
    }

}
