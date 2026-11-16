package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.isHrasva
import dev.panini.sutra.*

private fun DerivationState.longVowelTukSites(padaFinal: Boolean) =
    cheTukSites().filter { (it.vowel.varna as Svara).isHrasva != true && it.padaFinal == padaFinal &&
        !(padaFinal && isAngMang(it)) }

private fun DerivationState.isAngMang(site: CheTukSite) =
    terms.single { it.id == site.vowel.termId }.upadesha in setOf("आङ्", "माङ्")

/** 6.1.74: आङ्माङोश्च. After आङ् or माङ् the तुक् before छ is obligatory (आच्छादयति, मा च्छिदत्), not 6.1.76's option. */
object AngMangoshcaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.74", text = "आङ्माङोश्च",
    hindiExplanation = "आङ् और माङ् के बाद छकार परे होने पर नित्य तुक् आगम होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610074,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.VARNA, stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    private fun site(state: DerivationState) = state.cheTukSites().firstOrNull { state.isAngMang(it) }
    override fun matches(context: DerivationState) = site(context) != null
    override fun apply(context: DerivationState) = DerivationChange(
        context.insertTuk(requireNotNull(site(context)), sutra), "6.1.74 inserts तुक् after आङ्/माङ् before छ्.")
}

/** 6.1.75: दीर्घात्. तुक् before छ after a long vowel (म्लेच्छति); pada-final ones are 6.1.76's. */
object DirghatSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.75", text = "दीर्घात्",
    hindiExplanation = "दीर्घ स्वर के बाद छकार परे होने पर तुक् आगम होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610075,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.VARNA, stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = context.longVowelTukSites(padaFinal = false).isNotEmpty()
    override fun apply(context: DerivationState) = DerivationChange(
        context.insertTuk(context.longVowelTukSites(padaFinal = false).first(), sutra),
        "6.1.75 inserts तुक् after a long vowel before छ्.")
}

/** 6.1.76: पदान्ताद्वा. After a pada-final long vowel the तुक् is optional (लक्ष्मीच्छाया / लक्ष्मीछाया). */
object PadantadVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.76", text = "पदान्ताद्वा",
    hindiExplanation = "पदान्त दीर्घ स्वर के बाद छकार परे होने पर विकल्प से तुक् आगम होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 1, optional = true, kramaValue = 610076,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.VARNA, stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = context.longVowelTukSites(padaFinal = true).isNotEmpty()
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val site = state.longVowelTukSites(padaFinal = true).first()
        return listOf(DerivationChange(state.insertTuk(site, sutra), "6.1.76 inserts तुक् after a pada-final long vowel before छ्."),
            DerivationChange(state, "6.1.76 declined.", applied = false))
    }
}
