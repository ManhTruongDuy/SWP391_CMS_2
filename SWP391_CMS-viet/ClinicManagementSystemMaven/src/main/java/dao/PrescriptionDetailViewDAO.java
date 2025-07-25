package dao;
//123
import model.PrescriptionDetailView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.MedicineStatistic;

public class PrescriptionDetailViewDAO {
    private final Connection conn;

    public PrescriptionDetailViewDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public List<PrescriptionDetailView> searchPrescriptionDetails(
            Integer prescriptionId, String patientName, String sortDate// sortDate = "asc" hoặc "desc"
    ) {
        List<PrescriptionDetailView> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.prescription_id, pt.full_name AS patient_name, p.prescription_date, " +
                        "med.name AS medicine_name, med.price AS unit_price, ms.quantity, ms.dosage, " +
                        "(med.price * ms.quantity) AS total_price " +
                        "FROM Prescription p " +
                        "INNER JOIN MedicineRecords mr ON p.medicineRecord_id = mr.medicineRecord_id " +
                        "INNER JOIN Patient pt ON mr.patient_id = pt.patient_id " +
                        "INNER JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
                        "INNER JOIN Medicines ms ON pi.prescription_invoice_id = ms.prescription_invoice_id " +
                        "INNER JOIN Medicine med ON ms.medicine_id = med.medicine_id WHERE 1=1 "
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
        // Sắp xếp ngày kê đơn
        sql.append("ORDER BY p.prescription_date ");
        if ("asc".equalsIgnoreCase(sortDate)) {
            sql.append("ASC");
        } else {
            sql.append("DESC");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionDetailView d = new PrescriptionDetailView();
                    d.setPrescriptionId(rs.getInt("prescription_id"));
                    d.setPatientName(rs.getString("patient_name"));
                    d.setPrescriptionDate(rs.getDate("prescription_date"));
                    d.setMedicineName(rs.getString("medicine_name"));
                    d.setUnitPrice(rs.getDouble("unit_price"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setDosage(rs.getString("dosage"));
                    d.setTotalPrice(rs.getDouble("total_price"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MedicineStatistic> getMedicineStatistics(java.sql.Date date, boolean byMonth) {
        List<MedicineStatistic> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT med.name AS medicine_name, " +
            "SUM(ms.quantity) AS total_quantity, " +
            "COUNT(DISTINCT p.prescription_id) AS total_prescriptions " +
            "FROM Prescription p " +
            "INNER JOIN PrescriptionInvoice pi ON p.prescription_id = pi.prescription_id " +
            "INNER JOIN Medicines ms ON pi.prescription_invoice_id = ms.prescription_invoice_id " +
            "INNER JOIN Medicine med ON ms.medicine_id = med.medicine_id " +
            "WHERE 1=1 "
        );
        if (date != null) {
            if (byMonth) {
                sql.append(" AND MONTH(p.prescription_date) = MONTH(?) AND YEAR(p.prescription_date) = YEAR(?) ");
            } else {
                sql.append(" AND p.prescription_date = ? ");
            }
        }
        sql.append(" GROUP BY med.name ORDER BY med.name");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (date != null) {
                if (byMonth) {
                    ps.setDate(1, date);
                    ps.setDate(2, date);
                } else {
                    ps.setDate(1, date);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicineStatistic stat = new MedicineStatistic();
                    stat.setMedicineName(rs.getString("medicine_name"));
                    stat.setTotalQuantity(rs.getInt("total_quantity"));
                    stat.setTotalPrescriptions(rs.getInt("total_prescriptions"));
                    list.add(stat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}