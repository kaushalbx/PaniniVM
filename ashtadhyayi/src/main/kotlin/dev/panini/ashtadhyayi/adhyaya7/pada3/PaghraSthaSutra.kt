package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Varna
import dev.panini.sutra.*

/** 7.3.78: currently implements the स्था -> तिष्ठ member before adjacent śit. */
object PaghraSthaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.78",
    text = "पाघ्राध्मास्थाम्नादाण्दृश्यर्तिसर्तिशदसदां पिबजिघ्रधमतिष्ठमनयच्छपश्यर्च्छधौशीयसीदाः",
    hindiExplanation = "शित् प्रत्यय परे स्था को तिष्ठ आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730078, role = SutraRole.Vidhi, action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    private val stha: List<Varna> = listOf(Vyanjana.SA, Vyanjana.THA, Svara.AA)
    private val tistha: List<Varna> = listOf(Vyanjana.TA, Svara.I, Vyanjana.SSA, Vyanjana.TTHA, Svara.A)

    override fun matches(context: DerivationState): Boolean {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (index < 0) return false
        val root = context.terms[index]
        val following = context.terms.getOrNull(index + 1)
        return (root.matchesUpadesha("ष्ठा") || root.matchesUpadesha("स्थाञँ")) && root.varnas == stha &&
            following?.upadesha in setOf("शप्", "श") &&
            context.substitutions.none { it.targetId == root.id && it.sutra == sutra }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { it.kind == TermKind.DHATU }
        return DerivationChange(
            context.replaceTermVarnaRange(root.id, 0, root.varnas.size, tistha, mapOf(1 to 2), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            "7.3.78 replaces स्था by तिष्ठ before the adjacent śit stem-forming suffix.",
        )
    }
}
