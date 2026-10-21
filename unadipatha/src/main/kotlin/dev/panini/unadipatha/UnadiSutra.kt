package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.core.SanadiAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraSource

/** An explicit phonological replacement prescribed by an Uṇādi sūtra. */
data class UnadiVarnaSubstitution(
    val source: Varna,
    val replacement: Varna,
)

/** Replacement of one varṇa by an ordered sequence of varṇas. */
data class UnadiVarnaExpansion(
    val source: Varna,
    val replacement: List<Varna>,
)

/** An explicit deletion (lopa) of a varṇa prescribed by an Uṇādi sūtra. */
data class UnadiVarnaLopa(val target: Varna)

/** Lopa of one contiguous, explicitly prescribed varṇa sequence. */
data class UnadiVarnaSequenceLopa(val target: List<Varna>)

enum class UnadiAgamaPlacement { AFTER_FINAL_ROOT_VOWEL, BEFORE_SUFFIX }

/** A phonological augment, retaining both its varṇas and its prescribed insertion site. */
data class UnadiVarnaAgama(
    val varnas: List<Varna>,
    val placement: UnadiAgamaPlacement,
    /** Source upadeśa when it contains pronunciation-only or it material. */
    val upadesha: String? = null,
)

/** A simultaneous exchange of the positions of two explicitly named varṇas. */
data class UnadiVarnaViparyaya(
    val initial: Varna,
    val final: Varna,
)

/** Reduplication details explicitly prescribed for one root. */
data class UnadiReduplication(
    val explicitDve: Boolean = true,
    val abhyasaAgamaUpadesha: String? = null,
    val abhyasaAgamaVarnas: List<Varna> = emptyList(),
    val tiLopa: Boolean = false,
)

/** Operations restricted to one member of a multi-root sūtra. */
data class UnadiRootMorphology(
    val conditionedStem: List<Varna>? = null,
    val requiredSanadiAffixes: Set<SanadiAffix> = emptySet(),
    /** Sanādi affixes explicitly removed by luk/lopa in this Uṇādi formation. */
    val lopaSanadiAffixes: Set<SanadiAffix> = emptySet(),
    /** Upasargas required only for this root in a multi-root rule. */
    val requiredUpasargas: Set<List<Varna>> = emptySet(),
    /** Upapadas required only for this root in a multi-root rule. */
    val requiredUpapadas: Set<List<Varna>> = emptySet(),
    val varnaAgamas: List<UnadiVarnaAgama> = emptyList(),
    val optionalVarnaAgamas: List<UnadiVarnaAgama> = emptyList(),
    val stemAdesha: List<Varna>? = null,
    val varnaSubstitutions: List<UnadiVarnaSubstitution> = emptyList(),
    val varnaExpansions: List<UnadiVarnaExpansion> = emptyList(),
    val varnaLopas: List<UnadiVarnaLopa> = emptyList(),
    val varnaSequenceLopas: List<UnadiVarnaSequenceLopa> = emptyList(),
    val optionalVarnaSubstitutions: List<UnadiVarnaSubstitution> = emptyList(),
    /** Substitutions applying to the audible suffix rather than to the root. */
    val pratyayaVarnaSubstitutions: List<UnadiVarnaSubstitution> = emptyList(),
    val reduplication: UnadiReduplication? = null,
    /** Accent prescribed only for this root/output branch. */
    val assignedAccent: Accent? = null,
    /** Zero-based vowel ordinal for [assignedAccent]. */
    val accentVowelIndex: Int? = null,
)

/** A suffix assignment restricted to one root when a single sūtra prescribes heterogeneous suffixes. */
data class UnadiRootPratyaya(
    val upadesha: String,
    val surface: String,
    val itMarkers: Set<ItMarker> = emptySet(),
)

/** Application of desiderative-like operations without adding the desiderative affix itself. */
data class UnadiSanvatTreatment(
    val explicitDve: Boolean = false,
    val blocksItva: Boolean = false,
)

