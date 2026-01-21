# Advise - Enrich Inventories

This process takes an input inventory and enriches it with vulnerability information. Additional configurations can influence
the information contained in the resulting inventory such as which vulnerability databases should be used, custom vulnerabilities and
many more, listed in the table below.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../README.md#processor-conventions)
of this repository.

### Input / Output
| Parameter                        | Required | Description                                                               |
|----------------------------------|----------|---------------------------------------------------------------------------|
| input.inventory.file             | yes      | The input inventory file which will be enriched.                          |
| output.inventory.file            | yes      | The file of the resulting output inventory.                               |
| output.tmp.dir                   | yes      | The directory containing temporary files like intermediate inventories.   |

### Parameters
| Parameter                        | Required | Description                                                                                   |
|----------------------------------|----------|-----------------------------------------------------------------------------------------------|
| param.correlation.dir            | yes      | The directory containing vulnerability correlation information.                               |
| param.assessment.dir             | yes      | The directory containing the vulnerability assessment information.                            |
| param.context.dir                | yes      | The directory containing the vulnerability context information.                               |
| param.security.policy.file       | yes      | The security policy file to use.                                                              |
| param.security.policy.active.ids | no       | The activeIds of the security policy configurations to use for enrichment.                    |
| param.activate.*                 | no       | Switches determining which vulnerability databases are utilized during the enrichment process. |
| param.exclude.nvd.equivalent.*   | no       | Switches determining if vulnerabilities equivalent to those found via nvd should be excluded.  |
| param.vulnerabilities.custom.dir | no       | The directory for custom vulnerabilities that can be added to enrichment.                      |
| param.dashboard.title            | ignore   | Title for a dashboard which could be generated during this process.                            |
| param.dashboard.subtitle         | ignore   | Subtitle for a dashboard which could be generated during this process.                         |
| param.dashboard.footer           | ignore   | Footer for a dashboard which could be generated during this process.                           |
=
### Environment
| Parameter                    | Required | Description                                        |
|------------------------------|----------|----------------------------------------------------|
| env.vulnerability.mirror.dir | yes      | The directory of the vulnerability mirror / index. |
