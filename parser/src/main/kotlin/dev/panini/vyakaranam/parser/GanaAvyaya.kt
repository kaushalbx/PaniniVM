package dev.panini.vyakaranam.parser

import dev.panini.ganapatha.ChadiGana
import dev.panini.ganapatha.SvaradiGana

/** Indeclinables from the Gaṇapāṭha, so a bare word needs no reserved token (1.1.37, 1.4.57). */
internal object GanaAvyaya {
    private val listed = (SvaradiGana.members + ChadiGana.members).map { it.text }.toSet()

    /** 1.1.38 तद्धितश्चासर्वविभक्तिः: these तसिल् forms govern the accusative by the vārttika on 2.3.2. */
    private val taddhita = setOf("परितः", "अभितः", "सर्वतः", "उभयतः")

    /** The gaṇa lists the stem नमस्, उच्चैस्; the written pada ends in visarga. */
    fun isAvyaya(word: String): Boolean = word in listed || word in taddhita ||
        (word.endsWith("ः") && word.dropLast(1) + "स्" in listed)
}
