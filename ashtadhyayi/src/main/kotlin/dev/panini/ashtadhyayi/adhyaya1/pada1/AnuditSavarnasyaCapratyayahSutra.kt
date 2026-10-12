package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.ItMarker
import dev.panini.shiksha.Svara
import dev.panini.shiksha.VarnaReference
import dev.panini.shiksha.VarnaReferenceUse
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.69 अणुदित्सवर्णस्य चाप्रत्ययः.
 * Meta-rule: aṇ vowels and u-it consonants (ku, chu, ṭu, tu, pu) represent all their homogeneous (savarṇa) variants.
 */
object AnuditSavarnasyaCapratyayahSutra : Sutra<VarnaReference, Boolean>(
    number = "1.1.69", text = "अणुदित्सवर्णस्य चाप्रत्ययः",
    hindiExplanation = "प्रत्यय से भिन्न अण् (अ, इ, उ, ऋ, ऌ, ए, ओ, ऐ, औ, ह, य, व, र, ल) तथा उदित् (कु, चु, टु, तु, पु) अपने सवर्ण वर्णों के ग्राहक होते हैं।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110069,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA, SutraInput.PRATYAHARA),
    adhikara = emptySet(),
) {
    // aṇ is taken with the later ṇ in the Māheśvara sequence.
    private val an = setOf(
        Svara.A, Svara.I, Svara.U, Svara.R, Svara.L, Svara.E, Svara.O, Svara.AI, Svara.AU,
        Vyanjana.HA, Vyanjana.YA, Vyanjana.VA, Vyanjana.RA, Vyanjana.LA,
    )

    override fun matches(context: VarnaReference): Boolean =
        context.use == VarnaReferenceUse.DENOTATION && context.tMarkerPosition == null &&
            (context.varna in an || ItMarker.U in context.itMarkers)

    override fun apply(context: VarnaReference): Boolean = true
}
