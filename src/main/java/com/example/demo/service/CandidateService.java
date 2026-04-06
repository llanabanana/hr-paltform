package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.SkillRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    public CandidateService(CandidateRepository candidateRepository, SkillRepository skillRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Transactional(readOnly = true)
    public Candidate getById(Long id) {
        return findCandidateOrThrow(id);
    }

    @Transactional(readOnly = true)
    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        findCandidateOrThrow(id);
        candidateRepository.deleteById(id);
    }

    @Transactional
    public Candidate addSkill(Long candidateId, Long skillId) {
        Candidate candidate = findCandidateOrThrow(candidateId);
        Skill skill = findSkillOrThrow(skillId);

        candidate.getSkills().add(skill);
        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate removeSkill(Long candidateId, Long skillId) {
        Candidate candidate = findCandidateOrThrow(candidateId);
        Skill skill = findSkillOrThrow(skillId);

        candidate.getSkills().remove(skill);
        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate update(Long id, Candidate updates) {
        Candidate candidate = findCandidateOrThrow(id);
        if (updates.getFullName() != null) {
            candidate.setFullName(updates.getFullName());
        }
        if (updates.getDateOfBirth() != null) {
            candidate.setDateOfBirth(updates.getDateOfBirth());
        }
        if (updates.getContactNumber() != null) {
            candidate.setContactNumber(updates.getContactNumber());
        }
        if (updates.getEmail() != null) {
            candidate.setEmail(updates.getEmail());
        }
        return candidate;
    }

    private Candidate findCandidateOrThrow(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found: " + id));
    }

    private Skill findSkillOrThrow(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found: " + id));
    }

}
