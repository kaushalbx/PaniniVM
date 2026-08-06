package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti

data class SamasaTestCase(
    val id: String,
    val description: String,
    val padas: List<SamasaPada>,
    val type: SamasaType,
    val expectedSurface: String,
    val expectedStem: String? = null,
    val expectedSutra: String? = null,
)

object SamasaBenchmarkData {
    val canonicalCases = listOf(
        // 1. Consonant stems ending in -न् (8.2.7 नलोपः प्रातिपदिकान्तस्य)
        SamasaTestCase(
            id = "NLOPA_001",
            description = "8.2.7 Na-lopa for rajan + purusha",
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "राजपुरुषः",
            expectedSutra = "8.2.7",
        ),
        SamasaTestCase(
            id = "NLOPA_002",
            description = "8.2.7 Na-lopa for atman + jnana",
            padas = listOf(
                SamasaPada("आत्मन्", Vibhakti.SASTHI),
                SamasaPada("ज्ञान", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "आत्मज्ञानः",
            expectedSutra = "8.2.7",
        ),
        SamasaTestCase(
            id = "NLOPA_003",
            description = "8.2.7 Na-lopa for karman + phala",
            padas = listOf(
                SamasaPada("कर्मन्", Vibhakti.SASTHI),
                SamasaPada("फल", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "कर्मफलः",
            expectedSutra = "8.2.7",
        ),
        SamasaTestCase(
            id = "NLOPA_004",
            description = "8.2.7 Na-lopa for svamin + bhakti",
            padas = listOf(
                SamasaPada("स्वामिन्", Vibhakti.SASTHI),
                SamasaPada("भक्ति", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "स्वामिभक्तिः",
            expectedSutra = "8.2.7",
        ),

        // 2. Samāsānta affixes (5.4.91, 5.4.125, etc.)
        SamasaTestCase(
            id = "SAMASANTA_001",
            description = "5.4.91 Rajahah Sakhibhyas Tac - mahat + rajan",
            padas = listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.KARMADHARAYA,
            expectedSurface = "महाराजः",
            expectedSutra = "5.4.91",
        ),
        SamasaTestCase(
            id = "SAMASANTA_002",
            description = "5.4.91 Rajahah Sakhibhyas Tac - param + sakhi",
            padas = listOf(
                SamasaPada("परम", Vibhakti.PRATHAMA),
                SamasaPada("सखि", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.KARMADHARAYA,
            expectedSurface = "परमसखः",
            expectedSutra = "5.4.91",
        ),
        SamasaTestCase(
            id = "SAMASANTA_003",
            description = "5.4.151 Kap affix - vyudha + uras",
            padas = listOf(
                SamasaPada("व्यूढ", Vibhakti.PRATHAMA),
                SamasaPada("उरस्", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
            expectedSurface = "व्यूढोरस्कः",
            expectedSutra = "5.4.151",
        ),
        SamasaTestCase(
            id = "SAMASANTA_004",
            description = "5.4.154 Kap affix - a + putra",
            padas = listOf(
                SamasaPada("अ", Vibhakti.PRATHAMA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
            expectedSurface = "अपुत्रकः",
            expectedSutra = "5.4.154",
        ),
        SamasaTestCase(
            id = "SAMASANTA_005",
            description = "5.4.153 Kap affix - bahu + kumari",
            padas = listOf(
                SamasaPada("बहु", Vibhakti.PRATHAMA),
                SamasaPada("कुमारी", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
            expectedSurface = "बहुकुमारीकः",
            expectedSutra = "5.4.153",
        ),

        // 3. Pūrvapada alterations (6.3.100, 6.3.86, 6.3.87)
        SamasaTestCase(
            id = "PURVAPADA_001",
            description = "6.3.100 Mahat in Karmadharaya - mahat + navami",
            padas = listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("नवमी", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.KARMADHARAYA,
            expectedSurface = "महानवमी",
        ),
        SamasaTestCase(
            id = "PURVAPADA_002",
            description = "6.3.86 Dvandva matu + pitu",
            padas = listOf(
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVANDVA,
            expectedSurface = "मातृपितरौ",
        ),
        SamasaTestCase(
            id = "PURVAPADA_003",
            description = "6.3.87 Dvandva pitu + matu",
            padas = listOf(
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVANDVA,
            expectedSurface = "पित्रामाते",
        ),

        // 4. Aluk Tatpuruṣa (6.3.7)
        SamasaTestCase(
            id = "ALUK_001",
            description = "6.3.7 Aluk Tatpurusa - atman + pada",
            padas = listOf(
                SamasaPada("आत्मन्", Vibhakti.SASTHI),
                SamasaPada("पद", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.ALUK_TATPURUSA,
            expectedSurface = "आत्मनेपदम्",
        ),

        // 5. Nañ Tatpuruṣa (2.2.6)
        SamasaTestCase(
            id = "NAN_001",
            description = "2.2.6 Nanj Tatpurusa for consonant - na + brahmana",
            padas = listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ब्राह्मण", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.NAN_TATPURUSA,
            expectedSurface = "अब्राह्मणः",
            expectedSutra = "2.2.6",
        ),
        SamasaTestCase(
            id = "NAN_002",
            description = "2.2.6 Nanj Tatpurusa for vowel - na + asva",
            padas = listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("अश्व", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.NAN_TATPURUSA,
            expectedSurface = "अनश्वः",
            expectedSutra = "2.2.6",
        ),

        // 6. Case Tatpuruṣa & Avyayībhāva / Dvigu
        SamasaTestCase(
            id = "CASE_001",
            description = "2.1.24 Dvitiya Tatpurusa - krsna + srita",
            padas = listOf(
                SamasaPada("कृष्ण", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "कृष्णश्रितः",
            expectedSutra = "2.1.24",
        ),
        SamasaTestCase(
            id = "CASE_002",
            description = "2.1.36 Caturthi Tatpurusa - yupa + daru",
            padas = listOf(
                SamasaPada("यूप", Vibhakti.CHATURTHI),
                SamasaPada("दारु", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.TATPURUSA,
            expectedSurface = "यूपदारुः",
            expectedSutra = "2.1.36",
        ),
        SamasaTestCase(
            id = "CASE_003",
            description = "2.1.6 Avyayibhava - upa + krsna",
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.AVYAYIBHAVA,
            expectedSurface = "उपकृष्णम्",
            expectedSutra = "2.1.6",
        ),
        SamasaTestCase(
            id = "AVYAYIBHAVA_002",
            description = "2.1.6 Avyayibhava - anu + ganga",
            padas = listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA),
                SamasaPada("गङ्गा", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.AVYAYIBHAVA,
            expectedSurface = "अनुगङ्गा",
        ),
        SamasaTestCase(
            id = "AVYAYIBHAVA_003",
            description = "2.1.6 Avyayibhava - yatha + sakti",
            padas = listOf(
                SamasaPada("यथा", Vibhakti.PRATHAMA),
                SamasaPada("शक्ति", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.AVYAYIBHAVA,
            expectedSurface = "यथाशक्तिः",
        ),

        // 7. Bahuvrīhi Compounds (2.2.24)
        SamasaTestCase(
            id = "BAHUVRIHI_001",
            description = "2.2.24 Bahuvrihi - pita + ambara",
            padas = listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
            expectedSurface = "पीताम्बरः",
            expectedSutra = "2.2.24",
        ),
        SamasaTestCase(
            id = "BAHUVRIHI_002",
            description = "2.2.24 Bahuvrihi - dasa + anana",
            padas = listOf(
                SamasaPada("दश", Vibhakti.PRATHAMA),
                SamasaPada("आनन", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.BAHUVRIHI,
            expectedSurface = "दशाननः",
        ),

        // 8. Dvigu Compounds (2.1.52)
        SamasaTestCase(
            id = "DVIGU_001",
            description = "2.1.52 Dvigu - tri + bhuvana",
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("भुवन", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVIGU,
            expectedSurface = "त्रिभुवनम्",
        ),
        SamasaTestCase(
            id = "DVIGU_002",
            description = "2.1.52 Dvigu - tri + loka",
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("लोक", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVIGU,
            expectedSurface = "त्रिलोकम्",
        ),

        // 9. Dvandva Compounds (2.2.29)
        SamasaTestCase(
            id = "DVANDVA_001",
            description = "2.2.29 Dvandva - rama + laksmana",
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVANDVA,
            expectedSurface = "रामलक्ष्मणौ",
        ),
        SamasaTestCase(
            id = "DVANDVA_002",
            description = "2.4.2 Samahara Dvandva - pani + pada",
            padas = listOf(
                SamasaPada("पाणि", Vibhakti.PRATHAMA),
                SamasaPada("पाद", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVANDVA,
            expectedSurface = "पाणिपादम्",
        ),
        SamasaTestCase(
            id = "DVANDVA_003",
            description = "2.2.29 Multi-pada Dvandva - rama + laksmana + bharata + satrughna",
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
                SamasaPada("भरत", Vibhakti.PRATHAMA),
                SamasaPada("शत्रुघ्न", Vibhakti.PRATHAMA),
            ),
            type = SamasaType.DVANDVA,
            expectedSurface = "रामलक्ष्मणभरतशत्रुघ्णाः",
        ),
    )
}
