grammar Vakya;

@header {
package dev.panini.execution.parser;
}

// ====================================================
// Top-level sentence structure
// ====================================================

utterance
    : sambodhana? vakya (CONNECTIVE vakya)* DANDA? EOF
    ;

sambodhana
    : HE subantaPada COMMA?
    ;

vakya
    : pada* tingantaPada pada*
    ;

pada
    : coordinatedSubanta
    | subantaPada
    | avyayaKridantaPada
    | avyayaPada
    ;

// ====================================================
// Nominal expressions
// ====================================================

/*
 * Parses:
 *
 * एक + अम् द्वि + औट् त्रि + शस् च
 *
 * All members belong to one coordinated expression.
 */
coordinatedSubanta
    : subantaPada (COMMA? subantaPada)+ CHA
    ;

/*
 * Kāraka is intentionally not determined here.
 *
 * ANTLR identifies the nominal form. A later semantic
 * analysis stage determines कर्तृ, कर्म, करण, etc.
 */
subantaPada
    : nominalBase PLUS supPratyaya
    | nominalBase
    ;

nominalBase
    : taddhitaPratipadika
    | kridantaPratipadika
    | samasaPratipadika
    | NUMERAL
    | RESULT_REFERENCE
    | IDENTIFIER
    ;

samasaPratipadika
    : simplePratipadika (COMPOUND_SEPARATOR simplePratipadika)+
    ;

simplePratipadika
    : NUMERAL
    | RESULT_REFERENCE
    | IDENTIFIER
    ;

// ====================================================
// Kṛdanta derivations
// ====================================================

kridantaPratipadika
    : dhatu (PLUS vikarana)? PLUS krtPratyaya
    ;

avyayaKridantaPada
    : dhatu (PLUS vikarana)? PLUS avyayaKrtPratyaya
    ;

krtPratyaya
    : KTA
    | KTAVATU
    | TAVYAT
    | ANIYAR
    | SHATR
    | SHANACH
    | GHANJ
    | LYUT
    | NVUL
    | TRICH
    | ANIN
    | KYAP
    | YAT
    | NYAT
    ;

avyayaKrtPratyaya
    : KTVA
    | LYAP
    | TUMUN
    ;

// ====================================================
// Taddhitānta derivations
// ====================================================

taddhitaPratipadika
    : simplePratipadika PLUS taddhitaPratyaya
    ;

taddhitaPratyaya
    : MATUP
    | VATUP
    | INI
    | TVA
    | TAL
    | TARAP
    | TAMAP
    | MAYAT
    | TASIL
    | AN
    | INJ
    | DHAK
    | THAJ
    | CHA_SUFFIX
    ;

// ====================================================
// Verbal expressions
// ====================================================

/*
 * Parses:
 *
 * युज् + णिच् + लोट् + सिप्
 *
 * Multiple sanādi suffixes are allowed because a verbal
 * derivation may contain more than one such suffix.
 */
tingantaPada
    : dhatu
      (PLUS sanadiPratyaya)*
      (PLUS vikarana)?
      PLUS lakara
      (PLUS tingPratyaya)?
    | IDENTIFIER
    ;

dhatu
    : IDENTIFIER
    ;

sanadiPratyaya
    : NIC
    | SAN
    | YANG
    ;

vikarana
    : SHAP
    | SHYAN
    | SHNAM
    | SHNU
    | SNU
    ;

lakara
    : LAKARA
    ;

tingPratyaya
    : TING
    ;

// ====================================================
// Sup suffixes
// ====================================================

supPratyaya
    : SUP_PRATHAMA
    | SUP_DVITIYA
    | SUP_TRTIYA
    | SUP_CHATURTHI
    | SUP_PANCHAMI
    | SUP_SHASTHI
    | SUP_SAPTAMI
    ;

// ====================================================
// Indeclinables
// ====================================================

avyayaPada
    : MAA
    | KRPAYA
    ;

// ====================================================
// Lexer rules: punctuation and indeclinables
// ====================================================

HE
    : 'हे'
    ;

CHA
    : 'च'
    ;

MAA
    : 'मा'
    ;

KRPAYA
    : 'कृपया'
    ;

CONNECTIVE
    : 'ततः'
    | 'अथ'
    | 'अनन्तरम्'
    ;

PLUS
    : '+'
    ;

COMMA
    : ','
    | '،'
    ;

COMPOUND_SEPARATOR
    : '-'
    | '—'
    ;

DANDA
    : '।'
    | '॥'
    | '.'
    ;

// ====================================================
// Canonical number and execution-reference tokens
// ====================================================

