package com.affirm.api.exception;

public class CsvParserException extends RuntimeException {
    public CsvParserException(String msg) {
        super(msg);
    }

    public CsvParserException(String msg, Throwable cause) {
        super(msg, cause);
    }

}


