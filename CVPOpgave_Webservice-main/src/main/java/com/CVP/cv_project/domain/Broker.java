package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "brokers")
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "broker_id", nullable = false)
    private Integer id;

    private Date startDate;
    private Date endDate;
    private String name;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_project_id")
    private Project project;

    public Broker() {
    }

    public Broker(Date startDate, Date endDate, String name, Project project) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.project = project;
    }

    public Integer getId() {
        return id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}