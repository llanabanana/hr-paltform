package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.example.demo.dto.SkillRequest;
import com.example.demo.dto.SkillResponse;
import com.example.demo.service.SkillService;
import com.example.demo.model.Skill;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(@Valid @RequestBody SkillRequest request) {
        Skill skill = new Skill();
        skill.setName(request.getName());
        return toResponse(skillService.save(skill));
    }

    @GetMapping("/{id}")
    public SkillResponse getSkillById(@PathVariable Long id) {
        return toResponse(skillService.getById(id));
    }

    @GetMapping
    public List<SkillResponse> getAllSkills() {
        return skillService.getAll().stream().map(this::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
    }

    private SkillResponse toResponse(Skill skill) {
        SkillResponse response = new SkillResponse();
        response.setId(skill.getId());
        response.setName(skill.getName());
        return response;
    }
}
