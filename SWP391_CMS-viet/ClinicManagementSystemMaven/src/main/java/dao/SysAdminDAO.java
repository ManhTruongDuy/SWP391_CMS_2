package dao;

import model.StaffProfileDTO;
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
                            rs.getString("department"),
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
                            rs.getString("department"),
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
                        da.setDepartment(rs.getString("department"));
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
                        ma.setDepartment(rs.getString("department"));
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
                        sa.setDepartment(rs.getString("department"));
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
                    da.setDepartment(rs.getString("department"));
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
                    ma.setDepartment(rs.getString("department"));
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
                    sa.setDepartment(rs.getString("department"));
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

    // ======================== DOCTOR ========================
    public boolean updateDoctorInfo(StaffProfileDTO doctor) {
        String sql1 = "UPDATE AccountStaff SET full_name = ?, phone = ?, email = ?, department = ? " +
                "WHERE account_staff_id = (SELECT account_staff_id FROM Doctor WHERE doctor_id = ?)";
        String sql2 = "UPDATE Doctor SET eduLevel = ?, availability = ? WHERE doctor_id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sql1);
                 PreparedStatement ps2 = conn.prepareStatement(sql2)) {

                ps1.setString(1, doctor.getFullName());
                ps1.setString(2, doctor.getPhone());
                ps1.setString(3, doctor.getEmail());
                ps1.setString(4, doctor.getDepartment());
                ps1.setInt(5, doctor.getDoctorId());

                ps2.setString(1, doctor.getEduLevel());
                ps2.setString(2, doctor.getAvailability());
                ps2.setInt(3, doctor.getDoctorId());

                ps1.executeUpdate();
                ps2.executeUpdate();

                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    // ======================== PHARMACIST ========================
    public boolean updatePharmacistInfo(PharmacistAccount pharmacist) {
        String sql = "UPDATE AccountPharmacist SET full_name = ?, phone = ?, email = ? " +
                "WHERE account_pharmacist_id = (SELECT account_pharmacist_id FROM Pharmacist WHERE pharmacist_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pharmacist.getName());
            ps.setString(2, pharmacist.getMobile());
            ps.setString(3, pharmacist.getEmail());
            ps.setInt(4, pharmacist.getID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================== MANAGER ========================
    public boolean updateManagerInfo(ManagerAccount manager) {
        String sql = "UPDATE AccountStaff SET full_name = ?, phone = ?, email = ?, department = ? " +
                "WHERE account_staff_id = (SELECT account_staff_id FROM AdminBusiness WHERE admin_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, manager.getFullName());
            ps.setString(2, manager.getPhone());
            ps.setString(3, manager.getEmail());
            ps.setString(4, manager.getDepartment());
            ps.setInt(5, manager.getAdmin_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================== SYSTEM ADMIN ========================
    public boolean updateSysAdminInfo(SysAdminAccount admin) {
        String getIdSql = "SELECT account_staff_id FROM AdminSystem WHERE admin_id = ?";
        String updateSql = "UPDATE AccountStaff SET full_name = ?, phone = ?, email = ?, department = ? WHERE account_staff_id = ?";

        try (
                PreparedStatement ps1 = conn.prepareStatement(getIdSql);
                PreparedStatement ps2 = conn.prepareStatement(updateSql)
        ) {
            ps1.setInt(1, admin.getAdmin_id());
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int accountStaffId = rs.getInt("account_staff_id");

                ps2.setString(1, admin.getFullName());
                ps2.setString(2, admin.getPhone());
                ps2.setString(3, admin.getEmail());
                ps2.setString(4, admin.getDepartment());
                ps2.setInt(5, accountStaffId);

                return ps2.executeUpdate() > 0;
            } else {
                System.out.println("🔧 Đã chạy vào DAO updateSysAdminInfo");
                System.out.println("Thông tin update: " + admin.getFullName() + " | " + admin.getPhone() + " | " + admin.getEmail() + " | " + admin.getDepartment() + " | " + admin.getAdmin_id());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ======================== PATIENT ========================
    public boolean updatePatientInfo(PatientAccount patient) {
        String sql = "UPDATE AccountPatient SET full_name = ?, phone = ?, email = ? " +
                "WHERE account_patient_id = (SELECT account_patient_id FROM Patient_AccountPatient WHERE patient_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFullName());
            ps.setString(2, patient.getPhone());
            ps.setString(3, patient.getEmail());
            ps.setInt(4, patient.getPatient_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
// get department doctor
    public List<String> getDoctorDepartments() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM Doctor";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }


    public List<String> getBusinessAdminDepartments() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM AdminBusiness";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    public List<String> getSystemAdminDepartments() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM AdminSystem";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }




}
