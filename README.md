# DecisionEngine — Inbank

## Overview
A decision engine that determines the maximum approvable loan amount for a given person, built with a Java Spring Boot backend and a plain HTML/JavaScript frontend.

## How to Run
1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run `LoanApplication.java`
4. Open `http://localhost:8080` in your browser
5. Can run the test cases in `DecisionEngineTest`

---

## Design Choices

### Backend — Java with Spring Boot
Java was chosen as the primary language as for backend. Spring Boot was selected over plain Java Servlets because it eliminates boilerplate code around HTTP request handling.

In traditional Java Servlets, handling an HTTP request requires manually extending `HttpServlet`, parsing the request body, and writing the response manually. Spring Boot abstracts all of this through annotations:
- `@RestController` — marks a class as an HTTP request handler and automatically converts return values to JSON
- `@PostMapping` — maps a method to a specific HTTP endpoint
- `@RequestBody` — automatically deserializes incoming JSON into a Java object using the Jackson library 

This abstaction prevents overhead for such a small design.

### Single API Endpoint
The assignment requires a single endpoint:
```
POST /loan/decision
```
which accepts a JSON body with `personalCode`, `loanAmount`, and `loanPeriod`, and returns a decision and approved amount.

POST was chosen over GET because the request sends data for processing rather than fetching existing data, and POST allows a structured JSON body rather than passing values through the URL.

### Data Transfer Objects (DTOs)
Two simple classes were created to represent incoming and outgoing data:
- `LoanRequest` — maps incoming JSON fields to Java fields. Requires setters because Spring fills it in automatically by matching JSON field names to setter names.
- `LoanDecision` — represents the response. Uses a constructor because it is created manually in the business logic and only needs to be read by Spring when converting to JSON.

---

### Decision Engine Logic
For code clarity represent the possible outcomes of a loan request (POSITIVE or NEGATIVE) using an enum and all fixed values as constants.

The credit score formula provided is:
```
credit_score = (credit_modifier / loan_amount) * loan_period
```

A loan is approved when `credit_score >= 1`. Rather than plugging in the requested loan amount and checking whether it passes, the engine **solves the inequality directly for the maximum approvable amount**:

```
(credit_modifier / loan_amount) * loan_period >= 1
loan_amount <= credit_modifier * loan_period
```

So the maximum approvable amount is always:
```
max_amount = credit_modifier * loan_period
```

This value is then clamped to the allowed range of 2000–10000€. The requested loan amount is intentionally not used in this calculation — the assignment states the engine should always return the maximum approvable sum, so computing it directly is more efficient.

If the calculated maximum falls below 2000€ (meaning the requested period is too short for any valid loan to be issued), the engine **increments the loan period by one month at a time** up to the 60-month cap and recalculates, looking for the earliest period where a valid loan becomes possible.

If no valid amount exists within any allowed period, a **negative decision** is returned.

The credit modifier is hardcoded per personal code as a mock for what would in production be an external registry lookup:

| Personal Code | Status    | Credit Modifier |
|---------------|-----------|-----------------|
| 49002010965   | Debt      | —               |
| 49002010976   | Segment 1 | 100             |
| 49002010987   | Segment 2 | 300             |
| 49002010998   | Segment 3 | 1000            |

If a person has debt, no loan amount is approved regardless of the period.

### Input Validation
The engine validates inputs before processing:
- Loan amount must be between 2000€ and 10000€
- Loan period must be between 12 and 60 months
- Personal code has to exist in "database".

Invalid inputs return a `NEGATIVE` decision immediately without running the algorithm.

### Frontend — Plain HTML/CSS/JavaScript
A plain HTML page was chosen over React or TypeScript to keep the project simple and focused on the backend logic. JavaScript's `fetch` API sends a POST request to the backend and displays the result. No framework or build tool is needed because Spring Boot serves the `index.html` file automatically from the `src/main/resources/static/` directory.

---

## What I Would Improve
Firstly, The requested loan amount is  redundant in current algorithm because the engine ignores it and always calculates the maximum approvable amount anyway. I would redesign the algorithm to make the requested amount meaningful, for example by first checking whether the exact requested amount can be approved, and only searching for alternatives if it cannot. Secondly, the period for the load could be modified if the loan amount is too small, however the client will never know that because we do not return the period. These both changes would make the engine more realistic and more purposeful.
