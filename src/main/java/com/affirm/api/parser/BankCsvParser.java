package com.affirm.api.parser;

import com.affirm.api.exception.CsvParserException;
import com.affirm.api.modal.Bank;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class BankCsvParser implements ICsvParser<Integer, Bank> {

    private final String fileName;
    private int bankIdColumnIndex;
    private int bankNameColumnIndex;

    public BankCsvParser(boolean isSmall) {
        fileName = ((isSmall) ? "small" : "large") + "/banks.csv";
    }

    @Override
    public Map<Integer, Bank> parse() {
        return readCsv();
    }

    // read file and create cache of "key -> Bank"
    private Map<Integer, Bank> readCsv() {
        Map<Integer, Bank> banks = new HashMap<>();
        try {
            BufferedReader reader = brReader(fileName);
            String line;
            // find order of fields
            parseHeader(reader.readLine());
            while ((line = reader.readLine()) != null) {
                Bank bank = create(line);
                banks.put(bank.getBankId(), bank);
            }
            return banks;
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
                case "id" -> bankIdColumnIndex = i;
                case "name" -> bankNameColumnIndex = i;
                default -> throw new CsvParserException("Unexpected column name: " + field);
            }
        }
    }

    // create Bank object
    private Bank create(String line) {
        try {
            String[] fields = line.split(",");
            return new Bank(parseInt(fields[bankIdColumnIndex]), fields[bankNameColumnIndex]);
        } catch (Exception e) {
            throw new CsvParserException("Create bank failed for input line: " + line, e);
        }
    }

}
