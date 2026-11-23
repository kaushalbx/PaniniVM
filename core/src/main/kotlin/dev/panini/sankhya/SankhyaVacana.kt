package dev.panini.sankhya

import dev.panini.core.SupAffix
import dev.panini.core.Vacana

/** Intrinsic agreement used when a cardinal directly counts its referents. */
object SankhyaVacana {
    /** Diagnostic code shared by the parser, the binder and the validator. */
    const val AGREEMENT_CODE: String = "NUMERAL_NUMBER_AGREEMENT"

    fun requiredFor(value: Long): Vacana = when (value) {
        0L, 1L -> Vacana.EKAVACANA
        2L -> Vacana.DVIVACANA
        in 20L..Long.MAX_VALUE -> Vacana.EKAVACANA
        else -> Vacana.BAHUVACANA
    }

    /** Higher cardinals are nominal quantities, so any number is grammatical from विंशति (plural hundreds too). */
    fun isCompatible(value: Long, vacana: Vacana): Boolean = value >= 20L || vacana == requiredFor(value)

    /** Whether some reading of the written sup agrees with the numeral (अम् is only singular; भ्याम् dual). */
    fun acceptsAny(value: Long, candidates: Collection<SupAffix>): Boolean =
        candidates.isEmpty() || candidates.any { isCompatible(value, it.vacana) }

    fun mismatchMessage(value: Long, vacana: Vacana): String {
        val required = requiredFor(value)
        return "सङ्ख्या $value ${required.sanskrit()} अपेक्षते, किन्तु ${vacana.sanskrit()} प्रयुक्तम्। " +
            "($value takes the ${required.english()}, not the ${vacana.english()}.)"
    }

    /** The sup of the same case in the numeral's own number: the fix for a mismatched ending. */
    fun correctedSup(value: Long, written: SupAffix): SupAffix? =
        SupAffix.entries.firstOrNull { it.vibhakti == written.vibhakti && it.vacana == requiredFor(value) }

    fun requireCompatible(value: Long, vacana: Vacana) {
        require(isCompatible(value, vacana)) { mismatchMessage(value, vacana) }
    }

    private fun Vacana.sanskrit() = when (this) {
        Vacana.EKAVACANA -> "एकवचनम्"
        Vacana.DVIVACANA -> "द्विवचनम्"
        Vacana.BAHUVACANA -> "बहुवचनम्"
    }

    private fun Vacana.english() = when (this) {
        Vacana.EKAVACANA -> "singular"
        Vacana.DVIVACANA -> "dual"
        Vacana.BAHUVACANA -> "plural"
    }
}
