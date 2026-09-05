package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.86-7.2.97: yusmad-asmadoḥ.
 * Substitutions for yusmad and asmad pronominal stems before case affixes across all vibhaktis.
 */
object YusmadAsmadSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.86",
    text = "युष्मदस्मदोरनादेशे",
    hindiExplanation = "विभक्तौ परे होने पर युष्मद् और अस्मद् अङ्गों के स्थान पर आदेश और लोप नियम लागू होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720086,
    role = SutraRole.Apavada,
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

        val isYusmadOrAsmad = stem.upadesha in setOf("युष्मद्", "अस्मद्") || stem.surface in setOf("युष्मद्", "अस्मद्")
        if (!isYusmadOrAsmad) return false

        return affix.id.startsWith("sup-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val rupa = context.effectiveContext.rupa

        val isYusmad = stem.upadesha == "युष्मद्" || stem.surface == "युष्मद्"

        val (replacement, newAffixSurface) = when (rupa.vacana) {
            Vacana.EKAVACANA -> when (rupa.vibhakti) {
                Vibhakti.PRATHAMA -> Pair(if (isYusmad) "त्वम्" else "अहम्", "")
                Vibhakti.DVITIYA -> Pair(if (isYusmad) "त्वाम्" else "माम्", "")
                Vibhakti.TRTIYA -> Pair(if (isYusmad) "त्वया" else "मया", "")
                Vibhakti.CHATURTHI -> Pair(if (isYusmad) "तुभ्यम्" else "मह्यम्", "")
                Vibhakti.PANCHAMI -> Pair(if (isYusmad) "त्वत्" else "मत्", "")
                Vibhakti.SASTHI -> Pair(if (isYusmad) "तव" else "मम", "")
                else -> Pair(if (isYusmad) "त्वयि" else "मयि", "")
            }
            Vacana.DVIVACANA -> when (rupa.vibhakti) {
                Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> Pair(if (isYusmad) "युवाम्" else "आवाम्", "")
                Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI -> Pair(if (isYusmad) "युवाभ्याम्" else "आवाभ्याम्", "")
                else -> Pair(if (isYusmad) "युवयोः" else "आवयोः", "")
            }
            else -> when (rupa.vibhakti) {
                Vibhakti.PRATHAMA -> Pair(if (isYusmad) "यूयम्" else "वयम्", "")
                Vibhakti.DVITIYA -> Pair(if (isYusmad) "युष्मान्" else "अस्मान्", "")
                Vibhakti.TRTIYA -> Pair(if (isYusmad) "युष्माभिः" else "अस्माभिः", "")
                Vibhakti.CHATURTHI -> Pair(if (isYusmad) "युष्मभ्यम्" else "अस्मभ्यम्", "")
                Vibhakti.PANCHAMI -> Pair(if (isYusmad) "युष्मत्" else "अस्मत्", "")
                Vibhakti.SASTHI -> Pair(if (isYusmad) "युष्माकम्" else "अस्माकम्", "")
                else -> Pair(if (isYusmad) "युष्मासु" else "अस्मासु", "")
            }
        }

        val newTerms = context.terms.dropLast(2) + stem.copy(surface = replacement)

        return DerivationChange(
            state = context.copy(terms = newTerms, stage = DerivationStage.ANGAKARYA)
                .copy(droppedTerms = context.droppedTerms +
                    dev.panini.derivation.consumeAffixForDrop(affix, sutra, newAffixSurface)),
            explanation = "7.2.86-7.2.97: Derives $replacement for ${stem.surface} in ${rupa.vibhakti} ${rupa.vacana}."
        )
    }
}
