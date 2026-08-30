package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SthaniProperties
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.82: परस्मैपदानां णलतुसुस्थलथुसणल्वमाः. */
object ParasmaipadanamNalatUsusthalathusaNalvamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.82", text = "परस्मैपदानां णलतुसुस्थलथुसणल्वमाः",
    hindiExplanation = "लिट् में परस्मैपद के नौ तिङ्-प्रत्ययों के स्थान पर णल् आदि नौ आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340082,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val replacements = mapOf("तिप्" to "णल्", "तस्" to "अतुस्", "झि" to "उस्", "सिप्" to "थल्", "थस्" to "अथुस्", "थ" to "अ", "मिप्" to "अ", "वस्" to "व", "मस्" to "म")

    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        val replacement = replacements[ending.upadesha] ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            ending.surface != replacement
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = requireNotNull(replacements[ending.upadesha])
        val requiresItProcessing = replacement in setOf("णल्", "थल्")
        val substituted = ending.copy(
            surface = replacement,
            upadesha = if (requiresItProcessing) replacement else ending.upadesha,
            itMarkers = emptySet(),
            itDesignations = emptyList(),
            itProcessingPhase = if (requiresItProcessing) {
                dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA
            } else {
                dev.panini.derivation.ItProcessingPhase.PROCESSED
            },
            sthaniProps = SthaniProperties(ending.upadesha, emptySet()),
        )
        return DerivationChange(context.replaceTerm(ending.id, substituted),
            "3.4.82 replaces the Parasmaipada ${ending.upadesha} ending with $replacement in लिट्.")
    }
}
