package com.attornatus.proj.api.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.attornatus.proj.api.dto.PessoaDto;
import com.attornatus.proj.domain.model.Pessoa;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PessoaConverter {
    private ModelMapper modelMapper;

    public PessoaDto toDTO(Pessoa pessoa) {
        return modelMapper.map(pessoa, PessoaDto.class);
    }

    public List<PessoaDto> toListDTO(List<Pessoa> pessoas) {
        return pessoas.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
