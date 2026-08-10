package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.KrdantaEngine
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import dev.panini.derivation.TingantaDerivationRequest
import dev.panini.derivation.TingantaEngine
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.sankhya.SankhyaAbhyasaRenderer
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
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
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.ProgramNodeVisitor
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.accept
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.lexicon.PratipadikaLexicon
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon
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
    private val pratipadikaLexicon: PratipadikaLexicon = StandardPratipadikaLexicon,
    private val parser: PaniniParser = PaniniParser(),
) {

    fun sadhayaScript(scriptContent: String): String {
        return scriptContent.lines().joinToString("\n") { line ->
            val trimmed = line.trim()
            when {
                trimmed.isEmpty() -> ""
                trimmed.startsWith("#") || trimmed.startsWith("//") -> line
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
                        line
                    } else {
                        val hasDanda = codePart.endsWith("।") || codePart.endsWith("॥") || codePart.contains("।") || codePart.contains("॥")
                        var surface = try { sadhayaLine(codePart) } catch (_: Throwable) { codePart }
                        if (!hasDanda) {
                            surface = surface.replace("॥", "").replace("।", "").replace(Regex("\\s+"), " ").trim()
                        }
                        if (commentPart.isNotEmpty()) "$surface $commentPart" else surface
                    }
                }
            }
        }
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

        parts += "${sadhayaProgramNode(ukti.body)} $dandaDelimiter"

        return parts.joinToString(" ")
    }

    private fun sadhayaProgramNode(node: ProgramNode): String = node.accept(programRenderer)

    private val programRenderer = object : ProgramNodeVisitor<String> {
        private fun render(node: ProgramNode): String = node.accept(this)
        override fun visitInvocation(node: Invocation): String = node.implicitValue
            ?: node.vakya.padas.joinToString(" ") { pada -> sadhayaPada(pada) }
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
            node is Sequence && node.connectors.lastOrNull() == "ततः" -> render(node.statements.first())
            else -> render(node)
        }
        override fun visitQuotation(node: Quotation): String =
            "${node.quoted.vakya.padas.joinToString(" ") { sadhayaPada(it) }} इति ${node.reporting.accept(this)}"
        override fun visitRepeat(node: Repeat): String = render(node.body)
        override fun visitWhileLoop(node: WhileLoop): String = buildString {
            if (node.maximumIterationStems.isNotEmpty()) {
                append(node.maximumIterationStems.joinToString(" + "))
                append(" + कृत्वः ")
            }
            append("यावत् ")
            append(node.condition.vakya.padas.joinToString(" ") { sadhayaPada(it) })
            append(" तावत् ")
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
            node.renderPadas.joinToString(" ") { sadhayaPada(it) }
        override fun visitProcedure(node: Procedure): String = node.sourceText
        override fun visitScope(node: Scope): String = node.sourceText
    }

    private val sankhyaEvaluator = SankhyaEvaluator()
    private val sankhyaGenerator = SankhyaGenerator()
    private val sankhyaAbhyasaRenderer = SankhyaAbhyasaRenderer()

    fun sadhayaPada(pada: Pada): String = when (pada) {
        is SubantaPada -> sadhayaSubanta(pada)
        is SamuccitaSubanta -> pada.members.joinToString(" ") { sadhayaSubanta(it) } + " च"
        is TingantaPada -> sadhayaTinganta(pada)
        is AvyayaPada -> pada.form
        is SankhyaPada -> sadhayaSankhya(pada)
        is SankhyaPuranaPada -> sadhayaSankhyaPurana(pada)
        is SankhyaAbhyasaPada -> sadhayaSankhyaAbhyasa(pada)
        is KatapayadiPada -> pada.sourceText
        is AryabhatiyaPada -> pada.sourceText
        is BhutasamkhyaPada -> pada.sourceText
    }

    fun sadhayaSankhya(pada: SankhyaPada): String {
        return try {
            val expr = sankhyaEvaluator.evaluateStems(pada.stems)
            val baseText = sankhyaGenerator.cardinal(expr.value).final.surface
            val supAffix = SupAffix.fromUpadesha(pada.sup.text) ?: return baseText
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana)
            subantaEngine.derive(req).final.surface
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

    fun sadhayaSubanta(subanta: SubantaPada): String {
        val kridanta = subanta.pratipadika as? KridantaPratipadika
        val sourceStem = kridanta?.let {
            krdantaEngine.deriveSourceStem(it.dhatu.mulaDhatu, it.krtPratyaya)
        }
        val baseText = sourceStem?.surface ?: subanta.pratipadika.baseText()
        val supAffix = SupAffix.fromUpadesha(subanta.sup.text) ?: return baseText
        if (sourceStem?.supportsAStemDeclension == true) {
            pvmKridantaSurface(baseText, supAffix)?.let { return it }
        } else if (sourceStem?.preservesSourceSurface == true) {
            return baseText
        }
        val linga = pratipadikaLexicon.findPratipadika(baseText)?.linga?.singleOrNull() ?: Linga.PUMS
        return try {
            val req = SubantaDerivationRequest(baseText, supAffix.vibhakti, supAffix.vacana, linga)
            subantaEngine.derive(req).final.surface
        } catch (e: Exception) {
            baseText
        }
    }

    /** Stable a-stem forms for the PVM's action/state krdantas. */
    private fun pvmKridantaSurface(stem: String, affix: SupAffix): String? {
        return when (affix) {
            SupAffix.AM -> "${stem}म्"
            SupAffix.NGE -> "${stem}ाय"
            SupAffix.NGAS -> "${stem}स्य"
            else -> null
        }
    }

    fun sadhayaTinganta(tinganta: TingantaPada): String {
        val rawDhatu = tinganta.dhatu.mulaDhatu
        val derivationDhatu = DhatuPatha.all.firstOrNull { candidate ->
            candidate.preferredForSourceDerivation &&
                (candidate.upadesha == rawDhatu || candidate.derivationalSurface == rawDhatu || candidate.sourceSurface == rawDhatu)
        }?.upadesha ?: rawDhatu
        val tingAffix = TingAffix.fromUpadesha(tinganta.ting.text) ?: return rawDhatu
        return try {
            val useSanadiEngine = tingantaEngine.supportsSanadi(
                derivationDhatu,
                tinganta.dhatu.sanadiPratyayas,
                tingAffix.pada,
            )
            val req = TingantaDerivationRequest(
                dhatu = derivationDhatu,
                vacana = tingAffix.vacana,
                purusha = tingAffix.purusha,
                lakara = tinganta.lakara,
                pada = tingAffix.pada.takeIf { useSanadiEngine },
                sanadiPratyayas = tinganta.dhatu.sanadiPratyayas.takeIf { useSanadiEngine }.orEmpty(),
            )
            val derived = tingantaEngine.derive(req).final.surface
            if (tinganta.upasargas.isNotEmpty()) {
                tinganta.upasargas.joinToString("") + derived
            } else {
                derived
            }
        } catch (e: Exception) {
            rawDhatu
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is SankhyaPratipadika -> sourceText
        is KridantaPratipadika -> krdantaEngine.deriveSourceStem(dhatu.mulaDhatu, krtPratyaya).surface
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("") { it.pratipadika.baseText() }
    }

}
