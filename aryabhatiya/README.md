# Module `:aryabhatiya`

Āryabhaṭīya consonant-vowel numerical cipher decoder.

## Overview

Implements the numerical encoding system described in Āryabhaṭa's *Āryabhaṭīya* (Gitikāpāda 2):
- **Varga consonants** ($vargas$: `क्` to `म्` = 1 to 25).
- **Avarga consonants** ($avargas$: `य्` to `ह्` = 30 to 100).
- **Vowels** ($swaras$: `अ`, `इ`, `उ`, `ऋ`, `लृ`, `ए`, `ऐ`, `ओ`, `औ` representing powers of 100).
- **Example**: `ख्युघृ` = $2 \times 10^4 + 30 \times 10^4 + 4 \times 10^6 = 4,320,000$.
