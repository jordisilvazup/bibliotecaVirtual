package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroEmprestimoDeExemplarRequest;
import br.com.zup.edu.biblioteca.model.*;
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
import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
class CadastrarNovoEmprestimoControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager manager;

    @Test
    @Transactional
    void deveCadastrarUmEmprestimoParaUsuarioPadrao() throws Exception {
        Livro novoLivro= new Livro("Micro servico",new BigDecimal("25"),"978-6555871906");
        manager.persist(novoLivro);
        Exemplar exemplar=new Exemplar(TipoCirculacao.LIVRE,novoLivro);
        manager.persist(exemplar);
        Usuario usuario= new Usuario("Jords", TipoUsuario.PADRAO);
        manager.persist(usuario);

        CadastroEmprestimoDeExemplarRequest cadastroEmprestimoDeExemplarRequest = new CadastroEmprestimoDeExemplarRequest(30, novoLivro.getId(), usuario.getId());
        String request = mapper.writeValueAsString(cadastroEmprestimoDeExemplarRequest);

        mockMvc.perform(post("/api/v1/emprestimos")
                .contentType(APPLICATION_JSON)
                .content(request)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                redirectedUrlPattern("/api/v1/emprestimos/*")
        );

    }
    @Test
    @Transactional
    void deveCadastrarUmEmprestimoParaUsuarioPesquisador() throws Exception {
        Livro novoLivro= new Livro("Micro servico",new BigDecimal("25"),"978-8501110121");
        manager.persist(novoLivro);
        Exemplar exemplar=new Exemplar(TipoCirculacao.LIVRE,novoLivro);
        manager.persist(exemplar);
        Usuario usuario= new Usuario("Jords", TipoUsuario.PESQUISADOR);
        manager.persist(usuario);

        CadastroEmprestimoDeExemplarRequest cadastroEmprestimoDeExemplarRequest = new CadastroEmprestimoDeExemplarRequest(30, novoLivro.getId(), usuario.getId());
        String request = mapper.writeValueAsString(cadastroEmprestimoDeExemplarRequest);

        mockMvc.perform(post("/api/v1/emprestimos")
                .contentType(APPLICATION_JSON)
                .content(request)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                redirectedUrlPattern("/api/v1/emprestimos/*")
        );

    }
}