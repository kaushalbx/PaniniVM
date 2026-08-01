package dev.panini.execution

import dev.panini.dhatupatha.DhatuPathaRegistration
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExecutionArchitectureTest {
    @Test
    fun `operation catalog resolves exact upadesha only`() {
        DhatuPathaRegistration.ensureRegistered()
        val catalog = DhatuPathaRegistration.operationCatalog

        assertTrue(DhatuPathaRegistration.resolve("यु").size > 1)
        assertEquals(
            DhatuPathaRegistration.resolve("यु").flatMap { it.operations }.distinct(),
            DhatuPathaRegistration.resolveOperations("यु"),
        )
        assertTrue(catalog.resolveAll("यु").size > 1)
        assertEquals(null, catalog.resolve("यु"), "Ambiguous root lookup must not choose an arbitrary overload")
        assertTrue(catalog.resolveAll("युत").isEmpty(), "Surface aliases must not resolve")
        assertTrue(catalog.resolveAll("सङ्ख्यायोजनम्").isEmpty(), "Action names must not resolve as dhātus")
    }

    @Test
    fun `default VM scope does not preauthorize dangerous effects`() {
        val capabilities = PaniniVM().defaultScope.capabilities

        assertFalse(ExecutionEffect.NETWORK in capabilities)
        assertFalse(ExecutionEffect.EXECUTE_PROCESS in capabilities)
        assertFalse(ExecutionEffect.SEND_MESSAGE in capabilities)
        assertTrue(ExecutionEffect.PURE in capabilities)
    }

    @Test
    fun `every pvm example has a reproducible readable Sanskrit rendering`() {
        val root = File("examples")
        val sources = root.walkTopDown().filter { it.isFile && it.extension == "pvm" }.toList()
        assertTrue(sources.isNotEmpty())

        sources.forEach { source ->
            assertTrue(PvmScript.parse(source.readText()).isNotEmpty(), "No statements parsed from ${source.path}")
            val generated = File(source.parentFile, "${source.nameWithoutExtension}.txt")
            assertTrue(generated.isFile, "Missing generated artifact for ${source.path}")
            val rendered = PvmUktiSadhaka().sadhayaScript(source.readText()).trimEnd()
            assertEquals(
                generated.readText().trimEnd(),
                rendered,
                "Generated artifact is stale for ${source.path}",
            )
        }
    }
}
