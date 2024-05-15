package ru.kotb.lno.optimization;

import java.util.List;


/**
 * The class that describes various compromise schemes in multi-criteria
 * optimization
 */
public class CompromiseScheme {

    /**
     * Finds the optimal vector based on a weighted sum of criteria
     *
     * @param globalCriteriaList global criteria list
     * @param criteriaWeights    criteria weights
     * @return optimal vector
     */
    public GlobalCriteria convolution(List<GlobalCriteria> globalCriteriaList, double[] criteriaWeights) {
        double maxCriteriaSum = Integer.MIN_VALUE;
        GlobalCriteria optimalCriteria = null;

        for (GlobalCriteria currentCriteria : globalCriteriaList) {
            double criteriaSum = computeCriteriaSum(currentCriteria, criteriaWeights);
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
     * @param criteria        global criteria
     * @param criteriaWeights criteria weights
     * @return weighted sum of criteria
     */
    private double computeCriteriaSum(GlobalCriteria criteria, double[] criteriaWeights) {
        double sum = 0;
        for (int i = 0; i < criteriaWeights.length; i++) {
            sum += criteria.getLocalCriteriaArray()[i] * criteriaWeights[i];
        }
        return sum;
    }
}
