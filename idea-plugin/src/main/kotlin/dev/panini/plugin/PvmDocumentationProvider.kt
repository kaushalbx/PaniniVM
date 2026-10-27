package dev.panini.plugin

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.PrakriyaSignatureCompiler
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.ganapatha.GanaPatha
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.aryabhatiya.AryabhatiyaMapping
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.katapayadi.KatapayadiMapping
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaLexicon
import dev.panini.core.KrtAffix
import dev.panini.core.Lakara
import dev.panini.core.SanadiAffix
import dev.panini.core.SupAffix
import dev.panini.core.TaddhitaAffix
import dev.panini.core.TingAffix
import dev.panini.core.Upasarga
import dev.panini.linganushasanam.LinganushasanamRegistry
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import dev.panini.unadipatha.UnadiPathaCatalog
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.ast.morphologicalKey
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.PrakriyaVisibility

class PvmDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val target = originalElement ?: element ?: return null
        val text = target.text.trim()
        if (text.isEmpty()) return null

        projectPrakriyaDoc(target)?.let { return it }
        canonicalPadaDoc(target)?.let { return it }

        // 0. Check for Sandhi Expression containing '+'
        val parentText = target.parent?.text?.trim() ?: ""
        val candidateExpr = if (parentText.contains("+")) parentText else if (text.contains("+")) text else ""
        if (candidateExpr.isNotEmpty()) {
            val sandhiDoc = getSandhiDoc(candidateExpr)
            if (sandhiDoc != null) {
                return sandhiDoc
            }
        }

        // 1. Check Gaṇapāṭha by Gaṇa Name
        val ganasByName = GanaPatha.all.filter { it.name.contains(text, ignoreCase = true) || text.contains(it.name, ignoreCase = true) }
        if (ganasByName.isNotEmpty()) {
            val sb = StringBuilder("<html><body><h2>Gaṇapāṭha Catalog Entry</h2>")
            ganasByName.forEach { gana ->
                sb.append("<h3>${gana.name} (${gana.sutra})</h3>")
                sb.append("<p><b>Members (${gana.members.size}):</b> ")
                sb.append(gana.members.take(15).joinToString(", ") { it.text })
                if (gana.members.size > 15) sb.append("... and ${gana.members.size - 15} more.")
                sb.append("</p>")
            }
            sb.append("</body></html>")
            return sb.toString()
        }

        // 2. Check Gaṇapāṭha by Member Word
        val containingGanas = GanaPatha.ganasContaining(text)
        if (containingGanas.isNotEmpty()) {
            val sb = StringBuilder("<html><body><h2>Gaṇapāṭha Member: $text</h2>")
            sb.append("<p>This word is listed in the following Paninian Gaṇas:</p><ul>")
            containingGanas.forEach { gana ->
                sb.append("<li><b>${gana.name}</b> (Sūtra: ${gana.sutra})</li>")
            }
            sb.append("</ul></body></html>")
            return sb.toString()
        }

        // 3. Check DhatuPatha catalog
        val dhatu = DhatuPatha.all.firstOrNull {
            it.upadesha == text || it.sourceSurface == text || it.surfaceAliases.contains(text)
        }
        if (dhatu != null) {
            return """
                <html>
                <body>
                <h2>Dhātu: ${dhatu.upadesha} (${dhatu.sourceSurface})</h2>
                <p><b>Meaning (English):</b> ${dhatu.arthaEnglish}</p>
                <p><b>Meaning (Hindi):</b> ${dhatu.arthaHindi}</p>
                <p><b>Meaning (Sanskrit):</b> ${dhatu.artha}</p>
                <ul>
                    <li><b>Gaṇa:</b> ${dhatu.gana}</li>
                    <li><b>Pada:</b> ${dhatu.pada ?: "Default"}</li>
                    <li><b>It Status:</b> ${dhatu.itStatus ?: "N/A"}</li>
                    <li><b>Karmatva:</b> ${dhatu.karmatva ?: "N/A"}</li>
                    <li><b>Accent:</b> ${dhatu.svara ?: "N/A"}</li>
                </ul>
                <p><i>Aliases:</i> ${dhatu.surfaceAliases.joinToString(", ")}</p>
                </body>
                </html>
            """.trimIndent()
        }

        // 4. Check the complete Uṇādipāṭha catalog by affix or sūtra text.
        val unadiEntries = UnadiPathaCatalog.sutras.filter {
            it.pratyaya == text || it.text == text || (text.length >= 3 && it.text.contains(text))
        }.take(12)
        if (unadiEntries.isNotEmpty()) {
            return buildString {
                append("<html><body><h2>Uṇādipāṭha</h2><ul>")
                unadiEntries.forEach {
                    append("<li><b>${it.number}</b> ${it.text}<br/><b>Pratyaya:</b> ${it.pratyaya}</li>")
                }
                append("</ul></body></html>")
            }
        }

        // 5. Check the canonical Liṅgānuśāsanam corpus.
        val lingaSutras = LinganushasanamRegistry.canonicalSutras.filter {
            it.text == text || (text.length >= 3 && it.text.contains(text))
        }.take(12)
        if (lingaSutras.isNotEmpty()) {
            return buildString {
                append("<html><body><h2>Liṅgānuśāsanam</h2><ul>")
                lingaSutras.forEach {
                    append("<li><b>${it.number}</b> ${it.text}<br/>")
                    append("<b>Liṅga:</b> ${it.targetLingas.joinToString()}<br/>")
                    append("${it.hindiExplanation}</li>")
                }
                append("</ul></body></html>")
            }
        }

        // 6. Check Sup / Ting affixes
        val affixDoc = getAffixDoc(text)
        if (affixDoc != null) {
            return affixDoc
        }

        // 7. Check canonical upasarga identities.
        val upasarga = Upasarga.fromUpadesha(text)
        if (upasarga != null) {
            return """
                <html><body><h3>Upasarga</h3>
                <p><b>${upasarga.upadesha}</b> (${upasarga.name}) is a canonical prādi identity used as an upasarga in verbal composition.</p>
                </body></html>
            """.trimIndent()
        }

        // 8. Check Control keywords
        val keywordDoc = getKeywordDoc(text)
        if (keywordDoc != null) {
            return keywordDoc
        }

        // 9. Check Bhutasamkhya
        val bhutaDoc = getBhutasamkhyaDoc(text)
        if (bhutaDoc != null) {
            return bhutaDoc
        }

        // 10. Check Aryabhatiya
        val aryaDoc = getAryabhatiyaDoc(text)
        if (aryaDoc != null) {
            return aryaDoc
        }

        // 11. Check Katapayadi
        val kataDoc = getKatapayadiDoc(text)
        if (kataDoc != null) {
            return kataDoc
        }

        return null
    }

    private fun canonicalPadaDoc(target: PsiElement): String? {
        var structural: PsiElement? = target
        while (structural != null && structural.node.elementType !in setOf(
                PvmTokenTypes.SUBANTA,
                PvmTokenTypes.TINGANTA,
            )) {
            structural = structural.parent
        }
        val node = structural ?: return null
        val file = node.containingFile as? PvmFile ?: return null
        return when (val pada = file.canonicalPadaAt(node.textRange.startOffset)) {
            is SubantaPada -> subantaDoc(pada)
            is TingantaPada -> tingantaDoc(pada)
            else -> null
        }
    }

    private fun subantaDoc(pada: SubantaPada): String = buildString {
        val stem = pada.pratipadika.semanticKey()
        val lexical = runCatching { PaninianPratipadikaLexicon.findPratipadika(stem) }.getOrNull()
        append("<html><body><h2>Subanta: ").append(html(pada.sourceText)).append("</h2><ul>")
        append("<li><b>Prātipadika:</b> <code>").append(html(stem)).append("</code></li>")
        append("<li><b>Morphology:</b> <code>")
            .append(html(pada.pratipadika.morphologicalKey())).append("</code></li>")
        val slots = pada.sup.affixCandidates.joinToString(", ") {
            "${it.vibhakti.name.lowercase()} ${it.vacana.name.lowercase()}"
        }.ifBlank { "unresolved (${pada.sup.text})" }
        append("<li><b>Sup:</b> <code>").append(html(pada.sup.text)).append("</code> — ")
            .append(html(slots)).append("</li>")
        lexical?.let {
            if (it.linga.isNotEmpty()) append("<li><b>Liṅga:</b> ")
                .append(html(it.linga.joinToString { value -> value.name.lowercase() })).append("</li>")
            if (it.lingaRuleIds.isNotEmpty()) append("<li><b>Liṅgānuśāsanam:</b> ")
                .append(html(it.lingaRuleIds.joinToString())).append("</li>")
            if (it.ganaNames.isNotEmpty()) append("<li><b>Gaṇapāṭha:</b> ")
                .append(html(it.ganaNames.joinToString())).append("</li>")
        }
        when (val pratipadika = pada.pratipadika) {
            is UnadyantaPratipadika -> {
                val sutras = UnadiPathaCatalog.sutras.filter { it.pratyaya == pratipadika.unadiPratyaya }
                append("<li><b>Uṇādi pratyaya:</b> ").append(html(pratipadika.unadiPratyaya))
                if (sutras.isNotEmpty()) append(" — ").append(html(sutras.take(4).joinToString { it.number }))
                append("</li>")
            }
            is KridantaPratipadika -> append("<li><b>Kṛt derivation:</b> ")
                .append(html(pratipadika.dhatu.mulaDhatu)).append(" + ")
                .append(html(pratipadika.krtPratyaya)).append("</li>")
            else -> Unit
        }
        append("</ul></body></html>")
    }

    private fun tingantaDoc(pada: TingantaPada): String = buildString {
        val dhatu = DhatuPatha.all.firstOrNull {
            it.upadesha == pada.dhatu.mulaDhatu || it.sourceSurface == pada.dhatu.mulaDhatu ||
                pada.dhatu.mulaDhatu in it.surfaceAliases
        }
        append("<html><body><h2>Tiṅanta: ").append(html(pada.sourceText)).append("</h2><ul>")
        if (pada.upasargas.isNotEmpty()) append("<li><b>Upasarga:</b> ")
            .append(html(pada.upasargas.joinToString(" + "))).append("</li>")
        append("<li><b>Dhātu:</b> <code>").append(html(pada.dhatu.mulaDhatu)).append("</code>")
        dhatu?.let { append(" — ").append(html(it.arthaEnglish.ifBlank { it.artha })) }
        append("</li>")
        dhatu?.let {
            append("<li><b>Dhātupāṭha:</b> ").append(html(it.upadesha))
                .append("; ").append(html(it.gana.name.lowercase())).append(" gaṇa</li>")
        }
        if (pada.dhatu.sanadiPratyayas.isNotEmpty()) append("<li><b>Sanādi:</b> ")
            .append(html(pada.dhatu.sanadiPratyayas.joinToString(" + "))).append("</li>")
        pada.vikarana?.let { append("<li><b>Vikaraṇa:</b> ").append(html(it.upadesha)).append("</li>") }
        append("<li><b>Lakāra:</b> ").append(html(pada.lakara.upadesha))
            .append(" (").append(pada.lakara.name.lowercase()).append(")</li>")
        val ting = pada.ting.affix
        append("<li><b>Tiṅ:</b> ").append(html(pada.ting.text))
        ting?.let {
            append(" — ${it.pada.name.lowercase()}, ${it.purusha.name.lowercase()}, ${it.vacana.name.lowercase()}")
        }
        append("</li></ul></body></html>")
    }

    private fun projectPrakriyaDoc(target: PsiElement): String? {
        val currentFile = target.containingFile as? PvmFile ?: return null
        val currentDocument = currentFile.canonicalDocument() ?: return null
        val knownOperationStems = PvmProjectFileIndex.knownOperationStems(currentFile)
        val identity = PvmSymbolResolver.prakriyaIdentityAt(
            currentDocument, target.textOffset, knownOperationStems,
        ) ?: return null
        val documents = PvmProjectFileIndex.documentsForIdentity(currentFile, identity)

        val definitions = buildList<Triple<PvmFile, PvmScriptStatement.PrakriyaDefinition, String?>> {
            documents.forEach { (sourceFile, document) ->
                var activeDomain: String? = null
                PvmScript.fromDocument(document).forEach statementLoop@{ statement ->
                    if (statement is PvmScriptStatement.AdhikaraDefinition) {
                        activeDomain = statement.scope.domainIdentity
                        return@statementLoop
                    }
                    val definition = statement as? PvmScriptStatement.PrakriyaDefinition
                        ?: return@statementLoop
                    val prakriya = definition.prakriya
                    val domain = prakriya.domainIdentity ?: activeDomain
                    if (prakriya.nameIdentity != identity.first || domain != identity.second) {
                        return@statementLoop
                    }
                    if (prakriya.modifiers.visibility == PrakriyaVisibility.INTERNAL && sourceFile != currentFile) {
                        return@statementLoop
                    }
                    add(Triple(sourceFile, definition, domain))
                }
            }
        }
        if (definitions.isEmpty()) return null

        return buildString {
            append("<html><body><h2>Project Prakriyā: ")
            append(html(identity.first))
            append("</h2>")
            definitions.forEach { (sourceFile, definition, domain) ->
                val prakriya = definition.prakriya
                val signature = runCatching {
                    PrakriyaSignatureCompiler.compile(
                        definition.body, prakriya.declaredResult, prakriya.resultPredicate,
                    )
                }.getOrNull()
                append("<h3>").append(html(sourceFile.name)).append("</h3><ul>")
                append("<li><b>Canonical identity:</b> <code>").append(html(prakriya.nameIdentity))
                    .append("</code></li>")
                append("<li><b>Domain:</b> ").append(html(domain ?: "unscoped")).append("</li>")
                append("<li><b>Visibility:</b> ").append(prakriya.modifiers.visibility.name.lowercase())
                    .append("</li>")
                append("<li><b>Precedence:</b> ").append(prakriya.modifiers.precedence.name.lowercase())
                    .append("</li>")
                append("<li><b>Body statements:</b> ").append(definition.body.size).append("</li>")
                signature?.let {
                    val parameters = it.parameters.joinToString(", ") { parameter ->
                        "${parameter.nameStem}: ${parameter.type.sanskritName}"
                    }.ifBlank { "none" }
                    val result = it.resultType?.sanskritName ?: it.resultSchema ?: "unspecified"
                    append("<li><b>Parameters:</b> ").append(html(parameters)).append("</li>")
                    append("<li><b>Result:</b> ").append(html(result)).append("</li>")
                    it.argumentType?.let { type ->
                        append("<li><b>Argument guard:</b> ").append(html(type.sanskritName)).append("</li>")
                    }
                }
                append("</ul>")
            }
            append("</body></html>")
        }
    }

    private fun html(value: String): String = buildString(value.length) {
        value.forEach { character ->
            append(
                when (character) {
                    '&' -> "&amp;"
                    '<' -> "&lt;"
                    '>' -> "&gt;"
                    '"' -> "&quot;"
                    '\'' -> "&#39;"
                    else -> character
                },
            )
        }
    }

    private fun getSandhiDoc(expression: String): String? {
        val vm = PaniniVM()
        val result = runCatching { vm.eval(expression) }.getOrNull() ?: return null
        if (result !is ExecutionResult.Success || result.value.isBlank()) return null

        val sb = StringBuilder("<html><body>")
        sb.append("<h2>PaniniVM Sandhi Conjugated Surface Form</h2>")
        sb.append("<p><b>Original Expression:</b> <code>$expression</code></p>")
        sb.append("<p><b>Sandhi Conjugated Form:</b> <font color=\"#2E7D32\" size=\"+1\"><b>${result.value}</b></font></p>")
        sb.append("<p><b>Operation:</b> ${result.operation}</p>")
        if (result.trace.isNotEmpty()) {
            sb.append("<h4>Derivation Trace:</h4><ul>")
            result.trace.forEach { sb.append("<li>$it</li>") }
            sb.append("</ul>")
        }
        sb.append("</body></html>")
        return sb.toString()
    }

    private fun getAffixDoc(text: String): String? {
        val sup = SupAffix.candidates(text)
        if (sup.isNotEmpty()) {
            val slots = sup.joinToString("<br/>") { "${it.vibhakti} ${it.vacana}" }
            return affixHtml(text, "Sup", slots)
        }

        TingAffix.fromUpadesha(text)?.let {
            return affixHtml(text, "Tiṅ", "${it.pada} ${it.purusha} ${it.vacana}")
        }
        Lakara.entries.singleOrNull { it.upadesha == text }?.let {
            return affixHtml(text, "Lakāra", it.name)
        }
        SanadiAffix.fromUpadesha(text)?.let {
            return affixHtml(text, "Sanādi", it.name)
        }
        KrtAffix.fromUpadesha(text)?.let {
            return affixHtml(text, "Kṛt", it.name)
        }
        TaddhitaAffix.fromUpadesha(text)?.let {
            return affixHtml(text, "Taddhita", it.name)
        }
        return null
    }

    private fun affixHtml(upadesha: String, category: String, details: String): String =
        "<html><body><h3>Paninian $category Specification</h3><p><b>$upadesha</b>: $details</p></body></html>"

    private fun getKeywordDoc(text: String): String? {
        val map = mapOf(
            "च" to "<b>च</b> (Conjunction): Joins multiple nominal or verbal expressions.",
            "इति" to "<b>इति</b> (Clause Terminator): Marks completion of a quotation, condition, or expression.",
            "यदि" to "<b>यदि</b> (Conditional): Begins a conditional branch expression.",
            "तर्हि" to "<b>तर्हि</b> (Conditional Consequent): Marks the consequence of a <i>यदि</i> clause.",
            "हे" to "<b>हे</b> (Vocative Indicator): Address indicator used with Sambuddhi nominal forms.",
            "यन्त्र" to "<b>यन्त्र</b> (System Target): PaniniVM system invocation receiver.",
            "उणादि" to "<b>उणादि</b> (Uṇādipāṭha): Prefix used to select and run Uṇādi nominal derivations."
        )
        val doc = map[text] ?: return null
        return "<html><body><h3>PaniniVM Control Keyword</h3><p>$doc</p></body></html>"
    }

    private fun getBhutasamkhyaDoc(text: String): String? {
        val parts = text.split("-", " ").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.isEmpty()) return null
        if (parts.all { BhutasamkhyaLexicon.isSymbol(it) }) {
            val decoded = runCatching { BhutasamkhyaDecoder().decode(text) }.getOrNull() ?: return null
            val valList = parts.map { BhutasamkhyaLexicon.getValue(it) ?: 0L }
            val sb = StringBuilder("<html><body>")
            sb.append("<h2>Bhūta-saṅkhyā Numeral</h2>")
            sb.append("<p><b>Expression:</b> <code>$text</code></p>")
            sb.append("<p><b>Decoded Value:</b> <font color=\"#2E7D32\" size=\"+1\"><b>$decoded</b></font></p>")
            sb.append("<h4>Component Values:</h4><ul>")
            parts.forEachIndexed { i, part -> sb.append("<li><code>$part</code>: ${valList[i]}</li>") }
            sb.append("</ul></body></html>")
            return sb.toString()
        }
        return null
    }

    private fun getAryabhatiyaDoc(text: String): String? {
        if (!text.all { it in '\u0900'..'\u097F' }) return null
        val isValidArya = text.all { AryabhatiyaMapping.isConsonant(it) || AryabhatiyaMapping.getVowelPower(it) != null || it == '्' }
        if (!isValidArya) return null

        val decoded = runCatching { AryabhatiyaDecoder().decode(text) }.getOrNull() ?: return null
        val sb = StringBuilder("<html><body>")
        sb.append("<h2>Āryabhaṭīya Numeral</h2>")
        sb.append("<p><b>Word:</b> <code>$text</code></p>")
        sb.append("<p><b>Decoded Value:</b> <font color=\"#2E7D32\" size=\"+1\"><b>$decoded</b></font></p>")
        sb.append("</body></html>")
        return sb.toString()
    }

    private fun getKatapayadiDoc(text: String): String? {
        if (!text.all { it in '\u0900'..'\u097F' }) return null
        val hasConsonant = text.any { KatapayadiMapping.isConsonant(it) }
        if (!hasConsonant) return null

        val decoded = runCatching { KatapayadiDecoder().decode(text) }.getOrNull() ?: return null
        val sb = StringBuilder("<html><body>")
        sb.append("<h2>Kaṭapayādi Numeral</h2>")
        sb.append("<p><b>Word:</b> <code>$text</code></p>")
        sb.append("<p><b>Decoded Value:</b> <font color=\"#2E7D32\" size=\"+1\"><b>$decoded</b></font></p>")
        val consonantDigits = text.filter { KatapayadiMapping.isConsonant(it) }
            .map { it to KatapayadiMapping.getDigit(it) }
            .filter { it.second != null }
        sb.append("<p><i>Consonants:</i> ")
        sb.append(consonantDigits.joinToString(", ") { "${it.first} = ${it.second}" })
        sb.append("</p></body></html>")
        return sb.toString()
    }
}
