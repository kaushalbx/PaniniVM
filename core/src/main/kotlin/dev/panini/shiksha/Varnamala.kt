package dev.panini.shiksha

object Varnamala {
    fun fromChar(c: Char): Varna? {
        return Svara.fromIndependent(c)
            ?: Svara.fromMatra(c)
            ?: Vyanjana.fromDevanagari(c)
    }

    fun isVowel(c: Char): Boolean = Svara.fromIndependent(c) != null || Svara.fromMatra(c) != null
    fun isConsonant(c: Char): Boolean = Vyanjana.fromDevanagari(c) != null

    fun getSthana(varna: Varna): Set<Sthana> =
        when (varna) {
            is Svara -> varna.sthana.constituents()
            is Vyanjana -> varna.sthana.flatMap { it.constituents() }.toSet()
            else -> emptySet()
        }

    fun getSthana(c: Char): Set<Sthana> = fromChar(c)?.let(::getSthana).orEmpty()

    fun getAbhyantaraPrayatna(varna: Varna): AbhyantaraPrayatna? =
        when (varna) {
            is Svara -> varna.abhyantaraPrayatna
            is Vyanjana -> varna.abhyantaraPrayatna
            else -> null
        }

    fun getAbhyantaraPrayatna(c: Char): AbhyantaraPrayatna? =
        fromChar(c)?.let(::getAbhyantaraPrayatna)

    fun areSavarna(first: Varna, second: Varna): Boolean {
        if (first == second) return true
        if ((first is Svara) != (second is Svara)) return false
        if (first is Svara && second is Svara) {
            val firstFamily = svaraFamily(first)
            val secondFamily = svaraFamily(second)
            return firstFamily == secondFamily ||
                firstFamily == Svara.R && secondFamily == Svara.L ||
                firstFamily == Svara.L && secondFamily == Svara.R
        }
        return (getSthana(first) intersect getSthana(second)).isNotEmpty() &&
            getAbhyantaraPrayatna(first) == getAbhyantaraPrayatna(second)
    }

    fun areSavarna(c1: Char, c2: Char): Boolean {
        val v1 = fromChar(c1) ?: return false
        val v2 = fromChar(c2) ?: return false
        return areSavarna(v1, v2)
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
    fun getHrasva(svara: Svara): Svara = svara.toHrasva()

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

    fun getGuna(svara: Svara): List<Varna>? = when (svara) {
        Svara.I, Svara.II -> listOf(Svara.E)
        Svara.U, Svara.UU -> listOf(Svara.O)
        Svara.R, Svara.RR -> listOf(Svara.A, Vyanjana.RA)
        Svara.L, Svara.LL -> listOf(Svara.A, Vyanjana.LA)
        else -> null
    }

    fun getVrddhi(c: Char): String? = when (c) {
        'अ', 'आ' -> Svara.AA.devanagari
        'ा' -> "ा"
        'ि', 'ी' -> "ै"
        'ु', 'ू' -> "ौ"
        'े', 'ै' -> "ै"
        'ो', 'ौ' -> "ौ"
        'इ', 'ई' -> Svara.AI.devanagari
        'उ', 'ऊ' -> Svara.AU.devanagari
        'ए', 'ऐ' -> Svara.AI.devanagari
        'ओ', 'औ' -> Svara.AU.devanagari
        'ृ', 'ॄ' -> Svara.AA.matra + Vyanjana.RA.halanta
        'ऋ', 'ॠ' -> Svara.AA.devanagari + Vyanjana.RA.halanta
        'ऌ' -> Svara.AA.devanagari + Vyanjana.LA.halanta
        else -> null
    }

    fun getVrddhi(svara: Svara): List<Varna>? = when (svara) {
        Svara.A, Svara.AA -> listOf(Svara.AA)
        Svara.I, Svara.II, Svara.E, Svara.AI -> listOf(Svara.AI)
        Svara.U, Svara.UU, Svara.O, Svara.AU -> listOf(Svara.AU)
        Svara.R, Svara.RR -> listOf(Svara.AA, Vyanjana.RA)
        Svara.L, Svara.LL -> listOf(Svara.AA, Vyanjana.LA)
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

    fun endsWithA(surface: String): Boolean = surface.lastSvara() == Svara.A

    fun endsWithAA(surface: String): Boolean = surface.lastSvara() == Svara.AA

    fun endsWithI(surface: String): Boolean = surface.lastSvara() == Svara.I

    fun endsWithU(surface: String): Boolean = surface.lastSvara() == Svara.U

    private fun svaraFamily(svara: Svara): Svara = when (svara) {
        Svara.A, Svara.AA -> Svara.A
        Svara.I, Svara.II -> Svara.I
        Svara.U, Svara.UU -> Svara.U
        Svara.R, Svara.RR -> Svara.R
        Svara.L, Svara.LL -> Svara.L
        else -> svara
    }
}
