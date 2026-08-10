# execution Module

For user-facing `.pvm` syntax and complete examples, see the
[`PVM language guide`](../docs/pvm-language-guide.md).

The `:execution` module is the runtime orchestrator of PaniniVM. It compiles parsed Sanskrit grammatical ASTs (`Vyakaranam AST`) into executable program plans, manages multi-turn dialogue/session contexts, executes actions using a sandboxed runtime environment, handles side effects via external capability dispatchers, and persists state across turns.

---

## 1. Directory Structure

```
c:\Users\User\Documents\SanskritSandhi\execution\src\main\kotlin\dev\panini\execution\
├── PaniniVM.kt                 # Main entry point and runtime facade
├── ExecutionAuthority.kt       # Permission gating, interactive loops, and safety checks
├── PvmUktiSadhaka.kt           # Orchestrator for individual sentence execution turns
├── DiscourseModel.kt           # Models for conversation speaker/listener identities
├── Phala.kt                    # Represents successful (Siddha) or failed (Asiddha) outcomes
├── SanskritPrativacanaRenderer.kt # Renders Sanskrit responses and execution traces in Devanagari
├── PvmScript.kt                # Parser for multi-turn .pvm script files
├── SamjnaKriyaRegistry.kt      # Reusable Sanskrit kriyā definitions and dispatch
├── SamjnaSignature.kt          # Typed parameter and result signatures
├── NamedSamjnaArgumentResolver.kt # Named ṣaṣṭhī-to-dvitīyā call binding
├── SamjnaScriptValidator.kt    # Non-executing signature and call diagnostics
│
├── binding/
│   └── VyakaranamExecutionAdapter.kt # Maps grammatical elements to execution expressions
│
├── runtime/
│   ├── ExecutionRuntime.kt     # Concrete execution engine for atomic action sūtras
│   ├── ExecutionEngine.kt      # Core VM evaluation loop
│   └── ValueEnvironment.kt     # Centralized type-safe value context mapper
│
├── planning/
│   ├── ExecutionPlanner.kt     # Analyzes dependencies and sequences operations
│   ├── OperationResolver.kt    # Matches verbs and Kārakas to concrete computational sūtras
│   └── DispositionResolver.kt  # Resolves intentional moods (Artha/Prayoga)
│
├── sutra/
│   ├── SutraExecutionPipeline.kt # Multi-clause execution pipeline with yield/resume logic
│   ├── ExecutableUktiSutraCompiler.kt # Compiles AST clauses to execution blueprints
│   └── ProgramBlueprintGranthaEngine.kt # Evaluates compiled script/grantha files
│
└── persistence/
    └── FileStateStore.kt       # Versioned disk serialization for dialogue contexts
```

---

## 2. Core Pipelines & Lifecycle

When a Sanskrit sentence (*Ukti*) is evaluated:

1. **Morphological Parsing**:
   The input (e.g. `एक + अम् उपान्तिम + ङे दा + लोट् + सिप्`) is processed by the `:parser` module into a grammatical AST.
2. **Semantic Kāraka Binding**:
   `VyakaranamExecutionAdapter` maps case suffixes to semantic roles (Kārakas like `KARMAN`, `SAMPRADANA`, `KARTR`) and resolves positional terms (`अन्तिम`, `उपान्तिम`, `फल`) to historical step outputs.
3. **Execution Planning**:
   `ExecutionPlanner` orders the clauses based on data-flow dependencies and resolves actions to executable operations.
4. **Resumable Execution**:
   The `SutraExecutionPipeline` runs each operation in sequence. If a side-effect (e.g., executing a command or accessing a URL) requires user approval, the pipeline yields a continuation object (`SutraPipelineContinuation`) and halts execution until explicitly resumed via `PaniniVM.resume()`.
5. **State Persistency**:
   At the end of a successful turn, `FileStateStore` serializes the conversation state (`SambhashanaContext`) into binary-safe base64 records containing all values, Saṁjñās, and step histories.

---

## 3. Notable Architectural Features

### A. First-class Saṃjñā-Kriyās

A saṃjñā block is a reusable Sanskrit operation rather than a textual macro.
`SamjnaKriyaRegistry` registers definitions across a project, resolves domain
and visibility rules, and selects overloads using typed signatures and
Pāṇinian precedence.

```pvm
योजन + ल्युट् + सुँ ।
वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
सङ्ख्या + सुँ इति परिणाम + सुँ ।
वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
```

Parameter declarations use `… इति मान + सुँ`; a primitive result declaration
uses `… इति परिणाम + सुँ`. Calls may bind parameters positionally or by name:

```pvm
दक्षिण + ङस् त्रि + अम् वाम + ङस् द्वि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

Named binding rejects duplicate, unknown, missing, and mixed positional/named
arguments. `SamjnaScriptValidator` performs the same checks without executing
the script, allowing IDE diagnostics before a run.

Structured return signatures name an existing result schema:

```pvm
अवस्था + अम् प्रयत्नसङ्ख्या + अम् अनुमानपरिणाम + मतुप् + सुँ ।

अनुमान + ल्युट् + सुँ ।
अनुमानपरिणाम + सुँ इति परिणाम + सुँ ।
...
```

`SanskritValue.Rupa` carries the schema name and typed fields through nested
calls, pipelines, semantic codecs, and persisted state. Runtime return
validation checks the schema identity and required field set.

### B. Purely Semantic Positional Reference Resolution
Instead of mapping terms like `अन्तिम`/`चरम` (last) and `उपान्तिम`/`उपान्त` (penultimate) to hardcoded string variables, the engine resolves them semantically:
- **Value Lookup**: Maps to the last (`resultHistory.last()`) or penultimate (`resultHistory[size-2]`) step outputs.
- **Assignment Compatibility**: If a positional term is the target of an active assignment (e.g. maps to `SAMPRADANA` of the verb `दा`), the compiler treats it as a local variable declaration to ensure loop assignments function correctly without environment collisions.

### C. Nominal Sentence (NamaVakya) Execution
For Sanskrit sentences lacking an explicit verb, the runtime implicitly resolves the copula root **`असँ`** (`AsDhatu` / "to be") and infers semantic roles directly from the nouns' case endings:
- `PRATHAMA` (`सुँ`) maps to `Karaka.KARTR` (Agent).
- `DVITIYA` (`अम्`) maps to `Karaka.KARMAN` (Object).

This allows statements like `एक + सुँ युज् + ल्युट् + ङस् फल + अम् ।` ("One exists in the result") to execute natively.
