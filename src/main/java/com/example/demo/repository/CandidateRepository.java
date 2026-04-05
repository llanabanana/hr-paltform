package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

}
