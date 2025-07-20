package com.example.service;

import com.example.dao.EmployeeAuthDAO;
import com.example.dao.EmployeeDAO;
import com.example.model.Employee;
import com.example.utils.AppLogger;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final EmployeeAuthDAO employeeAuthDAO = new EmployeeAuthDAO();
    private final AppLogger logger = AppLogger.getInstance();

    @Override
    public Employee create(Employee emp) {
        logger.info("Service: Adding employee: " + emp.getEmail());
        boolean success = employeeDAO.addEmployee(emp);
        return success ? employeeDAO.getEmployeeById(emp.getId()) : null;
    }

    @Override
    public Employee update(int id, Employee emp) {
        logger.info("Service: Updating employee: " + emp.getEmail());
        emp.setId(id);
        boolean success = employeeDAO.updateEmployee(emp);
        return success ? employeeDAO.getEmployeeById(id) : null;
    }

    @Override
    public boolean delete(int id) {
        logger.info("Service: Deleting employee ID: " + id);
        employeeAuthDAO.deleteEmployee(id);
        return employeeDAO.deleteEmployee(id);
    }

    @Override
    public Employee get(int id) {
        logger.debug("Service: Fetching employee by ID: " + id);
        return employeeDAO.getEmployeeById(id);
    }

    @Override
    public List<Employee> getAll() {
        logger.debug("Service: Fetching all employees");
        return employeeDAO.getAllEmployees();
    }
} 