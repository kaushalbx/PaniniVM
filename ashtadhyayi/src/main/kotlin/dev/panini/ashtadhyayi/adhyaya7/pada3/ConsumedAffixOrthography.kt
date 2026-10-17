package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.*

/** Avagraha boundaries follow the incorporated affix vowel; a terminal sign
 * follows the complete ending. Explicitly retained consonant occurrences keep
 * their glyph qualifiers; consumed consonants have no surviving glyph to qualify.
 * Nasal marking belongs to the caller's replacement annotation policy,
 * not an independently copied written sign. */
internal fun DerivationTerm.consumedVowelAffixSignProjection(replacementVowelIndex: Int, replacementSize: Int,
    retainedConsonantMap: Map<Int, Int> = emptyMap()):
    Map<OrthographicSignPlacement, OrthographicSignPlacement?> {
    val vowel = varnas.indexOfFirst { it is Svara }
    require(retainedConsonantMap.all { (source, target) -> varnas.getOrNull(source) is Vyanjana && target in 0 until replacementSize })
    return (orthographicSigns + phonologicalText.sourceOrthographicSigns).distinct().associateWith { sign ->
        when (sign.sign) {
            OrthographicSign.AVAGRAHA -> sign.copy(afterVarnaCount = when {
                sign.afterVarnaCount == varnas.size -> replacementSize
                vowel >= 0 && sign.afterVarnaCount > vowel -> replacementVowelIndex + 1
                else -> replacementVowelIndex
            })
            OrthographicSign.CHANDRABINDU -> null
            OrthographicSign.ZERO_WIDTH_JOINER,
            OrthographicSign.ZERO_WIDTH_NON_JOINER -> retainedConsonantMap[sign.afterVarnaCount - 1]
                ?.let { sign.copy(afterVarnaCount = it + 1) }
        }
    }
}
