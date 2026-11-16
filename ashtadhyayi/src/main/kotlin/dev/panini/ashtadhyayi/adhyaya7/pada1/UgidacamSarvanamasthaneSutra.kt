package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.derivation.hasSarvanamasthanaSupIdentity
import dev.panini.pratyahara.ItMarker
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/**
 * 7.1.70 उगिदचां सर्वनामस्थानेऽधातोः — before a सर्वनामस्थान, an aṅga that is उगित् (not a dhātu) takes
 * नुम्, placed after its last vowel (1.1.47 मिदचोऽन्त्यात्परः): पुमस् (असुँङ्, 7.1.89) → पुमन्स्.
 * Only an उक् it that the derivation itself recorded (an ādeśa's it provenance) makes the aṅga उगित्;
 * a bare stem spelling is never taken as proof. The अच् (अञ्चु) half is not modelled.
 */
object UgidacamSarvanamasthaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.70", text = "उगिदचां सर्वनामस्थानेऽधातोः",
    hindiExplanation = "सर्वनामस्थान परे धातु-भिन्न उगित् अङ्ग को नुम् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false, kramaValue = 710070,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val uk = Pratyahara(Svara.U, ItMarker.K)

    private fun DerivationTerm.isRecordedUgit() = adeshaOrigins.any { origin ->
        origin.itMarkerProvenance.any { provenance ->
            provenance.designatedText.toVarnas().firstOrNull()
                ?.let { Ashtadhyayi.pratyaharaEngine.contains(uk, it) } == true
        }
    }

    private fun target(context: DerivationState): DerivationTerm? {
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return null
        val affix = context.terms.last()
        if (stem.kind != TermKind.PRATIPADIKA || !stem.isRecordedUgit()) return null
        val sarvanamasthana = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SARVANAMASTHANA } ||
            (affix.hasSarvanamasthanaSupIdentity() && !HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context))
        if (!sarvanamasthana) return null
        if (context.substitutions.any { it.targetId == stem.id && it.sutra == sutra }) return null
        return stem
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        val lastVowel = stem.varnas.indexOfLast { it is Svara }
        require(lastVowel >= 0)
        return DerivationChange(context.insertTermVarnas(stem.id, lastVowel + 1, listOf(Vyanjana.NA), sutra),
            "7.1.70 inserts नुम् (न्) after the last vowel of the उगित् aṅga ${stem.surface}.")
    }
}
