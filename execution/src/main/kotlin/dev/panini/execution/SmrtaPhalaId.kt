package dev.panini.execution

/** Canonical identity of a remembered invocation result within a conversation turn. */
internal object SmrtaPhalaId {
    fun turnPrefix(turnNumber: Int): String {
        require(turnNumber > 0) { "A remembered result requires a positive turn number." }
        return "उक्ति-${DevanagariDigits.render(turnNumber)}"
    }

    fun of(turnNumber: Int, invocationId: String): String {
        require(invocationId.isNotBlank()) { "A remembered result requires an invocation identity." }
        return "${turnPrefix(turnNumber)}/$invocationId"
    }
}
