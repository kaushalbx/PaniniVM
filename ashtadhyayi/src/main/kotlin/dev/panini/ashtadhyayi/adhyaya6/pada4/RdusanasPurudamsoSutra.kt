package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.hasSarvanamasthanaSupIdentity
import dev.panini.shiksha.Svara
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.11: aptṛntṛcsvasaṛnaptṛneṣṭṛtvaṣṭṛkṣattṛhotṛpotṛpraśāstṝṇām.
 * Lengthens the penultimate vowel of the enumerated stems before a non-vocative
 * sarvanāmasthāna ending. General ऋ-final nominative singular formation belongs
 * to 7.1.94 followed by 6.4.8 and 8.2.7, not to this rule.
 * Source: https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.4.11.htm
 */
object ApTrnTrcSvasrNaptrNestrTvastrKsattrHotrPotrPrasastrnamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.11",
    text = "अप्तृन्तृच्स्वसृनप्तृनेष्टृत्वष्टृक्षत्तृहोतृपोतृप्रशास्तॄणाम्",
    hindiExplanation = "असम्बुद्धि सर्वनामस्थान परे निर्दिष्ट अङ्गों की उपधा दीर्घ होती है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640011,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.upadesha !in explicitlyEnumerated || stem.varnas.size < 2) return false
        if (!affix.hasSarvanamasthanaSupIdentity() || affix.sourceSupAffix == dev.panini.core.SupAffix.SU) return false
        val penultimate = stem.varnas.lastIndex - 1
        return stem.varnas.getOrNull(penultimate) == Svara.A
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val penultimate = stem.varnas.lastIndex - 1
        return DerivationChange(
            state = context.replaceTermVarna(stem.id, penultimate, listOf(Svara.AA), sutra),
            explanation = "6.4.11 lengthens the exact penultimate a of the enumerated stem."
        )
    }

    private val explicitlyEnumerated = setOf("स्वसृ", "नप्तृ", "नेष्टृ", "त्वष्टृ", "क्षत्तृ", "होतृ", "पोतृ", "प्रशास्तृ")
}
