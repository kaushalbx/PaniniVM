package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama

// 1.19: वलेर्गुक्च
object ValerGukCaSutra : UnadiSutra(
    number = "1.19",
    text = "वलेर्गुक्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0564" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    rootSamjnaMap = mapOf("वलँ" to Samjna.Rudhi("वल्गु")),
    rootMeaningMap = mapOf("वलँ" to Artha.Rudhi("वल्गु")),
    varnaAgamas = listOf(
        UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("वल्गु"),
    hindiExplanation = "संवरण और सञ्चरण अर्थ वाले वलँ धातु के बाद उ प्रत्यय होता है। पिछले सूत्र से गुक् का विधान भी यहाँ चकार द्वारा ग्रहण किया गया है। गुक् में क् इत्संज्ञक और उ उच्चारण-सहायक होने से प्रभावी ग्-वर्ण प्रत्यय से पहले जुड़ता है; फलतः वल् + ग् + उ से वल्गु रूप सिद्ध होता है। " +
        "१.९ का नित्-अधिकार १.१८ तक ही था, इसलिए इस सूत्र में नित् अथवा उससे प्राप्त आद्युदात्त नहीं जोड़ा गया है।",
)
