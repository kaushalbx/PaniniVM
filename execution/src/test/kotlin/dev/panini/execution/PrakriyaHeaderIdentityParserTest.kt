package dev.panini.execution

import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PrakriyaHeaderIdentityParserTest {

    @Test
    fun `extracts standalone and domain method identities from cases`() {
        assertEquals(
            PrakriyaHeaderIdentity(operationStem = "गणित", domainStem = null),
            PrakriyaHeaderIdentityParser.parse("गणित + सुँ"),
        )
        assertEquals(
            PrakriyaHeaderIdentity(operationStem = "वृध् + ल्युट्", domainStem = "गुण"),
            PrakriyaHeaderIdentityParser.parse("गुण + मतुप् + ङस् वृध् + ल्युट् + सुँ"),
        )
        assertEquals(
            PrakriyaHeaderIdentity(operationStem = "वृध् + ल्युट्", domainStem = "गुण"),
            PrakriyaHeaderIdentityParser.parse("गुण + वत् + ङस् वृध् + ल्युट् + सुँ"),
        )
    }

    @Test
    fun `requires parsed nominal header structure`() {
        assertNull(PrakriyaHeaderIdentityParser.parse("वृध् + ल्युट् + टा कृ + लोट् + सिप्"))
        assertNull(PrakriyaHeaderIdentityParser.parse("not a grammatical header"))
    }

    @Test
    fun `classifies operation krt pratyaya structurally`() {
        assertTrue(
            PrakriyaHeaderIdentityParser.hasOperationKrtPratyayaIdentity(
                "सिद्ध+क्त+सुँ",
                KrtPratyayaIdentity.KTA,
            ),
        )
        assertFalse(
            PrakriyaHeaderIdentityParser.hasOperationKrtPratyayaIdentity(
                "सिद्ध + ल्युट् + सुँ",
                KrtPratyayaIdentity.KTA,
            ),
        )
        assertFalse(
            PrakriyaHeaderIdentityParser.hasOperationKrtPratyayaIdentity(
                "अवैध + क्त",
                KrtPratyayaIdentity.KTA,
            ),
        )
    }
}
