# Report - Aggregate Annex Folders With TMD

This process enables the aggregation of license and component information using a reference inventory and Terms Metadata 
(TMD) for a specified inventory. The content will be generated to the specified target directories. This process is a 
part of the creation of a Software Distribution Annex.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter            | Required | Description                                                      |
|----------------------|----------|------------------------------------------------------------------|
| input.inventory.file | yes      | The input inventory for which the contents are to be aggregated. |

### Parameters
| Parameter                             | Required | Description                                                                                                                                            |
|---------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| env.kosmos.password                   | yes      | The password to decrypt the metaeffekt license database (Kosmos).                                                                                      |
| env.kosmos.password                   | yes      | The userkeys file to access the metaeffekt license database (Kosmos).                                                                                  |
| param.reference.inventory.dir         | no       | The input reference inventory with which the input inventory will be enriched. This is the parent directory of the license and components directories. |
| param.reference.license.path          | no       | The license path of the reference inventory, default is  "licenses".                                                                                   |
| param.reference.component.path        | no       | The component path of the reference inventory, default is  "components".                                                                               |
| param.reference.inventory.includes    | no       | A comma separated list of included file types for the reference inventory, default is "\*\*/\*.ser,\*\*/\*.xls,\*\*/\*.xlsx.                           |
| param.target.license.dir              | no       | The target directory for the licenses, default is "{project.build.dir}/annex/licenses".                                                                |
| param.target.component.dir            | no       | The target directory for the components, default is "${project.build.dir}/annex/components".                                                           |
| param.fail.on.missing.license.file    | no       | A boolean for controlling whether the process fails if a license file is missing, default is "false".                                                  |
| param.fail.on.missing.component.files | no       | A boolean for controlling whether the process fails if component files are missing, default is "false".                                                |


### Environment
None
