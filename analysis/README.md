# Module `:analysis`

Grammatical analyzer for surface padas and sentence-level vākya structures.

Canonical rule coverage and unresolved verification work are tracked in
[`KARAKA_SUTRA_AUDIT.md`](KARAKA_SUTRA_AUDIT.md). That matrix deliberately
distinguishes an implemented rule from a fully audited one.

## Overview

The executable [JSON benchmark](BENCHMARK.md) records structured inputs, supplied
semantic facts, exact expected chains, uncertainty cases and source provenance.
Its report separates annotated from unannotated inputs.

The [PVM corpus grammar benchmark](CORPUS_BENCHMARK.md) inventories actual
example/project clauses and verifies independently reviewed JSON
expectations through the execution adapter's grammar analysis. Its report
keeps unreviewed and unsupported clauses visible.

`VakyaAnalyzer.analyze(..., semanticContext = VakyaSemanticContext(...))` accepts
additional established facts keyed by flattened nominal occurrence index. Facts
retain `CLAUSE_CONTEXT` provenance and do not mutate lexical entries, spread to
repeated stems or persist into later calls. Invalid indices are rejected. This
context is supplied evidence; it does not claim automatic semantic discovery.

For finite clauses, `VakyaSemanticContext.intendedObjects` maps a recipient's
nominal index to its mediating object's index; an explicitly present null value
asserts an implied object. Written links must point to a distinct object in the
same clause, with object semantics. A wrong link cannot fall back to another
object. These supplied links carry clause-context evidence and do not infer a
case from a known role when the observed morphology is incompatible.

When a participant's role is resolved but no case assignment has been validated,
the frame emits `UNVALIDATED_VIBHAKTI`. This warning records missing proof rather
than declaring the sentence ungrammatical. Participant diagnostics include a
`nominalIndex`, distinguishing repeated identical source text. Observed optional
case ambiguity keeps its existing `AMBIGUOUS_VIBHAKTI` diagnostic.

Passive, bhāve and causative instrumental morphology retains agent and instrument
candidates. Selected means require lexical or clause-level meaning; case, tool
category or verbal valency alone does not establish 1.4.42. Nonactive agent
assignment also requires semantic evidence beyond its instrumental ending.
Equally supported supplied readings remain competing analyses; the common
instrumental case can be validated while the role remains ambiguous. These
readings do not introduce an optional sūtra or simultaneous kāraka assignment.

Finite clauses can supply `VakyaSemanticContext.krtGovernors`, linking a nominal
dependent to a kṛdanta occurrence with its selected expressed-role reading.
`KriyaFrame.nominalKarakas` holds those scoped relations separately from the
finite head's `relations`; they do not control finite agreement or borrow its
co-arguments. Known simple unprefixed lexical readings establish genitive
eligibility, including implied arguments; explicit contradictory eligibility is
rejected. Unknown readings retain unvalidated case. Verb-less kṛt-governed case
analysis and complete prefixed/complex derivational domains remain unfinished.

The kāraka/vibhakti rule lists accept canonical Aṣṭādhyāyī sūtras only, as
requested by the user. Added vārttika registrations and commentary-only
prohibition rules have been removed. Commentary can inform this audit but does
not supply extra executable rule identities. Commentary-dependent extensions
are outside the selected implementation scope.

For an explicitly linked kṛt governor, dual agent/object genitive eligibility
can be supplied as `KrtGenitiveEligibility`, with a matching
`KarakaRuleContext.governingKrtOccurrenceId`. The identity denotes an occurrence,
not its text. Eligible roles can include an implied argument. This premise must
be established by contextual analysis; neither two unrelated governors nor the
affix alone establishes dual eligibility. Unbound dual markers and contradictory
expressed-role readings leave case assignment unresolved. Automatic governor
linking remains unfinished. The commentary-only feminine exceptions to 2.3.66
are not implemented under the canonical-only scope.

