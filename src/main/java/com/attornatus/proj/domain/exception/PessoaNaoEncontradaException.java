package com.attornatus.proj.domain.exception;

public class PessoaNaoEncontradaException extends RuntimeException{
    public PessoaNaoEncontradaException(String msg){
        super(msg);
    }
}
