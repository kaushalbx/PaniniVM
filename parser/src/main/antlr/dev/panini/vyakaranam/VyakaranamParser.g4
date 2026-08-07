parser grammar VyakaranamParser;

options {
    tokenVocab = VyakaranamLexer;
}

@header {
package dev.panini.parser;
}

// ============================================================================
// उक्तिः
// ============================================================================

ukti
    : conditionalClause
    | sambodhana?
      vakya
      (vakyaSambandha vakya)*
      DANDA?
      EOF
    ;

conditionalClause
    : YADI condition=vakya TARHI consequent=vakya (ANYATHA alternate=vakya)? DANDA? EOF
    ;

// ============================================================================
// वाक्यम्
// ============================================================================

vakya
    : akhyataVakya
    | namaVakya
    ;

/*
 * A finite sentence must contain at least one तिङन्तपदम्.
 *
 * Examples:
 *
 * राम + सुँ फल + अम् खाद् + लट् + तिप्
 * फल + अम् राम + सुँ खाद् + लट् + तिप्
 * खाद् + लट् + तिप् राम + सुँ फल + अम्
 */
akhyataVakya
    : purvaVakyaPada*
      tingantaPada
      uttaraVakyaPada*
    ;

purvaVakyaPada
    : vakyaPada
    ;

uttaraVakyaPada
    : vakyaPada
    ;

/*
 * Nominal sentence containing an understood copular verb.
 *
 * Example:
 *
 * राम + सुँ राजा + सुँ
 */
namaVakya
    : vakyaPada+
    ;

vakyaPada
    : subantaVakyaPada
    | avyayaPada
    ;

subantaVakyaPada
    : subantaPada
    | samuccitaSubanta
    | sankhyaPada
    | sankhyaPuranaPada
    | sankhyaAbhyasaPada
    | katapayadiPada
    | aryabhatiyaPada
    | bhutasamkhyaPada
    ;

vakyaSambandha
    : CHA
    | VAA
    | ATHA
    | TATAH
    | ANANTARAM
    | KINTU
    | ATAH
    | YATAH
    | DANDA
    ;

// ============================================================================
// सम्बोधनम्
// ============================================================================

sambodhana
    : sambodhanaSuchaka
      subantaPada
      COMMA?
    ;

sambodhanaSuchaka
    : HE
    | BHOH
    ;

// ============================================================================
// पदम्
// ============================================================================

pada
    : subantaPada
    | tingantaPada
    | avyayaPada
    | sankhyaPada
    ;

// ============================================================================
// संख्यापदम्
// ============================================================================

sankhyaPada
    : (sankhyaStem PLUS)+ supPratyaya
    ;

sankhyaPuranaPada
    : (sankhyaStem PLUS)+ puranaPratyaya PLUS supPratyaya
    ;

puranaPratyaya
    : THA
    | PRATYAYA_MA
    | PRATYAYA_TAMA
    | PRATYAYA_TIYA
    ;

sankhyaAbhyasaPada
    : (sankhyaStem PLUS)+ KRITVAS
    | (sankhyaStem PLUS)+ SUC
    | (sankhyaStem PLUS)+ DHAA
    ;

katapayadiPada
    : KATAPAYADI IDENTIFIER PLUS supPratyaya
    ;

aryabhatiyaPada
    : ARYABHATIYA IDENTIFIER PLUS supPratyaya
    ;

bhutasamkhyaPada
    : BHUTASAMKHYA (IDENTIFIER PLUS)+ supPratyaya
    ;

sankhyaStem
    : IDENTIFIER
    ;

// ============================================================================
// सुबन्तपदम्
// ============================================================================

subantaPada
    : pratipadika
      PLUS supPratyaya
    ;

// ============================================================================
// प्रातिपदिकम्
// ============================================================================

pratipadika
    : pratipadikaMula
      pratipadikaVikara*
    ;

pratipadikaMula
    : mulaPratipadika
    | kridantaPratipadika
    | unadyantaPratipadika
    | samasaPratipadika
    | LPAREN pratipadika RPAREN
    ;

pratipadikaVikara
    : PLUS taddhitaPratyaya
    | PLUS striPratyaya
    ;

mulaPratipadika
    : IDENTIFIER
    ;

