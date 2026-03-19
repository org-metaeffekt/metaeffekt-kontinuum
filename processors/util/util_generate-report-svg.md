# Util - Generate Report SVG

This process generates the SVG resources for different types of vulnerability reports for a specified inventory.

## Properties

The different properties are sorted into three different groups.

### Input / Output
| Parameter            | Required | Description                                         |
|----------------------|----------|-----------------------------------------------------|
| input.inventory.file | yes      | The file of the input inventory for SVG generation. |
| output.svg.dir       | yes      | The output directory for the svg resources.         |

### Parameters
| Parameter                            | Required | Description                                                             |
|--------------------------------------|----------|-------------------------------------------------------------------------|
| param.overview.active                | no       | Boolean for enabling overview charts to be generated default is "true". |
| param.cvss.active                    | no       | Boolean for enabling cvss charts to be generated default is "true".     |
| param.cvss.vulnerability.count.limit | no       | The limit of the cvss vulnerability count.                              |
| param.security.policy.file           | no       | The security policy file used for SVG generation.                       |
| param.security.policy.active.ids     | mo       | The Ids of the security policy configurations to activate.              |

### Environment
None
