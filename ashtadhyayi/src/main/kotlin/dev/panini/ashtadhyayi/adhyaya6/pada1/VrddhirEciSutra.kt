package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.DhatuGana
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.1.88: a/ā + ec -> vṛddhi. An apavāda to 6.1.87 (Ad Guna). */
object VrddhirEciSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.88",
    text = "वृद्धिरेचि",
    hindiExplanation = "अ या आ के बाद एच् (ए, ऐ, ओ, औ) आए तो पूर्व और पर के स्थान पर एक वृद्ध्यादेश होता है।",
    type = SutraType.APAVADA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610088,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
    priority = SutraPriority.APAVADA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        if (augmentRootPair(context) != null) return true
        val leftTerm = context.terms[context.terms.size - 2]
        if (leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) return false
        val right = context.terms.last().surface.firstOrNull() ?: return false

        val engine = Ashtadhyayi.pratyaharaEngine
        val isA = dev.panini.shiksha.Varnamala.endsWithA(leftTerm.surface) || dev.panini.shiksha.Varnamala.endsWithAA(leftTerm.surface)
        return isA && engine.contains(Pratyahara.EC, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        augmentRootPair(context)?.let { index ->
            val augment = terms[index]
            val root = terms[index + 1]
            val substitute = when (root.surface.first()) {
                'ए', 'ऐ' -> "ऐ"
                'ओ', 'औ' -> "औ"
                else -> error("Unsupported ec vowel in ${root.surface}")
            }
            val newRoot = root.copy(surface = substitute + root.surface.drop(1))
            return DerivationChange(
                state = context.copy(
                    terms = terms.take(index) + newRoot + terms.drop(index + 2),
                    droppedTerms = context.droppedTerms + augment.copy(surface = ""),
                    stage = DerivationStage.PADA_FORMED,
                ).addSubstitution(VarnaSubstitution(root.id, root.surface.first(), substitute, sutra)),
                explanation = "6.1.88: Vṛddhi substitution ($substitute) for augment अ + ${root.surface.first()}.",
            )
        }
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()

        val leftChar = leftTerm.surface.last()
        val rightChar = rightTerm.surface.first()

        val substitute = getVrddhi(rightChar)

        val newSurface = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            leftTerm.surface + substitute + rightTerm.surface.drop(1)
        } else {
            leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        }

        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + leftTerm.copy(
                    surface = newSurface,
                    // Ekadeśa combines the sounds, not their grammatical identity.
                    // Preserve the right-hand term's it-status for 1.3.9.
                    itMarkers = leftTerm.itMarkers + rightTerm.itMarkers,
                ),
                droppedTerms = context.droppedTerms + terms.last().copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra)),
            explanation = "6.1.88: Vṛddhi substitution ($substitute) for $leftChar + $rightChar."
        )
    }

    private fun getVrddhi(right: Char): String = when (right) {
        'ए', 'ऐ', 'े', 'ै' -> "ै"
        'ओ', 'औ', 'ो', 'ौ' -> "ौ"
        else -> "ा"
    }

    private fun augmentRootPair(context: DerivationState): Int? {
        val engine = Ashtadhyayi.pratyaharaEngine
        return (0 until context.terms.lastIndex).firstOrNull { index ->
            val left = context.terms[index]
            val right = context.terms[index + 1]
            left.id == "at-agama" && right.kind == TermKind.DHATU &&
                right.surface.firstOrNull()?.let { engine.contains(Pratyahara.EC, it) } == true
        }
    }
}

object TasmacChasoNahPumsiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.103",
    text = "तस्माच्छसो नः पुंसि",
    hindiExplanation = "पुंसि शस् के सकार के स्थान पर नकार होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610103,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
    priority = SutraPriority.APAVADA,
    blocks = setOf("8.2.66"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PADA_FORMED &&
                HasMorphosyntax(
                    linga = Linga.PUMS,
                    vibhakti = Vibhakti.DVITIYA,
                    vacana = Vacana.BAHUVACANA,
                ).matches(context) &&
                context.terms.lastOrNull()?.surface?.let { s -> s.endsWith("ास्") || s.endsWith("ीस्") || s.endsWith("ूस्") } == true

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .copy(surface = context.terms.last().surface.dropLast(2) + "न्"),
            stage = DerivationStage.FINAL,
        ),
        explanation = "6.1.103 replaces final स् with न् after the lengthened stem in masculine accusative plural.",
    )
}

object AmiPurvahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.107",
    text = "अमि पूर्वः",
    hindiExplanation = "अम् प्रत्यय के अच् के स्थान पर पूर्ववर्ण रहता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610107,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PRATYAYA_SELECTED &&
            context.effectiveContext.rupa.linga != Linga.NAPUMSAKA &&
            context.terms.lastOrNull()?.id == "sup-am"

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface + "म्"),
                stage = DerivationStage.FINAL
            ), "6.1.107 retains the preceding vowel before अम्."
        )
    }
}
