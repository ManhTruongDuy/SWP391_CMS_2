package dao;
import model.Invoice;
//123
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
public class InvoiceDAO {
    private Connection conn;

    public InvoiceDAO() {
        // Code khởi tạo kết nối DB
    }

    // Thêm hóa đơn mới
    public boolean addInvoice(Invoice invoice) {
        String sql = "INSERT INTO Invoice (patient_id, medicineRecord_id, issue_date, total_amount, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoice.getPatientId());
            ps.setInt(2, invoice.getMedicineRecordId());
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getIssueDate()));
            ps.setBigDecimal(4, invoice.getTotalAmount());
            ps.setString(5, invoice.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả hóa đơn (có phân trang)
    public List<Invoice> getInvoices(int limit, int offset) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice ORDER BY issue_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("medicineRecord_id"),
                        rs.getTimestamp("issue_date").toLocalDateTime(),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status")
                );
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy hóa đơn theo ID
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM Invoice WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("medicineRecord_id"),
                        rs.getTimestamp("issue_date").toLocalDateTime(),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật hóa đơn
    public boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE Invoice SET patient_id = ?, medicineRecord_id = ?, issue_date = ?, total_amount = ?, status = ? WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoice.getPatientId());
            ps.setInt(2, invoice.getMedicineRecordId());
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getIssueDate()));
            ps.setBigDecimal(4, invoice.getTotalAmount());
            ps.setString(5, invoice.getStatus());
            ps.setInt(6, invoice.getInvoiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa hóa đơn
    public boolean deleteInvoice(int id) {
        String sql = "DELETE FROM Invoice WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
