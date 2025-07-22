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
                        pa.setStatus(rs.getString("status"));
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
                        ma.setAdmin_id(rs.getInt("doctor_id"));
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
    // Tìm bác sĩ theo tên
    public List<DoctorAccount> getDoctorsByName(String name) {
        List<DoctorAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor d JOIN AccountStaff ast ON d.account_staff_id = ast.account_staff_id " +
                "WHERE d.full_name COLLATE Latin1_General_CI_AI LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DoctorAccount da = new DoctorAccount();
                    da.setId(rs.getInt("doctor_id"));
                    da.setName(rs.getString("full_name"));
                    da.setPhone(rs.getString("phone"));
                    da.setEmail(rs.getString("email"));
                    da.setUsername(rs.getString("username"));
                    da.setPassword(rs.getString("password"));
                    da.setStatus(rs.getString("status"));
                    list.add(da);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm dược sĩ theo tên
    public List<PharmacistAccount> getPharmacistsByName(String name) {
        List<PharmacistAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM Pharmacist p JOIN AccountPharmacist ap ON p.account_pharmacist_id = ap.account_pharmacist_id " +
                "WHERE p.full_name COLLATE Latin1_General_CI_AI LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PharmacistAccount pa = new PharmacistAccount();
                    pa.setID(rs.getInt("pharmacist_id"));
                    pa.setName(rs.getString("full_name"));
                    pa.setMobile(rs.getString("phone"));
                    pa.setEmail(rs.getString("email"));
                    pa.setUsername(rs.getString("username"));
                    pa.setPassword(rs.getString("password"));
                    pa.setStatus(rs.getString("status"));
                    list.add(pa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm quản lý theo tên
    public List<ManagerAccount> getManagersByName(String name) {
        List<ManagerAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM AdminBusiness ab JOIN AccountStaff ast ON ab.account_staff_id = ast.account_staff_id " +
                "WHERE ab.full_name COLLATE Latin1_General_CI_AI LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ManagerAccount ma = new ManagerAccount();
                    ma.setAdmin_id(rs.getInt("admin_id"));
                    ma.setFullName(rs.getString("full_name"));
                    ma.setPhone(rs.getString("phone"));
                    ma.setEmail(rs.getString("email"));

                    ma.setUsername(rs.getString("username"));
                    ma.setPassword(rs.getString("password"));
                    ma.setStatus(rs.getString("status"));
                    list.add(ma);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<SysAdminAccount> getSysAdminsByName(String name) {
        List<SysAdminAccount> list = new ArrayList<>();
        String sql =
                "SELECT * FROM AdminSystem asy " +
                        "JOIN AccountStaff ast ON asy.account_staff_id = ast.account_staff_id " +
                        "WHERE asy.full_name COLLATE Latin1_General_CI_AI LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SysAdminAccount sa = new SysAdminAccount();
                    sa.setAdmin_id(rs.getInt("admin_id"));
                    sa.setFullName(rs.getString("full_name"));
                    sa.setPhone(rs.getString("phone"));
                    sa.setEmail(rs.getString("email"));
                    sa.setUsername(rs.getString("username"));
                    sa.setPassword(rs.getString("password"));
                    sa.setStatus(rs.getString("status"));
                    list.add(sa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    // Tìm bệnh nhân theo tên
    public List<PatientAccount> getPatientsByName(String name) {
        List<PatientAccount> list = new ArrayList<>();
        String sql = "SELECT * FROM ((Patient p JOIN Patient_AccountPatient pap ON p.patient_id = pap.patient_id) " +
                "JOIN AccountPatient ap ON pap.account_patient_id = ap.account_patient_id) " +
                "WHERE p.full_name COLLATE Latin1_General_CI_AI LIKE ?";        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PatientAccount pa = new PatientAccount();
                    pa.setPatient_id(rs.getInt("patient_id"));
                    pa.setFullName(rs.getString("full_name"));
                    pa.setPhone(rs.getString("phone"));
                    pa.setEmail(rs.getString("email"));
                    pa.setUsername(rs.getString("username"));
                    pa.setPassword(rs.getString("password"));
                    pa.setStatus(rs.getString("status"));
                    list.add(pa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Cập nhật trạng thái cho SysAdmin
    public boolean updateSysAdminStatus(int adminId, String newStatus) {
        String sql = "UPDATE AccountStaff SET status = ? " +
                "WHERE account_staff_id = (SELECT account_staff_id FROM AdminSystem WHERE admin_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, adminId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái cho Doctor
    public boolean updateDoctorStatus(int doctorId, String newStatus) {
        String sql = "UPDATE AccountStaff SET status = ? " +
                "WHERE account_staff_id = (SELECT account_staff_id FROM Doctor WHERE doctor_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, doctorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái cho Business Admin
    public boolean updateBusinessAdminStatus(int adminId, String newStatus) {
        String sql = "UPDATE AccountStaff SET status = ? " +
                "WHERE account_staff_id = (SELECT account_staff_id FROM AdminBusiness WHERE admin_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, adminId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái cho Pharmacist
    public boolean updatePharmacistStatus(int pharmacistId, String newStatus) {
        String sql = "UPDATE AccountPharmacist SET Status = ? " +
                "WHERE account_pharmacist_id = (SELECT account_pharmacist_id FROM Pharmacist WHERE pharmacist_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, pharmacistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái cho Patient
    public boolean updatePatientStatus(int patientId, String newStatus) {
        String sql = "UPDATE AccountPatient SET status = ? " +
                "WHERE account_patient_id = (SELECT account_patient_id FROM Patient_AccountPatient WHERE patient_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
