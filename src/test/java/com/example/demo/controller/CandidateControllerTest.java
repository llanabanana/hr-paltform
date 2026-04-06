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

import java.time.LocalDate;
import java.util.HashSet;
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
import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.service.CandidateService;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(candidateController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createCandidateReturnsCreatedCandidate() throws Exception {
        Candidate candidate = candidate("Jane Doe");
        when(candidateService.save(any(Candidate.class))).thenReturn(candidate);

        mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fullName": "Jane Doe",
                          "dateOfBirth": "1995-01-01",
                          "contactNumber": "123456789",
                          "email": "jane.doe@example.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));

        verify(candidateService).save(any(Candidate.class));
    }

    @Test
    void createCandidateReturnsBadRequestForInvalidBody() throws Exception {
        mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fullName": "",
                          "dateOfBirth": "1995-01-01",
                          "contactNumber": "123456789",
                          "email": "jane.doe@example.com"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Full name cannot be blank"));
    }

    @Test
    void getCandidateByIdReturnsCandidate() throws Exception {
        Candidate candidate = candidate("Jane Doe");
        when(candidateService.getById(1L)).thenReturn(candidate);

        mockMvc.perform(get("/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.skillIds").isEmpty());
    }

    @Test
    void getAllCandidatesReturnsList() throws Exception {
        Candidate candidate = candidate("Jane Doe");
        when(candidateService.getAll()).thenReturn(List.of(candidate));

        mockMvc.perform(get("/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Jane Doe"));
    }

    @Test
    void deleteCandidateReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/candidates/1"))
                .andExpect(status().isNoContent());

        verify(candidateService).delete(1L);
    }

    @Test
    void addSkillToCandidateReturnsUpdatedCandidate() throws Exception {
        Candidate candidate = candidate("Jane Doe");
        Skill skill = skill("Java");
        candidate.getSkills().add(skill);
        when(candidateService.addSkill(1L, 10L)).thenReturn(candidate);

        mockMvc.perform(post("/candidates/1/skills/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void removeSkillFromCandidateReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/candidates/1/skills/10"))
                .andExpect(status().isNoContent());

        verify(candidateService).removeSkill(1L, 10L);
    }

    private Candidate candidate(String fullName) {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setDateOfBirth(LocalDate.of(1995, 1, 1));
        candidate.setContactNumber("123456789");
        candidate.setEmail("jane.doe@example.com");
        candidate.setSkills(new HashSet<>());
        return candidate;
    }

    private Skill skill(String name) {
        Skill skill = new Skill();
        skill.setName(name);
        return skill;
    }

}