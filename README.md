# {metæffekt} Kontinuum

The {metæffekt} Kontinuum repository aggregates baseline configurations to support using the 
{metæffekt} plugins, tools and content.


## Getting started

For getting started see [Getting Started](GETTING_STARTED.md). The readme provides some basic steps using the repository.


## Use Cases

{metæffekt} Kontinuum supports a set of use cases in the following fields:
* Software Composition Analysis
* License and Copyright Scanning
* Vulnerability Correlation, Monitoring, and Assessment
* Creating Compliance Artifacts such as License Documentation and different types of Vulnerability Reports

See also the use cases detailed in [{metæffekt} BOM Essentials)](https://github.com/org-metaeffekt/metaeffekt-bom-essentials?tab=readme-ov-file#sbom-use-cases).


## Processors

To enable broad adoption the Kontinuum uses Maven as an integration level. Yet, for specific tasks, isolated
task definitions are provided as individual .xml files in the `processors` folder. Every .xml file defines
a process step in analogy to an isolated workflow engine task.

On the integration level the processors can be combined to define pipelines and workflows. The {metæffekt} Kontinuum
are also supplied as GitHub actions and GitLab components, while this is currently work in progress.

For further details see [Processors](processors/README.md).


## Integration

Integration of the {metæffekt} processors is manifold. The following diagram illustrates the anticipated integration
scenarios on repository level.

![](docs/concept_kontinuum-workbench.png)

The {metæffekt} Kontinuum provides the infrastructure to set up integration projects referred to as workbenches. 
Workbenches may exist in different repositories of different parties and with varying audiences.

{metæffekt} provides the {metæffekt} Workbench as public repository to provide a reference for project- or
customer-specific workbenches. In the concrete application we anticipate Workbench Extensions to supply sufficient
context information and to enable sophisticated, version-controlled monitoring and reporting features for everyone.
