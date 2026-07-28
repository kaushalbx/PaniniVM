package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PuranaAbhyasaExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_purana_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates segmented sankhyaPuranaPada`() {
        val result = vm.eval("द्वि + तीय + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        assertEquals(102L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates segmented sankhyaAbhyasaPada`() {
        val result = vm.eval("पञ्च + कृत्वः दश + अम् युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        assertEquals(15L, sankhya.value)
    }
}
