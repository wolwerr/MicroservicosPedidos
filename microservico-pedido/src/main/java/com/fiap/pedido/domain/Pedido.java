package com.fiap.pedido.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private Date dataCriacao;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    private String itens;
    private double valorTotal;
}
