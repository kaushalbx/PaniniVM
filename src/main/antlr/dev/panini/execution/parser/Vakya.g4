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
// 1. Subantapada & Direct Kāraka AST Alternatives
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
    : karmanSubanta                                       # KarmanPada
    | kartrSubanta                                        # KartrPada
    | karanaSubanta                                       # KaranaPada
    | sampradanaSubanta                                   # SampradanaPada
    | apadanaSubanta                                      # ApadanaPada
    | adhikaranaSubanta                                   # AdhikaranaPada
    | numeralSubanta                                      # NumeralSubantaPada
    | resultSubanta                                       # ResultSubantaPada
    | kridantaPratipadika (PLUS supPratyaya)?             # KridantaSubantaDerivation
    | taddhitaPratipadika (PLUS supPratyaya)?             # TaddhitaSubantaDerivation
    | basePratipadika PLUS supPratyaya                    # BaseSubantaDerivation
    | IDENTIFIER                                          # SurfaceSubanta
    ;

karmanSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_DVITIYA | DVITIYA_END)
    ;

kartrSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_TRTIYA | TRTIYA_END)
    ;

karanaSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_TRTIYA | TRTIYA_END)
    ;

sampradanaSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_CHATURTHI | CHATURTHI_END)
    ;

apadanaSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_PANCHAMI | PANCHAMI_END)
    ;

adhikaranaSubanta
    : (pratipadika | IDENTIFIER) (PLUS SUP_SAPTAMI | SAPTAMI_END)
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
    | SUP_DVITIYA
    | SUP_TRTIYA
    | SUP_CHATURTHI
    | SUP_PANCHAMI
    | SUP_SAPTAMI
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

// Vibhakti Case Endings (Lexer Suffix Tokens)
DVITIYA_END  : 'म्' | 'ं' ;
TRTIYA_END   : 'ेण' | 'ेना' | 'ैः' ;
CHATURTHI_END: 'ाय' | 'ये' | 'भ्यः' | 'मह्यम्' | 'तुभ्यम्' ;
PANCHAMI_END : 'ात्' ;
SAPTAMI_END  : 'ेषु' ;

SUP_DVITIYA  : 'अम्' | 'औट्' | 'शस्' ;
SUP_TRTIYA   : 'टा' | 'भ्याम्' | 'भिस्' ;
SUP_CHATURTHI: 'ङे' | 'भ्यस्' ;
SUP_PANCHAMI : 'ङसिँ' ;
SUP_SAPTAMI  : 'ङि' | 'सुप्' ;

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
SUP       : 'सुँ' | 'औ' | 'जस्' | 'ओस्' | 'आम्' ;

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
