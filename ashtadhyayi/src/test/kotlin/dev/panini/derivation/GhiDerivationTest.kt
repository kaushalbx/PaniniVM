package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class GhiDerivationTest {
    @Test fun `ghi ablative and genitive use purvarupa rutva and visarga rather than shortcut`() {
        val engine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras)
        for ((stem, expected) in listOf("अग्नि" to "अग्नेः", "वायु" to "वायोः")) {
            for (vibhakti in listOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)) {
                val result = engine.derive(SubantaDerivationRequest(stem, vibhakti,
                    Vacana.EKAVACANA, Linga.PUMS).initialState())
                assertEquals(expected, result.final.surface)
                val rules = result.applications.map { it.sutra }.toSet()
                assertTrue(rules.containsAll(setOf("7.3.111", "6.1.110", "8.2.66", "8.3.15")))
                assertFalse("7.3.125" in rules)
            }
        }
    }

    @Test
    fun `derive full masculine i-stem paradigm for kavi`() = assertSubantaParadigm(
        "कवि",
        Linga.PUMS,
        """
            कविः कवी कवयः कविम् कवी कवीन् कविना कविभ्याम् कविभिः
            कवये कविभ्याम् कविभ्यः कवेः कविभ्याम् कविभ्यः
            कवेः कव्योः कवीनाम् कवौ कव्योः कविषु
        """,
    )

    @Test
    fun `derive full masculine i-stem paradigm for rishi`() = assertSubantaParadigm(
        "ऋषि",
        Linga.PUMS,
        """
            ऋषिः ऋषी ऋषयः ऋषिम् ऋषी ऋषीन् ऋषिणा ऋषिभ्याम् ऋषिभिः
            ऋषये ऋषिभ्याम् ऋषिभ्यः ऋषेः ऋषिभ्याम् ऋषिभ्यः
            ऋषेः ऋष्योः ऋषीणाम् ऋषौ ऋष्योः ऋषिषु
        """,
    )

    @Test
    fun `derive full masculine u-stem paradigm for bhanu`() = assertSubantaParadigm(
        "भानु",
        Linga.PUMS,
        """
            भानुः भानू भानवः भानुम् भानू भानून् भानुना भानुभ्याम् भानुभिः
            भानवे भानुभ्याम् भानुभ्यः भानोः भानुभ्याम् भानुभ्यः
            भानोः भान्वोः भानूनाम् भानौ भान्वोः भानुषु
        """,
    )
}
