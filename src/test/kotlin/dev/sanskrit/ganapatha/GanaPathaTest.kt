package dev.sanskrit.ganapatha

import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SarvanamaSutra
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GanaPathaTest {
    @Test
    fun `sarvadi gana exposes source pronoun stems`() {
        val sarvadi = GanaPatha.require(GanaIds.SARVADI)

        assertEquals("सर्वादिः", sarvadi.name)
        assertEquals(1, sarvadi.sourceIndex)
        assertEquals("1.1.27", sarvadi.sutra)
        assertEquals("11027", sarvadi.sutraId)
        assertEquals("सर्वादीनि सर्वनामानि", sarvadi.sutraText)
        assertEquals("sarvaadeenisarvanaamaani", sarvadi.sutraTransliteration)
        assertEquals("S${'$'}सर्वनामसंज्ञा${'$'}", sarvadi.sutraType)
        assertEquals("", sarvadi.vartika)
        assertTrue(sarvadi.rawWords.contains("पूर्वपरावरदक्षिणोत्तरापराधराणि"))
        assertEquals("सर्वादिगणे विद्यमानानाम् शब्दानाम् 'सर्वनाम' इति संज्ञा भवति ।", sarvadi.sanskritMeaning)
        assertEquals("", sarvadi.hindiMeaning)
        assertEquals("The words belonging to the सर्वादिगण are called सर्वनाम.", sarvadi.englishMeaning)
        assertEquals(GanaSource.GANAPATHA_DATA, sarvadi.source)
        assertEquals(GanaPathaSources.DATA_URL, sarvadi.sourceUrl)
        assertEquals(GanaKind.PATHA, sarvadi.kind)
        assertTrue(GanaPatha.contains(GanaIds.SARVADI, "सर्व"))
        assertTrue(GanaPatha.contains(GanaIds.SARVADI, " तद् "))
        assertTrue(GanaPatha.contains(GanaIds.SARVADI, "किम्"))
        assertFalse(GanaPatha.contains(GanaIds.SARVADI, "राम"))
    }

    @Test
    fun `ganapatha exposes source ganas in order`() {
        assertEquals(
            listOf(
                GanaIds.SARVADI,
                GanaIds.SVARADI,
                GanaIds.CHADI,
                GanaIds.PRADI,
                GanaIds.URYADI,
                GanaIds.SAKSHATPRABHRTI,
                GanaIds.TISHTHADGUPRABHRTI,
                GanaIds.SHAUNDADI,
                GanaIds.PATRESAMITADI,
                GanaIds.VYAGHRADI,
                GanaIds.SHRENYADI,
                GanaIds.KRTADI,
                GanaIds.SHAKAPARTHIVADI,
                GanaIds.SHRAMANADI,
                GanaIds.MAYURAVYAMSAKADI,
                GanaIds.YAJAKADI,
                GanaIds.RAJADANTADI,
                GanaIds.AHITAGNYADI,
                GanaIds.KADARADI,
                GanaIds.NAVADI,
                GanaIds.PRAKRTYADI,
            ),
            GanaPatha.all.take(21).map { it.id },
        )
        assertEquals(71, GanaPatha.all.size)
        assertEquals(GanaIds.PRATYADI, GanaPatha.all[21].id)
        assertEquals(GanaIds.YAUDHEYADI, GanaPatha.all.last().id)
        assertEquals("1.4.58", GanaPatha.require(GanaIds.PRADI).sutra)
        assertEquals("प्रादयः", GanaPatha.require(GanaIds.PRADI).sutraText)
        assertEquals(
            "The words included in the प्रादिगण are called निपात when used in a sense that does not denote an object.",
            GanaPatha.require(GanaIds.PRADI).englishMeaning,
        )
        assertEquals(GanaKind.AKRTI, GanaPatha.require(GanaIds.CHADI).kind)
        assertEquals(11, GanaPatha.require(GanaIds.SHRENYADI).sourceIndex)
        assertEquals("V${'$'}${'$'}", GanaPatha.require(GanaIds.SHRENYADI).sutraType)
        assertTrue(GanaPatha.contains(GanaIds.PRADI, "प्रति"))
        assertTrue(GanaPatha.contains(GanaIds.SVARADI, "स्वर्"))
        assertTrue(GanaPatha.contains(GanaIds.SHRENYADI, "देव"))
        assertEquals("2.1.59", GanaPatha.require(GanaIds.KRTADI).sutra)
        assertEquals("श्रेण्यादयः कृतादिभिः", GanaPatha.require(GanaIds.KRTADI).sutraText)
        assertEquals(GanaKind.AKRTI, GanaPatha.require(GanaIds.MAYURAVYAMSAKADI).kind)
        assertTrue(GanaPatha.contains(GanaIds.KRTADI, "कृत"))
        assertTrue(GanaPatha.contains(GanaIds.MAYURAVYAMSAKADI, "अकुतोभयः"))
        assertTrue(GanaPatha.contains(GanaIds.NAVADI, "शृगाल"))
        assertEquals("2.3.43", GanaPatha.require(GanaIds.PRATYADI).sutra)
        assertTrue(GanaPatha.contains(GanaIds.ARDHARCADI, "कार्षापण"))
        assertTrue(GanaPatha.contains(GanaIds.BIDADI, "भरद्वाज"))
        assertTrue(GanaPatha.contains(GanaIds.YAUDHEYADI, "यौधेय"))
    }

    @Test
    fun `ganapatha finds every gana containing a member`() {
        assertEquals(listOf(GanaIds.SARVADI), GanaPatha.ganasContaining("इदम्").map { it.id })
        assertEquals(listOf(GanaIds.SHRENYADI, GanaIds.UTSADI), GanaPatha.ganasContaining("देव").map { it.id })
        assertEquals(emptyList(), GanaPatha.ganasContaining("अकल्पितशब्दः"))
    }

    @Test
    fun `gana exposes member lookup helpers`() {
        val sarvadi = GanaPatha.require(GanaIds.SARVADI)

        assertTrue(sarvadi.contains(" सर्व "))
        assertEquals("सर्व", sarvadi.findMember("सर्व")?.text)
        assertEquals("तद्", sarvadi.requireMember(" तद् ").text)
        assertTrue("सर्व" in sarvadi.memberTexts)
        assertTrue("किम्" in sarvadi.normalizedMemberTexts)
        assertTrue(sarvadi.hasMeaning())
    }

    @Test
    fun `chadi gana members include translations`() {
        val chadi = GanaPatha.require(GanaIds.CHADI)

        assertTrue(chadi.hindiMeaning.isNotBlank())
        assertTrue(chadi.members.all { it.hindiArtha.isNotBlank() })
        assertTrue(chadi.members.all { it.englishArtha.isNotBlank() })
        assertEquals("और, भी", chadi.requireMember("च").hindiArtha)
        assertEquals("if", chadi.requireMember("चेत्").englishArtha)
        assertEquals("ॐ, प्रणव", chadi.requireMember("ओम्").hindiArtha)
    }

    @Test
    fun `gana member builder preserves member order`() {
        val members = ganaMembers {
            member("च", "और", "and")
            member("वा", "या", "or")
        }

        assertEquals(listOf("च", "वा"), members.map { it.text })
        assertEquals("और", members.first().hindiArtha)
        assertEquals("or", members.last().englishArtha)
    }

    @Test
    fun `unknown gana id fails clearly`() {
        val error = assertFailsWith<IllegalStateException> {
            GanaPatha.require("missing")
        }

        assertTrue(error.message!!.contains("missing"))
    }

    @Test
    fun `sarvanama sutra uses sarvadi gana membership`() {
        val initial = DerivationState(
            terms = listOf(DerivationTerm("stem", "सर्व", TermKind.PRATIPADIKA)),
        )

        val result = SarvanamaSutra.apply(initial)

        assertTrue(SamjnaAssignment("stem", Samjna.SARVANAMA) in result.state.samjnas)
        assertFalse(SarvanamaSutra.matches(result.state))
    }
}
