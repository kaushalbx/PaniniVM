package dev.panini.analysis

import dev.panini.vyakaranam.ast.*

/** A written word outside any kriyā's frame. */
data class NonKriyaWord(val start: Int, val text: String, val reading: String, val end: Int = -1)

/**
 * Every sentence writes its kriyā. A naming or describing word reports the kāraka it shares through agreement. A word related only to another
 * noun, such as सम्बन्ध or निर्धारण, truly has no kāraka and says so.
 */
object NonKriyaWords {
    const val OWNER = "सम्बन्ध"
    const val SELECTED_FROM = "निर्धारण"
    const val TOGETHER = "सहार्थ"
    const val ADVERB = "क्रियाविशेषण"
    private val kartr get() = dev.panini.core.Karaka.KARTR.sanskritName
    private val karman get() = dev.panini.core.Karaka.KARMAN.sanskritName
    private val karana get() = dev.panini.core.Karaka.KARANA.sanskritName
    val KARTR_NAME get() = "$kartr-समानाधिकरण: सञ्ज्ञा"
    val KARTR_SAME get() = "$kartr-समानाधिकरण"
    val KARMAN_SAME get() = "$karman-समानाधिकरण"
    val KARTR_QUALIFIER get() = "$kartr-विशेषण"
    val KARMAN_QUALIFIER get() = "$karman-विशेषण"
    val KARTR_PREDICATE get() = "$kartr-विधेय"

    /** What a reading may be: a kāraka, a word tied to one, or a relation that truly has none. */
    val allowedReadings: Set<String> get() = setOf(OWNER, SELECTED_FROM, TOGETHER, ADVERB, kartr, karman, karana,
        KARTR_NAME, KARTR_SAME, KARMAN_SAME, KARTR_QUALIFIER, KARMAN_QUALIFIER, KARTR_PREDICATE)

    fun of(document: ProgramDocument, analyze: (String) -> UktiAnalysis?): List<NonKriyaWord> = document.items.flatMapIndexed { index, item ->
        val span = document.itemSpans.getOrNull(index)
        when (item) {
            is Ukti -> walk(item.body, analyze)
            is Prakriya -> {
                val bodies = document.prakriyaBodySpans[index].orEmpty()
                val headerEnd = bodies.minOfOrNull { it.start } ?: span?.endExclusive
                val header = if (span != null && headerEnd != null) declaration(document.sourceText, span.start, headerEnd, analyze) else emptyList()
                header + item.body.flatMap { walk(it, analyze) }
            }
            is Scope, is RangeDeclaration -> span?.let { declaration(document.sourceText, it.start, it.endExclusive, analyze) }.orEmpty()
            else -> emptyList()
        }
    }

    /** One statement at a time, read from its frame. A quoted word has no relation of its own: it is what the कर्ता names or contains. */
    private fun declaration(source: String, start: Int, end: Int, analyze: (String) -> UktiAnalysis?): List<NonKriyaWord> {
        var offset = start
        // A comment is not program text. Blanking it keeps every later offset true.
        val code = source.substring(start, end).replace(Regex("#[^\\r\\n]*")) { " ".repeat(it.value.length) }
        return code.split(Regex("(?<=[।॥])")).flatMap { statement ->
            val at = offset
            offset += statement.length
            val analysis = statement.takeIf { it.isNotBlank() }?.let { analyze(it.trim()) } ?: return@flatMap emptyList()
            val quoted = (analysis.ukti.body as? Quotation)?.quoted?.vakya?.padas.orEmpty().filterNot { it is TingantaPada || it is AvyayaPada }
            val named = quoted.count { !isGenitive(it) }
            val readings = quoted.map { pada ->
                pada to when {
                    isGenitive(pada) -> OWNER
                    named == 1 && pada !is ParyantaRangePada -> KARTR_NAME
                    else -> sharedRole(analysis) ?: KARTR_SAME
                }
            } + analysis.frames.flatMap { frame ->
                frame.vakya.padas.mapNotNull { pada ->
                    frame.relations.firstOrNull { it.participant.pada === pada }?.let { relation -> frame.karakaLabel(relation)?.let { pada to it } }
                }
            }
            var cursor = 0
            readings.mapNotNull { (pada, reading) ->
                val text = pada.sourceText.replace(" ", "")
                val found = Regex(text.map { Regex.escape(it.toString()) }.joinToString("\\s*")).find(statement, cursor) ?: return@mapNotNull null
                cursor = found.range.last + 1
                NonKriyaWord(at + found.range.first, text, reading, at + found.range.last + 1)
            }
        }
    }

