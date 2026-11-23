package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

/** Grammar the merge sort example depends on, each checked on its own. */
class MergeSortGrammarTest {
    private val sadhaka = PvmUktiSadhaka()

    @Test fun `a compound subject gives its predicate its final member's gender (2·4·26)`() {
        assertEquals("यदि वामसङ्ख्या एकस्माद् न्यूना अस्ति तर्हि सत्यं मुद्रय ।", sadhaka.sadhayaLine(
            "यदि वाम-सङ्ख्या + सुँ एक + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि सत्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
    }

    @Test fun `a named range bound declines as a noun and forms the पर्यन्त compound`() {
        assertEquals("दत्तस्य एकस्माद् मध्यपर्यन्तम् अंशं गृहाण ।", sadhaka.sadhayaLine(
            "दत्त + ङस् एक + ङसिँ मध्य + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।"))
        assertEquals("दत्तस्य आरम्भात् परिमाणपर्यन्तम् अंशं गृहाण ।", sadhaka.sadhayaLine(
            "दत्त + ङस् आरम्भ + ङसिँ परिमाण + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।"))
    }

    @Test fun `a locative ordinal in an indexed transformation is a position, not a procedure input`() {
        val source = "पञ्चन् + जस् एक + सुँ इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "अग्रनिक्षेप + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सूचि + सुँ नाम मान + सुँ सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूचि + ङस् प्रथम + ङि त्रि + शस् नि + क्षिप् + लोट् + सिप् ॥\n" +
            "दत्त + अम् अग्रनिक्षेप + टा डुकृञ् + उ + लोट् + सिप् ।\nफल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val shown = PaniniVM().evalScript(source).last()
        assertEquals("[त्रि, पञ्चन्, एक]", (shown as ExecutionResult.Success).value, shown.toString())
        val removal = "अग्रनिष्कास + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सूचि + सुँ नाम मान + सुँ सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूचि + ङस् प्रथम + ङि सदस्य + अम् निस् + हृ + लोट् + सिप् ॥\n" +
            "दत्त + अम् अग्रनिष्कास + टा डुकृञ् + उ + लोट् + सिप् ।\nफल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val removed = PaniniVM().evalScript(source.substringBefore("अग्रनिक्षेप") + removal).last()
        assertEquals("[एक]", (removed as ExecutionResult.Success).value, removed.toString())
    }

    @Test fun `a compound name bounds a range in either position`() {
        assertEquals("दत्तस्य उत्तरारम्भात् त्रिपर्यन्तम् अंशं गृहाण ।", sadhaka.sadhayaLine(
            "दत्त + ङस् उत्तर-आरम्भ + ङसिँ त्रि + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।"))
        assertEquals("दत्तस्य एकस्माद् उत्तरारम्भपर्यन्तम् अंशं गृहाण ।", sadhaka.sadhayaLine(
            "दत्त + ङस् एक + ङसिँ उत्तर-आरम्भ + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।"))
        val setup = "पञ्चन् + जस् एक + सुँ त्रि + जस् इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः युज् + ल्युट् + ङस् फल + अम् उत्तर-आरम्भ + ङि स्था + णिच् + लोट् + सिप् ।\n"
        fun slice(range: String): String {
            val shown = PaniniVM().evalScript(setup + "दत्त + ङस् $range परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।\n" +
                "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।").last()
            return (shown as ExecutionResult.Success).value
        }
        assertEquals("[एक, त्रि]", slice("उत्तर-आरम्भ + ङसिँ त्रि + शस्"))
        assertEquals("[पञ्चन्, एक]", slice("एक + ङसिँ उत्तर-आरम्भ + शस्"))
    }

    @Test fun `an unsegmented name declines and agrees as its final feminine member (2·4·26)`() {
        assertEquals("फलं वामसङ्ख्यायां स्थापय ।",
            sadhaka.sadhayaLine("फल + अम् वामसङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।"))
        assertEquals("यदि वामसङ्ख्या एकस्माद् न्यूना अस्ति तर्हि सत्यं मुद्रय ।", sadhaka.sadhayaLine(
            "यदि वामसङ्ख्या + सुँ एक + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि सत्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
    }
}
