# {metæffekt} kontinuum

The {metæffekt} kontinuum repository provides a series of base-configurations to support using the {metæffekt} plugins, 
tools and content. All configurations can be found in the [processors](processors) directory with their respective
documentation.

## Getting started

To quickly get setup and work with the {metæffekt} kontinuum or integrate it into other projects, take a look at the
[Getting Started](GETTING_STARTED.md) documentation.

## Use Cases

The {metæffekt} kontinuum supports a set of use cases in the following fields:
* Software Composition Analysis
* License and Copyright Scanning
* Vulnerability Correlation, Monitoring, and Assessment
* Creating Compliance Artifacts such as License Documentation and different types of Vulnerability Reports

See also the use cases detailed in [{metæffekt} bom essentials](https://github.com/org-metaeffekt/metaeffekt-bom-essentials?tab=readme-ov-file#sbom-use-cases).

## Processors

To enable broad compatibility the kontinuum provides all processors as .xml files, which can be executed via the
maven build tool. Every processor defines the steps necessary to execute a specific goal, as well as the configuration
parameters required to successfully run it.

Multiple processors can be combined to define pipelines and workflows for integration into larger projects. Reusable
CI/CD components for GitLab are available in the
[metaeffekt-components](https://gitlab.opencode.de/metaeffekt/metaeffekt-components) repository.

For further details see [processors](processors/README.md).

## Stages

Processors are grouped into numbered stages to indicate their usual order. Later stages may depend on results from
earlier stages, while the order of processors within a stage usually does not matter.

### Workspace

A workspace is a directory structure that groups software assets into stages and, where needed, divides them into
asset-parts. It stores the inputs and outputs for each stage and asset. A workspace is not a separate repository or
project. The included test scripts and the CI/CD components in
[metaeffekt-components](https://gitlab.opencode.de/metaeffekt/metaeffekt-components) create or populate their workspaces.

``` text
.
└── workspace/
    └── asset/
        ├── xx_additional/
        │   └── asset-part/
        │       └── auxiliary inputs / outputs
        ├── 00_fetched/
        │   └── asset-part/
        │       └── fetched software
        ├── 01_extracted/
        │   └── asset-part/
        │       └── extracted software
        ├── 02_prepared/
        │   └── asset-part/
        │       └── prepared inventories
        ├── 03_aggregated/
        │   └── asset-part/
        │       └── aggregated inventories
        ├── 04_resolved/
        │   └── asset-part/
        │       └── inventories with resolved artifacts
        ├── 05_scanned/
        │   └── asset-part/
        │       └── inventories with licensing and copyright data
        ├── 06_advised/
        │   └── asset-part/
        │       └── inventories with vulnerability data
        ├── 07_grouped/
        │   └── asset-part/
        │       └── inventories grouped for reporting
        ├── 08_reported/
        │   └── asset-part/
        │       └── reports and dashboards
        └── 09_summarized/
            └── asset-part/
                └── summary reports
```

This is a representative layout; a pipeline may use only some stages or add input directories such as `01_assets` and
`00_portfolio`.
    
## Integration

Integration of the {metæffekt} processors is manifold. The following diagram illustrates the anticipated integration
scenarios on repository level.

![](docs/kontinuum-overview.png)

The {metæffekt} kontinuum provides an interface to execute {metæffekt} plugins and tools. The necessary resources
and configurations are deposited in a [{metæffekt} workbench](https://github.com/org-metaeffekt/metaeffekt-workbench) 
project which, depending on the information contained
within, can either be public / private on a remote repository such as GitHub or privately hosted at {metæffekt}.
Any additional requirements not covered by the workbench will be contained in a workbench-extension project, which
can be a locally hosted customer controlled repository.

Reusable CI/CD components are available for GitLab pipelines. An example of their use is available in
[metaeffekt-examples](https://gitlab.opencode.de/metaeffekt/metaeffekt-examples).

Generally speaking any templates or examples created by {metæffekt} meant as a reference for custom projects will be 
available in a public repository while other data is stored increasingly more private and secure, depending on 
the nature of the data.
