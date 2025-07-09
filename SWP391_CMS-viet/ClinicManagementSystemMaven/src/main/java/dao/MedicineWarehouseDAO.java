package dao;

import model.Medicine;

import java.sql.*;
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

    // Thêm mới thuốc
    public boolean addMedicine(Medicine med) {
        String sql = "INSERT INTO Medicine (name, unit_id, category_id, ingredient, usage, preservation, manuDate, expDate, quantity, price, warehouse_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, med.getName());
            ps.setInt(2, med.getUnit_id());
            ps.setInt(3, med.getCategory_id());
            ps.setString(4, med.getIngredient());
            ps.setString(5, med.getUsage());
            ps.setString(6, med.getPreservation());
            if (med.getManuDate() != null) ps.setDate(7, Date.valueOf(med.getManuDate())); else ps.setNull(7, Types.DATE);
            if (med.getExpDate() != null) ps.setDate(8, Date.valueOf(med.getExpDate())); else ps.setNull(8, Types.DATE);
            ps.setInt(9, med.getQuantity());
            ps.setFloat(10, med.getPrice());
            ps.setInt(11, med.getWarehouse_id());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        med.setMedicine_id(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thuốc
    public boolean updateMedicine(Medicine med) {
        String sql = "UPDATE Medicine SET name=?, unit_id=?, category_id=?, ingredient=?, usage=?, preservation=?, manuDate=?, expDate=?, quantity=?, price=?, warehouse_id=? WHERE medicine_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, med.getName());
            ps.setInt(2, med.getUnit_id());
            ps.setInt(3, med.getCategory_id());
            ps.setString(4, med.getIngredient());
            ps.setString(5, med.getUsage());
            ps.setString(6, med.getPreservation());
            if (med.getManuDate() != null) ps.setDate(7, Date.valueOf(med.getManuDate())); else ps.setNull(7, Types.DATE);
            if (med.getExpDate() != null) ps.setDate(8, Date.valueOf(med.getExpDate())); else ps.setNull(8, Types.DATE);
            ps.setInt(9, med.getQuantity());
            ps.setFloat(10, med.getPrice());
            ps.setInt(11, med.getWarehouse_id());
            ps.setInt(12, med.getMedicine_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thuốc
    public boolean deleteMedicine(int id) {
        String deleteMedicines = "DELETE FROM Medicines WHERE medicine_id = ?";
        String deleteImportInfo = "DELETE FROM ImportInfo WHERE medicine_id = ?";
        String deleteMedicine = "DELETE FROM Medicine WHERE medicine_id = ?";

        try {
            conn.setAutoCommit(false); // Bắt đầu transaction

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

            conn.commit(); // Xác nhận transaction
            return affectedRows > 0;
        } catch (SQLException e) {
            try {
                conn.rollback(); // Nếu lỗi, rollback về trạng thái ban đầu
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true); // Trả lại chế độ auto commit mặc định
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public List<Medicine> getExpiredMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE expDate < CURDATE()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicine medicine = extractMedicineFromResultSet(rs);
                list.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Medicine extractMedicineFromResultSet(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setMedicine_id(rs.getInt("medicine_id"));
        medicine.setName(rs.getString("name"));
        medicine.setUnit_id(rs.getInt("unit_id"));
        medicine.setCategory_id(rs.getInt("category_id"));
        medicine.setIngredient(rs.getString("ingredient"));
        medicine.setUsage(rs.getString("usage"));
        medicine.setPreservation(rs.getString("preservation"));
        medicine.setManuDate(rs.getDate("manuDate").toLocalDate());
        medicine.setExpDate(rs.getDate("expDate").toLocalDate());
        medicine.setQuantity(rs.getInt("quantity"));
        medicine.setPrice(rs.getFloat("price"));
        medicine.setWarehouse_id(rs.getInt("warehouse_id"));
        return medicine;
    }



}
