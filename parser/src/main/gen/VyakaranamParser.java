// Generated from /Users/kaushalbx/StudioProjects/AshtadhyayiSandhi/parser/src/main/antlr/dev/panini/vyakaranam/VyakaranamParser.g4 by ANTLR 4.13.2

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
		UNA=46, SAKRIT=47, DVIH=48, TRIH=49, CHATUH=50, KRITVAS=51, KATAPAYADI=52, 
		ARYABHATIYA=53, BHUTASAMKHYA=54, SAARDHA=55, SAPAADA=56, PAADONA=57, ARDHA=58, 
		PAADA=59, AMSHA=60, GUNITA=61, BHAKTA=62, VARGA=63, GHANA=64, MOOLA=65, 
		KRITA=66, SAHITA=67, RAHITA=68, TEEYA=69, PURANA_THA=70, PURANA_MA=71, 
		INTERJECTION=72, PRA=73, PARAA=74, SAM=75, ANUU=76, AVA=77, NIS=78, DUS=79, 
		VI=80, AANG=81, NI=82, ADHI=83, ATI=84, SU=85, UD=86, ABHI=87, PRATI=88, 
		PARI=89, UPA=90, SAN=91, KYACH=92, KAAMYACH=93, KYANG=94, KYASH=95, NIC=96, 
		YAN=97, YUK_SAN=98, LAT=99, LIT=100, LUT=101, LRT=102, LET=103, LOT=104, 
		LANG=105, LIN=106, LUNG=107, LRNG=108, TIP=109, TAS=110, JHI=111, SIP=112, 
		THAS=113, THA=114, MIP=115, VAS=116, MAS=117, TA=118, ATAAM=119, JHA=120, 
		THAS_A=121, ATHAAM=122, DHVAM=123, IT=124, VAHI=125, MAHING=126, SUP_SU=127, 
		SUP_AU=128, SUP_JAS=129, SUP_AM=130, SUP_AUT=131, SUP_SHAS=132, SUP_TA=133, 
		SUP_BHYAM=134, SUP_BHIS=135, SUP_NGE=136, SUP_BHYAS=137, SUP_NGASI=138, 
		SUP_NGAS=139, SUP_OS=140, SUP_AAM=141, SUP_NGI=142, SUP_SUP=143, SHAP=144, 
		SHYAN=145, SHNU=146, SHNAM=147, SHNA=148, U_VIKARANA=149, SHNAAM=150, 
		YAK=151, SHAH=152, SYA=153, TAS_VIKARANA=154, CLI=155, SIC=156, ANG=157, 
		CHANG=158, KSA=159, AT=160, IIT_AGAMA=161, NUM=162, TUK=163, MUT=164, 
		NUT=165, YASUT=166, SIYUT=167, SUK=168, RUK=169, RIK=170, PUK=171, YUK=172, 
		VUK=173, KTA=174, KTAVATU=175, TAVYAT=176, ANIYAR=177, YAT=178, NYAT=179, 
		KYAP=180, SHATR=181, SHANACH=182, GHANJ=183, LYUT=184, NVUL=185, TRICH=186, 
		ANIN=187, KHAL=188, KWIP=189, KTIN=190, AC=191, AP=192, KA=193, NIN=194, 
		NINI=195, IN_KRT=196, TI_KRT=197, TRA=198, ITRA=199, ISHNUCH=200, UK=201, 
		KTVA=202, LYAP=203, TUMUN=204, NAMUL=205, KASUN=206, KTVOS=207, MATUP=208, 
		VATUP=209, INI=210, TVA=211, TAL=212, TARAP=213, TAMAP=214, MAYAT=215, 
		PRATYAYA_TIYA=216, PRATYAYA_MA=217, PRATYAYA_TAMA=218, TASIL=219, AN=220, 
		INJ=221, DHAK=222, THAJ=223, CHHA=224, KAN=225, AYANA=226, IYA=227, INA=228, 
		HA=229, DAA=230, THAAL=231, THAMU=232, VAT=233, DHAA=234, TAAP=235, DAAP=236, 
		CHAAP=237, NEEP=238, NEESH=239, NEEN=240, UUNG=241, TICH=242, LUK=243, 
		SHLU=244, LUP=245, ALUK=246, ABHYASA=247, ADESHA=248, UNADI=249, YADI=250, 
		TARHI=251, ANYATHA=252, YAVAT=253, TAVAT=254, IDENTIFIER=255, WS=256;
	public static final int
		RULE_ukti = 0, RULE_conditionalClause = 1, RULE_loopClause = 2, RULE_vakya = 3, 
		RULE_akhyataVakya = 4, RULE_purvaVakyaPada = 5, RULE_uttaraVakyaPada = 6, 
		RULE_namaVakya = 7, RULE_vakyaPada = 8, RULE_subantaVakyaPada = 9, RULE_vakyaSambandha = 10, 
		RULE_sambodhana = 11, RULE_sambodhanaSuchaka = 12, RULE_pada = 13, RULE_sankhyaPada = 14, 
		RULE_sankhyaPuranaPada = 15, RULE_puranaPratyaya = 16, RULE_sankhyaAbhyasaPada = 17, 
		RULE_katapayadiPada = 18, RULE_aryabhatiyaPada = 19, RULE_bhutasamkhyaPada = 20, 
		RULE_sankhyaBhinnaPada = 21, RULE_sankhyaMathPada = 22, RULE_sankhyaStem = 23, 
		RULE_subantaPada = 24, RULE_pratipadika = 25, RULE_pratipadikaMula = 26, 
		RULE_pratipadikaVikara = 27, RULE_mulaPratipadika = 28, RULE_kridantaPratipadika = 29, 
		RULE_unadyantaPratipadika = 30, RULE_unadiPratyaya = 31, RULE_taddhitaPratyaya = 32, 
		RULE_striPratyaya = 33, RULE_samasaPratipadika = 34, RULE_samasaAnga = 35, 
		RULE_samasaSupAvastha = 36, RULE_supAvastha = 37, RULE_asamasikaPratipadika = 38, 
		RULE_asamasikaPratipadikaMula = 39, RULE_samuccitaSubanta = 40, RULE_dhatuPrakriti = 41, 
		RULE_dhatuMula = 42, RULE_sanadiPratyaya = 43, RULE_upasargaKrama = 44, 
		RULE_upasarga = 45, RULE_tingantaPada = 46, RULE_vyutpattiTinganta = 47, 
		RULE_vyutpattiAnga = 48, RULE_vyutpattiAvayava = 49, RULE_abhyasa = 50, 
		RULE_adesham = 51, RULE_lakara = 52, RULE_tingPratyaya = 53, RULE_supPratyaya = 54, 
		RULE_vikarana = 55, RULE_agama = 56, RULE_krtPratyaya = 57, RULE_avyayaKrtPratyaya = 58, 
		RULE_avyayaKridanta = 59, RULE_avyayaPada = 60, RULE_sankhyaAvyaya = 61, 
		RULE_mulaAvyaya = 62, RULE_avyayaTaddhitanta = 63, RULE_avyayaTaddhitaPratyaya = 64, 
		RULE_avyayibhavaPada = 65;
	private static String[] makeRuleNames() {
		return new String[] {
			"ukti", "conditionalClause", "loopClause", "vakya", "akhyataVakya", "purvaVakyaPada", 
			"uttaraVakyaPada", "namaVakya", "vakyaPada", "subantaVakyaPada", "vakyaSambandha", 
			"sambodhana", "sambodhanaSuchaka", "pada", "sankhyaPada", "sankhyaPuranaPada", 
			"puranaPratyaya", "sankhyaAbhyasaPada", "katapayadiPada", "aryabhatiyaPada", 
			"bhutasamkhyaPada", "sankhyaBhinnaPada", "sankhyaMathPada", "sankhyaStem", 
			"subantaPada", "pratipadika", "pratipadikaMula", "pratipadikaVikara", 
			"mulaPratipadika", "kridantaPratipadika", "unadyantaPratipadika", "unadiPratyaya", 
			"taddhitaPratyaya", "striPratyaya", "samasaPratipadika", "samasaAnga", 
			"samasaSupAvastha", "supAvastha", "asamasikaPratipadika", "asamasikaPratipadikaMula", 
			"samuccitaSubanta", "dhatuPrakriti", "dhatuMula", "sanadiPratyaya", "upasargaKrama", 
			"upasarga", "tingantaPada", "vyutpattiTinganta", "vyutpattiAnga", "vyutpattiAvayava", 
			"abhyasa", "adesham", "lakara", "tingPratyaya", "supPratyaya", "vikarana", 
			"agama", "krtPratyaya", "avyayaKrtPratyaya", "avyayaKridanta", "avyayaPada", 
			"sankhyaAvyaya", "mulaAvyaya", "avyayaTaddhitanta", "avyayaTaddhitaPratyaya", 
			"avyayibhavaPada"
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
			"'\\u0936\\u094D\\u0935\\u0903'", "'\\u0939\\u094D\\u092F\\u0903'", null, 
			null, "'\\u0938\\u0915\\u0943\\u0924\\u094D'", "'\\u0926\\u094D\\u0935\\u093F\\u0903'", 
			"'\\u0924\\u094D\\u0930\\u093F\\u0903'", "'\\u091A\\u0924\\u0941\\u0903'", 
			null, null, null, "'\\u092D\\u0942\\u0924\\u0938\\u0919\\u094D\\u0916\\u094D\\u092F\\u093E'", 
			"'\\u0938\\u093E\\u0930\\u094D\\u0927'", "'\\u0938\\u092A\\u093E\\u0926'", 
			"'\\u092A\\u093E\\u0926\\u094B\\u0928'", "'\\u0905\\u0930\\u094D\\u0927'", 
			"'\\u092A\\u093E\\u0926'", null, null, null, "'\\u0935\\u0930\\u094D\\u0917'", 
			"'\\u0918\\u0928'", null, "'\\u0915\\u0943\\u0924'", null, null, null, 
			null, null, null, "'\\u092A\\u094D\\u0930'", "'\\u092A\\u0930\\u093E'", 
			"'\\u0938\\u092E\\u094D'", "'\\u0905\\u0928\\u0941'", "'\\u0905\\u0935'", 
			"'\\u0928\\u093F\\u0938\\u094D'", "'\\u0926\\u0941\\u0938\\u094D'", "'\\u0935\\u093F'", 
			"'\\u0906\\u0919\\u094D'", "'\\u0928\\u093F'", "'\\u0905\\u0927\\u093F'", 
			"'\\u0905\\u0924\\u093F'", "'\\u0938\\u0941'", "'\\u0909\\u0926\\u094D'", 
			"'\\u0905\\u092D\\u093F'", "'\\u092A\\u094D\\u0930\\u0924\\u093F'", "'\\u092A\\u0930\\u093F'", 
			"'\\u0909\\u092A'", "'\\u0938\\u0928\\u094D'", "'\\u0915\\u094D\\u092F\\u091A\\u094D'", 
			"'\\u0915\\u093E\\u092E\\u094D\\u092F\\u091A\\u094D'", "'\\u0915\\u094D\\u092F\\u0919\\u094D'", 
			"'\\u0915\\u094D\\u092F\\u0937\\u094D'", "'\\u0923\\u093F\\u091A\\u094D'", 
			"'\\u092F\\u0919\\u094D'", "'\\u092F\\u0919\\u094D\\u0932\\u0941\\u0915\\u094D'", 
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
			"'\\u0935\\u0924\\u0941\\u092A\\u094D'", "'\\u0907\\u0928\\u093F'", "'\\u0924\\u094D\\u0935'", 
			"'\\u0924\\u0932\\u094D'", "'\\u0924\\u0930\\u092A\\u094D'", "'\\u0924\\u092E\\u092A\\u094D'", 
			"'\\u092E\\u092F\\u091F\\u094D'", null, "'\\u092E'", "'\\u0924\\u092E'", 
			"'\\u0924\\u0938\\u093F\\u0932\\u094D'", "'\\u0905\\u0923\\u094D'", "'\\u0907\\u091E\\u094D'", 
			"'\\u0922\\u0915\\u094D'", "'\\u0920\\u091E\\u094D'", "'\\u091B'", "'\\u0915\\u0928\\u094D'", 
			"'\\u0906\\u092F\\u0928'", "'\\u0908\\u092F'", "'\\u0907\\u0928'", "'\\u0939'", 
			"'\\u0926\\u093E'", "'\\u0925\\u093E\\u0932\\u094D'", "'\\u0925\\u092E\\u0941'", 
			"'\\u0935\\u0924\\u094D'", "'\\u0927\\u093E'", "'\\u091F\\u093E\\u092A\\u094D'", 
			"'\\u0921\\u093E\\u092A\\u094D'", "'\\u091A\\u093E\\u092A\\u094D'", "'\\u0919\\u0940\\u092A\\u094D'", 
			"'\\u0919\\u0940\\u0937\\u094D'", "'\\u0919\\u0940\\u0928\\u094D'", "'\\u090A\\u0919\\u094D'", 
			"'\\u0924\\u093F\\u091A\\u094D'", "'\\u0932\\u0941\\u0915\\u094D'", "'\\u0936\\u094D\\u0932\\u0941'", 
			"'\\u0932\\u0941\\u092A\\u094D'", "'\\u0905\\u0932\\u0941\\u0915\\u094D'", 
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
			"TRIH", "CHATUH", "KRITVAS", "KATAPAYADI", "ARYABHATIYA", "BHUTASAMKHYA", 
			"SAARDHA", "SAPAADA", "PAADONA", "ARDHA", "PAADA", "AMSHA", "GUNITA", 
			"BHAKTA", "VARGA", "GHANA", "MOOLA", "KRITA", "SAHITA", "RAHITA", "TEEYA", 
			"PURANA_THA", "PURANA_MA", "INTERJECTION", "PRA", "PARAA", "SAM", "ANUU", 
			"AVA", "NIS", "DUS", "VI", "AANG", "NI", "ADHI", "ATI", "SU", "UD", "ABHI", 
			"PRATI", "PARI", "UPA", "SAN", "KYACH", "KAAMYACH", "KYANG", "KYASH", 
			"NIC", "YAN", "YUK_SAN", "LAT", "LIT", "LUT", "LRT", "LET", "LOT", "LANG", 
			"LIN", "LUNG", "LRNG", "TIP", "TAS", "JHI", "SIP", "THAS", "THA", "MIP", 
			"VAS", "MAS", "TA", "ATAAM", "JHA", "THAS_A", "ATHAAM", "DHVAM", "IT", 
			"VAHI", "MAHING", "SUP_SU", "SUP_AU", "SUP_JAS", "SUP_AM", "SUP_AUT", 
			"SUP_SHAS", "SUP_TA", "SUP_BHYAM", "SUP_BHIS", "SUP_NGE", "SUP_BHYAS", 
			"SUP_NGASI", "SUP_NGAS", "SUP_OS", "SUP_AAM", "SUP_NGI", "SUP_SUP", "SHAP", 
			"SHYAN", "SHNU", "SHNAM", "SHNA", "U_VIKARANA", "SHNAAM", "YAK", "SHAH", 
			"SYA", "TAS_VIKARANA", "CLI", "SIC", "ANG", "CHANG", "KSA", "AT", "IIT_AGAMA", 
			"NUM", "TUK", "MUT", "NUT", "YASUT", "SIYUT", "SUK", "RUK", "RIK", "PUK", 
			"YUK", "VUK", "KTA", "KTAVATU", "TAVYAT", "ANIYAR", "YAT", "NYAT", "KYAP", 
			"SHATR", "SHANACH", "GHANJ", "LYUT", "NVUL", "TRICH", "ANIN", "KHAL", 
			"KWIP", "KTIN", "AC", "AP", "KA", "NIN", "NINI", "IN_KRT", "TI_KRT", 
			"TRA", "ITRA", "ISHNUCH", "UK", "KTVA", "LYAP", "TUMUN", "NAMUL", "KASUN", 
			"KTVOS", "MATUP", "VATUP", "INI", "TVA", "TAL", "TARAP", "TAMAP", "MAYAT", 
			"PRATYAYA_TIYA", "PRATYAYA_MA", "PRATYAYA_TAMA", "TASIL", "AN", "INJ", 
			"DHAK", "THAJ", "CHHA", "KAN", "AYANA", "IYA", "INA", "HA", "DAA", "THAAL", 
			"THAMU", "VAT", "DHAA", "TAAP", "DAAP", "CHAAP", "NEEP", "NEESH", "NEEN", 
			"UUNG", "TICH", "LUK", "SHLU", "LUP", "ALUK", "ABHYASA", "ADESHA", "UNADI", 
			"YADI", "TARHI", "ANYATHA", "YAVAT", "TAVAT", "IDENTIFIER", "WS"
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
		public ConditionalClauseContext conditionalClause() {
			return getRuleContext(ConditionalClauseContext.class,0);
		}
		public LoopClauseContext loopClause() {
			return getRuleContext(LoopClauseContext.class,0);
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
			setState(151);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case YADI:
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				conditionalClause();
				}
				break;
			case YAVAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				loopClause();
				}
				break;
			case LPAREN:
			case HE:
			case BHOH:
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
			case SAARDHA:
			case SAPAADA:
			case PAADONA:
			case ARDHA:
			case PAADA:
			case AMSHA:
			case GUNITA:
			case BHAKTA:
			case VARGA:
			case GHANA:
			case MOOLA:
			case SAHITA:
			case RAHITA:
			case INTERJECTION:
			case PRA:
			case PARAA:
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
			case DAA:
			case DHAA:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HE || _la==BHOH) {
					{
					setState(134);
					sambodhana();
					}
				}

				setState(137);
				vakya();
				setState(143);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(138);
						vakyaSambandha();
						setState(139);
						vakya();
						}
						} 
					}
					setState(145);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DANDA) {
					{
					setState(146);
					match(DANDA);
					}
				}

				setState(149);
				match(EOF);
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
	public static class ConditionalClauseContext extends ParserRuleContext {
		public VakyaContext condition;
		public VakyaContext consequent;
		public VakyaContext alternate;
		public TerminalNode YADI() { return getToken(VyakaranamParser.YADI, 0); }
		public TerminalNode TARHI() { return getToken(VyakaranamParser.TARHI, 0); }
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public List<VakyaContext> vakya() {
			return getRuleContexts(VakyaContext.class);
		}
		public VakyaContext vakya(int i) {
			return getRuleContext(VakyaContext.class,i);
		}
		public TerminalNode ANYATHA() { return getToken(VyakaranamParser.ANYATHA, 0); }
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
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
		enterRule(_localctx, 2, RULE_conditionalClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			match(YADI);
			setState(154);
			((ConditionalClauseContext)_localctx).condition = vakya();
			setState(155);
			match(TARHI);
			setState(156);
			((ConditionalClauseContext)_localctx).consequent = vakya();
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ANYATHA) {
				{
				setState(157);
				match(ANYATHA);
				setState(158);
				((ConditionalClauseContext)_localctx).alternate = vakya();
				}
			}

			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(161);
				match(DANDA);
				}
			}

			setState(164);
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
	public static class LoopClauseContext extends ParserRuleContext {
		public VakyaContext condition;
		public VakyaContext body;
		public TerminalNode YAVAT() { return getToken(VyakaranamParser.YAVAT, 0); }
		public TerminalNode TAVAT() { return getToken(VyakaranamParser.TAVAT, 0); }
		public TerminalNode EOF() { return getToken(VyakaranamParser.EOF, 0); }
		public List<VakyaContext> vakya() {
			return getRuleContexts(VakyaContext.class);
		}
		public VakyaContext vakya(int i) {
			return getRuleContext(VakyaContext.class,i);
		}
		public TerminalNode DANDA() { return getToken(VyakaranamParser.DANDA, 0); }
		public LoopClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterLoopClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitLoopClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitLoopClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopClauseContext loopClause() throws RecognitionException {
		LoopClauseContext _localctx = new LoopClauseContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_loopClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(YAVAT);
			setState(167);
			((LoopClauseContext)_localctx).condition = vakya();
			setState(168);
			match(TAVAT);
			setState(169);
			((LoopClauseContext)_localctx).body = vakya();
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DANDA) {
				{
				setState(170);
				match(DANDA);
				}
			}

			setState(173);
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
		enterRule(_localctx, 6, RULE_vakya);
		try {
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				akhyataVakya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
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
		enterRule(_localctx, 8, RULE_akhyataVakya);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(179);
					purvaVakyaPada();
					}
					} 
				}
				setState(184);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(185);
			tingantaPada();
			setState(189);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(186);
					uttaraVakyaPada();
					}
					} 
				}
				setState(191);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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
		enterRule(_localctx, 10, RULE_purvaVakyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
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
		enterRule(_localctx, 12, RULE_uttaraVakyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
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
		enterRule(_localctx, 14, RULE_namaVakya);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(197); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(196);
					vakyaPada();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(199); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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
		enterRule(_localctx, 16, RULE_vakyaPada);
		try {
			setState(203);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(201);
				subantaVakyaPada();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
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
		public SankhyaBhinnaPadaContext sankhyaBhinnaPada() {
			return getRuleContext(SankhyaBhinnaPadaContext.class,0);
		}
		public SankhyaMathPadaContext sankhyaMathPada() {
			return getRuleContext(SankhyaMathPadaContext.class,0);
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
		enterRule(_localctx, 18, RULE_subantaVakyaPada);
		try {
			setState(215);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(205);
				subantaPada();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(206);
				samuccitaSubanta();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(207);
				sankhyaPada();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(208);
				sankhyaPuranaPada();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(209);
				sankhyaAbhyasaPada();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(210);
				katapayadiPada();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(211);
				aryabhatiyaPada();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(212);
				bhutasamkhyaPada();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(213);
				sankhyaBhinnaPada();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(214);
				sankhyaMathPada();
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
		enterRule(_localctx, 20, RULE_vakyaSambandha);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 130576L) != 0)) ) {
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
		enterRule(_localctx, 22, RULE_sambodhana);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			sambodhanaSuchaka();
			setState(220);
			subantaPada();
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(221);
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
		enterRule(_localctx, 24, RULE_sambodhanaSuchaka);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
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
		enterRule(_localctx, 26, RULE_pada);
		try {
			setState(230);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(226);
				subantaPada();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(227);
				tingantaPada();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(228);
				avyayaPada();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(229);
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
		enterRule(_localctx, 28, RULE_sankhyaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(232);
				sankhyaStem();
				setState(233);
				match(PLUS);
				}
				}
				setState(237); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(239);
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
		enterRule(_localctx, 30, RULE_sankhyaPuranaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(241);
				sankhyaStem();
				setState(242);
				match(PLUS);
				}
				}
				setState(246); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(248);
			puranaPratyaya();
			setState(249);
			match(PLUS);
			setState(250);
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
		public TerminalNode TEEYA() { return getToken(VyakaranamParser.TEEYA, 0); }
		public TerminalNode PURANA_THA() { return getToken(VyakaranamParser.PURANA_THA, 0); }
		public TerminalNode PURANA_MA() { return getToken(VyakaranamParser.PURANA_MA, 0); }
		public TerminalNode THA() { return getToken(VyakaranamParser.THA, 0); }
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
		enterRule(_localctx, 32, RULE_puranaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_la = _input.LA(1);
			if ( !(((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 35184372088839L) != 0)) ) {
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
		enterRule(_localctx, 34, RULE_sankhyaAbhyasaPada);
		int _la;
		try {
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(257); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(254);
					sankhyaStem();
					setState(255);
					match(PLUS);
					}
					}
					setState(259); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER );
				setState(261);
				match(KRITVAS);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(266); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(263);
					sankhyaStem();
					setState(264);
					match(PLUS);
					}
					}
					setState(268); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER );
				setState(270);
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
		enterRule(_localctx, 36, RULE_katapayadiPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(KATAPAYADI);
			setState(275);
			match(IDENTIFIER);
			setState(276);
			match(PLUS);
			setState(277);
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
		enterRule(_localctx, 38, RULE_aryabhatiyaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(ARYABHATIYA);
			setState(280);
			match(IDENTIFIER);
			setState(281);
			match(PLUS);
			setState(282);
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
		enterRule(_localctx, 40, RULE_bhutasamkhyaPada);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			match(BHUTASAMKHYA);
			setState(287); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(285);
				match(IDENTIFIER);
				setState(286);
				match(PLUS);
				}
				}
				setState(289); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			setState(291);
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
	public static class SankhyaBhinnaPadaContext extends ParserRuleContext {
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public List<SankhyaStemContext> sankhyaStem() {
			return getRuleContexts(SankhyaStemContext.class);
		}
		public SankhyaStemContext sankhyaStem(int i) {
			return getRuleContext(SankhyaStemContext.class,i);
		}
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public TerminalNode SAARDHA() { return getToken(VyakaranamParser.SAARDHA, 0); }
		public TerminalNode SAPAADA() { return getToken(VyakaranamParser.SAPAADA, 0); }
		public TerminalNode PAADONA() { return getToken(VyakaranamParser.PAADONA, 0); }
		public TerminalNode ARDHA() { return getToken(VyakaranamParser.ARDHA, 0); }
		public List<TerminalNode> PAADA() { return getTokens(VyakaranamParser.PAADA); }
		public TerminalNode PAADA(int i) {
			return getToken(VyakaranamParser.PAADA, i);
		}
		public TerminalNode AMSHA() { return getToken(VyakaranamParser.AMSHA, 0); }
		public PuranaPratyayaContext puranaPratyaya() {
			return getRuleContext(PuranaPratyayaContext.class,0);
		}
		public SankhyaBhinnaPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaBhinnaPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaBhinnaPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaBhinnaPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaBhinnaPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaBhinnaPadaContext sankhyaBhinnaPada() throws RecognitionException {
		SankhyaBhinnaPadaContext _localctx = new SankhyaBhinnaPadaContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_sankhyaBhinnaPada);
		int _la;
		try {
			setState(318);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SAARDHA:
			case SAPAADA:
			case PAADONA:
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 252201579132747776L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(294);
				match(PLUS);
				setState(295);
				sankhyaStem();
				setState(296);
				match(PLUS);
				setState(297);
				supPratyaya();
				}
				break;
			case ARDHA:
			case PAADA:
			case AMSHA:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(299);
					sankhyaStem();
					setState(300);
					match(PLUS);
					}
					break;
				}
				setState(313);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(304);
					sankhyaStem();
					setState(305);
					match(PLUS);
					setState(309);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 35184372088839L) != 0)) {
						{
						setState(306);
						puranaPratyaya();
						setState(307);
						match(PLUS);
						}
					}

					}
					break;
				case 2:
					{
					setState(311);
					match(PAADA);
					setState(312);
					match(PLUS);
					}
					break;
				}
				setState(315);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2017612633061982208L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(316);
				match(PLUS);
				setState(317);
				supPratyaya();
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
	public static class SankhyaMathPadaContext extends ParserRuleContext {
		public TerminalNode GUNITA() { return getToken(VyakaranamParser.GUNITA, 0); }
		public List<TerminalNode> PLUS() { return getTokens(VyakaranamParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(VyakaranamParser.PLUS, i);
		}
		public SupPratyayaContext supPratyaya() {
			return getRuleContext(SupPratyayaContext.class,0);
		}
		public SankhyaStemContext sankhyaStem() {
			return getRuleContext(SankhyaStemContext.class,0);
		}
		public TerminalNode BHAKTA() { return getToken(VyakaranamParser.BHAKTA, 0); }
		public TerminalNode VARGA() { return getToken(VyakaranamParser.VARGA, 0); }
		public TerminalNode KRITA() { return getToken(VyakaranamParser.KRITA, 0); }
		public TerminalNode GHANA() { return getToken(VyakaranamParser.GHANA, 0); }
		public TerminalNode MOOLA() { return getToken(VyakaranamParser.MOOLA, 0); }
		public TerminalNode SAHITA() { return getToken(VyakaranamParser.SAHITA, 0); }
		public TerminalNode RAHITA() { return getToken(VyakaranamParser.RAHITA, 0); }
		public SankhyaMathPadaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sankhyaMathPada; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).enterSankhyaMathPada(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VyakaranamParserListener ) ((VyakaranamParserListener)listener).exitSankhyaMathPada(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VyakaranamParserVisitor ) return ((VyakaranamParserVisitor<? extends T>)visitor).visitSankhyaMathPada(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SankhyaMathPadaContext sankhyaMathPada() throws RecognitionException {
		SankhyaMathPadaContext _localctx = new SankhyaMathPadaContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_sankhyaMathPada);
		int _la;
		try {
			setState(356);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(320);
					sankhyaStem();
					setState(321);
					match(PLUS);
					}
				}

				setState(325);
				match(GUNITA);
				setState(326);
				match(PLUS);
				setState(327);
				supPratyaya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(328);
					sankhyaStem();
					setState(329);
					match(PLUS);
					}
				}

				setState(333);
				match(BHAKTA);
				setState(334);
				match(PLUS);
				setState(335);
				supPratyaya();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(336);
				match(VARGA);
				setState(339);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(337);
					match(PLUS);
					setState(338);
					match(KRITA);
					}
					break;
				}
				setState(341);
				match(PLUS);
				setState(342);
				supPratyaya();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(343);
				match(GHANA);
				setState(346);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(344);
					match(PLUS);
					setState(345);
					match(KRITA);
					}
					break;
				}
				setState(348);
				match(PLUS);
				setState(349);
				supPratyaya();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(350);
				match(MOOLA);
				setState(351);
				match(PLUS);
				setState(352);
				supPratyaya();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(353);
				_la = _input.LA(1);
				if ( !(_la==SAHITA || _la==RAHITA) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(354);
				match(PLUS);
				setState(355);
				supPratyaya();
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
		enterRule(_localctx, 46, RULE_sankhyaStem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
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
		enterRule(_localctx, 48, RULE_subantaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			pratipadika();
			setState(361);
			match(PLUS);
			setState(362);
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
		enterRule(_localctx, 50, RULE_pratipadika);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			pratipadikaMula();
			setState(368);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(365);
					pratipadikaVikara();
					}
					} 
				}
				setState(370);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
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
		enterRule(_localctx, 52, RULE_pratipadikaMula);
		try {
			setState(379);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(371);
				mulaPratipadika();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(372);
				kridantaPratipadika();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(373);
				unadyantaPratipadika();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(374);
				samasaPratipadika();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(375);
				match(LPAREN);
				setState(376);
				pratipadika();
				setState(377);
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
		enterRule(_localctx, 54, RULE_pratipadikaVikara);
		try {
			setState(385);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				match(PLUS);
				setState(382);
				taddhitaPratyaya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(383);
				match(PLUS);
				setState(384);
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
		enterRule(_localctx, 56, RULE_mulaPratipadika);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
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
		enterRule(_localctx, 58, RULE_kridantaPratipadika);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(389);
				upasargaKrama();
				}
				break;
			}
			setState(392);
			dhatuPrakriti();
			setState(393);
			match(PLUS);
			setState(394);
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
		enterRule(_localctx, 60, RULE_unadyantaPratipadika);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(396);
				upasargaKrama();
				}
				break;
			}
			setState(399);
			dhatuPrakriti();
			setState(400);
			match(PLUS);
			setState(401);
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
		enterRule(_localctx, 62, RULE_unadiPratyaya);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			match(UNADI);
			setState(404);
			match(LPAREN);
			setState(405);
			match(IDENTIFIER);
			setState(406);
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
		public TerminalNode TRA() { return getToken(VyakaranamParser.TRA, 0); }
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
		enterRule(_localctx, 64, RULE_taddhitaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			_la = _input.LA(1);
			if ( !(((((_la - 178)) & ~0x3f) == 0 && ((1L << (_la - 178)) & 78811068260974593L) != 0)) ) {
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
		enterRule(_localctx, 66, RULE_striPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			_la = _input.LA(1);
			if ( !(((((_la - 235)) & ~0x3f) == 0 && ((1L << (_la - 235)) & 255L) != 0)) ) {
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
		enterRule(_localctx, 68, RULE_samasaPratipadika);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412);
			samasaAnga();
			setState(415); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(413);
				match(SAMASA_SEPARATOR);
				setState(414);
				samasaAnga();
				}
				}
				setState(417); 
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
		enterRule(_localctx, 70, RULE_samasaAnga);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			asamasikaPratipadika();
			setState(421);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(420);
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
		enterRule(_localctx, 72, RULE_samasaSupAvastha);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(PLUS);
			setState(424);
			supPratyaya();
			setState(425);
			match(PLUS);
			setState(426);
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
		enterRule(_localctx, 74, RULE_supAvastha);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			_la = _input.LA(1);
			if ( !(((((_la - 243)) & ~0x3f) == 0 && ((1L << (_la - 243)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 76, RULE_asamasikaPratipadika);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			asamasikaPratipadikaMula();
			setState(434);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(431);
					pratipadikaVikara();
					}
					} 
				}
				setState(436);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
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
		enterRule(_localctx, 78, RULE_asamasikaPratipadikaMula);
		try {
			setState(444);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(437);
				mulaPratipadika();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(438);
				kridantaPratipadika();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(439);
				unadyantaPratipadika();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(440);
				match(LPAREN);
				setState(441);
				samasaPratipadika();
				setState(442);
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
		enterRule(_localctx, 80, RULE_samuccitaSubanta);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			subantaPada();
			setState(451); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(448);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(447);
					match(COMMA);
					}
				}

				setState(450);
				subantaPada();
				}
				}
				setState(453); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 1048616L) != 0) || ((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & 262143L) != 0) || ((((_la - 230)) & ~0x3f) == 0 && ((1L << (_la - 230)) & 33554449L) != 0) );
			setState(455);
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
		enterRule(_localctx, 82, RULE_dhatuPrakriti);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			dhatuMula();
			setState(462);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(458);
					match(PLUS);
					setState(459);
					sanadiPratyaya();
					}
					} 
				}
				setState(464);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
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
		enterRule(_localctx, 84, RULE_dhatuMula);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			_la = _input.LA(1);
			if ( !(_la==SU || ((((_la - 230)) & ~0x3f) == 0 && ((1L << (_la - 230)) & 33554449L) != 0)) ) {
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
		enterRule(_localctx, 86, RULE_sanadiPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			_la = _input.LA(1);
			if ( !(((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 255L) != 0)) ) {
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
		enterRule(_localctx, 88, RULE_upasargaKrama);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			upasarga();
			setState(470);
			match(PLUS);
			setState(476);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(471);
					upasarga();
					setState(472);
					match(PLUS);
					}
					} 
				}
				setState(478);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
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
		enterRule(_localctx, 90, RULE_upasarga);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(479);
			_la = _input.LA(1);
			if ( !(_la==API || ((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & 262143L) != 0)) ) {
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
		enterRule(_localctx, 92, RULE_tingantaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(481);
				upasargaKrama();
				}
				break;
			}
			setState(484);
			dhatuPrakriti();
			setState(485);
			match(PLUS);
			setState(486);
			lakara();
			setState(487);
			match(PLUS);
			setState(488);
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
		enterRule(_localctx, 94, RULE_vyutpattiTinganta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			vyutpattiAnga();
			setState(491);
			match(PLUS);
			setState(492);
			lakara();
			setState(493);
			match(PLUS);
			setState(494);
			tingPratyaya();
			setState(495);
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
		enterRule(_localctx, 96, RULE_vyutpattiAnga);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			vyutpattiAvayava();
			setState(502);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(498);
					match(PLUS);
					setState(499);
					vyutpattiAvayava();
					}
					} 
				}
				setState(504);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
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
		enterRule(_localctx, 98, RULE_vyutpattiAvayava);
		try {
			setState(511);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(505);
				upasarga();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(506);
				dhatuPrakriti();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(507);
				agama();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(508);
				vikarana();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(509);
				abhyasa();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(510);
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
		enterRule(_localctx, 100, RULE_abhyasa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			match(ABHYASA);
			setState(514);
			match(LPAREN);
			setState(515);
			match(IDENTIFIER);
			setState(516);
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
		enterRule(_localctx, 102, RULE_adesham);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			match(ADESHA);
			setState(519);
			match(LPAREN);
			setState(520);
			match(IDENTIFIER);
			setState(521);
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
		enterRule(_localctx, 104, RULE_lakara);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			_la = _input.LA(1);
			if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & 1023L) != 0)) ) {
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
		enterRule(_localctx, 106, RULE_tingPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			_la = _input.LA(1);
			if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & 262143L) != 0)) ) {
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
		enterRule(_localctx, 108, RULE_supPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			_la = _input.LA(1);
			if ( !(((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & 131071L) != 0)) ) {
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
		enterRule(_localctx, 110, RULE_vikarana);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
			_la = _input.LA(1);
			if ( !(((((_la - 144)) & ~0x3f) == 0 && ((1L << (_la - 144)) & 65535L) != 0)) ) {
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
		enterRule(_localctx, 112, RULE_agama);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			_la = _input.LA(1);
			if ( !(((((_la - 124)) & ~0x3f) == 0 && ((1L << (_la - 124)) & 1125831187365889L) != 0)) ) {
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
		enterRule(_localctx, 114, RULE_krtPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(533);
			_la = _input.LA(1);
			if ( !(((((_la - 174)) & ~0x3f) == 0 && ((1L << (_la - 174)) & 268435455L) != 0)) ) {
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
		enterRule(_localctx, 116, RULE_avyayaKrtPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			_la = _input.LA(1);
			if ( !(((((_la - 202)) & ~0x3f) == 0 && ((1L << (_la - 202)) & 63L) != 0)) ) {
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
		enterRule(_localctx, 118, RULE_avyayaKridanta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(538);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(537);
				upasargaKrama();
				}
				break;
			}
			setState(540);
			dhatuPrakriti();
			setState(541);
			match(PLUS);
			setState(542);
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
		enterRule(_localctx, 120, RULE_avyayaPada);
		try {
			setState(549);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(544);
				mulaAvyaya();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(545);
				avyayaKridanta();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(546);
				avyayaTaddhitanta();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(547);
				avyayibhavaPada();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(548);
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
		enterRule(_localctx, 122, RULE_sankhyaAvyaya);
		try {
			setState(563);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(551);
				match(ADHIKA);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(552);
				match(UNA);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(553);
				match(SAKRIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(554);
				match(DVIH);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(555);
				match(TRIH);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(556);
				match(CHATUH);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(557);
				match(IDENTIFIER);
				setState(558);
				match(KRITVAS);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(559);
				match(IDENTIFIER);
				setState(560);
				match(DHAA);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(561);
				match(IDENTIFIER);
				setState(562);
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
		public TerminalNode NA() { return getToken(VyakaranamParser.NA, 0); }
		public TerminalNode ITI() { return getToken(VyakaranamParser.ITI, 0); }
		public TerminalNode API() { return getToken(VyakaranamParser.API, 0); }
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
		enterRule(_localctx, 124, RULE_mulaAvyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			_la = _input.LA(1);
			if ( !(((((_la - 9)) & ~0x3f) == 0 && ((1L << (_la - 9)) & -9223371968135299073L) != 0)) ) {
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
		enterRule(_localctx, 126, RULE_avyayaTaddhitanta);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
			mulaPratipadika();
			setState(568);
			match(PLUS);
			setState(569);
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
		enterRule(_localctx, 128, RULE_avyayaTaddhitaPratyaya);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(571);
			_la = _input.LA(1);
			if ( !(((((_la - 198)) & ~0x3f) == 0 && ((1L << (_la - 198)) & 135293566977L) != 0)) ) {
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
		enterRule(_localctx, 130, RULE_avyayibhavaPada);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
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
		"\u0004\u0001\u0100\u0240\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"@\u0002A\u0007A\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0088"+
		"\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u008e"+
		"\b\u0000\n\u0000\f\u0000\u0091\t\u0000\u0001\u0000\u0003\u0000\u0094\b"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0098\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u00a0"+
		"\b\u0001\u0001\u0001\u0003\u0001\u00a3\b\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"\u00ac\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u00b2\b\u0003\u0001\u0004\u0005\u0004\u00b5\b\u0004\n\u0004\f\u0004\u00b8"+
		"\t\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00bc\b\u0004\n\u0004\f\u0004"+
		"\u00bf\t\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0004\u0007\u00c6\b\u0007\u000b\u0007\f\u0007\u00c7\u0001\b\u0001\b\u0003"+
		"\b\u00cc\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u00d8\b\t\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u00df\b\u000b\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0003\r\u00e7\b\r\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0004\u000e\u00ec\b\u000e\u000b\u000e\f\u000e\u00ed\u0001\u000e\u0001"+
		"\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0004\u000f\u00f5\b\u000f\u000b"+
		"\u000f\f\u000f\u00f6\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0004\u0011\u0102"+
		"\b\u0011\u000b\u0011\f\u0011\u0103\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0004\u0011\u010b\b\u0011\u000b\u0011\f\u0011"+
		"\u010c\u0001\u0011\u0001\u0011\u0003\u0011\u0111\b\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0004"+
		"\u0014\u0120\b\u0014\u000b\u0014\f\u0014\u0121\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u012f\b\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0136\b\u0015"+
		"\u0001\u0015\u0001\u0015\u0003\u0015\u013a\b\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0003\u0015\u013f\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0003\u0016\u0144\b\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0003\u0016\u014c\b\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0154\b\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016"+
		"\u015b\b\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0165\b\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019"+
		"\u0001\u0019\u0005\u0019\u016f\b\u0019\n\u0019\f\u0019\u0172\t\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0003\u001a\u017c\b\u001a\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0003\u001b\u0182\b\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001d\u0003\u001d\u0187\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001e\u0003\u001e\u018e\b\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0004\"\u01a0"+
		"\b\"\u000b\"\f\"\u01a1\u0001#\u0001#\u0003#\u01a6\b#\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001%\u0001%\u0001&\u0001&\u0005&\u01b1\b&\n&\f&\u01b4"+
		"\t&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u01bd"+
		"\b\'\u0001(\u0001(\u0003(\u01c1\b(\u0001(\u0004(\u01c4\b(\u000b(\f(\u01c5"+
		"\u0001(\u0001(\u0001)\u0001)\u0001)\u0005)\u01cd\b)\n)\f)\u01d0\t)\u0001"+
		"*\u0001*\u0001+\u0001+\u0001,\u0001,\u0001,\u0001,\u0001,\u0005,\u01db"+
		"\b,\n,\f,\u01de\t,\u0001-\u0001-\u0001.\u0003.\u01e3\b.\u0001.\u0001."+
		"\u0001.\u0001.\u0001.\u0001.\u0001/\u0001/\u0001/\u0001/\u0001/\u0001"+
		"/\u0001/\u00010\u00010\u00010\u00050\u01f5\b0\n0\f0\u01f8\t0\u00011\u0001"+
		"1\u00011\u00011\u00011\u00011\u00031\u0200\b1\u00012\u00012\u00012\u0001"+
		"2\u00012\u00013\u00013\u00013\u00013\u00013\u00014\u00014\u00015\u0001"+
		"5\u00016\u00016\u00017\u00017\u00018\u00018\u00019\u00019\u0001:\u0001"+
		":\u0001;\u0003;\u021b\b;\u0001;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001"+
		"<\u0001<\u0001<\u0003<\u0226\b<\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0003=\u0234\b=\u0001>\u0001"+
		">\u0001?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001A\u0001A\u0001A\u0000"+
		"\u0000B\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0000\u0015\u0002\u0000\u0004\u0004\t\u0010\u0001\u0000\u0007\b"+
		"\u0002\u0000EGrr\u0001\u000079\u0001\u0000:<\u0001\u0000CD\u0007\u0000"+
		"\u00b2\u00b2\u00c1\u00c1\u00c6\u00c6\u00d0\u00d7\u00db\u00e4\u00e6\u00e6"+
		"\u00ea\u00ea\u0001\u0000\u00eb\u00f2\u0001\u0000\u00f3\u00f6\u0004\u0000"+
		"UU\u00e6\u00e6\u00ea\u00ea\u00ff\u00ff\u0001\u0000[b\u0002\u0000\u0014"+
		"\u0014IZ\u0001\u0000cl\u0001\u0000m~\u0001\u0000\u007f\u008f\u0001\u0000"+
		"\u0090\u009f\u0002\u0000||\u00a0\u00ad\u0001\u0000\u00ae\u00c9\u0001\u0000"+
		"\u00ca\u00cf\u0002\u0000\t,HH\u0003\u0000\u00c6\u00c6\u00db\u00db\u00e5"+
		"\u00ea\u0251\u0000\u0097\u0001\u0000\u0000\u0000\u0002\u0099\u0001\u0000"+
		"\u0000\u0000\u0004\u00a6\u0001\u0000\u0000\u0000\u0006\u00b1\u0001\u0000"+
		"\u0000\u0000\b\u00b6\u0001\u0000\u0000\u0000\n\u00c0\u0001\u0000\u0000"+
		"\u0000\f\u00c2\u0001\u0000\u0000\u0000\u000e\u00c5\u0001\u0000\u0000\u0000"+
		"\u0010\u00cb\u0001\u0000\u0000\u0000\u0012\u00d7\u0001\u0000\u0000\u0000"+
		"\u0014\u00d9\u0001\u0000\u0000\u0000\u0016\u00db\u0001\u0000\u0000\u0000"+
		"\u0018\u00e0\u0001\u0000\u0000\u0000\u001a\u00e6\u0001\u0000\u0000\u0000"+
		"\u001c\u00eb\u0001\u0000\u0000\u0000\u001e\u00f4\u0001\u0000\u0000\u0000"+
		" \u00fc\u0001\u0000\u0000\u0000\"\u0110\u0001\u0000\u0000\u0000$\u0112"+
		"\u0001\u0000\u0000\u0000&\u0117\u0001\u0000\u0000\u0000(\u011c\u0001\u0000"+
		"\u0000\u0000*\u013e\u0001\u0000\u0000\u0000,\u0164\u0001\u0000\u0000\u0000"+
		".\u0166\u0001\u0000\u0000\u00000\u0168\u0001\u0000\u0000\u00002\u016c"+
		"\u0001\u0000\u0000\u00004\u017b\u0001\u0000\u0000\u00006\u0181\u0001\u0000"+
		"\u0000\u00008\u0183\u0001\u0000\u0000\u0000:\u0186\u0001\u0000\u0000\u0000"+
		"<\u018d\u0001\u0000\u0000\u0000>\u0193\u0001\u0000\u0000\u0000@\u0198"+
		"\u0001\u0000\u0000\u0000B\u019a\u0001\u0000\u0000\u0000D\u019c\u0001\u0000"+
		"\u0000\u0000F\u01a3\u0001\u0000\u0000\u0000H\u01a7\u0001\u0000\u0000\u0000"+
		"J\u01ac\u0001\u0000\u0000\u0000L\u01ae\u0001\u0000\u0000\u0000N\u01bc"+
		"\u0001\u0000\u0000\u0000P\u01be\u0001\u0000\u0000\u0000R\u01c9\u0001\u0000"+
		"\u0000\u0000T\u01d1\u0001\u0000\u0000\u0000V\u01d3\u0001\u0000\u0000\u0000"+
		"X\u01d5\u0001\u0000\u0000\u0000Z\u01df\u0001\u0000\u0000\u0000\\\u01e2"+
		"\u0001\u0000\u0000\u0000^\u01ea\u0001\u0000\u0000\u0000`\u01f1\u0001\u0000"+
		"\u0000\u0000b\u01ff\u0001\u0000\u0000\u0000d\u0201\u0001\u0000\u0000\u0000"+
		"f\u0206\u0001\u0000\u0000\u0000h\u020b\u0001\u0000\u0000\u0000j\u020d"+
		"\u0001\u0000\u0000\u0000l\u020f\u0001\u0000\u0000\u0000n\u0211\u0001\u0000"+
		"\u0000\u0000p\u0213\u0001\u0000\u0000\u0000r\u0215\u0001\u0000\u0000\u0000"+
		"t\u0217\u0001\u0000\u0000\u0000v\u021a\u0001\u0000\u0000\u0000x\u0225"+
		"\u0001\u0000\u0000\u0000z\u0233\u0001\u0000\u0000\u0000|\u0235\u0001\u0000"+
		"\u0000\u0000~\u0237\u0001\u0000\u0000\u0000\u0080\u023b\u0001\u0000\u0000"+
		"\u0000\u0082\u023d\u0001\u0000\u0000\u0000\u0084\u0098\u0003\u0002\u0001"+
		"\u0000\u0085\u0098\u0003\u0004\u0002\u0000\u0086\u0088\u0003\u0016\u000b"+
		"\u0000\u0087\u0086\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000"+
		"\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008f\u0003\u0006\u0003"+
		"\u0000\u008a\u008b\u0003\u0014\n\u0000\u008b\u008c\u0003\u0006\u0003\u0000"+
		"\u008c\u008e\u0001\u0000\u0000\u0000\u008d\u008a\u0001\u0000\u0000\u0000"+
		"\u008e\u0091\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000"+
		"\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0093\u0001\u0000\u0000\u0000"+
		"\u0091\u008f\u0001\u0000\u0000\u0000\u0092\u0094\u0005\u0004\u0000\u0000"+
		"\u0093\u0092\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000"+
		"\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u0000\u0000\u0001"+
		"\u0096\u0098\u0001\u0000\u0000\u0000\u0097\u0084\u0001\u0000\u0000\u0000"+
		"\u0097\u0085\u0001\u0000\u0000\u0000\u0097\u0087\u0001\u0000\u0000\u0000"+
		"\u0098\u0001\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u00fa\u0000\u0000"+
		"\u009a\u009b\u0003\u0006\u0003\u0000\u009b\u009c\u0005\u00fb\u0000\u0000"+
		"\u009c\u009f\u0003\u0006\u0003\u0000\u009d\u009e\u0005\u00fc\u0000\u0000"+
		"\u009e\u00a0\u0003\u0006\u0003\u0000\u009f\u009d\u0001\u0000\u0000\u0000"+
		"\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a3\u0005\u0004\u0000\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a5\u0005\u0000\u0000\u0001\u00a5\u0003\u0001\u0000\u0000\u0000"+
		"\u00a6\u00a7\u0005\u00fd\u0000\u0000\u00a7\u00a8\u0003\u0006\u0003\u0000"+
		"\u00a8\u00a9\u0005\u00fe\u0000\u0000\u00a9\u00ab\u0003\u0006\u0003\u0000"+
		"\u00aa\u00ac\u0005\u0004\u0000\u0000\u00ab\u00aa\u0001\u0000\u0000\u0000"+
		"\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000"+
		"\u00ad\u00ae\u0005\u0000\u0000\u0001\u00ae\u0005\u0001\u0000\u0000\u0000"+
		"\u00af\u00b2\u0003\b\u0004\u0000\u00b0\u00b2\u0003\u000e\u0007\u0000\u00b1"+
		"\u00af\u0001\u0000\u0000\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2"+
		"\u0007\u0001\u0000\u0000\u0000\u00b3\u00b5\u0003\n\u0005\u0000\u00b4\u00b3"+
		"\u0001\u0000\u0000\u0000\u00b5\u00b8\u0001\u0000\u0000\u0000\u00b6\u00b4"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b9"+
		"\u0001\u0000\u0000\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u00bd"+
		"\u0003\\.\u0000\u00ba\u00bc\u0003\f\u0006\u0000\u00bb\u00ba\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bf\u0001\u0000\u0000\u0000\u00bd\u00bb\u0001\u0000"+
		"\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\t\u0001\u0000\u0000"+
		"\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000\u00c0\u00c1\u0003\u0010\b\u0000"+
		"\u00c1\u000b\u0001\u0000\u0000\u0000\u00c2\u00c3\u0003\u0010\b\u0000\u00c3"+
		"\r\u0001\u0000\u0000\u0000\u00c4\u00c6\u0003\u0010\b\u0000\u00c5\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c5"+
		"\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\u000f"+
		"\u0001\u0000\u0000\u0000\u00c9\u00cc\u0003\u0012\t\u0000\u00ca\u00cc\u0003"+
		"x<\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cb\u00ca\u0001\u0000\u0000"+
		"\u0000\u00cc\u0011\u0001\u0000\u0000\u0000\u00cd\u00d8\u00030\u0018\u0000"+
		"\u00ce\u00d8\u0003P(\u0000\u00cf\u00d8\u0003\u001c\u000e\u0000\u00d0\u00d8"+
		"\u0003\u001e\u000f\u0000\u00d1\u00d8\u0003\"\u0011\u0000\u00d2\u00d8\u0003"+
		"$\u0012\u0000\u00d3\u00d8\u0003&\u0013\u0000\u00d4\u00d8\u0003(\u0014"+
		"\u0000\u00d5\u00d8\u0003*\u0015\u0000\u00d6\u00d8\u0003,\u0016\u0000\u00d7"+
		"\u00cd\u0001\u0000\u0000\u0000\u00d7\u00ce\u0001\u0000\u0000\u0000\u00d7"+
		"\u00cf\u0001\u0000\u0000\u0000\u00d7\u00d0\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d1\u0001\u0000\u0000\u0000\u00d7\u00d2\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d3\u0001\u0000\u0000\u0000\u00d7\u00d4\u0001\u0000\u0000\u0000\u00d7"+
		"\u00d5\u0001\u0000\u0000\u0000\u00d7\u00d6\u0001\u0000\u0000\u0000\u00d8"+
		"\u0013\u0001\u0000\u0000\u0000\u00d9\u00da\u0007\u0000\u0000\u0000\u00da"+
		"\u0015\u0001\u0000\u0000\u0000\u00db\u00dc\u0003\u0018\f\u0000\u00dc\u00de"+
		"\u00030\u0018\u0000\u00dd\u00df\u0005\u0003\u0000\u0000\u00de\u00dd\u0001"+
		"\u0000\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u0017\u0001"+
		"\u0000\u0000\u0000\u00e0\u00e1\u0007\u0001\u0000\u0000\u00e1\u0019\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e7\u00030\u0018\u0000\u00e3\u00e7\u0003\\"+
		".\u0000\u00e4\u00e7\u0003x<\u0000\u00e5\u00e7\u0003\u001c\u000e\u0000"+
		"\u00e6\u00e2\u0001\u0000\u0000\u0000\u00e6\u00e3\u0001\u0000\u0000\u0000"+
		"\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e6\u00e5\u0001\u0000\u0000\u0000"+
		"\u00e7\u001b\u0001\u0000\u0000\u0000\u00e8\u00e9\u0003.\u0017\u0000\u00e9"+
		"\u00ea\u0005\u0001\u0000\u0000\u00ea\u00ec\u0001\u0000\u0000\u0000\u00eb"+
		"\u00e8\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed"+
		"\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee"+
		"\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f0\u0003l6\u0000\u00f0\u001d\u0001"+
		"\u0000\u0000\u0000\u00f1\u00f2\u0003.\u0017\u0000\u00f2\u00f3\u0005\u0001"+
		"\u0000\u0000\u00f3\u00f5\u0001\u0000\u0000\u0000\u00f4\u00f1\u0001\u0000"+
		"\u0000\u0000\u00f5\u00f6\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u00f8\u0001\u0000"+
		"\u0000\u0000\u00f8\u00f9\u0003 \u0010\u0000\u00f9\u00fa\u0005\u0001\u0000"+
		"\u0000\u00fa\u00fb\u0003l6\u0000\u00fb\u001f\u0001\u0000\u0000\u0000\u00fc"+
		"\u00fd\u0007\u0002\u0000\u0000\u00fd!\u0001\u0000\u0000\u0000\u00fe\u00ff"+
		"\u0003.\u0017\u0000\u00ff\u0100\u0005\u0001\u0000\u0000\u0100\u0102\u0001"+
		"\u0000\u0000\u0000\u0101\u00fe\u0001\u0000\u0000\u0000\u0102\u0103\u0001"+
		"\u0000\u0000\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0103\u0104\u0001"+
		"\u0000\u0000\u0000\u0104\u0105\u0001\u0000\u0000\u0000\u0105\u0106\u0005"+
		"3\u0000\u0000\u0106\u0111\u0001\u0000\u0000\u0000\u0107\u0108\u0003.\u0017"+
		"\u0000\u0108\u0109\u0005\u0001\u0000\u0000\u0109\u010b\u0001\u0000\u0000"+
		"\u0000\u010a\u0107\u0001\u0000\u0000\u0000\u010b\u010c\u0001\u0000\u0000"+
		"\u0000\u010c\u010a\u0001\u0000\u0000\u0000\u010c\u010d\u0001\u0000\u0000"+
		"\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e\u010f\u0005\u00ea\u0000"+
		"\u0000\u010f\u0111\u0001\u0000\u0000\u0000\u0110\u0101\u0001\u0000\u0000"+
		"\u0000\u0110\u010a\u0001\u0000\u0000\u0000\u0111#\u0001\u0000\u0000\u0000"+
		"\u0112\u0113\u00054\u0000\u0000\u0113\u0114\u0005\u00ff\u0000\u0000\u0114"+
		"\u0115\u0005\u0001\u0000\u0000\u0115\u0116\u0003l6\u0000\u0116%\u0001"+
		"\u0000\u0000\u0000\u0117\u0118\u00055\u0000\u0000\u0118\u0119\u0005\u00ff"+
		"\u0000\u0000\u0119\u011a\u0005\u0001\u0000\u0000\u011a\u011b\u0003l6\u0000"+
		"\u011b\'\u0001\u0000\u0000\u0000\u011c\u011f\u00056\u0000\u0000\u011d"+
		"\u011e\u0005\u00ff\u0000\u0000\u011e\u0120\u0005\u0001\u0000\u0000\u011f"+
		"\u011d\u0001\u0000\u0000\u0000\u0120\u0121\u0001\u0000\u0000\u0000\u0121"+
		"\u011f\u0001\u0000\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000\u0122"+
		"\u0123\u0001\u0000\u0000\u0000\u0123\u0124\u0003l6\u0000\u0124)\u0001"+
		"\u0000\u0000\u0000\u0125\u0126\u0007\u0003\u0000\u0000\u0126\u0127\u0005"+
		"\u0001\u0000\u0000\u0127\u0128\u0003.\u0017\u0000\u0128\u0129\u0005\u0001"+
		"\u0000\u0000\u0129\u012a\u0003l6\u0000\u012a\u013f\u0001\u0000\u0000\u0000"+
		"\u012b\u012c\u0003.\u0017\u0000\u012c\u012d\u0005\u0001\u0000\u0000\u012d"+
		"\u012f\u0001\u0000\u0000\u0000\u012e\u012b\u0001\u0000\u0000\u0000\u012e"+
		"\u012f\u0001\u0000\u0000\u0000\u012f\u0139\u0001\u0000\u0000\u0000\u0130"+
		"\u0131\u0003.\u0017\u0000\u0131\u0135\u0005\u0001\u0000\u0000\u0132\u0133"+
		"\u0003 \u0010\u0000\u0133\u0134\u0005\u0001\u0000\u0000\u0134\u0136\u0001"+
		"\u0000\u0000\u0000\u0135\u0132\u0001\u0000\u0000\u0000\u0135\u0136\u0001"+
		"\u0000\u0000\u0000\u0136\u013a\u0001\u0000\u0000\u0000\u0137\u0138\u0005"+
		";\u0000\u0000\u0138\u013a\u0005\u0001\u0000\u0000\u0139\u0130\u0001\u0000"+
		"\u0000\u0000\u0139\u0137\u0001\u0000\u0000\u0000\u0139\u013a\u0001\u0000"+
		"\u0000\u0000\u013a\u013b\u0001\u0000\u0000\u0000\u013b\u013c\u0007\u0004"+
		"\u0000\u0000\u013c\u013d\u0005\u0001\u0000\u0000\u013d\u013f\u0003l6\u0000"+
		"\u013e\u0125\u0001\u0000\u0000\u0000\u013e\u012e\u0001\u0000\u0000\u0000"+
		"\u013f+\u0001\u0000\u0000\u0000\u0140\u0141\u0003.\u0017\u0000\u0141\u0142"+
		"\u0005\u0001\u0000\u0000\u0142\u0144\u0001\u0000\u0000\u0000\u0143\u0140"+
		"\u0001\u0000\u0000\u0000\u0143\u0144\u0001\u0000\u0000\u0000\u0144\u0145"+
		"\u0001\u0000\u0000\u0000\u0145\u0146\u0005=\u0000\u0000\u0146\u0147\u0005"+
		"\u0001\u0000\u0000\u0147\u0165\u0003l6\u0000\u0148\u0149\u0003.\u0017"+
		"\u0000\u0149\u014a\u0005\u0001\u0000\u0000\u014a\u014c\u0001\u0000\u0000"+
		"\u0000\u014b\u0148\u0001\u0000\u0000\u0000\u014b\u014c\u0001\u0000\u0000"+
		"\u0000\u014c\u014d\u0001\u0000\u0000\u0000\u014d\u014e\u0005>\u0000\u0000"+
		"\u014e\u014f\u0005\u0001\u0000\u0000\u014f\u0165\u0003l6\u0000\u0150\u0153"+
		"\u0005?\u0000\u0000\u0151\u0152\u0005\u0001\u0000\u0000\u0152\u0154\u0005"+
		"B\u0000\u0000\u0153\u0151\u0001\u0000\u0000\u0000\u0153\u0154\u0001\u0000"+
		"\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0155\u0156\u0005\u0001"+
		"\u0000\u0000\u0156\u0165\u0003l6\u0000\u0157\u015a\u0005@\u0000\u0000"+
		"\u0158\u0159\u0005\u0001\u0000\u0000\u0159\u015b\u0005B\u0000\u0000\u015a"+
		"\u0158\u0001\u0000\u0000\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015b"+
		"\u015c\u0001\u0000\u0000\u0000\u015c\u015d\u0005\u0001\u0000\u0000\u015d"+
		"\u0165\u0003l6\u0000\u015e\u015f\u0005A\u0000\u0000\u015f\u0160\u0005"+
		"\u0001\u0000\u0000\u0160\u0165\u0003l6\u0000\u0161\u0162\u0007\u0005\u0000"+
		"\u0000\u0162\u0163\u0005\u0001\u0000\u0000\u0163\u0165\u0003l6\u0000\u0164"+
		"\u0143\u0001\u0000\u0000\u0000\u0164\u014b\u0001\u0000\u0000\u0000\u0164"+
		"\u0150\u0001\u0000\u0000\u0000\u0164\u0157\u0001\u0000\u0000\u0000\u0164"+
		"\u015e\u0001\u0000\u0000\u0000\u0164\u0161\u0001\u0000\u0000\u0000\u0165"+
		"-\u0001\u0000\u0000\u0000\u0166\u0167\u0005\u00ff\u0000\u0000\u0167/\u0001"+
		"\u0000\u0000\u0000\u0168\u0169\u00032\u0019\u0000\u0169\u016a\u0005\u0001"+
		"\u0000\u0000\u016a\u016b\u0003l6\u0000\u016b1\u0001\u0000\u0000\u0000"+
		"\u016c\u0170\u00034\u001a\u0000\u016d\u016f\u00036\u001b\u0000\u016e\u016d"+
		"\u0001\u0000\u0000\u0000\u016f\u0172\u0001\u0000\u0000\u0000\u0170\u016e"+
		"\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u01713\u0001"+
		"\u0000\u0000\u0000\u0172\u0170\u0001\u0000\u0000\u0000\u0173\u017c\u0003"+
		"8\u001c\u0000\u0174\u017c\u0003:\u001d\u0000\u0175\u017c\u0003<\u001e"+
		"\u0000\u0176\u017c\u0003D\"\u0000\u0177\u0178\u0005\u0005\u0000\u0000"+
		"\u0178\u0179\u00032\u0019\u0000\u0179\u017a\u0005\u0006\u0000\u0000\u017a"+
		"\u017c\u0001\u0000\u0000\u0000\u017b\u0173\u0001\u0000\u0000\u0000\u017b"+
		"\u0174\u0001\u0000\u0000\u0000\u017b\u0175\u0001\u0000\u0000\u0000\u017b"+
		"\u0176\u0001\u0000\u0000\u0000\u017b\u0177\u0001\u0000\u0000\u0000\u017c"+
		"5\u0001\u0000\u0000\u0000\u017d\u017e\u0005\u0001\u0000\u0000\u017e\u0182"+
		"\u0003@ \u0000\u017f\u0180\u0005\u0001\u0000\u0000\u0180\u0182\u0003B"+
		"!\u0000\u0181\u017d\u0001\u0000\u0000\u0000\u0181\u017f\u0001\u0000\u0000"+
		"\u0000\u01827\u0001\u0000\u0000\u0000\u0183\u0184\u0005\u00ff\u0000\u0000"+
		"\u01849\u0001\u0000\u0000\u0000\u0185\u0187\u0003X,\u0000\u0186\u0185"+
		"\u0001\u0000\u0000\u0000\u0186\u0187\u0001\u0000\u0000\u0000\u0187\u0188"+
		"\u0001\u0000\u0000\u0000\u0188\u0189\u0003R)\u0000\u0189\u018a\u0005\u0001"+
		"\u0000\u0000\u018a\u018b\u0003r9\u0000\u018b;\u0001\u0000\u0000\u0000"+
		"\u018c\u018e\u0003X,\u0000\u018d\u018c\u0001\u0000\u0000\u0000\u018d\u018e"+
		"\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f\u0190"+
		"\u0003R)\u0000\u0190\u0191\u0005\u0001\u0000\u0000\u0191\u0192\u0003>"+
		"\u001f\u0000\u0192=\u0001\u0000\u0000\u0000\u0193\u0194\u0005\u00f9\u0000"+
		"\u0000\u0194\u0195\u0005\u0005\u0000\u0000\u0195\u0196\u0005\u00ff\u0000"+
		"\u0000\u0196\u0197\u0005\u0006\u0000\u0000\u0197?\u0001\u0000\u0000\u0000"+
		"\u0198\u0199\u0007\u0006\u0000\u0000\u0199A\u0001\u0000\u0000\u0000\u019a"+
		"\u019b\u0007\u0007\u0000\u0000\u019bC\u0001\u0000\u0000\u0000\u019c\u019f"+
		"\u0003F#\u0000\u019d\u019e\u0005\u0002\u0000\u0000\u019e\u01a0\u0003F"+
		"#\u0000\u019f\u019d\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1\u019f\u0001\u0000\u0000\u0000\u01a1\u01a2\u0001\u0000\u0000"+
		"\u0000\u01a2E\u0001\u0000\u0000\u0000\u01a3\u01a5\u0003L&\u0000\u01a4"+
		"\u01a6\u0003H$\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a5\u01a6\u0001"+
		"\u0000\u0000\u0000\u01a6G\u0001\u0000\u0000\u0000\u01a7\u01a8\u0005\u0001"+
		"\u0000\u0000\u01a8\u01a9\u0003l6\u0000\u01a9\u01aa\u0005\u0001\u0000\u0000"+
		"\u01aa\u01ab\u0003J%\u0000\u01abI\u0001\u0000\u0000\u0000\u01ac\u01ad"+
		"\u0007\b\u0000\u0000\u01adK\u0001\u0000\u0000\u0000\u01ae\u01b2\u0003"+
		"N\'\u0000\u01af\u01b1\u00036\u001b\u0000\u01b0\u01af\u0001\u0000\u0000"+
		"\u0000\u01b1\u01b4\u0001\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000"+
		"\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3M\u0001\u0000\u0000\u0000"+
		"\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b5\u01bd\u00038\u001c\u0000\u01b6"+
		"\u01bd\u0003:\u001d\u0000\u01b7\u01bd\u0003<\u001e\u0000\u01b8\u01b9\u0005"+
		"\u0005\u0000\u0000\u01b9\u01ba\u0003D\"\u0000\u01ba\u01bb\u0005\u0006"+
		"\u0000\u0000\u01bb\u01bd\u0001\u0000\u0000\u0000\u01bc\u01b5\u0001\u0000"+
		"\u0000\u0000\u01bc\u01b6\u0001\u0000\u0000\u0000\u01bc\u01b7\u0001\u0000"+
		"\u0000\u0000\u01bc\u01b8\u0001\u0000\u0000\u0000\u01bdO\u0001\u0000\u0000"+
		"\u0000\u01be\u01c3\u00030\u0018\u0000\u01bf\u01c1\u0005\u0003\u0000\u0000"+
		"\u01c0\u01bf\u0001\u0000\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c1\u01c2\u0001\u0000\u0000\u0000\u01c2\u01c4\u00030\u0018\u0000\u01c3"+
		"\u01c0\u0001\u0000\u0000\u0000\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5"+
		"\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c6\u0001\u0000\u0000\u0000\u01c6"+
		"\u01c7\u0001\u0000\u0000\u0000\u01c7\u01c8\u0005\t\u0000\u0000\u01c8Q"+
		"\u0001\u0000\u0000\u0000\u01c9\u01ce\u0003T*\u0000\u01ca\u01cb\u0005\u0001"+
		"\u0000\u0000\u01cb\u01cd\u0003V+\u0000\u01cc\u01ca\u0001\u0000\u0000\u0000"+
		"\u01cd\u01d0\u0001\u0000\u0000\u0000\u01ce\u01cc\u0001\u0000\u0000\u0000"+
		"\u01ce\u01cf\u0001\u0000\u0000\u0000\u01cfS\u0001\u0000\u0000\u0000\u01d0"+
		"\u01ce\u0001\u0000\u0000\u0000\u01d1\u01d2\u0007\t\u0000\u0000\u01d2U"+
		"\u0001\u0000\u0000\u0000\u01d3\u01d4\u0007\n\u0000\u0000\u01d4W\u0001"+
		"\u0000\u0000\u0000\u01d5\u01d6\u0003Z-\u0000\u01d6\u01dc\u0005\u0001\u0000"+
		"\u0000\u01d7\u01d8\u0003Z-\u0000\u01d8\u01d9\u0005\u0001\u0000\u0000\u01d9"+
		"\u01db\u0001\u0000\u0000\u0000\u01da\u01d7\u0001\u0000\u0000\u0000\u01db"+
		"\u01de\u0001\u0000\u0000\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dc"+
		"\u01dd\u0001\u0000\u0000\u0000\u01ddY\u0001\u0000\u0000\u0000\u01de\u01dc"+
		"\u0001\u0000\u0000\u0000\u01df\u01e0\u0007\u000b\u0000\u0000\u01e0[\u0001"+
		"\u0000\u0000\u0000\u01e1\u01e3\u0003X,\u0000\u01e2\u01e1\u0001\u0000\u0000"+
		"\u0000\u01e2\u01e3\u0001\u0000\u0000\u0000\u01e3\u01e4\u0001\u0000\u0000"+
		"\u0000\u01e4\u01e5\u0003R)\u0000\u01e5\u01e6\u0005\u0001\u0000\u0000\u01e6"+
		"\u01e7\u0003h4\u0000\u01e7\u01e8\u0005\u0001\u0000\u0000\u01e8\u01e9\u0003"+
		"j5\u0000\u01e9]\u0001\u0000\u0000\u0000\u01ea\u01eb\u0003`0\u0000\u01eb"+
		"\u01ec\u0005\u0001\u0000\u0000\u01ec\u01ed\u0003h4\u0000\u01ed\u01ee\u0005"+
		"\u0001\u0000\u0000\u01ee\u01ef\u0003j5\u0000\u01ef\u01f0\u0005\u0000\u0000"+
		"\u0001\u01f0_\u0001\u0000\u0000\u0000\u01f1\u01f6\u0003b1\u0000\u01f2"+
		"\u01f3\u0005\u0001\u0000\u0000\u01f3\u01f5\u0003b1\u0000\u01f4\u01f2\u0001"+
		"\u0000\u0000\u0000\u01f5\u01f8\u0001\u0000\u0000\u0000\u01f6\u01f4\u0001"+
		"\u0000\u0000\u0000\u01f6\u01f7\u0001\u0000\u0000\u0000\u01f7a\u0001\u0000"+
		"\u0000\u0000\u01f8\u01f6\u0001\u0000\u0000\u0000\u01f9\u0200\u0003Z-\u0000"+
		"\u01fa\u0200\u0003R)\u0000\u01fb\u0200\u0003p8\u0000\u01fc\u0200\u0003"+
		"n7\u0000\u01fd\u0200\u0003d2\u0000\u01fe\u0200\u0003f3\u0000\u01ff\u01f9"+
		"\u0001\u0000\u0000\u0000\u01ff\u01fa\u0001\u0000\u0000\u0000\u01ff\u01fb"+
		"\u0001\u0000\u0000\u0000\u01ff\u01fc\u0001\u0000\u0000\u0000\u01ff\u01fd"+
		"\u0001\u0000\u0000\u0000\u01ff\u01fe\u0001\u0000\u0000\u0000\u0200c\u0001"+
		"\u0000\u0000\u0000\u0201\u0202\u0005\u00f7\u0000\u0000\u0202\u0203\u0005"+
		"\u0005\u0000\u0000\u0203\u0204\u0005\u00ff\u0000\u0000\u0204\u0205\u0005"+
		"\u0006\u0000\u0000\u0205e\u0001\u0000\u0000\u0000\u0206\u0207\u0005\u00f8"+
		"\u0000\u0000\u0207\u0208\u0005\u0005\u0000\u0000\u0208\u0209\u0005\u00ff"+
		"\u0000\u0000\u0209\u020a\u0005\u0006\u0000\u0000\u020ag\u0001\u0000\u0000"+
		"\u0000\u020b\u020c\u0007\f\u0000\u0000\u020ci\u0001\u0000\u0000\u0000"+
		"\u020d\u020e\u0007\r\u0000\u0000\u020ek\u0001\u0000\u0000\u0000\u020f"+
		"\u0210\u0007\u000e\u0000\u0000\u0210m\u0001\u0000\u0000\u0000\u0211\u0212"+
		"\u0007\u000f\u0000\u0000\u0212o\u0001\u0000\u0000\u0000\u0213\u0214\u0007"+
		"\u0010\u0000\u0000\u0214q\u0001\u0000\u0000\u0000\u0215\u0216\u0007\u0011"+
		"\u0000\u0000\u0216s\u0001\u0000\u0000\u0000\u0217\u0218\u0007\u0012\u0000"+
		"\u0000\u0218u\u0001\u0000\u0000\u0000\u0219\u021b\u0003X,\u0000\u021a"+
		"\u0219\u0001\u0000\u0000\u0000\u021a\u021b\u0001\u0000\u0000\u0000\u021b"+
		"\u021c\u0001\u0000\u0000\u0000\u021c\u021d\u0003R)\u0000\u021d\u021e\u0005"+
		"\u0001\u0000\u0000\u021e\u021f\u0003t:\u0000\u021fw\u0001\u0000\u0000"+
		"\u0000\u0220\u0226\u0003|>\u0000\u0221\u0226\u0003v;\u0000\u0222\u0226"+
		"\u0003~?\u0000\u0223\u0226\u0003\u0082A\u0000\u0224\u0226\u0003z=\u0000"+
		"\u0225\u0220\u0001\u0000\u0000\u0000\u0225\u0221\u0001\u0000\u0000\u0000"+
		"\u0225\u0222\u0001\u0000\u0000\u0000\u0225\u0223\u0001\u0000\u0000\u0000"+
		"\u0225\u0224\u0001\u0000\u0000\u0000\u0226y\u0001\u0000\u0000\u0000\u0227"+
		"\u0234\u0005-\u0000\u0000\u0228\u0234\u0005.\u0000\u0000\u0229\u0234\u0005"+
		"/\u0000\u0000\u022a\u0234\u00050\u0000\u0000\u022b\u0234\u00051\u0000"+
		"\u0000\u022c\u0234\u00052\u0000\u0000\u022d\u022e\u0005\u00ff\u0000\u0000"+
		"\u022e\u0234\u00053\u0000\u0000\u022f\u0230\u0005\u00ff\u0000\u0000\u0230"+
		"\u0234\u0005\u00ea\u0000\u0000\u0231\u0232\u0005\u00ff\u0000\u0000\u0232"+
		"\u0234\u0005\u0098\u0000\u0000\u0233\u0227\u0001\u0000\u0000\u0000\u0233"+
		"\u0228\u0001\u0000\u0000\u0000\u0233\u0229\u0001\u0000\u0000\u0000\u0233"+
		"\u022a\u0001\u0000\u0000\u0000\u0233\u022b\u0001\u0000\u0000\u0000\u0233"+
		"\u022c\u0001\u0000\u0000\u0000\u0233\u022d\u0001\u0000\u0000\u0000\u0233"+
		"\u022f\u0001\u0000\u0000\u0000\u0233\u0231\u0001\u0000\u0000\u0000\u0234"+
		"{\u0001\u0000\u0000\u0000\u0235\u0236\u0007\u0013\u0000\u0000\u0236}\u0001"+
		"\u0000\u0000\u0000\u0237\u0238\u00038\u001c\u0000\u0238\u0239\u0005\u0001"+
		"\u0000\u0000\u0239\u023a\u0003\u0080@\u0000\u023a\u007f\u0001\u0000\u0000"+
		"\u0000\u023b\u023c\u0007\u0014\u0000\u0000\u023c\u0081\u0001\u0000\u0000"+
		"\u0000\u023d\u023e\u0003D\"\u0000\u023e\u0083\u0001\u0000\u0000\u0000"+
		"1\u0087\u008f\u0093\u0097\u009f\u00a2\u00ab\u00b1\u00b6\u00bd\u00c7\u00cb"+
		"\u00d7\u00de\u00e6\u00ed\u00f6\u0103\u010c\u0110\u0121\u012e\u0135\u0139"+
		"\u013e\u0143\u014b\u0153\u015a\u0164\u0170\u017b\u0181\u0186\u018d\u01a1"+
		"\u01a5\u01b2\u01bc\u01c0\u01c5\u01ce\u01dc\u01e2\u01f6\u01ff\u021a\u0225"+
		"\u0233";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}