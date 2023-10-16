package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cvs_knowledge")
public class CVs_Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cvs_knowledge_id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fk_cv_id")
    private CV cv;
    @ManyToOne
    @JoinColumn(name = "fk_knowledge_id")
    private Knowledge knowledge;

    private String levelSkill;
    private int years;
    private String note;

    public CVs_Knowledge() {
    }

    public CVs_Knowledge(String levelSkill, int years, String note, Knowledge knowledge) {
        this.levelSkill = levelSkill;
        this.years = years;
        this.note = note;
        this.knowledge = knowledge;
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
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

    public String getLevelSkill() {
        return levelSkill;
    }

    public void setLevelSkill(String levelSkill) {
        this.levelSkill = levelSkill;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}