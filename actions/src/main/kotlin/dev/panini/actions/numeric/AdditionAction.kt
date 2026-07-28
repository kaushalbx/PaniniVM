package dev.panini.actions.numeric

/** Addition over a coordinated expression of canonical Sanskrit number words. */
object AdditionAction : NumericFoldDhatuAction(
    id = "dhatu-action.numeric.add",
    source = "सङ्ख्यानां योगः",
    name = "सङ्ख्यायोजनम्",
    description = "सङ्ख्यानां योगः",
    operator = NumericFoldOperator.ADD,
    minimumOperands = 1,
)
