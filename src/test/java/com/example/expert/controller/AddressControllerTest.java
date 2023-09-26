package com.example.expert.controller;

import com.example.expert.entity.Address;
import com.example.expert.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    Address addressL1;
    Address addressEntityL1;

    Long idNotExist = -1L;

    Long idNull = null;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressController = new AddressController(addressService);
        addressL1 = new Address(1L, "Jr. San francisco",null);
        addressEntityL1 = new Address(1L, "Jr. San francisco",null);
    }

    public void assertAddressEquals(Address address, Address addressAdapter){
        assertEquals(address.getId(), addressAdapter.getId());
        assertEquals(address.getStreet(), addressAdapter.getStreet());
    }

    @Test
    void getAllAddresses() {
        when(addressService.findAll()).thenReturn(List.of(addressEntityL1));
        List<Address> addresses = addressController.getAllAddresses().getBody();
        assertNotNull(addresses);
        assertEquals(1, addresses.size());
    }

    @Test
    void createAddress() {
        when(addressService.save(addressL1)).thenReturn(addressEntityL1);
        Address addressAdapter = addressController.createAddress(addressL1).getBody();
        assertNotNull(addressAdapter);
        assertAddressEquals(addressL1, addressAdapter);
    }

    @Test
    void getAddressById() {
        when(addressService.findById(addressL1.getId())).thenReturn(addressEntityL1);
        Address addressAdapter = addressController.getAddressById(addressL1.getId()).getBody();
        assertNotNull(addressAdapter);
        assertAddressEquals(addressL1, addressAdapter);
    }

    @Test
    void updateAddress() {
        when(addressService.findById(addressL1.getId())).thenReturn(addressEntityL1);
        when(addressService.save(addressL1)).thenReturn(addressEntityL1);
        Address addressAdapter = addressController.updateAddress(addressL1.getId(), addressL1).getBody();
        assertNotNull(addressAdapter);
        assertAddressEquals(addressL1, addressAdapter);
    }

    @Test
    void deleteAddressById() {
        when(addressService.findById(addressL1.getId())).thenReturn(addressEntityL1);
        addressController.deleteAddressById(addressL1.getId());
        verify(addressService, times(1)).deleteById(addressL1.getId());
    }

    @Test
    void getAddressByIdNotFound() {
        when(addressService.findById(idNotExist)).thenReturn(null);
        Address addressAdapter = addressController.getAddressById(idNotExist).getBody();
        assertNull(addressAdapter);
    }

    @Test
    void updateAddressIdNull() {
        when(addressService.findById(idNull)).thenReturn(null);
        Address addressAdapter = addressController.updateAddress(idNull, addressL1).getBody();
        assertNull(addressAdapter);
    }

    @Test
    void deleteAddressByIdException() {
        when(addressService.findById(addressL1.getId())).thenReturn(addressEntityL1);
        doThrow(new RuntimeException()).when(addressService).deleteById(addressL1.getId());
        addressController.deleteAddressById(addressL1.getId());
        verify(addressService, times(1)).deleteById(addressL1.getId());
    }
}