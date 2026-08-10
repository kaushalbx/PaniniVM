package dev.panini.vyakaranam.ast

import dev.panini.core.Lakara
import dev.panini.core.SupLopa

sealed interface VyakaranamNode {
    val sourceText: String
}

data class Ukti(
    override val sourceText: String,
    val sambodhana: Sambodhana? = null,
    val body: ProgramNode,
) : VyakaranamNode {
    /** Explicit grammatical query; execution order must be read from [body]. */
    fun grammaticalVakyas(): List<Vakya> = body.invocations().map(Invocation::vakya)
}

/**
 * Uniform, recursive representation of executable structure.
 *
 * Grammatical nodes such as [Vakya] remain leaves. Control-flow consumers must
 * inspect this tree instead of deriving structure from clause positions or text.
 */
sealed interface ProgramNode : VyakaranamNode

data class Invocation(
    val vakya: Vakya,
) : ProgramNode {
    override val sourceText: String = vakya.sourceText
}

data class Sequence(
    override val sourceText: String,
    val statements: List<ProgramNode>,
    val connectors: List<String> = emptyList(),
) : ProgramNode {
    init {
        require(statements.isNotEmpty()) { "A sequence must contain at least one statement." }
        require(connectors.size <= statements.size - 1) {
            "A sequence cannot have more connectors than statement boundaries."
        }
    }
}

data class Conditional(
    override val sourceText: String,
    val condition: ProgramNode,
    val consequent: ProgramNode,
    val alternate: ProgramNode? = null,
) : ProgramNode

/** A command mentioned with इति and supplied as data to a reporting command. */
data class Quotation(
    override val sourceText: String,
    val quoted: Invocation,
    val reporting: ProgramNode,
) : ProgramNode

data class Repeat(
    override val sourceText: String,
    val count: Int,
    val body: ProgramNode,
) : ProgramNode {
    init {
        require(count > 0) { "A repetition count must be positive." }
    }
}

/** A condition-controlled loop, optionally bounded by a Sanskrit repetition count. */
data class WhileLoop(
    override val sourceText: String,
    val condition: Invocation,
    val body: ProgramNode,
    val maximumIterationStems: List<String> = emptyList(),
) : ProgramNode

data class PipelineStage(
    override val sourceText: String,
    val domainStem: String?,
    val operationStem: String,
) : VyakaranamNode

data class Pipeline(
    override val sourceText: String,
    val arguments: List<String>,
    val stages: List<PipelineStage>,
    val renderPadas: List<Pada> = emptyList(),
) : ProgramNode

data class ProcedureModifiers(
    val isInternal: Boolean = false,
    val isApavada: Boolean = false,
    val isAntaranga: Boolean = false,
    val isNitya: Boolean = false,
)

data class Procedure(
    override val sourceText: String,
    val name: String,
    val domain: String? = null,
    val body: List<ProgramNode>,
    val modifiers: ProcedureModifiers = ProcedureModifiers(),
) : ProgramNode

data class Scope(
    override val sourceText: String,
    val domain: String,
    val body: List<ProgramNode> = emptyList(),
) : ProgramNode

fun ProgramNode.invocations(): List<Invocation> =
    depthFirst().filterIsInstance<Invocation>().toList()

/** Invocations in execution order, including copies introduced by [Repeat]. */
fun ProgramNode.expandedInvocations(): List<Invocation> =
    depthFirst(expandRepeats = true).filterIsInstance<Invocation>().toList()

data class Sambodhana(
    override val sourceText: String,
    val suchaka: String?,
    val subanta: SubantaPada,
) : VyakaranamNode

sealed interface Vakya : VyakaranamNode {
    val padas: List<Pada>
}

data class AkhyataVakya(
    override val sourceText: String,
    override val padas: List<Pada>,
    val tinganta: TingantaPada,
) : Vakya

data class NamaVakya(
    override val sourceText: String,
    override val padas: List<Pada>,
) : Vakya

sealed interface Pada : VyakaranamNode

data class SubantaPada(
    override val sourceText: String,
    val pratipadika: Pratipadika,
    val sup: SupPratyaya,
) : Pada

data class TingantaPada(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val lakara: Lakara,
    val ting: TingPratyaya,
) : Pada

data class AvyayaPada(
    override val sourceText: String,
    val form: String,
    val derivation: AvyayaDerivation? = null,
) : Pada {
    val function: AvyayaFunction? = AvyayaFunction.fromForm(form)
}

enum class AvyayaFunction {
    NISHEDHA,
    QUOTATIVE,
    REPETITION,
    ;

    companion object {
        fun fromForm(form: String): AvyayaFunction? = when (form.trim()) {
            "न", "मा" -> NISHEDHA
            "इति" -> QUOTATIVE
            "पुनः", "पुनर्" -> REPETITION
            else -> null
        }
    }
}

data class SamuccitaSubanta(
    override val sourceText: String,
    val members: List<SubantaPada>,
) : Pada

sealed interface Pratipadika : VyakaranamNode

