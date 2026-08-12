# Module `:idea-plugin`

IntelliJ IDEA plugin integration for PaniniVM.

## Overview

Provides IDE integration features:
- `.pvm` script syntax highlighting and file type recognition.
- Sūtra reference navigation and derivation inspection.
- Live saṃjñā-kriyā signature diagnostics for duplicate declarations, argument
  arity/type errors, unknown or missing named arguments, incompatible typed
  pipeline stages, and unresolved structured result schemas.

## Install the plugin from disk

### 1. Build the plugin artifact

From the PaniniVM repository root, build the plugin JAR and its required
runtime contents:

```sh
./gradlew :idea-plugin:jar
```

On Windows:

```powershell
.\gradlew.bat :idea-plugin:jar
```

The installable artifact is generated at:

```text
idea-plugin/build/libs/idea-plugin.jar
```

Use the JAR from `build/libs`, not a JAR from `build/tmp`, an individual module
dependency, or the source directory. Do not extract the JAR before installing
it. The build task packages the PaniniVM runtime dependencies and
`META-INF/plugin.xml` into the artifact expected by the JetBrains plugin
loader.

### 2. Open the plugin settings

In IntelliJ IDEA or Android Studio:

1. Open **Settings** on Windows/Linux or **Preferences** on macOS.
2. Select **Plugins**.
3. Open the gear menu at the top of the **Installed** tab.
4. Select **Install Plugin from Disk…**.

You do not need to search the JetBrains Marketplace when installing a locally
built PaniniVM plugin.

### 3. Select the built JAR

Choose:

```text
<PaniniVM repository>/idea-plugin/build/libs/idea-plugin.jar
```

Confirm the selection. The IDE should show **PaniniVM Language Support** in the
installed-plugin list. Accept the installation prompt and restart the IDE when
requested.

### 4. Verify the installation

After restart:

1. Open **Settings/Preferences → Plugins → Installed**.
2. Confirm that **PaniniVM Language Support** is present and enabled.
3. Open this repository or another project containing a `.pvm` file.
4. Confirm that the file has PaniniVM syntax highlighting and editor
   diagnostics.
5. Open a runnable `.pvm` file and use its gutter icon to select **Run via VM**
   or **Run via CLI Process**.

For an interactive verification, open
`projects/number-guessing-game/number_guessing_game.pvm`, choose **Run via CLI
Process**, click inside the Run console, enter a number, and press Enter.

### Updating a local installation

After changing plugin or runtime code:

1. Rebuild with `./gradlew :idea-plugin:jar`.
2. Open **Settings/Preferences → Plugins → Installed**.
3. Use **Install Plugin from Disk…** again and select the newly built JAR.
4. Confirm replacement of the existing plugin and restart the IDE.

Rebuilding the JAR alone does not update an already running IDE; the plugin is
loaded from the IDE's plugin directory during startup.

### Troubleshooting

- **The JAR cannot be selected:** verify that the build completed and that you
  selected `idea-plugin/build/libs/idea-plugin.jar` without extracting it.
- **The plugin is installed but `.pvm` files remain plain text:** verify that
  the plugin is enabled, restart the IDE, and check that `.pvm` is not manually
  associated with **Text** under **Editor → File Types**.
- **The IDE reports an incompatible plugin:** use an IntelliJ Platform-based IDE
  compatible with the platform API used by this build, or update the plugin's
  IntelliJ Platform dependencies before rebuilding.
- **Old behavior remains after rebuilding:** reinstall the new JAR from disk
  and restart. If necessary, uninstall the old PaniniVM plugin, restart, then
  install the rebuilt artifact.
- **Interactive input appears to wait forever:** select **Run via CLI Process**
  and enter input in the Run tool window. Do not launch an interactive program
  through Gradle's `:cli:run` console proxy.

## Running interactive scripts

Run a `.pvm` file from its gutter icon and select **Run via VM**. The Run tool
window uses the same interactive execution pipeline as the CLI. When a script
executes `ग्रह्`, click the console, enter the requested value, and press Enter.
Typed number, boolean, and choice validation, capability approvals, `:cancel`,
explicit-output filtering, rollback, and exit codes therefore match terminal
execution.

**Run via CLI Process** starts `dev.panini.MainKt` in an isolated Java process and
forwards Run-console input directly, avoiding Gradle's Windows stdin buffering.
The Stop action closes pending input and terminates the child process.
