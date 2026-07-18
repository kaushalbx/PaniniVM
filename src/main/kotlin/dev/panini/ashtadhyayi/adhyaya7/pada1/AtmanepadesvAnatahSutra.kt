package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.*
import dev.panini.dhatupatha.Gana
import dev.panini.sutra.*

/** 7.1.5: आत्मनेपदेष्वनतः. */
object AtmanepadesvAnatahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.5", text = "आत्मनेपदेष्वनतः",
    hindiExplanation = "अकारान्त से भिन्न अङ्ग के बाद आत्मनेपद के झ् को अत् आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false, kramaValue = 710005,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lakara = context.effectiveContext.rupa.lakara
        if (lakara !in setOf(Lakara.LAT, Lakara.LOT, Lakara.LANG, Lakara.LUNG)) return false
        val hasNonAStemVikarana = context.terms.firstOrNull { it.kind == TermKind.DHATU }?.gana in setOf(
            Gana.ADADI, Gana.JUHOTYADI, Gana.SVADI, Gana.RUDHADI, Gana.TANADI, Gana.KRYADI,
        )
        if (lakara in setOf(Lakara.LAT, Lakara.LOT, Lakara.LANG) && !hasNonAStemVikarana) return false
        val endingIndex = context.terms.indexOfLast { it.upadesha == "झ" && it.surface.startsWith("झ") }
        if (endingIndex <= 0) return false
        return !context.terms[endingIndex - 1].surface.endsWith("अ")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last { it.upadesha == "झ" && it.surface.startsWith("झ") }
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "अत")),
            "7.1.5 substitutes अत् for the Atmanepada झ after a non-a-final anga.",
        )
    }
}
