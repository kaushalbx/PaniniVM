package dev.panini.execution

import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.KridantaArtha
import dev.panini.vyakaranam.ast.KridantaLexicalMeaning
import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import dev.panini.vyakaranam.ast.MorphemeSemanticConcept
import dev.panini.vyakaranam.ast.MorphemeSemanticEvidence
import dev.panini.vyakaranam.ast.MorphemeSemanticPattern
import dev.panini.vyakaranam.ast.MorphemeSemanticRegistry
import dev.panini.vyakaranam.ast.MorphemeSemanticRule
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.morphemeSemanticIdentity
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MorphemeSemanticIdentityTest {
    private val parser = PaniniParser()

    @Test
    fun `segmented outcome has a stable semantic identity`() {
        val pada = parser.parse("परि + नम् + घञ् + सुँ ।").grammaticalVakyas()
            .single().padas.single()
        val pratipadika = assertIs<KridantaPratipadika>(assertIs<SubantaPada>(pada).pratipadika)
        val identity = assertNotNull(pratipadika.morphemeSemanticIdentity())

        assertEquals(listOf("परि"), identity.upasargas)
        assertEquals("नम्", identity.dhatu.sourceSurface)
        assertEquals(MorphemeSemanticConcept.OUTCOME, identity.concept)
        assertEquals("परिणाम", identity.canonicalName)
        assertEquals(KridantaArtha.BHAVA, identity.kridantaArtha)
        assertEquals(KridantaLexicalMeaning.RESULTING_CHANGE, identity.lexicalMeaning)
        assertEquals("3.3.18", identity.evidence.single().sutra)
    }

    @Test
    fun `segmented state has a stable semantic identity`() {
        val pada = parser.parse("अव + स्था + अङ् + अम् ।").grammaticalVakyas()
            .single().padas.single()
        val identity = assertNotNull(assertIs<SubantaPada>(pada).pratipadika.morphemeSemanticIdentity())

        assertEquals(MorphemeSemanticConcept.STATE, identity.concept)
        assertEquals("अवस्था", identity.canonicalName)
    }

    @Test
    fun `segmented schema and fields use semantic rather than surface identity`() {
        val source =
            "अव + स्था + अङ् + अम् प्रयत्नसङ्ख्या + अम् परि + नम् + घञ् + मतुप् + सुँ ।"
        val schema = assertNotNull(TaddhitaStructEngine.detectResultSchema(source))

        assertEquals("परिणाम", schema.nameStem)
        assertEquals(listOf("अवस्था", "प्रयत्नसङ्ख्या"), schema.fields)
    }

    @Test
    fun `segmented outcome and state work in a complete loop program`() {
        val results = PaniniVM().evalScript(
            """
            अव + स्था + अङ् + अम् प्रयत्नसङ्ख्या + अम् परि + नम् + घञ् + मतुप् + सुँ ।
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            एक + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परि + नम् + घञ् + मतुप् + ङस् अव + स्था + अङ् + अम् ।
            """.trimIndent(),
        )

        val state = results.filterIsInstance<ExecutionResult.Success>().last()
        assertEquals("समाप्ति", state.value, results.toString())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `segmented result marker compiles a typed samjna signature`() {
        val definition = assertIs<PvmScriptStatement.SamjnaDefinition>(
            PvmScript.parse(
                """
                योजन + ल्युट् + सुँ ।
                वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                सङ्ख्या + सुँ इति परि + नम् + घञ् + सुँ ।
                वाम + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
                """.trimIndent(),
            ).single(),
        )

        assertEquals(SamjnaValueType.SANKHYA, SamjnaSignatureCompiler.compile(definition.body).resultType)
    }

    @Test
    fun `additional meanings can be registered without changing the AST`() {
        val customRegistry = MorphemeSemanticRegistry(
            listOf(
                MorphemeSemanticRule(
                    id = "test.nam-ghan.state",
                    pattern = MorphemeSemanticPattern(
                        upasargas = emptyList(),
                        dhatuIds = setOf("01.1136"),
                        krtPratyaya = KrtPratyayaIdentity.GHAN,
                        kridantaArtha = KridantaArtha.BHAVA,
                    ),
                    lexicalMeaning = KridantaLexicalMeaning.SETTLED_STATE,
                    evidence = listOf(MorphemeSemanticEvidence("3.3.18", "भावे", "Test rule evidence.")),
                ),
            ),
        )
        val pada = parser.parse("नम् + घञ् + सुँ ।").grammaticalVakyas().single().padas.single()
        val identity = assertNotNull(
            assertIs<SubantaPada>(pada).pratipadika.morphemeSemanticIdentity(customRegistry),
        )

        assertEquals("test.nam-ghan.state", identity.ruleId)
        assertEquals(MorphemeSemanticConcept.STATE, identity.concept)
    }
}
