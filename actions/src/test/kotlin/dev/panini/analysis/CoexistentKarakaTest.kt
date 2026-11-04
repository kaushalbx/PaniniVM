package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class CoexistentKarakaTest {
    @Test fun `div designation follows the governing action and rejects stem prefix lookalikes`() {
        val p = ParticipantFacts("dice", AvyayaPada("dice", "dice"), setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        val outer = TingantaPada("outer", emptyList(), DhatuPrakriti("दिव्", "दिव्"), Lakara.LAT,
            TingPratyaya("तिप्", "तिप्"))
        val nominal = KridantaPratipadika("nominal", emptyList(), DhatuPrakriti("दिव्", "दिव्"), "ल्युट्")
        val context = KarakaRuleContext(DhatuIdentity("दिव्"), p, listOf(p), Prayoga.KARTARI,
            verbNode = outer, governingKrt = nominal)
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.DivahKarmaCaSutra
        assertTrue(rule.matches(context.copy(dhatu = DhatuIdentity("पठ्"),
            verbNode = outer.copy(dhatu = DhatuPrakriti("पठ्", "पठ्")))))
        assertFalse(rule.matches(context.copy(governingKrt = nominal.copy(dhatu = DhatuPrakriti("पठ्", "पठ्")))))
        for (root in listOf("दीव्यकल्पित", "अन्यदिव्", "दीव्य", "दीव्य्")) {
            val result = KarakaRuleEngine.resolve(context.copy(dhatu = DhatuIdentity(root), verbNode = null,
                governingKrt = null))
            assertEquals(listOf("1.4.42", "2.3.18"), result.evidence.map { it.sutra }, root)
            assertTrue(result.coexistingKarakas.isEmpty(), root)
        }
    }

    @Test fun `canonical div source spelling selects the same simultaneous designation`() {
        val participant = ParticipantFacts("dice", AvyayaPada("dice", "dice"), setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        for (root in listOf("दिव्", "दिव", "दीव्")) {
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), participant,
                listOf(participant), Prayoga.KARTARI))
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.coexistingKarakas, root)
            assertEquals(listOf("1.4.43", "2.3.2"), result.evidence.map { it.sutra }, root)
        }
    }

    private fun resolve(cases: Set<Vibhakti>, passive: Boolean = false): KarakaResolution {
        val p = ParticipantFacts("dice", AvyayaPada("dice", "dice"), cases,
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दिव"), p, listOf(p),
            if (passive) Prayoga.KARMANI else Prayoga.KARTARI))
    }

    @Test fun `ca establishes both names and case selects their projection without declining the rule`() {
        for ((case, role) in listOf(Vibhakti.DVITIYA to Karaka.KARMAN, Vibhakti.TRTIYA to Karaka.KARANA)) {
            val result = resolve(setOf(case))
            assertEquals(role, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.coexistingKarakas)
            assertEquals("1.4.43", result.evidence.first().sutra)
            assertFalse(result.evidence.first().optional)
            assertNull(result.primaryOptionalDecision)
            assertEquals(KarakaAlternativeKind.COEXISTENT_CASE, result.alternatives.single().kind)
            assertNull(result.alternatives.single().optionalDecision)
            assertTrue(result.alternatives.single().evidence.all { it.applied })
        }
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.DivahKarmaCaSutra
        assertFalse(rule.optional)
        assertEquals(setOf("1.4.42"), rule.anuvrtti)
    }

    @Test fun `expression of the common power prevents an extra passive instrumental`() {
        val passive = resolve(setOf(Vibhakti.PRATHAMA), true)
        assertEquals(Karaka.KARMAN, passive.resolved)
        assertEquals(Vibhakti.PRATHAMA, passive.resolvedVibhakti)
        assertEquals(listOf("1.4.43", "2.3.46"), passive.evidence.map { it.sutra })
        assertEquals(emptyList(), passive.alternatives)
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), passive.coexistingKarakas)
        val badInstrumental = resolve(setOf(Vibhakti.TRTIYA), true)
        assertNull(badInstrumental.resolvedVibhakti)
        assertTrue(badInstrumental.evidence.none { it.sutra == "2.3.18" })
    }

    @Test fun `case uncertainty does not erase simultaneous names`() {
        for (cases in listOf(setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA), setOf(Vibhakti.SASTHI))) {
            val result = resolve(cases)
            assertEquals(Karaka.KARMAN, result.resolved)
            assertNull(result.resolvedVibhakti)
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.coexistingKarakas)
        }
    }

    @Test fun `krt expression through one name expresses the shared power through both names`() {
        val p = ParticipantFacts("dice", AvyayaPada("dice", "dice"), setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        val governor = KridantaPratipadika("devana", emptyList(), DhatuPrakriti("दिव", "दिव"), "ल्युट्")
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दिव"), p, listOf(p), Prayoga.KARTARI,
            governingKrt = governor, governingKrtExpressedRoles = setOf(Karaka.KARANA)))
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.coexistingKarakas)
        assertEquals(Vibhakti.PRATHAMA, result.resolvedVibhakti)
        assertEquals(listOf("1.4.43", "2.3.46"), result.evidence.map { it.sutra })
        assertTrue(result.alternatives.none { it.vibhakti == Vibhakti.DVITIYA || it.vibhakti == Vibhakti.TRTIYA })
    }

    @Test fun `instrumental realization does not erase object status needed by a linked recipient`() {
        val recipient = ParticipantFacts("recipient", AvyayaPada("recipient", "recipient"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.RECIPIENT), intendedObject = IntendedObject.Written("dice"))
        val dice = ParticipantFacts("dice", AvyayaPada("dice", "dice"), setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दिव"), recipient,
            listOf(recipient, dice), Prayoga.KARTARI))
        assertEquals(listOf("1.4.32", "2.3.13"), result.evidence.map { it.sutra })
        assertEquals(emptyMap(), result.unresolvedObjectMediators)
    }
}
