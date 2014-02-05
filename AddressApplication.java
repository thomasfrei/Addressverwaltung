package ch.zli.m120.addressverwaltung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import ch.zli.m120.addressapplication.addressdata.Address;
import ch.zli.m120.addressapplication.addressdata.AddressData;

/*
 * Author: Thomas Frei
 */

public class AddressApplication extends Observable{

	private AddressData data;
	private AddressToViewMap viewMap;
	
	/**
	 * Constructor
	 */
	public AddressApplication(AddressData data) {
		
		this.data = data;
		viewMap = new AddressToViewMap();

		// Open the Main Window
		new ListAddressView(this);
		
		setChanged();
		notifyObservers();
	}
	
	public Collection<Address> getAddressData() {
		
		ArrayList<Address> dataList = new ArrayList<Address>();
		
		for (Address addr : data){
			dataList.add(addr);
		}
		return dataList;
	}
	
	//----------------
	// Hilfsfunktionen
	//----------------
	
	public void editAddress(Address addr) {viewMap.showAddressInEditView(addr);}
	
	//-------------------------------------------
	// MC (AddressApplication, SingleAddressView)
	//-------------------------------------------
	
	public void modifyAddress(Address addr) {;}
	public void closeSingleAddressView(Address addr) {viewMap.closeAddressInEditView(addr);}
	
	//-------------------------------------------
	// MC (AddressApplication, ListAddressView)
	//-------------------------------------------
	
	public void addNewAddress() { viewMap.showAddressInEditView(data.getNewAddress());}
	public void deleteAddress(int index) { }
	public void editAddress(int index) {}
	public void closeApplication() {System.exit(0);}
	
	//-------------------------------------------
	// MV (AddressApplication, ListAddressView)
	//-------------------------------------------
	
	public int getMaxAddressDataLength() {return 0;}
	// public Address getAddressAtIdx(int idx) {}
	
	//----------------
	// HilfsKlassen
	//----------------
	
	private class AddressToViewMap {
		private int viewCnt = 0;
		private Map<Integer, SingleAddressView> viewMap = new HashMap<Integer, SingleAddressView>();
		
		/**
		 * Addresse in Fenster öffnen
		 * @param address
		 * @return void
		 */
		public void showAddressInEditView(Address address){
			if (address == null) { 
				return; 
			}
			
			SingleAddressView view = viewMap.get(address.getUniqueId());
			if(view == null) {
				viewCnt++;
				view = new SingleAddressView(AddressApplication.this, address);
				view.setBounds(25 * viewCnt, 25 * viewCnt, 300, 250);
				view.setVisible(true);
				viewMap.put(address.getUniqueId(), view);
			} else {
				view.toFront();
			}
		}
		
		/**
		 * Fenster dieser addresse schliessen
		 * @param address
		 * @return void
		 */
		public void closeAddressInEditView(Address address) {
			SingleAddressView view = viewMap.remove(address.getUniqueId());
			if(view != null) {
				view.dispose();
			}
		}
	}

	
	
}
