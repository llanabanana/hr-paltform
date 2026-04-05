package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.SkillRepository;

public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    public CandidateService(CandidateRepository candidateRepository, SkillRepository skillRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate getById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    public void delete(Long id) {
        candidateRepository.deleteById(id);
    }

    public Candidate addSkill(Long candidateId, Long skillId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            return null;
        }
        var skill = skillRepository.findById(skillId).orElse(null);
        if (skill == null) {
            return null;
        }

        candidate.getSkills().add(skill);
        return candidateRepository.save(candidate);
    }

    public Candidate removeSkill(Long candidateId, Long skillId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            return null;
        }
        var skill = skillRepository.findById(skillId).orElse(null);
        if (skill == null) {
            return null;
        }
        candidate.getSkills().remove(skill);
        return candidateRepository.save(candidate);
    }

}
