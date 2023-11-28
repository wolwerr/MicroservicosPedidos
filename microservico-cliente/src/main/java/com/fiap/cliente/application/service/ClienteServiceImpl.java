package com.fiap.cliente.application.service;

import com.fiap.cliente.application.port.ClienteServicePort;
import com.fiap.cliente.domain.Cliente;
import com.fiap.cliente.infrastructure.config.ClienteKafkaProducer;
import com.fiap.cliente.infrastructure.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.fiap.cliente.infrastructure.config.JsonUtil.converteClienteParaJson;

@Service
public class ClienteServiceImpl implements ClienteServicePort {

    private final ClienteRepository clienteRepository;
    private final ClienteKafkaProducer clienteKafkaProducer;


    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteKafkaProducer clienteKafkaProducer) {
        this.clienteRepository = clienteRepository;
        this.clienteKafkaProducer = clienteKafkaProducer;
    }

    @Override
    public Cliente criarCliente(Cliente cliente) {
        Cliente novoCliente = clienteRepository.save(cliente);
        ClienteKafkaProducer producer = new ClienteKafkaProducer("localhost:9092", "clientePedidoTopic");
        try {
            producer.enviarMensagem(cliente.getId().toString(), "Informações do Cliente");
        } finally {
            producer.close();
        }
        return novoCliente;
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long id) {
        // Retorna o cliente ou Optional.empty() se não encontrado
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Override
    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado")); // Substitua por uma exceção mais específica
    }

    @Override
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
