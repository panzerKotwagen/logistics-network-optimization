package ru.kotb.lno.optimization;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.optimization.schemes.impl.Convolution;

import java.util.ArrayList;
import java.util.List;


class SolverTest {

    private final Solver solver = new Solver();

    private final Convolution convolutionCompromiseScheme
            = new Convolution(0.2, 1);

    @Test
    void compareGlobalCriteria() {
        int[] criteriaList1 = new int[]{1, 2};
        int[] criteriaList2 = new int[]{3, 4};
        int[] criteriaList3 = new int[]{4, 3};
        GlobalCriteria globalCriteria1 = new GlobalCriteria(criteriaList1);
        GlobalCriteria globalCriteria2 = new GlobalCriteria(criteriaList2);
        GlobalCriteria globalCriteria3 = new GlobalCriteria(criteriaList3);

        int res1 = solver.compareTwoCriteria(globalCriteria1, globalCriteria2);
        int res2 = solver.compareTwoCriteria(globalCriteria1, globalCriteria3);
        int res3 = solver.compareTwoCriteria(globalCriteria2, globalCriteria1);
        int res4 = solver.compareTwoCriteria(globalCriteria2, globalCriteria3);
        int res5 = solver.compareTwoCriteria(globalCriteria3, globalCriteria2);

        Assertions.assertThat(res1).isEqualTo(-1);
        Assertions.assertThat(res2).isEqualTo(-1);
        Assertions.assertThat(res3).isEqualTo(1);
        Assertions.assertThat(res4).isEqualTo(0);
        Assertions.assertThat(res5).isEqualTo(0);
    }

    @Test
    void paretoSet() {
        int[] criteriaList1 = new int[]{1, 2};
        int[] criteriaList2 = new int[]{3, 4};
        int[] criteriaList3 = new int[]{4, 3};
        int[] criteriaList4 = new int[]{5, 5};
        GlobalCriteria globalCriteria1 = new GlobalCriteria(criteriaList1);
        GlobalCriteria globalCriteria2 = new GlobalCriteria(criteriaList2);
        GlobalCriteria globalCriteria3 = new GlobalCriteria(criteriaList3);
        GlobalCriteria globalCriteria4 = new GlobalCriteria(criteriaList4);

        List<GlobalCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(globalCriteria1);
        criteriaList.add(globalCriteria2);
        criteriaList.add(globalCriteria3);
        criteriaList.add(globalCriteria4);

        criteriaList = solver.paretoSet(criteriaList);
        Assertions.assertThat(criteriaList.size()).isEqualTo(1);
    }

    @Test
    void convolution() {
        int[] criteriaList1 = new int[]{1, 2};
        int[] criteriaList2 = new int[]{3, 4};
        int[] criteriaList3 = new int[]{4, 3};
        int[] criteriaList4 = new int[]{6, 2};
        GlobalCriteria globalCriteria1 = new GlobalCriteria(criteriaList1);
        GlobalCriteria globalCriteria2 = new GlobalCriteria(criteriaList2);
        GlobalCriteria globalCriteria3 = new GlobalCriteria(criteriaList3);
        GlobalCriteria globalCriteria4 = new GlobalCriteria(criteriaList4);

        List<GlobalCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(globalCriteria1);
        criteriaList.add(globalCriteria2);
        criteriaList.add(globalCriteria3);
        criteriaList.add(globalCriteria4);

        GlobalCriteria optimal = convolutionCompromiseScheme.findOptimal(criteriaList);
        Assertions.assertThat(optimal).isEqualTo(globalCriteria2);
    }
}