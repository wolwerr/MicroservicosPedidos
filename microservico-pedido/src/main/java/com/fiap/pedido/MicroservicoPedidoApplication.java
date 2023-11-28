package com.fiap.pedido;

import com.fiap.pedido.infrastruture.messaging.consumer.PedidoKafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.BlockingQueue;

@SpringBootApplication
public class MicroservicoPedidoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MicroservicoPedidoApplication.class, args);
        BlockingQueue<Long> clienteIdQueue = context.getBean(BlockingQueue.class);

        PedidoKafkaConsumer consumer = new PedidoKafkaConsumer("localhost:9092", "clientePedidoTopic", clienteIdQueue);
        Thread consumerThread = new Thread(consumer::runConsumer);
        consumerThread.start();
    }
}
