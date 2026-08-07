package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * 5.2.94 तदस्यास्त्यस्मिन्निति मतुप्
 * Pāṇinian Taddhita Structs (मतुप् / वत्) and Genitive Attribute Access Engine.
 */
data class TaddhitaStruct(
    val nameStem: String,
    val attributes: Map<String, String>,
)

object TaddhitaStructEngine {

    private val parser = PaniniParser()

    /**
     * Detects struct construction sentence ending with "<struct> + वत् + सुँ" or "<struct> + मत् + सुँ".
     * e.g. "दश + अम् मूल्य + अम् पञ्च + अम् परिमाण + अम् गुण + वत् + सुँ ।"
     */
    fun detectStructConstruction(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaStruct? {
        val trimmed = sentenceText.trim().trimEnd('।', '॥', ' ')
        val isMatup = trimmed.contains("+ वत्") || trimmed.contains("+ मत्")
        if (!isMatup) return null

        val ukti = preParsedUkti ?: runCatching { parser.parse(trimmed) }.getOrNull()
        val karmaStems = mutableListOf<String>()

        if (ukti != null) {
            val padas = ukti.vakyas.flatMap { it.padas }.filterIsInstance<SubantaPada>()
            val matupPada = padas.lastOrNull { it.sup.text == "सुँ" && (it.sourceText.contains("+ वत्") || it.sourceText.contains("+ मत्")) }
            if (matupPada != null) {
                val structName = matupPada.pratipadika.sourceText.substringBefore("+").trim()
                for (pada in padas) {
                    if (pada != matupPada && pada.sup.text == "अम्") {
                        val stem = pada.pratipadika.sourceText.trim()
                        if (stem.isNotEmpty()) {
                            karmaStems.add(stem)
                        }
                    }
                }
                if (karmaStems.size >= 2) {
                    val attributes = mutableMapOf<String, String>()
                    for (i in 0 until karmaStems.size - 1 step 2) {
                        val valTerm = karmaStems[i]
                        val keyTerm = karmaStems[i + 1]
                        attributes[keyTerm] = valTerm
                    }
                    return TaddhitaStruct(nameStem = structName, attributes = attributes)
                }
            }
        }

        // Regex fallback
        val matupMatch = Regex("""(\S+)\s*\+\s*(?:वत्|मत्)\s*\+\s*सुँ""").find(trimmed) ?: return null
        val structName = matupMatch.groupValues[1]

        val matches = Regex("""(\S+)\s*\+\s*अम्""").findAll(trimmed).map { it.groupValues[1] }.toList()
        if (matches.size >= 2) {
            val attributes = mutableMapOf<String, String>()
            for (i in 0 until matches.size - 1 step 2) {
                val valTerm = matches[i]
                val keyTerm = matches[i + 1]
                attributes[keyTerm] = valTerm
            }
            return TaddhitaStruct(nameStem = structName, attributes = attributes)
        }

        return null
    }

    /**
     * Detects Genitive attribute access query: "<struct> + वत् + ङस् <key> + अम्"
     * e.g. "गुण + वत् + ङस् मूल्य + अम् ।"
     */
    fun detectAttributeAccess(sentenceText: String, preParsedUkti: Ukti? = null): Pair<String, String>? {
        val trimmed = sentenceText.trim().trimEnd('।', '॥', ' ')
        val isGenitiveMatup = trimmed.contains("+ ङस्") && (trimmed.contains("+ वत्") || trimmed.contains("+ मत्"))
        if (!isGenitiveMatup) return null

        val match = Regex("""(\S+)\s*\+\s*(?:वत्|मत्)\s*\+\s*ङस्\s+(\S+)\s*\+\s*अम्""").find(trimmed)
        if (match != null) {
            val structName = match.groupValues[1]
            val keyTerm = match.groupValues[2]
            return Pair(structName, keyTerm)
        }

        return null
    }

    /**
     * Detects struct method header definition: "<struct> + वत् + ङस् <method> + ल्युट् + सुँ"
     * e.g. "गुण + वत् + ङस् वर्द्धन + ल्युट् + सुँ"
     */
    fun detectMethodHeader(headerName: String): Pair<String, String>? {
        val trimmed = headerName.trim()
        val match = Regex("""(\S+)\s*\+\s*(?:वत्|मत्)\s*\+\s*ङस्\s+(.+)""").find(trimmed)
        if (match != null) {
            val structStem = match.groupValues[1]
            val methodHeader = match.groupValues[2]
            return Pair(structStem, methodHeader)
        }
        return null
    }

    /**
     * Detects struct method invocation: "<karma> <struct> + वत् + ङस् <method> + ल्युट् + टा कृ"
     * e.g. "पञ्च + अम् गुण + वत् + ङस् वर्द्धन + ल्युट् + टा कृ + लोट् + सिप्"
     */
    fun detectMethodInvocation(sentenceText: String): Triple<String, String, String>? {
        val trimmed = sentenceText.trim()
        val isMatupInvocation = trimmed.contains("+ ङस्") && (trimmed.contains("+ वत्") || trimmed.contains("+ मत्")) && trimmed.contains("+ टा")
        if (!isMatupInvocation) return null

        val match = Regex("""(.*)\s+(\S+)\s*\+\s*(?:वत्|मत्)\s*\+\s*ङस्\s+(\S+)\s*\+\s*(?:ल्युट्|णिच्|तव्यत्|सुँ|टा)""").find(trimmed)
        if (match != null) {
            val karmaText = match.groupValues[1].trim()
            val structStem = match.groupValues[2].trim()
            val methodStem = match.groupValues[3].trim()
            return Triple(structStem, methodStem, karmaText)
        }

        return null
    }
}
