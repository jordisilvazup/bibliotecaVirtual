package br.com.zup.edu.biblioteca.controller;

import br.com.zup.edu.biblioteca.controller.requests.CadastroUsuarioRequest;
import br.com.zup.edu.biblioteca.model.TipoUsuario;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class CadastrarNovoUsuarioControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarUmUsuario() throws Exception{
        CadastroUsuarioRequest usuarioRequest = new CadastroUsuarioRequest("Jordi H.", "PADRAO");
        String request = mapper.writeValueAsString(usuarioRequest);
        mockMvc.perform(
                post("/api/v1/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content(request)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                redirectedUrlPattern("/api/v1/usuarios/*")
        );
    }
}