# Report - Aggregate Licenses

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
| param.reference.inventory.dir         | no       | The input reference inventory with which the input inventory will be enriched. This is the parent directory of the license and components directories. |
| param.reference.licenses.dir          | no       | The license path of the reference inventory, default is  "licenses".                                                                                   |
| param.reference.components.dir        | no       | The component path of the reference inventory, default is  "components".                                                                               |
| param.reference.inventory.includes    | no       | A comma separated list of included file types for the reference inventory, default is "\*\*/\*.ser,\*\*/\*.xls,\*\*/\*.xlsx.                           |
| param.target.licenses.dir              | no       | The target directory for the licenses, default is "{project.build.dir}/annex/licenses".                                                                |
| param.target.components.dir            | no       | The target directory for the components, default is "${project.build.dir}/annex/components".                                                           |
| param.fail.on.missing.license.file    | no       | A boolean for controlling whether the process fails if a license file is missing, default is "false".                                                  |
| param.fail.on.missing.component.files | no       | A boolean for controlling whether the process fails if component files are missing, default is "false".                                                |

### Environment
| Parameter             | Required | Description                                                                               |
|-----------------------|----------|-------------------------------------------------------------------------------------------|
| env.tmd.source        | no       | The license database source. Either `ae-universe` or `ae-kosmos`. Default is `ae-kosmos`. |
| env.tmd.userkeys.file | yes      | The userkeys file to access the metaeffekt license database (TMD).                        |
| env.tmd.password      | yes      | The password to decrypt the metaeffekt license database (TMD).                            |
