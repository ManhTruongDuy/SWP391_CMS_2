package dao;

import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CounterDAO extends DBContext {

    /**
     * Truy vấn danh sách thuốc có phân trang.
     * Kết hợp thông tin từ các bảng Medicine, Unit, Category, Warehouse.
     */
    public List<MedicineCounter> getMedicineCounter(int page, int pageSize) {
        List<MedicineCounter> medicines = new ArrayList<>();

        String sql = "SELECT medicine_id, me.name AS medicine_name, unit.unitName, cate.categoryName, " +
                "ingredient, usage, preservation, manuDate, expDate, quantity, price, " +
                "ware.name AS warehouse_name, ware.location " +
                "FROM Medicine me " +
                "JOIN Unit unit ON me.unit_id = unit.unit_id " +
                "JOIN Category cate ON me.category_id = cate.category_id " +
                "JOIN Warehouse ware ON me.warehouse_id = ware.warehouse_id " +
                "ORDER BY medicine_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Sử dụng phân trang

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
            e.printStackTrace(); // Nên thay bằng logging
        }

        return medicines;
    }

    /**
     * Đếm tổng số thuốc hiện có trong bảng Medicine.
     */
    public int getMedicineCount() {
        String sql = "SELECT COUNT(*) FROM Medicine";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên log chi tiết
        }
        return 0;
    }

    /**
     * Thêm một bản ghi PrescriptionInvoice (liên kết giữa đơn thuốc và hóa đơn).
     * @return trả về ID được sinh ra nếu thành công, -1 nếu lỗi.
     */
    public int insertPrescriptionInvoice(int invoiceId, int prescriptionId) {
        String sql = "INSERT INTO PrescriptionInvoice (invoice_id, pharmacist_id, prescription_id) VALUES (?, 1, ?)"; // pharmacist_id hardcoded

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

        return -1;
    }

    /**
     * Thêm danh sách thuốc vào bảng Medicines theo prescriptionInvoiceId.
     */
    public void insertMedicines(int prescriptionInvoiceId, List<MedicineCounter> items) {
        String sql = "INSERT INTO Medicines (prescription_invoice_id, medicine_id, quantity, dosage) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (MedicineCounter item : items) {
                ps.setInt(1, prescriptionInvoiceId);
                ps.setInt(2, item.getId());
                ps.setInt(3, item.getQuantity());
                ps.setString(4, ""); // dosage đang để trống - cần xử lý sau

                ps.addBatch(); // Thêm vào batch để xử lý hàng loạt
            }

            ps.executeBatch(); // Thực hiện insert hàng loạt
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thêm một hóa đơn mới.
     * @return trả về invoice_id nếu thành công.
     */
    public int insertInvoice(int patientId, int medicineRecordId, double totalAmount, String status, Long pid) {
        String sql = "INSERT INTO Invoice (patient_id, medicineRecord_id, issue_date, total_amount, status, pid) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            ps.setInt(2, medicineRecordId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now())); // Ngày phát hành là thời điểm hiện tại
            ps.setDouble(4, totalAmount);
            ps.setString(5, status);
            ps.setLong(6, pid);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Cập nhật trạng thái hóa đơn.
     */
    public int updateInvoiceStatus(int invoiceId, String status) {
        String sql = "UPDATE Invoice SET status = ? WHERE invoice_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Tìm ID hóa đơn dựa vào pid (lịch khám).
     */
    public int findInvoiceIdByPid(Long pid) {
        String sql = "SELECT invoice_id FROM Invoice WHERE pid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("invoice_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Tìm prescription_id từ invoice_id trong bảng PrescriptionInvoice.
     */
    public int findPrescriptionIdByInvoiceId(int id) {
        String sql = "SELECT prescription_id FROM PrescriptionInvoice WHERE invoice_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("prescription_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Cập nhật trạng thái của đơn thuốc.
     */
    public int updatePrescriptionStatus(int prescriptionId, String status) {
        String sql = "UPDATE Prescription SET status = ? WHERE prescription_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, prescriptionId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lấy danh sách tất cả các hóa đơn có pid, kèm thông tin bệnh nhân và bác sĩ kê đơn.
     */
    public List<Invoice> getAllWithPid() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT i.invoice_id, i.patient_id, i.medicineRecord_id, issue_date, total_amount, i.status ,p.full_name AS patientName,doc.full_name AS phaName " +
                "FROM Invoice i " +
                "JOIN Patient p ON i.patient_id = p.patient_id " +
                "JOIN PrescriptionInvoice pre ON pre.invoice_id = i.invoice_id " +
                "JOIN Prescription prec ON pre.prescription_id = prec.prescription_id " +
                "JOIN Doctor doc ON doc.doctor_id = prec.doctor_id " +
                "WHERE pid IS NOT NULL ORDER BY issue_date DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("medicineRecord_id"),
                        rs.getTimestamp("issue_date").toLocalDateTime(),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getString("patientName"),
                        rs.getString("phaName")
                );
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trả về danh sách thuốc theo invoiceId.
     */
    public List<MedicineRes> getMedicineByInvoiceId(int invoiceId) {
        List<MedicineRes> list = new ArrayList<>();
        String sql = "SELECT med.medicine_id,med.name,med.price,med.manuDate,med.expDate,me.quantity " +
                "FROM PrescriptionInvoice pre " +
                "JOIN Medicines me ON me.prescription_invoice_id = pre.prescription_invoice_id " +
                "JOIN Medicine med ON med.medicine_id = me.medicine_id " +
                "WHERE pre.invoice_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, invoiceId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                MedicineRes medicine = new MedicineRes(
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDate("manuDate"),
                        rs.getDate("expDate"),
                        rs.getInt("quantity")
                );
                list.add(medicine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Tìm thuốc theo công dụng (sử dụng LIKE N'%usage%').
     */
    public List<MedicineCounter> findByUsage(String usage) {
        List<MedicineCounter> medicines = new ArrayList<>();
        String sql = "SELECT * FROM Medicine WHERE [usage] LIKE N?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + usage + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MedicineCounter med = new MedicineCounter();
                    med.setId(rs.getInt("medicine_id"));
                    med.setName(rs.getString("name"));
                    med.setManuDate(rs.getDate("manuDate"));
                    med.setExpDate(rs.getDate("expDate"));
                    med.setUsage(rs.getString("usage"));
                    med.setIngredient(rs.getString("ingredient"));

                    medicines.add(med);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * Trả về thống kê tổng hợp trong khoảng thời gian: số hóa đơn, số bệnh nhân, tổng tiền.
     * Đồng thời trả về danh sách hóa đơn chi tiết.
     */
    public StatisticCounter getStatisticCounter(Date startDate, Date endDate) {
        String statSql = "SELECT COUNT(i.invoice_id) AS total_invoices, SUM(i.total_amount) AS total_amount, COUNT(DISTINCT i.patient_id) AS total_patient " +
                "FROM Invoice AS i " +
                "WHERE i.status = 'Paid' AND i.pid IS NOT NULL " +
                "AND issue_date >= ? AND issue_date <= ?";

        String listSql = "SELECT invoice_id, issue_date, total_amount, status " +
                "FROM Invoice " +
                "WHERE pid IS NOT NULL AND issue_date >= ? AND issue_date <= ?";

        PreparedStatement statStmt = null;
        PreparedStatement listStmt = null;
        ResultSet statRs = null;
        ResultSet listRs = null;

        try {
            statStmt = connection.prepareStatement(statSql);
            statStmt.setDate(1, startDate);
            statStmt.setDate(2, endDate);
            statRs = statStmt.executeQuery();

            int totalInvoices = 0;
            double totalAmount = 0;
            int totalPatients = 0;

            if (statRs.next()) {
                totalInvoices = statRs.getInt("total_invoices");
                totalAmount = statRs.getDouble("total_amount");
                totalPatients = statRs.getInt("total_patient");
            }

            listStmt = connection.prepareStatement(listSql);
            listStmt.setDate(1, startDate);
            listStmt.setDate(2, endDate);
            listRs = listStmt.executeQuery();

            List<Invoice> invoices = new ArrayList<>();
            while (listRs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(listRs.getInt("invoice_id"));
                invoice.setIssueDate(listRs.getTimestamp("issue_date").toLocalDateTime());
                invoice.setTotalAmount(listRs.getBigDecimal("total_amount"));
                invoice.setStatus(listRs.getString("status"));
                invoices.add(invoice);
            }

            // --- Thêm thống kê thuốc ---
            int totalMedicine = getMedicineCount();
            int totalExpiringMedicine = 0;
            String expSql = "SELECT SUM(quantity) FROM Medicine WHERE expDate <= ?";
            try (PreparedStatement expStmt = connection.prepareStatement(expSql)) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.YEAR, 3);
                java.sql.Date threeYearsLater = new java.sql.Date(cal.getTimeInMillis());
                expStmt.setDate(1, threeYearsLater);
                try (ResultSet expRs = expStmt.executeQuery()) {
                    if (expRs.next()) {
                        totalExpiringMedicine = expRs.getInt(1);
                    }
                }
            }
            // ---

            return new StatisticCounter(totalInvoices, totalAmount, totalPatients, invoices, totalMedicine, totalExpiringMedicine);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo đóng các tài nguyên JDBC
            try {
                if (statRs != null) statRs.close();
                if (listRs != null) listRs.close();
                if (statStmt != null) statStmt.close();
                if (listStmt != null) listStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
