package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "knowledge")
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_id", nullable = false)
    private Integer id;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_category_id")
    private KnowCategory category;

    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "knowledge")
    private List<CVs_Knowledge> cVs_knowledge;

    //@ManyToMany()
    //@JoinTable(
    //        name = "knowledge_projects",
    //        joinColumns = @JoinColumn(name = "fk_knowledge_id"),
    //        inverseJoinColumns = @JoinColumn(name = "fk_project_id"))
    //private List<Project> projects;

    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "knowledge")
    private List<Project_Knowledge> projects_knowledge;
    public Knowledge() {
    }

    public Knowledge(String name, KnowCategory category) {
        this.name = name;
        this.category = category;
        this.category.addKnowledge(this);
        this.cVs_knowledge = new ArrayList<>();
        this.projects_knowledge = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KnowCategory getCategory() {
        return category;
    }

    public void setCategory(KnowCategory category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public List<CVs_Knowledge> getcVs_knowledge() {
        return cVs_knowledge;
    }
    public List<Project_Knowledge> getProjects(){
        return projects_knowledge;
    }

    public void addProjectKnowledgeForProject(Project_Knowledge projects_knowledge){
        if (projects_knowledge!=null){
            this.projects_knowledge.add(projects_knowledge);
        }
    }
    public void addKnowledgeToCV_Knowledge(CVs_Knowledge cvKnowledge){
        if (cvKnowledge != null) {
            this.cVs_knowledge.add(cvKnowledge);
            cvKnowledge.setKnowledge(this);

        }
    }

    public void setcVs_knowledge(List<CVs_Knowledge> cVs_knowledge) {
        this.cVs_knowledge = cVs_knowledge;
    }

    public void setProjects(List<Project_Knowledge> projects_knowledge) {
        this.projects_knowledge = projects_knowledge;
    }
}