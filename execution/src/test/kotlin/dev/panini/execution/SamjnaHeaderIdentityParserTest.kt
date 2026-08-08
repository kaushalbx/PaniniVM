package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SamjnaHeaderIdentityParserTest {

    @Test
    fun `extracts standalone and domain method identities from cases`() {
        assertEquals(
            SamjnaHeaderIdentity(operationStem = "गणित", domainStem = null),
            SamjnaHeaderIdentityParser.parse("गणित + सुँ"),
        )
        assertEquals(
            SamjnaHeaderIdentity(operationStem = "वृध् + ल्युट्", domainStem = "गुण"),
            SamjnaHeaderIdentityParser.parse("गुण + मतुप् + ङस् वृध् + ल्युट् + सुँ"),
        )
        assertEquals(
            SamjnaHeaderIdentity(operationStem = "वृध् + ल्युट्", domainStem = "गुण"),
            SamjnaHeaderIdentityParser.parse("गुण + वत् + ङस् वृध् + ल्युट् + सुँ"),
        )
    }

    @Test
    fun `requires parsed nominal header structure`() {
        assertNull(SamjnaHeaderIdentityParser.parse("वृध् + ल्युट् + टा कृ + लोट् + सिप्"))
        assertNull(SamjnaHeaderIdentityParser.parse("not a grammatical header"))
    }
}
