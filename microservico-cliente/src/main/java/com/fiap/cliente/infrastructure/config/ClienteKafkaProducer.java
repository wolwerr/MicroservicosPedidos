package com.fiap.cliente.infrastructure.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class ClienteKafkaProducer {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public ClienteKafkaProducer(String servers, String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    public void enviarMensagem(String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        System.out.println("Enviando mensagem - Chave: " + key + ", Valor: " + value);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }
}
