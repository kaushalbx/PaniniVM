package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class TypedAttributeMorphologyTest {
    @Test fun `field access retains the source sup node and all grammatical slots`() {
        for (ending in listOf("भ्याम्", "भ्यस्", "ओस्", "अम्", "ङसिँ")) {
            val padas = PaniniParser().parse("गुण + मतुप् + ङस् मूल्य + $ending ।")
                .grammaticalVakyas().single().padas
            val key = padas.last() as SubantaPada
            val access = assertNotNull(TaddhitaStructEngine.detectAttributeAccess(padas), ending)
            assertSame(key.sup, access.resultSup)
            assertEquals(SupAffix.candidates(ending), access.resultSup.affixCandidates, ending)
            assertEquals(listOf("गुण", "मूल्य"), access.chain)
        }
    }
}
