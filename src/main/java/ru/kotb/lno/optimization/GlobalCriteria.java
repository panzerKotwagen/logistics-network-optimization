package ru.kotb.lno.optimization;

import lombok.Getter;


/**
 * A class describing the global criteria for any strategy
 */
@Getter
public class GlobalCriteria {

    private static final int CRITERIA_COUNT = 2;

    private final int[] localCriteriaArray = new int[CRITERIA_COUNT];

    public GlobalCriteria(int[] criteriaList) {
        System.arraycopy(criteriaList, 0, localCriteriaArray, 0, CRITERIA_COUNT);
    }
}
