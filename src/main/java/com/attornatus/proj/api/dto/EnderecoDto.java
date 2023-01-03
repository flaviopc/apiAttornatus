package com.attornatus.proj.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoDto {
    private String logradouro;
    private String cep;
    private String numero;
    private String cidade;
    private PessoaDto pessoa;
}
