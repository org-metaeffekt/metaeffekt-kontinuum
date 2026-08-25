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
| Parameter                         | Required | Description                                                                                            |
|-----------------------------------|----------|--------------------------------------------------------------------------------------------------------|
| param.correlation.dir             | yes      | The directory containing vulnerability correlation information.                                        |
| param.assessment.dirs             | yes      | A comma separated list of all directories or files containing assessments.                             |
| param.assessment.labels           | no       | The labels for activating assessments. Default is empty, meaning assessments with labels are inactive. |                                                                  |
| param.context.dirs                | yes      | A comma separated list of all directories or files containing contexts.                                |
| param.security.policy.file        | yes      | The security policy file to use.                                                                       |
| param.security.policy.active.ids  | no       | The activeIds of the security policy configurations to use for enrichment.                             |
| param.activate.*                  | no       | Activates the defined vulnerability enrichment step. Defaults to true.                                 |
| param.exclude.nvd.equivalent.msrc | no       | Excludes MSRC vulnerabilities equivalent to NVD. Defaults to false.                                    |
| param.exclude.nvd.equivalent.osv  | no       | Excludes OSV vulnerabilities equivalent to NVD. Defaults to false.                                     |
| param.remove.ghsa.unreviewed      | no       | Removes unreviewed GHSA advisories. Defaults to false.                                                 |
| param.vulnerabilities.custom.dir  | no       | The directory for custom vulnerabilities that can be added to enrichment.                              |
| param.threat.catalog.file         | no       | A file pointing to the threat catalog to use when ${param.activate.threat}=true                        |
| param.dashboard.title             | ignore   | Title for a dashboard which could be generated during this process.                                    |
| param.dashboard.subtitle          | ignore   | Subtitle for a dashboard which could be generated during this process.                                 |
| param.dashboard.footer            | ignore   | Footer for a dashboard which could be generated during this process.                                   |

### Environment
| Parameter                    | Required | Description                                        |
|------------------------------|----------|----------------------------------------------------|
| env.vulnerability.mirror.dir | yes      | The directory of the vulnerability mirror / index. |


## Examples

The following construct activates only GHSA as an OSV provider:

    -Dparam.activate.osv.providers='[{"src":"GHSA","impl":"OSV"} ]'

The default enables any provider source:

    -Dparam.activate.osv.providers='[{"src":"*","impl":"OSV"}]'

