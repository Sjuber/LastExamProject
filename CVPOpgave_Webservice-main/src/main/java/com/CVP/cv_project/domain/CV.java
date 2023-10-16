package com.CVP.cv_project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cvs")
public class CV implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cv_id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_consultant_id")
    private User consultant;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_user_author_id")
    private User author;

    @Column(unique = true, nullable = false)
    private String title;
    private int bookedHours;
    private int maxHours;
    private String description;
    private String techBackground;
    //@Enumerated(EnumType.STRING)
   // @Transient
    //@Column(name = "cvState")
    private CVState cvState;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CVs_Knowledge> knowledgeList;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "cv")
    private List<Project> projects;


    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "cv")
    private List<CoursesCertification> coursesCertifications;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "cv")
    private List<PractisedLanguage> practisedLanguages;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "cv")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<Job> jobs;

    @OneToOne()
    @JoinColumn(name = "fk_cv_original")
    private CV cvOriginal;

    public CV() {
    }

    public CV(User consultant, int bookedHours, int weeklyHours, String description, String techBackground, String title) {
        this.author = consultant;
        this.bookedHours = bookedHours;
        this.maxHours = weeklyHours;
        this.description = description;
        this.techBackground = techBackground;
        for (UserRole uR: consultant.getUserRoles()) {
            if (uR.getRole().getTitle().equals("Konsulent")) {
                this.consultant = consultant;
                this.consultant.addCV(this);
            }
        }
        this.knowledgeList = new ArrayList<>(); // TODO - rettes til rigtige navn !!!!
        this.projects = new ArrayList<>();
        this.coursesCertifications = new ArrayList<>();
        this.practisedLanguages = new ArrayList<>();
        this.educations = new ArrayList<>();
        this.jobs = new ArrayList<>();
        this.cvState = CVState.inDraft;
        this.title = title;
    }
    public CV( User author, CV cvOriginal) {
        if(cvOriginal.getCVState() == CVState.inDraft) {
            this.consultant = cvOriginal.getConsultant();
            this.bookedHours = cvOriginal.getBookedHours();
            this.maxHours = cvOriginal.getMaxHours();
            this.description = cvOriginal.getDescription();
            this.techBackground = cvOriginal.getTechBackground();
            this.knowledgeList = new ArrayList<>();
            this.projects = new ArrayList<>();
            this.coursesCertifications = new ArrayList<>();
            this.practisedLanguages = new ArrayList<>();
            this.educations = new ArrayList<>();
            this.jobs = new ArrayList<>();
            cvOriginal.getKnowledgeList().forEach(this.knowledgeList::add);
            cvOriginal.getProjects().forEach(this.projects::add);
            cvOriginal.getCoursesCertifications().forEach(this.coursesCertifications::add);
            cvOriginal.getPractisedLanguages().forEach(this.practisedLanguages::add);
            cvOriginal.getEducations().forEach(this.educations::add);
            cvOriginal.getJobs().forEach(this.jobs::add);
            this.cvState = cvOriginal.getCVState();
            //if (author.getId() != cvOriginal.getConsultant().getId() || (!(author.getPhone().equals(cvOriginal.getConsultant().getPhone())))) {
                this.author = author;
                this.author.addCV(this);
            //}
        }
    }

    public Integer getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getConsultant() {
        return consultant;
    }

    public void setConsultant(User consultant) {
        this.consultant = consultant;
    }



    public int getBookedHours() {
        return bookedHours;
    }

    public void setBookedHours(int bookedHours) {
        this.bookedHours = bookedHours;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(int weeklyHours) {
        this.maxHours = weeklyHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechBackground() {
        return techBackground;
    }

    public void setTechBackground(String techBackground) {
        this.techBackground = techBackground;
    }
    public List<CVs_Knowledge> getCV_KnowledgeList() {
        return knowledgeList;
    }

    public void setCV_KnowledgeList(List<CVs_Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    public List<Project> getProjects() {
        return projects;
    }


    public void addEducation(Education education) {
        this.educations.add(education);
        education.setCv(this);
        }
    public void addCVToCV_Knowledge(CVs_Knowledge cvKnowledge){
        if (cvKnowledge != null) {
            this.knowledgeList.add(cvKnowledge);
            cvKnowledge.setCv(this);
        }
    }

    public void addProject(Project project){
        if (project != null) {
            this.projects.add(project);
            project.setCv(this);
        }
    }

    public List<CVs_Knowledge> getKnowledgeList() {
        return knowledgeList;
    }

    public void setKnowledgeList(List<CVs_Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    public void addCoursesCertafications(CoursesCertification coursesCertification) {
        if(coursesCertification != null){
            this.coursesCertifications.add(coursesCertification);
            coursesCertification.setCv(this);
        }

    }

    public void setCoursesCertafications(List<CoursesCertification> coursesCertifications) {
        this.coursesCertifications = coursesCertifications;
    }

    public List<PractisedLanguage> getPractisedLanguages() {
        return practisedLanguages;
    }

    public void setPractisedLanguages(List<PractisedLanguage> practisedLanguages) {
        this.practisedLanguages = practisedLanguages;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
    public void addJob(Job job) {
        if(!job.equals(null)){
            this.jobs.add(job);
        }

    }

    public List<CoursesCertification> getCoursesCertifications() {
        return this.coursesCertifications;
    }

    public void setCoursesCertifications(List<CoursesCertification> coursesCertifications) {
        this.coursesCertifications = coursesCertifications;
    }

    public CV getCvOriginal() {
        return cvOriginal;
    }

    public void setCvOriginal(CV cvOriginal) {
        this.cvOriginal = cvOriginal;
    }

    public CVState getCVState() {
        return this.cvState;
    }

    public void setCVState(CVState cv_state) {
        this.cvState = cv_state;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CV{" +
                "id=" + id +
                ", consultant=" + consultant.getName() +
                ", author=" + author.getName() +
                ", bookedHours=" + bookedHours +
                ", weeklyHours=" + maxHours +
                ", description='" + description + '\'' +
                ", techBackground='" + techBackground + '\'' +
                '}';
    }

}