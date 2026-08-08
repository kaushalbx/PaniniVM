package dev.panini.vyakaranam.parser

import dev.panini.core.Lakara
import dev.panini.core.SupLopa
import dev.panini.vyakaranam.ast.*
import dev.panini.parser.VyakaranamParser as PaniniyaVyakaranamParser

class VyakaranamAstBuilder {

    fun build(
        context: PaniniyaVyakaranamParser.UktiContext,
    ): Ukti {
        val body = context.pipelineClause()?.let(::buildPipeline) ?: context.conditionalClause()?.let { conditional ->
            Conditional(
                sourceText = conditional.text,
                condition = Invocation(buildVakya(conditional.condition!!)),
                consequent = Invocation(buildVakya(conditional.consequent!!)),
                alternate = conditional.alternate?.let { Invocation(buildVakya(it)) },
            )
        } ?: Sequence(
            sourceText = context.text,
            statements = context.vakya().map { Invocation(buildVakya(it)) },
            connectors = context.vakyaSambandha().map { it.text },
        )

        return Ukti(
            sourceText = context.text,
            sambodhana = context.sambodhana()?.let(::buildSambodhana),
            body = body,
        )
    }

    private fun buildPipeline(
        context: PaniniyaVyakaranamParser.PipelineClauseContext,
    ): Pipeline {
        val arguments = context.arguments.map(::buildSubanta)
        val stagePadas = context.stages.map { stage ->
            val domain = buildSubanta(stage.domain!!)
            val operation = buildSubanta(stage.operation!!)
            Triple(
                PipelineStage(
                    sourceText = "${canonicalSegmented(domain.sourceText)} ${canonicalSegmented(operation.pratipadika.sourceText)}",
                    domainStem = canonicalSegmented(domain.pratipadika.sourceText),
                    operationStem = canonicalSegmented(operation.pratipadika.sourceText),
                ),
                domain,
                operation,
            )
        }
        val directive = AvyayaPada(
            sourceText = context.purvaparaDirective()!!.text,
            form = context.purvaparaDirective()!!.text,
        )
        return Pipeline(
            sourceText = context.text,
            arguments = arguments.map { it.pratipadika.sourceText },
            stages = stagePadas.map { it.first },
            renderPadas = arguments +
                AvyayaPada(sourceText = "च", form = "च") +
                stagePadas.flatMap { listOf(it.second, it.third) } +
                directive +
                buildSubanta(context.pipelineResult()!!.subantaPada()!!) +
                buildTinganta(context.tingantaPada()!!),
        )
    }

    private fun canonicalSegmented(source: String): String =
        source.replace("+", " + ").replace(Regex("\\s+"), " ").trim()

    private fun buildVakya(
        context: PaniniyaVyakaranamParser.VakyaContext,
    ): Vakya =
        when {
            context.akhyataVakya() != null ->
                buildAkhyataVakya(context.akhyataVakya()!!)

            context.namaVakya() != null ->
                buildNamaVakya(context.namaVakya()!!)

            else -> error("अज्ञातः वाक्यप्रकारः: ${context.text}")
        }

    private fun buildAkhyataVakya(
        context: PaniniyaVyakaranamParser.AkhyataVakyaContext,
    ): AkhyataVakya {
        val purvaPadas = context.purvaVakyaPada()
            .map { buildVakyaPada(it.vakyaPada()!!) }

        val tinganta = buildTinganta(context.tingantaPada()!!)

        val uttaraPadas = context.uttaraVakyaPada()
            .map { buildVakyaPada(it.vakyaPada()!!) }

        return AkhyataVakya(
            sourceText = context.text,
            padas = purvaPadas + tinganta + uttaraPadas,
            tinganta = tinganta,
        )
    }

    private fun buildNamaVakya(
        context: PaniniyaVyakaranamParser.NamaVakyaContext,
    ): NamaVakya {
        val padas = context.vakyaPada()
            .map(::buildVakyaPada)

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
                buildSubantaVakyaPada(context.subantaVakyaPada()!!).single()

            context.avyayaPada() != null ->
                buildAvyaya(context.avyayaPada()!!)

            else -> error("अज्ञातं वाक्यपदम्: ${context.text}")
        }

