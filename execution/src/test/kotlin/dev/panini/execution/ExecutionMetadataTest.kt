package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class ExecutionMetadataTest {

    @Test
    fun `builds canonical dhatu metadata keys`() {
        assertEquals("dhatu:योग-2", ExecutionMetadata.dhatu(KriyaInvocationId.of(2)))
        assertEquals("dhatu:उक्ति-१/योग-2", ExecutionMetadata.dhatu("उक्ति-१/योग-2"))
    }
}
