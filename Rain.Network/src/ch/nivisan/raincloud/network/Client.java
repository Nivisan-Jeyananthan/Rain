package ch.nivisan.raincloud.network;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame {
    private static final long serialVersionUID = 1L;

    private PlaceholderTextField messagTextField;
    private JTextArea txtHistory;
    private JPanel contentPanel;
    private DefaultCaret caret;

    private NetworkUtils networkUtils;
    private final String name;
    private final String address;
    private final int port;

    public Client(String name, String address, int port) {
        this.port = port;
        this.name = name;
        this.address = address;
        networkUtils = new NetworkUtils(address, port);

        if (!networkUtils.connected()) {
            writeConsole("Connectioin failed!");
            return;
        }

        createWindow();
        writeConsole("Attempting to connect to: " + address + " on port " + port + " as " + name);
        String data = "/c/" + name;
        networkUtils.sendBytes(data.getBytes());

        printWelcomeMessage();
    }

    private void printWelcomeMessage() {
        writeConsole("Connected successfully! ");
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
        gblContentPanel.columnWidths = new int[] { 10, 850, 30, 10 };
        gblContentPanel.rowHeights = new int[] { 35, 475, 40 };
        gblContentPanel.columnWeights = new double[] { 1.0, 1.0 };
        gblContentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        contentPanel.setLayout(gblContentPanel);

        txtHistory = new JTextArea();
        txtHistory.setBorder(BorderFactory.createCompoundBorder(txtHistory.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtHistory.setEditable(false);

        JScrollPane scroll = new JScrollPane(txtHistory);

        GridBagConstraints gbcHistory = new GridBagConstraints();
        gbcHistory.fill = GridBagConstraints.BOTH;
        gbcHistory.gridx = 0;
        gbcHistory.gridy = 0;
        gbcHistory.gridwidth = 3;
        gbcHistory.gridheight = 2;
        contentPanel.add(scroll, gbcHistory);

        messagTextField = new PlaceholderTextField();
        messagTextField.setPlaceholder("Write a message...");
        GridBagConstraints gbcMessage = new GridBagConstraints();
        gbcMessage.insets = new Insets(0, 0, 0, 5);
        gbcMessage.fill = GridBagConstraints.HORIZONTAL;
        gbcMessage.gridx = 0;
        gbcMessage.gridy = 2;
        gbcMessage.gridwidth = 2;
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
        gbcBtnSend.insets = new Insets(0, 0, 0, 5);
        gbcBtnSend.gridx = 2;
        gbcBtnSend.gridy = 2;
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendMessage();
            }
        });
        contentPanel.add(btnSend, gbcBtnSend);

        setVisible(true);
        requestFocus();
        messagTextField.requestFocusInWindow();
    }

    private void sendMessage() {
        String message = messagTextField.getText();
        if (message.trim().length() == 0)
            return;

        message = name + " : " + message;
        writeConsole(message);
        message = "/m/" + message;
        networkUtils.sendBytes(message.getBytes());
        txtHistory.setCaretPosition(txtHistory.getDocument().getLength());
        messagTextField.setText(null);
    }

    private void writeConsole(String message) {
        txtHistory.append(message + "\r\n");
    }

}
