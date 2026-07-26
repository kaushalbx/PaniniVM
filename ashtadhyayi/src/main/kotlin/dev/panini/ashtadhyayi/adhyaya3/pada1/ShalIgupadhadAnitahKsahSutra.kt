package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.45: शल इगुपधादनिटः क्सः. Substitutes क्स for च्लि after śal-ending aniṭ roots with ik penult. */
object ShalIgupadhadAnitahKsahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.45",
    text = "शल इगुपधादनिटः क्सः",
    hindiExplanation = "शलन्त, इगुपध और अनिट् धातुओं से परे च्लि के स्थान पर क्स प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310045,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val ksaRoots = setOf("दिश्", "दृश्", "लिख्", "विष्", "कृष्", "द्विष्")

    override fun matches(context: DerivationState): Boolean {
        val cli = context.terms.firstOrNull { it.upadesha == "च्लि" } ?: return false
        return context.allEffectiveTerms.any { term ->
            term.surface in ksaRoots || (term.upadesha != null && ksaRoots.any { root -> term.upadesha!!.startsWith(root) })
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        val ksa = DerivationTerm(
            id = cli.id,
            surface = "क्स",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.KIT),
            upadesha = "क्स",
        )
        return DerivationChange(
            context.replaceTerm(cli.id, ksa).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.45 substitutes क्स for च्लि after śal-ending aniṭ root.",
        )
    }
}
