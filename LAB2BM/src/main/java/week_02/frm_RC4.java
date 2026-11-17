package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_RC4 extends JFrame {

    private JTextArea txt_plaintext;
    private JTextField txt_key;
    private JTextArea txt_ciphertext;
    private JButton btn_encrypt, btn_decrypt, btn_save, btn_open;
    private JLabel lbl_status;

    public frm_RC4() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("RC4 Cipher");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("MẬT MÃ DÒNG RC4");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Plaintext:"), gbc);
        txt_plaintext = new JTextArea(3, 45);
        txt_plaintext.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(new JScrollPane(txt_plaintext), gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Key:"), gbc);
        txt_key = new JTextField("Key", 22);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txt_key, gbc);
        gbc.gridx = 2; gbc.gridy = 2; panel.add(new JLabel("1 - 256 byte; bản mã hiển thị dạng HEX"), gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Ciphertext:"), gbc);
        txt_ciphertext = new JTextArea(3, 45);
        txt_ciphertext.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(new JScrollPane(txt_ciphertext), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btn_encrypt = new JButton("Encrypt"); btn_decrypt = new JButton("Decrypt");
        btn_save = new JButton("Save File"); btn_open = new JButton("Open File");
        btnPanel.add(btn_encrypt); btnPanel.add(btn_decrypt);
        btnPanel.add(btn_save); btnPanel.add(btn_open);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        lbl_status = new JLabel("Sẵn sàng.");
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(lbl_status, gbc);

        btn_encrypt.addActionListener(e -> {
            String plaintext = txt_plaintext.getText();
            if (plaintext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
                return;
            }
            try {
                String ciphertext = RC4Cipher.encrypt(plaintext, txt_key.getText());
                txt_ciphertext.setText(ciphertext);
                lbl_status.setText("<html>Đã mã hóa RC4. " + HistoryDao.tryInsert("RC4", "ENCRYPT", null, plaintext.length(), ciphertext.length()) + "</html>");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Mã hóa thất bại:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_decrypt.addActionListener(e -> {
            String ciphertext = txt_ciphertext.getText();
            if (ciphertext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
                return;
            }
            try {
                String plaintext = RC4Cipher.decrypt(ciphertext, txt_key.getText());
                txt_plaintext.setText(plaintext);
                lbl_status.setText("<html>Đã giải mã RC4. " + HistoryDao.tryInsert("RC4", "DECRYPT", null, ciphertext.length(), plaintext.length()) + "</html>");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Giải mã thất bại: sai Key hoặc bản mã bị sửa.\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_save.addActionListener(e -> {
            String path = FileTextIO.saveText(this, txt_ciphertext, "rc4_ciphertext.txt");
            if (path != null) lbl_status.setText("Đã lưu tệp: " + path);
        });

        btn_open.addActionListener(e -> {
            String path = FileTextIO.openText(this, txt_ciphertext);
            if (path != null) lbl_status.setText("Đã mở tệp: " + path);
        });

        setContentPane(panel);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frm_RC4().setVisible(true));
    }
}