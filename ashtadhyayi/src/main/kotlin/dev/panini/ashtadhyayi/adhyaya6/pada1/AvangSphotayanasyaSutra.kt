package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

internal fun goVowelBoundary(context: DerivationState, blockedRule: String? = null): Int? =
    (0 until context.terms.lastIndex).firstOrNull { index ->
        val left = context.terms[index]
        left.varnas == listOf(Vyanjana.GA, Svara.O) &&
            context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA } &&
            context.terms[index + 1].varnas.firstOrNull() is Svara &&
            (blockedRule == null || !context.isBlockedAtBoundary(blockedRule, left.id, context.terms[index + 1].id))
    }

internal fun applyAvang(context: DerivationState, index: Int, sutra: String): DerivationChange {
    val left = context.terms[index]
    val ava: List<Varna> = listOf(Svara.A, Vyanjana.VA, Svara.A)
    return DerivationChange(
        context.substituteTermVarnas(left.id, left.varnas.dropLast(1) + ava, Svara.O, ava, sutra),
        "$sutra substitutes अव for the final ओ of पदसंज्ञक गो, retaining the following vowel boundary.",
    )
}

/** 6.1.123: the optional avaṅ substitution of Sphoṭāyana before a vowel. */
object AvangSphotayanasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.123", text = "अवङ् स्फोटायनस्य",
    hindiExplanation = "पदान्त गो के ओकार के स्थान पर अच् परे विकल्प से अवङ् आदेश होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 1, optional = true,
    kramaValue = 610123, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.VARNA, stage = SutraStage.SANDHI,
    priority = SutraPriority.APAVADA, blocks = setOf("6.1.78", "6.1.109", "6.1.122"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = goVowelBoundary(context, sutra) != null
    override fun apply(context: DerivationState): DerivationChange =
        applyAvang(context, requireNotNull(goVowelBoundary(context, sutra)), sutra)
}
