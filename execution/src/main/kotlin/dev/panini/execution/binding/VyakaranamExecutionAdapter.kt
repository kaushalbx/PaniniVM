package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.shiksha.Samjna
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.Polarity
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaPrayojana
import dev.panini.execution.bindingName
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaInference
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.analysis.PadaAnalyzer
import dev.panini.analysis.UktiAnalyzer
import dev.panini.analysis.VakyaAnalyzer
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon
import kotlin.collections.plusAssign

/**
 * Thin bridge from canonical vyākaraṇa analysis to execution semantics.
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package.
 */
object VyakaranamExecutionAdapter {
    private val parser = PaniniParser()
    private val sankhyaGenerator = SankhyaGenerator()
    private val sankhyaEvaluator = dev.panini.sankhya.SankhyaEvaluator()
    private val katapayadiDecoder = KatapayadiDecoder()
    private val aryabhatiyaDecoder = AryabhatiyaDecoder()
    private val bhutasamkhyaDecoder = BhutasamkhyaDecoder()

    private val upadeshaDhatuCache by lazy {
        DhatuPatha.all.associateBy { it.upadesha }
    }

    private val dhatuRootsCache by lazy {
        DhatuPatha.all.associateWith { getDhatuRoot(it.upadesha) }
    }

    private val dhatuActionRootsCache by lazy {
        DhatuPatha.all.associateWith { dhatu ->
            dhatu.operations.mapTo(mutableSetOf()) { getActionRoot(it.name) }
        }
    }

    private val dhatuCacheMap by lazy {
        val map = mutableMapOf<String, Dhatu>()
        DhatuPatha.all.forEach { dhatu ->
            if (dhatu.operations.isNotEmpty()) {
                map[dhatu.id] = dhatu
                map[dhatu.upadesha] = dhatu
                map[dhatu.sourceSurface] = dhatu
                map[dhatu.derivationalSurface] = dhatu
                map[dhatu.upadesha.normalizeDhatuSurface()] = dhatu
                map[dhatu.sourceSurface.normalizeDhatuSurface()] = dhatu
                map[dhatu.derivationalSurface.normalizeDhatuSurface()] = dhatu
            }
        }
        map
    }

