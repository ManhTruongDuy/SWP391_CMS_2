package dao;

import model.StaffProfileDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffProfileDAO extends DBContext{
    private Connection connection;

    public StaffProfileDAO(Connection connection) {
        this.connection = connection;
    }

    public StaffProfileDTO getProfileById(int accountStaffId) throws SQLException {
        StaffProfileDTO profile = null;

        String sql = "SELECT a.account_staff_id, a.username, a.password, a.role, a.email, a.img, a.status, " +
                "       d.doctor_id, d.full_name AS doctor_name, d.department AS doctor_dept, d.phone AS doctor_phone, d.eduLevel, d.availability, " +
                "       ads.admin_id AS system_admin_id, ads.full_name AS ads_name, ads.department AS ads_dept, ads.phone AS ads_phone, " +
                "       adb.admin_id AS business_admin_id, adb.full_name AS adb_name, adb.department AS adb_dept, adb.phone AS adb_phone " +
                "FROM accountstaff a " +
                "LEFT JOIN Doctor d ON a.account_staff_id = d.account_staff_id " +
                "LEFT JOIN AdminSystem ads ON a.account_staff_id = ads.account_staff_id " +
                "LEFT JOIN AdminBusiness adb ON a.account_staff_id = adb.account_staff_id " +
                "WHERE a.account_staff_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountStaffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    profile = new StaffProfileDTO();

                    // Common fields
                    profile.setAccountStaffId(rs.getInt("account_staff_id"));
                    profile.setUsername(rs.getString("username"));
                    profile.setPassword(rs.getString("password"));
                    profile.setRole(rs.getString("role"));
                    profile.setEmail(rs.getString("email"));
                    profile.setImg(rs.getString("img"));
                    profile.setStatus(rs.getString("status"));

                    String role = rs.getString("role");
                    if ("Doctor".equalsIgnoreCase(role)) {
                        profile.setDoctorId(rs.getInt("doctor_id"));
                        profile.setFullName(rs.getString("doctor_name"));
                        profile.setDepartment(rs.getString("doctor_dept"));
                        profile.setPhone(rs.getString("doctor_phone"));
                        profile.setEduLevel(rs.getString("eduLevel"));
                        profile.setAvailability(rs.getString("availability"));
                    } else if ("AdminSys".equalsIgnoreCase(role)) {
                        profile.setAdminId(rs.getInt("system_admin_id"));
                        profile.setFullName(rs.getString("ads_name"));
                        profile.setDepartment(rs.getString("ads_dept"));
                        profile.setPhone(rs.getString("ads_phone"));
                    } else if ("AdminBusiness".equalsIgnoreCase(role)) {
                        profile.setAdminId(rs.getInt("business_admin_id"));
                        profile.setFullName(rs.getString("adb_name"));
                        profile.setDepartment(rs.getString("adb_dept"));
                        profile.setPhone(rs.getString("adb_phone"));
                    }
                }
            }
        }

        return profile;
    }
}
