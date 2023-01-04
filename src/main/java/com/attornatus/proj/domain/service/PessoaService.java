package com.attornatus.proj.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attornatus.proj.api.converter.PessoaConverter;
import com.attornatus.proj.api.dto.PessoaDto;
import com.attornatus.proj.domain.exception.EnderecoNaoEncontradoException;
import com.attornatus.proj.domain.exception.PessoaNaoEncontradaException;
import com.attornatus.proj.domain.model.Endereco;
import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.repository.EnderecoRepository;
import com.attornatus.proj.domain.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository repository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaConverter pessoaConverter;

    public List<PessoaDto> exibirTodas() {
        return pessoaConverter.toListDTO(repository.findAll());
    }

    public PessoaDto salvar(Pessoa pessoa) {
        return pessoaConverter.toDTO(repository.save(pessoa));
    }

    public Pessoa buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(
                        String.format("Pessoa de código %d não existe", id)));
    }

    public PessoaDto atualizar(Long id, Pessoa pessoaNova) {
        Pessoa pessoaSalva = buscar(id);
        pessoaSalva.setNascimento(pessoaNova.getNascimento());
        pessoaSalva.setNome(pessoaNova.getNome());
        return pessoaConverter.toDTO(repository.save(pessoaSalva));
    }

    public PessoaDto definirEnderecoPrincipal(Long pessoaId, Long enderecoId) {
        Pessoa pessoa = buscar(pessoaId);
        List<Endereco> enderecos = pessoa.getEnderecos();

        enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(
                        String.format("O Endereço de código %d não existe", enderecoId)));

        removeEnderecoPrincipal(enderecos);

        salvaEnderecoPrincipal(enderecoId, enderecos);

        return pessoaConverter.toDTO(pessoa);
    }

    private void salvaEnderecoPrincipal(Long enderecoId, List<Endereco> enderecos) {
        Optional<Endereco> enderecoPrincipal = enderecos.stream()
                .filter(endereco -> endereco.getId() == enderecoId).findFirst();

        if (enderecoPrincipal.isPresent()) {
            enderecoPrincipal.get().setPrincipal(true);
            enderecoRepository.save(enderecoPrincipal.get());
        }
    }

    private void removeEnderecoPrincipal(List<Endereco> enderecos) {
        Optional<Endereco> enderecoPrincipal = enderecos.stream()
                .filter(endereco -> endereco.isPrincipal()).findFirst();

        if (enderecoPrincipal.isPresent()) {
            enderecoPrincipal.get().setPrincipal(false);
            enderecoRepository.save(enderecoPrincipal.get());
        }
    }
}
