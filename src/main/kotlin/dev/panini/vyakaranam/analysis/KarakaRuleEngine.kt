package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada4.BhuvahPrabhavahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AdhishingsthasamKarmaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.JanikartuhPrakrtihSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KrudhaDruhersyasuyarthanamYamPratiKopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.RadhiksyorYasyaViprasnahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.BhitharthanamBhayahetuhSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.RucyarthanamPriyamanahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KrudhaDruhorUpasrstayohKarmaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.UpanvadhyangvasahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.ParajerasodhahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AkhyatopayogeSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SprherIpsitahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AbhinivishasCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.VaranarthanamIpsitahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AntardhauYenadarsanamIcchatiSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.DharerUttamarnahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.ShlaghahnusthashapamJnyipsyamanahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.DivahKarmaCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.ParikrayaneSampradanamAnyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.PratyangbhruvahPurvasyaKartaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AnupratigrnasCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.TathayuktamCanipsitamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AdharoAdhikaranamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.DhruvamApayeApadanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarmanaYamAbhipraitiSampradanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.AkathitamCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.JugupsaViramaPramadarthanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.GatiBuddhiAniKartaSaNauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.HrKrorAnyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarturIpsitatamamKarmaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SadhakatamamKaranamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SvatantrahKartaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.TatPrayojakoHetusCaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.*
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraRole

data class KarakaEvidence(
    val sutra: String,
    val text: String,
    val reason: String,
)

data class KarakaResolution(
    val candidates: Set<Karaka>,
    val resolved: Karaka?,
    val possibleVibhaktis: Set<Vibhakti>,
    val evidence: List<KarakaEvidence>,
    val resolvedVibhakti: Vibhakti? = null,
)

data class DhatuIdentity(
    val surface: String,
    val sakarmaka: Boolean = true,
)

data class KarakaRuleContext(
    val dhatu: DhatuIdentity,
    val participant: ParticipantFacts,
    val allParticipants: List<ParticipantFacts>,
    val prayoga: Prayoga,
    val candidates: Set<Karaka> = emptySet(),
    val verbNode: dev.panini.vyakaranam.ast.Pada? = null,
    val baseDhatu: dev.panini.dhatupatha.Dhatu? = null,
)

sealed interface KarakaRuleResult {
    data class Assigned(val karaka: Karaka, val evidence: KarakaEvidence) : KarakaRuleResult
}

data class VibhaktiRuleContext(
    val karaka: Karaka,
    val morphologicalCandidates: Set<Vibhakti>,
    val abhihita: Boolean = false,
    val participant: ParticipantFacts? = null,
) {
    fun accepts(expectedKaraka: Karaka, vibhakti: Vibhakti): Boolean =
        !abhihita && karaka == expectedKaraka && vibhakti in morphologicalCandidates
}

sealed interface VibhaktiRuleResult {
    data class Assigned(val vibhakti: Vibhakti, val evidence: KarakaEvidence) : VibhaktiRuleResult
}

/** Semantic kāraka assignment (1.4) followed by nominal-case validation (2.3). */
object KarakaRuleEngine {
    val karakaRules: List<Sutra<KarakaRuleContext, KarakaRuleResult>> = listOf(
        KarakeSutra,
        DhruvamApayeApadanamSutra,
        JugupsaViramaPramadarthanamSutra,
        BhitharthanamBhayahetuhSutra,
        ParajerasodhahSutra,
        VaranarthanamIpsitahSutra,
        AntardhauYenadarsanamIcchatiSutra,
        AkhyatopayogeSutra,
        JanikartuhPrakrtihSutra,
        BhuvahPrabhavahSutra,
        KarmanaYamAbhipraitiSampradanamSutra,
        RucyarthanamPriyamanahSutra,
        ShlaghahnusthashapamJnyipsyamanahSutra,
        DharerUttamarnahSutra,
        SprherIpsitahSutra,
        KrudhaDruhersyasuyarthanamYamPratiKopahSutra,
        RadhiksyorYasyaViprasnahSutra,
        PratyangbhruvahPurvasyaKartaSutra,
        AnupratigrnasCaSutra,
        KrudhaDruhorUpasrstayohKarmaSutra,
        SadhakatamamKaranamSutra,
        DivahKarmaCaSutra,
        ParikrayaneSampradanamAnyatarasyamSutra,
        AdharoAdhikaranamSutra,
        AdhishingsthasamKarmaSutra,
        AbhinivishasCaSutra,
        UpanvadhyangvasahSutra,
        KarturIpsitatamamKarmaSutra,
        AkathitamCaSutra,
        GatiBuddhiAniKartaSaNauSutra,
        HrKrorAnyatarasyamSutra,
        TathayuktamCanipsitamSutra,
        SvatantrahKartaSutra,
        TatPrayojakoHetusCaSutra,
    ).sortedBy { it.krama }

