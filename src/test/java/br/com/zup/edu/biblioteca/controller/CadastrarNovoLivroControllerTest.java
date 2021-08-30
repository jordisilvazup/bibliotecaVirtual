package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroLivroRequest;
import br.com.zup.edu.biblioteca.model.Livro;
import br.com.zup.edu.biblioteca.util.ErrorMessage;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureDataJpa
@Transactional
class CadastrarNovoLivroControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager manager;

    @Test
    public void deveCadastrarUmLivroERetornar201EIdDoLivroNaLocation() throws Exception{
        CadastroLivroRequest livroRequest=new CadastroLivroRequest("TDD na Zup Edu",new BigDecimal("250.0"),"978-8550804606");
        String request=mapper.writeValueAsString(livroRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        ).andExpect(MockMvcResultMatchers.redirectedUrlPattern("/api/v1/livros/*"));
    }

    @Test
    public void naoDeveCadastrarUmLivroComISBNJaCadastrardoEDeveRetornar400EMensagemAmigavel() throws Exception{
        Livro antigo=new Livro("TDD na Zup Edu",new BigDecimal("250.0"),"978-8576082675");
        manager.persist(antigo);
        CadastroLivroRequest livroRequest=new CadastroLivroRequest("TDD na Zup Edu",new BigDecimal("250.0"),"978-8576082675");
        String request=mapper.writeValueAsString(livroRequest);
        ErrorMessage errorMessage =new ErrorMessage();
        errorMessage.adicionarError("isbn","Já esta cadastrado na base da dados");
        String response=mapper.writeValueAsString(errorMessage);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }


}