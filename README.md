_**HR Platform
**_
A small Spring Boot REST API built as part of an internship task.
It manages Candidates and their Skills, exposing CRUD endpoints backed by JPA repositories.

The project follows a layered architecture with DTOs, a service layer, and centralized exception handling.

**What I Found Most Interesting
**
The most interesting part was designing the relationship between Candidate and Skill, and then deciding how that relationship should be exposed through DTOs.

Key points:

Entities represent how data is stored (JPA relationships, database structure)
DTOs represent how data is exposed (API contracts)

Separating these two layers kept the API clean and maintainable. Everything goes through:

CandidateRequest / CandidateResponse
SkillRequest / SkillResponse

Introducing a service layer, instead of letting controllers talk directly to repositories, made the application feel structured and intentional.

The GlobalExceptionHandler adds polish by turning raw exceptions into predictable API responses, making the system feel production-ready.

**What I Found Hardest
**
The most challenging part was writing tests for the REST API.

Challenges included:
Using MockMvc to simulate HTTP requests
Asserting JSON responses correctly, including status codes and content types

The real difficulty was deciding what each test should prove:

Testing the controller
Testing the service
Testing the integration

It was easy to write tests that simply re-asserted what mocks already returned.

This project taught me to think about proper test design, not just whether the code runs, which was the steepest but most valuable part of the task.
