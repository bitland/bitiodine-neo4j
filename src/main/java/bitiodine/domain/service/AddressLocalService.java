package bitiodine.domain.service;

import bitiodine.domain.model.Address;

public interface AddressLocalService {
	//CRUD
	public Address addAddress(String address);
	public Address updateAddress(Address address);
	public Address deleteAddress(String address);
	
	//Other operations
	public Address linkAddressToCluster(String address, String cluster_id);
	
	// Finder methods
	public Address findAddressByAddress(String address);
}
