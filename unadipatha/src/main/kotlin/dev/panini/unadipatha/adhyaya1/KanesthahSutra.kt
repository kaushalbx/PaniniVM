package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.103: कणेष्ठः
object KanesthahSutra : UnadiSutra(
    number = "1.103",
    text = "कणेष्ठः",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0517" },
        BhvadiDhatus.all.single { it.id == "01.0903" },
    ),
    pratyaya = "ठ",
    pratyayaSurface = "ठ",
    rootSamjnaMap = mapOf("कणँ" to Samjna.Rudhi("कण्ठ")),
    rootMorphology = mapOf("कणँ" to UnadiRootMorphology(conditionedStem = "कण्".toVarnas())),
    lexicalAttestations = listOf(UnadiLexicalAttestation("कण्ठ".toVarnas(), attributedSutra = "1.103")),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कण्ठ ठ-प्रत्ययान्त प्रातिपदिक है।"),
    hindiExplanation = "कणँ धातु के कण् आधार के बाद ठ प्रत्यय होता है। दोनों वर्ण मूर्धन्य होने से कण् + ठ का सीधा संयोग कण्ठ प्रातिपदिक देता है; सूत्र कोई अतिरिक्त आगम, लोप या आदेश नहीं बताता। धातुपाठ में शब्दार्थ वाली कणँ की दो भ्वादि प्रविष्टियाँ हैं और सूत्र गणभेद नहीं करता, इसलिए दोनों सुरक्षित रखी गई हैं; निमीलनार्थक चुरादि कणँ को अर्थतः असंबद्ध होने से नहीं मिलाया गया। वृत्ति केवल ‘कण्ठः’ रूप देती है, अर्थ और लिङ्ग स्पष्ट नहीं करती, अतः वे अनुमान से नहीं जोड़े गए।",
)
