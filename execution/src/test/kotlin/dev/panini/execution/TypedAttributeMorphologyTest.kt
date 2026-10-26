package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertIs

class TypedAttributeMorphologyTest {
    @Test fun `invalid finite field access exposes typed relation morphology`() {
        val source = "गुण + मतुप् + ङस् मूल्य + सुँ ग्रहँ + श्ना + लोट् + सिप् ।"
        val vakya = PaniniParser().parse(source).grammaticalVakyas().single()
        val invalid = assertIs<TaddhitaStructEngine.AttributeAccessAnalysis.Invalid>(
            TaddhitaStructEngine.analyzeAttributeAccess(vakya))
        assertEquals("ATTRIBUTE_ACCESS_MORPHOLOGY", invalid.diagnostic.code)
        assertNotNull(invalid.diagnostic.span)
    }

    @Test fun `disconnected embedded field phrase exposes a typed diagnostic`() {
        val source = "गुण + मतुप् + ङस् न मूल्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val vakya = PaniniParser().parse(source).grammaticalVakyas().single()
        val invalid = assertIs<TaddhitaStructEngine.AttributeReferenceAnalysis.Invalid>(
            TaddhitaStructEngine.analyzeAttributeReference(vakya))
        assertEquals("ATTRIBUTE_REFERENCE_DISCONNECTED", invalid.diagnostic.code)
        assertNotNull(invalid.diagnostic.span)
    }


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
