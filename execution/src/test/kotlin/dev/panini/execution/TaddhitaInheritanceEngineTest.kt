package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TaddhitaInheritanceEngineTest {

    @Test
    fun `detects inheritance from parsed taddhita morphology`() {
        assertEquals(
            InheritanceRelation(childStem = "गाणित", parentStem = "गणित"),
            TaddhitaInheritanceEngine.detectInheritanceAdhikara("गणित + अण् + सुँ ।"),
        )
        assertNull(TaddhitaInheritanceEngine.detectInheritanceAdhikara("गणित + सुँ ।"))
    }

    @Test
    fun `applies vrddhi to the first pronounced vowel`() {
        val cases = mapOf(
            "गणित" to "गाणित",
            "गुण" to "गौण",
            "शिव" to "शैव",
            "ब्राह्मण" to "ब्राह्मण",
        )
        cases.forEach { (source, expected) ->
            assertEquals(expected, TaddhitaInheritanceEngine.deriveVriddhiStem(source))
        }
    }
}
