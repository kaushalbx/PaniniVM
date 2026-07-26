package dev.panini.analysis

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import kotlin.collections.any
import kotlin.collections.firstOrNull
import kotlin.collections.isNotEmpty
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlin.text.contains
import kotlin.text.startsWith
import kotlin.text.trimEnd

/** Semantic valency facts consumed by kāraka-saṃjñā rules. */
object DhatuKarakaProfiles {
    private val profiles = listOf(
        DhatuKarakaProfile(
            setOf("दा", "ददाति", "यच्छति", "देहि"),
            setOf(SemanticRelation.RECIPIENT)
        ),
        DhatuKarakaProfile(setOf("लिख्", "लिख"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("पलाय्", "पलाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("भू", "प्रभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(
            setOf(
                "शी",
                "स्था",
                "तिष्ठति",
                "तिष्ठ",
                "आस्",
                "अधिशी",
                "अधिस्था",
                "अध्यास्"
            ), setOf(SemanticRelation.LOCATION)
        ),
        DhatuKarakaProfile(setOf("जन्", "जन", "जाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(
            setOf(
                "क्रुध्",
                "क्रुध",
                "द्रुह",
                "ईर्ष्या",
                "असूया",
                "अभिक्रुध",
                "अभिद्रुह"
            ), setOf(SemanticRelation.RECIPIENT)
        ),
        DhatuKarakaProfile(
            setOf(
                "भी",
                "बिभेति",
                "बिभ्यति",
                "बिभ्यात्",
                "भीति",
                "बिभ",
                "त्रा",
                "त्राय"
            ), setOf(SemanticRelation.SOURCE)
        ),
        DhatuKarakaProfile(setOf("रुच", "रोच"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(
            setOf("वस", "वसति", "वसत", "उपवस", "अनुवस", "अधिवस", "आवस"),
            setOf(SemanticRelation.LOCATION)
        ),
        DhatuKarakaProfile(setOf("पराजि", "पराजय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(
            setOf("अधी", "पठ"),
            setOf(SemanticRelation.SOURCE, SemanticRelation.DESIRED_OBJECT)
        ),
        DhatuKarakaProfile(setOf("स्पृह", "स्पृहय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अभिनिविश"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("वारय", "वार"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("निली", "तिरोभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("धारय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("श्लाघ", "ह्नु", "शप"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("दिव", "दीव्"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("परिक्री", "क्री"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("प्रतिश्रु", "आश्रु"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(
            setOf("ग्रह्", "गृह्णाति", "गृह्ण", "अनुगृ", "प्रतिगृ"),
            setOf(SemanticRelation.RECIPIENT, SemanticRelation.DESIRED_OBJECT)
        ),
        // Additional Dhātus
        DhatuKarakaProfile(
            setOf("कृ", "करोति", "करो", "कुर्व", "कारय", "अकृ"),
            setOf(
                SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.PROMPTER_CAUSE,
                SemanticRelation.INSTRUMENT
            )
        ),
        DhatuKarakaProfile(
            setOf(
                "गम्",
                "गच्छति",
                "गच्छ",
                "आगम्",
                "आगच्छ",
                "अनुगम्",
                "अनुगच्छ",
                "उपागम्",
                "उपागच्छ"
            ),
            setOf(
                SemanticRelation.MOTION_GOAL,
                SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.LOCATION
            )
        ),
        DhatuKarakaProfile(
            setOf("पा", "पिबति", "पिब", "पाति"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)
        ),
        DhatuKarakaProfile(
            setOf("दृश्", "पश्यति", "पश्य", "द्रक्ष्य"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDIFFERENT_OBJECT)
        ),
        DhatuKarakaProfile(
            setOf("लभ्", "लभते", "लभ", "लप्स्य"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)
        ),
        DhatuKarakaProfile(
            setOf("ज्ञा", "जानाति", "जाना", "ज्ञाय"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MEMORY_OR_RULING_OBJECT)
        ),
        DhatuKarakaProfile(
            setOf("श्रु", "शृणोति", "शृणु", "श्रोष्य"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)
        ),
        DhatuKarakaProfile(
            setOf("वच्", "वक्ति", "वक्", "ब्रू", "कथ", "कथय", "भाष्"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.RECIPIENT)
        ),
        DhatuKarakaProfile(
            setOf("हृ", "हरति", "हर", "हरय"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)
        ),
        DhatuKarakaProfile(setOf("गण", "गणयति", "गणय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(
            setOf("युज्", "योजयति", "युञ्ज", "योजय"),
            setOf(SemanticRelation.DESIRED_OBJECT)
        ),
        DhatuKarakaProfile(setOf("इष्", "इच्छति", "इच्छ"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(
            setOf("नी", "नयति", "नय"),
            setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MOTION_GOAL)
        ),
    )

    fun forDhatu(dhatu: Dhatu): DhatuKarakaProfile? {
        val profile = forSurface(dhatu.sourceSurface) ?: forSurface(dhatu.upadesha)
        if (profile != null) return profile
        if (dhatu.semanticRelations.isNotEmpty()) {
            return DhatuKarakaProfile(setOf(dhatu.sourceSurface, dhatu.upadesha) + dhatu.surfaceAliases, dhatu.semanticRelations)
        }
        return null
    }

    fun forSurface(surface: String): DhatuKarakaProfile? {
        val normSurface = surface.trimEnd('्', 'ँ')
        val registeredProfile = profiles.firstOrNull { profile ->
            profile.surfaces.any { entry ->
                val normEntry = entry.trimEnd('्', 'ँ')
                surface == entry || normSurface == normEntry || surface.startsWith(entry) || entry.startsWith(surface)
            }
        }
        val dhatuEntries = DhatuPatha.all.filter { dhatu ->
            val normUp = dhatu.upadesha.trimEnd('्', 'ँ')
            val normSrc = dhatu.sourceSurface.trimEnd('्', 'ँ')
            surface == dhatu.upadesha || surface == dhatu.sourceSurface || surface == dhatu.derivationalSurface ||
                normSurface == normUp || normSurface == normSrc ||
                dhatu.surfaceAliases.contains(surface) ||
                dhatu.surfaceAliases.any { entry ->
                    val normEntry = entry.trimEnd('्', 'ँ')
                    surface == entry || normSurface == normEntry || surface.startsWith(entry) || entry.startsWith(surface)
                }
        }
        val dhatuRelations = dhatuEntries.flatMap { it.semanticRelations }.toSet()
        val combinedRelations = (registeredProfile?.relations.orEmpty() + dhatuRelations)
        if (combinedRelations.isEmpty() && registeredProfile == null) return null
        return DhatuKarakaProfile(
            surfaces = registeredProfile?.surfaces ?: setOf(surface),
            relations = combinedRelations,
        )
    }
}
