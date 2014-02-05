package ch.zli.m120.addressverwaltung;

import ch.zli.m120.addressapplication.addressdata.AddressData;
import ch.zli.m120.addressapplication.addressdata.AddressDataFactory;

/**
 * Author: Thomas Frei
 */
public class Main {

	public static void main(String[] args) {
		
		AddressData data;
		data = AddressDataFactory.createAddressData();
		new AddressApplication(data);
	}

}
