# Metaeffekt Kontinuum

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

See also the use cases detailed in [{metæffekt} bom essentials)](https://github.com/org-metaeffekt/metaeffekt-bom-essentials?tab=readme-ov-file#sbom-use-cases).

## Processors

To enable broad compatibility the kontinuum provides all processors as .xml files, which can be executed via the
maven build tool. Every processor defines the steps necessary to execute a specific goal, as well as the configuration
parameters required to successfully run it.

Multiple processors can be combined to define pipelines and workflows for integration into larger projects. The {metæffekt}
kontinuum is also supplied as [GitLab componeonts](https://gitlab.opencode.de/metaeffekt/metaeffekt-components).

For further details see [processors](processors/README.md).

## Stages

Each process is sorted into one of many stages. All stages are numbered as to easily recognize in what order
all processors should be run as later stages may require results from earlier stages to produce satisfactory results. 
The order inside a single stage usually does not matter.

### Workspace

A workspace is a set directory structure which groups software assets into the aforementioned stages and additionally
dividing them into parts if required. Its purpose is to store inputs and outputs for stage and every asset. The workspace
is not a separate repository or project and will automatically be created when executing any of the included
test scripts or when using CI/CD components provided by [metaeffekt-components](https://gitlab.opencode.de/metaeffekt/metaeffekt-components).

``` text
.
└── workspace/
└── asset/
├── xx_additional/
│   └── asset-part/
│       └── inputs / outputs
├── 00_fetched/
│   └── asset-part/
│       └── software (as is)
├── 01_extracted/
│   └── asset-part/
│       └── software extract
├── 02_prepared/
│   └── asset-part/
│       └── software extract inventory
├── 03_aggregated/
│   └── asset-part/
│       └── filtered extracted inventories
├── 04_resolved/
│   └── asset-part/
│       └── inventories containing resolved artifacts
├── 05_scanned/
│   └── asset-part/
│       └── inventories containing licensing / copyright info
├── 06_advised/
│   └── asset-part/
│       └── inventories containing vulnerability info
├── 07_grouped/
│   └── asset-part/
│       └── sorted inventories for report generation
├── 08_reported/
│   └── asset-part/
│       └── PDF reports and dashboards
└── 09_summarized/
└── asset-part/
└── summary report
```

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

To execute different workflows CI/CD components have been made available for use with GitLab pipelines and / or 
GitHub workflows. An example of how to use these components can be seen at [metaeffekt-gitlab-examples](https://gitlab.opencode.de/metaeffekt/metaeffekt-examples)

Generally speaking any templates or examples created by {metæffekt} meant as a reference for custom projects will be 
available in a public repository while other data is stored increasingly more private and secure, depending on 
the nature of the data.

## Workspace