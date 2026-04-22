# Kontinuum Tools

The Kontinuum Tools provide a Spring Shell-based CLI for executing Maven processors in an interactive manner. The shell allows users to list, inspect, and run processors with a user-friendly interface.

## Architecture

The tools module consists of three sub-modules:

| Module      | Description                                                                       |
|-------------|-----------------------------------------------------------------------------------|
| `models`    | Data models for processor definitions, YAML parsing, and configuration management |
| `execution` | Maven execution backend for building and running processor commands               |
| `shell-cli` | Spring Shell CLI application providing the interactive interface                  |

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Access to the Kontinuum repository root (for locating `processors/processors.yaml`)

## Building / Running

Build all modules from the `tools/` directory:

```bash
mvn clean install
```

Option 1: Using Maven 

```bash
mvn -f shell-cli/pom.xml spring-boot:run
```

Option 2: Using the Executable JAR

```bash
# Build the executable JAR (includes all dependencies)
mvn -f shell-cli/pom.xml clean package -DskipTests

# Run the JAR
java -jar shell-cli/target/shell-cli-0.1.0-SNAPSHOT.jar
```

## Shell CLI

### processor list

Lists all known processors with their index and ID.

```
shell:>processor list
[0] Aggregate Annex Folders - aggregate-annex-folders
[1] Aggregate Sources - aggregate-sources
[2] Attach Metadata - attach-metadata
...
```

### processor show

Shows the required and optional parameters for a specific processor.

```
shell:>processor show --processor-id enrich-inventory
enrich-inventory
pom: processors/advise/advise_enrich-inventory.xml
goal: process-resources

required:
 - input.inventory.file
   The input inventory file which will be enriched.
 - output.inventory.file
   The file of the resulting output inventory.
...

optional:
 - param.activate.msrc
   Determines if msrc is enabled.
...
```

### processor run

Executes a processor with the specified parameters. The processor can either be executed via its id or index number shown
via the "processor list" command.

#### Interactive Mode

```
shell:>processor run --processor-id enrich-inventory
```

The shell will prompt for each parameter:
```
[input.inventory.file]
The input inventory file which will be enriched.
(required): /path/to/input.json

[output.inventory.file]
The file of the resulting output inventory.
(required): /path/to/output.json
```

#### Using a Configuration File

```
shell:>processor run --configuration enrich-inventory-20260421-124452.yaml
```

#### Options

| Option            | Description                                               | Default |
|-------------------|-----------------------------------------------------------|---------|
| `--processor-id`  | The processor ID or index (required for interactive mode) | -       |
| `--configuration` | Path to a saved configuration file                        | -       |
| `--dry-run`       | Enable dry-run mode                                       | `false` |
| `--debug`         | Enable Maven debug flag (`-X`)                            | `false` |

## Configuration Files

After each successful interactive run, the configuration is automatically saved to `processors/configurations/` with a timestamp:

```
processors/configurations/
├── enrich-inventory-20260421-124452.yaml
├── attach-metadata-20260421-130100.yaml
└── ...
```

Configuration files use the same YAML format as `processors/processors.yaml` but include the values used during execution:

```yaml
processors:
  - name: Enrich Inventory
    id: enrich-inventory
    description: |
      This process takes an input inventory and enriches it...
    pomLocation: advise/advise_enrich-inventory.xml
    goal: process-resources
    parameters:
      - key: input.inventory.file
        description: The input inventory file which will be enriched.
        required: true
        value: /path/to/input.json
      - key: output.inventory.file
        description: The file of the resulting output inventory.
        required: true
        value: /path/to/output.json
```

While these files are created automatically after each interactive run, they can also be created manually for easier / faster
configuration of single processes. To create your own configuration file simply copy the relevant processor from the
`processors/processors.yaml` and fill in the desired values as shown in the example above.


## Example Workflow

1. **List available processors:**
   ```
   shell:>processor list
   ```

2. **Inspect a processor:**
   ```
   shell:>processor show --processor-id enrich-inventory
   ```

3. **Run the processor (interactive):**
   ```
   shell:>processor run --processor-id enrich-inventory
   ```

4. **Re-run with saved configuration:**
   ```
   shell:>processor run --configuration enrich-inventory-20260421-124452.yaml
   ```

5. **Run with debug output:**
   ```
   shell:>processor run --configuration enrich-inventory-20260421-124452.yaml --debug
   ```

## Local Configuration

### project.properties

The `project.properties` file in the `tools/` directory is automatically loaded at startup and its values are set as system properties. This file contains project-specific settings:

```properties
kontinuum.dir=/path/to/kontinuum
workbench.dir=/path/to/workbench

ae.core.version=0.153.2
ae.artifact.analysis.version=0.156.3
```

The properties are loaded regardless of how the application is run (Maven or standalone JAR). When packaged as a JAR, the `project.properties` file is included in the classpath.

### external.rc

The application also expects an `external.rc` file at the repository root. See `external-template.rc` for the required format.
