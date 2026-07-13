# Derivation Sutra Roadmap

This project is organized so Paninian sutras can be added one at a time.

## Current scope

The current engine handles a small external sandhi subset at pada boundaries:

- svara + svara transformations
- final visarga before `khar` vyanjanas derived by `PratyaharaEngine`
- sutra tracing with sutra numbers
- typed sutra metadata with krama, vaikalpika status, adhyaya, pada, and sutra type
- typed Maheshvara Sutra tokens with real varnas separated from it markers

## Next sutra groups

1. Svara sandhi completion
   - vrddhi sutras
   - purvarupa and pararupa cases
   - pragrihya exceptions

2. Visarga sandhi
   - `रामः गच्छति -> रामो गच्छति`
   - sibilant-specific variants
   - pause and pada-end handling

3. Vyanjana sandhi
   - `स्तोः श्चुना श्चुः`
   - जश्त्व
   - अनुस्वार and परसवर्ण

4. Sutra governance
   - asiddha/asiddhavat ordering
   - vaikalpika sutras
   - environment-specific blocking sutras

5. Akshara tokenization
   - independent svaras
   - vyanjana + virama
   - matra normalization
   - anusvara, visarga, Vedic accent signs

## Design sutra

Each sutra should include:

- sutra number
- readable sutra name
- `SutraType`
- krama
- focused unit tests
- notes for known exceptions or blocked environments
