package com.CVP.cv_project.handlers;

import com.CVP.cv_project.domain.*;
import com.CVP.cv_project.domain.Enums.LevelOfCVKnowledgeSkill;
import com.CVP.cv_project.domain.Enums.LevelOfLanguage;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.MutablePair;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class BestMatchOfCVHandler
        implements Comparable<BestMatchOfCVHandler> {

    private User consultant;
    private int priorityScore;
    private int priorityNamesScore;
    private List<CVs_Knowledge> cVs_knowledgeListToSortBy;
    private CV bestMatchedCvForConsultant;
    private Map<CV, List<List<Integer[]>>> mapWithCvsWithScores;
    private PractisedLanguage languageSearchedBy;
    private int hoursPerWeekFreeSearched;
    private int secondPriorityScore;
    private int priorityYearsScore;
    private int priorityLanguageScore;
    private int priorityIndustryScore;
    private int priorityFreeHoursScore;
    private int mostHaveCVKnowledgeScore;
    private int mostHaveCVKnowledgeAmount;

    private List<String> searchFeaturePrioritizedList;

    public BestMatchOfCVHandler(User consultant, List<CVs_Knowledge> cVs_knowledgeListToSortBy, PractisedLanguage language, int hoursPerWeekFreeSearched, List<String> searchFeaturePrioritizedList, int mostHaveCVKnowledgeAmount) {
        this.consultant = consultant;
        this.cVs_knowledgeListToSortBy = cVs_knowledgeListToSortBy;
        this.languageSearchedBy = language;
        this.hoursPerWeekFreeSearched = hoursPerWeekFreeSearched;
        this.mapWithCvsWithScores = new HashMap<>();
        this.searchFeaturePrioritizedList = searchFeaturePrioritizedList;
        this.mostHaveCVKnowledgeAmount = mostHaveCVKnowledgeAmount;
        this.bestMatchedCvForConsultant = getWeightsAndBestMatchedCVForConsultant();

    }

    public CV getWeightsAndBestMatchedCVForConsultant() {
        List<CV> cvs = this.consultant.getCvs();
        List<CVs_Knowledge> cVs_knowledgeListSearchedCopy = new ArrayList<>(this.cVs_knowledgeListToSortBy);
        for (CV cv : cvs) {
            if (cv.getCVState() == CVState.published) {
                this.mapWithCvsWithScores.put(cv, getPointsForConsultantsCVsMapped(cv, cVs_knowledgeListSearchedCopy));
            }
        }
        CV bestMatchedCV = validatingBestPointsNCV(this.mapWithCvsWithScores);

        List<Integer> mostScoreNPriorityScoreNCountCVKnowledgePoints = validatingCVForCVknowledge(cVs_knowledgeListSearchedCopy, bestMatchedCV, this.mostHaveCVKnowledgeAmount);
        this.mostHaveCVKnowledgeScore = mostScoreNPriorityScoreNCountCVKnowledgePoints.get(0);
        this.priorityScore = mostScoreNPriorityScoreNCountCVKnowledgePoints.get(1);
        this.secondPriorityScore = mostScoreNPriorityScoreNCountCVKnowledgePoints.get(2);
        this.priorityNamesScore = mostScoreNPriorityScoreNCountCVKnowledgePoints.get(3);
        this.priorityYearsScore = mostScoreNPriorityScoreNCountCVKnowledgePoints.get(4);

        if (languageSearchedBy == null) {
            this.priorityLanguageScore = 0;
        } else {
            this.priorityLanguageScore = validatingCVForLanguage(this.languageSearchedBy, bestMatchedCV);
        }

        this.priorityFreeHoursScore = validatingCVForFreeHours(this.hoursPerWeekFreeSearched, bestMatchedCV);
        this.priorityIndustryScore = validatingCVForIndustryToScore(this.cVs_knowledgeListToSortBy, bestMatchedCV);
        return bestMatchedCV;
    }

    private List<List<Integer[]>> getPointsForConsultantsCVsMapped(CV cv, List<CVs_Knowledge> cVs_knowledgeListSearchedCopy) {
        List<List<Integer[]>> pointsLists = new ArrayList<>();
        List<String> searchFeaturesPrioritizedListCopy = new ArrayList<>(this.searchFeaturePrioritizedList);

        for (int i = 0; i < this.searchFeaturePrioritizedList.size(); i++) {
            if (searchFeaturesPrioritizedListCopy.get(i).equals("done")) {
                continue;
            }
            switch (searchFeaturesPrioritizedListCopy.get(i)) {
                case ("cvs_knowlegde"): {
                    if (!this.cVs_knowledgeListToSortBy.isEmpty()) {
                        List<Integer[]> listScoreCVKnowledgeList = settingVectorCVForCVknowledgeListToScoreList(cVs_knowledgeListSearchedCopy, cv);
                        pointsLists.add(listScoreCVKnowledgeList);
                        searchFeaturesPrioritizedListCopy.get(i).replace("cvs_knowlegde", "done");
                    }
                }
                case ("industry"): {
                    if (!this.cVs_knowledgeListToSortBy.isEmpty()) {
                        int priorityIndustryScoreTmp = validatingCVForIndustryToScore(cVs_knowledgeListSearchedCopy, cv);
                        Integer[] industryScoreList = new Integer[1];
                        industryScoreList[0] = priorityIndustryScoreTmp;
                        List<Integer[]> pointsListIndustry = new ArrayList<>();
                        pointsListIndustry.add(industryScoreList);
                        pointsLists.add(pointsListIndustry);
                        searchFeaturesPrioritizedListCopy.get(i).replace("industry", "done");
                    }
                }
                case "language": {
                    if (this.languageSearchedBy != null) {
                        Integer[] pointsForLanguageArray = settingVectorCVForLanguageWithArray(this.languageSearchedBy, cv);
                        List<Integer[]> pointsForLanguageList = new ArrayList<>();
                        pointsForLanguageList.add(pointsForLanguageArray);
                        pointsLists.add(pointsForLanguageList);
                        searchFeaturesPrioritizedListCopy.get(i).replace("language", "done");
                    }
                }
                case "freeHours": {
                    Integer[] freeHouarsPointsArray = validatingCVForFreeHoursToScore(this.hoursPerWeekFreeSearched, cv);
                    List<Integer[]> pointsListsForFreeHours = new ArrayList<>();
                    pointsListsForFreeHours.add(freeHouarsPointsArray);
                    pointsLists.add(pointsListsForFreeHours);
                    searchFeaturesPrioritizedListCopy.get(i).replace("freeHours", "done");
                }
            }
        }
        return pointsLists;
    }


    public CV validatingBestPointsNCV(Map<CV, List<List<Integer[]>>> mapWithCvsWithScores) {
        Pair<CV, List<List<Integer[]>>> bestMatchedCV = new MutablePair<>();
        CV cvToValidate;
        boolean validateOtherCV;

        for (Map.Entry<CV, List<List<Integer[]>>> cvNPoints : mapWithCvsWithScores.entrySet()) {
            cvToValidate = cvNPoints.getKey();
            validateOtherCV = false;
            for (int i = 0; i < cvNPoints.getValue().size(); i++) {
                List<Integer[]> pointsLists = cvNPoints.getValue().get(i);
                for (int k = 0; k < pointsLists.size(); k++) {
                    Integer[] pointsArray = pointsLists.get(k);
                    for (int j = 0; j < pointsArray.length; j++) {
                        if (bestMatchedCV.getKey() == null) {
                            bestMatchedCV = new MutablePair<>(cvToValidate, cvNPoints.getValue());
                        } else {
                            List<Integer[]> bestCVNPoints = bestMatchedCV.getValue().get(i);
                            Integer[] bestPoints = bestCVNPoints.get(k);
                            if (pointsArray[j] == null) {
                                validateOtherCV = true;
                                break;
                            }
                            if (pointsArray[j] == 1 && bestPoints[j] == 1) {
                                continue;
                            } else if (pointsArray[j] == 0 && bestPoints[j] == 0) {
                                continue;
                            } else if (pointsArray[j] == 1 && bestPoints[j] == 0) {
                                bestMatchedCV = new MutablePair<>(cvToValidate, cvNPoints.getValue());
                                validateOtherCV = true;
                            } else if (pointsArray[j] == 0 && bestPoints[j] == 1) {
                                validateOtherCV = true;
                                break;
                            }
                        }
                        if (validateOtherCV) {
                            break;
                        }
                    }
                    if (validateOtherCV) {
                        break;
                    }
                }
                if (validateOtherCV) {
                    break;
                }
            }
        }
        return bestMatchedCV.getKey();
    }

    public Integer[] validatingCVForFreeHoursToScore(int hoursPerWeekFreeSearched, CV cv) {
        int freeHoursPoints = 0;
        if (hoursPerWeekFreeSearched > 0 && (cv.getMaxHours() - cv.getBookedHours() >= this.hoursPerWeekFreeSearched)) {
            freeHoursPoints = 1;
        }
        Integer[] freeHouarsPointsArray = new Integer[1];
        freeHouarsPointsArray[0] = freeHoursPoints;
        List<Integer[]> list = new ArrayList<>();
        list.add(freeHouarsPointsArray);
        return freeHouarsPointsArray;
    }

    public Integer[] settingVectorCVForLanguageWithArray(PractisedLanguage languageSearchedBy, CV cv) {
        Integer[] pointForLangaugeArray = new Integer[3];
        for (PractisedLanguage language : cv.getPractisedLanguages()) {

            //Validating parameter and make sure of no whitespace and lowercase
            String languageNameToSearchBy = languageSearchedBy.getName().toLowerCase().stripIndent();
            String languageName = language.getName().toLowerCase().stripIndent();

            if (languageNameToSearchBy.equals(languageName)) {
                pointForLangaugeArray[0] = 1;

                if (languageSearchedBy.getLevelReading() == null) {
                    pointForLangaugeArray[1] = 0;
                } else {
                    int levelOfSpeakingLanguageRated = Integer.valueOf(LevelOfLanguage.valueOf(
                            language.getLevelReading().replace(" ", "_")).getState());

                    int levelOfSpeakingLanguageRatedSearched = Integer.valueOf(LevelOfLanguage.valueOf(
                            languageSearchedBy.getLevelReading().replace(" ", "_")).getState());

                    if (levelOfSpeakingLanguageRated >= levelOfSpeakingLanguageRatedSearched) {
                        pointForLangaugeArray[1] = 1;
                    } else {
                        pointForLangaugeArray[1] = 0;
                    }
                }

                if (languageSearchedBy.getLevelWritting() == null) {
                    pointForLangaugeArray[2] = 0;
                } else {
                    int levelOfWrittingLanguageRated = Integer.valueOf(LevelOfLanguage.valueOf(
                            language.getLevelWritting().replace(" ", "_")).getState());

                    int levelOfWrittingLanguageRatedSearched = Integer.valueOf(LevelOfLanguage.valueOf(
                            languageSearchedBy.getLevelWritting().replace(" ", "_")).getState());

                    if (levelOfWrittingLanguageRated >= levelOfWrittingLanguageRatedSearched) {
                        pointForLangaugeArray[2] = 1;
                    } else {
                        pointForLangaugeArray[2] = 0;
                    }
                }
            } else {
                pointForLangaugeArray[0] = 0;
                pointForLangaugeArray[1] = 0;
                pointForLangaugeArray[2] = 0;
            }
        }
        return pointForLangaugeArray;
    }

    public int validatingCVForIndustryToScore(List<CVs_Knowledge> cVsKnowledgeListSearched, CV cv) {
        List<CVs_Knowledge> cvsCVknowledgeCopy = new ArrayList<>(cv.getCV_KnowledgeList());

        for (CVs_Knowledge cvKnowledgeToSearchBy : cVsKnowledgeListSearched) {
            if (cvKnowledgeToSearchBy == null || cvKnowledgeToSearchBy.getKnowledge() == null) {
                cVsKnowledgeListSearched.remove(cvKnowledgeToSearchBy);
                break;
            } else if (cvKnowledgeToSearchBy.getKnowledge().getCategory().getName().equals("Industribrancher")) {
                for (CVs_Knowledge cvKnowledgeByCV : cvsCVknowledgeCopy) {
                    if (cvKnowledgeByCV.getKnowledge().getCategory().getName().equals("Industribrancher")
                            && cvKnowledgeByCV.getKnowledge().getName().equals(cvKnowledgeToSearchBy.getKnowledge().getName())
                    ) {
                        return 1;
                    } else if (cvKnowledgeByCV.getKnowledge().getCategory().getName().equals("Industribrancher")) {
                        cv.getCV_KnowledgeList().remove(cvKnowledgeByCV);
                    }
                }
                break;
            }
        }
        return 0;
    }

    public List<Integer[]> settingVectorCVForCVknowledgeListToScoreList(List<CVs_Knowledge> cVsKnowledgeListSearched, CV cv) {
        List<Integer[]> listOfPointsForCVKnowledgeNameNlevelNYearsArrays = new ArrayList<>();

        for (CVs_Knowledge cvKnowledgeToSearchBy : cVsKnowledgeListSearched) {
            int levelOfCVKnowledgeSkillRatedSearched = -100;

            Integer[] pointsForCVKnowledgeNameNlevelNYearsArray = new Integer[3];

            if (cvKnowledgeToSearchBy.getLevelSkill() != null &&
                    !cvKnowledgeToSearchBy.getLevelSkill().isEmpty()) {
                levelOfCVKnowledgeSkillRatedSearched = Integer.valueOf(LevelOfCVKnowledgeSkill.valueOf(
                        cvKnowledgeToSearchBy.getLevelSkill().replace(" ", "_")).getState());
            }
            for (CVs_Knowledge cvKnowledgeByCV : cv.getCV_KnowledgeList()) {
                if (cvKnowledgeToSearchBy == null || cvKnowledgeToSearchBy.getKnowledge() == null) {
                    //skip
                } else if (((cvKnowledgeByCV.getKnowledge().getName() == null)
                        && (cvKnowledgeToSearchBy.getKnowledge().getName() == null)
                        && (!cvKnowledgeByCV.getNote().equals(""))
                        && cvKnowledgeByCV.getNote().equals(cvKnowledgeToSearchBy.getNote()))
                        || ((cvKnowledgeByCV.getKnowledge().getName() != null)
                        && (cvKnowledgeToSearchBy.getKnowledge().getName() != null)
                        && cvKnowledgeByCV.getKnowledge().getName().equals(cvKnowledgeToSearchBy.getKnowledge().getName()))
                ) {
                    pointsForCVKnowledgeNameNlevelNYearsArray[0] = 1;

                    if (cvKnowledgeByCV.getLevelSkill() == null
                            || pointsForCVKnowledgeNameNlevelNYearsArray[0] == null
                            || pointsForCVKnowledgeNameNlevelNYearsArray[0] == 0) {
                        continue;
                    }

                    int levelOfCVKnowledgeSkillRated = Integer.valueOf(LevelOfCVKnowledgeSkill.valueOf(
                            cvKnowledgeByCV.getLevelSkill().replace(" ", "_")).getState());

                    if (levelOfCVKnowledgeSkillRated >= levelOfCVKnowledgeSkillRatedSearched
                            || levelOfCVKnowledgeSkillRatedSearched == -100) {
                        pointsForCVKnowledgeNameNlevelNYearsArray[1] = 1;
                    } else {
                        pointsForCVKnowledgeNameNlevelNYearsArray[1] = 0;
                    }
                    if (cvKnowledgeByCV.getYears() >= cvKnowledgeToSearchBy.getYears()
                            && cvKnowledgeToSearchBy.getYears() >= 0) {
                        pointsForCVKnowledgeNameNlevelNYearsArray[2] = 1;
                    } else {
                        pointsForCVKnowledgeNameNlevelNYearsArray[2] = 0;
                    }
                }
            }
            if (pointsForCVKnowledgeNameNlevelNYearsArray[0] == null || pointsForCVKnowledgeNameNlevelNYearsArray[0] == 0) {
                pointsForCVKnowledgeNameNlevelNYearsArray[0] = 0;
                pointsForCVKnowledgeNameNlevelNYearsArray[1] = 0;
                pointsForCVKnowledgeNameNlevelNYearsArray[2] = 0;
            }
            listOfPointsForCVKnowledgeNameNlevelNYearsArrays.add(pointsForCVKnowledgeNameNlevelNYearsArray);
        }
        return listOfPointsForCVKnowledgeNameNlevelNYearsArrays;
    }

    public int validatingCVForLanguage(PractisedLanguage languageSearchedBy, CV cv) {
        int points = 0;

        for (PractisedLanguage language : cv.getPractisedLanguages()) {

            String languageNameToSearchBy = languageSearchedBy.getName().toLowerCase().stripIndent();
            String languageName = language.getName().toLowerCase().stripIndent();

            if (languageNameToSearchBy.equals(languageName)) {
                points++;

                if (languageSearchedBy.getLevelReading() != null) {
                    int levelOfSpeakingLanguageRated = Integer.valueOf(LevelOfLanguage.valueOf(
                            language.getLevelReading().replace(" ", "_")).getState());

                    int levelOfSpeakingLanguageRatedSearched = Integer.valueOf(LevelOfLanguage.valueOf(
                            languageSearchedBy.getLevelReading().replace(" ", "_")).getState());

                    if (levelOfSpeakingLanguageRated >= levelOfSpeakingLanguageRatedSearched) {
                        points++;
                    }
                }

                if (languageSearchedBy.getLevelWritting() != null){
                    int levelOfWrittingLanguageRated = Integer.valueOf(LevelOfLanguage.valueOf(
                            language.getLevelWritting().replace(" ", "_")).getState());

                    int levelOfWrittingLanguageRatedSearched = Integer.valueOf(LevelOfLanguage.valueOf(
                            languageSearchedBy.getLevelWritting().replace(" ", "_")).getState());

                    if (levelOfWrittingLanguageRated >= levelOfWrittingLanguageRatedSearched) {
                        points++;
                    }
                }
            }
        }
        return points;
    }

    public Integer validatingCVForFreeHours(int freeHoursPerWeekSearched, CV cv) {
        int freeHours = cv.getMaxHours() - cv.getBookedHours();
        if (freeHoursPerWeekSearched <= freeHours) {
            return 1;
        }
        return 0;
    }

    public List<Integer> validatingCVForCVknowledge(List<CVs_Knowledge> cVs_knowledgeListToSortBy, CV cv, int knowledgeMostAmount) {
        int priorityScoreTmp = 0;
        int priorityNameScoreTmp = 0;
        int priorityYearScoreTmp = 0;
        int secondPriorityScoreTmp = 0;
        int highNameScoreTmp = 0;
        int countCVKnowledgeFromSearchedList = 0;

        for (CVs_Knowledge cvKnowledgeToSearchBy : cVs_knowledgeListToSortBy) {
            int levelOfCVKnowledgeSkillRatedSearched = -1000;
            countCVKnowledgeFromSearchedList++;

            if (cvKnowledgeToSearchBy.getLevelSkill() != null
                    && cvKnowledgeToSearchBy.getLevelSkill() != "") {
                levelOfCVKnowledgeSkillRatedSearched = Integer.valueOf(LevelOfCVKnowledgeSkill.valueOf(
                        cvKnowledgeToSearchBy.getLevelSkill().replace(" ", "_")).getState());
            }

            for (CVs_Knowledge cvKnowledgeByCV : cv.getCV_KnowledgeList()) {
                if (cvKnowledgeToSearchBy == null
                        || cvKnowledgeToSearchBy.getKnowledge() == null
                        || cvKnowledgeToSearchBy.getKnowledge().getCategory().getName().equals("Industribrancher")
                        || cvKnowledgeByCV.getKnowledge().getCategory().getName().equals("Industribrancher")) {
                    continue;
                } else if ((cvKnowledgeByCV.getKnowledge().getName() == null
                        && cvKnowledgeToSearchBy.getKnowledge().getName() == null
                        && !cvKnowledgeByCV.getNote().equals("")
                        && cvKnowledgeByCV.getNote().equals(cvKnowledgeToSearchBy.getNote()))
                        || (cvKnowledgeByCV.getKnowledge().getName() != null
                        && cvKnowledgeToSearchBy.getKnowledge().getName() != null
                        && cvKnowledgeByCV.getKnowledge().getName().equals(cvKnowledgeToSearchBy.getKnowledge().getName()))
                ) {
                    priorityScoreTmp++;
                    if (countCVKnowledgeFromSearchedList <= knowledgeMostAmount) {
                        highNameScoreTmp++;
                    }
                    priorityNameScoreTmp = priorityNameScoreTmp + (cVs_knowledgeListToSortBy.size() - cVs_knowledgeListToSortBy.indexOf(cvKnowledgeToSearchBy));

                    int levelOfCVKnowledgeSkillRated = Integer.valueOf(LevelOfCVKnowledgeSkill.valueOf(
                            cvKnowledgeByCV.getLevelSkill().replace(" ", "_")).getState());

                    if (levelOfCVKnowledgeSkillRated >= levelOfCVKnowledgeSkillRatedSearched || levelOfCVKnowledgeSkillRatedSearched == -1000) {
                        secondPriorityScoreTmp = secondPriorityScoreTmp + (cVs_knowledgeListToSortBy.size() - cVs_knowledgeListToSortBy.indexOf(cvKnowledgeToSearchBy));
                    }

                    if (cvKnowledgeByCV.getYears() >= cvKnowledgeToSearchBy.getYears()
                            && cvKnowledgeToSearchBy.getYears() > 0) {
                        priorityYearScoreTmp = priorityYearScoreTmp + (cVs_knowledgeListToSortBy.size() - cVs_knowledgeListToSortBy.indexOf(cvKnowledgeToSearchBy));
                    }
                }
            }
        }
        List<Integer> scores = new ArrayList<>();
        scores.add(highNameScoreTmp);
        scores.add(priorityScoreTmp);
        scores.add(secondPriorityScoreTmp);
        scores.add(priorityNameScoreTmp);
        scores.add(priorityYearScoreTmp);
        return scores;
    }

    public User getConsultant() {
        return this.consultant;
    }

    public List<CVs_Knowledge> getcVs_knowledgeListToSortBy() {
        return this.cVs_knowledgeListToSortBy;
    }

    public CV getBestMatchedCvForConsultant() {
        return this.bestMatchedCvForConsultant;
    }

    public PractisedLanguage getLanguageSearchedBy() {
        return this.languageSearchedBy;
    }

    public int getHoursPerWeekFreeSearched() {
        return this.hoursPerWeekFreeSearched;
    }

    public Map<CV, List<List<Integer[]>>> getMapWithCvsWithScores() {
        return this.mapWithCvsWithScores;
    }

    public int getPriorityScore() {
        return this.priorityScore;
    }

    public int getSecondPriorityScore() {
        return this.secondPriorityScore;
    }

    public int getPriorityNamesScore() {
        return this.priorityNamesScore;
    }

    public int getPriorityYearsScore() {
        return this.priorityYearsScore;
    }

    public int getPriorityLanguageScore() {
        return this.priorityLanguageScore;
    }

    public int getPriorityIndustryScore() {
        return this.priorityIndustryScore;
    }

    public int getPriorityFreeHoursScore() {
        return this.priorityFreeHoursScore;
    }

    public List<String> getSearchFeatureList() {
        return searchFeaturePrioritizedList;
    }

    public int getMostHaveCVKnowledgeScore() {
        return mostHaveCVKnowledgeScore;
    }

    @Override
    public int compareTo(@NotNull BestMatchOfCVHandler o) {
        return o.getConsultant().getId() - this.getConsultant().getId();
    }

}
