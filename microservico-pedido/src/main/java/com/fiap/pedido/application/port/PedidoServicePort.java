package com.fiap.pedido.application.port;

import com.fiap.pedido.domain.Pedido;
import com.fiap.pedido.domain.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoServicePort {

    Pedido criarPedido(Pedido pedido) throws InterruptedException;

    Optional<Pedido> buscarPedidoPorId(Long id);

    List<Pedido> listarTodosPedidos();

    Pedido atualizarPedido(Long id, Pedido pedido);

    void deletarPedido(Long id);

    Pedido atualizarStatusPedido(Long id, StatusPedido novoStatus);

    Pedido prepararPedido(Long id);
}
