package dev.panini.linganushasanam

import dev.panini.linganushasanam.sutras.AnyuPratyayantaDhatuSutra
import dev.panini.linganushasanam.sutras.AsaniBharaniAraniSutra
import dev.panini.linganushasanam.sutras.DundubhiAksesuSutra
import dev.panini.linganushasanam.sutras.DundubhiNabhiAnyatraPumsiSutra
import dev.panini.linganushasanam.sutras.IkarantaSutra
import dev.panini.linganushasanam.sutras.KtinnantaCanonicalSutra
import dev.panini.linganushasanam.sutras.LingamAdhikaraSutra
import dev.panini.linganushasanam.sutras.MinyantaSutra
import dev.panini.linganushasanam.sutras.NabhiAksatriyeSutra
import dev.panini.linganushasanam.sutras.RkarantaMatrDuhitrSutra
import dev.panini.linganushasanam.sutras.SroniYoniUrmiSutra
import dev.panini.linganushasanam.sutras.StriAdhikaraSutra
import dev.panini.linganushasanam.sutras.VahniVrsniAgniSutra
import dev.panini.linganushasanam.sutras.TalantaCanonicalSutra
import dev.panini.linganushasanam.sutras.UngNgyabantaSutra
import dev.panini.linganushasanam.sutras.VimsatyadiAnavatiSutra
import dev.panini.linganushasanam.sutras.YvantaEkaksaraSutra
import dev.panini.linganushasanam.sutras.BhumiVidyutSaritLataVanitaSutra
import dev.panini.linganushasanam.sutras.YadasNapumsakaSutra
import dev.panini.linganushasanam.sutras.BhasSrucSrajDisDrsUsnihUpanahSutra
import dev.panini.linganushasanam.sutras.SthunaUrnaNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.GrhaSasaKlibaSutra
import dev.panini.linganushasanam.sutras.PravrsViprusRutVitTvisSutra
import dev.panini.linganushasanam.sutras.DarviVidiVediKhaniSutra
import dev.panini.linganushasanam.sutras.TithiNadiRuciViciSutra
import dev.panini.linganushasanam.sutras.SaskuliRajiKutiAvantiSutra
import dev.panini.linganushasanam.sutras.PratipadApadVipadSampadSutra
import dev.panini.linganushasanam.sutras.AsisDhurPurGirDvarSutra
import dev.panini.linganushasanam.sutras.ApSumanasVarsaBahutvaSutra
import dev.panini.linganushasanam.sutras.SrajTvacJyokVacYavaguNauSphicSutra
import dev.panini.linganushasanam.sutras.TatitSimaSambadhaSutra
import dev.panini.linganushasanam.sutras.CulliVeniKhariSutra
import dev.panini.linganushasanam.sutras.TaraDharaJyotsnaSutra
import dev.panini.linganushasanam.sutras.SalakaStriyamNityamSutra
import dev.panini.linganushasanam.sutras.GhajantashCaSutra
import dev.panini.linganushasanam.sutras.DevasuratmaSvargaGiriSamudraSutra
import dev.panini.linganushasanam.sutras.GhanAbantahSutra
import dev.panini.linganushasanam.sutras.PumsiSutra
import dev.panini.linganushasanam.sutras.ShopadhahSutra
import dev.panini.linganushasanam.sutras.TopadhahSutra
import dev.panini.linganushasanam.sutras.UkarantahSutra
import dev.panini.linganushasanam.sutras.RsiRasiDrtiGranthiKrimiDhvaniSutra
import dev.panini.linganushasanam.sutras.DhvajaGajaMunjaPunjaSutra
import dev.panini.linganushasanam.sutras.HastaKuntaAntaVataVrataDutaSutra
import dev.panini.linganushasanam.sutras.HradaKandaKundaBudbudaSabdaSutra
import dev.panini.linganushasanam.sutras.PallavaPalvalaKapharephaSutra
import dev.panini.linganushasanam.sutras.SandaMandaKarandaSutra
import dev.panini.linganushasanam.sutras.VamsaAmsaPurodasaSutra
import dev.panini.linganushasanam.sutras.ArghaPathiMathiRbhuksiSutra
import dev.panini.linganushasanam.sutras.SarathiAtithiKuksiSutra
import dev.panini.linganushasanam.sutras.BhayaLingaBhagaPadaNapumsakaSutra
import dev.panini.linganushasanam.sutras.NangantaSutra
import dev.panini.linganushasanam.sutras.YacnaStriyamSutra
import dev.panini.linganushasanam.sutras.KyantaGhuSutra
import dev.panini.linganushasanam.sutras.IsudhiStriCaSutra
import dev.panini.linganushasanam.sutras.TrivistapaTribhuvanaNapumsakaSutra
import dev.panini.linganushasanam.sutras.DyauStriyamSutra
import dev.panini.linganushasanam.sutras.IsuBahuStriCaSutra
import dev.panini.linganushasanam.sutras.BanaKandaNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.NantaSutra
import dev.panini.linganushasanam.sutras.KratuPurusaKapolaGulphaMeghaSutra
import dev.panini.linganushasanam.sutras.AbhraNapumsakaSutra
import dev.panini.linganushasanam.sutras.DhenuRajjuKuhuSarayuTanuRenuPriyanguSutra
import dev.panini.linganushasanam.sutras.SamaseRajjuPumsiCaSutra
import dev.panini.linganushasanam.sutras.SmasruJanuSvaduAsruJatuTraputaluSutra
import dev.panini.linganushasanam.sutras.VasuArthavaciSutra
import dev.panini.linganushasanam.sutras.MadguMadhuSidhuSanuKamandaluSutra
import dev.panini.linganushasanam.sutras.RutvantaSutra
import dev.panini.linganushasanam.sutras.DaruKaseruJatruVastuMastuSutra
import dev.panini.linganushasanam.sutras.SaktuNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.PragRasmerAkarantaAdhikaraSutra
import dev.panini.linganushasanam.sutras.KopadhaSutra
import dev.panini.linganushasanam.sutras.CibukaSalukaPratipadikaAmsukaUlmukaSutra
import dev.panini.linganushasanam.sutras.KantakaAnikaSarakamodakaSutra
import dev.panini.linganushasanam.sutras.KiritaMukutaLalataVataVitaSutra
import dev.panini.linganushasanam.sutras.KutaKutaKapataKapataSutra
import dev.panini.linganushasanam.sutras.NopadhaSutra
import dev.panini.linganushasanam.sutras.RnaLavanaParnaToranaRanaUsnaSutra
import dev.panini.linganushasanam.sutras.KarsapanaSvarnaSuvarnaSutra
import dev.panini.linganushasanam.sutras.ThopadhaSutra
import dev.panini.linganushasanam.sutras.KasthaPrsthaRikthaUkthaSutra
import dev.panini.linganushasanam.sutras.KasthaDigarthaStriyamSutra
import dev.panini.linganushasanam.sutras.TirthaProthaYuthaGuthaSutra
import dev.panini.linganushasanam.sutras.NopadhaDentalSutra
import dev.panini.linganushasanam.sutras.JaghanaAjinaTuhinaKananaSutra
import dev.panini.linganushasanam.sutras.ManaYanaAbhidhanaNalinaSutra
import dev.panini.linganushasanam.sutras.PopadhaSutra
import dev.panini.linganushasanam.sutras.PapaRupaUdupaTalpaSilpaSutra
import dev.panini.linganushasanam.sutras.SurpaKutapaKunapaDvipaVitapaSutra
import dev.panini.linganushasanam.sutras.BhopadhaSutra
import dev.panini.linganushasanam.sutras.TalabhaNapumsakaSutra
import dev.panini.linganushasanam.sutras.JrmbhaNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.MopadhaSutra
import dev.panini.linganushasanam.sutras.RukmaSidhmaYugmaIdhmaSutra
import dev.panini.linganushasanam.sutras.SangramaDadimaKusumaAsramaSutra
import dev.panini.linganushasanam.sutras.YopadhaSutra
import dev.panini.linganushasanam.sutras.KisalayaHrdayaIndriyaUttariyaSutra
import dev.panini.linganushasanam.sutras.GomayaKasayaMalayaAnvayaAvyayaSutra
import dev.panini.linganushasanam.sutras.RopadhaSutra
import dev.panini.linganushasanam.sutras.DvaraAgraSpharaTakraSutra
import dev.panini.linganushasanam.sutras.SukraAdevatayaSutra
import dev.panini.linganushasanam.sutras.CakraVajraAndhakaraSaraSutra
import dev.panini.linganushasanam.sutras.SirisaRjisaAmbarisaPiyusaSutra
import dev.panini.linganushasanam.sutras.YusaKarisaMisaVisaVarsaSutra
import dev.panini.linganushasanam.sutras.SopadhaSutra
import dev.panini.linganushasanam.sutras.ManasaBisaBusaSahasaSutra
import dev.panini.linganushasanam.sutras.CamasaAmsaRasaNiryasaSutra
import dev.panini.linganushasanam.sutras.KamsaApraniniSutra
import dev.panini.linganushasanam.sutras.RasmiDivasaAbhidhanaSutra
import dev.panini.linganushasanam.sutras.DidhitiStriyamSutra
import dev.panini.linganushasanam.sutras.DinaAhaniNapumsakaSutra
import dev.panini.linganushasanam.sutras.ManaAbhidhanaSutra
import dev.panini.linganushasanam.sutras.DronaAdhakaNapumsakeCaSutra
import dev.panini.linganushasanam.sutras.KhariManikaStriyamSutra
import dev.panini.linganushasanam.sutras.DaraAksataLajaAsuBahutvaSutra
import dev.panini.linganushasanam.sutras.NadiApajanaVranaAngaPadaSutra
import dev.panini.linganushasanam.sutras.MarutGarutUttaraRtvijSutra
import dev.panini.linganushasanam.sutras.BhrtrAmitraChatraPutraSutra
import dev.panini.linganushasanam.sutras.SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra
import dev.panini.linganushasanam.sutras.DandaMandaKhandaSavaSaindhavaParsvaSutra
import dev.panini.linganushasanam.sutras.NapumsakamAdhikaraSutra
import dev.panini.linganushasanam.sutras.BhaveLyudantaSutra
import dev.panini.linganushasanam.sutras.NisthaBhaveSutra
import dev.panini.linganushasanam.sutras.TvaSyanTaddhitaSutra
import dev.panini.linganushasanam.sutras.KarmaniBrahmanadiGunavacanaSutra
import dev.panini.linganushasanam.sutras.YadYadhakBhavaKarmaniSutra
import dev.panini.linganushasanam.sutras.DvandvaEkatvaSutra
import dev.panini.linganushasanam.sutras.AbhasayamHemantaSisiraAhoratraSutra
import dev.panini.linganushasanam.sutras.AnanKarmadharayaTatpurusaAdhikaraSutra
import dev.panini.linganushasanam.sutras.AnalpeChayaSutra
import dev.panini.linganushasanam.sutras.RajaAmanusyaPurvaSabhaSutra
import dev.panini.linganushasanam.sutras.SuraSenaChayaSalaNisaSutra
import dev.panini.linganushasanam.sutras.ParavatSutra
import dev.panini.linganushasanam.sutras.SankhyaPurvaRatriSutra
import dev.panini.linganushasanam.sutras.ApathaPunyahaNapumsakaSutra
import dev.panini.linganushasanam.sutras.DviguStriCaSutra
import dev.panini.linganushasanam.sutras.IsUsantaSutra
import dev.panini.linganushasanam.sutras.ArcisStriCaSutra
import dev.panini.linganushasanam.sutras.ChadisStriyamEvaSutra
import dev.panini.linganushasanam.sutras.MukhaNayanaLohaVanaSutra
import dev.panini.linganushasanam.sutras.SiraArthaOdanaPumsiSutra
import dev.panini.linganushasanam.sutras.VaktraNetraAranyaGandivaSutra
import dev.panini.linganushasanam.sutras.AtaviStriyamSutra
import dev.panini.linganushasanam.sutras.LopadhaSutra
import dev.panini.linganushasanam.sutras.TulaUpalaTalaKusulaSutra
import dev.panini.linganushasanam.sutras.SatadiSankhyaSutra
import dev.panini.linganushasanam.sutras.SataAyutaPrayutaPumsiCaSutra
import dev.panini.linganushasanam.sutras.LaksaKotiStriyamSutra
import dev.panini.linganushasanam.sutras.SankuPumsiSutra
import dev.panini.linganushasanam.sutras.SahasraPumsiCaSutra
import dev.panini.linganushasanam.sutras.ManDvyacAkartariSutra
import dev.panini.linganushasanam.sutras.BrahmanPumsiCaSutra
import dev.panini.linganushasanam.sutras.NamanRomanNapumsakaSutra
import dev.panini.linganushasanam.sutras.AsantaDvyacSutra
import dev.panini.linganushasanam.sutras.ApsarasStriyamSutra
import dev.panini.linganushasanam.sutras.TrantaSutra
import dev.panini.linganushasanam.sutras.YatraMatraBhastraDamstraVastraSutra
import dev.panini.linganushasanam.sutras.PatraPatraPavitraSutraChatraSutra
import dev.panini.linganushasanam.sutras.BalaKusumaSulbaPattanaRanaSutra
import dev.panini.linganushasanam.sutras.PadmaKamalaUtpalaPumsiCaSutra
import dev.panini.linganushasanam.sutras.AhavaSangramaPumsiSutra
import dev.panini.linganushasanam.sutras.AjiStriyamEvaSutra
import dev.panini.linganushasanam.sutras.PhalaJatiSutra
import dev.panini.linganushasanam.sutras.VrksaJatiStriyamEvaSutra
import dev.panini.linganushasanam.sutras.ViyatJagatSakrtSakanSutra
import dev.panini.linganushasanam.sutras.NavanitaAvanataAnrtaAmrtaSutra
import dev.panini.linganushasanam.sutras.SraddhaKulisaDaivaPithaSutra
import dev.panini.linganushasanam.sutras.DaivaPumsiCaSutra
import dev.panini.linganushasanam.sutras.DhanyaAjyaSasyaRupyaSutra
import dev.panini.linganushasanam.sutras.DvandvaBarhaDuhkhaBadisaSutra
import dev.panini.linganushasanam.sutras.AksaIndriyeSutra
import dev.panini.linganushasanam.sutras.StriPumsayohAdhikaraSutra
import dev.panini.linganushasanam.sutras.GoManiYastiMustiSutra
import dev.panini.linganushasanam.sutras.MrtyuSidhuKarkandhuSutra
import dev.panini.linganushasanam.sutras.GunavacanaUkarantaNapumsakamCaSutra
import dev.panini.linganushasanam.sutras.ApatyarthaTaddhitaSutra
import dev.panini.linganushasanam.sutras.PumNapumsakayohAdhikaraSutra
import dev.panini.linganushasanam.sutras.DhrtaBhutaMustaKsvelitaSutra
import dev.panini.linganushasanam.sutras.SrngaArdhaNidaghaUdyamaSutra
import dev.panini.linganushasanam.sutras.VrajaKunjaKuthaKurcaSutra
import dev.panini.linganushasanam.sutras.KabandhaAusadhaAyudhantaSutra
import dev.panini.linganushasanam.sutras.GrhaMehaDehaPattaSutra
import dev.panini.linganushasanam.sutras.AvisistaLingamAdhikaraSutra
import dev.panini.linganushasanam.sutras.AvyayaKatiYusmadAsmadSutra
import dev.panini.linganushasanam.sutras.SnaantaSankhyaSutra
import dev.panini.linganushasanam.sutras.GunavacanamCaSutra
import dev.panini.linganushasanam.sutras.KrtyasCaSutra
import dev.panini.linganushasanam.sutras.KaranaAdhikaranaLyutSutra
import dev.panini.linganushasanam.sutras.SarvadiniSarvanamaniSutra

