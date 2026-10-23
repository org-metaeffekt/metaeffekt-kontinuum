# Getting Started

Before proceeding, please review the top-level [README](README.md) to understand this repository's purpose and intended 
use cases.

## Repository Structure

This repository is organized into two main sections: **processors** and **tests**.

The **processors** section contains XML files that execute predefined parameterized steps. Each processor represents a 
specific isolated workflow or task. Processors are grouped by domain (for example `artifacts`, `assessment`, `bom`, 
`inventory`, `licensing`, `mirror`, `portfolio-manager`, `report`, `vulnerability`). For detailed information about each 
processor's capabilities, usage instructions, and expected results, refer to the processor-specific `.md` file next to 
its `.xml`.

The **tests** section provides a small, standalone test per processor:
- Testing individual processors
- Demonstrating how to call each processor with its parameters
- Showing how the processors can be combined into custom pipelines

Each test lives at `tests/processors/<domain>/<processor>.sh`, sources the shared helper `tests/processors/shared.sh`, 
and references shared fixtures under `tests/resources/<category>/` directly (it never copies them).

## Prerequisites

To ensure all reference processes in this repository can run, a .local.properties file
must be available in the root of this repository or in the encompassing integration harness. A template for this file with additional hints and details has been provided here: 
[.local.properties.template](.local.properties.template).

Depending on which processors will be run, an instance of the [metaeffekt-workbench](https://github.com/org-metaeffekt/metaeffekt-workbench)
might be required and checked out locally. Some processors additionally require a local instance of the vulnerability 
mirror which can be generated via the [update-index.xml](processors/mirror/update-index.xml) processor.

## Running the Processor Tests

To get started with executing processors:

1. Run the [run-all.sh](tests/pipelines/run-all.sh) runner to execute every processor test. It continues past failures, 
   reports the ones that failed, and exits nonzero if any failed. Depending on your environment this may take several 
   minutes and may fail for processors whose external configuration (vulnerability mirror, workbench, 
   portfolio-manager, TMD credentials) is unavailable.
2. Run an individual processor test from [`tests/processors`](tests/processors) (see below).

**Note:** Scripts can be executed from any directory.

### Running Without a Vulnerability Mirror

Only some processors require the vulnerability mirror. Since there is no separate mirror-free pipeline, either:

1. Run only the processor tests that do not depend on the mirror, or
2. Copy [run-all.sh](tests/pipelines/run-all.sh) and remove the mirror-dependent entries from its `PROCESSOR_TESTS` list 
   before running it.

The tests that need a mirror are the `mirror/update-index*.sh`, `report/create-assessment-dashboard.sh`, 
`report/create-report.sh`, and `vulnerability/enrich-*.sh` tests; `mirror/download-index.sh` needs a mirror archive URL 
instead.

## Running a Single Processor

To execute an individual processor, run its test script in `tests/processors/<domain>/`. For example:

```bash
bash tests/processors/inventory/scan-artifact-directory.sh
```

Note that some processors may require a vulnerability mirror instance.

All processor results are stored in the [`target`](tests/target) directory, under 
`tests/target/<domain>/<processor>/`.

## Creating Custom Implementations

While this repository is not designed as a custom workbench for executing processors with arbitrary resources and 
parameters, it is technically possible. To create a custom processor execution:

1. Copy an existing test script from `tests/processors/<domain>/` as a starting point and adjust its parameters and 
   assertions.
2. Or invoke a processor POM directly with Maven, passing the documented `param.`, `input.`, `output.`, and `env.` 
   properties:

   ```bash
   mvn -f processors/inventory/scan-artifact-directory.xml process-resources \
     -Dinput.extract.dir=/path/to/extracted \
     -Doutput.scan.dir=/path/to/scan \
     -Doutput.inventory.file=/path/to/inventory.xls \
     -Dparam.reference.inventory.dir=/path/to/reference
   ```

This repository is mainly meant to be used in conjunction with the [metaeffekt-workbench](https://github.com/org-metaeffekt/metaeffekt-workbench)
or via CI/CD components like the [metaeffekt-components](https://gitlab.opencode.de/metaeffekt/metaeffekt-components).
