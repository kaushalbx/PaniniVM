# PaniniVM next plans

Updated: 2026-10-26.

This document tracks the next language and semantic-analysis work. Completed
syntax belongs in [`pvm-language-guide.md`](pvm-language-guide.md); derivation
coverage belongs in [`rule-roadmap.md`](rule-roadmap.md); implementation design
belongs in [`panini-vm-architecture.md`](panini-vm-architecture.md). This file is
not a chronological work log.

## Direction

PaniniVM should interpret natural, segmented Sanskrit through explicit typed
meaning. Grammar, morphology, agreement, attachment, discourse, and control
relations must be represented before execution. The interpreter and compiler
must consume the same semantic result and produce the same diagnostics.

The project should not add a special action merely to solve one example. A new
construction must express a reusable grammatical relation and remain meaningful
outside the motivating program.

## P0: General attachment analysis

Replace remaining proximity and fixed-frame assumptions with typed attachment
relations.

- Model adjective–head agreement by case, number, gender, and semantic type.
- Model genitive whole/member, possessor/field, and modifier attachment.
- Represent ambiguity explicitly instead of selecting the nearest word.
- Preserve every participating pada and its source span.
- Reuse one analyzed relation in the interpreter, compiler, validator, and IDE.

Completion evidence:

- equivalent supported word orders produce the same typed relation;
- genuinely ambiguous phrases receive a stable source-located diagnostic;
- no consumer reconstructs attachment from rendered text or operand position;
- parser, semantic, negative, interpreter, and compiler-parity tests pass.

## P0: Discourse and anaphora

Generalize the existing procedure-local and iteration-local referent model.

- Support safe top-level discourse references across completed sentences.
- Define antecedent visibility, recency, agreement, and shadowing explicitly.
- Keep interrogative, relative, demonstrative, and quantifier senses distinct.
- Reject missing or ambiguous antecedents without lexical fallback.
- Preserve referent identity through assignment, branches, loops, and calls.

Completion evidence:

- anaphora resolves from typed discourse state rather than rendered history;
- interpreter and compiler select the same antecedent;
- scope exit and procedure boundaries cannot leak local referents;
- diagnostics identify both the unresolved reference and relevant candidates.

## P0: Remove semantic fallbacks

Eliminate compatibility behavior that can manufacture meaning after analysis.

- Replace remaining nullable recognizers with `NotApplicable`, `Resolved`, and
  `Invalid` results where malformed intent can be distinguished.
- Remove literal-value fallback when a phrase is a failed reference.
- Replace generic language-facing `IllegalArgumentException` failures with typed
  diagnostics carrying code, message, offending text, and source span.
- Type nested structured-value traversal and missing-field failures.
- Keep internal `require` checks only for unreachable implementation invariants.

Completion evidence:

- a repository audit finds no language-facing generic exception path;
- invalid input never becomes an unrelated literal or action;
- validator, interpreter, and compiler diagnostics agree.

## P1: Morphology and agreement

Broaden validation beyond the currently supported construction-specific checks.

- General sanādi–vikaraṇa compatibility.
- Passive and bhāve relations in prior-action and finite clauses.
- Derivational gender propagation without assuming every suffix is transparent.
- Additional upasarga, vowel-sandhi, and root-specific rendering rules.
- Consistent morphology evidence for procedure names and action-history nouns.

Every new rule must cite its grammatical basis, retain derivation provenance,
and include positive and negative tests. Unsupported derivations must remain
segmented rather than being rendered as invented surface Sanskrit.

## P1: Unified control and result flow

- Consolidate procedure return, loop exit, script stop, and failure propagation
  into an explicit typed control-effect model.
- Complete suspended enclosing procedure continuations.
- Make branch and pipeline result types statically visible where provable.
- Diagnose unreachable statements and missing declared results.
- Preserve the same behavior through interpreted and compiled nested control
  flow.

## P1: Uniform collection relations

Existing list construction, indexing, indexed iteration, previous/next member
selection, quantifiers, grouping, uniqueness, sorting, partitioning, and related
operations should share one collection-semantic model.

- Represent collection source, member binder, index, relative selection, key
  function, predicate, and result type explicitly.
- Remove older compatibility frames once examples use the natural forms.
- Generalize quantified collection phrases through attachment analysis.
- Ensure empty collections, heterogeneous values, and key failures have defined
  typed behavior.
- Keep all operations available to both interpreter and compiler without
  operation-specific reparsing.

## P2: Static analysis and tooling

- Exhaustiveness checks for structured result variants.
- Duplicate and unreachable branch detection.
- Branch and pipeline result-type compatibility.
- Definite procedure-result analysis.
- Navigation and completion from analyzed procedure signatures and schemas.
- Quick fixes driven by diagnostic codes, never by parsing message strings.

## P2: Language composition

After the P0 and P1 semantic foundations are stable, evaluate:

- optional or default procedure parameters;
- structured destructuring;
- generic collection signatures;
- higher-order kriyā values;
- explicit modules, imports, aliases, and cycle diagnostics.

No syntax should be selected before its Sanskrit relation, type behavior,
scoping, diagnostics, and compiler representation are designed together.

## Delivery order

1. General attachment relations.
2. Top-level discourse and anaphora.
3. Removal of semantic fallbacks and generic language errors.
4. Passive/bhāve and general morphology validation.
5. Unified control-effect and procedure-continuation model.
6. Uniform collection semantic nodes.
7. Static flow analysis and IDE support.
8. Reassess advanced composition features.

## Required gate for every change

Each semantic change must include the applicable layers:

1. parser acceptance and rejection;
2. typed AST or semantic relation assertions;
3. morphology and agreement tests;
4. source-located negative diagnostics;
5. interpreter behavior;
6. compiler parity;
7. rendering or readable-example verification;
8. language-guide updates;
9. a passing complete repository test suite;
10. a clean `git diff --check`.

An item is complete only when the implementation no longer depends on a hidden
fallback for the supported construction and the checked-in documentation shows
the natural form users should write.
