package com.university.ManageNotes.model.enums;

public enum StudentLevel {
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    LEVEL4(4),
    LEVEL5(5);

    private final int value;

    StudentLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
