package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PatientDAO;
import dao.PatientViewPrescriptionDAO;
import dao.PrescriptionChatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AccountPatient;
import model.Patient;
import model.PrescriptionView;
import model.PrescriptionChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@WebServlet("/api/patientview/*")
public class PatientViewServlet extends HttpServlet {
    private final PatientViewPrescriptionDAO prescriptionDAO = new PatientViewPrescriptionDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final PrescriptionChatDAO chatDAO = new PrescriptionChatDAO();
    private final Gson gson;

    public PatientViewServlet() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (com.google.gson.JsonSerializer<Date>) (src, typeOfSrc, context) ->
                        src == null ? null : new com.google.gson.JsonPrimitive(src.toString()))
                .registerTypeAdapter(Date.class, (com.google.gson.JsonDeserializer<Date>) (json, typeOfT, context) ->
                        json == null ? null : Date.valueOf(json.getAsString()))
                .create();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received POST request: " + request.getRequestURI());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null || !"patient".equals(session.getAttribute("userType"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Unauthorized access. Please log in as a patient.\"}");
                out.flush();
            }
            return;
        }
        AccountPatient account = (AccountPatient) session.getAttribute("account");

        try (PrintWriter out = response.getWriter()) {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            System.out.println("POST body: " + body);

            PrescriptionChat chat = gson.fromJson(body, PrescriptionChat.class);

            if (chat == null || chat.getMessage() == null || chat.getPrescriptionId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid request body\"}");
            } else {
                chat.setSenderType("patient");
                chat.setSenderId(account.getAccount_patient_id());
                chat.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

                boolean success = chatDAO.insertChat(chat);
                if (success) {
                    out.print("{\"success\": true}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to save message\"}");
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Internal server error\"}");
                out.flush();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received GET request: " + request.getRequestURI() + "?" + request.getQueryString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null || !"patient".equals(session.getAttribute("userType"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Unauthorized access. Please log in as a patient.\"}");
                out.flush();
            }
            return;
        }

        AccountPatient account = (AccountPatient) session.getAttribute("account");
        String pathInfo = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {
            if ("/current".equals(pathInfo)) {
                Patient patient = patientDAO.getPatientByAccountId(account.getAccount_patient_id());
                if (patient != null) {
                    out.print(gson.toJson(patient));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Patient not found\"}");
                }

            } else if (pathInfo != null && pathInfo.matches("/\\d+")) {
                int prescriptionId = Integer.parseInt(pathInfo.substring(1));
                PrescriptionView prescription = prescriptionDAO.getPrescriptionDetailsById(prescriptionId, account.getAccount_patient_id());
                if (prescription == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Prescription not found\"}");
                } else {
                    out.print(gson.toJson(prescription));
                }

            } else {
                String action = request.getParameter("action");
                if ("purchased".equals(action)) {
                    // ✅ Lấy param đúng với JS: fromDate & toDate
                    String fromDate = request.getParameter("fromDate");
                    String toDate = request.getParameter("toDate");

                    List<PrescriptionView> prescriptions;
                    if (fromDate != null && toDate != null && !fromDate.isEmpty() && !toDate.isEmpty()) {
                        prescriptions = prescriptionDAO.getPurchasedPrescriptionsByDateRange(
                                account.getAccount_patient_id(), fromDate, toDate);
                    } else {
                        prescriptions = prescriptionDAO.getPurchasedPrescriptionsByPatientId(
                                account.getAccount_patient_id());
                    }

                    out.print(gson.toJson(prescriptions));

                } else if ("chat".equals(action)) {
                    String presIdRaw = request.getParameter("prescriptionId");
                    if (presIdRaw == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\":\"Missing prescriptionId parameter\"}");
                    } else {
                        try {
                            int prescriptionId = Integer.parseInt(presIdRaw);
                            List<PrescriptionChat> chats = chatDAO.getChatsByPrescriptionId(prescriptionId);
                            out.print(gson.toJson(chats));
                        } catch (NumberFormatException ex) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.print("{\"error\":\"Invalid prescriptionId format\"}");
                        }
                    }

                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Unknown or missing action\"}");
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Internal server error occurred.\"}");
                out.flush();
            }
        }
    }
}