// ============================================================================
// कृदन्तप्रातिपदिकम्
// ============================================================================

kridantaPratipadika
    : upasargaKrama?
      dhatuPrakriti
      PLUS krtPratyaya
    ;

// ============================================================================
// उणाद्यन्तप्रातिपदिकम्
// ============================================================================

unadyantaPratipadika
    : upasargaKrama?
      dhatuPrakriti
      PLUS unadiPratyaya
    ;

unadiPratyaya
    : UNADI LPAREN IDENTIFIER RPAREN
    ;

// ============================================================================
// तद्धितप्रत्ययाः
// ============================================================================

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
    | CHHA
    | KA
    | KAN
    | YAT
    | AYANA
    | IYA
    | INA
    | DAA
    | DHAA
    | TRA
    ;

// ============================================================================
// स्त्रीप्रत्ययाः
// ============================================================================

striPratyaya
    : TAAP
    | DAAP
    | CHAAP
    | NEEP
    | NEESH
    | NEEN
    | UUNG
    | TICH
    ;

// ============================================================================
// समासप्रातिपदिकम्
// ============================================================================

samasaPratipadika
    : samasaAnga
      (SAMASA_SEPARATOR samasaAnga)+
    ;

samasaAnga
    : asamasikaPratipadika
      samasaSupAvastha?
    ;

samasaSupAvastha
    : PLUS supPratyaya
      PLUS supAvastha
    ;

supAvastha
    : LUK
    | SHLU
    | LUP
    | ALUK
    ;

asamasikaPratipadika
    : asamasikaPratipadikaMula
      pratipadikaVikara*
    ;

asamasikaPratipadikaMula
    : mulaPratipadika
    | kridantaPratipadika
    | unadyantaPratipadika
    | LPAREN samasaPratipadika RPAREN
    ;

// ============================================================================
// समुच्चितसुबन्तम्
// ============================================================================

samuccitaSubanta
    : subantaPada
      (COMMA? subantaPada)+
      CHA
    ;

// ============================================================================
// धातुप्रकृतिः
// ============================================================================

dhatuPrakriti
    : dhatuMula
      (PLUS sanadiPratyaya)*
    ;

dhatuMula
    : IDENTIFIER
    | DAA
    | DHAA
    | SU
    | VAA
    ;

// ============================================================================
// सनादिप्रत्ययाः
// ============================================================================

sanadiPratyaya
    : SAN
    | NIC
    | YAN
    | YUK_SAN
    | KYACH
    | KAAMYACH
    | KYASH
    | KYANG
    ;

// ============================================================================
// उपसर्गाः
// ============================================================================

upasargaKrama
    : upasarga PLUS
      (upasarga PLUS)*
    ;

upasarga
    : PRA
    | PARAA
    | APA
    | SAM
    | ANUU
    | AVA
    | NIS
    | DUS
    | VI
    | AANG
    | NI
    | ADHI
    | API
    | ATI
    | SU
    | UD
    | ABHI
    | PRATI
    | PARI
    | UPA
    ;

// ============================================================================
// तिङन्तपदम्
// ============================================================================

tingantaPada
    : upasargaKrama?
      dhatuPrakriti
      PLUS lakara
      PLUS tingPratyaya
    ;

// ============================================================================
// विस्तृतव्युत्पत्तिः
// ============================================================================

vyutpattiTinganta
    : vyutpattiAnga
      PLUS lakara
      PLUS tingPratyaya
      EOF
    ;

vyutpattiAnga
    : vyutpattiAvayava
      (PLUS vyutpattiAvayava)*
    ;

vyutpattiAvayava
    : upasarga
    | dhatuPrakriti
    | agama
    | vikarana
    | abhyasa
    | adesham
    ;

abhyasa
    : ABHYASA LPAREN IDENTIFIER RPAREN
    ;

adesham
    : ADESHA LPAREN IDENTIFIER RPAREN
    ;

// ============================================================================
// लकाराः
// ============================================================================

lakara
    : LAT
    | LIT
    | LUT
    | LRT
    | LET
    | LOT
    | LANG
    | LIN
    | LUNG
    | LRNG
    ;

// ============================================================================
// तिङ्प्रत्ययाः
// ============================================================================

