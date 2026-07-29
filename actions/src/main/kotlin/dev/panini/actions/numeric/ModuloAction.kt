package dev.panini.actions.numeric

/** Modulo (remainder after division) over Sanskrit number words. */
object ModuloAction : NumericFoldDhatuAction(
    id = "dhatu-action.numeric.modulo",
    source = "सङ्ख्याविभाजनात् शेषः",
    name = "सङ्ख्याशेषः",
    description = "सङ्ख्याविभाजनात् शेषः",
    operator = NumericFoldOperator.MODULO,
    minimumOperands = 2,
)

