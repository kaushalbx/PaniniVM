package dev.panini.vyakaranam.analysis

import dev.panini.shiksha.Samjna

data class PhonologicalContext(
    val targetPhoneme: String,
    val secondPhoneme: String = "",
    val environment: String = "",
    val isConjunctConsonants: Boolean = false,
    val isNasalized: Boolean = false,
    val samePlaceAndEffort: Boolean = false,
)

data class MorphologicalContext(
    val surface: String,
    val stem: String = "",
    val isMeaningfulBase: Boolean = false,
    val isRoot: Boolean = false,
    val isAffix: Boolean = false,
    val hasSupPratyaya: Boolean = false,
    val hasTingPratyaya: Boolean = false,
    val isDualEndingInIdUdEd: Boolean = false,
)

sealed class SamjnaRuleResult {
    data class Assigned(
        val samjna: Samjna,
        val evidence: KarakaEvidence,
    ) : SamjnaRuleResult()

    object Unmatched : SamjnaRuleResult()
}

object SamjnaRuleEngine {
    private val vrddhiVowels = setOf("आ", "ऐ", "औ")
    private val gunaVowels = setOf("अ", "ए", "ओ")
    private val nasalPhonemes = setOf("ङ्", "ञ्", "ण्", "न्", "म्", "ँ", "ङ", "ञ", "ण", "न", "म")
    private val commonRoots = setOf("भू", "पठ्", "गम्", "कृ", "हृ", "स्था", "दा", "दृश्", "वद्", "जि", "नी")

    fun resolvePhonological(context: PhonologicalContext): SamjnaRuleResult {
        return when {
            context.samePlaceAndEffort -> SamjnaRuleResult.Assigned(
                Samjna.SAVARNA,
                KarakaEvidence("1.1.9", "तुल्यास्यप्रयत्नं सवर्णम्", "Assigns Savarṇa-saṃjñā to homogeneous sound pair ${context.targetPhoneme} and ${context.secondPhoneme}."),
            )
            context.targetPhoneme in vrddhiVowels -> SamjnaRuleResult.Assigned(
                Samjna.VRDDHI,
                KarakaEvidence("1.1.1", "वृद्धिरादैच्", "Assigns Vṛddhi-saṃjñā to ${context.targetPhoneme}."),
            )
            context.targetPhoneme in gunaVowels -> SamjnaRuleResult.Assigned(
                Samjna.GUNA,
                KarakaEvidence("1.1.2", "अदेङ्गुणः", "Assigns Guṇa-saṃjñā to ${context.targetPhoneme}."),
            )
            context.isConjunctConsonants -> SamjnaRuleResult.Assigned(
                Samjna.SAMYOGA,
                KarakaEvidence("1.1.7", "हलोऽनन्तराः संयोगः", "Assigns Saṁyoga-saṃjñā to conjunct consonants ${context.targetPhoneme}."),
            )
            context.isNasalized || context.targetPhoneme in nasalPhonemes -> SamjnaRuleResult.Assigned(
                Samjna.ANUNASIKA,
                KarakaEvidence("1.1.8", "मुखनासिकावचनोऽनुनासिकः", "Assigns Anunāsika-saṃjñā to ${context.targetPhoneme}."),
            )
            else -> SamjnaRuleResult.Unmatched
        }
    }

    fun resolveMorphological(context: MorphologicalContext): SamjnaRuleResult {
        return when {
            context.isDualEndingInIdUdEd -> SamjnaRuleResult.Assigned(
                Samjna.PRAGRHYA,
                KarakaEvidence("1.1.11", "ईदूदेद्द्विवचनं प्रगृह्यम्", "Assigns Pragṛhya-saṃjñā to dual ending in ī/ū/e (${context.surface})."),
            )
            context.isRoot || context.surface in commonRoots -> SamjnaRuleResult.Assigned(
                Samjna.DHATU,
                KarakaEvidence("1.3.1", "भूवादयो धातवः", "Assigns Dhātu-saṃjñā to verbal root ${context.surface}."),
            )
            context.hasSupPratyaya || context.hasTingPratyaya -> SamjnaRuleResult.Assigned(
                Samjna.PADA,
                KarakaEvidence("1.4.14", "सुप्तिङन्तं पदम्", "Assigns Pada-saṃjñā to suptinganta word."),
            )
            context.isMeaningfulBase && !context.isRoot && !context.isAffix -> SamjnaRuleResult.Assigned(
                Samjna.PRATIPADIKA,
                KarakaEvidence("1.2.45", "अर्थवदधातुरप्रत्ययः प्रातिपदिकम्", "Assigns Prātipadika-saṃjñā to nominal base ${context.surface}."),
            )
            else -> SamjnaRuleResult.Unmatched
        }
    }
}
