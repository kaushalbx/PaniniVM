package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class PurposeActionNounCaseTest {
    @Test fun `purpose dative requires selected bhava and direct verbal noun under anabhihita`() {
        val noun = KridantaPratipadika("पाक", emptyList(), DhatuPrakriti("पच्", "पच्"), "घञ्")
        val pada = SubantaPada("पाकाय", noun, SupPratyaya("ङे", "ङे"))
        val meanings = setOf(SemanticRelation.PURPOSE_ACTION, SemanticRelation.ACTION_NOUN_DENOTATION)
        val p = ParticipantFacts("p", pada, setOf(Vibhakti.CHATURTHI), semanticRelations = meanings)
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, p.possibleVibhaktis, participant = p)
        assertTrue(TumarthacCaBhavavacanatSutra.matches(context))
        assertFalse(TumarthacCaBhavavacanatSutra.matches(context.copy(abhihita = true)))
        meanings.forEach { missing ->
            assertFalse(TumarthacCaBhavavacanatSutra.matches(context.copy(participant = p.copy(
                semanticRelations = meanings - missing))))
        }
        val extended = pada.copy(pratipadika = noun.copy(vikaras = listOf(TaddhitaVikara("त्व", "त्व"))))
        assertFalse(TumarthacCaBhavavacanatSutra.matches(context.copy(participant = p.copy(expression = extended))))
        val agent = pada.copy(pratipadika = noun.copy(krtPratyaya = "ण्वुल्"))
        assertFalse(TumarthacCaBhavavacanatSutra.matches(context.copy(participant = p.copy(expression = agent))))
    }
}
