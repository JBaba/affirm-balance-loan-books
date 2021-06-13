package com.affirm.api.parser;

import com.affirm.api.exception.CsvParserException;
import com.affirm.api.modal.Facility;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class FacilityCsvParser implements ICsvParser<Integer, Facility> {

    private final String fileName;
    private int bankIdColumnIndex;
    private int facilityColumnIdIndex;
    private int intRateColumnIndex;
    private int amountColumnIndex;

    public FacilityCsvParser(boolean isSmall) {
        fileName = ((isSmall) ? "small" : "large") + "/facilities.csv";
    }

    @Override
    public Map<Integer, Facility> parse() {
        return readCsv();
    }

    // read file and create cache of "key -> Facility"
    private Map<Integer, Facility> readCsv() {
        Map<Integer, Facility> facilityMap = new HashMap<>();
        try {
            BufferedReader reader = brReader(fileName);
            String line;
            // find order of fields
            parseHeader(reader.readLine());
            while ((line = reader.readLine()) != null) {
                Facility facility = create(line);
                facilityMap.put(facility.getFacilityId(), facility);
            }
            return facilityMap;
        } catch (Exception e) {
            throw new CsvParserException("Error while reading csv file: " + fileName, e);
        }
    }

    // find order of fields
    private void parseHeader(String line) {
        String[] fields = line.split(",");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            switch (field) {
                case "bank_id" -> bankIdColumnIndex = i;
                case "id" -> facilityColumnIdIndex = i;
                case "interest_rate" -> intRateColumnIndex = i;
                case "amount" -> amountColumnIndex = i;
                default -> throw new CsvParserException("Unexpected column name: " + field);
            }
        }
    }

    // create Facility object
    private Facility create(String line) {
        try {
            String[] fields = line.split(",");
            // remove decimals from int facility amount
            String amount = fields[amountColumnIndex];
            String amountWithoutZeroDecimals = amount.substring(0, amount.indexOf('.'));
            return new Facility(parseInt(fields[bankIdColumnIndex]),
                    parseInt(fields[facilityColumnIdIndex]),
                    parseFloat(fields[intRateColumnIndex]),
                    parseInt(amountWithoutZeroDecimals));
        } catch (Exception e) {
            throw new CsvParserException("Facility create failed for input line: " + line, e);
        }
    }

}