/** A separately attested output of a rule whose result varies by prefix or construction. */
data class UnadiDerivedVariant(
    val upasarga: List<Varna>? = null,
    val output: List<Varna>,
    val meaning: Artha? = null,
    val upapada: List<Varna>? = null,
    /** Upadeśa of the source root when the commentary identifies one. */
    val sourceRoot: String? = null,
    /** Operations belonging specifically to this output branch. */
    val morphology: UnadiRootMorphology? = null,
)

/** A source-attested derivative formed from an Uṇādi output by a later affix. */
data class UnadiSecondaryDerivative(
    val base: List<Varna>,
    val affix: String,
    val output: List<Varna>,
    val meaning: Artha? = null,
)

/** A lexically attested form, including source-stated gender and cross-reference. */
data class UnadiLexicalAttestation(
    val form: List<Varna>,
    val lingas: Set<Linga> = emptySet(),
    val meaning: Artha? = null,
    val attributedSutra: String? = null,
)

/** Source-prescribed formation of an upapada before it conditions the root. */
data class UnadiUpapadaFormation(
    val source: List<Varna>,
    val output: List<Varna>,
    val initialVarnaLopas: List<Varna> = emptyList(),
    val finalVarnaLopas: List<Varna> = emptyList(),
)

/**
 * Declarative Base class for all Uṇādi Sūtras.
 * Representing Kṛt-pratyaya assignment rules under Aṣṭādhyāyī 3.3.1 (उणादयो बहुलम्).
 */
