package dev.panini.execution

/** A language-facing response to an interpreted utterance. */
data class Prativacana(
    val text: String,
    val phala: Phala,
)

/** Keeps Sanskrit presentation separate from interpretation and task execution. */
object SanskritPrativacanaRenderer {
    fun render(phala: Phala): Prativacana = Prativacana(
        text = when (phala) {
            is Phala.Siddha -> success(phala)
            is Phala.Asiddha -> failure(phala)
            is Phala.AnumatiApekshita ->
                "${phala.invocationId} इत्यस्य अनुष्ठानाय अनुमतिः अपेक्षिता।"
            is Phala.SvikaraApekshita ->
                "${phala.listener}, ${phala.speaker} इत्यस्य प्रार्थनां स्वीकरोषि किम्?"
            is Phala.Nirasta ->
                "${phala.invocationId} इति निर्देशः निरस्तः। कारणम् — ${phala.reason}"
            is Phala.Avagata ->
                "वाक्यम् अवगतम्। प्रयोजनम् — ${phala.disposition}। क्रिया न कृता।"
        },
        phala = phala,
    )

    private fun success(phala: Phala.Siddha): String {
        if (phala.values.isEmpty()) return "कार्यम् सिद्धम्।"
        val results = phala.values.entries.joinToString(separator = " ") { (id, value) -> "$id — $value।" }
        return "कार्यम् सिद्धम्। $results"
    }

    private fun failure(phala: Phala.Asiddha): String = when (val result = phala.result) {
        is ExecutionResult.Failure -> "कार्यं न सिद्धम्। कारणम् — ${result.message}"
        is ExecutionResult.NeedsInput -> "सूचना अपेक्षिता। ${result.message}"
        is ExecutionResult.Ambiguous -> "अर्थः अनिश्चितः। ${result.message}"
        is ExecutionResult.Success -> "कार्यम् सिद्धम्। फलम् — ${result.value}।"
    }
}
