package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;

    private int maxWeeklyHours;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserRole> userRoles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consultant")
    private List<CV> cvs;

    public User() {
    }

    public User(String name, String phone, String email) {
        this.cvs = new ArrayList<>();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.userRoles = new ArrayList<>();
        this.password = "123456!"; //TODO THis is only at the moment instead of AD
        this.maxWeeklyHours = 37;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void addUserRole(UserRole userRole) {
        if (userRole != null) {
            this.getUserRoles().add(userRole);
            userRole.setUser(this);
        }
    }

    public List<CV> getCvs() {
        return cvs;
    }

    public void addCV(CV cv) {
        if (cv != null) {
            cvs.add(cv);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxWeeklyHours() {
        return maxWeeklyHours;
    }

    public void setMaxWeeklyHours(int maxWeeklyHours) {
        this.maxWeeklyHours = maxWeeklyHours;
    }
}