package ch.zli.m120.addressverwaltung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.zli.m120.addressapplication.addressdata.Address;

/**
 * @author Thomas Frei
 *
 */
public class ListAddressView extends JFrame implements ActionListener, Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7842140321604138537L;
	private static final String CMD_NEW_ADR = "Neue Addr";
	private static final String CMD_ADR_BEA = "Addr. bearbeiten";
	private static final String CMD_ADR_DEL = "Addr. löschen";
	private static final String CMD_PROG_EXIT = "Prog. beenden";
	private static final String CMD_SORT_BYNAME = null;
	private static final String CMD_SORT_BYFIRSTNAME = null;
	private static final String CMD_SORT_NONE = null;
	private JButton button1 ,button2, button3, button4;
	private AddressApplication addressApp;
	private ListViewData viewData;
	private JList<String> listView;
	private JTextPane addressView;

	/**
	 * Constructor
	 */
	public ListAddressView(AddressApplication addressApplication)
	{
		this.addressApp = addressApplication;
		addressApplication.addObserver(this);
		
		listView = new JList<String>();
		addressView = new JTextPane();
		viewData = new ListAddressView.ListViewData();		
		listView.addListSelectionListener(viewData);

		createGui();
		setSize(600,400);
		setVisible(true);
	}

	/**
	 * Creates the main Gui for the Address Application
	 * @return void
	 */
	private void createGui() {
		setTitle("Addressverwaltung");
		setLayout(new BorderLayout(5,5));
		add(createAddressPanels(), BorderLayout.CENTER);
		add(createControlls(), BorderLayout.SOUTH);
		pack();
	}

	/**
	 * Creates the address panel
	 * @return address panel
	 */
	private JPanel createAddressPanels() {
		JPanel addressPanel = new JPanel();
		addressPanel.setLayout(new GridLayout(1,0));
		
		JPanel listDisplayPanel = new JPanel(new BorderLayout());
		JPanel textDisplayPanel = new JPanel(new BorderLayout());
		
		listDisplayPanel.add(listView);
		textDisplayPanel.add(addressView);
		
		listDisplayPanel.setBorder(BorderFactory.createEtchedBorder());
		textDisplayPanel.setBorder(BorderFactory.createEtchedBorder());
		
		addressPanel.add(listDisplayPanel);
		addressPanel.add(textDisplayPanel);
		return addressPanel;
	}
	
	/**
	 * Creates the control Panel including the 4 Buttons
	 * @return control panel
	 */
	private JPanel createControlls() {
		JPanel controlPanel = new JPanel();
		
		button1 = new JButton("Neue Addr.");
		button2 = new JButton("Addr. bearbeiten.");
		button3 = new JButton("Addr. löschen");
		button4 = new JButton("Prog. beenden");
		
		button2.setEnabled(false);
		button3.setEnabled(false);
		
		button1.setActionCommand(CMD_NEW_ADR);
		button2.setActionCommand(CMD_ADR_BEA);
		button3.setActionCommand(CMD_ADR_DEL);
		button4.setActionCommand(CMD_PROG_EXIT);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		
		controlPanel.add(button1);
		controlPanel.add(button2);
		controlPanel.add(button3);
		controlPanel.add(button4);
		
		return controlPanel;
	}
	
	// EventHandling
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
			case CMD_NEW_ADR:
				addressApp.addNewAddress();
				break;
			case CMD_ADR_BEA:
				addressApp.editAddress(viewData.getSelectedAddress());
				break;
			case CMD_ADR_DEL:
				addressApp.deleteAddress(viewData.getSelectedAddress().getUniqueId());
				break;
			case CMD_PROG_EXIT:
				addressApp.closeApplication();
				break;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		viewData.dataChanged();
	}
	
	private class ListViewData implements ListSelectionListener, ActionListener {

		
		private List<Address> dataList;
		private SortOrder sortOrder = SortOrder.UNSORTED;
		
		
		public void dataChanged(){
			dataList = new ArrayList<Address>(addressApp.getAddressData());
			sortDataList();
			int oldIdx = listView.getSelectedIndex();
			listView.setListData(getListData());
			listView.setSelectedIndex(oldIdx);
			updateTextualRepresentation();
		}
		
		public Address getSelectedAddress() {
			Address addr = null;
			try {
				addr = dataList.get(listView.getSelectedIndex());
			} catch (ArrayIndexOutOfBoundsException e) {}
			return addr;
		}
		
		private String[] getListData(){
			String[] res = new String[dataList.size()];
			for(int i = 0; i < res.length; ++i) {
				Address address = dataList.get(i);
				if (sortOrder == SortOrder.UNSORTED) {
					res[i] = address.getFirstName() + ", " + address.getName();
				} else {
					res[i] = address.getName() + ", " + address.getFirstName();
				}
			}
			
			return res;
		}
		
		private String getTextualRepresentationForIdx(int idx) {
			String res = "";
			try {
				Address addr = dataList.get(idx);
				res = 
						addr.getFirstName() + " " + addr.getName()+ "\n" +
						addr.getStreet() + "\n" +
						addr.getLocation() + "\n" + 
						addr.getEmail() + "\n";
			} catch (ArrayIndexOutOfBoundsException e) {}
			return res;
		}
		
		private void updateTextualRepresentation(){
			addressView.setText(getTextualRepresentationForIdx(listView.getSelectedIndex()));
		}
		
		@Override
		public void valueChanged(ListSelectionEvent lse) {
			if (lse.getValueIsAdjusting()){
				updateTextualRepresentation();
				ListAddressView.this.button2.setEnabled(true);
				ListAddressView.this.button3.setEnabled(true);
			}
			
		}
				
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == CMD_SORT_NONE) {
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				sortOrder = SortOrder.UNSORTED;
				dataChanged();
			} else if (cmd == CMD_SORT_BYNAME) {
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				sortOrder = SortOrder.UNSORTED;
				dataChanged();
			} else if (cmd == CMD_SORT_BYFIRSTNAME) {
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				sortOrder = SortOrder.UNSORTED;
				dataChanged();
			}
		}

		private void sortDataList() {
			Collections.sort(dataList, new Comparator<Address>() {

				@Override
				public int compare(Address a0, Address a1) {
					switch(sortOrder) {
						case ASCENDING: {
							return a0.getFirstName().compareTo(a1.getFirstName());
						}
						case DESCENDING: {
							return a0.getName().compareTo(a1.getName());
						}
						default: {
							return 0;
						}
					}
				}
			});
			
		}

		

	}

	
}
