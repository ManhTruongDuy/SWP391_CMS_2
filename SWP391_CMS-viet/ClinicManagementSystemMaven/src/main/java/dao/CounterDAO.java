package dao;

import model.Category;
import model.Unit;
import model.MedicineCounter;
import model.Warehouse;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CounterDAO extends DBContext {
    public List<MedicineCounter> getMedicineCounter(int page, int pageSize) {
        List<MedicineCounter> medicines = new ArrayList<>();

        String sql = "SELECT medicine_id, me.name AS medicine_name, unit.unitName, cate.categoryName, " +
                "ingredient, usage, preservation, manuDate, expDate, quantity, price, " +
                "ware.name AS warehouse_name, ware.location " +
                "FROM Medicine me " +
                "JOIN Unit unit ON me.unit_id = unit.unit_id " +
                "JOIN Category cate ON me.category_id = cate.category_id " +
                "JOIN Warehouse ware ON me.warehouse_id = ware.warehouse_id " +
                "ORDER BY medicine_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            int offset = (page - 1) * pageSize;
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MedicineCounter med = new MedicineCounter();
                    med.setId(rs.getInt("medicine_id"));
                    med.setName(rs.getString("medicine_name"));

                    Unit unit = new Unit();
                    unit.setUnitName(rs.getString("unitName"));
                    med.setUnit(unit);
                    Category category = new Category();
                    category.setCategoryName(rs.getString("categoryName"));
                    med.setCategory(category);
                    med.setIngredient(rs.getString("ingredient"));
                    med.setUsage(rs.getString("usage"));
                    med.setPreservation(rs.getString("preservation"));
                    med.setManuDate(rs.getDate("manuDate"));
                    med.setExpDate(rs.getDate("expDate"));
                    med.setQuantity(rs.getInt("quantity"));
                    med.setPrice(rs.getBigDecimal("price"));
                    Warehouse ware = new Warehouse();
                    ware.setName(rs.getString("warehouse_name"));
                    ware.setLocation(rs.getString("location"));
                    med.setWarehouse(ware);
                    medicines.add(med);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }
    public int getMedicineCount() {
        String sql = "SELECT COUNT(*) FROM Medicine";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int insertPrescriptionInvoice(int invoiceId, int prescriptionId) {
        String sql = "INSERT INTO PrescriptionInvoice (invoice_id,pharmacist_id, prescription_id) VALUES (?,1,?)";
        try (
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoiceId);
            ps.setInt(2, prescriptionId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // lỗi
    }

    public void insertMedicines(int prescriptionInvoiceId, List<MedicineCounter> items) {
        String sql = "INSERT INTO Medicines (prescription_invoice_id, medicine_id, quantity, dosage) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (MedicineCounter item : items) {
                ps.setInt(1, prescriptionInvoiceId);
                ps.setInt(2, item.getId());
                ps.setInt(3, item.getQuantity());
                ps.setString(4, "");

                ps.addBatch();
            }

            ps.executeBatch(); // Insert hàng loạt

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int insertInvoice(int patientId, int medicineRecordId, double totalAmount, String status) {
        String sql = "INSERT INTO Invoice (patient_id, medicineRecord_id, issue_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, patientId);
            ps.setInt(2, medicineRecordId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDouble(4, totalAmount);
            ps.setString(5, status);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Trả về -1 nếu lỗi
    }
}
