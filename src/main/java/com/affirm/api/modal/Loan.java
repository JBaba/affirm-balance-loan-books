package com.affirm.api.modal;

public class Loan {
    private int id;
    private int amount;
    private float interestRate;
    private float defaultLikelihood;
    private String state;

    public Loan(int id, int amount, float interestRate, float defaultLikelihood, String state) {
        this.id = id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.defaultLikelihood = defaultLikelihood;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public float getDefaultLikelihood() {
        return defaultLikelihood;
    }

    public void setDefaultLikelihood(float defaultLikelihood) {
        this.defaultLikelihood = defaultLikelihood;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", defaultLikelihood=" + defaultLikelihood +
                ", state='" + state + '\'' +
                '}';
    }
}
