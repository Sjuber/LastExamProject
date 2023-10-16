package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Integer id;
    private String description;
    private String roleForProject;
    private Date dateStart;
    private Date dateEnd;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_company_id")
    private Company customer;

    @OneToOne(mappedBy = "project")
    private Broker broker;

    @ManyToOne()
    @JoinColumn(name = "fk_cv_id")
    private CV cv;

    //@ManyToMany(cascade = CascadeType.MERGE)
    //@JoinTable(
    //        name = "knowledge_projects",
    //        joinColumns = @JoinColumn(name = "fk_project_id"),
    //        inverseJoinColumns = @JoinColumn(name = "fk_knowledge_id"))
    //private List<Knowledge> knowledgeList;

    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "project")
    private List<Project_Knowledge> projectknowledgeList;

    public Project() {
    }

    public Project(String description, String roleForProject, Date dateStart, Date dateEnd) {
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.roleForProject = roleForProject;
        this.projectknowledgeList = new ArrayList<>();
    }
    public Company getCustomer() {
        return customer;
    }

    public void setCustomer(Company customer) {
        this.customer = customer;
    }
    public void addProjectKnowledge(Project_Knowledge project_knowledge){
        if(project_knowledge!=null){
            this.projectknowledgeList.add(project_knowledge);
        }
    }

    public List<Project_Knowledge> getProjectsKnowledgeList() {
        return this.projectknowledgeList;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    public Integer getId() {
        return id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getRoleForProject() {
        return roleForProject;
    }

    public void setRoleForProject(String roleForProject) {
        this.roleForProject = roleForProject;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", roleForProject='" + roleForProject + '\'' +
                ", customer=" + customer +
                ", cv=" + cv +
                ", projects_knowledge=" + projectknowledgeList +
                '}';
    }
}