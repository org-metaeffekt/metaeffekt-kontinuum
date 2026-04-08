# Kontinuum Tools

This directory scaffolds a unified processor execution stack with one shared core and two backends:

- `execution-local`: direct Maven execution for standalone use.
- `execution-container`: transport adapter for a running container wrapper.

Both backends consume the same request model from `execution-core`, so guided validation and execution planning stay consistent.

## Modules

- `execution-contract`: immutable request/response and processor metadata contracts.
- `execution-core`: catalog loading, required-parameter validation, and Maven command planning.
- `execution-local`: `ProcessBuilder` backend for local Maven execution.
- `execution-container`: backend that forwards execution plans through a client interface.
- `shell-cli`: Spring Boot Shell app wired to the shared core.

## Current status

This is an architecture-first bootstrap. It already supports local execution of a processor with normalized input and dry-run support. The container backend is wired through a client interface and can be connected to HTTP/gRPC in the next step.

## Build

```bash
mvn -f tools/pom.xml test
```

## Processor Test Scaffold (JUnit)

The scaffold includes parameterized JUnit coverage for all processors in `processors/processors.yaml`:

- `AllProcessorsDryRunExecutionTest`: builds execution plans for every processor with generated required properties.
- `ProcessorCatalogContractTest`: validates catalog-level assumptions.

Useful commands:

```bash
# default: runs the all-processor dry-run scaffold
mvn -f tools/pom.xml test
```
