package dev.panini.derivation

import dev.panini.core.Linga
import kotlin.test.Test

class GhiDerivationTest {
    @Test
    fun `derive full masculine i-stem paradigm for kavi`() = assertSubantaParadigm(
        "कवि",
        Linga.PUMS,
        """
            कविः कवी कवयः कविम् कवी कवीन् कविना कविभ्याम् कविभिः
            कवये कविभ्याम् कविभ्यः कवेऽः कविभ्याम् कविभ्यः
            कवेऽः कव्योः कवीनाम् कवौ कव्योः कविषु
        """,
    )

    @Test
    fun `derive full masculine i-stem paradigm for rishi`() = assertSubantaParadigm(
        "ऋषि",
        Linga.PUMS,
        """
            ऋषिः ऋषी ऋषयः ऋषिम् ऋषी ऋषीन् ऋषिणा ऋषिभ्याम् ऋषिभिः
            ऋषये ऋषिभ्याम् ऋषिभ्यः ऋषेऽः ऋषिभ्याम् ऋषिभ्यः
            ऋषेऽः ऋष्योः ऋषीणाम् ऋषौ ऋष्योः ऋषिषु
        """,
    )

    @Test
    fun `derive full masculine u-stem paradigm for bhanu`() = assertSubantaParadigm(
        "भानु",
        Linga.PUMS,
        """
            भानुः भानू भानवः भानुम् भानू भानून् भानुना भानुभ्याम् भानुभिः
            भानवे भानुभ्याम् भानुभ्यः भानोऽः भानुभ्याम् भानुभ्यः
            भानोऽः भान्वोः भानूनाम् भानौ भान्वोः भानुषु
        """,
    )
}
