# Util - Enrich Advisors

This process enriches an inventory based on the specified advisors. It is only used for cert-report document generation.

## Properties

The different properties are sorted into three different groups.

### Input / Output
| Parameter             | Required | Description                                     |
|-----------------------|----------|-------------------------------------------------|
| input.inventory.file  | yes      | The file of the input inventory for enrichment. |
| output.inventory.file | yes      | The output inventory of the enrichment process. |

### Parameters
| Parameter                        | Required | Description                                              |
|----------------------------------|----------|----------------------------------------------------------|
| param.security.policy.file       | yes      | The security policy file needed to filter the inventory. |
| param.security.policy.active.ids | no       | The activeIds of the security policy configuration.      |
| param.report.period.since        | no       | The lower bound of the report query period.              |
| param.report.period.until        | no       | The upper bound of the report query period.              |

### Environment
| Parameter                           | Required | Description                                                               |
|-------------------------------------|----------|---------------------------------------------------------------------------|
| env.vulnerability.mirror.dir        | yes      | The location of the vulnerability database used for inventory enrichment. |
