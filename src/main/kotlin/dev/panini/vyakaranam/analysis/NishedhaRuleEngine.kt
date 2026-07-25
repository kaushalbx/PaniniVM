package dev.panini.vyakaranam.analysis

data class ProhibitionContext(
    val targetSutraNumber: String,
    val affixItMarkers: Set<Char> = emptySet(),
    val isKitOrNgitAffix: Boolean = false,
    val targetPhonemeIsVowel: Boolean = false,
    val secondPhonemeIsConsonant: Boolean = false,
    val isDidhiVeviOrItAugment: Boolean = false,
    val isSetKtvaAffix: Boolean = false,
    val isKrtProhibitedForSasthi: Boolean = false,
)

sealed class NishedhaRuleResult {
    data class Blocked(
        val blockerSutraNumber: String,
        val blockerSutraText: String,
        val blockedTargetSutraNumber: String,
        val evidence: KarakaEvidence,
    ) : NishedhaRuleResult()

    object Allowed : NishedhaRuleResult()
}

object NishedhaRuleEngine {
    fun evaluateProhibition(context: ProhibitionContext): NishedhaRuleResult {
        val isBlockedByKngitiCa = context.isKitOrNgitAffix ||
            'क' in context.affixItMarkers ||
            'ङ' in context.affixItMarkers

        return when {
            context.isKrtProhibitedForSasthi && context.targetSutraNumber == "2.3.65" -> NishedhaRuleResult.Blocked(
                blockerSutraNumber = "2.3.66",
                blockerSutraText = "न लोकाव्ययनिष्ठानिष्ठाखलर्थतृनाम्",
                blockedTargetSutraNumber = "2.3.65",
                evidence = KarakaEvidence("2.3.66", "न लोकाव्ययनिष्ठानिष्ठाखलर्थतृनाम्", "Prohibits Ṣaṣṭhī for la, u, uka, avyaya, niṣṭhā, khalartha, and tṛn affixes."),
            )
            context.isDidhiVeviOrItAugment && context.targetSutraNumber in setOf("1.1.1", "1.1.2", "1.1.3", "7.3.84") -> NishedhaRuleResult.Blocked(
                blockerSutraNumber = "1.1.6",
                blockerSutraText = "दीधीवेवीटाम्",
                blockedTargetSutraNumber = context.targetSutraNumber,
                evidence = KarakaEvidence("1.1.6", "दीधीवेवीटाम्", "Prohibits Guṇa/Vṛddhi sūtra ${context.targetSutraNumber} for dīdhi/vevī/iṭ."),
            )
            context.targetSutraNumber == "1.1.9" && context.targetPhonemeIsVowel && context.secondPhonemeIsConsonant -> NishedhaRuleResult.Blocked(
                blockerSutraNumber = "1.1.10",
                blockerSutraText = "नाज्झलौ",
                blockedTargetSutraNumber = "1.1.9",
                evidence = KarakaEvidence("1.1.10", "नाज्झलौ", "Prohibits Savarṇa-saṃjñā between vowel and consonant."),
            )
            context.isSetKtvaAffix && context.targetSutraNumber == "KIT_STATUS" -> NishedhaRuleResult.Blocked(
                blockerSutraNumber = "1.2.4",
                blockerSutraText = "न क्त्वा सेट्",
                blockedTargetSutraNumber = "1.2.4",
                evidence = KarakaEvidence("1.2.4", "न क्त्वा सेट्", "Prohibits kit-status for ktvā suffix with iṭ augment."),
            )
            isBlockedByKngitiCa && context.targetSutraNumber in setOf("1.1.1", "1.1.2", "1.1.3", "7.3.84") -> NishedhaRuleResult.Blocked(
                blockerSutraNumber = "1.1.5",
                blockerSutraText = "क्ङिति च",
                blockedTargetSutraNumber = context.targetSutraNumber,
                evidence = KarakaEvidence("1.1.5", "क्ङिति च", "Prohibits Guṇa/Vṛddhi sūtra ${context.targetSutraNumber} before K-it/Ṅ-it affix."),
            )
            else -> NishedhaRuleResult.Allowed
        }
    }
}
