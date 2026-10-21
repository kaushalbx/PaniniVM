package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.svadi.MiDhatu
import dev.panini.dhatupatha.tanadi.TanDhatu
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.7: भृमृशीतॄचरित्सरितनिधनिमिमस्जिभ्य उः
object BhrMrShiTrCariTsariTaniDhaniMiMasjibhyaUhSutra : UnadiSutra(
    number = "1.7",
    text = "भृमृशीतॄचरित्सरितनिधनिमिमस्जिभ्य उः",
    roots = setOf(
        JuhotyadiDhatus.all.single { it.id == "03.0006" },
        TudadiDhatus.all.single { it.id == "06.0139" },
        AdadiDhatus.all.single { it.id == "02.0026" },
        BhvadiDhatus.all.single { it.id == "01.1124" },
        BhvadiDhatus.all.single { it.id == "01.0640" },
        BhvadiDhatus.all.single { it.id == "01.0635" },
        TanDhatu(),
        JuhotyadiDhatus.all.single { it.id == "03.0024" },
        MiDhatu(),
        TudadiDhatus.all.single { it.id == "06.0151" },
    ),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    rootSamjnaMap = mapOf(
        "डुभृञ्" to Samjna.Rudhi("भरु"), "मृङ्" to Samjna.Rudhi("मरु"),
        "शीङ्" to Samjna.Rudhi("शयु"), "तॄ" to Samjna.Rudhi("तरु"),
        "चरँ" to Samjna.Rudhi("चरु"), "त्सरँ" to Samjna.Rudhi("त्सरु"),
        TanDhatu().upadesha to Samjna.Rudhi("तनु"), "धनँ" to Samjna.Rudhi("धनु"),
        MiDhatu().upadesha to Samjna.Rudhi("मयु"), "टुमस्जोँ" to Samjna.Rudhi("मद्गु"),
    ),
    rootMeaningMap = mapOf(
        "डुभृञ्" to Artha.Rudhi("भरु", "master or lord"), "मृङ्" to Artha.Rudhi("मरु", "arid region"),
        "शीङ्" to Artha.Rudhi("शयु", "python snake"), "तॄ" to Artha.Rudhi("तरु", "tree"),
        "चरँ" to Artha.Rudhi("चरु", "sacrificial oblation"), "त्सरँ" to Artha.Rudhi("त्सरु", "sword hilt"),
        TanDhatu().upadesha to Artha.Rudhi("तनु", "body"), "धनँ" to Artha.Rudhi("धनु", "bow"),
        MiDhatu().upadesha to Artha.Rudhi("मयु", "kinnara"), "टुमस्जोँ" to Artha.Rudhi("मद्गु", "water bird"),
    ),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA),
        UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.DA),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("इस सूत्र में प्रत्येक धातु से प्राप्त शब्द का रूढ़ अर्थ पृथक् है।"),
    hindiExplanation = "भृ, मृ, शी, तॄ, चर्, त्सर्, तन्, धन्, मि और मस्ज् धातुओं के बाद उ प्रत्यय होता है। इससे क्रमशः भरु, मरु, शयु, तरु, चरु, त्सरु, तनु, धनु, मयु और मद्गु शब्द सिद्ध होते हैं। " +
        "इनके अर्थ क्रमशः स्वामी, निर्जल प्रदेश, अजगर, वृक्ष, देवताओं का हविष्य, खड्ग की मूठ, शरीर, धनुष, किन्नर और जलपक्षी हैं। मद्गु की सिद्धि में ज् को ग् तथा स् को द् आदेश होता है।",
)
