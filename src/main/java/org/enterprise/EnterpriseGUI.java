package org.enterprise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterpriseGUI {

    private final Enterprise enterprise;

    private final JFrame frame;
    private JTextField departmentNameField;
    private JTextField employeeNameField;
    private JTextField employeeAgeField;
    private JTextField employeeSalaryField;
    private JTextArea outputArea;

    private JButton addDepartmentButton;
    private JButton removeDepartmentButton;
    private JButton editDepartmentButton;
    private JButton addEmployeeButton;
    private JButton removeEmployeeButton;
    private JButton editEmployeeButton;
    private JButton showInfoButton;

    private Department selectedDepartment;
    private Employee selectedEmployee;

    public EnterpriseGUI(Enterprise enterprise) {
        this.enterprise = enterprise;

        frame = new JFrame("Enterprise Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        initializeComponents();
        createLayout();
        addActionListeners();

        frame.setVisible(true);
    }

    private void initializeComponents() {
        addDepartmentButton = new JButton("Добавить отдел");
        removeDepartmentButton = new JButton("Удалить отдел");
        editDepartmentButton = new JButton("Редактировать отдел");
        addEmployeeButton = new JButton("Добавить сотрудника");
        removeEmployeeButton = new JButton("удалить сотрудника");
        editEmployeeButton = new JButton("Редактировать сотрудника");
        showInfoButton = new JButton("Показать информацию");

        departmentNameField = new JTextField(20);
        employeeNameField = new JTextField(20);
        employeeAgeField = new JTextField(20);
        employeeSalaryField = new JTextField(20);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
    }

    private void createLayout() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Название Отдела:"));
        panel.add(departmentNameField);
        panel.add(new JLabel("ФИО сотрудника:"));
        panel.add(employeeNameField);
        panel.add(new JLabel("Возраст сотрудника:"));
        panel.add(employeeAgeField);
        panel.add(new JLabel("З/П сотрудника:"));
        panel.add(employeeSalaryField);

        panel.add(addDepartmentButton);
        panel.add(removeDepartmentButton);
        panel.add(editDepartmentButton);
        panel.add(addEmployeeButton);
        panel.add(removeEmployeeButton);
        panel.add(editEmployeeButton);
        panel.add(showInfoButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void addActionListeners() {
        addDepartmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                if (!departmentName.isEmpty()) {
                    Department newDepartment = new Department(departmentName);
                    enterprise.addDepartment(newDepartment);
                    outputArea.setText("Отдел добавлен: " + departmentName);
                } else {
                    outputArea.setText("Пожалуйста введите название отдела.");
                }
            }
        });

        removeDepartmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                Department targetDepartment = findDepartmentByName(departmentName);

                if (targetDepartment != null) {
                    enterprise.removeDepartment(targetDepartment);
                    outputArea.setText("Отдел удалён: " + departmentName);
                } else {
                    outputArea.setText("Отдел не найден: " + departmentName);
                }
            }
        });

        editDepartmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                selectedDepartment = findDepartmentByName(departmentName);

                if (selectedDepartment != null) {
                    editDepartment(selectedDepartment);
                } else {
                    outputArea.setText("Отдел не найден: " + departmentName);
                }
            }
        });

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                String employeeName = employeeNameField.getText();
                int employeeAge = Integer.parseInt(employeeAgeField.getText());
                double employeeSalary = Double.parseDouble(employeeSalaryField.getText());

                Department targetDepartment = findDepartmentByName(departmentName);

                if (targetDepartment != null) {
                    Employee newEmployee = new Employee(employeeName, employeeAge, employeeSalary);
                    targetDepartment.addEmployee(newEmployee);
                    outputArea.setText("Сотрудник добавлен в отдел " + departmentName + ": " + employeeName);
                } else {
                    outputArea.setText("Отдел не найден: " + departmentName);
                }
            }
        });

        removeEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                String employeeName = employeeNameField.getText();
                Department targetDepartment = findDepartmentByName(departmentName);

                if (targetDepartment != null) {
                    Employee targetEmployee = findEmployeeByName(targetDepartment, employeeName);

                    if (targetEmployee != null) {
                        targetDepartment.removeEmployee(targetEmployee);
                        outputArea.setText("Сотрудник удалён из отдела " + departmentName + ": " + employeeName);
                    } else {
                        outputArea.setText("Сотрудник не найден в отделе: " + employeeName);
                    }
                } else {
                    outputArea.setText("Отдел не найден: " + departmentName);
                }
            }
        });

        editEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departmentName = departmentNameField.getText();
                String employeeName = employeeNameField.getText();
                Department targetDepartment = findDepartmentByName(departmentName);

                if (targetDepartment != null) {
                    selectedEmployee = findEmployeeByName(targetDepartment, employeeName);

                    if (selectedEmployee != null) {
                        editEmployee(selectedEmployee);
                    } else {
                        outputArea.setText("Сотрудник не найден в отделе: " + employeeName);
                    }
                } else {
                    outputArea.setText("Отдел не найден: " + departmentName);
                }
            }
        });

        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder info = new StringBuilder();

                for (Department department : enterprise.getDepartments()) {
                    info.append("Отдел: ").append(department.getName()).append(", Сотрудник: ").append(department.getNumberOfEmployees()).append("\n");

                    for (Employee employee : department.getEmployees()) {
                        info.append("    Сотрудник: ").append(employee.getFullName()).append(", Возраст: ").append(employee.getAge()).append(", З/П: ").append(employee.getSalary()).append("\n");
                    }

                    info.append("\n");
                }

                outputArea.setText(info.toString());
            }
        });
    }

    private Department findDepartmentByName(String name) {
        for (Department department : enterprise.getDepartments()) {
            if (department.getName().equals(name)) {
                return department;
            }
        }
        return null;
    }

    private Employee findEmployeeByName(Department department, String name) {
        for (Employee employee : department.getEmployees()) {
            if (employee.getFullName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    private void editDepartment(Department department) {
        String newName = JOptionPane.showInputDialog(frame, "Введите новое название отдела:", department.getName());
        if (newName != null) {
            department.setName(newName);
            outputArea.setText("Информация о отделе изменена: " + newName);
        }
    }

    private void editEmployee(Employee employee) {
        String newName = JOptionPane.showInputDialog(frame, "Введите новое имя:", employee.getFullName());
        if (newName != null) {
            employee.setFullName(newName);
        }

        String newAge = JOptionPane.showInputDialog(frame, "Введите новый возраст:", String.valueOf(employee.getAge()));
        if (newAge != null) {
            try {
                int age = Integer.parseInt(newAge);
                employee.setAge(age);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid age format. Please enter a number.");
            }
        }

        String newSalary = JOptionPane.showInputDialog(frame, "Введите новую з/п:", String.valueOf(employee.getSalary()));
        if (newSalary != null) {
            try {
                double salary = Double.parseDouble(newSalary);
                employee.setSalary(salary);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid salary format. Please enter a number.");
            }
        }

        outputArea.setText("Информация о сотруднике изменена: " + employee.getFullName());
    }
}
