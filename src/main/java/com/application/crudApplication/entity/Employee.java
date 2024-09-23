package com.application.crudApplication.entity;

import jakarta.persistence.Entity;

@Entity
public class Employee extends Person {

    private String department;

    // Getters and Setters

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
