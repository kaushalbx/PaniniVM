# Module `:analysis`

Grammatical analyzer for surface padas and sentence-level vākya structures.

## Overview

The `:analysis` module provides:
- **`PadaAnalyzer`**: Analyzes subanta and tiṅanta surface forms into possible morphological interpretations.
- **`VakyaAnalyzer`**: Performs sentence-level Kāraka resolution, resolving syncretic case endings (such as `भ्याम्` across तृतीया, चतुर्थी, पञ्चमी) via 1.4 Kāraka and 2.3 Vibhakti sūtras.
- **Uṇādi Integration**: Automatically enriches nominal stem parses in sentences with Uṇādi etymological stem analyses.
