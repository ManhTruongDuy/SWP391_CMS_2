package dao;

import model.History;
import model.Medicine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicineWarehouseDAO {
    private final Connection conn;

    public MedicineWarehouseDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    // Lấy tất cả thuốc
    public List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicine";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicine_id(rs.getInt("medicine_id"));
                m.setName(rs.getString("name"));
                m.setUnit_id(rs.getInt("unit_id"));
                m.setCategory_id(rs.getInt("category_id"));
                m.setIngredient(rs.getString("ingredient"));
                m.setUsage(rs.getString("usage"));
                m.setPreservation(rs.getString("preservation"));
                m.setManuDate(rs.getDate("manuDate") != null ? rs.getDate("manuDate").toLocalDate() : null);
                m.setExpDate(rs.getDate("expDate") != null ? rs.getDate("expDate").toLocalDate() : null);
                m.setQuantity(rs.getInt("quantity"));
                m.setPrice(rs.getFloat("price"));
                m.setWarehouse_id(rs.getInt("warehouse_id"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    // Lấy thuốc theo ID
    public Medicine getMedicineById(int id) {
        String sql = "SELECT medicine_id, name, unit_id, category_id, ingredient, usage, preservation, manuDate, expDate, quantity, price, warehouse_id FROM Medicine WHERE medicine_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("name"),
                            rs.getInt("unit_id"),
                            rs.getInt("category_id"),
                            rs.getString("ingredient"),
                            rs.getString("usage"),
                            rs.getString("preservation"),
                            rs.getDate("manuDate") != null ? rs.getDate("manuDate").toLocalDate() : null,
                            rs.getDate("expDate") != null ? rs.getDate("expDate").toLocalDate() : null,
                            rs.getInt("quantity"),
                            rs.getFloat("price"),
                            rs.getInt("warehouse_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Medicine> getMedicineByName(String name) {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM Medicine WHERE name LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicine m = new Medicine();
                    m.setMedicine_id(rs.getInt("medicine_id"));
                    m.setName(rs.getString("name"));
                    m.setUnit_id(rs.getInt("unit_id"));
                    m.setCategory_id(rs.getInt("category_id"));
                    m.setIngredient(rs.getString("ingredient"));
                    m.setUsage(rs.getString("usage"));
                    m.setPreservation(rs.getString("preservation"));
                    m.setManuDate(rs.getDate("manuDate") != null ? rs.getDate("manuDate").toLocalDate() : null);
                    m.setExpDate(rs.getDate("expDate") != null ? rs.getDate("expDate").toLocalDate() : null);
                    m.setQuantity(rs.getInt("quantity"));
                    m.setPrice(rs.getFloat("price"));
                    m.setWarehouse_id(rs.getInt("warehouse_id"));
                    medicines.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    // Thêm mới thuốc
    public boolean addMedicine(Medicine med) {
        String sql = "INSERT INTO Medicine (name, unit_id, category_id, ingredient, usage, preservation, manuDate, expDate, quantity, price, warehouse_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        HistoryDAO historyDAO = new HistoryDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, med.getName());
            ps.setInt(2, med.getUnit_id());
            ps.setInt(3, med.getCategory_id());
            ps.setString(4, med.getIngredient());
            ps.setString(5, med.getUsage());
            ps.setString(6, med.getPreservation());
            if (med.getManuDate() != null) ps.setDate(7, Date.valueOf(med.getManuDate()));
            else ps.setNull(7, Types.DATE);
            if (med.getExpDate() != null) ps.setDate(8, Date.valueOf(med.getExpDate()));
            else ps.setNull(8, Types.DATE);
            ps.setInt(9, med.getQuantity());
            ps.setFloat(10, med.getPrice());
            ps.setInt(11, med.getWarehouse_id());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        med.setMedicine_id(generatedKeys.getInt(1));
                        History history = new History(
                                "CREATE",
                                "Medicine",
                                med.getMedicine_id(),
                                String.format("Thêm thuốc: %s, Số lượng: %d, Giá: %.2f", med.getName(), med.getQuantity(), med.getPrice())
                        );
                        history.setActionTime(LocalDateTime.now());
                        historyDAO.addHistory(history);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm thuốc: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Invalid body or data: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật thuốc
    public boolean updateMedicine(Medicine med) {
        String sql = "UPDATE Medicine SET name = ?, unit_id = ?, category_id = ?, ingredient = ?, usage = ?, preservation = ?, manuDate = ?, expDate = ?, quantity = ?, price = ?, warehouse_id = ? WHERE medicine_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();
        // Lấy dữ liệu cũ
        Medicine oldMed = getMedicineById(med.getMedicine_id());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, med.getName());
            ps.setInt(2, med.getUnit_id());
            ps.setInt(3, med.getCategory_id());
            ps.setString(4, med.getIngredient());
            ps.setString(5, med.getUsage());
            ps.setString(6, med.getPreservation());
            if (med.getManuDate() != null) ps.setDate(7, Date.valueOf(med.getManuDate()));
            else ps.setNull(7, Types.DATE);
            if (med.getExpDate() != null) ps.setDate(8, Date.valueOf(med.getExpDate()));
            else ps.setNull(8, Types.DATE);
            ps.setInt(9, med.getQuantity());
            ps.setFloat(10, med.getPrice());
            ps.setInt(11, med.getWarehouse_id());
            ps.setInt(12, med.getMedicine_id());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                StringBuilder details = new StringBuilder("Cập nhật thuốc: " + med.getName());
                if (!med.getName().equals(oldMed.getName())) details.append(String.format(", Tên: %s -> %s", oldMed.getName(), med.getName()));
                if (med.getQuantity() != oldMed.getQuantity()) details.append(String.format(", Số lượng: %d -> %d", oldMed.getQuantity(), med.getQuantity()));
                if (med.getPrice() != oldMed.getPrice()) details.append(String.format(", Giá: %.2f -> %.2f", oldMed.getPrice(), med.getPrice()));
                History history = new History("UPDATE", "Medicine", med.getMedicine_id(), details.toString());
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật thuốc: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thuốc
    public boolean deleteMedicine(int id) throws SQLException {
        String checkMedicines = "SELECT COUNT(*) FROM Medicines WHERE medicine_id = ?";
        String checkImportInfo = "SELECT COUNT(*) FROM ImportInfo WHERE medicine_id = ?";
        String deleteMedicines = "DELETE FROM Medicines WHERE medicine_id = ?";
        String deleteImportInfo = "DELETE FROM ImportInfo WHERE medicine_id = ?";
        String deleteMedicine = "DELETE FROM Medicine WHERE medicine_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();

        try {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Kiểm tra ràng buộc khóa ngoại
            try (PreparedStatement checkPs1 = conn.prepareStatement(checkMedicines)) {
                checkPs1.setInt(1, id);
                try (ResultSet rs = checkPs1.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Không thể xóa thuốc: Có bản ghi trong Medicines tham chiếu thuốc này.");
                    }
                }
            }
            try (PreparedStatement checkPs2 = conn.prepareStatement(checkImportInfo)) {
                checkPs2.setInt(1, id);
                try (ResultSet rs = checkPs2.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Không thể xóa thuốc: Có bản ghi trong ImportInfo tham chiếu thuốc này.");
                    }
                }
            }

            // Lấy thông tin thuốc trước khi xóa
            Medicine medicine = getMedicineById(id);
            String details = medicine != null ?
                    String.format("Xóa thuốc: %s, Số lượng: %d, Giá: %.2f",
                            medicine.getName(), medicine.getQuantity(), medicine.getPrice())
                    : "Xóa thuốc: ID " + id;

            // Xóa ở bảng Medicines
            try (PreparedStatement ps1 = conn.prepareStatement(deleteMedicines)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            // Xóa ở bảng ImportInfo
            try (PreparedStatement ps2 = conn.prepareStatement(deleteImportInfo)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }

            // Xóa ở bảng Medicine
            int affectedRows = 0;
            try (PreparedStatement ps3 = conn.prepareStatement(deleteMedicine)) {
                ps3.setInt(1, id);
                affectedRows = ps3.executeUpdate();
            }

            // Ghi lịch sử nếu xóa thành công
            if (affectedRows > 0) {
                History history = new History(
                        "DELETE",
                        "Medicine",
                        id,
                        details
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
            }

            conn.commit(); // Xác nhận transaction
            return affectedRows > 0;

        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback nếu lỗi
            } catch (SQLException ex) {
                throw new SQLException("Lỗi rollback: " + ex.getMessage(), ex);
            }
            throw e; // Ném lại SQLException để servlet xử lý
        } finally {
            try {
                conn.setAutoCommit(true); // Trả lại auto commit
            } catch (SQLException e) {
                throw new SQLException("Lỗi auto commit: " + e.getMessage(), e);
            }
        }
    }





}
