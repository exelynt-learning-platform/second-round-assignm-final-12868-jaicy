package com.multigenesis.ecomm_assesment.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multigenesis.ecomm_assesment.exceptions.ResourceNotFoundException;
import com.multigenesis.ecomm_assesment.model.Address;
import com.multigenesis.ecomm_assesment.model.User;
import com.multigenesis.ecomm_assesment.payload.AddressDTO;
import com.multigenesis.ecomm_assesment.repositories.AddressRepository;
import com.multigenesis.ecomm_assesment.repositories.UserRepository;


@Service
public class AddressServiceImpl implements AddressService{
	
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
    	  if (addressDTO == null) {
    	        throw new IllegalArgumentException("Address data must not be null");
    	    }

    	    if (user == null) {
    	        throw new IllegalArgumentException("User must not be null");
    	    }
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        List<Address> addressesList = user.getAddresses();
        if (addressesList == null) {
            addressesList = new java.util.ArrayList<>();
        }
        addressesList.add(address);
        user.setAddresses(addressesList);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses == null || addresses.isEmpty()) {
            return List.of(); 
        }
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        if (user == null) {
    	        throw new IllegalArgumentException("User must not be null");
    	}
        List<Address> addresses = user.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            return List.of();
        }
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
    	  if (addressId == null) {
    	        throw new IllegalArgumentException("Address ID must not be null");
    	    }

    	    if (addressDTO == null) {
    	        throw new IllegalArgumentException("Address data must not be null");
    	    }
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        if (addressDTO.getCity() != null) {
        addressFromDatabase.setCity(addressDTO.getCity());}
        if (addressDTO.getPincode() != null) {
        addressFromDatabase.setPincode(addressDTO.getPincode());}
        if (addressDTO.getState() != null) {
        addressFromDatabase.setState(addressDTO.getState());}
        if (addressDTO.getCountry() != null) {
        addressFromDatabase.setCountry(addressDTO.getCountry());}
        if (addressDTO.getStreet() != null) {
        addressFromDatabase.setStreet(addressDTO.getStreet());}
        if (addressDTO.getBuildingName() != null) {
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());}

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        User user = addressFromDatabase.getUser();
        if (user != null && user.getAddresses() != null) {
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
        }

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
    	if (addressId == null) {
            throw new IllegalArgumentException("Address ID must not be null");
        }
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        if (user != null && user.getAddresses() != null) {
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        }
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }

}
