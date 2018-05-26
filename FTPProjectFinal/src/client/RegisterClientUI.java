package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;

import lib.LibraryConnectDb;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class RegisterClientUI extends JFrame {
	static RegisterClientUI frame;
	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblPath;
	private JTextField textField_4;
	private JButton btnNewButton;
	private JLabel lblQuyn;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textField_1;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel_1;
	private LibraryConnectDb connectDb;
	private java.sql.Connection conn;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new RegisterClientUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterClientUI() {
		connectDb = new LibraryConnectDb();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tên đăng nhập");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(34, 97, 110, 19);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(173, 93, 240, 31);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblFullname = new JLabel("Họ tên");
		lblFullname.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFullname.setBounds(34, 146, 88, 19);
		contentPane.add(lblFullname);
		
		JLabel lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(34, 196, 88, 19);
		contentPane.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu");
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConfirmPassword.setBounds(34, 249, 129, 19);
		contentPane.add(lblConfirmPassword);
		
		lblPath = new JLabel("Đường dẫn");
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPath.setBounds(34, 298, 88, 19);
		contentPane.add(lblPath);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_4.setColumns(10);
		textField_4.setBounds(173, 294, 240, 31);
		contentPane.add(textField_4);
		
		btnNewButton = new JButton("Duyệt");
		btnNewButton.setBounds(436, 298, 67, 23);
		contentPane.add(btnNewButton);
		
		lblQuyn = new JLabel("Quyền");
		lblQuyn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuyn.setBounds(34, 339, 88, 19);
		contentPane.add(lblQuyn);
		
		
		final JRadioButton rdbtnNewRadioButton = new JRadioButton("Chỉ tải");
		rdbtnNewRadioButton.setBounds(173, 339, 109, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		final JRadioButton rdbtnTonQuyn = new JRadioButton("Toàn quyền");
		rdbtnTonQuyn.setBounds(173, 365, 109, 23);
		contentPane.add(rdbtnTonQuyn);
		
		final ButtonGroup group = new ButtonGroup();
		group.add(rdbtnNewRadioButton);
		group.add(rdbtnTonQuyn);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField.setBounds(173, 192, 240, 31);
		contentPane.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField_1.setBounds(173, 245, 240, 31);
		contentPane.add(passwordField_1);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_1.setColumns(10);
		textField_1.setBounds(173, 142, 240, 31);
		contentPane.add(textField_1);
		
		btnNewButton_1 = new JButton("ĐĂNG KÝ");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String fullname = textField_1.getText();
				String password = new String(passwordField.getPassword());
				String repassword = new String(passwordField_1.getPassword());
				String path = textField_4.getText();
				rdbtnNewRadioButton.setActionCommand("onlydown");
				rdbtnTonQuyn.setActionCommand("allrule");
				int role = group.getSelection().getActionCommand().equals("onlydown")?0:1;
				
				int result = 0;
				String sql ="INSERT INTO users(username,password,fullname,path,rule) VALUES(?, ?, ?, ?, ?)";
				conn = connectDb.getConnectMySql();
				try {
					pst = conn.prepareStatement(sql);
					pst.setString(1, username);
					pst.setString(2, password);
					pst.setString(3, fullname);
					pst.setString(4, path);
					pst.setInt(5, role);
					result = pst.executeUpdate();
				} catch (SQLException exception) {
					exception.printStackTrace();
				}finally{
					try{
						pst.close();
						conn.close();
					}catch(SQLException exception2){
						exception2.printStackTrace();
					}
				}
				
				if(result==1){
					JOptionPane.showMessageDialog(contentPane, "Đăng ký thành công, mời bạn đăng nhập.");
					frame.dispose();
				}else{
					JOptionPane.showMessageDialog(contentPane, "Đăng ký không thành công.");
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_1.setBounds(217, 422, 129, 37);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel_1 = new JLabel("Đăng ký tài khoản");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblNewLabel_1.setBounds(173, 11, 259, 42);
		contentPane.add(lblNewLabel_1);
	}
}
