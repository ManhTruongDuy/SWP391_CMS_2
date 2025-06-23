package dao;

import model.PrescriptionView;
//123
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionViewDAO {
    private final Connection conn;

    public PrescriptionViewDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // Lấy toàn bộ đơn thuốc
    public List<PrescriptionView> getAllPrescriptionViews() {
        List<PrescriptionView> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                "p.prescription_date, p.status, ISNULL(i.total_amount, 0) AS total_amount " +
                "FROM Prescription p " +
                "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id " +
                "ORDER BY p.prescription_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
            e.printStackTrace();
        }
        return list;
    }

    // Lấy đơn thuốc theo bệnh nhân
    public List<PrescriptionView> getPrescriptionViewsByPatientId(int patientId) {
        List<PrescriptionView> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                "p.prescription_date, p.status, ISNULL(i.total_amount, 0) AS total_amount " +
                "FROM Prescription p " +
                "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id " +
                "WHERE pt.patient_id = ? " +
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
            e.printStackTrace();
        }
        return list;
    }
    public List<PrescriptionView> searchPrescriptionViews(
            Integer prescriptionId, String patientName, String doctorName, String status, String sort
    ) {
        List<PrescriptionView> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.prescription_id, pt.full_name AS patient_name, d.full_name AS doctor_name, " +
                        "p.prescription_date, p.status, ISNULL(i.total_amount, 0) AS total_amount " +
                        "FROM Prescription p " +
                        "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                        "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                        "INNER JOIN Doctor d ON p.doctor_id = d.doctor_id " +
                        "LEFT JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                        "LEFT JOIN Invoice i ON pi.invoice_id = i.invoice_id WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (prescriptionId != null) {
            sql.append("AND p.prescription_id = ? ");
            params.add(prescriptionId);
        }
        if (patientName != null && !patientName.trim().isEmpty()) {
            sql.append("AND pt.full_name LIKE ? ");
            params.add("%" + patientName.trim() + "%");
        }
        if (doctorName != null && !doctorName.trim().isEmpty()) {
            sql.append("AND d.full_name LIKE ? ");
            params.add("%" + doctorName.trim() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND p.status = ? ");
            params.add(status.trim());
        }
        sql.append("ORDER BY p.prescription_date ");
        if ("asc".equalsIgnoreCase(sort)) sql.append("ASC"); else sql.append("DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}