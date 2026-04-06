package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.model.Skill;
import com.example.demo.service.SkillService;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(skillController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createSkillReturnsCreatedSkill() throws Exception {
        Skill skill = skill("Java");
        when(skillService.save(any(Skill.class))).thenReturn(skill);

        mockMvc.perform(post("/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Java"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java"));

        verify(skillService).save(any(Skill.class));
    }

    @Test
    void createSkillReturnsBadRequestForInvalidBody() throws Exception {
        mockMvc.perform(post("/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                    .andExpect(content().string("Skill name is required"));
    }

    @Test
    void getSkillByIdReturnsSkill() throws Exception {
        Skill skill = skill("Java");
        when(skillService.getById(1L)).thenReturn(skill);

        mockMvc.perform(get("/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void getAllSkillsReturnsList() throws Exception {
        Skill skill = skill("Java");
        when(skillService.getAll()).thenReturn(List.of(skill));

        mockMvc.perform(get("/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test
    void deleteSkillReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/skills/1"))
                .andExpect(status().isNoContent());

        verify(skillService).delete(1L);
    }

    private Skill skill(String name) {
        Skill skill = new Skill();
        skill.setName(name);
        return skill;
    }

}