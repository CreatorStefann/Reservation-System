# Prompt To Give Next AI

Use this prompt in the new chat so the next AI can continue fast:

```text
Read these files first and use them as source of truth:
1) HANDOFF_NEXT_AI.md
2) AGENTS.md
3) README_LOCAL_PLAN.md

Then do this:
- Verify baseline with: ./mvnw test
- Summarize current state in 8-12 bullets
- Start implementing the next mandatory requirement: Spring Security

Constraints:
- Do NOT commit automatically; only provide commit messages.
- Keep tests green after each change.
- Update AGENTS.md and README_LOCAL_PLAN.md after each major step.
- Keep controller tests as @WebMvcTest + @MockitoBean style.
```

Optional quick command before opening new AI chat:

```zsh
cd "/path/to/Reservation-System"
./mvnw test
```

