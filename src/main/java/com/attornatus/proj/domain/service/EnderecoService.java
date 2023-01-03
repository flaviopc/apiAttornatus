package com.attornatus.proj.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.proj.domain.model.Endereco;
import com.attornatus.proj.domain.repository.EnderecoRepository;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository repository;

    public Endereco salvar(Endereco endereco) {
        return repository.save(endereco);
    }
}
