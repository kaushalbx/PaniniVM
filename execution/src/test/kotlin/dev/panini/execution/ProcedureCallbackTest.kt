package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** A declared procedure is an operation, so collection actions take it as their instrumental callback. */
class ProcedureCallbackTest {
    private val list = "पञ्चन् + जस् एक + सुँ चतुर् + जस् द्वि + औ इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n"

    /** Is the member less than सीमा? सीमा is read from the call that runs the collection action. */
    private val lessThan = "लघु + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
        "सदस्य + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
        "यदि सदस्य + सुँ सीमा + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि फल + सुँ सत्य + सुँ असँ + लट् + तिप्\n" +
        "अन्यथा फल + सुँ असत्य + सुँ असँ + लट् + तिप् ॥\n"

    private fun shown(source: String): String {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.filterIsInstance<ExecutionResult.Failure>().toString())
        return assertIs<ExecutionResult.Success>(results.last()).value
    }

    @Test fun `partition calls a procedure for each member and reads its caller's values`() {
        assertEquals("द्विविभाग(सत्यसूची: [एक, द्वि], असत्यसूची: [पञ्चन्, चतुर्])", shown(lessThan + list +
            "त्रि + शस् सीमा + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "दत्त + अम् लघु + टा प्रति + वि + भजँ + लोट् + सिप् ।"))
    }

    @Test fun `filter keeps the members a procedure accepts`() {
        assertEquals("[एक, द्वि]", shown(lessThan + list +
            "त्रि + शस् सीमा + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "दत्त + अम् लघु + टा वि + वृजीँ + लोट् + सिप् ।"))
    }

    @Test fun `map transforms each member by a procedure`() {
        val double = "द्विगुण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप् ॥\n"
        assertEquals("[दश, द्वे, अष्ट, चत्वारि]", shown(double + list + "दत्त + अम् द्विगुण + टा सम् + यु + लोट् + सिप् ।"))
    }

    @Test fun `a callback does not change its caller's values`() {
        val assigning = "अङ्कन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + अम् अन्तिम + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "फल + सुँ सत्य + सुँ असँ + लट् + तिप् ॥\n"
        assertEquals("शून्य", shown(assigning + list + "शून्य + अम् अन्तिम + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "दत्त + अम् अङ्कन + टा वि + वृजीँ + लोट् + सिप् ।\nअन्तिम + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
    }

    @Test fun `reading a record field is a ग्रहण that a later ग्रहणस्य फलम् refers to`() {
        val setup = list + "दत्त + ङसिँ प्रथम + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।\n" +
            "दत्त + अम् यु + टा प्रति + वि + भजँ + लोट् + सिप् ततः प्रति + वि + भजँ + ल्युट् + ङस् फल + अम् विभाग + ङि स्था + णिच् + लोट् + सिप् ।\n"
        assertEquals("[चतुर्, द्वि]", shown(setup + "विभाग + मतुप् + ङस् सत्यसूची + अम् ग्रहँ + श्ना + लोट् + सिप् ।\n" +
            "ग्रहँ + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
        assertEquals("[चतुर्, द्वि]", shown(setup + "विभाग + मतुप् + ङस् सत्यसूची + अम् ग्रहँ + श्ना + लोट् + सिप्\n" +
            "    ततः ग्रहँ + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
    }

    @Test fun `a truth predicate agrees with its subject`() {
        assertEquals("फलम् असत्यम् अस्ति ।", PvmUktiSadhaka().sadhayaLine("फल + सुँ असत्य + सुँ असँ + लट् + तिप् ।"))
        assertEquals("फलं सत्यम् अस्ति ।", PvmUktiSadhaka().sadhayaLine("फल + सुँ सत्य + सुँ असँ + लट् + तिप् ।"))
    }
}
