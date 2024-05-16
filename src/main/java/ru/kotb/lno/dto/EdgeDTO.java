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

    private int weight1;

    private int weight2;
}
