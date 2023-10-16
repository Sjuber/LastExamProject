package com.CVP.cv_project.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class KnowCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "category")
    private List<Knowledge> knowledgeList;

    public KnowCategory() {
    }

    public KnowCategory(String name, String  description) {
        this.name = name;
        this.description = description;
        this.knowledgeList = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Knowledge> getKnowledgeList() {
        return knowledgeList;
    }

    public void addKnowledge(Knowledge knowledge){
        if (knowledge !=null){
            this.knowledgeList.add(knowledge);
            knowledge.setCategory(this);
        }
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

}