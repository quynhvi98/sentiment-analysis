/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package vn.com.epi.treebank.converter;

import static edu.stanford.parser.nlp.trees.GrammaticalRelation.DEPENDENT;
import static edu.stanford.parser.nlp.trees.GrammaticalRelation.GOVERNOR;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import edu.stanford.parser.nlp.trees.GrammaticalRelation;
import edu.stanford.parser.nlp.trees.GrammaticalRelation.GrammaticalRelationAnnotation;
import edu.stanford.parser.nlp.trees.GrammaticalRelation.Language;
import edu.stanford.parser.nlp.util.Generics;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class VietGrammaticalRelations {

	/**
	 * This class is just a holder for static classes that act a bit like an
	 * enum.
	 */
	private VietGrammaticalRelations() {
	}

	public static final GrammaticalRelation CUM_DANH_TU = new GrammaticalRelation(
			Language.English, "np", "cum danh tu", CumDanhTuGRAnnotation.class,
			DEPENDENT, "NP", new String[] {
					"NP < /.*/ =target"});

	public static class CumDanhTuGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation CHU_NGU = new GrammaticalRelation(
			Language.English, "sub", "chu ngu", ChuNguGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-SUB/ =target"});

	public static class ChuNguGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation CHU_NGU_BI_DONG = new GrammaticalRelation(
			Language.English, "lgs", "chu ngu the bi dong", ChuNguBiDongGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-LGS/ =target"});

	public static class ChuNguBiDongGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation TAN_NGU_TRUC_TIEP = new GrammaticalRelation(
			Language.English, "dob", "tan ngu truc tiep", TanNguTrucTiepGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-DOB/ =target"});

	public static class TanNguTrucTiepGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation TAN_NGU_GIAN_TIEP = new GrammaticalRelation(
			Language.English, "iob", "tan ngu gian tiep", TanNguGianTiepGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-IOB/ =target"});

	public static class TanNguGianTiepGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation VI_NGU = new GrammaticalRelation(
			Language.English, "prd", "vi ngu", ViNguGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-PRD/ =target"});

	public static class ViNguGRAnnotation extends
			GrammaticalRelationAnnotation {
	}
	
	public static final GrammaticalRelation KHOI_NGU = new GrammaticalRelation(
			Language.English, "tpc", "khoi ngu", KhoiNguGRAnnotation.class,
			DEPENDENT, "VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP|S|SQ|S-EXC|S-CMD|SBAR", new String[] {
					"/.*/ < /.*-TPC/=target"});

	public static class KhoiNguGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation TINH_TU_BN = new GrammaticalRelation(
			Language.English, "ttbn", "tinh tu bo nghia",
			TinhTuBoNghiaGRAnnotation.class, DEPENDENT,
			"VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < A=target", "AP < A=target", "RP < A=target",
					"PP < A=target", "QP < A=target", "MDP < A=target",
					"LST < A=target", "WHNP < A=target", "WHAP < A=target",
					"WHPP < A=target", "/.*/< AP=target" });

	public static class TinhTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation DONG_TU_BN = new GrammaticalRelation(
			Language.English, "đtbn", "dong tu bo nghia",
			DongTuBoNghiaGRAnnotation.class, DEPENDENT,
			"NP|VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < V=target", "AP < V=target", "RP < V=target",
					"PP < V=target", "QP < V=target", "MDP < V=target",
					"LST < V=target", "WHNP < V=target", "WHAP < V=target",
					"WHPP < V=target", "/.*/< VP=target" });

	public static class DongTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation SO_TU_BN = new GrammaticalRelation(
			Language.English, "stbn", "so tu bo nghia",
			SoTuBoNghiaGRAnnotation.class, DEPENDENT,
			"NP|VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < M=target", "AP < M=target", "RP < M=target",
					"PP < M=target", "QP < M=target", "MDP < M=target",
					"LST < M=target", "WHNP < M=target", "WHAP < M=target",
					"WHPP < M=target" });

	public static class SoTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation PHU_TU_BN = new GrammaticalRelation(
			Language.English, "ptbn", "phu tu bo nghia",
			PhuTuBoNghiaGRAnnotation.class, DEPENDENT,
			"NP|VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < R=target", "AP < R=target", "RP < R=target",
					"PP < R=target", "QP < R=target", "MDP < R=target",
					"LST < R=target", "WHNP < R=target", "WHAP < R=target",
					"WHPP < R=target", "/.*/< RP=target"  });

	public static class PhuTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation GIOI_TU_BN = new GrammaticalRelation(
			Language.English, "gtbn", "gioi tu bo nghia",
			GioiTuBoNghiaGRAnnotation.class, DEPENDENT,
			"NP|VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < E=target", "AP < E=target", "RP < E=target",
					"PP < E=target", "QP < E=target", "MDP < E=target",
					"LST < E=target", "WHNP < E=target", "WHAP < E=target",
					"WHPP < E=target" });

	public static class GioiTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation TRO_TU_BN = new GrammaticalRelation(
			Language.English, "trtbn", "tro tu bo nghia",
			TroTuBoNghiaGRAnnotation.class, DEPENDENT,
			"NP|VP|AP|RP|PP|QP|MDP|UCP|LST|WHNP|WHAP|WHRP|WHPP", new String[] {
					"VP < T=target", "AP < T=target", "RP < T=target",
					"PP < T=target", "QP < T=target", "MDP < T=target",
					"LST < T=target", "WHNP < T=target", "WHAP < T=target",
					"WHPP < T=target" });

	public static class TroTuBoNghiaGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation LIEN_HIEP = new GrammaticalRelation(
			Language.English,
			"lh",
			"lien hiep",
			LienHiepGRAnnotation.class,
			DEPENDENT,
			".*",
			new String[] { // remember conjunction can be left or right headed....
		          // this is more ugly, but the first 3 patterns are now duplicated and for clausal things, that daughter to the left of the CC/CONJP can't be a PP or RB or ADVP either
		          // non-parenthetical or comma in suitable phrase with conjunction to left
		          "VP|S|SBAR|SBARQ|SINV|SQ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN|PP|ADVP|RB)/ $+ !/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/=target)",
		          // non-parenthetical or comma in suitable phrase with conj then adverb to left
		          "VP|S|SBAR|SBARQ|SINV|SQ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN|PP|ADVP|RB)/ $+ (ADVP $+ !/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/=target))",
		          // content phrase to the right of a comma or a parenthetical
		          "VP|S|SBAR|SBARQ|SINV|SQ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN|PP|ADVP|RB)/) < (/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/ $+ /^S$|^(?:A|N|V|PP|PRP|J|W|R)/=target)",

		          // non-parenthetical or comma in suitable phrase with conjunction to left
		          "/^(?:ADJP|JJP|PP|QP|(?:WH)?NP(?:-TMP|-ADV)?|ADVP|UCP|NX|NML)$/ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN)$/ $+ !/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/=target)",
		          // non-parenthetical or comma in suitable phrase with conj then adverb to left
		          "/^(?:ADJP|PP|(?:WH)?NP(?:-TMP|-ADV)?|ADVP|UCP|NX|NML)$/ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN)$/ $+ (ADVP $+ !/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/=target))",
		          // content phrase to the right of a comma or a parenthetical
		          "/^(?:ADJP|PP|(?:WH)?NP(?:-TMP|-ADV)?|ADVP|UCP|NX|NML)$/ < (CC|CONJP $-- !/^(?:``|-LRB-|PRN)$/) < (/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/ $+ /^S$|^(?:A|N|V|PP|PRP|J|W|R)/=target)",

		          // content phrase to the left of a comma for at least NX
		          "NX|NML < (CC|CONJP $- __) < (/^,$/ $- /^(?:A|N|V|PP|PRP|J|W|R|S)/=target)",
		          // to take the conjunct in a preconjunct structure "either X or Y"
		          "/^(?:VP|S|SBAR|SBARQ|ADJP|PP|QP|(?:WH)?NP(?:-TMP|-ADV)?|ADVP|UCP|NX|NML)$/ < (CC $++ (CC|CONJP $+ !/^(?:PRN|``|''|-[LR]RB-|,|:|\\.)$/=target))",
		        });

	public static class LienHiepGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation TU_NOI = new GrammaticalRelation(
			Language.English, "tn", "tu noi", TuNoiGRAnnotation.class,
			DEPENDENT, ".*", new String[] { "/.*/ < C = target",
					"/.*/ < /,/ = target" });

	public static class TuNoiGRAnnotation extends GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation TU_VIET_TAT = new GrammaticalRelation(
			Language.English, "vt", "tu viet tat", VietTatGRAnnotation.class,
			DEPENDENT, ".*", new String[] { "NP < Ny = target" });

	public static class VietTatGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	public static final GrammaticalRelation CAM_THAN = new GrammaticalRelation(
			Language.English, "ct", "cam than", CamThanGRAnnotation.class,
			DEPENDENT, ".*", new String[] { "/.*/ < I = target " });

	public static class CamThanGRAnnotation extends
			GrammaticalRelationAnnotation {
	}

	// TODO would be nice to have this set up automatically...
	/**
	 * A list of GrammaticalRelation values. New GrammaticalRelations must be
	 * added to this list (until we make this an enum!). The GR recognizers are
	 * tried in the order listed. A taxonomic relationship trumps an ordering
	 * relationship, but otherwise, the first listed relation will appear in
	 * dependency output. Known ordering constraints where both match include:
	 * <ul>
	 * <li>NUMERIC_MODIFIER &lt; ADJECTIVAL_MODIFIER
	 * </ul>
	 */
	@SuppressWarnings({ "RedundantArrayCreation" })
	// private static final List<GrammaticalRelation> values = Generics
	// .newArrayList(Arrays.asList(new GrammaticalRelation[] { GOVERNOR,
	// DEPENDENT}));
	private static final List<GrammaticalRelation> values = Generics
			.newArrayList(Arrays.asList(new GrammaticalRelation[] { GOVERNOR,
					DEPENDENT,CHU_NGU, CHU_NGU_BI_DONG, VI_NGU, TAN_NGU_GIAN_TIEP, TAN_NGU_TRUC_TIEP, KHOI_NGU, CUM_DANH_TU, TINH_TU_BN, DONG_TU_BN, SO_TU_BN,
					PHU_TU_BN, GIOI_TU_BN, TRO_TU_BN, LIEN_HIEP, TU_NOI,
					TU_VIET_TAT }));
	/* Cache frequently used views of the values list */
	private static final List<GrammaticalRelation> unmodifiableValues = Collections
			.unmodifiableList(values);
	private static final List<GrammaticalRelation> synchronizedValues = Collections
			.synchronizedList(values);
	private static final List<GrammaticalRelation> unmodifiableSynchronizedValues = Collections
			.unmodifiableList(values);
	public static final ReadWriteLock valuesLock = new ReentrantReadWriteLock();

	// Map from Vietnamese GrammaticalRelation short names to their
	// corresponding
	// GrammaticalRelation objects
	public static final Map<String, GrammaticalRelation> shortNameToGRel = new ConcurrentHashMap<String, GrammaticalRelation>();
	static {
		for (GrammaticalRelation gr : values()) {
			shortNameToGRel.put(gr.toString().toLowerCase(), gr);
		}
	}

	public static List<GrammaticalRelation> values() {
		return values(false);
	}

	public static List<GrammaticalRelation> values(boolean threadSafe) {
		return threadSafe ? unmodifiableSynchronizedValues : unmodifiableValues;
	}

	public static Lock valuesLock() {
		return valuesLock.readLock();
	}

}
