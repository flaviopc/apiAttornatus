package com.attornatus.proj.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.attornatus.proj.api.converter.PessoaConverter;
import com.attornatus.proj.api.dto.EnderecoDto;
import com.attornatus.proj.api.dto.PessoaDto;
import com.attornatus.proj.domain.model.Endereco;
import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.service.EnderecoService;
import com.attornatus.proj.domain.service.PessoaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private PessoaService pessoaService;
    private EnderecoService enderecoService;
    private PessoaConverter pessoaConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaDto salvar(@RequestBody Pessoa pessoa) {
        return pessoaService.salvar(pessoa);
    }

    @PostMapping("/{id}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoDto salvarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Pessoa pessoa = pessoaService.buscar(id);
        endereco.setPessoa(pessoa);
        EnderecoDto enderecoDto = enderecoService.salvar(endereco);
        return enderecoDto;
    }

    @PutMapping("/{id}")
    public PessoaDto alterar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return pessoaService.atualizar(id, pessoa);
    }

    @PutMapping("/{pessoaId}/enderecos/{enderecoId}/principal")
    public PessoaDto alterarEnderecoPrincipal(@PathVariable Long pessoaId, @PathVariable Long enderecoId) {
        return pessoaService.definirEnderecoPrincipal(pessoaId, enderecoId);
    }

    @GetMapping()
    public List<PessoaDto> exibirTodos() {
        return pessoaService.exibirTodas();
    }

    @GetMapping("/{id}")
    public PessoaDto buscar(@PathVariable Long id) {
        return pessoaConverter.toDTO(pessoaService.buscar(id));
    }
}
