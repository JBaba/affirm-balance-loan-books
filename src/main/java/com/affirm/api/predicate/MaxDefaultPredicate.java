package com.affirm.api.predicate;

import com.affirm.api.modal.Loan;

public class MaxDefaultPredicate implements IPredicate {

    float thresholdRate;

    public MaxDefaultPredicate(float thresholdRate) {
        this.thresholdRate = thresholdRate;
    }

    /**
     * check is default likelihood is under threshold
     *
     * @param loan input
     * @return true or false
     */
    @Override
    public boolean isValidLoan(Loan loan) {
        return thresholdRate >= loan.getDefaultLikelihood();
    }
}