    fun bind(input: SanskritUktiInput, conversation: SambhashanaContext): ExecutionBindingResult {
        if (input.text.isBlank()) return ExecutionBindingResult.Invalid("The Sanskrit utterance is empty.")
        val ukti = try {
            parser.parse(input.text)
        } catch (e: PaniniParseException) {
            return ExecutionBindingResult.Invalid(e.message ?: "Invalid annotated Sanskrit morphology.")
        }

        var listener = input.listener
        ukti.sambodhana?.subanta?.pratipadika?.baseText()?.let { addressed ->
            if (!input.listener.startsWith(addressed)) listener = addressed
        }

        if (input.speaker != conversation.speaker) {
            return ExecutionBindingResult.Invalid("Utterance speaker does not match the trusted conversation context.")
        }
        val unresolved = ukti.vakyas.filterIsInstance<AkhyataVakya>()
            .firstOrNull { resolveDhatu(it.tinganta) == null }
        if (unresolved != null) {
            return ExecutionBindingResult.Invalid(
                "Unknown verbal action/dhātu: ${unresolved.tinganta.sourceText}",
            )
        }
        val utteranceAnalysis = UktiAnalyzer { vakya, frameId ->
            val akhyata = vakya as? AkhyataVakya
            if (akhyata == null) {
                VakyaAnalyzer(
                    PadaAnalyzer(
                        object : VyakaranamLexicon {
                            override fun findPratipadika(text: String): PratipadikaEntry? = null
                            override fun findDhatu(text: String): Dhatu? = null
                        },
                    ),
                ).analyze(vakya, frameId)
            } else {
                val dhatu = requireNotNull(resolveDhatu(akhyata.tinganta))
                VakyaAnalyzer(
                    PadaAnalyzer(
                        object : VyakaranamLexicon {
                            override fun findPratipadika(text: String): PratipadikaEntry? = null
                            override fun findDhatu(text: String): Dhatu = dhatu
                        },
                        validatePadaCompatibility = false,
                    ),
                ).analyze(vakya, frameId)
            }
        }.analyze(ukti)
        val invocations = mutableListOf<DhatuInvocation>()
        var prayer = false
        var prohibition = false
        val localVariables = mutableSetOf<String>()
        val abhyasaCounts = ukti.vakyas.mapNotNull { vakya ->
            val frame = utteranceAnalysis.frames.firstOrNull { it.vakya == vakya }
            if (frame != null) extractFrequencyCount(vakya.padas, frame) else null
        }
        val repeatCount = abhyasaCounts.maxOrNull() ?: 1

        // Only unroll if this is a multi-clause statement.
        // Single-clause repetition loops are evaluated in-memory using the original ExecutionRuntime loop.
        val shouldUnroll = repeatCount > 1 && ukti.vakyas.size > 1
        val unrolledVakyas = buildList {
            val count = if (shouldUnroll) repeatCount else 1
            repeat(count) {
                addAll(ukti.vakyas)
            }
        }

        unrolledVakyas.forEachIndexed { index, vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().forEach {
                prayer = prayer || it.form == "कृपया"
                prohibition = prohibition || it.form == "मा"
            }
            val tinganta = (vakya as? AkhyataVakya)?.tinganta ?: return@forEachIndexed
            val dhatu = resolveDhatu(tinganta)
                ?: return ExecutionBindingResult.Invalid("Unknown verbal action/dhātu: ${tinganta.sourceText}")
            val frame = utteranceAnalysis.frames.firstOrNull { it.vakya == vakya } ?: return@forEachIndexed
            val extracted = extractKarakas(vakya.padas, conversation, index, dhatu, frame, invocations.map { it.dhatu }, localVariables)
            val bindings = extracted.bindings.toMutableMap()
            println("BINDINGS KEYS for Clause $index (${dhatu.upadesha}): ${bindings.keys}")
            if (purposeRequiresListenerAsAgent(prayer, tinganta.lakara) && Karaka.KARTR !in bindings) {
                bindings[Karaka.KARTR] = ExecutionExpression.Pada(listener)
            }
            val frequencyCount = if (shouldUnroll) null else extractFrequencyCount(vakya.padas, frame)
            val metadataMap = buildMap {
                put("dhatuName", dhatu.upadesha)
                if (frequencyCount != null) {
                    put("frequencyCount", frequencyCount.toString())
                }
            }
            invocations += DhatuInvocation(
                id = "योग-${index + 1}",
                dhatu = dhatu,
                bindings = bindings,
                selectedOperation = null,
                metadata = metadataMap,
                grammaticalFeatures = GrammaticalFeatures(
                    upasargas = tinganta.upasargas.toSet(),
                    sanadi = tinganta.dhatu.sanadiPratyayas.toSet(),
                    avyayas = vakya.padas.filterIsInstance<AvyayaPada>()
                        .mapTo(mutableSetOf()) { it.form },
                    lakara = tinganta.lakara,
                ),
                ambiguousBindings = extracted.ambiguous,
                karakaTrace = extracted.trace,
            )
            val bindingKaraka = dhatu.operations.firstOrNull { it.resultBindingKaraka != null }?.resultBindingKaraka
            val bindingName = bindingKaraka?.let { bindings[it] }?.bindingName()
            if (bindingName != null) {
                localVariables.add(bindingName)
            }
        }

        val lakara = ukti.vakyas.filterIsInstance<AkhyataVakya>().firstOrNull()?.tinganta?.lakara
        val purpose = when {
            prohibition -> VakyaPrayojana.NISHEDHA
            prayer -> VakyaPrayojana.PRARTHANA
            lakara == Lakara.LOT -> VakyaPrayojana.AJNA
            else -> VakyaPrayojana.VIDHANA
        }
        if (invocations.isEmpty()) return ExecutionBindingResult.Invalid("No executable verbal action was identified.")
        if (listener != conversation.listener) {
            return ExecutionBindingResult.Invalid("Addressed listener does not match the trusted conversation context.")
        }
        return ExecutionBindingResult.Bound(
            ExecutableUkti(
                speaker = input.speaker,
                listener = listener,
                text = input.text,
                prayojana = purpose,
                polarity = if (prohibition) Polarity.NEGATIVE else Polarity.POSITIVE,
                lakara = lakara,
                invocations = invocations,
            ),
            listOf("Bound canonical vyākaraṇa AST with ${ukti.vakyas.size} clause(s) directly to execution."),
        )
    }

