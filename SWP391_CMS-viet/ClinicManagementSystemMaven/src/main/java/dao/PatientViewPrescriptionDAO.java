package dao;

import model.MedicationView;
import model.PrescriptionView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientViewPrescriptionDAO {
    private final Connection conn;

    public PatientViewPrescriptionDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // 1. Lấy danh sách đơn thuốc đã mua của bệnh nhân
    public List<PrescriptionView> getPurchasedPrescriptionsByPatientId(int patientId) {
        List<PrescriptionView> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                "p.prescription_date, p.status, ISNULL(SUM(i.total_amount), 0) AS total_amount " +
                "FROM Prescription p " +
                "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id " +
                "WHERE pt.patient_id = ? AND p.status = 'Dispensed' " +
                "GROUP BY p.prescription_id, pt.full_name, d.full_name, p.prescription_date, p.status " +
                "ORDER BY p.prescription_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionView pv = new PrescriptionView();
                    pv.setPrescriptionId(rs.getInt("prescription_id"));
                    pv.setPatientName(rs.getString("patient_name"));
                    pv.setDoctorName(rs.getString("doctor_name"));
                    pv.setPrescriptionDate(rs.getDate("prescription_date"));
                    pv.setStatus(rs.getString("status"));
                    pv.setTotalAmount(rs.getDouble("total_amount"));
                    list.add(pv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc ghi log nếu cần
        }
        return list;
    }

    // 2. Lấy chi tiết 1 đơn thuốc (bao gồm danh sách thuốc)
    public PrescriptionView getPrescriptionDetailsById(int prescriptionId, int patientId) throws SQLException {
        String sql = "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                "p.prescription_date, p.status, ISNULL(i.total_amount, 0) AS total_amount " +
                "FROM Prescription p " +
                "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id " +
                "WHERE p.prescription_id = ? AND pt.patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            ps.setInt(2, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PrescriptionView pv = new PrescriptionView();
                    pv.setPrescriptionId(rs.getInt("prescription_id"));
                    pv.setPatientName(rs.getString("patient_name"));
                    pv.setDoctorName(rs.getString("doctor_name"));
                    pv.setPrescriptionDate(rs.getDate("prescription_date"));
                    pv.setStatus(rs.getString("status"));
                    pv.setTotalAmount(rs.getDouble("total_amount"));

                    // Lấy danh sách thuốc trong đơn
                    List<MedicationView> meds = getMedicationsByPrescriptionId(prescriptionId);
                    pv.setMedications(meds);

                    return pv;
                }
            }
        }
        return null;
    }

    // 3. Lấy danh sách thuốc của đơn thuốc
    public List<MedicationView> getMedicationsByPrescriptionId(int prescriptionId) {
        List<MedicationView> meds = new ArrayList<>();
        String sql = "SELECT m.name, md.dosage, md.quantity, m.usage AS instructions " +
                "FROM PrescriptionInvoice pi " +
                "JOIN Medicines md ON pi.prescription_invoice_id = md.prescription_invoice_id " +
                "JOIN Medicine m ON md.medicine_id = m.medicine_id " +
                "WHERE pi.prescription_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicationView mv = new MedicationView();
                    mv.setName(rs.getString("name"));
                    mv.setDosage(rs.getString("dosage"));
                    mv.setQuantity(rs.getInt("quantity"));
                    mv.setInstructions(rs.getString("instructions"));
                    meds.add(mv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc ghi log nếu cần
        }
        return meds;
    }
    public List<PrescriptionView> getPurchasedPrescriptionsByDateRange(int patientId, String fromDate, String toDate) {
        List<PrescriptionView> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                "p.prescription_date, p.status, ISNULL(i.total_amount, 0) AS total_amount " +
                "FROM Prescription p " +
                "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id " +
                "WHERE pt.patient_id = ? AND p.status = 'Dispensed' " +
                "AND p.prescription_date BETWEEN ? AND ? " +
                "ORDER BY p.prescription_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PrescriptionView pv = new PrescriptionView();
                pv.setPrescriptionId(rs.getInt("prescription_id"));
                pv.setPatientName(rs.getString("patient_name"));
                pv.setDoctorName(rs.getString("doctor_name"));
                pv.setPrescriptionDate(rs.getDate("prescription_date"));
                pv.setStatus(rs.getString("status"));
                pv.setTotalAmount(rs.getDouble("total_amount"));
                list.add(pv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }




}
