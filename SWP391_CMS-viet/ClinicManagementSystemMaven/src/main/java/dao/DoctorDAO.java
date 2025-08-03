package dao;

import model.Doctor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO extends DBContext {

    /**
     * Lấy thông tin bác sĩ theo ID
     */
    public Doctor getDoctorById(int id) {
        String sql = "SELECT doctor_id, full_name, phone, department, eduLevel FROM Doctor WHERE doctor_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setEduLevel(rs.getString("eduLevel"));
                return doctor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm bác sĩ theo tên
     */
    public Doctor getDoctorByName(String name) {
        String sql = "SELECT doctor_id, full_name, phone, department, eduLevel FROM Doctor WHERE full_name LIKE ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + name + "%");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setEduLevel(rs.getString("eduLevel"));
                return doctor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả bác sĩ
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT doctor_id, full_name, phone, department, eduLevel FROM Doctor";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setEduLevel(rs.getString("eduLevel"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Tìm kiếm bác sĩ theo tên (trả về danh sách)
     */
    public List<Doctor> searchDoctorsByName(String name) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT doctor_id, full_name, phone, department, eduLevel FROM Doctor WHERE full_name LIKE ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + name + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setEduLevel(rs.getString("eduLevel"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}