package com.affirm.api.util;

import com.affirm.api.exception.FileWritingException;
import com.affirm.api.modal.Facility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class FileWriteUtil {
    public static void writeAssignmentsCsvFile(List<Map.Entry<Integer, Integer>> assignments) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("loan_id,facility_id\n");
        for (Map.Entry<Integer, Integer> entry : assignments) {
            stringBuilder.append(entry.getKey())
                    .append(",")
                    .append(entry.getValue()).append("\n");
        }
        writeFile(stringBuilder, "assignments.csv");
    }

    public static void writeYieldsCsvFile(Map<Integer, Facility> facilityMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("facility_id,expected_yield\n");
        for (Facility facility : facilityMap.values()) {
            stringBuilder.append(facility.getFacilityId())
                    .append(",")
                    .append(facility.expectedYield()).append("\n");
        }
        writeFile(stringBuilder, "yields.csv");
    }

    private static void writeFile(StringBuilder stringBuilder, String fileName) {
        try {
            File file = new File("./" + fileName);
            file.createNewFile();
            Files.write(Paths.get(file.toURI()), stringBuilder.toString().getBytes());
        } catch (IOException e) {
            throw new FileWritingException("Failed to write assignments.csv file.", e);
        }
    }
}
