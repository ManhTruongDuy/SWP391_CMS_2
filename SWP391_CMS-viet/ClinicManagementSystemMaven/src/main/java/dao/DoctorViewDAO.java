package dao;

import model.PrescriptionView;
import model.MedicationView;
import model.StaffProfileDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorViewDAO {
    private final Connection conn;

    public DoctorViewDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public PrescriptionView getPrescriptionDetailsById(int prescriptionId, int accountStaffId) {
        String sql = """
            SELECT p.prescription_id, p.prescription_date, p.status,
                   pt.full_name AS patient_name,
                   d.full_name AS doctor_name
            FROM Prescription p
            JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id
            JOIN Patient pt ON mr.patient_id = pt.patient_id
            JOIN Doctor d ON p.doctor_id = d.doctor_id
            JOIN AccountStaff a ON d.account_staff_id = a.account_staff_id
            WHERE p.prescription_id = ? AND a.account_staff_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            ps.setInt(2, accountStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PrescriptionView pv = new PrescriptionView();
                    pv.setPrescriptionId(rs.getInt("prescription_id"));
                    pv.setPrescriptionDate(rs.getDate("prescription_date"));
                    pv.setStatus(rs.getString("status"));
                    pv.setPatientName(rs.getString("patient_name"));
                    pv.setDoctorName(rs.getString("doctor_name"));

                    // Thêm danh sách thuốc
                    pv.setMedications(getMedicationsByPrescriptionId(prescriptionId));
                    return pv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PrescriptionView> getPrescriptionsByDoctorId(int accountStaffId) {
        String sql = """
            SELECT p.prescription_id, p.prescription_date, p.status,
                   pt.full_name AS patient_name,
                   d.full_name AS doctor_name
            FROM Prescription p
            JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id
            JOIN Patient pt ON mr.patient_id = pt.patient_id
            JOIN Doctor d ON p.doctor_id = d.doctor_id
            JOIN AccountStaff a ON d.account_staff_id = a.account_staff_id
            WHERE a.account_staff_id = ?
            ORDER BY p.prescription_date DESC
        """;

        List<PrescriptionView> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountStaffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionView pv = new PrescriptionView();
                    pv.setPrescriptionId(rs.getInt("prescription_id"));
                    pv.setPrescriptionDate(rs.getDate("prescription_date"));
                    pv.setStatus(rs.getString("status"));
                    pv.setPatientName(rs.getString("patient_name"));
                    pv.setDoctorName(rs.getString("doctor_name"));
                    list.add(pv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<PrescriptionView> getPrescriptionsByDateRange(int accountStaffId, String fromDate, String toDate) {
        String sql = """
            SELECT p.prescription_id, p.prescription_date, p.status,
                   pt.full_name AS patient_name,
                   d.full_name AS doctor_name
            FROM Prescription p
            JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id
            JOIN Patient pt ON mr.patient_id = pt.patient_id
            JOIN Doctor d ON p.doctor_id = d.doctor_id
            JOIN AccountStaff a ON d.account_staff_id = a.account_staff_id
            WHERE a.account_staff_id = ?
              AND p.prescription_date BETWEEN ? AND ?
            ORDER BY p.prescription_date DESC
        """;

        List<PrescriptionView> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountStaffId);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionView pv = new PrescriptionView();
                    pv.setPrescriptionId(rs.getInt("prescription_id"));
                    pv.setPrescriptionDate(rs.getDate("prescription_date"));
                    pv.setStatus(rs.getString("status"));
                    pv.setPatientName(rs.getString("patient_name"));
                    pv.setDoctorName(rs.getString("doctor_name"));
                    list.add(pv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MedicationView> getMedicationsByPrescriptionId(int prescriptionId) {
        List<MedicationView> list = new ArrayList<>();

        String sql = """
            SELECT m.name AS medicine_name, me.quantity, me.dosage
            FROM Prescription p
            JOIN PrescriptionInvoice pi ON pi.prescription_id = p.prescription_id
            JOIN Medicines me ON me.prescription_invoice_id = pi.prescription_invoice_id
            JOIN Medicine m ON me.medicine_id = m.medicine_id
            WHERE p.prescription_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicationView med = new MedicationView();
                    med.setName(rs.getString("medicine_name"));
                    med.setQuantity(rs.getInt("quantity"));
                    med.setDosage(rs.getString("dosage"));
                    list.add(med);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public StaffProfileDTO getStaffProfileDTOByAccountStaffId(int accountStaffId) {
        StaffProfileDTO dto = null;
        String sql = """
        SELECT a.account_staff_id, a.username, a.password, a.role, a.email, a.img, a.status,
               d.doctor_id, d.full_name, d.department, d.phone, d.eduLevel
        FROM AccountStaff a
        LEFT JOIN Doctor d ON a.account_staff_id = d.account_staff_id
        WHERE a.account_staff_id = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountStaffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new StaffProfileDTO();
                dto.setAccountStaffId(rs.getInt("account_staff_id"));
                dto.setUsername(rs.getString("username"));
                dto.setPassword(rs.getString("password"));
                dto.setRole(rs.getString("role"));
                dto.setEmail(rs.getString("email"));
                dto.setImg(rs.getString("img"));
                dto.setStatus(rs.getString("status"));

                dto.setDoctorId((Integer) rs.getObject("doctor_id")); // nullable
                dto.setFullName(rs.getString("full_name"));
                dto.setDepartment(rs.getString("department"));
                dto.setPhone(rs.getString("phone"));
                dto.setEduLevel(rs.getString("eduLevel"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

}
