package com.affirm.api.service;

import com.affirm.api.modal.Covenant;
import com.affirm.api.modal.Facility;
import com.affirm.api.modal.Loan;
import com.affirm.api.parser.CovenantCsvParser;
import com.affirm.api.parser.FacilityCsvParser;
import com.affirm.api.parser.LoanCsvParser;
import com.affirm.api.util.FileWriteUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssignmentYieldService {

    private final List<Loan> loans;
    // facility to covenants map
    private Map<Integer, Facility> facilityMap;
    // facility assignments will be stored as we stream loans
    private final List<Map.Entry<Integer, Integer>> assignments;

    /**
     * @param isSmall identifies if we are reading small folder from resources or
     *                large folder
     */
    public AssignmentYieldService(boolean isSmall) {
        assignments = new ArrayList<>();
        // keep cheapest rate at the top
        sortFacility(new FacilityCsvParser(isSmall).parse());
        linkCovenantToFacilities(new CovenantCsvParser(isSmall).parse());
        loans = new LoanCsvParser(isSmall).parse().values().stream().toList();
    }

    /**
     * Add covenants to its respective facilities
     * @param covenantMap covenants to be linked
     */
    private void linkCovenantToFacilities(Map<Integer, List<Covenant>> covenantMap) {
        for (Map.Entry<Integer, List<Covenant>> entry : covenantMap.entrySet()) {
            facilityMap.get(entry.getKey()).setCovenants(entry.getValue());
        }
    }

    /**
     * sort facilities so cheapest rate is at top
     * @param facilityMap facilities
     */
    private void sortFacility(Map<Integer, Facility> facilityMap) {
        List<Map.Entry<Integer, Facility>> list =
                new ArrayList<>(facilityMap.entrySet());
        // comparator for sorting
        list.sort((o1, o2) -> {
            float rate1 = o1.getValue().getInterestRate();
            float rate2 = o2.getValue().getInterestRate();
            return Float.compare(rate1, rate2);
        });
        // keep order by linked hash map
        this.facilityMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Facility> entry : list) {
            this.facilityMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * processing all loans
     */
    public void run() {
        for (Loan loan : loans) {
            processLoan(loan);
        }
    }

    /**
     * Process single loan
     * @apiNote this method can be expose using rest endpoint
     * @param loan input
     */
    private void processLoan(Loan loan) {
        // start from top as we need to book cheapest first
        for (Facility facility : facilityMap.values()) {
            // we are checking if we can fund the loan
            if (facility.canFacilityFundLoan(loan)) {
                facility.fundLoan(loan);
                // add to assignments for writing to file
                assignments.add(Map.entry(loan.getId(), facility.getFacilityId()));
                break; // exit as no need to proceed
            }
        }
    }

    /**
     * write details to file
     */
    public void writeFiles() {
        FileWriteUtil.writeAssignmentsCsvFile(assignments);
        FileWriteUtil.writeYieldsCsvFile(facilityMap);
    }

}
