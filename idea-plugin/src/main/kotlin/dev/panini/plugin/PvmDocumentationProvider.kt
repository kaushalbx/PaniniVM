package dev.panini.plugin

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.ganapatha.GanaPatha
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.aryabhatiya.AryabhatiyaMapping
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.katapayadi.KatapayadiMapping
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaLexicon

class PvmDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val target = originalElement ?: element ?: return null
        val text = target.text.trim()
        if (text.isEmpty()) return null

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

        // 4. Check Sup / Ting affixes
        val affixDoc = getAffixDoc(text)
        if (affixDoc != null) {
            return affixDoc
        }

        // 5. Check Control keywords
        val keywordDoc = getKeywordDoc(text)
        if (keywordDoc != null) {
            return keywordDoc
        }

        // 6. Check Bhutasamkhya
        val bhutaDoc = getBhutasamkhyaDoc(text)
        if (bhutaDoc != null) {
            return bhutaDoc
        }

        // 7. Check Aryabhatiya
        val aryaDoc = getAryabhatiyaDoc(text)
        if (aryaDoc != null) {
            return aryaDoc
        }

        // 8. Check Katapayadi
        val kataDoc = getKatapayadiDoc(text)
        if (kataDoc != null) {
            return kataDoc
        }

        return null
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
        val map = mapOf(
            "सुँ" to "<b>सुँ</b>: Sup affix for <i>Prathamā Ekavacana</i> (Nominative Singular).",
            "अम्" to "<b>अम्</b>: Sup affix for <i>Dvitīyā Ekavacana</i> (Accusative Singular).",
            "औट्" to "<b>औट्</b>: Sup affix for <i>Prathamā/Dvitīyā Dvivacana</i> (Dual).",
            "शस्" to "<b>शस्</b>: Sup affix for <i>Dvitīyā Bahuvacana</i> (Accusative Plural).",
            "टा" to "<b>टा</b>: Sup affix for <i>Tṛtīyā Ekavacana</i> (Instrumental Singular).",
            "भ्याम्" to "<b>भ्याम्</b>: Sup affix for Dual <i>Tṛtīyā/Caturthī/Pañcamī</i>.",
            "भिसँ" to "<b>भिसँ</b>: Sup affix for <i>Tṛtīyā Bahuvacana</i> (Instrumental Plural).",
            "ङे" to "<b>ङे</b>: Sup affix for <i>Caturthī Ekavacana</i> (Dative Singular).",
            "भ्यस्" to "<b>भ्यस्</b>: Sup affix for Plural <i>Caturthī/Pañcamī</i>.",
            "ङसिँ" to "<b>ङसिँ</b>: Sup affix for <i>Pañcamī Ekavacana</i> (Ablative Singular).",
            "ङस्" to "<b>ङस्</b>: Sup affix for <i>Ṣaṣṭhī Ekavacana</i> (Genitive Singular).",
            "ओस्" to "<b>ओस्</b>: Sup affix for Dual <i>Ṣaṣṭhī/Saptamī</i>.",
            "आम्" to "<b>आम्</b>: Sup affix for <i>Ṣaṣṭhī Bahuvacana</i> (Genitive Plural).",
            "ङि" to "<b>ङि</b>: Sup affix for <i>Saptamī Ekavacana</i> (Locative Singular).",
            "सुप्" to "<b>सुप्</b>: Sup affix for <i>Saptamī Bahuvacana</i> (Locative Plural).",
            "तिप्" to "<b>तिप्</b>: Tiṅ affix for <i>Parasmaipada Prathama Puruṣa Ekavacana</i> (3rd Person Singular).",
            "तस्" to "<b>तस्</b>: Tiṅ affix for <i>Parasmaipada Prathama Puruṣa Dvivacana</i> (3rd Person Dual).",
            "झि" to "<b>झि</b>: Tiṅ affix for <i>Parasmaipada Prathama Puruṣa Bahuvacana</i> (3rd Person Plural).",
            "सिप्" to "<b>सिप्</b>: Tiṅ affix for <i>Parasmaipada Madhyama Puruṣa Ekavacana</i> (2nd Person Singular).",
            "थस्" to "<b>थस्</b>: Tiṅ affix for <i>Parasmaipada Madhyama Puruṣa Dvivacana</i> (2nd Person Dual).",
            "थ" to "<b>थ</b>: Tiṅ affix for <i>Parasmaipada Madhyama Puruṣa Bahuvacana</i> (2nd Person Plural).",
            "मिप्" to "<b>मिप्</b>: Tiṅ affix for <i>Parasmaipada Uttama Puruṣa Ekavacana</i> (1st Person Singular).",
            "वस्" to "<b>वस्</b>: Tiṅ affix for <i>Parasmaipada Uttama Puruṣa Dvivacana</i> (1st Person Dual).",
            "मस्" to "<b>मस्</b>: Tiṅ affix for <i>Parasmaipada Uttama Puruṣa Bahuvacana</i> (1st Person Plural).",
            "णिच्" to "<b>णिच्</b>: Sanādi causative suffix (Hetumatī ṇic).",
            "लोट्" to "<b>लोट्</b>: Lakāra for Imperative Mood (Ājñā/Prārthanā).",
            "लट्" to "<b>लट्</b>: Lakāra for Present Tense (Vartamāna Kāla)."
        )
        val doc = map[text] ?: return null
        return "<html><body><h3>Paninian Affix Specification</h3><p>$doc</p></body></html>"
    }

    private fun getKeywordDoc(text: String): String? {
        val map = mapOf(
            "च" to "<b>च</b> (Conjunction): Joins multiple nominal or verbal expressions.",
            "इति" to "<b>इति</b> (Clause Terminator): Marks completion of a quotation, condition, or expression.",
            "यदि" to "<b>यदि</b> (Conditional): Begins a conditional branch expression.",
            "तर्हि" to "<b>तर्हि</b> (Conditional Consequent): Marks the consequence of a <i>यदि</i> clause.",
            "हे" to "<b>हे</b> (Vocative Indicator): Address indicator used with Sambuddhi nominal forms.",
            "यन्त्र" to "<b>यन्त्र</b> (System Target): PaniniVM system invocation receiver."
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