/**
 * Catalog of authentic Pāṇinian Liṅgānuśāsana Sūtras.
 */
object LinganushasanamRegistry {
    private val canonicalImplementations: List<LinganushasanaSutra> = listOf(
        LingamAdhikaraSutra,
        StriAdhikaraSutra,
        RkarantaMatrDuhitrSutra,
        AnyuPratyayantaDhatuSutra,
        AsaniBharaniAraniSutra,
        MinyantaSutra,
        VahniVrsniAgniSutra,
        SroniYoniUrmiSutra,
        KtinnantaCanonicalSutra,
        IkarantaSutra,
        UngNgyabantaSutra,
        YvantaEkaksaraSutra,
        VimsatyadiAnavatiSutra,
        DundubhiAksesuSutra,
        NabhiAksatriyeSutra,
        DundubhiNabhiAnyatraPumsiSutra,
        TalantaCanonicalSutra,
        BhumiVidyutSaritLataVanitaSutra,
        YadasNapumsakaSutra,
        BhasSrucSrajDisDrsUsnihUpanahSutra,
        SthunaUrnaNapumsakeCaSutra,
        GrhaSasaKlibaSutra,
        PravrsViprusRutVitTvisSutra,
        DarviVidiVediKhaniSutra,
        TithiNadiRuciViciSutra,
        SaskuliRajiKutiAvantiSutra,
        PratipadApadVipadSampadSutra,
        AsisDhurPurGirDvarSutra,
        ApSumanasVarsaBahutvaSutra,
        SrajTvacJyokVacYavaguNauSphicSutra,
        TatitSimaSambadhaSutra,
        CulliVeniKhariSutra,
        TaraDharaJyotsnaSutra,
        SalakaStriyamNityamSutra,
        BhayaLingaBhagaPadaNapumsakaSutra,
        NangantaSutra,
        YacnaStriyamSutra,
        KyantaGhuSutra,
        IsudhiStriCaSutra,
        TrivistapaTribhuvanaNapumsakaSutra,
        DyauStriyamSutra,
        IsuBahuStriCaSutra,
        BanaKandaNapumsakeCaSutra,
        NantaSutra,
        KratuPurusaKapolaGulphaMeghaSutra,
        AbhraNapumsakaSutra,
        DhenuRajjuKuhuSarayuTanuRenuPriyanguSutra,
        SamaseRajjuPumsiCaSutra,
        SmasruJanuSvaduAsruJatuTraputaluSutra,
        VasuArthavaciSutra,
        MadguMadhuSidhuSanuKamandaluSutra,
        RutvantaSutra,
        DaruKaseruJatruVastuMastuSutra,
        SaktuNapumsakeCaSutra,
        PragRasmerAkarantaAdhikaraSutra,
        KopadhaSutra,
        CibukaSalukaPratipadikaAmsukaUlmukaSutra,
        KantakaAnikaSarakamodakaSutra,
        KiritaMukutaLalataVataVitaSutra,
        KutaKutaKapataKapataSutra,
        NopadhaSutra,
        RnaLavanaParnaToranaRanaUsnaSutra,
        KarsapanaSvarnaSuvarnaSutra,
        ThopadhaSutra,
        KasthaPrsthaRikthaUkthaSutra,
        KasthaDigarthaStriyamSutra,
        TirthaProthaYuthaGuthaSutra,
        NopadhaDentalSutra,
        JaghanaAjinaTuhinaKananaSutra,
        ManaYanaAbhidhanaNalinaSutra,
        PopadhaSutra,
        PapaRupaUdupaTalpaSilpaSutra,
        SurpaKutapaKunapaDvipaVitapaSutra,
        BhopadhaSutra,
        TalabhaNapumsakaSutra,
        JrmbhaNapumsakeCaSutra,
        MopadhaSutra,
        RukmaSidhmaYugmaIdhmaSutra,
        SangramaDadimaKusumaAsramaSutra,
        YopadhaSutra,
        KisalayaHrdayaIndriyaUttariyaSutra,
        GomayaKasayaMalayaAnvayaAvyayaSutra,
        RopadhaSutra,
        DvaraAgraSpharaTakraSutra,
        SukraAdevatayaSutra,
        CakraVajraAndhakaraSaraSutra,
        SirisaRjisaAmbarisaPiyusaSutra,
        YusaKarisaMisaVisaVarsaSutra,
        SopadhaSutra,
        ManasaBisaBusaSahasaSutra,
        CamasaAmsaRasaNiryasaSutra,
        KamsaApraniniSutra,
        RasmiDivasaAbhidhanaSutra,
        DidhitiStriyamSutra,
        DinaAhaniNapumsakaSutra,
        ManaAbhidhanaSutra,
        DronaAdhakaNapumsakeCaSutra,
        KhariManikaStriyamSutra,
        DaraAksataLajaAsuBahutvaSutra,
        NadiApajanaVranaAngaPadaSutra,
        MarutGarutUttaraRtvijSutra,
        NapumsakamAdhikaraSutra,
        BhaveLyudantaSutra,
        NisthaBhaveSutra,
        TvaSyanTaddhitaSutra,
        KarmaniBrahmanadiGunavacanaSutra,
        YadYadhakBhavaKarmaniSutra,
        DvandvaEkatvaSutra,
        AbhasayamHemantaSisiraAhoratraSutra,
        AnanKarmadharayaTatpurusaAdhikaraSutra,
        AnalpeChayaSutra,
        RajaAmanusyaPurvaSabhaSutra,
        SuraSenaChayaSalaNisaSutra,
        ParavatSutra,
        SankhyaPurvaRatriSutra,
        ApathaPunyahaNapumsakaSutra,
        DviguStriCaSutra,
        IsUsantaSutra,
        ArcisStriCaSutra,
        ChadisStriyamEvaSutra,
        MukhaNayanaLohaVanaSutra,
        SiraArthaOdanaPumsiSutra,
        VaktraNetraAranyaGandivaSutra,
        AtaviStriyamSutra,
        LopadhaSutra,
        TulaUpalaTalaKusulaSutra,
        SatadiSankhyaSutra,
        SataAyutaPrayutaPumsiCaSutra,
        LaksaKotiStriyamSutra,
        SankuPumsiSutra,
        SahasraPumsiCaSutra,
        ManDvyacAkartariSutra,
        BrahmanPumsiCaSutra,
        NamanRomanNapumsakaSutra,
        AsantaDvyacSutra,
        ApsarasStriyamSutra,
        TrantaSutra,
        YatraMatraBhastraDamstraVastraSutra,
        PatraPatraPavitraSutraChatraSutra,
        BalaKusumaSulbaPattanaRanaSutra,
        PadmaKamalaUtpalaPumsiCaSutra,
        AhavaSangramaPumsiSutra,
        AjiStriyamEvaSutra,
        PhalaJatiSutra,
        VrksaJatiStriyamEvaSutra,
        ViyatJagatSakrtSakanSutra,
        NavanitaAvanataAnrtaAmrtaSutra,
        SraddhaKulisaDaivaPithaSutra,
        DaivaPumsiCaSutra,
        DhanyaAjyaSasyaRupyaSutra,
        DvandvaBarhaDuhkhaBadisaSutra,
        AksaIndriyeSutra,
        StriPumsayohAdhikaraSutra,
        GoManiYastiMustiSutra,
        MrtyuSidhuKarkandhuSutra,
        GunavacanaUkarantaNapumsakamCaSutra,
        ApatyarthaTaddhitaSutra,
        PumNapumsakayohAdhikaraSutra,
        DhrtaBhutaMustaKsvelitaSutra,
        SrngaArdhaNidaghaUdyamaSutra,
        VrajaKunjaKuthaKurcaSutra,
        KabandhaAusadhaAyudhantaSutra,
        GrhaMehaDehaPattaSutra,
        AvisistaLingamAdhikaraSutra,
        AvyayaKatiYusmadAsmadSutra,
        SnaantaSankhyaSutra,
        GunavacanamCaSutra,
        KrtyasCaSutra,
        KaranaAdhikaranaLyutSutra,
        SarvadiniSarvanamaniSutra,

        PumsiSutra,
        DevasuratmaSvargaGiriSamudraSutra,
        UkarantahSutra,
        TopadhahSutra,
        ShopadhahSutra,
        RsiRasiDrtiGranthiKrimiDhvaniSutra,
        DhvajaGajaMunjaPunjaSutra,
        HastaKuntaAntaVataVrataDutaSutra,
        SandaMandaKarandaSutra,
        VamsaAmsaPurodasaSutra,
        HradaKandaKundaBudbudaSabdaSutra,
        ArghaPathiMathiRbhuksiSutra,
        PallavaPalvalaKapharephaSutra,
        SarathiAtithiKuksiSutra,
        GhanAbantahSutra,
        GhajantashCaSutra,

        BhrtrAmitraChatraPutraSutra,
        SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra,
        DandaMandaKhandaSavaSaindhavaParsvaSutra,
    )

    private val canonicalImplementationsByNumber = canonicalImplementations.associateBy { it.number }

    internal val dedicatedCanonicalNumbers: Set<String> get() = canonicalImplementationsByNumber.keys

    /** All 189 Ashtadhyayi.com-edition sūtras, in textual order. */
    val canonicalSutras: List<LinganushasanaSutra> = canonicalImplementations
        .sortedBy { requireNotNull(it.number.toIntOrNull()) { "Noncanonical sūtra number ${it.number}." } }
        .also { sutras ->
            require(sutras.map { it.number } == (1..189).map(Int::toString)) {
                "Registry must contain each Ashtadhyayi.com Liṅgānuśāsana Sūtra from 1 through 189 exactly once."
            }
        }

    /** Complete canonical corpus in runtime priority order. */
    val sutras: List<LinganushasanaSutra> =
        canonicalSutras.sortedWith(compareByDescending<LinganushasanaSutra> { it.priority }
            .thenByDescending { it.number.toIntOrNull() ?: 0 })

    fun require(number: String): LinganushasanaSutra =
        sutras.firstOrNull { it.number == number } ?: error("Liṅgānuśāsana Sūtra $number not found in registry.")
}