    private fun purposeRequiresListenerAsAgent(prayer: Boolean, lakara: Lakara): Boolean =
        prayer || lakara == Lakara.LOT

    private fun extractKarakas(
        padas: List<Pada>,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
        dhatu: Dhatu,
        frame: KriyaFrame,
        previousDhatus: List<Dhatu>,
        localVariables: Set<String>
    ): ExtractedBindings {
        println("PADAS CLASSES for Clause $clauseIndex (${dhatu.upadesha}): ${padas.map { it::class.simpleName }}")
        val grouped = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()
        val ambiguous = mutableListOf<AmbiguousKarakaBinding>()
        val trace = mutableListOf<String>()
        val requiredKarakas = dhatu.operations
            .flatMapTo(mutableSetOf()) { operation ->
                operation.signature.requirements.map { it.karaka } + operation.signature.optionalKarakas
            }

        val subantas = padas.filterIsInstance<SubantaPada>()
        val phalaPada = subantas.firstOrNull { it.pratipadika.baseText() == "फल" }
        val genitiveModifier = if (phalaPada != null) {
            subantas.firstOrNull { it != phalaPada && it.sup.text in setOf("ङस्", "आम्") }
        } else null

        val resolvedGenitives = mutableSetOf<SubantaPada>()
        var resolvedPhalaId: String? = null
        if (genitiveModifier != null) {
            val base = genitiveModifier.pratipadika.baseText()
            val root = getActionRoot(base)
            val matchedIndex = (0 until clauseIndex).lastOrNull { i ->
                val prevDhatu = previousDhatus.getOrNull(i)
                if (prevDhatu == null) false else {
                    val prevRoot = dhatuRootsCache[prevDhatu]
                    root == prevRoot || dhatuActionRootsCache[prevDhatu]?.contains(root) == true
                }
            }
            if (matchedIndex != null) {
                resolvedPhalaId = "योग-${matchedIndex + 1}"
                resolvedGenitives.add(genitiveModifier)
            } else {
                val historicalResult = conversation?.resultHistory?.lastOrNull { result ->
                    val dhatuUpadesha = conversation.metadata["dhatu:${result.id}"]
                    if (dhatuUpadesha != null) {
                        val prevDhatu = upadeshaDhatuCache[dhatuUpadesha]
                        if (prevDhatu != null) {
                            val prevRoot = dhatuRootsCache[prevDhatu]
                            root == prevRoot || dhatuActionRootsCache[prevDhatu]?.contains(root) == true
                        } else false
                    } else false
                }
                if (historicalResult != null) {
                    resolvedPhalaId = historicalResult.id
                    resolvedGenitives.add(genitiveModifier)
                }
            }
        }

        fun inferKarakas(pada: SubantaPada): Set<Karaka> {
            val relation = frame.relations.firstOrNull {
                it.participant.pada.sourceText == pada.sourceText
            }
            if (relation != null) {
                println("INFER RESOLUTION: pada = '${pada.sourceText}' | resolution = ${relation.resolution}")
            } else {
                println("INFER RESOLUTION NOT FOUND: pada = '${pada.sourceText}'")
            }
            if (relation == null) return emptySet()
            trace += relation.evidence.map { "${it.sutra} ${it.text}: ${it.reason}" }
            val candidates = when (val resolution = relation.resolution) {
                is FrameKarakaResolution.Resolved -> {
                    if (resolution.karaka in requiredKarakas) return setOf(resolution.karaka)
                    setOf(resolution.karaka)
                }
                is FrameKarakaResolution.Ambiguous -> resolution.candidates
                is FrameKarakaResolution.Unassigned -> emptySet()
            }
            val requiredCandidates = candidates intersect requiredKarakas
            if (requiredCandidates.size == 1) return requiredCandidates
            val legacyMorphologicalCandidates = relation.participant.supCandidates.mapNotNullTo(mutableSetOf()) {
                KarakaInference.infer(it.vibhakti, dev.panini.core.Prayoga.KARTARI, dhatu.karmatva != dev.panini.shiksha.Karmatva.AKARMAKA)
            }
            val compatibleLegacyCandidates = legacyMorphologicalCandidates intersect requiredKarakas
            return compatibleLegacyCandidates.takeIf { it.size == 1 } ?: candidates
        }
        fun addBinding(expression: ExecutionExpression, candidates: Set<Karaka>) {
            when (candidates.size) {
                0 -> grouped.getOrPut(Karaka.ANIRDHARITA) { mutableListOf() } += expression
                1 -> grouped.getOrPut(candidates.single()) { mutableListOf() } += expression
                else -> ambiguous += AmbiguousKarakaBinding(expression, candidates)
            }
        }
        fun add(subanta: SubantaPada) {
            val expr = if (subanta == phalaPada && resolvedPhalaId != null) {
                expression(subanta, conversation, clauseIndex, resolvedPhalaId, localVariables)
            } else {
                expression(subanta, conversation, clauseIndex, null, localVariables)
            }
            addBinding(expr, inferKarakas(subanta))
        }
        val mathTargetValues = mutableSetOf<Int>()

        fun extractNumeralValue(pada: Pada): Long? = when (pada) {
            is SankhyaPada -> pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
            is SubantaPada -> (pada.pratipadika as? SankhyaPratipadika)?.value
                ?: dev.panini.sankhya.PrimitiveSankhya.fromAnnotatedPratipadika(pada.pratipadika.sourceText)?.value
            is KatapayadiPada -> pada.value ?: katapayadiDecoder.decode(pada.word)
            is AryabhatiyaPada -> pada.value ?: aryabhatiyaDecoder.decode(pada.word)
            is BhutasamkhyaPada -> pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
            else -> null
        }

        padas.forEachIndexed { index, pada ->
            if (index in mathTargetValues) return@forEachIndexed
            if (pada in resolvedGenitives) return@forEachIndexed
            when (pada) {
                is SubantaPada -> add(pada)
                is SankhyaPada -> {
                    if (pada.stems.contains("कृत्वः") || pada.stems.contains("कृत्वस")) return@forEachIndexed
                    var targetIdx = -1
                    var nextVal: Long? = null
                    val lastStem = pada.stems.lastOrNull()
                    val isOpStem = lastStem in setOf("गुणित", "हते", "भक्त", "हृत", "कृत") || (pada.stems.size >= 1 && pada.stems[0] in setOf("वर्ग", "घन", "मूल"))
                    if (isOpStem) {
                        for (j in (index + 1) until padas.size) {
                            val v = extractNumeralValue(padas[j])
                            if (v != null) {
                                targetIdx = j
                                nextVal = v
                                break
                            }
                        }
                    }
                    val fullStems = if (nextVal != null && (pada.stems.size <= 2 || (pada.stems.size == 2 && pada.stems[1] == "कृत"))) {
                        val stemStr = dev.panini.sankhya.PrimitiveSankhya.fromValue(nextVal)?.let { if (it.purvapada.isNotEmpty()) it.purvapada else it.pratipadika } ?: "शत"
                        mathTargetValues.add(targetIdx)
                        if (pada.stems[0] in setOf("वर्ग", "घन", "मूल")) {
                            listOf(pada.stems[0]) + (if (pada.stems.size >= 2 && pada.stems[1] == "कृत") listOf("कृत") else emptyList()) + listOf(stemStr)
                        } else {
                            pada.stems + listOf(stemStr)
                        }
                    } else {
                        pada.stems
                    }
                    val expr = sankhyaEvaluator.evaluateStems(fullStems)
                    val value = expr.value
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SankhyaPuranaPada -> {
                    val value = pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SankhyaAbhyasaPada -> {
                    // अभ्यास-सङ्ख्या qualifies the action with a repetition count; it is
                    // metadata for execution, not one of the action's numeric arguments.
                    Unit
                }
                is KatapayadiPada -> {
                    val value = pada.value ?: katapayadiDecoder.decode(pada.word)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is AryabhatiyaPada -> {
                    val value = pada.value ?: aryabhatiyaDecoder.decode(pada.word)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is BhutasamkhyaPada -> {
                    val value = pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SamuccitaSubanta -> {
                    val members = pada.members.map { expression(it, conversation, clauseIndex, null, localVariables) }
                    val candidates = pada.members.firstOrNull()?.let { inferKarakas(it) }.orEmpty()
                    addBinding(ExecutionExpression.Coordination(members), candidates)
                }
                else -> Unit
            }
        }
        val bindings = grouped.mapValues { (karaka, values) ->
            val filteredValues = if (karaka == Karaka.KARMAN && values.size > 1) {
                val abhyasaPadas = padas.filterIsInstance<SankhyaAbhyasaPada>()
                val nonAbhyasa = values.filterNot { expr ->
                    expr is ExecutionExpression.Pada && abhyasaPadas.any { p ->
                        p.sourceText.contains(expr.prakriti) || expr.prakriti.contains(p.sourceText) || p.stems.contains(expr.prakriti)
                    }
                }
                if (nonAbhyasa.isNotEmpty()) nonAbhyasa else values
            } else {
                values
            }
            if (filteredValues.size == 1) filteredValues.single() else ExecutionExpression.Coordination(filteredValues)
        }
        trace += frame.qualifications.map {
            "Kriyā qualification ${it.kind}: ${it.value}"
        }
        return ExtractedBindings(bindings, ambiguous, trace.distinct())
    }

    private data class ExtractedBindings(
        val bindings: Map<Karaka, ExecutionExpression>,
        val ambiguous: List<AmbiguousKarakaBinding>,
        val trace: List<String>,
    )

    private fun expression(
        pada: SubantaPada,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
        resolvedPhalaId: String? = null,
        localVariables: Set<String> = emptySet()
    ): ExecutionExpression {
        val text = pada.pratipadika.baseText()
        var resolvedId: String? = null
        var isOrdinalReference = false

        if (conversation?.previousTypedResults?.containsKey(text) == true ||
            conversation?.previousResults?.containsKey(text) == true ||
            localVariables.contains(text)
        ) {
            resolvedId = text
        } else if (text == "फल") {
            resolvedId = resolvedPhalaId ?: (if (clauseIndex > 0) "योग-$clauseIndex" else
                conversation?.resultHistory?.lastOrNull()?.id ?: conversation?.previousResults?.keys?.lastOrNull())
        } else if (text.endsWith("फल")) {
            val prefix = text.removeSuffix("फल")
            val idx = (1..50).firstOrNull { i ->
                sankhyaGenerator.ordinal(i.toLong()).final.surface == prefix ||
                sankhyaGenerator.ordinalVariants(i.toLong()).any { it.final.surface == prefix }
            }?.minus(1)
            if (idx != null) {
                resolvedId = conversation?.resultHistory?.getOrNull(idx)?.id
                isOrdinalReference = true
            }
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = when (val prat = pada.pratipadika) {
            is SankhyaPratipadika -> prat.value
            is MulaPratipadika -> sankhyaGenerator.annotatedPratipadikaValue(prat.text)
            else -> null
        }
        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (sankhyaValue != null) add(Samjna.SANKHYA)
            if (text == "फल" || isOrdinalReference) add(Samjna.REFERENCE)
            when (pada.pratipadika) {
                is KridantaPratipadika -> add(Samjna.KRIDANTA)
                is SamasaPratipadika -> add(Samjna.SAMASA)
                else -> Unit
            }
        }
        return if (sankhyaValue != null) {
            ExecutionExpression.Companion.sankhya(sankhyaValue, text)
        } else {
            ExecutionExpression.Pada(text, samjnas)
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is SankhyaPratipadika -> sourceText
        is MulaPratipadika -> text
        is KridantaPratipadika -> dhatu.mulaDhatu
        is UnadyantaPratipadika -> {
            sourceText
        }
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }

    private fun resolveDhatu(tinganta: TingantaPada): Dhatu? {
        val text = tinganta.dhatu.mulaDhatu
        val cached = dhatuCacheMap[text]
        if (cached != null) return cached
        val normalizedText = text.normalizeDhatuSurface()
        return dhatuCacheMap[normalizedText]
    }

    private fun String.normalizeDhatuSurface(): String = trimEnd('्', 'ँ')

    private fun extractFrequencyCount(padas: List<Pada>, frame: KriyaFrame): Int? {
        val sankhyaAbhyasa = padas.filterIsInstance<SankhyaAbhyasaPada>().firstOrNull()
        if (sankhyaAbhyasa != null) {
            val numStems = sankhyaAbhyasa.stems.filter { it != "कृत्वः" && it != "कृत्वा" }
            val evaluated = if (numStems.isNotEmpty()) sankhyaEvaluator.evaluateStems(numStems) else sankhyaEvaluator.evaluateStems(sankhyaAbhyasa.stems)
            return evaluated.value.toInt()
        }
        val freqQual = frame.qualifications.firstOrNull { it.kind == KriyaQualificationKind.FREQUENCY }
        if (freqQual != null) {
            val text = freqQual.value
            if (text.endsWith("कृत्वः")) {
                val prefix = text.removeSuffix("कृत्वः")
                val eval = sankhyaEvaluator.evaluateStems(listOf(prefix))
                if (eval.value > 0) return eval.value.toInt()
            }
            return when (text) {
                "सकृत्" -> 1
                "द्विः", "द्विकृत्वः" -> 2
                "त्रिः", "त्रिकृत्वः" -> 3
                "चतुः" -> 4
                "पञ्चकृत्वः" -> 5
                "दशकृत्वः" -> 10
                "शतकृत्वः" -> 100
                "बहुकृत्वः" -> 10
                "पुनः", "पुनर्" -> 2
                else -> null
            }
        }
        return null
    }

    private fun getActionRoot(stem: String): String {
        val clean = stem.removePrefix("नि").removePrefix("प्र").trimEnd('न', 'म', '्', 'अ')
        return when {
            clean.contains("योज") || clean.contains("योग") || clean.contains("युज") -> "युज्"
            clean.contains("क्षेप") || clean.contains("क्षिप") -> "क्षिप्"
            clean.contains("दा") -> "दा"
            clean.contains("प्रेष") -> "प्रेष्"
            else -> clean
        }
    }

    private fun getDhatuRoot(upadesha: String): String {
        val clean = upadesha.trimEnd('ँ', '्', 'ि', 'र', 'ञ')
        return when {
            clean.contains("युज") -> "युज्"
            clean.contains("क्षिप") -> "क्षिप्"
            clean.contains("दा") -> "दा"
            clean.contains("प्रेष") -> "प्रेष्"
            else -> clean
        }
    }
}
