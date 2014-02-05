package ch.zli.m120.addressverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.zli.m120.addressapplication.addressdata.Address ;

public class SingleAddressView extends JFrame implements ActionListener{

	/**
	 * Author: Thomas Frei
	 */
	private static final long serialVersionUID = 1719542829642308652L;
	private static final String CMD_TAKEOVER = "Übernehmen";
	private static final String CMD_REJECT = "Verwerfen";
	private JTextField txtFieldSecondName, txtFieldFirstName, txtFieldStreet, txtFieldCity, txtFieldMail;
	private JTextArea commentArea;
	private Address addr;
	private AddressApplication addressApp;
	

	/**
	 * Constructor
	 */
	public SingleAddressView(AddressApplication addressApplication, Address address) {
		this.addressApp = addressApplication;
		this.addr = address;
		createGui();
		
		// TODO createMenu();
		setSize(500,500);
		setVisible(true);
		setBounds(500, 200, 600, 400);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		this.setUiFromData();
	}

	/**
	 * Creates the Single Address View Gui
	 */
	private void createGui() {
		setTitle("Addressverwaltung");
		setLayout(new BorderLayout());
		add(addressPanelNorth(), BorderLayout.NORTH);
		add(commentPanelCenter(), BorderLayout.CENTER);
		add(buttonPanelSouth(), BorderLayout.SOUTH);

	}
	
	private Component createMenu() {
		
		JMenuBar menubar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenu menuLang = new JMenu("Language");
		
		menubar.add(menuFile);
		menubar.add(menuLang);
		setJMenuBar(menubar);
		
		JMenuItem exit = new JMenuItem("Quit");
		exit.setActionCommand("cmd_quit"); // besser Konstante
		exit.addActionListener(new CommandHandler());
		menuFile.add(exit);
		
		JMenuItem en = new JMenuItem("English");
		en.setActionCommand("en");
		en.addActionListener(new CommandHandler());
		menuLang.add(en);
		
		JMenuItem de = new JMenuItem("German");
		de.setActionCommand("de");
		de.addActionListener(new CommandHandler());
		menuLang.add(de);
		
		return menubar;
	}

	/**
	 * Northern Panel including Labels and TextFields 
	 * @return NOrthern Panel
	 */
	
	private JPanel addressPanelNorth() {
		
		JPanel northPanel = new JPanel();
		JPanel leftSubPanel = new JPanel();
		JPanel rightSubPanel = new JPanel();

		northPanel.setLayout(new GridLayout(0, 2));
		leftSubPanel.setLayout(new GridLayout(5, 0));
		rightSubPanel.setLayout(new GridLayout(5, 0));

		leftSubPanel.setBackground(Color.LIGHT_GRAY);

		JLabel leftListSecondName = new JLabel("Name");
		JLabel leftListFirstName = new JLabel("Vorname");
		JLabel leftListStreet = new JLabel("Strasse");
		JLabel leftListCity = new JLabel("PLZ Ort");
		JLabel leftListEMail = new JLabel("E-Mail");

		leftSubPanel.add(leftListSecondName);
		leftSubPanel.add(leftListFirstName);
		leftSubPanel.add(leftListStreet);
		leftSubPanel.add(leftListCity);
		leftSubPanel.add(leftListEMail);

		txtFieldSecondName = new JTextField();
		txtFieldFirstName = new JTextField();
		txtFieldStreet = new JTextField();
		txtFieldCity = new JTextField();
		txtFieldMail = new JTextField();

		rightSubPanel.add(txtFieldSecondName);
		rightSubPanel.add(txtFieldFirstName);
		rightSubPanel.add(txtFieldStreet);
		rightSubPanel.add(txtFieldCity);
		rightSubPanel.add(txtFieldMail);

		northPanel.add(leftSubPanel);
		northPanel.add(rightSubPanel);

		return northPanel;
	}
	
	/**
	 * Center Panel including a textarea
	 * @return Center Panel
	 */
	private Component commentPanelCenter() {
		JPanel centerPanel = new JPanel();
		commentArea = new JTextArea();
		JLabel titleCommentPanel = new JLabel("Bemerkungen");

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(titleCommentPanel, BorderLayout.NORTH);
		centerPanel.add(commentArea, BorderLayout.CENTER);

		return centerPanel;
	}
	
	/**
	 * Southern Panel including two buttons
	 * @return Southern Panel
	 */
	private Component buttonPanelSouth() {
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());

		JButton btnTakeOver = new JButton("Übernehmen");
		JButton btnReject = new JButton("Verwerfen");
		
		btnTakeOver.setActionCommand(CMD_TAKEOVER);
		btnReject.setActionCommand(CMD_REJECT);
		btnTakeOver.addActionListener(this);
		btnReject.addActionListener(this);
		
		southPanel.add(btnTakeOver);
		southPanel.add(btnReject);

		return southPanel;
	}
	
	private void setUiFromData() {
		txtFieldSecondName.setText(this.addr.getName());
		txtFieldFirstName.setText(this.addr.getFirstName());
		txtFieldStreet.setText(this.addr.getStreet());
		txtFieldCity.setText(this.addr.getLocation());
		txtFieldMail.setText(this.addr.getEmail());
		commentArea.setText(this.addr.getRemark());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		switch(cmd) {
			case CMD_TAKEOVER:
				addressApp.modifyAddress(this.addr);
				break;
			case CMD_REJECT:
				addressApp.closeSingleAddressView(this.addr);
				break;
		}
	}

}