    val vibhaktiRules: List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>> = listOf(
        AnabhihiteSutra,
        KarmaniDvitiyaSutra,
        AntarantarenaYukteSutra,
        KaladhvanorAtyantasamyogeSutra,
        SaptamiPancamyauKarakamadhyeSutra,
        KarmapravacaniyayukteDvitiyaSutra,
        EnapaDvitiyaSutra,
        PancamyApangParibhihSutra,
        PratinidhiPratidaneCaYasmatSutra,
        GatyarthaKarmaniDvitiyaCaturthyauSutra,
        ChaturthiSampradaneSutra,
        KriyarthopapadasyaCaKarmaniSthaninahSutra,
        TumarthacCaBhavavacanatSutra,
        NamahSvastiSvahaSutra,
        ManyaKarmaniAnadareSutra,
        KartrkaranayostrtiyaSutra,
        SahaYukteApradhaneSutra,
        YenangavikarahSutra,
        ItthambhutalaksaneSutra,
        SamjnyoAnyatarasyamKarmaniSutra,
        HetauSutra,
        VibhasaGuneAstriyamSutra,
        SasthiHetuprayogeSutra,
        SarvanamnasTrtiyaCaSutra,
        ApadanePancamiSutra,
        AnyaraditarartedikshabdaSutra,
        SasthyAtasarthaPratyayenaSutra,
        PrthagVinaNanabhihSutra,
        KaranadAkhyatayamSutra,
        StokanAlpaKrcchraSutra,
        DurantikarthebhyoDvitiyaCaSutra,
        SaptamyAdhikaraneCaSutra,
        YasyaCaBhavenaBhavalaksanamSutra,
        SasthiCanadareSutra,
        SwamyIsvaraAdhipatiSutra,
        AyuktaKusalabhyamCaSevayamSutra,
        YatasCaNirdharanamSutra,
        PancamiVibhakteSutra,
        SadhuNipunabhyamSutra,
        PrasitotsukabhyamTrtiyaCaSutra,
        NaksatreCaLupiSutra,
        PratipadikarthalingaparimanavacanamatrePrathamaSutra,
        SambodhaneCaSutra,
        SamantriteSutra,
        EkavacanamSambuddhihSutra,
        SasthiSeseSutra,
        JnyoAvidarthasyaKaraneSutra,
        AdhigarthaDayesamKarmaniSutra,
        KrjahPratiyatneSutra,
        RujarthanamBhavavacananamAjvarehSutra,
        AsisiNathahSutra,
        JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra,
        VyavahruPanohSamarthayohSutra,
        DivasTadarthasyaSutra,
        VibhasopasargeSutra,
        DvisahKarmaniSutra,
        KartrKarmanohKrtiSutra,
        KasyaCaVartamaneSutra,
        AdhikaranavacinacCaSutra,
        AkasyaCaBhavisyadadamarnyayohSutra,
        KrtyanamKartariVaSutra,
        TulyarthairAtulopamabhyamTrtiyanatarasyamSutra,
        CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra,
    ).sortedBy { it.krama }

    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val possibleVibhaktis = context.participant.possibleVibhaktis
        val candidates = context.candidates.ifEmpty {
            context.participant.possibleVibhaktis.mapNotNull { vibhakti ->
                KarakaInference.infer(vibhakti, context.prayoga, context.dhatu.sakarmaka)
            }.toSet()
        }
        val semanticContext = context.copy(candidates = candidates)
        val semantic = karakaRules.firstOrNull { rule ->
            val prohibition = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
            prohibition !is NishedhaRuleResult.Blocked && rule.matches(semanticContext)
        }?.apply(semanticContext) as? KarakaRuleResult.Assigned
        val resolved = semantic?.karaka ?: candidates.singleOrNull()
        var resolvedVibhakti: Vibhakti? = null
        val evidence = buildList {
            semantic?.let { add(it.evidence) }
            resolved?.let { karaka ->
                val isAbhihita = when (context.prayoga) {
                    Prayoga.KARTARI -> karaka == Karaka.KARTR
                    Prayoga.KARMANI -> karaka == Karaka.KARMAN
                    Prayoga.CAUSATIVE -> karaka == Karaka.KARTR
                    Prayoga.BHAVE -> false
                    Prayoga.ANIRDHARITA -> false
                }
                val vibhaktiContext = VibhaktiRuleContext(karaka, possibleVibhaktis, abhihita = isAbhihita, participant = context.participant)
                val assignment = vibhaktiRules.firstOrNull { rule ->
                    val prohibition = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
                    val adhikaraEligible = isVibhaktiEligible(rule.krama, vibhaktiContext)
                    prohibition !is NishedhaRuleResult.Blocked && adhikaraEligible && rule.matches(vibhaktiContext)
                }?.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned
                assignment?.let {
                    resolvedVibhakti = it.vibhakti
                    add(it.evidence)
                }
            }
        }
        return KarakaResolution(candidates, resolved, possibleVibhaktis, evidence, resolvedVibhakti)
    }
}

//need to fix it or remove it
fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
    val activeDomains = Ashtadhyayi.adhikaraSutras.filter { domain ->
        val role = domain.role as SutraRole.Adhikara
        val start = role.customStartKrama ?: domain.krama
        val end = role.endKrama
        sutraKrama in start..end
    }
    return activeDomains.all { (it.role as SutraRole.Adhikara).isContextEligible(context) }
}
