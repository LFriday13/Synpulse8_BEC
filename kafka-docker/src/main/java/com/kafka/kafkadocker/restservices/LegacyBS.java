package com.kafka.kafkadocker.restservices;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LegacyBS {

    private final List<String> customers = Arrays.asList("P-111_111_1111", "P-222_222_2222", "P-333_333_3333", "P-444_444_4444", "P-555_555_5555", "P-666_666_6666");
    private final List<String> currencies = Arrays.asList("GBP", "EUR", "CHF", "HKD");
    private final Random rand = new Random(92_000_000);

    public Transaction getTransaction() {

        return new Transaction()
                .setAmount(rand.nextInt(100))
                .setCustomer(randomCustomer())
                .setCategory(categories.get(rand.nextInt(categories.size())))
                .setOccurred(new Date().getTime());
    }

    private String randomCustomer() {

        return customers.get(rand.nextInt(customers.size()));
    }
}
