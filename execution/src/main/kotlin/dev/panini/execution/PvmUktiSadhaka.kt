package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.Lakara
import dev.panini.core.DhatuGana
import dev.panini.core.SamasaType
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.KrdantaEngine
import dev.panini.derivation.KrdantaSourceStem
import dev.panini.derivation.NominalStemFormation
import dev.panini.derivation.SamasaEngine
import dev.panini.derivation.SandhiEngine
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.StriPratyayaEngine
import dev.panini.derivation.StriPratyayaRequest
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.derivation.TaddhitaEngine
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.analysis.SamasaPada
import dev.panini.execution.binding.NumeralAstNormalizer
import dev.panini.execution.binding.toSamasaPada
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.sankhya.SankhyaAbhyasaRenderer
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.sankhya.SankhyaVacana
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.AvyayaKridantaDerivation
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.StriVikara
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.ProgramNodeVisitor
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.accept
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.lexicon.PratipadikaLexicon
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * Pāninian grammatical sādhaka (उक्तिसाधक) using SubantaEngine, TingantaEngine,
 * and DerivationEngine to perform rupa-siddhi (रूपसिद्धि) on segmented PVM ASTs.
 */
class PvmUktiSadhaka(
    private val derivationEngine: DerivationEngine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras),
    private val subantaEngine: SubantaEngine = SubantaEngine(derivationEngine),
    private val tingantaEngine: TingantaEngine = TingantaEngine(derivationEngine),
    private val krdantaEngine: KrdantaEngine = KrdantaEngine(),
    private val samasaEngine: SamasaEngine = SamasaEngine(derivationEngine),
    private val sandhiEngine: SandhiEngine = SandhiEngine(derivationEngine),
    private val striPratyayaEngine: StriPratyayaEngine = StriPratyayaEngine(),
    private val pratipadikaLexicon: PratipadikaLexicon = PaninianPratipadikaLexicon,
    private val parser: PaniniParser = PaniniParser(),
) {

    fun sadhayaScript(scriptContent: String): String {
        val renderedLines = mutableListOf<String>()
        val pendingCode = mutableListOf<String>()
        val pendingComments = mutableListOf<String>()

        fun renderPending() {
            if (pendingCode.isEmpty()) return
            val source = pendingCode.joinToString(" ")
            val surface = try {
                sadhayaLine(source)
            } catch (_: Throwable) {
                source
            }
            renderedLines += if (pendingComments.isEmpty()) {
                surface
            } else {
                "$surface ${pendingComments.joinToString(" ")}"
            }
            pendingCode.clear()
            pendingComments.clear()
        }

        scriptContent.lines().forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.isEmpty() && pendingCode.isEmpty() -> renderedLines += ""
                trimmed.startsWith("#") || trimmed.startsWith("//") -> {
                    if (pendingCode.isEmpty()) renderedLines += line else pendingComments += trimmed
                }
                else -> {
                    val commentIdx = when {
                        trimmed.contains("#") && trimmed.contains("//") -> minOf(trimmed.indexOf('#'), trimmed.indexOf("//"))
                        trimmed.contains("#") -> trimmed.indexOf('#')
                        trimmed.contains("//") -> trimmed.indexOf("//")
                        else -> -1
                    }
                    val codePart = if (commentIdx != -1) trimmed.substring(0, commentIdx).trim() else trimmed
                    val commentPart = if (commentIdx != -1) line.substring(line.indexOf(if (trimmed.contains('#')) '#' else '/')) else ""

                    if (codePart.isEmpty()) {
                        if (pendingCode.isEmpty()) renderedLines += line else if (commentPart.isNotEmpty()) pendingComments += commentPart
                    } else {
                        pendingCode += codePart
                        if (commentPart.isNotEmpty()) pendingComments += commentPart
                        if (codePart.contains('।') || codePart.contains('॥')) renderPending()
                    }
                }
            }
        }
        renderPending()
        return renderedLines.joinToString("\n")
    }

    fun sadhayaLine(lineText: String): String {
        val ukti = parser.parse(lineText)
        val parts = mutableListOf<String>()

        val dandaDelimiter = when {
            lineText.trim().endsWith("॥") -> "॥"
            else -> "।"
        }

        ukti.sambodhana?.let { sambodhana ->
            val header = sambodhana.suchaka?.let { "$it " } ?: ""
            val derivedSub = sadhayaSubanta(sambodhana.subanta)
            parts += "$header$derivedSub,"
        }

        parts += "${applyExternalSandhi(sadhayaProgramNode(ukti.body))} $dandaDelimiter"

        return parts.joinToString(" ")
    }

    private fun sadhayaProgramNode(node: ProgramNode): String = node.accept(programRenderer)

    /** Applies rule-driven external sandhi after every pada has been derived. */
    private fun applyExternalSandhi(text: String): String {
        val words = text.split(' ').filter { it.isNotBlank() }
        if (words.size < 2) return text
        val rendered = mutableListOf(words.first())
        words.drop(1).forEach { right ->
            val left = rendered.removeLast()
            // The readable renderer retains segmented PVM notation when a
            // pada has not been derived. Source operators and punctuation
            // are syntax boundaries, not phonological padas.
            val hasSourceSyntax = listOf(left, right).any { word ->
                word.any { it in "+।॥,():={}[]" }
            }
            rendered += (if (hasSourceSyntax) "$left $right" else sandhiEngine.joinPadas(left, right))
                .split(' ')
                .filter { it.isNotBlank() }
        }
        return rendered.joinToString(" ")
    }

    private val programRenderer = object : ProgramNodeVisitor<String> {
        private fun render(node: ProgramNode): String = node.accept(this)
        override fun visitInvocation(node: Invocation): String = node.implicitValuePada
            ?.let(::sadhayaPada)
            ?: node.implicitValue
            ?: sadhayaPadas(node.vakya.padas)
        override fun visitSequence(node: Sequence): String = buildString {
            node.statements.forEachIndexed { index, statement ->
                if (index > 0) {
                    append(' ')
                    append(node.connectors.getOrNull(index - 1) ?: "।")
                    append(' ')
                }
                append(render(statement))
            }
        }
        override fun visitConditional(node: Conditional): String = renderConditional(node, includePipelineTarget = true)

        private fun renderConditional(node: Conditional, includePipelineTarget: Boolean): String = buildString {
            val hasSharedTarget = includePipelineTarget && node.surfacePipelineTarget != null
            val stripLoweredTargets = hasSharedTarget || !includePipelineTarget
            append("यदि ")
            append(render(node.condition))
            append(" तर्हि ")
            append(renderBranch(node.consequent, stripPipelineTarget = stripLoweredTargets))
            node.alternate?.let {
                append(" अन्यथा ")
                append(renderBranch(it, stripPipelineTarget = stripLoweredTargets))
            }
            if (hasSharedTarget) {
                append(" ततः ")
                append(render(requireNotNull(node.surfacePipelineTarget)))
            }
        }

        private fun renderBranch(node: ProgramNode, stripPipelineTarget: Boolean): String = when {
            !stripPipelineTarget -> render(node)
            node is Conditional -> renderConditional(node, includePipelineTarget = false)
            node is Sequence && node.connectorKinds.lastOrNull() == SequenceConnector.ANANTARYA ->
                render(node.statements.first())
            else -> render(node)
        }
        override fun visitQuotation(node: Quotation): String =
            "${sadhayaPadas(node.quoted.vakya.padas)} इति ${node.reporting.accept(this)}"
        override fun visitRepeat(node: Repeat): String = render(node.body)
        override fun visitWhileLoop(node: WhileLoop): String = buildString {
            if (node.maximumIterationStems.isNotEmpty() && node.maximumBoundaryPadas.isEmpty()) {
                val count = sankhyaEvaluator.evaluateStems(node.maximumIterationStems).value
                append(sankhyaAbhyasaRenderer.render("कृत्वसुच्", count))
                append(' ')
            }
            append("यावत् ")
            append(sadhayaPadas(node.condition.vakya.padas))
            append(" तावत् ")
            if (node.maximumBoundaryPadas.isNotEmpty()) {
                append(sadhayaPadas(node.maximumBoundaryPadas))
                append(' ')
            }
            append(render(node.body))
            node.exhausted?.let {
                append(" अन्यथा ")
                append(render(it))
            }
            node.resultTarget?.let {
                append(" ततः ")
                append(render(it))
            }
        }
        override fun visitPipeline(node: Pipeline): String =
            sadhayaPadas(node.renderPadas)
        override fun visitPrakriya(node: Prakriya): String = node.sourceText
        override fun visitScope(node: Scope): String = node.sourceText
    }

    private val sankhyaEvaluator = SankhyaEvaluator()
    private val sankhyaGenerator = SankhyaGenerator()
    private val sankhyaAbhyasaRenderer = SankhyaAbhyasaRenderer()
    private val taddhitaEngine = TaddhitaEngine()

    private fun sadhayaPadas(padas: List<Pada>): String =
        padas.mapIndexed { index, pada ->
            sadhayaPada(pada, numeralAgreementLinga(padas, index) ?: predicateAgreementLinga(padas, index))
        }.joinToString(" ")

    /** A predicative adjective agrees with the nominative subject of the copular clause. */
    private fun predicateAgreementLinga(padas: List<Pada>, index: Int): Linga? {
        val predicate = padas.getOrNull(index) as? SubantaPada ?: return null
        if ((predicate.pratipadika as? MulaPratipadika)?.lexicalIdentity !in setOf(
                MulaPratipadikaIdentity.SAMA,
                MulaPratipadikaIdentity.NYUNA,
                MulaPratipadikaIdentity.ADHIKA,
                MulaPratipadikaIdentity.SAMAPTA,
                MulaPratipadikaIdentity.GUPTA,
            )
        ) return null
        val predicateSup = SupAffix.fromUpadesha(predicate.sup.text) ?: return null
        if (predicateSup.vibhakti != Vibhakti.PRATHAMA) return null
        val subject = padas.firstOrNull { candidate ->
            candidate !== predicate && supAffixOf(candidate)?.vibhakti == Vibhakti.PRATHAMA
        } ?: return null
        if (numeralValue(subject) != null) {
            return numeralIntrinsicLinga(subject)
                ?: numeralAgreementLinga(padas, padas.indexOf(subject)) ?: Linga.NAPUMSAKA
        }
        if (subject !is SubantaPada) return null
        return pratipadikaLexicon.findPratipadika(subject.pratipadika.baseText())?.linga?.singleOrNull()
            ?: Linga.NAPUMSAKA.takeIf {
                (subject.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.PHALA
            }
    }

    private fun numeralAgreementLinga(padas: List<Pada>, index: Int): Linga? {
        val numeral = padas.getOrNull(index) ?: return null
        val numeralSup = supAffixOf(numeral) ?: return null
        if (numeralValue(numeral) == null) return null
        numeralIntrinsicLinga(numeral)?.let { return it }

        val counted = padas.getOrNull(index + 1) as? SubantaPada ?: return null
        if (NumeralAstNormalizer.resolve(counted.pratipadika) != null) return null
        val countedSup = SupAffix.fromUpadesha(counted.sup.text) ?: return null
        if (countedSup.vibhakti != numeralSup.vibhakti || countedSup.vacana != numeralSup.vacana) return null

        if ((counted.pratipadika as? MulaPratipadika)?.vikaras?.any { it is StriVikara } == true) {
            return Linga.STRI
        }

        val countedBase = counted.pratipadika.baseText()
        return pratipadikaLexicon.findPratipadika(countedBase)?.linga?.singleOrNull()
    }

    private fun numeralIntrinsicLinga(pada: Pada): Linga? = when (pada) {
        is SankhyaPada -> runCatching {
            sankhyaGenerator.intrinsicLinga(sankhyaEvaluator.evaluateStems(pada.stems))
        }.getOrNull()
        else -> numeralValue(pada)?.let { sankhyaGenerator.intrinsicLinga(it) }
    }

    private fun numeralValue(pada: Pada): Long? = when (pada) {
        is SankhyaPada -> pada.value ?: runCatching {
            sankhyaEvaluator.evaluateStems(pada.stems).value
        }.getOrNull()
        is SubantaPada -> NumeralAstNormalizer.resolve(pada.pratipadika)?.semanticValue?.value
        else -> null
    }

    private fun supAffixOf(pada: Pada): SupAffix? = when (pada) {
        is SankhyaPada -> SupAffix.fromUpadesha(pada.sup.text)
        is SubantaPada -> SupAffix.fromUpadesha(pada.sup.text)
        else -> null
    }

    fun sadhayaPada(pada: Pada, linga: Linga? = null): String = when (pada) {
        is ParyantaRangePada -> sadhayaParyantaRange(pada)
        is SubantaPada -> sadhayaSubanta(pada, linga)
        is SamuccitaSubanta -> pada.members.joinToString(" ") { sadhayaSubanta(it, linga) } + " च"
        is TingantaPada -> sadhayaTinganta(pada)
        is AvyayaPada -> sadhayaAvyaya(pada)
        is SankhyaPada -> sadhayaSankhya(pada, linga)
        is SankhyaPuranaPada -> sadhayaSankhyaPurana(pada)
        is SankhyaAbhyasaPada -> sadhayaSankhyaAbhyasa(pada)
        is KatapayadiPada -> sadhayaEncodedNumeral(pada.word, pada.sup)
        is AryabhatiyaPada -> sadhayaEncodedNumeral(pada.word, pada.sup)
        is BhutasamkhyaPada -> sadhayaEncodedNumeral(pada.terms.joinToString(""), pada.sup)
    }

    /** The numeral notation selects a value; its written code-word still bears the parsed sup. */
    private fun sadhayaEncodedNumeral(stem: String, sup: dev.panini.vyakaranam.ast.SupPratyaya): String {
        val affix = SupAffix.fromUpadesha(sup.text) ?: return stem
        return runCatching {
            subantaEngine.derive(SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana)).final.surface
        }.getOrDefault(stem)
    }

    private fun sadhayaAvyaya(pada: AvyayaPada): String {
        val derivation = pada.derivation as? AvyayaKridantaDerivation ?: return pada.form
        return runCatching {
            when (val stem = krdantaEngine.deriveSourceStem(
                derivation.dhatu.mulaDhatu,
                derivation.pratyaya,
                derivation.dhatu.sanadiPratyayas,
                derivation.upasargas,
            )) {
                is KrdantaSourceStem.Productive -> stem.surface
                is KrdantaSourceStem.Unresolved -> pada.form
            }
        }.getOrDefault(pada.form)
    }

    private fun sadhayaParyantaRange(range: ParyantaRangePada): String {
        val lower = when (val boundary = range.lowerLimit) {
            is SankhyaPada -> sadhayaSankhya(boundary)
            is SankhyaPuranaPada -> sadhayaSankhyaPurana(boundary)
        }
        val upperValue = range.upperLimit.value ?: when (val boundary = range.upperLimit) {
            is SankhyaPada -> sankhyaEvaluator.evaluateStems(boundary.stems).value
            is SankhyaPuranaPada -> requireNotNull(PuranaPratyayaResolver.ordinalValue(boundary))
        }
        val upper = when (range.upperLimit) {
            is SankhyaPada -> sankhyaGenerator.cardinal(upperValue).final.surface
            is SankhyaPuranaPada -> sankhyaGenerator.ordinal(upperValue).final.surface
        }
        val boundary = range.marker.pratipadika.baseText()
        val upperBoundary = samasaEngine.derive(
            padas = listOf(
                SamasaPada(upper, Vibhakti.PRATHAMA, samjnas = setOf(dev.panini.shiksha.Samjna.SANKHYA)),
                SamasaPada(boundary, Vibhakti.PRATHAMA, linga = Linga.NAPUMSAKA),
            ),
            type = SamasaType.KARMADHARAYA,
            outputLinga = Linga.NAPUMSAKA,
        ).final.surface
        return "$lower $upperBoundary"
    }

    fun sadhayaSankhya(pada: SankhyaPada, linga: Linga? = null): String {
        return try {
            val expr = sankhyaEvaluator.evaluateStems(pada.stems)
            val baseText = sankhyaGenerator.cardinal(expr.value).final.surface
            val supAffix = SupAffix.fromUpadesha(pada.sup.text) ?: return baseText
            if (linga == null) {
                sankhyaGenerator.decline(expr, supAffix.vibhakti, supAffix.vacana)
            } else {
                sankhyaGenerator.decline(expr, supAffix.vibhakti, supAffix.vacana, linga)
            }
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSankhyaPurana(pada: SankhyaPuranaPada): String {
        return try {
            val expr = sankhyaEvaluator.evaluateStems(pada.stems)
            val baseText = sankhyaGenerator.ordinal(expr.value).final.surface
            val supAffix = SupAffix.fromUpadesha(pada.sup.text) ?: return baseText
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana)
            subantaEngine.derive(req).final.surface
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSankhyaAbhyasa(pada: SankhyaAbhyasaPada): String {
        return try {
            val lastStem = pada.stems.lastOrNull() ?: return pada.sourceText
            val numStems = sankhyaAbhyasaRenderer.numericStems(pada.stems)
            val count = if (numStems.isNotEmpty()) {
                sankhyaEvaluator.evaluateStems(numStems).value
            } else {
                sankhyaEvaluator.evaluateStems(pada.stems).value
            }
            sankhyaAbhyasaRenderer.render(lastStem, count)
        } catch (_: Throwable) {
            pada.sourceText
        }
    }

    fun sadhayaSubanta(subanta: SubantaPada, lingaOverride: Linga? = null): String {
        val normalized = NumeralAstNormalizer.normalize(subanta)
        val samasa = normalized.pratipadika as? SamasaPratipadika
        if (samasa != null) {
            return try {
                val padas = samasa.angas.map { anga ->
                    val vibhakti = anga.sup?.text
                        ?.let { SupAffix.fromUpadesha(it)?.vibhakti }
                        ?: Vibhakti.PRATHAMA
                    anga.pratipadika.toSamasaPada(anga.pratipadika.baseText(), vibhakti)
                }
                val outerAffix = SupAffix.fromUpadesha(normalized.sup.text)
                    ?: return subanta.sourceText
                samasaEngine.derive(
                    padas, SamasaType.TATPURUSA,
                    outputLinga = lingaOverride,
                    outputVacana = outerAffix.vacana,
                    outputVibhakti = outerAffix.vibhakti,
                ).final.surface
            } catch (_: Exception) {
                subanta.sourceText
            }
        }
        val kridanta = normalized.pratipadika as? KridantaPratipadika
        val sourceStem = kridanta?.let {
            krdantaEngine.deriveSourceStem(
                it.dhatu.mulaDhatu,
                it.krtPratyaya,
                it.dhatu.sanadiPratyayas,
                it.upasargas,
            )
        }
        val mula = normalized.pratipadika as? MulaPratipadika
        val possessiveTaddhitas = mula?.vikaras?.filterIsInstance<TaddhitaVikara>()?.filter {
            it.pratyayaClass == TaddhitaPratyayaClass.POSSESSIVE
        }.orEmpty()
        if (possessiveTaddhitas.size > 1) return subanta.sourceText
        val possessiveTaddhita = possessiveTaddhitas.singleOrNull()
        val taddhitaDerivation = possessiveTaddhita?.let {
            val source = requireNotNull(mula).text
            runCatching { taddhitaEngine.derive(source, dev.panini.shiksha.Samjna.MATUP) }
                    .getOrNull()
        }
        val taddhitaBase = taddhitaDerivation?.final?.surface
        val sourceAffixItMarkers = taddhitaDerivation?.final?.allEffectiveTerms
            ?.filter { it.kind == dev.panini.derivation.TermKind.PRATYAYA }
            ?.flatMap { it.itMarkers + it.sthaniProps?.itMarkers.orEmpty() }?.toSet().orEmpty()
        if (possessiveTaddhita != null && taddhitaBase == null) return subanta.sourceText
        val baseText = when {
            taddhitaBase != null -> taddhitaBase
            else -> sourceStem?.surface ?: normalized.pratipadika.baseText()
        }
        val striVikaras = (normalized.pratipadika as? MulaPratipadika)
            ?.vikaras
            ?.filterIsInstance<StriVikara>()
            .orEmpty()
        if (striVikaras.size > 1) return subanta.sourceText
        val explicitStri = striVikaras.singleOrNull()
        val explicitlyFeminineBase = explicitStri?.let { vikara ->
            val samjna = when (vikara.pratyaya) {
                "टाप्" -> dev.panini.shiksha.Samjna.TAP
                "ङीप्" -> dev.panini.shiksha.Samjna.NIP
                "ङीष्" -> dev.panini.shiksha.Samjna.NIS
                "ङीन्" -> dev.panini.shiksha.Samjna.NIN
                else -> return subanta.sourceText
            }
            runCatching { striPratyayaEngine.derive(StriPratyayaRequest(baseText, samjna, sourceAffixItMarkers)).final.surface }
                .getOrNull() ?: return subanta.sourceText
        }
        val lexicalSurface = taddhitaBase ?: (normalized.pratipadika as? MulaPratipadika)?.text ?: baseText
        val feminineAaBase = when {
            lexicalSurface.endsWith("ा") || lexicalSurface.endsWith("आ") -> lexicalSurface
            else -> null
        }
        val hasFeminineAaSurface = feminineAaBase != null
        val supAffix = SupAffix.fromUpadesha(normalized.sup.text) ?: return baseText
        when (sourceStem) {
            is dev.panini.derivation.KrdantaSourceStem.Productive -> Unit
            is dev.panini.derivation.KrdantaSourceStem.Unresolved -> return subanta.sourceText
            null -> Unit
        }
        val sankhya = normalized.pratipadika as? SankhyaPratipadika
        val intrinsicNumeralLinga = sankhya?.semanticValue?.value
            ?.let { sankhyaGenerator.intrinsicLinga(it) }
        val lexicalLinga = pratipadikaLexicon.findPratipadika(baseText)?.linga?.singleOrNull()
        val linga = when {
            explicitStri != null -> Linga.STRI
            intrinsicNumeralLinga != null -> intrinsicNumeralLinga
            lingaOverride != null -> lingaOverride
            sankhya != null -> Linga.NAPUMSAKA
            kridanta?.krtPratyaya == "ल्युट्" -> Linga.NAPUMSAKA
            else -> lexicalLinga
                ?: if (hasFeminineAaSurface) Linga.STRI else Linga.PUMS
        }
        return try {
            if (sankhya?.semanticValue != null) {
                SankhyaVacana.requireCompatible(sankhya.semanticValue.value, supAffix.vacana)
                return sankhyaGenerator.decline(sankhya.semanticValue.value, supAffix.vibhakti, supAffix.vacana, linga)
            }
            val derivationBase = explicitlyFeminineBase
                ?: feminineAaBase
                ?: (if (lingaOverride == Linga.STRI && explicitStri == null && lexicalLinga != Linga.STRI) {
                    val feminineAffix = if (dev.panini.core.ItMarker.U in sourceAffixItMarkers) {
                        dev.panini.shiksha.Samjna.NIP
                    } else dev.panini.shiksha.Samjna.TAP
                    striPratyayaEngine.derive(StriPratyayaRequest(baseText, feminineAffix, sourceAffixItMarkers)).final.surface
                } else null)
                ?: baseText
            val stemFormation = if (
                linga == Linga.STRI && (derivationBase.endsWith("ा") || derivationBase.endsWith("आ"))
            ) {
                NominalStemFormation.AP
            } else {
                NominalStemFormation.UNSPECIFIED
            }
            val req = SubantaDerivationRequest(
                derivationBase,
                supAffix.vibhakti,
                supAffix.vacana,
                linga,
                stemFormation,
                sourceSuffixUpadeshas = buildSet {
                    kridanta?.krtPratyaya?.let(::add)
                    mula?.vikaras?.forEach { vikara ->
                        when (vikara) {
                            is TaddhitaVikara -> add(vikara.pratyaya)
                            is StriVikara -> add(vikara.pratyaya)
                            else -> Unit
                        }
                    }
                },
            )
            subantaEngine.derive(req).final.surface
        } catch (e: Exception) {
            subanta.sourceText
        }
    }

    fun sadhayaTinganta(tinganta: TingantaPada): String {
        tinganta.priorAction?.let { derivation ->
            val source = (derivation.upasargas + derivation.dhatu.sourceText + derivation.pratyaya)
                .joinToString(" + ")
            return sadhayaPada(AvyayaPada(source, source, derivation))
        }
        val rawDhatu = tinganta.dhatu.mulaDhatu
        val explicitGana = tinganta.vikarana?.gana
        val matchingDhatus = DhatuPatha.all.filter { candidate ->
            sameDhatuSurface(candidate.upadesha, rawDhatu) ||
                sameDhatuSurface(candidate.derivationalSurface, rawDhatu) ||
                sameDhatuSurface(candidate.sourceSurface, rawDhatu)
        }
        if (explicitGana != null && matchingDhatus.none { it.gana == explicitGana }) {
            return tinganta.sourceText
        }
        val derivationEntry = (
            explicitGana?.let { gana -> matchingDhatus.firstOrNull { it.gana == gana } }
                ?: matchingDhatus.firstOrNull { it.preferredForSourceDerivation }
                ?: matchingDhatus.firstOrNull { it.operations.isNotEmpty() }
                ?: matchingDhatus.singleOrNull()
            )
        val derivationDhatu = derivationEntry?.upadesha ?: rawDhatu
        val tingAffix = TingAffix.fromUpadesha(tinganta.ting.text) ?: return tinganta.sourceText
        val dhatuIdentity = tinganta.canonicalDhatuIdentity()
        val specialSurface = when {
            dhatuIdentity == CanonicalDhatuIdentity.AS &&
                derivationEntry?.gana == DhatuGana.ADADI &&
                tinganta.dhatu.sanadiPratyayas.isEmpty() &&
                tinganta.lakara == Lakara.LAT -> when (tingAffix) {
                TingAffix.TIP -> "अस्ति"
                TingAffix.TAS -> "स्तः"
                TingAffix.JHI -> "सन्ति"
                TingAffix.SIP -> "असि"
                TingAffix.THAS -> "स्थः"
                TingAffix.THA -> "स्थ"
                TingAffix.MIP -> "अस्मि"
                TingAffix.VAS -> "स्वः"
                TingAffix.MAS -> "स्मः"
                else -> null
            }
            else -> null
        }
        if (specialSurface != null) {
            return joinUpasargas(tinganta.upasargas, specialSurface)
        }
        return try {
            val req = TingantaDerivationRequest(
                dhatu = derivationDhatu,
                vacana = tingAffix.vacana,
                purusha = tingAffix.purusha,
                lakara = tinganta.lakara,
                pada = tingAffix.pada,
                sanadiPratyayas = tinganta.dhatu.sanadiPratyayas,
                gana = derivationEntry?.gana,
            )
            val engineSurface = tingantaEngine.derive(req).final.surface
            joinUpasargas(tinganta.upasargas, engineSurface)
        } catch (e: Exception) {
            tinganta.sourceText
        }
    }

    /**
     * Joins an upasarga to an already-derived verbal form. The final म् of
     * सम् undergoes anusvāra and, before a stop, homorganic replacement
     * (Aṣṭādhyāyī 8.3.23 and 8.4.58). Thus सम् + योजय becomes संयोजय and
     * सम् + गणय becomes सङ्गणय rather than mechanical concatenations.
     */
    private fun joinUpasargas(upasargas: List<String>, verbalSurface: String): String {
        if (upasargas.isEmpty()) return verbalSurface

        return upasargas.foldRight(verbalSurface) { prefix, following ->
            joinUpasarga(prefix, following)
        }
    }

    private fun joinUpasarga(finalUpasarga: String, verbalSurface: String): String {
        return sandhiEngine.joinPrefix(finalUpasarga, verbalSurface)
    }

    /** Source spelling may retain the final virāma that a lexicon stem omits. */
    private fun sameDhatuSurface(left: String, right: String): Boolean =
        left == right || left.removeSuffix("्") == right.removeSuffix("्")

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is SankhyaPratipadika -> sourceText
        is KridantaPratipadika -> krdantaEngine.deriveSourceStem(
            dhatu.mulaDhatu,
            krtPratyaya,
            dhatu.sanadiPratyayas,
            upasargas,
        ).surface
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("") { it.pratipadika.baseText() }
    }

}
