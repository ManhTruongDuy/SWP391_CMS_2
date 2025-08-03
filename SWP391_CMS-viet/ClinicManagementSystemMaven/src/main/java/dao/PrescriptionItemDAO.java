package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.PrescriptionItem;

public class PrescriptionItemDAO {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public PrescriptionItemDAO() {
        conn = new DBContext().connection;
    }

    /**
     * Thêm một thuốc vào đơn thuốc
     * @param item Đối tượng PrescriptionItem cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addPrescriptionItem(PrescriptionItem item) {
        // Đã loại bỏ id_prescription_item khỏi câu lệnh INSERT vì nó là cột IDENTITY
        String sql = "INSERT INTO PrescriptionItem (prescription_id, medicine_id, quantity) VALUES (?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getPrescriptionId());
            ps.setInt(2, item.getMedicineId());
            ps.setFloat(3, item.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thêm nhiều thuốc vào đơn thuốc
     * @param items Danh sách các PrescriptionItem cần thêm
     * @param prescriptionId ID của đơn thuốc
     * @return Số lượng thuốc đã thêm thành công
     */
    public int addPrescriptionItems(List<PrescriptionItem> items, int prescriptionId) {
        int count = 0;
        for (PrescriptionItem item : items) {
            item.setPrescriptionId(prescriptionId);
            if (addPrescriptionItem(item)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Lấy danh sách thuốc trong đơn thuốc
     * @param prescriptionId ID của đơn thuốc
     * @return Danh sách các PrescriptionItem
     */
    public List<PrescriptionItem> getPrescriptionItemsByPrescriptionId(int prescriptionId) {
        List<PrescriptionItem> items = new ArrayList<>();
        String sql = "SELECT * FROM PrescriptionItem WHERE prescription_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, prescriptionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                PrescriptionItem item = new PrescriptionItem();
                // Đảm bảo tên cột khớp với tên cột thực tế trong lược đồ cơ sở dữ liệu của bạn
                item.setIdPrescriptionItem(rs.getInt("id_prescription_item"));
                item.setPrescriptionId(rs.getInt("prescription_id"));
                item.setMedicineId(rs.getInt("medicine_id"));
                item.setQuantity(rs.getFloat("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
}