package dao;

import model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO {
    private final Connection conn;

    public PatientDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // ✅ Lấy bệnh nhân theo patient_id (dùng trực tiếp patient_id)
    public Patient getPatientById(int patientId) {
        String sql = "SELECT patient_id, full_name, dob, gender, phone, address FROM Patient WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patient p = new Patient();
                    p.setId(rs.getInt("patient_id"));
                    p.setFullName(rs.getString("full_name"));
                    p.setDob(rs.getDate("dob"));
                    p.setGender(rs.getString("gender"));
                    p.setPhone(rs.getString("phone"));
                    p.setAddress(rs.getString("address"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Lấy bệnh nhân theo account_patient_id (mapping 1:1 với patient_id)
    public Patient getPatientByAccountId(int accountPatientId) {
        // Vì DB không có account_patient_id nên dùng patient_id để map 1:1
        String sql = "SELECT patient_id, full_name, dob, gender, phone, address " +
                "FROM Patient WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountPatientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patient p = new Patient();
                    p.setId(rs.getInt("patient_id"));
                    p.setFullName(rs.getString("full_name"));
                    p.setDob(rs.getDate("dob"));
                    p.setGender(rs.getString("gender"));
                    p.setPhone(rs.getString("phone"));
                    p.setAddress(rs.getString("address"));
                    return p;
                } else {
                    System.out.println("DEBUG: Không tìm thấy bệnh nhân với patient_id = " + accountPatientId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
