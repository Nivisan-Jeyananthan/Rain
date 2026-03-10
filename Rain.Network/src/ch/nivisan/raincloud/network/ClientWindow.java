package ch.nivisan.raincloud.network;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private PlaceholderTextField messagTextField;
    private JTextArea txtHistory;
    private JPanel contentPanel;
    private Thread recieveThread;
    private Client client;

    private boolean running;

    public ClientWindow(final String name, final String address, final int port) {
        createWindow();
        writeConsole("Attempting to connect to: " + address + " on port " + port + " as " + name);
        client = new Client(name, address, port);

        if (!client.connected()) {
            writeConsole("Connectioin failed!");
            return;
        }

        running = true;
        listen();
        printWelcomeMessage();
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
        gblContentPanel.columnWidths = new int[] { 10, 850, 30, 10 };
        gblContentPanel.rowHeights = new int[] { 35, 475, 40 };
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
        gbcHistory.weightx = 1;
        gbcHistory.weighty = 1;
        contentPanel.add(scroll, gbcHistory);

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
        gbcBtnSend.insets = new Insets(0, 0, 0, 5);
        gbcBtnSend.gridx = 2;
        gbcBtnSend.gridy = 2;
        gbcBtnSend.weightx = 0;
        gbcBtnSend.weighty = 0;
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                running = false;
                sendMessage();
            }
        });
        contentPanel.add(btnSend, gbcBtnSend);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                running = false;
                client.quit();
            }
        });

        setVisible(true);
        requestFocus();
        messagTextField.requestFocusInWindow();
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
                    String message = client.recieveBytes();
                     if (message.startsWith("/m/")) {
                        String text = message.split("/m/|/e/")[1];
                        writeConsole(text);
                    }else if (message.startsWith("/d/")) {
                    	dispose();
                    	new Login();
            		}
                }

            }
        };
        recieveThread.start();
    }

    private void writeConsole(String message) {
        txtHistory.append(message + "\r\n");
    }

}
