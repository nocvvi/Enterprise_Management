package org.enterprise;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private int numberOfEmployees;
    private List<Employee> employees;

    public Department(String name) {
        this.name = name;
        this.numberOfEmployees = 0;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        numberOfEmployees++;
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        numberOfEmployees--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        this.numberOfEmployees = employees.size();
    }

}
