# Kāraka sūtra audit

## Competing nominal object mediation under 1.4.32

Thirty-six additional JSON records check object/source, object/instrument and
object/locus competition under one linked दा + ल्युट् action. Each includes
explicit/unbound mediation, both outer voices, and endings fitting the object,
the other role, or neither. A nominal genitive resolving the object establishes
the recipient; a resolved non-object does not. If neither reading fits, the
recipient retains the occurrence-scoped unresolved object-mediator candidates
and AMBIGUOUS_OBJECT_MEDIATION diagnostic instead of receiving 1.4.32 proof.

Frame regressions independently check the mediator's own 1.4.49→2.3.66,
1.4.24→2.3.28, 1.4.42→2.3.18 and 1.4.45→2.3.36 chains, alongside the
recipient's 1.4.32→2.3.13 chain or unresolved dependency. The outer finite
voice does not determine a nominal mediator's case. Existing implementation
passed this matrix without changes. Current structured benchmark: 1323 passing
records, 1279 with supplied premises and 44 without. General joint/cyclic
solving, automatic intention discovery and full competition remain partial.

## 1.4.32 nominal recipient mediation scope

Twenty-eight JSON records and two frame regressions now check recipient/object
mediation under explicitly linked दा + ल्युट् governors. A recipient receives
1.4.32→2.3.13 only through a proven object of the same action occurrence, or
an explicit implied-object premise. Its nominal object retains 1.4.49→2.3.66.
Finite objects retain their own active/passive chains but cannot mediate an
unrelated nominal recipient. Neither an object of another nominal occurrence
nor an explicit link pointing outside the action scope establishes 1.4.32.
Unbound and explicitly linked cases are checked in both outer voices.

Omitting the object does not activate a sole-recipient fallback: the implied
object must be supplied as a premise. These tests verify scope and the existing
bounded canonical snapshot, not general joint/cyclic semantic solving. Existing
implementation passed without rule changes. Semantics rechecked against
[Kāśikā on 1.4.32](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).
Current structured benchmark: 1287 passing records, 1243 with supplied premises
and 44 without. Automatic intent discovery and complete lexical/competition
coverage remain partial.

## 1.4.44 under linked nominal hiring governance

Twelve JSON records now check the existing fixed-term hiring/payment domain
under परि-क्री + ल्युट्, in both outer voices. The applied branch has
1.4.44→2.3.13 and the declined branch has underlying 1.4.42→2.3.18. Each
counter-branch retains its canonical proof and a separate optional decision;
these are alternatives, not simultaneous names. Shared भ्याम् retains the
unresolved branches, and an invalid genitive does not select either designation.
Missing-payment and missing-instrument premises cannot establish the optional
recipient. Frame tests independently check dative/instrumental/shared/invalid
endings and original participant identity. Existing implementation passed.

Semantics rechecked against [Kāśikā on 1.4.44](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).
The supplied hiring reading is restricted to fixed-term acceptance for payment;
ordinary outright purchase does not establish it. Current structured benchmark:
1259 passing records, 1215 with supplied premises and 44 without. Automatic
construction/vivakṣā discovery and complete competing-reading coverage remain
partial. No noncanonical rules were added.

## 1.4.43 under linked nominal governance

Eight JSON records and two frame regressions now verify simultaneous कर्म/करण
names for a selected instrument of दिव् under a linked ल्युट् governor, in both
outer voices. Genitive object realization has 1.4.43→2.3.66; instrumental means
realization has 1.4.43→2.3.18. The other projection remains a COEXISTENT_CASE
branch with its exact chain, not an optional designation decision. An invalid
accusative retains both names but receives no case proof.

