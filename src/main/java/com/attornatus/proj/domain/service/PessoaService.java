package com.attornatus.proj.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.proj.api.converter.PessoaConverter;
import com.attornatus.proj.api.dto.PessoaDto;
import com.attornatus.proj.domain.exception.PessoaNaoEncontradaException;
import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository repository;

    @Autowired
    private PessoaConverter pessoaConverter;

    public List<PessoaDto> exibirTodas() {
        return pessoaConverter.toListDTO(repository.findAll());
    }

    public PessoaDto salvar(Pessoa pessoa) {
        return pessoaConverter.toDTO(repository.save(pessoa));
    }

    public Pessoa buscar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new PessoaNaoEncontradaException(String.format("Pessoa de código %d não existe", id)));
    }

    public PessoaDto atualizar(Long id, Pessoa pessoaNova) {
        Pessoa pessoaSalva = buscar(id);
        pessoaSalva.setNascimento(pessoaNova.getNascimento());
        pessoaSalva.setNome(pessoaNova.getNome());
        return pessoaConverter.toDTO(repository.save(pessoaSalva));
    }
}
