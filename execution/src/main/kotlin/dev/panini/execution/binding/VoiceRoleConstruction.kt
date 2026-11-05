package dev.panini.execution.binding

import dev.panini.analysis.AdverbialAnalyzer
import dev.panini.analysis.DhatuArthaClass
import dev.panini.analysis.hasArtha
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KrtGovernorReading
import dev.panini.analysis.participialRelations
import dev.panini.core.Vacana
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SanadiAffix
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Roles shown by the voice of the verb itself (3.4.69, 2.3.2, 2.3.46), for any root in the
 * Dhātupāṭha and whether or not the machine can execute it.
 *
 * It commits only where the sentence leaves one reading. Several uncoordinated nominatives may be
 * apposition or predication, and several accusatives may be dvikarmaka or predicative, so those stay
 * unassigned. A causative's accusative may be the causee, and a passive's instrumental the kartā or
 * the karaṇa, so those stay open too.
 */
internal object VoiceRoleConstruction {
    /** 3.2.182 and 3.2.184 derive these "करणे कारके" from listed roots with ष्ट्रन् and इत्र. */
    private val karanaDerived = setOf("दात्र", "नेत्र", "शस्त्र", "योत्र", "योक्त्र", "स्तोत्र", "तोत्र", "सेत्र", "सेक्त्र", "पत्र",
        "अरित्र", "लवित्र", "धवित्र", "सवित्र", "खनित्र", "सहित्र", "चरित्र")
    /** Not from the grammar: underived or unanalysed tool words that no text here marks. A kośa should replace this. */
    private val underivedTools = setOf("परशु", "दण्ड", "कुञ्चिका", "खड्ग", "बाण", "लेखनी", "अक्ष", "यष्टि", "रज्जु", "कुठार")

    private fun namesMeans(stem: String): Boolean = stem in karanaDerived || stem in underivedTools ||
        dev.panini.unadipatha.analysis.UnadiAnalyzer.analyzeStem(stem).matches.any { it.meaning == dev.panini.shiksha.Artha.Karaka.KARANA }

    private fun relationOpen(frame: KriyaFrame, nominal: dev.panini.analysis.SubantaAnalysis, vararg karakas: Karaka): Boolean {
        val relation = frame.relations.singleOrNull { it.participant.pada === nominal.pada } ?: return false
        val candidates = when (val resolution = relation.resolution) {
            is FrameKarakaResolution.Unassigned -> resolution.candidates
            is FrameKarakaResolution.Ambiguous -> resolution.candidates
            else -> return false
        }
        return relation.alternatives.isEmpty() && karakas.any { it in candidates }
    }

    val timeWords = setOf("क्षण", "मुहूर्त", "दिन", "दिवस", "अहन्", "रात्रि", "पक्ष", "मास", "ऋतु", "वर्ष", "संवत्सर", "युग")
    // योजन is left out: it also means joining, so योजनेन may be a real karaṇa.
    val distanceWords = setOf("क्रोश", "गव्यूति")
    private val actionNounAffixes = setOf("ल्युट्", "घञ्", "क्तिन्", "तृच्", "ण्वुल्")

    /** बालं शयानं पश्य: a participle agreeing with its noun is predicated of it, not a second role. */
    fun isAgreeingParticiple(nominal: dev.panini.analysis.SubantaAnalysis): Boolean =
        ((nominal.pada as? SubantaPada)?.pratipadika as? KridantaPratipadika)?.krtPratyaya in setOf("शतृ", "शानच्")

