package com.attornatus.proj.api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDto {
    private String nome;
    private LocalDate nascimento;
    private List<EnderecoDto> enderecos;
}
