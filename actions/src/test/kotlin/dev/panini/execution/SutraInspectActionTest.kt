package dev.panini.execution

import dev.panini.actions.state.SutraInspectAction
import dev.panini.core.Karaka
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.GranthaImport
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraAvastha
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaRegistry
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SutraInspectActionTest {
    @Test
    fun `action reads exported sutra semantic field through import alias`() {
        val public = sutra("public")
        val library = SutraGrantha(
            GranthaId("library"),
            listOf(public),
            exports = setOf(public.id),
        )
        val application = SutraGrantha(
            GranthaId("application"),
            listOf(sutra("main")),
            imports = listOf(GranthaImport(library.id, "lib")),
        )
        val operation = SutraInspectAction.op {
            requires(Karaka.KARMAN)
            optional(Karaka.ADHIKARANA, Karaka.KARANA)
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("public"),
                Karaka.ADHIKARANA to ExecutionExpression.Pada("lib"),
                Karaka.KARANA to ExecutionExpression.Pada("dhatu"),
            ),
            sutraRegistry = SutraGranthaRegistry(listOf(library, application)),
            currentGrantha = application.id,
        )

        val result = assertIs<ExecutionResult.Success>(
            SutraInspectAction.execute(context, operation),
        )

        assertEquals(SanskritValue.Shabda("युज्"), result.typedValue)
    }

    @Test
    fun `action cannot read non-exported imported sutra`() {
        val hidden = sutra("hidden")
        val library = SutraGrantha(GranthaId("library"), listOf(hidden))
        val application = SutraGrantha(
            GranthaId("application"),
            listOf(sutra("main")),
            imports = listOf(GranthaImport(library.id, "lib")),
        )
        val operation = SutraInspectAction.op {
            requires(Karaka.KARMAN)
            optional(Karaka.ADHIKARANA)
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("hidden"),
                Karaka.ADHIKARANA to ExecutionExpression.Pada("lib"),
            ),
            sutraRegistry = SutraGranthaRegistry(listOf(library, application)),
            currentGrantha = application.id,
        )

        assertIs<ExecutionResult.Failure>(
            SutraInspectAction.execute(context, operation),
        )
    }

    private fun sutra(id: String): RuntimeSutra<TestAvastha> = RuntimeSutra(
        id = SutraId(id),
        source = SutraSource.Program("test", id, id),
        role = SutraRole.Vidhi,
        artha = SutraArtha(
            "kriya",
            mapOf("dhatu" to SutraArthaValue.Text("युज्")),
        ),
        evaluator = { _, _ -> SutraNirnaya.Applicable(emptyList()) },
    )

    private data object TestAvastha : SutraAvastha
}