    private fun isGenitive(pada: Pada) = pada is SubantaPada && pada.sup.supports(dev.panini.core.Vibhakti.SASTHI)

    /** Quoted words are the content of whichever of the kartā or karman of the reporting verb is written first. */
    private fun sharedRole(analysis: UktiAnalysis?): String? = analysis?.frames.orEmpty().firstNotNullOfOrNull { frame ->
        frame.vakya.padas.firstNotNullOfOrNull { pada ->
            when ((frame.relations.firstOrNull { it.participant.pada === pada }?.resolution as? FrameKarakaResolution.Resolved)?.karaka) {
                dev.panini.core.Karaka.KARTR -> KARTR_SAME
                dev.panini.core.Karaka.KARMAN -> KARMAN_SAME
                else -> null
            }
        }
    }

    private fun walk(node: ProgramNode?, analyze: (String) -> UktiAnalysis?): List<NonKriyaWord> = when (node) {
        // … इति सूचीं स्थापय: the quoted words are the content of the object; … इति सूची अस्ति, of the subject.
        is Quotation -> located(node.quoted.vakya.padas, sharedRole(analyze(node.sourceText.trim().let { if (it.endsWith("।") || it.endsWith("॥")) it else "$it ।" })) ?: KARTR_SAME) + walk(node.reporting, analyze)
        is Invocation ->
            // एकस्मात् षट्पर्यन्तं सङ्ख्यां चित्वा: the range qualifies the object it agrees with.
            located(node.vakya.padas.filterIsInstance<ParyantaRangePada>(), KARMAN_QUALIFIER) +
                // षट्कृत्वः, त्रिः (5.4.17, 5.4.18): an indeclinable counting how often the action happens.
                located(node.vakya.padas.filterIsInstance<SankhyaAbhyasaPada>(), ADVERB)
        // दत्तसूच्याः एकैकां सङ्ख्यां तदुत्तराभिः योजय: joined with its neighbours, an instrumental by 2.3.19.
        is CollectionRelativeAction -> located(listOf(node.collection), SELECTED_FROM) +
            located(listOf(node.member), karman) + located(listOf(node.quantifier), KARMAN_QUALIFIER) +
            located(listOf(node.operand.relative) + listOfNotNull(node.operand.memberType.takeIf { node.operand.memberTypeWritten }), TOGETHER) +
            walk(node.continuation, analyze)
        // सूच्याः कश्चित् सदस्यः: the genitive names the group selected from, 2.3.41.
        is QuantifiedCollectionPredicate -> located(listOf(node.collection), SELECTED_FROM) +
            located(listOf(node.member), kartr) + walk(node.predicate, analyze)
        is Pipeline -> (node.argumentPadas + node.renderPadas).flatMap { pada ->
            // गणितस्य गणनेन कुरु: read each word from its case; any other case fails the source test.
            val case = pada.sourceText.replace(" ", "").substringAfterLast("+")
            located(listOf(pada), when (case) {
                "अम्", "औट्", "शस्" -> karman
                "टा", "भ्याम्", "भिस्" -> karana
                "ङस्", "ओस्", "आम्" -> OWNER
                else -> "अनिश्चित"
            })
        } + node.stages.map { NonKriyaWord(-1, it.sourceText.replace(" ", ""), karana) }
        is Sequence -> node.statements.flatMap { walk(it, analyze) }
        is Conditional -> walk(node.consequent, analyze) + walk(node.alternate, analyze)
        // पञ्चमस्य प्रयत्नस्य पर्यन्तम्: the limit qualifies the repeating itself.
        is WhileLoop -> located(node.maximumBoundaryPadas + listOfNotNull(node.maximumFrequency), ADVERB) + walk(node.body, analyze) + walk(node.exhausted, analyze)
        is Repeat -> walk(node.body, analyze)
        is Recurrence -> walk(node.body, analyze)
        is ForEach -> walk(node.body, analyze)
        else -> emptyList()
    }

    private fun located(padas: List<Pada>, reading: String) = padas.filterNot { it is TingantaPada || it is AvyayaPada }.map { pada ->
        NonKriyaWord((pada as? SourceLocatedNode)?.sourceSpan?.start ?: -1, pada.sourceText.replace(" ", ""), reading, (pada as? SourceLocatedNode)?.sourceSpan?.endExclusive ?: -1)
    }
}
