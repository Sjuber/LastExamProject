package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "practised_languages")
public class PractisedLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "practised_language_id", nullable = false)
    private Integer id;
    private String name;
    private String levelWritting;
    private String levelReading;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_cv_id")
    private CV cv;

    public PractisedLanguage() {
    }

    public PractisedLanguage(String name, String levelWritting, String levelReading) {
        this.name = name;
        this.levelWritting = levelWritting;
        this.levelReading = levelReading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelWritting() {
        return levelWritting;
    }

    public void setLevelWritting(String levelWritting) {
        this.levelWritting = levelWritting;
    }

    public String getLevelReading() {
        return levelReading;
    }

    public void setLevelReading(String levelReading) {
        this.levelReading = levelReading;
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


}