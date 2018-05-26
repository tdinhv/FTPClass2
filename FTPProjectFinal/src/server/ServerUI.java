package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.ServerSocket;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class ServerUI extends JFrame {
	private static ServerSocket socfd;
	private static FTPServer t;
	private JPanel contentPane;
	
	private DataInputStream din;
	private DataOutputStream dout;   

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI frame = new ServerUI();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			socfd = new ServerSocket(4000);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("FTP Server Started on Port Number 21");
        while(true)
        {
            System.out.println("Waiting for Connection ...");
            try {
				t=new FTPServer(socfd.accept());
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}            
        }
	}

	/**
	 * Create the frame.
	 */
	public ServerUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Đăng ký tài khoản");
		btnNewButton.setBounds(48, 86, 135, 58);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("FTP ADMIN");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 36));
		lblNewLabel.setBounds(127, 11, 199, 43);
		contentPane.add(lblNewLabel);
		
		JButton btnManageAccount = new JButton("Quản lý tài khoản");
		btnManageAccount.setBounds(254, 86, 135, 58);
		contentPane.add(btnManageAccount);
		
		JButton btnExit = new JButton("Thoát");
		btnExit.setBounds(300, 216, 89, 34);
		contentPane.add(btnExit);
	}
	
}
