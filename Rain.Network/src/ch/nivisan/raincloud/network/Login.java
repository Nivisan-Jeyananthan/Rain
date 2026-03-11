package ch.nivisan.raincloud.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField txtName;
	private PlaceholderTextField txtIpAddress;
	private PlaceholderTextField txtPort;

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

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
		contentPanel.add(portLabel);

		txtPort = new PlaceholderTextField();
		txtPort.setBounds(67, 180, 165, 28);
		txtPort.setPlaceholder("e.g 8080");
		contentPanel.add(txtPort);
		txtPort.setColumns(10);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(91, 280, 117, 29);
		contentPanel.add(loginButton);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String name = txtName.getText();
				String ipaddress = txtIpAddress.getText();
				String portText = txtPort.getText().trim().length() > 0 ? txtPort.getText() : "8080";
				int port = Integer.parseInt(portText);
				login(name, ipaddress, port);
			}

		});

		setVisible(true);
	}

	private void login(String name, String ipaddress, int port) {
		dispose();
		System.out.println("Name: " + name);
		System.out.println("IP: " + ipaddress);
		System.out.println("Port: " + port);

		if (ipaddress.trim().length() == 0)
			ipaddress = "localhost";

		new ClientWindow(name, ipaddress, port);
	}
}
