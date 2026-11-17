package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_LichSu extends JFrame {

    private JTable tbl_history;
    private JButton btn_refresh;
    private JLabel lbl_status;

    public frm_LichSu() {
        initComponents();
        setLocationRelativeTo(null);
        tbl_history.setModel(HistoryDao.emptyModel());
        loadData();
    }

    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải " + tbl_history.getRowCount() + " dòng mới nhất từ dbo.crypto_history.");
        } catch (java.sql.SQLException ex) {
            lbl_status.setText("Không đọc được lịch sử: " + ex.getMessage());
        }
    }

    private void initComponents() {
        setTitle("Lịch sử thao tác (SQL Server)");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel lblTitle = new JLabel("LỊCH SỬ THAO TÁC - BẢNG DBO.CRYPTO_HISTORY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lblTitle, BorderLayout.NORTH);

        tbl_history = new JTable();
        JScrollPane scrollPane = new JScrollPane(tbl_history);
        scrollPane.setPreferredSize(new Dimension(760, 260));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btn_refresh = new JButton("Tải lại");
        lbl_status = new JLabel("Đang tải...");
        bottomPanel.add(btn_refresh);
        bottomPanel.add(lbl_status);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        btn_refresh.addActionListener(e -> loadData());

        setContentPane(panel);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frm_LichSu().setVisible(true));
    }
}