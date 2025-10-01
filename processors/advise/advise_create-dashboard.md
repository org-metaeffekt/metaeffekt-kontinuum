# Advise - Create Dashboard

This process takes an enriched input inventory (see [advise_enrich-inventory](advise_enrich-inventory.md)) and creates 
a Vulnerability Assessment Dashboard from it. Additional parameters can influence the information contained in the 
resulting dashboard which are listed in the table below.

## Future Developments

The current implementation of the Vulnerability Assessment Dashboard is under revision to allow more dynamic interactions with 
the dashboard.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                                     | Required | Description                                                                      |
|-----------------------------------------------|----------|----------------------------------------------------------------------------------|
| input.inventory.file                          | yes      | The input inventory from which the dashboard will be generated.                  |
| output.dashboard.file                         | yes      | The file of the resulting vulnerability assessment dashboard.                    |

### Parameters
| Parameter                          | Required | Description                                                       |
|------------------------------------|----------|-------------------------------------------------------------------|
| param.security.policy.file         | yes      | The security policy file to use.                                                 |
| param.timeline.conf.enabled        | yes      | Enables the timeline configuration overall.                       | 
| param.timeline.max.threads         | yes      | The maximum number of threads working on timelines.               |
| param.timeline.time.spent.max      | yes      | The maximum number of seconds spent per timeline.                 |
| param.timeline.vuln.providers.list | yes      | A list of vulnerability providers used to generate the timelines. |

### Environment
| Parameter                                          | Required | Description                                                                      |
|----------------------------------------------------|----------|----------------------------------------------------------------------------------|
| env.vulnerability.mirror.dir                       | yes      | The directory containing the vulnerability database/index.                       |

