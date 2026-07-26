// Generated from /Users/kaushalbx/StudioProjects/AshtadhyayiSandhi/parser/src/main/antlr/dev/panini/vyakaranam/VyakaranamParser.g4 by ANTLR 4.13.2

package dev.panini.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link VyakaranamParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface VyakaranamParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#ukti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUkti(VyakaranamParser.UktiContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#conditionalClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalClause(VyakaranamParser.ConditionalClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#loopClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopClause(VyakaranamParser.LoopClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vakya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVakya(VyakaranamParser.VakyaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#akhyataVakya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAkhyataVakya(VyakaranamParser.AkhyataVakyaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#purvaVakyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPurvaVakyaPada(VyakaranamParser.PurvaVakyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#uttaraVakyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUttaraVakyaPada(VyakaranamParser.UttaraVakyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#namaVakya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamaVakya(VyakaranamParser.NamaVakyaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vakyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVakyaPada(VyakaranamParser.VakyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#subantaVakyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubantaVakyaPada(VyakaranamParser.SubantaVakyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vakyaSambandha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVakyaSambandha(VyakaranamParser.VakyaSambandhaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sambodhana}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSambodhana(VyakaranamParser.SambodhanaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sambodhanaSuchaka}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSambodhanaSuchaka(VyakaranamParser.SambodhanaSuchakaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#pada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPada(VyakaranamParser.PadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaPada(VyakaranamParser.SankhyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaPuranaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaPuranaPada(VyakaranamParser.SankhyaPuranaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#puranaPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPuranaPratyaya(VyakaranamParser.PuranaPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaAbhyasaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaAbhyasaPada(VyakaranamParser.SankhyaAbhyasaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#katapayadiPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKatapayadiPada(VyakaranamParser.KatapayadiPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#aryabhatiyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAryabhatiyaPada(VyakaranamParser.AryabhatiyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#bhutasamkhyaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBhutasamkhyaPada(VyakaranamParser.BhutasamkhyaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaBhinnaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaBhinnaPada(VyakaranamParser.SankhyaBhinnaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaMathPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaMathPada(VyakaranamParser.SankhyaMathPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaStem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaStem(VyakaranamParser.SankhyaStemContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#subantaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubantaPada(VyakaranamParser.SubantaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#pratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPratipadika(VyakaranamParser.PratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#pratipadikaMula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPratipadikaMula(VyakaranamParser.PratipadikaMulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#pratipadikaVikara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPratipadikaVikara(VyakaranamParser.PratipadikaVikaraContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#mulaPratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulaPratipadika(VyakaranamParser.MulaPratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#kridantaPratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKridantaPratipadika(VyakaranamParser.KridantaPratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#unadyantaPratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnadyantaPratipadika(VyakaranamParser.UnadyantaPratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#unadiPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnadiPratyaya(VyakaranamParser.UnadiPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#taddhitaPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTaddhitaPratyaya(VyakaranamParser.TaddhitaPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#striPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStriPratyaya(VyakaranamParser.StriPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#samasaPratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSamasaPratipadika(VyakaranamParser.SamasaPratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#samasaAnga}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSamasaAnga(VyakaranamParser.SamasaAngaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#samasaSupAvastha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSamasaSupAvastha(VyakaranamParser.SamasaSupAvasthaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#supAvastha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupAvastha(VyakaranamParser.SupAvasthaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#asamasikaPratipadika}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsamasikaPratipadika(VyakaranamParser.AsamasikaPratipadikaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#asamasikaPratipadikaMula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsamasikaPratipadikaMula(VyakaranamParser.AsamasikaPratipadikaMulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#samuccitaSubanta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSamuccitaSubanta(VyakaranamParser.SamuccitaSubantaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#dhatuPrakriti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDhatuPrakriti(VyakaranamParser.DhatuPrakritiContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#dhatuMula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDhatuMula(VyakaranamParser.DhatuMulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sanadiPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSanadiPratyaya(VyakaranamParser.SanadiPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#upasargaKrama}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpasargaKrama(VyakaranamParser.UpasargaKramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#upasarga}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpasarga(VyakaranamParser.UpasargaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#tingantaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTingantaPada(VyakaranamParser.TingantaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vyutpattiTinganta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVyutpattiTinganta(VyakaranamParser.VyutpattiTingantaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vyutpattiAnga}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVyutpattiAnga(VyakaranamParser.VyutpattiAngaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vyutpattiAvayava}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVyutpattiAvayava(VyakaranamParser.VyutpattiAvayavaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#abhyasa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbhyasa(VyakaranamParser.AbhyasaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#adesham}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdesham(VyakaranamParser.AdeshamContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#lakara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLakara(VyakaranamParser.LakaraContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#tingPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTingPratyaya(VyakaranamParser.TingPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#supPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSupPratyaya(VyakaranamParser.SupPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#vikarana}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVikarana(VyakaranamParser.VikaranaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#agama}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAgama(VyakaranamParser.AgamaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#krtPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKrtPratyaya(VyakaranamParser.KrtPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayaKrtPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayaKrtPratyaya(VyakaranamParser.AvyayaKrtPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayaKridanta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayaKridanta(VyakaranamParser.AvyayaKridantaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayaPada(VyakaranamParser.AvyayaPadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#sankhyaAvyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSankhyaAvyaya(VyakaranamParser.SankhyaAvyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#mulaAvyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulaAvyaya(VyakaranamParser.MulaAvyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayaTaddhitanta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayaTaddhitanta(VyakaranamParser.AvyayaTaddhitantaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayaTaddhitaPratyaya}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayaTaddhitaPratyaya(VyakaranamParser.AvyayaTaddhitaPratyayaContext ctx);
	/**
	 * Visit a parse tree produced by {@link VyakaranamParser#avyayibhavaPada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvyayibhavaPada(VyakaranamParser.AvyayibhavaPadaContext ctx);
}