package com.affirm.api.modal;

import com.affirm.api.predicate.FacilityOverdrawPredicate;

import java.util.ArrayList;
import java.util.List;

public class Facility {
    private final List<Loan> fundedLoans;
    private int bankID;
    private int facilityId;
    private float interestRate;
    private int amount;
    private int fundedAmount;
    private List<Covenant> covenants;
    private final FacilityOverdrawPredicate facilityOverdrawPredicate;

    public Facility(int bankID, int facilityId, float interestRate, int amount) {
        this.bankID = bankID;
        this.facilityId = facilityId;
        this.interestRate = interestRate;
        this.amount = amount;
        this.fundedAmount = 0;
        this.fundedLoans = new ArrayList<>();
        facilityOverdrawPredicate = new FacilityOverdrawPredicate(this);
    }

    // loan calculator yield help functions

    /**
     * check can we fund the loan
     *  1. check all covenants rules are satisfied
     *  2. facility is overdrawn or not with new loan
     * @param loan input
     * @return true or false
     */
    public boolean canFacilityFundLoan(Loan loan) {
        if (!isValidCovenant(loan)) return false;
        return facilityOverdrawPredicate.isValidLoan(loan);
    }

    /**
     * 1. check all covenants rules are satisfied
     * @param loan input
     * @return true or false
     */
    private boolean isValidCovenant(Loan loan) {
        boolean isValidLoan = true;
        for (Covenant covenant : covenants) {
            isValidLoan &= covenant.isValidLoan(loan);
        }
        return isValidLoan;
    }

    /**
     * fund the loan using facility amount
     *  1. keep track of loan by adding into fundedLoans list
     *  2. increase the funded loan amount
     * @param loan input
     */
    public void fundLoan(Loan loan) {
        fundedLoans.add(loan);
        increaseFundedAmount(loan.getAmount());
    }

    /**
     * increase the funded loan amount
     * @param loanAmount amount
     */
    private void increaseFundedAmount(int loanAmount) {
        fundedAmount += loanAmount;
    }

    /**
     * calculate yield of facility
     * @return total yield
     */
    public long expectedYield() {
        double yieldAmount = 0;
        for (Loan loan : fundedLoans) {
            yieldAmount += calculateYield(loan);
        }
        return Math.round(yieldAmount);
    }

    /**
     * calculate yield for individual facility
     * @param loan input loan
     * @return amount
     */
    private float calculateYield(Loan loan) {
        return ((1 - loan.getDefaultLikelihood()) * loan.getInterestRate() * loan.getAmount())
                - (loan.getDefaultLikelihood() * loan.getAmount())
                - (interestRate * loan.getAmount());
    }

    // getter and setters

    public void setCovenants(List covenants) {
        this.covenants = covenants;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Loan> getFundedLoans() {
        return fundedLoans;
    }

    public int getFundedAmount() {
        return fundedAmount;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "bankID=" + bankID +
                ", facilityId=" + facilityId +
                ", interestRate=" + interestRate +
                ", amount=" + amount +
                '}';
    }
}
