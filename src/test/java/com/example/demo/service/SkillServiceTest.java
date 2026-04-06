package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Skill;
import com.example.demo.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void shouldSaveSkillToRepository() {
        Skill skill = skill("Java");
        when(skillRepository.save(skill)).thenReturn(skill);

        Skill result = skillService.save(skill);

        assertEquals(skill, result);
        verify(skillRepository).save(skill);
    }

    @Test
    void getByIdReturnsSkillWhenFound() {
        Skill skill = skill("Java");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        Skill result = skillService.getById(1L);

        assertEquals(skill, result);
        verify(skillRepository).findById(1L);
    }

    @Test
    void getByIdThrowsNotFoundWhenMissing() {
        when(skillRepository.findById(42L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> skillService.getById(42L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Skill not found: 42", exception.getReason());
    }

    @Test
    void getAllReturnsAllSkills() {
        Skill skill = skill("Java");
        when(skillRepository.findAll()).thenReturn(List.of(skill));

        List<Skill> result = skillService.getAll();

        assertEquals(1, result.size());
        assertEquals(skill, result.get(0));
    }

    @Test
    void deleteRemovesExistingSkill() {
        Skill skill = skill("Java");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        skillService.delete(1L);

        verify(skillRepository).deleteById(1L);
    }

    @Test
    void updateSkillUpdatesExistingSkill() {
        Skill existing = skill("Java");
        Skill updates = skill("Python");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(existing));

        Skill result = skillService.update(1L, updates);

        assertEquals("Python", result.getName());
    }

    @Test
    void updateSkillThrowsWhenMissing() {
        Skill updates = skill("Python");
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> skillService.update(1L, updates));
    }

    private Skill skill(String name) {
        Skill skill = new Skill();
        skill.setName(name);
        return skill;
    }
}