package com.example.demo.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import com.example.demo.dto.CandidateRequest;
import com.example.demo.dto.CandidateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse createCandidate(@Valid @RequestBody CandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setFullName(request.getFullName());
        candidate.setDateOfBirth(request.getDateOfBirth());
        candidate.setContactNumber(request.getContactNumber());
        candidate.setEmail(request.getEmail());

        return toResponse(candidateService.save(candidate));
    }

    @GetMapping("/{id}")
    public CandidateResponse getCandidateById(@PathVariable Long id) {
        return toResponse(candidateService.getById(id));
    }

    @GetMapping
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getAll().stream().map(this::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.delete(id);
    }

    @PostMapping("/{candidateId}/skills/{skillId}")
    public CandidateResponse addSkillToCandidate(@PathVariable Long candidateId, @PathVariable Long skillId) {
        return toResponse(candidateService.addSkill(candidateId, skillId));
    }

    @DeleteMapping("/{candidateId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSkillFromCandidate(@PathVariable Long candidateId, @PathVariable Long skillId) {
        candidateService.removeSkill(candidateId, skillId);
    }

    @PutMapping("/{id}")
    public CandidateResponse updateCandidate(@PathVariable Long id, @Valid @RequestBody CandidateRequest request) {
        Candidate updates = new Candidate();
        updates.setFullName(request.getFullName());
        updates.setDateOfBirth(request.getDateOfBirth());
        updates.setContactNumber(request.getContactNumber());
        updates.setEmail(request.getEmail());
        return toResponse(candidateService.update(id, updates));
    }

    // Helper to map Candidate to CandidateResponse
    private CandidateResponse toResponse(Candidate candidate) {
        CandidateResponse response = new CandidateResponse();
        response.setId(candidate.getId());
        response.setFullName(candidate.getFullName());
        response.setDateOfBirth(candidate.getDateOfBirth());
        response.setContactNumber(candidate.getContactNumber());
        response.setEmail(candidate.getEmail());

        Set<Long> skillIds = candidate.getSkills().stream()
                .map(skill -> skill.getId())
                .collect(Collectors.toSet());
        response.setSkillIds(skillIds);

        return response;
    }

}
