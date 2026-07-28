# Module `:actions`

Concrete runtime actions for Sanskrit execution contexts.

## Execution contract

Each `DhatuAction`:

1. reads typed operands from kāraka bindings in `ExecutionContext`;
2. resolves references to `SanskritValue` objects;
3. performs one operation; and
4. returns a structured `ExecutionResult` with a trace and typed value.

Missing or invalid operands produce `ExecutionResult.Failure`; they must not
escape as exceptions.

## Capabilities

- `numeric`: arithmetic, comparison, geometry, randomness and trigonometry
- `collection`: list construction, querying, transformation and folding
- `control`: conditional, counted, `foreach` and bounded `while` execution
- `io`: read, print and emit results
- `linguistic`: sandhi, subanta derivation and summarization
- `state`: value operations and host-supplied persistent state
- `external`: capability-controlled external dispatch
- `resource`: resource consumption and release abstractions

External effects and persistence require capabilities supplied by the host.
Nested and loop-like actions forward those capabilities and propagate failures.

## Numeric results

Integral results use `SanskritValue.Sankhya`. Fractional approximations use
`SanskritValue.Rational`. Operations that cannot return a meaningful value in
the current numeric model fail explicitly rather than silently truncating.

## Tests

Run the module suite from the repository root:

```shell
./gradlew :actions:test
```
