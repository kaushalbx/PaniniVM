# PVM naturalness review

Every sentence pattern in the example and project sources, 102 shapes grouped by verb and case,
was read for three things: is it grammatical, would a writer say it this way, and is there a model
for it? Programming has no attested Sanskrit, so judgments about programming meaning are reasoned,
and they need review by a Sanskritist.

## Changed

| Before | After | Why |
|---|---|---|
| `त्रीणि द्वौ च गणय` for multiplication | `गुणय` | `गण` is `सङ्ख्याने`, counting. The Līlāvatī's multiplication rule uses `guṇya`, `guṇaka` and `guṇite`, so the family is sourced. The imperative `गुणय` is a regular form from it, not itself confirmed. |
| One verb meaning multiply or count by data type | `गुण` multiplies, `गण` counts | A verb's meaning should not depend on runtime values. |
| `धारणाय गुणय` to store a result | `गुणय ।` then `फलं धारणे स्थापय ।` | The plain sentence says what happens. The purpose reading stays valid grammar. |
| `द्वि + ङसिँ` | `द्वि + भ्याम्` | `द्वि` is dual only. It was the one wrong numeral among 245 scanned. |
| A list of subject words for equational clauses | First nominative | That list was a program choice, not grammar. |

## Changed by grammatical decision

| Before | After | Why |
|---|---|---|
| `सूचीं क्राम` for sorting | `सूचीम् अनुक्रमय` | `क्रमु` is `पादविक्षेपे`, so the plain verb says "step over the list". The causative with `अनु` says "put it in sequence", as in `अनुक्रम`. `क्रम्` is an `अमन्त` root, so `मित्`, and stays short by 6.4.92; the commentaries have `संक्रमय्य`. |
| `सूचीं प्रतिक्राम` | `सूचीं प्रतिक्रमय` | The same reasoning for reverse sequence. |

## Reviewed and kept

- **`द्वौ त्रीन् च योगेन कुरु`, procedure calls:** this has the structure of the idiom `विधिना कुरु`, "do it by the rule", with procedure as means. A genitive form, `द्वयोः त्रयाणां च योगं कुरु`, is arguably better for arithmetic procedures. It is not adopted because genitives already mark domains and named arguments.
- **`एकस्य द्वयोः त्रयाणां च सूची अस्ति`:** natural, as is `क्रमो नाम … सूची अस्ति`.
- **`सङ्ख्यावतः मूल्यं पञ्चाशत् अस्ति`:** a natural equational sentence. Using a statement of fact to create a structure is a language convention.
- **`योजय`, `वियोजय`, `मुद्रय`, `स्थापय`, `गृहाण`, `विनिमयस्व`:** ordinary, with their cases.

## Decided

These are the auditor's grammatical judgments, recorded with their basis.

- **`एधय`, `वेदय`, `मूलय`, `विशिष`:** kept. `एध्` is `वृद्धौ`, `वेदय` is "make known", `मूलय` is a denominative from the mathematical `मूल`, and `विशिष्` is "distinguish".
- **Equational subject:** the first nominative is the subject, since `उद्देश्य` ordinarily precedes `विधेय`. Paramalaghumañjūṣā attests that structure; the word-order rule is usage, not a stated sūtra, so a reversed sentence can read differently.

## Not yet settled

- **`Program*Construction` files:** these give programs readings ordinary Sanskrit does not get. Objects and agents of imperatives match natural usage, and procedure calls are a program convention. Unambiguous genitives no longer need one: ordinary analysis reads them under 2.3.50. Only the syncretic `ओस्` dual in a declaration remains.
