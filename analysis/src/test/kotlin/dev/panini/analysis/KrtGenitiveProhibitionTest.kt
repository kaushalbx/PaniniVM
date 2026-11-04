package dev.panini.analysis

import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertIs

class KrtGenitiveProhibitionTest {
    @Test fun `krt genitive restrictions cover dual eligibility and its exceptions`() {
        for (target in listOf("2.3.66")) {
            val context = ProhibitionContext(target, proposedVibhakti = Vibhakti.SASTHI)
            assertIs<NishedhaRuleResult.Blocked>(NishedhaRuleEngine.evaluateProhibition(
                context.copy(isKrtProhibitedForSasthi = true)))
            assertIs<NishedhaRuleResult.Blocked>(NishedhaRuleEngine.evaluateProhibition(
                context.copy(krtGenitiveProhibition = KrtGenitiveProhibition.AKA_FUTURE_DERIVATION)))
        }
    }

    @Test fun `genitive prohibitions do not suppress optional instrumental`() {
        val context = ProhibitionContext("2.3.66", proposedVibhakti = Vibhakti.TRTIYA,
            isKrtProhibitedForSasthi = true, krtGenitiveProhibition = KrtGenitiveProhibition.IN_DEBT)
        assertIs<NishedhaRuleResult.Allowed>(NishedhaRuleEngine.evaluateProhibition(context))
    }
}
