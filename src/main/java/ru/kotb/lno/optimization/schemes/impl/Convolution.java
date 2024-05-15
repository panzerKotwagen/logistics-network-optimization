package ru.kotb.lno.optimization.schemes.impl;

import ru.kotb.lno.optimization.schemes.CompromiseScheme;
import ru.kotb.lno.optimization.GlobalCriteria;

import java.util.List;


/**
 * Finds the optimal vector based on a weighted sum of criteria
 */
public class Convolution implements CompromiseScheme {

    /**
     * Criteria weights indicating the significance of each criterion
     * and the degree of its influence on the total amount
     */
    private final double[] criteriaWeights;

    public Convolution(double ... criteriaWeights) {
        this.criteriaWeights = criteriaWeights;
    }

    @Override
    public GlobalCriteria findOptimal(List<GlobalCriteria> globalCriteriaList) {
        double maxCriteriaSum = Integer.MIN_VALUE;
        GlobalCriteria optimalCriteria = null;

        for (GlobalCriteria currentCriteria : globalCriteriaList) {
            double criteriaSum = computeCriteriaSum(currentCriteria);
            if (criteriaSum > maxCriteriaSum) {
                maxCriteriaSum = criteriaSum;
                optimalCriteria = currentCriteria;
            }
        }

        return optimalCriteria;
    }

    /**
     * Calculates weighted sum of criteria
     *
     * @param criteria global criteria
     * @return weighted sum of criteria
     */
    private double computeCriteriaSum(GlobalCriteria criteria) {
        double sum = 0;
        for (int i = 0; i < criteriaWeights.length; i++) {
            sum += criteria.getLocalCriteriaArray()[i] * criteriaWeights[i];
        }
        return sum;
    }
}
