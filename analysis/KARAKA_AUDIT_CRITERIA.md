# Kāraka audit completion criteria

This defines the completion contract for the active audit. The full inventory
remains 1.4.23–55 and interacting canonical rules in 2.3. The coverage matrix in
KARAKA_SUTRA_AUDIT.md retains its existing evidence and open obligations.

Assess each rule separately on three axes:

1. **Rule correctness:** verify authoritative meaning, prerequisites, exceptions,
   optionality, precedence, anuvṛtti/adhikāra and exact evidence chains. Resolve
   contradictions and implementation defects. Record justified exclusions.
2. **Analyzer integration:** identify supported AST constructions and test rule
   applicability, missing evidence, competing readings, relevant voice/causative
   forms, occurrence identity and case realization. Preserve unresolved readings
   rather than manufacture a designation or proof.
3. **Meaning inference:** explicitly classify each prerequisite as automatically
   inferred, supplied, or unsupported. Supplied-premise benchmarks establish
   conditional rule application; they do not establish automatic understanding.

An inference limitation alone does not prove that rule logic is incorrect or
that integration is unfinished. Conversely, green tests do not establish a rule
as complete without evidence covering its applicable obligations. Every rule
needs an evidence-backed disposition and concrete remaining requirements.

Review PVM sources against intended meaning and validate affected programs
through analysis, interpretation and compilation. Preserve JSON expectations as
grammatical judgments. Add regressions to close identified obligations, not to
increase counts without additional evidence.

Backward compatibility with legacy implementations is not required. Prefer
architecture following Sanskrit grammar and correct analysis/execution. Replace
or delete obsolete code, aliases, fallback paths and tests preserving incorrect
behavior. Migrate callers, PVM sources and documentation; verify replacements
end to end and preserve functionality that remains valid.

Completion requires an evidence-backed disposition for every rule, resolved
correctness/integration obligations within explicitly documented supported
constructions, precisely recorded inference limitations, reviewed PVM validation
and a passing full multi-module suite. Do not narrow supported scope simply to
declare success. Keep the goal active until the completion evidence is proven.
