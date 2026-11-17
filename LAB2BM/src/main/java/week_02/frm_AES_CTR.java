package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_AES_CTR extends JFrame {

    private JTextArea txt_plaintext;
    private JTextField txt_key;
    private JButton btn_genkey;
    private JTextField txt_nonce;
    private JButton btn_gennonce;
    private JTextArea txt_ciphertext;
    private JButton btn_encrypt, btn_decrypt, btn_save, btn_open;
    private JLabel lbl_status;

    public frm_AES_CTR() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("AES-CTR Cipher");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("BÀI NÂNG CAO 01 - AES-256 CHẾ ĐỘ CTR");
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
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Key (Base64):"), gbc);
        txt_key = new JTextField(32);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txt_key, gbc);
        btn_genkey = new JButton("Generate Key");
        gbc.gridx = 2; gbc.gridy = 2; panel.add(btn_genkey, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Nonce (Base64):"), gbc);
        txt_nonce = new JTextField(32);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txt_nonce, gbc);
        btn_gennonce = new JButton("Generate Nonce");
        gbc.gridx = 2; gbc.gridy = 3; panel.add(btn_gennonce, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Ciphertext:"), gbc);
        txt_ciphertext = new JTextArea(3, 45);
        txt_ciphertext.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(new JScrollPane(txt_ciphertext), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btn_encrypt = new JButton("Encrypt"); btn_decrypt = new JButton("Decrypt");
        btn_save = new JButton("Save File"); btn_open = new JButton("Open File");
        btnPanel.add(btn_encrypt); btnPanel.add(btn_decrypt);
        btnPanel.add(btn_save); btnPanel.add(btn_open);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        lbl_status = new JLabel("Sẵn sàng.");
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(lbl_status, gbc);

        btn_genkey.addActionListener(e -> {
            txt_key.setText(AESCTRCipher.newKey());
            lbl_status.setText("Đã tạo khóa ngẫu nhiên 256 bit.");
        });

        btn_gennonce.addActionListener(e -> {
            txt_nonce.setText(AESCTRCipher.newNonce());
            lbl_status.setText("Đã tạo nonce ngẫu nhiên 96 bit. Mỗi lần mã hóa nên tạo nonce mới.");
        });

        btn_encrypt.addActionListener(e -> {
            String plaintext = txt_plaintext.getText();
            if (plaintext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
                return;
            }
            try {
                String ciphertext = AESCTRCipher.encrypt(plaintext, txt_key.getText(), txt_nonce.getText());
                txt_ciphertext.setText(ciphertext);
                lbl_status.setText("<html>Đã mã hóa AES-CTR. " + HistoryDao.tryInsert("AES-CTR", "ENCRYPT", null, plaintext.length(), ciphertext.length()) + "</html>");
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
                String plaintext = AESCTRCipher.decrypt(ciphertext, txt_key.getText(), txt_nonce.getText());
                txt_plaintext.setText(plaintext);
                lbl_status.setText("<html>Đã giải mã AES-CTR. " + HistoryDao.tryInsert("AES-CTR", "DECRYPT", null, ciphertext.length(), plaintext.length()) + "</html>");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Giải mã thất bại: sai Key hoặc bản mã bị sửa.\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_save.addActionListener(e -> {
            if (txt_ciphertext.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Chưa có bản mã để lưu.");
                return;
            }
            String content = AESCTRCipher.toFile(txt_nonce.getText(), txt_ciphertext.getText());
            String path = FileTextIO.saveString(this, content, "aes_ctr_ciphertext.txt");
            if (path != null) lbl_status.setText("Đã lưu nonce + bản mã (không lưu Key) vào: " + path);
        });

        btn_open.addActionListener(e -> {
            String content = FileTextIO.openString(this);
            if (content == null) return;
            try {
                String[] parts = AESCTRCipher.fromFile(content);
                txt_nonce.setText(parts[0]);
                txt_ciphertext.setText(parts[1]);
                lbl_status.setText("Đã mở tệp. Nhập đúng Key rồi bấm Decrypt.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Tệp không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new frm_AES_CTR().setVisible(true));
    }
}