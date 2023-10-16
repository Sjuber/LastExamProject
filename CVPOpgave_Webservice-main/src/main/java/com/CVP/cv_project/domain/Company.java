package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Integer id;
    private String name;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "company")
    private List<Job> jobs;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "customer")
    private List<Project> projects;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
        jobs = new ArrayList<>();
        projects = new ArrayList<>();
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

    public void addJobTitle(Job job) {
        this.jobs.add(job);
    }
}