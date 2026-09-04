package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya3.pada2.KanacCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatrsanacauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LitahKvasuSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.UnadayoBahulamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ChutuSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ShahPratyayasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KartariShapSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EcoYavayavahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AaneMukSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.SarvadhatukardhadhatukayohSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.LitiDhatorAnabhyasasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.PurvoBhyasahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.AtaEkahalmadhyeAnadesaderLitiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.BhavaterAhSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.HaladisSeshahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.HrasvahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.AbhyaseCarCaSutra
import dev.panini.shiksha.Samjna
import dev.panini.core.Lakara
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.ashtadhyayi.adhyaya7.pada2.AcoNnitiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.VatoYukSutra

/** Request object for Participle and Uṇādi stem derivations. */
data class ParticipleDerivationRequest(
    val root: String,
    val samjna: Samjna,
)

/** Main entry point for deriving Participles (Śatṛ, Śānac, Kvasu, Kānac) and Uṇādi stems. */
class ParticipleEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
) {
    private val presentParticipleEngine = DerivationEngine(listOf(
        LatahSatrsanacauSutra,
        KartariShapSutra,
        UpadesheAjanunasikaItSutra,
        HalantyamSutra,
        ChutuSutra,
        ShahPratyayasyaSutra,
        LasakvataddhiteSutra,
        AdyantauTakitauSutra,
        TasyaLopahSutra,
        AaneMukSutra,
        SarvadhatukardhadhatukayohSutra,
        EcoYavayavahSutra,
    ))
    private val perfectParticipleEngine = DerivationEngine(listOf(
        LitahKvasuSutra,
        KanacCaSutra,
        UpadesheAjanunasikaItSutra,
        HalantyamSutra,
        ChutuSutra,
        ShahPratyayasyaSutra,
        LasakvataddhiteSutra,
        TasyaLopahSutra,
        LitiDhatorAnabhyasasyaSutra,
        PurvoBhyasahSutra,
        HrasvahSutra,
        HaladisSeshahSutra,
        BhavaterAhSutra,
        AtaEkahalmadhyeAnadesaderLitiSutra,
        AbhyaseCarCaSutra,
    ))
    private val unadiEngine = DerivationEngine(listOf(
        UnadayoBahulamSutra,
        UpadesheAjanunasikaItSutra,
        HalantyamSutra,
        ChutuSutra,
        ShahPratyayasyaSutra,
        LasakvataddhiteSutra,
        AdyantauTakitauSutra,
        TasyaLopahSutra,
        VatoYukSutra,
        AcoNnitiSutra,
    ))

    fun derive(request: ParticipleDerivationRequest): DerivationResult {
        val rootTerm = DhatuPatha.all.firstOrNull {
            it.upadesha == request.root || it.derivationalSurface == request.root || it.sourceSurface == request.root
        }?.let { DerivationTerm.fromDhatu(it) }
            ?: DerivationTerm("dhatu", request.root, TermKind.DHATU)
        val state = DerivationState(
            terms = listOf(rootTerm),
            samjnas = setOf(
                SamjnaAssignment(rootTerm.id, Samjna.DHATU),
                SamjnaAssignment(rootTerm.id, request.samjna),
            ),
            activeAdhikaras = setOf("3.1.91", "6.4.1"),
            context = DerivationalContext(rupa = Rupa(lakara = if (request.samjna in setOf(Samjna.KVASU, Samjna.KANAC)) Lakara.LIT else null)),
            stage = DerivationStage.INITIAL,
        )

        return when (request.samjna) {
            Samjna.SATR, Samjna.SANAC -> presentParticipleEngine.derive(state)
            Samjna.KVASU, Samjna.KANAC -> perfectParticipleEngine.derive(state)
            Samjna.UNADI, Samjna.ASUN, Samjna.USI -> unadiEngine.derive(state)
            else -> derivationEngine.derive(state)
        }
    }

}
