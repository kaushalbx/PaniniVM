package dev.panini.shiksha

import java.text.Normalizer

/** Converts Devanāgarī orthography into phonological varṇa occurrences. */
object DevanagariParser {
    private const val CHANDRABINDU = 'ँ'
    private const val AVAGRAHA = 'ऽ'

    fun parse(text: String, tokenIdPrefix: String = "varna"): SanskritText {
        require(tokenIdPrefix.isNotBlank()) { "A token ID prefix is required." }
        val normalized = Normalizer.normalize(text, Normalizer.Form.NFC)
        val tokens = mutableListOf<VarnaToken>()

        fun add(varna: Varna) {
            tokens += VarnaToken(VarnaTokenId("$tokenIdPrefix:${tokens.size}"), varna)
        }

        fun nasalizeLastVowel(index: Int) {
            val tokenIndex = tokens.indexOfLast { it.varna is Svara }
            require(tokenIndex >= 0 && tokens.drop(tokenIndex + 1).none { it.varna is Svara || it.varna is Vyanjana }) {
                "Anunāsika mark at Unicode index $index has no vowel to qualify in '$text'."
            }
            tokens[tokenIndex] = tokens[tokenIndex].copy(nasalized = true)
        }

        var index = 0
        while (index < normalized.length) {
            val character = normalized[index]
            val independent = Svara.fromIndependent(character)
            if (independent != null) {
                add(independent)
                index++
                continue
            }

            val consonant = Vyanjana.fromDevanagari(character)
            if (consonant != null) {
                add(consonant)
                val following = normalized.getOrNull(index + 1)
                when {
                    following == Vyanjana.VIRAMA -> index += 2
                    following != null && Svara.fromMatra(following) != null -> {
                        add(requireNotNull(Svara.fromMatra(following)))
                        index += 2
                    }
                    else -> {
                        add(Svara.A)
                        index++
                    }
                }
                continue
            }

            val ayogavaha = Ayogavaha.entries.firstOrNull { it.devanagari.single() == character }
            if (ayogavaha != null) {
                add(ayogavaha)
                index++
                continue
            }

            if (character == CHANDRABINDU) {
                nasalizeLastVowel(index)
                index++
                continue
            }

            if (character == AVAGRAHA) {
                // Avagraha records vowel elision orthographically; it is not a
                // varṇa and therefore has no token in the phonological sequence.
                index++
                continue
            }

            val standaloneMatra = Svara.fromMatra(character)
            if (standaloneMatra != null) {
                // Transitional derivation terms can be orthographic fragments
                // such as े.  Interpret them phonologically; rendering a complete
                // SanskritText will canonicalize them as independent vowels.
                add(standaloneMatra)
                index++
                continue
            }
            error("Unsupported Devanāgarī character '$character' at Unicode index $index in '$text'.")
        }
        return SanskritText(tokens)
    }
}

/** Renders phonological varṇas into canonical Devanāgarī orthography. */
object DevanagariRenderer {
    private const val CHANDRABINDU = 'ँ'

    fun render(text: SanskritText): String = buildString {
        val tokens = text.effectiveVarnas
        var index = 0
        while (index < tokens.size) {
            val token = tokens[index]
            when (val varna = token.varna) {
                is Vyanjana -> {
                    append(varna.devanagari)
                    val vowel = tokens.getOrNull(index + 1)?.takeIf { it.varna is Svara }
                    if (vowel == null) {
                        append(Vyanjana.VIRAMA)
                    } else {
                        val svara = vowel.varna as Svara
                        if (svara != Svara.A) append(requireNotNull(svara.matra))
                        if (vowel.nasalized) append(CHANDRABINDU)
                        index++
                    }
                }
                is Svara -> {
                    append(varna.devanagari)
                    if (token.nasalized) append(CHANDRABINDU)
                }
                is Ayogavaha -> append(varna.devanagari)
            }
            index++
        }
    }
}

fun String.toSanskritText(tokenIdPrefix: String = "varna"): SanskritText =
    SanskritText.parse(this, tokenIdPrefix)

fun String.toVarnas(): List<Varna> = toSanskritText().effectiveVarnas.map(VarnaToken::varna)

fun String.firstVarna(): Varna? = toSanskritText().first()?.varna

fun String.lastVarna(): Varna? = toSanskritText().last()?.varna

fun String.firstSvara(): Svara? = firstVarna() as? Svara

fun String.lastSvara(): Svara? = lastVarna() as? Svara

fun String.startsWithVarna(varna: Varna): Boolean = firstVarna() == varna

fun String.endsWithVarna(varna: Varna): Boolean = lastVarna() == varna

fun String.replaceLastVarna(expected: Varna, replacement: List<Varna>): String {
    val varnas = toVarnas().toMutableList()
    require(varnas.lastOrNull() == expected) { "Expected final varṇa $expected in '$this'." }
    varnas.removeLast()
    varnas.addAll(replacement)
    return varnas.toDevanagari()
}

fun List<Varna>.toDevanagari(): String = DevanagariRenderer.render(
    SanskritText(mapIndexed { index, varna -> VarnaToken(VarnaTokenId("render:$index"), varna) }),
)
