package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Skill;
import com.example.demo.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Transactional
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Transactional(readOnly = true)
    public Skill getById(Long id) {
        return findSkillOrThrow(id);
    }

    @Transactional(readOnly = true)
    public List<Skill> getAll() {
        return skillRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        findSkillOrThrow(id);
        skillRepository.deleteById(id);
    }

    private Skill findSkillOrThrow(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found: " + id));
    }

}
