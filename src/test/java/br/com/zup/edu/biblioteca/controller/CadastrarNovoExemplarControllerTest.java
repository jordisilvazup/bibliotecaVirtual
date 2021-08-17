package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroExemplarRequest;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
class CadastrarNovoExemplarControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LivroRepository repository;

    @Test
    void deveCadastrarUmExemplarCasoExistaOCadastroDoLivro() throws Exception {
        final Livro dddDaMassa = new Livro("DDD da massa", new BigDecimal("230.0"), "978-8550800653");
        repository.save(dddDaMassa);
        CadastroExemplarRequest exemplarRequest=new CadastroExemplarRequest("LIVRE");
        final String request = mapper.writeValueAsString(exemplarRequest);
        mockMvc.perform(
                post("/api/v1/livros/978-8550800653/exemplares")
                .contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                redirectedUrlPattern("/api/v1/livros/*/exemplares/*")
        );
    }

    @Test
    void naoDeveCadastrarUmExemplarCasoNaoExistaOCadastroDoLivro() throws Exception {
        CadastroExemplarRequest exemplarRequest=new CadastroExemplarRequest("LIVRE");
        final String request = mapper.writeValueAsString(exemplarRequest);
        mockMvc.perform(
                post("/api/v1/livros/978-8550800653/exemplares")
                        .contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(
                status().isNotFound()
        );
    }
}