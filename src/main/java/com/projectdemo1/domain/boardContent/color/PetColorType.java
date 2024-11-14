package com.projectdemo1.domain.boardContent.color;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PetColorType {

    BLACK,
    WHITE,
    GRAY,
    CREME,
    CHOCOLATE,
    GOLDEN,
    MIXED,
    OTHER;

    @JsonCreator
    public static PetColorType fromString(String value) {
        return PetColorType.valueOf(value.toUpperCase());
    }

    public String getColor() {
        return name();
    }
}
