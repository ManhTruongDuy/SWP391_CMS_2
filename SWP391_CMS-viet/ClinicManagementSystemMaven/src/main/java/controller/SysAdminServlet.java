package controller;

import com.google.gson.Gson;
import dao.SysAdminDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import model.accounts.*;
import model.StaffProfileDTO;
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

        if ("toggle-status".equalsIgnoreCase(action)) {
            try (var reader = req.getReader(); PrintWriter out = resp.getWriter()) {
                var body = gson.fromJson(reader, java.util.Map.class);

                String role = (String) body.get("role");
                String status = (String) body.get("status");

                if (role == null || status == null || !body.containsKey("id")) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Missing required fields (id, role, status)\"}");
                    return;
                }

                int id = ((Double) body.get("id")).intValue();  // Gson parses numbers as Double
                boolean result = false;

                switch (role) {
                    case "SysAdmin" -> result = dao.updateSysAdminStatus(id, status);
                    case "Doctor" -> result = dao.updateDoctorStatus(id, status);
                    case "BusinessAdmin" -> result = dao.updateBusinessAdminStatus(id, status);
                    case "Pharmacist" -> result = dao.updatePharmacistStatus(id, status);
                    case "Patient" -> result = dao.updatePatientStatus(id, status);
                    default -> {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid role type\"}");
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
        }
        else if ("update".equalsIgnoreCase(action)) {
            try (var reader = req.getReader(); PrintWriter out = resp.getWriter()) {
                var body = gson.fromJson(reader, java.util.Map.class);

                String role = (String) body.get("role");
                if (role == null || !body.containsKey("id")) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Missing required fields (id, role)\"}");
                    return;
                }

                int id = ((Double) body.get("id")).intValue();
                boolean result = false;

                switch (role) {
                    case "Doctor" -> {
                        StaffProfileDTO doctor = new StaffProfileDTO();
                        doctor.setDoctorId(id);
                        doctor.setFullName((String) body.get("name"));
                        doctor.setPhone((String) body.get("phone"));
                        doctor.setEmail((String) body.get("email"));
                        doctor.setDepartment((String) body.get("department"));
                        doctor.setEduLevel((String) body.get("eduLevel"));
                        doctor.setAvailability((String) body.get("availability"));
                        result = dao.updateDoctorInfo(doctor);
                    }
                    case "Pharmacist" -> {
                        PharmacistAccount p = new PharmacistAccount();
                        p.setID(id);
                        p.setName((String) body.get("name"));
                        p.setEmail((String) body.get("email"));
                        p.setMobile((String) body.get("phone"));
                        result = dao.updatePharmacistInfo(p);
                    }
                    case "BusinessAdmin" -> {
                        ManagerAccount m = new ManagerAccount();
                        m.setAdmin_id(id);
                        m.setFullName((String) body.get("name"));
                        m.setPhone((String) body.get("phone"));
                        m.setEmail((String) body.get("email"));
                        m.setDepartment((String) body.get("department"));
                        result = dao.updateManagerInfo(m);
                    }
                    case "SysAdmin" -> {
                        SysAdminAccount s = new SysAdminAccount();
                        s.setAdmin_id(id);
                        s.setFullName((String) body.get("name"));
                        s.setPhone((String) body.get("phone"));
                        s.setEmail((String) body.get("email"));
                        s.setDepartment((String) body.get("department"));
                        result = dao.updateSysAdminInfo(s);
                    }
                    case "Patient" -> {
                        PatientAccount p = new PatientAccount();
                        p.setPatient_id(id);
                        p.setFullName((String) body.get("name"));
                        p.setPhone((String) body.get("phone"));
                        p.setEmail((String) body.get("email"));
                        result = dao.updatePatientInfo(p);
                    }
                    default -> {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Invalid role type\"}");
                        return;
                    }
                }

                if (result) {
                    out.print("{\"message\":\"Cập nhật thông tin thành công\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Cập nhật thất bại\"}");
                }

            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            }
        }



        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or missing action");
        }
    }

}
