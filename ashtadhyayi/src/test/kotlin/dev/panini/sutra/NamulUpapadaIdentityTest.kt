package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya2.pada2.AmaivavyayenaSutra
import dev.panini.core.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class NamulUpapadaIdentityTest {
    private fun context(affixes: Set<KrtAffix> = setOf(KrtAffix.NAMUL), derived: KrtAffix? = KrtAffix.NAMUL) =
        SamasaRuleContext(listOf(
            SamasaPada("स्वादुम्", upapadaAffixPrescription = UpapadaAffixPrescription("3.4.26", affixes)),
            SamasaPada("कारम्", samjnas = setOf(Samjna.AVYAYA), krtAffix = derived),
        ), SamasaType.TATPURUSA)

    @Test fun `exclusive namul prescription licenses the indeclinable compound`() {
        assertTrue(AmaivavyayenaSutra.matches(context()))
        assertTrue(AmaivavyayenaSutra.matches(context().copy(samasaType = SamasaType.UPAPADA_TATPURUSA)))
        val formed = AmaivavyayenaSutra.apply(context()) as SamasaRuleResult.Formed
        assertEquals("स्वादुम्कारम्", formed.compoundStem)
    }

    @Test fun `joint ktva namul prescription does not satisfy eva restriction`() {
        assertFalse(AmaivavyayenaSutra.matches(context(setOf(KrtAffix.KTVA, KrtAffix.NAMUL))))
        assertFalse(AmaivavyayenaSutra.matches(context(setOf(KrtAffix.TUMUN), KrtAffix.TUMUN)))
        assertFalse(AmaivavyayenaSutra.matches(context(derived = KrtAffix.KTVA)))
    }

    @Test fun `spelling and sup am do not establish derivational identity`() {
        val untyped = SamasaRuleContext(listOf(SamasaPada("स्वाहाकृतम्"), SamasaPada("कृ")), SamasaType.TATPURUSA)
        assertFalse(AmaivavyayenaSutra.matches(untyped))
        assertFalse(AmaivavyayenaSutra.matches(untyped.copy(padas = listOf(SamasaPada("रामम्"), SamasaPada("कारम्")))))
        val licensed = context()
        assertFalse(AmaivavyayenaSutra.matches(licensed.copy(padas = listOf(licensed.purvaPada, licensed.uttaraPada.copy(samjnas = emptySet())))))
        assertFalse(AmaivavyayenaSutra.matches(context(derived = null)))
    }

    @Test fun `prescription source and identity survive pada copying`() {
        val original = context().purvaPada
        val copy = original.copy(upadesha = "स्वादुम्")
        assertEquals(original.upapadaAffixPrescription, copy.upapadaAffixPrescription)
        assertEquals(KrtAffix.NAMUL, KrtAffix.fromUpadesha("णमुल्"))
    }
}
