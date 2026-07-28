package dev.panini.actions.numeric

/** Multiplication over a coordinated expression of canonical Sanskrit number words. */
object MultiplicationAction : NumericFoldDhatuAction(
    id = "dhatu-action.numeric.multiply",
    source = "सङ्ख्यानां गुणनम्",
    name = "सङ्ख्यागुणनम्",
    description = "सङ्ख्यानां गुणनम्",
    operator = NumericFoldOperator.MULTIPLY,
    minimumOperands = 2,
)
