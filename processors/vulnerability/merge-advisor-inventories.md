# Util - Merge Advisors

This process merges inventories based on an individual security advisor and filters them using the security policy.

## Properties

The different properties are sorted into three different groups. 

### Input / Output
| Parameter             | Required | Description                                                        |
|-----------------------|----------|--------------------------------------------------------------------|
| input.inventory.dir   | yes      | The directory containing the inventories to be merged.             |
| output.inventory.file | yes      | The file path of the output inventory containing the merged data.  |

### Parameters
| Parameter                           | Required | Description                                              |
|-------------------------------------|----------|----------------------------------------------------------|
| param.security.policy.file          | yes      | The security policy file needed to filter the inventory. |
| param.security.policy.active.ids    | no       | The activeIds of the security policy configuration.      |

### Environment
None