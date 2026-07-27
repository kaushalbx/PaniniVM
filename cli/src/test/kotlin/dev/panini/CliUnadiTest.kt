package dev.panini

import kotlin.test.Test
import kotlin.test.assertTrue

class CliUnadiTest {

    @Test
    fun testCliUnadiList() {
        val output = runCli(arrayOf("--unadi", "list"))
        assertTrue(output.any { it.contains("Uṇādipāṭha Catalog") }, "Should output catalog title")
        assertTrue(output.any { it.contains("1.1") }, "Should include sūtra 1.1")
    }

    @Test
    fun testCliUnadiLookup() {
        val output = runCli(arrayOf("--unadi", "lookup", "पितृ"))
        assertTrue(output.any { it.contains("Uṇādi Etymological Analysis for 'पितृ'") })
        assertTrue(output.any { it.contains("2.95") })
    }

    @Test
    fun testCliUnadiPair() {
        val output = runCli(arrayOf("--unadi", "pair", "कृ", "कनिन्"))
        assertTrue(output.any { it.contains("Uṇādi Pair Analysis") })
        assertTrue(output.any { it.contains("4.1") })
    }
}
