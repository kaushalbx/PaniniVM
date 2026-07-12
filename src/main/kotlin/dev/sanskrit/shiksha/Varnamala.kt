package dev.sanskrit.shiksha

object Varnamala {
    fun fromChar(c: Char): Varna? {
        return Svara.fromIndependent(c) 
            ?: Svara.fromMatra(c) 
            ?: Vyanjana.fromDevanagari(c)
    }

    fun isVowel(c: Char): Boolean = Svara.fromIndependent(c) != null || Svara.fromMatra(c) != null
    fun isConsonant(c: Char): Boolean = Vyanjana.fromDevanagari(c) != null

    fun getSthana(c: Char): Set<Sthana> {
        return when (val varna = fromChar(c)) {
            is Svara -> setOf(varna.sthana)
            is Vyanjana -> varna.sthana
            else -> emptySet()
        }
    }

    fun getAbhyantaraPrayatna(c: Char): AbhyantaraPrayatna? {
        return when (val varna = fromChar(c)) {
            is Svara -> varna.abhyantaraPrayatna
            is Vyanjana -> varna.abhyantaraPrayatna
            else -> null
        }
    }

    fun areSavarna(c1: Char, c2: Char): Boolean {
        if (c1 == c2) return true
        val v1 = fromChar(c1) ?: return false
        val v2 = fromChar(c2) ?: return false
        if ((v1 is Svara && v2 is Vyanjana) || (v1 is Vyanjana && v2 is Svara)) return false
        val s1 = getSthana(c1)
        val s2 = getSthana(c2)
        val p1 = getAbhyantaraPrayatna(c1)
        val p2 = getAbhyantaraPrayatna(c2)
        if ((normalize(c1) == 'ऋ' && normalize(c2) == 'ऌ') || (normalize(c1) == 'ऌ' && normalize(c2) == 'ऋ')) return true
        return (s1 intersect s2).isNotEmpty() && p1 == p2
    }

    /** 1.1.69: Maps Udit (ku, cu, etc.) to their respective vargas. */
    fun expandUdit(udit: String): Set<Char> = when (udit) {
        "कु" -> setOf('क', 'ख', 'ग', 'घ', 'ङ')
        "चु" -> setOf('च', 'छ', 'ज', 'झ', 'ञ')
        "टु" -> setOf('ट', 'ठ', 'ड', 'ढ', 'ण')
        "तु" -> setOf('त', 'थ', 'द', 'ध', 'न')
        "पु" -> setOf('प', 'फ', 'ब', 'भ', 'म')
        else -> emptySet()
    }

    /** Helper to get the specific member of a varga at a given index. */
    fun getVargaMember(varga: String, index: Int): Char? {
        val members = expandUdit(varga).toList().sorted() // Assumes standard Devanagari order
        return members.getOrNull(index)
    }

    /** Helper to find which varga a consonant belongs to and its index. */
    fun getVargaInfo(c: Char): Pair<String, Int>? {
        listOf("कु", "चु", "टु", "तु", "पु").forEach { varga ->
            val members = expandUdit(varga).toList().sorted()
            val index = members.indexOf(c)
            if (index != -1) return varga to index
        }
        return null
    }

    /** 1.1.48: eca igghrasvādeśe. Mapping EC to IK for shortening. */
    fun getHrasva(c: Char): String {
        return when (c) {
            'आ', 'ा' -> "अ"
            'ई', 'ि', 'ी' -> "इ"
            'ऊ', 'ु', 'ू' -> "उ"
            'ॠ', 'ृ', 'ॄ' -> "ऋ"
            'ए', 'े', 'ऐ', 'ै' -> "इ" // 1.1.48: e/ai -> i
            'ओ', 'ो', 'औ', 'ौ' -> "उ" // 1.1.48: o/au -> u
            else -> c.toString()
        }
    }

    fun getGuna(c: Char): String? = when (normalize(c)) {
        'इ' -> Svara.E.devanagari
        'उ' -> Svara.O.devanagari
        'ऋ' -> Svara.A.devanagari + Vyanjana.RA.halanta
        'ऌ' -> Svara.A.devanagari + Vyanjana.LA.halanta
        else -> null
    }

    fun getVrddhi(c: Char): String? = when (normalize(c)) {
        'इ' -> Svara.AI.devanagari
        'उ' -> Svara.AU.devanagari
        'ऋ' -> Svara.AA.devanagari + Vyanjana.RA.halanta
        'ऌ' -> Svara.AA.devanagari + Vyanjana.LA.halanta
        else -> null
    }

    private fun normalize(c: Char): Char = when (c) {
        'अ', 'आ', 'ा' -> 'अ'
        'इ', 'ई', 'ि', 'ी' -> 'इ'
        'उ', 'ऊ', 'ु', 'ू' -> 'उ'
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> 'ऋ'
        'ऌ', 'ॢ' -> 'ऌ'
        else -> c
    }
}
