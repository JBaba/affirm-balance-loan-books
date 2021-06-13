package com.affirm.api.predicate;

import com.affirm.api.modal.Loan;

public interface IPredicate {
    boolean isValidLoan(Loan loan);
}
