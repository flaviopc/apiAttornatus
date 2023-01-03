package com.attornatus.proj.api.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.attornatus.proj.api.dto.EnderecoDto;
import com.attornatus.proj.domain.model.Endereco;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class EnderecoConverter {
    private ModelMapper modelMapper;

    public EnderecoDto toDTO(Endereco endereco) {
        return modelMapper.map(endereco, EnderecoDto.class);
    }

    public List<EnderecoDto> toListDTO(List<Endereco> enderecos) {
        return enderecos.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
