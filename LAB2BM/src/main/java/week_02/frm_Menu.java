package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_Menu extends JFrame {

    private JButton btn_des, btn_tripledes, btn_aes, btn_rc4, btn_twofish, btn_aesctr;
    private JButton btn_sql, btn_history;

    public frm_Menu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("LAB 2 - Mã hóa đối xứng hiện đại");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("LAB 2 - MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Chọn một bài để mở form tương ứng:");
        gbc.gridy = 1;
        panel.add(lblSubtitle, gbc);

        gbc.gridwidth = 1;
        btn_des = new JButton("2.1 DES"); gbc.gridx = 0; gbc.gridy = 2; panel.add(btn_des, gbc);
        btn_tripledes = new JButton("2.2 TripleDES"); gbc.gridx = 1; panel.add(btn_tripledes, gbc);

        btn_aes = new JButton("2.3 AES + Login"); gbc.gridx = 0; gbc.gridy = 3; panel.add(btn_aes, gbc);
        btn_rc4 = new JButton("2.4 RC4"); gbc.gridx = 1; panel.add(btn_rc4, gbc);

        btn_twofish = new JButton("2.5 Twofish"); gbc.gridx = 0; gbc.gridy = 4; panel.add(btn_twofish, gbc);
        btn_aesctr = new JButton("2.6 Nâng cao - AES-CTR"); gbc.gridx = 1; panel.add(btn_aesctr, gbc);

        btn_sql = new JButton("Kết nối SQL Server"); gbc.gridx = 0; gbc.gridy = 5; panel.add(btn_sql, gbc);
        btn_history = new JButton("Xem lịch sử"); gbc.gridx = 1; panel.add(btn_history, gbc);

        JLabel lblPython = new JLabel("Bài nâng cao 02 (AES-CBC) viết bằng Python: chạy tệp aes_cbc_gui.py.");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(lblPython, gbc);

        btn_des.addActionListener(e -> new frm_DES().setVisible(true));
        btn_tripledes.addActionListener(e -> new frm_TripleDES().setVisible(true));
        btn_aes.addActionListener(e -> new frm_AES().setVisible(true));
        btn_rc4.addActionListener(e -> new frm_RC4().setVisible(true));
        btn_twofish.addActionListener(e -> new frm_Twofish().setVisible(true));
        btn_aesctr.addActionListener(e -> new frm_AES_CTR().setVisible(true));
        btn_sql.addActionListener(e -> new frm_KetNoiSQL().setVisible(true));
        btn_history.addActionListener(e -> new frm_LichSu().setVisible(true));

        setContentPane(panel);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frm_Menu().setVisible(true));
    }
}