With an explicitly supplied instrument-denoting nominal reading, the shared
power is expressed and receives 1.4.43→2.3.46; no extra instrumental projection
is generated. This tests that reading as a premise rather than deriving it from
the suffix alone. Existing rule/engine behavior passed this matrix without a
new rule implementation. Semantics rechecked against
[Kāśikā on 1.4.43](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

Current structured benchmark: 1247 passing records, 1203 with supplied premises
and 44 without. Full lexical/competition coverage, automatic reading discovery
and the remaining audit obligations stay partial.

## Nominal 1.4.52 lexical and sense restrictions

Forty-four JSON records now check the existing नी, अद्/खाद्, वह् and भक्ष्
applicability boundaries under a linked णिच् + ल्युट् governor. Each construction
has active/passive outer-voice and genitive/instrumental ending checks. Selected
वह् controller and injurious भक्ष् readings receive 1.4.52→2.3.66. Excluded,
missing, or contradictory construction readings retain independently established
agent meaning through 1.4.54→2.3.18. Wrong endings preserve the designated role
without a case proof. Outer voice does not choose the nominal construction.

Frame regressions independently cover the eleven root/sense combinations. No
rule implementation change was needed for this matrix. The existing
commentary-based restrictions remain applicability conditions; no vārttika was
registered as an additional sūtra. Semantics rechecked against
[Kāśikā on 1.4.52](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

Current structured benchmark: 1239 passing records, 1195 with supplied premises
and 44 without. Automatic construction/sense discovery, nested causatives,
complete lexical coverage and the full competing-rule audit remain partial.

## Nominal causatives: causee and causer candidates

A nominal causee written in genitive could not reach 1.4.52 because its candidate
set was derived from the ending. Linked kṛt governance now adds causee role
candidates from the supplied CAUSEE_AGENT meaning; 1.4.52/53 still decide the
designation from the governor's own root and causative morphology. PROMPTER_CAUSE
likewise supplies a nominal agent candidate and participates in eligibility
validation. Candidate generation does not itself assign a kāraka.

Sixteen JSON records check paired causee/causer dependents under आस्/कृ + णिच् +
ल्युट् in both outer voices, with valid and invalid endings. Exact chains are
1.4.52/53→2.3.66 for the nominal causee object and 1.4.55→2.3.18 for the
hetu-agent. The HETU designation belongs only to the causer. Optional 1.4.53
retains underlying 1.4.54→2.3.18; if neither branch fits the written ending,
KARTR/KARMAN ambiguity remains rather than inventing a selected branch.
Frame negatives verify that outer ṇic cannot supply missing nominal causation.
All dependents retain their actual nominal action ID.

Current structured benchmark: 1195 passing records, 1151 with supplied premises
and 44 without. Automatic causee/causer discovery, nested causatives and the full
competition/voice matrix remain partial. No noncanonical rules were added.

## Unknown nominal prefixes and sanādi markers

Extended the unsupported-suffix correction to the governor's complete typed
prefix/sanādi/suffix inventory. Unknown prefix or sanādi markers previously
could establish kṛt governance and genitive proof despite an unresolved action
identity. A shared recognition predicate now prevents both those derived
premises and case assignment; independently supplied role meaning is retained.
Markers are preserved, not discarded or treated as absent.

Eight JSON records check agents and objects with genitive endings under unknown
prefixes and sanādi markers in active/passive outer clauses. A frame regression
also checks that no explicit-kṛt-governor provenance is manufactured. Existing
known-prefix, causative, prohibition and optionality records continue to pass.
Current structured benchmark: 1179 passing records, 1135 with supplied premises
and 44 without. Recognition of these components is not a claim that every
possible derivational combination has been grammatically validated.

## Unsupported nominal suffixes cannot license case

An explicitly linked nominal governor with an unrecognized kṛt suffix now
blocks vibhakti assignment instead of falling through to general 2.3.2/18.
Independent 1.4 role evidence is retained without a fabricated case proof.
Four JSON records check agent/object readings in both outer voices; a frame
regression covers the instrumental-agent failure that exposed this gap.

The JSON verifier now requires recognized suffix identities by default.
Intentional unknown-suffix cases declare `recognizedAffix: false`, so a typo
cannot silently masquerade as a valid negative. This exposed four old fixtures
using unrecognized `तुमुन्`; they now use canonical `तुमुँन्` and still pass
their root-ownership and optional-designation expectations. The two intentional
`कल्प्` negatives are marked explicitly. No permissive spelling alias was added.

Current structured benchmark: 1171 passing records, 1127 with supplied premises
and 44 without. This verifies typed governor identity and case licensing; it
does not establish complete derivation validation or raw Sanskrit parsing.

## Genitive prohibition and optionality boundaries, 2.3.69–71

The JSON benchmark now accepts typed derivational evidence on the actual kṛt
governor. Thirty-six records check future ण्वुल्, future णिनि, and debt णिनि
in both outer voices, with established, absent, and wrong-domain derivational
premises and both accusative/genitive endings. Only established 3.3.10/FUTURE,
3.3.3/FUTURE, or 3.3.170/DEBT premises activate the corresponding 2.3.70
prohibition. A future meaning alone does not establish the derivational domain.
Ordinary nominal genitive remains mandatory when that evidence is absent or
wrong; the prior precedence fix does not suppress a verified prohibition.

Twelve further records check शतृ/क्तवतुँ object genitive prohibitions under
2.3.69 and both directions of the optional agent cases under 2.3.71. Rejected
genitives must record the canonical prohibition, not merely lack case proof.
The benchmark verifier now supports that explicit provenance assertion. Both
optional kṛtya-agent cases retain the other case as an alternative. No
noncanonical rules were added. These are supplied-derivation tests; automatic
derivation/sense discovery, remaining suffixes and full competition stay partial.

Current structured benchmark: 1167 passing records, 1123 with supplied premises
and 44 without. Semantics checked against
[Kāśikā on 2.3.69–71](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Mandatory kṛt genitive versus wrong written endings

Nested-governor tests exposed an incorrect fallback: an agent under ordinary
2.3.65 could receive 2.3.18 merely because its written ending was instrumental
instead of genitive. The engine now checks the mandatory domains of 2.3.65–68
independently of observed case before permitting general 2.3.2/18 fallback.
Canonical prohibitions remain effective, and optional 2.3.71 agent instrumentals
remain available. Wrong endings preserve established 1.4 designations but
receive no false case proof. Special present-क्त and locus-denoting-क्त agent
regressions check this boundary alongside the kṛtya alternative.

Sixteen JSON records cover nested nominal actions, each dependent in both outer
voices, and valid/invalid agent and object endings. An inner nominal action can
own its agent while itself being an object of another nominal action. Its two
roles preserve distinct action IDs and original word occurrences. Current
structured benchmark: 1119 passing records, 1075 with supplied premises and
44 without. This does not close automatic meaning discovery, all special-case
precedence, or the full 1.4/2.3 audit matrix.

## Nominal occurrence identity in canonical evidence

Relations, kṛt-governance evidence, and genitive-eligibility premises now use
the same nominal action ID. The earlier relation-ID correction exposed a
duplicated `/krt_index` suffix in the latter two; that redundant reconstruction
has been removed. The JSON verifier checks evidence IDs against relation IDs.
Distinct but equal-valued nominal governor forms retain distinct action IDs;
dependents of one occurrence share its ID without merging another occurrence.
Frame regressions check both outer voices, original participants, exact
1.4.54→2.3.18 and 1.4.46→2.3.66 chains, and eligibility provenance.

Six additional JSON records cover identical nominal forms with separate
dependents. Current structured benchmark: 1103 passing records, 1059 with
supplied premises and 44 without. Raw-text semantic discovery and the remaining
rule-matrix obligations are still partial. An outdated inventory paragraph
claiming that removed vārttikas remained executable has also been corrected.

## 2.3.66 with two dependents of one nominal action

Added paired agent and special-locus-object checks under the same अधि-आस् +
ल्युट् occurrence. With established dual eligibility, the nominal agent receives
1.4.54→2.3.18 and the nominal object 1.4.46→2.3.66. Both share the nominal
action's identity, distinct from the outer finite action, and preserve their
original occurrences. A genitive ending on the agent retains its independently
established KARTR designation but receives no case proof. The object's chain
remains valid. Active versus passive outer voice changes neither result.

Eight additional JSON records check each dependent in these paired clauses.
The structured benchmark now passes 1097 records: 1053 with supplied premises
and 44 without. These tests establish the ordinary restriction from
[Kāśikā on 2.3.66](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/);
they do not claim automatic derivation-reading discovery or coverage of every
commentarial exception. The stronger identity regression exposed and corrected
a representation bug: nominal relations previously carried the outer finite
action ID despite correct nominal rule evidence. They now use the governor's
occurrence-scoped ID, shared by its dependents and distinct from the finite ID.
The JSON verifier checks this invariant for every linked kṛt record.

## Nominal-action matrix for 1.4.46–48

Extended linked ल्युट् governor checks to all three अधि roots (शी, स्था, आस्)
and all four वस् prefixes (उप, अनु, अधि, आङ्), in both active and passive
outer clauses. Twenty additional JSON records cover the previously missing
root/prefix combinations with and without supplied locus meaning. Positive
nominal कर्म readings assert 1.4.46/48→2.3.66; the outer voice cannot change
the nominal action's case rule. The structured benchmark now passes 1089/1089
records: 1045 with supplied premises and 44 without.

Frame regressions additionally reject missing, wrong, reversed, and unknown
nominal prefixes despite a qualifying outer prefix. Explicit nominal fasting
meaning retains 1.4.45→2.3.36 in either outer voice. No new noncanonical sūtra
was registered. This checks the existing sense restriction rather than claiming
it is a separate canonical rule. Lexical sense discovery and the full competing
construction matrix remain partial.

Semantics rechecked against [Kāśikā on 1.4.46–48](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/)
and [2.3.65–66](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Prior-action ownership permutations and competing meanings

The independent JSON benchmark now runs seven base cases plus four action-local
word-order variants. The variants preserve the same expected ownership, case
chains, patient agreement, and original participant identities when finite
patients or independently selected agents precede or separate the prior object
and its head. A competing lexical agent/instrument case must retain the blocking
ownership diagnostic; merely having an explicit agent reading is insufficient.
This covers bounded cross-action permutations, not arbitrary Sanskrit word order
or all nonfinite constructions.

## Evidence-based preposed passive agent ownership

An instrumental written before a ktvā/lyap boundary can now be attached to the
finite passive or bhāve action when a candidate analysis establishes KARTR,
TRTIYA, and independent lexical-entry or clause-context agent meaning. Voice,
case morphology, and root valency alone cannot authorize the attachment. The
candidate is discarded; final frames retain the original participant occurrence
and analyze its canonical 1.4.54→2.3.18 chain again. Every preposed instrumental
must satisfy the ownership check; unresolved ones retain the blocking diagnostic.
Tests cover finite patient agreement, bhāve, multiple prior actions, source
identity, and the no-premise negative. Seven base JSON fixtures include the selected
preposed variant. Ordinary passive prior-chain execution remains guarded.

## Several prior actions and one finite controller

For multiple ktvā/lyap projections of the same original written clause, each
prior action now receives a direct shared-agent link to the established finite
controller. The adjacent temporal links remain separate. This closes the gap
where only the final prior action inherited the controller under 3.4.21.
The link preserves the finite participant's written realization and does not
create another nominal occurrence or a new vibhakti evidence chain. Ambiguous
agent/tool readings block sharing. Regression tests cover both a three-action
chain and its passive ambiguous-agent counterpart. Unresolved preposed passive
instrumental attachment and the broader audit obligations below remain unfinished.

## Passive and bhāve main actions after ktvā/lyap

Removed the grammar-only blanket exclusion of a yak main action. Finite
nominatives remain with that action as patients, not shared agents. With an
established instrumental-agent reading, the finite agent has 1.4.54→2.3.18,
the passive patient has 1.4.49→2.3.46, and the prior active object retains
1.4.49→2.3.2. Shared-agent links preserve the finite realization rather than
copying it into the prior frame. Tests cover a patient before the prior-action
boundary, patient-controlled singular/dual agreement and bhāve's impersonal form.

The positive agent cases disclose their lexical reading premises. An unselected
instrumental retains agent/tool ambiguity and cannot create a definite shared
agent. A preposed instrumental without independent finite-agent evidence still
produces an explicit unresolved-ownership diagnostic. Ordinary passive prior-chain
execution is separately guarded. Seven base JSON fixtures distinguish analyzed roles,
ambiguity, an agreement error and an unsupported attachment. This is progress
within the full audit, not completion of nonfinite/passive coverage.

## Finite controller licensing and ordinary kṛ agents

For active ktvā/lyap chains, the written nominative controller is now retained
on the finite action, including when it precedes the nonfinite word. Prior
actions share the agent through a link instead of receiving copied finite case
realizations. Parsed-source tests verify 1.4.54→2.3.46, occurrence identity,
before/after positions and singular/dual finite agreement.

The tests exposed a root-wide 1.4.55 overreach: ordinary kṛ carried the
PROMPTER_CAUSE premise in both its lexical entry and fallback profile. Removing
that premise restores ordinary 1.4.54 agent evidence. The positive ṇic test
still requires 1.4.55→2.3.46 with HETU. This fixes that lexical overgeneration;
it does not complete the wider 1.4.55 lexical/sense or nonfinite audit.

## Written ktvā/lyap action ownership in PVM analysis

Grammar and execution now share the structural partition of written prior
actions. The original nonfinite derivation, head span and nominal occurrences
are retained. Each grammar frame records its original source clause, and a
nonfinite head is explicitly distinguished from a finite source head. The
internal binding projection does not claim the source has the inherited tense
or person fields. Execution-only omitted values are not added to grammar frames.

The two-sum source now has a reviewed printing frame with the action genitive
licensed by 2.3.50 and the printed object by 1.4.49→2.3.2, followed by an
operand-free stop frame. Composite range choice consumes each operand's own
grammatical relation; it does not reclassify every operand under the choice root.
Source identity replaces frame-position assumptions in frequency handling.

This remains partial nonfinite coverage. Grammar supports the passive cases
described above; preposed instrumental ownership without independent agent
evidence remains unresolved, and ordinary passive-chain execution remains
unsupported. Complete nonfinite voice/case licensing and
multi-action permutation coverage remain outstanding.
Composite VM execution is not claimed to create separate executed history events
for every grammatical frame. The full 1.4/2.3 audit matrix below remains active.

## Established secondary objects in causatives (1.4.51)

Removed the blanket active-causative exclusion. An established secondary-object
reading can persist in a causative construction; an unannotated multiple-object
hypothesis alone cannot identify it there. The structural causative check applies
to finite/passive and nominal actions alike, avoiding the earlier voice-only
boundary. No new rule identifier is registered.

Ṣaṭkāraka's causative milking example distinguishes two objects, an instrumental
original agent and a nominative causer. Four source-backed JSON records check
each occurrence: secondary object 1.4.51→2.3.2, primary object 1.4.49→2.3.2,
original agent 1.4.54→2.3.18, and causer 1.4.55→2.3.46 with HETU retained.
Unit tests cover active/passive causatives and reject unannotated secondary-object
hypotheses in either construction. Complete action/analysis suites passed before
adding the final two occurrence checks; focused tests and all 1069 JSON records
pass afterward (1025 annotated, 44 unannotated). The preceding nine-module gate
passed before this causative correction. Full passive-promotion policy across
the lexical inventory and automatic multi-object event discovery remain open.

Source: [Ṣaṭkāraka, causative discussion](https://sa.wikisource.org/wiki/षट्कारकम्).

## Secondary-object versus specific-role readings (1.4.51)

Secondary-object readings now participate in the independent-meaning strength
comparison with ordinary roles. A stronger tool/locus/agent reading cannot be
silently replaced by a weaker secondary-object hypothesis. Conversely, a
stronger supplied secondary-object reading survives a weaker lexical tool
default, even with an incompatible observed ending. Equal established readings
retain both 1.4 proofs: a uniquely compatible case can resolve the reading;
both/neither compatible cases remain unresolved. This represents competing
vivakṣā readings, not grammatical optionality of 1.4.51.
The engine includes the explicit secondary-object or ambiguous-dvikarmaka
reading's strongest evidence in the existing competing-analysis path. Tests
cover equal and unequal object/instrument readings, exact counterproofs and
nullable case proof. Four JSON records cover the same boundaries. Complete
action and analysis suites pass; after simplifying the predicate, focused
competition tests and all 1065 JSON records pass (1021 annotated, 44 unannotated).
Causative applicability and full passive promotion remain separate obligations.

## Secondary-object designation independent of case compatibility (1.4.51)

An established SECONDARY_OBJECT reading in a represented dvikarmaka domain now
retains कर्म even when the observed ending does not offer a karman-compatible
case. Morphology/category/voice hypotheses cannot establish that meaning by
themselves. Existing candidate-supported construction hypotheses remain distinct
from this independent-meaning path. A provenance matrix checks all eight evidence
sources in active and passive contexts; two JSON cases check incompatible
instrumentals. They retain only 1.4.51, with no 2.3 proof or resolved case.
Complete action and analysis suites pass with all 1061 JSON records
(1017 annotated, 44 unannotated). Causative applicability, complete passive
promotion and competing specific-role interpretations remain open; the row is
still Partial.

## Secondary-object action ownership (1.4.51)

The dvikarmaka-domain context helper now uses the governing nominal action's
root rather than the enclosing finite verb. Removed obsolete derived/graded
दोह्/रोध्/चे/जे/मोष्/ने/हार/कर्ष/वाह forms from the root inventory. Unit tests
check both ownership directions and all removed compatibility forms. Three JSON
cases assert nominal दुह् under finite पठ् (1.4.51→2.3.2), reject the reverse
borrowed identity, and reject a derived stem as a root. Complete action and
analysis suites pass with all 1059 JSON records (1015 annotated, 44 unannotated).
The preceding nine-module gate passed before this change. The role-versus-case
boundary for invalid endings and causative applicability still need a separate
audit; this ownership fix does not close 1.4.51 or establish automatic secondary
object discovery.

Source: [Kāśikā/Kaumudī on 1.4.51](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.51.htm).

## Causative designation ownership (1.4.52–53)

The root-domain helpers for 1.4.52–53 previously inspected the enclosing finite
verb even when a nominal action supplied causative morphology. Both now use the
governing action's root. Removed the derived हार/कार/हर/कर spelling exception
from the हृ/कृ domain. Nominal actions cannot borrow the enclosing finite pada
for दृश् eligibility or the separate ātmanepada extension. अभिवद् construction
checks likewise use their own root and prefixes. Tests cover both ownership
directions, motion-root eligibility, नी exclusion, missing nominal णिच्,
derived-stem rejection and missing independent pada evidence.

The JSON runner now preserves optional `krt.sanadi`; previously that nominal
morphology could not be represented in the benchmark. Two records exercise
nominal कृ under finite गम् (1.4.53→2.3.2) and nominal गम् under finite कृ
(1.4.52→2.3.2). All 1056 JSON records pass (1012 annotated, 44 unannotated).
Complete action and analysis suites passed before adding the two records;
the focused ownership tests pass with the expanded benchmark. Broader nominal
voice/pada determination and event-level causee discovery remain open.

Source: [Kāśikā, 1.4.53](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.53.htm).

## Provenance of conditioned locus readings (1.4.47–48)

The conditioned अभिनिवेश locative usage and participant-supplied fasting sense
previously tested only label membership. Both now require clause or lexical
meaning evidence (strength at least two). Morphology, category, voice,
construction-hypothesis and distributed-valency records cannot activate those
readings and suppress a special karman designation. Independently declared
lexical fasting sense on the governing dhātu remains supported. A provenance
matrix covers all eight evidence sources for both constructions and checks
designation plus exact case chains. Established locative readings retain
1.4.45→2.3.36; weak readings retain 1.4.47/48 without an invented locative case
proof. No new noncanonical rule identity is registered. Full action and analysis
suites and all 1054 JSON records pass. The preceding nine-module gate for the
flat-spelling removal also passed; it preceded this provenance correction.

Sources: [Kāśikā, 1.4.47](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.47.htm),
[Kāśikā commentary on 1.4.48](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.48.htm).

## Removal of flat-spelling locus compatibility paths (1.4.45–48)

Deleted the no-AST अभिनिविश-prefix matcher and the old flat-root/present-stem
blacklist that suppressed ordinary adhikaraṇa. Rule 1.4.47 now requires the
governing finite or nominal action's recognized विश् root and ordered अभि–नि
prefixes. A flat spelling does not manufacture those construction facts.
Likewise, unsupported flat spellings do not erase an independently established
locus relation under 1.4.45. Removed the now-unused varṇa prefix-matching helpers.
New regressions cover four flat-spelling cases and correct/reversed/missing/
unknown structured prefixes. Complete action and analysis suites, including
all 1054 JSON records, pass. Existing structured special-locus and conditioned
locative-reading coverage remains in place. Full construction discovery and the
remaining source/lexical coverage gaps remain open.

## Div action ownership and removal of present-stem fallback (1.4.43)

The दिव् domain no longer uses open दीव्य-prefix recognition or accepts the
bare present stems दीव्य/दीव्य् as root identities. Only represented canonical
root spellings qualify. Rule 1.4.43 now takes root identity from its governing
finite or nominal action; an enclosing दिव् cannot license the rule for a
different nominal action, and an unrelated enclosing verb cannot block a
governing nominal दिव्. Negative cases preserve ordinary 1.4.42→2.3.18 when
an instrument premise is established, without adding simultaneous names.
The full action and analysis suites and coexisting-history integration passed
after the ownership change. After removing the remaining present-stem exception,
focused canonical/coexisting-history tests and all 1054 JSON records pass
(1010 annotated, 44 unannotated). Complete lexical senses and broad source-level
semantic discovery remain open; this slice does not close the 1.4.43 row.

## Obsolete lexical paths and PVM source review

Removed the no-AST धारय compatibility exception: 1.4.35 now requires the
canonical धृ identity plus its causative construction at every entry point.
A negative regression checks that a derived-stem spelling cannot replace those
facts, even with a causative voice flag. The obsolete `isLearning` root whitelist
had no production callers after 1.4.29 moved to formal-instruction premises;
it and its helper-only assertions are deleted. Unused varṇa substring/overlap
helpers are deleted as well. Complete action and analysis suites and the corpus
benchmark pass after these removals. Other lexical paths remain subject to audit;
this is not a claim that all historical shortcuts have been eliminated.

The PVM corpus now has 10 independently reviewed clauses and 11 passing
word-order variants, out of 453 clauses in 98 files. The three additional reviews
cover a false-valued existential condition and result printing before/after the
verb. They certify grammatical relations, not computed results or whole-file
meaning. 443 clauses remain unreviewed. The existing blocked two-sum clause is
an unresolved lexical-head analysis for वि + रम्; the root exists in Dhātupāṭha,
so that diagnostic alone cannot be treated as a source-writing error. No source
rewrite or guessed homonym selection is justified by that diagnostic alone.

## Anger action identity and lexical boundaries (1.4.37–38)

Anger roots were recognized through embedded substrings and open present-stem
prefixes. The helpers now recognize explicit root/stem forms and, for the
low-level composed-root API, an exact known prefix plus a recognized क्रुध्/
द्रुह् form. An arbitrary intervening or trailing string cannot establish either
rule. Structured finite and nominal actions own both root and prefix identity;
the enclosing verb cannot supply or remove a nominal action's upasarga.
Known prefixed क्रुध्/द्रुह् retains 1.4.38 precedence over 1.4.37, while unknown
prefixes establish neither reading. Tests cover opposing nominal/outer prefixes,
wrong nominal roots, unknown prefixes, embedded-root/stem-prefix negatives and
both low-level proof chains. Complete action and analysis suites pass, including
all 1053 JSON records (1009 annotated, 44 unannotated). The broader synonym class
and raw sandhi interpretation remain incomplete; no claim of full lexical
coverage follows from exact matching of the currently represented forms.

Sources: [Kāśikā/Nyāsa, 1.4.37](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.37.htm),
[Kāśikā, 1.4.38](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.38.htm).

## Recipient lexical identity boundaries (1.4.33, 34, 36)

Root-prefix matching in the three recipient-domain helpers admitted invented
roots beginning with रोच, श्लाघ/ह्नु/शप or स्पृह. These checks now use explicit
recognized root/stem forms, including canonical halant forms, rather than open
string prefixes. The written governing action supplies the root; a linked
nominal action cannot borrow the enclosing verb's matching identity. A unit
matrix covers positive chains, prefix lookalikes, low-level input and both
directions of nominal/outer root disagreement. Three JSON negatives cover the
same false-positive boundary at sentence-analysis level. The complete action
and analysis suites passed before adding those three JSON records; their
expanded benchmark passes all 1051 records (1007 annotated, 44 unannotated).
The focused lexical-identity test also passes after that data expansion. These finite lexical
forms do not claim complete synonym coverage for रुच्यर्थ or automatic sense
discovery. The selected Nyāsa policy for स्पृह remains unchanged.

Sources: [Kāśikā/Nyāsa, 1.4.33](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.33.htm),
[Kāśikā, 1.4.34](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/),
[Kāśikā/Nyāsa, 1.4.36](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.36.htm).

## Creditor construction morphology (1.4.35)

The Nyāsa explains धारयति here through causative धृ. Structured finite and
nominal actions now require that causative construction; a creditor premise
with bare धृ no longer suffices. The governing nominal action owns its root
and णिच्, so an enclosing causative cannot supply missing nominal morphology.
The initial exact-derived धारय exception has subsequently been removed; all
entry points require canonical धृ plus the causative construction. Eight existing boundary fixtures now
explicitly represent their intended active/passive causatives. Two new JSON
negatives retain creditor meaning with noncausative active/passive धृ and assert
absence of the special designation. Dedicated tests cover both voices, nominal
ownership and lexical lookalikes. Complete action and analysis suites pass,
including all 1048 JSON records (1004 annotated, 44 unannotated). The metadata
nine-module gate passed before this applicability correction; it does not prove
the new change across all modules. A subsequent nine-module gate covering the
causative correction passed. `CreditorSentenceTest` then passed a parsed
24-combination matrix: both word orders; ङे, भ्याम् and incompatible टा;
causative/noncausative; and active versus यक् constructions. The intransitive
noncausative यक् case is correctly bhāve, while causative यक् is karmāṇi.
Causative creditors retain 1.4.35→2.3.13 for dative-compatible endings; an
incompatible instrumental retains the designation without a 2.3 proof.
Noncausatives cannot receive 1.4.35 merely from creditor meaning. Ordinary
recipient readings are tested separately and are not forbidden by this check.
Automatic creditor-event discovery and full
lexical identity/sense coverage remain open.

Source: [Kāśikā/Nyāsa on 1.4.35](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.35.htm).

## Source, locus and causative inheritance metadata

Rules 1.4.25–31 now declare the inherited अपादान label from 1.4.24;
1.4.31 also records 1.4.30 for the agent reference. This does not import
1.4.24's complete separation predicate into the special constructions.
Rules 1.4.46–48 now declare the inherited आधार term (1.4.45), with कर्म
from 1.4.46 additionally retained in 1.4.47–48. Rule 1.4.47 also declares
the conditioned option from 1.4.44; 1.4.46 and 1.4.48 do not inherit it.
The existing व्यवस्थितविभाषा implementation remains conditioned by its supplied
usage premise rather than creating unrestricted alternate readings.
Rules 1.4.51–52 now put inherited कर्म (1.4.49) in `anuvrtti`, and 1.4.55
puts inherited कर्तृ (1.4.54) there, removing these assigning rules from their
`adhikara` sets. Exact registry tests cover each corrected reference set.
The full sūtra/action/analysis suites passed for the locus/causative corrections;
the nine-module gate covering the additional source-block metadata passed.
This audits declaration provenance, not complete semantic applicability.

Sources: [Kāśikā, 1.4](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/),
[1.4.47](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.47.htm),
[1.4.51](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.51.htm),
[1.4.52](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.52.htm),
[1.4.55](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.55.htm).

## Recipient-block inheritance metadata (1.4.33–41)

The recipient-block declarations now record inherited सम्प्रदान from 1.4.32
for 1.4.33–37 and 1.4.39–41. Rule 1.4.38 retains its existing 1.4.37 reference
for the anger-target condition, rather than inheriting the replaced recipient
designation. Rule 1.4.41 additionally records 1.4.40 for the preceding action's
agent. These are anuvṛtti links, not extra governing adhikāras or additional
applied designation proofs. A registry test checks the exact inheritance sets
and prevents their accidental placement in `adhikara`. This metadata correction
does not establish complete semantic discovery or close the corresponding rows.
Validation: the two canonical metadata tests, complete action and analysis test
suites, and all 1046 structured JSON benchmark records pass after this change.

Source: [Kāśikā, recipient block](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/),
and [1.4.35 inherited पदच्छेद](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.35.htm).

## Fixed-term hiring construction scope (1.4.44)

Kāśikā defines परिक्रयण as acceptance for a limited period through wages or
similar payment, contrasted with outright purchase. Applicability now requires
an established `FIXED_TERM_HIRING_PAYMENT` premise and a selected instrument,
rather than membership in a purchase-root spelling list. Explicit clause and
lexical premises qualify; case, category, voice and distributed valency alone
do not. The obsolete root-domain helper is removed, including its use in
instrument-hypothesis distribution. Tests cover provenance across every evidence
source and multiple root spellings, plus ordinary-purchase absence of the hiring
premise. Two JSON regressions assert both optional branches outside the former
root list. All 1046 JSON records pass (1002 annotated, 44 unannotated), as do the
focused optional-branch and coexisting-history integration tests. Automatic
hiring-event recognition and full lexical/voice competition remain open.

Source: [Kāśikā and Nyāsa on 1.4.44](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.44.htm).

## Optional decisions versus simultaneous designation (1.4.43–44, 53)

The earlier model incorrectly treated 1.4.43 as an optional replacement of
करण by कर्म. Kāśikā/Padamañjarī explicitly distinguishes च's simultaneous
names from अन्यतरस्याम्'s alternatives. The registered 1.4.43 is now nonoptional,
inherits साधकतमम् from 1.4.42, and records both names in `coexistingKarakas`.
Case realization selects a projection without deleting the other designation.
Alternative projections use COEXISTENT_CASE, with no fictitious declined rule.

Expression of this shared kāraka power through one name expresses both names.
The generic co-designation interface participates in abhihita handling for
finite passive and supplied kṛt readings. A passive nominative therefore does
not generate an additional instrumental; two older JSON expectations were
corrected. Unknown/invalid case projection retains both semantic names but
cannot manufacture an execution operand. A recipient's explicit object link
can still use the established coexisting कर्म status of an instrumental form.

For genuine options, 1.4.44 and 1.4.53, the engine now constructs both semantic
branches independently of case candidates. A uniquely compatible case selects
the corresponding branch; when both or neither fits, both role interpretations
remain. The opposite form is retained in either direction, including when the
observed form is instrumental. Rule 1.4.44 inherits 1.4.42; 1.4.53 records the
inherited terms of 1.4.49/52 as anuvṛtti rather than mislabelling 1.4.49 an
adhikāra. Original-agent meaning for 1.4.53 must be established; case/category/
voice hypotheses alone cannot activate it. No commentary extension is added.

An optional decision is separate from designation proof. Declining 1.4.44
retains 1.4.42→2.3.18; declining 1.4.53 retains 1.4.54→2.3.18. The primary
analysis branch carries `primaryOptionalDecision`, and alternatives carry
`optionalDecision`; these fields do not assert a settled choice when the
overall result is unresolved. No unapplied optional rule masquerades as proof
of its underlying designation. Frame diagnostics and execution traces retain
this distinction.

Forty new JSON cases bring the benchmark to 1044 records (1000 annotated,
44 unannotated). They cover both voices, both causee roots, shared endings,
invalid cases, missing antecedents, canonical proof chains and decision metadata.
Low-level tests also cover multiple case analyses and common expression through
a supplied instrument-denoting kṛt. Automatic semantic discovery, full sense/
competition matrices, and end-to-end value-history queries through coexisting
names remain open. Ordered occurrence metadata now carries simultaneous names
through binding, operation resolution and blueprint serialization; interpreted
history capture indexes each occurrence under its established names without
duplicating execution operands. Three core regressions cover mixed/repeated
occurrences, codec round trips and unresolved-value rejection. History value
queries now require captured values instead of reconstructing them from source
stems; two execution regressions cover missing captures and simultaneous-name
relation lookup. The execution suite passes after removing reconstruction, and
the two new regressions pass separately. Compiler propagation, mutable-value
snapshot parity and canonical end-to-end history coverage remain unverified;
these changes do not close the 1.4.43 row or prove the broad multi-module gate.
Direct compiled actions now lower the ordered occurrence metadata to history
roles and freeze captured values before action execution. `SnapshotHistoryValue`
has a stack verifier check and native bytecode emission; compiler IR tests pass,
including nested mutable-collection isolation and retrieval through both stored
names. These are component checks, not canonical end-to-end licensing proof.
Dynamic participant-group dispatch now propagates ordered occurrence metadata
through JVM bytecode and resolves its values before invoking the action. A
transport regression checks mixed ordinary/coexisting occurrences, history under
both names and an unchanged numeric operand count. The compiler suite passes
before adding this regression, and the extended compiler IR suite passes after
it. Canonical end-to-end licensing, persistence and broad-gate verification
remain open; metadata transport alone does not prove semantic discovery.

An execution integration regression subsequently exposed a lexical mismatch:
the simultaneous-designation domain accepted `दिव` and `दीव्` but omitted the
executable DivDhatu's canonical `sourceSurface = दिव्`. The domain now includes
that exact form. `CoexistentHistoryBindingTest` passes from a parsed accusative
participant with an explicitly supplied instrument premise through
1.4.43→2.3.2, typed binding, semantic codec, operation selection, pre-action
snapshot and actual deterministic random-choice execution. Both names retain
the same collection participant; execution receives only its karman projection.
This does not demonstrate automatic discovery of the instrument premise,
passive/instrumental execution parity, persisted history or compiled licensing.
The nine-module gate covering the correction and root-spelling regression
exited successfully: core 214, parser 84, ashtadhyayi 926, analysis 72, actions
275, derivation 974, execution 744, compiler 536 and cli 36 tests, with no
failures (3861 tests total). The two-test integration class then passed with
additional parsed active-instrumental and passive-nominative history binding,
passive-instrumental rejection, and a fresh-store snapshot file round trip.
The persistence callback supplies the established frame; it does not prove
that clause-local semantic premises can be reconstructed from source alone.
Actual operation execution is covered for the accusative projection only;
instrumental and passive cases check grammar, binding and capture, without
claiming that every executable operation accepts those projections.

Sources: [Kāśikā/Padamañjarī on 1.4.43](<https://sa.wikisource.org/wiki/काशिका_(पदमञ्जरीव्याख्यासहिता)/प्रथमोऽध्यायः/चतुर्थः_पादः>),
[1.4.44](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.44.htm),
[1.4.53](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.53.htm).

Validation: all 1044 JSON records and the nine-module regression gate passed
with `--no-parallel --continue` (58 tasks: 20 executed, 38 up-to-date).
`git diff --check` is clean. The overall goal remains active.

## Canonical object-proof handoff and desired/undesired readings (1.4.32, 49–50)

The alternate 1.4.49/51 predicate shortcut inside 1.4.32 is removed. Written
mediation now requires a resolved, applied canonical object proof from the
engine's bounded same-action snapshot. The core context defaults to an empty
proof map rather than selecting a fallback implementation. A weaker śeṣa tag
cannot override a resolved object; an actual śeṣa resolution still cannot
mediate. The unresolved-mediator diagnostic recognizes special and optional
object proofs, while morphology-only object candidates supply no such proof.

The matrix covers all ten registered rules assigning कर्मन्: 1.4.38, 43,
46–53. Eighty JSON records assert both ends of the link, exact 1.4→2.3 chains,
active/passive or causative/passive-causative constructions, and written/unbound
discovery. The later audit above corrects 1.4.43 to simultaneous names; optional
causee/object-agent ambiguity remains distinct. These are supplied semantic constructions, not attestations that every
synthetic action sense expresses giving.

The matrix exposed a 1.4.49–50 evidence error: automatic desired-object valency
could eclipse an explicit anīpsita reading. The stronger occurrence meaning now
selects the correct proof. Established anīpsita meaning survives an invalid case
without inventing a 2.3 proof; category hypotheses alone cannot establish it.
Equally supported desired/undesired interpretations retain both proof chains
and COMPETING_KARAKA_ANALYSIS, while their shared कर्मन् designation remains
resolved. Case disambiguation counts distinct roles, so two object proofs do
not prevent rejection of a competing source reading. The generic दृश् profile
no longer asserts indifference merely from the verb of seeing.
Rule 1.4.50 explicitly records inheritance of कर्मन् from 1.4.49 in `anuvrtti`,
separately from the actual कारके adhikāra (1.4.23).

The JSON benchmark now contains 1004 records (960 annotated, 44 unannotated).
Eight added śeṣa and twelve desired/undesired records supplement the eighty
object-designation records. Automatic vivakṣā discovery, nested/cyclic joint
resolution and complete exception/competition matrices remain open.

References: [1.4.32](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.32.htm),
[1.4.46](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.46.htm),
[1.4.50](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.50.htm).

Validation: all 1004 JSON records pass. The final nine-module gate passed:
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test :compiler:test :cli:test --no-parallel`
(58 tasks: 14 executed, 44 up-to-date). A prior concurrent run exceeded one
CLI subprocess's existing 30-second deadline; the nonparallel gate passed
without changing that deadline. `git diff --check` is clean. The remaining
Partial rows still prevent completion of the overall audit goal.

## Explicit object evidence and removal of blanket recipient exclusions (1.4.32)

A lone participant no longer establishes an implied object. A recipient needs
either a resolved same-action written mediator or an explicit
`IntendedObject.Implied` premise. The root-wide exclusion list is removed:
applicable special 1.4.33–41 constructions still take precedence, and unproved
prior-agent/destiny readings cannot become ordinary recipient intent.

The derivation API now accepts `intendedObject`; requesting SAMPRADANA alone
does not supply this missing premise. The CLI exposes `--implied-object` so an
established omitted object can be stated explicitly. Neither path falls back to
an invented case.

Compiler projections and pipelines can supply an object structurally. For a
declared result-destination construction, that explicit input supplies the
omitted-object premise to an otherwise unassigned, unambiguous dative finite
participant. It does not resolve shared endings, overwrite a proven role or
mediator ambiguity, attach an outer object to a nominal action, or consult the
runtime payload. Contextual analysis bypasses the grammar-only cache; a later
raw grammar query retains the original unresolved frame.

Forty-eight new generated JSON cases contrast absent and explicit object
evidence across twelve root spellings and active/passive frames. These are
logical rule regressions with supplied readings, not attestations of every
synthetic lexical sense. The benchmark now has 904 records (860 annotated,
44 unannotated). Unit/integration tests also cover provenance, derivation
requests, cache isolation, syncretic destination rejection and compiler result
destinations. No sūtra identity or commentary extension is added.

Reference: [Kāśikā, Nyāsa and other commentaries on 1.4.32](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.32.htm).
The rule remains Partial: automatic discovery of object-mediated intention,
the differing commentary accounts of donation, complete object-designation
coverage and joint/cyclic interpretation remain open. The two legacy shortcuts
identified above are removed rather than retained for compatibility.

Validation: all 904 JSON records pass. The full eight-module gate passed
(54 tasks: 10 executed, 44 up-to-date). The CLI suite and added derivation
regression then passed (41 tasks: 6 executed, 35 up-to-date). `git diff --check`
is clean. This validates the implemented changes, not completion of the Partial
coverage matrix.

## Strict analysis-to-execution handoff and nominal comparisons

Execution no longer invents a kāraka from a sup ending when the sentence frame
is missing or unassigned. Shared-ending alternatives must come from the frame.
Numeric operands retain their original AST identity rather than passing through
synthetic nominals. Runtime operation selection cannot reassign an established
role to make an operation fit.

Comparison standards and copular predicates are explicit typed operands, rather
than aliases for करण, अपादान or कर्मन्. Unambiguous copular constructions establish
the nominal links licensed by 2.3.72 (equality, instrumental/genitive) and 2.3.42
(distinct comparison reference, ablative). These facts survive blueprint
serialization, symbolic reference planning and JVM compilation. Ambiguous
attachments still require evidence. Quoted nominal cases are rendered as words;
an ablative and a locative in a quotation no longer manufacture a numeric range.

The finite action uses its selected lexical entry for valency. Declared operand
positions supply lexical evidence, including an explicitly declared recipient
for a result destination; canonical saṃjñā and case rules must still apply.
Instrumental valency does not automatically license accusative/dative instruments
outside the dedicated दिव्/parikraya domains. Ordinary passive instrument/agent
competition remains unresolved without independent evidence.

Recipient mediation now checks a bounded snapshot of other participants'
resolved canonical readings. A competing mediator that remains ambiguous cannot
silently supply कर्मन्: the dependent frame retains AMBIGUOUS_OBJECT_MEDIATION
and the binding layer cannot turn it into सम्प्रदान. Thirty-six additional JSON
records bring the benchmark to 856 (812 annotated, 44 unannotated). This replaces
the earlier local-predicate-only limitation below. It is not a general cyclic
constraint solver; the broader 1.4.32 donation-intent audit remains open, along
with the Partial matrix entries. The explicit-object audit above removes the
sole-participant fallback and blanket root exclusions.

Validation: all 856 JSON records pass. The full eight-module gate passed:
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test :compiler:test`
(54 tasks: 11 executed, 43 up-to-date). Compiler coverage includes repository
examples, dynamic bubble sort, history-qualified comparisons and reordered
comparison operands. The execution analyzer now uses the standard nominal
lexicon. A proven nominal link is no longer blocked by a conditional candidate
whose required semantic premises are absent; multiple proven links remain
ambiguous. These checks validate this implemented slice, not the open audit rows.

## Canonical eligibility of the written mediator (1.4.32)

The ordinary recipient rule no longer treats a DESIRED_OBJECT or SECONDARY_OBJECT
tag as sufficient evidence of mediation. The referenced participant must satisfy
the corresponding canonical 1.4.49 or 1.4.51 predicate for the same action and
its own case candidates. These predicates are called directly; recipient links
cannot recursively resolve one another through the rule engine.

This rejects stronger locus/source/instrument readings, unsupported secondary
objects under दा and causee-tagged participants that cannot take the ordinary
object designation. Established object meaning with a wrong ending remains
eligible, as do regular unambiguous active/passive object constructions. A
known recipient with an eligible written mediator also retains 1.4.32 when its
own ending is invalid, including when a written mediator is discovered without
an explicitly indexed link. Its case remains
unvalidated; no 2.3.13 proof is fabricated.

Forty JSON records cover written/unbound links, active/passive morphology,
stronger non-object meanings, incompatible special tags, and invalid endings on
either endpoint. Low-level provenance tests contrast unsupported object
hypotheses with explicit meaning and ordinary voice constructions.

Reference: [1.4.32](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.32.htm).
This earlier object-eligibility slice is superseded by the bounded mediator
resolution and explicit-object audits above. Complete object-designation
coverage, joint/cyclic interpretation and discovery of donation-versus-other
object-mediated intent remain open. The rule remains Partial.
Validation: all 820 structured JSON cases passed. The seven-module gate
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test`
passed (50 tasks: 8 executed, 42 up-to-date).

## Late recipient constructions, typed prefixes and गॄ identity (1.4.39–41)

Established destiny-target and prior-agent recipient meanings now license their
canonical designations independently of case candidates. Incorrect endings
retain the corresponding 1.4 proof and receive UNVALIDATED_VIBHAKTI. Hypothesis
provenance alone cannot establish the defining premises.

Rules 1.4.40–41 use typed Upasarga identities, reject every unknown identity in
the prefix list and inspect the action owning the participant, including a linked
kṛt. Rule 1.4.39 now also uses that action's root and prefixes. Positive and
negative nominal-action tests prevent the outer verb's root/prefixes from leaking
into these decisions; the tests explicitly select an unexpressed recipient.

For 1.4.41, the selected lexical entry must identify क्र्यादि गॄ शब्दे. तुदादि
गॄ निगरणे and absent lexical identity cannot establish this construction. Both
the catalog's गॄ spelling and the legacy short गृ notation are accepted only
with that lexical identity. The old अनुगृ test now selects the actual क्र्यादि
catalog entry instead of the first entry matching a short spelling.

General 1.4.32 now defers to applicable canonical 1.4.33–41 predicates. Its
unbound written-object discovery cannot replace a failed prior-agent or destiny-target
reading with an assumed ordinary recipient. Prior-agent prefix domains also
block that unbound fallback when their defining semantic premise is missing.
The later explicit-object audit above removes the sole-participant shortcut
and blanket root exclusions; broader object-mediated intent remains open.

Seventy-six JSON cases cover the two inquiry roots and each prescribed prefix,
valid/invalid case, missing meanings, wrong/unknown prefixes and roots, and the
swallowing homonym. Optional fixture `gana` supplies explicit lexical class.
Active/bhāve cases cover the intransitive inquiry and praise readings; promise
cases include transitive passive. Low-level tests separately weaken every premise,
vary candidate sets and check linked nominal-action ownership.

References: [1.4.39](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.39.htm),
[1.4.40](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.40.htm)
and [1.4.41](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.41.htm).
Discourse/prior-event extraction, complete lexical/sense coverage and the wider
special-role competition matrix remain partial.
Validation: all 780 structured JSON cases passed. The seven-module gate
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test`
passed (50 tasks: 4 executed, 46 up-to-date). The preceding broad run had exposed
one stale candidate-dependence unit assertion; it now checks designation
independence and retains a missing-meaning negative.

## Special recipient meanings and case-independent longing precedence (1.4.33–38)

The pleasure, intended-information, creditor, longing and anger constructions
now require independently established RECIPIENT and their defining special
premise. The prefixed क्रुध्/द्रुह् exception retains its कर्म designation.
Their saṃjñā predicates no longer require a case-derived role candidate. Known
meaning with an invalid ending retains its 1.4 proof and receives
UNVALIDATED_VIBHAKTI without a fabricated case proof.

For स्पृह्, the project's existing Nyāsa reading now gives an established
DESIRED_OBJECT precedence over 1.4.36 independently of the ending. Previously
a dative ending could suppress the intended कर्म reading and produce a false
1.4.36→2.3.13 proof. The corrected result retains 1.4.49 and reports invalid case.
This changes application of the existing canonical rules, not the registered
rule inventory or commentary policy.

Fifty-two JSON cases cover the six constructions in two prayogas, missing
premises, invalid endings and longing/object precedence. Low-level tests weaken
each defining premise separately and both together, across three candidate sets.
Canonical references: [1.4.33](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.33.htm),
[1.4.36](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.36.htm)
and [1.4.38](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.38.htm).

The candidate, prefix and गॄ lexical-identity findings recorded in this slice
are addressed by the later 1.4.39–41 audit above. The later explicit-object
audit also removes the sole-participant and root-wide recipient shortcuts.
Validation: all 704 structured JSON cases passed. The seven-module gate
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test`
passed (50 tasks: 6 executed, 44 up-to-date).

## General and special source boundaries (1.4.24–31)

The root-family blacklist formerly used by 1.4.24 has been removed. The general
rule now defers when one of the canonical 1.4.25–31 rules actually matches its
construction. It calls those rule predicates directly, so there is no duplicate
list of special semantic conditions to drift out of sync. An independently
established separation reference remains eligible when the special premise is
absent, even with a root that also occurs in a special construction.

Rules 1.4.25–31 now require established SOURCE and their defining special premise
independently of the ending. They preserve their designation and exact 1.4 proof
when the written case is invalid; no 2.3.28 proof is fabricated. The general rule
also requires established SOURCE and SEPARATION_REFERENCE, including when the
ending already suggests an ablative. Morphology-only provenance is insufficient
for either defining premise.

Eighty-four JSON cases check all seven special constructions, each in two
prayogas, across valid case, wrong case, missing special/source meaning, overlap
with separation and general separation without the special premise. These use
typed परा + जि, नि + वृत् + णिच्, नि + ली and अधि + इ where applicable, with
active, bhāve, transitive passive and active/passive-causative coverage.
Low-level tests weaken each defining premise separately and both together,
with compatible and incompatible candidate sets. The canonical 1.4→2.3 chains
and invalid-case diagnostics are checked independently in JSON.

References: [1.4.24](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.24.htm)
and [1.4.25](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.25.htm).
The commentary-only upasaṅkhyāna on the first page is not registered or restored.
Automatic construction discovery, open lexical classes and the remaining
cross-role/special-rule competition matrix remain partial.
Validation: all 652 structured JSON records passed. The seven-module gate
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test`
passed (50 tasks: 17 executed, 33 up-to-date).

## Ordinary agent, instrument and locus candidate independence

Established independent-agent meaning (1.4.54) now survives an incompatible
ending. Selected-instrument meaning (1.4.42) and established locus meaning
(1.4.45) also remain available when competing object meaning has equal evidence;
the ending no longer suppresses one semantic alternative before comparison.
The existing case-chain comparison selects a unique compatible reading or
retains both proofs with ambiguity and UNVALIDATED_VIBHAKTI if neither fits.
Special root/construction exclusions are preserved.

Thirty-eight JSON records cover independent agents and object/instrument,
object/locus and object/agent competition. Low-level morphology-only negatives
cover all four ordinary meanings. Causative-exception tests now assert that
an explicitly established agent with an inadmissible accusative retains only
1.4.54, with no case proof or optional case alternative; they still reject the
excluded 1.4.52 object designation.

Every fixture's declared prayoga is now checked against its inferred frame.
Sixteen intransitive passive-shaped fixtures formerly labeled KARMANI were
actually BHAVE and have been relabeled. This is a benchmark-description
correction; it adds no transitive-passive coverage.

Canonical references: [1.4.42](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.42.htm),
[1.4.45](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.45.htm),
[1.4.54](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.54.htm).
Automatic selection of the intended ordinary meaning and the complete
special-rule competition matrix remain partial.
Validation: all 568 structured JSON cases passed, and the seven-module test gate
passed (50 tasks: 5 executed, 45 up-to-date). The 1.4.54 matrix row is now marked
Partial to reflect the remaining meaning-discovery and competition work.

## Independent object meaning and case validation (1.4.49)

An established most-desired-object reading in a transitive action now licenses
the general कर्म designation independently of morphological candidates.
An incompatible ending retains 1.4.49, receives no case proof and produces
UNVALIDATED_VIBHAKTI. Morphology-only meaning cannot bypass candidate compatibility;
intransitive purpose governors still cannot acquire an ordinary object this way.
Twenty-eight JSON cases cover valid endings, invalid endings and absent meanings
in active, passive, causative and passive-causative clauses. The sam-jñā
construction still requires its own structural premises to license 2.3.22.

Nominal derivation now records the requested participant reading separately
from other dhātu valencies. The latter retain DHATU_VALENCY provenance instead
of silently becoming equally explicit facts about that participant. Explicit
conflicting input meanings remain competing readings. Automatic discovery of
agent-relative desire and the full competing-rule matrix remain partial.
Canonical reference: [1.4.49](https://sanskritdocuments.org/~sanskrit/learning_tools/sarvanisutrani/1.4.49.htm).
Validation: all 530 structured JSON records passed. The seven-module gate
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test :derivation:test :execution:test`
passed (50 tasks: 5 executed, 1 from cache, 44 up-to-date). This validates the
implemented slice, not completion of the remaining partial audit matrix.

## Ordinary source/recipient competition

Ordinary semantic evidence comparison now includes source (1.4.24) and recipient
(1.4.32), alongside agent, object, instrument and locus. Weaker evidence cannot
override a stronger established reading. Equal ordinary readings are compared
against their canonical case chains; a unique compatible reading resolves, while
multiple compatible readings retain ambiguity and their separate proofs.
If neither reading validates the written ending, both remain ambiguous and an
UNVALIDATED_VIBHAKTI diagnostic is emitted. Specialized-rule precedence is unchanged.
Eighteen JSON regressions cover these comparisons in active/passive constructions.
This closes this comparison gap; the remaining partial matrix entries still
require their own audits.

## Current scope: canonical Aṣṭādhyāyī sūtras only

The user's canonical-only instruction supersedes earlier commentary-extension
work below. The added executable vārttikas 1.4.24V, 1.4.53V, 2.3.32V1,
2.3.69V1 and 2.3.66V1/V2 have been removed from the kāraka/vibhakti rule lists
and their implementations removed. The commentary-only 2.3.61V and 2.3.71V
prohibition branches have also been removed. Earlier descriptions of these
extensions are historical audit findings, not current supported behavior.
`CanonicalKarakaRulesOnlyTest` rejects noncanonical identities in both lists.
Commentary references remain documentary; no replacement extra rule identities
are introduced. Commentary-dependent extensions are intentionally out of scope
under this instruction, rather than missing canonical sūtras.
After removal, the five grammar-module test suites and
`:execution:test --tests dev.panini.execution.CollectionSummationTest` passed
(46 tasks; 15 executed, 31 up-to-date). The focused canonical-only registry,
inference and frame regressions also passed.

This is the authoritative work list for the sentence analyzer's kāraka layer. A
green module test alone does not change a row to **Audited**. That status
requires: canonical metadata, a positive semantic assignment, a negative
non-application case, relevant precedence/optionality checks, and an exact
1.4→2.3 evidence-chain assertion.

Status meanings:

- **Audited** — all applicable obligations above are covered.
- **Implemented** — reachable implementation and positive exact-chain coverage exist.
- **Partial** — implementation exists, but one or more audit obligations remain.
- **Governing** — an adhikāra rather than a participant assignment.
- **Excluded** — deliberately not a 1.4 kāraka rule; the reason is recorded.

## 1.4.24 → 2.3.28: source designation before case validation

Forty-four new JSON cases audit ordinary separation sources in active,
passive, causative, and passive-causative constructions. They cover
singular and shared dual/plural ablative endings, missing source or fixed
separation-reference meaning, a bare ablative, and incompatible
instrumental/genitive/dative endings. Shared endings preserve the full
voice-sensitive hypothesis set when sufficient meaning is absent.

Twelve negatives reproduced a designation error: 1.4.24 required an
apādāna case candidate even when independent source and separation-reference
meaning were established. The ending therefore erased the semantic role
instead of being validated against it. Established evidence for both
meanings now allows 1.4.24 independently of the incompatible case
hypothesis. The result retains APADANA and 1.4.24 while emitting
UNVALIDATED_VIBHAKTI; it does not invent a successful 2.3.28 assignment.
Morphological hypotheses alone do not gain that independent-evidence path.

Existing source-rule tests were updated only for this intentional 1.4.24
behavior change, with an added weak-provenance negative. The other source
saṃjñā predicates and lexical exclusions remain unchanged. The Kāśikā's
canonical separation-reference definition and 2.3.28 case rule support the
distinction; commentary-only extension identities remain excluded.

All 484 records pass (456 annotated, 28 without supplied semantic facts).
All seven complete module suites passed (50 tasks; 6 executed, 44
up-to-date). `git diff --check` passed. The complete 1.4.24/2.3.28 audit
remains Partial: special-source interactions and source/recipient versus
other-role evidence ranking still require further checks.

Sources: [Kāśikā 1.4.24](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/),
[Kāśikā 2.3.28](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Joint case alternatives for 2.3.27

The previously open joint-reinflection representation is now available as
`NominalCaseLinkAnalysis.jointAlternatives`. Each alternative identifies
all flattened nominal indices that must change case together, its target
case, and canonical 2.3.27 evidence marked optional and unapplied. It does
not mutate the source padas or offer an isolated incompatible case change.

Only an established, currently valid linked group receives the suggestion.
Every incoming member must follow 2.3.27, and all members must currently
validate the same case under that rule. A shared head includes all linked
pronouns in the switch. An ordinary cause member requiring genitive under
2.3.26 blocks the switch for the entire shared group. Missing premises,
competing heads, invalid case/number, and unresolved category readings do
not produce joint alternatives.

Ten existing JSON records now assert the pair-level choice in both
directions. Six additional active/passive records cover reversed order,
multiple pronouns sharing one head, and mandatory-genitive interference.
Every other structured record explicitly checks absence by default.
All 440 records pass (416 annotated, 24 without supplied semantic facts).
The actual-PVM corpus report also exposes grouped alternatives and checks
them against reviewed expectations, including AST and text word-order
alignment. Its source-review count remains separate from these fixtures.

This closes the representation gap for the verified 2.3.27 construction;
it does not implement automatic cause-meaning discovery, all agreement
groups, or complete corpus review. The overall audit remains incomplete.

Validation: all seven complete module suites passed together (50 tasks;
11 executed, 39 up-to-date), including all 440 structured records and
the actual-PVM corpus benchmark. `git diff --check` passed.

## Actual हेतु governors and canonical pronoun evidence

The nominal-link analyzer now recognizes an actual, unchanged मूल
prātipadika हेतु as a possible governor of an independently established
cause. A unique established attachment derives EXPLICIT_HETU_USE; cause
meaning is still required independently. Missing cause meaning and
competing हेतु occurrences do not establish an attachment. A derived
homonym or कारण is not treated as the literal हेतु word.

Pronominal recognition reuses the existing 1.1.27 predicate and its
canonical sarvādi catalogue. It does not introduce a separate hand-written
pronoun list. A listed but context-dependent entry whose qualifying use
is unknown, such as पूर्व, remains unresolved instead of being assumed
non-pronominal. Independently supplied category readings remain available
through the semantic-context API.

Established construction evidence is propagated to the written हेतु
member as well as its dependent. Case compatibility is checked on both
members, and pronominal number mismatches are rejected. A selected
explicit-hetu construction also blocks morphology-based verbal-agent
fallback, so an invalid nominative head is not silently reinterpreted as
the subject. These checks follow the co-referent construction illustrated
by कस्य हेतोः and केन हेतुना; see the [Nyāsa on 2.3.26](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.26.htm).

Construction discovery and case validation are deliberately distinct.
When cause meaning and a unique actual governor establish the construction,
a link may have no compatible observed case. That allows case validation
to reject the phrase rather than fall back to an unrelated rule. The
link's derived facts are asserted only after its required independent
premises are present; conditional candidates alone do not supply them.

Forty-four new JSON cases cover active/passive enclosing clauses, literal
and derived governors, missing/competing evidence, canonical pronouns,
context-dependent category uncertainty, both written members, wrong case
and number, nominative-head rejection, and shared genitive/locative dual
endings. All 434 records pass (410 annotated, 24 without supplied semantic
information). A frame regression also checks reversed order, an intervening
adverb, and an intervening finite verb, with exactly 2.3.26 on both members.

Written pairs must be reinflected together to switch between the 2.3.27
options. The current per-participant alternative list therefore does not
offer a case change that conflicts with its fixed written partner. Both
complete pair forms are tested, and the low-level supplied-construction
API still exposes both case options. A single frame-level joint-reinflection
alternative is not yet represented. Independent cause-meaning discovery,
unresolved contextual category uses, and complete modifier agreement remain
open; the overall rows are still Partial.

Validation: all seven complete module suites passed with the final
context-dependent category guard and positional regression (50 tasks;
18 executed, 32 up-to-date). All 434 structured records and the actual-PVM
corpus checks passed. `git diff --check` passed.

## 2.3.26–27: explicit-hetu precedence and pronoun options

The Kāśikā explains genitive for a cause in construction with the word
हेतु under 2.3.26, and instrumental alongside genitive for a pronoun under
2.3.27. The existing implementation correctly checked both CAUSE_HETU
and EXPLICIT_HETU_USE, plus PRONOMINAL_HETU for 2.3.27. It did not make
the earlier quality-cause rule defer throughout the explicit-hetu domain.
Four new active/passive negatives reproduced incorrect 2.3.25 assignments
to instrumental and ablative in that domain.

2.3.26 now exposes the construction independently of observed case;
2.3.24 and 2.3.25 defer to it, so their earlier case options cannot rescue
an invalid observed ending. 2.3.27 retains its genitive/instrumental
alternatives. The generic 2.3.23 explicit-hetu exclusion remains intact.
No extra sūtra or commentary-only all-case option is registered.

Twenty-four JSON cases cover ordinary and pronominal explicit-hetu cause,
both pronoun cases, missing inherited cause and explicit-use premises,
quality/debt overlap, and rejected non-pronominal instrumental/ablative,
under active and passive enclosing voices. All 390 structured-input
records pass (368 annotated, 22 without supplied semantic information).
These are supplied-premise tests, not automatic recognition of a written
हेतु governor or full sarvanāma classification. The complete 2.3.26/27
pipeline rows remain Partial for those recognition and attachment gaps.

Validation: all seven complete module suites passed (50 tasks; 11 executed,
39 up-to-date), including the structured and actual-PVM corpus benchmarks.
`git diff --check` passed.

Source: [Kāśikā, 2.3.26–27](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Corpus recipient, existential, and word-order reviews

Two more independently reviewed corpus clauses cover giving a value to a
pronoun recipient (local_pronouns.pvm) and existence within a collection
(membership.pvm). Expectations require exactly 1.4.32 → 2.3.13 for the
recipient and 1.4.54 → 2.3.46 for the existential subject, alongside the
existing object/locus chains. Pronoun runtime-reference lookup is not
claimed as part of grammatical role identification.

Seven JSON-declared word-order variants now pass for giving, existence,
placement, and printing. Each permutes every pada exactly once, preserving
the selected construction meaning. The runner checks the reviewed
participant expectations through both identity-preserving AST permutations
and fresh parsing of the reordered morphological fragments. It does not
use observed baseline assignments as the variant answer key. These seven
variants do not count as seven additional reviewed source clauses.

Corpus coverage is now 7 reviewed passes and 446 unreviewed clauses among
453 clauses in 98 files. All source spans remain present; the unresolved
रम् head retains a blocking diagnostic. This does not certify other word
orders, dislocated modifiers, every shared-ending interpretation, or full
corpus grammar correctness.

Validation: all seven complete module suites passed together (50 tasks;
1 executed, 49 up-to-date). The corpus runner passed all seven source
reviews and seven AST/text word-order variants. `git diff --check` passed.

## Corpus source spans and unresolved lexical heads

Bare nominal conditional branches now retain the span of the written
prātipadika when the parser supplies its implicit nominative ending.
Regression coverage checks लघु and गुरु against original document slices,
including an initial comment. Additional comment coverage verifies that
whole-line and trailing `#` text containing grammatical-looking Sanskrit
cannot create clauses or shift source locations. Comments remain in the
original document for offset accounting; the lexer excludes them from
grammatical input.

The public grammar-only adapter now allows an unresolved lexical head to
produce a partial frame with the existing blocking UNKNOWN_DHATU
diagnostic. It does not select an arbitrary रम् homonym. The default
PadaAnalyzer remains strict; unresolved-head support is explicit opt-in.
Memory analysis and executable binding still reject that unresolved head,
which a dedicated regression verifies. No canonical rule identities or
kāraka assignments were added to hide the lexical uncertainty.

Corpus inventory now produces frames for all 453 clauses and reports zero
missing spans or unsupported analyses. One frame still has a blocking
UNKNOWN_DHATU diagnostic. The reviewed count remains 5 passing clauses;
448 clauses remain unreviewed. See [CORPUS_BENCHMARK.md](CORPUS_BENCHMARK.md).

Validation: all seven complete module suites passed (50 tasks; 7 executed,
43 up-to-date), including the explicit `#` comment regression, bare nominal
span regression, unresolved-head execution/memory guards, and corpus
benchmark. `git diff --check` passed.

## Actual PVM corpus grammar coverage

KarakaCorpusBenchmarkTest now inventories all example/project PVM files
through the native document parser, including procedure bodies and loop
conditions. It reuses the execution adapter's grammar-only analysis and
does not execute programs or supply semantic premises. JSON expectations
check complete participant lists, exact chains, roles, cases, candidate
sets, additional designations, and optional alternatives. Observations
are written only to a separate generated report, never to the answer key.

The initial independent reviews cover five clauses: arithmetic objects,
collection gathering and placement locus, a project entry-point print,
and operands in a multi-file procedure body. The inventory has 98 files
and 453 clauses: 5 reviewed passes, 448 unreviewed, 452 analyzed, one
unsupported analysis, and two missing source spans. New clauses remain
explicitly unreviewed. Declaration headers are not yet clause-reviewed.
See [CORPUS_BENCHMARK.md](CORPUS_BENCHMARK.md) for artifact paths, matching
semantics, limitations, and the remaining unsupported/span cases.

Validation: the complete seven-module suite passed with the corpus test
included (50 tasks; 3 executed, 5 from cache, 42 up-to-date).
`git diff --check` passed. A green inventory test does not certify the
448 unreviewed clauses or close the full canonical coverage matrix.

## 2.3.23–25: debt exception and cause-case matrix

The Kāśikā on 2.3.24 explicitly describes ablative as an exception to
the general instrumental for a cause. The previous implementation checked
the observed ablative only in 2.3.24, so a selected non-agent debt cause
with instrumental still fell through to 2.3.23. Two active/passive JSON
negatives reproduced that incorrect 2.3.23 assignment.

2.3.24 now exposes its construction domain independently of the observed
case; 2.3.23 defers throughout that domain. An established KARTR and an
abhihita reading are excluded from 2.3.24. Direct rule tests verify both
exclusions and the blocked instrumental fallback. The composite premise
NON_AGENT_DEBT_REFERENCE denotes debt construed as a non-agent cause,
including inherited हेतौ; ordinary debt ownership alone is insufficient.
No separate apādāna designation or supplementary rule is introduced.

Sixteen JSON records cover active/passive enclosing frames, general cause
instrumental and rejected ablative, debt ablative and rejected
instrumental, quality-cause ablative/instrumental optionality, and a
feminine lexical reading contradicting a supplied non-feminine-quality
claim. The feminine reading retains general instrumental, while ablative
is rejected. Two further passive-causative cases verify the debt-as-agent
counterexample: the instigator retains exactly 1.4.55 → 2.3.18 and the
additional HETU designation, even against a contradictory non-agent debt
marker. All 366 records pass (344 annotated, 22 without supplied
semantic information). Their construction meanings are supplied; this
does not certify automatic cause/quality/debt interpretation. The full
rows remain Partial pending their remaining semantic and competing-rule
audit obligations.

Validation: all seven complete module suites passed (49 tasks; 11 executed,
38 up-to-date). The JSON runner then passed again with the two added
instigator counterexamples. `git diff --check` passed.

Source: [Kāśikā, 2.3.23–25](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.22 / 2.3.65: linked kṛt precedence

The primary commentary explicitly discusses competition between these
canonical rules: मातुः संज्ञाता takes genitive under the later 2.3.65,
rather than the finite construction's optional accusative/instrumental.
See the [Mahābhāṣya passage on 2.3.22](https://ashtadhyayi.github.io/suutra/2.3/2.3.22/?transliteration_target=kannada).
This is implemented as precedence between the existing canonical rules;
no vārttika identity or extra case-assigning rule is registered.

VerbalConstructionEvidence now reads a linked kṛt governor before the
enclosing finite verb. For the verified sam-jñā object domain, 2.3.22
defers when a bound kṛt governor licenses genitive by 2.3.65; 2.3.2 also
defers so rejected accusative is not restored through a fallback. The
genitive test uses a counterfactual candidate case, rather than making
precedence depend on the observed ending. It requires an actual linked
governor and respects the existing 2.3.69/70 prohibition facts.

Eighteen new JSON cases cover active/passive enclosing clauses, तृच् and
तृन् governors, genitive and rejected/allowed accusative/instrumental,
and independently supplied versus structurally derived SAM_JNA_OBJECT.
The तृच् object follows exactly 1.4.49 → 2.3.65. For तृन्, 2.3.69
prohibits genitive and the 1.4.49 → 2.3.22 alternatives remain available.
Outer passive voice does not express the bound nominal governor's object.
All 348 JSON cases pass (326 annotated, 22 without supplied semantic
information). This still does not establish independent object-meaning
discovery or every kṛt eligibility/exception interaction, so the complete
2.3.22 row remains Partial.

Validation: all seven complete module suites passed together after this
change (49 tasks; 14 executed, 35 up-to-date), including the expanded
348-record JSON benchmark. `git diff --check` passed.

## 2.3.22: optional object cases respect the governing AST

The Kāśikā describes optional instrumental in place of accusative for the
object of सम्-prefixed ज्ञा. The rule previously trusted SAM_JNA_OBJECT
even when an actual governing AST contradicted that construction. Three
new JSON negatives reproduced incorrect 1.4.49 → 2.3.22 chains for प्र +
ज्ञा, सम् + जप्, and प्र + जप्.

The case rule now checks the known governor's root and recognized prefix
identities. A linked kṛt governor is checked before the enclosing finite
verb, so the outer clause cannot lend a prefix to another governor. The
low-level API still permits a supplied complete construction premise when
no governing AST is available; this is explicitly supplied information,
not automatic prefix discovery.

Nine JSON cases cover active accusative/instrumental alternatives, passive
nominative via 2.3.46, blocking instrumental object realization when
abhihita in the passive, active causative accusative, missing independent
object meaning, and the three contradictory markers. Exact chains and
optional alternatives are asserted. All 330 JSON records pass: 308 with
supplied semantic information and 22 without. Existing inference tests
also verify unresolved case selection when both optional cases are given.

The full 2.3.22 row remains Partial: automatic construction enrichment for
linked nonfinite governors and interaction with their other case rules
still need dedicated audit coverage. The heading and canonical registry
identity are unchanged; no supplementary rule is added.

Validation of the behavior change: the complete seven-module command
(`core`, `parser`, `ashtadhyayi`, `analysis`, `actions`, `derivation`, and
`execution`) passed, with 49 tasks (10 executed, 39 up-to-date). The
Hindi explanation was subsequently clarified to name ज्ञा and अनभिहित;
the canonical heading is unchanged.

## Broad validation restored: negative loop predicates

The remaining recursive-dispatch failure had two causes. A loop predicate
containing न was passed as an independent negative utterance and classified
as CONSTRAIN, so it did not return a truth value. The loop now evaluates
its affirmative predicate and applies the loop-scoped negation exactly
once. It retains the original participant occurrences and does not change
standalone negative utterance handling or register any additional sūtra.

The recursive fixture also used a negated greater-than comparison where
its termination guard needed inequality with zero. That guard now uses
explicit सम … अस् equality under negation. Its state-dispatch greater-than
branch remains unchanged. The existing expected final value त्रीणि passes.
Additional bounded regressions cover both false→true and true→false
predicate negation, checking loop iteration and console output counts.

Validation: all seven complete suites passed together:
`:core:test :parser:test :ashtadhyayi:test :analysis:test :actions:test
:derivation:test :execution:test` (49 tasks; 2 executed, 47 up-to-date).
ConditionControlledLoopTest has 18 passing tests and PrakriyaMultiFileTest
has 28. The JSON benchmark remains 321/321. This supersedes earlier
execution-failure counts below, but does not certify the still-partial
canonical sūtra matrix or complete independent meaning extraction.

## Execution fallback-agent integration

Follow-up: the printing test expected success despite an unsupported explicit
instrumental participant. Both its baseline and same-named-procedure forms
correctly reject that participant under the existing no-discard invariant.
The regression now requires INVALID_VALUE, the unsupported-role diagnostic,
identical failures with/without the procedure, and no console output from
the procedure body. The printing implementation was not relaxed to ignore
the participant. The three focused execution classes now pass; recursive
negative-comparison dispatch remains open.

Final validation for this follow-up: core, parser, ashtadhyayi, analysis,
actions, and derivation suites passed. The complete execution suite ran
735 tests with one failure, the recursive two-counter dispatch above
(49 Gradle tasks; 1 executed, 48 up-to-date). All 321 JSON benchmark cases
remain passing. The complete audit is still active and unproven.

Collection concatenation exposed an ordering error after grammatical
ambiguity was correctly retained: the binder inserted the listener as an
implicit agent, then operation resolution coordinated that fallback with
a written participant selected as agent. This fabricated two agents and
failed the operation's maximum-one cardinality check.

The binder now marks only its own implicit-listener insertion. During joint
role resolution that marked value is deferred and restored only if no
written participant takes the agent role. Established written agents are
never removed. Regressions verify replacement by a selected written agent,
retention when the written participant is an instrument, and rejection of
an actual extra agent. This changes fallback ordering, not the canonical
kāraka candidate set or the sūtra registry.

NaturalOperationResolverTest and JointKarakaResolutionTest pass. In the
focused four-class execution run, 59 tests ran and only the two procedure
tests still failed: unrelated printing with a same-named procedure, and
recursive two-counter dispatch. The latter reaches a negative comparison
as a CONSTRAIN speech act instead of a condition result; this is an open
execution integration issue, not evidence of canonical kāraka correctness.

## Text coordination: frame integration follow-up

The text-based shared-recipient regression now passes. The frame analyzer
recognizes a final standalone च after two or more contiguous nominal
occurrences with identical candidate-case sets. These occurrences are passed
to the existing coordinated-valency analysis without rewriting the parser
tree or replacing any participant node. A different case, another word,
or the verb terminates the supported group; adjacency without च remains
insufficient. Explicit SamuccitaSubanta nodes continue to work.

The frame regression verifies exactly 1.4.32 → 2.3.13 for both recipients
in the final-च form, while separated and uncoordinated competitors remain
ambiguous. This is a bounded construction recognizer, not a complete
discourse/coordination attachment algorithm. Discontinuous coordination
still requires additional evidence and remains open.

An attempted parser-priority rewrite exposed assumptions in attribute
projection and numeral agreement; that rewrite was reverted. The final
change stays in frame interpretation and preserves the existing parser
contract. No parser implementation or parser test changes remain from this
attempt, and no new sūtra identities are registered.

Focused parser, analysis, and TypedOperandBindingTest suites passed before
the final expanded negative frame checks. The following broad run confirms
the remaining execution failures are collection concatenation, same-named
procedure/printing dispatch, and recursive two-counter dispatch. The old
text-coordination failure is closed; the three other failures remain open.

## 2.3.20/21 and coordinated recipient audit

Sixteen further JSON regressions cover body impairment in active/passive
frames (including कृ valency), absence of the impairment reading, wrong
genitive morphology, priority over the generic characteristic-mark reading,
state-identifying marks, and marks incorporated in a compound. All 321 JSON
records pass (300 annotated, 21 without supplied semantic premises).
These tests supply the specialized construction meanings; they do not
discover bodily impairment or an identifying state from unannotated text.
The Kāśikā on 2.3.20/21 supplies the semantic distinctions and compound
counterexample. Neither implementation needed changing for these cases.

Valency distribution now recognizes an explicit, homogeneous canonical AST
coordination as one eligible position. A frame regression verifies the
1.4.32 → 2.3.13 chain for both coordinated recipients; separate competing
nouns stay ambiguous. Core regressions reject overlapping/out-of-range
coordination indices and retain uncertainty for mixed morphology and an
additional competing participant. Governor ownership boundaries are retained.

Broad validation now includes the complete execution suite, rather than
only CollectionSummationTest. That revealed five failures initially. One
was an outdated passive भ्याम् candidate expectation: both agent and
instrument must remain possible. The other four remain open: the text-based
coordinated recipient test receives no canonical coordination node;
collection concatenation has an agent cardinality conflict; printing with
a same-named procedure fails; and recursive two-counter dispatch fails.
Do not treat the previous selected execution test as full-suite success.
The canonical AST coordination frame test and JSON runner pass. These
remaining execution failures and the partial coverage matrix prevent goal
completion.

## 2.3.19: accompaniment JSON regression matrix

Ten additional JSON regressions verify both active and passive enclosing
clauses: instrumental accompaniment with सह, with सार्धम्, and without a
written accompaniment particle; each required semantic premise is also
individually omitted. Successful cases establish only 2.3.19, with no
invented agent or instrument saṃjñā. Missing-premise cases establish neither
a kāraka nor a case. The Kāśikā on 2.3.19 explicitly permits a synonym and
absence of the written सह word; this is interpretation of the canonical
rule, not registration of an additional sūtra.

The JSON benchmark now contains 305 passing records: 286 have supplied
semantic premises and 19 do not. These cases do not certify independent
discovery of accompaniment, non-principal status, or its attachment in
unannotated sentences. The complete 2.3.19 row remains Partial.

Validation: all five grammar-module suites passed together with
`:execution:test --tests dev.panini.execution.CollectionSummationTest`
(47 tasks; 1 executed, 46 up-to-date). The focused JSON runner also passed.

## 1.4.26: prefix identity on a linked kṛt governor

The context predicate now reads a linked kṛdanta's own upasarga identities
before falling back to finite-verb or legacy root identity. Previously a
structured परा + जि nominal governor lost its prefix and could not reach
1.4.26. The semantic conditions SOURCE and UNBEARABLE_OBJECT remain
required; a prefix alone does not establish the असोढ reading.

`KrtGovernorFrameTest` checks the exact 1.4.26 → 2.3.28 chain, active and
passive enclosing clauses, missing unbearable sense, another prefix, and
unknown prefix identity. No extra rule identity is added. This closes this
specific frame integration gap; the complete competition and derivation
audit for the row remains open.

Validation: all five grammar-module test suites and the selected execution
`CollectionSummationTest` passed (47 tasks: 7 executed, 40 up-to-date).
The JSON benchmark remains 47/47. The new frame test exercises twelve
prefix/sense/outer-voice combinations, including the missing-sense negatives.

Source: [Kāśikā 1.4.26](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

## 1.4.46–48: nominal action prefixes and special karmans

Structured kṛt governors now supply their own root and prefixes for the
special locus designations, including the 1.4.45 exclusion predicate.
1.4.47 recognizes the nominal governor's अभि–नि sequence. Residence/fasting
checks for वस् use the owning action root rather than requiring a finite AST.
The existing fasting exclusion is preserved; no new commentary rule is added.

The three saṃjñā predicates no longer require accusative-derived KARMAN
candidates: established root, prefix and locus semantics designate the role
before case selection. Thus incompatible locative morphology can retain the
special karman while receiving no validated vibhakti; it does not silently
become an ordinary adhikaraṇa. The conditioned 1.4.47 locative interpretation
still requires its separate reading premise.

Bound dual genitive eligibility now describes both eligible roles under the
same nominal action, independently of DESIRED_OBJECT. The vibhakti rules
still check the actual saṃjñā. This lets a specially designated karman reach
2.3.66 without falsely labeling the locus as an īpsitatama object.
Frame regressions check all three exact 1.4 → 2.3.66 chains in both outer
voices, and reject designation when locus meaning is absent.

Source: [Kāśikā 1.4.45–48](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).
Full derivation, prefix/sense discovery and the complete competition matrix
remain open; these rows are not promoted to Audited.

Validation after final edits: the five grammar-module suites and selected
execution CollectionSummationTest passed (47 tasks: 9 executed, 38 up-to-date).
The 47-record JSON benchmark passed unchanged. The new nominal locus test
checks twelve root/prefix/locus-premise/outer-voice combinations.

## 1.4.55: retain the hetu co-designation

The rule already returned KARTR but discarded the separate hetu designation
from its public typed result. `KarakaDesignation.HETU` now passes from the
canonical 1.4.55 assignment through `KarakaResolution` to `KarakaRelation`.
It is a co-designation licensed by the same rule evidence, not a new kāraka
argument or an additional executable sūtra. Competing unresolved role readings
do not publish the winning branch's designation as established.

Regressions check the active 1.4.55→2.3.46 and passive-causative
1.4.55→2.3.18 chains, the absence of HETU on the causee and ordinary
1.4.54 agent, and the JSON causer result. Every other JSON record also checks
that the designation set is empty unless explicitly expected.
Automatic prompter/causee event linking and the full negative/competition
matrix remain open; this does not establish the entire row as Audited.

Source: [Kāśikā 1.4.55](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

Validation after final edits: all five grammar-module test suites and the
selected execution CollectionSummationTest passed (47 tasks: 20 executed,
27 up-to-date). The JSON benchmark passes all 59 records.

## 1.4.23: governing domain and executable membership

`KarakaCanonicalMetadataTest` verifies every canonical heading in 1.4.23–55,
unique registration, the governing rule's ADHIKARA action, and declaration of
1.4.23 on every following saṃjñā rule. `CanonicalKarakaRulesOnlyTest` additionally
checks that the analyzer's executable list is exactly 1.4.24–55, with no missing
or duplicate entry, and excludes the governing rule itself.

Direct invocation of `KarakeSutra.apply` previously manufactured an assignment
from the first candidate. It now rejects that unsupported operation. The rule's
predicate remains false, and a morphology-only apādāna hypothesis is checked
to receive neither a saṃjñā nor spurious 1.4.23 assignment evidence.
This verifies registry propagation and the nonassigning boundary. It does not
prove that every following rule's semantic conditions correctly delimit its
action relation; those remain individual audit obligations.

Source: [Kāśikā 1.4.23](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

Validation: focused registry/metadata tests and the final five grammar-module
suites plus selected execution CollectionSummationTest passed (47 tasks:
10 executed, 37 up-to-date). The JSON benchmark remains 59/59.

## 1.4.49: agent-relative and superlative counterexamples

Kāśikā distinguishes the horse from beans desired by the horse, and the
rice object from milk construed as an instrument. Eight JSON regressions
check both participants separately in active/passive constructions, preserving
1.4.42→2.3.18 and 1.4.45→2.3.36 alongside 1.4.49→2.3.2/46.
The meaning is explicitly supplied at each nominal occurrence; the tests do
not claim automatic desire-perspective extraction.

`IpsitatamaEvidenceTest` separately checks the low-level rule boundary:
a morphology-derived KARMAN candidate with no semantic premise receives no
assignment, while DESIRED_OBJECT and INDIFFERENT_OBJECT yield exact 1.4.49
and 1.4.50 chains in both voices. The sentence inferrer still supplies
case/voice object hypotheses, so this low-level negative does not prove that
the full frame pipeline independently recognizes īpsitatama meaning.
Automatic distinction of the desire-holder, semantic superlative, and competing
evidence remains open. No production rule change was needed for these fixtures.

Source: [Kāśikā 1.4.49](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

## Stronger locus/instrument meaning versus object hypotheses

The frame could discard a supplied locus or selected-instrument meaning when
accusative or passive nominative morphology nominated KARMAN. 1.4.49 now
defers when its desired-object premise has weaker evidence than those readings.
1.4.42 and 1.4.45 can establish their roles beyond case-derived candidates
when their meaning is stronger than the desired-object hypothesis; ordinary
case-supported readings remain available on equal-strength defaults.

This compares evidence for different readings, not grammatical priority between
equally applicable sūtras. Equal-strength semantic conflicts still require a
broader competing-reading model; this slice does not claim to resolve them.
Case validation remains separate: four JSON records check explicit locus and
instrument readings against active accusative/passive nominative hypotheses,
with exact 1.4.42 or 1.4.45 evidence and UNVALIDATED_VIBHAKTI.
Existing conditioned-abhiniveśa, fasting and ordinary-purchase tests now assert
the retained role and missing case proof rather than erasing the semantic role.
The full action and analysis suites passed before the final passive additions;
the final broader validation also passed: five grammar-module suites and
selected execution CollectionSummationTest, 47 tasks (5 executed, 42 up-to-date).
The JSON benchmark passed all 71 records.

## Ordinary-role competing readings

The resolver's former competing-reading branch handled only 1.4.42 versus
1.4.54. It now also preserves equal-strength independently supported readings
among the ordinary 1.4.42/45/49/54 predicates when more than one matches.
Special designation rules retain their separate precedence and optionality
handling; this does not turn apavāda competition into a free choice.

`CompetingOrdinaryReadingsTest` supplies alternative morphology analyses and
equal explicit object/instrument or object/locus meanings. Each reading retains
its own exact 1.4→2.3 chain, differing observed-compatible roles/cases leave the
result unresolved, and alternatives are COMPETING_ANALYSIS rather than optional
sūtra applications. The role candidate set includes the evidenced alternatives.
Ordinary instrument/locus/object predicates also compare evidence strengths so
a weaker meaning does not win merely by list order.

These are low-level alternative-analysis tests, not claims that one sup slot
has all those cases. The JSON runner still takes one supplied sup slot per
nominal. Full morphology-parse alternatives, special-construction conflicts and
all ordinary role-pair/voice combinations remain to be audited.

Validation: the complete action and analysis suites and final five grammar-module
suites plus selected execution CollectionSummationTest passed (47 tasks:
7 executed, 40 up-to-date). All 71 JSON benchmark records passed.

## Ordinary-role active/passive pair matrix

`CompetingOrdinaryReadingsTest` now covers all six pairs among KARTR,
KARMAN, KARANA and ADHIKARANA in both active and passive contexts.
Twelve equal-strength cases assert each branch's exact saṃjñā/case chain and
unresolved role. The passive KARTR/KARANA pair retains its shared validated
tṛtīyā while leaving the semantic role unresolved; other differing case pairs
leave the case unresolved as well.

Twenty-four further cases supply each pair's stronger reading in turn against
a voice-derived hypothesis. A shared evidence comparator now includes all
four ordinary meanings; previously the object/locus guards omitted independent
agent meaning, allowing a weaker object or locus hypothesis to win by order.
These comparisons rank evidence for interpretations; they do not introduce
new sūtras or reorder special designation rules.

The matrix uses explicitly supplied alternative morphological analyses and
does not prove automatic semantic extraction or full morphology parsing.
Special-rule precedence, construction-specific relations and additional
register/derivation combinations remain separate audit work.

Validation: the final five grammar-module test suites plus selected execution
CollectionSummationTest passed (47 tasks: 17 executed, 30 up-to-date).
The JSON benchmark remains 71/71; the 36 pair/voice/strength combinations
are additional low-level regressions, not additional JSON records.

## 1.4.29: formal instruction is not a closed dhātu list

The former `isLearning` root check excluded a structured अधि + इ AST and
Kāśikā's explicit आगमयति example. 1.4.29 now uses the supplied
FORMAL_INSTRUCTION_SOURCE construction premise plus SOURCE and the apādāna
hypothesis, without an extra closed-root condition absent from the sūtra.
No automatic inference from a prefix or a learning-like root is introduced.

`FormalInstructionSourceTest` checks structured अधि + इ, आङ् + गम् + णिच्,
and a supplied formal श्रु reading in active/passive contexts; every generic
source without the construction premise is rejected. The ordinary performer
genitive retains 2.3.50. Eight JSON records separately verify the two structured
source examples and missing-premise negatives, including passive causative.
The benchmark runner now preserves ordered verbal prefix lists.

This verifies behavior given explicit upayoga meaning; recognizing regulated
instruction and identifying its teacher automatically remain open.
Validation: all five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 4 executed, 43 up-to-date).
All 79 JSON records passed.
Source: [Kāśikā 1.4.29](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

## 1.4.28: concealment domain and intended observer

The rule now consults the owning action's structured नि + ली identity rather
than only a combined root string. Exact legacy निली/तिरोभू inputs remain
supported; substring lookalikes no longer establish the domain. Unknown or
wrong prefixes are rejected in the structured lexical path.

An independently established SELF_CONCEALMENT premise can supply अन्तर्धि
meaning outside that lexical slice, including explicitly interpreted धा
constructions. It is separate from the intended CONCEALMENT_OBSERVER and
SOURCE premises. The observer may actually see the agent; the supplied intent
does not imply successful concealment, following Kāśikā's इच्छति explanation.
No अन्तः-as-upasarga fiction or additional rule identity is introduced.

Eight active/passive JSON cases assert 1.4.28→2.3.28 and missing-domain/observer
negatives. `ConcealmentDomainTest` checks wrong/unknown prefixes and substring
lookalikes. Automatic concealment-event/observer recognition, complete अन्तर्धा
representation and broader sense competition remain open.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 15 executed, 32 up-to-date).
All 87 JSON benchmark cases passed.
Source: [Kāśikā 1.4.28](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

## Apādāna lexical boundaries and 1.4.27 structured prevention

Fear/protection, birth, manifestation, para-ji, learning and prevention lexical
predicates no longer admit arbitrary strings through prefix matching. Listed
legacy forms are explicit exact identities; this is not a general lemmatizer.
`ApadanaLexicalBoundaryTest` supplies the corresponding semantic premises and
rejects seven root lookalikes, plus two learning-domain lookalikes.

1.4.27 now recognizes structured नि + वृत् + णिच् as the verified निवर्तयति
prevention slice in Kāśikā, using the owning action root/prefix and causation
evidence. The intended prevented object and source meanings remain required.
Eight JSON cases assert 1.4.27→2.3.28 in active/passive causatives and reject
missing intent, wrong prefix and unknown prefix. The complete open वारणार्थ
class and automatic prevention-perspective interpretation remain incomplete.

Validation: five grammar-module suites and selected execution
CollectionSummationTest passed (47 tasks: 13 executed, 34 up-to-date).
All 95 JSON records passed.

Source: [Kāśikā 1.4.25–31](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

## 2.3.37–38: action marking and inherited disregard domain

2.3.38 formerly accepted bare DISREGARD_ANADARA without the inherited
भावलक्षण domain. It now requires ACTION_MARKING as well and exposes both
genitive and locative through OptionalVibhaktiRuleOutcome. 2.3.37 defers in
that established special domain, preserving 2.3.38 provenance for both cases.

Both rules are now reachable through the direct construction path. An explicit
ACTION_MARKING reading is not converted into the main action's adhikaraṇa
merely because it bears locative morphology. Residual 2.3.50 cannot rescue an
incompatible genitive in ordinary action marking without disregard.

Twelve frame-level JSON cases check active/passive outer voices, both licensed
cases, ओस् ambiguity, bare-disregard rejection and wrong-genitive rejection.
`ActionMarkingCasesTest` checks inherited premises and anabhihita gating.
These tests supply event-marking semantics; a typed two-event/participant link,
automatic absolute-construction recognition and wider construction competition
remain open. The implementation does not infer भावलक्षण from a lone locative.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 9 executed, 38 up-to-date).
All 107 JSON benchmark cases passed.
Source: [Kāśikā 2.3.37–38](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.33: lexical non-substance karaṇa domain

The former rule accepted any INDETERMINATE_QUANTITY marker without checking
करणे, the named lexical stems, or असत्त्ववचन. It now requires an established
KARANA result, an unchanged Mula stem स्तोक/अल्प/कृच्छ्र/कतिपय, and a
separately selected NON_SUBSTANCE_MEANING reading. Generic quantity does not
establish these conditions. 1.4.42 supplies the saṃjñā; 2.3.33 only realizes
its optional instrumental/ablative cases.

The rule exposes both permitted cases. General 2.3.18 defers within this
specific domain so both forms retain 1.4.42→2.3.33 provenance. Twenty-eight
JSON records cover all four stems in both cases/outer voices, भ्याम् ambiguity,
and substance, other-stem, wrong-role, missing-instrument and missing-reading
negatives. An instrumental substance reading retains 1.4.42→2.3.18; an
unsupported ablative remains unvalidated rather than inventing apādāna.

Automatic non-substance interpretation, qualified substance versus abstract
property attachment and derived lexical identities remain open. The inspection
also found incomplete relation/domain and optionality handling in 2.3.34–36;
their separate verified slice is recorded in the following section.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 14 executed, 33 up-to-date).
All 135 JSON benchmark cases passed.
Source: [Kāśikā 2.3.33](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.34–36: reference noun versus distance word

The former DISTANCE_OR_PROXIMITY-only predicates conflated two different
nominals. 2.3.34 now requires the reference's established link to a distance
governor, and exposes genitive/ablative alternatives. Conditional governor
discovery uses the same canonical predicate and requires the distance-relation
premise; multiple heads remain competing candidates, not a nearest-word guess.

2.3.35–36 instead analyze the distance word itself in a selected non-substance
reading. The verified unchanged-Mula slice is दूर/अन्तिक/विप्रकृष्ट/अभ्याश.
The inherited NON_SUBSTANCE_MEANING premise is separate from distance meaning.
2.3.35 supplies accusative/instrumental/ablative; 2.3.36 supplies locative.
Contextual case-outcome methods let the resolver retain each alternative's own
canonical source while ordinary 2.3.36 loci receive no extra options.

Explicit distance-construction readings are not turned into a main-verb kāraka
from surface case. Ordinary residual genitive cannot rescue an incompatible
genitive on a selected distance word. The excluded आरात् term is not silently
added to this lexical slice; its 2.3.29 domain is separate audit work.

Twenty-eight JSON records cover both constructions in active/passive outer
contexts, missing meanings, competing heads, invalid cases and ओस्/भ्याम्.
`DistanceCaseBranchesTest` checks all four stems and four case branches in both
voices (32 combinations), including exact alternative-source evidence. Ordinary
locus regression retains 1.4.45→2.3.36 without distance alternatives.
The full open synonym class, derived stem identities, automatic sense selection
and every 2.3.29 exception/competition remain open; these rows stay Partial.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 17 executed, 30 up-to-date).
All 163 JSON benchmark records passed.
Source: [Kāśikā 2.3.34–36](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.30–31: actual derived avyaya governance

Generic SPATIAL_DIRECTION and ENAPA_SUFFIX markers previously sufficed to
activate these rules. The verified दक्षिण/उत्तर slice now requires a linked
AvyayaPada carrying AvyayaTaddhitaDerivation with typed अतसुच् or एनप् identity.
The affix catalog additions provide recognition, not full 5.3.28/35 formation.
Surface -tas/-ena strings, another affix and unsupported bases do not substitute
for that proof. Case evidence records the actual governor and derivation.

`VakyaSemanticContext.avyayaGovernors` supplies occurrence-local links. The
governor must be an actual node in the clause (an equal-text copy is insufficient),
and incompatible simultaneous kṛt governance is rejected. Explicit avyaya links
are not rebound by nominal-governor discovery. The case path treats these as
direct constructions, rather than guessing a main-verb object from accusative
morphology; residual 2.3.50 cannot rescue an unsupported selected avyaya relation.
Verb-less avyaya-governed case analysis is explicitly rejected pending support.

2.3.31 retains accusative/genitive alternatives under Kāśikā's explanation by
yogavibhāga of the canonical sūtra; no extra vārttika number or rule is added.
Twenty-six JSON cases check both bases, both outer voices, both ENAP cases,
ओस् selection and marker/surface/affix/base/case negatives.
`AvyayaGovernorContextTest` checks occurrence membership and invalid links.
Automatic governor discovery, the full atas-artha class, formation licensing and
2.3.29's distinct governor-domain audit remain open.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 18 executed, 29 up-to-date).
All 189 JSON benchmark records passed.
Source: [Kāśikā 2.3.30–31](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.29: established exclusion/direction governor

The former generic DIRECTIONAL_EXCLUSION marker was sufficient without a
governing expression, and the rule was absent from the direct case path.
The verified slice now requires an unchanged nominal governor अन्य/भिन्न/
अर्थान्तर/विलक्षण/इतर/पूर्व/उत्तर or a linked indeclinable आरात्/ऋते,
together with the construction meaning. आरात् also accepts the supplied
distance reading and requires ablative, without the 2.3.34 genitive option.

Nominal dependency discovery calls the canonical predicate and retains
missing-premise and competing-head candidates. It never uses a nearest-head
guess or rebinds an explicit avyaya dependency. Candidate permitted cases
are computed from each rule's predicate instead of assuming every nominal
construction implements optional-case outcomes. The case evidence records
the selected governor and does not manufacture an apādāna saṃjñā.

Thirty-four JSON cases check all seven nominal heads, both particles, both
outer voices, missing meanings, multiple heads, wrong particles and lookalikes.
`ExclusionDirectionCaseTest` checks anabhihita gating, rejection of a derived
nominal homonym and failure of residual 2.3.50 to rescue an incompatible case
under an established link. The complete अन्यार्थ/dik synonym domains,
अञ्चूत्तरपद/आच्/आहि derivations and automatic particle attachment remain open.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 18 executed, 29 up-to-date).
All 223 JSON benchmark records passed.
Source: [Kāśikā 2.3.29](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.15: purpose action noun versus mere purpose

The previous PURPOSE_ACTION-only rule did not verify भाववचन and was absent
from the analyzer's direct-construction path. The verified घञ्/क्तिन्/ल्युट्
slice now requires a direct KridantaPratipadika, a selected ACTION_NOUN_DENOTATION
(bhāva) and the separate PURPOSE_ACTION (tumartha) interpretation. Taddhita
extensions, plain Mula representations and unsupported affixes do not establish
this structural condition. Recognized feminine derivation layers are retained.

The resulting dative is a direct 2.3.15 construction, not an invented recipient
saṃjñā of the main verb. A purpose-action claim is not rescued by a weak
case/voice object or agent hypothesis; residual genitive also cannot validate
an incompatible case once this specific construction is established.

Twenty-two JSON records check पाक/त्याग/भूति/सम्पत्ति/लेखन readings in both
outer voices and missing-purpose, missing-bhāva, agent-affix, plain-stem,
unknown-affix and wrong-case negatives. `PurposeActionNounCaseTest` checks
anabhihita, inherited semantic premises and taddhita-extension rejection.
These are selected-reading tests, not automatic purpose interpretation or
full 3.3.11 formation validation. The full bhāvavacana affix class and the
omitted-purpose-verb/argument scopes of 2.3.14 remain open.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 18 executed, 29 up-to-date).
All 245 JSON benchmark records passed.
Source: [Kāśikā 2.3.15](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.14: scope the object to its omitted purpose action

The previous bare IMPLIED_PURPOSE_OBJECT marker did not identify the omitted
action, establish its karman or distinguish a written purpose verb. A typed
PurposeActionGovernor now separates selected Implied actions from actual Written
तुमुन् occurrences. The relation to the enclosing action is an explicitly
supplied purpose interpretation, not an inference from motion, a dative or an
omitted root alone. Written governors retain their AST and must belong to the
clause by occurrence identity. Conflicting IDs/readings/governors are rejected.

Purpose dependents have their own action IDs and participant groups. They do
not enter finite valency distribution, object lookup or agreement control.
Known transitive base/णिच् structure and established DESIRED_OBJECT meaning
license the verified primary-karman hypothesis; 1.4.49 supplies the saṃjñā.
2.3.14 then assigns dative only under the selected omitted action. Ordinary
2.3.2 does not rescue its incompatible accusative. Under a written तुमुन्
action, 2.3.14 does not apply and its object retains 1.4.49→2.3.2.
No finite verb node is fabricated for the omitted or nonfinite action.

`KriyaFrame.purposeKarakas` stores the scoped relations separately. Eighteen
JSON cases check both outer voices, written/omitted modes, invalid cases,
missing object meaning, bare-marker rejection and an unsupported intransitive
object claim. `PurposeActionFrameTest` additionally checks finite participant/
agreement isolation and invalid roots, morphology, IDs and written occurrences.
The broader grammar test suites still cover ordinary accusatives independently.

This is a verified primary-object scope slice. Automatic purpose/ellipsis
recognition, validation of the selected omission against discourse, other
nonfinite kārakas, gati/secondary-object classes, nested purpose relations and
all prefix/sanādi-sensitive rules on these scopes remain open. Verb-less
purpose bindings are explicitly rejected pending support. The supplied
IMPLIED/WRITTEN distinction is not reported as independently discovered meaning.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 15 executed, 32 up-to-date).
All 263 JSON benchmark records passed.
Source: [Kāśikā 2.3.14](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## 2.3.16: linked expression, sufficiency and svasti precedence

The former predicate accepted almost every dative under ANIRDHARITA or
SAMPRADANA, with no नमः-group governor. It now requires the actual selected
indeclinable नमः/स्वस्ति/स्वाहा/स्वधा/वषट् or अलम्. For अलम्, the पर्याप्ति
reading is independently required as SUFFICIENCY. The supported unchanged-Mula
प्रभु/शक्त synonyms also require that meaning; nominal candidates retain
missing-sense and competing-head states. Nearby word presence is insufficient.

The analyzer now reaches 2.3.16 as a direct construction without manufacturing
sampradāna. A supplied sufficiency reading does not inherit a weaker verbal
recipient hypothesis from an outer passive clause. The initial bare-dative
negative fixture used भाष्, whose existing recipient profile separately supplies
such a hypothesis; those isolation negatives now use a neutral root. This change
does not certify the full automatic recipient-intent inference of 1.4.32.

Under an established स्वस्ति link, benediction does not license the general
genitive alternative. 2.3.73 explicitly defers to this special construction,
and residual 2.3.50 cannot rescue the genitive. `NamahGovernorCaseTest` checks
that precedence even with overlapping nominal benediction facts, as well as
anabhihita, the six expressions, sufficiency and derived-homonym negatives.
Thirty-two active/passive JSON cases cover the same frame integration.

The verified nominal-synonym slice is unchanged Mula प्रभु/शक्त; derived
governor identities, the full sufficiency synonym class, automatic particle
attachment and discourse sense selection remain open. No extra rule identity
or vārttika is added.
Validation: final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 9 executed, 38 up-to-date).
All 295 JSON benchmark records passed.
Source: [Kāśikā 2.3.16](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Additional 2.3 wording and optionality verification

The Kāśikā headings establish corrections to 2.3.32 (अन्यतरस्याम्),
2.3.39 (स्वामीश्वर…साक्षि…), 2.3.48 (सामन्त्रितम्), and 2.3.73
(चतुर्थी चाशिष्य…). `VibhaktiCanonicalMetadataTest` now checks the complete
73-rule heading sequence, unique registration, and rule action classification.
The fixture retains the project's documented nasal and avagraha orthographic
variants; only whitespace is ignored during comparison. This completes that
metadata check, not the semantic, anuvṛtti or precedence audit of all 73 rules.

The same pass hardened 2.3.1's API: its predicate never matches an assigning
application and direct `apply` rejects the operation. Previously that call
manufactured a prathamā assignment, although the adhikāra only governs
eligibility. `CanonicalKarakaRulesOnlyTest` checks its context gate for both
abhihita states and verifies that governing/designation/prohibition rules do
not enter the case-assignment list. Nominative case evidence remains with
2.3.46/47, not 2.3.1.

Validation for the complete metadata/API pass: focused metadata/registry tests
and final five grammar-module suites plus selected execution
CollectionSummationTest passed (47 tasks: 10 executed, 37 up-to-date).
All 95 JSON benchmark cases passed.

For 2.3.32, the commentary inherits pañcamī and explains dvitīyā by
yogavibhāga. The resolver now exposes tṛtīyā/pañcamī/dvitīyā alternatives,
including unresolved case selection when observed morphology supports several.
It requires both the exclusion premise and an explicit pṛthag/vinā/nānā link;
mere exclusion semantics or word presence does not prove that connection.
Analyzer tests check all three cases in active and passive contexts, missing
premises, an incompatible locative, and syncretic case candidates. This is a
direct vibhakti construction and does not manufacture an apādāna saṃjñā.
Automatic recognition of the link and the complete competition audit remain
open, so the inventory row stays Partial.

Validation for this slice: `:core:test :ashtadhyayi:test :analysis:test
:actions:test :derivation:test` passed (34 tasks, 7 executed, 27 up-to-date).
This verifies regression compatibility, not completion of the remaining audit.

Source: [Kāśikā 2.3](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

## Nominal governance audit: 2.3.39–40

`ParticipantFacts.governingNominal` now carries an explicitly established
nominal dependency. For 2.3.39 the rule checks the seven stated lexical stems,
not a broad ownership semantic marker. Nyāsa explicitly excludes synonymous
राजन् from the special locative permission. For 2.3.40, the linked governor
must be आयुक्त or कुशल and आसेवा (`ENGROSSED_ATTACHMENT`) must independently
be established. A bare engrossment marker no longer suffices.

Both rules expose genitive/locative alternatives through the direct vibhakti
path. Syncretic षष्ठी/सप्तमी preserves unresolved case selection while avoiding
an invented अधिकरण saṃjñā. Tests cover all seven 2.3.39 governors, active and
passive contexts, single-case and syncretic readings, unlinked word presence,
synonyms, incompatible case, and ordinary 2.3.50 genitive outside the lexical
domain. The 2.3.40 counterexample with independent locus evidence retains
1.4.45→2.3.36 when आसेवा is absent.

Remaining limitations: links are supplied explicitly, not discovered from word
order or the nominal attachment candidates. Only unchanged MulaPratipadika
governors are recognized here; derived representations (including a kṛdanta
representation of प्रसूत) and feminine derivations require a lexical identity
layer before extending recognition. Competition with independently established
verbal relations remains to be audited. These rows therefore remain Partial.

Sources: [Kāśikā 2.3.39–40](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/),
[Nyāsa 2.3.39](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.39.htm).

Validation: `NominalGovernanceInferenceTest`, `KarakaInferenceTest`,
`RemainingVibhaktiSutrasTest`, and `:analysis:test` passed after the final edits.

## Selection and distinct-group comparison: 2.3.41–42

Kāśikā distinguishes selecting a member within a containing group (2.3.41)
from the distinct-group comparison of 2.3.42. The latter inherits the
निर्धारण reference and is explicitly an exception to the genitive/locative
pair. Nyāsa explains the expressed common-versus-distinct basis; mere
inequality or an arbitrary comparison is not sufficient for the exception.

The analyzer now requires `NIRDHARANA_REFERENCE` for both constructions.
2.3.41 additionally requires `GROUP_SELECTION` and exposes both genitive and
locative outcomes. 2.3.42 requires `COMPARATIVE_DISTINCTION` and
`SEPARATE_COMPARISON_GROUPS`; this last premise denotes distinction in the
expressed comparison basis, not a claim that the groups share no conceivable
property. The special domain blocks 2.3.41 even when the observed case is
incompatible with the required ablative. It also blocks a residual 2.3.50
fallback from rescuing that incompatible genitive.

`NirdharanaInferenceTest` covers active/passive contexts, the ओस् genitive/
locative pair, alternative-case provenance, missing premises, incompatible
case, exception precedence with overlapping markers, and ordinary genitive
outside the exception domain. Canonical wording checks now include 2.3.41–42.
The semantic premises remain supplied by the caller; automatic extraction,
reference-group linking, and competition with independently established
verbal relations remain open. Neither row is fully Audited.

Sources: [Kāśikā and Nyāsa 2.3.41](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.41.htm),
[Kāśikā and Nyāsa 2.3.42](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.42.htm).

Validation: the new selection/comparison tests, existing kāraka and nominal
governance regressions, `RemainingVibhaktiSutrasTest`, and `:analysis:test`
passed after the rule changes.

## Respect and attachment constructions: 2.3.43–44

2.3.43 previously accepted a bare locative with empty semantic premises. It
now requires an explicitly linked unchanged साधु/निपुण governor and the
अर्चा sense (`ARCHA_RESPECT`). Following Kāśikā's अप्रत्यादिभिः extension,
participant-specific प्रति/परि/अनु karmapravacanīya links exclude this special
locative. With independent karmapravacanīya governance, those examples follow
2.3.8 accusative instead. Neither nearby particles nor verbal prefixes count
as such a nominal link.

2.3.44 now requires an explicitly linked प्रसित/उत्सुक governor and the
attachment/eagerness sense, rather than applying to every engrossed participant.
It exposes instrumental and locative alternatives, observes अनभिहिते, and
declares 2.3.1 governance. In particular, प्रसित in the colour sense does not
meet the attachment premise. Residual 2.3.50 cannot rescue an incompatible
genitive in either established special construction.

Tests check exact rule provenance, active/passive contexts, missing governor
and sense, all three particle exclusions, ordinary genitive outside the
respect sense, incompatible cases, optional-case ambiguity, and the former
bare-locative false positive. The canonical-wording fixture includes 2.3.43–44.
Automatic sense/link extraction, derived governor representations, the separate
साध्वसाधु locative vārttika under 2.3.36, and competition with independent
verbal kāraka evidence remain open. Both inventory entries stay Partial.

Sources: [Kāśikā and Nyāsa 2.3.43](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.43.htm),
[Kāśikā 2.3.44](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: `:core:test :ashtadhyayi:test :analysis:test :actions:test
:derivation:test` passed after these changes (34 tasks; 6 executed, 28 up-to-date).

## Nakṣatra time under lup: 2.3.45

Kāśikā requires both लुप् and an अधिकरण use; it explicitly excludes
अद्य पुष्यः despite that expression's lup derivation. The former broad
`ASTROLOGICAL_TIME` check did not enforce either condition. The rule now
requires an independently established अधिकरण role, locus semantics, a
nakṣatra expression, time semantics, and typed `TaddhitaLopaEvidence` with
`LopaType.LUP`. Written absence of an affix, ordinary lopa, luk, and ślu are
not interchangeable with lup. The evidence records its licensing sūtra;
4.2.3–4.2.4 supply the documented nakṣatra-time derivation.

2.3.45 exposes instrumental/locative alternatives. The general 2.3.36 defers
within this specific domain so that both observed cases and alternative cases
retain exact 1.4.45→2.3.45 provenance. Outside it, independently established
loci retain 1.4.45→2.3.36. Tests cover both voices, each permitted case,
ambiguous observed cases, missing premises, all other lopa kinds, the nonlocus
nominative counterexample, incompatible case, and अनभिहिते gating.

The construction still requires supplied derivational evidence and a supplied
locus hypothesis. Automatic 4.2.3–4.2.4 derivation and discovery of temporal
attachment are not implemented by this change. Tests supplying the
अधिकरण candidate do not prove that an instrumental surface form alone can
discover its temporal reading. The row remains Partial.

Sources: [Kāśikā 2.3.45](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/),
[4.2.3](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/4/4.2.3.htm),
[4.2.4](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/4/4.2.4.htm).

Validation: nakṣatra-lup, nominal governance, kāraka inference, additional
vibhakti and canonical wording regressions plus `:analysis:test` passed.

## Nominative and vocative designations: 2.3.46–49

The unspecified-nominative branch of 2.3.46 now requires an independently
established `BARE_NOMINAL_MEANING` premise; nominative morphology alone does
not establish that interpretation. Abhihita kāraka nominatives remain supported.
The separate `DIRECT_ADDRESS` interpretation selects 2.3.47 and is not
converted into a verbal agent or into bare naming under 2.3.46.

2.3.48–49 now return `NominalDesignationResult`, not a case-assignment result.
After an address nominative is established, the resolution records आमन्त्रित
and, for independently known singular number, सम्बुद्धि. Dual, plural and
unknown number receive no सम्बुद्धि designation. Case evidence (2.3.47) and
designation evidence (2.3.48–49) remain separate in both `KarakaResolution`
and the sentence frame's `KarakaRelation`. The existing `Karaka.SAMBODHANA`
enum value is retained for API compatibility; no 1.4 kāraka saṃjñā is invented.

Frame construction now propagates number when all sup candidates agree;
previously it dropped this information before relation analysis. Tests include
a frame-level singular address, all three numbers and unknown number, active/
passive contexts, absent address/bare-meaning premises, incompatible case,
and explicit address versus agent/naming premises. Existing active/passive
abhihita nominative regressions remain part of validation.

Automatic discourse recognition of address, referent/predicate distinctions,
vocative surface derivation, and the complete scope audit of 2.3.46 remain
unfinished. These rows remain Partial. Explicit context premises in these
tests are not a claim that proper nouns are inherently vocative.

Sources: [Kāśikā and Nyāsa 2.3.46](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.46.htm),
[2.3.47](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.47.htm),
[Kāśikā 2.3.48–49](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: the full `:core:test :ashtadhyayi:test :analysis:test :actions:test
:derivation:test` suite passed (34 tasks; 7 executed, 27 up-to-date), including
the new frame and designation regressions.

## Relational use under 2.3.50

Kāśikā identifies शेष as a relation beyond bare nominal meaning, including
possession, part/whole and parent/child relations. The resolver previously
treated any otherwise-unresolved genitive as sufficient proof of this use.
2.3.50 now requires an independently established `SESHA_RELATION` premise.
Its Hindi explanation also now correctly distinguishes kāraka and nominal
stem meaning from the relational use.

The direct relation path can select genitive from an ओस् genitive/locative
analysis when that premise is supplied, yielding सम्बन्ध with only 2.3.50
evidence rather than inventing a 1.4 kāraka designation. Without the premise,
a plain genitive remains an unresolved सम्बन्ध hypothesis and ओस् retains
both morphological hypotheses. Existing fixtures describing actual possessors
now state their intended relational premise rather than relying on case alone.

`SeshaInferenceTest` covers plain genitive and ओस् without premises, explicit
relation in both voices, incompatible locative, direct-address exclusion, and
the 2.3.42 exception even when a residual relation premise is supplied.
Automatic binary relation/link discovery, extraction of relation subtype, and
competition between independently supported relational and verbal readings
remain open. The 2.3.50 row remains Partial.

Source: [Kāśikā and Nyāsa 2.3.50](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.50.htm).

Validation: the five-module grammar suite plus
`:execution:test --tests dev.panini.execution.CollectionSummationTest` passed
(46 tasks; 15 executed, 31 up-to-date). All six collection-binding tests passed.

## Root-specific śeṣa genitives: 2.3.51–52

Vibhakti contexts now carry the governing dhātu and verb AST. 2.3.51 checks
ज्ञा, its non-knowledge sense, an underlying instrument, and independently
established शेष interpretation. The old memory/ruling-object marker was an
incorrect substitute for अविदर्थ and is no longer supplied by the generic
ज्ञा profile. Kāśikā explains प्रवृत्ति or मिथ्याज्ञान, contrasting ordinary
knowledge with an instrumental such as स्वर.

2.3.52 now checks a supported remembering construction (स्मृ or अधि + इ),
दय् in its specified sense, or ईश् in ऐश्वर्य, plus an underlying object and
शेष interpretation. It observes अनभिहिते and declares 2.3.1 governance.
Root/sense evidence is participant-specific, not a sentence-wide keyword.
The structured verb takes precedence over a raw alias, and the अधि + इ
construction requires its recognized prefix. The current remembering lexicon
does not exhaust the semantically open अधीगर्थ class.

When शेष is the supplied interpretation, underlying instrument/object facts
do not force a realized verbal kāraka. These constructions resolve सम्बन्ध
with 2.3.51 or 2.3.52 evidence; general 2.3.50 defers to their specific domains.
Without शेष, the regression examples retain ordinary 1.4.42→2.3.18 and
1.4.49→2.3.2 behavior. Tests cover active/passive contexts, each premise,
wrong roots, underlying instrument versus object, structured prefix checks,
and removal of the old conflated marker.

Remaining work includes automatic sense recognition, the complete lexical
class, causative/prefixed sense coverage, compound restrictions associated
with प्रतिपदविहिता षष्ठी, and competing interpretations. Both rows remain
Partial; supplied शेष interpretation selects a reading rather than proving
that no other reading exists.

Sources: [Kāśikā and Nyāsa 2.3.51](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.51.htm),
[2.3.52](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.52.htm).

Validation: the full five-module grammar suite passed after the final root and
prefix changes (34 tasks; 22 executed, 12 up-to-date).

## Quality addition and bhāva-agent affliction: 2.3.53–54

2.3.53 now requires कृ, an underlying object, an existing substrate receiving
another quality (`EXISTING_SUBSTRATE` with `TRANSFORMATION_ENDOWMENT`), and
शेष interpretation. The existing-substrate premise distinguishes प्रतियत्न
from creating an object. An instrument does not acquire this special genitive
merely because it participates in an improvement action.

2.3.54 now requires the underlying affected object construed as शेष, the
affliction sense, and a distinct participant independently identified as the
agent and as an event/state nominal (`BHAVA_EVENT`). Vibhakti contexts carry
the clause participants so the affected object's own properties or an unrelated
abstract noun cannot substitute for that agent. The verified lexical slice
currently covers रुज् and अम्; the open रुजार्थ class is not exhausted.
ज्वर् and सन्तापि are excluded from the specific rule, including structured
सम् + तप् + णिच्. The exclusion does not prohibit generic शेष genitive.

General 2.3.50 defers to established 2.3.53–54 constructions. Tests cover both
voices, each required premise, wrong roots, non-object instruments, missing/
material/non-agent/self-identical proposed agents, the exclusions, and ordinary
accusative interpretation without शेष. Canonical wording fixtures include
both sūtras. Automatic substrate/agent/sense recognition, the remaining lexical
class, derivational restrictions on the agent nominal, compound prohibitions,
and competing readings remain open; both inventory rows stay Partial.

Sources: [Kāśikā and Nyāsa 2.3.53](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.53.htm),
[2.3.54](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.54.htm).

Validation: the full five-module grammar suite passed after the final changes
(34 tasks; 10 executed, 24 up-to-date), including six special-genitive regressions.

## Wish sense of nāth: 2.3.55

Kāśikā distinguishes आशिष्, wishing that something be available to oneself,
from the root's requesting and other senses. 2.3.55 now checks the governing
नाथ्/नाध् root, the independently established wish sense, an underlying
object, and inherited शेष interpretation. It no longer applies to a generic
blessing marker on an arbitrary root. Its Hindi explanation and evidence
describe this आशासन sense rather than treating it solely as blessing another.

The specific domain defers general 2.3.50 and records सम्बन्ध with 2.3.55
provenance. Missing wish evidence cannot establish this specific rule;
ordinary object interpretation without शेष retains 1.4.49→2.3.2. Tests cover
both root variants named in the commentary, active/passive contexts, each
premise, wrong root, general benediction versus wish, underlying non-object,
missing governing root, अनभिहिते, and canonical wording.

Automatic sense recognition, verbal-pada formation, the full prefixed and
causative matrix, and compound/accent consequences remain open. The row
remains Partial; the tests establish behavior with supplied interpretations.

Source: [Kāśikā and commentaries 2.3.55](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.55.htm).

Validation: the special-genitive and inference regressions, `:analysis:test`,
and the complete 2.3 unit-test package passed after this change.

## Specified injury constructions: 2.3.56

The canonical text is जासिनिप्रहणनाटक्राथपिषां हिंसायाम्; the extra क in
नाटकक्राथ was corrected in the declaration and inventory. The rule now
requires its specified verb construction, underlying object, violence sense,
and inherited शेष interpretation. Generic violence on an arbitrary root no
longer establishes this particular genitive.

Vibhakti contexts now carry the base lexical dhātu. For JAS, the rule requires
matching curādi identity and rejects both unknown lexical class and the
divādi मोक्षण homonym. Regression data uses the actual entries 10.0184,
10.0243 and 04.0108. For HAN, structured नि, प्र, नि+प्र and प्र+नि are
supported; bare HAN, वि+HAN, unknown prefixes and duplicated नि do not
establish the specified construction. A structured AST overrides a misleading
combined raw alias. नट्, क्रथ् and पिष् still require independently supplied
violence meaning, so ordinary grain crushing is not classified as this use.

The direct relation result records 2.3.56 rather than generic 2.3.50; ordinary
object interpretation without शेष retains 1.4.49→2.3.2 in the tested cases.
Tests cover root families, each premise, the prefix matrix, lexical homonyms,
active/passive contexts, wrong underlying role, अनभिहिते and canonical wording.
Automatic sense discovery, the complete prefixed/causative/passive formation
matrix, नाट/क्राथ derivation, and compound consequences remain open. The row
stays Partial.

Source: [Kāśikā 2.3.56](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: injury/special-genitive and inference regressions, `:analysis:test`,
and the complete 2.3 unit-test package passed; final gating assertions were rerun.

## Shared transaction sense: 2.3.57 and adjacent DIV audit

2.3.57 now requires पण् or the vi-ava-hṛ construction, an underlying object,
the common द्यूत/क्रयविक्रय sense, and inherited शेष interpretation.
Structured वि + अव + हृ must have the stated prefix order; a raw व्यवहृ
alias cannot override another structured construction. General 2.3.50 defers
within this specific domain so its genitive records 2.3.57 provenance.

The adjacent 2.3.59 implementation incorrectly described and matched prefixed
व्यवहृ/पण्. Its scope is now restricted to a recognized prefixed DIV AST,
the inherited transaction sense, underlying object, and शेष interpretation.
This removes an invalid accusative fallback in the 2.3.57 domain. The joint
case-branch and Brāhmaṇa-exception implementation is recorded in the following
2.3.58–60 section, including replacement of the old gambling-instrument premise.

`TransactionGenitiveTest` checks both 2.3.57 root families, active/passive
contexts, each premise, structured prefix ordering, wrong root, ordinary
accusative without शेष, counting/praise without transaction sense, and the
absence of a 2.3.59 accusative option for these verbs. Automatic sense
recognition, prefixed/causative lexical coverage, compounds, and full DIV
instrument/object competition remain open. No row is promoted to Audited.

Source: [Kāśikā 2.3.57–60](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: transaction, injury, special-genitive and inference regressions,
`:analysis:test`, and the 2.3 unit-test package passed. The final 2.3.59
gating checks were rerun after the last code changes.

## DIV transaction cases and Brāhmaṇa exception: 2.3.58–60

2.3.58 now checks DIV, the inherited transaction/gambling sense, underlying
object and supplied relational interpretation. `GAMBLING_INSTRUMENT` alone
does not establish this genitive. Unprefixed non-Brāhmaṇa use records specific
2.3.58 genitive provenance; it does not inherit a free accusative option.
The general 2.3.50 defers throughout the established DIV transaction domain.

For recognized prefixed DIV, 2.3.59 exposes genitive and accusative branches.
`CaseDependentKarakaOutcome` records that these can select different
interpretations: सम्बन्ध for genitive and कर्मन् for accusative. The analyzer
requires a matching 1.4 saṃjñā proof for an object branch instead of creating
one from case alone. In the independent-object regression fixtures this is
1.4.49, followed by 2.3.59. A genitive alternative does not retain that object
proof. Compatible observed branches with different roles leave both role and
case unresolved and retain their distinct evidence.

Branch selection observes अनभिहिते for the branch's actual role. A passive
object already expressed by the verb therefore receives no accusative branch;
the supplied relational reading can retain its genitive. These tests do not
claim to derive passive surface sentences or to force relational interpretation.

Explicit Brāhmaṇa usage with the inherited DIV conditions selects accusative
under 2.3.60. Both unprefixed and prefixed fixtures exclude genitive fallback
through 2.3.50, 2.3.58 or 2.3.59. Counting/non-transaction senses and a bare
Brāhmaṇa marker do not establish this exception.

`DivTransactionCasesTest` covers exact evidence, both prefixed case branches,
alternative provenance, compatible-input ambiguity, passive object-expression
restrictions, missing conditions, wrong roots, unknown prefixes, and the
Brāhmaṇa exception. Automatic register/sense recognition, the full 1.4.43
instrument/object competition, causative/pada formation, and compound/accent
consequences remain open. All three rows stay Partial.

Sources: [Kāśikā 2.3.58–60](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/),
[Kāśikā and Nyāsa 2.3.60](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.60.htm).

Validation: the full five-module grammar suite and the six collection-binding
integration tests passed after these changes (46 tasks; 13 executed,
33 up-to-date).

## Ritual imperatives and prasthita prohibition: 2.3.61

2.3.61 now checks a structured second-person singular imperative: प्र + इष्
with matching divādi lexical identity, or ब्रू. Present tense, other person/
number endings, causative derivation and passive यक् do not establish those
specified forms. Unknown or mismatched lexical identity cannot establish
प्रेष्य. The rule additionally requires the oblation/object premises, the
supplied relational interpretation, and a distinct participant independently
identified both as deity and recipient. An unrelated deity noun is insufficient.

The accompanying prohibition for प्रस्थित हविस् is represented by 2.3.61V
in `NishedhaRuleEngine`, targeted specifically at the 2.3.61 genitive. Rejected
direct-rule candidates are now retained in resolution diagnostics; previously
that early direct-assignment path could discard prohibition provenance.
General 2.3.50 defers inside the established ritual construction and cannot
rescue the forbidden genitive. The prohibited oblation can follow its
underlying object analysis and ordinary 1.4.49→2.3.2 accusative, rather than
being forced into a relational reading by the supplied marker.

`RitualOblationGenitiveTest` covers both specified verbs, real divādi इष् and
its kryādi homonym, missing lexical identity, mismatched lexeme, prefixes,
all object premises, recipient identity/role, tense/person/number/pada and
derivational negatives, prohibition evidence, and the ordinary accusative
counterexample. Automatic oblation/deity/recipient and प्रस्थित recognition,
pluta/accent realization, full śeṣa-scope review and ritual lexical coverage
remain open. The 2.3.61 inventory row remains Partial.

Source: [Kāśikā 2.3.61](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: the full five-module grammar suite and six collection-binding
integration tests passed (46 tasks; 15 executed, 31 up-to-date), including
the three ritual-oblation regression groups.

## Vedic bahulam usages: 2.3.62–63

Both rules now require explicit Vedic/Brāhmaṇa register and a participant-local
`VedicCaseLicense` identifying the rule and a nonblank source reference.
A Vedic-labeled semantic marker alone no longer establishes the exception.
This is supplied construction evidence, not automatic attestation lookup or a
licence covering every word in a Vedic passage. `VakyaAnalyzer` can receive
the register explicitly; occurrence-specific licences currently enter through
the participant/context API, not automatic surface recognition.

2.3.62 checks the licensed dative-meaning genitive without manufacturing a
specific kāraka. Where an independent recipient analysis is already available,
its genitive alternative retains the same 1.4.32 proof. 2.3.63 additionally
checks यज् and independent instrument semantics. Its licensed genitive keeps
करण, with exact 1.4.42→2.3.63 evidence, instead of becoming an unspecified
relation or सम्बन्ध merely because its case is genitive.

Bahulam is not treated as a freely productive case switch. Ordinary dative or
instrumental input does not acquire a generated genitive alternative. If the
input already supplies competing case analyses and the exceptional form has
the local licence, the resolver preserves that observed alternative and its
separate evidence. Compatible cases remain unresolved. Classical/unspecified
register, absent/wrong-rule licence, wrong root or missing instrument premises
do not establish 2.3.63. Reusing an existing semantic proof no longer duplicates
its 1.4 evidence in a case-dependent branch.

Tests cover register and licence gates, source provenance, both voices for
the licensed instrument genitive, wrong root and premises, unambiguous ordinary
instrumental, competing observed instrumental/genitive and recipient cases,
role restrictions, अनभिहिते, and canonical wording. Full attestation coverage,
the reciprocal genitive-meaning dative extension, other बहुलम् uses,
occurrence-licence extraction, and all ordinary passive role competitors remain
open. Both inventory entries remain Partial.

Sources: [Kāśikā and Nyāsa 2.3.62](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.62.htm),
[Kāśikā 2.3.63–64](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

Validation: the full five-module grammar suite and collection-binding
integration tests passed (46 tasks; 16 executed, 30 up-to-date), including
four Vedic licence regression groups and the role/anabhihita unit checks.

## Expressed counting frequency and time: 2.3.64

The Vedic/bahulam restrictions stop before 2.3.64. It is now tested in every
register without a Vedic licence. The rule requires an expressed linked
counting-frequency pada, independently temporal reference and underlying
locus facts, and supplied शेष interpretation. A bare frequency-time marker,
implied repetition, an unrelated frequency word, a spatial vessel or a time
instrument does not establish this special genitive. General 2.3.50 defers
within the verified construction so the relational result retains 2.3.64
provenance. Ordinary locus construal without शेष keeps 1.4.45→2.3.36.

`CountingFrequencyExpressions` recognizes a checked lexical slice and typed
counting derivations over primitive numeral stems. Declared derivation takes
precedence over surface spelling; nonnumeral affix use and unrelated DHA/SHAS
constructors are not guessed into this class. The adverbial analyzer retains
the recognized action count in `AdverbialSemantics.repetitionCount`, while
qualitative recurrence such as पुनः remains without an invented count.

Tests cover all registers and both voices for the supplied relation, each
premise, explicit versus absent/unlinked frequency, typed forms, wrong
derivational kind, ordinary locative, role and अनभिहिते gates, and canonical
wording. Automatic frequency-to-time attachment, compound/quantifier numeral
derivations, full lexical and sandhi recognition, and compound restrictions
remain unfinished. The linked governor is supplied through the participant
API, not chosen by proximity. The inventory row stays Partial.

Sources: [Kāśikā and commentaries 2.3.64](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.64.htm),
[5.4.19](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/5/5.4.19.htm).

Validation: the full five-module grammar suite and collection-binding
integration tests passed (46 tasks; 16 executed, 30 up-to-date), including
frequency/time, adverbial count and role/anabhihita regressions.

## Linked kṛt governance foundation: 2.3.65–66 (partial)

Rechecked Kāśikā/Nyāsa on
[2.3.65](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.65.htm)
and [2.3.66](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.66.htm).
The former applies to unexpressed agent/object roles under a kṛt governor,
excludes an instrument and a taddhita governor, and ends the inherited śeṣa
condition. The latter restricts agent genitive when both roles are eligible
under one kṛt; an implied object can satisfy the construction. Two unrelated
kṛt governors do not jointly establish this condition.

`KrtGovernanceEvidence` now enriches morphological candidates with a uniquely
supplied independent-agent or desired-object premise under an explicitly linked,
recognized kṛt governor. It does not invent either premise from genitive case.
Taddhita and unknown derivational wrappers do not establish direct kṛt governance.
Recognized feminine wrappers remain structurally eligible. A supplied śeṣa
marker no longer suppresses these independently established kṛt roles.

`KarakaRuleContext.governingKrtExpressedRoles` supplies the selected denotation:
an empty set means an action-only reading; null means unknown. Case assignment
for a kṛt-dependent kāraka remains unresolved for an unknown reading. A known
reading determines abhihita independently of the enclosing finite verb's voice;
the affix alone does not select the reading. Rule 2.3.66 now requires both a
proven object role and the kṛt-governance premise, rather than accepting a bare
dual-eligibility marker as an unassigned direct case rule.

`LinkedKrtGovernanceTest` checks exact 1.4.54/49 → 2.3.65 chains in active,
passive and impersonal outer contexts, śeṣa independence, unknown/expressed
readings, taddhita/unrecognized/unlinked governors, instrument exclusion, and
1.4.49 → 2.3.66 versus an agent's instrumental fallback. Existing inference
fixtures now explicitly state their kṛt readings. Metadata tests include both
canonical headings.

The next audit increment adds `KrtGenitiveEligibility`, bound by an explicit
`governingKrtOccurrenceId`. For AST-linked governors, the enrichment layer now
derives dual-eligibility markers from this bound semantic premise rather than
trusting supplied markers. Eligibility cannot include an expressed role and
must cover the dependent's independently established unexpressed role. A foreign
occurrence, missing identity, contradictory reading, or bare dual marker leaves
case assignment unresolved. Identical AST values with different occurrence
identities remain distinct. Implied arguments may appear in the eligible-role
set, so this check does not require two written dependents. Separate single-role
premises attached to separate governors continue through 2.3.65.

Still open: automatic derivation of the eligibility premise and governor links;
automatic governor discovery; governing kṛt root/prefix domains separate
from the enclosing finite verb; and joint agent/object semantic ambiguity.
Legacy externally supplied kṛt markers remain logical premises rather than
automatically verified AST facts. These rules remain **Partial**.

Validation: `:core:test :ashtadhyayi:test :analysis:test :actions:test
:derivation:test :execution:test --tests dev.panini.execution.CollectionSummationTest`
passed after these changes (46 tasks). The execution portion covers that named
integration suite, not the complete execution module.
The same command also passed after the occurrence-bound eligibility increment
(46 tasks; 19 executed, 27 up-to-date).

### Feminine commentary exceptions to 2.3.66: excluded

The aka/akāra exemption and other feminine optionality were tested as
commentary vārttikas, then removed following the user's canonical-only
instruction. They are not current executable rules. General canonical
prohibitions 2.3.69–70 still check genitive assignments through canonical
2.3.66, preventing that path from bypassing the kṛt-genitive restrictions.

## Canonical kta special genitives: 2.3.67–68 (partial)

Checked the canonical headings and interpretations in
[Kāśikā 2.3.67–68](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/)
and [Nyāsa on 2.3.68](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.68.htm).
No additional vārttika rules are introduced.

2.3.67 now requires an explicitly linked, directly kta-derived governor, a
known expressed-role reading, an unexpressed proven agent and the supplied
present-kta semantic premise. Its canonical text is normalized to
क्तस्य च वर्तमाने, and the 2.3.1 governance is declared. A present-tense outer
finite verb alone does not establish the premise.

2.3.68 requires a linked kta actually selected to denote adhikaraṇa. The
enrichment layer derives the construction marker from that expressed-role
reading; a bare marker or a generic action-only kta does not suffice. Either
unexpressed agent or object may receive the genitive. The 2.3.66 restriction
does not override this special assignment, including under explicitly bound
dual eligibility. The ordinary agent instrumental rule defers to the special
genitive in both domains. The general 2.3.69 prohibition on 2.3.65/66 does not
block the special 2.3.67/68 assignments.

`KtaSpecialGenitiveTest` asserts exact 1.4.54/49 → 2.3.67/68 chains in active
and passive outer contexts, the location-reading versus action-reading contrast,
dual eligibility, mandatory case precedence, and rejection of unknown readings,
unlinked governors, ktavatu, taddhita and unknown feminine wrappers. Unit tests
check role, abhihita, case and structural-premise exclusions. Metadata tests
include both canonical headings.

Remaining obligations: automatic governor attachment, automatic recognition and
validation of the selected present-time kta derivation, and validation of the
adhikaraṇa formation under 3.4.76 across its full root/sense domain. Supplied
reading premises are not proof of those automatic analyses. Both rules remain
Partial. Commentary-only neuter-bhāva extensions remain outside canonical scope.

Validation: all five grammar-module suites plus the collection execution
integration passed after this increment (46 tasks; 17 executed, 29 up-to-date).

## Canonical kṛt exclusions: 2.3.69–70 (partial)

Checked [2.3.69](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.69.htm)
and [2.3.70](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.70.htm).
Only canonical rules are executable; commentary-only exceptions remain excluded.

The supported structural exclusion set now includes śatṛ, śānac, kānac, kvasu,
ki/kin, u, ukañ, khal/yuc, śānan/cānaś and tṛn, in addition to the previously
supported niṣṭhā and indeclinable kṛt forms. Shared typed affix identities are
used; tṛc is distinct from tṛn and retains ordinary 2.3.65 behavior. This is a
recognition catalog, not implementation of every affix's formation rules.

`KridantaPratipadika.derivationEvidence` now carries an established canonical
governing sūtra and a selected semantic domain. A future ṇvul licensed by
3.3.10 establishes the aka exclusion; ini/ṇini under future domain 3.3.3
establish the in-future exclusion; ṇini under 3.3.170 in debt meaning
establishes the debt exclusion. A future interpretation of ordinary 3.1.133
ṇvul and necessity rather than debt under 3.3.170 do not establish 2.3.70.
The evidence is attached to the governor AST, not inferred from a finite tense
or from suffix-like surface text. A supplied unbound prohibition conflicting
with the recognized governor leaves case assignment unresolved.

`KrtExclusionDerivationTest` checks the supported exclusion families and
retained 1.4.49/54 proofs plus rejected 2.3.65 → 2.3.69 provenance under both
outer active and passive voices. It covers the three 2.3.70 conditions,
source/sense/affix contrasts, ordinary tṛc/aka, unbound claims and taddhita
wrappers. Existing 2.3.67–68 exception tests remain applicable. Canonical
metadata tests include 2.3.69–70.

Remaining: automatic generation/validation of the attached derivation evidence,
the complete l-ādeśa/avyaya/tṛn-range catalog, other licensed future in
derivations, and automatic governor attachment. Legacy supplied prohibition
premises without an explicit governor are not automatically verified facts.
The inventory remains Partial rather than claiming complete derivational support.

Validation: the five grammar-module suites and the collection execution
integration passed (46 tasks; 16 executed, 30 up-to-date).

## Canonical comparison and benediction cases: 2.3.72–73 (partial)

Checked [2.3.72 and Nyāsa](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.72.htm)
and [Kāśikā on 2.3.73](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).
Both constructions require the inherited śeṣa relation. 2.3.72 optionally
licenses instrumental alongside genitive; 2.3.73 carries forward the option,
not the instrumental case, and licenses dative alongside genitive in blessing.

The implementation now requires an explicitly linked nominal governor, a
supported lexical identity, the construction meaning and śeṣa. 2.3.72 currently
recognizes tulya/sadṛśa/samāna/sama, excluding tulā/upamā and particle lookalikes.
2.3.73 recognizes the seven named governors āyuṣya/madra/bhadra/kuśala/sukha/
artha/hita, requiring benediction meaning rather than well-being vocabulary
alone. Recognized feminine wrappers preserve the base; taddhita/unknown wrappers
do not inherit it. These are lexical slices, not a claim to recognize every
intrinsic equality synonym or the full semantic paraphrase class.

Both rules expose explicit optional-case outcomes and direct non-kāraka
assignment. The resulting relation is sambandha; instrumental does not invent
karaṇa and dative does not invent sampradāna. Generic 2.3.50 defers inside a
validated special construction so the genitive branch retains its option.
Both observed cases remain ambiguous. Missing special conditions may still
allow independently established ordinary śeṣa-genitive under 2.3.50.

`ComparisonBenedictionCasesTest` checks each supported governor under outer
active/passive contexts, exact direct 2.3.72/73 chains, optional alternatives,
observed ambiguity, excluded words, absent links/meanings/śeṣa, wrappers,
ordinary non-benediction genitive and independently established instrumental
1.4.42 → 2.3.18. Unit tests cover abhihita/role/case exclusions and metadata
records the inherited rule references. No noncanonical rule is introduced.

Still open: full synonym/derivational recognition, automatic nominal dependency
discovery and scope/sense disambiguation. Both inventory entries remain Partial.

Validation: all five grammar-module suites and the collection execution
integration passed after correcting the new unit fixture (46 tasks; final run
3 executed, 43 up-to-date from the preceding run on the same production changes).

## Sentence-level governor discovery and JSON benchmark

`NominalCaseLinkAnalyzer` now enumerates supported 2.3.72/73 nominal governors
directly from each clause's analyzed nominals, without positional preference.
It reuses the canonical rule predicates rather than a second governor catalog.
Each candidate retains dependent/governor occurrence indices, compatible cases,
required semantic premises and missing premises. A unique candidate is bound
into `VakyaAnalyzer` participant facts only when its semantic premises are
already established. Multiple identical governors remain separate occurrences;
a single morphologically compatible word with missing semantics is not bound.
Both finite and nominal frames expose the analysis; finite frames use established
links for actual case resolution and preserve governor-link evidence in the
canonical assignment trace. Nominal-clause case execution remains unchanged.

`NominalCaseFrameTest` covers reordered/intervening words, actual sentence-frame
2.3.72/73 resolution, optional cases, identical competing governors and missing
premises in finite/nominal clauses. This closes the explicit governor-binding
gap for this supported slice when semantic facts are available; recognizing
those semantic facts independently remains open.

`analysis/src/test/resources/karaka_benchmark.json` contains 21 executable
correctness records, with exact chains, relation/case/alternative expectations,
link counts, missing premises, and source versus generated provenance. The JSON
runner executes each record and writes a grouped report separating supplied-
premise cases from unannotated cases. The benchmark includes core active/passive
and causative checks as well as this nominal-linking slice. It is neither a
raw-text parsing benchmark nor a complete kāraka-coverage certification.

Validation: all 21 JSON records passed (13 with supplied premises, 8 without).
The five grammar-module suites and collection execution integration also passed
(47 tasks; 12 executed, 35 up-to-date). Canonical-only rule registration remains
enforced by its regression test.

## Occurrence-local clause evidence

`VakyaSemanticContext` now provides an explicit sentence-level channel for
established participant relations. Indices address the same flattened nominal
occurrences as the frame's nominal analyses. Finite and nominal clauses consume
the facts without mutating the lexicon; finite rule traces retain
`CLAUSE_CONTEXT` provenance. The dvikarmaka ambiguity check also reads these
facts when checking whether an argument was annotated. Invalid negative or
out-of-clause indices are rejected; the original analyze overload remains.

The JSON runner now supplies `premises` through this context rather than storing
them in lexical entries. It supports distinct premise sets on repeated identical
stems. Two additional records test the annotated occurrence's exact 2.3.72 chain
and the unannotated occurrence's unresolved/missing-premise result. The dataset
now has 23 records. Unit tests additionally verify that a subsequent analysis
without context does not reuse earlier facts and that nominal clauses obey the
same occurrence boundaries. Independent discovery of these semantic facts is
still open; this change provides correct scope and provenance for available facts.

Validation: 23/23 JSON records passed (15 clauses with supplied premises and 8
without). All five grammar-module suites and collection execution integration
passed (47 tasks; 20 executed, 27 up-to-date).

## Recipient evidence under canonical 1.4.32

Checked [1.4.32 and its interpretation](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.32.htm).
The rule no longer treats a dative/category-only recipient hypothesis as
established intended-recipient meaning, even in a one-participant clause or
alongside an object. Recipient evidence must come from lexical semantics,
clause-local facts or participant-specific verbal valency. Low-level callers'
unattributed recipient premises remain explicit supplied facts, not automatic
discoveries. A written object co-argument now requires desired/secondary-object
semantics; accusative morphology alone does not establish it.

`SampradanaEvidenceTest` checks morphology/category negatives, independently
supplied recipient facts, implied-object compatibility in the existing
single-participant API and rejection of a bare accusative co-argument. Four JSON
records add sole-dative, dative-plus-object, unannotated benediction and an
established giving recipient. The dataset now has 27 records.

Remaining: binding an intended recipient to its particular object/event,
recognizing implied objects alongside other participants, full lexical-sense
disambiguation and competing non-kāraka constructions. The 1.4.32 matrix row is
therefore Partial rather than treating a passing basic gift example as an audit.

Validation: 27/27 JSON records passed (16 clauses with supplied semantic premises,
11 without). The five grammar-module suites and collection execution integration
passed (47 tasks; 6 executed, 41 up-to-date).

## Particular object mediation under 1.4.32

`IntendedObject` now distinguishes a written participant identity from an
explicitly implied object. `VakyaSemanticContext.intendedObjects` binds recipient
and object nominal occurrences in finite clauses, adding explicit recipient
intent with clause-local provenance. Negative, self and out-of-clause indices
are rejected. The canonical rule checks the selected written endpoint, requires
object rather than śeṣa semantics, and rejects missing/duplicate identities.
It does not borrow some other object's eligibility after a failed explicit link.
An implied object may now be established alongside an expressed agent or other
participants, without relying on the old one-participant shortcut.

With a valid explicit link, semantic sampradāna can be established independently
of case morphology; an incompatible observed genitive retains 1.4.32 evidence
but no validated 2.3 case. No false dative is assigned. Tests cover written,
implied, foreign/missing/self/duplicate and non-object endpoints, unrelated
co-arguments, scope validation and clause-local link evidence. The JSON dataset
now has 32 records, including five mediation cases and a passive nominative
object linked to its dative recipient.

Remaining: automatic discovery of recipient/object intent and distinguishing
embedded/nested event bindings. The later explicit-object audit removes the
sole-participant compatibility behavior: omitted mediators require an explicit
link; unbound discovery requires a resolved written object. No new sūtra identity
is added.

Validation: five grammar-module suites and collection execution integration
passed (47 tasks; 14 executed, 33 up-to-date). After adding the final passive
data record, the JSON runner passed 32/32 records (21 clauses with supplied
facts and 11 without), with no further production-code changes.

## Known role with unvalidated case: sentence diagnostics

`VakyaAnalyzer` now emits `UNVALIDATED_VIBHAKTI` when a role is resolved but its
case assignment is not validated and no observed optional case alternative
explains the missing singleton. This is a warning about incomplete case proof,
not an assertion that the sentence is ungrammatical. Existing optional-case
ambiguity and unresolved-role diagnostics remain distinct.

Participant diagnostics now carry the flattened `nominalIndex`. Identical source
text no longer forces consumers or benchmarks to guess which occurrence is
affected. Clause-wide diagnostics leave the index absent. A frame regression
checks a sampradāna/genitive mismatch beside an identical, valid śeṣa-genitive,
and distinguishes it from an unresolved role. The JSON runner can assert exact
participant diagnostic sets by occurrence; two additional records bring the
dataset to 34 cases. No canonical rule or kāraka/case assignment is changed by
this diagnostic increment.

Validation: 34/34 JSON records passed (23 clauses with supplied facts and 11
without). The five grammar-module suites and collection execution integration
passed (47 tasks; 10 executed, 37 up-to-date).

## Agent/instrument distinction under 1.4.42/54

Rechecked [Kāśikā on 1.4.42 and following rules](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).
The plural candidate mapper now retains both kartṛ and karaṇa for instrumental
forms in passive, bhāve and causative contexts. Its old singleton `infer` API
remains a compatibility default; analysis and kṛt enrichment use the plural API.
A passive instrument no longer disappears before its semantic rule is considered.

1.4.42 requires established selected-means semantics, rather than a tool-category
or instrumental-case hypothesis. 1.4.54 no longer treats passive/bhāve/causative
instrumental morphology as proof of an independent agent. Ordinary finite active
nominative voice evidence remains available. Verbal valency alone does not
establish the selected-means construal. Explicit clause readings outrank
lexical defaults, which outrank distributed verbal valency; these are input-
evidence specificity distinctions, not additional grammatical sūtras. Rules
1.4.43–44 use the same selected-means check for their inherited condition.

Equally supported agent/instrument readings retain both 1.4.42 → 2.3.18 and
1.4.54 → 2.3.18 analyses rather than resolving by source order. The common
instrumental case may remain validated while the role stays ambiguous.
These are competing supplied input readings, not simultaneous kāraka assignments
or a new grammatical optionality rule. A bare
passive instrumental retains both morphological candidates without claiming
either semantic assignment. The JSON benchmark now has 38 records, including
selected agent, selected active/passive instrument and competing readings. The
previous morphology-only passive-agent expectation is corrected to uncertainty;
the selected-agent positive is tested separately with its premise explicit.

Still open: automatic selection of independent agency and prakṛṣṭopakāraka
construal, full lexical/sense/voice coverage and remaining derivational-domain
checks. Existing low-level unattributed facts are explicitly supplied premises.
This increment does not certify full natural-language semantic discovery.

Validation: 38/38 JSON cases passed (27 clauses with supplied facts and 11
without). All five grammar-module suites and collection execution integration
passed after correcting the typed-candidate and selected-means fixtures (47
tasks; final run 2 executed, 45 up-to-date from the same production changes).

## Kṛt-governed case analysis in finite sentence frames

`VakyaSemanticContext.krtGovernors` now links dependent occurrences to actual
kṛdanta nominal occurrences and a selected expressed-role reading. The frame
passes the governing AST, lexical root and occurrence identity into the existing
canonical rule engine. Conflicting readings of one governor, self-links, cycles,
wrong target kinds, out-of-range indices and absent root declarations are rejected.

`KriyaFrame.nominalKarakas` separates nominal-action relations from finite
participants. Agreement reads finite relations only. Verbal valency, dvikarmaka
ambiguity and object/recipient lookup are scoped by action owner; nominal
dependents do not borrow the enclosing finite action's arguments or vice versa.
Valency provenance records the owning root/action. Kṛt causation checks its own
sanādi structure rather than inheriting the outer verb's causative voice.

Known unprefixed simple lexical readings infer genitive eligibility from
karmatva and selected expressed roles, including an implied object. Thus a
transitive action-only kṛt cannot receive an unrestricted agent genitive merely
because its object is unwritten. The positive agent-genitive fixture uses
intransitive ās + lyuṭ; the transitive kṛ counterpart retains 1.4.54 but no
validated genitive under the 2.3.66 restriction. Supplied eligibility contradicting
this known reading is rejected. Unknown readings do not invent case assignment.

Sentence tests cover 1.4.54 → 2.3.65/67/68/71, finite active/passive contexts,
number-agreement isolation, canonical owner/root provenance and invalid graphs.
Nine JSON records extend the benchmark to 47 cases, covering these paths plus
1.4.49 → 2.3.66 and rejection of an outer action's object as a kṛt recipient's
mediator. These checks no longer depend only on direct rule-engine fixtures.

Remaining: automatic governor/reading discovery, complete formation validation,
prefixed and complex argument structures, all sanādi/prefix-sensitive kāraka
domains, and verb-less kṛt-governed case analysis. The latter currently rejects
explicit kṛt bindings rather than silently ignoring them. Existing low-level
legacy contexts remain explicit supplied premises. No extra rule IDs are added.

## Canonical 1.4.23–55 matrix

The complete 33-rule block now has a checked canonical-wording fixture in
`KarakaCanonicalMetadataTest`. It selects the Kāśikā heading forms used here,
ignores editorial whitespace only, checks unique canonical numbering and
verifies that 1.4.23 is an adhikāra while 1.4.24–55 declare its governance and
perform saṃjñā. The audit corrected 1.4.49's कर्तूरीप्सिततमं to
कर्तुरीप्सिततमं and removed the mistaken claim that 1.4.23 itself enforces
mutual exclusivity. This is a wording/metadata check, not proof of every
semantic anuvṛtti, precedence or exception obligation below.
Reference: [Kāśikā, 1.4](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

For source rows 1.4.24–31, the general/special boundary audit above additionally
checks 84 JSON cases and independent provenance for every defining premise.
Established special meanings survive invalid endings, while 1.4.24 defers to
applicable canonical predicates instead of a root-wide blacklist. These shared
checks supplement each row's construction-specific coverage and remaining work.

Recipient rows 1.4.33–38 additionally have 52 JSON boundary/precedence cases and
independent-premise provenance checks, as described in the latest audit above.
Their established semantic designations survive invalid endings without a case
proof. Rows 1.4.39–41 additionally have 76 JSON cases for case, prefix and lexical
identity boundaries plus nominal-action ownership tests. The broader general
1.4.32 mediation audit remains separate work.

| Sūtra | Function in the analyzer | Status | Current evidence / remaining work |
|---|---|---|---|
| 1.4.23 | `कारके` governing domain | Governing | Canonical metadata for all 33 entries and declaration on every 1.4.24–55 rule verified. Executable list contains exactly the 32 assigning rules. Direct governing-rule assignment rejected; morphology-only hypothesis cannot receive 1.4.23 proof. Semantic action-relation scope remains an obligation of each assigning rule. |
| 1.4.24 | reference point in separation → अपादान | Partial | Established `SEPARATION_REFERENCE` and SOURCE required independently of case. Root-wide exclusions removed; applicable canonical 1.4.25–31 predicates control special-construction precedence. Ablative alone rejected; running-horse reference asserts 1.4.24→2.3.28, falling agent 1.4.54→2.3.46 and owner 2.3.50 remain distinct. Automatic event/reference extraction, negated separation and broader competing-rule coverage remain. |
| 1.4.25 | feared danger → अपादान | Partial | Explicit `FEAR_CAUSE` plus source evidence required. भी/त्रा/रक्ष्/उद्विज् matrix asserts 1.4.25→2.3.28, generic-source rejection and forest-locus 1.4.45→2.3.36; protected object retains 1.4.49→2.3.2. Automatic danger inference, lexical senses and structured-prefix/voice coverage remain. |
| 1.4.26 | unbearable participant with `परा-जि` → अपादान | Partial | Requires `UNBEARABLE_OBJECT` plus source evidence. Explicit परा + जि AST asserts 1.4.26→2.3.28; generic source is rejected and conquered enemy retains 1.4.49→2.3.2. Wrong/unknown prefixes do not establish 1.4.26. Automatic sense extraction and broader prefix/voice matrix remain. |
| 1.4.27 | desired object of prevention → अपादान | Partial | Explicit prevented-object and source meaning required. Barley/cows/field chains covered. Structured नि + वृत् + णिच् active/passive-causative JSON positives and missing-intent/wrong-prefix negatives pass; lexical substring lookalikes rejected. Automatic perspective/intent extraction and complete open prevention-root class remain. |
| 1.4.28 | intended observer in self-concealment → अपादान | Partial | Explicit observer plus source and concealment-domain evidence required. Structured नि + ली and supplied SELF_CONCEALMENT domains assert 1.4.28→2.3.28 in active/passive JSON; missing-domain/observer, wrong/unknown-prefix and substring negatives covered. Ordinary seeing-object competitor retained. Automatic intent/observer extraction, complete अन्तर्धा representation and wider competition remain. |
| 1.4.29 | teacher/source in formal study → अपादान | Partial | Exact 1.4.29→2.3.28 chain requires explicit `FORMAL_INSTRUCTION_SOURCE` and SOURCE; no artificial closed root list. Structured अधि + इ and आङ् + गम् + णिच् covered in active/passive JSON. Generic sources rejected; ordinary performer genitive retains 2.3.50. Automatic regulated-instruction/teacher extraction and wider competition remain pending. |
| 1.4.30 | cause/origin of birth → अपादान | Partial | Requires explicit `BIRTH_MATERIAL_CAUSE` or `BIRTH_ASSISTING_CAUSE` plus source evidence, following Kāśikā/Nyāsa. Both assert 1.4.30→2.3.28; generic-source and wrong-root negatives and ordinary-locus competitor covered. Automatic cause extraction, lexical normalization and alternative-commentary configuration remain. |
| 1.4.31 | first manifestation source with `भू` → अपादान | Partial | Explicit `FIRST_MANIFESTATION_SOURCE` plus source evidence required. Exact 1.4.31→2.3.28 chain, generic-source negative and ordinary-locus 1.4.45→2.3.36 competitor covered. Automatic semantic extraction, lexical normalization and voice matrix remain. |
| 1.4.32 | recipient intended through an object → सम्प्रदान | Partial | Exact chains; morphology/category-only recipient rejected. A bounded canonical resolution snapshot validates written mediators; unresolved object/non-object competition blocks dependent recipient assignment. Explicit links remain occurrence-scoped. Linked दा+ल्युट् tests reject finite/other-nominal objects even through explicit cross-action links, in both outer voices. Omitted objects require an explicit premise: the sole-participant fallback and root-wide exclusions are removed. Applicable 1.4.33–41 predicates take precedence; failed unbound prior-agent/destiny readings cannot become assumed ordinary recipients. General joint/cyclic solving, complete object-designation coverage, donation-intent discovery and complete lexical senses remain. |
| 1.4.33 | one pleased by रुच्-type action → सम्प्रदान | Partial | Explicit `PLEASED_EXPERIENCER` plus recipient evidence required. रुच्/स्वद् matrix asserts 1.4.33→2.3.13, generic-recipient rejection, sweet-subject 1.4.54→2.3.46 and path-locus 1.4.45→2.3.36. Automatic experiencer extraction, pleasure versus shining senses and broader synonyms remain. |
| 1.4.34 | person intended to be informed by the stated act → सम्प्रदान | Partial | `INTENDED_TO_BE_INFORMED` required in addition to recipient evidence; exact 1.4.34→2.3.13 chain and generic-recipient negative covered. Full listed-root senses remain. |
| 1.4.35 | creditor with धार् → सम्प्रदान | Partial | Explicit `CREDITOR` premise required; generic recipient rejected. Positive 1.4.35→2.3.13 chain covered; creditor discovery and broader lexical senses remain. |
| 1.4.36 | object of longing with स्पृह् → सम्प्रदान | Partial | Established `LONGED_FOR_OBJECT` and RECIPIENT required independently of case. Nyāsa policy: established `DESIRED_OBJECT` takes 1.4.49 independently of candidates, including invalid dative endings that receive no case proof. Generic recipient alone is rejected. Ordinary forest retains 1.4.45→2.3.36. Automatic desire-degree inference and alternate-commentary policy remain. |
| 1.4.37 | target of anger/hostility/envy → सम्प्रदान | Partial | Requires explicit `ANGER_TARGET` plus recipient evidence. क्रुध्/द्रुह्/ईर्ष्या/असूया exact chains and missing-hostility negatives covered; ordinary envy-object retains 1.4.49→2.3.2. Automatic hostility extraction and broader lexical senses remain. |
| 1.4.38 | prefixed क्रुध्/द्रुह् target → कर्म | Partial | Shared Upasarga inventory and explicit Tinganta AST prefix identities are used. All 20 typed prefixes select 1.4.38→2.3.2 for क्रुध्; an empty prefix list selects 1.4.37→2.3.13 and unknown prefixes remain unresolved. Raw surface-only sandhi normalization remains pending. |
| 1.4.39 | destiny-inquiry target with राध्/ईक्ष् → सम्प्रदान | Partial | Established target meaning licenses designation independently of case. Both roots have active/bhāve valid/invalid-case, missing-target and unknown-prefix JSON checks. Linked kṛt owns root/prefixes rather than the outer verb. Exact 1.4.39→2.3.13 chain retained for valid dative. Automatic enquiry/event extraction, complete lexical variants and wider competition remain. |
| 1.4.40 | prior agent of प्रति/आङ्-श्रु construction → सम्प्रदान | Partial | Established PRIOR_ACTION_AGENT and RECIPIENT required independently of case. Both typed prefixes have active/passive valid/invalid-case, missing-premise, wrong/unknown-prefix and wrong-root JSON checks. Linked nominal-action ownership and general 1.4.32 deferral covered. Discourse extraction and full lexical/competition coverage remain. |
| 1.4.41 | prior agent with अनु/प्रति-गॄ शब्दे → सम्प्रदान | Partial | Established inherited prior-agent and recipient meanings required independently of case. Both typed prefixes have active/bhāve JSON boundaries. Requires selected क्र्यादि गॄ lexical identity; तुदादि swallowing homonym and absent identity rejected. Linked nominal-action ownership and general 1.4.32 deferral covered. Discourse extraction and complete lexical/competition coverage remain. |
| 1.4.42 | most effective means → करण | Partial | Selected-means evidence required; case/category hypotheses alone rejected. Active/passive instrumental, agent competitors and equal object/instrument readings covered. Case candidates no longer suppress an equally established instrument reading; invalid endings preserve the role proof. Automatic prakṛṣṭopakāraka selection and full sense coverage remain. |
| 1.4.43 | instrument of दिव् bears कर्म and करण together | Partial | Nonoptional च records simultaneous names; inherits 1.4.42. Active case projections assert 1.4.43→2.3.2/18 without declining either designation. Linked ल्युट् checks genitive 2.3.66 and instrumental 2.3.18 projections with exact coexistent branches in both outer voices. Shared-power abhihita handling covers passive nominative and supplied instrument-denoting kṛt expression; no extra instrumental is generated. Invalid case does not erase names or supply a guessed execution operand. Full lexical/voice/competition coverage and coexisting-name value-history integration remain. |
| 1.4.44 | hiring price optionally → सम्प्रदान | Partial | Explicit fixed-term payment and selected-instrument premises distinguish hiring from outright purchase. Both directions retain applied 1.4.44→2.3.13 and underlying 1.4.42→2.3.18, with optional decisions separate from proofs. Linked परि-क्री+ल्युट् JSON covers both outer voices, missing premises, shared भ्याम् and invalid endings. Ordinary purchase has no optional dative. Inherits 1.4.42; automatic construction, vivakṣā discovery and complete competing-reading coverage remain. |
| 1.4.45 | action locus → अधिकरण | Partial | Exact 1.4.45→2.3.36 chain; structured finite and nominal action exclusions for 1.4.46–48 share owner-root/prefix predicates. Equally established object/locus alternatives survive candidate filtering and are compared by case compatibility, retaining ambiguity if neither case fits. Complete special-rule competition, sense discovery and homonym matrix remain. |
| 1.4.46 | locus of अधि-शी/स्था/आस् → कर्म | Partial | All three finite AST roots assert 1.4.46→2.3.2 over locative competition; all three linked nominal roots assert 1.4.46→2.3.66 in both outer voices. Missing locus premise rejected in JSON. General 1.4.45 defers to the same structured domain. Wrong/missing/unknown nominal prefixes and outer-prefix leakage rejected. Automatic sense discovery and complete lexical/competition coverage remain. |
| 1.4.47 | locus of अभिनिविश् → कर्म | Partial | Ordered अभि/नि AST prefixes and विश् root establish 1.4.47→2.3.2; linked nominal action asserts 1.4.47→2.3.66 in both outer voices, with missing-locus JSON negatives. Explicit `ABHINIVESH_LOCATIVE_USAGE` instead retains 1.4.45→2.3.36 under व्यवस्थितविभाषा; no free alternatives generated. Incompatible observed locative retains special karman with unvalidated case when that reading premise is absent. Automatic construction discrimination and wider lexical variants remain. |
| 1.4.48 | locus of उप/अनु/अधि/आ-वस् → कर्म | Partial | All four finite prefixes including typed आङ् assert 1.4.48→2.3.2; bare वस् asserts 1.4.45→2.3.36. All four linked nominal prefixes assert 1.4.48→2.3.66 in both outer voices, with missing-locus JSON negatives. `FASTING_SENSE` blocks special कर्म; nominal उप-वस् fasting retains 1.4.45→2.3.36. Known root class must be BHVADI; ADADI covering homonym retains general locus. Automatic sense extraction, full competition and unresolved homonym handling remain. |
| 1.4.49 | most desired object → कर्म | Partial | Active/passive exact chains and Kāśikā-based milk/rice and horse/beans contrasts are checked in JSON with supplied occurrence meanings. Established transitive object meaning survives invalid endings; 28 active/passive/causative/passive-causative JSON cases separate role designation from case validation and missing meaning. Morphology-only hypotheses cannot bypass case candidates. Requested nominal derivation readings outrank unrelated verb valencies without suppressing explicit competing meanings. Desired and indifferent readings assert distinct 1.4.49/50 chains. Automatic agent-relative desire/superlative discovery and the full competing-rule matrix remain. |
| 1.4.50 | connected but undesired object → कर्म | Partial | Active/passive 1.4.50→2.3.2/46 chains and mediation under 1.4.32 covered. Stronger occurrence meaning outranks generic desired-object valency; established anīpsita survives invalid endings. Category hypotheses cannot prove it. Equally supported 1.4.49/50 readings retain both proofs without losing the common object role; distinct-role competition is checked. Automatic agent-relative vivakṣā discovery, full unwanted/indifferent subclasses and special-rule competition remain open. |
| 1.4.51 | otherwise unexpressed dvikarmaka participant → कर्म | Partial | Explicit and competing analyses assert 1.4.51→2.3.2. Exact-root domain includes Kāśikā's भिक्ष् and Kaumudī's पच्/दण्ड्; 18-root matrix and missing-premise negatives pass. Milking tool/locus retain their own chains; substring lookalikes rejected. Automatic secondary-object discovery and lexical sense restrictions remain. |
| 1.4.52 | causee of motion/cognition/etc. causative → कर्म | Partial | नी/अद्/खाद् restrictions override general classes. भक्ष् needs injurious sense; वह् needs नियन्तृकर्तृक construction for कर्म. Excluded senses retain instrumental agent with independent-agent evidence. Missing/contradictory construction evidence cannot establish special causee कर्म. Linked nominal णिच्+ल्युट् matrix checks all eleven root/sense combinations in both outer voices and valid/invalid endings, with exact 1.4.52→2.3.66 or 1.4.54→2.3.18 chains. Nested causatives, automatic sense discovery, complete lexical/competition coverage and complete restriction provenance remain. |
| 1.4.53 | causee with हृ/कृ optionally → कर्म | Partial | Established original-agent meaning required; case/category/voice hypotheses alone rejected. Both roots have active/passive-causative applied and declined branch chains, invalid endings and missing antecedents. Optional 1.4.53→2.3.2/46 and underlying 1.4.54→2.3.18 remain available from either observed form; decisions are separate from designation proofs. Correct anuvṛtti of 1.4.49/52; prior-causative agents and the आत्मनेपद अभिवाद्/दृश् commentary extension remain excluded. Automatic event/sense linking, complete competition and full passive promotion remain. |
| 1.4.54 | independent participant → कर्तृ | Partial | Active/passive/bhāve exact chains and causative exclusions covered. Established independence survives wrong endings; morphology-only meaning cannot bypass candidate compatibility. Equal object/agent readings retain both proofs and resolve only with a unique compatible case. Automatic independence/vivakṣā discovery and the complete special-rule negative matrix remain. |
| 1.4.55 | causer/prompter → हेतु-कर्तृ | Partial | Typed HETU co-designation retained alongside KARTR from rule to sentence frame. Active 1.4.55→2.3.46, passive-causative 1.4.55→2.3.18, priority over 1.4.54, and ordinary-agent/causee designation negatives covered. Removed the unjustified root-wide kṛ prompter premise from lexical and fallback profiles; parsed ordinary/ṇic contrast preserves 1.4.54 versus 1.4.55. Automatic prompter-event linking and full competing-reading matrix remain. |

## Interacting 2.3 rules: first audit slice

2.3.9 now distinguishes superiority reference with उप from sovereignty
reference with अधि. Both require explicit कर्मप्रवचनीय governance and the
matching relation-specific particle link. General 2.3.8 defers to either fully
established special locative construction, independently of offered morphology.
Regressions cover both domains, missing premises, mismatched links and
accusative/locative competition. The sovereignty premise identifies the
selected स्व/स्वामिन् reference; automatic selection and the constraint against
marking both relational endpoints simultaneously remain pending.
See [Kāśikā, 2.3.9](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

2.3.10 now requires the explicit ap/āṅ/pari कर्मप्रवचनीय link, inherited
governance and exclusion/limit sense. It is reachable as a direct ablative;
general 2.3.8 defers to the fully established special construction even when
only incompatible accusative morphology is offered. Regressions cover each
missing premise and non-exclusion परि retaining 2.3.8. This follows Kāśikā's
restriction of परि here to the exclusion sense; automatic distinction of the
particle's sense and relation remains pending.
See [Kāśikā, 2.3.10](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

2.3.8 and 2.3.11 are now reachable as direct case constructions. 2.3.11
requires representative/exchange semantics, the original reference (`यस्मात्`),
explicit कर्मप्रवचनीय governance and a प्रति link. General 2.3.8 defers when
all those premises establish the special ablative construction, even if only
accusative morphology is offered. Regressions cover competing accusative/
ablative readings, each missing premise, ordinary 2.3.8 and unmarked accusative
rejection. The 2.3.11 explanation no longer incorrectly includes अभि.
Automatic 1.4 कर्मप्रवचनीय classification and relation linkage remain pending;
the separate 2.3.10 exception now has explicit supplied-premise coverage.
See [2.3.8](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.8.htm)
and [2.3.11](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.11.htm).

2.3.7 now requires both intervening time/distance and `KARAKA_POWER_INTERVAL`
evidence and is reachable as a direct case relation. It exposes locative and
ablative `OPTIONAL_VIBHAKTI` branches; compatible observed readings preserve
case ambiguity. The rule does not require two distinct physical entities:
Nyāsa's two agent-powers of one person in successive actions are within its
domain. Regressions test both branches, missing premises and syncretic case
ambiguity without inventing अपादान/अधिकरण saṃjñā for the interval itself.
Automatic event/role linkage and inference of the intervening measure remain
pending.
See [2.3.7 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.7.htm).

2.3.4 now requires `ANTARA_NIPATA_LINK` in addition to between/without
semantics. The supplied premise identifies the participant directly linked to
nipāta अन्तरा/अन्तरेण, excluding nominal homonyms and unrelated possessors.
The rule is reachable as a direct accusative case construction, taking priority
over a competing generic genitive hypothesis. Missing-premise, unrelated
genitive and nominative-principal regressions are covered. Automatic nipāta
disambiguation and dependency discovery remain pending.
See [2.3.4 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.4.htm).

2.3.5 no longer treats the absence of other semantic tags as proof of
अत्यन्तसंयोग. `CONTINUOUS_TIME_DISTANCE_EXTENT` explicitly establishes the
time/distance full-extent construction. 2.3.6 inherits that premise and also
requires `COMPLETION_LIMIT` (result attainment, not merely stopping work).
General extent accusative is excluded in the explicitly completed construction.
Direct-case regressions assert 2.3.5 versus 2.3.6, missing-premise rejection,
incompatible accusative rejection and preservation of an ordinary genitive.
Automatic continuity/result inference and finer action/quality/substance scope
remain pending. 2.3.4's separate nipāta-specific linkage now requires explicit
construction evidence; automatic discovery is still not certified.
See [2.3.5](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.5.htm)
and [2.3.6](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.6.htm).

2.3.21 no longer matches an instrumental merely because other constructions
are absent. It requires positive `CHARACTERISTIC_MARK` evidence, and an explicit
`MARK_INCORPORATED_IN_COMPOUND` premise excludes the separate case relation.
The rule is now reachable as a direct identifying-mark instrumental, without
fabricating करण. Regressions cover unmarked instrumental rejection, the
positive 2.3.21 assignment, compound-incorporation exclusion, and ordinary
instrument 1.4.42→2.3.18. Automatic target/mark linking and detection of the
compound's incorporated meaning remain pending.
See [2.3.21 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.21.htm).

Passive-causative coverage now includes a `VakyaAnalyzer` integration test,
not only direct rule-engine contexts. An explicit णिच् + यक् sentence with
lexically supplied causee/causer construction premises retains `KARMANI` and
asserts causee 1.4.52→2.3.46 plus causer 1.4.55→2.3.18 in the produced frame.
This verifies premise propagation and rule reachability; assigning those
semantic relations to the lexical entries in the fixture is deliberate and
does not demonstrate automatic recognition from unannotated sentence text.

Causative construction detection now reads explicit AST णिच् independently of
prayoga. Passive yak therefore does not make 1.4.52/53 unreachable simply
because the frame is `KARMANI`. Supplied-fact regressions assert passive
causee-karman 1.4.52/53→2.3.46, optional instrumental agent 1.4.53→2.3.18,
and causer 1.4.55→2.3.18. Removing णिच् or supplying a prior-causative-agent
premise prevents the special assignment. The 1.4.53V lexical-pada extension
does not infer its required ātmanepada status from passive endings alone;
underlying lexical pada and full passive object promotion remain partial.
Automatic identification of which passive participant is the causee is not
proved by these supplied-premise regressions.

The अभिवादिदृशोरात्मनेपद उपसङ्ख्यानम् extension is registered separately as
`1.4.53V` (commentary vārttika, not a canonical sūtra). It requires explicit
आत्मनेपद morphology; अभिवाद् also requires the supplied salutation sense.
Applied/declined chains assert 1.4.53V→2.3.2 and 1.4.53V→2.3.18. In the
परस्मैपद contrasts, दृश् retains causee-karman while salutation अभिवाद् retains
instrumental agent. `PRIOR_CAUSATIVE_AGENT` blocks the extension. Tests cover
both roots and missing salutation evidence. Automatic sense recognition,
unknown-pada alternatives and passive-causative interaction remain partial.
See [Kāśikā/Nyāsa on 1.4.53](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.53.htm).

1.4.52–53 now enforce the inherited अणि कर्ता restriction when a participant
is explicitly marked `PRIOR_CAUSATIVE_AGENT`: an agent of the preceding
causative action cannot receive these rules' causee-karman treatment merely
because the original root belongs to their domain. Two-णिच् AST regressions
for गम् and कृ assert 1.4.54→2.3.18, reject accusative-only outer causees and
generate no 1.4.53 कर्म alternative. The text of 1.4.53 is corrected to
हृक्रोरन्यतरस्याम्. Counting णिच् affixes alone does not identify which event a
participant belongs to; automatic event-layer linking remains pending.
See [1.4.52](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.52.htm)
and [1.4.53](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.53.htm).

वह्'s conditional exception to 1.4.52 is checked before generic motion-class
matching. `VAH_NIYANTR_KARTRKA` explicitly licenses the causee-karman domain;
`VAH_ANIYANTR_KARTRKA` excludes it. Structured णिच् regressions assert
1.4.52→2.3.2 versus 1.4.54→2.3.18, requiring independent-agent evidence for
the latter. Missing/contradictory construction premises cannot establish an
accusative causee. No automatic inference from a human/animal noun or from
the existence of a causer is made. Kāśikā/Nyāsa's controller-sensitive
distinction is represented as supplied construction evidence, not flattened
into “all motion causees are कर्म.”
See [1.4.52 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.52.htm).

The भक्षेरहिंसार्थस्य exception to 1.4.52 is now enforced before broad
consumption-class matching. `BHAKS_INJURIOUS_SENSE` licenses the कर्म branch;
`BHAKS_NONINJURIOUS_SENSE` prevents it. Exact णिच् regressions assert
1.4.52→2.3.2 versus 1.4.54→2.3.18, with the latter requiring independently
supplied agent evidence. Missing or contradictory senses cannot establish an
accusative causee. This is explicit construction licensing, not inference that
all eating harms something or that every भक्ष् use is an ordinary causative.
Automatic sense interpretation and unresolved-sense alternatives remain partial.
See [1.4.52 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.52.htm).

1.4.52 no longer includes नी/अद्/खाद् as unconditional positive roots. Kāśikā's
नी and आदिखाद्योः prohibitions are checked before general motion/consumption
classes, including when supplied by a lexical entry. Structured णिच् regressions
reject accusative-only causees and retain instrumental कर्तृ under 1.4.54→2.3.18
when independent-agent evidence and instrumental morphology are present.
Conditional वह्/भक्ष् cases and nested causatives remain pending. The blanket
causative exclusion in 1.4.51 is also still an audit item; it has not been
removed on the strength of these different 1.4.52 examples.
See [1.4.52 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.52.htm).

1.4.51's exact-root inventory now includes previously omitted भिक्ष् from
Kāśikā and पच्/दण्ड् from the Kaumudī enumeration, plus मथ् alongside मन्थ्.
An 18-root matrix requires an explicit secondary-object premise and asserts
1.4.51→2.3.2. Kāśikā's milking instrument and vessel retain 1.4.42→2.3.18 and
1.4.45→2.3.36 respectively; root membership alone does not make them अकथित.
The inventory is a lexical domain, not proof of the construction's sense or
of participant-specific अपूर्वविधि. Those semantic obligations remain partial.
See [1.4.51 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.51.htm).

1.4.47 now records inherited अन्यतरस्याम् and respects an explicitly established
`ABHINIVESH_LOCATIVE_USAGE`. Kāśikā's कल्याणे/पापेऽभिनिवेशः examples and the
saṃjñā/saṃjñin construction motivate a conditioned locative domain. This is
व्यवस्थितविभाषा, not unrestricted accusative/locative interchangeability.
The explicit construction premise blocks special कर्म and restores general
अधिकरण. Regressions assert both chains and reject case readings incompatible
with the supplied construction. Automatic recognition of that construction
and unresolved ambiguity between senses remain unfinished.
See [Kāśikā, 1.4.47](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

1.4.48 now respects a supplied lexical root class: भ्वादि वस् निवासे is
distinguished from आदादि वस् आच्छादने. Both the special rule and general-locus
exclusion use the same domain predicate. A known आदादि root therefore cannot
receive the residence-locus कर्म assignment or block general अधिकरण merely
because its spelling is वस्. The regression asserts both exact chains. When
no lexical root entry is supplied, the existing AST-only domain remains a
hypothesis; unresolved homonym discovery is not certified by this change.
See [Tattvabodhinī on 1.4.48](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.48.htm).

The 1.4.48 वसेरश्यर्थस्य प्रतिषेधः vārttika is enforced when `FASTING_SENSE`
is explicitly supplied on the participant construction or base-root semantics.
It blocks residence-based कर्म while allowing general 1.4.45 for the fasting
locus. Competing accusative/locative hypotheses resolve to 1.4.45→2.3.36;
ordinary residence retains 1.4.48→2.3.2. Accusative-only fasting locus and unknown
prefix identities remain unresolved. This does not infer fasting from उपवसति
alone or from incidental meal abstention; the fasting sense must be established.
See [1.4.48 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.48.htm).

1.4.39 retains its explicit `INQUIRY_DESTINY_TARGET` requirement, but now uses
exact राध्/ईक्ष् identities instead of a three-varṇa prefix comparison. An
explicit AST root takes precedence over an opaque surface identity; unknown
prefix identities do not establish this domain. Both roots and missing-premise,
wrong-root and prefix-lookalike negatives are covered. Canonical text and
saṃjñā metadata are corrected.
See [Kāśikā, 1.4.39](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

1.4.37–38 now require `ANGER_TARGET`, enforcing यं प्रति कोपः and its
inheritance into the prefixed क्रुध्/द्रुह् exception. Generic recipient evidence
alone cannot establish either special role. The existing prefix matrices now
supply this premise explicitly; a separate six-root matrix checks its removal.
Kāśikā's भार्याम् ईर्ष्यति contrast is represented by the ordinary object
chain, with no directed-hostility premise. Existing prefix recognition does not
claim automatic recovery of the target's semantic relation.
See [1.4.37](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.37.htm)
and [1.4.38](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.38.htm).

The derivation caller no longer substitutes nominative when case resolution is
missing or ambiguous. `UnresolvedKarakaDerivationException` preserves the full
resolution for callers; explicit case derivation remains available separately.
Regressions cover missing separation evidence, optional quality-cause cases,
and passive nominative licensed by 2.3.46. The optional-case test also exposed
an `ANIRDHARITA` bypass of direct-rule eligibility; that provisional-role path
now uses the same audited direct-case rule set as candidate-derived unknown
roles. This prevents an unrelated non-kāraka rule from hiding case ambiguity.

2.3.19 now requires both accompaniment and `NON_PRINCIPAL_ACCOMPANIMENT`
evidence, enforcing अप्रधाने rather than applying to every सह-related nominal.
The canonical text is corrected to सहयुक्तेऽप्रधाने. It is reachable as a direct
instrumental-case rule without inventing करण. Regressions cover both missing
premises and preservation of the principal's genitive under 2.3.50. Automatic
discovery of the principal/subordinate relation, synonyms and implicit सहार्थ
remains pending; the rule does not require an overt सह token at this boundary.
See [2.3.19 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.19.htm).

2.3.26–27 now require cause meaning (`CAUSE_HETU`) together with explicit
हेतु use, rather than treating the mere occurrence of हेतु as sufficient.
Both are reachable as direct case rules. `PRONOMINAL_HETU` additionally
licenses instrumental/genitive alternatives under 2.3.27; general 2.3.26
defers to it. General 2.3.23 does not rescue instrumental for an explicitly
marked non-pronominal हेतु construction. Both branches, observed-case
ambiguity, missing inherited premises and the non-pronominal genitive are
covered. Discovering the syntactic हेतु relation and the broader vārttika
on निमित्त/कारण/हेतु remains pending.
See [2.3.26](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.26.htm)
and [Kāśikā, 2.3.27](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

2.3.23 and 2.3.25 are now reachable as direct causal-case rules without a
fabricated 1.4 करण/अपादान assignment. `CAUSE_HETU` licenses instrumental;
`NON_FEMININE_QUALITY_CAUSE` licenses optional instrumental/ablative under
2.3.25, to which general 2.3.23 defers. Both branches and observed case ambiguity
are tested. Explicitly feminine-only morphology prevents the ordinary 2.3.25
domain even if the supplied composite premise contradicts it. Non-quality and
feminine causes retain 2.3.23 instrumental; ablative alone does not establish
quality-cause semantics. The योगविभाग extensions discussed by Nyāsa and later
commentaries are not inferred automatically and remain pending.
See [2.3.25 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.25.htm).

2.3.71 now exposes instrumental/genitive `OPTIONAL_VIBHAKTI` outcomes for
an explicitly linked kṛtya governor and independently marked agent. General
2.3.18 defers in this special domain, allowing both observed-case branches to
retain 1.4.54→2.3.71 evidence. Both cases compatible with the observed form
leave the agent resolved but case unresolved; without the kṛtya governor the
ordinary instrumental chain remains 1.4.54→2.3.18. The उभयप्राप्ति vārttika
now blocks the genitive branch when `KRT_DUAL_ELIGIBLE_AGENT` and kṛtya
governance are independently established. A rejected observed genitive records
`2.3.71V` as prohibition evidence; instrumental survives even when both case
readings fit the observed form. No forbidden genitive alternative is generated.
Automatic establishment of the special construction remains pending; this does
not certify all kṛtya syntax. `2.3.71V` identifies the commentary vārttika,
not a new canonical numbered sūtra.
See [Kāśikā, 2.3.71](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/2/03/).

1.4.36 now requires `LONGED_FOR_OBJECT` (īpsitamātra). Under the current
Nyāsa interpretation, an independently supplied īpsitatama premise
(`DESIRED_OBJECT`) with compatible कर्म morphology allows 1.4.49 instead.
The competing-case regression supplies both dative and accusative hypotheses
and asserts 1.4.49→2.3.2. Generic recipient evidence alone does not establish
longing. Later commentaries report a contrary Vākyapadīya/Helārāja interpretation
that blocks कर्म and śeṣa-genitive here; that alternative is not implemented.
See [1.4.36 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.36.htm).

1.4.33 now requires `PLEASED_EXPERIENCER`; a dative/generic-recipient hypothesis
does not itself establish प्रीयमाण. Both रुच् and Kāśikā's स्वद् are recognized.
The pleasing subject is kept distinct from the experiencer, and पथि remains
अधिकरण. The explicit premise restricts the supplied-fact domain; it does not
claim automatic distinction of रुच्'s pleasure and shining senses.
See [1.4.33 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.33.htm).

The vārttika on 1.4.24 (`1.4.24V`) no longer uses substring overlap against
bare रम्/मद्. Exact जुगुप्स्/विरम्/प्रमद् constructions and explicit वि + रम् /
प्र + मद् AST forms assert 1.4.24V→2.3.28. Bare roots, wrong prefixes, substring
lookalikes and unknown prefix identities are negative regressions. Explanatory
references incorrectly naming 1.4.25 are corrected. Status remains **Partial**:
polysemous senses, participant-specific semantic licensing and derived गुप् + सन्
normalization still need audit. This does not certify every prefixed root sense.

1.4.24 now requires `SEPARATION_REFERENCE`, an event-relative avadhi, rather
than accepting every `SOURCE` hypothesis. Nyāsa's moving-horse example shows
that this does not require physical immobility. No source premise is derived
merely from the case ending into this stronger relation. Nyāsa also retains
अपादान in negated separation clauses; future event inference must therefore
not require an actually completed separation or erase the role under negation.
See [1.4.24 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.24.htm).

1.4.25 requires भयहेतु, represented by `FEAR_CAUSE`; a generic source in a
fear/protection clause is insufficient. रक्ष् and उद्विज् are included in the
lexical domain following Kāśikā's examples. The forest in अरण्ये बिभेति is
retained as अधिकरण, and the protected participant with रक्ष् remains कर्म.
See [1.4.25 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.25.htm).

An active/passive रक्ष् clause regression now resolves the protector, protected
participant and danger together at the rule-engine boundary. It asserts agent
1.4.54→2.3.46/2.3.18 and object 1.4.49→2.3.2/2.3.46 alternation, while the danger
retains 1.4.25→2.3.28 in both voices. The danger uses syncretic भ्याम् morphology;
removing `FEAR_CAUSE` prevents resolution in both voices. These are supplied-fact
tests, not evidence that surface-text analysis discovers fear semantics.

1.4.31 requires first manifestation, not an arbitrary source or location.
The place where the bhū-agent first appears is distinct from the causal origin
in 1.4.30. The latter's explanatory text no longer incorrectly says “maternal”
cause. The cause-domain restriction now requires separately represented material
or assisting causes. Current rule policy follows Kāśikā/Nyāsa in accepting both;
later commentaries' narrower material-cause reading is documented rather than
silently treated as identical. No automatic causal inference is claimed.
See [1.4.30](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.30.htm)
and [1.4.31](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.31.htm).

1.4.27 now distinguishes the object desired by the restrained participant from
the participant being restrained. The former requires the explicit
`PREVENTED_DESIRED_OBJECT` premise; neither generic source nor the agent's
ordinary desired-object relation substitutes for it. Kāśikā's यवेभ्यो गा वारयति
क्षेत्रे supplies the barley/cows/field regression distinctions.
See [Kāśikā, 1.4.27](https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/).

1.4.26 now enforces असोढ rather than treating every source with परा-जि as
अपादान. This is a supplied semantic premise, not inferred from the case ending.
Kāśikā contrasts अध्ययनात् पराजयते with शत्रून् पराजयते; both constructions
have exact evidence-chain regression coverage. Structured परा + जि is recognized,
and general 1.4.24 cannot bypass the missing असोढ premise in that construction.
See [1.4.26 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.26.htm).

2.3.22 now requires `SAM_JNA_OBJECT` instead of matching every कर्म with
instrumental morphology. General 2.3.2 defers in that domain, and the optional
accusative/instrumental cases are represented by `OPTIONAL_VIBHAKTI` branches.
Tests cover ordinary-object rejection, exact 1.4.49→2.3.22 chains, generated
alternatives, and ambiguity when both cases fit. The governing सम्-ज्ञा AST
now supplies construction evidence when an independent desired-object premise
is present. Enrichment preserves morphological or explicitly supplied role
hypotheses and is idempotent. Unknown prefix identities and missing independent
object evidence do not establish this construction. Automatic object discovery,
lexical sense distinctions, and competition with kṛt genitives remain pending.
The commentaries also disagree about the ध्यान sense and 2.3.52; this requires
an explicit interpretation rather than a fabricated universal precedence rule.
See [2.3.22 and its commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.22.htm).

The same integration test exposed unrestricted 2.3.17 masking 2.3.22.
2.3.17 now requires the supplied `MAN_INANIMATE_DISREGARD_OBJECT` construction
premise; its positive and ordinary-object negative are tested. General 2.3.2
defers in this domain, so both accusative and dative branches carry exact
1.4.49→2.3.17 evidence. `OPTIONAL_VIBHAKTI` retains the generated alternative;
if both cases fit observed morphology, the kāraka remains कर्म while the case
remains unresolved. Separate
extraction of the root, disregard, animacy, and exceptional lexical classes
remains pending.

Kāśikā/Nyāsa explicitly distinguishes दिवादि मन (मन्यते) from तनादि मनु
(मन्वे), and the vārttika exceptions include नाव/अन्न exclusions and a श्वन्
inclusion despite animacy. The existing composite premise asserts eligibility;
its name does not model that complete exception system. This rule remains
**Partial**, not certified from word identity or a simplistic animacy flag.
See [2.3.17 and commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.17.htm).

Frame relations now retain `resolvedVibhakti`. Case alternatives yield
`OPTIONAL_VIBHAKTI` informational diagnostics for generated forms and
`AMBIGUOUS_VIBHAKTI` warnings when alternative cases fit the input. Case
ambiguity is kept distinct from kāraka ambiguity.

2.3.12 now requires explicit `MOTION_GOAL`, `PHYSICAL_MOTION` (ceṣṭā), and
`NON_PATH_DESTINATION` (anadhvan) premises. Removing any premise prevents
the special case rule from matching; general 2.3.2 remains available for an
ordinary accusative object. With all premises present, 2.3.2 explicitly defers.
Both accusative and dative branches assert canonical 2.3.12 evidence.

`OptionalVibhaktiRuleOutcome` records explicit permitted case sets, separately
from optional kāraka assignment. For 2.3.12, alternatives propagate through
`KarakaResolution.alternatives` as `OPTIONAL_VIBHAKTI`. When both cases fit
the morphology, कर्म remains resolved while the case remains unresolved;
generated forms incompatible with the input are labeled accordingly.
Exact 1.4.49→2.3.12 chains cover dative-only and mixed-case inputs. These
integration tests supply the independently established कर्म candidate;
automatic semantic and candidate generation for such dative motion objects
remains pending. Source: Kāśikā 2.3.12 (physical versus mental movement,
and destination versus traversed path).

2.3.70 is corrected to `अकेनोर्भविष्यदाधमर्ण्ययोः`. Its supplied construction
evidence distinguishes `AKA_FUTURE_DERIVATION`, `IN_FUTURE`, and `IN_DEBT`;
there is no blanket aka-in-debt condition. Each branch blocks 2.3.65 in the
sentence resolver and retains canonical rejected-rule provenance. No such
evidence leaves the general kṛt genitive available. The former direct apply
stub assigning prathamā was removed: this niṣedha does not select a replacement
case. The legacy boolean remains for caller compatibility; new callers should
use the typed condition. Automatic derivational-domain recognition remains
pending. Source: [Kāśikā/Nyāsa 2.3.70](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.3.70.htm).

The full 73-entry implementation and audit-status inventory is maintained in
[`VIBHAKTI_SUTRA_INVENTORY.md`](VIBHAKTI_SUTRA_INVENTORY.md). Its declared-text
column is a baseline for canonical comparison, not a claim that every current
spelling is correct. All entries remain partial pending their individual
semantic, textual, exception, precedence and integration audits.

The dvikarmaka lookup no longer treats containment in either direction as root
identity. चिन्त्/अर्चि/अज्ञातदुह् counterexamples remain unresolved even with a
secondary-object marker; a typed प्र-दुह् base retains 1.4.51→2.3.2. Sentence
ambiguity generation uses the same AST base root. This domain test does not
replace the construction-specific akathita requirement.

The causative domain distinguishes `WORD_AS_OBJECT` (śabdakarman) from generic
`SPEECH`. The latter alone no longer generates a causee hypothesis. For
1.4.52, a marked causee is insufficient without the root-domain condition.
Homonymous पा/विद् are not admitted by a spelling-only fallback; their relevant
meaning must come from lexical semantic evidence. The explicit fallback list
is partial and must be expanded through sourced lexical entries, not substring
overlap. Tests reject unrelated लिख्/दा and opaque कारय/संस्कार as independent
base-root evidence, while a typed प्र-कृ-णिच् AST selects 1.4.53.

1.4.44 requires fixed-term hiring-payment evidence in addition to an instrument
and a purchase/hiring root. General 1.4.42 defers only when that special
construction is present. Negative tests exercise both क्री and परिक्री without
the hiring premise: syncretic instrumental/dative morphology resolves only
as करण, while a dative-only form remains unresolved. Source:
[Kāśikā 1.4.44](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.44.htm).

Optional alternatives distinguish grammatical generation from analysis of the
written form using `matchesObservedMorphology`. If a declined optional branch
has a different kāraka and also fits the observed cases, the resolver leaves
both `resolved` and `resolvedVibhakti` unset. Thus general 1.4.42 no longer
hides optional 1.4.43/44 branches in syncretic constructions. Unambiguous
accusative/dative forms retain their selected reading while still exposing
the grammatical instrumental alternative as incompatible with that spelling.

The 1.4.37 exclusion is limited to the actual prefixed क्रुध्/द्रुह् domain
of 1.4.38. Explicitly prefixed ईर्ष्या/असूया retain the 1.4.37 dative chain
in the counterexample matrix. When the AST is supplied, its root and prefix
identities take precedence over a concatenated spelling hypothesis.

The 1.4.24–31 regression matrix exercises every rule with its lexical domain,
with the semantic premise removed, and with an incompatible kāraka candidate.
This checks prerequisite enforcement, not the full semantic definition of
each special source. The upayoga condition of 1.4.29 is separately tested:
root and source alone do not establish disciplined instruction.
Source: [1.4.29 and its commentaries](https://ashtadhyayi.com/sutraani/1/4/29).

| Sūtra | Use | Status / obligation |
|---|---|---|
| 2.3.1 | `अनभिहिते` governing domain | Implemented; eligibility uses declared adhikāra metadata rather than numeric range. |
| 2.3.2 | unexpressed कर्म → द्वितीया | Implemented; active, causative, optional and dvikarmaka chains covered. |
| 2.3.13 | सम्प्रदान → चतुर्थी | Implemented; multiple lexical rules and optional 1.4.44 covered. |
| 2.3.18 | कर्तृ/करण → तृतीया | Implemented; passive agent, instrument, and declined optional branches covered. |
| 2.3.20 | body-part deformity → तृतीया | Implemented directly as a non-kāraka 2.3 relation; exact analyzer regression rejects any fabricated 1.4 evidence. |
| 2.3.28 | अपादान → पञ्चमी | Implemented across 1.4.24–31 lexical rules. |
| 2.3.36 | अधिकरण → सप्तमी | Implemented; general 1.4.45 exact chain covered. |
| 2.3.46 | अभिहित कर्तृ/कर्म → प्रथमा | Implemented in active, passive and causative chains. |
| 2.3.50 | remaining non-kāraka relation → षष्ठी | Implemented as सम्बन्ध rather than a fabricated kāraka. |

The remaining special-purpose 2.3 VIDHI and NIṢEDHA rules must be inventoried
against `VibhaktiRuleContext` before this audit can be closed. In particular,
their optional outputs and priority relative to the general kāraka realization
rules cannot be inferred merely from registry order.

### Canonical 2.3 inventory gaps

The canonical text is checked against the [Sanskrit Documents sortable
Aṣṭādhyāyī index](https://sanskritdocuments.org/learning_tools/ashtadhyayi/).
The registry now enforces unique canonical numbers and records this exact
remaining gap set:

There are currently no missing canonical numbers in 2.3.1–73. The registry
test requires the complete set and rejects duplicate canonical identifiers.

Corrections made during this inventory:

- `एनपा द्वितीया` is 2.3.31, not 2.3.9.
- `न लोकाव्ययनिष्ठाखलर्थतृनाम्` is 2.3.69, not 2.3.66.
- `करणे च स्तोकाल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य` is 2.3.33, not 2.3.34.
- 2.3.34 `दूरान्तिकार्थैः षष्ठ्यन्यतरस्याम्` is now implemented as the
  optional ṣaṣṭhī branch paired with 2.3.35.
- The previously added `करणादाख्यातायाम्` and `द्विषः शतुर्वा वचनम्`
  vārttika entries (`2.3.32V1`, `2.3.69V1`) were removed from executable
  rule lists, consistent with the canonical-sūtras-only requirement.
- 2.3.3, 2.3.6, 2.3.9, and 2.3.24 now have explicit semantic predicates,
  positive and negative rule tests, and opt-in direct analyzer eligibility.
  The `छन्दसि` restriction of 2.3.3 is represented by a dedicated Vedic
  semantic relation, so ordinary objects of `हु` do not match it.
- 2.3.60–64 and 2.3.66 are implemented from their inherited ṣaṣṭhī context:
  Brāhmaṇa `दिव्` object, deity-oblation construction, Vedic dative meaning,
  Vedic `यज्` instrument, frequency-qualified temporal locus, and the
  object-only restriction when a kṛt form makes both agent and object eligible.
  Each has positive, negative, registry, and direct-engine coverage.

## Explicit exclusion discovered by the audit

### Remaining construction-evidence work

Canonical number coverage is complete; semantic coverage is still partial.
The new 2.3.3/6/9/24/60–64/66 predicates currently consume explicitly supplied
construction relations. Their tests establish rule and engine reachability;
they do not establish automatic extraction of every condition from an AST.
In particular, Vedic register, karmapravacanīya governance in 2.3.9, the
oblation exception in 2.3.61, and the competing agent restriction/exceptions
in 2.3.66 still require structured construction evidence and counterexamples.

2.3.65 now explicitly requires `KRT_GOVERNED_PARTICIPANT`, preventing ordinary
finite-clause agents/objects from receiving a kṛt-governed ṣaṣṭhī by case alone.
Positive agent/object tests and negative finite-clause, abhihita, and instrument
tests cover this boundary. Automatic kṛt governance extraction remains pending.

The ordinary 2.3.66 dual-eligibility domain now excludes both marked participants
from general 2.3.65: the competing agent is denied that genitive assignment,
and the object receives its specific evidence from 2.3.66. The construction
markers must still be supplied; feminine-affix exceptions remain pending.

Explicit participant governing-affix evidence now reaches the 2.3.69 prohibition
check in the sentence resolver. Its integration regression compares an allowed
1.4.54→2.3.65 chain with a prohibited case retaining 1.4.54 but no licensed
ṣaṣṭhī realization. Automatic affix classification and rejected-rule diagnostic
provenance remain pending.

Update: `KarakaRuleContext.governingKrt` now accepts an explicit syntactic link
to a typed `KridantaPratipadika`. `KrtGovernanceEvidence` extracts general
kṛt governance, kṛtya-agent governance, and the modeled 2.3.69 prohibited affix
classes from that node, preserving `GOVERNING_CONSTRUCTION` provenance.
The linked-AST regression covers ल्युट्→2.3.65, क्त exclusion, तव्यत्→2.3.71,
and absence of governance. Discovering the syntactic link automatically and
handling all lexical exceptions remain pending.

Matched vibhakti rules rejected by niṣedha now appear in
`KarakaResolution.rejectedRules` and propagate to `KarakaRelation`. The
2.3.65/2.3.69 regression requires both the rejected target and canonical
blocker evidence, while confirming an allowed branch has no rejection.

2.3.71 now requires the specific `KRTYA_GOVERNED_AGENT` premise. Its previous
unrestricted agent match could re-license a genitive rejected under 2.3.69.
The sentence prohibition regression and a direct non-kṛtya negative cover
this bypass; automatic gerundive governance extraction remains pending.

Specific directly licensed 2.3 constructions are tested before the general
2.3.50 fallback, so a supplied oblation/frequency/Vedic relation is no longer
silently reduced to generic सम्बन्ध.

`येनाङ्गविकारः` is 2.3.20, not a kāraka-saṃjñā sūtra in 1.4. The duplicate
`YenangavikarahKaranaSutra` pseudo-rule was removed rather than retained as
dead, misleading source. A registry invariant rejects `1.4.42V` and requires
the complete canonical number set 1.4.23–55.
