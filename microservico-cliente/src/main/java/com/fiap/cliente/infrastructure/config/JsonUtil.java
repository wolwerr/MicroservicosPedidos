package com.fiap.cliente.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.cliente.domain.Cliente;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String converteClienteParaJson(Cliente cliente) {
        try {
            return objectMapper.writeValueAsString(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter cliente para JSON", e);
        }
    }
}