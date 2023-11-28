package com.fiap.pedido.application.service;

import com.fiap.pedido.application.port.PedidoServicePort;
import com.fiap.pedido.domain.Pedido;
import com.fiap.pedido.domain.StatusPedido;
import com.fiap.pedido.infrastruture.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;


@Service
public class PedidoServiceImpl implements PedidoServicePort {

    private final PedidoRepository pedidoRepository;

    private final BlockingQueue<Long> clienteIdQueue;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, BlockingQueue<Long> clienteIdQueue) {
        this.pedidoRepository = pedidoRepository;
        this.clienteIdQueue = clienteIdQueue;
    }

    public void onMessageReceived(Long clienteId) {
        // Método chamado pelo consumidor Kafka quando uma mensagem é recebida
        clienteIdQueue.add(clienteId);
    }

    @Override
    public Pedido criarPedido(Pedido pedido) throws InterruptedException {
        // Aguarda até que um clienteId esteja disponível na fila
        Long clienteId = clienteIdQueue.take();
        pedido.setClienteId(clienteId);
        pedido.setDataCriacao(new Date());
        pedido.setStatus(StatusPedido.RECEBIDO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido atualizarPedido(Long id, Pedido pedidoAtualizado) {
        return pedidoRepository.findById(id)
                .map(pedidoExistente -> {
                    pedidoExistente.setDataCriacao(pedidoAtualizado.getDataCriacao());
                    pedidoExistente.setItens(pedidoAtualizado.getItens());
                    pedidoExistente.setStatus(pedidoAtualizado.getStatus());
                    pedidoExistente.setValorTotal(pedidoAtualizado.getValorTotal());
                    return pedidoRepository.save(pedidoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Override
    public void deletarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    public Pedido atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(novoStatus);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido prepararPedido(Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (pedido.getStatus() == StatusPedido.RECEBIDO) {
                        pedido.setStatus(StatusPedido.EMPREPARACAO);
                        // Lógica adicional para preparação do pedido
                    } else {
                        throw new RuntimeException("Pedido não está no estado apropriado para preparação");
                    }
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}