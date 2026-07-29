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
            Triple("यदि", "conditional", "If condition"),
            Triple("तर्हि", "conditional", "Then clause"),
            Triple("अन्यथा", "conditional", "Else clause"),
            Triple("इत्यादि", "enumeration", "Et cetera"),
            Triple("हे", "vocative", "Address marker"),
            Triple("यन्त्र", "system", "PaniniVM system target"),
            Triple("पूर्वफल", "स्मृति-सन्दर्भः", "Result of previous turn"),
            Triple("पूर्वपूर्वफल", "स्मृति-सन्दर्भः", "Result of 2 turns ago"),
            Triple("कटपय", "सङ्ख्या-पद्धतिः", "Kaṭapayādi number system prefix"),
            Triple("भूतसङ्ख्या", "सङ्ख्या-पद्धतिः", "Bhūta-saṅkhyā number system prefix"),
            Triple("आर्यभटीय", "सङ्ख्या-पद्धतिः", "Āryabhaṭīya number system prefix"),
            Triple("उणादि", "व्युत्पत्तिः", "Uṇādipāṭha nominal derivation")
        )

        private val abhyasaAffixes = listOf(
            Triple("कृत्वः", "सङ्ख्याभ्यासः", "Frequency repetition suffix"),
            Triple("सुच्", "सङ्ख्याभ्यासः", "Frequency suffix for 2-4 (e.g. द्विः)"),
            Triple("धा", "सङ्ख्याभ्यासः", "Distributive repetition suffix (e.g. त्रिधा)")
        )

        private val unadiAffixes = listOf(
            Triple("उण्", "उणादि-प्रत्ययः", "Unadi suffix (1.1) deriving nominal stems"),
            Triple("सिः", "उणादि-प्रत्ययः", "Unadi suffix (2.8) deriving nominal stems")
        )

        private val supAffixes = listOf(
            Triple("सुँ", "प्रथमा-एकवचनम्", "Nominative singular"),
            Triple("अम्", "द्वितीया-एकवचनम्", "Accusative singular"),
            Triple("औट्", "प्रथमा/द्वितीया-द्विवचनम्", "Dual affix"),
            Triple("शस्", "द्वितीया-बहुवचनम्", "Accusative plural"),
            Triple("टा", "तृतीया-एकवचनम्", "Instrumental singular"),
            Triple("भ्याम्", "तृतीया/चतुर्थी/पञ्चमी-द्विवचनम्", "Dual instrumental/dative/ablative"),
            Triple("भिसँ", "तृतीया-बहुवचनम्", "Instrumental plural"),
            Triple("ङे", "चतुर्थी-एकवचनम्", "Dative singular"),
            Triple("भ्यस्", "चतुर्थी/पञ्चमी-बहुवचनम्", "Plural dative/ablative"),
            Triple("ङसिँ", "पञ्चमी-एकवचनम्", "Ablative singular"),
            Triple("ङस्", "षष्ठी-एकवचनम्", "Genitive singular"),
            Triple("ओस्", "षष्ठी/सप्तमी-द्विवचनम्", "Dual genitive/locative"),
            Triple("आम्", "षष्ठी-बहुवचनम्", "Genitive plural"),
            Triple("ङि", "सप्तमी-एकवचनम्", "Locative singular"),
            Triple("सुप्", "सप्तमी-बहुवचनम्", "Locative plural")
        )

        private val tingAffixes = listOf(
            Triple("तिप्", "परस्मैपद-प्रथम-एक", "3rd person singular"),
            Triple("तस्", "परस्मैपद-प्रथम-द्वि", "3rd person dual"),
            Triple("झि", "परस्मैपद-प्रथम-बहु", "3rd person plural"),
            Triple("सिप्", "परस्मैपद-मध्यम-एक", "2nd person singular"),
            Triple("थस्", "परस्मैपद-मध्यम-द्वि", "2nd person dual"),
            Triple("थ", "परस्मैपद-मध्यम-बहु", "2nd person plural"),
            Triple("मिप्", "परस्मैपद-उत्तम-एक", "1st person singular"),
            Triple("वस्", "परस्मैपद-उत्तम-द्वि", "1st person dual"),
            Triple("मस्", "परस्मैपद-उत्तम-बहु", "1st person plural"),
            Triple("णिच्", "प्रत्ययः", "Causative affix"),
            Triple("लोट्", "लकारः", "Imperative mood"),
            Triple("लट्", "लकारः", "Present tense"),
            Triple("लङ्", "लकारः", "Past imperfect"),
            Triple("लुट्", "लकारः", "First future (periphrastic)"),
            Triple("लृट्", "लकारः", "Second future (simple)"),
            Triple("लेट्", "लकारः", "Subjunctive mood"),
            Triple("लिङ्", "लकारः", "Optative / Potential mood"),
            Triple("लुङ्", "लकारः", "Aorist tense"),
            Triple("लृङ्", "लकारः", "Conditional mood"),
            Triple("लिट्", "लकारः", "Perfect tense")
        )

        private val numbers = listOf(
            Triple("एक", "सङ्ख्या", "1"),
            Triple("द्वि", "सङ्ख्या", "2"),
            Triple("त्रि", "सङ्ख्या", "3"),
            Triple("चतुर्", "सङ्ख्या", "4"),
            Triple("पञ्च", "सङ्ख्या", "5"),
            Triple("दश", "सङ्ख्या", "10"),
            Triple("शत", "सङ्ख्या", "100"),
            Triple("सहस्र", "सङ्ख्या", "1000")
        )

        private val dhatus = listOf(
            Triple("भूँ", "स्वादि-धातुः", "to be / exist"),
            Triple("स्थाञँ", "स्वादि-धातुः", "to stand / wait"),
            Triple("गमॢँ", "स्वादि-धातुः", "to go / move"),
            Triple("डुदाञ्", "जुहोत्यादि-धातुः", "to give / assign"),
            Triple("लिखँ", "तुदादि-धातुः", "to write / draw"),
            Triple("डुकृञ्", "तनादि-धातुः", "to do / make")
        )

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            keywords.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" ($doc)", true)
                        .withBoldness(true)
                )
            }

            supAffixes.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" [$doc]", true)
                )
            }

            tingAffixes.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" [$doc]", true)
                )
            }

            abhyasaAffixes.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" [$doc]", true)
                )
            }

            unadiAffixes.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" [$doc]", true)
                )
            }

            numbers.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" = $doc", true)
                )
            }

            dhatus.forEach { (word, type, doc) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText(type)
                        .withTailText(" - $doc", true)
                )
            }

            GanaPatha.all.forEach { gana ->
                result.addElement(
                    LookupElementBuilder.create(gana.name)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText("गणपाठः")
                        .withTailText(" (Sūtra: ${gana.sutra})", true)
                )
            }

            BhutasamkhyaLexicon.allSymbols.forEach { (word, value) ->
                result.addElement(
                    LookupElementBuilder.create(word)
                        .withIcon(PvmIcons.FILE)
                        .withTypeText("भूतसङ्ख्या")
                        .withTailText(" = $value", true)
                )
            }
        }
    }
}
