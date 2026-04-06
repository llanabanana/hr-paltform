_**HR Platform**_
A small Spring Boot REST API built as part of an internship task.
It manages Candidates and their Skills, exposing CRUD endpoints backed by JPA repositories.

The project follows a layered architecture with DTOs, a service layer, and centralized exception handling.

**What I Found Most Interesting**
The most interesting part was designing the relationship between Candidate and Skill, and then deciding how that relationship should be exposed through DTOs.
It pushed me to think about the difference between how data is stored and how data is exposed — the entities live in the database with their JPA relationships, but the API speaks in CandidateRequest / CandidateResponse and SkillRequest / SkillResponse.

Introducing a service layer, instead of letting controllers talk directly to repositories, made the application feel structured and intentional.

The GlobalExceptionHandler adds polish by turning raw exceptions into predictable API responses, making the system feel production-ready.

**What I Found Hardest**
The most challenging part was writing tests for the REST API.

Challenges included:
Using MockMvc to simulate HTTP requests
Asserting JSON responses correctly, including status codes and content types
I had to think carefully about what each test was actually supposed to prove — whether I was testing the controller, the service, or the integration between them — and avoid the trap of writing tests that just re-asserted what the mocks were already returning
