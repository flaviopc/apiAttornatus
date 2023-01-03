package com.attornatus.proj.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attornatus.proj.domain.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
