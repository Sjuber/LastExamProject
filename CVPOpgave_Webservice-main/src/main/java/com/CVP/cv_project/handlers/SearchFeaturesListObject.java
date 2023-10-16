package com.CVP.cv_project.handlers;

import com.CVP.cv_project.domain.CVs_Knowledge;
import com.CVP.cv_project.domain.PractisedLanguage;

import java.util.List;

public class SearchFeaturesListObject {
    private List<CVs_Knowledge> cvsKnowledgeList;
    private PractisedLanguage language;
    private int notBookedHours;
    private String companyWorkedFor;

    private int amountMostContainCVknowledgeInCV;
    private List<String> prioritiesNamesList;

    public SearchFeaturesListObject() {
    }

    public SearchFeaturesListObject(List<CVs_Knowledge> cvsKnowledgeList, PractisedLanguage language, int notBookedHours,
                                    String companyWorkedFor, int amountMostContainCVknowledgeInCV) {
        this.cvsKnowledgeList = cvsKnowledgeList;
        this.language = language;
        this.notBookedHours = notBookedHours;
        this.companyWorkedFor = companyWorkedFor;
        this.amountMostContainCVknowledgeInCV = amountMostContainCVknowledgeInCV;
    }
    public SearchFeaturesListObject(List<CVs_Knowledge> cvsKnowledgeList, PractisedLanguage language, int notBookedHours,
                                    String companyWorkedFor, List<String> prioritiesNamesList, int amountMostContainCVknowledgeInCV) {
        this.cvsKnowledgeList = cvsKnowledgeList;
        this.language = language;
        this.notBookedHours = notBookedHours;
        this.companyWorkedFor = companyWorkedFor;
        this.prioritiesNamesList = prioritiesNamesList;
        this.amountMostContainCVknowledgeInCV = amountMostContainCVknowledgeInCV;
    }

    public List<CVs_Knowledge> getCvsKnowledgeList() {
        return cvsKnowledgeList;
    }

    public PractisedLanguage getLanguage() {
        return language;
    }

    public int getNotBookedHours() {
        return notBookedHours;
    }

    public String getIndustryName() {
        return companyWorkedFor;
    }

    public int getAmountMostContainCVknowledgeInCV() {
        return amountMostContainCVknowledgeInCV;
    }

    public List<String> getPrioritiesNamesList() {
        return prioritiesNamesList;
    }

}