tingPratyaya
    : TIP
    | TAS
    | JHI
    | SIP
    | THAS
    | THA
    | MIP
    | VAS
    | MAS
    | TA
    | ATAAM
    | JHA
    | THAS_A
    | ATHAAM
    | DHVAM
    | IT
    | VAHI
    | MAHING
    ;

// ============================================================================
// सुप्प्रत्ययाः
// ============================================================================

supPratyaya
    : SUP_SU
    | SUP_AU
    | SUP_JAS
    | SUP_AM
    | SUP_AUT
    | SUP_SHAS
    | SUP_TA
    | SUP_BHYAM
    | SUP_BHIS
    | SUP_NGE
    | SUP_BHYAS
    | SUP_NGASI
    | SUP_NGAS
    | SUP_OS
    | SUP_AAM
    | SUP_NGI
    | SUP_SUP
    ;

// ============================================================================
// विकरणाः
// ============================================================================

vikarana
    : SHAP
    | SHYAN
    | SHNU
    | SHNAM
    | SHNA
    | U_VIKARANA
    | SHNAAM
    | YAK
    | SHAH
    | SYA
    | TAS_VIKARANA
    | CLI
    | SIC
    | ANG
    | CHANG
    | KSA
    ;

// ============================================================================
// आगमाः
// ============================================================================

agama
    : AT
    | IT
    | IIT_AGAMA
    | NUM
    | TUK
    | MUT
    | NUT
    | YASUT
    | SIYUT
    | SUK
    | RUK
    | RIK
    | PUK
    | YUK
    | VUK
    ;

// ============================================================================
// कृत्प्रत्ययाः
// ============================================================================

krtPratyaya
    : KTA
    | KTAVATU
    | TAVYAT
    | ANIYAR
    | YAT
    | NYAT
    | KYAP
    | SHATR
    | SHANACH
    | GHANJ
    | LYUT
    | NVUL
    | TRICH
    | ANIN
    | KHAL
    | KWIP
    | KTIN
    | AC
    | AP
    | KA
    | NIN
    | NINI
    | IN_KRT
    | TI_KRT
    | TRA
    | ITRA
    | ISHNUCH
    | UK
    ;

// ============================================================================
// अव्ययकृदन्तम्
// ============================================================================

avyayaKrtPratyaya
    : KTVA
    | LYAP
    | TUMUN
    | NAMUL
    | KASUN
    | KTVOS
    ;

avyayaKridanta
    : upasargaKrama?
      dhatuPrakriti
      PLUS avyayaKrtPratyaya
    ;

// ============================================================================
// अव्ययपदम्
// ============================================================================

avyayaPada
    : mulaAvyaya
    | avyayaKridanta
    | avyayaTaddhitanta
    | avyayibhavaPada
    | sankhyaAvyaya
    ;

sankhyaAvyaya
    : ADHIKA
    | UNA
    | SAKRIT
    | DVIH
    | TRIH
    | CHATUH
    | IDENTIFIER KRITVAS
    | IDENTIFIER DHAA
    | IDENTIFIER SHAH
    ;

mulaAvyaya
    : MAA
    | YAVAT
    | TAVAT
    | NA
    | ITI
    | API
    | EVA
    | CHA
    | VAA
    | TU_AVYAYA
    | HI
    | KHALU
    | NANU
    | ATHA
    | TATAH
    | ANANTARAM
    | KINTU
    | ATAH
    | YATAH
    | YATHA
    | TATHA
    | YADA
    | TADA
    | YATRA
    | TATRA
    | KADA
    | KUTRA
    | SARVATRA
    | KATHAM
    | KUTAH
    | KRPAYA
    | SAHASAA
    | SHANAIH
    | PUNAH
    | NYUNATAYA
    | ADYA
    | SHVAH
    | HYAH
    | INTERJECTION
    ;

avyayaTaddhitanta
    : mulaPratipadika
      PLUS avyayaTaddhitaPratyaya
    ;

avyayaTaddhitaPratyaya
    : TASIL
    | TRA
    | HA
    | DAA
    | THAAL
    | THAMU
    | VAT
    | DHAA
    ;

avyayibhavaPada
    : samasaPratipadika
    ;
