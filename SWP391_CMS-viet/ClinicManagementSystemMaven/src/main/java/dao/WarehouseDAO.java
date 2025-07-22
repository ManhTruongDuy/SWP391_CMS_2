package dao;

import model.Medicine;
import model.MedicineWarehouse;
import model.Warehouse;
import java.sql.*;
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
            e.printStackTrace();
        }
        return list;
    }

    // Thêm kho mới và trả về ID tự động tạo
    public Warehouse addWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO Warehouse (name, location) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            if (ps.executeUpdate() > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        warehouse.setId(generatedKeys.getInt(1));
                        return warehouse;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateWarehouse(Warehouse warehouse) {
        String sql = "UPDATE Warehouse SET name = ?, location = ? WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            ps.setInt(3, warehouse.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWarehouse(int id) {
        String sql = "DELETE FROM Warehouse WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MedicineWarehouse> getMedicinesByWarehouseId(int warehouseId) {
        List<MedicineWarehouse> list = new ArrayList<>();
        String sql = "SELECT medicine_id, name, quantity,unit_id,manuDate, expDate, price FROM Medicine WHERE warehouse_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicineWarehouse medicine = new MedicineWarehouse();
                    medicine.setMedicine_id(rs.getInt("medicine_id"));
                    medicine.setName(rs.getString("name"));
                    medicine.setQuantity(rs.getInt("quantity"));
                    medicine.setUnit(rs.getString("unit_id"));
                    medicine.setManuDate(rs.getDate("manuDate").toLocalDate());
                    medicine.setExpDate(rs.getDate("expDate").toLocalDate());
                    medicine.setPrice(rs.getFloat("price"));
                    medicine.setWarehouse_id(warehouseId);
                    list.add(medicine);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
            return list;
        }
        return list;
    }
}
