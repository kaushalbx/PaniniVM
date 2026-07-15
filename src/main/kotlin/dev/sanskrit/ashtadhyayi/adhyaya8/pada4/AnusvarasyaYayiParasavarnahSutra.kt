package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 8.4.58: anusvārasya yayi parasavarṇaḥ.
 * Anusvāra is replaced by a sound homogeneous with the following sound (parasavarṇa)
 * if that sound is in the 'yay' pratyāhāra (all consonants except ś, ṣ, s, h).
 */
object AnusvarasyaYayiParasavarnahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.58",
    text = "अनुस्वारस्य ययि परसवर्णः",
    hindiExplanation = "यय् वर्ण परे होने पर अनुस्वार के स्थान पर परसवर्ण (बाद वाले वर्ण का सवर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840058,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val surface = context.surface
        val index = surface.indexOf('ं')
        if (index == -1 || index == surface.length - 1) return false

        val nextChar = surface[index + 1]
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, nextChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val surface = context.surface
        val index = surface.indexOf('ं')
        val nextChar = surface[index + 1]

        // Per 1.1.50, we pick the substitute that matches Sthāna.
        // For Anusvāra, we look for the Nasal member of the varga of nextChar.
        val vargaInfo = Varnamala.getVargaInfo(nextChar)
        val substitute = if (vargaInfo != null) {
            val member = Varnamala.getVargaMember(vargaInfo.first, 4)
            if (member != null) member.toString() + "्" else "न्"
        } else {
            // Semivowels (y, l, v) have nasalized counterparts (anunāsika).
            // Simplified here to just use the base nasal or the char itself.
            "न्"
        }

        var offset = 0
        val targetTerm = context.terms.find { term ->
            val start = offset
            offset += term.surface.length
            index in start until offset
        } ?: return DerivationChange(context, "8.4.58: Target anusvāra not found.")

        val newSurface = targetTerm.surface.replaceFirst("ं", substitute)
        if (newSurface == targetTerm.surface) {
            return DerivationChange(context, "8.4.58: Parasavarṇa substitution already reflected in this term.")
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.58: Replaced Anusvāra with nasal parasavarṇa '$substitute' before '$nextChar'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'ं', substitute, sutra))) }
    }
}
