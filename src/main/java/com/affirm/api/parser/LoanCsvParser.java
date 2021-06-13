package com.affirm.api.parser;

import com.affirm.api.exception.CsvParserException;
import com.affirm.api.modal.Loan;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoanCsvParser implements ICsvParser<Integer, Loan> {

    private final String fileName;
    private int idColumnIndex;
    private int intRateColumnIndex;
    private int amountColumnIndex;
    private int defaultLikelihoodColumnIndex;
    private int stateColumnIndex;

    public LoanCsvParser(boolean isSmall) {
        fileName = ((isSmall) ? "small" : "large") + "/loans.csv";
    }

    @Override
    public Map<Integer, Loan> parse() {
        return readCsv();
    }

    // read file and create cache of "key -> Loan"
    private Map<Integer, Loan> readCsv() {
        Map<Integer, Loan> loanMap = new LinkedHashMap<>();
        try {
            BufferedReader reader = brReader(fileName);
            String line;
            // find order of fields
            parseHeader(reader.readLine());
            while ((line = reader.readLine()) != null) {
                Loan loan = create(line);
                loanMap.put(loan.getId(), loan);
            }
            return loanMap;
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
                case "id" -> idColumnIndex = i;
                case "interest_rate" -> intRateColumnIndex = i;
                case "amount" -> amountColumnIndex = i;
                case "default_likelihood" -> defaultLikelihoodColumnIndex = i;
                case "state" -> stateColumnIndex = i;
                default -> throw new CsvParserException("Unexpected column name: " + field);
            }
        }
    }

    // create Loan object
    private Loan create(String line) {
        try {
            String[] fields = line.split(",");
            return new Loan(parseInt(fields[idColumnIndex]),
                    parseInt(fields[amountColumnIndex]),
                    parseFloat(fields[intRateColumnIndex]),
                    parseFloat(fields[defaultLikelihoodColumnIndex]),
                    fields[stateColumnIndex]);
        } catch (Exception e) {
            throw new CsvParserException("Loan create failed for input line: " + line, e);
        }
    }

}
