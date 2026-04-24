package ch.nivisan.raincloud.network.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import ch.nivisan.raincloud.network.utilities.PlaceholderTextField;

class ClientWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private PlaceholderTextField messagTextField;
	private JTextArea txtHistory;
	private JPanel contentPanel;
	private Thread recieveThread;
	private final Client client;
	private final OnlineUsersWindow usersWindow;
	private final MicRecorderToggle settingsWindow;

	private boolean running;

	ClientWindow(final String name, final String address, final int port) {
		createWindow();
		usersWindow = new OnlineUsersWindow();
		settingsWindow = new MicRecorderToggle();
		writeConsole("Attempting to connect to: " + address + " on port " + port + " as " + name);
		client = new Client(name, address, port);

		new Thread("connect") {
			public void run() {
				if (!client.connect()) {
					SwingUtilities.invokeLater(() -> writeConsole("Connection failed!"));
					return;
				}

				running = true;
				listen();
				SwingUtilities.invokeLater(() -> printWelcomeMessage());
			}
		}.start();
	}

	private void printWelcomeMessage() {
		writeConsole("You can start chatting.");
		writeConsole("------------------------");
	}

	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("Rain Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 550);
		setLocationRelativeTo(null);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);

		GridBagLayout gblContentPanel = new GridBagLayout();
		gblContentPanel.columnWidths = new int[] { 10, 850, 30, 30, 10 };
		gblContentPanel.rowHeights = new int[] { 35, 475, 40 };
		contentPanel.setLayout(gblContentPanel);

		createMenu();

		createTextHistory();

		createMessageArea();

		setVisible(true);
		requestFocus();
		messagTextField.requestFocusInWindow();
	}

	private void createMessageArea() {
		messagTextField = new PlaceholderTextField();
		messagTextField.setPlaceholder("Write a message...");
		GridBagConstraints gbcMessage = new GridBagConstraints();
		gbcMessage.insets = new Insets(0, 0, 0, 5);
		gbcMessage.fill = GridBagConstraints.HORIZONTAL;
		gbcMessage.gridx = 0;
		gbcMessage.gridy = 2;
		gbcMessage.gridwidth = 2;
		gbcMessage.weightx = 1;
		gbcMessage.weighty = 0;
		messagTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER)
					sendMessage();
			}
		});
		contentPanel.add(messagTextField, gbcMessage);
		messagTextField.setColumns(10);

		JButton btnSend = new JButton("Send");
		GridBagConstraints gbcBtnSend = new GridBagConstraints();
		gbcBtnSend.insets = new Insets(0, 0, 0, 0);
		gbcBtnSend.gridx = 2;
		gbcBtnSend.gridy = 2;
		gbcBtnSend.weightx = 0;
		gbcBtnSend.weighty = 0;
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				running = true;
				sendMessage();
			}
		});
		contentPanel.add(btnSend, gbcBtnSend);

		JButton btnRecord = new JButton("၊၊||၊");
		GridBagConstraints gbcBtnRecord = new GridBagConstraints();
		gbcBtnRecord.insets = new Insets(0, 0, 0, 5);
		gbcBtnRecord.gridx = 3;
		gbcBtnRecord.gridy = 2;
		gbcBtnRecord.weightx = 0;
		gbcBtnRecord.weighty = 0;
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				running = true;
				if (!client.getMicRunning()) {
					client.sendAudio();
					btnRecord.setText("🔴");
				} else {
					btnRecord.setText("၊၊||၊");
					client.closeAudio();
				}
			}
		});
		contentPanel.add(btnRecord, gbcBtnRecord);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				running = false;
				client.closeAudio();
				client.quit(false);
			}
		});
	}

	/**
	 * Creates the text history, where said messages will appear
	 */
	private void createTextHistory() {
		txtHistory = new JTextArea();
		txtHistory.setBorder(BorderFactory.createCompoundBorder(txtHistory.getBorder(),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		txtHistory.setEditable(false);

		JScrollPane scroll = new JScrollPane(txtHistory);

		GridBagConstraints gbcHistory = new GridBagConstraints();
		gbcHistory.fill = GridBagConstraints.BOTH;
		gbcHistory.gridx = 0;
		gbcHistory.gridy = 0;
		gbcHistory.gridwidth = 4;
		gbcHistory.gridheight = 2;
		gbcHistory.weightx = 1;
		gbcHistory.weighty = 1;
		contentPanel.add(scroll, gbcHistory);
	}

	/**
	 * Creates the Menu entries and menu itself
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("Window");
		menuBar.add(menu);

		JMenuItem settings = new JMenuItem("Settings");
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settingsWindow.setVisible(true);
			}
		});
		menu.add(settings);

		JMenuItem onlineUsersItem = new JMenuItem("Online Users");
		onlineUsersItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				client.requestUsernames();
				usersWindow.setVisible(true);
			}
		});

		menu.add(onlineUsersItem);

	}

	private void sendMessage() {
		String message = messagTextField.getText();
		if (message.trim().length() == 0)
			return;

		message = client.name + " : " + message;
		client.sendText(message);
		txtHistory.setCaretPosition(txtHistory.getDocument().getLength());
		messagTextField.setText(null);
	}

	/**
	 * Listen for incoming traffic
	 */
	private void listen() {
		recieveThread = new Thread("recieve") {
			public void run() {
				while (running) {
					String message = client.getBytes();
					if (message == null || message.isEmpty())
						continue;

					if (message.startsWith("/e/")) {
						int endIndex = message.lastIndexOf("/e/");
						if (endIndex > 3) {
							message = message.substring(3, endIndex);
						}
					}

					if (message.startsWith("/m/")) {
						int endIndex = message.indexOf("/m/", 3);
						if (endIndex > 3) {
							writeConsole(message.substring(3, endIndex));
						}
					} else if (message.startsWith("/u/")) {
						int endIndex = message.indexOf("/u/", 3);
						if (endIndex < 0)
							endIndex = message.length();
						String payload = message.substring(3, endIndex);
						if (payload.isBlank()) {
							usersWindow.updateUsers(new String[0]);
						} else {
							String[] users = payload.split("/n/");
							usersWindow.updateUsers(users);
						}
					} else if (message.startsWith("/d/")) {
						dispose();
						client.quit(true);
						new Login();
					}
				}
			}
		};
		recieveThread.start();
	}

	private void writeConsole(String message) {
		if (SwingUtilities.isEventDispatchThread()) {
			txtHistory.append(message + "\r\n");
		} else {
			SwingUtilities.invokeLater(() -> txtHistory.append(message + "\r\n"));
		}
	}
}
