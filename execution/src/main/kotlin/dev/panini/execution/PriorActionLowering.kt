package dev.panini.execution

import dev.panini.core.KrtAffix
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Shares prior-action ordering between interpreter binding and compiler lowering.
 * Source avyaya derivations remain intact. Finite heads below are internal binding
 * projections, not new source sentences or claims about the nonfinite word's tense.
 */
object PriorActionLowering {
    fun lower(root: ProgramNode): ProgramNode = object : ProgramNodeTransformer() {
        override fun visitInvocation(node: Invocation): ProgramNode = expand(node) ?: node
    }.transform(root)

    fun expand(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.priorAction != null) return null
        val padas = sentence.padas
        val boundaries = padas.indices.filter { index ->
            val derivation = (padas[index] as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation
            derivation != null && KrtAffix.fromUpadesha(derivation.pratyaya) in setOf(KrtAffix.KTVA, KrtAffix.LYAP)
        }
        if (boundaries.isEmpty()) return null
        // Preserve the established range-exclusion construction until its
        // contextual semantics are represented as a general prior-action frame.
        val specialized = NaturalSemanticNormalizer.normalize(node)
        if (specialized is NaturalSemanticNormalizer.Operation.RangeChoice && specialized.usesExclusionAbsolutive) return null
        require(sentence.tinganta.vikarana != Vikarana.YAK) {
            "पूर्वकालक्रियायाः कर्मणि-भावे मुख्यक्रियया सम्बन्धः अद्य न समर्थितः।"
        }
        boundaries.forEach { index ->
            val derivation = (padas[index] as AvyayaPada).derivation as AvyayaKridantaDerivation
            require(KrtAffix.fromUpadesha(derivation.pratyaya) != KrtAffix.LYAP || derivation.upasargas.isNotEmpty()) {
                "ल्यप्-प्रयोगे पूर्वपदम् अपेक्षितम्।"
            }
        }
        require(boundaries.last() < padas.indexOf(sentence.tinganta)) {
            "पूर्वकालक्रिया मुख्यक्रियायाः पूर्वम् अपेक्षिता।"
        }
        val groups = mutableListOf<List<Pada>>()
        var start = 0
        boundaries.forEach { index -> groups += padas.subList(start, index + 1); start = index + 1 }
        groups += padas.drop(start)
        fun isSubject(pada: SubantaPada): Boolean {
            val candidates = SupAffix.candidates(pada.sup.text)
            return candidates.isNotEmpty() && candidates.all { it.vibhakti == Vibhakti.PRATHAMA }
        }
        // Preserve coordination when sharing an understood agent. Flattening it
        // would turn a dual/plural controller into unrelated singular nouns.
        fun subjects(group: List<Pada>): List<Pada> = group.filter { pada ->
            when (pada) {
                is SubantaPada -> isSubject(pada)
                is SamuccitaSubanta -> pada.members.isNotEmpty() && pada.members.all(::isSubject)
                else -> false
            }
        }
        fun identities(subjects: List<Pada>) = subjects.flatMap { pada ->
            when (pada) {
                is SubantaPada -> listOf(pada)
                is SamuccitaSubanta -> pada.members
                else -> emptyList()
            }
        // Derivational suffixes identify the participant too: a base noun and
        // a possessive/feminine derivative are not automatically coreferential.
        }.map { it.pratipadika.morphologicalKey() to it.sup.text }.groupingBy { it }.eachCount()
        val declaredSubjects = groups.map(::subjects).filter { it.isNotEmpty() }
        val sharedSubjects = declaredSubjects.firstOrNull().orEmpty()
        val sharedIdentities = identities(sharedSubjects)
        require(declaredSubjects.all { subjects ->
            identities(subjects) == sharedIdentities
        }) { "क्त्वा-ल्यप्क्रिययोः मुख्यक्रियायाश्च समानः कर्ता अपेक्षितः।" }
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
                )
            }
            val writtenOperands = if (index == groups.lastIndex) group else group.dropLast(1) + head
            // A display verb needs an object. Within this prior-action discourse,
            // an otherwise objectless display refers to the immediately preceding
            // result. This does not make every prior-action connector a pipeline.
            val omittedDisplayObject = index > 0 && head.canonicalDhatuIdentity() == CanonicalDhatuIdentity.MUDR &&
                writtenOperands.all { it is TingantaPada || it is AvyayaPada || it in subjects(group) }
            val operands = if (omittedDisplayObject) writtenOperands.dropLast(1) + SubantaPada(
                "", MulaPratipadika("फल", "फल"), SupPratyaya("अम्", "अम्"),
            ) + head else writtenOperands
            val boundPadas = if (subjects(group).isEmpty()) sharedSubjects + operands else operands
            Invocation(AkhyataVakya(group.joinToString(" ") { it.sourceText }, boundPadas, head))
        }
        return Sequence(node.sourceText, statements, List(statements.size - 1) { SequenceConnector.PURVAKALA.surface })
    }
}
