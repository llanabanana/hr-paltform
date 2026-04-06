package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void shouldSaveCandidateToRepository() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate result = candidateService.save(candidate);

        assertEquals(candidate, result);
        verify(candidateRepository).save(candidate);
    }

    @Test
    void getByIdReturnsCandidateWhenFound() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        Candidate result = candidateService.getById(1L);

        assertEquals(candidate, result);
        verify(candidateRepository).findById(1L);
    }

    @Test
    void getByIdThrowsNotFoundWhenMissing() {
        when(candidateRepository.findById(42L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> candidateService.getById(42L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidate not found: 42", exception.getReason());
    }

    @Test
    void getAllReturnsAllCandidates() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.findAll()).thenReturn(List.of(candidate));

        List<Candidate> result = candidateService.getAll();

        assertEquals(1, result.size());
        assertEquals(candidate, result.get(0));
    }

    @Test
    void deleteRemovesExistingCandidate() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        candidateService.delete(1L);

        verify(candidateRepository).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenCandidateMissing() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> candidateService.delete(1L));

        verify(candidateRepository, never()).deleteById(any());
    }

    @Test
    void addSkillAddsSkillToCandidate() {
        Candidate candidate = candidate("Jane Doe");
        Skill skill = skill("Java");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate result = candidateService.addSkill(1L, 10L);

        assertEquals(1, result.getSkills().size());
        assertEquals(skill, result.getSkills().iterator().next());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void addSkillThrowsWhenCandidateMissing() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> candidateService.addSkill(1L, 10L));

        verify(skillRepository, never()).findById(any());
        verify(candidateRepository, never()).save(any());
    }

    @Test
    void addSkillThrowsWhenSkillMissing() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(skillRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> candidateService.addSkill(1L, 10L));

        verify(candidateRepository, never()).save(any());
    }

    @Test
    void removeSkillRemovesSkillFromCandidate() {
        Candidate candidate = candidate("Jane Doe");
        Skill skill = skill("Java");
        candidate.getSkills().add(skill);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skill));
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate result = candidateService.removeSkill(1L, 10L);

        assertEquals(0, result.getSkills().size());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void removeSkillThrowsWhenCandidateMissing() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> candidateService.removeSkill(1L, 10L));

        verify(skillRepository, never()).findById(any());
        verify(candidateRepository, never()).save(any());
    }

    @Test
    void removeSkillThrowsWhenSkillMissing() {
        Candidate candidate = candidate("Jane Doe");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(skillRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> candidateService.removeSkill(1L, 10L));

        verify(candidateRepository, never()).save(any());
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