Canonical 2.3.67–68 require a linked kta governor and a known selected reading.
The present-time kta agent uses 2.3.67; a location-denoting kta permits the
unexpressed agent or object genitive through 2.3.68. These special genitives
supersede the ordinary instrumental, and 2.3.66 does not suppress the agent
under 2.3.68. Selected readings are contextual premises; automatic derivational
and governor-link validation remains incomplete.

Canonical 2.3.69 recognizes a wider typed affix exclusion catalog, distinguishing
tṛn from tṛc. For 2.3.70, `KridantaPratipadika.derivationEvidence` can establish
the specified future aka/in or debt in domain. Future meaning alone and ordinary
aka formation do not establish the prohibition. Evidence generation and complete
derivational-domain validation remain unfinished.

Canonical 2.3.72–73 now preserve explicit instrumental/genitive comparison and
dative/genitive benediction alternatives. They require an established nominal
governor, the relevant meaning, and a śeṣa reading; the relation remains
sambandha. Supported lexical catalogs and automatic governor/sense discovery
remain incomplete. The dative option in 2.3.73 does not inherit an instrumental
option from 2.3.72.

`VakyaAnalyzer` now discovers nominal case-link candidates for the supported
2.3.29/34/72/73 governors across word order. `KriyaFrame.nominalCaseLinks` retains
occurrence indices and missing semantic premises. A unique candidate is used by
the finite-clause rule engine only when all required semantic premises are
already established. Identical competing governors remain separate candidates;
nearness does not resolve them. Nominal clauses also expose the link analysis.
This supplies dependency discovery for this slice, not automatic recognition of
the required comparison, benediction or śeṣa meanings.

### Nominal qualification

### Adverbial qualification

`AdverbialAnalyzer` populates the existing `KriyaFrame.qualifications` with
separate semantic dimensions, functions, scope candidates, morphology, and
source occurrence indices. `कदा/कुत्र/कथम्` are respectively time/place/manner
questions; `न` and `मा` retain distinct negation/prohibition functions.
`प्रत्येकम्` denotes distribution rather than repetition. Relative/correlative
markers retain clause-relation scope rather than a fabricated event timestamp.

Known adverbial stems may also yield a neuter accusative singular case-form
reading, retaining `competingNominalReading`. Arbitrary accusatives do not
receive adverbial semantics. An explicit stem-to-adverb lexicon licenses these
readings; no suffix concatenation is used to guess inflection. The same
classification is retained before or after the verb and across intervening
participants within the clause. A source index is relative to the frame's written
pada list and is paired with its `KriyaId`, preserving repeated occurrences.

Recognized counting-frequency forms retain `repetitionCount`; qualitative
recurrence such as पुनः receives no invented number. 2.3.64 requires an
explicit frequency-to-time link plus temporal/locus and शेष premises. The link
currently enters through `ParticipantFacts.frequencyGovernor`; automatic
attachment is unfinished.

Scope sets are hypotheses. This analysis does not yet lower questions,
correlations, distribution, or relative dates into runtime operations, resolve
multi-clause scope, or select between nominal and adverbial readings. Unknown
avyayas retain `OTHER` with no semantic interpretation. Positional attachment
and word membership alone do not establish an executable meaning.
Degree/intensity words preserve event and constituent scope candidates.
`यत्र` retains relative place scope; `तत्र` keeps its independent place reading
until a clause resolver can establish a relative/correlative pairing.

`AdverbialScopeResolver.resolve` validates an explicitly supplied event,
proposition, or constituent attachment against those scope candidates. Pass the
same ordered `PadaAnalysis` list used to build the qualifications. Targets carry
the frame identity and constituent occurrence index. Missing attachments,
unknown meanings, foreign frames, invalid occurrences, and unlicensed scopes
return `Unresolved`. A competing nominal reading also remains unresolved until
the caller explicitly supplies `nominalReadingExcluded = true` from its contextual
analysis. The resolver does not infer this exclusion from lexical membership or
word order. Clause correlations and collection distribution remain outside this
single-clause attachment API; resolving an attachment does not execute it.

