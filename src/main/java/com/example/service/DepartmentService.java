package com.example.service;


import com.example.dao.DepartmentDAO;
import com.example.model.Department;

import java.util.List;

public class DepartmentService {
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    public boolean addDepartment(Department dept, int managerId) {
        // Add any business validation here
        return departmentDAO.addDepartment(dept, managerId);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentDAO.getDepartmentById(departmentId);
    }

    public boolean updateDepartment(Department dept, int managerId) {
        // Add any business validation here
        return departmentDAO.updateDepartment(dept, managerId);
    }

    public boolean deleteDepartment(int departmentId) {
        // Add any business validation here (e.g., check if allowed)
        return departmentDAO.deleteDepartment(departmentId);
    }

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    public List<Department> searchDepartments(String keyword) {
        return departmentDAO.searchDepartments(keyword);
    }
} 