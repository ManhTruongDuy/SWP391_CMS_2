package dao;

import model.MedicineRecord;
import model.Patient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MedicineRecordDAO extends DBContext {

    /**
     * Lấy MedicineRecord theo ID
     */
    public MedicineRecord getMedicineRecordById(int id) {
        String sql = "SELECT mr.medicineRecord_id, p.patient_id, p.full_name " +
                "FROM MedicineRecords mr " +
                "JOIN Patient p ON mr.patient_id = p.patient_id " +
                "WHERE mr.medicineRecord_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                MedicineRecord mr = new MedicineRecord();
                mr.setId(rs.getInt("medicineRecord_id"));

                Patient patient = new Patient();
                patient.setId(rs.getInt("patient_id"));
                patient.setFullName(rs.getString("full_name"));

                mr.setPatient(patient);
                return mr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm MedicineRecord theo patient_id
     */
    public MedicineRecord getMedicineRecordByPatientId(int patientId) {
        String sql = "SELECT medicineRecord_id FROM MedicineRecords WHERE patient_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, patientId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                MedicineRecord mr = new MedicineRecord();
                mr.setId(rs.getInt("medicineRecord_id"));

                Patient patient = new Patient();
                patient.setId(patientId);

                mr.setPatient(patient);
                return mr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo mới MedicineRecord cho bệnh nhân
     */
    public int createMedicineRecord(int patientId) {
        String sql = "INSERT INTO MedicineRecords (patient_id) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Tạo mới MedicineRecord cho bệnh nhân từ đối tượng Patient
     */
    public MedicineRecord createMedicineRecord(Patient patient) {
        // Tìm patient_id từ tên bệnh nhân
        String findPatientSql = "SELECT patient_id FROM Patient WHERE full_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(findPatientSql)) {
            ps.setString(1, patient.getFullName());
            ResultSet rs = ps.executeQuery();

            int patientId;
            if (rs.next()) {
                patientId = rs.getInt("patient_id");
                patient.setId(patientId);
            } else {
                // Nếu không tìm thấy bệnh nhân, tạo mới bệnh nhân
                String insertPatientSql = "INSERT INTO Patient (full_name) VALUES (?)";
                try (PreparedStatement psInsert = connection.prepareStatement(insertPatientSql, Statement.RETURN_GENERATED_KEYS)) {
                    psInsert.setString(1, patient.getFullName());
                    int affectedRows = psInsert.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                            if (rsKeys.next()) {
                                patientId = rsKeys.getInt(1);
                                patient.setId(patientId);
                            } else {
                                return null; // Không thể tạo bệnh nhân mới
                            }
                        }
                    } else {
                        return null; // Không thể tạo bệnh nhân mới
                    }
                }
            }

            // Tạo MedicineRecord với patientId đã có hoặc vừa tạo
            String sql = "INSERT INTO MedicineRecords (patient_id) VALUES (?)";
            try (PreparedStatement psInsertMR = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                psInsertMR.setInt(1, patientId);
                int affectedRows = psInsertMR.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rsMR = psInsertMR.getGeneratedKeys()) {
                        if (rsMR.next()) {
                            MedicineRecord newMr = new MedicineRecord();
                            newMr.setId(rsMR.getInt(1));
                            newMr.setPatient(patient);
                            return newMr;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm MedicineRecord theo tên bệnh nhân
     */
    public MedicineRecord getMedicineRecordByPatientName(String patientName) {
        String sql = "SELECT mr.medicineRecord_id, p.patient_id, p.full_name " +
                "FROM MedicineRecords mr " +
                "JOIN Patient p ON mr.patient_id = p.patient_id " +
                "WHERE p.full_name = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, patientName);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                MedicineRecord mr = new MedicineRecord();
                mr.setId(rs.getInt("medicineRecord_id"));

                Patient patient = new Patient();
                patient.setId(rs.getInt("patient_id"));
                patient.setFullName(rs.getString("full_name"));

                mr.setPatient(patient);
                return mr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}