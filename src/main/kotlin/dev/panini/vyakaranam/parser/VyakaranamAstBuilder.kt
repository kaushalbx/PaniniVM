/*
package dev.panini.vyakaranam.parser

import dev.panini.parser.PaniniyaVyakaranamParser
import dev.panini.vyakaranam.ast.*

class VyakaranamAstBuilder {

    fun build(
        context: PaniniyaVyakaranamParser.UktiContext,
    ): Ukti {
        val vakyas = context.vakya().map(::buildVakya)

        return Ukti(
            sourceText = context.text,
            sambodhana = context.sambodhana()?.let(::buildSambodhana),
            vakyas = vakyas,
            sambandhas = context.vakyaSambandha().map { it.text },
        )
    }

    private fun buildVakya(
        context: PaniniyaVyakaranamParser.VakyaContext,
    ): Vakya =
        when {
            context.akhyataVakya() != null ->
                buildAkhyataVakya(context.akhyataVakya())

            context.namaVakya() != null ->
                buildNamaVakya(context.namaVakya())

            else -> error("अज्ञातः वाक्यप्रकारः: ${context.text}")
        }

    private fun buildAkhyataVakya(
        context: PaniniyaVyakaranamParser.AkhyataVakyaContext,
    ): AkhyataVakya {
        val purvaPadas = context.purvaVakyaPada()
            .map { buildVakyaPada(it.vakyaPada()) }

        val tinganta = buildTinganta(context.tingantaPada())

        val uttaraPadas = context.uttaraVakyaPada()
            .map { buildVakyaPada(it.vakyaPada()) }

        return AkhyataVakya(
            sourceText = context.text,
            padas = purvaPadas + tinganta + uttaraPadas,
            tinganta = tinganta,
        )
    }

    private fun buildNamaVakya(
        context: PaniniyaVyakaranamParser.NamaVakyaContext,
    ): NamaVakya {
        val padas = context.subantaVakyaPada()
            .flatMap(::buildSubantaVakyaPada)

        return NamaVakya(
            sourceText = context.text,
            padas = padas,
        )
    }

    private fun buildVakyaPada(
        context: PaniniyaVyakaranamParser.VakyaPadaContext,
    ): Pada =
        when {
            context.subantaVakyaPada() != null ->
                buildSubantaVakyaPada(context.subantaVakyaPada()).single()

            context.avyayaPada() != null ->
                buildAvyaya(context.avyayaPada())

            else -> error("अज्ञातं वाक्यपदम्: ${context.text}")
        }

    private fun buildSubantaVakyaPada(
        context: PaniniyaVyakaranamParser.SubantaVakyaPadaContext,
    ): List<Pada> =
        when {
            context.subantaPada() != null ->
                listOf(buildSubanta(context.subantaPada()))

            context.samuccitaSubanta() != null ->
                listOf(buildSamuccitaSubanta(context.samuccitaSubanta()))

            else -> error("अज्ञातं सुबन्तवाक्यपदम्: ${context.text}")
        }

    private fun buildSamuccitaSubanta(
        context: PaniniyaVyakaranamParser.SamuccitaSubantaContext,
    ): SamuccitaSubanta =
        SamuccitaSubanta(
            sourceText = context.text,
            members = context.subantaPada().map(::buildSubanta),
        )

    private fun buildSambodhana(
        context: PaniniyaVyakaranamParser.SambodhanaContext,
    ): Sambodhana =
        Sambodhana(
            sourceText = context.text,
            suchaka = context.sambodhanaSuchaka()?.text,
            subanta = buildSubanta(context.subantaPada()),
        )

    private fun buildSubanta(
        context: PaniniyaVyakaranamParser.SubantaPadaContext,
    ): SubantaPada =
        SubantaPada(
            sourceText = context.text,
            pratipadika = buildPratipadika(context.pratipadika()),
            sup = SupPratyaya(
                sourceText = context.supPratyaya().text,
                text = context.supPratyaya().text,
            ),
        )

    private fun buildPratipadika(
        context: PaniniyaVyakaranamParser.PratipadikaContext,
    ): Pratipadika {
        val mula = buildPratipadikaMula(context.pratipadikaMula())
        val vikaras = context.pratipadikaVikara().map(::buildPratipadikaVikara)

        return attachVikaras(mula, vikaras)
    }

    private fun buildPratipadikaMula(
        context: PaniniyaVyakaranamParser.PratipadikaMulaContext,
    ): Pratipadika =
        when {
            context.mulaPratipadika() != null ->
                MulaPratipadika(
                    sourceText = context.text,
                    text = context.mulaPratipadika().text,
                )

            context.kridantaPratipadika() != null ->
                buildKridanta(context.kridantaPratipadika())

            context.unadyantaPratipadika() != null ->
                buildUnadyanta(context.unadyantaPratipadika())

            context.samasaPratipadika() != null ->
                buildSamasa(context.samasaPratipadika())

            context.pratipadika() != null ->
                buildPratipadika(context.pratipadika())

            else -> error("अज्ञातं प्रातिपदिकमूलम्: ${context.text}")
        }

    private fun buildKridanta(
        context: PaniniyaVyakaranamParser.KridantaPratipadikaContext,
    ): KridantaPratipadika =
        KridantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()),
            krtPratyaya = context.krtPratyaya().text,
        )

    private fun buildUnadyanta(
        context: PaniniyaVyakaranamParser.UnadyantaPratipadikaContext,
    ): UnadyantaPratipadika =
        UnadyantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()),
            unadiPratyaya = context.unadiPratyaya().IDENTIFIER().text,
        )

    private fun buildSamasa(
        context: PaniniyaVyakaranamParser.SamasaPratipadikaContext,
    ): SamasaPratipadika =
        SamasaPratipadika(
            sourceText = context.text,
            angas = context.samasaAnga().map(::buildSamasaAnga),
        )

    private fun buildSamasaAnga(
        context: PaniniyaVyakaranamParser.SamasaAngaContext,
    ): SamasaAnga {
        val supAvastha = context.samasaSupAvastha()

        return SamasaAnga(
            sourceText = context.text,
            pratipadika = buildAsamasikaPratipadika(
                context.asamasikaPratipadika(),
            ),
            sup = supAvastha?.supPratyaya()?.let {
                SupPratyaya(
                    sourceText = it.text,
                    text = it.text,
                )
            },
            supLopa = supAvastha?.supAvastha()?.let {
                SupLopa.valueOf(it.text.toSupLopaName())
            },
        )
    }

    private fun buildAsamasikaPratipadika(
        context: PaniniyaVyakaranamParser.AsamasikaPratipadikaContext,
    ): Pratipadika {
        val mulaContext = context.asamasikaPratipadikaMula()

        val mula = when {
            mulaContext.mulaPratipadika() != null ->
                MulaPratipadika(
                    sourceText = mulaContext.text,
                    text = mulaContext.mulaPratipadika().text,
                )

            mulaContext.kridantaPratipadika() != null ->
                buildKridanta(mulaContext.kridantaPratipadika())

            mulaContext.unadyantaPratipadika() != null ->
                buildUnadyanta(mulaContext.unadyantaPratipadika())

            mulaContext.samasaPratipadika() != null ->
                buildSamasa(mulaContext.samasaPratipadika())

            else -> error("अज्ञातम् असमासिकप्रातिपदिकम्: ${context.text}")
        }

        return attachVikaras(
            mula,
            context.pratipadikaVikara().map(::buildPratipadikaVikara),
        )
    }

    private fun buildPratipadikaVikara(
        context: PaniniyaVyakaranamParser.PratipadikaVikaraContext,
    ): PratipadikaVikara =
        when {
            context.taddhitaPratyaya() != null ->
                TaddhitaVikara(
                    sourceText = context.text,
                    pratyaya = context.taddhitaPratyaya().text,
                )

            context.striPratyaya() != null ->
                StriVikara(
                    sourceText = context.text,
                    pratyaya = context.striPratyaya().text,
                )

            else -> error("अज्ञातः प्रातिपदिकविकारः: ${context.text}")
        }

    private fun buildTinganta(
        context: PaniniyaVyakaranamParser.TingantaPadaContext,
    ): TingantaPada =
        TingantaPada(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()),
            lakara = context.lakara().text.toLakara(),
            ting = TingPratyaya(
                sourceText = context.tingPratyaya().text,
                text = context.tingPratyaya().text,
            ),
        )

    private fun buildDhatu(
        context: PaniniyaVyakaranamParser.DhatuPrakritiContext,
    ): DhatuPrakriti =
        DhatuPrakriti(
            sourceText = context.text,
            mulaDhatu = context.dhatuMula().text,
            sanadiPratyayas = context.sanadiPratyaya().map { it.text },
        )

    private fun buildAvyaya(
        context: PaniniyaVyakaranamParser.AvyayaPadaContext,
    ): AvyayaPada =
        when {
            context.mulaAvyaya() != null ->
                AvyayaPada(
                    sourceText = context.text,
                    form = context.mulaAvyaya().text,
                )

            context.avyayaKridanta() != null -> {
                val kridanta = context.avyayaKridanta()

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaKridantaDerivation(
                        upasargas = buildUpasargas(kridanta.upasargaKrama()),
                        dhatu = buildDhatu(kridanta.dhatuPrakriti()),
                        pratyaya = kridanta.avyayaKrtPratyaya().text,
                    ),
                )
            }

            context.avyayaTaddhitanta() != null -> {
                val taddhitanta = context.avyayaTaddhitanta()

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaTaddhitaDerivation(
                        pratipadika = taddhitanta.mulaPratipadika().text,
                        pratyaya = taddhitanta.avyayaTaddhitaPratyaya().text,
                    ),
                )
            }

            context.avyayibhavaPada() != null ->
                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayibhavaDerivation(
                        samasa = buildSamasa(
                            context
                                .avyayibhavaPada()
                                .samasaPratipadika(),
                        ),
                    ),
                )

            else -> error("अज्ञातम् अव्ययपदम्: ${context.text}")
        }

    private fun buildUpasargas(
        context: PaniniyaVyakaranamParser.UpasargaKramaContext?,
    ): List<String> =
        context?.upasarga()?.map { it.text }.orEmpty()

    private fun attachVikaras(
        pratipadika: Pratipadika,
        vikaras: List<PratipadikaVikara>,
    ): Pratipadika =
        when (pratipadika) {
            is MulaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is KridantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is UnadyantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is SamasaPratipadika -> pratipadika.copy(vikaras = vikaras)
        }

    */
/*
     * This helper needs the token position. Since the AST nodes currently
     * store only source text, ordering is retained naturally for most input.
     * Replace this with a sourceSpan field when exact source ordering is needed.
     *//*

    private fun startTokenIndex(pada: Pada): Int = 0

    private fun String.toLakara(): Lakara =
        when (this) {
            "लट्" -> Lakara.LAT
            "लिट्" -> Lakara.LIT
            "लुट्" -> Lakara.LUT
            "लृट्" -> Lakara.LRT
            "लेट्" -> Lakara.LET
            "लोट्" -> Lakara.LOT
            "लङ्" -> Lakara.LANG
            "लिङ्" -> Lakara.LING
            "लुङ्" -> Lakara.LUNG
            "लृङ्" -> Lakara.LRNG
            else -> error("अज्ञातः लकारः: $this")
        }

    private fun String.toSupLopaName(): String =
        when (this) {
            "लुक्" -> "LUK"
            "श्लु" -> "SHLU"
            "लुप्" -> "LUP"
            "अलुक्" -> "ALUK"
            else -> error("अज्ञाता सुपः अवस्था: $this")
        }
}
*/
