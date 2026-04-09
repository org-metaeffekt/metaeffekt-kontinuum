# Kontinuum Tools

This directory scaffolds a unified processor execution stack with one shared core and two backends.

Both backends consume the same request model from `execution-core`, so guided validation and execution planning stay consistent.

## Modules

- `execution-contract`: immutable request/response and processor metadata contracts.
- `execution-core`: catalog loading, required-parameter validation, and Maven command planning.
- `execution-environment`: grouping module for runtime backends.
- `execution-local`: `ProcessBuilder` backend for local Maven execution.
- `execution-container`: backend that forwards execution plans through a client interface.
- `shell-cli`: Spring Boot Shell app wired to the shared core.

## Current status

This is an architecture-first bootstrap. It supports:

- local execution of a processor with normalized input and dry-run support,
- a container backend interface that can be connected to HTTP/gRPC,
- JUnit scenario coverage for all processors in `processors/processors.yaml`.

## Build

```bash
mvn -f tools/pom.xml test
```

## Processor Test Scaffold (JUnit)

The scaffold includes:

- `AllProcessorsDryRunExecutionTest` in `execution-core`: builds execution plans for all catalog processors.
- `ProcessorCatalogContractTest` in `execution-core`: validates catalog assumptions (ID uniqueness, optional strict POM-path checks).
- `ProcessorScenarioCoverageTest` in `execution-local`: enforces that every catalog processor has a test scenario.
- `ProcessorFunctionalExecutionTest` in `execution-local`: runs one test per processor scenario (`PLAN_ONLY` dry-run by default, `EXECUTE` for scenarios with local fixtures).

Useful commands:

```bash
# full tools reactor tests
mvn -f tools/pom.xml test

# strict catalog validation (fails on stale yaml -> pom references)
mvn -f tools/pom.xml test -Dkontinuum.verifyCatalogPomPaths=true

# run only local functional scenario tests
mvn -f tools/execution-local/pom.xml test
```
