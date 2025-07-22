package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PharmacistDAO {
    private Connection connection;

    public PharmacistDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(String name, String mobile, String email, String password) throws SQLException {
        // Kiểm tra email đã tồn tại
        if (emailExists(email)) {
            throw new SQLException("Email đã tồn tại: " + email);
        }

        // Chèn vào bảng accountpharmacist với status và img
        String sql = "INSERT INTO accountpharmacist (username, email, password, status, img) VALUES (?, ?, ?, 'Enable', 'https://randomuser.me/api/portraits/men/1.jpg')";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);        // Sử dụng name làm username
            stmt.setString(2, email);       // Email
            stmt.setString(3, password);    // Password
            stmt.executeUpdate();

            // Lấy account_pharmacist_id vừa chèn
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int accountPharmacistId = generatedKeys.getInt(1);
                    // Chèn vào bảng pharmacist
                    insertPharmacist(accountPharmacistId, name, mobile);
                }
            }
        }
    }

    private void insertPharmacist(int accountPharmacistId, String fullName, String phone) throws SQLException {
        String sql = "INSERT INTO pharmacist (full_name, phone, account_pharmacist_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, phone);
            stmt.setInt(3, accountPharmacistId);
            stmt.executeUpdate();
        }
    }

    public void update(int pharmacistId, String fullName, String phone, int accountPharmacistId, String username, String email, String password, String status, String img) throws SQLException {
        connection.setAutoCommit(false); // Bắt đầu giao dịch

        try {
            // Cập nhật bảng pharmacist
            String sqlPharmacist = "UPDATE pharmacist SET full_name = ?, phone = ?, account_pharmacist_id = ? WHERE pharmacist_id = ?";
            try (PreparedStatement stmtPharmacist = connection.prepareStatement(sqlPharmacist)) {
                stmtPharmacist.setString(1, fullName);
                stmtPharmacist.setString(2, phone);
                stmtPharmacist.setInt(3, accountPharmacistId);
                stmtPharmacist.setInt(4, pharmacistId);
                stmtPharmacist.executeUpdate();
            }

            // Cập nhật bảng accountpharmacist
            String sqlAccount = "UPDATE accountpharmacist SET username = ?, email = ?, password = ?, status = ?, img = ? WHERE account_pharmacist_id = ?";
            try (PreparedStatement stmtAccount = connection.prepareStatement(sqlAccount)) {
                stmtAccount.setString(1, username);
                stmtAccount.setString(2, email);
                stmtAccount.setString(3, password);
                stmtAccount.setString(4, status);
                stmtAccount.setString(5, img);
                stmtAccount.setInt(6, accountPharmacistId);
                stmtAccount.executeUpdate();
            }

            connection.commit(); // Xác nhận giao dịch
        } catch (SQLException e) {
            connection.rollback(); // Hoàn tác nếu có lỗi
            throw e;
        } finally {
            connection.setAutoCommit(true); // Khôi phục chế độ tự động commit
        }
    }
    public ResultSet getPharmacistById(int pharmacistId) throws SQLException {
        String sql = "SELECT p.pharmacist_id, p.full_name, p.phone, p.account_pharmacist_id, a.username, a.email, a.password, a.status, a.img " +
                "FROM pharmacist p JOIN accountpharmacist a ON p.account_pharmacist_id = a.account_pharmacist_id " +
                "WHERE p.pharmacist_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pharmacistId);
        return stmt.executeQuery();
    }

    private boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accountpharmacist WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}