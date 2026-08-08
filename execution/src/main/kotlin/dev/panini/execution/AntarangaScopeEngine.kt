package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser

/** Resolves the case-marked अन्तरङ्ग dynamic-scope directive. */
object AntarangaScopeEngine {

    private val parser = PaniniParser()

    fun detectAntaranga(sentenceText: String, preParsedUkti: Ukti? = null): Boolean =
        findDirective(parsed(sentenceText, preParsedUkti)) != null

    /** Removes the directive identified by the parser while retaining the invocation text. */
    fun stripAntarangaDirective(sentenceText: String, preParsedUkti: Ukti? = null): String {
        val directive = findDirective(parsed(sentenceText, preParsedUkti)) ?: return sentenceText
        val sourcePattern = listOf(directive.first.sourceText, directive.second.sourceText)
            .joinToString("\\s+") { pada ->
                pada.split('+').joinToString("\\s*\\+\\s*") { Regex.escape(it) }
            }
        return sentenceText.replaceFirst(Regex(sourcePattern), "")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }

    private fun parsed(text: String, supplied: Ukti?): Ukti? =
        supplied ?: parser.parseOrNull(text.trim().trimEnd('।', '॥', ' '))

    private fun findDirective(ukti: Ukti?): Pair<SubantaPada, SubantaPada>? {
        val padas = ukti?.grammaticalVakyas()?.flatMap { it.padas } ?: return null
        return padas.zipWithNext().firstNotNullOfOrNull { (first, second) ->
            val antar = first as? SubantaPada ?: return@firstNotNullOfOrNull null
            val anga = second as? SubantaPada ?: return@firstNotNullOfOrNull null
            (antar.takeIf { it.hasForm("अन्तर", Vibhakti.DVITIYA) }
                ?.let { anga.takeIf { pada -> pada.hasForm("अङ्ग", Vibhakti.PANCHAMI) } }
                ?.let { antar to it })
        }
    }

    private fun SubantaPada.hasForm(stem: String, vibhakti: Vibhakti): Boolean =
        (pratipadika as? MulaPratipadika)?.text == stem &&
            SupAffix.fromUpadesha(sup.text)?.vibhakti == vibhakti
}
