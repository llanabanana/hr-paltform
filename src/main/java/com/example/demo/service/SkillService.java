package com.example.demo.service;

import com.example.demo.model.Skill;
import com.example.demo.repository.SkillRepository;

public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill getById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    public java.util.List<Skill> getAll() {
        return skillRepository.findAll();
    }

    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

}
