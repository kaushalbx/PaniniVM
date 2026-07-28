package dev.panini.actions.numeric

/** Subtraction over a coordinated expression of canonical Sanskrit number words. */
object SubtractionAction : NumericFoldDhatuAction(
    id = "dhatu-action.numeric.subtract",
    source = "सङ्ख्यानां वियोगः",
    name = "सङ्ख्यावियोगः",
    description = "सङ्ख्यानां वियोगः",
    operator = NumericFoldOperator.SUBTRACT,
    minimumOperands = 2,
)
