package com.fiap.cliente.application.port;


import com.fiap.cliente.domain.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteServicePort {

    Cliente criarCliente(Cliente cliente);

    Optional<Cliente> buscarClientePorId(Long id);

    List<Cliente> listarTodosClientes();

    Optional<Cliente> buscarClientePorEmail(String email);

    Optional<Cliente> buscarClientePorCpf(String cpf);
    Cliente atualizarCliente(Long id, Cliente cliente);

    void deletarCliente(Long id);
}
