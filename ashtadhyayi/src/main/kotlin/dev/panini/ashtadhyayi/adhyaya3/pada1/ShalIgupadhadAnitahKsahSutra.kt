package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.derivation.TermKind
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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
    private val shal = setOf(Vyanjana.SHA, Vyanjana.SSA, Vyanjana.SA, Vyanjana.HA)
    private val ik = setOf(Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR, Svara.L, Svara.LL)

    override fun matches(context: DerivationState): Boolean {
        val cliIndex = context.terms.indexOfFirst { it.kind == TermKind.PRATYAYA && it.upadesha == "च्लि" }
        if (cliIndex < 0) return false
        val rootIndex = context.terms.take(cliIndex).indexOfLast { it.kind == TermKind.DHATU }
        if (rootIndex < 0) return false
        // A surviving derivational suffix changes the aṅga; do not inspect its bare parent root.
        if (context.terms.subList(rootIndex + 1, cliIndex).any { it.kind == TermKind.PRATYAYA }) return false
        val root = context.terms[rootIndex]
        return root.itStatus == ItStatus.ANIT && root.varnas.lastOrNull() in shal &&
            root.varnas.getOrNull(root.varnas.lastIndex - 1) in ik
    }

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        return DerivationChange(
            context.replaceWholeAffix(
                id = cli.id,
                varnas = listOf(Vyanjana.KA, Vyanjana.SA, Svara.A),
                sutra = sutra,
                policy = WholeAffixDesignationPolicy.Consume,
                upadesha = "क्स",
            ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.45 substitutes क्स for च्लि after śal-ending aniṭ root.",
        )
    }
}
