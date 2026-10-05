package dev.panini.execution

import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PrakriyaHeaderIdentityParserTest {

    @Test
    fun `procedure identity is rebuilt from morphology rather than source text`() {
        val pratipadika = KridantaPratipadika(
            sourceText = "भ्रामकः पाठः",
            upasargas = listOf("वि"),
            dhatu = DhatuPrakriti("युज् + णिच्", "युज्", listOf("णिच्")),
            krtPratyaya = "ल्युट्",
        )

        assertEquals("वि + युज् + णिच् + ल्युट्", pratipadika.prakriyaIdentity())
    }

    @Test
    fun `existing header padas are interpreted without trusting their source text`() {
        val operation = SubantaPada(
            sourceText = "भ्रामक + अम्",
            pratipadika = MulaPratipadika(sourceText = "अन्यत्", text = "गणित"),
            sup = SupPratyaya(sourceText = "अज्ञात", text = "सुँ"),
        )

        assertEquals(
            PrakriyaHeaderIdentity(operationStem = "गणित", domainStem = null),
            PrakriyaHeaderIdentityParser.parse(listOf(operation)),
        )
    }

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
