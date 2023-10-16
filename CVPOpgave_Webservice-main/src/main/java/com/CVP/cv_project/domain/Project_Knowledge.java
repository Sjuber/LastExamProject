package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "projects_knowledge")
public class Project_Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_knowledge_id", nullable = false)
    private Integer id;

    private String note;

    @ManyToOne
    @JoinColumn(name = "fk_project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "fk_knowledge_id")
    private Knowledge knowledge;
    public Project_Knowledge() {
    }
    public Project_Knowledge(String note, Knowledge knowledge, Project project) {
        this.note = note;
        this.knowledge = knowledge;
        this.project = project;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Knowledge getKnowledge() {
        return this.knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }
}
