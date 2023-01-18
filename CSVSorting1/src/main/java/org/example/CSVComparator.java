package org.example;

import java.util.Comparator;
public class CSVComparator implements Comparator<String> {
    private final String fieldName;

    public CSVComparator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public int compare(String line1, String line2) {
        String[] fields1 = line1.split(",");
        String[] fields2 = line2.split(",");

        int fieldIndex = -1;

        if (fieldName.equals("first_name")) {
            fieldIndex = 0;
        } else if (fieldName.equals("last_name")) {
            fieldIndex = 1;
        } else if (fieldName.equals("age")) {
            fieldIndex = 2;
        }

        if (fieldIndex == -1) {
            throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        if (fieldName.equals("age")) {
            return Integer.compare(Integer.parseInt(fields1[2]), Integer.parseInt(fields2[2]));
        }
        return fields1[fieldIndex].compareTo(fields2[fieldIndex]);
    }

}


