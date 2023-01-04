package com.attornatus.proj.api.controller;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.repository.PessoaRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PessoaControllerTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/pessoas";

        prepararDados();
    }

    private void prepararDados() {
        var pessoa = new Pessoa();
        pessoa.setNome("Marcos");
        pessoa.setNascimento(LocalDate.of(2000, 9, 4));
        pessoa = pessoaRepository.save(pessoa);

        var pessoa2 = new Pessoa();
        pessoa2.setNome("Roger");
        pessoa2.setNascimento(LocalDate.of(1990, 10, 30));
        pessoa2 = pessoaRepository.save(pessoa2);
    }

    @Test
    public void deveRetornarStatus200E2Pessoas_QuandoConsultarPessoas() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", Matchers.hasSize(2));
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarPessoaExistente() {
        RestAssured.given()
                .pathParam("id", 1)
                .accept(ContentType.JSON)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo("Marcos"));
    }

    @Test
    public void deveRetornar200EDadosAlterados_QuandoAlterarPessoa() {
        String json = "{" +
                "\"nome\":\"flavio\"," +
                "\"nascimento\":\"1992-01-11\"" +
                "}";
        RestAssured.given()
                .pathParam("id", 1)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo("flavio"),
                        "nascimento", Matchers.equalTo("1992-01-11"));
    }

    @Test
    public void deveRetornar404_QuandoTentarAlterarPessoaInexistente() {
        String json = "{" +
                "\"nome\":\"flavio\"," +
                "\"nascimento\":\"1992-01-11\"" +
                "}";
        RestAssured.given()
                .pathParam("id", 100)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
