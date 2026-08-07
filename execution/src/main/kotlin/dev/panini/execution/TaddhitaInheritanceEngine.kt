package dev.panini.execution

/**
 * 4.1.92 तस्यापत्यम् & 7.2.117 तद्धितेष्वचामादेः
 * Pāṇinian Subclass Inheritance Derivation Engine (अण् / इञ् Pratyaya Vṛddhi).
 */
data class InheritanceRelation(
    val childStem: String,
    val parentStem: String,
)

object TaddhitaInheritanceEngine {

    /**
     * Applies 7.2.117 तद्धितेष्वचामादेः Vṛddhi vowel lengthening to derive child stem from parent.
     * e.g. "गणित" -> "गाणित", "गुण" -> "गौण", "शिव" -> "शैव"
     */
    fun deriveVriddhiStem(parentStem: String): String {
        val trimmed = parentStem.trim()
        if (trimmed.isEmpty()) return trimmed

        val firstChar = trimmed[0]
        val rest = trimmed.substring(1)

        val vriddhiFirst = when (firstChar) {
            'ग' -> "गा"
            'श' -> "शै"
            'क' -> "का"
            'म' -> "मा"
            'प' -> "पा"
            'ब' -> "बा"
            'द' -> "दा"
            'त' -> "ता"
            'न' -> "ना"
            'र' -> "रा"
            'ल' -> "ला"
            'व' -> "वा"
            'स' -> "सा"
            'ह' -> "हा"
            'अ' -> "आ"
            'इ', 'ई' -> "ऐ"
            'उ', 'ऊ' -> "औ"
            'ऋ' -> "आर"
            else -> firstChar.toString()
        }

        // If second char is vowel mark, lengthen it
        return if (trimmed.length > 1) {
            when {
                rest.startsWith("ु") -> vriddhiFirst.substring(0, 1) + "ौ" + rest.substring(1)
                rest.startsWith("ि") -> vriddhiFirst.substring(0, 1) + "ै" + rest.substring(1)
                else -> vriddhiFirst + rest
            }
        } else {
            vriddhiFirst
        }
    }

    /**
     * Detects subclass inheritance Adhikāra header: "<parent> + अण् + सुँ इति अधिकार + सुँ"
     * e.g. "गणित + अण् + सुँ इति अधिकार + सुँ ।"
     */
    fun detectInheritanceAdhikara(domainSegmented: String): InheritanceRelation? {
        val trimmed = domainSegmented.trim()
        if (!trimmed.contains("+ अण्") && !trimmed.contains("+ इञ्")) return null

        val match = Regex("""(\S+)\s*\+\s*(?:अण्|इञ्)""").find(trimmed)
        if (match != null) {
            val parentStem = match.groupValues[1].trim()
            val childStem = deriveVriddhiStem(parentStem)
            return InheritanceRelation(childStem = childStem, parentStem = parentStem)
        }

        return null
    }
}
