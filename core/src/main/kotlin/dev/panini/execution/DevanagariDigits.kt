package dev.panini.execution

object DevanagariDigits {
    fun render(value: Int): String = value.toString().map { digit ->
        digits[digit] ?: digit
    }.joinToString("")

    private val digits = mapOf(
        '0' to '०', '1' to '१', '2' to '२', '3' to '३', '4' to '४',
        '5' to '५', '6' to '६', '7' to '७', '8' to '८', '9' to '९',
    )
}
