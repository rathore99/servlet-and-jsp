package com.example.service;

import com.example.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee update(int id, Employee employee);
    boolean delete(int id);
    Employee get(int id);
    List<Employee> getAll();
}
