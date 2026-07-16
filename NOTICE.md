# Dhātupāṭha data attribution

The generated gaṇa catalogues under
`src/main/kotlin/dev/sanskrit/dhatupatha/*Dhatus.kt` were imported from the
`ashtadhyayi-com/data` Dhātupāṭha dataset:

- Source: <https://github.com/ashtadhyayi-com/data/blob/master/dhatu/data.txt>
- Imported fields: `aupadeshik`, `baseindex`, `gana`, `pada`, `artha`, and
  `artha_hindi`.
- Licence statement: the `vidyut-prakriya` project records that the
  ashtadhyayi.com Dhātupāṭha was shared under the MIT licence:
  <https://docs.rs/crate/vidyut-prakriya/0.1.0/source/data/README.md>

The generated catalogues cover all ten traditional gaṇas:

- `BhvadiDhatus.kt`
- `AdadiDhatus.kt`
- `JuhotyadiDhatus.kt`
- `DivadiDhatus.kt`
- `SvadiDhatus.kt`
- `TudadiDhatus.kt`
- `RudhadiDhatus.kt`
- `TanadiDhatus.kt`
- `KryadiDhatus.kt`
- `CuradiDhatus.kt`

Each catalogue preserves the source upadeśa, Sanskrit and Hindi meanings,
gaṇa, and pada information. English glosses and normalized derivational
surfaces are project-maintained additions. Do not edit individual generated
Dhātupāṭha entries by hand; regenerate the catalogues from the cited source.
