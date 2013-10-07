package bitiodine.domain.service;

import bitiodine.domain.model.Address;

public interface AddressLocalService {
	//CRUD
	public Address getOrCreateAddress(String address);
	public Address updateAddress(String address);
	public Address deleteAddress(String address);
	
	//Other operations
	public Address linkAddressToCluster(String address, String cluster_id);
	
	// Finder methods
	public Address findAddressByAddress(String address);
}