NUMERAL
    : 'शून्य'
    | 'शून्यम्'
    | 'शून्यं'
    | 'एक'
    | 'एकम्'
    | 'एकं'
    | 'द्वि'
    | 'द्वे'
    | 'त्रि'
    | 'त्रीणि'
    | 'चतुर्'
    | 'चत्वारि'
    | 'पञ्च'
    | 'षट्'
    | 'सप्त'
    | 'अष्ट'
    | 'नव'
    | 'दश'
    ;

RESULT_REFERENCE
    : 'फल'
    | 'फलम्'
    | 'फलं'
    | 'फले'
    | 'फलानि'
    | 'पूर्वफल'
    | 'पूर्वफलम्'
    | 'पूर्वफलं'
    | 'पूर्वफले'
    | 'पूर्वफलानि'
    ;

// ====================================================
// Sup suffix tokens
// ====================================================

SUP_PRATHAMA
    : 'सुँ'
    | 'औ'
    | 'जस्'
    ;

SUP_DVITIYA
    : 'अम्'
    | 'औट्'
    | 'शस्'
    ;

SUP_TRTIYA
    : 'टा'
    | 'भ्याम्'
    | 'भिस्'
    ;

SUP_CHATURTHI
    : 'ङे'
    | 'भ्याम्'
    | 'भ्यस्'
    ;

SUP_PANCHAMI
    : 'ङसिँ'
    | 'भ्याम्'
    | 'भ्यस्'
    ;

SUP_SHASTHI
    : 'ङस्'
    | 'ओस्'
    | 'आम्'
    ;

SUP_SAPTAMI
    : 'ङि'
    | 'ओस्'
    | 'सुप्'
    ;

// ====================================================
// Sanādi and vikaraṇa suffixes
// ====================================================

NIC
    : 'णिच्'
    ;

SAN
    : 'सँन्'
    ;

YANG
    : 'यँङ्'
    ;

SHAP
    : 'शप्'
    ;

SHYAN
    : 'श्यन्'
    ;

SHNAM
    : 'श्नम्'
    ;

SHNU
    : 'श्नु'
    ;

SNU
    : 'स्नु'
    ;

// ====================================================
// Kṛt suffixes
// ====================================================

KTA
    : 'क्त'
    ;

KTAVATU
    : 'क्तवतुँ'
    ;

TAVYAT
    : 'तव्यत्'
    | 'तव्य'
    ;

ANIYAR
    : 'अनीयर्'
    ;

SHATR
    : 'शतृँ'
    ;

SHANACH
    : 'शानच्'
    ;

GHANJ
    : 'घञ्'
    ;

LYUT
    : 'ल्युट्'
    ;

NVUL
    : 'ण्वुल्'
    ;

KTVA
    : 'क्त्वा'
    ;

LYAP
    : 'ल्यप्'
    ;

TUMUN
    : 'तुमुन्'
    ;

TRICH
    : 'तृच्'
    ;

ANIN
    : 'अणिन्'
    ;

KYAP
    : 'क्यप्'
    ;

YAT
    : 'यत्'
    ;

NYAT
    : 'ण्यत्'
    ;

// ====================================================
// Taddhita suffixes
// ====================================================

MATUP
    : 'मतुँप्'
    ;

VATUP
    : 'वतुँप्'
    ;

INI
    : 'इनिँ'
    ;

TVA
    : 'त्व'
    ;

TAL
    : 'तल्'
    ;

TARAP
    : 'तरप्'
    ;

TAMAP
    : 'तमप्'
    ;

MAYAT
    : 'मयट्'
    ;

TASIL
    : 'तसिल्'
    ;

AN
    : 'अण्'
    ;

INJ
    : 'इञ्'
    ;

DHAK
    : 'ढक्'
    ;

THAJ
    : 'ठञ्'
    ;

CHA_SUFFIX
    : 'छ'
    ;

// ====================================================
// Lakāra and tiṅ suffixes
// ====================================================

LAKARA
    : 'लट्'
    | 'लिट्'
    | 'लुट्'
    | 'लृट्'
    | 'लेट्'
    | 'लोट्'
    | 'लङ्'
    | 'लिङ्'
    | 'लुङ्'
    | 'लृङ्'
    ;

TING
    : 'तिप्'
    | 'तस्'
    | 'झि'
    | 'सिप्'
    | 'थस्'
    | 'थ'
    | 'मिप्'
    | 'वस्'
    | 'मस्'
    | 'त'
    | 'आताम्'
    | 'झ'
    | 'थास्'
    | 'आथाम्'
    | 'ध्वम्'
    | 'इट्'
    | 'वहि'
    | 'महिङ्'
    ;

// ====================================================
// Generic identifiers
// ====================================================

IDENTIFIER
    : DEVANAGARI_CHARACTER+
    ;

fragment DEVANAGARI_CHARACTER
    : [\u0900-\u097F]
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
