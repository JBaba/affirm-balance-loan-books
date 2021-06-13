package com.affirm.api.parser;

import com.affirm.api.exception.CsvParserException;
import com.affirm.api.modal.Covenant;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CovenantCsvParser implements ICsvParser<Integer, List<Covenant>> {

    private final String fileName;
    private int bankIdColumnIndex;
    private int facilityColumnIdIndex;
    private int defaultLikelihoodColumnIndex;
    private int bannedStateColumnIndex;

    public CovenantCsvParser(boolean isSmall) {
        fileName = ((isSmall) ? "small" : "large") + "/covenants.csv";
    }

    @Override
    public Map<Integer, List<Covenant>> parse() {
        return readCsv();
    }

    // read file and create cache of "key -> Covenant"
    private Map<Integer, List<Covenant>> readCsv() {
        Map<Integer, List<Covenant>> covenantMap = new HashMap<>();
        try {
            BufferedReader reader = brReader(fileName);
            String line;
            // find order of fields
            parseHeader(reader.readLine());
            while ((line = reader.readLine()) != null) {
                Covenant covenant = create(line);
                List<Covenant> covenants = covenantMap.getOrDefault(covenant.getFacilityId(),
                        new ArrayList<>());
                covenants.add(covenant);
                covenantMap.put(covenant.getFacilityId(), covenants);
            }
            return covenantMap;
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
                case "facility_id" -> facilityColumnIdIndex = i;
                case "max_default_likelihood" -> defaultLikelihoodColumnIndex = i;
                case "banned_state" -> bannedStateColumnIndex = i;
                default -> throw new CsvParserException("Unexpected column name: " + field);
            }
        }
    }

    // create Covenant object
    private Covenant create(String line) {
        try {
            String[] fields = line.split(",");
            String defaultLikelihood = fields[defaultLikelihoodColumnIndex];
            return new Covenant(parseInt(fields[bankIdColumnIndex]),
                    parseInt(fields[facilityColumnIdIndex]),
                    // if empty max default would be 1
                    defaultLikelihood.isEmpty() ? 1 : parseFloat(defaultLikelihood),
                    fields[bannedStateColumnIndex]);
        } catch (Exception e) {
            throw new CsvParserException("Covenant create failed for input line: " + line, e);
        }
    }

}
