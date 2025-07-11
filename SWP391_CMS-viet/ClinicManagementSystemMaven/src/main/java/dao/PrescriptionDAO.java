package dao;

import model.Doctor;
import model.MedicineRecord;
import model.Patient;
import model.Prescription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO extends DBContext{
    public List<Prescription> getAllPrescriptions(int page, int pageSize) {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT pre.prescription_id, pre.prescription_date, pre.status, " +
                "pat.full_name AS patientName, doc.full_name AS doctorName " +
                "FROM Prescription pre " +
                "JOIN MedicineRecords mr ON pre.medicineRecord_id = mr.medicineRecord_id " +
                "JOIN Patient pat ON pat.patient_id = mr.patient_id " +
                "JOIN Doctor doc ON doc.doctor_id = pre.doctor_id " +
                "ORDER BY pre.prescription_date DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, (page - 1) * pageSize); // OFFSET
            stm.setInt(2, pageSize);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Prescription pre = new Prescription();
                pre.setId(rs.getInt("prescription_id"));
                pre.setStatus(rs.getString("status"));
                pre.setPrescriptionDate(rs.getDate("prescription_date"));

                Patient pat = new Patient();
                pat.setFullName(rs.getString("patientName"));

                MedicineRecord mr = new MedicineRecord();
                mr.setPatient(pat);
                pre.setMedicineRecord(mr);

                Doctor doc = new Doctor();
                doc.setFullName(rs.getString("doctorName"));
                pre.setDoctor(doc);

                prescriptions.add(pre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prescriptions;
    }
    public int getTotalPrescriptions() {
        String sql = "SELECT COUNT(*) AS total FROM Prescription";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public Prescription getPrescriptionById(int id) {
        String sql = "SELECT pre.prescription_id, pre.prescription_date, pre.status, " +
                "mr.medicineRecord_id, pat.patient_id, pat.full_name AS patientName, " +
                "doc.doctor_id, doc.full_name AS doctorName " +
                "FROM Prescription pre " +
                "JOIN MedicineRecords mr ON pre.medicineRecord_id = mr.medicineRecord_id " +
                "JOIN Patient pat ON mr.patient_id = pat.patient_id " +
                "JOIN Doctor doc ON pre.doctor_id = doc.doctor_id " +
                "WHERE pre.prescription_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Prescription pre = new Prescription();
                pre.setId(rs.getInt("prescription_id"));
                pre.setPrescriptionDate(rs.getDate("prescription_date"));
                pre.setStatus(rs.getString("status"));

                Patient pat = new Patient();
                pat.setId(rs.getInt("patient_id"));
                pat.setFullName(rs.getString("patientName"));

                MedicineRecord mr = new MedicineRecord();
                mr.setId(rs.getInt("medicineRecord_id"));
                mr.setPatient(pat);
                pre.setMedicineRecord(mr);

                Doctor doc = new Doctor();
                doc.setId(rs.getInt("doctor_id"));
                doc.setFullName(rs.getString("doctorName"));
                pre.setDoctor(doc);

                return pre;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
