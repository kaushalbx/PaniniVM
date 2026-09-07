package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.AccentType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SvaraNimittaKind
import dev.panini.sutra.*

object AnudattauSuppitauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.4", text = "अनुदात्तौ सुप्पितौ", hindiExplanation = "सुप् तथा पित् प्रत्यय अनुदात्त होते हैं।",
    type = SutraType.APAVADA, chapter = 3, pada = 1, optional = false, kramaValue = 310004,
    role = SutraRole.Apavada, action = SutraAction.VIDHI, scope = SutraScope.PRATYAYA, stage = SutraStage.SVARA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.svaraAssignments.none { it.accent == AccentType.UDATTA } &&
            context.svaraNimittas.any { it.kind == SvaraNimittaKind.PIT_OR_SUP }

    override fun apply(context: DerivationState): DerivationChange {
        val vowelIndex = (svaraVowelPositions(context.surface).size - 2).coerceAtLeast(0)
        return DerivationChange(context.assignSvara(vowelIndex, AccentType.UDATTA, number), "$text leaves the stem accent operative before an anudātta sup/pit affix.")
    }
}

internal fun svaraVowelPositions(word: String): List<Int> = word.indices.filter { index ->
    word[index] in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ', 'ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'े', 'ै', 'ो', 'ौ')
}
