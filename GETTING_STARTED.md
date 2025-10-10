# Getting Started

Before proceeding, please review the top-level [README](README.md) to understand this repository's purpose and intended 
use cases.

## Repository Structure

This repository is organized into two main sections: **processors** and **tests**.

The **processors** section contains Maven POM files that execute predefined parameterized steps. Each processor 
represents a specific isolated workflow or task. For detailed information about each processor's capabilities, usage 
instructions, and expected results, refer to the processor-specific README files.

The **tests** section provides basic test infrastructure for:
- Testing individual processors
- Testing processor sequences through pipeline scripts
- Demonstrating how to call each processor with available parameters
- Showing how to connect processors into custom pipelines

## Scripts and Cases

To better understand processor execution and requirements, we recommend reviewing the 
[Tests-Documentation](tests/README.md) before continuing.

## Prerequisites

To ensure all reference processes and pipelines in this repository can run, we need to create an external.rc file
in the root of this repository. A template for this file with additional hints and details has been provided here: 
[external-template.rc](external-template.rc).

## Running the Reference Pipeline

To get started with executing processors, you can either:

1. Run the [`run_workspace-001.sh`](tests/scripts/pipelines/run-all.sh) pipeline script (produces all available results but may take several minutes)
2. Run any other pipeline script from the [`tests/scripts/pipelines`](tests/scripts/pipelines) directory

**Note:** Scripts can be executed from any directory. All required resources for processor execution are included in 
this repository. The only additional requirement is a local instance of our vulnerability mirror (a public version will 
be available in the future).

### Running Without a Vulnerability Mirror

Since only two processors currently require the vulnerability mirror, you can omit these processors from your pipeline:

1. Copy the [`run_workspace-001.sh`](tests/scripts/pipelines/run-all.sh) script
2. Remove the following entries from the underlying pipelines:
   ```bash
   sh "$PROCESSOR_SCRIPTS_DIR/util/util_update-mirror.sh"
   sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh"
   sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh"
   ```
3. Run the newly created pipeline script


## Running a Single Processor

To execute an individual processor, run the corresponding script in the [`processors`](tests/scripts/processors) directory. Note that some 
rocessors may require a vulnerability mirror instance.

## Results

All processor results are stored in the [`target`](tests/target) directory. The target structure mirrors the *metaeffekt-space* 
organization, where:
- Each workspace contains multiple products
- Each product contains multiple phases
- The phases correspond to those in the [`processors`](processors) directory

## Creating Custom Implementations

While this repository is not designed as a custom workbench for executing processors with arbitrary resources and 
parameters, it is technically possible. To create a custom script or processor execution:

1. Create a new "case script" defining all necessary parameters
2. Call the processor script with your case as a parameter:
   ```bash
   sh your-processor.sh -c /path/to/your/case.sh
   ```
