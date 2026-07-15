package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.82: परस्मैपदानां णलतुसुस्थलथुसणल्वमाः. */
object ParasmaipadanamNalatUsusthalathusaNalvamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.82", text = "परस्मैपदानां णलतुसुस्थलथुसणल्वमाः",
    hindiExplanation = "लिट् में परस्मैपद के नौ तिङ्-प्रत्ययों के स्थान पर णल् आदि नौ आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340082,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val replacements = mapOf("तिप्" to "अ", "तस्" to "अतुस्", "झि" to "उस्", "सिप्" to "थल्", "थस्" to "अथुस्", "थ" to "अ", "मिप्" to "अ", "वस्" to "व", "मस्" to "म")

    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        val replacement = replacements[ending.upadesha] ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.stage != DerivationStage.IT_PROCESSED && ending.surface != replacement
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = requireNotNull(replacements[ending.upadesha])
        val nal = if (ending.upadesha == "तिप्") {
            ending.copy(surface = replacement, upadesha = "णल्", itMarkers = ending.itMarkers + ItMarker.NIT)
        } else {
            ending.copy(surface = replacement)
        }
        return DerivationChange(context.replaceTerm(ending.id, nal),
            "3.4.82 replaces the Parasmaipada ${ending.upadesha} ending with $replacement in लिट्.")
    }
}
