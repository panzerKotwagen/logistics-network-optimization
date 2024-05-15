package ru.kotb.lno.optimization;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;


/**
 * A class describing the global criteria for any strategy
 */
@Getter
public class GlobalCriteria {

    private static final int CRITERIA_COUNT = 2;

    private final int[] localCriteriaArray = new int[CRITERIA_COUNT];

    public static final GlobalCriteria MAX_CRITERIA = new GlobalCriteria(Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final GlobalCriteria ZERO_CRITERIA = new GlobalCriteria(0, 0);

    public GlobalCriteria(int ... criteriaList) {
        System.arraycopy(criteriaList, 0, localCriteriaArray, 0, CRITERIA_COUNT);
    }

    public GlobalCriteria add(GlobalCriteria globalCriteria) {
        int[] summarizedCriteriaArray = new int[2];
        for (int i = 0; i < CRITERIA_COUNT; i++) {
            summarizedCriteriaArray[i] = this.getLocalCriteriaArray()[i] + globalCriteria.getLocalCriteriaArray()[i];
        }
        return new GlobalCriteria(summarizedCriteriaArray);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalCriteria that)) return false;
        return Arrays.equals(localCriteriaArray, that.localCriteriaArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(localCriteriaArray);
    }
}
