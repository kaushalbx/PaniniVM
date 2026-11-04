package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KrtExclusionDerivationTest {
    private fun resolve(affix: KrtAffix, derivation: KrtDerivationEvidence? = null,
        voice: Prayoga = Prayoga.KARTARI, role: SemanticRelation = SemanticRelation.DESIRED_OBJECT,
        cases: Set<Vibhakti> = setOf(Vibhakti.SASTHI),
        legacy: KrtGenitiveProhibition? = null, vikaras: List<PratipadikaVikara> = emptyList()): KarakaResolution {
        val p = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), cases,
            semanticRelations = setOf(role), krtGenitiveProhibition = legacy)
        val root = when {
            affix == KrtAffix.INI -> "गम्"
            affix == KrtAffix.NINI && derivation?.sense == KrtDerivationSense.DEBT -> "दा"
            else -> "कृ"
        }
        val governor = KridantaPratipadika("$root + ${affix.upadesha}", emptyList(), DhatuPrakriti(root, root),
            affix.upadesha, vikaras, derivationEvidence = derivation)
        val expressed = if (affix in setOf(KrtAffix.KTA, KrtAffix.KHAL, KrtAffix.YUC))
            setOf(Karaka.KARMAN) else setOf(Karaka.KARTR)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p), voice,
            governingKrt = governor, governingKrtExpressedRoles = expressed))
    }

    @Test fun `canonical 2 3 69 exclusion families retain rejection provenance in either outer voice`() {
        val affixes = listOf(KrtAffix.SHATR, KrtAffix.SHANAC, KrtAffix.KANAC, KrtAffix.KVASU,
            KrtAffix.KI, KrtAffix.KIN, KrtAffix.U, KrtAffix.UKAN, KrtAffix.KTA, KrtAffix.KTAVATU,
            KrtAffix.KTVA, KrtAffix.LYAP, KrtAffix.TUMUN, KrtAffix.NAMUL, KrtAffix.KHAL,
            KrtAffix.YUC, KrtAffix.SHANAN, KrtAffix.CANASH, KrtAffix.TRN)
        for (affix in affixes) for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val agent = affix in setOf(KrtAffix.KTA, KrtAffix.KHAL, KrtAffix.YUC)
            val result = resolve(affix, voice = voice, role = if (agent) SemanticRelation.INDEPENDENT_AGENT else SemanticRelation.DESIRED_OBJECT)
            assertEquals(null, result.resolvedVibhakti, affix.name)
            assertEquals(listOf(if (agent) "1.4.54" else "1.4.49"), result.evidence.map { it.sutra }, affix.name)
            assertTrue(result.rejectedRules.any { it.targetSutra == "2.3.65" && it.prohibition.sutra == "2.3.69" }, affix.name)
        }
        assertEquals(listOf("1.4.49", "2.3.65"), resolve(KrtAffix.TRC).evidence.map { it.sutra })
        assertEquals(listOf("1.4.49", "2.3.65"), resolve(KrtAffix.NVUL).evidence.map { it.sutra })
    }

    @Test fun `2 3 70 derives its three exclusions from matching governor evidence`() {
        val cases = listOf(
            KrtAffix.NVUL to KrtDerivationEvidence("3.3.10", KrtDerivationSense.FUTURE),
            KrtAffix.INI to KrtDerivationEvidence("3.3.3", KrtDerivationSense.FUTURE),
            KrtAffix.NINI to KrtDerivationEvidence("3.3.3", KrtDerivationSense.FUTURE),
            KrtAffix.NINI to KrtDerivationEvidence("3.3.170", KrtDerivationSense.DEBT))
        for ((affix, evidence) in cases) {
            val result = resolve(affix, evidence)
            assertEquals(null, result.resolvedVibhakti)
            assertTrue(result.rejectedRules.any { it.targetSutra == "2.3.65" && it.prohibition.sutra == "2.3.70" })
            assertTrue(result.evidence.first().semanticEvidence.any { it.detail.contains(evidence.governingSutra) })
        }
    }

    @Test fun `future meaning alone wrong source ordinary aka and necessary in retain genitive`() {
        for ((affix, evidence) in listOf(
            KrtAffix.NVUL to KrtDerivationEvidence("3.1.133", KrtDerivationSense.FUTURE),
            KrtAffix.NVUL to KrtDerivationEvidence("3.3.10", KrtDerivationSense.GENERAL),
            KrtAffix.NINI to KrtDerivationEvidence("3.3.170", KrtDerivationSense.NECESSITY),
            KrtAffix.TRC to KrtDerivationEvidence("3.3.10", KrtDerivationSense.FUTURE))) {
            val result = resolve(affix, evidence)
            assertEquals(listOf("1.4.49", "2.3.65"), result.evidence.map { it.sutra })
            assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
        }
        assertEquals(null, resolve(KrtAffix.TRC, legacy = KrtGenitiveProhibition.AKA_FUTURE_DERIVATION).resolvedVibhakti)
        assertEquals(null, resolve(KrtAffix.NVUL, KrtDerivationEvidence("3.3.10", KrtDerivationSense.FUTURE),
            vikaras = listOf(TaddhitaVikara("इनि", "इनि"))).resolvedVibhakti)
    }
}
