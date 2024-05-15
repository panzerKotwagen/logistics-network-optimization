package ru.kotb.lno.optimization.schemes;

import ru.kotb.lno.optimization.GlobalCriteria;

import java.util.List;


/**
 * The class that describes various compromise schemes in multi-criteria
 * optimization
 */
public interface CompromiseScheme {

    /**
     * Finds the optimal vector
     *
     * @param globalCriteriaList global criteria list
     * @return optimal vector
     */
    GlobalCriteria findOptimal(List<GlobalCriteria> globalCriteriaList);
}
