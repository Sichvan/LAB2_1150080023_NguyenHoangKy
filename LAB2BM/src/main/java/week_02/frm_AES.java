package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_AES extends JFrame {

    private String currentUser = null;

    private JTextField txt_username;
    private JPasswordField txt_password;
    private JTextField txt_ticket;
    private JTextArea txt_plaintext;
    private JTextField txt_key;
    private JTextArea txt_ciphertext;
    private JButton btn_sql, btn_register, btn_login;
    private JButton btn_encrypt, btn_decrypt, btn_save, btn_open;
    private JLabel lbl_status;

    public frm_AES() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("AES Cipher - Register / Login");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("AES-GCM - ĐĂNG KÝ / ĐĂNG NHẬP (SQL SERVER)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Username:"), gbc);
        txt_username = new JTextField("sinhvien01", 20);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txt_username, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password:"), gbc);
        txt_password = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txt_password, gbc);
        gbc.gridx = 2; gbc.gridy = 2; panel.add(new JLabel("(ít nhất 8 ký tự)"), gbc);

        JPanel btnTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btn_sql = new JButton("Kết nối SQL Server");
        btn_register = new JButton("Register");
        btn_login = new JButton("Login");
        btnTopPanel.add(btn_sql); btnTopPanel.add(btn_register); btnTopPanel.add(btn_login);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnTopPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Ticket:"), gbc);
        txt_ticket = new JTextField(35);
        txt_ticket.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(txt_ticket, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Plaintext:"), gbc);
        txt_plaintext = new JTextArea(3, 45);
        txt_plaintext.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(new JScrollPane(txt_plaintext), gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Key AES:"), gbc);
        txt_key = new JTextField(22);
        gbc.gridx = 1; gbc.gridy = 6; panel.add(txt_key, gbc);
        gbc.gridx = 2; gbc.gridy = 6; panel.add(new JLabel("Mật khẩu để tạo khóa AES-256"), gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Ciphertext:"), gbc);
        txt_ciphertext = new JTextArea(3, 45);
        txt_ciphertext.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(new JScrollPane(txt_ciphertext), gbc);

        JPanel btnBotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btn_encrypt = new JButton("Encrypt"); btn_decrypt = new JButton("Decrypt");
        btn_save = new JButton("Save File"); btn_open = new JButton("Open File");
        btnBotPanel.add(btn_encrypt); btnBotPanel.add(btn_decrypt);
        btnBotPanel.add(btn_save); btnBotPanel.add(btn_open);
        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(btnBotPanel, gbc);

        lbl_status = new JLabel("Sẵn sàng.");
        gbc.gridx = 1; gbc.gridy = 9;
        panel.add(lbl_status, gbc);

        btn_sql.addActionListener(e -> new frm_KetNoiSQL().setVisible(true));

        btn_register.addActionListener(e -> {
            try {
                String user = txt_username.getText().trim();
                UserDao.register(user, txt_password.getPassword());
                HistoryDao.tryInsert("AES", "REGISTER", user, 0, 0);
                lbl_status.setText("Register thành công: " + user + ". Bấm Login để đăng nhập.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi SQL Server: " + ex.getMessage(), "Lỗi SQL Server", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_login.addActionListener(e -> {
            try {
                String user = txt_username.getText().trim();
                if (UserDao.login(user, txt_password.getPassword())) {
                    currentUser = user;
                    txt_ticket.setText(UserDao.newTicket());
                    HistoryDao.tryInsert("AES", "LOGIN", user, 0, 0);
                    lbl_status.setText("Login thành công: " + user + ". Có thể dùng Encrypt/Decrypt.");
                } else {
                    currentUser = null;
                    txt_ticket.setText("");
                    JOptionPane.showMessageDialog(this, "Sai username hoặc mật khẩu.", "Login thất bại", JOptionPane.ERROR_MESSAGE);
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi SQL Server: " + ex.getMessage(), "Lỗi SQL Server", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_encrypt.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Hãy Login trước khi dùng AES.");
                return;
            }
            String plaintext = txt_plaintext.getText();
            if (plaintext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
                return;
            }
            try {
                String ciphertext = AESCipher.encrypt(plaintext, txt_key.getText());
                txt_ciphertext.setText(ciphertext);
                lbl_status.setText("<html>Đã mã hóa AES. " + HistoryDao.tryInsert("AES", "ENCRYPT", currentUser, plaintext.length(), ciphertext.length()) + "</html>");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Mã hóa thất bại:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_decrypt.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Hãy Login trước khi dùng AES.");
                return;
            }
            String ciphertext = txt_ciphertext.getText();
            if (ciphertext.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
                return;
            }
            try {
                String plaintext = AESCipher.decrypt(ciphertext, txt_key.getText());
                txt_plaintext.setText(plaintext);
                lbl_status.setText("<html>Đã giải mã AES. " + HistoryDao.tryInsert("AES", "DECRYPT", currentUser, ciphertext.length(), plaintext.length()) + "</html>");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Giải mã thất bại: sai Key hoặc bản mã bị sửa.\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_save.addActionListener(e -> {
            String path = FileTextIO.saveText(this, txt_ciphertext, "aes_ciphertext.txt");
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
        SwingUtilities.invokeLater(() -> new frm_AES().setVisible(true));
    }
}