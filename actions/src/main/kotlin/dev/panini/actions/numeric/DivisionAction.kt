package dev.panini.actions.numeric

/** Division over a coordinated expression of canonical Sanskrit number words. */
object DivisionAction : NumericFoldDhatuAction(
    id = "dhatu-action.numeric.divide",
    source = "सङ्ख्यानां विभाजनम्",
    name = "सङ्ख्याहरणम्",
    description = "सङ्ख्यानां विभाजनम्",
    operator = NumericFoldOperator.DIVIDE,
    minimumOperands = 2,
)

