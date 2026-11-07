package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.AvyayaPada

private val reasons = mapOf("2.3.5" to "अत्यन्तसंयोग", "2.3.6" to "अपवर्ग", "2.3.19" to "सहार्थ", "2.3.20" to "अङ्गविकार",
    "2.3.23" to "हेतु", "2.3.25" to "हेतु", "2.3.32" to "विनार्थ", "2.3.37" to "भावलक्षण", "2.3.41" to "निर्धारण",
    "2.3.42" to "विभक्त", "2.3.46" to "विधेय")

/** Why a resolved word has no kāraka, read from the sūtra that gave its case. Null for anything else. */
fun KriyaFrame.akarakaReason(relation: KarakaRelation): String? {
    if ((relation.resolution as? FrameKarakaResolution.Resolved)?.karaka != Karaka.ANIRDHARITA) return null
    val padas = vakya.padas
    val at = padas.indexOfFirst { it === relation.participant.pada }
    if (at >= 0 && (padas.getOrNull(at + 1) as? AvyayaPada)?.form == "नाम") return "सञ्ज्ञा"
    // प्रथमा सङ्ख्या: an ordinal before its noun only agrees with it; the noun carries the role.
    val text = relation.participant.pada.sourceText.replace(" ", "")
    if (at >= 0 && padas.getOrNull(at + 1) is dev.panini.vyakaranam.ast.SupBearingPada &&
        isOrdinalWord(text)) return "विशेषण"
    // अपवादः प्रक्रिया: a nominative left without a kāraka right before the procedure says what kind it is.
    if (at >= 0 && padas.getOrNull(at + 1)?.sourceText?.replace(" ", "") == "प्रक्रिया+सुँ") return "विशेषण"
    return relation.evidence.lastOrNull()?.sutra?.let(reasons::get)
}

/** The word to show for a resolved relation: its kāraka, or अकारक with the reason. Null while undecided. */
fun KriyaFrame.karakaLabel(relation: KarakaRelation): String? {
    val karaka = (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka ?: return null
    // दत्तसूच्याः एकैकं सदस्यम्: a genitive before the quantifier names the group selected from, 2.3.41.
    val at = vakya.padas.indexOfFirst { it === relation.participant.pada }
    val next = vakya.padas.getOrNull(at + 1)?.sourceText?.replace(" ", "").orEmpty()
    if (karaka == Karaka.SAMBANDHA && at >= 0 && (next.startsWith("एकैक+") || next.startsWith("प्रत्येक+"))) return "${karaka.sanskritName}: निर्धारण"
    return when (val reason = akarakaReason(relation)) {
        null -> karaka.sanskritName
        // These agree with the subject and share its role through it.
        "सञ्ज्ञा" -> NonKriyaWords.KARTR_NAME
        "विशेषण" -> NonKriyaWords.KARTR_QUALIFIER
        "विधेय" -> NonKriyaWords.KARTR_PREDICATE
        else -> "${karaka.sanskritName}: $reason"
    }
}
