package dev.panini.vyakaranam.ast

import dev.panini.core.KrtAffix
import dev.panini.core.Vibhakti

/** Written ktvā/lyap ownership, without operation selection or implicit values. */
object PriorActionStructure {
    data class Diagnostic(val code: String, val message: String)
    sealed interface Analysis {
        data object NotPriorAction : Analysis
        data class Resolved(val sequence: Sequence) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }
    private class InvalidStructure(val code: String, message: String) : IllegalArgumentException(message)
    private fun requireStructure(value: Boolean, code: String, message: String) {
        if (!value) throw InvalidStructure(code, message)
    }

    fun analyze(node: Invocation, shareOmittedAgents: Boolean = false,
        establishedFiniteAgents: List<SupBearingPada> = emptyList()): Analysis = try {
        split(node, shareOmittedAgents, establishedFiniteAgents)?.let(Analysis::Resolved) ?: Analysis.NotPriorAction
    } catch (error: InvalidStructure) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty()))
    }

    fun nominatives(padas: List<Pada>): List<Pada> {
        fun nominative(pada: SupBearingPada) = pada.sup.affixCandidates.let { candidates ->
            candidates.isNotEmpty() && candidates.all { it.vibhakti == Vibhakti.PRATHAMA }
        }
        return padas.filter { pada -> when (pada) {
            is SupBearingPada -> nominative(pada)
            is SamuccitaSubanta -> pada.members.isNotEmpty() && pada.members.all(::nominative)
            else -> false
        } }
    }

    /** A hypothesis for semantic checking, never a resolved ownership assignment. */
    fun passiveOwnershipCandidate(node: Invocation): AkhyataVakya? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.vikarana != Vikarana.YAK) return null
        val boundary = sentence.padas.indexOfLast {
            ((it as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation)?.krtAffix in setOf(KrtAffix.KTVA, KrtAffix.LYAP)
        }
        if (boundary < 0) return null
        val preposed = sentence.padas.take(boundary).filter {
            nominatives(listOf(it)).isNotEmpty() || hasInstrumentalReading(it)
        }
        return AkhyataVakya(sentence.sourceText, preposed + sentence.padas.drop(boundary + 1), sentence.tinganta)
    }

    private fun hasInstrumentalReading(pada: Pada): Boolean = when (pada) {
        is SupBearingPada -> pada.sup.affixCandidates.any { it.vibhakti == Vibhakti.TRTIYA }
        is SamuccitaSubanta -> pada.members.any(::hasInstrumentalReading)
        else -> false
    }

    private fun split(node: Invocation, shareOmittedAgents: Boolean,
        establishedFiniteAgents: List<SupBearingPada>): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.priorAction != null) return null
        val padas = sentence.padas
        val boundaries = padas.indices.filter { index ->
            val derivation = (padas[index] as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation
            derivation?.krtAffix in setOf(KrtAffix.KTVA, KrtAffix.LYAP)
        }
        if (boundaries.isEmpty()) return null
        val passiveMain = sentence.tinganta.vikarana == Vikarana.YAK
        requireStructure(!passiveMain || !shareOmittedAgents, "PRIOR_ACTION_PASSIVE_AGENT_SHARING",
            "Passive agents cannot be shared by copying nominative case forms.")
        fun established(pada: Pada): Boolean = when (pada) {
            is SupBearingPada -> establishedFiniteAgents.any { it === pada }
            is SamuccitaSubanta -> pada.members.isNotEmpty() && pada.members.all { member -> establishedFiniteAgents.any { it === member } }
            else -> false
        }
        val preposedAgents = if (passiveMain) padas.take(boundaries.last()).filter(::established) else emptyList()
        requireStructure(!passiveMain || padas.take(boundaries.last()).none { hasInstrumentalReading(it) && !established(it) },
            "PRIOR_ACTION_PASSIVE_PREPOSED_INSTRUMENTAL",
            "An instrumental before the prior-action boundary needs an independently established finite-agent reading; ownership remains unresolved.")
        boundaries.forEach { index ->
            val derivation = (padas[index] as AvyayaPada).derivation as AvyayaKridantaDerivation
            requireStructure(derivation.krtAffix != KrtAffix.LYAP || derivation.upasargas.isNotEmpty(),
                "PRIOR_ACTION_LYAP_REQUIRES_PREFIX", "ल्यप्-प्रयोगे पूर्वपदम् अपेक्षितम्।")
        }
        requireStructure(boundaries.last() < padas.indexOf(sentence.tinganta), "PRIOR_ACTION_ORDER",
            "पूर्वकालक्रिया मुख्यक्रियायाः पूर्वम् अपेक्षिता।")
        val groups = mutableListOf<List<Pada>>()
        var start = 0
        boundaries.forEach { index -> groups += padas.subList(start, index + 1); start = index + 1 }
        groups += padas.drop(start)
        fun identities(subjects: List<Pada>) = subjects.flatMap { pada -> when (pada) {
            is SupBearingPada -> listOf(pada)
            is SamuccitaSubanta -> pada.members
            else -> emptyList()
        } }.map { member ->
            val identity: Any = when (member) {
                is SubantaPada -> member.pratipadika.morphologicalKey()
                is SankhyaPuranaPada -> member.stems to member.striAffix
                is SankhyaPada -> member.stems
                else -> member
            }
            identity to member.sup.affixCandidates
        }.groupingBy { it }.eachCount()
        val declaredNominatives = groups.map(::nominatives).filter { it.isNotEmpty() }
        val sharedSubjects = declaredNominatives.firstOrNull().orEmpty()
        val writtenNominatives = declaredNominatives.flatten()
        val sharedIdentities = identities(sharedSubjects)
        requireStructure(passiveMain || declaredNominatives.all { identities(it) == sharedIdentities }, "PRIOR_ACTION_AGENT_IDENTITY",
            "क्त्वा-ल्यप्क्रिययोः मुख्यक्रियायाश्च समानः कर्ता अपेक्षितः।")
        val statements = groups.mapIndexed { index, group ->
            val head = if (index == groups.lastIndex) sentence.tinganta else {
                val avyaya = group.last() as AvyayaPada
                val derivation = avyaya.derivation as AvyayaKridantaDerivation
                // Internal analysis/binding projection. The original nonfinite
                // derivation and its span identify the head; no source tense is claimed.
                sentence.tinganta.copy(sourceText = avyaya.sourceText,
                    upasargas = derivation.upasargas, dhatu = derivation.dhatu,
                    vikarana = null, priorAction = derivation).locatedAt(avyaya.sourceSpan)
            }
            val operands = if (index == groups.lastIndex) group else group.dropLast(1) + head
            val boundPadas = if (shareOmittedAgents) {
                if (nominatives(group).isEmpty()) sharedSubjects + operands else operands
            } else if (index == groups.lastIndex) {
                // The finite action licenses the nominative: an active controller
                // or passive patient, even when it is written before ktvā.
                // Shared agents are linked after their actual roles are analyzed.
                (writtenNominatives + preposedAgents).filter { subject -> operands.none { it === subject } } + operands
            } else {
                val controllers = nominatives(group)
                operands.filterNot { pada -> (controllers + preposedAgents).any { it === pada } }
            }
            Invocation(AkhyataVakya(group.joinToString(" ") { it.sourceText }, boundPadas, head))
        }
        return Sequence(node.sourceText, statements, List(statements.size - 1) { SequenceConnector.PURVAKALA.surface })
    }
}
