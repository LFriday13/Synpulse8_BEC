package com.kafka.kafkadocker.restservices;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;

public class LegacyBS {
    private final List<String> accounts = Arrays.asList("P-111_111_1111", "P-222_222_2222", "P-333_333_3333", "P-444_444_4444", "P-555_555_5555", "P-666_666_6666");
    private final List<String> currencies = Arrays.asList("GBP", "EUR", "CHF", "HKD");
    private final Random rand = new Random(92_000_000);
    private final long minDay = LocalDate.of(2022,1,1).toEpochDay();
    private final long maxDay = LocalDate.of(2023,5,31).toEpochDay();

    public Transaction getTransaction() {
        return new Transaction()
                .setId("T-"+Long.toString(Transaction.generateID()))
                .setAmount(randomCurrency() + rand.nextInt(1000))
                .setIban(randomAccount())
                .setDate(randomDate())
                .setDescription(Long.toString(rand.nextLong()));
    }

    private String randomCurrency() {
        return currencies.get(rand.nextInt(currencies.size()));
    }

    private String randomAccount() {
        return accounts.get(rand.nextInt(accounts.size()));
    }

    private LocalDate randomDate() {
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
