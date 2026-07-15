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
            is Svara -> varna.sthana.constituents()
            is Vyanjana -> varna.sthana.flatMap { it.constituents() }.toSet()
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
        
        if (v1 is Svara && v2 is Svara) {
            val n1 = normalize(c1)
            val n2 = normalize(c2)
            return n1 == n2 || (n1 == 'ऋ' && n2 == 'ऌ') || (n1 == 'ऌ' && n2 == 'ऋ')
        }
        
        val s1 = getSthana(c1)
        val s2 = getSthana(c2)
        val p1 = getAbhyantaraPrayatna(c1)
        val p2 = getAbhyantaraPrayatna(c2)
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

    fun getGuna(c: Char): String? = when (c) {
        'ि', 'ी' -> "े"
        'ु', 'ू' -> "ो"
        'ृ', 'ॄ' -> "र्"
        'इ', 'ई' -> Svara.E.devanagari
        'उ', 'ऊ' -> Svara.O.devanagari
        'ऋ', 'ॠ' -> Svara.A.devanagari + Vyanjana.RA.halanta
        'ऌ' -> Svara.A.devanagari + Vyanjana.LA.halanta
        else -> null
    }

    fun getVrddhi(c: Char): String? = when (c) {
        'अ', 'आ' -> Svara.AA.devanagari
        'ा' -> "ा"
        'ि', 'ी' -> "ै"
        'ु', 'ू' -> "ौ"
        'इ', 'ई' -> Svara.AI.devanagari
        'उ', 'ऊ' -> Svara.AU.devanagari
        'ृ', 'ॄ' -> Svara.AA.matra + Vyanjana.RA.halanta
        'ऋ', 'ॠ' -> Svara.AA.devanagari + Vyanjana.RA.halanta
        'ऌ' -> Svara.AA.devanagari + Vyanjana.LA.halanta
        else -> null
    }

    fun normalize(c: Char): Char = when (c) {
        'अ', 'आ', 'ा' -> 'अ'
        'इ', 'ई', 'ि', 'ी' -> 'इ'
        'उ', 'ऊ', 'ु', 'ू' -> 'उ'
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> 'ऋ'
        'ऌ', 'ॢ' -> 'ऌ'
        else -> c
    }

    val independentVowelsOrMarks: Set<Char> = buildSet {
        Svara.entries.forEach { svara ->
            add(svara.devanagari.single())
            svara.matra?.single()?.let(::add)
        }
        Ayogavaha.entries.forEach { add(it.devanagari.single()) }
        add('ँ')
        add(Vyanjana.VIRAMA)
    }

    fun endsWithA(surface: String): Boolean {
        if (surface.isEmpty()) return false
        val last = surface.last()
        if (last == 'अ') return true
        return last !in independentVowelsOrMarks
    }

    fun endsWithAA(surface: String): Boolean {
        if (surface.isEmpty()) return false
        val last = surface.last()
        return last == 'आ' || last == 'ा'
    }

    fun endsWithI(surface: String): Boolean {
        if (surface.isEmpty()) return false
        val last = surface.last()
        return last == 'इ' || last == 'ि'
    }

    fun endsWithU(surface: String): Boolean {
        if (surface.isEmpty()) return false
        val last = surface.last()
        return last == 'उ' || last == 'ु'
    }
}
