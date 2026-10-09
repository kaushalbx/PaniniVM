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
    : utterance (DANDA | DOUBLE_DANDA)? EOF
    ;

document
    : documentItem* (trailingHeader=prakriyaHeader | trailing=utterance)? EOF
    ;

documentItem
    : prakriyaBlock
    | prakriyaHeader DANDA
    | utterance (DANDA | DOUBLE_DANDA)
    ;

prakriyaEntry
    : prakriyaBlock EOF
    ;

rangeDeclarationEntry
    : rangeDeclaration DANDA? EOF
    ;

rangeDeclaration
    : boundary=paryantaRange ITI marker=subantaPada
    ;

scopeDeclarationEntry
    : scopeDeclaration DANDA? EOF
    ;

scopeDeclaration
    : domain=subantaPada ITI marker=subantaPada
    ;

// Explicit nominal naming declaration; lexical marker/qualifier meaning is
// validated from the parsed morphology by the AST builder.
prakriyaBlock
    : prakriyaHeader DANDA
      (body+=utterance DANDA)* body+=utterance DOUBLE_DANDA
    ;

prakriyaHeader
    : names+=subantaPada+ (ITI | NAAMA) declaration=prakriyaDeclaration
    ;

prakriyaDeclaration
    : qualifiers+=subantaPada* PRAKRIYA_NOUN PLUS markerSup=supPratyaya copula=tingantaPada
    ;

// Reusable clause content: document/block entry rules own their delimiters and EOF.
utterance
    : quotationClause
    | whileClause
    | conditionalPipelineClause
    | attributePipelineClause
    | pipelineClause
    | conditionalClause
    | sambodhana?
      vakya
      (vakyaSambandha vakya)*
    ;

quotationClause
    : quoted=vakya ITI reporting=akhyataVakya
    ;

conditionalPipelineClause
    : source=akhyataVakya (TATAH stages+=akhyataVakya)*
      TATAH conditional=conditionalExpression
    ;

attributePipelineClause
    : source+=subantaPada source+=subantaPada+
      TATAH targets+=akhyataVakya (TATAH targets+=akhyataVakya)*
    ;

whileClause
    : (limit=sankhyaAbhyasaPada YAVAT condition=vakya TAVAT body=vakya
      | YAVAT condition=vakya TAVAT boundary=ordinalAttemptBoundary body=vakya
      | YAVAT condition=vakya TAVAT body=vakya)
      (ANYATHA exhausted=whileExhausted)? (TATAH target=vakya)?
    ;

whileExhausted
    : quoted=vakya ITI reporting=vakya
    | plain=vakya
    ;

/* “up to the fifth attempt”: पञ्चमस्य प्रयत्नस्य पर्यन्तम्. */
ordinalAttemptBoundary
    : ordinal=sankhyaPuranaPada attempt=subantaPada
      PARI PLUS limitBase=IDENTIFIER PLUS SUP_AM
    ;

pipelineClause
    : (arguments+=subantaPada)+ CHA
      stages+=pipelineStage stages+=pipelineStage+
      purvaparaDirective pipelineResult tingantaPada
    | (arguments+=subantaPada)+ CHA
      stages+=pipelineStage (TATAH stages+=pipelineStage)+
      tingantaPada
    ;

pipelineStage
    : domain=subantaPada operation=subantaPada
    ;

purvaparaDirective
    : purva=subantaPada para=subantaPada
    ;

pipelineResult
    : subantaPada
    ;

conditionalClause
    : conditionalExpression (TATAH target=vakya)?
    ;

conditionalExpression
    : YADI condition=vakya TARHI consequent=conditionalArm
      (ANYATHA (nested=conditionalExpression | alternate=conditionalArm))?
    ;

conditionalArm
    : value=pratipadika
    | vakya
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
    : paryantaRange
    | subantaVakyaPada
    | avyayaPada
    ;

/*
 * Inclusive limit construction:
 *
 * एक + ङसिँ दशन् + शस् परि + अन्त + अम्
 * "from one through ten"
 *
 * The lexical identity of the final IDENTIFIER is checked by the AST builder;
 * only अन्त licenses the पर्यन्त limit relation.
 */
paryantaRange
    : lower=ablativeBoundary
      upper=accusativeBoundary
      PARI PLUS limitBase=IDENTIFIER PLUS SUP_AM
    ;

ablativeBoundary
    : ablativeNumeral
    | ablativeOrdinal
    ;

accusativeBoundary
    : accusativeNumeral
    | accusativeOrdinal
    ;

ablativeNumeral
    : (sankhyaStem PLUS)+ ablativeSup
    ;

accusativeNumeral
    : (sankhyaStem PLUS)+ accusativeSup
    ;

ablativeOrdinal
    : (sankhyaStem PLUS)+ puranaPratyaya PLUS ablativeSup
    ;

accusativeOrdinal
    : (sankhyaStem PLUS)+ puranaPratyaya PLUS accusativeSup
    ;

ablativeSup
    : SUP_NGASI
    | SUP_BHYAM
    | SUP_BHYAS
    ;

accusativeSup
    : SUP_AM
    | SUP_AUT
    | SUP_SHAS
    ;

subantaVakyaPada
    : explicitSamuccitaSubanta
    | subantaPada
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
    | ITI
    | ATHA
    | TATAH
    | ANANTARAM
    | KINTU
    | ATAH
    | YATAH
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
    | PRATYAYA_AMACH
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
    | PRAKRIYA_NOUN
    | UNA
    | ADHIKA
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
    | samjnaQualifierPratipadika
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
    | NAAMA
    | PRAKRIYA_NOUN
    | ADHIKA
    | UNA
    ;

samjnaQualifierPratipadika
    : NI PLUS TYA
    | ANTAR PLUS IDENTIFIER
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
    | MAT
    | VAT
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
    | TYAP
    | TYA
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
    | samjnaQualifierPratipadika
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

explicitSamuccitaSubanta
    : subantaPada CHA (subantaPada CHA)+
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
    | ANTAR
    ;

// ============================================================================
// तिङन्तपदम्
// ============================================================================

tingantaPada
    : upasargaKrama?
      dhatuPrakriti
      (PLUS vikarana)?
      PLUS lakara
      PLUS tingPratyaya
    ;

// ============================================================================
// विस्तृतव्युत्पत्तिः
// ============================================================================

// Derivation entry uses the same segmented verbal morphology as ordinary source.
// Augments, reduplication and substitutions are licensed by the derivation engine,
// not supplied as invented constructor calls in source text.
vyutpattiTinganta
    : tingantaPada
      EOF
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
    | NAAMA
    | NI
    | EVA
    | CHA
    | VAA
    | TU_AVYAYA
    | HI
    | KHALU
    | NANU
    | ATHA
    // TATAH is reserved as the structural sequence boundary. Allowing it here
    // lets an akhyāta greedily absorb the next stage's pre-verbal operands.
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
