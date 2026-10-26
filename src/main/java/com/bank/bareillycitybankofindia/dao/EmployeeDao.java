package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Employee;

import java.util.List;

public interface EmployeeDao {
    Employee findByUserId(long userId);
    List<Employee> findAll();
    long createEmployee(Employee employee);
}
