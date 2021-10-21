package com.lahmxu.log.pojo;

import lombok.Data;

@Data
public class Operator {

    private String operatorId;

    private String name;

    public Operator(String name) {
        this.name = name;
    }
}
