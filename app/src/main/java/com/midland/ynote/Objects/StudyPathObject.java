package com.midland.ynote.Objects;

import java.util.ArrayList;

public class StudyPathObject {

    String mainField;
    ArrayList<String> fieldUnits;

    public StudyPathObject(String mainField, ArrayList<String> fieldUnits) {
        this.mainField = mainField;
        this.fieldUnits = fieldUnits;
    }

    public String getMainField() {
        return mainField;
    }

    public ArrayList<String> getFieldUnits() {
        return fieldUnits;
    }
}
