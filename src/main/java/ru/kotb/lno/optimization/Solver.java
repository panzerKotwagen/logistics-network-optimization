package ru.kotb.lno.optimization;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * The class that solve multi-criteria task
 */
public class Solver {

    /**
     * Creates the pareto set
     *
     * @param globalCriteriaList list of compared global criteria
     * @return pareto set
     */
    public List<GlobalCriteria> paretoSet(List<GlobalCriteria> globalCriteriaList) {
        List<GlobalCriteria> paretoSet = new ArrayList<>(globalCriteriaList);
        for (GlobalCriteria criteria1 : globalCriteriaList) {
            paretoSet.remove(criteria1);
            boolean isDominated = false;
            for (GlobalCriteria criteria2 : paretoSet) {
                if (compareTwoCriteria(criteria1, criteria2) < 0) {
                    isDominated = true;
                    break;
                }
            }
            if (!isDominated) {
                paretoSet.add(criteria1);
            }
        }
        return paretoSet;
    }

    /**
     * Compares two global criteria.
     *
     * @param globalCriteria1 first global criteria
     * @param globalCriteria2 second global criteria
     * @return the value 1 if the first one dominates the second one;
     * the value 0 if they are compromise;
     * the value -1 if the second one dominates the first one
     */
    public int compareTwoCriteria(GlobalCriteria globalCriteria1, GlobalCriteria globalCriteria2) {
        int[] localCriteriaArray1 = globalCriteria1.getLocalCriteriaArray();
        int[] localCriteriaArray2 = globalCriteria2.getLocalCriteriaArray();
        boolean isBetter = false;
        boolean isWorse = false;

        for (int i = 0; i < localCriteriaArray1.length; i++) {
            if (localCriteriaArray1[i] > localCriteriaArray2[i]) {
                isBetter = true;
            } else if (localCriteriaArray1[i] < localCriteriaArray2[i]) {
                isWorse = true;
            }
        }

        if (!isBetter && !isWorse || isBetter && isWorse) {
            return 0;
        }
        if (!isBetter) {
            return -1;
        }
        return 1;
    }

    @Getter
    static class GlobalCriteria {

        private static final int CRITERIA_COUNT = 2;

        private final int[] localCriteriaArray = new int[CRITERIA_COUNT];

        public GlobalCriteria(int[] criteriaList) {
            System.arraycopy(criteriaList, 0, localCriteriaArray, 0, CRITERIA_COUNT);
        }
    }
}
