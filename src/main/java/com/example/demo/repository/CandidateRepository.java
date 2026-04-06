package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Override
    @EntityGraph(attributePaths = "skills")
    Optional<Candidate> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "skills")
    List<Candidate> findAll();

}
