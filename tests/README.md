# Processor tests

Each canonical leaf processor POM under `processors/<domain>/` has a small Bash test at
`tests/processors/<domain>/<processor>.sh`. A test supplies its own non-secret parameters, invokes its processor with
Maven's `process-resources` phase, and checks that its configured outputs were created.

## Run tests

Run an individual processor test directly:

```bash
bash tests/processors/inventory/scan-artifact-directory.sh
```

Run all processor tests via the runner:

```bash
bash tests/pipelines/run-all.sh
```

The runner invokes every processor test, continues past failures, reports them, and exits nonzero if any failed. Missing
required services, credentials, configuration, or input files are failures; tests are not silently skipped.

## Inputs, configuration, and outputs

- Shared input fixtures are stored under `tests/resources/<category>/` (for example,
  `tests/resources/inventories/sample-asset-001.xlsx`). There are no workspace trees in the new test structure.
- Processor parameters are specified in the corresponding test script. Secrets and environment-specific values are
  loaded from `.local.properties` by `tests/processors/shared.sh`; values are never logged by the helper.
- Each invocation creates a unique output directory under `tests/target/<domain>/<processor>/`. Tests do not clear
  `tests/target`; delete it manually when you no longer need the outputs.
- A test asserts the existence of every output it configures. Each processor POM is bound so its work runs when Maven
  is invoked with `process-resources`.

## Shared helper

`tests/processors/shared.sh` provides repository paths, `.local.properties` loading, unique output-directory creation, the
Maven invocation, configuration checks, and output assertions. It does not seed workspaces or remove generated data.

## Transition from the previous test tree

The former case scripts and workspace-based runners have been removed. Processor tests are standalone and read their
inputs directly from `tests/resources/`; no fixtures are copied at run time. Pipeline callers live under
`tests/pipelines/`.