    private fun buildSankhyaPada(
        context: PaniniyaVyakaranamParser.SankhyaPadaContext,
    ): SankhyaPada {
        val stems = context.sankhyaStem().map { it.text }
        return SankhyaPada(
            sourceText = context.text,
            stems = stems,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSankhyaPuranaPada(
        context: PaniniyaVyakaranamParser.SankhyaPuranaPadaContext,
    ): SankhyaPuranaPada {
        val stems = context.sankhyaStem().map { it.text } + context.puranaPratyaya()!!.text
        return SankhyaPuranaPada(
            sourceText = context.text,
            stems = stems,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSankhyaAbhyasaPada(
        context: PaniniyaVyakaranamParser.SankhyaAbhyasaPadaContext,
    ): SankhyaAbhyasaPada {
        val stems = context.sankhyaStem().map { it.text } + (context.KRITVAS()?.text ?: context.SUC()?.text ?: context.DHAA()!!.text)
        return SankhyaAbhyasaPada(
            sourceText = context.text,
            stems = stems,
        )
    }

    private fun buildKatapayadiPada(
        context: PaniniyaVyakaranamParser.KatapayadiPadaContext,
    ): KatapayadiPada {
        val word = context.IDENTIFIER().text
        return KatapayadiPada(
            sourceText = context.text,
            word = word,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildAryabhatiyaPada(
        context: PaniniyaVyakaranamParser.AryabhatiyaPadaContext,
    ): AryabhatiyaPada {
        val word = context.IDENTIFIER().text
        return AryabhatiyaPada(
            sourceText = context.text,
            word = word,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildBhutasamkhyaPada(
        context: PaniniyaVyakaranamParser.BhutasamkhyaPadaContext,
    ): BhutasamkhyaPada {
        val terms = context.IDENTIFIER().map { it.text }
        return BhutasamkhyaPada(
            sourceText = context.text,
            terms = terms,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSubantaVakyaPada(
        context: PaniniyaVyakaranamParser.SubantaVakyaPadaContext,
    ): List<Pada> =
        when {
            context.subantaPada() != null ->
                listOf(buildSubanta(context.subantaPada()!!))

            context.samuccitaSubanta() != null ->
                listOf(buildSamuccitaSubanta(context.samuccitaSubanta()!!))

            context.sankhyaPada() != null ->
                listOf(buildSankhyaPada(context.sankhyaPada()!!))

            context.sankhyaPuranaPada() != null ->
                listOf(buildSankhyaPuranaPada(context.sankhyaPuranaPada()!!))

            context.sankhyaAbhyasaPada() != null ->
                listOf(buildSankhyaAbhyasaPada(context.sankhyaAbhyasaPada()!!))

            context.katapayadiPada() != null ->
                listOf(buildKatapayadiPada(context.katapayadiPada()!!))

            context.aryabhatiyaPada() != null ->
                listOf(buildAryabhatiyaPada(context.aryabhatiyaPada()!!))

            context.bhutasamkhyaPada() != null ->
                listOf(buildBhutasamkhyaPada(context.bhutasamkhyaPada()!!))

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
            subanta = buildSubanta(context.subantaPada()!!),
        )

    private fun buildSubanta(
        context: PaniniyaVyakaranamParser.SubantaPadaContext,
    ): SubantaPada =
        SubantaPada(
            sourceText = context.text,
            pratipadika = buildPratipadika(context.pratipadika()!!),
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )

    private fun buildPratipadika(
        context: PaniniyaVyakaranamParser.PratipadikaContext,
    ): Pratipadika {
        val mula = buildPratipadikaMula(context.pratipadikaMula()!!)
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
                    text = context.mulaPratipadika()!!.text,
                )

            context.samjnaQualifierPratipadika() != null ->
                MulaPratipadika(
                    sourceText = context.text,
                    text = context.samjnaQualifierPratipadika()!!.text,
                )

            context.kridantaPratipadika() != null ->
                buildKridanta(context.kridantaPratipadika()!!)

            context.unadyantaPratipadika() != null ->
                buildUnadyanta(context.unadyantaPratipadika()!!)

            context.samasaPratipadika() != null ->
                buildSamasa(context.samasaPratipadika()!!)

            context.pratipadika() != null ->
                buildPratipadika(context.pratipadika()!!)

            else -> error("अज्ञातं प्रातिपदिकमूलम्: ${context.text}")
        }

    private fun buildKridanta(
        context: PaniniyaVyakaranamParser.KridantaPratipadikaContext,
    ): KridantaPratipadika =
        KridantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            krtPratyaya = context.krtPratyaya()!!.text,
        )

    private fun buildUnadyanta(
        context: PaniniyaVyakaranamParser.UnadyantaPratipadikaContext,
    ): UnadyantaPratipadika =
        UnadyantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            unadiPratyaya = context.unadiPratyaya()!!.IDENTIFIER()!!.text,
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
                context.asamasikaPratipadika()!!,
            ),
            sup = supAvastha?.supPratyaya()?.let {
                SupPratyaya(
                    sourceText = it.text,
                    text = it.text,
                )
            },
            supLopa = supAvastha?.supAvastha()?.let {
                SupLopa.fromUpadesha(it.text)
            },
        )
    }

    private fun buildAsamasikaPratipadika(
        context: PaniniyaVyakaranamParser.AsamasikaPratipadikaContext,
    ): Pratipadika {
        val mulaContext = context.asamasikaPratipadikaMula()!!

        val mula = when {
            mulaContext.mulaPratipadika() != null ->
                MulaPratipadika(
                    sourceText = mulaContext.text,
                    text = mulaContext.mulaPratipadika()!!.text,
                )

            mulaContext.samjnaQualifierPratipadika() != null ->
                MulaPratipadika(
                    sourceText = mulaContext.text,
                    text = mulaContext.samjnaQualifierPratipadika()!!.text,
                )

            mulaContext.kridantaPratipadika() != null ->
                buildKridanta(mulaContext.kridantaPratipadika()!!)

            mulaContext.unadyantaPratipadika() != null ->
                buildUnadyanta(mulaContext.unadyantaPratipadika()!!)

            mulaContext.samasaPratipadika() != null ->
                buildSamasa(mulaContext.samasaPratipadika()!!)

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
                    pratyaya = context.taddhitaPratyaya()!!.text,
                )

            context.striPratyaya() != null ->
                StriVikara(
                    sourceText = context.text,
                    pratyaya = context.striPratyaya()!!.text,
                )

            else -> error("अज्ञातः प्रातिपदिकविकारः: ${context.text}")
        }

    private fun buildTinganta(
        context: PaniniyaVyakaranamParser.TingantaPadaContext,
    ): TingantaPada =
        TingantaPada(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            lakara = Lakara.fromUpadesha(context.lakara()!!.text),
            ting = TingPratyaya(
                sourceText = context.tingPratyaya()!!.text,
                text = context.tingPratyaya()!!.text,
            ),
        )

    private fun buildDhatu(
        context: PaniniyaVyakaranamParser.DhatuPrakritiContext,
    ): DhatuPrakriti =
        DhatuPrakriti(
            sourceText = context.text,
            mulaDhatu = context.dhatuMula()!!.text,
            sanadiPratyayas = context.sanadiPratyaya().map { it.text },
        )

    private fun buildAvyaya(
        context: PaniniyaVyakaranamParser.AvyayaPadaContext,
    ): AvyayaPada =
        when {
            context.mulaAvyaya() != null ->
                AvyayaPada(
                    sourceText = context.text,
                    form = context.mulaAvyaya()!!.text,
                )

            context.avyayaKridanta() != null -> {
                val kridanta = context.avyayaKridanta()!!

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaKridantaDerivation(
                        upasargas = buildUpasargas(kridanta.upasargaKrama()),
                        dhatu = buildDhatu(kridanta.dhatuPrakriti()!!),
                        pratyaya = kridanta.avyayaKrtPratyaya()!!.text,
                    ),
                )
            }

            context.avyayaTaddhitanta() != null -> {
                val taddhitanta = context.avyayaTaddhitanta()!!

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaTaddhitaDerivation(
                        pratipadika = taddhitanta.mulaPratipadika()!!.text,
                        pratyaya = taddhitanta.avyayaTaddhitaPratyaya()!!.text,
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
                                .avyayibhavaPada()!!
                                .samasaPratipadika()!!,
                        ),
                    ),
                )

            context.sankhyaAvyaya() != null -> {
                val saCtx = context.sankhyaAvyaya()!!
                val kind = when {
                    saCtx.ADHIKA() != null -> "ADHIKA"
                    saCtx.UNA() != null -> "UNA"
                    saCtx.SAKRIT() != null -> "SAKRIT"
                    saCtx.DVIH() != null -> "DVIH"
                    saCtx.TRIH() != null -> "TRIH"
                    saCtx.CHATUH() != null -> "CHATUH"
                    saCtx.KRITVAS() != null -> "KRITVAS"
                    saCtx.DHAA() != null -> "DHA"
                    saCtx.SHAH() != null -> "SHAS"
                    else -> "UNKNOWN"
                }
                val stemList = saCtx.IDENTIFIER()?.let { listOf(it.text) } ?: emptyList()
                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = SankhyaAvyayaDerivation(kind = kind, stems = stemList),
                )
            }

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
            is SankhyaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is KridantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is UnadyantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is SamasaPratipadika -> pratipadika.copy(vikaras = vikaras)
        }

    private fun startTokenIndex(pada: Pada): Int = 0
}
