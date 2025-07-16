package dao;

import model.AccountPharmacist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountPharmacistDAO extends DBContext {

    /* ---------------- map row dùng chung ---------------- */
    private AccountPharmacist mapRow(ResultSet rs) throws SQLException {
        return new AccountPharmacist(
                rs.getInt("account_pharmacist_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("img"),
                rs.getString("status")
        );
    }

    /* ---------------- GET ALL ---------------- */
    public List<AccountPharmacist> getAllAccount() {
        List<AccountPharmacist> listAccount = new ArrayList<>();
        String sql = "SELECT * FROM AccountPharmacist";
        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                listAccount.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error loading pharmacist accounts: " + ex.getMessage());
        }
        return listAccount;
    }

    /* ---------------- GET BY ID ---------------- */
    public AccountPharmacist getById(int id) {
        String sql = "SELECT * FROM AccountPharmacist WHERE account_pharmacist_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Error getById: " + ex.getMessage());
        }
        return null;
    }

    /* ---------------- GET BY USERNAME ---------------- */
    public AccountPharmacist getByUsername(String username) {
        String sql = "SELECT * FROM AccountPharmacist WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Error getByUsername: " + ex.getMessage());
        }
        return null;
    }

    /* ---------------- LOGIN ---------------- */
    public AccountPharmacist getAccountByEmailAndPassword(String email, String password) {
        AccountPharmacist account = null;
        String sql = "SELECT * FROM AccountPharmacist WHERE email = ? AND password = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email != null ? email.trim() : null);
            stm.setString(2, password);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) account = mapRow(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting account: " + ex.getMessage());
        }
        return account;
    }

    /* ---------------- EXISTS ---------------- */
    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM AccountPharmacist WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("Error checking email existence: " + ex.getMessage());
            return false;
        }
    }

    public boolean checkExistUsername(String username) {
        String sql = "SELECT 1 FROM AccountPharmacist WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("Error checking username existence: " + ex.getMessage());
            return false;
        }
    }

    /* ---------------- INSERT ---------------- */
    /** Return generated id, or -1 if fail. */
    public int insertAccount(AccountPharmacist account) {
        String sql = "INSERT INTO AccountPharmacist (username, password, email, status, img) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, account.getUsername());
            stm.setString(2, account.getPassword());
            stm.setString(3, account.getEmail());
            stm.setString(4, account.getStatus());
            stm.setString(5, account.getImg());
            int rows = stm.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error adding account: " + ex.getMessage());
        }
        return -1;
    }

    /** Giữ hàm cũ (void), gọi insertAccount(). */
    public void addAccount(AccountPharmacist account) {
        insertAccount(account);
    }

    /* ---------------- UPDATE FULL ---------------- */
    public boolean updateAccount(int id, String username, String password, String email, String status, String img) {
        String sql = "UPDATE AccountPharmacist SET username=?, password=?, email=?, status=?, img=? WHERE account_pharmacist_id=?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            stm.setString(2, password);
            stm.setString(3, email);
            stm.setString(4, status);
            stm.setString(5, img);
            stm.setInt(6, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updateAccount: " + ex.getMessage());
            return false;
        }
    }

    /* ---------------- PARTIAL UPDATES ---------------- */
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE AccountPharmacist SET password = ? WHERE email = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newPassword);
            stm.setString(2, email);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating password: " + ex.getMessage());
            return false;
        }
    }

    public boolean checkCurrentPassword(String username, String password) {
        String sql = "SELECT 1 FROM AccountPharmacist WHERE username = ? AND password = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            stm.setString(2, password);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.out.println("Error checking current password: " + ex.getMessage());
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE AccountPharmacist SET password = ? WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, newPassword);
            stm.setString(2, username);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error changing password: " + ex.getMessage());
            return false;
        }
    }

    public boolean updateImg(String username, String img) {
        String sql = "UPDATE AccountPharmacist SET img = ? WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, img);
            stm.setString(2, username);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating img: " + ex.getMessage());
            return false;
        }
    }

    public boolean updateEmail(String username, String email) {
        String sql = "UPDATE AccountPharmacist SET email = ? WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            stm.setString(2, username);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating email: " + ex.getMessage());
            return false;
        }
    }

    public boolean updateStatus(String username, String status) {
        String sql = "UPDATE AccountPharmacist SET status = ? WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, status);
            stm.setString(2, username);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating status: " + ex.getMessage());
            return false;
        }
    }

    /* ---------------- DISABLE (soft delete) ---------------- */
    public boolean disableById(int id) {
        String sql = "UPDATE AccountPharmacist SET status='Disable' WHERE account_pharmacist_id=?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error disableById: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        AccountPharmacistDAO dao = new AccountPharmacistDAO();
        List<AccountPharmacist> list = dao.getAllAccount();

        if (list.isEmpty()) {
            System.out.println("Không có tài khoản dược sĩ nào trong CSDL.");
        } else {
            System.out.println("Danh sách tài khoản dược sĩ:");
            for (AccountPharmacist acc : list) {
                System.out.println("Username: " + acc.getUsername() + ", Email: " + acc.getEmail());
            }
        }
    }
    public boolean updateEmailByEmail(String oldEmail, String newEmail) {
        String sql = "UPDATE AccountPharmacist SET email = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setString(2, oldEmail);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateImgByEmail(String email, String img) {
        String sql = "UPDATE AccountPharmacist SET img = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, img);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
