package dev.panini.execution

import dev.panini.core.KrtAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Shares prior-action ordering between interpreter binding and compiler lowering.
 * Source avyaya derivations remain intact. Finite heads below are internal binding
 * projections, not new source sentences or claims about the nonfinite word's tense.
 */
object PriorActionLowering {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object NotPriorAction : Analysis
        data class Resolved(val sequence: Sequence) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidPriorAction(val code: String, message: String) : IllegalArgumentException(message)

    private fun requirePrior(value: Boolean, code: String, message: () -> String) {
        if (!value) throw InvalidPriorAction(code, message())
    }

    fun lower(root: ProgramNode): ProgramNode = object : ProgramNodeTransformer() {
        override fun visitInvocation(node: Invocation): ProgramNode = expand(node) ?: node
    }.transform(root)

    fun analyze(node: Invocation): Analysis = try {
        expandInternal(node)?.let(Analysis::Resolved) ?: Analysis.NotPriorAction
    } catch (error: InvalidPriorAction) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), node.relationSpan(), node.sourceText))
    }

    fun expand(node: Invocation): Sequence? = when (val analysis = analyze(node)) {
        Analysis.NotPriorAction -> null
        is Analysis.Resolved -> analysis.sequence
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }

    private fun expandInternal(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.priorAction != null) return null
        val padas = sentence.padas
        val boundaries = padas.indices.filter { index ->
            val derivation = (padas[index] as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation
            derivation != null && derivation.krtAffix in setOf(KrtAffix.KTVA, KrtAffix.LYAP)
        }
        if (boundaries.isEmpty()) return null
        // Preserve the established range-exclusion construction until its
        // contextual semantics are represented as a general prior-action frame.
        val specialized = NaturalSemanticNormalizer.normalize(node)
        if (specialized is NaturalSemanticNormalizer.Operation.RangeChoice && specialized.usesExclusionAbsolutive) return null
        requirePrior(sentence.tinganta.vikarana != Vikarana.YAK, "PRIOR_ACTION_PASSIVE_MAIN_ACTION") {
            "पूर्वकालक्रियायाः कर्मणि-भावे मुख्यक्रियया सम्बन्धः अद्य न समर्थितः।"
        }
        boundaries.forEach { index ->
            val derivation = (padas[index] as AvyayaPada).derivation as AvyayaKridantaDerivation
            requirePrior(derivation.krtAffix != KrtAffix.LYAP || derivation.upasargas.isNotEmpty(),
                "PRIOR_ACTION_LYAP_REQUIRES_PREFIX") {
                "ल्यप्-प्रयोगे पूर्वपदम् अपेक्षितम्।"
            }
        }
        requirePrior(boundaries.last() < padas.indexOf(sentence.tinganta), "PRIOR_ACTION_ORDER") {
            "पूर्वकालक्रिया मुख्यक्रियायाः पूर्वम् अपेक्षिता।"
        }
        val groups = mutableListOf<List<Pada>>()
        var start = 0
        boundaries.forEach { index -> groups += padas.subList(start, index + 1); start = index + 1 }
        groups += padas.drop(start)
        fun isSubject(pada: dev.panini.vyakaranam.ast.SupBearingPada): Boolean {
            val candidates = pada.sup.affixCandidates
            return candidates.isNotEmpty() && candidates.all { it.vibhakti == Vibhakti.PRATHAMA }
        }
        // Preserve coordination when sharing an understood agent. Flattening it
        // would turn a dual/plural controller into unrelated singular nouns.
        fun subjects(group: List<Pada>): List<Pada> = group.filter { pada ->
            when (pada) {
                is dev.panini.vyakaranam.ast.SupBearingPada -> isSubject(pada)
                is SamuccitaSubanta -> pada.members.isNotEmpty() && pada.members.all(::isSubject)
                else -> false
            }
        }
        fun identities(subjects: List<Pada>) = subjects.flatMap { pada ->
            when (pada) {
                is dev.panini.vyakaranam.ast.SupBearingPada -> listOf(pada)
                is SamuccitaSubanta -> pada.members
                else -> emptyList()
            }
        // Derivational suffixes identify the participant too: a base noun and
        // a possessive/feminine derivative are not automatically coreferential.
        }.map { member ->
            val identity: Any = when (member) {
                is SubantaPada -> member.pratipadika.morphologicalKey()
                is dev.panini.vyakaranam.ast.SankhyaPuranaPada -> member.stems to member.striAffix
                is dev.panini.vyakaranam.ast.SankhyaPada -> member.stems
                else -> member
            }
            identity to member.sup.affixCandidates
        }.groupingBy { it }.eachCount()
        val declaredSubjects = groups.map(::subjects).filter { it.isNotEmpty() }
        val sharedSubjects = declaredSubjects.firstOrNull().orEmpty()
        val sharedIdentities = identities(sharedSubjects)
        requirePrior(declaredSubjects.all { subjects ->
            identities(subjects) == sharedIdentities
        }, "PRIOR_ACTION_AGENT_IDENTITY") {
            "क्त्वा-ल्यप्क्रिययोः मुख्यक्रियायाश्च समानः कर्ता अपेक्षितः।"
        }
        val statements = groups.mapIndexed { index, group ->
            val head = if (index == groups.lastIndex) sentence.tinganta else {
                val avyaya = group.last() as AvyayaPada
                val derivation = avyaya.derivation as AvyayaKridantaDerivation
                sentence.tinganta.copy(
                    sourceText = (derivation.upasargas + derivation.dhatu.mulaDhatu +
                        derivation.dhatu.sanadiPratyayas +
                        sentence.tinganta.lakara.upadesha + sentence.tinganta.ting.text).joinToString(" + "),
                    upasargas = derivation.upasargas, dhatu = derivation.dhatu,
                    vikarana = null, priorAction = derivation,
                ).inheritLocation(sentence.tinganta)
            }
            val writtenOperands = if (index == groups.lastIndex) group else group.dropLast(1) + head
            // A display verb needs an object. Within this prior-action discourse,
            // an otherwise objectless display refers to the immediately preceding
            // result. This does not make every prior-action connector a pipeline.
            val omittedDisplayObject = index > 0 && head.canonicalDhatuIdentity() == CanonicalDhatuIdentity.MUDR &&
                writtenOperands.all { it is TingantaPada || it is AvyayaPada || it in subjects(group) }
            val operands = if (omittedDisplayObject) writtenOperands.filterNot { it === head } + SubantaPada(
                "", MulaPratipadika("फल", "फल"), SupPratyaya("अम्", "अम्"),
            ) + head else writtenOperands
            val boundPadas = if (subjects(group).isEmpty()) sharedSubjects + operands else operands
            Invocation(AkhyataVakya(group.joinToString(" ") { it.sourceText }, boundPadas, head)).also { invocation ->
                when (val member = CollectionMemberMorphology.analyze(invocation)) {
                    CollectionMemberMorphology.Analysis.Valid -> Unit
                    is CollectionMemberMorphology.Analysis.Invalid ->
                        throw InvalidPriorAction(member.diagnostic.code, member.diagnostic.message)
                }
            }
        }
        return Sequence(node.sourceText, statements, List(statements.size - 1) { SequenceConnector.PURVAKALA.surface })
    }
}
