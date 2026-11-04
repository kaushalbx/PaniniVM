package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class RecipientLexicalIdentityTest {
    @Test fun `recipient lexical domains reject prefix lookalikes and follow nominal action identity`() {
        val constructions = listOf(
            Triple("1.4.33", "रोच", SemanticRelation.PLEASED_EXPERIENCER),
            Triple("1.4.34", "श्लाघ्", SemanticRelation.INTENDED_TO_BE_INFORMED),
            Triple("1.4.34", "ह्नु", SemanticRelation.INTENDED_TO_BE_INFORMED),
            Triple("1.4.34", "शप्", SemanticRelation.INTENDED_TO_BE_INFORMED),
            Triple("1.4.36", "स्पृह्", SemanticRelation.LONGED_FOR_OBJECT),
        )
        for ((number, root, meaning) in constructions) {
            val p = ParticipantFacts("recipient", AvyayaPada("recipient", "recipient"), setOf(Vibhakti.CHATURTHI),
                semanticRelations = setOf(SemanticRelation.RECIPIENT, meaning))
            val rule = KarakaRuleEngine.karakaRules.single { it.number == number }
            val outer = TingantaPada("verb", emptyList(), DhatuPrakriti(root, root), Lakara.LAT,
                TingPratyaya("तिप्", "तिप्"))
            val context = KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI, verbNode = outer)
            assertTrue(rule.matches(context), root)
            val fake = root.removeSuffix("्") + "कल्पित"
            assertFalse(rule.matches(context.copy(dhatu = DhatuIdentity(fake),
                verbNode = outer.copy(dhatu = DhatuPrakriti(fake, fake)))), fake)
            assertFalse(rule.matches(context.copy(verbNode = null, dhatu = DhatuIdentity(fake))), fake)
            val nominal = KridantaPratipadika("nominal", emptyList(), DhatuPrakriti(root, root), "ल्युट्")
            assertTrue(rule.matches(context.copy(dhatu = DhatuIdentity("पठ्"),
                verbNode = outer.copy(dhatu = DhatuPrakriti("पठ्", "पठ्")), governingKrt = nominal)), root)
            assertFalse(rule.matches(context.copy(governingKrt = nominal.copy(
                dhatu = DhatuPrakriti("पठ्", "पठ्")))), root)
            assertEquals(listOf(number, "2.3.13"), KarakaRuleEngine.resolve(context).evidence.map { it.sutra }, root)
        }
    }
}
