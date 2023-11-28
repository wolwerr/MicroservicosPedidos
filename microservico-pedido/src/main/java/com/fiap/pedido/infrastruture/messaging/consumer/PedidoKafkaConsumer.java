package com.fiap.pedido.infrastruture.messaging.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PedidoKafkaConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final BlockingQueue<Long> clienteIdQueue;

    public PedidoKafkaConsumer(String servers, String topic, BlockingQueue<Long> clienteIdQueue) {
        this.clienteIdQueue = clienteIdQueue;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pedido-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public void runConsumer() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Mensagem recebida - Tópico: %s, Chave: %s, Valor: %s%n", record.topic(), record.key(), record.value());
                    try {
                        Long clienteId = Long.parseLong(record.key());
                        clienteIdQueue.put(clienteId);
                    } catch (NumberFormatException e) {
                        System.err.println("Formato inválido para clienteId: " + record.key());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Consumidor Kafka interrompido.");
                        return;
                    }
                }
            }
        } finally {
            consumer.close();
            System.out.println("Consumidor Kafka fechado.");
        }
    }
}