abstract class UnadiSutra(
    val number: String,
    val text: String,
    val roots: Set<Dhatu>,
    /** Root spellings stated by the sūtra but not yet represented in Dhātupāṭha. */
    val rootReferences: Set<String> = emptySet(),
    val pratyaya: String,
    val pratyayaSurface: String,
    val itMarkers: Set<ItMarker> = emptySet(),
    val rootPratyayaMap: Map<String, UnadiRootPratyaya> = emptyMap(),
    /** It-markers assigned only in the optional branch stated by the sūtra. */
    val optionalItMarkers: Set<ItMarker> = emptySet(),
    /** True when the rule or its cited governing rule explicitly blocks iṭ-āgama. */
    val blocksItAgama: Boolean = false,
    val rootSamjnaMap: Map<String, Samjna.Rudhi> = emptyMap(),
    /** A source-supported meaning for an individual root when the rule is semantically heterogeneous. */
    val rootMeaningMap: Map<String, Artha> = emptyMap(),
    /** Semantic restriction stated by the rule, distinct from the resulting word's meaning. */
    val applicationMeaning: Artha? = null,
    /** Lexical upapadas required by this rule, represented as phonological sequences. */
    val upapadas: Set<List<Varna>> = emptySet(),
    /** Verbal prefixes explicitly required by this rule, represented as phonological sequences. */
    val upasargas: Set<List<Varna>> = emptySet(),
    val varnaSubstitutions: List<UnadiVarnaSubstitution> = emptyList(),
    val varnaExpansions: List<UnadiVarnaExpansion> = emptyList(),
    val varnaLopas: List<UnadiVarnaLopa> = emptyList(),
    val varnaSequenceLopas: List<UnadiVarnaSequenceLopa> = emptyList(),
    val varnaAgamas: List<UnadiVarnaAgama> = emptyList(),
    val varnaViparyayas: List<UnadiVarnaViparyaya> = emptyList(),
    val rootMorphology: Map<String, UnadiRootMorphology> = emptyMap(),
    val sanvatTreatment: UnadiSanvatTreatment? = null,
    val derivedVariants: List<UnadiDerivedVariant> = emptyList(),
    val secondaryDerivatives: List<UnadiSecondaryDerivative> = emptyList(),
    val lexicalAttestations: List<UnadiLexicalAttestation> = emptyList(),
    val relatedSutraNumbers: Set<String> = emptySet(),
    val upapadaFormations: List<UnadiUpapadaFormation> = emptyList(),
    /** True when the source explicitly prescribes the outputs by nipātana. */
    val isNipatita: Boolean = false,
    /** True when the source explicitly treats the listed forms as an open ākṛtigaṇa. */
    val isAkritiGana: Boolean = false,
    val assignedAccent: Accent? = null,
    /** Zero-based vowel ordinal; 0 represents आद्युदात्त. */
    val accentVowelIndex: Int? = null,
    val baseSamjnas: Set<Samjna> = setOf(
        Samjna.Affix.KRT,
        Samjna.Unit.PRATIPADIKA,
        Samjna.Karaka.KARTA
    ),
    val meaning: Artha = Artha.Karaka.KARTA,
    val hindiExplanation: String? = null,
) {
    val sutraId: SutraId get() = SutraId(number)

    /** Operative suffix as phonological units; empty while the surface is unresolved. */
    val pratyayaSurfaceVarnas: List<Varna> by lazy {
        if (pratyayaSurface.isEmpty()) emptyList() else pratyayaSurface.toVarnas()
    }

    /** Textual root references parsed as varṇas without inventing Dhātupāṭha metadata. */
    val rootReferenceVarnas: Map<String, List<Varna>> by lazy {
        rootReferences.associateWith(String::toVarnas)
    }

    /** Attested derived stems represented phonologically rather than inferred from spelling. */
    val rootOutputVarnas: Map<String, List<Varna>> by lazy {
        rootSamjnaMap.mapValues { (_, samjna) -> samjna.word.toVarnas() }
    }

    val source: SutraSource get() = SutraSource.Unadipatha(number, text)

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
                        } + rootReferences.map(SutraArthaValue::Text),
                    ),
                )
                put("pratyaya", SutraArthaValue.Text(pratyaya))
                if (pratyayaSurface.isNotEmpty()) {
                    put("pratyayaSurface", SutraArthaValue.Text(pratyayaSurface))
                }
                put(
                    "itMarkers",
                    SutraArthaValue.Sequence(
                        itMarkers.map {
                            SutraArthaValue.Symbol(it.name)
                        },
                    ),
                )
                if (rootPratyayaMap.isNotEmpty()) {
                    put(
                        "rootPratyayas",
                        SutraArthaValue.Record(
                            rootPratyayaMap.mapValues { (_, value) ->
                                SutraArthaValue.Record(
                                    mapOf(
                                        "upadeśa" to SutraArthaValue.Text(value.upadesha),
                                        "surface" to SutraArthaValue.Text(value.surface),
                                        "itMarkers" to SutraArthaValue.Sequence(
                                            value.itMarkers.map { SutraArthaValue.Symbol(it.name) },
                                        ),
                                    ),
                                )
                            },
                        ),
                    )
                }
                if (optionalItMarkers.isNotEmpty()) {
                    put(
                        "optionalItMarkers",
                        SutraArthaValue.Sequence(
                            optionalItMarkers.map { SutraArthaValue.Symbol(it.name) },
                        ),
                    )
                }
                if (blocksItAgama) put("blocksIṭĀgama", SutraArthaValue.Symbol("true"))
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
                if (rootMeaningMap.isNotEmpty()) {
                    put(
                        "rootMeanings",
                        SutraArthaValue.Record(
                            rootMeaningMap.mapValues { (_, value) ->
                                when (value) {
                                    is Artha.Rudhi -> SutraArthaValue.Text(value.devanagari)
                                    is Artha.Explanation -> SutraArthaValue.Text(value.hindi)
                                    else -> SutraArthaValue.Symbol(value.toString())
                                }
                            },
                        ),
                    )
                }
                applicationMeaning?.let {
                    put(
                        "applicationMeaning",
                        when (it) {
                            is Artha.Rudhi -> SutraArthaValue.Text(it.devanagari)
                            is Artha.Explanation -> SutraArthaValue.Text(it.hindi)
                            else -> SutraArthaValue.Symbol(it.toString())
                        },
                    )
                }
                if (upapadas.isNotEmpty()) {
                    put(
                        "upapadas",
                        SutraArthaValue.Sequence(
                            upapadas.map { varnas ->
                                SutraArthaValue.Sequence(varnas.map { SutraArthaValue.Text(it.devanagari) })
                            },
                        ),
                    )
                }
                if (upasargas.isNotEmpty()) {
                    put(
                        "upasargas",
                        SutraArthaValue.Sequence(
                            upasargas.map { varnas ->
                                SutraArthaValue.Sequence(varnas.map { SutraArthaValue.Text(it.devanagari) })
                            },
                        ),
                    )
                }
                if (varnaSubstitutions.isNotEmpty()) {
                    put(
                        "varṇaSubstitutions",
                        SutraArthaValue.Sequence(
                            varnaSubstitutions.map {
                                SutraArthaValue.Record(
                                    mapOf(
                                        "source" to SutraArthaValue.Text(it.source.devanagari),
                                        "replacement" to SutraArthaValue.Text(it.replacement.devanagari),
                                    ),
                                )
                            },
                        ),
                    )
                }
                if (varnaExpansions.isNotEmpty()) {
                    put(
                        "varṇaExpansions",
                        SutraArthaValue.Sequence(
                            varnaExpansions.map { expansion ->
                                SutraArthaValue.Record(
                                    mapOf(
                                        "source" to SutraArthaValue.Text(expansion.source.devanagari),
                                        "replacement" to SutraArthaValue.Sequence(
                                            expansion.replacement.map { SutraArthaValue.Text(it.devanagari) },
                                        ),
                                    ),
                                )
                            },
                        ),
                    )
                }
                if (varnaLopas.isNotEmpty()) {
                    put(
                        "varṇaLopas",
                        SutraArthaValue.Sequence(
                            varnaLopas.map {
                                SutraArthaValue.Record(
                                    mapOf("target" to SutraArthaValue.Text(it.target.devanagari)),
                                )
                            },
                        ),
                    )
                }
                if (varnaSequenceLopas.isNotEmpty()) {
                    put(
                        "varṇaSequenceLopas",
                        SutraArthaValue.Sequence(
                            varnaSequenceLopas.map { lopa ->
                                SutraArthaValue.Sequence(lopa.target.map { SutraArthaValue.Text(it.devanagari) })
                            },
                        ),
                    )
                }
                if (varnaAgamas.isNotEmpty()) {
                    put(
                        "varṇaĀgamas",
                        SutraArthaValue.Sequence(
                            varnaAgamas.map { agama ->
                                SutraArthaValue.Record(
                                    mapOf(
                                        "varṇas" to SutraArthaValue.Sequence(
                                            agama.varnas.map { SutraArthaValue.Text(it.devanagari) },
                                        ),
                                        "placement" to SutraArthaValue.Symbol(agama.placement.name),
                                    ),
                                )
                            },
                        ),
                    )
                }
                if (varnaViparyayas.isNotEmpty()) {
                    put(
                        "varṇaViparyayas",
                        SutraArthaValue.Sequence(
                            varnaViparyayas.map { viparyaya ->
                                SutraArthaValue.Record(
                                    mapOf(
                                        "initial" to SutraArthaValue.Text(viparyaya.initial.devanagari),
                                        "final" to SutraArthaValue.Text(viparyaya.final.devanagari),
                                    ),
                                )
                            },
                        ),
                    )
                }
                if (rootMorphology.isNotEmpty()) {
                    put(
                        "rootMorphology",
                        SutraArthaValue.Record(
                            rootMorphology.mapValues { (_, morphology) ->
                                SutraArthaValue.Record(
                                    buildMap {
                                        morphology.conditionedStem?.let { value -> put("conditionedStem", SutraArthaValue.Text(value.joinToString("") { it.devanagari })) }
                                        if (morphology.requiredSanadiAffixes.isNotEmpty()) {
                                            put("requiredSanadiAffixes", SutraArthaValue.Sequence(morphology.requiredSanadiAffixes.map { SutraArthaValue.Text(it.upadesha) }))
                                        }
                                        if (morphology.lopaSanadiAffixes.isNotEmpty()) {
                                            put("lopaSanadiAffixes", SutraArthaValue.Sequence(morphology.lopaSanadiAffixes.map { SutraArthaValue.Text(it.upadesha) }))
                                        }
                                        if (morphology.requiredUpasargas.isNotEmpty()) {
                                            put(
                                                "requiredUpasargas",
                                                SutraArthaValue.Sequence(
                                                    morphology.requiredUpasargas.map { value ->
                                                        SutraArthaValue.Text(value.joinToString("") { it.devanagari })
                                                    },
                                                ),
                                            )
                                        }
                                        if (morphology.requiredUpapadas.isNotEmpty()) {
                                            put(
                                                "requiredUpapadas",
                                                SutraArthaValue.Sequence(
                                                    morphology.requiredUpapadas.map { value ->
                                                        SutraArthaValue.Text(value.joinToString("") { it.devanagari })
                                                    },
                                                ),
                                            )
                                        }
                                        if (morphology.varnaAgamas.isNotEmpty()) {
                                            put(
                                                "āgamas",
                                                SutraArthaValue.Sequence(
                                                    morphology.varnaAgamas.map { agama ->
                                                        SutraArthaValue.Record(
                                                            buildMap {
                                                                agama.upadesha?.let { put("upadeśa", SutraArthaValue.Text(it)) }
                                                                put("varṇas", SutraArthaValue.Text(agama.varnas.joinToString("") { it.devanagari }))
                                                                put("placement", SutraArthaValue.Symbol(agama.placement.name))
                                                            },
                                                        )
                                                    },
                                                ),
                                            )
                                        }
                                        if (morphology.optionalVarnaAgamas.isNotEmpty()) {
                                            put(
                                                "optionalĀgamas",
                                                SutraArthaValue.Sequence(
                                                    morphology.optionalVarnaAgamas.map { agama ->
                                                        SutraArthaValue.Record(
                                                            buildMap {
                                                                agama.upadesha?.let { put("upadeśa", SutraArthaValue.Text(it)) }
                                                                put("varṇas", SutraArthaValue.Text(agama.varnas.joinToString("") { it.devanagari }))
                                                                put("placement", SutraArthaValue.Symbol(agama.placement.name))
                                                            },
                                                        )
                                                    },
                                                ),
                                            )
                                        }
                                        morphology.stemAdesha?.let { value -> put("stemĀdeśa", SutraArthaValue.Text(value.joinToString("") { it.devanagari })) }
                                        if (morphology.varnaSubstitutions.isNotEmpty()) {
                                            put("substitutions", SutraArthaValue.Sequence(morphology.varnaSubstitutions.map { SutraArthaValue.Text("${it.source.devanagari}>${it.replacement.devanagari}") }))
                                        }
                                        if (morphology.varnaExpansions.isNotEmpty()) {
                                            put(
                                                "expansions",
                                                SutraArthaValue.Sequence(
                                                    morphology.varnaExpansions.map { expansion ->
                                                        SutraArthaValue.Text(
                                                            "${expansion.source.devanagari}>${expansion.replacement.joinToString("") { it.devanagari }}",
                                                        )
                                                    },
                                                ),
                                            )
                                        }
                                        if (morphology.varnaLopas.isNotEmpty()) {
                                            put(
                                                "lopas",
                                                SutraArthaValue.Sequence(
                                                    morphology.varnaLopas.map { SutraArthaValue.Text(it.target.devanagari) },
                                                ),
                                            )
                                        }
                                        if (morphology.varnaSequenceLopas.isNotEmpty()) {
                                            put(
                                                "sequenceLopas",
                                                SutraArthaValue.Sequence(
                                                    morphology.varnaSequenceLopas.map { lopa ->
                                                        SutraArthaValue.Text(lopa.target.joinToString("") { it.devanagari })
                                                    },
                                                ),
                                            )
                                        }
                                        if (morphology.optionalVarnaSubstitutions.isNotEmpty()) {
                                            put(
                                                "optionalSubstitutions",
                                                SutraArthaValue.Sequence(
                                                    morphology.optionalVarnaSubstitutions.map {
                                                        SutraArthaValue.Text("${it.source.devanagari}>${it.replacement.devanagari}")
                                                    },
                                                ),
                                            )
                                        }
                                        morphology.assignedAccent?.let {
                                            put("assignedAccent", SutraArthaValue.Symbol(it.name))
                                        }
                                        morphology.accentVowelIndex?.let {
                                            put("accentVowelIndex", SutraArthaValue.Number(it.toLong()))
                                        }
                                        if (morphology.pratyayaVarnaSubstitutions.isNotEmpty()) {
                                            put(
                                                "pratyayaSubstitutions",
                                                SutraArthaValue.Sequence(
                                                    morphology.pratyayaVarnaSubstitutions.map {
                                                        SutraArthaValue.Text("${it.source.devanagari}>${it.replacement.devanagari}")
                                                    },
                                                ),
                                            )
                                        }
                                        morphology.reduplication?.let { reduplication ->
                                            put(
                                                "reduplication",
                                                SutraArthaValue.Record(
                                                    buildMap {
                                                        put("explicitDve", SutraArthaValue.Symbol(reduplication.explicitDve.toString()))
                                                        reduplication.abhyasaAgamaUpadesha?.let {
                                                            put("abhyāsaĀgamaUpadeśa", SutraArthaValue.Text(it))
                                                        }
                                                        if (reduplication.abhyasaAgamaVarnas.isNotEmpty()) {
                                                            put(
                                                                "abhyāsaĀgamaVarnas",
                                                                SutraArthaValue.Sequence(
                                                                    reduplication.abhyasaAgamaVarnas.map { SutraArthaValue.Text(it.devanagari) },
                                                                ),
                                                            )
                                                        }
                                                        put("ṭiLopa", SutraArthaValue.Symbol(reduplication.tiLopa.toString()))
                                                    },
                                                ),
                                            )
                                        }
                                    },
                                )
                            },
                        ),
                    )
                }
                sanvatTreatment?.let { treatment ->
                    put(
                        "sanvatTreatment",
                        SutraArthaValue.Record(
                            mapOf(
                                "explicitDve" to SutraArthaValue.Symbol(treatment.explicitDve.toString()),
                                "blocksItva" to SutraArthaValue.Symbol(treatment.blocksItva.toString()),
                            ),
                        ),
                    )
                }
                if (derivedVariants.isNotEmpty()) {
                    put(
                        "derivedVariants",
                        SutraArthaValue.Sequence(
                            derivedVariants.map { variant ->
                                SutraArthaValue.Record(
                                    buildMap {
                                        variant.upasarga?.let { value -> put("upasarga", SutraArthaValue.Text(value.joinToString("") { it.devanagari })) }
                                        variant.upapada?.let { value -> put("upapada", SutraArthaValue.Text(value.joinToString("") { it.devanagari })) }
                                        variant.sourceRoot?.let { put("sourceRoot", SutraArthaValue.Text(it)) }
                                        put("output", SutraArthaValue.Text(variant.output.joinToString("") { it.devanagari }))
                                        variant.meaning?.let { value ->
                                            put(
                                                "meaning",
                                                when (value) {
                                                    is Artha.Rudhi -> SutraArthaValue.Text(value.devanagari)
                                                    is Artha.Explanation -> SutraArthaValue.Text(value.hindi)
                                                    else -> SutraArthaValue.Symbol(value.toString())
                                                },
                                            )
                                        }
                                        variant.morphology?.let { morphology ->
                                            put(
                                                "morphology",
                                                SutraArthaValue.Record(
                                                    buildMap {
                                                        morphology.conditionedStem?.let { value ->
                                                            put("conditionedStem", SutraArthaValue.Text(value.joinToString("") { it.devanagari }))
                                                        }
                                                        if (morphology.varnaAgamas.isNotEmpty()) {
                                                            put(
                                                                "āgamas",
                                                                SutraArthaValue.Sequence(
                                                                    morphology.varnaAgamas.map { agama ->
                                                                        SutraArthaValue.Record(
                                                                            buildMap {
                                                                                agama.upadesha?.let { put("upadeśa", SutraArthaValue.Text(it)) }
                                                                                put("varṇas", SutraArthaValue.Text(agama.varnas.joinToString("") { it.devanagari }))
                                                                                put("placement", SutraArthaValue.Symbol(agama.placement.name))
                                                                            },
                                                                        )
                                                                    },
                                                                ),
                                                            )
                                                        }
                                                        if (morphology.varnaSubstitutions.isNotEmpty()) {
                                                            put("substitutions", SutraArthaValue.Sequence(morphology.varnaSubstitutions.map { SutraArthaValue.Text("${it.source.devanagari}>${it.replacement.devanagari}") }))
                                                        }
                                                        if (morphology.varnaLopas.isNotEmpty()) {
                                                            put("lopas", SutraArthaValue.Sequence(morphology.varnaLopas.map { SutraArthaValue.Text(it.target.devanagari) }))
                                                        }
                                                        if (morphology.varnaSequenceLopas.isNotEmpty()) {
                                                            put("sequenceLopas", SutraArthaValue.Sequence(morphology.varnaSequenceLopas.map { lopa -> SutraArthaValue.Text(lopa.target.joinToString("") { it.devanagari }) }))
                                                        }
                                                        if (morphology.pratyayaVarnaSubstitutions.isNotEmpty()) {
                                                            put("pratyayaSubstitutions", SutraArthaValue.Sequence(morphology.pratyayaVarnaSubstitutions.map { SutraArthaValue.Text("${it.source.devanagari}>${it.replacement.devanagari}") }))
                                                        }
                                                    },
                                                ),
                                            )
                                        }
                                    },
                                )
                            },
                        ),
                    )
                }
                if (secondaryDerivatives.isNotEmpty()) {
                    put(
                        "secondaryDerivatives",
                        SutraArthaValue.Sequence(
                            secondaryDerivatives.map { derivative ->
                                SutraArthaValue.Record(
                                    buildMap {
                                        put("base", SutraArthaValue.Text(derivative.base.joinToString("") { it.devanagari }))
                                        put("affix", SutraArthaValue.Text(derivative.affix))
                                        put("output", SutraArthaValue.Text(derivative.output.joinToString("") { it.devanagari }))
                                        derivative.meaning?.let { value ->
                                            put(
                                                "meaning",
                                                when (value) {
                                                    is Artha.Rudhi -> SutraArthaValue.Text(value.devanagari)
                                                    is Artha.Explanation -> SutraArthaValue.Text(value.hindi)
                                                    else -> SutraArthaValue.Symbol(value.toString())
                                                },
                                            )
                                        }
                                    },
                                )
                            },
                        ),
                    )
                }
                if (lexicalAttestations.isNotEmpty()) {
                    put(
                        "lexicalAttestations",
                        SutraArthaValue.Sequence(
                            lexicalAttestations.map { attestation ->
                                SutraArthaValue.Record(
                                    buildMap {
                                        put("form", SutraArthaValue.Text(attestation.form.joinToString("") { it.devanagari }))
                                        if (attestation.lingas.isNotEmpty()) {
                                            put("lingas", SutraArthaValue.Sequence(attestation.lingas.map { SutraArthaValue.Symbol(it.name) }))
                                        }
                                        attestation.meaning?.let { value ->
                                            put(
                                                "meaning",
                                                when (value) {
                                                    is Artha.Rudhi -> SutraArthaValue.Text(value.devanagari)
                                                    is Artha.Explanation -> SutraArthaValue.Text(value.hindi)
                                                    else -> SutraArthaValue.Symbol(value.toString())
                                                },
                                            )
                                        }
                                        attestation.attributedSutra?.let { put("attributedSutra", SutraArthaValue.Text(it)) }
                                    },
                                )
                            },
                        ),
                    )
                }
                if (relatedSutraNumbers.isNotEmpty()) {
                    put("relatedSutras", SutraArthaValue.Sequence(relatedSutraNumbers.map(SutraArthaValue::Text)))
                }
                if (upapadaFormations.isNotEmpty()) {
                    put(
                        "upapadaFormations",
                        SutraArthaValue.Sequence(
                            upapadaFormations.map { formation ->
                                SutraArthaValue.Record(
                                    buildMap {
                                        put("source", SutraArthaValue.Text(formation.source.joinToString("") { it.devanagari }))
                                        put("output", SutraArthaValue.Text(formation.output.joinToString("") { it.devanagari }))
                                        if (formation.initialVarnaLopas.isNotEmpty()) {
                                            put(
                                                "initialVarṇaLopas",
                                                SutraArthaValue.Sequence(
                                                    formation.initialVarnaLopas.map { SutraArthaValue.Text(it.devanagari) },
                                                ),
                                            )
                                        }
                                        if (formation.finalVarnaLopas.isNotEmpty()) {
                                            put(
                                                "finalVarṇaLopas",
                                                SutraArthaValue.Sequence(
                                                    formation.finalVarnaLopas.map { SutraArthaValue.Text(it.devanagari) },
                                                ),
                                            )
                                        }
                                    },
                                )
                            },
                        ),
                    )
                }
                if (isNipatita) put("isNipātita", SutraArthaValue.Symbol("true"))
                if (isAkritiGana) put("isĀkṛtigaṇa", SutraArthaValue.Symbol("true"))
                assignedAccent?.let { put("assignedAccent", SutraArthaValue.Symbol(it.name)) }
                accentVowelIndex?.let { put("accentVowelIndex", SutraArthaValue.Number(it.toLong())) }
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
            it.id == dhatu.id ||
            (it.javaClass != Dhatu::class.java && it::class == dhatu::class) ||
            it.sourceSurface == dhatu.sourceSurface ||
            it.upadesha == dhatu.upadesha ||
            (it.surfaceAliases.isNotEmpty() && (it.surfaceAliases.contains(dhatu.sourceSurface) || it.surfaceAliases.contains(dhatu.upadesha)))
        } || rootReferences.any {
            it == dhatu.upadesha || it == dhatu.sourceSurface ||
                dhatu.upadesha.startsWith(it) || dhatu.sourceSurface.startsWith(it)
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
        val specificMeaning = rootMeaningMap[dhatu.upadesha]
            ?: rootMeaningMap[dhatu.sourceSurface]
            ?: rootMeaningMap.entries.firstOrNull { (key, _) ->
                dhatu.upadesha.startsWith(key) || dhatu.sourceSurface.startsWith(key)
            }?.value
            ?: meaning
        val specificPratyaya = rootPratyayaMap[dhatu.upadesha]
            ?: rootPratyayaMap[dhatu.sourceSurface]
            ?: rootPratyayaMap.entries.firstOrNull { (key, _) ->
                dhatu.upadesha.startsWith(key) || dhatu.sourceSurface.startsWith(key)
            }?.value
        return UnadiMatch(
            sutraNumber = number,
            sutraText = text,
            dhatu = dhatu,
            pratyaya = specificPratyaya?.upadesha ?: pratyaya,
            pratyayaSurface = specificPratyaya?.surface ?: pratyayaSurface,
            pratyayaSurfaceVarnas = (specificPratyaya?.surface ?: pratyayaSurface).let {
                if (it.isEmpty()) emptyList() else it.toVarnas()
            },
            derivedStemVarnas = specificRudhi?.word?.toVarnas(),
            itMarkers = specificPratyaya?.itMarkers ?: itMarkers,
            samjnas = samjnas,
            meaning = specificMeaning,
            hindiExplanation = hindiExplanation
        )
    }
}
