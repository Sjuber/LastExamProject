package com.CVP.cv_project.handlers;

import java.util.Comparator;

public class SortByHandler implements Comparator<BestMatchOfCVHandler> {

    @Override
    public int compare(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        if (o2.getMostHaveCVKnowledgeScore() - o1.getMostHaveCVKnowledgeScore() != 0) {
            return o2.getMostHaveCVKnowledgeScore() - o1.getMostHaveCVKnowledgeScore();
        }
        for (String namePriority : o2.getSearchFeatureList()) {
            switch (namePriority) {
                case "cvs_knowlegde": {
                    int knowledgeSort = sortByCVknowledgeNames(o1, o2);
                    if (knowledgeSort != 0) {
                        return knowledgeSort;
                    }
                    int knowledgeSortLevel = sortByCVknowledgeLevel(o1, o2);
                    if (knowledgeSortLevel != 0) {
                        return knowledgeSortLevel;
                    }
                    int knowledgeSortYear = sortByCVknowledgeYear(o1, o2);
                    if (knowledgeSortYear != 0) {
                        return knowledgeSortYear;
                    }
                }
                case "industry": {
                    int industrySort = sortByIndustry(o1, o2);
                    if (industrySort != 0) {
                        return industrySort;
                    }
                }
                case "language": {
                    int languageSort = sortByLanguage(o1, o2);
                    if (languageSort != 0) {
                        return languageSort;
                    }
                }
                case "freeHours": {
                    int freeHoursSort = sortByFreeHours(o1, o2);
                    if (freeHoursSort != 0) {
                        return freeHoursSort;
                    }
                }
            }
        }
        return o2.compareTo(o1);
}

    private int sortByIndustry(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        return o2.getPriorityIndustryScore() - o1.getPriorityIndustryScore();
    }

    private int sortByFreeHours(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        return o2.getPriorityFreeHoursScore() - o1.getPriorityFreeHoursScore();
    }

    private int sortByLanguage(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        return o2.getPriorityLanguageScore() - o1.getPriorityLanguageScore();
    }

    private int sortByCVknowledgeYear(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        return o2.getPriorityYearsScore() - o1.getPriorityYearsScore();
    }

    private int sortByCVknowledgeLevel(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        return o2.getSecondPriorityScore() - o1.getSecondPriorityScore();
    }

    private int sortByCVknowledgeNames(BestMatchOfCVHandler o1, BestMatchOfCVHandler o2) {
        if (o2.getPriorityNamesScore() - o1.getPriorityNamesScore() != 0) {
            return o2.getPriorityScore() - o1.getPriorityScore();
        }
        return o2.getPriorityNamesScore() - o1.getPriorityNamesScore();
    }
}
