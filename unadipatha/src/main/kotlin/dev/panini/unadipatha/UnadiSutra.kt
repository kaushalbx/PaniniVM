package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.Dhatu
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

/**
 * Declarative Base class for all Uṇādi Sūtras.
 * Representing Kṛt-pratyaya assignment rules under Aṣṭādhyāyī 3.3.1 (उणादयो बहुलम्).
 */
abstract class UnadiSutra(
    val number: String,
    val text: String,
    val roots: Set<Dhatu>,
    val pratyaya: String,
    val pratyayaSurface: String,
    val itMarkers: Set<ItMarker> = emptySet(),
    val rootSamjnaMap: Map<String, Samjna.Rudhi> = emptyMap(),
    val baseSamjnas: Set<Samjna> = setOf(
        Samjna.Affix.KRT,
        Samjna.Unit.PRATIPADIKA,
        Samjna.Karaka.KARTA
    ),
    val meaning: Artha = Artha.Karaka.KARTA,
    val hindiExplanation: String? = null
) {
    /**
     * Checks if this Uṇādi sūtra matches a given root.
     */
    fun matchesRoot(dhatu: Dhatu): Boolean {
        return roots.any {
            it::class == dhatu::class ||
            it.sourceSurface == dhatu.sourceSurface ||
            it.upadesha == dhatu.upadesha ||
            (it.surfaceAliases.isNotEmpty() && (it.surfaceAliases.contains(dhatu.sourceSurface) || it.surfaceAliases.contains(dhatu.upadesha)))
        }
    }

    /**
     * Creates an UnadiMatch for a matching root.
     */
    fun matchFor(dhatu: Dhatu): UnadiMatch {
        val specificRudhi = rootSamjnaMap[dhatu.upadesha]
            ?: rootSamjnaMap[dhatu.sourceSurface]
            ?: rootSamjnaMap.entries.firstOrNull { (k, _) -> dhatu.upadesha.startsWith(k) || dhatu.sourceSurface.startsWith(k) }?.value
        val samjnas = if (specificRudhi != null) baseSamjnas + specificRudhi else baseSamjnas
        return UnadiMatch(
            sutraNumber = number,
            sutraText = text,
            dhatu = dhatu,
            pratyaya = pratyaya,
            pratyayaSurface = pratyayaSurface,
            itMarkers = itMarkers,
            samjnas = samjnas,
            meaning = meaning,
            hindiExplanation = hindiExplanation
        )
    }
}
