package ch.nivisan.raincloud.network;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class OnlineUsersWindow extends JFrame {
	private JPanel contentPanel;
	private JList<String> usersList;

	private static final long serialVersionUID = 1L;

	public OnlineUsersWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setType(Type.UTILITY);
		setTitle("Users currently Online");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(200, 300);
		setLocationRelativeTo(null);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 0 };
		contentPanel.setLayout(gridBagLayout);

		usersList = new JList<String>();
		GridBagConstraints gbcUsersList = new GridBagConstraints();
		gbcUsersList.fill = GridBagConstraints.BOTH;
		gbcUsersList.gridx = 0;
		gbcUsersList.gridy = 0;
		contentPanel.add(usersList, gbcUsersList);
		usersList.setFont(new Font("Verdana", 0, 15));
	}

	public void updateUsers(String[] users) {
		usersList.setListData(users);
	}

}
