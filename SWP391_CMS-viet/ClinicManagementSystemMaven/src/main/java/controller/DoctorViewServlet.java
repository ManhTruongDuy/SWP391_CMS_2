package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DoctorViewDAO;
import dao.PrescriptionChatDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.PrescriptionView;
import model.PrescriptionChat;
import model.AccountStaff;
import model.StaffProfileDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@WebServlet("/api/doctorview/*")
public class DoctorViewServlet extends HttpServlet {

    private final DoctorViewDAO doctorDAO = new DoctorViewDAO();
    private final PrescriptionChatDAO chatDAO = new PrescriptionChatDAO();
    private final Gson gson;

    public DoctorViewServlet() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (com.google.gson.JsonSerializer<Date>) (src, typeOfSrc, context) ->
                        src == null ? null : new com.google.gson.JsonPrimitive(src.toString()))
                .registerTypeAdapter(Date.class, (com.google.gson.JsonDeserializer<Date>) (json, typeOfT, context) ->
                        json == null ? null : Date.valueOf(json.getAsString()))
                .create();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        HttpSession session = req.getSession(false);
        AccountStaff doctor = (session != null) ? (AccountStaff) session.getAttribute("account") : null;

        if (doctor == null || !"Doctor".equalsIgnoreCase(doctor.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"error\":\"Unauthorized. Please login as doctor.\"}");
                out.flush();
            }
            return;
        }

        try (PrintWriter out = resp.getWriter()) {
            String body = req.getReader().lines().reduce("", String::concat);
            PrescriptionChat chat = gson.fromJson(body, PrescriptionChat.class);

            if (chat == null || chat.getMessage() == null || chat.getPrescriptionId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid request body\"}");
            } else {
                chat.setSenderType("doctor");
                chat.setSenderId(doctor.getAccount_staff_id());
                chat.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
                boolean success = chatDAO.insertChat(chat);
                if (success) {
                    out.print("{\"success\":true}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Failed to save message\"}");
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"error\":\"Internal server error\"}");
                out.flush();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        HttpSession session = req.getSession(false);
        AccountStaff doctor = (session != null) ? (AccountStaff) session.getAttribute("account") : null;

        if (doctor == null || !"Doctor".equalsIgnoreCase(doctor.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"error\":\"Unauthorized. Please login as doctor.\"}");
                out.flush();
            }
            return;
        }

        String pathInfo = req.getPathInfo();

        try (PrintWriter out = resp.getWriter()) {
            if ("/current".equals(pathInfo)) {
                out.print(gson.toJson(doctor));
            }

            else if (pathInfo != null && pathInfo.matches("/\\d+")) {
                int prescriptionId = Integer.parseInt(pathInfo.substring(1));
                PrescriptionView pv = doctorDAO.getPrescriptionDetailsById(prescriptionId, doctor.getAccount_staff_id());
                if (pv == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Prescription not found\"}");
                } else {
                    out.print(gson.toJson(pv));
                }

            } else {
                String action = req.getParameter("action");

                if ("doctorInfo".equals(action)) {
                    StaffProfileDTO profile = doctorDAO.getStaffProfileDTOByAccountStaffId(doctor.getAccount_staff_id());
                    if (profile == null) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Doctor profile not found\"}");
                    } else {
                        out.print(gson.toJson(profile));
                    }
                }


                else if ("history".equals(action)) {
                    String fromDate = req.getParameter("fromDate");
                    String toDate = req.getParameter("toDate");

                    List<PrescriptionView> list;
                    if (fromDate != null && toDate != null && !fromDate.isEmpty() && !toDate.isEmpty()) {
                        list = doctorDAO.getPrescriptionsByDateRange(doctor.getAccount_staff_id(), fromDate, toDate);
                    } else {
                        list = doctorDAO.getPrescriptionsByDoctorId(doctor.getAccount_staff_id());
                    }
                    out.print(gson.toJson(list));

                } else if ("chat".equals(action)) {
                    String presIdRaw = req.getParameter("prescriptionId");
                    if (presIdRaw == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Missing prescriptionId parameter\"}");
                    } else {
                        try {
                            int prescriptionId = Integer.parseInt(presIdRaw);
                            List<PrescriptionChat> chats = chatDAO.getChatsByPrescriptionId(prescriptionId);
                            out.print(gson.toJson(chats));
                        } catch (NumberFormatException ex) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.print("{\"error\":\"Invalid prescriptionId format\"}");
                        }
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Unknown or missing action\"}");
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"error\":\"Internal server error occurred\"}");
                out.flush();
            }
        }
    }
}
