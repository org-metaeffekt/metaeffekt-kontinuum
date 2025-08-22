# metaeffekt-kontinuum

The {metaeffekt} Kontinuum repository aggregates baseline configurations to support using the 
{metaeffekt} plugins, tools and content.

## Processors

To enable broad adoption the Kontinuum uses Maven as integration level. Yet, for specific tasks, isolated
task definitions are provided as individual .xml files in the `processors` folder. Every .xml files defines
a process step in analogy to an isolated workflow engine task.

On integration level the processors can be combined to define pipelines and workflows.

For further details see [Processors](processors/README.md).

## Parameters

Each processor requires a series of parameters to be set to function correctly. The required and optional parameters
are grouped into three categories, input / output, parameters and environment. 

### Input / Output

Input / output parameters usually describe files or directories which the processor requires to run.
These can be in the form of configuration files, inventories, SBOMs, property-files and so on.

Parameters in this category are prefixed with:
- input
- output

Suffixed with:
- file
- dir
- path

### Parameters
The "parameters" category simply describes any additional parameters which are needed for the processor to run or to
configure the processors flow and influence the outpout.

Parameters in this category are prefixed with:
- param

Suffixed with:
- enabled (for parameters which can be true or false)

### Environment
Environment parameters describe a series of prerequisites which are not necessarily specific to this single processor.
They usually describe directories or config files containing resources required by multiple processors which can be
shared project wide. Examples are the vulnerability database, maven mirror etc.

Parameters in this category are prefixed with:
- env
