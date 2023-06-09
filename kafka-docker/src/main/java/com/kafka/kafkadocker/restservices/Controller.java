package com.kafka.kafkadocker.restservices;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Vector;
import java.time.Duration;
import java.util.Arrays;

@RequestMapping ("/api/kafka")
@RestController
public class Controller {
    final KafkaTemplate<String,Transaction> kafkaTemplate;
    Logger logger = LoggerFactory.getLogger(Controller.class);
    
    public Controller(KafkaTemplate<String,Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    String server = "127.0.0.1:9092";
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    @PostMapping(value = "/publish")
    public String sentMessage(@RequestBody Transaction transaction) {
        String topic = transaction.getIban();
        this.kafkaTemplate.send(topic, (new Transaction()
            .setId(transaction.getId()) 
            .setAmount(transaction.getAmount())
            .setDate(transaction.getDate())
            .setIban(transaction.getIban())
            .setDescription(transaction.getDescription())));
        return "Posted Transaction";
    }

    private long start_date;
    private long end_date;
    // Calculate Timestamp borders of a month
    private void calculateDateParam(int year, int month){
        Calendar cal = new GregorianCalendar(year, month, 1);
        start_date = cal.getTimeInMillis();
        cal = new GregorianCalendar(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        end_date = cal.getTimeInMillis(); 
    }

    private int convertCurrency(String amount){
        int value = Integer.parseInt(amount.substring(4));
        return value;
    }

    /* Method to return transactions */
    @GetMapping
    public String getTransactions(@RequestParam("customer") String customer, @RequestParam("year") int year, @RequestParam("month") int month) {
        
        // Convert Query Parameters to Timestamps
        calculateDateParam(year, month);
        
        // Set relevant Topic, Partition
        String topic = customer; // This would execute as a que
        TopicPartition partition = new TopicPartition(topic, 0);

        KafkaConsumer<String, Transaction> consumer = new KafkaConsumer<String, Transaction>(properties); // Create Consumer

        long offsetBeginning = consumer.offsetsForTimes(Collections.singletonMap(partition, start_date).get(partition).offset());
        
        consumer.assign(Arrays.asList(partition));
        consumer.seek(partition, offsetBeginning);

        Vector<String> list = new Vector<>();
        for (ConsumerRecord<String, Transaction> record : consumer.poll()) {
            if (record.timestamp() > end_date) {
                break;
            }
            list.add(record.value().toString());
        }

        return list.toString();
    }

    @KafkaListener(topics = "transaction-1")
    public void listener(@Payload Transaction transaction,  ConsumerRecord<String, Transaction> cr) {
        logger.info("Topic [transaction-1] Received transaction {} from {} ", transaction.getId(), transaction.getIban());
        logger.info(cr.toString());
    }
}
