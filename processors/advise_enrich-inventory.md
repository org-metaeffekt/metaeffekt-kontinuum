# Advise - Enrich Inventories

This process takes an input inventory and enriches it with vulnerability information. Additional configurations can influence
the information contained in the resulting inventory such as which vulnerability databases should be used, custom vulnerabilities and
many more, listed in the table below.

| Parameter                    | Required | Description                                                                                    |
|------------------------------|----------|------------------------------------------------------------------------------------------------|
| input.inventory.file         | yes      | The input inventory file which will be enriched.                                               |
| output.inventory.file        | yes      | The file of the resulting output inventory.                                                    |
| vulnerability.mirror.dir     | yes      | The directory of the vulnerability mirror / index.                                             |
| security.policy.file         | yes      | The security policy file to use.                                                               |
| correlation.dir              | yes      | The directory containing vulnerability correlation information.                                |
| context.dir                  | yes      | The directory containing the vulnerability context information .                               |
| assessment.dir               | yes      | The directory containing the vulnerability assessment information.                             |
| processor.tmp.dir            | yes      | The directory containing temporary files like intermediate inventories.                        |
| output.inventory.name        | no       | The name of the resulting inventory file.                                                      |
| output.inventory.correlation | no       | The directory containing the correlation of the output directory.                              |
| activate.*                   | no       | Switches determining which vulnerability databases are utilized during the enrichment process. |
| vulnerabilities.custom.dir   | no       | The directory for custom vulnerabilities that can be added to enrichment                       |
| dashboard.title              | ignore   | Title for a dashboard which could be generated during this process.                            |
| dashboard.subtitle           | ignore   | Subtitle for a dashboard which could be generated during this process.                         |
| dashboard.footer             | ignore   | Footer for a dashboard which could be generated during this process.                           |


