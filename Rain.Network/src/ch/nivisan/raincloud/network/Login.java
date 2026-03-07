package ch.nivisan.raincloud.network;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField txtName;
	private PlaceholderTextField txtIpAddress;
	private PlaceholderTextField txtPort;

	public Login() {
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 380);
		setLocationRelativeTo(null);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		JLabel nameLbl = new JLabel("Name");
		nameLbl.setBounds(127, 34, 45, 16);
		contentPanel.add(nameLbl);

		txtName = new JTextField();
		txtName.setBounds(67, 60, 165, 28);
		contentPanel.add(txtName);
		txtName.setColumns(10);

		JLabel ipLabel = new JLabel("IP Address");
		ipLabel.setBounds(127, 100, 100, 16);
		contentPanel.add(ipLabel);

		txtIpAddress = new PlaceholderTextField();
		txtIpAddress.setBounds(67, 120, 165, 28);
		txtIpAddress.setPlaceholder("e.g 127.0.0.1");
		contentPanel.add(txtIpAddress);
		txtIpAddress.setColumns(10);

		JLabel portLabel = new JLabel("Port");
		portLabel.setBounds(127, 160, 100, 16);
		contentPanel.add(ipLabel);

		txtPort = new PlaceholderTextField();
		txtPort.setBounds(67, 180, 165, 28);
		txtIpAddress.setText("e.g 8080");
		contentPanel.add(txtIpAddress);
		txtIpAddress.setColumns(10);
	}
}
