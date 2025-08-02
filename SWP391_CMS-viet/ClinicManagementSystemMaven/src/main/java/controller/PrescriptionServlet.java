package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DoctorDAO;
import dao.MedicineRecordDAO;
import dao.PrescriptionDAO;
import dao.PrescriptionItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Prescription;
import model.MedicineRecord;
import model.Doctor;
import model.Patient;
import model.PrescriptionItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@WebServlet("/api/prescriptions")
public class PrescriptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        PrescriptionDAO dao = new PrescriptionDAO();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Prescription pre = dao.getPrescriptionById(id);
                if (pre != null) {
                    resp.getWriter().write(gson.toJson(pre));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Prescription not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid prescription ID\"}");
            }
            return;
        }

        int page = 1;
        int pageSize = 10;

        try {
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(req.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid page or pageSize\"}");
            return;
        }

        String status = req.getParameter("status");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        try {
            if (startDateStr != null) startDate = java.sql.Date.valueOf(startDateStr);
            if (endDateStr != null) endDate = java.sql.Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid date format. Use yyyy-MM-dd\"}");
            return;
        }

        List<Prescription> prescriptions = dao.getAllPrescriptions(page, pageSize, status, startDate, endDate);
        int totalItems = dao.getTotalPrescriptions();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", prescriptions);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        try {
            // Đọc dữ liệu JSON từ request
            BufferedReader reader = req.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonData = jsonBuilder.toString();

            System.out.println("Received JSON data: " + jsonData);

            // Parse JSON thành đối tượng
            JSONObject jsonObject = new JSONObject(jsonData);

            // Lấy thông tin từ JSON
            String patientName = jsonObject.getString("patientName");
            String doctorName = jsonObject.getString("doctorName");
            String prescriptionDateStr = jsonObject.getString("prescriptionDate");
            String status = jsonObject.getString("status");

            System.out.println("Patient: " + patientName + ", Doctor: " + doctorName + ", Date: " + prescriptionDateStr);

            // Lấy danh sách thuốc từ JSON
            org.json.JSONArray itemsArray = jsonObject.getJSONArray("items");
            System.out.println("Number of items: " + itemsArray.length());

            // Chuyển đổi ngày
            Date prescriptionDate = Date.valueOf(prescriptionDateStr);

            // Tìm thông tin bác sĩ
            DoctorDAO doctorDAO = new DoctorDAO();
            Doctor doctor = doctorDAO.getDoctorByName(doctorName);

            if (doctor == null) {
                System.out.println("Doctor not found: " + doctorName);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Không tìm thấy bác sĩ với tên " + doctorName + "\"}");
                return;
            }

            System.out.println("Found doctor: " + doctor.getFullName() + " (ID: " + doctor.getId() + ")");

            // Tạo đối tượng Prescription
            Prescription prescription = new Prescription();
            prescription.setDoctor(doctor);
            prescription.setPrescriptionDate(prescriptionDate);
            prescription.setStatus(status);

            // Tìm hoặc tạo MedicineRecord cho bệnh nhân
            Patient patient = new Patient();
            patient.setFullName(patientName);

            MedicineRecordDAO medicineRecordDAO = new MedicineRecordDAO();
            MedicineRecord medicineRecord = medicineRecordDAO.getMedicineRecordByPatientName(patientName);

            if (medicineRecord != null) {
                System.out.println("Found existing MedicineRecord: " + medicineRecord.getId());
            } else {
                System.out.println("Creating new MedicineRecord for patient: " + patientName);
            }

            // Nếu không tìm thấy MedicineRecord, tạo mới
            if (medicineRecord == null) {
                medicineRecord = medicineRecordDAO.createMedicineRecord(patient);
                if (medicineRecord == null) {
                    System.out.println("Failed to create MedicineRecord for patient: " + patientName);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Không thể tạo hồ sơ thuốc cho bệnh nhân\"}");
                    return;
                } else {
                    System.out.println("Created new MedicineRecord: " + medicineRecord.getId());
                }
            }

            // Gán MedicineRecord vào Prescription
            prescription.setMedicineRecord(medicineRecord);

            // Lưu đơn thuốc vào database
            PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
            int prescriptionId = prescriptionDAO.addPrescription(prescription);

            System.out.println("Prescription saved with ID: " + prescriptionId);

            if (prescriptionId > 0) {
                // Lưu các thuốc trong đơn thuốc
                List<PrescriptionItem> prescriptionItems = new ArrayList<>();
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.getJSONObject(i);
                    PrescriptionItem item = new PrescriptionItem();
                    item.setPrescriptionId(prescriptionId);
                    item.setMedicineId(itemObj.getInt("id"));
                    item.setQuantity(itemObj.getFloat("quantity"));
                    prescriptionItems.add(item);

                    System.out.println("Added item: Medicine ID " + itemObj.getInt("id") + ", Quantity: " + itemObj.getFloat("quantity"));
                }

                // Lưu danh sách thuốc vào database
                PrescriptionItemDAO prescriptionItemDAO = new PrescriptionItemDAO();
                int itemsAdded = prescriptionItemDAO.addPrescriptionItems(prescriptionItems, prescriptionId);

                System.out.println("Items added: " + itemsAdded + " out of " + prescriptionItems.size());

                // Trả về kết quả thành công
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("success", true);
                responseMap.put("message", "Đơn thuốc đã được lưu thành công với " + itemsAdded + " thuốc");
                responseMap.put("prescriptionId", prescriptionId);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(gson.toJson(responseMap));
            } else {
                // Trả về lỗi nếu không lưu được
                System.out.println("Failed to save prescription");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "Không thể lưu đơn thuốc");
                resp.getWriter().write(gson.toJson(errorMap));
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(errorMap));
        }
    }
}