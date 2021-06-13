package com.affirm.api.predicate;

import com.affirm.api.modal.Facility;
import com.affirm.api.modal.Loan;

/**
 * predicate checks facility will overdrawn with new loan amount
 */
public class FacilityOverdrawPredicate implements IPredicate {

    private final Facility facility;

    public FacilityOverdrawPredicate(Facility facility) {
        this.facility = facility;
    }

    /**
     * check facility will overdrawn with new loan amount
     * @param loan input
     * @return will facility be overdrawn true or false
     */
    @Override
    public boolean isValidLoan(Loan loan) {
        return (facility.getFundedAmount() + loan.getAmount()) <= facility.getAmount();
    }
}
