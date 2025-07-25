package dao;

import model.MedicineWarehouse;
import model.Warehouse;
import model.History;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    private final Connection conn;

    public WarehouseDAO() {
        this.conn = DBContext.getInstance().getConnection();
    }

    public Warehouse getWarehouseById(int id) {
        String sql = "SELECT warehouse_id, name, location FROM Warehouse WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Warehouse warehouse = new Warehouse();
                    warehouse.setId(rs.getInt("warehouse_id"));
                    warehouse.setName(rs.getString("name"));
                    warehouse.setLocation(rs.getString("location"));
                    return warehouse;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy kho: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT warehouse_id, name, location FROM Warehouse";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setId(rs.getInt("warehouse_id"));
                warehouse.setName(rs.getString("name"));
                warehouse.setLocation(rs.getString("location"));
                list.add(warehouse);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách kho: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Warehouse addWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO Warehouse (name, location) VALUES (?, ?)";
        HistoryDAO historyDAO = new HistoryDAO();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        warehouse.setId(generatedKeys.getInt(1));
                        // Ghi lịch sử
                        History history = new History(
                                "CREATE",
                                "Warehouse",
                                warehouse.getId(),
                                String.format("Thêm kho: %s, Địa chỉ: %s", warehouse.getName(), warehouse.getLocation())
                        );
                        history.setActionTime(LocalDateTime.now());
                        historyDAO.addHistory(history);
                        return warehouse;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm kho: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateWarehouse(Warehouse warehouse) {
        String sql = "UPDATE Warehouse SET name = ?, location = ? WHERE warehouse_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();
        // Lấy dữ liệu cũ
        Warehouse oldWarehouse = getWarehouseById(warehouse.getId());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            ps.setInt(3, warehouse.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử với chi tiết thay đổi
                StringBuilder details = new StringBuilder("Cập nhật kho: " + warehouse.getName());
                if (oldWarehouse != null) {
                    if (!warehouse.getName().equals(oldWarehouse.getName())) {
                        details.append(String.format(", Tên: %s -> %s", oldWarehouse.getName(), warehouse.getName()));
                    }
                    if (!warehouse.getLocation().equals(oldWarehouse.getLocation())) {
                        details.append(String.format(", Địa chỉ: %s -> %s", oldWarehouse.getLocation(), warehouse.getLocation()));
                    }
                }
                History history = new History(
                        "UPDATE",
                        "Warehouse",
                        warehouse.getId(),
                        details.toString()
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật kho: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteWarehouse(int id) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Medicine WHERE warehouse_id = ?";
        String sql = "DELETE FROM Warehouse WHERE warehouse_id = ?";
        HistoryDAO historyDAO = new HistoryDAO();

        // Kiểm tra ràng buộc khóa ngoại
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, id);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Không thể xóa kho: Có thuốc đang lưu trữ trong kho này.");
                }
            }
        }

        // Lấy thông tin kho trước khi xóa
        Warehouse warehouse = getWarehouseById(id);
        String details = warehouse != null ?
                String.format("Xóa kho: %s, Địa chỉ: %s", warehouse.getName(), warehouse.getLocation())
                : String.format("Xóa kho: ID %d", id);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Ghi lịch sử
                History history = new History(
                        "DELETE",
                        "Warehouse",
                        id,
                        details
                );
                history.setActionTime(LocalDateTime.now());
                historyDAO.addHistory(history);
                return true;
            }
        }
        return false;
    }

    public List<MedicineWarehouse> getMedicinesByWarehouseId(int warehouseId) {
        List<MedicineWarehouse> list = new ArrayList<>();
        String sql = "SELECT medicine_id, name, quantity, unit_id, manuDate, expDate, price FROM Medicine WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicineWarehouse medicine = new MedicineWarehouse();
                    medicine.setMedicine_id(rs.getInt("medicine_id"));
                    medicine.setName(rs.getString("name"));
                    medicine.setQuantity(rs.getInt("quantity"));
                    medicine.setUnit(rs.getString("unit_id")); // Cần kiểm tra kiểu dữ liệu
                    if (rs.getDate("manuDate") != null) {
                        medicine.setManuDate(rs.getDate("manuDate").toLocalDate());
                    }
                    if (rs.getDate("expDate") != null) {
                        medicine.setExpDate(rs.getDate("expDate").toLocalDate());
                    }
                    medicine.setPrice(rs.getFloat("price"));
                    medicine.setWarehouse_id(warehouseId);
                    list.add(medicine);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách thuốc: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}