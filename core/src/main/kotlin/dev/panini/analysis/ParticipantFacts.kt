package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.Pada

/** Semantic and morphological properties of an individual nominal participant in a sentence. */
data class ParticipantFacts(
    val id: String,
    val expression: Pada,
    val possibleVibhaktis: Set<Vibhakti>,
    val semanticRelations: Set<SemanticRelation> = emptySet(),
    val linga: Set<Linga> = emptySet(),
    val vacana: Vacana? = null,
    val categories: Set<NominalCategory> = emptySet(),
    val semanticEvidence: Set<SemanticRelationEvidence> = emptySet(),
    /** Explicit governing-affix evidence for the prohibition in 2.3.69. */
    val isKrtProhibitedForSasthi: Boolean = false,
    val krtGenitiveProhibition: KrtGenitiveProhibition? = null,
    /** Established nominal dependency; a noun elsewhere in the clause is insufficient. */
    val governingNominal: dev.panini.vyakaranam.ast.SubantaPada? = null,
    val taddhitaLopa: TaddhitaLopaEvidence? = null,
    val vedicCaseLicense: VedicCaseLicense? = null,
    val derivationExpression: DerivationExpression? = null,
    /** Established dependency on an expressed counting-frequency pada, not inferred repetition. */
    val frequencyGovernor: dev.panini.vyakaranam.ast.AvyayaPada? = null,
    val intendedObject: IntendedObject? = null,
    /** Established dependency on an actual indeclinable occurrence in the clause. */
    val governingAvyaya: dev.panini.vyakaranam.ast.AvyayaPada? = null,
    val purposeGovernor: PurposeActionGovernor? = null,
)
