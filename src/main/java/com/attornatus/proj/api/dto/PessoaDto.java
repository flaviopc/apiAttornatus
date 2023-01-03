package com.attornatus.proj.api.dto;

import java.util.List;

import com.attornatus.proj.domain.model.Endereco;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDto {
    private String nome;
    private String dataNascimento;
    private List<Endereco> enderecos;
}
