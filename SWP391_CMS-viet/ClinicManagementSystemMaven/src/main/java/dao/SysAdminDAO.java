package dao;

import model.Warehouse;
import model.accounts.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SysAdminDAO {
    private final Connection conn;

    public SysAdminDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }
    // Lấy tất cả tk dược sĩ
    public List<PharmacistAccount> getAllPharmacists() {
        List<PharmacistAccount> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Pharmacist p JOIN AccountPharmacist ap " +
                "ON p.account_pharmacist_id = ap.account_pharmacist_id " +
                "ORDER BY p.pharmacist_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PharmacistAccount pa = new PharmacistAccount(
                            rs.getInt("pharmacist_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    list.add(pa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả tk bác sĩ
    public List<DoctorAccount> getAllDoctors() {
        List<DoctorAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor d JOIN AccountStaff ast ON d.account_staff_id = ast.account_staff_id ORDER BY d.doctor_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Executing query: " + sql);
                while (rs.next()) {
                    DoctorAccount da = new DoctorAccount(
                            rs.getInt("doctor_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    list.add(da);
                }
                System.out.println("Doctors found: " + list.size());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả tk quản lý
    public List<ManagerAccount> getAllManagers() {
        List<ManagerAccount> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM AdminBusiness ab JOIN AccountStaff ast " +
                "ON ab.account_staff_id = ast.account_staff_id " +
                "ORDER BY ab.admin_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ManagerAccount ma = new ManagerAccount(
                            rs.getInt("admin_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("department"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    list.add(ma);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả tk quản trị hệ thống
    public List<SysAdminAccount> getAllSysAdmins() {
        List<SysAdminAccount> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM AdminSystem asy JOIN AccountStaff ast " +
                "ON asy.account_staff_id = ast.account_staff_id " +
                "ORDER BY asy.admin_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SysAdminAccount sac = new SysAdminAccount(
                            rs.getInt("admin_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    list.add(sac);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả tk bệnh nhân
    public List<PatientAccount> getAllPatients() {
        List<PatientAccount> list = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM ((Patient p JOIN Patient_AccountPatient pap " +
                "ON p.patient_id = pap.patient_id) " +
                "JOIN AccountPatient ap ON pap.account_patient_id = ap.account_patient_id) " +
                "ORDER BY p.patient_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PatientAccount pa = new PatientAccount(
                            rs.getInt("patient_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    list.add(pa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
