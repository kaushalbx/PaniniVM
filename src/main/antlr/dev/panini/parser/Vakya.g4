grammar Vakya;

@header {
package dev.panini.parser;
}

// ====================================================
// Top-level sentence structure
// ====================================================

utterance
    : sambodhana? vakya (vakyaChain vakya)* DANDA? EOF
    ;

vakyaChain
    : CONNECTIVE
    | DANDA
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
    : SUP_SUFFIX
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
// Lexer rules: nominal, verbal, kṛt, and taddhita terms
// ====================================================

RESULT_REFERENCE
    : 'पूर्वफल'
    | 'फल'
    | 'फले'
    | 'फलानि'
    ;

NUMERAL
    : 'शून्य'
    | 'एक'
    | 'द्वि'
    | 'त्रि'
    | 'चतुर्'
    | 'पञ्च'
    | 'षट्'
    | 'सप्त'
    | 'अष्ट'
    | 'नव'
    | 'दश'
    | 'विंशति'
    | 'त्रिंशत्'
    | 'चत्वारिंशत्'
    | 'पञ्चाशत्'
    | 'षष्टि'
    | 'सप्तति'
    | 'अशीति'
    | 'नवति'
    | 'शत'
    | 'सहस्र'
    | 'अयुत'
    | 'लक्ष'
    | 'प्रयुत'
    | 'कोटि'
    ;

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

SUP_SUFFIX
    : 'सुँ'
    | 'औ'
    | 'जस्'
    | 'अम्'
    | 'औट्'
    | 'शस्'
    | 'टा'
    | 'भ्याम्'
    | 'भिस्'
    | 'ङे'
    | 'भ्यस्'
    | 'ङसिँ'
    | 'ङस्'
    | 'ओस्'
    | 'आम्'
    | 'ङि'
    | 'सुप्'
    ;

NIC
    : 'णिच्'
    ;

SAN
    : 'सन्'
    ;

YANG
    : 'यङ्'
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
    : 'श्नौ'
    ;

SNU
    : 'स्नु'
    ;

KTA
    : 'क्त'
    ;

KTAVATU
    : 'क्तवतु'
    ;

TAVYAT
    : 'तव्यत्'
    ;

ANIYAR
    : 'अनीयर'
    ;

SHATR
    : 'शतृ'
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

TRICH
    : 'तृच्'
    ;

ANIN
    : 'अनिन'
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

KTVA
    : 'क्त्वा'
    ;

LYAP
    : 'ल्याप्'
    ;

TUMUN
    : 'तुमुन्'
    ;

MATUP
    : 'मतुप्'
    ;

VATUP
    : 'वतुप्'
    ;

INI
    : 'इनि'
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
// Identifiers and whitespace
// ====================================================

IDENTIFIER
    : [a-zA-Z\u0900-\u097F]+
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