data class MulaPratipadika(
    override val sourceText: String,
    val text: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika {
    val lexicalIdentity: MulaPratipadikaIdentity? = MulaPratipadikaIdentity.fromText(text)
}

enum class MulaPratipadikaIdentity {
    ADHIKARA,
    ANTARANGA,
    APAVADA,
    NITYA,
    PURVA,
    SAMJNA,
    SAMAVAYA,
    ;

    companion object {
        fun fromText(text: String): MulaPratipadikaIdentity? = when (normalize(text)) {
            "अधिकार" -> ADHIKARA
            "अन्तरङ्ग", "अन्तरङ्गा", "अन्तर् + अङ्ग" -> ANTARANGA
            "अपवाद" -> APAVADA
            "नित्य", "नि + त्य" -> NITYA
            "पूर्व" -> PURVA
            "संज्ञा" -> SAMJNA
            "समवाय" -> SAMAVAYA
            else -> null
        }

        private fun normalize(text: String): String =
            text.split('+').joinToString(" + ") { it.trim() }.trim()
    }
}

data class KridantaPratipadika(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val krtPratyaya: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika {
    val krtPratyayaIdentity: KrtPratyayaIdentity? = KrtPratyayaIdentity.fromUpadesha(krtPratyaya)
    val lexicalIdentity: KridantaLexicalIdentity? = KridantaLexicalIdentity.fromStructure(
        upasargas = upasargas,
        mulaDhatu = dhatu.mulaDhatu,
        krtPratyaya = krtPratyayaIdentity,
    )
}

enum class KridantaLexicalIdentity {
    ADHIKARA,
    APAVADA,
    ;

    companion object {
        fun fromStructure(
            upasargas: List<String>,
            mulaDhatu: String,
            krtPratyaya: KrtPratyayaIdentity?,
        ): KridantaLexicalIdentity? = when {
            upasargas == listOf("अधि") &&
                mulaDhatu == "कृ" &&
                krtPratyaya == KrtPratyayaIdentity.GHAN -> ADHIKARA
            upasargas == listOf("अप") &&
                mulaDhatu == "वद्" &&
                krtPratyaya == KrtPratyayaIdentity.GHAN -> APAVADA
            else -> null
        }
    }
}

enum class KrtPratyayaIdentity {
    KTA,
    GHAN,
    ;

    companion object {
        fun fromUpadesha(upadesha: String): KrtPratyayaIdentity? = when (upadesha.trim()) {
            "क्त" -> KTA
            "घञ्" -> GHAN
            else -> null
        }
    }
}

data class UnadyantaPratipadika(
    override val sourceText: String,
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val unadiPratyaya: String,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika

data class SamasaPratipadika(
    override val sourceText: String,
    val angas: List<SamasaAnga>,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika

data class SankhyaPratipadika(
    override val sourceText: String,
    val value: Long? = null,
    val vikaras: List<PratipadikaVikara> = emptyList(),
) : Pratipadika

data class SankhyaPada(
    override val sourceText: String,
    val stems: List<String>,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class SankhyaPuranaPada(
    override val sourceText: String,
    val stems: List<String>,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class SankhyaAbhyasaPada(
    override val sourceText: String,
    val stems: List<String>,
    val value: Long? = null,
) : Pada

data class KatapayadiPada(
    override val sourceText: String,
    val word: String,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class AryabhatiyaPada(
    override val sourceText: String,
    val word: String,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class BhutasamkhyaPada(
    override val sourceText: String,
    val terms: List<String>,
    val value: Long? = null,
    val sup: SupPratyaya,
) : Pada

data class SamasaAnga(
    override val sourceText: String,
    val pratipadika: Pratipadika,
    val sup: SupPratyaya? = null,
    val supLopa: SupLopa? = null,
) : VyakaranamNode

sealed interface PratipadikaVikara : VyakaranamNode

enum class TaddhitaPratyayaClass {
    POSSESSIVE,
    APATYA,
    ;

    companion object {
        fun fromUpadesha(upadesha: String): TaddhitaPratyayaClass? = when (upadesha.trim()) {
            "मतुप्", "वतुप्", "मत्", "वत्" -> POSSESSIVE
            "अण्", "इञ्" -> APATYA
            else -> null
        }
    }
}

data class TaddhitaVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara {
    val pratyayaClass: TaddhitaPratyayaClass? = TaddhitaPratyayaClass.fromUpadesha(pratyaya)
}

data class StriVikara(
    override val sourceText: String,
    val pratyaya: String,
) : PratipadikaVikara

data class DhatuPrakriti(
    override val sourceText: String,
    val mulaDhatu: String,
    val sanadiPratyayas: List<String> = emptyList(),
) : VyakaranamNode

data class SupPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode

data class TingPratyaya(
    override val sourceText: String,
    val text: String,
) : VyakaranamNode

sealed interface AvyayaDerivation

data class AvyayaKridantaDerivation(
    val upasargas: List<String>,
    val dhatu: DhatuPrakriti,
    val pratyaya: String,
) : AvyayaDerivation

data class AvyayaTaddhitaDerivation(
    val pratipadika: String,
    val pratyaya: String,
) : AvyayaDerivation

data class AvyayibhavaDerivation(
    val samasa: SamasaPratipadika,
) : AvyayaDerivation

data class SankhyaAvyayaDerivation(
    val kind: String, // "ADHIKA", "UNA", "KRITVAS", "DHA", "SHAS"
    val stems: List<String> = emptyList(),
) : AvyayaDerivation
