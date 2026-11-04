package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KtaCaseRuleTest {
    private val kta = KridantaPratipadika("मतः", emptyList(), DhatuPrakriti("मन्", "मन्"), "क्त")
    private fun context(marker: SemanticRelation, expressed: Set<Karaka>) = VibhaktiRuleContext(
        Karaka.KARTR, setOf(Vibhakti.SASTHI), participant = ParticipantFacts("p", AvyayaPada("p", "p"),
            setOf(Vibhakti.SASTHI), semanticRelations = setOf(marker, SemanticRelation.KRT_GOVERNED_PARTICIPANT)),
        governingKrt = kta, governingKrtExpressedRoles = expressed)

    @Test fun `present kta genitive requires agent unexpressed role and linked kta`() {
        val ctx = context(SemanticRelation.PRESENT_PARTICIPLE_AGENT, setOf(Karaka.KARMAN))
        assertTrue(KasyaCaVartamaneSutra.matches(ctx))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(abhihita = true)))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(karaka = Karaka.KARMAN)))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(governingKrt = null)))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(governingKrt = kta.copy(krtPratyaya = "क्तवतुँ"))))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(governingKrtExpressedRoles = setOf(Karaka.KARTR))))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(governingKrtExpressedRoles = null)))
        assertFalse(KasyaCaVartamaneSutra.matches(ctx.copy(participant = ctx.participant!!.copy(semanticRelations =
            setOf(SemanticRelation.PRESENT_PARTICIPLE_AGENT)))))
        assertFalse(KartrKarmanohKrtiSutra.matches(ctx))
    }

    @Test fun `location denoting kta special genitive applies to agent or object only`() {
        val ctx = context(SemanticRelation.LOCATION_PARTICIPLE_RELATION, setOf(Karaka.ADHIKARANA))
        assertTrue(AdhikaranavacinacCaSutra.matches(ctx))
        assertTrue(AdhikaranavacinacCaSutra.matches(ctx.copy(karaka = Karaka.KARMAN)))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(abhihita = true)))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(karaka = Karaka.KARANA)))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(governingKrtExpressedRoles = emptySet())))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(governingKrtExpressedRoles = setOf(Karaka.ADHIKARANA, Karaka.KARTR))))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(governingKrt = kta.copy(vikaras = listOf(StriVikara("unknown", "unknown"))))))
        assertFalse(AdhikaranavacinacCaSutra.matches(ctx.copy(morphologicalCandidates = setOf(Vibhakti.TRTIYA))))
        assertFalse(KartrKarmanohKrtiSutra.matches(ctx))
    }
}