Reference for case forms used adverbially: Whitney, Sanskrit Grammar §1111
([Chapter XVI](https://en.wikisource.org/wiki/Sanskrit_Grammar_%28Whitney%29/Chapter_XVI)).

### Vedic case evidence

2.3.62–63 require an explicit Vedic/Brāhmaṇa `GrammarRegister` and a
participant-local `VedicCaseLicense` with the rule and source reference.
`VakyaAnalyzer` accepts a register; licences currently enter through the
participant/context API. The analyzer does not discover attestations from
surface text or apply बहुलम् as a universal case switch. Competing observed
licensed cases retain separate evidence; ordinary input does not receive an
automatically generated genitive alternative.

### Nominal attachment details

2.3.50 requires an independently established `SESHA_RELATION` interpretation.
Genitive morphology alone retains a सम्बन्ध hypothesis without proving it.
With relational evidence, an ओस् genitive/locative analysis can resolve to
genitive सम्बन्ध. Discovering the relational link and choosing among competing
semantic readings remain separate, unfinished tasks.

Case-dependent alternatives can carry different roles. In prefixed DIV
transactions (2.3.59), relational genitive and object accusative retain
separate evidence; the object branch requires a matching 1.4 rule and respects
whether that object is already expressed by the verb. Compatible observed
branches with different roles leave the relation unresolved.

For 2.3.51–52, `VibhaktiRuleContext` carries the governing dhātu and verb AST.
Specific root/sense and underlying instrument/object premises are required in
addition to the supplied शेष interpretation. These relational readings retain
their specific sūtra provenance instead of being replaced by a verbal kāraka
or the generic 2.3.50 rule. The supported remembering lexicon remains incomplete.

`KarakaRelation.nominalDesignations` records आमन्त्रित (2.3.48) and singular
सम्बुद्धि (2.3.49) separately from address case assignment (2.3.47).
`DIRECT_ADDRESS` is a supplied interpretation, not a lexical property inferred
from a proper noun. Unknown number does not license सम्बुद्धि. Bare nominal
use under 2.3.46 likewise requires an independent interpretation; nominative
morphology alone does not establish it.

Purpose-action government is separate from the finite head. Supply
`VakyaSemanticContext.purposeGovernors` with an explicitly selected
`PurposeActionGovernor.Implied` or `.Written` action and occurrence-local
object meaning. Results appear in `KriyaFrame.purposeKarakas` with their own
action IDs; they do not control finite agreement or enter its argument lookup.
The current verified primary-object slice preserves 1.4.49→2.3.14 for the
omitted purpose action and 1.4.49→2.3.2 for an actual written तुमुन् action.
The purpose/omission reading is supplied, not inferred automatically.

Explicit nominal government is separate from agreement-based attachment.
`ParticipantFacts.governingNominal` supplies an established dependency for
2.3.39–40: the specified governor licenses genitive/locative alternatives,
and 2.3.40 additionally requires the आसेवा sense. These alternatives do not
establish a verbal अधिकरण relation. Automatic dependency discovery and
derived governor identities remain incomplete.

`KriyaFrame.nominalRelations` exposes a `NominalRelationAnalysis` for finite
and nominal clauses. Modifier and head indices refer to `nominalPadas`, so
repeated identical words retain separate identities. Lexical `nominalUse`
licenses modifier candidates; `nominalMeanings` records orthogonal semantic
classes rather than prescribing a fixed Pāṇinian taxonomy.

Ordinary agreement requires a shared gender and a compatible **pair** of case
and number readings. Attachment can run in either direction and cross other
words. Every compatible head is retained; proximity never decides attachment.
Nominal and recognized copular clauses retain a nominative predicative reading
alongside attribution. Substantival readings of flexible adjectives/pronouns
remain explicit in `independentReadings`.

This is conservative candidate generation, not a completed semantic executor.
It does not yet resolve apposition, numeral agreement exceptions, quantifier
scope, reference identity, or coordinated-head agreement. Unknown gender
does not prove agreement. Cardinal/distributive behavior must not be inferred
from ordinary adjective agreement. Existing kāraka participants are retained
until a joint attachment/kāraka resolver can justify grouping them.

Grammatical reference: [Apte, Student's Guide to Sanskrit Composition,
lesson 2](https://www.payer.de/apte/apte02.htm) treats agreement and its
exceptions. Attested nonadjacency is exemplified by
[Bhagavad Gītā 11.5](https://heritage.iitk.ac.in/SBG/11.5), where `रूपाणि`
is separated from its following qualifications by `शतशोऽथ सहस्रशः`.

The `:analysis` module provides:
- **`PadaAnalyzer`**: Analyzes subanta and tiṅanta surface forms into possible morphological interpretations.
- **`VakyaAnalyzer`**: Performs sentence-level Kāraka resolution for syncretic case endings (such as `भ्याम्` and `ओस्`) via 1.4 Kāraka and 2.3 Vibhakti sūtras. Dhātu-valency evidence is distributed only to case-compatible participants; a syncretic form is resolved from that evidence only when its participant is uniquely compatible. Genuine ambiguity is preserved and reported with the underlying sup morphology.
- **Sūtra-evidence invariant**: Morphology proposes kāraka candidates but does not resolve them. A true kāraka requires semantic evidence accepted by a 1.4 kāraka-saṃjñā rule, followed by the applicable 2.3 vibhakti rule. Non-kāraka relations such as सम्बन्ध are established directly by their 2.3 rule (for example, 2.3.50 षष्ठी शेषे).
- **Passive clauses**: In कर्मणि प्रयोग, the independent instrumental agent is established by 1.4.54 and realized by 2.3.18; the desired nominative object is established by 1.4.49 and realized as an अभिहित participant by 2.3.46.
- **Causative clauses**: प्रयोजक and प्रयोज्य evidence is distinct from ordinary agent/object evidence. A causer is established by 1.4.55; a marked causee uses 1.4.52, or optional 1.4.53 with हृ/कृ, while an ordinary desired object remains under 1.4.49. General 1.4.54/1.4.49 rules explicitly defer to these marked causative relations.
- **Dvikarmaka clauses**: Primary `DESIRED_OBJECT` and secondary `SECONDARY_OBJECT` evidence remain distinct. The former uses 1.4.49 and an explicitly marked गौण/अकथित participant uses 1.4.51. When a known dvikarmaka root has multiple otherwise unannotated accusatives, each retains competing 1.4.49/1.4.51 provenance and a warning instead of being silently assigned a primary/secondary order.
- **Optional provenance and branches**: Genuine optional designations (1.4.44 and 1.4.53) retain both directions. The primary branch and each alternative carry a separate optional decision. Designation evidence always cites the rule actually establishing that role: declining 1.4.53 retains 1.4.54→2.3.18, and declining 1.4.44 retains 1.4.42→2.3.18. A uniquely compatible case selects the corresponding branch; both or neither compatible preserves uncertainty. `primaryOptionalDecision` describes the primary analysis branch, not a settled choice when the result remains unresolved.
- **Simultaneous names**: 1.4.43's च grants कर्म and करण together. `coexistingKarakas` preserves both names; `COEXISTENT_CASE` describes another case projection, not a declined designation. Expression of the shared power through one name expresses the other too, preventing an extra passive instrumental. A case that remains unvalidated cannot supply an execution operand merely from the primary projection.
- **Ellipsis safety**: An unresolved frame relation retains its morphological candidates. Utterance-level shared-agent inference is blocked only by an unresolved participant that could actually be `KARTR`, rather than by every unrelated unresolved nominal. A `SharedParticipant` link records the shared kāraka plus the exact frames where the participant is explicit and where it is reconstructed as omitted.
- **Semantic provenance**: Participant premises are retained as `SemanticRelationEvidence`, distinguishing lexical-entry facts, participant-specific dhātu valency, voice/construction hypotheses, nominal categories, unambiguous morphology, and unresolved construction ambiguity. The selected 1.4 `KarakaEvidence` carries these premises, so a trace exposes both the sūtra and why its semantic condition was considered true.
- **Uṇādi Integration**: Automatically enriches nominal stem parses in sentences with Uṇādi etymological stem analyses.
