package com.attornatus.proj.api.controller;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.attornatus.proj.domain.model.Endereco;
import com.attornatus.proj.domain.model.Pessoa;
import com.attornatus.proj.domain.repository.EnderecoRepository;
import com.attornatus.proj.domain.repository.PessoaRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PessoaControllerTest {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

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
        cadastrarPessoas();
        cadastrarEnderecos();
    }

    private void cadastrarPessoas() {
        var pessoa = new Pessoa();
        pessoa.setNome("Marcos");
        pessoa.setNascimento(LocalDate.of(2000, 9, 4));
        pessoa = pessoaRepository.save(pessoa);

        var pessoa2 = new Pessoa();
        pessoa2.setNome("Roger");
        pessoa2.setNascimento(LocalDate.of(1990, 10, 30));
        pessoa2 = pessoaRepository.save(pessoa2);
    }

    private void cadastrarEnderecos() {
        var pessoa = pessoaRepository.findById(1L).get();
        var endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        endereco.setCep("384875559");
        endereco.setNumero("10");
        endereco.setCidade("santos");
        endereco.setPrincipal(false);
        endereco.setPessoa(pessoa);
        endereco = enderecoRepository.save(endereco);

        var endereco2 = new Endereco();
        endereco2.setCep("384875559");
        endereco2.setLogradouro("Rua B");
        endereco2.setNumero("15");
        endereco2.setCidade("santos");
        endereco2.setPrincipal(false);
        endereco.setPessoa(pessoa);
        endereco2 = enderecoRepository.save(endereco2);
    }

    public String enderecoJSON() {
        return "{" +
                "\"logradouro\":\"avenida santos dumont\"," +
                "\"cep\":\"38495064\"," +
                "\"numero\":\"374\"," +
                "\"cidade\":\"rio branco\"" +
                "}";
    }

    public String PessoaJSON() {
        return "{" +
                "\"nome\":\"flavio\"," +
                "\"nascimento\":\"1992-01-11\"" +
                "}";
    }

    @Test
    public void deveRetornarStatus200E2Pessoas_QuandoConsultarPessoas() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.hasSize(2));
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarPessoaExistente() {
        RestAssured.given()
                .pathParam("id", 2)
                .accept(ContentType.JSON)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo("Roger"));
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
        RestAssured.given()
                .pathParam("id", 100)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(PessoaJSON())
                .when()
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deveRetornar404_QuandoTentarCadastrarEnderecoParaPessoaInexistente() {
        RestAssured.given()
                .pathParam("id", 100)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(enderecoJSON())
                .when()
                .post("/{id}/enderecos")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deveRetornar201EEndereco_QuandoCadastrarEnderecoParaPessoa() {
        RestAssured.given()
                .pathParam("id", 1)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(enderecoJSON())
                .when()
                .post("/{id}/enderecos")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.containsString(enderecoJSON()));
    }

    @Test
    public void deveRetornar200_QuandoDefinirEnderecoComoPrincipal() {
        RestAssured.given()
                .pathParam("id", 1)
                .pathParam("enderecoId", 1)
                .accept(ContentType.JSON)
                .when()
                .put("/{id}/enderecos/{enderecoId}/principal")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("enderecos[0].principal", Matchers.equalTo(true));
    }

    @Test
    public void deveRetornar404_QuandoTentarDefinirEnderecoInexistenteComoPrincipal() {
        RestAssured.given()
                .pathParam("id", 1)
                .pathParam("enderecoId", 100)
                .accept(ContentType.JSON)
                .when()
                .put("/{id}/enderecos/{enderecoId}/principal")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

}
