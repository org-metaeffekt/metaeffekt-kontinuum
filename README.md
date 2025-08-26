# metaeffekt-kontinuum

The {metaeffekt} Kontinuum repository aggregates baseline configurations to support using the 
{metaeffekt} plugins, tools and content.

## Processors

To enable broad adoption the Kontinuum uses Maven as integration level. Yet, for specific tasks, isolated
task definitions are provided as individual .xml files in the `processors` folder. Every .xml files defines
a process step in analogy to an isolated workflow engine task.

On integration level the processors can be combined to define pipelines and workflows.

For further details see [Processors](processors/README.md).

## Integration

Integration of the {metæffekt} processors is manifold. The following diagram illustrates the anticipated integration 
scenarios.

![](docs/concept_kontinuum-workbench.png)

The Kontinuum provides the infrastructure to set up integration projects referred to as workbenches. Workbenches may 
exist in different repositories of different parties and with varying audiences.

{metæffekt} provides the {metæffekt} Workbench as public repository to provide a reference for project- or 
customer-specific workbenches. In the concrete application we anticipate Workbench Extensions to supply sufficient 
context information and to enable sophisticated, version-controlled monitoring and reporting features for everyone.
