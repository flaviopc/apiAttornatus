package com.attornatus.proj.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.proj.domain.exception.PessoaNaoEncontradaException;
import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository repository;

    public Pessoa salvar(Pessoa pessoa) {
        return repository.save(pessoa);
    }

    public Pessoa buscar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new PessoaNaoEncontradaException(String.format("Pessoa de código %d não existe", id)));
    }

    public Pessoa atualizar(Long id, Pessoa pessoaNova) {
        Pessoa pessoaSalva = buscar(id);
        pessoaSalva.setNascimento(pessoaNova.getNascimento());
        pessoaSalva.setNome(pessoaNova.getNome());
        return repository.save(pessoaSalva);
    }
}
