package ru.kotb.lno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * DTO that represent an edge in graph
 */
@AllArgsConstructor
@Getter
public class EdgeDTO {

    private String source;

    private String target;

    private double weight1;

    private double weight2;
}
