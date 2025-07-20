package com.example.model;

import java.util.Date;
import java.util.Random;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Department department;
    private String mobileNumber;
    private double salary;
    private String jobId;
    private Date joiningDate;

    public Employee() {
    }

    public Employee(int id, String firstName, String lastName, String email, Department department, String mobileNumber, double salary, String jobId, Date joiningDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.mobileNumber = mobileNumber;
        this.salary = salary;
        this.jobId = jobId;
        this.joiningDate = joiningDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    private static final Department[] DEPARTMENTS = new Department[] {
        new Department(40, "HR", "Alice Smith"),
        new Department(30, "Finance", "Bob Johnson"),
        new Department(3, "Engineering", "Carol Williams"),
        new Department(4, "Marketing", "David Brown"),
        new Department(5, "Sales", "Eve Davis"),
        new Department(20, "IT", "Frank Miller"),
        new Department(7, "Support", "Grace Wilson"),
        new Department(8, "Legal", "Henry Moore"),
        new Department(9, "R&D", "Ivy Taylor"),
        new Department(11, "Admin", "Jack Anderson")
    };

    public static Employee fromName(String name) {
        Employee emp = new Employee();
        Random random = new Random();
        emp.setId(random.nextInt(100000));
        emp.setFirstName(name);
        emp.setLastName("Doe" + (int)(Math.random()*1000));
        emp.setEmail(name.toLowerCase().replaceAll("\\s+","") + (int)(Math.random()*1000) + "@example.com");
        Department dept = DEPARTMENTS[(int)(Math.random()*DEPARTMENTS.length)];
        emp.setDepartment(dept);
        emp.setMobileNumber("9" + (long)(Math.random()*1_000_000_000L));
        emp.setSalary(30000 + Math.random()*70000);
        emp.setJobId("JOB" + (int)(Math.random()*100));
        emp.setJoiningDate(new java.util.Date(System.currentTimeMillis() - (long)(Math.random()*1_000_000_000L)));
        return emp;
    }
}