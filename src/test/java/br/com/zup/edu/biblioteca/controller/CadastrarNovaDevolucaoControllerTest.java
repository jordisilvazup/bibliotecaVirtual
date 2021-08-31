package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroDevolucaoLivroRequest;
import br.com.zup.edu.biblioteca.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CadastrarNovaDevolucaoControllerTest {
    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private Livro novoLivro;

    private Exemplar exemplar;

    private Usuario usuario;

    private EmprestimoDeExemplar emprestimoDeExemplar;


    @Transactional
    @Test
    void deveCadastrarUmaDevolucao() throws Exception {
        this.novoLivro= new Livro("Micro servico",new BigDecimal("25"),"978-6555871906");
        manager.persist(novoLivro);
        this.exemplar=new Exemplar(TipoCirculacao.LIVRE,novoLivro);
        manager.persist(exemplar);
        this.usuario= new Usuario("Jords", TipoUsuario.PADRAO);
        manager.persist(usuario);
        this.emprestimoDeExemplar=new EmprestimoDeExemplar(exemplar,usuario,45);
        manager.persist(emprestimoDeExemplar);

        CadastroDevolucaoLivroRequest devolucaoLivroRequest= new CadastroDevolucaoLivroRequest(this.usuario.getId(),this.emprestimoDeExemplar.getId());

        String request= mapper.writeValueAsString(devolucaoLivroRequest);

        mockMvc.perform(
                post("/api/v1/devolucoes")
                        .contentType(APPLICATION_JSON)
                        .content(request)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                redirectedUrlPattern("/api/v1/devolucoes/*")
        );

    }
}