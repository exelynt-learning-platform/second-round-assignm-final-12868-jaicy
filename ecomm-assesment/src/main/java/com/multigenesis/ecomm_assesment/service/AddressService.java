package com.multigenesis.ecomm_assesment.service;

import java.util.List;

import com.multigenesis.ecomm_assesment.model.User;
import com.multigenesis.ecomm_assesment.payload.AddressDTO;

public interface AddressService {
	    
	     AddressDTO createAddress(AddressDTO addressDTO, User user);

	    List<AddressDTO> getAddresses();

	    AddressDTO getAddressesById(Long addressId);

	    List<AddressDTO> getUserAddresses(User user);

	    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

	    String deleteAddress(Long addressId);
}
