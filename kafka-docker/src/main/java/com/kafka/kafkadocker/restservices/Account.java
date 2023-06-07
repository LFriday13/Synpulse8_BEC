package com.kafka.kafkadocker.restservices;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private String holder;
    private String funds;
    private String currency;
    public Account(
            @JsonProperty String holder,
            @JsonProperty String funds,
            @JsonProperty String currency) {
        this.holder = holder;
        this.funds = funds;
        this.currency = currency;
    }
    public Account() {
    }
    
    public String getHolder() {
        return holder;
    }
    public void setHolder(String holder) {
        this.holder = holder;
    }
    
    public String getFunds() {
        return funds;
    }
    public void setFunds(String funds) {
        this.funds = funds;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}