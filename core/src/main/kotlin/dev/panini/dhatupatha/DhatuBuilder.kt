package dev.panini.dhatupatha

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Small DSL used by each gaṇa file to preserve Dhātupāṭha order. */
class DhatuBuilder(private val gana: DhatuGana) {
    private val dhatus = mutableListOf<Dhatu>()

    fun dhatu(
        id: String,
        krama: Int,
        upadesha: String,
        surface: String,
        artha: String,
        arthaHindi: String,
        arthaEnglish: String,
        pada: PadaType? = null,
        itStatus: ItStatus? = null,
        karmatva: Karmatva? = null,
        svara: Accent? = null,
        semanticRelations: Set<SemanticRelation> = emptySet(),
        surfaceAliases: Set<String> = emptySet(),
    ) {
        require(id.isNotBlank()) { "A dhātu id is required." }
        require(krama > 0) { "Dhātu krama must be positive." }
        require(upadesha.isNotBlank()) { "Dhātu upadeśa is required." }
        require(dhatus.none { it.id == id }) { "Duplicate dhātu id in $gana: $id" }
        require(dhatus.none { it.krama == krama }) { "Duplicate dhātu krama in $gana: $krama" }
        dhatus += Dhatu(
            id = id,
            krama = krama,
            upadesha = upadesha,
            sourceSurface = surface,
            artha = artha,
            arthaHindi = arthaHindi,
            arthaEnglish = arthaEnglish,
            gana = gana,
            pada = pada,
            itStatus = itStatus,
            karmatva = karmatva,
            svara = svara,
            semanticRelations = semanticRelations,
            surfaceAliases = surfaceAliases,
        )
    }

    /** Adds a specialized executable dhātu while preserving gaṇa invariants. */
    fun dhatu(dhatu: Dhatu) {
        require(dhatu.gana == gana) { "Dhātu ${dhatu.id} belongs to ${dhatu.gana}, not $gana." }
        require(dhatus.none { it.id == dhatu.id }) { "Duplicate dhātu id in $gana: ${dhatu.id}" }
        require(dhatus.none { it.krama == dhatu.krama }) { "Duplicate dhātu krama in $gana: ${dhatu.krama}" }
        dhatus += dhatu
    }

    fun build(): List<Dhatu> = dhatus.toList()
}

fun dhatuPatha(gana: DhatuGana, entries: DhatuBuilder.() -> Unit): List<Dhatu> =
    DhatuBuilder(gana).apply(entries).build()
