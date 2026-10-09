package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Projects an agreeing ordinal object phrase onto the existing retrieval frame.
 * The projected locative is internal IR-facing morphology, not required source syntax.
 */
object OrdinalObjectLowering {
    fun expand(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.GRAH ||
            sentence.tinganta.upasargas.isNotEmpty()) return null
        val head = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.MULYA &&
                it.sup.text == SupAffix.AM.upadesha
        } ?: return null
        val ordinals = sentence.padas.filter { PuranaPratyayaResolver.ordinalValue(it) != null }
        if (ordinals.isEmpty()) return null
        require((head.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Ordinal retrieval requires an underived value object; derived nominals need their own semantics."
        }
        require(ordinals.size == 1) { "Ordinal retrieval requires one unambiguous ordinal modifier." }
        val ordinal = ordinals.single()
        val sup = when (ordinal) {
            is SubantaPada -> ordinal.sup
            is SankhyaPuranaPada -> ordinal.sup
            else -> return null
        }
        // Locative position frames remain supported independently of object phrases.
        if (sup.text == SupAffix.NGI.upadesha) return null
        require(sup.text == head.sup.text) { "The ordinal must agree with its singular accusative object." }
        val headGenders = dev.panini.execution.binding.NominalGenderAgreement.genders(head)
        require(dev.panini.core.Linga.NAPUMSAKA in headGenders) {
            "The value object must retain its neuter lexical gender."
        }
        if (ordinal is SubantaPada) {
            require(dev.panini.execution.binding.NominalGenderAgreement.compatible(ordinal, head)) {
                "The ordinal gender must agree with its value object."
            }
        }
        val source = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            it !== head && it !== ordinal && SupAffix.candidates(it.sup.text).any { affix ->
                affix.vibhakti == dev.panini.core.Vibhakti.SASTHI ||
                    affix.vibhakti == dev.panini.core.Vibhakti.PANCHAMI
            }
        } ?: return null
        require(sentence.padas.size == 4) { "Ordinal retrieval requires one whole, ordinal, object, and verb." }
        require(source.sup.text in setOf(SupAffix.NGAS.upadesha, SupAffix.NGASI.upadesha)) {
            "Ordinal retrieval requires one singular source referent."
        }
        val locative = SupPratyaya("ङि", "ङि")
        val projectedOrdinal = when (ordinal) {
            is SubantaPada -> ordinal.copy(sup = locative,
                sourceText = "${ordinal.pratipadika.sourceText} + ङि")
            is SankhyaPuranaPada -> ordinal.copy(sup = locative,
                sourceText = ordinal.stems.joinToString(" + ") + " + ङि")
            else -> error("Unsupported ordinal")
        }
        val projected = sentence.padas.map {
            when {
                it === ordinal -> projectedOrdinal
                it === source -> source.copy(sup = SupPratyaya("ङसिँ", "ङसिँ"),
                    sourceText = "${source.pratipadika.sourceText} + ङसिँ")
                else -> it
            }
        }
        return Sequence(node.sourceText, listOf(Invocation(sentence.copy(padas = projected))), emptyList())
    }
}
