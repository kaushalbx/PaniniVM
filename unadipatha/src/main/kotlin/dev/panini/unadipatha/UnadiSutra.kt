package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraSource

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
    val sutraId: SutraId get() = SutraId(number)

    val source: SutraSource get() = SutraSource.Ashtadhyayi(number, text)

    val artha: SutraArtha by lazy {
        SutraArtha(
            kind = "unadi",
            fields = buildMap {
                put("number", SutraArthaValue.Text(number))
                put("text", SutraArthaValue.Text(text))
                put(
                    "roots",
                    SutraArthaValue.Sequence(
                        roots.map {
                            SutraArthaValue.Text(it.upadesha.ifEmpty { it.sourceSurface })
                        },
                    ),
                )
                put("pratyaya", SutraArthaValue.Text(pratyaya))
                put("pratyayaSurface", SutraArthaValue.Text(pratyayaSurface))
                put(
                    "itMarkers",
                    SutraArthaValue.Sequence(
                        itMarkers.map {
                            SutraArthaValue.Symbol(it.name)
                        },
                    ),
                )
                put(
                    "baseSamjnas",
                    SutraArthaValue.Sequence(
                        baseSamjnas.map {
                            SutraArthaValue.Symbol(it::class.simpleName ?: it.toString())
                        },
                    ),
                )
                put("meaning", SutraArthaValue.Symbol(meaning::class.simpleName ?: "Karaka"))
                if (rootSamjnaMap.isNotEmpty()) {
                    put(
                        "rūḍhiOutputs",
                        SutraArthaValue.Record(
                            rootSamjnaMap.mapValues {
                                SutraArthaValue.Text(it.value.word)
                            },
                        ),
                    )
                }
            },
        )
    }

    fun toBlueprint(): SutraBlueprint = SutraBlueprint(
        id = sutraId,
        source = source,
        role = SutraRole.Vidhi,
        artha = artha,
        relations = emptySet(),
        governance = SutraGovernance(),
    )

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
