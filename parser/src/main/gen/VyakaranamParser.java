// Generated from C:/Users/User/Documents/SanskritSandhi/parser/src/main/antlr/dev/panini/vyakaranam/VyakaranamParser.g4 by ANTLR 4.13.2

package dev.panini.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class VyakaranamParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLUS=1, SAMASA_SEPARATOR=2, COMMA=3, DANDA=4, LPAREN=5, RPAREN=6, HE=7, 
		BHOH=8, CHA=9, VAA=10, ATHA=11, TATAH=12, ANANTARAM=13, KINTU=14, ATAH=15, 
		YATAH=16, MAA=17, NA=18, ITI=19, API=20, EVA=21, TU_AVYAYA=22, HI=23, 
		KHALU=24, NANU=25, YATHA=26, TATHA=27, YADA=28, TADA=29, YATRA=30, TATRA=31, 
		KADA=32, KUTRA=33, SARVATRA=34, KATHAM=35, KUTAH=36, KRPAYA=37, SAHASAA=38, 
		SHANAIH=39, PUNAH=40, NYUNATAYA=41, ADYA=42, SHVAH=43, HYAH=44, ADHIKA=45, 
		UNA=46, SAKRIT=47, DVIH=48, TRIH=49, CHATUH=50, KRITVAS=51, SUC=52, KATAPAYADI=53, 
		ARYABHATIYA=54, BHUTASAMKHYA=55, INTERJECTION=56, PRA=57, PARAA=58, APA=59, 
		SAM=60, ANUU=61, AVA=62, NIS=63, DUS=64, VI=65, AANG=66, NI=67, ADHI=68, 
		ATI=69, SU=70, UD=71, ABHI=72, PRATI=73, PARI=74, UPA=75, ANTAR=76, SAN=77, 
		KYACH=78, KAAMYACH=79, KYANG=80, KYASH=81, NIC=82, YAN=83, YUK_SAN=84, 
		LAT=85, LIT=86, LUT=87, LRT=88, LET=89, LOT=90, LANG=91, LIN=92, LUNG=93, 
		LRNG=94, TIP=95, TAS=96, JHI=97, SIP=98, THAS=99, THA=100, MIP=101, VAS=102, 
		MAS=103, TA=104, ATAAM=105, JHA=106, THAS_A=107, ATHAAM=108, DHVAM=109, 
		IT=110, VAHI=111, MAHING=112, SUP_SU=113, SUP_AU=114, SUP_JAS=115, SUP_AM=116, 
		SUP_AUT=117, SUP_SHAS=118, SUP_TA=119, SUP_BHYAM=120, SUP_BHIS=121, SUP_NGE=122, 
		SUP_BHYAS=123, SUP_NGASI=124, SUP_NGAS=125, SUP_OS=126, SUP_AAM=127, SUP_NGI=128, 
		SUP_SUP=129, SHAP=130, SHYAN=131, SHNU=132, SHNAM=133, SHNA=134, U_VIKARANA=135, 
		SHNAAM=136, YAK=137, SHAH=138, SYA=139, TAS_VIKARANA=140, CLI=141, SIC=142, 
		ANG=143, CHANG=144, KSA=145, AT=146, IIT_AGAMA=147, NUM=148, TUK=149, 
		MUT=150, NUT=151, YASUT=152, SIYUT=153, SUK=154, RUK=155, RIK=156, PUK=157, 
		YUK=158, VUK=159, KTA=160, KTAVATU=161, TAVYAT=162, ANIYAR=163, YAT=164, 
		NYAT=165, KYAP=166, SHATR=167, SHANACH=168, GHANJ=169, LYUT=170, NVUL=171, 
		TRICH=172, ANIN=173, KHAL=174, KWIP=175, KTIN=176, AC=177, AP=178, KA=179, 
		NIN=180, NINI=181, IN_KRT=182, TI_KRT=183, TRA=184, ITRA=185, ISHNUCH=186, 
		UK=187, KTVA=188, LYAP=189, TUMUN=190, NAMUL=191, KASUN=192, KTVOS=193, 
		MATUP=194, VATUP=195, MAT=196, INI=197, TVA=198, TAL=199, TARAP=200, TAMAP=201, 
		MAYAT=202, PRATYAYA_MA=203, PRATYAYA_TAMA=204, PRATYAYA_TIYA=205, TASIL=206, 
		AN=207, INJ=208, DHAK=209, THAJ=210, CHHA=211, KAN=212, AYANA=213, IYA=214, 
		INA=215, TYAP=216, TYA=217, HA=218, DAA=219, THAAL=220, THAMU=221, VAT=222, 
		DHAA=223, TAAP=224, DAAP=225, CHAAP=226, NEEP=227, NEESH=228, NEEN=229, 
		UUNG=230, TICH=231, LUK=232, SHLU=233, LUP=234, ALUK=235, ABHYASA=236, 
		ADESHA=237, UNADI=238, YADI=239, TARHI=240, ANYATHA=241, YAVAT=242, TAVAT=243, 
		IDENTIFIER=244, LINE_COMMENT=245, WS=246;
	public static final int
		RULE_ukti = 0, RULE_quotationClause = 1, RULE_conditionalPipelineClause = 2, 
		RULE_attributePipelineClause = 3, RULE_whileClause = 4, RULE_whileExhausted = 5, 
		RULE_ordinalAttemptBoundary = 6, RULE_pipelineClause = 7, RULE_pipelineStage = 8, 
		RULE_purvaparaDirective = 9, RULE_pipelineResult = 10, RULE_conditionalClause = 11, 
		RULE_conditionalExpression = 12, RULE_conditionalArm = 13, RULE_vakya = 14, 
		RULE_akhyataVakya = 15, RULE_purvaVakyaPada = 16, RULE_uttaraVakyaPada = 17, 
		RULE_namaVakya = 18, RULE_vakyaPada = 19, RULE_paryantaRange = 20, RULE_ablativeNumeral = 21, 
		RULE_accusativeNumeral = 22, RULE_ablativeSup = 23, RULE_accusativeSup = 24, 
		RULE_subantaVakyaPada = 25, RULE_vakyaSambandha = 26, RULE_sambodhana = 27, 
		RULE_sambodhanaSuchaka = 28, RULE_pada = 29, RULE_sankhyaPada = 30, RULE_sankhyaPuranaPada = 31, 
		RULE_puranaPratyaya = 32, RULE_sankhyaAbhyasaPada = 33, RULE_katapayadiPada = 34, 
		RULE_aryabhatiyaPada = 35, RULE_bhutasamkhyaPada = 36, RULE_sankhyaStem = 37, 
		RULE_subantaPada = 38, RULE_pratipadika = 39, RULE_pratipadikaMula = 40, 
		RULE_pratipadikaVikara = 41, RULE_mulaPratipadika = 42, RULE_samjnaQualifierPratipadika = 43, 
		RULE_kridantaPratipadika = 44, RULE_unadyantaPratipadika = 45, RULE_unadiPratyaya = 46, 
		RULE_taddhitaPratyaya = 47, RULE_striPratyaya = 48, RULE_samasaPratipadika = 49, 
		RULE_samasaAnga = 50, RULE_samasaSupAvastha = 51, RULE_supAvastha = 52, 
		RULE_asamasikaPratipadika = 53, RULE_asamasikaPratipadikaMula = 54, RULE_samuccitaSubanta = 55, 
		RULE_dhatuPrakriti = 56, RULE_dhatuMula = 57, RULE_sanadiPratyaya = 58, 
		RULE_upasargaKrama = 59, RULE_upasarga = 60, RULE_tingantaPada = 61, RULE_vyutpattiTinganta = 62, 
		RULE_vyutpattiAnga = 63, RULE_vyutpattiAvayava = 64, RULE_abhyasa = 65, 
		RULE_adesham = 66, RULE_lakara = 67, RULE_tingPratyaya = 68, RULE_supPratyaya = 69, 
		RULE_vikarana = 70, RULE_agama = 71, RULE_krtPratyaya = 72, RULE_avyayaKrtPratyaya = 73, 
		RULE_avyayaKridanta = 74, RULE_avyayaPada = 75, RULE_sankhyaAvyaya = 76, 
		RULE_mulaAvyaya = 77, RULE_avyayaTaddhitanta = 78, RULE_avyayaTaddhitaPratyaya = 79, 
		RULE_avyayibhavaPada = 80;
	private static String[] makeRuleNames() {
		return new String[] {
			"ukti", "quotationClause", "conditionalPipelineClause", "attributePipelineClause", 
			"whileClause", "whileExhausted", "ordinalAttemptBoundary", "pipelineClause", 
			"pipelineStage", "purvaparaDirective", "pipelineResult", "conditionalClause", 
			"conditionalExpression", "conditionalArm", "vakya", "akhyataVakya", "purvaVakyaPada", 
			"uttaraVakyaPada", "namaVakya", "vakyaPada", "paryantaRange", "ablativeNumeral", 
			"accusativeNumeral", "ablativeSup", "accusativeSup", "subantaVakyaPada", 
			"vakyaSambandha", "sambodhana", "sambodhanaSuchaka", "pada", "sankhyaPada", 
			"sankhyaPuranaPada", "puranaPratyaya", "sankhyaAbhyasaPada", "katapayadiPada", 
			"aryabhatiyaPada", "bhutasamkhyaPada", "sankhyaStem", "subantaPada", 
			"pratipadika", "pratipadikaMula", "pratipadikaVikara", "mulaPratipadika", 
			"samjnaQualifierPratipadika", "kridantaPratipadika", "unadyantaPratipadika", 
			"unadiPratyaya", "taddhitaPratyaya", "striPratyaya", "samasaPratipadika", 
			"samasaAnga", "samasaSupAvastha", "supAvastha", "asamasikaPratipadika", 
			"asamasikaPratipadikaMula", "samuccitaSubanta", "dhatuPrakriti", "dhatuMula", 
			"sanadiPratyaya", "upasargaKrama", "upasarga", "tingantaPada", "vyutpattiTinganta", 
			"vyutpattiAnga", "vyutpattiAvayava", "abhyasa", "adesham", "lakara", 
			"tingPratyaya", "supPratyaya", "vikarana", "agama", "krtPratyaya", "avyayaKrtPratyaya", 
			"avyayaKridanta", "avyayaPada", "sankhyaAvyaya", "mulaAvyaya", "avyayaTaddhitanta", 
			"avyayaTaddhitaPratyaya", "avyayibhavaPada"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", null, null, null, "'('", "')'", "'\\u0939\\u0947'", "'\\u092D\\u094B\\u0903'", 
			"'\\u091A'", "'\\u0935\\u093E'", "'\\u0905\\u0925'", "'\\u0924\\u0924\\u0903'", 
			"'\\u0905\\u0928\\u0928\\u094D\\u0924\\u0930\\u092E\\u094D'", "'\\u0915\\u093F\\u0928\\u094D\\u0924\\u0941'", 
			"'\\u0905\\u0924\\u0903'", "'\\u092F\\u0924\\u0903'", "'\\u092E\\u093E'", 
			"'\\u0928'", "'\\u0907\\u0924\\u093F'", "'\\u0905\\u092A\\u093F'", "'\\u090F\\u0935'", 
			"'\\u0924\\u0941'", "'\\u0939\\u093F'", "'\\u0916\\u0932\\u0941'", "'\\u0928\\u0928\\u0941'", 
			"'\\u092F\\u0925\\u093E'", "'\\u0924\\u0925\\u093E'", "'\\u092F\\u0926\\u093E'", 
			"'\\u0924\\u0926\\u093E'", "'\\u092F\\u0924\\u094D\\u0930'", "'\\u0924\\u0924\\u094D\\u0930'", 
			"'\\u0915\\u0926\\u093E'", "'\\u0915\\u0941\\u0924\\u094D\\u0930'", "'\\u0938\\u0930\\u094D\\u0935\\u0924\\u094D\\u0930'", 
			"'\\u0915\\u0925\\u092E\\u094D'", "'\\u0915\\u0941\\u0924\\u0903'", "'\\u0915\\u0943\\u092A\\u092F\\u093E'", 
			"'\\u0938\\u0939\\u0938\\u093E'", "'\\u0936\\u0928\\u0948\\u0903'", "'\\u092A\\u0941\\u0928\\u0903'", 
			"'\\u0928\\u094D\\u092F\\u0942\\u0928\\u0924\\u092F\\u093E'", "'\\u0905\\u0926\\u094D\\u092F'", 
			"'\\u0936\\u094D\\u0935\\u0903'", "'\\u0939\\u094D\\u092F\\u0903'", "'\\u0905\\u0927\\u093F\\u0915'", 
			null, "'\\u0938\\u0915\\u0943\\u0924\\u094D'", "'\\u0926\\u094D\\u0935\\u093F\\u0903'", 
			"'\\u0924\\u094D\\u0930\\u093F\\u0903'", "'\\u091A\\u0924\\u0941\\u0903'", 
			"'\\u0915\\u0943\\u0924\\u094D\\u0935\\u0938\\u0941\\u091A\\u094D'", 
			"'\\u0938\\u0941\\u091A\\u094D'", null, null, "'\\u092D\\u0942\\u0924\\u0938\\u0919\\u094D\\u0916\\u094D\\u092F\\u093E'", 
			null, "'\\u092A\\u094D\\u0930'", "'\\u092A\\u0930\\u093E'", "'\\u0905\\u092A'", 
			"'\\u0938\\u092E\\u094D'", "'\\u0905\\u0928\\u0941'", "'\\u0905\\u0935'", 
			"'\\u0928\\u093F\\u0938\\u094D'", "'\\u0926\\u0941\\u0938\\u094D'", "'\\u0935\\u093F'", 
			"'\\u0906\\u0919\\u094D'", "'\\u0928\\u093F'", "'\\u0905\\u0927\\u093F'", 
			"'\\u0905\\u0924\\u093F'", "'\\u0938\\u0941'", "'\\u0909\\u0926\\u094D'", 
			"'\\u0905\\u092D\\u093F'", "'\\u092A\\u094D\\u0930\\u0924\\u093F'", "'\\u092A\\u0930\\u093F'", 
			"'\\u0909\\u092A'", "'\\u0905\\u0928\\u094D\\u0924\\u0930\\u094D'", "'\\u0938\\u0928\\u094D'", 
			"'\\u0915\\u094D\\u092F\\u091A\\u094D'", "'\\u0915\\u093E\\u092E\\u094D\\u092F\\u091A\\u094D'", 
			"'\\u0915\\u094D\\u092F\\u0919\\u094D'", "'\\u0915\\u094D\\u092F\\u0937\\u094D'", 
			"'\\u0923\\u093F\\u091A\\u094D'", "'\\u092F\\u0919\\u094D'", "'\\u092F\\u0919\\u094D\\u0932\\u0941\\u0915\\u094D'", 
			"'\\u0932\\u091F\\u094D'", "'\\u0932\\u093F\\u091F\\u094D'", "'\\u0932\\u0941\\u091F\\u094D'", 
			"'\\u0932\\u0943\\u091F\\u094D'", "'\\u0932\\u0947\\u091F\\u094D'", "'\\u0932\\u094B\\u091F\\u094D'", 
			"'\\u0932\\u0919\\u094D'", "'\\u0932\\u093F\\u0919\\u094D'", "'\\u0932\\u0941\\u0919\\u094D'", 
			"'\\u0932\\u0943\\u0919\\u094D'", "'\\u0924\\u093F\\u092A\\u094D'", "'\\u0924\\u0938\\u094D'", 
			"'\\u091D\\u093F'", "'\\u0938\\u093F\\u092A\\u094D'", "'\\u0925\\u0938\\u094D'", 
			"'\\u0925'", "'\\u092E\\u093F\\u092A\\u094D'", "'\\u0935\\u0938\\u094D'", 
			"'\\u092E\\u0938\\u094D'", "'\\u0924'", "'\\u0906\\u0924\\u093E\\u092E\\u094D'", 
			"'\\u091D'", "'\\u0925\\u093E\\u0938\\u094D'", "'\\u0906\\u0925\\u093E\\u092E\\u094D'", 
			"'\\u0927\\u094D\\u0935\\u092E\\u094D'", "'\\u0907\\u091F\\u094D'", "'\\u0935\\u0939\\u093F'", 
			"'\\u092E\\u0939\\u093F\\u0919\\u094D'", "'\\u0938\\u0941\\u0901'", "'\\u0914'", 
			"'\\u091C\\u0938\\u094D'", "'\\u0905\\u092E\\u094D'", "'\\u0914\\u091F\\u094D'", 
			"'\\u0936\\u0938\\u094D'", "'\\u091F\\u093E'", "'\\u092D\\u094D\\u092F\\u093E\\u092E\\u094D'", 
			"'\\u092D\\u093F\\u0938\\u094D'", "'\\u0919\\u0947'", "'\\u092D\\u094D\\u092F\\u0938\\u094D'", 
			"'\\u0919\\u0938\\u093F\\u0901'", "'\\u0919\\u0938\\u094D'", "'\\u0913\\u0938\\u094D'", 
			"'\\u0906\\u092E\\u094D'", "'\\u0919\\u093F'", "'\\u0938\\u0941\\u092A\\u094D'", 
			"'\\u0936\\u092A\\u094D'", "'\\u0936\\u094D\\u092F\\u0928\\u094D'", "'\\u0936\\u094D\\u0928\\u0941'", 
			"'\\u0936\\u094D\\u0928\\u092E\\u094D'", "'\\u0936\\u094D\\u0928\\u093E'", 
			"'\\u0909'", "'\\u0936\\u094D\\u0928\\u093E\\u092E\\u094D'", "'\\u092F\\u0915\\u094D'", 
			"'\\u0936\\u0903'", "'\\u0938\\u094D\\u092F'", "'\\u0924\\u093E\\u0938\\u094D'", 
			"'\\u091A\\u094D\\u0932\\u093F'", "'\\u0938\\u093F\\u091A\\u094D'", "'\\u0905\\u0919\\u094D'", 
			"'\\u091A\\u0919\\u094D'", "'\\u0915\\u094D\\u0938'", "'\\u0905\\u091F\\u094D'", 
			"'\\u0908\\u091F\\u094D'", "'\\u0928\\u0941\\u092E\\u094D'", "'\\u0924\\u0941\\u0915\\u094D'", 
			"'\\u092E\\u0941\\u091F\\u094D'", "'\\u0928\\u0941\\u091F\\u094D'", "'\\u092F\\u093E\\u0938\\u0941\\u091F\\u094D'", 
			"'\\u0938\\u0940\\u092F\\u0941\\u091F\\u094D'", "'\\u0938\\u0941\\u0915\\u094D'", 
			"'\\u0930\\u0941\\u0915\\u094D'", "'\\u0930\\u093F\\u0915\\u094D'", "'\\u092A\\u0941\\u0915\\u094D'", 
			"'\\u092F\\u0941\\u0915\\u094D'", "'\\u0935\\u0941\\u0915\\u094D'", "'\\u0915\\u094D\\u0924'", 
			"'\\u0915\\u094D\\u0924\\u0935\\u0924\\u0941'", "'\\u0924\\u0935\\u094D\\u092F\\u0924\\u094D'", 
			"'\\u0905\\u0928\\u0940\\u092F\\u0930\\u094D'", "'\\u092F\\u0924\\u094D'", 
			"'\\u0923\\u094D\\u092F\\u0924\\u094D'", "'\\u0915\\u094D\\u092F\\u092A\\u094D'", 
			"'\\u0936\\u0924\\u0943'", "'\\u0936\\u093E\\u0928\\u091A\\u094D'", "'\\u0918\\u091E\\u094D'", 
			"'\\u0932\\u094D\\u092F\\u0941\\u091F\\u094D'", "'\\u0923\\u094D\\u0935\\u0941\\u0932\\u094D'", 
			"'\\u0924\\u0943\\u091A\\u094D'", "'\\u0905\\u0928\\u093F\\u0928\\u094D'", 
			"'\\u0916\\u0932\\u094D'", "'\\u0915\\u094D\\u0935\\u093F\\u092A\\u094D'", 
			"'\\u0915\\u094D\\u0924\\u093F\\u0928\\u094D'", "'\\u0905\\u091A\\u094D'", 
			"'\\u0905\\u092A\\u094D'", "'\\u0915'", "'\\u0923\\u093F\\u0928\\u094D'", 
			"'\\u0923\\u093F\\u0928\\u093F'", "'\\u0907\\u0928\\u094D'", "'\\u0924\\u093F'", 
			"'\\u0924\\u094D\\u0930'", "'\\u0907\\u0924\\u094D\\u0930'", "'\\u0907\\u0937\\u094D\\u0923\\u0941\\u091A\\u094D'", 
			"'\\u0909\\u0915\\u094D'", "'\\u0915\\u094D\\u0924\\u094D\\u0935\\u093E'", 
			"'\\u0932\\u094D\\u092F\\u092A\\u094D'", "'\\u0924\\u0941\\u092E\\u0941\\u0928\\u094D'", 
			"'\\u0923\\u092E\\u0941\\u0932\\u094D'", "'\\u0915\\u0938\\u0941\\u0928\\u094D'", 
			"'\\u0915\\u094D\\u0924\\u094D\\u0935\\u094B\\u0938\\u094D'", "'\\u092E\\u0924\\u0941\\u092A\\u094D'", 
			"'\\u0935\\u0924\\u0941\\u092A\\u094D'", "'\\u092E\\u0924\\u094D'", "'\\u0907\\u0928\\u093F'", 
			"'\\u0924\\u094D\\u0935'", "'\\u0924\\u0932\\u094D'", "'\\u0924\\u0930\\u092A\\u094D'", 
			"'\\u0924\\u092E\\u092A\\u094D'", "'\\u092E\\u092F\\u091F\\u094D'", "'\\u092E'", 
			"'\\u0924\\u092E'", "'\\u0924\\u0940\\u092F'", "'\\u0924\\u0938\\u093F\\u0932\\u094D'", 
			"'\\u0905\\u0923\\u094D'", "'\\u0907\\u091E\\u094D'", "'\\u0922\\u0915\\u094D'", 
			"'\\u0920\\u091E\\u094D'", "'\\u091B'", "'\\u0915\\u0928\\u094D'", "'\\u0906\\u092F\\u0928'", 
			"'\\u0908\\u092F'", "'\\u0907\\u0928'", "'\\u0924\\u094D\\u092F\\u092A\\u094D'", 
			"'\\u0924\\u094D\\u092F'", "'\\u0939'", "'\\u0926\\u093E'", "'\\u0925\\u093E\\u0932\\u094D'", 
			"'\\u0925\\u092E\\u0941'", "'\\u0935\\u0924\\u094D'", "'\\u0927\\u093E'", 
			"'\\u091F\\u093E\\u092A\\u094D'", "'\\u0921\\u093E\\u092A\\u094D'", "'\\u091A\\u093E\\u092A\\u094D'", 
			"'\\u0919\\u0940\\u092A\\u094D'", "'\\u0919\\u0940\\u0937\\u094D'", "'\\u0919\\u0940\\u0928\\u094D'", 
			"'\\u090A\\u0919\\u094D'", "'\\u0924\\u093F\\u091A\\u094D'", "'\\u0932\\u0941\\u0915\\u094D'", 
			"'\\u0936\\u094D\\u0932\\u0941'", "'\\u0932\\u0941\\u092A\\u094D'", "'\\u0905\\u0932\\u0941\\u0915\\u094D'", 
			"'\\u0905\\u092D\\u094D\\u092F\\u093E\\u0938\\u0903'", "'\\u0906\\u0926\\u0947\\u0936\\u0903'", 
			"'\\u0909\\u0923\\u093E\\u0926\\u093F'", "'\\u092F\\u0926\\u093F'", "'\\u0924\\u0930\\u094D\\u0939\\u093F'", 
			"'\\u0905\\u0928\\u094D\\u092F\\u0925\\u093E'", "'\\u092F\\u093E\\u0935\\u0924\\u094D'", 
			"'\\u0924\\u093E\\u0935\\u0924\\u094D'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLUS", "SAMASA_SEPARATOR", "COMMA", "DANDA", "LPAREN", "RPAREN", 
			"HE", "BHOH", "CHA", "VAA", "ATHA", "TATAH", "ANANTARAM", "KINTU", "ATAH", 
			"YATAH", "MAA", "NA", "ITI", "API", "EVA", "TU_AVYAYA", "HI", "KHALU", 
			"NANU", "YATHA", "TATHA", "YADA", "TADA", "YATRA", "TATRA", "KADA", "KUTRA", 
			"SARVATRA", "KATHAM", "KUTAH", "KRPAYA", "SAHASAA", "SHANAIH", "PUNAH", 
			"NYUNATAYA", "ADYA", "SHVAH", "HYAH", "ADHIKA", "UNA", "SAKRIT", "DVIH", 
			"TRIH", "CHATUH", "KRITVAS", "SUC", "KATAPAYADI", "ARYABHATIYA", "BHUTASAMKHYA", 
			"INTERJECTION", "PRA", "PARAA", "APA", "SAM", "ANUU", "AVA", "NIS", "DUS", 
			"VI", "AANG", "NI", "ADHI", "ATI", "SU", "UD", "ABHI", "PRATI", "PARI", 
			"UPA", "ANTAR", "SAN", "KYACH", "KAAMYACH", "KYANG", "KYASH", "NIC", 
			"YAN", "YUK_SAN", "LAT", "LIT", "LUT", "LRT", "LET", "LOT", "LANG", "LIN", 
			"LUNG", "LRNG", "TIP", "TAS", "JHI", "SIP", "THAS", "THA", "MIP", "VAS", 
			"MAS", "TA", "ATAAM", "JHA", "THAS_A", "ATHAAM", "DHVAM", "IT", "VAHI", 
			"MAHING", "SUP_SU", "SUP_AU", "SUP_JAS", "SUP_AM", "SUP_AUT", "SUP_SHAS", 
			"SUP_TA", "SUP_BHYAM", "SUP_BHIS", "SUP_NGE", "SUP_BHYAS", "SUP_NGASI", 
			"SUP_NGAS", "SUP_OS", "SUP_AAM", "SUP_NGI", "SUP_SUP", "SHAP", "SHYAN", 
			"SHNU", "SHNAM", "SHNA", "U_VIKARANA", "SHNAAM", "YAK", "SHAH", "SYA", 
			"TAS_VIKARANA", "CLI", "SIC", "ANG", "CHANG", "KSA", "AT", "IIT_AGAMA", 
			"NUM", "TUK", "MUT", "NUT", "YASUT", "SIYUT", "SUK", "RUK", "RIK", "PUK", 
			"YUK", "VUK", "KTA", "KTAVATU", "TAVYAT", "ANIYAR", "YAT", "NYAT", "KYAP", 
			"SHATR", "SHANACH", "GHANJ", "LYUT", "NVUL", "TRICH", "ANIN", "KHAL", 
			"KWIP", "KTIN", "AC", "AP", "KA", "NIN", "NINI", "IN_KRT", "TI_KRT", 
			"TRA", "ITRA", "ISHNUCH", "UK", "KTVA", "LYAP", "TUMUN", "NAMUL", "KASUN", 
			"KTVOS", "MATUP", "VATUP", "MAT", "INI", "TVA", "TAL", "TARAP", "TAMAP", 
			"MAYAT", "PRATYAYA_MA", "PRATYAYA_TAMA", "PRATYAYA_TIYA", "TASIL", "AN", 
			"INJ", "DHAK", "THAJ", "CHHA", "KAN", "AYANA", "IYA", "INA", "TYAP", 
			"TYA", "HA", "DAA", "THAAL", "THAMU", "VAT", "DHAA", "TAAP", "DAAP", 
			"CHAAP", "NEEP", "NEESH", "NEEN", "UUNG", "TICH", "LUK", "SHLU", "LUP", 
			"ALUK", "ABHYASA", "ADESHA", "UNADI", "YADI", "TARHI", "ANYATHA", "YAVAT", 
			"TAVAT", "IDENTIFIER", "LINE_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "VyakaranamParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public VyakaranamParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UktiContext extends ParserRuleContext {
		public QuotationClauseContext quotationClause() {
			return getRuleContext(QuotationClauseContext.class,0);
		}
		public WhileClauseContext whileClause() {
			return getRuleContext(WhileClauseContext.class,0);
		}
		public ConditionalPipelineClauseContext conditionalPipelineClause() {
			return getRuleContext(ConditionalPipelineClauseContext.class,0);
		}
		public AttributePipelineClauseContext attributePipelineClause() {
			return getRuleContext(AttributePipelineClauseContext.class,0);
		}
		public PipelineClauseContext pipelineClause() {
			return getRuleContext(PipelineClauseContext.class,0);
		}
		public ConditionalClauseContext conditionalClause() {
			return getRuleContext(ConditionalClauseContext.class,0);
		}
		public List<VakyaContext> vakya() {
			return getRuleContexts(VakyaContext.class);
		}
		public VakyaContext vakya(int i) {
			return getRuleContext(VakyaContext.class,i);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public SambodhanaContext sambodhana() {
			return getRuleContext(SambodhanaContext.class,0);
		}
		public List<VakyaSambandhaContext> vakyaSambandha() {
			return getRuleContexts(VakyaSambandhaContext.class);
		}
		public VakyaSambandhaContext vakyaSambandha(int i) {
			return getRuleContext(VakyaSambandhaContext.class,i);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public UktiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ukti; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUkti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUkti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUkti(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UktiContext ukti() throws RecognitionException {
		UktiContext _localctx = new UktiContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_ukti);
		int _la;
		try {
			int _alt;
			setState(185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(162);
				quotationClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(163);
				whileClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(164);
				conditionalPipelineClause();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(165);
				attributePipelineClause();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(166);
				pipelineClause();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(167);
				conditionalClause();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(169);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HE || _la==BHOH) {
					{
					setState(168);
					sambodhana();
					}
				}

				setState(171);
				vakya();
				setState(177);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(172);
						vakyaSambandha();
						setState(173);
						vakya();
						}
						} 
					}
					setState(179);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DANDA) {
					{
					setState(180);
					match(DANDA);
					}
				}

				setState(183);
				match(EOF);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QuotationClauseContext extends ParserRuleContext {
		public VakyaContext quoted;
		public AkhyataVakyaContext reporting;
		public TerminalNode ITI() { return getToken(VyakaranamParser.ITI, 0); }
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public VakyaContext vakya() {
			return getRuleContext(VakyaContext.class,0);
		}
		public AkhyataVakyaContext akhyataVakya() {
			return getRuleContext(AkhyataVakyaContext.class,0);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public QuotationClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quotationClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterQuotationClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitQuotationClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitQuotationClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuotationClauseContext quotationClause() throws RecognitionException {
		QuotationClauseContext _localctx = new QuotationClauseContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_quotationClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			((QuotationClauseContext)_localctx).quoted = vakya();
			setState(188);
			match(ITI);
			setState(189);
			((QuotationClauseContext)_localctx).reporting = akhyataVakya();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(190);
				match(DANDA);
				}
			}

			setState(193);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalPipelineClauseContext extends ParserRuleContext {
		public AkhyataVakyaContext source;
		public AkhyataVakyaContext akhyataVakya;
		public List<AkhyataVakyaContext> stages = new ArrayList<AkhyataVakyaContext>();
		public ConditionalExpressionContext conditional;
		public List<TerminalNode> TATAH() { return getTokens(VyakaranamParser.TATAH); }
		public TerminalNode TATAH(int i) {
			return getToken(VyakaranamParser.TATAH, i);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public List<AkhyataVakyaContext> akhyataVakya() {
			return getRuleContexts(AkhyataVakyaContext.class);
		}
		public AkhyataVakyaContext akhyataVakya(int i) {
			return getRuleContext(AkhyataVakyaContext.class,i);
		}
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public ConditionalPipelineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalPipelineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterConditionalPipelineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitConditionalPipelineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitConditionalPipelineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalPipelineClauseContext conditionalPipelineClause() throws RecognitionException {
		ConditionalPipelineClauseContext _localctx = new ConditionalPipelineClauseContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_conditionalPipelineClause);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			((ConditionalPipelineClauseContext)_localctx).source = akhyataVakya();
			setState(200);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(196);
					match(TATAH);
					setState(197);
					((ConditionalPipelineClauseContext)_localctx).akhyataVakya = akhyataVakya();
					((ConditionalPipelineClauseContext)_localctx).stages.add(((ConditionalPipelineClauseContext)_localctx).akhyataVakya);
					}
					} 
				}
				setState(202);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(203);
			match(TATAH);
			setState(204);
			((ConditionalPipelineClauseContext)_localctx).conditional = conditionalExpression();
			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(205);
				match(DANDA);
				}
			}

			setState(208);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributePipelineClauseContext extends ParserRuleContext {
		public SubantaPadaContext subantaPada;
		public List<SubantaPadaContext> source = new ArrayList<SubantaPadaContext>();
		public AkhyataVakyaContext akhyataVakya;
		public List<AkhyataVakyaContext> targets = new ArrayList<AkhyataVakyaContext>();
		public List<TerminalNode> TATAH() { return getTokens(VyakaranamParser.TATAH); }
		public TerminalNode TATAH(int i) {
			return getToken(VyakaranamParser.TATAH, i);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public List<SubantaPadaContext> subantaPada() {
			return getRuleContexts(SubantaPadaContext.class);
		}
		public SubantaPadaContext subantaPada(int i) {
			return getRuleContext(SubantaPadaContext.class,i);
		}
		public List<AkhyataVakyaContext> akhyataVakya() {
			return getRuleContexts(AkhyataVakyaContext.class);
		}
		public AkhyataVakyaContext akhyataVakya(int i) {
			return getRuleContext(AkhyataVakyaContext.class,i);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public AttributePipelineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributePipelineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAttributePipelineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAttributePipelineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAttributePipelineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributePipelineClauseContext attributePipelineClause() throws RecognitionException {
		AttributePipelineClauseContext _localctx = new AttributePipelineClauseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_attributePipelineClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			((AttributePipelineClauseContext)_localctx).subantaPada = subantaPada();
			((AttributePipelineClauseContext)_localctx).source.add(((AttributePipelineClauseContext)_localctx).subantaPada);
			setState(212); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(211);
				((AttributePipelineClauseContext)_localctx).subantaPada = subantaPada();
				((AttributePipelineClauseContext)_localctx).source.add(((AttributePipelineClauseContext)_localctx).subantaPada);
				}
				}
				setState(214); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -144009634958539744L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8191L) != 0) || ((((_la - 219)) & ~0x3f) == 0 && ((1L << (_la - 219)) & 33554449L) != 0) );
			setState(216);
			match(TATAH);
			setState(217);
			((AttributePipelineClauseContext)_localctx).akhyataVakya = akhyataVakya();
			((AttributePipelineClauseContext)_localctx).targets.add(((AttributePipelineClauseContext)_localctx).akhyataVakya);
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TATAH) {
				{
				{
				setState(218);
				match(TATAH);
				setState(219);
				((AttributePipelineClauseContext)_localctx).akhyataVakya = akhyataVakya();
				((AttributePipelineClauseContext)_localctx).targets.add(((AttributePipelineClauseContext)_localctx).akhyataVakya);
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(225);
				match(DANDA);
				}
			}

			setState(228);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileClauseContext extends ParserRuleContext {
		public SankhyaAbhyasaPadaContext limit;
		public VakyaContext condition;
		public VakyaContext body;
		public OrdinalAttemptBoundaryContext boundary;
		public WhileExhaustedContext exhausted;
		public VakyaContext target;
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public TerminalNode YAVAT() { return getToken(VyakaranamParser.YAVAT, 0); }
		public TerminalNode TAVAT() { return getToken(VyakaranamParser.TAVAT, 0); }
		public SankhyaAbhyasaPadaContext sankhyaAbhyasaPada() {
			return getRuleContext(SankhyaAbhyasaPadaContext.class,0);
		}
		public List<VakyaContext> vakya() {
			return getRuleContexts(VakyaContext.class);
		}
		public VakyaContext vakya(int i) {
			return getRuleContext(VakyaContext.class,i);
		}
		public OrdinalAttemptBoundaryContext ordinalAttemptBoundary() {
			return getRuleContext(OrdinalAttemptBoundaryContext.class,0);
		}
		public TerminalNode ANYATHA() { return getToken(VyakaranamParser.ANYATHA, 0); }
		public TerminalNode TATAH() { return getToken(VyakaranamParser.TATAH, 0); }
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public WhileExhaustedContext whileExhausted() {
			return getRuleContext(WhileExhaustedContext.class,0);
		}
		public WhileClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterWhileClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitWhileClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitWhileClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileClauseContext whileClause() throws RecognitionException {
		WhileClauseContext _localctx = new WhileClauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_whileClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(230);
				((WhileClauseContext)_localctx).limit = sankhyaAbhyasaPada();
				setState(231);
				match(YAVAT);
				setState(232);
				((WhileClauseContext)_localctx).condition = vakya();
				setState(233);
				match(TAVAT);
				setState(234);
				((WhileClauseContext)_localctx).body = vakya();
				}
				break;
			case 2:
				{
				setState(236);
				match(YAVAT);
				setState(237);
				((WhileClauseContext)_localctx).condition = vakya();
				setState(238);
				match(TAVAT);
				setState(239);
				((WhileClauseContext)_localctx).boundary = ordinalAttemptBoundary();
				setState(240);
				((WhileClauseContext)_localctx).body = vakya();
				}
				break;
			case 3:
				{
				setState(242);
				match(YAVAT);
				setState(243);
				((WhileClauseContext)_localctx).condition = vakya();
				setState(244);
				match(TAVAT);
				setState(245);
				((WhileClauseContext)_localctx).body = vakya();
				}
				break;
			}
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ANYATHA) {
				{
				setState(249);
				match(ANYATHA);
				setState(250);
				((WhileClauseContext)_localctx).exhausted = whileExhausted();
				}
			}

			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TATAH) {
				{
				setState(253);
				match(TATAH);
				setState(254);
				((WhileClauseContext)_localctx).target = vakya();
				}
			}

			setState(258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(257);
				match(DANDA);
				}
			}

			setState(260);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileExhaustedContext extends ParserRuleContext {
		public VakyaContext quoted;
		public VakyaContext reporting;
		public VakyaContext plain;
		public TerminalNode ITI() { return getToken(VyakaranamParser.ITI, 0); }
		public List<VakyaContext> vakya() {
			return getRuleContexts(VakyaContext.class);
		}
		public VakyaContext vakya(int i) {
			return getRuleContext(VakyaContext.class,i);
		}
		public WhileExhaustedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileExhausted; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterWhileExhausted(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitWhileExhausted(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitWhileExhausted(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileExhaustedContext whileExhausted() throws RecognitionException {
		WhileExhaustedContext _localctx = new WhileExhaustedContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_whileExhausted);
		try {
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(262);
				((WhileExhaustedContext)_localctx).quoted = vakya();
				setState(263);
				match(ITI);
				setState(264);
				((WhileExhaustedContext)_localctx).reporting = vakya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(266);
				((WhileExhaustedContext)_localctx).plain = vakya();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrdinalAttemptBoundaryContext extends ParserRuleContext {
		public SankhyaPuranaPadaContext ordinal;
		public SubantaPadaContext attempt;
		public Token limitBase;
		public TerminalNode PARI() { return getToken(VyakaranamParser.PARI, 0); }
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public TerminalNode SUP_AM() { return getToken(VyakaranamParser.SUP_AM, 0); }
		public SankhyaPuranaPadaContext sankhyaPuranaPada() {
			return getRuleContext(SankhyaPuranaPadaContext.class,0);
		}
		public SubantaPadaContext subantaPada() {
			return getRuleContext(SubantaPadaContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public OrdinalAttemptBoundaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ordinalAttemptBoundary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterOrdinalAttemptBoundary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitOrdinalAttemptBoundary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitOrdinalAttemptBoundary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrdinalAttemptBoundaryContext ordinalAttemptBoundary() throws RecognitionException {
		OrdinalAttemptBoundaryContext _localctx = new OrdinalAttemptBoundaryContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_ordinalAttemptBoundary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			((OrdinalAttemptBoundaryContext)_localctx).ordinal = sankhyaPuranaPada();
			setState(270);
			((OrdinalAttemptBoundaryContext)_localctx).attempt = subantaPada();
			setState(271);
			match(PARI);
			setState(272);
			match(PLUS);
			setState(273);
			((OrdinalAttemptBoundaryContext)_localctx).limitBase = match(IDENTIFIER);
			setState(274);
			match(PLUS);
			setState(275);
			match(SUP_AM);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PipelineClauseContext extends ParserRuleContext {
		public SubantaPadaContext subantaPada;
		public List<SubantaPadaContext> arguments = new ArrayList<SubantaPadaContext>();
		public PipelineStageContext pipelineStage;
		public List<PipelineStageContext> stages = new ArrayList<PipelineStageContext>();
		public TerminalNode CHA() { return getToken(VyakaranamParser.CHA, 0); }
		public PurvaparaDirectiveContext purvaparaDirective() {
			return getRuleContext(PurvaparaDirectiveContext.class,0);
		}
		public PipelineResultContext pipelineResult() {
			return getRuleContext(PipelineResultContext.class,0);
		}
		public TingantaPadaContext tingantaPada() {
			return getRuleContext(TingantaPadaContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public List<PipelineStageContext> pipelineStage() {
			return getRuleContexts(PipelineStageContext.class);
		}
		public PipelineStageContext pipelineStage(int i) {
			return getRuleContext(PipelineStageContext.class,i);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public List<SubantaPadaContext> subantaPada() {
			return getRuleContexts(SubantaPadaContext.class);
		}
		public SubantaPadaContext subantaPada(int i) {
			return getRuleContext(SubantaPadaContext.class,i);
		}
		public PipelineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipelineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPipelineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPipelineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPipelineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipelineClauseContext pipelineClause() throws RecognitionException {
		PipelineClauseContext _localctx = new PipelineClauseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_pipelineClause);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(278); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(277);
				((PipelineClauseContext)_localctx).subantaPada = subantaPada();
				((PipelineClauseContext)_localctx).arguments.add(((PipelineClauseContext)_localctx).subantaPada);
				}
				}
				setState(280); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -144009634958539744L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8191L) != 0) || ((((_la - 219)) & ~0x3f) == 0 && ((1L << (_la - 219)) & 33554449L) != 0) );
			setState(282);
			match(CHA);
			setState(283);
			((PipelineClauseContext)_localctx).pipelineStage = pipelineStage();
			((PipelineClauseContext)_localctx).stages.add(((PipelineClauseContext)_localctx).pipelineStage);
			setState(285); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(284);
					((PipelineClauseContext)_localctx).pipelineStage = pipelineStage();
					((PipelineClauseContext)_localctx).stages.add(((PipelineClauseContext)_localctx).pipelineStage);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(287); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(289);
			purvaparaDirective();
			setState(290);
			pipelineResult();
			setState(291);
			tingantaPada();
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(292);
				match(DANDA);
				}
			}

			setState(295);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PipelineStageContext extends ParserRuleContext {
		public SubantaPadaContext domain;
		public SubantaPadaContext operation;
		public List<SubantaPadaContext> subantaPada() {
			return getRuleContexts(SubantaPadaContext.class);
		}
		public SubantaPadaContext subantaPada(int i) {
			return getRuleContext(SubantaPadaContext.class,i);
		}
		public PipelineStageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipelineStage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPipelineStage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPipelineStage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPipelineStage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipelineStageContext pipelineStage() throws RecognitionException {
		PipelineStageContext _localctx = new PipelineStageContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_pipelineStage);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			((PipelineStageContext)_localctx).domain = subantaPada();
			setState(298);
			((PipelineStageContext)_localctx).operation = subantaPada();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PurvaparaDirectiveContext extends ParserRuleContext {
		public SubantaPadaContext purva;
		public SubantaPadaContext para;
		public List<SubantaPadaContext> subantaPada() {
			return getRuleContexts(SubantaPadaContext.class);
		}
		public SubantaPadaContext subantaPada(int i) {
			return getRuleContext(SubantaPadaContext.class,i);
		}
		public PurvaparaDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_purvaparaDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPurvaparaDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPurvaparaDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPurvaparaDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PurvaparaDirectiveContext purvaparaDirective() throws RecognitionException {
		PurvaparaDirectiveContext _localctx = new PurvaparaDirectiveContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_purvaparaDirective);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			((PurvaparaDirectiveContext)_localctx).purva = subantaPada();
			setState(301);
			((PurvaparaDirectiveContext)_localctx).para = subantaPada();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PipelineResultContext extends ParserRuleContext {
		public SubantaPadaContext subantaPada() {
			return getRuleContext(SubantaPadaContext.class,0);
		}
		public PipelineResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipelineResult; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPipelineResult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPipelineResult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPipelineResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipelineResultContext pipelineResult() throws RecognitionException {
		PipelineResultContext _localctx = new PipelineResultContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pipelineResult);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			subantaPada();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalClauseContext extends ParserRuleContext {
		public VakyaContext target;
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public TerminalNode TATAH() { return getToken(VyakaranamParser.TATAH, 0); }
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public VakyaContext vakya() {
			return getRuleContext(VakyaContext.class,0);
		}
		public ConditionalClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterConditionalClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitConditionalClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitConditionalClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalClauseContext conditionalClause() throws RecognitionException {
		ConditionalClauseContext _localctx = new ConditionalClauseContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_conditionalClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			conditionalExpression();
			setState(308);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TATAH) {
				{
				setState(306);
				match(TATAH);
				setState(307);
				((ConditionalClauseContext)_localctx).target = vakya();
				}
			}

			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(310);
				match(DANDA);
				}
			}

			setState(313);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalExpressionContext extends ParserRuleContext {
		public VakyaContext condition;
		public ConditionalArmContext consequent;
		public ConditionalExpressionContext nested;
		public ConditionalArmContext alternate;
		public TerminalNode YADI() { return getToken(VyakaranamParser.YADI, 0); }
		public TerminalNode TARHI() { return getToken(VyakaranamParser.TARHI, 0); }
		public VakyaContext vakya() {
			return getRuleContext(VakyaContext.class,0);
		}
		public List<ConditionalArmContext> conditionalArm() {
			return getRuleContexts(ConditionalArmContext.class);
		}
		public ConditionalArmContext conditionalArm(int i) {
			return getRuleContext(ConditionalArmContext.class,i);
		}
		public TerminalNode ANYATHA() { return getToken(VyakaranamParser.ANYATHA, 0); }
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterConditionalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitConditionalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitConditionalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
		ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_conditionalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(YADI);
			setState(316);
			((ConditionalExpressionContext)_localctx).condition = vakya();
			setState(317);
			match(TARHI);
			setState(318);
			((ConditionalExpressionContext)_localctx).consequent = conditionalArm();
			setState(324);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ANYATHA) {
				{
				setState(319);
				match(ANYATHA);
				setState(322);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case YADI:
					{
					setState(320);
					((ConditionalExpressionContext)_localctx).nested = conditionalExpression();
					}
					break;
				case LPAREN:
				case CHA:
				case VAA:
				case ATHA:
				case TATAH:
				case ANANTARAM:
				case KINTU:
				case ATAH:
				case YATAH:
				case MAA:
				case NA:
				case ITI:
				case API:
				case EVA:
				case TU_AVYAYA:
				case HI:
				case KHALU:
				case NANU:
				case YATHA:
				case TATHA:
				case YADA:
				case TADA:
				case YATRA:
				case TATRA:
				case KADA:
				case KUTRA:
				case SARVATRA:
				case KATHAM:
				case KUTAH:
				case KRPAYA:
				case SAHASAA:
				case SHANAIH:
				case PUNAH:
				case NYUNATAYA:
				case ADYA:
				case SHVAH:
				case HYAH:
				case ADHIKA:
				case UNA:
				case SAKRIT:
				case DVIH:
				case TRIH:
				case CHATUH:
				case KATAPAYADI:
				case ARYABHATIYA:
				case BHUTASAMKHYA:
				case INTERJECTION:
				case PRA:
				case PARAA:
				case APA:
				case SAM:
				case ANUU:
				case AVA:
				case NIS:
				case DUS:
				case VI:
				case AANG:
				case NI:
				case ADHI:
				case ATI:
				case SU:
				case UD:
				case ABHI:
				case PRATI:
				case PARI:
				case UPA:
				case ANTAR:
				case DAA:
				case DHAA:
				case YAVAT:
				case TAVAT:
				case IDENTIFIER:
					{
					setState(321);
					((ConditionalExpressionContext)_localctx).alternate = conditionalArm();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalArmContext extends ParserRuleContext {
		public PratipadikaContext value;
		public PratipadikaContext pratipadika() {
			return getRuleContext(PratipadikaContext.class,0);
		}
		public VakyaContext vakya() {
			return getRuleContext(VakyaContext.class,0);
		}
		public ConditionalArmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalArm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterConditionalArm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitConditionalArm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitConditionalArm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalArmContext conditionalArm() throws RecognitionException {
		ConditionalArmContext _localctx = new ConditionalArmContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_conditionalArm);
		try {
			setState(328);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(326);
				((ConditionalArmContext)_localctx).value = pratipadika();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(327);
				vakya();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VakyaContext extends ParserRuleContext {
		public AkhyataVakyaContext akhyataVakya() {
			return getRuleContext(AkhyataVakyaContext.class,0);
		}
		public NamaVakyaContext namaVakya() {
			return getRuleContext(NamaVakyaContext.class,0);
		}
		public VakyaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vakya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVakya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVakya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVakya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VakyaContext vakya() throws RecognitionException {
		VakyaContext _localctx = new VakyaContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_vakya);
		try {
			setState(332);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(330);
				akhyataVakya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331);
				namaVakya();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AkhyataVakyaContext extends ParserRuleContext {
		public TingantaPadaContext tingantaPada() {
			return getRuleContext(TingantaPadaContext.class,0);
		}
		public List<PurvaVakyaPadaContext> purvaVakyaPada() {
			return getRuleContexts(PurvaVakyaPadaContext.class);
		}
		public PurvaVakyaPadaContext purvaVakyaPada(int i) {
			return getRuleContext(PurvaVakyaPadaContext.class,i);
		}
		public List<UttaraVakyaPadaContext> uttaraVakyaPada() {
			return getRuleContexts(UttaraVakyaPadaContext.class);
		}
		public UttaraVakyaPadaContext uttaraVakyaPada(int i) {
			return getRuleContext(UttaraVakyaPadaContext.class,i);
		}
		public AkhyataVakyaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_akhyataVakya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAkhyataVakya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAkhyataVakya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAkhyataVakya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AkhyataVakyaContext akhyataVakya() throws RecognitionException {
		AkhyataVakyaContext _localctx = new AkhyataVakyaContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_akhyataVakya);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(334);
					purvaVakyaPada();
					}
					} 
				}
				setState(339);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			setState(340);
			tingantaPada();
			setState(344);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(341);
					uttaraVakyaPada();
					}
					} 
				}
				setState(346);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PurvaVakyaPadaContext extends ParserRuleContext {
		public VakyaPadaContext vakyaPada() {
			return getRuleContext(VakyaPadaContext.class,0);
		}
		public PurvaVakyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_purvaVakyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPurvaVakyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPurvaVakyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPurvaVakyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PurvaVakyaPadaContext purvaVakyaPada() throws RecognitionException {
		PurvaVakyaPadaContext _localctx = new PurvaVakyaPadaContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_purvaVakyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			vakyaPada();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UttaraVakyaPadaContext extends ParserRuleContext {
		public VakyaPadaContext vakyaPada() {
			return getRuleContext(VakyaPadaContext.class,0);
		}
		public UttaraVakyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uttaraVakyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUttaraVakyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUttaraVakyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUttaraVakyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UttaraVakyaPadaContext uttaraVakyaPada() throws RecognitionException {
		UttaraVakyaPadaContext _localctx = new UttaraVakyaPadaContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_uttaraVakyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			vakyaPada();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamaVakyaContext extends ParserRuleContext {
		public List<VakyaPadaContext> vakyaPada() {
			return getRuleContexts(VakyaPadaContext.class);
		}
		public VakyaPadaContext vakyaPada(int i) {
			return getRuleContext(VakyaPadaContext.class,i);
		}
		public NamaVakyaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namaVakya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterNamaVakya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitNamaVakya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitNamaVakya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamaVakyaContext namaVakya() throws RecognitionException {
		NamaVakyaContext _localctx = new NamaVakyaContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_namaVakya);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(352); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(351);
					vakyaPada();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(354); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VakyaPadaContext extends ParserRuleContext {
		public ParyantaRangeContext paryantaRange() {
			return getRuleContext(ParyantaRangeContext.class,0);
		}
		public SubantaVakyaPadaContext subantaVakyaPada() {
			return getRuleContext(SubantaVakyaPadaContext.class,0);
		}
		public AvyayaPadaContext avyayaPada() {
			return getRuleContext(AvyayaPadaContext.class,0);
		}
		public VakyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vakyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVakyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVakyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVakyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VakyaPadaContext vakyaPada() throws RecognitionException {
		VakyaPadaContext _localctx = new VakyaPadaContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_vakyaPada);
		try {
			setState(359);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(356);
				paryantaRange();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(357);
				subantaVakyaPada();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(358);
				avyayaPada();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParyantaRangeContext extends ParserRuleContext {
		public AblativeNumeralContext lower;
		public AccusativeNumeralContext upper;
		public Token limitBase;
		public TerminalNode PARI() { return getToken(VyakaranamParser.PARI, 0); }
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public TerminalNode SUP_AM() { return getToken(VyakaranamParser.SUP_AM, 0); }
		public AblativeNumeralContext ablativeNumeral() {
			return getRuleContext(AblativeNumeralContext.class,0);
		}
		public AccusativeNumeralContext accusativeNumeral() {
			return getRuleContext(AccusativeNumeralContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public ParyantaRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paryantaRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterParyantaRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitParyantaRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitParyantaRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParyantaRangeContext paryantaRange() throws RecognitionException {
		ParyantaRangeContext _localctx = new ParyantaRangeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_paryantaRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			((ParyantaRangeContext)_localctx).lower = ablativeNumeral();
			setState(362);
			((ParyantaRangeContext)_localctx).upper = accusativeNumeral();
			setState(363);
			match(PARI);
			setState(364);
			match(PLUS);
			setState(365);
			((ParyantaRangeContext)_localctx).limitBase = match(IDENTIFIER);
			setState(366);
			match(PLUS);
			setState(367);
			match(SUP_AM);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AblativeNumeralContext extends ParserRuleContext {
		public AblativeSupContext ablativeSup() {
			return getRuleContext(AblativeSupContext.class,0);
		}
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public AblativeNumeralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ablativeNumeral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAblativeNumeral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAblativeNumeral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAblativeNumeral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AblativeNumeralContext ablativeNumeral() throws RecognitionException {
		AblativeNumeralContext _localctx = new AblativeNumeralContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_ablativeNumeral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(369);
				sankhyaStem();
				setState(370);
				match(PLUS);
				}
				}
				setState(374); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(376);
			ablativeSup();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AccusativeNumeralContext extends ParserRuleContext {
		public AccusativeSupContext accusativeSup() {
			return getRuleContext(AccusativeSupContext.class,0);
		}
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public AccusativeNumeralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accusativeNumeral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAccusativeNumeral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAccusativeNumeral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAccusativeNumeral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccusativeNumeralContext accusativeNumeral() throws RecognitionException {
		AccusativeNumeralContext _localctx = new AccusativeNumeralContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_accusativeNumeral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(381); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(378);
				sankhyaStem();
				setState(379);
				match(PLUS);
				}
				}
				setState(383); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(385);
			accusativeSup();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AblativeSupContext extends ParserRuleContext {
		public TerminalNode SUP_NGASI() { return getToken(VyakaranamParser.SUP_NGASI, 0); }
		public TerminalNode SUP_BHYAM() { return getToken(VyakaranamParser.SUP_BHYAM, 0); }
		public TerminalNode SUP_BHYAS() { return getToken(VyakaranamParser.SUP_BHYAS, 0); }
		public AblativeSupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ablativeSup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAblativeSup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAblativeSup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAblativeSup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AblativeSupContext ablativeSup() throws RecognitionException {
		AblativeSupContext _localctx = new AblativeSupContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_ablativeSup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			_la = _input.LA(1);
			if ( !(((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 25L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AccusativeSupContext extends ParserRuleContext {
		public TerminalNode SUP_AM() { return getToken(VyakaranamParser.SUP_AM, 0); }
		public TerminalNode SUP_AUT() { return getToken(VyakaranamParser.SUP_AUT, 0); }
		public TerminalNode SUP_SHAS() { return getToken(VyakaranamParser.SUP_SHAS, 0); }
		public AccusativeSupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accusativeSup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAccusativeSup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAccusativeSup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAccusativeSup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccusativeSupContext accusativeSup() throws RecognitionException {
		AccusativeSupContext _localctx = new AccusativeSupContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_accusativeSup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			_la = _input.LA(1);
			if ( !(((((_la - 116)) & ~0x3f) == 0 && ((1L << (_la - 116)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubantaVakyaPadaContext extends ParserRuleContext {
		public SubantaPadaContext subantaPada() {
			return getRuleContext(SubantaPadaContext.class,0);
		}
		public SamuccitaSubantaContext samuccitaSubanta() {
			return getRuleContext(SamuccitaSubantaContext.class,0);
		}
		public SankhyaPadaContext sankhyaPada() {
			return getRuleContext(SankhyaPadaContext.class,0);
		}
		public SankhyaPuranaPadaContext sankhyaPuranaPada() {
			return getRuleContext(SankhyaPuranaPadaContext.class,0);
		}
		public SankhyaAbhyasaPadaContext sankhyaAbhyasaPada() {
			return getRuleContext(SankhyaAbhyasaPadaContext.class,0);
		}
		public KatapayadiPadaContext katapayadiPada() {
			return getRuleContext(KatapayadiPadaContext.class,0);
		}
		public AryabhatiyaPadaContext aryabhatiyaPada() {
			return getRuleContext(AryabhatiyaPadaContext.class,0);
		}
		public BhutasamkhyaPadaContext bhutasamkhyaPada() {
			return getRuleContext(BhutasamkhyaPadaContext.class,0);
		}
		public SubantaVakyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subantaVakyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSubantaVakyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSubantaVakyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSubantaVakyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubantaVakyaPadaContext subantaVakyaPada() throws RecognitionException {
		SubantaVakyaPadaContext _localctx = new SubantaVakyaPadaContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_subantaVakyaPada);
		try {
			setState(399);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(391);
				subantaPada();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(392);
				samuccitaSubanta();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(393);
				sankhyaPada();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(394);
				sankhyaPuranaPada();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(395);
				sankhyaAbhyasaPada();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(396);
				katapayadiPada();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(397);
				aryabhatiyaPada();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(398);
				bhutasamkhyaPada();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VakyaSambandhaContext extends ParserRuleContext {
		public TerminalNode CHA() { return getToken(VyakaranamParser.CHA, 0); }
		public TerminalNode VAA() { return getToken(VyakaranamParser.VAA, 0); }
		public TerminalNode ITI() { return getToken(VyakaranamParser.ITI, 0); }
		public TerminalNode ATHA() { return getToken(VyakaranamParser.ATHA, 0); }
		public TerminalNode TATAH() { return getToken(VyakaranamParser.TATAH, 0); }
		public TerminalNode ANANTARAM() { return getToken(VyakaranamParser.ANANTARAM, 0); }
		public TerminalNode KINTU() { return getToken(VyakaranamParser.KINTU, 0); }
		public TerminalNode ATAH() { return getToken(VyakaranamParser.ATAH, 0); }
		public TerminalNode YATAH() { return getToken(VyakaranamParser.YATAH, 0); }
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public VakyaSambandhaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vakyaSambandha; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVakyaSambandha(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVakyaSambandha(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVakyaSambandha(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VakyaSambandhaContext vakyaSambandha() throws RecognitionException {
		VakyaSambandhaContext _localctx = new VakyaSambandhaContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_vakyaSambandha);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 654864L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SambodhanaContext extends ParserRuleContext {
		public SambodhanaSuchakaContext sambodhanaSuchaka() {
			return getRuleContext(SambodhanaSuchakaContext.class,0);
		}
		public SubantaPadaContext subantaPada() {
			return getRuleContext(SubantaPadaContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(VyakaranamParser.COMMA, 0); }
		public SambodhanaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sambodhana; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSambodhana(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSambodhana(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSambodhana(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SambodhanaContext sambodhana() throws RecognitionException {
		SambodhanaContext _localctx = new SambodhanaContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_sambodhana);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			sambodhanaSuchaka();
			setState(404);
			subantaPada();
			setState(406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(405);
				match(COMMA);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SambodhanaSuchakaContext extends ParserRuleContext {
		public TerminalNode HE() { return getToken(VyakaranamParser.HE, 0); }
		public TerminalNode BHOH() { return getToken(VyakaranamParser.BHOH, 0); }
		public SambodhanaSuchakaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sambodhanaSuchaka; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSambodhanaSuchaka(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSambodhanaSuchaka(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSambodhanaSuchaka(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SambodhanaSuchakaContext sambodhanaSuchaka() throws RecognitionException {
		SambodhanaSuchakaContext _localctx = new SambodhanaSuchakaContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_sambodhanaSuchaka);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			_la = _input.LA(1);
			if ( !(_la==HE || _la==BHOH) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PadaContext extends ParserRuleContext {
		public SubantaPadaContext subantaPada() {
			return getRuleContext(SubantaPadaContext.class,0);
		}
		public TingantaPadaContext tingantaPada() {
			return getRuleContext(TingantaPadaContext.class,0);
		}
		public AvyayaPadaContext avyayaPada() {
			return getRuleContext(AvyayaPadaContext.class,0);
		}
		public SankhyaPadaContext sankhyaPada() {
			return getRuleContext(SankhyaPadaContext.class,0);
		}
		public PadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PadaContext pada() throws RecognitionException {
		PadaContext _localctx = new PadaContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_pada);
		try {
			setState(414);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(410);
				subantaPada();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(411);
				tingantaPada();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(412);
				avyayaPada();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(413);
				sankhyaPada();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SankhyaPadaContext extends ParserRuleContext {
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public SankhyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaPadaContext sankhyaPada() throws RecognitionException {
		SankhyaPadaContext _localctx = new SankhyaPadaContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_sankhyaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(416);
				sankhyaStem();
				setState(417);
				match(PLUS);
				}
				}
				setState(421); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(423);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SankhyaPuranaPadaContext extends ParserRuleContext {
		public PuranaPratyayaContext puranaPratyaya() {
			return getRuleContext(PuranaPratyayaContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public SankhyaPuranaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaPuranaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaPuranaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaPuranaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaPuranaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaPuranaPadaContext sankhyaPuranaPada() throws RecognitionException {
		SankhyaPuranaPadaContext _localctx = new SankhyaPuranaPadaContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_sankhyaPuranaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(425);
				sankhyaStem();
				setState(426);
				match(PLUS);
				}
				}
				setState(430); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(432);
			puranaPratyaya();
			setState(433);
			match(PLUS);
			setState(434);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PuranaPratyayaContext extends ParserRuleContext {
		public TerminalNode THA() { return getToken(VyakaranamParser.THA, 0); }
		public TerminalNode PRATYAYA_MA() { return getToken(VyakaranamParser.PRATYAYA_MA, 0); }
		public TerminalNode PRATYAYA_TAMA() { return getToken(VyakaranamParser.PRATYAYA_TAMA, 0); }
		public TerminalNode PRATYAYA_TIYA() { return getToken(VyakaranamParser.PRATYAYA_TIYA, 0); }
		public PuranaPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_puranaPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPuranaPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPuranaPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPuranaPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PuranaPratyayaContext puranaPratyaya() throws RecognitionException {
		PuranaPratyayaContext _localctx = new PuranaPratyayaContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_puranaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			_la = _input.LA(1);
			if ( !(_la==THA || ((((_la - 203)) & ~0x3f) == 0 && ((1L << (_la - 203)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SankhyaAbhyasaPadaContext extends ParserRuleContext {
		public TerminalNode KRITVAS() { return getToken(VyakaranamParser.KRITVAS, 0); }
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public TerminalNode SUC() { return getToken(VyakaranamParser.SUC, 0); }
		public TerminalNode DHAA() { return getToken(VyakaranamParser.DHAA, 0); }
		public SankhyaAbhyasaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaAbhyasaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaAbhyasaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaAbhyasaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaAbhyasaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaAbhyasaPadaContext sankhyaAbhyasaPada() throws RecognitionException {
		SankhyaAbhyasaPadaContext _localctx = new SankhyaAbhyasaPadaContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_sankhyaAbhyasaPada);
		int _la;
		try {
			setState(465);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(441); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(438);
					sankhyaStem();
					setState(439);
					match(PLUS);
					}
					}
					setState(443); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER );
				setState(445);
				match(KRITVAS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(450); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(447);
					sankhyaStem();
					setState(448);
					match(PLUS);
					}
					}
					setState(452); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER );
				setState(454);
				match(SUC);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(459); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(456);
					sankhyaStem();
					setState(457);
					match(PLUS);
					}
					}
					setState(461); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER );
				setState(463);
				match(DHAA);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KatapayadiPadaContext extends ParserRuleContext {
		public TerminalNode KATAPAYADI() { return getToken(VyakaranamParser.KATAPAYADI, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public KatapayadiPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_katapayadiPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterKatapayadiPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitKatapayadiPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitKatapayadiPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KatapayadiPadaContext katapayadiPada() throws RecognitionException {
		KatapayadiPadaContext _localctx = new KatapayadiPadaContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_katapayadiPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			match(KATAPAYADI);
			setState(468);
			match(IDENTIFIER);
			setState(469);
			match(PLUS);
			setState(470);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AryabhatiyaPadaContext extends ParserRuleContext {
		public TerminalNode ARYABHATIYA() { return getToken(VyakaranamParser.ARYABHATIYA, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public AryabhatiyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aryabhatiyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAryabhatiyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAryabhatiyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAryabhatiyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AryabhatiyaPadaContext aryabhatiyaPada() throws RecognitionException {
		AryabhatiyaPadaContext _localctx = new AryabhatiyaPadaContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_aryabhatiyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472);
			match(ARYABHATIYA);
			setState(473);
			match(IDENTIFIER);
			setState(474);
			match(PLUS);
			setState(475);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BhutasamkhyaPadaContext extends ParserRuleContext {
		public TerminalNode BHUTASAMKHYA() { return getToken(VyakaranamParser.BHUTASAMKHYA, 0); }
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(VyakaranamParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(VyakaranamParser.IDENTIFIER, i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public BhutasamkhyaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bhutasamkhyaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterBhutasamkhyaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitBhutasamkhyaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitBhutasamkhyaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BhutasamkhyaPadaContext bhutasamkhyaPada() throws RecognitionException {
		BhutasamkhyaPadaContext _localctx = new BhutasamkhyaPadaContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_bhutasamkhyaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			match(BHUTASAMKHYA);
			setState(480); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(478);
				match(IDENTIFIER);
				setState(479);
				match(PLUS);
				}
				}
				setState(482); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(484);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SankhyaStemContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public SankhyaStemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaStem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaStem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaStem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaStem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaStemContext sankhyaStem() throws RecognitionException {
		SankhyaStemContext _localctx = new SankhyaStemContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_sankhyaStem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubantaPadaContext extends ParserRuleContext {
		public PratipadikaContext pratipadika() {
			return getRuleContext(PratipadikaContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public SubantaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subantaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSubantaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSubantaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSubantaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubantaPadaContext subantaPada() throws RecognitionException {
		SubantaPadaContext _localctx = new SubantaPadaContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_subantaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			pratipadika();
			setState(489);
			match(PLUS);
			setState(490);
			supPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PratipadikaContext extends ParserRuleContext {
		public PratipadikaMulaContext pratipadikaMula() {
			return getRuleContext(PratipadikaMulaContext.class,0);
		}
		public List<PratipadikaVikaraContext> pratipadikaVikara() {
			return getRuleContexts(PratipadikaVikaraContext.class);
		}
		public PratipadikaVikaraContext pratipadikaVikara(int i) {
			return getRuleContext(PratipadikaVikaraContext.class,i);
		}
		public PratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PratipadikaContext pratipadika() throws RecognitionException {
		PratipadikaContext _localctx = new PratipadikaContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_pratipadika);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			pratipadikaMula();
			setState(496);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(493);
					pratipadikaVikara();
					}
					} 
				}
				setState(498);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PratipadikaMulaContext extends ParserRuleContext {
		public MulaPratipadikaContext mulaPratipadika() {
			return getRuleContext(MulaPratipadikaContext.class,0);
		}
		public SamjnaQualifierPratipadikaContext samjnaQualifierPratipadika() {
			return getRuleContext(SamjnaQualifierPratipadikaContext.class,0);
		}
		public KridantaPratipadikaContext kridantaPratipadika() {
			return getRuleContext(KridantaPratipadikaContext.class,0);
		}
		public UnadyantaPratipadikaContext unadyantaPratipadika() {
			return getRuleContext(UnadyantaPratipadikaContext.class,0);
		}
		public SamasaPratipadikaContext samasaPratipadika() {
			return getRuleContext(SamasaPratipadikaContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(VyakaranamParser.LPAREN, 0); }
		public PratipadikaContext pratipadika() {
			return getRuleContext(PratipadikaContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(VyakaranamParser.RPAREN, 0); }
		public PratipadikaMulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pratipadikaMula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPratipadikaMula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPratipadikaMula(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPratipadikaMula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PratipadikaMulaContext pratipadikaMula() throws RecognitionException {
		PratipadikaMulaContext _localctx = new PratipadikaMulaContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_pratipadikaMula);
		try {
			setState(508);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(499);
				mulaPratipadika();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(500);
				samjnaQualifierPratipadika();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(501);
				kridantaPratipadika();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(502);
				unadyantaPratipadika();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(503);
				samasaPratipadika();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(504);
				match(LPAREN);
				setState(505);
				pratipadika();
				setState(506);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PratipadikaVikaraContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public TaddhitaPratyayaContext taddhitaPratyaya() {
			return getRuleContext(TaddhitaPratyayaContext.class,0);
		}
		public StriPratyayaContext striPratyaya() {
			return getRuleContext(StriPratyayaContext.class,0);
		}
		public PratipadikaVikaraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pratipadikaVikara; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterPratipadikaVikara(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitPratipadikaVikara(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitPratipadikaVikara(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PratipadikaVikaraContext pratipadikaVikara() throws RecognitionException {
		PratipadikaVikaraContext _localctx = new PratipadikaVikaraContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_pratipadikaVikara);
		try {
			setState(514);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(510);
				match(PLUS);
				setState(511);
				taddhitaPratyaya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(512);
				match(PLUS);
				setState(513);
				striPratyaya();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MulaPratipadikaContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode ADHIKA() { return getToken(VyakaranamParser.ADHIKA, 0); }
		public TerminalNode UNA() { return getToken(VyakaranamParser.UNA, 0); }
		public MulaPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mulaPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterMulaPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitMulaPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitMulaPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MulaPratipadikaContext mulaPratipadika() throws RecognitionException {
		MulaPratipadikaContext _localctx = new MulaPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_mulaPratipadika);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(516);
			_la = _input.LA(1);
			if ( !(_la==ADHIKA || _la==UNA || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SamjnaQualifierPratipadikaContext extends ParserRuleContext {
		public TerminalNode NI() { return getToken(VyakaranamParser.NI, 0); }
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public TerminalNode TYA() { return getToken(VyakaranamParser.TYA, 0); }
		public TerminalNode ANTAR() { return getToken(VyakaranamParser.ANTAR, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public SamjnaQualifierPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_samjnaQualifierPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSamjnaQualifierPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSamjnaQualifierPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSamjnaQualifierPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SamjnaQualifierPratipadikaContext samjnaQualifierPratipadika() throws RecognitionException {
		SamjnaQualifierPratipadikaContext _localctx = new SamjnaQualifierPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_samjnaQualifierPratipadika);
		try {
			setState(524);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NI:
				enterOuterAlt(_localctx, 1);
				{
				setState(518);
				match(NI);
				setState(519);
				match(PLUS);
				setState(520);
				match(TYA);
				}
				break;
			case ANTAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
				match(ANTAR);
				setState(522);
				match(PLUS);
				setState(523);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KridantaPratipadikaContext extends ParserRuleContext {
		public DhatuPrakritiContext dhatuPrakriti() {
			return getRuleContext(DhatuPrakritiContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public KrtPratyayaContext krtPratyaya() {
			return getRuleContext(KrtPratyayaContext.class,0);
		}
		public UpasargaKramaContext upasargaKrama() {
			return getRuleContext(UpasargaKramaContext.class,0);
		}
		public KridantaPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kridantaPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterKridantaPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitKridantaPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitKridantaPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KridantaPratipadikaContext kridantaPratipadika() throws RecognitionException {
		KridantaPratipadikaContext _localctx = new KridantaPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_kridantaPratipadika);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(526);
				upasargaKrama();
				}
				break;
			}
			setState(529);
			dhatuPrakriti();
			setState(530);
			match(PLUS);
			setState(531);
			krtPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnadyantaPratipadikaContext extends ParserRuleContext {
		public DhatuPrakritiContext dhatuPrakriti() {
			return getRuleContext(DhatuPrakritiContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public UnadiPratyayaContext unadiPratyaya() {
			return getRuleContext(UnadiPratyayaContext.class,0);
		}
		public UpasargaKramaContext upasargaKrama() {
			return getRuleContext(UpasargaKramaContext.class,0);
		}
		public UnadyantaPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unadyantaPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUnadyantaPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUnadyantaPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUnadyantaPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnadyantaPratipadikaContext unadyantaPratipadika() throws RecognitionException {
		UnadyantaPratipadikaContext _localctx = new UnadyantaPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_unadyantaPratipadika);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(533);
				upasargaKrama();
				}
				break;
			}
			setState(536);
			dhatuPrakriti();
			setState(537);
			match(PLUS);
			setState(538);
			unadiPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnadiPratyayaContext extends ParserRuleContext {
		public TerminalNode UNADI() { return getToken(VyakaranamParser.UNADI, 0); }
		public TerminalNode LPAREN() { return getToken(VyakaranamParser.LPAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode RPAREN() { return getToken(VyakaranamParser.RPAREN, 0); }
		public UnadiPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unadiPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUnadiPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUnadiPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUnadiPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnadiPratyayaContext unadiPratyaya() throws RecognitionException {
		UnadiPratyayaContext _localctx = new UnadiPratyayaContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_unadiPratyaya);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
			match(UNADI);
			setState(541);
			match(LPAREN);
			setState(542);
			match(IDENTIFIER);
			setState(543);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TaddhitaPratyayaContext extends ParserRuleContext {
		public TerminalNode MATUP() { return getToken(VyakaranamParser.MATUP, 0); }
		public TerminalNode VATUP() { return getToken(VyakaranamParser.VATUP, 0); }
		public TerminalNode MAT() { return getToken(VyakaranamParser.MAT, 0); }
		public TerminalNode VAT() { return getToken(VyakaranamParser.VAT, 0); }
		public TerminalNode INI() { return getToken(VyakaranamParser.INI, 0); }
		public TerminalNode TVA() { return getToken(VyakaranamParser.TVA, 0); }
		public TerminalNode TAL() { return getToken(VyakaranamParser.TAL, 0); }
		public TerminalNode TARAP() { return getToken(VyakaranamParser.TARAP, 0); }
		public TerminalNode TAMAP() { return getToken(VyakaranamParser.TAMAP, 0); }
		public TerminalNode MAYAT() { return getToken(VyakaranamParser.MAYAT, 0); }
		public TerminalNode TASIL() { return getToken(VyakaranamParser.TASIL, 0); }
		public TerminalNode AN() { return getToken(VyakaranamParser.AN, 0); }
		public TerminalNode INJ() { return getToken(VyakaranamParser.INJ, 0); }
		public TerminalNode DHAK() { return getToken(VyakaranamParser.DHAK, 0); }
		public TerminalNode THAJ() { return getToken(VyakaranamParser.THAJ, 0); }
		public TerminalNode CHHA() { return getToken(VyakaranamParser.CHHA, 0); }
		public TerminalNode KA() { return getToken(VyakaranamParser.KA, 0); }
		public TerminalNode KAN() { return getToken(VyakaranamParser.KAN, 0); }
		public TerminalNode YAT() { return getToken(VyakaranamParser.YAT, 0); }
		public TerminalNode AYANA() { return getToken(VyakaranamParser.AYANA, 0); }
		public TerminalNode IYA() { return getToken(VyakaranamParser.IYA, 0); }
		public TerminalNode INA() { return getToken(VyakaranamParser.INA, 0); }
		public TerminalNode DAA() { return getToken(VyakaranamParser.DAA, 0); }
		public TerminalNode DHAA() { return getToken(VyakaranamParser.DHAA, 0); }
		public TerminalNode TYAP() { return getToken(VyakaranamParser.TYAP, 0); }
		public TerminalNode TYA() { return getToken(VyakaranamParser.TYA, 0); }
		public TaddhitaPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_taddhitaPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterTaddhitaPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitTaddhitaPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitTaddhitaPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TaddhitaPratyayaContext taddhitaPratyaya() throws RecognitionException {
		TaddhitaPratyayaContext _localctx = new TaddhitaPratyayaContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_taddhitaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			_la = _input.LA(1);
			if ( !(((((_la - 164)) & ~0x3f) == 0 && ((1L << (_la - 164)) & 918730474619174913L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StriPratyayaContext extends ParserRuleContext {
		public TerminalNode TAAP() { return getToken(VyakaranamParser.TAAP, 0); }
		public TerminalNode DAAP() { return getToken(VyakaranamParser.DAAP, 0); }
		public TerminalNode CHAAP() { return getToken(VyakaranamParser.CHAAP, 0); }
		public TerminalNode NEEP() { return getToken(VyakaranamParser.NEEP, 0); }
		public TerminalNode NEESH() { return getToken(VyakaranamParser.NEESH, 0); }
		public TerminalNode NEEN() { return getToken(VyakaranamParser.NEEN, 0); }
		public TerminalNode UUNG() { return getToken(VyakaranamParser.UUNG, 0); }
		public TerminalNode TICH() { return getToken(VyakaranamParser.TICH, 0); }
		public StriPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_striPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterStriPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitStriPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitStriPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StriPratyayaContext striPratyaya() throws RecognitionException {
		StriPratyayaContext _localctx = new StriPratyayaContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_striPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			_la = _input.LA(1);
			if ( !(((((_la - 224)) & ~0x3f) == 0 && ((1L << (_la - 224)) & 255L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SamasaPratipadikaContext extends ParserRuleContext {
		public List<SamasaAngaContext> samasaAnga() {
			return getRuleContexts(SamasaAngaContext.class);
		}
		public SamasaAngaContext samasaAnga(int i) {
			return getRuleContext(SamasaAngaContext.class,i);
		}
		public List<TerminalNode> SAMASA_SEPARATOR() { return getTokens(VyakaranamParser.SAMASA_SEPARATOR); }
		public TerminalNode SAMASA_SEPARATOR(int i) {
			return getToken(VyakaranamParser.SAMASA_SEPARATOR, i);
		}
		public SamasaPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_samasaPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSamasaPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSamasaPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSamasaPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SamasaPratipadikaContext samasaPratipadika() throws RecognitionException {
		SamasaPratipadikaContext _localctx = new SamasaPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_samasaPratipadika);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			samasaAnga();
			setState(552); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(550);
				match(SAMASA_SEPARATOR);
				setState(551);
				samasaAnga();
				}
				}
				setState(554); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==SAMASA_SEPARATOR );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SamasaAngaContext extends ParserRuleContext {
		public AsamasikaPratipadikaContext asamasikaPratipadika() {
			return getRuleContext(AsamasikaPratipadikaContext.class,0);
		}
		public SamasaSupAvasthaContext samasaSupAvastha() {
			return getRuleContext(SamasaSupAvasthaContext.class,0);
		}
		public SamasaAngaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_samasaAnga; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSamasaAnga(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSamasaAnga(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSamasaAnga(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SamasaAngaContext samasaAnga() throws RecognitionException {
		SamasaAngaContext _localctx = new SamasaAngaContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_samasaAnga);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(556);
			asamasikaPratipadika();
			setState(558);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(557);
				samasaSupAvastha();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SamasaSupAvasthaContext extends ParserRuleContext {
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public SupAvasthaContext supAvastha() {
			return getRuleContext(SupAvasthaContext.class,0);
		}
		public SamasaSupAvasthaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_samasaSupAvastha; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSamasaSupAvastha(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSamasaSupAvastha(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSamasaSupAvastha(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SamasaSupAvasthaContext samasaSupAvastha() throws RecognitionException {
		SamasaSupAvasthaContext _localctx = new SamasaSupAvasthaContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_samasaSupAvastha);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(560);
			match(PLUS);
			setState(561);
			supPratyaya();
			setState(562);
			match(PLUS);
			setState(563);
			supAvastha();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SupAvasthaContext extends ParserRuleContext {
		public TerminalNode LUK() { return getToken(VyakaranamParser.LUK, 0); }
		public TerminalNode SHLU() { return getToken(VyakaranamParser.SHLU, 0); }
		public TerminalNode LUP() { return getToken(VyakaranamParser.LUP, 0); }
		public TerminalNode ALUK() { return getToken(VyakaranamParser.ALUK, 0); }
		public SupAvasthaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_supAvastha; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSupAvastha(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSupAvastha(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSupAvastha(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SupAvasthaContext supAvastha() throws RecognitionException {
		SupAvasthaContext _localctx = new SupAvasthaContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_supAvastha);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			_la = _input.LA(1);
			if ( !(((((_la - 232)) & ~0x3f) == 0 && ((1L << (_la - 232)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsamasikaPratipadikaContext extends ParserRuleContext {
		public AsamasikaPratipadikaMulaContext asamasikaPratipadikaMula() {
			return getRuleContext(AsamasikaPratipadikaMulaContext.class,0);
		}
		public List<PratipadikaVikaraContext> pratipadikaVikara() {
			return getRuleContexts(PratipadikaVikaraContext.class);
		}
		public PratipadikaVikaraContext pratipadikaVikara(int i) {
			return getRuleContext(PratipadikaVikaraContext.class,i);
		}
		public AsamasikaPratipadikaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asamasikaPratipadika; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAsamasikaPratipadika(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAsamasikaPratipadika(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAsamasikaPratipadika(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsamasikaPratipadikaContext asamasikaPratipadika() throws RecognitionException {
		AsamasikaPratipadikaContext _localctx = new AsamasikaPratipadikaContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_asamasikaPratipadika);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
			asamasikaPratipadikaMula();
			setState(571);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(568);
					pratipadikaVikara();
					}
					} 
				}
				setState(573);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsamasikaPratipadikaMulaContext extends ParserRuleContext {
		public MulaPratipadikaContext mulaPratipadika() {
			return getRuleContext(MulaPratipadikaContext.class,0);
		}
		public SamjnaQualifierPratipadikaContext samjnaQualifierPratipadika() {
			return getRuleContext(SamjnaQualifierPratipadikaContext.class,0);
		}
		public KridantaPratipadikaContext kridantaPratipadika() {
			return getRuleContext(KridantaPratipadikaContext.class,0);
		}
		public UnadyantaPratipadikaContext unadyantaPratipadika() {
			return getRuleContext(UnadyantaPratipadikaContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(VyakaranamParser.LPAREN, 0); }
		public SamasaPratipadikaContext samasaPratipadika() {
			return getRuleContext(SamasaPratipadikaContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(VyakaranamParser.RPAREN, 0); }
		public AsamasikaPratipadikaMulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asamasikaPratipadikaMula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAsamasikaPratipadikaMula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAsamasikaPratipadikaMula(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAsamasikaPratipadikaMula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsamasikaPratipadikaMulaContext asamasikaPratipadikaMula() throws RecognitionException {
		AsamasikaPratipadikaMulaContext _localctx = new AsamasikaPratipadikaMulaContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_asamasikaPratipadikaMula);
		try {
			setState(582);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(574);
				mulaPratipadika();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(575);
				samjnaQualifierPratipadika();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(576);
				kridantaPratipadika();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(577);
				unadyantaPratipadika();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(578);
				match(LPAREN);
				setState(579);
				samasaPratipadika();
				setState(580);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SamuccitaSubantaContext extends ParserRuleContext {
		public List<SubantaPadaContext> subantaPada() {
			return getRuleContexts(SubantaPadaContext.class);
		}
		public SubantaPadaContext subantaPada(int i) {
			return getRuleContext(SubantaPadaContext.class,i);
		}
		public TerminalNode CHA() { return getToken(VyakaranamParser.CHA, 0); }
		public List<TerminalNode> COMMA() { return getTokens(VyakaranamParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(VyakaranamParser.COMMA, i);
		}
		public SamuccitaSubantaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_samuccitaSubanta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSamuccitaSubanta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSamuccitaSubanta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSamuccitaSubanta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SamuccitaSubantaContext samuccitaSubanta() throws RecognitionException {
		SamuccitaSubantaContext _localctx = new SamuccitaSubantaContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_samuccitaSubanta);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(584);
			subantaPada();
			setState(589); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(586);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(585);
					match(COMMA);
					}
				}

				setState(588);
				subantaPada();
				}
				}
				setState(591); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -144009634958539736L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8191L) != 0) || ((((_la - 219)) & ~0x3f) == 0 && ((1L << (_la - 219)) & 33554449L) != 0) );
			setState(593);
			match(CHA);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DhatuPrakritiContext extends ParserRuleContext {
		public DhatuMulaContext dhatuMula() {
			return getRuleContext(DhatuMulaContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public List<SanadiPratyayaContext> sanadiPratyaya() {
			return getRuleContexts(SanadiPratyayaContext.class);
		}
		public SanadiPratyayaContext sanadiPratyaya(int i) {
			return getRuleContext(SanadiPratyayaContext.class,i);
		}
		public DhatuPrakritiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dhatuPrakriti; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterDhatuPrakriti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitDhatuPrakriti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitDhatuPrakriti(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DhatuPrakritiContext dhatuPrakriti() throws RecognitionException {
		DhatuPrakritiContext _localctx = new DhatuPrakritiContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_dhatuPrakriti);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(595);
			dhatuMula();
			setState(600);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(596);
					match(PLUS);
					setState(597);
					sanadiPratyaya();
					}
					} 
				}
				setState(602);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DhatuMulaContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode DAA() { return getToken(VyakaranamParser.DAA, 0); }
		public TerminalNode DHAA() { return getToken(VyakaranamParser.DHAA, 0); }
		public TerminalNode SU() { return getToken(VyakaranamParser.SU, 0); }
		public TerminalNode VAA() { return getToken(VyakaranamParser.VAA, 0); }
		public DhatuMulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dhatuMula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterDhatuMula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitDhatuMula(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitDhatuMula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DhatuMulaContext dhatuMula() throws RecognitionException {
		DhatuMulaContext _localctx = new DhatuMulaContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_dhatuMula);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(603);
			_la = _input.LA(1);
			if ( !(_la==VAA || _la==SU || ((((_la - 219)) & ~0x3f) == 0 && ((1L << (_la - 219)) & 33554449L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SanadiPratyayaContext extends ParserRuleContext {
		public TerminalNode SAN() { return getToken(VyakaranamParser.SAN, 0); }
		public TerminalNode NIC() { return getToken(VyakaranamParser.NIC, 0); }
		public TerminalNode YAN() { return getToken(VyakaranamParser.YAN, 0); }
		public TerminalNode YUK_SAN() { return getToken(VyakaranamParser.YUK_SAN, 0); }
		public TerminalNode KYACH() { return getToken(VyakaranamParser.KYACH, 0); }
		public TerminalNode KAAMYACH() { return getToken(VyakaranamParser.KAAMYACH, 0); }
		public TerminalNode KYASH() { return getToken(VyakaranamParser.KYASH, 0); }
		public TerminalNode KYANG() { return getToken(VyakaranamParser.KYANG, 0); }
		public SanadiPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sanadiPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSanadiPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSanadiPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSanadiPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SanadiPratyayaContext sanadiPratyaya() throws RecognitionException {
		SanadiPratyayaContext _localctx = new SanadiPratyayaContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_sanadiPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(605);
			_la = _input.LA(1);
			if ( !(((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & 255L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UpasargaKramaContext extends ParserRuleContext {
		public List<UpasargaContext> upasarga() {
			return getRuleContexts(UpasargaContext.class);
		}
		public UpasargaContext upasarga(int i) {
			return getRuleContext(UpasargaContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public UpasargaKramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_upasargaKrama; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUpasargaKrama(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUpasargaKrama(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUpasargaKrama(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpasargaKramaContext upasargaKrama() throws RecognitionException {
		UpasargaKramaContext _localctx = new UpasargaKramaContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_upasargaKrama);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(607);
			upasarga();
			setState(608);
			match(PLUS);
			setState(614);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(609);
					upasarga();
					setState(610);
					match(PLUS);
					}
					} 
				}
				setState(616);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UpasargaContext extends ParserRuleContext {
		public TerminalNode PRA() { return getToken(VyakaranamParser.PRA, 0); }
		public TerminalNode PARAA() { return getToken(VyakaranamParser.PARAA, 0); }
		public TerminalNode APA() { return getToken(VyakaranamParser.APA, 0); }
		public TerminalNode SAM() { return getToken(VyakaranamParser.SAM, 0); }
		public TerminalNode ANUU() { return getToken(VyakaranamParser.ANUU, 0); }
		public TerminalNode AVA() { return getToken(VyakaranamParser.AVA, 0); }
		public TerminalNode NIS() { return getToken(VyakaranamParser.NIS, 0); }
		public TerminalNode DUS() { return getToken(VyakaranamParser.DUS, 0); }
		public TerminalNode VI() { return getToken(VyakaranamParser.VI, 0); }
		public TerminalNode AANG() { return getToken(VyakaranamParser.AANG, 0); }
		public TerminalNode NI() { return getToken(VyakaranamParser.NI, 0); }
		public TerminalNode ADHI() { return getToken(VyakaranamParser.ADHI, 0); }
		public TerminalNode API() { return getToken(VyakaranamParser.API, 0); }
		public TerminalNode ATI() { return getToken(VyakaranamParser.ATI, 0); }
		public TerminalNode SU() { return getToken(VyakaranamParser.SU, 0); }
		public TerminalNode UD() { return getToken(VyakaranamParser.UD, 0); }
		public TerminalNode ABHI() { return getToken(VyakaranamParser.ABHI, 0); }
		public TerminalNode PRATI() { return getToken(VyakaranamParser.PRATI, 0); }
		public TerminalNode PARI() { return getToken(VyakaranamParser.PARI, 0); }
		public TerminalNode UPA() { return getToken(VyakaranamParser.UPA, 0); }
		public TerminalNode ANTAR() { return getToken(VyakaranamParser.ANTAR, 0); }
		public UpasargaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_upasarga; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterUpasarga(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitUpasarga(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitUpasarga(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpasargaContext upasarga() throws RecognitionException {
		UpasargaContext _localctx = new UpasargaContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_upasarga);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			_la = _input.LA(1);
			if ( !(((((_la - 20)) & ~0x3f) == 0 && ((1L << (_la - 20)) & 144115050636902401L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TingantaPadaContext extends ParserRuleContext {
		public DhatuPrakritiContext dhatuPrakriti() {
			return getRuleContext(DhatuPrakritiContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public LakaraContext lakara() {
			return getRuleContext(LakaraContext.class,0);
		}
		public TingPratyayaContext tingPratyaya() {
			return getRuleContext(TingPratyayaContext.class,0);
		}
		public UpasargaKramaContext upasargaKrama() {
			return getRuleContext(UpasargaKramaContext.class,0);
		}
		public VikaranaContext vikarana() {
			return getRuleContext(VikaranaContext.class,0);
		}
		public TingantaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tingantaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterTingantaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitTingantaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitTingantaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TingantaPadaContext tingantaPada() throws RecognitionException {
		TingantaPadaContext _localctx = new TingantaPadaContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_tingantaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(619);
				upasargaKrama();
				}
				break;
			}
			setState(622);
			dhatuPrakriti();
			setState(625);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(623);
				match(PLUS);
				setState(624);
				vikarana();
				}
				break;
			}
			setState(627);
			match(PLUS);
			setState(628);
			lakara();
			setState(629);
			match(PLUS);
			setState(630);
			tingPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VyutpattiTingantaContext extends ParserRuleContext {
		public VyutpattiAngaContext vyutpattiAnga() {
			return getRuleContext(VyutpattiAngaContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public LakaraContext lakara() {
			return getRuleContext(LakaraContext.class,0);
		}
		public TingPratyayaContext tingPratyaya() {
			return getRuleContext(TingPratyayaContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public VyutpattiTingantaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vyutpattiTinganta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVyutpattiTinganta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVyutpattiTinganta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVyutpattiTinganta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VyutpattiTingantaContext vyutpattiTinganta() throws RecognitionException {
		VyutpattiTingantaContext _localctx = new VyutpattiTingantaContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_vyutpattiTinganta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(632);
			vyutpattiAnga();
			setState(633);
			match(PLUS);
			setState(634);
			lakara();
			setState(635);
			match(PLUS);
			setState(636);
			tingPratyaya();
			setState(637);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VyutpattiAngaContext extends ParserRuleContext {
		public List<VyutpattiAvayavaContext> vyutpattiAvayava() {
			return getRuleContexts(VyutpattiAvayavaContext.class);
		}
		public VyutpattiAvayavaContext vyutpattiAvayava(int i) {
			return getRuleContext(VyutpattiAvayavaContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public VyutpattiAngaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vyutpattiAnga; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVyutpattiAnga(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVyutpattiAnga(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVyutpattiAnga(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VyutpattiAngaContext vyutpattiAnga() throws RecognitionException {
		VyutpattiAngaContext _localctx = new VyutpattiAngaContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_vyutpattiAnga);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			vyutpattiAvayava();
			setState(644);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(640);
					match(PLUS);
					setState(641);
					vyutpattiAvayava();
					}
					} 
				}
				setState(646);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VyutpattiAvayavaContext extends ParserRuleContext {
		public UpasargaContext upasarga() {
			return getRuleContext(UpasargaContext.class,0);
		}
		public DhatuPrakritiContext dhatuPrakriti() {
			return getRuleContext(DhatuPrakritiContext.class,0);
		}
		public AgamaContext agama() {
			return getRuleContext(AgamaContext.class,0);
		}
		public VikaranaContext vikarana() {
			return getRuleContext(VikaranaContext.class,0);
		}
		public AbhyasaContext abhyasa() {
			return getRuleContext(AbhyasaContext.class,0);
		}
		public AdeshamContext adesham() {
			return getRuleContext(AdeshamContext.class,0);
		}
		public VyutpattiAvayavaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vyutpattiAvayava; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVyutpattiAvayava(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVyutpattiAvayava(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVyutpattiAvayava(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VyutpattiAvayavaContext vyutpattiAvayava() throws RecognitionException {
		VyutpattiAvayavaContext _localctx = new VyutpattiAvayavaContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_vyutpattiAvayava);
		try {
			setState(653);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(647);
				upasarga();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(648);
				dhatuPrakriti();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(649);
				agama();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(650);
				vikarana();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(651);
				abhyasa();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(652);
				adesham();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbhyasaContext extends ParserRuleContext {
		public TerminalNode ABHYASA() { return getToken(VyakaranamParser.ABHYASA, 0); }
		public TerminalNode LPAREN() { return getToken(VyakaranamParser.LPAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode RPAREN() { return getToken(VyakaranamParser.RPAREN, 0); }
		public AbhyasaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abhyasa; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAbhyasa(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAbhyasa(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAbhyasa(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbhyasaContext abhyasa() throws RecognitionException {
		AbhyasaContext _localctx = new AbhyasaContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_abhyasa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			match(ABHYASA);
			setState(656);
			match(LPAREN);
			setState(657);
			match(IDENTIFIER);
			setState(658);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdeshamContext extends ParserRuleContext {
		public TerminalNode ADESHA() { return getToken(VyakaranamParser.ADESHA, 0); }
		public TerminalNode LPAREN() { return getToken(VyakaranamParser.LPAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode RPAREN() { return getToken(VyakaranamParser.RPAREN, 0); }
		public AdeshamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adesham; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAdesham(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAdesham(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAdesham(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdeshamContext adesham() throws RecognitionException {
		AdeshamContext _localctx = new AdeshamContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_adesham);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(660);
			match(ADESHA);
			setState(661);
			match(LPAREN);
			setState(662);
			match(IDENTIFIER);
			setState(663);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LakaraContext extends ParserRuleContext {
		public TerminalNode LAT() { return getToken(VyakaranamParser.LAT, 0); }
		public TerminalNode LIT() { return getToken(VyakaranamParser.LIT, 0); }
		public TerminalNode LUT() { return getToken(VyakaranamParser.LUT, 0); }
		public TerminalNode LRT() { return getToken(VyakaranamParser.LRT, 0); }
		public TerminalNode LET() { return getToken(VyakaranamParser.LET, 0); }
		public TerminalNode LOT() { return getToken(VyakaranamParser.LOT, 0); }
		public TerminalNode LANG() { return getToken(VyakaranamParser.LANG, 0); }
		public TerminalNode LIN() { return getToken(VyakaranamParser.LIN, 0); }
		public TerminalNode LUNG() { return getToken(VyakaranamParser.LUNG, 0); }
		public TerminalNode LRNG() { return getToken(VyakaranamParser.LRNG, 0); }
		public LakaraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lakara; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterLakara(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitLakara(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitLakara(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LakaraContext lakara() throws RecognitionException {
		LakaraContext _localctx = new LakaraContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_lakara);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(665);
			_la = _input.LA(1);
			if ( !(((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 1023L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TingPratyayaContext extends ParserRuleContext {
		public TerminalNode TIP() { return getToken(VyakaranamParser.TIP, 0); }
		public TerminalNode TAS() { return getToken(VyakaranamParser.TAS, 0); }
		public TerminalNode JHI() { return getToken(VyakaranamParser.JHI, 0); }
		public TerminalNode SIP() { return getToken(VyakaranamParser.SIP, 0); }
		public TerminalNode THAS() { return getToken(VyakaranamParser.THAS, 0); }
		public TerminalNode THA() { return getToken(VyakaranamParser.THA, 0); }
		public TerminalNode MIP() { return getToken(VyakaranamParser.MIP, 0); }
		public TerminalNode VAS() { return getToken(VyakaranamParser.VAS, 0); }
		public TerminalNode MAS() { return getToken(VyakaranamParser.MAS, 0); }
		public TerminalNode TA() { return getToken(VyakaranamParser.TA, 0); }
		public TerminalNode ATAAM() { return getToken(VyakaranamParser.ATAAM, 0); }
		public TerminalNode JHA() { return getToken(VyakaranamParser.JHA, 0); }
		public TerminalNode THAS_A() { return getToken(VyakaranamParser.THAS_A, 0); }
		public TerminalNode ATHAAM() { return getToken(VyakaranamParser.ATHAAM, 0); }
		public TerminalNode DHVAM() { return getToken(VyakaranamParser.DHVAM, 0); }
		public TerminalNode IT() { return getToken(VyakaranamParser.IT, 0); }
		public TerminalNode VAHI() { return getToken(VyakaranamParser.VAHI, 0); }
		public TerminalNode MAHING() { return getToken(VyakaranamParser.MAHING, 0); }
		public TingPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tingPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterTingPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitTingPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitTingPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TingPratyayaContext tingPratyaya() throws RecognitionException {
		TingPratyayaContext _localctx = new TingPratyayaContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_tingPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			_la = _input.LA(1);
			if ( !(((((_la - 95)) & ~0x3f) == 0 && ((1L << (_la - 95)) & 262143L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SupPratyayaContext extends ParserRuleContext {
		public TerminalNode SUP_SU() { return getToken(VyakaranamParser.SUP_SU, 0); }
		public TerminalNode SUP_AU() { return getToken(VyakaranamParser.SUP_AU, 0); }
		public TerminalNode SUP_JAS() { return getToken(VyakaranamParser.SUP_JAS, 0); }
		public TerminalNode SUP_AM() { return getToken(VyakaranamParser.SUP_AM, 0); }
		public TerminalNode SUP_AUT() { return getToken(VyakaranamParser.SUP_AUT, 0); }
		public TerminalNode SUP_SHAS() { return getToken(VyakaranamParser.SUP_SHAS, 0); }
		public TerminalNode SUP_TA() { return getToken(VyakaranamParser.SUP_TA, 0); }
		public TerminalNode SUP_BHYAM() { return getToken(VyakaranamParser.SUP_BHYAM, 0); }
		public TerminalNode SUP_BHIS() { return getToken(VyakaranamParser.SUP_BHIS, 0); }
		public TerminalNode SUP_NGE() { return getToken(VyakaranamParser.SUP_NGE, 0); }
		public TerminalNode SUP_BHYAS() { return getToken(VyakaranamParser.SUP_BHYAS, 0); }
		public TerminalNode SUP_NGASI() { return getToken(VyakaranamParser.SUP_NGASI, 0); }
		public TerminalNode SUP_NGAS() { return getToken(VyakaranamParser.SUP_NGAS, 0); }
		public TerminalNode SUP_OS() { return getToken(VyakaranamParser.SUP_OS, 0); }
		public TerminalNode SUP_AAM() { return getToken(VyakaranamParser.SUP_AAM, 0); }
		public TerminalNode SUP_NGI() { return getToken(VyakaranamParser.SUP_NGI, 0); }
		public TerminalNode SUP_SUP() { return getToken(VyakaranamParser.SUP_SUP, 0); }
		public SupPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_supPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSupPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSupPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSupPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SupPratyayaContext supPratyaya() throws RecognitionException {
		SupPratyayaContext _localctx = new SupPratyayaContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_supPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(669);
			_la = _input.LA(1);
			if ( !(((((_la - 113)) & ~0x3f) == 0 && ((1L << (_la - 113)) & 131071L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VikaranaContext extends ParserRuleContext {
		public TerminalNode SHAP() { return getToken(VyakaranamParser.SHAP, 0); }
		public TerminalNode SHYAN() { return getToken(VyakaranamParser.SHYAN, 0); }
		public TerminalNode SHNU() { return getToken(VyakaranamParser.SHNU, 0); }
		public TerminalNode SHNAM() { return getToken(VyakaranamParser.SHNAM, 0); }
		public TerminalNode SHNA() { return getToken(VyakaranamParser.SHNA, 0); }
		public TerminalNode U_VIKARANA() { return getToken(VyakaranamParser.U_VIKARANA, 0); }
		public TerminalNode SHNAAM() { return getToken(VyakaranamParser.SHNAAM, 0); }
		public TerminalNode YAK() { return getToken(VyakaranamParser.YAK, 0); }
		public TerminalNode SHAH() { return getToken(VyakaranamParser.SHAH, 0); }
		public TerminalNode SYA() { return getToken(VyakaranamParser.SYA, 0); }
		public TerminalNode TAS_VIKARANA() { return getToken(VyakaranamParser.TAS_VIKARANA, 0); }
		public TerminalNode CLI() { return getToken(VyakaranamParser.CLI, 0); }
		public TerminalNode SIC() { return getToken(VyakaranamParser.SIC, 0); }
		public TerminalNode ANG() { return getToken(VyakaranamParser.ANG, 0); }
		public TerminalNode CHANG() { return getToken(VyakaranamParser.CHANG, 0); }
		public TerminalNode KSA() { return getToken(VyakaranamParser.KSA, 0); }
		public VikaranaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vikarana; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterVikarana(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitVikarana(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitVikarana(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VikaranaContext vikarana() throws RecognitionException {
		VikaranaContext _localctx = new VikaranaContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_vikarana);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(671);
			_la = _input.LA(1);
			if ( !(((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & 65535L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AgamaContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(VyakaranamParser.AT, 0); }
		public TerminalNode IT() { return getToken(VyakaranamParser.IT, 0); }
		public TerminalNode IIT_AGAMA() { return getToken(VyakaranamParser.IIT_AGAMA, 0); }
		public TerminalNode NUM() { return getToken(VyakaranamParser.NUM, 0); }
		public TerminalNode TUK() { return getToken(VyakaranamParser.TUK, 0); }
		public TerminalNode MUT() { return getToken(VyakaranamParser.MUT, 0); }
		public TerminalNode NUT() { return getToken(VyakaranamParser.NUT, 0); }
		public TerminalNode YASUT() { return getToken(VyakaranamParser.YASUT, 0); }
		public TerminalNode SIYUT() { return getToken(VyakaranamParser.SIYUT, 0); }
		public TerminalNode SUK() { return getToken(VyakaranamParser.SUK, 0); }
		public TerminalNode RUK() { return getToken(VyakaranamParser.RUK, 0); }
		public TerminalNode RIK() { return getToken(VyakaranamParser.RIK, 0); }
		public TerminalNode PUK() { return getToken(VyakaranamParser.PUK, 0); }
		public TerminalNode YUK() { return getToken(VyakaranamParser.YUK, 0); }
		public TerminalNode VUK() { return getToken(VyakaranamParser.VUK, 0); }
		public AgamaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agama; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAgama(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAgama(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAgama(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AgamaContext agama() throws RecognitionException {
		AgamaContext _localctx = new AgamaContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_agama);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			_la = _input.LA(1);
			if ( !(((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & 1125831187365889L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KrtPratyayaContext extends ParserRuleContext {
		public TerminalNode KTA() { return getToken(VyakaranamParser.KTA, 0); }
		public TerminalNode KTAVATU() { return getToken(VyakaranamParser.KTAVATU, 0); }
		public TerminalNode TAVYAT() { return getToken(VyakaranamParser.TAVYAT, 0); }
		public TerminalNode ANIYAR() { return getToken(VyakaranamParser.ANIYAR, 0); }
		public TerminalNode YAT() { return getToken(VyakaranamParser.YAT, 0); }
		public TerminalNode NYAT() { return getToken(VyakaranamParser.NYAT, 0); }
		public TerminalNode KYAP() { return getToken(VyakaranamParser.KYAP, 0); }
		public TerminalNode SHATR() { return getToken(VyakaranamParser.SHATR, 0); }
		public TerminalNode SHANACH() { return getToken(VyakaranamParser.SHANACH, 0); }
		public TerminalNode GHANJ() { return getToken(VyakaranamParser.GHANJ, 0); }
		public TerminalNode LYUT() { return getToken(VyakaranamParser.LYUT, 0); }
		public TerminalNode NVUL() { return getToken(VyakaranamParser.NVUL, 0); }
		public TerminalNode TRICH() { return getToken(VyakaranamParser.TRICH, 0); }
		public TerminalNode ANIN() { return getToken(VyakaranamParser.ANIN, 0); }
		public TerminalNode KHAL() { return getToken(VyakaranamParser.KHAL, 0); }
		public TerminalNode KWIP() { return getToken(VyakaranamParser.KWIP, 0); }
		public TerminalNode KTIN() { return getToken(VyakaranamParser.KTIN, 0); }
		public TerminalNode AC() { return getToken(VyakaranamParser.AC, 0); }
		public TerminalNode AP() { return getToken(VyakaranamParser.AP, 0); }
		public TerminalNode KA() { return getToken(VyakaranamParser.KA, 0); }
		public TerminalNode NIN() { return getToken(VyakaranamParser.NIN, 0); }
		public TerminalNode NINI() { return getToken(VyakaranamParser.NINI, 0); }
		public TerminalNode IN_KRT() { return getToken(VyakaranamParser.IN_KRT, 0); }
		public TerminalNode TI_KRT() { return getToken(VyakaranamParser.TI_KRT, 0); }
		public TerminalNode TRA() { return getToken(VyakaranamParser.TRA, 0); }
		public TerminalNode ITRA() { return getToken(VyakaranamParser.ITRA, 0); }
		public TerminalNode ISHNUCH() { return getToken(VyakaranamParser.ISHNUCH, 0); }
		public TerminalNode UK() { return getToken(VyakaranamParser.UK, 0); }
		public KrtPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_krtPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterKrtPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitKrtPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitKrtPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KrtPratyayaContext krtPratyaya() throws RecognitionException {
		KrtPratyayaContext _localctx = new KrtPratyayaContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_krtPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			_la = _input.LA(1);
			if ( !(((((_la - 160)) & ~0x3f) == 0 && ((1L << (_la - 160)) & 268435455L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayaKrtPratyayaContext extends ParserRuleContext {
		public TerminalNode KTVA() { return getToken(VyakaranamParser.KTVA, 0); }
		public TerminalNode LYAP() { return getToken(VyakaranamParser.LYAP, 0); }
		public TerminalNode TUMUN() { return getToken(VyakaranamParser.TUMUN, 0); }
		public TerminalNode NAMUL() { return getToken(VyakaranamParser.NAMUL, 0); }
		public TerminalNode KASUN() { return getToken(VyakaranamParser.KASUN, 0); }
		public TerminalNode KTVOS() { return getToken(VyakaranamParser.KTVOS, 0); }
		public AvyayaKrtPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayaKrtPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayaKrtPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayaKrtPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayaKrtPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayaKrtPratyayaContext avyayaKrtPratyaya() throws RecognitionException {
		AvyayaKrtPratyayaContext _localctx = new AvyayaKrtPratyayaContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_avyayaKrtPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(677);
			_la = _input.LA(1);
			if ( !(((((_la - 188)) & ~0x3f) == 0 && ((1L << (_la - 188)) & 63L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayaKridantaContext extends ParserRuleContext {
		public DhatuPrakritiContext dhatuPrakriti() {
			return getRuleContext(DhatuPrakritiContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public AvyayaKrtPratyayaContext avyayaKrtPratyaya() {
			return getRuleContext(AvyayaKrtPratyayaContext.class,0);
		}
		public UpasargaKramaContext upasargaKrama() {
			return getRuleContext(UpasargaKramaContext.class,0);
		}
		public AvyayaKridantaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayaKridanta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayaKridanta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayaKridanta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayaKridanta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayaKridantaContext avyayaKridanta() throws RecognitionException {
		AvyayaKridantaContext _localctx = new AvyayaKridantaContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_avyayaKridanta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(679);
				upasargaKrama();
				}
				break;
			}
			setState(682);
			dhatuPrakriti();
			setState(683);
			match(PLUS);
			setState(684);
			avyayaKrtPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayaPadaContext extends ParserRuleContext {
		public MulaAvyayaContext mulaAvyaya() {
			return getRuleContext(MulaAvyayaContext.class,0);
		}
		public AvyayaKridantaContext avyayaKridanta() {
			return getRuleContext(AvyayaKridantaContext.class,0);
		}
		public AvyayaTaddhitantaContext avyayaTaddhitanta() {
			return getRuleContext(AvyayaTaddhitantaContext.class,0);
		}
		public AvyayibhavaPadaContext avyayibhavaPada() {
			return getRuleContext(AvyayibhavaPadaContext.class,0);
		}
		public SankhyaAvyayaContext sankhyaAvyaya() {
			return getRuleContext(SankhyaAvyayaContext.class,0);
		}
		public AvyayaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayaPadaContext avyayaPada() throws RecognitionException {
		AvyayaPadaContext _localctx = new AvyayaPadaContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_avyayaPada);
		try {
			setState(691);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(686);
				mulaAvyaya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(687);
				avyayaKridanta();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(688);
				avyayaTaddhitanta();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(689);
				avyayibhavaPada();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(690);
				sankhyaAvyaya();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SankhyaAvyayaContext extends ParserRuleContext {
		public TerminalNode ADHIKA() { return getToken(VyakaranamParser.ADHIKA, 0); }
		public TerminalNode UNA() { return getToken(VyakaranamParser.UNA, 0); }
		public TerminalNode SAKRIT() { return getToken(VyakaranamParser.SAKRIT, 0); }
		public TerminalNode DVIH() { return getToken(VyakaranamParser.DVIH, 0); }
		public TerminalNode TRIH() { return getToken(VyakaranamParser.TRIH, 0); }
		public TerminalNode CHATUH() { return getToken(VyakaranamParser.CHATUH, 0); }
		public TerminalNode IDENTIFIER() { return getToken(VyakaranamParser.IDENTIFIER, 0); }
		public TerminalNode KRITVAS() { return getToken(VyakaranamParser.KRITVAS, 0); }
		public TerminalNode DHAA() { return getToken(VyakaranamParser.DHAA, 0); }
		public TerminalNode SHAH() { return getToken(VyakaranamParser.SHAH, 0); }
		public SankhyaAvyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaAvyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaAvyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaAvyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaAvyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaAvyayaContext sankhyaAvyaya() throws RecognitionException {
		SankhyaAvyayaContext _localctx = new SankhyaAvyayaContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_sankhyaAvyaya);
		try {
			setState(705);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(693);
				match(ADHIKA);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(694);
				match(UNA);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(695);
				match(SAKRIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(696);
				match(DVIH);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(697);
				match(TRIH);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(698);
				match(CHATUH);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(699);
				match(IDENTIFIER);
				setState(700);
				match(KRITVAS);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(701);
				match(IDENTIFIER);
				setState(702);
				match(DHAA);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(703);
				match(IDENTIFIER);
				setState(704);
				match(SHAH);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MulaAvyayaContext extends ParserRuleContext {
		public TerminalNode MAA() { return getToken(VyakaranamParser.MAA, 0); }
		public TerminalNode YAVAT() { return getToken(VyakaranamParser.YAVAT, 0); }
		public TerminalNode TAVAT() { return getToken(VyakaranamParser.TAVAT, 0); }
		public TerminalNode NA() { return getToken(VyakaranamParser.NA, 0); }
		public TerminalNode ITI() { return getToken(VyakaranamParser.ITI, 0); }
		public TerminalNode API() { return getToken(VyakaranamParser.API, 0); }
		public TerminalNode NI() { return getToken(VyakaranamParser.NI, 0); }
		public TerminalNode EVA() { return getToken(VyakaranamParser.EVA, 0); }
		public TerminalNode CHA() { return getToken(VyakaranamParser.CHA, 0); }
		public TerminalNode VAA() { return getToken(VyakaranamParser.VAA, 0); }
		public TerminalNode TU_AVYAYA() { return getToken(VyakaranamParser.TU_AVYAYA, 0); }
		public TerminalNode HI() { return getToken(VyakaranamParser.HI, 0); }
		public TerminalNode KHALU() { return getToken(VyakaranamParser.KHALU, 0); }
		public TerminalNode NANU() { return getToken(VyakaranamParser.NANU, 0); }
		public TerminalNode ATHA() { return getToken(VyakaranamParser.ATHA, 0); }
		public TerminalNode TATAH() { return getToken(VyakaranamParser.TATAH, 0); }
		public TerminalNode ANANTARAM() { return getToken(VyakaranamParser.ANANTARAM, 0); }
		public TerminalNode KINTU() { return getToken(VyakaranamParser.KINTU, 0); }
		public TerminalNode ATAH() { return getToken(VyakaranamParser.ATAH, 0); }
		public TerminalNode YATAH() { return getToken(VyakaranamParser.YATAH, 0); }
		public TerminalNode YATHA() { return getToken(VyakaranamParser.YATHA, 0); }
		public TerminalNode TATHA() { return getToken(VyakaranamParser.TATHA, 0); }
		public TerminalNode YADA() { return getToken(VyakaranamParser.YADA, 0); }
		public TerminalNode TADA() { return getToken(VyakaranamParser.TADA, 0); }
		public TerminalNode YATRA() { return getToken(VyakaranamParser.YATRA, 0); }
		public TerminalNode TATRA() { return getToken(VyakaranamParser.TATRA, 0); }
		public TerminalNode KADA() { return getToken(VyakaranamParser.KADA, 0); }
		public TerminalNode KUTRA() { return getToken(VyakaranamParser.KUTRA, 0); }
		public TerminalNode SARVATRA() { return getToken(VyakaranamParser.SARVATRA, 0); }
		public TerminalNode KATHAM() { return getToken(VyakaranamParser.KATHAM, 0); }
		public TerminalNode KUTAH() { return getToken(VyakaranamParser.KUTAH, 0); }
		public TerminalNode KRPAYA() { return getToken(VyakaranamParser.KRPAYA, 0); }
		public TerminalNode SAHASAA() { return getToken(VyakaranamParser.SAHASAA, 0); }
		public TerminalNode SHANAIH() { return getToken(VyakaranamParser.SHANAIH, 0); }
		public TerminalNode PUNAH() { return getToken(VyakaranamParser.PUNAH, 0); }
		public TerminalNode NYUNATAYA() { return getToken(VyakaranamParser.NYUNATAYA, 0); }
		public TerminalNode ADYA() { return getToken(VyakaranamParser.ADYA, 0); }
		public TerminalNode SHVAH() { return getToken(VyakaranamParser.SHVAH, 0); }
		public TerminalNode HYAH() { return getToken(VyakaranamParser.HYAH, 0); }
		public TerminalNode INTERJECTION() { return getToken(VyakaranamParser.INTERJECTION, 0); }
		public MulaAvyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mulaAvyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterMulaAvyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitMulaAvyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitMulaAvyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MulaAvyayaContext mulaAvyaya() throws RecognitionException {
		MulaAvyayaContext _localctx = new MulaAvyayaContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_mulaAvyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(707);
			_la = _input.LA(1);
			if ( !(((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & 288371182359543807L) != 0) || _la==YAVAT || _la==TAVAT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayaTaddhitantaContext extends ParserRuleContext {
		public MulaPratipadikaContext mulaPratipadika() {
			return getRuleContext(MulaPratipadikaContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(VyakaranamParser.PLUS, 0); }
		public AvyayaTaddhitaPratyayaContext avyayaTaddhitaPratyaya() {
			return getRuleContext(AvyayaTaddhitaPratyayaContext.class,0);
		}
		public AvyayaTaddhitantaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayaTaddhitanta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayaTaddhitanta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayaTaddhitanta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayaTaddhitanta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayaTaddhitantaContext avyayaTaddhitanta() throws RecognitionException {
		AvyayaTaddhitantaContext _localctx = new AvyayaTaddhitantaContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_avyayaTaddhitanta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(709);
			mulaPratipadika();
			setState(710);
			match(PLUS);
			setState(711);
			avyayaTaddhitaPratyaya();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayaTaddhitaPratyayaContext extends ParserRuleContext {
		public TerminalNode TASIL() { return getToken(VyakaranamParser.TASIL, 0); }
		public TerminalNode TRA() { return getToken(VyakaranamParser.TRA, 0); }
		public TerminalNode HA() { return getToken(VyakaranamParser.HA, 0); }
		public TerminalNode DAA() { return getToken(VyakaranamParser.DAA, 0); }
		public TerminalNode THAAL() { return getToken(VyakaranamParser.THAAL, 0); }
		public TerminalNode THAMU() { return getToken(VyakaranamParser.THAMU, 0); }
		public TerminalNode VAT() { return getToken(VyakaranamParser.VAT, 0); }
		public TerminalNode DHAA() { return getToken(VyakaranamParser.DHAA, 0); }
		public AvyayaTaddhitaPratyayaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayaTaddhitaPratyaya; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayaTaddhitaPratyaya(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayaTaddhitaPratyaya(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayaTaddhitaPratyaya(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayaTaddhitaPratyayaContext avyayaTaddhitaPratyaya() throws RecognitionException {
		AvyayaTaddhitaPratyayaContext _localctx = new AvyayaTaddhitaPratyayaContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_avyayaTaddhitaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(713);
			_la = _input.LA(1);
			if ( !(((((_la - 184)) & ~0x3f) == 0 && ((1L << (_la - 184)) & 1082335952897L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AvyayibhavaPadaContext extends ParserRuleContext {
		public SamasaPratipadikaContext samasaPratipadika() {
			return getRuleContext(SamasaPratipadikaContext.class,0);
		}
		public AvyayibhavaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avyayibhavaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterAvyayibhavaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitAvyayibhavaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitAvyayibhavaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvyayibhavaPadaContext avyayibhavaPada() throws RecognitionException {
		AvyayibhavaPadaContext _localctx = new AvyayibhavaPadaContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_avyayibhavaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(715);
			samasaPratipadika();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u00f6\u02ce\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u00aa\b\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u00b0\b\u0000\n\u0000\f\u0000"+
		"\u00b3\t\u0000\u0001\u0000\u0003\u0000\u00b6\b\u0000\u0001\u0000\u0001"+
		"\u0000\u0003\u0000\u00ba\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u00c0\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002\u00c7\b\u0002\n\u0002\f\u0002\u00ca\t\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00cf\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0004\u0003\u00d5\b\u0003\u000b\u0003"+
		"\f\u0003\u00d6\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003"+
		"\u00dd\b\u0003\n\u0003\f\u0003\u00e0\t\u0003\u0001\u0003\u0003\u0003\u00e3"+
		"\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004\u00f8\b\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004\u00fc\b\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u0100\b\u0004"+
		"\u0001\u0004\u0003\u0004\u0103\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u010c\b\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0004\u0007\u0117\b\u0007\u000b\u0007"+
		"\f\u0007\u0118\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007\u011e\b"+
		"\u0007\u000b\u0007\f\u0007\u011f\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u0126\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u0135\b\u000b\u0001\u000b\u0003\u000b\u0138\b"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0003\f\u0143\b\f\u0003\f\u0145\b\f\u0001\r\u0001\r\u0003"+
		"\r\u0149\b\r\u0001\u000e\u0001\u000e\u0003\u000e\u014d\b\u000e\u0001\u000f"+
		"\u0005\u000f\u0150\b\u000f\n\u000f\f\u000f\u0153\t\u000f\u0001\u000f\u0001"+
		"\u000f\u0005\u000f\u0157\b\u000f\n\u000f\f\u000f\u015a\t\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0004\u0012\u0161\b\u0012"+
		"\u000b\u0012\f\u0012\u0162\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0168\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0004\u0015\u0175\b\u0015\u000b\u0015\f\u0015\u0176\u0001\u0015\u0001"+
		"\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0004\u0016\u017e\b\u0016\u000b"+
		"\u0016\f\u0016\u017f\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0190\b\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u0197"+
		"\b\u001b\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0003\u001d\u019f\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0004"+
		"\u001e\u01a4\b\u001e\u000b\u001e\f\u001e\u01a5\u0001\u001e\u0001\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0004\u001f\u01ad\b\u001f\u000b\u001f"+
		"\f\u001f\u01ae\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001 "+
		"\u0001 \u0001!\u0001!\u0001!\u0004!\u01ba\b!\u000b!\f!\u01bb\u0001!\u0001"+
		"!\u0001!\u0001!\u0001!\u0004!\u01c3\b!\u000b!\f!\u01c4\u0001!\u0001!\u0001"+
		"!\u0001!\u0001!\u0004!\u01cc\b!\u000b!\f!\u01cd\u0001!\u0001!\u0003!\u01d2"+
		"\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001$\u0001$\u0001$\u0004$\u01e1\b$\u000b$\f$\u01e2\u0001$\u0001"+
		"$\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001\'\u0001\'\u0005\'\u01ef"+
		"\b\'\n\'\f\'\u01f2\t\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0003(\u01fd\b(\u0001)\u0001)\u0001)\u0001)\u0003)\u0203"+
		"\b)\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u020d"+
		"\b+\u0001,\u0003,\u0210\b,\u0001,\u0001,\u0001,\u0001,\u0001-\u0003-\u0217"+
		"\b-\u0001-\u0001-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		"/\u0001/\u00010\u00010\u00011\u00011\u00011\u00041\u0229\b1\u000b1\f1"+
		"\u022a\u00012\u00012\u00032\u022f\b2\u00013\u00013\u00013\u00013\u0001"+
		"3\u00014\u00014\u00015\u00015\u00055\u023a\b5\n5\f5\u023d\t5\u00016\u0001"+
		"6\u00016\u00016\u00016\u00016\u00016\u00016\u00036\u0247\b6\u00017\u0001"+
		"7\u00037\u024b\b7\u00017\u00047\u024e\b7\u000b7\f7\u024f\u00017\u0001"+
		"7\u00018\u00018\u00018\u00058\u0257\b8\n8\f8\u025a\t8\u00019\u00019\u0001"+
		":\u0001:\u0001;\u0001;\u0001;\u0001;\u0001;\u0005;\u0265\b;\n;\f;\u0268"+
		"\t;\u0001<\u0001<\u0001=\u0003=\u026d\b=\u0001=\u0001=\u0001=\u0003=\u0272"+
		"\b=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001>\u0001>\u0001?\u0001?\u0001?\u0005?\u0283\b?\n?\f?\u0286\t?\u0001"+
		"@\u0001@\u0001@\u0001@\u0001@\u0001@\u0003@\u028e\b@\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001B\u0001B\u0001B\u0001B\u0001B\u0001C\u0001C\u0001"+
		"D\u0001D\u0001E\u0001E\u0001F\u0001F\u0001G\u0001G\u0001H\u0001H\u0001"+
		"I\u0001I\u0001J\u0003J\u02a9\bJ\u0001J\u0001J\u0001J\u0001J\u0001K\u0001"+
		"K\u0001K\u0001K\u0001K\u0003K\u02b4\bK\u0001L\u0001L\u0001L\u0001L\u0001"+
		"L\u0001L\u0001L\u0001L\u0001L\u0001L\u0001L\u0001L\u0003L\u02c2\bL\u0001"+
		"M\u0001M\u0001N\u0001N\u0001N\u0001N\u0001O\u0001O\u0001P\u0001P\u0001"+
		"P\u0000\u0000Q\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016"+
		"\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprt"+
		"vxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094"+
		"\u0096\u0098\u009a\u009c\u009e\u00a0\u0000\u0015\u0002\u0000xx{|\u0001"+
		"\u0000tv\u0003\u0000\u0004\u0004\t\u0010\u0013\u0013\u0001\u0000\u0007"+
		"\b\u0002\u0000dd\u00cb\u00cd\u0002\u0000-.\u00f4\u00f4\u0006\u0000\u00a4"+
		"\u00a4\u00b3\u00b3\u00c2\u00ca\u00ce\u00d9\u00db\u00db\u00de\u00df\u0001"+
		"\u0000\u00e0\u00e7\u0001\u0000\u00e8\u00eb\u0005\u0000\n\nFF\u00db\u00db"+
		"\u00df\u00df\u00f4\u00f4\u0001\u0000MT\u0002\u0000\u0014\u00149L\u0001"+
		"\u0000U^\u0001\u0000_p\u0001\u0000q\u0081\u0001\u0000\u0082\u0091\u0002"+
		"\u0000nn\u0092\u009f\u0001\u0000\u00a0\u00bb\u0001\u0000\u00bc\u00c1\u0004"+
		"\u0000\t,88CC\u00f2\u00f3\u0003\u0000\u00b8\u00b8\u00ce\u00ce\u00da\u00df"+
		"\u02de\u0000\u00b9\u0001\u0000\u0000\u0000\u0002\u00bb\u0001\u0000\u0000"+
		"\u0000\u0004\u00c3\u0001\u0000\u0000\u0000\u0006\u00d2\u0001\u0000\u0000"+
		"\u0000\b\u00f7\u0001\u0000\u0000\u0000\n\u010b\u0001\u0000\u0000\u0000"+
		"\f\u010d\u0001\u0000\u0000\u0000\u000e\u0116\u0001\u0000\u0000\u0000\u0010"+
		"\u0129\u0001\u0000\u0000\u0000\u0012\u012c\u0001\u0000\u0000\u0000\u0014"+
		"\u012f\u0001\u0000\u0000\u0000\u0016\u0131\u0001\u0000\u0000\u0000\u0018"+
		"\u013b\u0001\u0000\u0000\u0000\u001a\u0148\u0001\u0000\u0000\u0000\u001c"+
		"\u014c\u0001\u0000\u0000\u0000\u001e\u0151\u0001\u0000\u0000\u0000 \u015b"+
		"\u0001\u0000\u0000\u0000\"\u015d\u0001\u0000\u0000\u0000$\u0160\u0001"+
		"\u0000\u0000\u0000&\u0167\u0001\u0000\u0000\u0000(\u0169\u0001\u0000\u0000"+
		"\u0000*\u0174\u0001\u0000\u0000\u0000,\u017d\u0001\u0000\u0000\u0000."+
		"\u0183\u0001\u0000\u0000\u00000\u0185\u0001\u0000\u0000\u00002\u018f\u0001"+
		"\u0000\u0000\u00004\u0191\u0001\u0000\u0000\u00006\u0193\u0001\u0000\u0000"+
		"\u00008\u0198\u0001\u0000\u0000\u0000:\u019e\u0001\u0000\u0000\u0000<"+
		"\u01a3\u0001\u0000\u0000\u0000>\u01ac\u0001\u0000\u0000\u0000@\u01b4\u0001"+
		"\u0000\u0000\u0000B\u01d1\u0001\u0000\u0000\u0000D\u01d3\u0001\u0000\u0000"+
		"\u0000F\u01d8\u0001\u0000\u0000\u0000H\u01dd\u0001\u0000\u0000\u0000J"+
		"\u01e6\u0001\u0000\u0000\u0000L\u01e8\u0001\u0000\u0000\u0000N\u01ec\u0001"+
		"\u0000\u0000\u0000P\u01fc\u0001\u0000\u0000\u0000R\u0202\u0001\u0000\u0000"+
		"\u0000T\u0204\u0001\u0000\u0000\u0000V\u020c\u0001\u0000\u0000\u0000X"+
		"\u020f\u0001\u0000\u0000\u0000Z\u0216\u0001\u0000\u0000\u0000\\\u021c"+
		"\u0001\u0000\u0000\u0000^\u0221\u0001\u0000\u0000\u0000`\u0223\u0001\u0000"+
		"\u0000\u0000b\u0225\u0001\u0000\u0000\u0000d\u022c\u0001\u0000\u0000\u0000"+
		"f\u0230\u0001\u0000\u0000\u0000h\u0235\u0001\u0000\u0000\u0000j\u0237"+
		"\u0001\u0000\u0000\u0000l\u0246\u0001\u0000\u0000\u0000n\u0248\u0001\u0000"+
		"\u0000\u0000p\u0253\u0001\u0000\u0000\u0000r\u025b\u0001\u0000\u0000\u0000"+
		"t\u025d\u0001\u0000\u0000\u0000v\u025f\u0001\u0000\u0000\u0000x\u0269"+
		"\u0001\u0000\u0000\u0000z\u026c\u0001\u0000\u0000\u0000|\u0278\u0001\u0000"+
		"\u0000\u0000~\u027f\u0001\u0000\u0000\u0000\u0080\u028d\u0001\u0000\u0000"+
		"\u0000\u0082\u028f\u0001\u0000\u0000\u0000\u0084\u0294\u0001\u0000\u0000"+
		"\u0000\u0086\u0299\u0001\u0000\u0000\u0000\u0088\u029b\u0001\u0000\u0000"+
		"\u0000\u008a\u029d\u0001\u0000\u0000\u0000\u008c\u029f\u0001\u0000\u0000"+
		"\u0000\u008e\u02a1\u0001\u0000\u0000\u0000\u0090\u02a3\u0001\u0000\u0000"+
		"\u0000\u0092\u02a5\u0001\u0000\u0000\u0000\u0094\u02a8\u0001\u0000\u0000"+
		"\u0000\u0096\u02b3\u0001\u0000\u0000\u0000\u0098\u02c1\u0001\u0000\u0000"+
		"\u0000\u009a\u02c3\u0001\u0000\u0000\u0000\u009c\u02c5\u0001\u0000\u0000"+
		"\u0000\u009e\u02c9\u0001\u0000\u0000\u0000\u00a0\u02cb\u0001\u0000\u0000"+
		"\u0000\u00a2\u00ba\u0003\u0002\u0001\u0000\u00a3\u00ba\u0003\b\u0004\u0000"+
		"\u00a4\u00ba\u0003\u0004\u0002\u0000\u00a5\u00ba\u0003\u0006\u0003\u0000"+
		"\u00a6\u00ba\u0003\u000e\u0007\u0000\u00a7\u00ba\u0003\u0016\u000b\u0000"+
		"\u00a8\u00aa\u00036\u001b\u0000\u00a9\u00a8\u0001\u0000\u0000\u0000\u00a9"+
		"\u00aa\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab"+
		"\u00b1\u0003\u001c\u000e\u0000\u00ac\u00ad\u00034\u001a\u0000\u00ad\u00ae"+
		"\u0003\u001c\u000e\u0000\u00ae\u00b0\u0001\u0000\u0000\u0000\u00af\u00ac"+
		"\u0001\u0000\u0000\u0000\u00b0\u00b3\u0001\u0000\u0000\u0000\u00b1\u00af"+
		"\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\u00b5"+
		"\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4\u00b6"+
		"\u0005\u0004\u0000\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b8"+
		"\u0005\u0000\u0000\u0001\u00b8\u00ba\u0001\u0000\u0000\u0000\u00b9\u00a2"+
		"\u0001\u0000\u0000\u0000\u00b9\u00a3\u0001\u0000\u0000\u0000\u00b9\u00a4"+
		"\u0001\u0000\u0000\u0000\u00b9\u00a5\u0001\u0000\u0000\u0000\u00b9\u00a6"+
		"\u0001\u0000\u0000\u0000\u00b9\u00a7\u0001\u0000\u0000\u0000\u00b9\u00a9"+
		"\u0001\u0000\u0000\u0000\u00ba\u0001\u0001\u0000\u0000\u0000\u00bb\u00bc"+
		"\u0003\u001c\u000e\u0000\u00bc\u00bd\u0005\u0013\u0000\u0000\u00bd\u00bf"+
		"\u0003\u001e\u000f\u0000\u00be\u00c0\u0005\u0004\u0000\u0000\u00bf\u00be"+
		"\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c1"+
		"\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005\u0000\u0000\u0001\u00c2\u0003"+
		"\u0001\u0000\u0000\u0000\u00c3\u00c8\u0003\u001e\u000f\u0000\u00c4\u00c5"+
		"\u0005\f\u0000\u0000\u00c5\u00c7\u0003\u001e\u000f\u0000\u00c6\u00c4\u0001"+
		"\u0000\u0000\u0000\u00c7\u00ca\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00cb\u0001"+
		"\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00cb\u00cc\u0005"+
		"\f\u0000\u0000\u00cc\u00ce\u0003\u0018\f\u0000\u00cd\u00cf\u0005\u0004"+
		"\u0000\u0000\u00ce\u00cd\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000"+
		"\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005\u0000"+
		"\u0000\u0001\u00d1\u0005\u0001\u0000\u0000\u0000\u00d2\u00d4\u0003L&\u0000"+
		"\u00d3\u00d5\u0003L&\u0000\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d5\u00d6"+
		"\u0001\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7"+
		"\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8\u00d9"+
		"\u0005\f\u0000\u0000\u00d9\u00de\u0003\u001e\u000f\u0000\u00da\u00db\u0005"+
		"\f\u0000\u0000\u00db\u00dd\u0003\u001e\u000f\u0000\u00dc\u00da\u0001\u0000"+
		"\u0000\u0000\u00dd\u00e0\u0001\u0000\u0000\u0000\u00de\u00dc\u0001\u0000"+
		"\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e0\u00de\u0001\u0000\u0000\u0000\u00e1\u00e3\u0005\u0004"+
		"\u0000\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4\u00e5\u0005\u0000"+
		"\u0000\u0001\u00e5\u0007\u0001\u0000\u0000\u0000\u00e6\u00e7\u0003B!\u0000"+
		"\u00e7\u00e8\u0005\u00f2\u0000\u0000\u00e8\u00e9\u0003\u001c\u000e\u0000"+
		"\u00e9\u00ea\u0005\u00f3\u0000\u0000\u00ea\u00eb\u0003\u001c\u000e\u0000"+
		"\u00eb\u00f8\u0001\u0000\u0000\u0000\u00ec\u00ed\u0005\u00f2\u0000\u0000"+
		"\u00ed\u00ee\u0003\u001c\u000e\u0000\u00ee\u00ef\u0005\u00f3\u0000\u0000"+
		"\u00ef\u00f0\u0003\f\u0006\u0000\u00f0\u00f1\u0003\u001c\u000e\u0000\u00f1"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f2\u00f3\u0005\u00f2\u0000\u0000\u00f3"+
		"\u00f4\u0003\u001c\u000e\u0000\u00f4\u00f5\u0005\u00f3\u0000\u0000\u00f5"+
		"\u00f6\u0003\u001c\u000e\u0000\u00f6\u00f8\u0001\u0000\u0000\u0000\u00f7"+
		"\u00e6\u0001\u0000\u0000\u0000\u00f7\u00ec\u0001\u0000\u0000\u0000\u00f7"+
		"\u00f2\u0001\u0000\u0000\u0000\u00f8\u00fb\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0005\u00f1\u0000\u0000\u00fa\u00fc\u0003\n\u0005\u0000\u00fb\u00f9"+
		"\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc\u00ff"+
		"\u0001\u0000\u0000\u0000\u00fd\u00fe\u0005\f\u0000\u0000\u00fe\u0100\u0003"+
		"\u001c\u000e\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u00ff\u0100\u0001"+
		"\u0000\u0000\u0000\u0100\u0102\u0001\u0000\u0000\u0000\u0101\u0103\u0005"+
		"\u0004\u0000\u0000\u0102\u0101\u0001\u0000\u0000\u0000\u0102\u0103\u0001"+
		"\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000\u0104\u0105\u0005"+
		"\u0000\u0000\u0001\u0105\t\u0001\u0000\u0000\u0000\u0106\u0107\u0003\u001c"+
		"\u000e\u0000\u0107\u0108\u0005\u0013\u0000\u0000\u0108\u0109\u0003\u001c"+
		"\u000e\u0000\u0109\u010c\u0001\u0000\u0000\u0000\u010a\u010c\u0003\u001c"+
		"\u000e\u0000\u010b\u0106\u0001\u0000\u0000\u0000\u010b\u010a\u0001\u0000"+
		"\u0000\u0000\u010c\u000b\u0001\u0000\u0000\u0000\u010d\u010e\u0003>\u001f"+
		"\u0000\u010e\u010f\u0003L&\u0000\u010f\u0110\u0005J\u0000\u0000\u0110"+
		"\u0111\u0005\u0001\u0000\u0000\u0111\u0112\u0005\u00f4\u0000\u0000\u0112"+
		"\u0113\u0005\u0001\u0000\u0000\u0113\u0114\u0005t\u0000\u0000\u0114\r"+
		"\u0001\u0000\u0000\u0000\u0115\u0117\u0003L&\u0000\u0116\u0115\u0001\u0000"+
		"\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000"+
		"\u0000\u0000\u0118\u0119\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000"+
		"\u0000\u0000\u011a\u011b\u0005\t\u0000\u0000\u011b\u011d\u0003\u0010\b"+
		"\u0000\u011c\u011e\u0003\u0010\b\u0000\u011d\u011c\u0001\u0000\u0000\u0000"+
		"\u011e\u011f\u0001\u0000\u0000\u0000\u011f\u011d\u0001\u0000\u0000\u0000"+
		"\u011f\u0120\u0001\u0000\u0000\u0000\u0120\u0121\u0001\u0000\u0000\u0000"+
		"\u0121\u0122\u0003\u0012\t\u0000\u0122\u0123\u0003\u0014\n\u0000\u0123"+
		"\u0125\u0003z=\u0000\u0124\u0126\u0005\u0004\u0000\u0000\u0125\u0124\u0001"+
		"\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126\u0127\u0001"+
		"\u0000\u0000\u0000\u0127\u0128\u0005\u0000\u0000\u0001\u0128\u000f\u0001"+
		"\u0000\u0000\u0000\u0129\u012a\u0003L&\u0000\u012a\u012b\u0003L&\u0000"+
		"\u012b\u0011\u0001\u0000\u0000\u0000\u012c\u012d\u0003L&\u0000\u012d\u012e"+
		"\u0003L&\u0000\u012e\u0013\u0001\u0000\u0000\u0000\u012f\u0130\u0003L"+
		"&\u0000\u0130\u0015\u0001\u0000\u0000\u0000\u0131\u0134\u0003\u0018\f"+
		"\u0000\u0132\u0133\u0005\f\u0000\u0000\u0133\u0135\u0003\u001c\u000e\u0000"+
		"\u0134\u0132\u0001\u0000\u0000\u0000\u0134\u0135\u0001\u0000\u0000\u0000"+
		"\u0135\u0137\u0001\u0000\u0000\u0000\u0136\u0138\u0005\u0004\u0000\u0000"+
		"\u0137\u0136\u0001\u0000\u0000\u0000\u0137\u0138\u0001\u0000\u0000\u0000"+
		"\u0138\u0139\u0001\u0000\u0000\u0000\u0139\u013a\u0005\u0000\u0000\u0001"+
		"\u013a\u0017\u0001\u0000\u0000\u0000\u013b\u013c\u0005\u00ef\u0000\u0000"+
		"\u013c\u013d\u0003\u001c\u000e\u0000\u013d\u013e\u0005\u00f0\u0000\u0000"+
		"\u013e\u0144\u0003\u001a\r\u0000\u013f\u0142\u0005\u00f1\u0000\u0000\u0140"+
		"\u0143\u0003\u0018\f\u0000\u0141\u0143\u0003\u001a\r\u0000\u0142\u0140"+
		"\u0001\u0000\u0000\u0000\u0142\u0141\u0001\u0000\u0000\u0000\u0143\u0145"+
		"\u0001\u0000\u0000\u0000\u0144\u013f\u0001\u0000\u0000\u0000\u0144\u0145"+
		"\u0001\u0000\u0000\u0000\u0145\u0019\u0001\u0000\u0000\u0000\u0146\u0149"+
		"\u0003N\'\u0000\u0147\u0149\u0003\u001c\u000e\u0000\u0148\u0146\u0001"+
		"\u0000\u0000\u0000\u0148\u0147\u0001\u0000\u0000\u0000\u0149\u001b\u0001"+
		"\u0000\u0000\u0000\u014a\u014d\u0003\u001e\u000f\u0000\u014b\u014d\u0003"+
		"$\u0012\u0000\u014c\u014a\u0001\u0000\u0000\u0000\u014c\u014b\u0001\u0000"+
		"\u0000\u0000\u014d\u001d\u0001\u0000\u0000\u0000\u014e\u0150\u0003 \u0010"+
		"\u0000\u014f\u014e\u0001\u0000\u0000\u0000\u0150\u0153\u0001\u0000\u0000"+
		"\u0000\u0151\u014f\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000"+
		"\u0000\u0152\u0154\u0001\u0000\u0000\u0000\u0153\u0151\u0001\u0000\u0000"+
		"\u0000\u0154\u0158\u0003z=\u0000\u0155\u0157\u0003\"\u0011\u0000\u0156"+
		"\u0155\u0001\u0000\u0000\u0000\u0157\u015a\u0001\u0000\u0000\u0000\u0158"+
		"\u0156\u0001\u0000\u0000\u0000\u0158\u0159\u0001\u0000\u0000\u0000\u0159"+
		"\u001f\u0001\u0000\u0000\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015b"+
		"\u015c\u0003&\u0013\u0000\u015c!\u0001\u0000\u0000\u0000\u015d\u015e\u0003"+
		"&\u0013\u0000\u015e#\u0001\u0000\u0000\u0000\u015f\u0161\u0003&\u0013"+
		"\u0000\u0160\u015f\u0001\u0000\u0000\u0000\u0161\u0162\u0001\u0000\u0000"+
		"\u0000\u0162\u0160\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000"+
		"\u0000\u0163%\u0001\u0000\u0000\u0000\u0164\u0168\u0003(\u0014\u0000\u0165"+
		"\u0168\u00032\u0019\u0000\u0166\u0168\u0003\u0096K\u0000\u0167\u0164\u0001"+
		"\u0000\u0000\u0000\u0167\u0165\u0001\u0000\u0000\u0000\u0167\u0166\u0001"+
		"\u0000\u0000\u0000\u0168\'\u0001\u0000\u0000\u0000\u0169\u016a\u0003*"+
		"\u0015\u0000\u016a\u016b\u0003,\u0016\u0000\u016b\u016c\u0005J\u0000\u0000"+
		"\u016c\u016d\u0005\u0001\u0000\u0000\u016d\u016e\u0005\u00f4\u0000\u0000"+
		"\u016e\u016f\u0005\u0001\u0000\u0000\u016f\u0170\u0005t\u0000\u0000\u0170"+
		")\u0001\u0000\u0000\u0000\u0171\u0172\u0003J%\u0000\u0172\u0173\u0005"+
		"\u0001\u0000\u0000\u0173\u0175\u0001\u0000\u0000\u0000\u0174\u0171\u0001"+
		"\u0000\u0000\u0000\u0175\u0176\u0001\u0000\u0000\u0000\u0176\u0174\u0001"+
		"\u0000\u0000\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177\u0178\u0001"+
		"\u0000\u0000\u0000\u0178\u0179\u0003.\u0017\u0000\u0179+\u0001\u0000\u0000"+
		"\u0000\u017a\u017b\u0003J%\u0000\u017b\u017c\u0005\u0001\u0000\u0000\u017c"+
		"\u017e\u0001\u0000\u0000\u0000\u017d\u017a\u0001\u0000\u0000\u0000\u017e"+
		"\u017f\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u017f"+
		"\u0180\u0001\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181"+
		"\u0182\u00030\u0018\u0000\u0182-\u0001\u0000\u0000\u0000\u0183\u0184\u0007"+
		"\u0000\u0000\u0000\u0184/\u0001\u0000\u0000\u0000\u0185\u0186\u0007\u0001"+
		"\u0000\u0000\u01861\u0001\u0000\u0000\u0000\u0187\u0190\u0003L&\u0000"+
		"\u0188\u0190\u0003n7\u0000\u0189\u0190\u0003<\u001e\u0000\u018a\u0190"+
		"\u0003>\u001f\u0000\u018b\u0190\u0003B!\u0000\u018c\u0190\u0003D\"\u0000"+
		"\u018d\u0190\u0003F#\u0000\u018e\u0190\u0003H$\u0000\u018f\u0187\u0001"+
		"\u0000\u0000\u0000\u018f\u0188\u0001\u0000\u0000\u0000\u018f\u0189\u0001"+
		"\u0000\u0000\u0000\u018f\u018a\u0001\u0000\u0000\u0000\u018f\u018b\u0001"+
		"\u0000\u0000\u0000\u018f\u018c\u0001\u0000\u0000\u0000\u018f\u018d\u0001"+
		"\u0000\u0000\u0000\u018f\u018e\u0001\u0000\u0000\u0000\u01903\u0001\u0000"+
		"\u0000\u0000\u0191\u0192\u0007\u0002\u0000\u0000\u01925\u0001\u0000\u0000"+
		"\u0000\u0193\u0194\u00038\u001c\u0000\u0194\u0196\u0003L&\u0000\u0195"+
		"\u0197\u0005\u0003\u0000\u0000\u0196\u0195\u0001\u0000\u0000\u0000\u0196"+
		"\u0197\u0001\u0000\u0000\u0000\u01977\u0001\u0000\u0000\u0000\u0198\u0199"+
		"\u0007\u0003\u0000\u0000\u01999\u0001\u0000\u0000\u0000\u019a\u019f\u0003"+
		"L&\u0000\u019b\u019f\u0003z=\u0000\u019c\u019f\u0003\u0096K\u0000\u019d"+
		"\u019f\u0003<\u001e\u0000\u019e\u019a\u0001\u0000\u0000\u0000\u019e\u019b"+
		"\u0001\u0000\u0000\u0000\u019e\u019c\u0001\u0000\u0000\u0000\u019e\u019d"+
		"\u0001\u0000\u0000\u0000\u019f;\u0001\u0000\u0000\u0000\u01a0\u01a1\u0003"+
		"J%\u0000\u01a1\u01a2\u0005\u0001\u0000\u0000\u01a2\u01a4\u0001\u0000\u0000"+
		"\u0000\u01a3\u01a0\u0001\u0000\u0000\u0000\u01a4\u01a5\u0001\u0000\u0000"+
		"\u0000\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a5\u01a6\u0001\u0000\u0000"+
		"\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a8\u0003\u008aE\u0000"+
		"\u01a8=\u0001\u0000\u0000\u0000\u01a9\u01aa\u0003J%\u0000\u01aa\u01ab"+
		"\u0005\u0001\u0000\u0000\u01ab\u01ad\u0001\u0000\u0000\u0000\u01ac\u01a9"+
		"\u0001\u0000\u0000\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01ac"+
		"\u0001\u0000\u0000\u0000\u01ae\u01af\u0001\u0000\u0000\u0000\u01af\u01b0"+
		"\u0001\u0000\u0000\u0000\u01b0\u01b1\u0003@ \u0000\u01b1\u01b2\u0005\u0001"+
		"\u0000\u0000\u01b2\u01b3\u0003\u008aE\u0000\u01b3?\u0001\u0000\u0000\u0000"+
		"\u01b4\u01b5\u0007\u0004\u0000\u0000\u01b5A\u0001\u0000\u0000\u0000\u01b6"+
		"\u01b7\u0003J%\u0000\u01b7\u01b8\u0005\u0001\u0000\u0000\u01b8\u01ba\u0001"+
		"\u0000\u0000\u0000\u01b9\u01b6\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001"+
		"\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001"+
		"\u0000\u0000\u0000\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bd\u01be\u0005"+
		"3\u0000\u0000\u01be\u01d2\u0001\u0000\u0000\u0000\u01bf\u01c0\u0003J%"+
		"\u0000\u01c0\u01c1\u0005\u0001\u0000\u0000\u01c1\u01c3\u0001\u0000\u0000"+
		"\u0000\u01c2\u01bf\u0001\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000"+
		"\u0000\u01c4\u01c2\u0001\u0000\u0000\u0000\u01c4\u01c5\u0001\u0000\u0000"+
		"\u0000\u01c5\u01c6\u0001\u0000\u0000\u0000\u01c6\u01c7\u00054\u0000\u0000"+
		"\u01c7\u01d2\u0001\u0000\u0000\u0000\u01c8\u01c9\u0003J%\u0000\u01c9\u01ca"+
		"\u0005\u0001\u0000\u0000\u01ca\u01cc\u0001\u0000\u0000\u0000\u01cb\u01c8"+
		"\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd\u01cb"+
		"\u0001\u0000\u0000\u0000\u01cd\u01ce\u0001\u0000\u0000\u0000\u01ce\u01cf"+
		"\u0001\u0000\u0000\u0000\u01cf\u01d0\u0005\u00df\u0000\u0000\u01d0\u01d2"+
		"\u0001\u0000\u0000\u0000\u01d1\u01b9\u0001\u0000\u0000\u0000\u01d1\u01c2"+
		"\u0001\u0000\u0000\u0000\u01d1\u01cb\u0001\u0000\u0000\u0000\u01d2C\u0001"+
		"\u0000\u0000\u0000\u01d3\u01d4\u00055\u0000\u0000\u01d4\u01d5\u0005\u00f4"+
		"\u0000\u0000\u01d5\u01d6\u0005\u0001\u0000\u0000\u01d6\u01d7\u0003\u008a"+
		"E\u0000\u01d7E\u0001\u0000\u0000\u0000\u01d8\u01d9\u00056\u0000\u0000"+
		"\u01d9\u01da\u0005\u00f4\u0000\u0000\u01da\u01db\u0005\u0001\u0000\u0000"+
		"\u01db\u01dc\u0003\u008aE\u0000\u01dcG\u0001\u0000\u0000\u0000\u01dd\u01e0"+
		"\u00057\u0000\u0000\u01de\u01df\u0005\u00f4\u0000\u0000\u01df\u01e1\u0005"+
		"\u0001\u0000\u0000\u01e0\u01de\u0001\u0000\u0000\u0000\u01e1\u01e2\u0001"+
		"\u0000\u0000\u0000\u01e2\u01e0\u0001\u0000\u0000\u0000\u01e2\u01e3\u0001"+
		"\u0000\u0000\u0000\u01e3\u01e4\u0001\u0000\u0000\u0000\u01e4\u01e5\u0003"+
		"\u008aE\u0000\u01e5I\u0001\u0000\u0000\u0000\u01e6\u01e7\u0005\u00f4\u0000"+
		"\u0000\u01e7K\u0001\u0000\u0000\u0000\u01e8\u01e9\u0003N\'\u0000\u01e9"+
		"\u01ea\u0005\u0001\u0000\u0000\u01ea\u01eb\u0003\u008aE\u0000\u01ebM\u0001"+
		"\u0000\u0000\u0000\u01ec\u01f0\u0003P(\u0000\u01ed\u01ef\u0003R)\u0000"+
		"\u01ee\u01ed\u0001\u0000\u0000\u0000\u01ef\u01f2\u0001\u0000\u0000\u0000"+
		"\u01f0\u01ee\u0001\u0000\u0000\u0000\u01f0\u01f1\u0001\u0000\u0000\u0000"+
		"\u01f1O\u0001\u0000\u0000\u0000\u01f2\u01f0\u0001\u0000\u0000\u0000\u01f3"+
		"\u01fd\u0003T*\u0000\u01f4\u01fd\u0003V+\u0000\u01f5\u01fd\u0003X,\u0000"+
		"\u01f6\u01fd\u0003Z-\u0000\u01f7\u01fd\u0003b1\u0000\u01f8\u01f9\u0005"+
		"\u0005\u0000\u0000\u01f9\u01fa\u0003N\'\u0000\u01fa\u01fb\u0005\u0006"+
		"\u0000\u0000\u01fb\u01fd\u0001\u0000\u0000\u0000\u01fc\u01f3\u0001\u0000"+
		"\u0000\u0000\u01fc\u01f4\u0001\u0000\u0000\u0000\u01fc\u01f5\u0001\u0000"+
		"\u0000\u0000\u01fc\u01f6\u0001\u0000\u0000\u0000\u01fc\u01f7\u0001\u0000"+
		"\u0000\u0000\u01fc\u01f8\u0001\u0000\u0000\u0000\u01fdQ\u0001\u0000\u0000"+
		"\u0000\u01fe\u01ff\u0005\u0001\u0000\u0000\u01ff\u0203\u0003^/\u0000\u0200"+
		"\u0201\u0005\u0001\u0000\u0000\u0201\u0203\u0003`0\u0000\u0202\u01fe\u0001"+
		"\u0000\u0000\u0000\u0202\u0200\u0001\u0000\u0000\u0000\u0203S\u0001\u0000"+
		"\u0000\u0000\u0204\u0205\u0007\u0005\u0000\u0000\u0205U\u0001\u0000\u0000"+
		"\u0000\u0206\u0207\u0005C\u0000\u0000\u0207\u0208\u0005\u0001\u0000\u0000"+
		"\u0208\u020d\u0005\u00d9\u0000\u0000\u0209\u020a\u0005L\u0000\u0000\u020a"+
		"\u020b\u0005\u0001\u0000\u0000\u020b\u020d\u0005\u00f4\u0000\u0000\u020c"+
		"\u0206\u0001\u0000\u0000\u0000\u020c\u0209\u0001\u0000\u0000\u0000\u020d"+
		"W\u0001\u0000\u0000\u0000\u020e\u0210\u0003v;\u0000\u020f\u020e\u0001"+
		"\u0000\u0000\u0000\u020f\u0210\u0001\u0000\u0000\u0000\u0210\u0211\u0001"+
		"\u0000\u0000\u0000\u0211\u0212\u0003p8\u0000\u0212\u0213\u0005\u0001\u0000"+
		"\u0000\u0213\u0214\u0003\u0090H\u0000\u0214Y\u0001\u0000\u0000\u0000\u0215"+
		"\u0217\u0003v;\u0000\u0216\u0215\u0001\u0000\u0000\u0000\u0216\u0217\u0001"+
		"\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218\u0219\u0003"+
		"p8\u0000\u0219\u021a\u0005\u0001\u0000\u0000\u021a\u021b\u0003\\.\u0000"+
		"\u021b[\u0001\u0000\u0000\u0000\u021c\u021d\u0005\u00ee\u0000\u0000\u021d"+
		"\u021e\u0005\u0005\u0000\u0000\u021e\u021f\u0005\u00f4\u0000\u0000\u021f"+
		"\u0220\u0005\u0006\u0000\u0000\u0220]\u0001\u0000\u0000\u0000\u0221\u0222"+
		"\u0007\u0006\u0000\u0000\u0222_\u0001\u0000\u0000\u0000\u0223\u0224\u0007"+
		"\u0007\u0000\u0000\u0224a\u0001\u0000\u0000\u0000\u0225\u0228\u0003d2"+
		"\u0000\u0226\u0227\u0005\u0002\u0000\u0000\u0227\u0229\u0003d2\u0000\u0228"+
		"\u0226\u0001\u0000\u0000\u0000\u0229\u022a\u0001\u0000\u0000\u0000\u022a"+
		"\u0228\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000\u0000\u0000\u022b"+
		"c\u0001\u0000\u0000\u0000\u022c\u022e\u0003j5\u0000\u022d\u022f\u0003"+
		"f3\u0000\u022e\u022d\u0001\u0000\u0000\u0000\u022e\u022f\u0001\u0000\u0000"+
		"\u0000\u022fe\u0001\u0000\u0000\u0000\u0230\u0231\u0005\u0001\u0000\u0000"+
		"\u0231\u0232\u0003\u008aE\u0000\u0232\u0233\u0005\u0001\u0000\u0000\u0233"+
		"\u0234\u0003h4\u0000\u0234g\u0001\u0000\u0000\u0000\u0235\u0236\u0007"+
		"\b\u0000\u0000\u0236i\u0001\u0000\u0000\u0000\u0237\u023b\u0003l6\u0000"+
		"\u0238\u023a\u0003R)\u0000\u0239\u0238\u0001\u0000\u0000\u0000\u023a\u023d"+
		"\u0001\u0000\u0000\u0000\u023b\u0239\u0001\u0000\u0000\u0000\u023b\u023c"+
		"\u0001\u0000\u0000\u0000\u023ck\u0001\u0000\u0000\u0000\u023d\u023b\u0001"+
		"\u0000\u0000\u0000\u023e\u0247\u0003T*\u0000\u023f\u0247\u0003V+\u0000"+
		"\u0240\u0247\u0003X,\u0000\u0241\u0247\u0003Z-\u0000\u0242\u0243\u0005"+
		"\u0005\u0000\u0000\u0243\u0244\u0003b1\u0000\u0244\u0245\u0005\u0006\u0000"+
		"\u0000\u0245\u0247\u0001\u0000\u0000\u0000\u0246\u023e\u0001\u0000\u0000"+
		"\u0000\u0246\u023f\u0001\u0000\u0000\u0000\u0246\u0240\u0001\u0000\u0000"+
		"\u0000\u0246\u0241\u0001\u0000\u0000\u0000\u0246\u0242\u0001\u0000\u0000"+
		"\u0000\u0247m\u0001\u0000\u0000\u0000\u0248\u024d\u0003L&\u0000\u0249"+
		"\u024b\u0005\u0003\u0000\u0000\u024a\u0249\u0001\u0000\u0000\u0000\u024a"+
		"\u024b\u0001\u0000\u0000\u0000\u024b\u024c\u0001\u0000\u0000\u0000\u024c"+
		"\u024e\u0003L&\u0000\u024d\u024a\u0001\u0000\u0000\u0000\u024e\u024f\u0001"+
		"\u0000\u0000\u0000\u024f\u024d\u0001\u0000\u0000\u0000\u024f\u0250\u0001"+
		"\u0000\u0000\u0000\u0250\u0251\u0001\u0000\u0000\u0000\u0251\u0252\u0005"+
		"\t\u0000\u0000\u0252o\u0001\u0000\u0000\u0000\u0253\u0258\u0003r9\u0000"+
		"\u0254\u0255\u0005\u0001\u0000\u0000\u0255\u0257\u0003t:\u0000\u0256\u0254"+
		"\u0001\u0000\u0000\u0000\u0257\u025a\u0001\u0000\u0000\u0000\u0258\u0256"+
		"\u0001\u0000\u0000\u0000\u0258\u0259\u0001\u0000\u0000\u0000\u0259q\u0001"+
		"\u0000\u0000\u0000\u025a\u0258\u0001\u0000\u0000\u0000\u025b\u025c\u0007"+
		"\t\u0000\u0000\u025cs\u0001\u0000\u0000\u0000\u025d\u025e\u0007\n\u0000"+
		"\u0000\u025eu\u0001\u0000\u0000\u0000\u025f\u0260\u0003x<\u0000\u0260"+
		"\u0266\u0005\u0001\u0000\u0000\u0261\u0262\u0003x<\u0000\u0262\u0263\u0005"+
		"\u0001\u0000\u0000\u0263\u0265\u0001\u0000\u0000\u0000\u0264\u0261\u0001"+
		"\u0000\u0000\u0000\u0265\u0268\u0001\u0000\u0000\u0000\u0266\u0264\u0001"+
		"\u0000\u0000\u0000\u0266\u0267\u0001\u0000\u0000\u0000\u0267w\u0001\u0000"+
		"\u0000\u0000\u0268\u0266\u0001\u0000\u0000\u0000\u0269\u026a\u0007\u000b"+
		"\u0000\u0000\u026ay\u0001\u0000\u0000\u0000\u026b\u026d\u0003v;\u0000"+
		"\u026c\u026b\u0001\u0000\u0000\u0000\u026c\u026d\u0001\u0000\u0000\u0000"+
		"\u026d\u026e\u0001\u0000\u0000\u0000\u026e\u0271\u0003p8\u0000\u026f\u0270"+
		"\u0005\u0001\u0000\u0000\u0270\u0272\u0003\u008cF\u0000\u0271\u026f\u0001"+
		"\u0000\u0000\u0000\u0271\u0272\u0001\u0000\u0000\u0000\u0272\u0273\u0001"+
		"\u0000\u0000\u0000\u0273\u0274\u0005\u0001\u0000\u0000\u0274\u0275\u0003"+
		"\u0086C\u0000\u0275\u0276\u0005\u0001\u0000\u0000\u0276\u0277\u0003\u0088"+
		"D\u0000\u0277{\u0001\u0000\u0000\u0000\u0278\u0279\u0003~?\u0000\u0279"+
		"\u027a\u0005\u0001\u0000\u0000\u027a\u027b\u0003\u0086C\u0000\u027b\u027c"+
		"\u0005\u0001\u0000\u0000\u027c\u027d\u0003\u0088D\u0000\u027d\u027e\u0005"+
		"\u0000\u0000\u0001\u027e}\u0001\u0000\u0000\u0000\u027f\u0284\u0003\u0080"+
		"@\u0000\u0280\u0281\u0005\u0001\u0000\u0000\u0281\u0283\u0003\u0080@\u0000"+
		"\u0282\u0280\u0001\u0000\u0000\u0000\u0283\u0286\u0001\u0000\u0000\u0000"+
		"\u0284\u0282\u0001\u0000\u0000\u0000\u0284\u0285\u0001\u0000\u0000\u0000"+
		"\u0285\u007f\u0001\u0000\u0000\u0000\u0286\u0284\u0001\u0000\u0000\u0000"+
		"\u0287\u028e\u0003x<\u0000\u0288\u028e\u0003p8\u0000\u0289\u028e\u0003"+
		"\u008eG\u0000\u028a\u028e\u0003\u008cF\u0000\u028b\u028e\u0003\u0082A"+
		"\u0000\u028c\u028e\u0003\u0084B\u0000\u028d\u0287\u0001\u0000\u0000\u0000"+
		"\u028d\u0288\u0001\u0000\u0000\u0000\u028d\u0289\u0001\u0000\u0000\u0000"+
		"\u028d\u028a\u0001\u0000\u0000\u0000\u028d\u028b\u0001\u0000\u0000\u0000"+
		"\u028d\u028c\u0001\u0000\u0000\u0000\u028e\u0081\u0001\u0000\u0000\u0000"+
		"\u028f\u0290\u0005\u00ec\u0000\u0000\u0290\u0291\u0005\u0005\u0000\u0000"+
		"\u0291\u0292\u0005\u00f4\u0000\u0000\u0292\u0293\u0005\u0006\u0000\u0000"+
		"\u0293\u0083\u0001\u0000\u0000\u0000\u0294\u0295\u0005\u00ed\u0000\u0000"+
		"\u0295\u0296\u0005\u0005\u0000\u0000\u0296\u0297\u0005\u00f4\u0000\u0000"+
		"\u0297\u0298\u0005\u0006\u0000\u0000\u0298\u0085\u0001\u0000\u0000\u0000"+
		"\u0299\u029a\u0007\f\u0000\u0000\u029a\u0087\u0001\u0000\u0000\u0000\u029b"+
		"\u029c\u0007\r\u0000\u0000\u029c\u0089\u0001\u0000\u0000\u0000\u029d\u029e"+
		"\u0007\u000e\u0000\u0000\u029e\u008b\u0001\u0000\u0000\u0000\u029f\u02a0"+
		"\u0007\u000f\u0000\u0000\u02a0\u008d\u0001\u0000\u0000\u0000\u02a1\u02a2"+
		"\u0007\u0010\u0000\u0000\u02a2\u008f\u0001\u0000\u0000\u0000\u02a3\u02a4"+
		"\u0007\u0011\u0000\u0000\u02a4\u0091\u0001\u0000\u0000\u0000\u02a5\u02a6"+
		"\u0007\u0012\u0000\u0000\u02a6\u0093\u0001\u0000\u0000\u0000\u02a7\u02a9"+
		"\u0003v;\u0000\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a8\u02a9\u0001\u0000"+
		"\u0000\u0000\u02a9\u02aa\u0001\u0000\u0000\u0000\u02aa\u02ab\u0003p8\u0000"+
		"\u02ab\u02ac\u0005\u0001\u0000\u0000\u02ac\u02ad\u0003\u0092I\u0000\u02ad"+
		"\u0095\u0001\u0000\u0000\u0000\u02ae\u02b4\u0003\u009aM\u0000\u02af\u02b4"+
		"\u0003\u0094J\u0000\u02b0\u02b4\u0003\u009cN\u0000\u02b1\u02b4\u0003\u00a0"+
		"P\u0000\u02b2\u02b4\u0003\u0098L\u0000\u02b3\u02ae\u0001\u0000\u0000\u0000"+
		"\u02b3\u02af\u0001\u0000\u0000\u0000\u02b3\u02b0\u0001\u0000\u0000\u0000"+
		"\u02b3\u02b1\u0001\u0000\u0000\u0000\u02b3\u02b2\u0001\u0000\u0000\u0000"+
		"\u02b4\u0097\u0001\u0000\u0000\u0000\u02b5\u02c2\u0005-\u0000\u0000\u02b6"+
		"\u02c2\u0005.\u0000\u0000\u02b7\u02c2\u0005/\u0000\u0000\u02b8\u02c2\u0005"+
		"0\u0000\u0000\u02b9\u02c2\u00051\u0000\u0000\u02ba\u02c2\u00052\u0000"+
		"\u0000\u02bb\u02bc\u0005\u00f4\u0000\u0000\u02bc\u02c2\u00053\u0000\u0000"+
		"\u02bd\u02be\u0005\u00f4\u0000\u0000\u02be\u02c2\u0005\u00df\u0000\u0000"+
		"\u02bf\u02c0\u0005\u00f4\u0000\u0000\u02c0\u02c2\u0005\u008a\u0000\u0000"+
		"\u02c1\u02b5\u0001\u0000\u0000\u0000\u02c1\u02b6\u0001\u0000\u0000\u0000"+
		"\u02c1\u02b7\u0001\u0000\u0000\u0000\u02c1\u02b8\u0001\u0000\u0000\u0000"+
		"\u02c1\u02b9\u0001\u0000\u0000\u0000\u02c1\u02ba\u0001\u0000\u0000\u0000"+
		"\u02c1\u02bb\u0001\u0000\u0000\u0000\u02c1\u02bd\u0001\u0000\u0000\u0000"+
		"\u02c1\u02bf\u0001\u0000\u0000\u0000\u02c2\u0099\u0001\u0000\u0000\u0000"+
		"\u02c3\u02c4\u0007\u0013\u0000\u0000\u02c4\u009b\u0001\u0000\u0000\u0000"+
		"\u02c5\u02c6\u0003T*\u0000\u02c6\u02c7\u0005\u0001\u0000\u0000\u02c7\u02c8"+
		"\u0003\u009eO\u0000\u02c8\u009d\u0001\u0000\u0000\u0000\u02c9\u02ca\u0007"+
		"\u0014\u0000\u0000\u02ca\u009f\u0001\u0000\u0000\u0000\u02cb\u02cc\u0003"+
		"b1\u0000\u02cc\u00a1\u0001\u0000\u0000\u0000=\u00a9\u00b1\u00b5\u00b9"+
		"\u00bf\u00c8\u00ce\u00d6\u00de\u00e2\u00f7\u00fb\u00ff\u0102\u010b\u0118"+
		"\u011f\u0125\u0134\u0137\u0142\u0144\u0148\u014c\u0151\u0158\u0162\u0167"+
		"\u0176\u017f\u018f\u0196\u019e\u01a5\u01ae\u01bb\u01c4\u01cd\u01d1\u01e2"+
		"\u01f0\u01fc\u0202\u020c\u020f\u0216\u022a\u022e\u023b\u0246\u024a\u024f"+
		"\u0258\u0266\u026c\u0271\u0284\u028d\u02a8\u02b3\u02c1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}