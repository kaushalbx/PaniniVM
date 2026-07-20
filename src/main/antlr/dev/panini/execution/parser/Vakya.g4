grammar Vakya;

@header {
package dev.panini.execution.parser;
}

// ----------------------------------------------------
// Top-Level Sentence & Discourse Rules
// ----------------------------------------------------
utterance
    : salutation? vakya (CONNECTIVE vakya)* DANDA? EOF
    ;

salutation
    : HE IDENTIFIER COMMA?
    ;

vakya
    : pada* (tingantaPada | avyayaKridantaPada) pada*     # StandardVakya
    ;

pada
    : coordinatedSubanta                                  # CoordinatedPada
    | samasaSubanta                                       # SamasaPada
    | subantaPada                                         # SingleSubantaPada
    | avyayaKridantaPada                                  # KridantaAvyayaPada
    | avyayaPada                                          # AvyayaPadaRule
    ;

// ----------------------------------------------------
// 1. Subantapada (Nominals, Kṛdantas, Taddhitāntas & Samāsa)
// ----------------------------------------------------
coordinatedSubanta
    : subantaPada (COMMA? subantaPada)* CHA subantaPada
    ;

samasaSubanta
    : samasaPratipadika (PLUS supPratyaya)?
    ;

samasaPratipadika
    : pratipadika (COMPOUND_SEP pratipadika)+
    ;

subantaPada
    : numeralSubanta                                      # NumeralSubantaPada
    | resultSubanta                                       # ResultSubantaPada
    | kridantaPratipadika (PLUS supPratyaya)?             # KridantaSubantaDerivation
    | taddhitaPratipadika (PLUS supPratyaya)?             # TaddhitaSubantaDerivation
    | basePratipadika PLUS supPratyaya                    # BaseSubantaDerivation
    | IDENTIFIER                                          # SurfaceSubanta
    ;

numeralSubanta
    : NUMERAL_LEAF (PLUS supPratyaya)?
    ;

resultSubanta
    : RESULT_LEAF (PLUS supPratyaya)?
    ;

basePratipadika
    : NUMERAL_LEAF
    | RESULT_LEAF
    | IDENTIFIER
    ;

pratipadika
    : basePratipadika
    | kridantaPratipadika
    ;

// ----------------------------------------------------
// 2. Kṛdanta Derivations (Primary Verbal Suffixes)
// ----------------------------------------------------
kridantaPratipadika
    : dhatu (PLUS VIKARANA)? PLUS krtPratyaya
    ;

avyayaKridantaPada
    : (dhatu (PLUS VIKARANA)? PLUS avyayaKrtPratyaya)     # ExplicitAvyayaKridanta
    | IDENTIFIER                                          # SurfaceAvyayaKridanta
    ;

krtPratyaya
    : KTA | KTAVATU | TAVYAT | ANIYAR | SHATRI | SHANACH | GHANJ | LYUT | NVUL | KRT_PRATYAYA
    ;

avyayaKrtPratyaya
    : KTVAA | LYAP | TUMUN
    ;

// ----------------------------------------------------
// 3. Taddhitānta Derivations (Secondary Nominal Suffixes)
// ----------------------------------------------------
taddhitaPratipadika
    : pratipadika PLUS taddhitaPratyaya
    ;

taddhitaPratyaya
    : MATUP | INI | TVA | TAL | TARAP | TAMAP | MAYAT | TASI | TADDHITA_PRATYAYA
    ;

// ----------------------------------------------------
// 4. Tiṅantapada (Verbal Expressions & Derivations)
// ----------------------------------------------------
tingantaPada
    : dhatu (PLUS SANADI)? (PLUS VIKARANA)? PLUS lakara (PLUS tingPratyaya)?  # ExplicitTingantaDerivation
    | IDENTIFIER                                                               # SurfaceTinganta
    ;

dhatu
    : IDENTIFIER
    ;

supPratyaya
    : SUP
    ;

lakara
    : LAKARA
    ;

tingPratyaya
    : TING
    ;

// ----------------------------------------------------
// 5. Avyayapada (Invariables, Modifiers & Connectives)
// ----------------------------------------------------
avyayaPada
    : KRPAYA                                              # PrarthanaAvyaya
    | MAA                                                 # NishedhaAvyaya
    | CHA                                                 # SamuccayaAvyaya
    ;

