package dev.sanskrit.ganapatha

import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SarvanamaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SvaradiNipatamAvyayamSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.ChadayoAsattveSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.GatishCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.PradayaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.SakshatPrabhrtiniCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.UryadiCvidacashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.UpasargahKriyayogeSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.KandvadibhyoYakSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.BhrshadibhyoBhuvyacverLopashCaHalahSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.SukhadibhyoKartrvedanayamSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.NandigrahipacadibhyoLyuninyacahSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada3.BhavishyatiGamyadayahSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada3.ShidbhidadibhyoAngSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada4.BhimadayoApadaneSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.PashadibhyoYahSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.RajanyadibhyoVunSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.KramadibhyoVunSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.VasantadibhyashThakSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.KratukthadisutrantatThakSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.SankaladibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.SuvastvadibhyoAnSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.UtkaradibhyashChahSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.KattrayadibhyoDhakanySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada2.NadyadibhyoDhakSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada3.SandhiveladyRtunakshatrebhyoAnSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada3.DigadibhyoYatSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada3.RgayandibhyoAnSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada3.ShundikadibhyoAnSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada3.ShandikadibhyoNyahSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.AjadyatastapSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.BahvadibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.NadadibhyahPhakSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.GargadibhyoYanySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.AshvapatyadibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.BaahvadibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.GotreKunjadibhyashCaPhanySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.AnrshyanantaryeBidadibhyoAnySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.AshvadibhyahPhanySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.ShivadibhyoAnySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.GrshtyadibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.KurvadibhyoNyahSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.KambojalLukSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.ShubhradibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.RevatyadibhyashThakSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.TikadibhyahPhinySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.KalyanyadinamInangSutra
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.LohitadidajbhyahKyashSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.SharngaravadyanyoNginSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.VakinadinamKukCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.ShidGauradibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.KraudyadibhyashCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.UtsadibhyoAnySutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.NaShatsvasradibhyahSutra
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.NaKrodadibahvacahSutra
import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.LexicalUse
import dev.sanskrit.shiksha.SemanticFeature
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GanaPathaTest {
    @Test
    fun `sarvadi gana exposes source pronoun stems`() {
        val sarvadi = GanaPatha.require(1)

        assertEquals("सर्वादिः", sarvadi.name)
        assertEquals(1, sarvadi.sourceIndex)
        assertEquals("1.1.27", sarvadi.sutra)
        assertEquals("11027", sarvadi.sutraId)
        assertEquals("सर्वादीनि सर्वनामानि", sarvadi.sutraText)
        assertEquals("sarvaadeenisarvanaamaani", sarvadi.sutraTransliteration)
        assertEquals(setOf(dev.sanskrit.shiksha.Samjna.SARVANAMA), sarvadi.resultSamjnas)
        assertEquals("", sarvadi.vartika)
        assertTrue(sarvadi.rawWords.contains("पूर्वपरावरदक्षिणोत्तरापराधराणि"))
        assertEquals("सर्वादिगणे विद्यमानानाम् शब्दानाम् 'सर्वनाम' इति संज्ञा भवति ।", sarvadi.sanskritMeaning)
        assertEquals("", sarvadi.hindiMeaning)
        assertEquals("The words belonging to the सर्वादिगण are called सर्वनाम.", sarvadi.englishMeaning)
        assertEquals(GanaSource.GANAPATHA_DATA, sarvadi.source)
        assertEquals(GanaPathaSources.DATA_URL, sarvadi.sourceUrl)
        assertEquals(GanaKind.PATHA, sarvadi.kind)
        assertTrue(GanaPatha.contains(1, "सर्व"))
        assertTrue(GanaPatha.contains(1, " तद् "))
        assertTrue(GanaPatha.contains(1, "किम्"))
        assertFalse(GanaPatha.contains(1, "राम"))
    }

    @Test
    fun `ganapatha exposes source ganas in order`() {
        assertEquals(
            listOf(
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
                13,
                14,
                15,
                16,
                17,
                18,
                19,
                20,
                21,
            ),
            GanaPatha.all.take(21).map { it.sourceIndex },
        )
        assertEquals(262, GanaPatha.all.size)
        assertEquals(22, GanaPatha.all[21].sourceIndex)
        assertEquals(262, GanaPatha.all.last().sourceIndex)
        assertEquals((1..262).toList(), GanaPatha.all.map { it.sourceIndex })
        assertEquals("1.4.58", GanaPatha.require(4).sutra)
        assertEquals("प्रादयः", GanaPatha.require(4).sutraText)
        assertEquals(
            "The words included in the प्रादिगण are called निपात when used in a sense that does not denote an object.",
            GanaPatha.require(4).englishMeaning,
        )
        assertEquals(GanaKind.AKRTI, GanaPatha.require(3).kind)
        assertEquals(11, GanaPatha.require(11).sourceIndex)
        assertTrue(GanaPatha.require(11).resultSamjnas.isEmpty())
        assertTrue(GanaPatha.contains(4, "प्रति"))
        assertTrue(GanaPatha.contains(2, "स्वर्"))
        assertTrue(GanaPatha.contains(11, "देव"))
        assertEquals("2.1.59", GanaPatha.require(12).sutra)
        assertEquals("श्रेण्यादयः कृतादिभिः", GanaPatha.require(12).sutraText)
        assertEquals(GanaKind.AKRTI, GanaPatha.require(15).kind)
        assertEquals(GanaKind.UNSPECIFIED, GanaPatha.require(43).kind)
        assertEquals(
            setOf(dev.sanskrit.shiksha.Samjna.GATI),
            GanaPatha.require(5).resultSamjnas,
        )
        assertEquals(
            Adhikara("भविष्यदधिकारः", "3.3.15"),
            GanaPatha.require(41).adhikara,
        )
        assertTrue(GanaPatha.contains(12, "कृत"))
        assertTrue(GanaPatha.contains(15, "अकुतोभयः"))
        assertTrue(GanaPatha.contains(20, "शृगाल"))
        assertEquals("2.3.43", GanaPatha.require(22).sutra)
        assertTrue(GanaPatha.contains(25, "कार्षापण"))
        assertTrue(GanaPatha.contains(58, "भरद्वाज"))
        assertTrue(GanaPatha.contains(71, "यौधेय"))
    }

    @Test
    fun `implemented gana sutras resolve to concrete ashtadhyayi rules`() {
        listOf(1, 2, 3, 4, 5, 6, 32, 33, 34, 35, 36, 37, 38, 41, 42, 44, 45, 46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 74, 76, 77, 78, 79, 80, 81, 82, 83, 103, 105, 106, 112, 113, 116, 117, 118).forEach { sourceIndex ->
            val gana = GanaPatha.require(sourceIndex)
            assertEquals(gana.sutra, Ashtadhyayi.sutraFor(gana).sutra)
        }
        assertEquals(SarvanamaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(1)))
        assertEquals(SvaradiNipatamAvyayamSutra, Ashtadhyayi.sutraFor(GanaPatha.require(2)))
        assertEquals(ChadayoAsattveSutra, Ashtadhyayi.sutraFor(GanaPatha.require(3)))
        assertEquals(PradayaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(4)))
        assertEquals(UryadiCvidacashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(5)))
        assertEquals(SakshatPrabhrtiniCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(6)))
        assertEquals(BhrshadibhyoBhuvyacverLopashCaHalahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(32)))
        assertEquals(KandvadibhyoYakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(35)))
        assertEquals(NandigrahipacadibhyoLyuninyacahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(36)))
        assertEquals(NandigrahipacadibhyoLyuninyacahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(37)))
        assertEquals(NandigrahipacadibhyoLyuninyacahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(38)))
        assertEquals(BhavishyatiGamyadayahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(41)))
        assertEquals(ShidbhidadibhyoAngSutra, Ashtadhyayi.sutraFor(GanaPatha.require(42)))
        assertEquals(BhimadayoApadaneSutra, Ashtadhyayi.sutraFor(GanaPatha.require(44)))
        assertEquals(PashadibhyoYahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(74)))
        assertEquals(RajanyadibhyoVunSutra, Ashtadhyayi.sutraFor(GanaPatha.require(76)))
        assertEquals(BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra, Ashtadhyayi.sutraFor(GanaPatha.require(77)))
        assertEquals(BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra, Ashtadhyayi.sutraFor(GanaPatha.require(78)))
        assertEquals(KramadibhyoVunSutra, Ashtadhyayi.sutraFor(GanaPatha.require(80)))
        assertEquals(VasantadibhyashThakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(81)))
        assertEquals(KratukthadisutrantatThakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(79)))
        assertEquals(SankaladibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(82)))
        assertEquals(SuvastvadibhyoAnSutra, Ashtadhyayi.sutraFor(GanaPatha.require(83)))
        assertEquals(UtkaradibhyashChahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(103)))
        assertEquals(KattrayadibhyoDhakanySutra, Ashtadhyayi.sutraFor(GanaPatha.require(105)))
        assertEquals(NadyadibhyoDhakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(106)))
        assertEquals(SandhiveladyRtunakshatrebhyoAnSutra, Ashtadhyayi.sutraFor(GanaPatha.require(112)))
        assertEquals(DigadibhyoYatSutra, Ashtadhyayi.sutraFor(GanaPatha.require(113)))
        assertEquals(RgayandibhyoAnSutra, Ashtadhyayi.sutraFor(GanaPatha.require(116)))
        assertEquals(ShundikadibhyoAnSutra, Ashtadhyayi.sutraFor(GanaPatha.require(117)))
        assertEquals(ShandikadibhyoNyahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(118)))
        assertEquals(AjadyatastapSutra, Ashtadhyayi.sutraFor(GanaPatha.require(45)))
        assertEquals(NaShatsvasradibhyahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(46)))
        assertEquals(ShidGauradibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(48)))
        assertEquals(BahvadibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(49)))
        assertEquals(NaKrodadibahvacahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(50)))
        assertEquals(NadadibhyahPhakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(57)))
        assertEquals(GargadibhyoYanySutra, Ashtadhyayi.sutraFor(GanaPatha.require(59)))
        assertEquals(AshvapatyadibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(53)))
        assertEquals(BaahvadibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(55)))
        assertEquals(GotreKunjadibhyashCaPhanySutra, Ashtadhyayi.sutraFor(GanaPatha.require(56)))
        assertEquals(AnrshyanantaryeBidadibhyoAnySutra, Ashtadhyayi.sutraFor(GanaPatha.require(58)))
        assertEquals(AshvadibhyahPhanySutra, Ashtadhyayi.sutraFor(GanaPatha.require(60)))
        assertEquals(ShivadibhyoAnySutra, Ashtadhyayi.sutraFor(GanaPatha.require(61)))
        assertEquals(GrshtyadibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(64)))
        assertEquals(KurvadibhyoNyahSutra, Ashtadhyayi.sutraFor(GanaPatha.require(66)))
        assertEquals(KambojalLukSutra, Ashtadhyayi.sutraFor(GanaPatha.require(69)))
        assertEquals(ShubhradibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(62)))
        assertEquals(RevatyadibhyashThakSutra, Ashtadhyayi.sutraFor(GanaPatha.require(65)))
        assertEquals(TikadibhyahPhinySutra, Ashtadhyayi.sutraFor(GanaPatha.require(67)))
        assertEquals(KalyanyadinamInangSutra, Ashtadhyayi.sutraFor(GanaPatha.require(63)))
        assertEquals(LohitadidajbhyahKyashSutra, Ashtadhyayi.sutraFor(GanaPatha.require(33)))
        assertEquals(SukhadibhyoKartrvedanayamSutra, Ashtadhyayi.sutraFor(GanaPatha.require(34)))
        assertEquals(SharngaravadyanyoNginSutra, Ashtadhyayi.sutraFor(GanaPatha.require(51)))
        assertEquals(KraudyadibhyashCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(52)))
        assertEquals(UtsadibhyoAnySutra, Ashtadhyayi.sutraFor(GanaPatha.require(54)))
        assertEquals(VakinadinamKukCaSutra, Ashtadhyayi.sutraFor(GanaPatha.require(68)))
    }

    @Test
    fun `ganapatha finds every gana containing a member`() {
        assertEquals(listOf(1), GanaPatha.ganasContaining("इदम्").map { it.sourceIndex })
        assertEquals(listOf(11, 54), GanaPatha.ganasContaining("देव").map { it.sourceIndex })
        assertEquals(emptyList(), GanaPatha.ganasContaining("अकल्पितशब्दः"))
    }

    @Test
    fun `gana exposes member lookup helpers`() {
        val sarvadi = GanaPatha.require(1)

        assertTrue(sarvadi.contains(" सर्व "))
        assertEquals("सर्व", sarvadi.findMember("सर्व")?.text)
        assertEquals("तद्", sarvadi.requireMember(" तद् ").text)
        assertTrue("सर्व" in sarvadi.memberTexts)
        assertTrue("किम्" in sarvadi.normalizedMemberTexts)
        assertTrue(sarvadi.hasMeaning())
    }

    @Test
    fun `antargana members remain associated with their parent gana`() {
        val sarvadi = GanaPatha.require(1)

        assertTrue(sarvadi.isAntarGanaMember("डतर"))
        assertFalse(sarvadi.isAntarGanaMember("सर्व"))
        assertEquals(
            listOf("डतराद्यन्तर्गणः"),
            sarvadi.antarGanasContaining("डतम").map { it.name },
        )
        assertEquals(
            listOf(1),
            GanaPatha.antarGanasContaining("डतर").map { it.first.sourceIndex },
        )
        assertTrue(GanaPatha.isEligibleMember(1, "कतर"))
        assertTrue(GanaPatha.isEligibleMember(1, "एकतम"))
    }

    @Test
    fun `sarvadi conditions are executable and retain their source text`() {
        val sarvadi = GanaPatha.require(1)
        assertEquals(
            "व्यवस्थायाम् असंज्ञायाम्",
            sarvadi.requireMember("पूर्व").condition,
        )
        assertTrue(
            GanaPatha.isEligibleMember(
                1,
                "पूर्व",
                setOf(LexicalUse.RELATIVE_POSITION),
            ),
        )
        assertFalse(
            GanaPatha.isEligibleMember(
                1,
                "पूर्व",
                setOf(LexicalUse.RELATIVE_POSITION, LexicalUse.PROPER_NAME),
            ),
        )
        assertFalse(GanaPatha.isEligibleMember(1, "पूर्व"))
        assertTrue(GanaPatha.isEligibleMember(1, "तद्"))
    }

    @Test
    fun `sarvanama sutra applies sarvadi restrictions per term`() {
        val eligible = DerivationState(
            terms = listOf(
                DerivationTerm(
                    "stem",
                    "पूर्व",
                    TermKind.PRATIPADIKA,
                    lexicalUses = setOf(LexicalUse.RELATIVE_POSITION),
                ),
            ),
        )
        val ineligible = eligible.copy(
            terms = listOf(
                eligible.terms.single().copy(
                    lexicalUses = setOf(LexicalUse.RELATIVE_POSITION, LexicalUse.PROPER_NAME),
                ),
            ),
        )

        assertTrue(SarvanamaSutra.matches(eligible))
        assertFalse(SarvanamaSutra.matches(ineligible))
        assertTrue(SamjnaAssignment("stem", Samjna.SARVANAMA) in SarvanamaSutra.apply(eligible).state.samjnas)
    }

    @Test
    fun `sarvanama sutra recognizes ḍataraadi antargana forms`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "कतर", TermKind.PRATIPADIKA)),
        )

        assertTrue(SarvanamaSutra.matches(state))
        assertTrue(SamjnaAssignment("stem", Samjna.SARVANAMA) in SarvanamaSutra.apply(state).state.samjnas)
    }

    @Test
    fun `chadi gana members include translations`() {
        val chadi = GanaPatha.require(3)

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
            GanaPatha.require(999)
        }

        assertTrue(error.message!!.contains("999"))
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

    @Test
    fun `gana driven nipata and gati sutras enforce their semantic gates`() {
        val chadi = DerivationState(
            terms = listOf(DerivationTerm("term", "च", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.ASATTVA),
        )
        assertTrue(ChadayoAsattveSutra.matches(chadi))
        assertTrue(SamjnaAssignment("term", Samjna.NIPATA) in ChadayoAsattveSutra.apply(chadi).state.samjnas)
        assertFalse(ChadayoAsattveSutra.matches(chadi.copy(semanticFeatures = emptySet())))

        val gati = DerivationState(
            terms = listOf(DerivationTerm("term", "ऊरी", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.KRIYAYOGA),
        )
        assertTrue(UryadiCvidacashCaSutra.matches(gati))
        assertTrue(SamjnaAssignment("term", Samjna.GATI) in UryadiCvidacashCaSutra.apply(gati).state.samjnas)
        assertFalse(UryadiCvidacashCaSutra.matches(gati.copy(semanticFeatures = emptySet())))

        val pradi = DerivationState(
            terms = listOf(DerivationTerm("term", "प्र", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.KRIYAYOGA),
        )
        assertTrue(UpasargahKriyayogeSutra.matches(pradi))
        assertTrue(GatishCaSutra.matches(pradi))
        assertTrue(SamjnaAssignment("term", Samjna.UPASARGA) in UpasargahKriyayogeSutra.apply(pradi).state.samjnas)
        assertTrue(SamjnaAssignment("term", Samjna.GATI) in GatishCaSutra.apply(pradi).state.samjnas)
    }

    @Test
    fun `kandvadibhyo yak introduces yak after an eligible pratipadika`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("base", "कण्डूञ्", TermKind.PRATIPADIKA)),
        )

        assertTrue(KandvadibhyoYakSutra.matches(state))
        val result = KandvadibhyoYakSutra.apply(state).state
        assertEquals(listOf("कण्डूञ्", "य"), result.terms.map { it.surface })
        assertEquals("यक्", result.terms.last().upadesha)
        assertFalse(KandvadibhyoYakSutra.matches(result))
    }

    @Test
    fun `ajadyatastap includes ajadi members that do not end in short a`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "अजा", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI),
            activeAdhikaras = setOf("4.1.3"),
        )

        assertTrue(AjadyatastapSutra.matches(state))
        assertEquals("टाप्", AjadyatastapSutra.apply(state).state.terms.last().upadesha)
    }

    @Test
    fun `shid gauradibhyash ca introduces ngish for a gauradi stem`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "गौर", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI),
            activeAdhikaras = setOf("4.1.3"),
        )

        assertTrue(ShidGauradibhyashCaSutra.matches(state))
        assertEquals("ङीष्", ShidGauradibhyashCaSutra.apply(state).state.terms.last().upadesha)
    }

    @Test
    fun `bahvadibhyash ca offers an optional ngish branch`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "बहु", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI),
            activeAdhikaras = setOf("4.1.3"),
        )

        assertTrue(BahvadibhyashCaSutra.matches(state))
        assertEquals("ङीष्", BahvadibhyashCaSutra.apply(state).state.terms.last().upadesha)
        assertEquals(2, BahvadibhyashCaSutra.applyAll(state).size)
    }

    @Test
    fun `nadadibhyah phak selects phak in the apatya sense`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "नड", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.APATYA),
        )

        assertTrue(NadadibhyahPhakSutra.matches(state))
        assertEquals("फक्", NadadibhyahPhakSutra.apply(state).state.terms.last().upadesha)
        assertFalse(NadadibhyahPhakSutra.matches(state.copy(semanticFeatures = emptySet())))
    }

    @Test
    fun `gargadibhyo yany selects yany in the apatya sense`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "गर्ग", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.APATYA),
        )

        assertTrue(GargadibhyoYanySutra.matches(state))
        assertEquals("यञ्", GargadibhyoYanySutra.apply(state).state.terms.last().upadesha)
    }

    @Test
    fun `batch patronymic sutras select their distinct affixes`() {
        val apatya = setOf(SemanticFeature.APATYA)
        val ashvapati = DerivationState(listOf(DerivationTerm("stem", "अश्वपति", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val baahvadi = DerivationState(listOf(DerivationTerm("stem", "बाहु", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val gotra = DerivationState(listOf(DerivationTerm("stem", "कुञ्ज", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.GOTRA))

        assertEquals("अण्", AshvapatyadibhyashCaSutra.apply(ashvapati).state.terms.last().upadesha)
        assertEquals("इञ्", BaahvadibhyashCaSutra.apply(baahvadi).state.terms.last().upadesha)
        assertEquals("च्फञ्", GotreKunjadibhyashCaPhanySutra.apply(gotra).state.terms.last().upadesha)

        val bidadi = DerivationState(listOf(DerivationTerm("stem", "बिद", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.GOTRA))
        val ashvadi = DerivationState(listOf(DerivationTerm("stem", "अश्व", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.GOTRA))
        assertEquals("अञ्", AnrshyanantaryeBidadibhyoAnySutra.apply(bidadi).state.terms.last().upadesha)
        assertEquals("फञ्", AshvadibhyahPhanySutra.apply(ashvadi).state.terms.last().upadesha)

        val shivadi = DerivationState(listOf(DerivationTerm("stem", "शिव", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val grshtyadi = DerivationState(listOf(DerivationTerm("stem", "गृष्टि", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        assertEquals("अण्", ShivadibhyoAnySutra.apply(shivadi).state.terms.last().upadesha)
        assertEquals("ढञ्", GrshtyadibhyashCaSutra.apply(grshtyadi).state.terms.last().upadesha)

        val kurvadi = DerivationState(listOf(DerivationTerm("stem", "कुरु", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        assertEquals("ण्य", KurvadibhyoNyahSutra.apply(kurvadi).state.terms.last().upadesha)

        val kamboja = DerivationState(
            listOf(
                DerivationTerm("stem", "कम्बोज", TermKind.PRATIPADIKA),
                DerivationTerm("any", "अ", TermKind.PRATYAYA, upadesha = "अञ्"),
            ),
            semanticFeatures = setOf(SemanticFeature.TADRAJA),
        )
        assertTrue(KambojalLukSutra.matches(kamboja))
        assertTrue(KambojalLukSutra.apply(kamboja).state.terms.none { it.id == "any" })

        val shubhradi = DerivationState(listOf(DerivationTerm("stem", "शुभ्र", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val revatyadi = DerivationState(listOf(DerivationTerm("stem", "रेवती", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val tikadi = DerivationState(listOf(DerivationTerm("stem", "तिक", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        assertEquals("ढक्", ShubhradibhyashCaSutra.apply(shubhradi).state.terms.last().upadesha)
        assertEquals("ठक्", RevatyadibhyashThakSutra.apply(revatyadi).state.terms.last().upadesha)
        assertEquals("फिञ्", TikadibhyahPhinySutra.apply(tikadi).state.terms.last().upadesha)

        val kalyanyadi = DerivationState(listOf(DerivationTerm("stem", "कल्याणी", TermKind.PRATIPADIKA)), semanticFeatures = apatya)
        val kalyanyadiResult = KalyanyadinamInangSutra.apply(kalyanyadi).state
        assertEquals("कल्याणिन्", kalyanyadiResult.terms.first().surface)
        assertEquals("ढक्", kalyanyadiResult.terms.last().upadesha)

        val lohitadi = DerivationState(listOf(DerivationTerm("stem", "लोहित", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.BHAVE))
        assertEquals("क्यष्", LohitadidajbhyahKyashSutra.apply(lohitadi).state.terms.last().upadesha)

        val bhrshadi = DerivationState(listOf(DerivationTerm("stem", "सुमनस्", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.BHAVE))
        val bhrshadiResult = BhrshadibhyoBhuvyacverLopashCaHalahSutra.apply(bhrshadi).state
        assertEquals("सुमन", bhrshadiResult.terms.first().surface)
        assertEquals("क्यङ्", bhrshadiResult.terms.last().upadesha)

        val sukhadi = DerivationState(listOf(DerivationTerm("stem", "सुख", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.KARTR_VEDANA))
        assertEquals("क्यङ्", SukhadibhyoKartrvedanayamSutra.apply(sukhadi).state.terms.last().upadesha)

        val nandyadi = DerivationState(listOf(DerivationTerm("stem", "नन्दनः", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.KARTARI))
        val grahyadi = DerivationState(listOf(DerivationTerm("stem", "ग्राही", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.KARTARI))
        val pacadi = DerivationState(listOf(DerivationTerm("stem", "पच", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.KARTARI))
        assertEquals("ल्यु", NandigrahipacadibhyoLyuninyacahSutra.apply(nandyadi).state.terms.last().upadesha)
        assertEquals("णिनि", NandigrahipacadibhyoLyuninyacahSutra.apply(grahyadi).state.terms.last().upadesha)
        assertEquals("अच्", NandigrahipacadibhyoLyuninyacahSutra.apply(pacadi).state.terms.last().upadesha)

        val gamyadi = DerivationState(listOf(DerivationTerm("stem", "गमी", TermKind.PRATIPADIKA)))
        assertTrue(SemanticFeature.BHAVISYAT in BhavishyatiGamyadayahSutra.apply(gamyadi).state.semanticFeatures)

        val bhidadi = DerivationState(
            listOf(DerivationTerm("stem", "विदा", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI),
        )
        assertEquals("अङ्", ShidbhidadibhyoAngSutra.apply(bhidadi).state.terms.last().upadesha)

        val bhimadi = DerivationState(
            listOf(DerivationTerm("stem", "भीम", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.APADANA),
        )
        assertTrue(SemanticFeature.UNADI_LICENSED in BhimadayoApadaneSutra.apply(bhimadi).state.semanticFeatures)

        val pashadi = DerivationState(
            listOf(DerivationTerm("stem", "पाश", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.SAMUHA),
        )
        assertEquals("य", PashadibhyoYahSutra.apply(pashadi).state.terms.last().upadesha)

        val rajanyadi = DerivationState(
            listOf(DerivationTerm("stem", "राजन्य", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.VISHAYA_DESE),
        )
        assertEquals("वुञ्", RajanyadibhyoVunSutra.apply(rajanyadi).state.terms.last().upadesha)

        val bhaurikyadi = DerivationState(
            listOf(DerivationTerm("stem", "भौरिकि", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.VISHAYA_DESE),
        )
        val aishukaryadi = DerivationState(
            listOf(DerivationTerm("stem", "ऐषुकारि", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.VISHAYA_DESE),
        )
        assertEquals("विधल्", BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra.apply(bhaurikyadi).state.terms.last().upadesha)
        assertEquals("भक्तल्", BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra.apply(aishukaryadi).state.terms.last().upadesha)

        val kramadi = DerivationState(listOf(DerivationTerm("stem", "क्रम", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.ADHYAYANA_VEDANA))
        val vasantadi = DerivationState(listOf(DerivationTerm("stem", "वसन्त", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.ADHYAYANA_VEDANA))
        assertEquals("वुन्", KramadibhyoVunSutra.apply(kramadi).state.terms.last().upadesha)
        assertEquals("ठक्", VasantadibhyashThakSutra.apply(vasantadi).state.terms.last().upadesha)
        val ukthadi = DerivationState(listOf(DerivationTerm("stem", "उक्थ", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.ADHYAYANA_VEDANA))
        assertEquals("ठक्", KratukthadisutrantatThakSutra.apply(ukthadi).state.terms.last().upadesha)

        val sankaladi = DerivationState(
            listOf(DerivationTerm("stem", "संकल", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.NIVASA),
        )
        val suvastvadi = DerivationState(
            listOf(DerivationTerm("stem", "सुवास्तु", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.NIVASA),
        )
        assertEquals("अण्", SankaladibhyashCaSutra.apply(sankaladi).state.terms.last().upadesha)
        assertEquals("अण्", SuvastvadibhyoAnSutra.apply(suvastvadi).state.terms.last().upadesha)

        val utkaradi = DerivationState(listOf(DerivationTerm("stem", "उत्कर", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.CHATURARTHIKA))
        val kattryadi = DerivationState(listOf(DerivationTerm("stem", "कत्रि", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.CHATURARTHIKA))
        val nadyadi = DerivationState(listOf(DerivationTerm("stem", "नदी", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.JATA))
        assertEquals("छ", UtkaradibhyashChahSutra.apply(utkaradi).state.terms.last().upadesha)
        assertEquals("ढकञ्", KattrayadibhyoDhakanySutra.apply(kattryadi).state.terms.last().upadesha)
        assertEquals("ढक्", NadyadibhyoDhakSutra.apply(nadyadi).state.terms.last().upadesha)

        val sandhiveladi = DerivationState(listOf(DerivationTerm("stem", "संध्या", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.KALAVRTTI))
        val digadi = DerivationState(listOf(DerivationTerm("stem", "दिश्", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.TATRA_BHAVA))
        assertEquals("अण्", SandhiveladyRtunakshatrebhyoAnSutra.apply(sandhiveladi).state.terms.last().upadesha)
        assertEquals("यत्", DigadibhyoYatSutra.apply(digadi).state.terms.last().upadesha)
        val rgayanadi = DerivationState(listOf(DerivationTerm("stem", "ऋगयन", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.VYAKHYANA))
        val shundikadi = DerivationState(listOf(DerivationTerm("stem", "शुण्डिक", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.TATAH_AGATA))
        assertEquals("अण्", RgayandibhyoAnSutra.apply(rgayanadi).state.terms.last().upadesha)
        assertEquals("अण्", ShundikadibhyoAnSutra.apply(shundikadi).state.terms.last().upadesha)
        val shandikadi = DerivationState(listOf(DerivationTerm("stem", "शण्डिक", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.ABHIJANA))
        assertEquals("ञ्य", ShandikadibhyoNyahSutra.apply(shandikadi).state.terms.last().upadesha)

        val sharngaravadi = DerivationState(listOf(DerivationTerm("stem", "शार्ङ्गरव", TermKind.PRATIPADIKA)), semanticFeatures = setOf(SemanticFeature.STRI))
        assertEquals("ङीन्", SharngaravadyanyoNginSutra.apply(sharngaravadi).state.terms.last().upadesha)

        val kraudyadi = DerivationState(
            listOf(DerivationTerm("stem", "क्रौडि", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI, SemanticFeature.GOTRA),
        )
        assertEquals("ष्यङ्", KraudyadibhyashCaSutra.apply(kraudyadi).state.terms.last().upadesha)

        val utsadi = DerivationState(
            listOf(DerivationTerm("stem", "उत्स", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.PRAGDIVYATIYA),
        )
        assertEquals("अञ्", UtsadibhyoAnySutra.apply(utsadi).state.terms.last().upadesha)

        val svasradi = DerivationState(
            listOf(DerivationTerm("stem", "स्वसृ", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI),
        )
        assertEquals("4.1.10", NaShatsvasradibhyahSutra.apply(svasradi).state.blockedSutras["STRI_PRATYAYA"])

        val krodadi = DerivationState(
            listOf(DerivationTerm("stem", "क्रोडा", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.STRI, SemanticFeature.SVANGA),
        )
        assertEquals("4.1.56", NaKrodadibahvacahSutra.apply(krodadi).state.blockedSutras["4.1.54"])

        val vakinadi = DerivationState(
            listOf(DerivationTerm("stem", "वाकिन", TermKind.PRATIPADIKA)),
            semanticFeatures = setOf(SemanticFeature.APATYA, SemanticFeature.UDICYA),
        )
        val vakinadiResult = VakinadinamKukCaSutra.apply(vakinadi).state
        assertEquals("वाकिनक", vakinadiResult.terms.first().surface)
        assertEquals("फिञ्", vakinadiResult.terms.last().upadesha)
    }
}
