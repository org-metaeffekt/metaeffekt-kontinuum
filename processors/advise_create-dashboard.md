# Advise - Create Dashboard

This process takes an enriched input inventory (see [advise_enrich-inventory](advise_enrich-inventory.md)) and creates 
a Vulnerability Assessment Dashboard from it. Additional parameters can influence the information contained in the 
resulting dashboard which are listed in the table below.

| Parameter                                   | Required | Description                                                                                                                      |
|---------------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------|
| input.inventory.file                        | yes      | The file of an enriched inventory used to generated the dashboard.                                                               |
| output.dashboard.file                       | yes      | The file of the resulting Vulnerability Assessment Dashboard.                                                                    |
| vulnerability.mirror.d ir                   | yes      | The directory containing the vulnerability database/index.                                                                       |
| security.policy.file                        | yes      | The security policy file to use.                                                                                                 |
| ae.dashboard.timeline                       | no       | Switch deciding whether a version timeline for artifacts is generated listing all vulnerabilities per version. Defaults to true. |
| ae.dashboard.timeline.vulnerabilities.limit | no       | Limits the amount of vulnerabilities per timeline.                                                                               |
| ae.cpe.correlation.limit                    | no       | Limits the amount of cpe correlations.                                                                                           |


## Future Developments

The current implementation of the Vulnerability Assessment Dashboard is under revision to allow more dynamic interactions with 
the dashboard.


