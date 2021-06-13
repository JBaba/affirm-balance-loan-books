package com.affirm.api;

import com.affirm.api.service.AssignmentYieldService;

public class App {
    public static void main(String[] args) {
        // large resource processing
        AssignmentYieldService service = new AssignmentYieldService(false);
        // small resource processing
//        AssignmentYieldService service = new AssignmentYieldService(true);
        service.run();
        service.writeFiles();
    }
}