// ----------------------------------------------------
// Lexer Tokens
// ----------------------------------------------------
HE        : 'हे' ;
CHA       : 'च' ;
MAA       : 'मा' ;
KRPAYA    : 'कृपया' ;
CONNECTIVE: 'ततः' | 'अथ' | 'अनन्तरम्' ;

PLUS      : '+' ;
COMMA     : ',' | '،' ;
COMPOUND_SEP : '-' | '—' ;
DANDA     : '\u0964' | '\u0965' | '.' ;

// Concrete Numeral & Result Leaves
NUMERAL_LEAF
    : 'शून्य' | 'शून्यम्' | 'शून्यं'
    | 'एक' | 'एकम्' | 'एकं'
    | 'द्वि' | 'द्वे'
    | 'त्रि' | 'त्रीणि'
    | 'चतुर्' | 'चत्वारि'
    | 'पञ्च' | 'षट्' | 'सप्त' | 'अष्ट' | 'नव' | 'दश'
    ;

RESULT_LEAF
    : 'फल' | 'फलम्' | 'फलं' | 'फले' | 'फलानि'
    | 'पूर्वफल' | 'पूर्वफलम्' | 'पूर्वफलं' | 'पूर्वफले' | 'पूर्वफलानि'
    ;

// Affix Classes
SANADI    : 'णिच्' | 'सँन्' | 'यँङ्' ;
VIKARANA  : 'शप्' | 'श्यन्' | 'श्नम्' | 'उ' | 'स्नु' ;

// Kṛt Affixes (Primary Verbal Suffixes)
KTA       : 'क्त' ;
KTAVATU   : 'क्तवतुँ' ;
TAVYAT    : 'तव्यत्' | 'तव्य' ;
ANIYAR    : 'अनीयर्' ;
SHATRI    : 'शतृँ' ;
SHANACH   : 'शानच्' ;
GHANJ     : 'घञ्' ;
LYUT      : 'ल्युट्' ;
NVUL      : 'ण्वुल्' ;
KTVAA     : 'क्त्वा' ;
LYAP      : 'ल्यप्' ;
TUMUN     : 'तुमुन्' ;
KRT_PRATYAYA : 'तृच्' | 'अणिन्' | 'क्यप्' | 'यत' | 'ण्यत्' ;

// Sup Case Endings
SUP       : 'सुँ' | 'औ' | 'जस्' | 'अम्' | 'औट्' | 'शस्' 
          | 'टा' | 'भ्याम्' | 'भिस्' | 'ङे' | 'भ्यस्' 
          | 'ङसिँ' | 'ओस्' | 'आम्' | 'ङि' | 'सुप्' ;

// Lakāra Moods/Tenses
LAKARA    : 'लट्' | 'लिट्' | 'लुट्' | 'लृट्' | 'लेट्' 
          | 'लोट्' | 'लङ्' | 'लिङ्' | 'लुङ्' | 'लृङ्' ;

// Tiṅ Verbal Suffixes
TING      : 'तिप्' | 'तस्' | 'झि' | 'सिप्' | 'थस्' | 'थ' 
          | 'मिप्' | 'वस्' | 'मस्' | 'त' | 'आताम्' | 'झ' 
          | 'थास्' | 'आथाम्' | 'ध्वम्' | 'इट्' | 'वहिके' | 'महिङ्' ;

// Taddhita Affixes (Secondary Nominal Suffixes)
MATUP     : 'मतुँप्' | 'वतुँप्' ;
INI       : 'इिनिँ' ;
TVA       : 'त्व' ;
TAL       : 'तल्' ;
TARAP     : 'तरप्' ;
TAMAP     : 'तमप्' ;
MAYAT     : 'मयट्' ;
TASI      : 'तसिल्' ;
TADDHITA_PRATYAYA : 'अण्' | 'इञ्' | 'ढक्' | 'यत्' | 'ठञ्' | 'छ' ;

IDENTIFIER: DEVANAGARI+ ;

fragment DEVANAGARI : [\u0900-\u0963\u0966-\u097Fa-zA-Z0-9_-] ;
WS        : [ \t\r\n]+ -> skip ;
