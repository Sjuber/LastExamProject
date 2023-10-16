package com.CVP.cv_project.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "users_roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", nullable = false)
    private Integer id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

    private Date endDate;
    @JoinColumn(name = "fk_user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
    @JoinColumn(name = "fk_role_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Role role;



    public UserRole() {
    }

    public UserRole(Date startDate, Date endDate, Role role) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
    }

    public User getUser() {
        return user;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if(role != null) {
            this.role = role;
           // role.getUserRole().add(this);
        }
    }
    public void setUser(User user) {
        if(user != null){

            this.user = user;

        }
    }

    public Integer getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}