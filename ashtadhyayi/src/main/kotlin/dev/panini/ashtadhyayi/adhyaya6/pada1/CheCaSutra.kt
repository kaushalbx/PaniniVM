package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.LocatedVarna
import dev.panini.derivation.isPadaFinalVarna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.isHrasva
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** A vowel directly before छ, within a term or across a boundary in saṃhitā (6.1.72). */
internal data class CheTukSite(val vowel: LocatedVarna, val cha: LocatedVarna, val padaFinal: Boolean)

internal fun DerivationState.cheTukSites(): List<CheTukSite> {
    if (terms.any { it.itProcessingPending || it.itDesignations.isNotEmpty() || it.deferredItDesignations.isNotEmpty() }) return emptyList()
    val sounds = terms.flatMap { term -> term.varnas.mapIndexed { index, varna -> LocatedVarna(term.id, index, varna) } }
    return sounds.zipWithNext().filter { (vowel, cha) -> vowel.varna is Svara && cha.varna == Vyanjana.CHA }.map { (vowel, cha) ->
        CheTukSite(vowel, cha, isPadaFinalVarna(terms.single { it.id == vowel.termId }, vowel.index))
    }
}

internal fun DerivationState.insertTuk(site: CheTukSite, sutra: String): DerivationState =
    insertTermVarnas(site.cha.termId, site.cha.index, listOf(Vyanjana.TA), sutra)

/** 6.1.73: छे च. तुक् after a short vowel before छ् (तव + छाया → तवच्छाया by 8.4.40). */
object CheCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.73",
    text = "छे च",
    hindiExplanation = "ह्रस्व स्वर के बाद छकार परे होने पर तुक् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610073,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.VARNA,
    stage = dev.panini.sutra.SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    private fun site(context: DerivationState) =
        context.cheTukSites().firstOrNull { (it.vowel.varna as Svara).isHrasva }

    override fun matches(context: DerivationState): Boolean = site(context) != null

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.insertTuk(requireNotNull(site(context)), sutra),
        explanation = "6.1.73 inserts तुक् after a short vowel before छ्.",
    )
}
