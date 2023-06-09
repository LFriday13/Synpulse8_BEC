package com.kafka.kafkadocker.restservices;

import java.time.LocalDate;
import java.util.Random;

public class Transaction {

    
    private static Random rand = new Random();
    private static long sequence = rand.nextInt();
    private String tid;
    private String amount;
    private String iban;
    private LocalDate date;
    private String desc;

    public static long generateID() {
        sequence = sequence + rand.nextInt(100);
        return sequence;
    }

    public String getId() {
        return tid;
    }

    public Transaction setId(String tid) {
        this.tid = tid;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public Transaction setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Transaction setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Transaction setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return desc;
    }

    public Transaction setDescription(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "tid='" + tid + '\'' +
                ", amount=" + amount +
                ", iban=" + iban +
                ", date=" + date +
                ", desc=" + desc +
                '}';
    }
    
}