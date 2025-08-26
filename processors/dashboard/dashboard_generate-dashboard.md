# Generate Dashboard

This process converts an inventory, enriched with vulnerability information into a vulnerability assessment dashboard.
Depending on the configuration the dashboard contains different different kinds of information about the listed vulnerabilites
such as assessments, scores, severity, cvss vectors, graphs and much more.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                  | Required | Description                                                    |
|----------------------------|----------|----------------------------------------------------------------|
| input.inventory.file       | yes      | The input inventory file used to generate the dashboard.       |
| input.security.policy.file | yes      | The security policy file containing additional configurations. |
| output.dashboard.file      | yes      | The output file where the generated dashboard will be written. |

### Parameters
None

### Environment
| Parameter                     | Required | Description                        |
|-------------------------------|----------|------------------------------------|
| env.vulnerability.mirror.dir  | yes      | The vulnerability database mirror. |


