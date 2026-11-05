package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.SanadiAffix
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.GrammaticalFeatures
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Vikarana

/** PVM's written nominative executor/initiator of a registered active instruction. */
internal object ProgramAgentConstruction {
    /** Vārttika पदसामानाधिकरण्ये on 2.3.46: a nominative in apposition takes prathamā with no kāraka. */
    private fun appositive(index: Int, nominal: dev.panini.analysis.SubantaAnalysis, relation: dev.panini.analysis.KarakaRelation) =
        (index to setOf(SemanticRelation.BARE_NOMINAL_MEANING)).takeIf {
            relation.resolution is FrameKarakaResolution.Unassigned && nominal.supCandidates.isNotEmpty() &&
                nominal.supCandidates.all { it.vibhakti == Vibhakti.PRATHAMA } }

    fun infer(sentence: AkhyataVakya, dhatu: Dhatu?, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val head = sentence.tinganta
        if (dhatu == null || head.lakara !in setOf(Lakara.LOT, Lakara.LAT) || head.vikarana == Vikarana.YAK ||
            head.upasargaIdentities.any { it == null } || head.dhatu.sanadiAffixes.any { it == null }) return emptyMap()
        val features = GrammaticalFeatures(head.upasargas.toSet(), head.dhatu.sanadiPratyayas.toSet(), lakara = head.lakara)
        if (dhatu.operations.none { it.trigger.matches(features) }) return emptyMap()
        val meaning = if (SanadiAffix.NIC in head.dhatu.sanadiAffixes) SemanticRelation.PROMPTER_CAUSE
            else SemanticRelation.INDEPENDENT_AGENT
        // A copular comparison predicate is not an agent; its subject remains one.
        val copular = head.dhatu.mulaDhatu in setOf("अस्", "असँ")
        val predicateStems = setOf("सम", "समान", "तुल्य", "सदृश", "न्यून", "अधिक")
        val written = sentence.padas
        val namedDeclarations = written.indices.filter { written[it].let { p -> p is dev.panini.vyakaranam.ast.AvyayaPada && p.form == "नाम" } }
            .mapNotNull { written.getOrNull(it - 1) }
        fun stemOf(n: dev.panini.analysis.SubantaAnalysis) = ((n.pada as? dev.panini.vyakaranam.ast.SubantaPada)?.pratipadika
            as? dev.panini.vyakaranam.ast.MulaPratipadika)?.text
        val nominatives = frame.nominalRelations?.nominalPadas.orEmpty().filter { n ->
            namedDeclarations.none { it === n.pada } && n.supCandidates.isNotEmpty() &&
                n.supCandidates.all { it.vibhakti == Vibhakti.PRATHAMA } && stemOf(n) !in predicateStems }
        // The known thing is stated before what is said of it, so the first nominative is the subject.
        val firstNominative = nominatives.firstOrNull()
        val valueDeclaration = frame.nominalRelations?.nominalPadas.orEmpty().none { n ->
            ((n.pada as? dev.panini.vyakaranam.ast.SubantaPada)?.pratipadika as? dev.panini.vyakaranam.ast.MulaPratipadika)?.text in predicateStems }
        return frame.nominalRelations?.nominalPadas.orEmpty().mapIndexedNotNull { index, nominal ->
            val relation = frame.relations.singleOrNull { it.participant.pada === nominal.pada }
                ?: return@mapIndexedNotNull null
            if (namedDeclarations.any { it === nominal.pada }) return@mapIndexedNotNull appositive(index, nominal, relation)
            // The stated subject precedes its predicate in a value declaration.
            if (copular && valueDeclaration && firstNominative != null && nominal !== firstNominative) return@mapIndexedNotNull appositive(index, nominal, relation)
            if (copular && ((nominal.pada as? dev.panini.vyakaranam.ast.SubantaPada)?.pratipadika as? dev.panini.vyakaranam.ast.MulaPratipadika)
                    ?.text in predicateStems) return@mapIndexedNotNull appositive(index, nominal, relation)
            val unresolved = relation.resolution as? FrameKarakaResolution.Unassigned
            if (unresolved?.candidates != setOf(Karaka.KARTR) || relation.alternatives.isNotEmpty() ||
                nominal.supCandidates.isEmpty() || nominal.supCandidates.any { it.vibhakti != Vibhakti.PRATHAMA } ||
                nominal.lexicalEntry?.semanticRelations.orEmpty().any { it != meaning }) return@mapIndexedNotNull null
            index to setOf(meaning)
        }.toMap()
    }
}
