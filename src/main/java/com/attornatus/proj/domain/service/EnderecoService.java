package com.attornatus.proj.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.proj.api.converter.EnderecoConverter;
import com.attornatus.proj.api.dto.EnderecoDto;
import com.attornatus.proj.domain.model.Endereco;
import com.attornatus.proj.domain.repository.EnderecoRepository;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private EnderecoConverter enderecoConverter;

    public EnderecoDto salvar(Endereco endereco) {
        endereco.setPrincipal(false);
        return enderecoConverter.toDTO(enderecoRepository.save(endereco));
    }
}
