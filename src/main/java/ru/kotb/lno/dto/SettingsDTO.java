package ru.kotb.lno.dto;

import lombok.Getter;


@Getter
public class SettingsDTO {

    private final String start;

    private final String end;

    private final int mainCriteriaNum;

    private final double concessionValue;

    public SettingsDTO(String start, String end, int mainCriteriaNum, double concessionValue) {
        this.start = start;
        this.end = end;
        this.mainCriteriaNum = mainCriteriaNum;
        this.concessionValue = concessionValue;
    }
}
