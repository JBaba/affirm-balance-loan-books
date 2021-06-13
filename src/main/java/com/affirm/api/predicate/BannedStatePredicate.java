package com.affirm.api.predicate;

import com.affirm.api.modal.Loan;

/**
 * Predicate checks if given loan has banned state
 */
public class BannedStatePredicate implements IPredicate {

    String bannedState;

    public BannedStatePredicate(String bannedState) {
        this.bannedState = bannedState;
    }

    /**
     * check if given loan has banned state
     * @param loan input
     * @return is loan from banned state true or false
     */
    @Override
    public boolean isValidLoan(Loan loan) {
        return !bannedState.equals(loan.getState());
    }
}
