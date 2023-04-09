/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Kyle Christian
 */
public class Customer {
   
    private final SimpleIntegerProperty Id = new SimpleIntegerProperty();
    private final SimpleStringProperty Name = new SimpleStringProperty();
    private final SimpleStringProperty Address = new SimpleStringProperty();
    private final SimpleStringProperty Address2 = new SimpleStringProperty();
    private final SimpleStringProperty City = new SimpleStringProperty();
    private final SimpleStringProperty Zip = new SimpleStringProperty();
    private final SimpleStringProperty Phone = new SimpleStringProperty();
    private final SimpleStringProperty Country = new SimpleStringProperty();
    
    
    private final SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty cityId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty countryId = new SimpleIntegerProperty();
    
    public Customer() {}
    
    public Customer(int id, String name, String address, String address2, String city, String phone, String zip, String country, int addressId, int cityId, int countryId) {
        setId(id);
        setName(name);
        setAddress(address);
        setAddress2(address2);
        setCity(city);
        setPhone(phone);
        setZip(zip);
        setCountry(country);
        setAddressId(addressId);
        setCityId(cityId);
        setCountryId(countryId);
    }
    
   
    
    public int getAddressId()
    {
        return addressId.get();
    }
    
    public int getCityId()
    {
        return cityId.get();
    }
    
    public int getCountryId()
    {
        return countryId.get();
    }
    
    public int getId() {
        return Id.get();
    }
    
    public String getName() {
        return Name.get();
    }
    
    public String getAddress() {
        return Address.get();
    }
   
    public String getAddress2()
    {
        return Address2.get();
    }
    public String getCity() {
        return City.get();
    }
    
    public String getPhone() {
        return Phone.get();
    }
    
    public String getZip() {
        return Zip.get();
    }
    
    public String getCountry() {
        return Country.get();
    }
    
    public void setAddressId(int addressID)
    {
        this.addressId.set(addressID);
    }
    
    public void setCityId(int cityID)
    {
        this.cityId.set(cityID);
    }
    
    public void setCountryId(int countryID)
    {
        this.countryId.set(countryID);
    }
    
    public void setId(int customerId) {
        this.Id.set(customerId);
    }
    
    public void setName(String name) {
        this.Name.set(name);
    }
    
    public void setAddress(String address) {
        this.Address.set(address);
    }
    
    public void setAddress2(String address2) {
        this.Address2.set(address2);
    }
    
    public void setCity(String city) {
        this.City.set(city);
    }
    
    public void setPhone(String phone) {
        this.Phone.set(phone);
    }
    
    public void setZip(String zip) {
        this.Zip.set(zip);
    }

    private void setCountry(String country) {
        this.Country.set(country);
        
    }
}
    

