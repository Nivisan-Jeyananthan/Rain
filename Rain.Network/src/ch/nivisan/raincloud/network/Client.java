package ch.nivisan.raincloud.network;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPanel;
    private final String name;
    private final String address;
    private final int port;

    public Client(String name, String address, int port) {
        this.port = port;
        this.name = name;
        this.address = address;

        createWindow();
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
        setContentPane(contentPanel);

        GridBagLayout gblContentPanel = new GridBagLayout();
        gblContentPanel.columnWidths = new int[] { 10, 850, 30, 10 };
        gblContentPanel.rowHeights = new int[] { 35, 475, 40 };
        gblContentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gblContentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        contentPanel.setLayout(gblContentPanel);

        JTextArea txtHistory = new JTextArea();
        txtHistory.setBorder(BorderFactory.createCompoundBorder(txtHistory.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtHistory.setText("history");

        GridBagConstraints gbcHistory = new GridBagConstraints();
        gbcHistory.fill = GridBagConstraints.BOTH;
        gbcHistory.gridx = 1;
        gbcHistory.gridy = 1;
        contentPanel.add(txtHistory, gbcHistory);

        PlaceholderTextField messagTextField = new PlaceholderTextField();
        messagTextField.setPlaceholder("Write a message...");
        GridBagConstraints gbcMessage = new GridBagConstraints();
        gbcMessage.insets = new Insets(0, 0, 0, 5);
        gbcMessage.fill = GridBagConstraints.HORIZONTAL;
        gbcMessage.gridx = 1;
        gbcMessage.gridy = 2;
        contentPanel.add(messagTextField, gbcMessage);
        messagTextField.setColumns(10);

        JButton btnSend = new JButton("Send");
        GridBagConstraints gbcBtnSend = new GridBagConstraints();
        gbcHistory.gridx = 1;
        gbcHistory.gridy = 2;
        contentPanel.add(btnSend, gbcBtnSend);

        setVisible(true);
    }

}
