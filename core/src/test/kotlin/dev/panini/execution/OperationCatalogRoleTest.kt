package dev.panini.execution

import dev.panini.core.Karaka
import kotlin.test.*

class OperationCatalogRoleTest {
    private val action = object : DhatuAction("role-fixture", "signature selection fixture") {
        override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
            error("Catalog selection must not execute an action")
    }

    @Test fun `operation control effects are explicit and absent by default`() {
        assertNull(action.op().controlSignal)
        assertEquals(ExecutionControlSignal.BREAK_LOOP,
            action.op { signals(ExecutionControlSignal.BREAK_LOOP) }.controlSignal)
    }

    @Test fun `catalog respects forbidden roles even with required roles present`() {
        val operation = action.op {
            requires(Karaka.KARMAN)
            optional(Karaka.KARANA)
            forbids(Karaka.KARTR, Karaka.APADANA)
        }
        val catalog = OperationCatalog({ listOf(operation) })
        assertEquals(operation, catalog.resolve("fixture", setOf(Karaka.KARMAN)))
        assertEquals(operation, catalog.resolve("fixture", setOf(Karaka.KARMAN, Karaka.KARANA)))
        assertNull(catalog.resolve("fixture", setOf(Karaka.KARMAN, Karaka.KARTR)))
        assertNull(catalog.resolve("fixture", setOf(Karaka.KARMAN, Karaka.APADANA)))
    }

    @Test fun `optional and forbidden roles cannot overlap`() {
        assertFailsWith<IllegalArgumentException> {
            action.op {
                optional(Karaka.KARANA)
                forbids(Karaka.KARANA)
            }
        }
    }
}
