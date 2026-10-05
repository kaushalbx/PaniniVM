package dev.panini.sankhya

import dev.panini.core.Vacana

/** Intrinsic agreement used when a cardinal directly counts its referents. */
object SankhyaVacana {
    fun requiredFor(value: Long): Vacana = when (value) {
        0L, 1L -> Vacana.EKAVACANA
        2L -> Vacana.DVIVACANA
        in 20L..Long.MAX_VALUE -> Vacana.EKAVACANA
        else -> Vacana.BAHUVACANA
    }

    fun requireCompatible(value: Long, vacana: Vacana) {
        // Higher cardinals are nominal quantities: plural counts of hundreds,
        // for example, are grammatical too. Do not impose referent agreement.
        if (value >= 20L) return
        val required = requiredFor(value)
        require(vacana == required) {
            "सङ्ख्या $value $required अपेक्षते, किन्तु $vacana प्रयुक्तम्।"
        }
    }
}
