package dev.panini.plugin

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import dev.panini.ganapatha.GanaPatha
import dev.panini.bhutasamkhya.BhutasamkhyaLexicon
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.core.KrtAffix
import dev.panini.core.Lakara
import dev.panini.core.FrequencyAffix
import dev.panini.core.SanadiAffix
import dev.panini.core.StriAffix
import dev.panini.core.SupAffix
import dev.panini.core.TaddhitaAffix
import dev.panini.core.TingAffix
import dev.panini.core.Upasarga
import dev.panini.unadipatha.UnadiPathaCatalog
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Vikarana

class PvmCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(PvmLanguage.INSTANCE),
            PvmCompletionProvider()
        )
    }

    private class PvmCompletionProvider : CompletionProvider<CompletionParameters>() {
        private val keywords = listOf(
            Triple("च", "conjunction", "And / Join"),
            Triple("इति", "terminator", "End of expression / clause"),
            Triple("वद्", "धातुः", "Dhātu root (वदँ व्यक्तायां वाचि)"),
            Triple("कृ", "धातुः", "Dhātu root (डुकृञ् करणे)"),
            Triple("यदि", "conditional", "If condition"),
            Triple("तर्हि", "conditional", "Then clause"),
            Triple("अन्यथा", "conditional", "Else clause"),
            Triple("इत्यादि", "enumeration", "Et cetera"),
            Triple("हे", "vocative", "Address marker"),
            Triple("यन्त्र", "system", "PaniniVM system target"),
            Triple("पूर्व", "स्मृति-क्रमः", "Previous matching kriyā; qualify फल or a kāraka"),
            Triple("फल", "क्रियाफलम्", "Result of a referenced kriyā"),
            Triple("कटपय", "सङ्ख्या-पद्धतिः", "Kaṭapayādi number system prefix"),
            Triple("भूतसङ्ख्या", "सङ्ख्या-पद्धतिः", "Bhūta-saṅkhyā number system prefix"),
            Triple("आर्यभटीय", "सङ्ख्या-पद्धतिः", "Āryabhaṭīya number system prefix"),
            Triple("उणादि", "व्युत्पत्तिः", "Uṇādipāṭha nominal derivation")
        )

        private val upasargas = Upasarga.entries.map {
            Triple(it.upadesha, "उपसर्गः", "Upasarga preverb (Sūtra 1.4.59)")
        }

        private val frequencyAffixes = FrequencyAffix.entries.map {
            Triple(it.upadesha, "सङ्ख्याभ्यासः", it.name)
        }

        private val unadiAffixes by lazy {
            UnadiPathaCatalog.sutras
                .filter { it.pratyaya.isNotBlank() }
                .distinctBy { it.pratyaya }
                .map { Triple(it.pratyaya, "उणादि-प्रत्ययः", "Uṇādipāṭha ${it.number}") }
        }

        private val supAffixes = SupAffix.entries.map {
            Triple(it.initialSurface, "सुप्-प्रत्ययः", "${it.vibhakti.name.lowercase()} ${it.vacana.name.lowercase()}")
        }.distinctBy { it.first }

        private val lakaras = Lakara.entries.map { Triple(it.upadesha, "लकारः", it.name) }

        private val sanadiAffixes = SanadiAffix.entries.map {
            Triple(it.upadesha, "सनादि-प्रत्ययः", it.name)
        }

        private val krtAffixes = KrtAffix.entries.map { Triple(it.upadesha, "कृत्-प्रत्ययः", it.name) }

        private val taddhitaAffixes = TaddhitaAffix.entries.map {
            Triple(it.upadesha, "तद्धित-प्रत्ययः", it.name)
        }

        private val tingAffixes = TingAffix.entries.map {
            Triple(
                it.upadesha,
                "तिङ्-प्रत्ययः",
                "${it.pada.name.lowercase()} ${it.purusha.name.lowercase()} ${it.vacana.name.lowercase()}",
            )
        }

        private val vikaranas = Vikarana.entries.map {
            Triple(it.upadesha, "विकरणः", it.gana?.name?.lowercase() ?: "verbal stem marker")
        }

        private val striAffixes = StriAffix.entries.map {
            Triple(it.upadesha, "स्त्री-प्रत्ययः", it.name)
        }

        private val numbers = PrimitiveSankhya.entries.map {
            Triple(it.pratipadika, "सङ्ख्या", it.value.toString())
        }

        private val dhatus by lazy {
            DhatuPathaRegistration.allDhatus.flatMap { dhatu ->
                val description = dhatu.arthaEnglish.ifBlank { dhatu.artha }
                listOf(dhatu.upadesha, dhatu.sourceSurface)
                    .filter(String::isNotBlank)
                    .distinct()
                    .map { surface ->
                        Triple(surface, "${dhatu.gana.name.lowercase()}-धातुः", description)
                    }
            }.distinctBy { it.first }
        }

        private val upasargaSurfaces = Upasarga.entries.mapTo(mutableSetOf()) { it.upadesha }

        private fun sourceWordBeforePlus(sourceBeforeCaret: String): String? {
            val beforePlus = sourceBeforeCaret.substringBeforeLast('+', missingDelimiterValue = "")
            return Regex("[\\p{L}\\p{M}]+$").find(beforePlus.trimEnd())?.value
        }

        private fun devanagariToIast(text: String): String {
            val result = StringBuilder()
            var i = 0
            while (i < text.length) {
                val c = text[i]
                when (c) {
                    'क' -> result.append("k")
                    'ख' -> result.append("kh")
                    'ग' -> result.append("g")
                    'घ' -> result.append("gh")
                    'ङ' -> result.append("ng")
                    'च' -> result.append("c")
                    'छ' -> result.append("ch")
                    'ज' -> result.append("j")
                    'झ' -> result.append("jh")
                    'ञ' -> result.append("n")
                    'ट' -> result.append("t")
                    'ठ' -> result.append("th")
                    'ड' -> result.append("d")
                    'ढ' -> result.append("dh")
                    'ण' -> result.append("n")
                    'त' -> result.append("t")
                    'थ' -> result.append("th")
                    'द' -> result.append("d")
                    'ध' -> result.append("dh")
                    'न' -> result.append("n")
                    'प' -> result.append("p")
                    'फ' -> result.append("ph")
                    'ब' -> result.append("b")
                    'भ' -> result.append("bh")
                    'म' -> result.append("m")
                    'य' -> result.append("y")
                    'र' -> result.append("r")
                    'ल' -> result.append("l")
                    'व' -> result.append("v")
                    'श' -> result.append("sh")
                    'ष' -> result.append("sh")
                    'स' -> result.append("s")
                    'ह' -> result.append("h")
                    'अ' -> result.append("a")
                    'आ' -> result.append("a")
                    'इ' -> result.append("i")
                    'ई' -> result.append("i")
                    'उ' -> result.append("u")
                    'ऊ' -> result.append("u")
                    'ऋ' -> result.append("r")
                    'ए' -> result.append("e")
                    'ऐ' -> result.append("ai")
                    'ओ' -> result.append("o")
                    'औ' -> result.append("au")
                    'ा' -> result.append("a")
                    'ि' -> result.append("i")
                    'ी' -> result.append("i")
                    'ु' -> result.append("u")
                    'ू' -> result.append("u")
                    'ृ' -> result.append("r")
                    'े' -> result.append("e")
                    'ै' -> result.append("ai")
                    'ो' -> result.append("o")
                    'ौ' -> result.append("au")
                    'ं' -> result.append("m")
                    'ः' -> result.append("h")
                    'ँ' -> result.append("m")
                    '्' -> {}
                    else -> result.append(c)
                }
                i++
            }
            return result.toString()
        }

        private fun buildLookup(
            word: String,
            type: String,
            doc: String,
            bold: Boolean = false,
        ): LookupElementBuilder {
            var builder = LookupElementBuilder.create(word)
                .withIcon(PvmIcons.FILE)
                .withTypeText(type)
                .withTailText(" [$doc]", true)
            if (bold) {
                builder = builder.withBoldness(true)
            }
            val iast = devanagariToIast(word)
            if (iast != word && iast.isNotBlank()) {
                builder = builder.withLookupString(iast)
                val simplified = iast.replace("c", "ch").replace("s", "sh")
                if (simplified != iast) {
                    builder = builder.withLookupString(simplified)
                }
            }
            val wordsInDoc = doc.split(Regex("[^a-zA-Z0-9]+")).filter { it.length >= 2 }
            for (w in wordsInDoc) {
                builder = builder.withLookupString(w.lowercase())
            }
            return builder
        }

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val sourceBeforeCaret = parameters.editor.document.charsSequence
                .subSequence(0, parameters.offset.coerceAtMost(parameters.editor.document.textLength))
                .toString()
            val beforeCurrentToken = sourceBeforeCaret.dropLastWhile {
                it.isLetter() || Character.getType(it) == Character.NON_SPACING_MARK.toInt()
            }
            val afterSegmentSeparator = beforeCurrentToken.trimEnd().endsWith('+')

            val precedingSegment = if (afterSegmentSeparator) sourceWordBeforePlus(sourceBeforeCaret) else null
            val afterUpasarga = precedingSegment in upasargaSurfaces

            if (!afterSegmentSeparator) {
                keywords.forEach { (word, type, doc) ->
                    result.addElement(buildLookup(word, type, doc, bold = true))
                }

                upasargas.forEach { (word, type, doc) ->
                    result.addElement(buildLookup(word, type, doc))
                }
            }

            if (!afterSegmentSeparator || !afterUpasarga) {
                sequenceOf(
                    supAffixes,
                    lakaras,
                    sanadiAffixes,
                    krtAffixes,
                    taddhitaAffixes,
                    tingAffixes,
                    vikaranas,
                    striAffixes,
                    frequencyAffixes,
                    unadiAffixes,
                ).flatten().forEach { (word, type, doc) ->
                    result.addElement(buildLookup(word, type, doc))
                }
            }

            if (!afterSegmentSeparator || afterUpasarga) {
                dhatus.forEach { (word, type, doc) ->
                    result.addElement(buildLookup(word, type, doc))
                }
            }

            if (!afterSegmentSeparator) {
                numbers.forEach { (word, type, doc) ->
                    result.addElement(buildLookup(word, type, doc))
                }
                GanaPatha.all.forEach { gana ->
                    result.addElement(buildLookup(gana.name, "गणपाठः", "Sūtra: ${gana.sutra}"))
                }
                BhutasamkhyaLexicon.allSymbols.forEach { (word, value) ->
                    result.addElement(buildLookup(word, "भूतसङ्ख्या", "Value: $value"))
                }
            }

            val currentFile = parameters.originalFile as? PvmFile ?: return
            if (afterSegmentSeparator) return
            val seenPrakriyas = mutableSetOf<Pair<String, String?>>()
            PvmProjectFileIndex.declarationDocuments(currentFile).forEach { (sourceFile, document) ->
                var activeDomain: String? = null
                document.items.forEach itemLoop@{ item ->
                    if (item is Scope) {
                        activeDomain = item.domainIdentity
                        return@itemLoop
                    }
                    val declaration = item as? Prakriya ?: return@itemLoop
                    if (declaration.modifiers.visibility == PrakriyaVisibility.INTERNAL && sourceFile != currentFile) {
                        return@itemLoop
                    }
                    val domain = declaration.domainIdentity ?: activeDomain
                    if (!seenPrakriyas.add(declaration.nameIdentity to domain)) return@itemLoop
                    val scope = domain?.let { "Domain: $it" } ?: "Unscoped project prakriyā"
                    result.addElement(
                        buildLookup(declaration.nameIdentity, "प्रक्रिया", scope, bold = true),
                    )
                }
            }
        }
    }
}
