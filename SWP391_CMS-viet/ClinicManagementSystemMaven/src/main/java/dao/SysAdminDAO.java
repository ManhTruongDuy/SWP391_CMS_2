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

    //Lấy tất cả tk quản lí kho
    public List<WarehouseManagerAccount> getAllWarehouseManagers() {
        List<WarehouseManagerAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM Warehouse_manager w JOIN AccountStaff a ON w.account_staff_id = a.account_staff_id ORDER BY w.Warehouse_manager_id " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WarehouseManagerAccount wa = new WarehouseManagerAccount(
                            rs.getInt("Warehouse_manager_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    list.add(wa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Lấy bác sĩ theo id
    public DoctorAccount getDoctorById(int id) {
        String sql = "SELECT * FROM Doctor d JOIN AccountStaff ast ON d.account_staff_id = ast.account_staff_id WHERE d.doctor_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DoctorAccount da = new DoctorAccount();
                        da.setId(rs.getInt("doctor_id"));
                        da.setName(rs.getString("full_name"));
                        da.setPhone(rs.getString("phone"));
                        da.setEmail(rs.getString("email"));
                        da.setUsername(rs.getString("username"));
                        da.setPassword(rs.getString("password"));
                        da.setStatus(rs.getString("status"));
                    return da;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Lấy dược sĩ theo id
    public PharmacistAccount getPharmacistById(int id) {
        String sql = "SELECT * FROM Pharmacist p JOIN AccountPharmacist ap ON p.account_pharmacist_id = ap.account_pharmacist_id WHERE p.pharmacist_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PharmacistAccount pa = new PharmacistAccount();
                        pa.setID(rs.getInt("pharmacist_id"));
                        pa.setName(rs.getString("full_name"));
                        pa.setMobile(rs.getString("phone"));
                        pa.setEmail(rs.getString("email"));
                        pa.setUsername(rs.getString("username"));
                        pa.setPassword(rs.getString("password"));
                    return pa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Lấy quản lý theo id
    public ManagerAccount getManagerById(int id) {
        String sql = "SELECT * FROM AdminBusiness ab JOIN AccountStaff ast ON ab.account_staff_id = ast.account_staff_id WHERE ab.admin_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ManagerAccount ma = new ManagerAccount();
                        ma.setAdmin_id(rs.getInt("admin_id"));
                        ma.setFullName(rs.getString("full_name"));
                        ma.setPhone(rs.getString("phone"));
                        ma.setEmail(rs.getString("email"));
                        ma.setUsername(rs.getString("username"));
                        ma.setPassword(rs.getString("password"));
                        ma.setStatus(rs.getString("status"));
                    return ma;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Lấy quản trị hệ thống theo id
    public SysAdminAccount getSysAdminById(int id) {
        String sql = "SELECT * FROM AdminSystem asy JOIN AccountStaff ast ON asy.account_staff_id = ast.account_staff_id WHERE asy.admin_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SysAdminAccount sa = new SysAdminAccount();
                        sa.setAdmin_id(rs.getInt("admin_id"));
                        sa.setFullName(rs.getString("full_name"));
                        sa.setPhone(rs.getString("phone"));
                        sa.setEmail(rs.getString("email"));
                        sa.setUsername(rs.getString("username"));
                        sa.setPassword(rs.getString("password"));
                        sa.setStatus(rs.getString("status"));
                    return sa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Lấy bệnh nhân theo id
    public PatientAccount getPatientById(int id) {
        String sql = "SELECT * FROM ((Patient p JOIN Patient_AccountPatient pap ON p.patient_id = pap.patient_id) JOIN AccountPatient ap ON pap.account_patient_id = ap.account_patient_id) WHERE p.patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PatientAccount pa = new PatientAccount();
                        pa.setPatient_id(rs.getInt("patient_id"));
                        pa.setFullName(rs.getString("full_name"));
                        pa.setPhone(rs.getString("phone"));
                        pa.setEmail(rs.getString("email"));
                        pa.setUsername(rs.getString("username"));
                        pa.setPassword(rs.getString("password"));
                        pa.setStatus(rs.getString("status"));
                    return pa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Lấy tk quản lí kho theo id
    public WarehouseManagerAccount getWarehouseManagerById(int id) {
        String sql = "SELECT * FROM Warehouse_manager w JOIN AccountStaff a ON w.account_staff_id = a.account_staff_id WHERE w.Warehouse_manager_id = ? " ;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WarehouseManagerAccount wa = new WarehouseManagerAccount(
                            rs.getInt("Warehouse_manager_id"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("status")
                    );
                    return wa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
