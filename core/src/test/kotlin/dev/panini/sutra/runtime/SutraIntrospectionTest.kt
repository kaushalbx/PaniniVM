package dev.panini.sutra.runtime

import dev.panini.execution.SanskritValue
import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

class SutraIntrospectionTest {
    @Test
    fun `registry resolves only exported imported sutras`() {
        val public = sutra("public", SutraArtha("kriya"))
        val private = sutra("private", SutraArtha("paribhasha"))
        val library = SutraGrantha(
            id = GranthaId("library"),
            sutras = listOf(public, private),
            exports = setOf(public.id),
        )
        val application = SutraGrantha(
            id = GranthaId("application"),
            sutras = listOf(sutra("main", SutraArtha("kriya"))),
            imports = listOf(GranthaImport(library.id, "lib")),
        )
        val registry = SutraGranthaRegistry(listOf(library, application))

        assertEquals(
            public,
            registry.resolve(application.id, "lib", public.id),
        )
        assertNull(registry.resolve(application.id, "lib", private.id))
        assertEquals(
            private,
            registry.sutra(SutraAddress(library.id, private.id)),
        )
        assertEquals(listOf(public), registry.exported(library.id))
    }

    @Test
    fun `registry rejects duplicate grantha identities`() {
        val first = SutraGrantha(
            GranthaId("same"),
            listOf(sutra("one", SutraArtha("test"))),
        )
        val second = SutraGrantha(
            GranthaId("same"),
            listOf(sutra("two", SutraArtha("test"))),
        )

        assertFailsWith<IllegalArgumentException> {
            SutraGranthaRegistry(listOf(first, second))
        }
    }

    @Test
    fun `introspector exposes semantic fields as Sanskrit values`() {
        val sutra = sutra(
            "inspect",
            SutraArtha(
                kind = "kriya",
                fields = mapOf(
                    "dhatu" to SutraArthaValue.Text("युज्"),
                    "optional" to SutraArthaValue.Truth(false),
                    "dependencies" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.SutraReference(SutraId("before"))),
                    ),
                ),
            ),
        )

        assertEquals(
            SanskritValue.Shabda("युज्"),
            SutraIntrospector.arthaField(sutra, "dhatu"),
        )
        assertEquals(
            SanskritValue.Satya(false),
            SutraIntrospector.arthaField(sutra, "optional"),
        )
        val description = assertIs<SanskritValue.Suchi>(
            SutraIntrospector.describe(sutra),
        )
        assertEquals(5, description.items.size)
    }

    private fun sutra(
        id: String,
        artha: SutraArtha,
    ): RuntimeSutra<TestAvastha> = RuntimeSutra(
        id = SutraId(id),
        source = SutraSource.Program("test", id, id),
        role = SutraRole.Vidhi,
        artha = artha,
        evaluator = { _, _ -> SutraNirnaya.Applicable(emptyList()) },
    )

    private data object TestAvastha : SutraAvastha
}
