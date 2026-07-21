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
    : (pada | tingantaPada)+
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

coordinatedSubanta
    : subantaPada (COMMA? subantaPada)+ CHA
    ;

subantaPada
    : nominalBase PLUS supPratyaya
    | nominalBase
    ;

/**
 * Direct left recursion is supported by ANTLR 4.
 * This refactoring avoids indirect left recursion while allowing recursive
 * derivations like Stri on Compounds or Taddhita on Stri.
 */
nominalBase
    : simplePratipadika                             # simpleBase
    | kridantaPratipadika                           # kridantaBase
    | nominalBase PLUS striPratyaya                 # striBase
    | nominalBase PLUS taddhitaPratyaya             # taddhitaBase
    | nominalBase COMPOUND_SEPARATOR nominalBase    # samasaBase
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
    : (upasarga PLUS)* dhatu (PLUS vikarana)? PLUS krtPratyaya
    ;

avyayaKridantaPada
    : (upasarga PLUS)* dhatu (PLUS vikarana)? PLUS avyayaKrtPratyaya
    ;

krtPratyaya
    : KTA | KTAVATU | TAVYAT | ANIYAR | SHATR | SHANACH | GHANJ | LYUT | NVUL | TRICH | ANIN | KYAP | YAT | NYAT | KHAL | KWIP | KTIN
    ;

avyayaKrtPratyaya
    : KTVA | LYAP | TUMUN
    ;

// ====================================================
// Suffix groups
// ====================================================

striPratyaya
    : TAAP | DAAP | CHAAP | NEEP | NEESH | NEEN
    ;

taddhitaPratyaya
    : MATUP | VATUP | INI | TVA | TAL | TARAP | TAMAP | MAYAT | TASIL | AN | INJ | DHAK | THAJ | CHA_SUFFIX
    ;

// ====================================================
// Verbal expressions
// ====================================================

tingantaPada
    : (upasarga PLUS)* dhatu
      (PLUS sanadiPratyaya)*
      (PLUS vikarana)?
      PLUS lakara
      (PLUS tingPratyaya)?
    | IDENTIFIER
    ;

dhatu
    : IDENTIFIER
    ;

upasarga
    : UPASARGA
    ;

sanadiPratyaya
    : NIC | SAN | YANG
    ;

vikarana
    : SHAP | SHYAN | SHNAM | SHNU | SNU | YAK | SHAH | U_VIKARANA | SNA
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
    : MAA | NA | ITI | EVA | TATHA | KRPAYA | CHA | KIM | YATHA | KATHAM | KUTA | YADA | TADA | KADA
    ;

// ====================================================
// Lexer rules
// ====================================================

HE: 'हे' ;
CHA: 'च' ;
MAA: 'मा' ;
NA: 'न' ;
ITI: 'इति' ;
API: 'अपि' ;
EVA: 'एव' ;
TATHA: 'तथा' ;
KRPAYA: 'कृपया' ;
KIM: 'किम्' ;
YATHA: 'यथा' ;
KATHAM: 'कथम्' ;
KUTA: 'कुतः' ;
YADA: 'यदा' ;
TADA: 'तदा' ;
KADA: 'कदा' ;

CONNECTIVE: 'ततः' | 'अथ' | 'अनन्तरम्' ;
PLUS: '+' ;
COMMA: ',' | '\u060C' ;
COMPOUND_SEPARATOR: '-' | '—' ;
DANDA: '।' | '॥' | '.' ;

RESULT_REFERENCE: 'पूर्वफल' | 'फल' | 'फले' | 'फलानि' ;

NUMERAL
    : 'शून्य' | 'एक' | 'द्वि' | 'त्रि' | 'चतुर्' | 'पञ्च' | 'षट्' | 'सप्त' | 'अष्ट' | 'नव' | 'दश'
    | 'विंशति' | 'त्रिंशत्' | 'चत्वारिंशत्' | 'पञ्चाशत्' | 'षष्टि' | 'सप्तति' | 'अशीति' | 'नवति'
    | 'शत' | 'सहस्र' | 'अयुत' | 'लक्ष' | 'प्रयुत' | 'कोटि'
    ;

LAKARA: 'लट्' | 'लिट्' | 'लुट्' | 'लृट्' | 'लेट्' | 'लोट्' | 'लङ्' | 'लिङ्' | 'लुङ्' | 'लृङ्' ;

UPASARGA
    : 'प्र' | 'परा' | 'अप' | 'सम्' | 'अनु' | 'अव' | 'निस्' | 'निर्' | 'दुस्' | 'दुर्'
    | 'वि' | 'आङ्' | 'आ' | 'नि' | 'अधि' | 'अति' | 'सु' | 'उद्' | 'उत्'
    | 'अभि' | 'प्रति' | 'परि' | 'उप'
    ;

TING
    : 'तिप्' | 'तस्' | 'झि' | 'सिप्' | 'थस्' | 'थ' | 'मिप्' | 'वस्' | 'मस्'
    | 'त' | 'आताम्' | 'झ' | 'थास्' | 'आथाम्' | 'ध्वम्' | 'इट्' | 'वहि' | 'महिङ्'
    ;

SUP_SUFFIX
    : 'सुँ' | 'औ' | 'जस्' | 'अम्' | 'औट्' | 'शस्' | 'टा' | 'भ्याम्' | 'भिस्'
    | 'ङे' | 'भ्यस्' | 'ङसिँ' | 'ङस्' | 'ओस्' | 'आम्' | 'ङि' | 'सुप्'
    ;

NIC: 'णिच्' ;
SAN: 'सन्' ;
YANG: 'यङ्' ;
SHAP: 'शप्' ;
SHYAN: 'श्यन्' ;
SHNAM: 'श्नम्' ;
SHNU: 'श्नौ' ;
SNU: 'स्नु' ;
YAK: 'यक्' ;
SHAH: 'शः' ;
U_VIKARANA: 'उ' ;
SNA: 'श्ना' ;

// Stri Pratyayas
TAAP: 'टाप्' ;
DAAP: 'डाप्' ;
CHAAP: 'चाप्' ;
NEEP: 'ङीप्' ;
NEESH: 'ङीष्' ;
NEEN: 'ङीन्' ;

KTA: 'क्त' ;
KTAVATU: 'क्तवतु' ;
TAVYAT: 'तव्यत्' ;
ANIYAR: 'अनीयर' ;
SHATR: 'शतृ' ;
SHANACH: 'शानच्' ;
GHANJ: 'घञ्' ;
LYUT: 'ल्युट्' ;
NVUL: 'ण्वुल्' ;
TRICH: 'तृच्' ;
ANIN: 'अनिन' ;
KYAP: 'क्यप्' ;
YAT: 'यत्' ;
NYAT: 'ण्यत्' ;
KHAL: 'खल्' ;
KWIP: 'क्विप्' ;
KTIN: 'क्तिन्' ;

KTVA: 'क्त्वा' ;
LYAP: 'ल्याप्' ;
TUMUN: 'तुमुन्' ;
MATUP: 'मतुप्' ;
VATUP: 'वतुप्' ;
INI: 'इनि' ;
TVA: 'त्व' ;
TAL: 'तल्' ;
TARAP: 'तरप्' ;
TAMAP: 'तमप्' ;
MAYAT: 'मयट्' ;
TASIL: 'तसिल्' ;
AN: 'अण्' ;
INJ: 'इञ्' ;
DHAK: 'ढक्' ;
THAJ: 'ठञ्' ;
CHA_SUFFIX: 'छ' ;

IDENTIFIER: [a-zA-Z\u0900-\u097F]+ ;
WS: [ \t\r\n]+ -> skip ;
