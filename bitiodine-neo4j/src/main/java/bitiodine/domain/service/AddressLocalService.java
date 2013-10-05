package bitiodine.domain.service;

import bitiodine.domain.model.Address;

public interface AddressLocalService {
	public Address addAddress(Address address);
	public Address updateAddress(Address address);
	public Address deleteAddress(Address address);
	
	public Address getAddress(String address);
	
	// Finder methods
	
}
