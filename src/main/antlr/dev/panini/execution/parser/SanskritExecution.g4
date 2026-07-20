grammar SanskritExecution;

@header {
package dev.panini.execution.parser;
}

utterance
    : vakya DANDA? EOF
    ;

vakya
    : pada+ tingantaPada
    | pada* tingantaPada pada+
    ;

pada
    : coordinatedSubanta
    | subantaPada
    | avyayaPada
    ;

coordinatedSubanta
    : subantaPada
      (COMMA? subantaPada)*
      CHA
      subantaPada
    ;

subantaPada
    : pratipadika (PLUS supPratyaya)?
    | IDENTIFIER
    ;

tingantaPada
    : (dhatu PLUS lakara PLUS tingPratyaya)
    | IDENTIFIER
    ;

pratipadika
    : IDENTIFIER
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

avyayaPada
    : MAA
    | KRPAYA
    | CHA
    ;

PLUS
    : '+'
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

DANDA
    : '\u0964'
    | '\u0965'
    | '.'
    ;

COMMA
    : ','
    | '،'
    ;

SUP
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
    | 'ओस्'
    | 'आम्'
    | 'ङि'
    | 'सुप्'
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
    ;

IDENTIFIER
    : DEVANAGARI+
    ;

fragment DEVANAGARI
    : [\u0900-\u0963\u0966-\u097Fa-zA-Z0-9_-]
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
