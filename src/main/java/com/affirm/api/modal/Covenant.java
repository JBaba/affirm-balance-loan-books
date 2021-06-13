package com.affirm.api.modal;

import com.affirm.api.predicate.BannedStatePredicate;
import com.affirm.api.predicate.IPredicate;
import com.affirm.api.predicate.MaxDefaultPredicate;

public class Covenant implements IPredicate {

    private final MaxDefaultPredicate maxDefaultPredicate;
    private final BannedStatePredicate bannedStatePredicate;
    private int bankID;
    private int facilityId;
    private float maxDefaultLikelihood;
    private String bannedState;

    public Covenant(int bankID, int facilityId, float maxDefaultLikelihood, String bannedState) {
        this.bankID = bankID;
        this.facilityId = facilityId;
        this.maxDefaultLikelihood = maxDefaultLikelihood;
        this.bannedState = bannedState;
        this.maxDefaultPredicate = new MaxDefaultPredicate(maxDefaultLikelihood);
        this.bannedStatePredicate = new BannedStatePredicate(bannedState);
    }

    @Override
    public boolean isValidLoan(Loan loan) {
        return maxDefaultPredicate.isValidLoan(loan)
                && bannedStatePredicate.isValidLoan(loan);
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

    public float getMaxDefaultLikelihood() {
        return maxDefaultLikelihood;
    }

    public void setMaxDefaultLikelihood(float maxDefaultLikelihood) {
        this.maxDefaultLikelihood = maxDefaultLikelihood;
    }

    public String getBannedState() {
        return bannedState;
    }

    public void setBannedState(String bannedState) {
        this.bannedState = bannedState;
    }

    @Override
    public String toString() {
        return "Covenant{" +
                "bankID=" + bankID +
                ", facilityId=" + facilityId +
                ", maxDefaultLikelihood=" + maxDefaultLikelihood +
                ", bannedState='" + bannedState + '\'' +
                '}';
    }

}
