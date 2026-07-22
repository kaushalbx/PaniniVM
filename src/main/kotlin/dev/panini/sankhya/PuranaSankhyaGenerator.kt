package dev.panini.sankhya

import dev.panini.ashtadhyayi.adhyaya5.pada2.DvesTiyahSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.NantadAsankhyaderMatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.ShatKatiKatipayaChaturamThukSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TresSamprasaranamCaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.NaloPratipadikantasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.TiVimshaterDitiSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TasyaPuraneDatSutra
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import java.math.BigInteger

/** Derives the currently implemented pūraṇa numerals through A.5.2.49–55. */
class PuranaSankhyaGenerator(
    private val cardinalGenerator: SanskritSankhyaGenerator,
) {
    private val engine = DerivationEngine(
        listOf(
            ShatKatiKatipayaChaturamThukSutra,
            DvesTiyahSutra,
            TresSamprasaranamCaSutra,
            NantadAsankhyaderMatSutra,
            TasyaPuraneDatSutra,
            TiVimshaterDitiSutra,
            NaloPratipadikantasyaSutra,
        )
    )

    fun generate(value: BigInteger): DerivationResult {
        require(value in BigInteger.ONE..BigInteger.valueOf(29)) {
            "Pūraṇa derivation is currently complete only for 1–29: $value"
        }

        val base = if (value == BigInteger.ONE) "प्रथम" else PrimitiveSankhya.fromValue(value)?.pratipadika
            ?: cardinalGenerator.generate(value).final.surface
        val term = DerivationTerm("purana_base", base, TermKind.PRATIPADIKA, upadesha = base)
        val initial = DerivationState(
            terms = listOf(term),
            samjnas = setOf(
                SamjnaAssignment(term.id, Samjna.PRATIPADIKA),
                SamjnaAssignment(term.id, Samjna.SANKHYA),
                SamjnaAssignment(term.id, Samjna.PURANA),
            ),
            stage = DerivationStage.PADA_FORMED,
        )
        // प्रथम is lexical; the rule engine must preserve it without fabricating a sūtra application.
        return engine.derive(initial)
    }

    fun generateSurface(value: BigInteger): String = generate(value).final.surface
}
