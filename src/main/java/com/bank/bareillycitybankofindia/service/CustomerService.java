package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.KycRecord;

public interface CustomerService {
    Customer getCustomerByUserId(long userId);
    Customer getCustomerById(long customerId);
    void updateProfile(Customer customer) throws Exception;
    void submitKyc(long customerId, String docType, String docNumberMasked) throws Exception;
    KycRecord getKycRecord(long customerId);
}
