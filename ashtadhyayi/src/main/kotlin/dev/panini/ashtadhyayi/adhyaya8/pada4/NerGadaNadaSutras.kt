package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** The upasarga नि at the start of a verbal form whose root is known by its Dhātupāṭha upadeśa. */
private fun DerivationTerm.niBeforeRoot(): dev.panini.dhatupatha.Dhatu? {
    if (varnas.getOrNull(0) != Vyanjana.NA || varnas.getOrNull(1) != Svara.I) return null
    return sourceDhatuId?.let { DhatuPatha.find(it) }
}

private fun DerivationState.niTarget(accepts: (dev.panini.dhatupatha.Dhatu) -> Boolean): String? =
    (0 until terms.lastIndex).firstNotNullOfOrNull { i ->
        val verb = terms[i + 1]
        val root = verb.niBeforeRoot() ?: return@firstNotNullOfOrNull null
        if (!accepts(root)) return@firstNotNullOfOrNull null
        UpasargadAsamasePiSutra.nasalAfterUpasarga(this, terms[i], verb, 0)?.let { verb.id }
    }

/** 8.4.17: roots by upadeśa — गद्, नद्, पत्, पद्, घु (1.1.20), मा, सो, हन्, या, वा, द्रा, प्सा, वप्, वह्, शम्, चि, दिह्. */
internal val NER_GADA_ROOTS = setOf(
    "गदँ", "णदँ", "पतॢँ", "पदँ", "डुदाञ्", "दाण्", "दो", "देङ्", "डुधाञ्", "धेट्",
    "माङ्", "मा", "मेङ्", "षो", "हनँ", "या", "वा", "द्रा", "प्सा", "डुवपँ", "वहँ", "शमुँ", "चिञ्", "दिहँ",
)

/**
 * 8.4.17 नेर्गदनदपतपदघुमास्यतिहन्तियातिवातिद्रातिप्सातिवपतिवहतिशाम्यतिचिनोतिदेग्धिषु च —
 * after an upasarga's र्/ष्, the न of नि becomes ण before these roots: प्र + निगदति → प्रणिगदति.
 */
object NerGadaNadaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.17", text = "नेर्गदनदपतपदघुमास्यतिहन्तियातिवातिद्रातिप्सातिवपतिवहतिशाम्यतिचिनोतिदेग्धिषु च",
    hindiExplanation = "उपसर्ग के र्/ष् से परे नि के न् को गद्, नद्, पत् आदि धातुओं के परे ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840017,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = context.niTarget { it.upadesha in NER_GADA_ROOTS } != null
    override fun apply(context: DerivationState): DerivationChange {
        val id = requireNotNull(context.niTarget { it.upadesha in NER_GADA_ROOTS })
        return DerivationChange(context.replaceTermVarna(id, 0, listOf(Vyanjana.NNA), sutra),
            "8.4.17: the न of नि becomes ण after the upasarga, before a listed root.")
    }
}

/**
 * 8.4.18 शेषे विभाषाऽकखादावषान्त उपदेशे — before other roots, optionally, unless the root begins
 * with क/ख or ends in ष् in upadeśa: प्रणिपचति / प्रनिपचति, but only प्रनिकरोति, प्रनिपिनष्टि.
 * Declined by default, so stated forms stay.
 */
object SheseVibhashaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.18", text = "शेषे विभाषाऽकखादावषान्त उपदेशे",
    hindiExplanation = "शेष धातुओं में, जो उपदेश में कवर्ग-ख आदि वाले या षान्त न हों, नि के न् को विकल्प से ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840018,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    // The root shorn of its anubandhas: डुकृञ् begins with क, पिषॢँ ends in ष्; डुपचँष्'s ष् is an it.
    private fun eligible(root: dev.panini.dhatupatha.Dhatu) = root.upadesha !in NER_GADA_ROOTS &&
        !root.sourceSurface.startsWith("क") && !root.sourceSurface.startsWith("ख") && !root.sourceSurface.endsWith("ष्")

    override val appliesByDefault = false
    override fun matches(context: DerivationState) = context.niTarget(::eligible) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val id = requireNotNull(state.niTarget(::eligible))
        return listOf(DerivationChange(state.replaceTermVarna(id, 0, listOf(Vyanjana.NNA), sutra),
            "8.4.18: the न of नि optionally becomes ण."), DerivationChange(state, "8.4.18 declined.", applied = false))
    }
}