    /**
     * 2.3.65: the clause's one genitive beside its one action noun may be that noun's kartā or karma;
     * meaning decides which. Word order is free, so position plays no part.
     */
    fun krtGovernors(frame: KriyaFrame): Map<Int, KrtGovernorReading> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        val absoluteObjects = frame.participialRelations.filter { it.participle > 0 }.mapNotNull { relation ->
            absoluteObject(frame, relation)?.let { it to KrtGovernorReading(relation.participle, setOf(Karaka.KARTR)) }
        }.toMap()
        return absoluteObjects + genitiveOnActionNoun(nominals)
    }

    /** रामे वनं गच्छति, पाठं पठन्: the accusative written directly before a participle is its object. */
    private fun absoluteObject(frame: KriyaFrame, relation: dev.panini.analysis.ParticipialRelation): Int? {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        val before = relation.participle - 1
        return before.takeIf { it >= 0 && it != relation.bearer && nominals[it].supCandidates.isNotEmpty() &&
            nominals[it].supCandidates.all { sup -> sup.vibhakti == Vibhakti.DVITIYA } }
    }

    private fun genitiveOnActionNoun(nominals: List<dev.panini.analysis.SubantaAnalysis>): Map<Int, KrtGovernorReading> {
        val governor = nominals.indices.filter { index ->
            ((nominals[index].pada as? SubantaPada)?.pratipadika as? KridantaPratipadika)?.krtPratyaya in actionNounAffixes
        }.singleOrNull() ?: return emptyMap()
        val genitive = nominals.indices.filter { index -> nominals[index].supCandidates.isNotEmpty() &&
            nominals[index].supCandidates.all { it.vibhakti == Vibhakti.SASTHI } }.singleOrNull() ?: return emptyMap()
        return if (genitive == governor) emptyMap()
            else mapOf(genitive to KrtGovernorReading(governor, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN)))
    }

    /**
     * Which object the passive expresses (Padamañjarī on 1.4.51). नी, हृ, कृष्, वह् express the primary, दुहादि the secondary,
     * and a motion causative the causee. Cognition, eating and speech causatives may express either, so they stay open.
     */
    private fun passiveObjects(frame: KriyaFrame, dhatu: Dhatu, written: String, causative: Boolean, nominative: Int?, accusative: Int?): Map<Int, Set<SemanticRelation>> {
        if (frame.prayoga != Prayoga.KARMANI || nominative == null || accusative == null) return emptyMap()
        val names = setOf(written, dhatu.sourceSurface)
        fun pair(expressed: SemanticRelation, other: SemanticRelation) = mapOf(nominative to setOf(expressed, SemanticRelation.DVIKARMAKA_ACTION_SENSE),
            accusative to setOf(other, SemanticRelation.DVIKARMAKA_ACTION_SENSE))
        return when {
            causative -> if (dhatu.hasArtha(DhatuArthaClass.MOTION)) pair(SemanticRelation.CAUSEE_AGENT, SemanticRelation.DESIRED_OBJECT) else emptyMap()
            names.any { it in primaryExpressing } -> pair(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SECONDARY_OBJECT)
            names.any { it in secondaryExpressing } -> pair(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.DESIRED_OBJECT)
                .mapValues { it.value + SemanticRelation.SECONDARY_OBJECT_PROMOTING_SENSE }
            else -> emptyMap()
        }
    }

    /**
     * Two accusatives are both karma; only the sub-role is unproved, so they follow the order of the commentary examples
     * and stay marked as defaults: गां दोग्धि पयः, अजां नयति ग्रामम्, माणवकं वेदम् अध्यापयति.
     */
    private fun twoObjects(frame: KriyaFrame, dhatu: Dhatu, written: String, causative: Boolean, open: List<Int>): Map<Int, Set<SemanticRelation>> {
        if (open.size != 2) return emptyMap()
        val names = setOf(written, dhatu.sourceSurface)
        val (first, second) = open
        return when {
            frame.prayoga == Prayoga.CAUSATIVE && causative && dhatu.hasArtha(DhatuArthaClass.MOTION, DhatuArthaClass.COGNITION, DhatuArthaClass.EATING, DhatuArthaClass.SPEECH) ->
                mapOf(first to setOf(SemanticRelation.CAUSEE_AGENT), second to setOf(SemanticRelation.DESIRED_OBJECT))
            frame.prayoga != Prayoga.KARTARI -> emptyMap()
            names.any { it in primaryExpressing } -> mapOf(first to setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE),
                second to setOf(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE))
            names.any { it in secondaryExpressing } -> mapOf(first to setOf(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE),
                second to setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE))
            else -> emptyMap()
        }
    }

    private val primaryExpressing = setOf("नी", "हृ", "कृष्", "वह्")
    private val secondaryExpressing = setOf("दुह्", "दुहँ", "याच्", "दण्ड्", "रुध्", "प्रच्छ्", "चि", "ब्रू", "शास्", "जि", "मथ्", "मुष्")

    /** Readings grammar reports but an instruction may not take as operands unless it declares them. */
    fun nonBinding(frame: KriyaFrame, relations: Map<Int, Set<SemanticRelation>>): Set<Int> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        return relations.filter { (index, meanings) ->
            SemanticRelation.INSTRUMENT in meanings || SemanticRelation.CAUSE_HETU in meanings || SemanticRelation.CAUSEE_AGENT in meanings ||
                SemanticRelation.DVIKARMAKA_ACTION_SENSE in meanings ||
                (frame.prayoga == Prayoga.CAUSATIVE && SemanticRelation.DESIRED_OBJECT in meanings && nominals.indices.any { other ->
                    relations[other]?.contains(SemanticRelation.INDEPENDENT_AGENT) == true && nominals[other].supCandidates.all { it.vibhakti == Vibhakti.TRTIYA } }) ||
                (frame.prayoga == Prayoga.CAUSATIVE && SemanticRelation.INDEPENDENT_AGENT in meanings && nominals[index].supCandidates.all { it.vibhakti == Vibhakti.TRTIYA })
        }.keys
    }

    /** Readings the case and voice do not prove; intention could select another. */
    fun defaults(frame: KriyaFrame, relations: Map<Int, Set<SemanticRelation>>): Set<Int> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        fun only(index: Int, case: Vibhakti) = nominals[index].supCandidates.all { it.vibhakti == case }
        return relations.filter { (index, meanings) ->
            SemanticRelation.LOCATION in meanings || SemanticRelation.SOURCE in meanings || SemanticRelation.GROUP_SELECTION in meanings ||
                SemanticRelation.INSTRUMENT in meanings || SemanticRelation.BARE_NOMINAL_MEANING in meanings || SemanticRelation.CAUSEE_AGENT in meanings ||
                SemanticRelation.SECONDARY_OBJECT in meanings || SemanticRelation.FORMAL_INSTRUCTION_SOURCE in meanings || SemanticRelation.CAUSE_HETU in meanings || SemanticRelation.ACCUSATIVE_CONCORD in meanings ||
                SemanticRelation.BIRTH_MATERIAL_CAUSE in meanings || SemanticRelation.FIRST_MANIFESTATION_SOURCE in meanings || SemanticRelation.UNBEARABLE_OBJECT in meanings ||
                SemanticRelation.PREVENTED_DESIRED_OBJECT in meanings || SemanticRelation.CREDITOR in meanings || SemanticRelation.FEAR_CAUSE in meanings ||
                SemanticRelation.PLEASED_EXPERIENCER in meanings || SemanticRelation.ANGER_TARGET in meanings ||
                SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT in meanings || SemanticRelation.MOTION_GOAL in meanings ||
                (SemanticRelation.DESIRED_OBJECT in meanings && only(index, Vibhakti.DVITIYA)) ||
                (only(index, Vibhakti.TRTIYA) && meanings.any { it == SemanticRelation.INDEPENDENT_AGENT || it == SemanticRelation.PROMPTER_CAUSE })
        }.keys
    }

    fun infer(sentence: AkhyataVakya, dhatu: Dhatu?, frame: KriyaFrame, priorAction: Boolean = false): Map<Int, Set<SemanticRelation>> {
        val head = sentence.tinganta
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        // A nominative in active voice is the kartā whichever homonym was meant; only object questions need the root.
        if (dhatu == null && frame.prayoga == Prayoga.KARTARI) return nominals.indices.filter { index ->
            nominals[index].supCandidates.isNotEmpty() && nominals[index].supCandidates.all { it.vibhakti == Vibhakti.PRATHAMA }
        }.singleOrNull()?.takeIf { (frame.relations.singleOrNull { r -> r.participant.pada === nominals[it].pada }?.resolution as? FrameKarakaResolution.Unassigned)?.candidates == setOf(Karaka.KARTR) }
            ?.let { mapOf(it to setOf(SemanticRelation.INDEPENDENT_AGENT)) }.orEmpty()
        if (dhatu == null || head.upasargaIdentities.any { it == null } || head.dhatu.sanadiAffixes.any { it == null }) return emptyMap()
        fun inCase(case: Vibhakti) = nominals.indices.filter { index ->
            nominals[index].supCandidates.isNotEmpty() && nominals[index].supCandidates.all { it.vibhakti == case }
        }
        val coordinated = sentence.padas.any { (it is AvyayaPada && it.form in setOf("च", "वा")) || it is dev.panini.vyakaranam.ast.SamuccitaSubanta }
        val nominatives = inCase(Vibhakti.PRATHAMA).filterNot { isAgreeingParticiple(nominals[it]) }
            .takeIf { it.size == 1 || coordinated }.orEmpty()
        // 2.3.41: क्षत्रियेषु शूरः, a plural beside a subject and its predicate names the group selected from.
        val groups = if (dhatu.hasArtha(DhatuArthaClass.BEING) && inCase(Vibhakti.PRATHAMA).size == 2) nominals.indices.filter { index ->
            nominals[index].supCandidates.isNotEmpty() &&
                nominals[index].supCandidates.all { it.vibhakti == Vibhakti.SAPTAMI && it.vacana == Vacana.BAHUVACANA }
        } else emptyList()
        fun stem(index: Int) = ((nominals[index].pada as? SubantaPada)?.pratipadika as? MulaPratipadika)?.text
        val motion = dhatu.hasArtha(DhatuArthaClass.MOTION)
        val absolutes = frame.participialRelations.filter { it.absolute }
        val absoluteObjects = frame.participialRelations.mapNotNull { absoluteObject(frame, it) }
        val upapada = CaseFormConstruction.upapadaDependents(sentence, frame).keys
        val accusatives = (inCase(Vibhakti.DVITIYA) - absoluteObjects.toSet() - upapada - CaseFormConstruction.infinitiveObjects(sentence, frame).keys).filterNot { isAgreeingParticiple(nominals[it]) }
        // 2.3.5: time always, and distance unless the root is motion, where क्रोशं गच्छति may name the goal.
        val extents = accusatives.filter { stem(it) in timeWords || (stem(it) in distanceWords && !motion) }
        val notObjects = extents + accusatives.filter { stem(it) in distanceWords || AdverbialAnalyzer.licensesCaseForm(stem(it)) }
        // अहं त्वां मूढं मन्ये: with मन्, the first accusative is the object and the second is said of it.
        val accusative = (accusatives - notObjects.toSet()).let { open -> if (open.size == 2 && (head.dhatu.mulaDhatu in setOf("मन्", "मनँ") || dhatu.sourceSurface == "मन्")) open.first() else open.singleOrNull() }
        val causative = SanadiAffix.NIC in head.dhatu.sanadiAffixes
        // उद्घटते तालम्: an upasarga can give an akarmaka root an object, so a written accusative is believed.
        val sakarmaka = dhatu.karmatva != Karmatva.AKARMAKA || head.upasargas.isNotEmpty()
        // 3.4.69 गम्यते ग्रामो देवदत्तेन: a passive leaves its kartā unexpressed, so one plain instrumental is by default that kartā.
        // Kartā and karaṇa turn on intention (1.4.42, 1.4.54), so this is a default, not a proof.
        // कारकेण, कर्त्रा: ण्वुल् and तृच् name a doer (3.1.133), so a passive instrumental of one is its kartā.
        fun isDoerNoun(index: Int) = ((nominals[index].pada as? SubantaPada)?.pratipadika as? KridantaPratipadika)?.krtPratyaya in setOf("ण्वुल्", "तृच्")
        val instrumentals = inCase(Vibhakti.TRTIYA) - upapada
        // With a क्त्वा or ल्यप् action in the clause, the instrumental may belong to either action, so it stays open.
        val passiveAgent = instrumentals.singleOrNull()?.takeIf { !priorAction }?.takeIf { index -> isDoerNoun(index) || stem(index).let { it != null && !namesMeans(it) && it !in timeWords && it !in distanceWords &&
            CaseFormConstruction.infer(frame)[index] == null } }
        // मारीचः नाम राक्षसः: the word before नाम is the name, not the subject.
        val namedAt = sentence.padas.indexOfFirst { it is AvyayaPada && it.form == "नाम" }
        val name = sentence.padas.getOrNull(namedAt - 1)?.let { before -> nominals.indexOfFirst { it.pada === before }.takeIf { it >= 0 && it in inCase(Vibhakti.PRATHAMA) } }
        val plainNominatives = inCase(Vibhakti.PRATHAMA).filterNot { isAgreeingParticiple(nominals[it]) || it == name }
        val namedSubject = plainNominatives.singleOrNull().takeIf { name != null }
        val sameNumber = plainNominatives.size == 2 && nominals[plainNominatives[0]].supCandidates.any { one ->
            nominals[plainNominatives[1]].supCandidates.any { it.vacana == one.vacana } }
        // 2.3.46 vārttika: देवदत्तः अध्यापकः अस्ति states of the first; दाशरथिः रामः names the second.
        val (subject, predicate) = when {
            namedSubject != null -> namedSubject to null
            coordinated || !sameNumber || groups.isNotEmpty() -> null to null
            // रामः अध्यापकः भूत्वा: the second nominative is said of the first through the prior action.
            priorAction || dhatu.hasArtha(DhatuArthaClass.BEING) -> plainNominatives[0] to plainNominatives[1]
            plainNominatives[1] == plainNominatives[0] + 1 -> plainNominatives[1] to plainNominatives[0]
            else -> null to null
        }
        val participles = inCase(Vibhakti.PRATHAMA).filter { isAgreeingParticiple(nominals[it]) }
        // 1.4.52 शाययति: an akarmaka base makes its sole accusative the causee. पाचयति: a causee stays instrumental.
        val causativeInstrumental = inCase(Vibhakti.TRTIYA).singleOrNull().takeIf { frame.prayoga == Prayoga.CAUSATIVE }
        val roles = buildMap<Int, Pair<SemanticRelation, Karaka>> {
            if (frame.prayoga == Prayoga.KARTARI) subject?.let { put(it, SemanticRelation.INDEPENDENT_AGENT to Karaka.KARTR) }
            if (frame.prayoga == Prayoga.CAUSATIVE) {
                subject?.let { put(it, SemanticRelation.PROMPTER_CAUSE to Karaka.KARTR) }
                if (dhatu.karmatva == Karmatva.AKARMAKA) accusative?.let { put(it, SemanticRelation.CAUSEE_AGENT to Karaka.KARMAN) }
                causativeInstrumental?.let { put(it, SemanticRelation.INDEPENDENT_AGENT to Karaka.KARTR)
                    if (sakarmaka) accusative?.let { obj -> put(obj, SemanticRelation.DESIRED_OBJECT to Karaka.KARMAN) } }
            }
            // 1.4.42 दात्रेण लुनाति: active voice expresses the kartā, so a plain instrumental is the means.
            if (frame.prayoga == Prayoga.KARTARI) instrumentals.filter { stem(it) != null && stem(it) !in timeWords &&
                stem(it) !in distanceWords && CaseFormConstruction.infer(frame)[it] == null }
                .forEach { put(it, SemanticRelation.INSTRUMENT to Karaka.KARANA) }
            // 1.4.24 ग्रामादागच्छति, रथात् पतितः: the fixed point left behind in a movement.
            if (dhatu.hasArtha(DhatuArthaClass.MOTION, DhatuArthaClass.FALLING))
                inCase(Vibhakti.PANCHAMI).forEach { put(it, SemanticRelation.SEPARATION_REFERENCE to Karaka.APADANA) }
            // 1.4.45, 2.3.36: वृक्षे वसति. A locative names the locus unless a more specific rule below claims it.
            // दूरे ग्रामस्य takes saptamī by 2.3.36 as a word of distance, with no kāraka.
            inCase(Vibhakti.SAPTAMI).filterNot { stem(it) in setOf("दूर", "अन्तिक", "अभ्याश", "निकट", "समीप") }.filterNot { ((nominals[it].pada as? SubantaPada)?.pratipadika as? KridantaPratipadika)?.krtPratyaya in setOf("शतृ", "शानच्", "क्त") }
                .forEach { put(it, SemanticRelation.LOCATION to Karaka.ADHIKARANA) }
            if (frame.prayoga == Prayoga.KARMANI || frame.prayoga == Prayoga.BHAVE) passiveAgent?.let { put(it, (if (causative) SemanticRelation.PROMPTER_CAUSE else SemanticRelation.INDEPENDENT_AGENT) to Karaka.KARTR) }
            extents.forEach { put(it, SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT to Karaka.KARMAN) }
            when (frame.prayoga) {
                Prayoga.KARTARI -> {
                    nominatives.forEach { put(it, SemanticRelation.INDEPENDENT_AGENT to Karaka.KARTR) }
                    if (sakarmaka && accusative != null) put(accusative, SemanticRelation.DESIRED_OBJECT to Karaka.KARMAN)
                }
                Prayoga.CAUSATIVE -> nominatives.forEach { put(it, SemanticRelation.PROMPTER_CAUSE to Karaka.KARTR) }
                Prayoga.KARMANI -> if (!causative && sakarmaka)
                    nominatives.forEach { put(it, SemanticRelation.DESIRED_OBJECT to Karaka.KARMAN) }
                else -> Unit
            }
        }
        // 2.3.12 ग्रामाय गच्छति: the goal of bodily motion, unless it is a road being travelled.
        val goals = if (dhatu.hasArtha(DhatuArthaClass.MOTION)) inCase(Vibhakti.CHATURTHI).filter { index ->
            stem(index) !in setOf("पथिन्", "मार्ग", "अध्वन्") && relationOpen(frame, nominals[index], Karaka.KARMAN, Karaka.SAMPRADANA) } else emptyList()
        // Vārttika on 2.3.28: आसनात् प्रेक्षते has lost उपविश्य, so its ablative is no separation.
        // Vārttika on 2.3.18: वेगेन धावति, प्रकृत्या दर्शनीयः names the manner, not a means.
        val mannerInstrumentals = inCase(Vibhakti.TRTIYA).filter { stem(it) in setOf("प्रकृति", "प्राय", "गोत्र", "सम", "विषम", "वेग", "सुख", "दुःख") }
        val lostAbsolutive = if (head.dhatu.mulaDhatu in setOf("ईक्ष्", "ईक्षँ")) inCase(Vibhakti.PANCHAMI) else emptyList()
        return roles.mapNotNull { (index, role) ->
            val (meaning, karaka) = role
            val relation = frame.relations.singleOrNull { it.participant.pada === nominals[index].pada }
            val open = relation != null && relation.alternatives.isEmpty() && when (val resolution = relation.resolution) {
                is FrameKarakaResolution.Unassigned -> resolution.candidates == setOf(karaka)
                is FrameKarakaResolution.Ambiguous -> karaka in resolution.candidates
                else -> false
            }
            if (open && nominals[index].lexicalEntry?.semanticRelations.orEmpty().all { it == meaning })
                index to setOf(meaning) else null
        }.toMap() + groups.associateWith { setOf(SemanticRelation.GROUP_SELECTION, SemanticRelation.NIRDHARANA_REFERENCE) } +
            absolutes.flatMap { listOfNotNull(it.participle, it.bearer) }.associateWith { setOf(SemanticRelation.ACTION_MARKING) } +
            extents.associateWith { setOf(SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT) } +
            twoObjects(frame, dhatu, head.dhatu.mulaDhatu, causative, (accusatives - notObjects.toSet())) +
            // बालं शयानं पश्य, त्वां मूढं मन्ये, मन्दं गच्छति: said of the object or the action, not a second object.
            (inCase(Vibhakti.DVITIYA).filter { isAgreeingParticiple(nominals[it]) && it !in absoluteObjects } +
                accusatives.filter { AdverbialAnalyzer.licensesCaseForm(stem(it)) } +
                listOfNotNull((accusatives - notObjects.toSet()).takeIf { it.size == 2 && (head.dhatu.mulaDhatu in setOf("मन्", "मनँ") || dhatu.sourceSurface == "मन्") }?.get(1)))
                .associateWith { setOf(SemanticRelation.ACCUSATIVE_CONCORD) } +
            mannerInstrumentals.associateWith { setOf(SemanticRelation.PRAKRTYADI_MANNER) } +
            lostAbsolutive.associateWith { setOf(SemanticRelation.LYAB_LOPA_REFERENCE) } +
            passiveObjects(frame, dhatu, head.dhatu.mulaDhatu, causative, inCase(Vibhakti.PRATHAMA).singleOrNull(), accusative) +
            listOfNotNull(predicate, name).plus(participles).associateWith { setOf(SemanticRelation.BARE_NOMINAL_MEANING) } +
            absoluteObjects.associateWith { setOf(SemanticRelation.DESIRED_OBJECT) } +
            goals.associateWith { setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MOTION_GOAL, SemanticRelation.PHYSICAL_MOTION, SemanticRelation.NON_PATH_DESTINATION) } +
            roles.filterValues { it.first == SemanticRelation.SEPARATION_REFERENCE }.keys.filter { relationOpen(frame, nominals[it], Karaka.APADANA) }
                .associateWith { setOf(SemanticRelation.SOURCE, SemanticRelation.SEPARATION_REFERENCE) }
    }
}
