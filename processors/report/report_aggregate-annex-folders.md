# Report - Aggregate Annex Folders

This process enables the aggregation of license and component information for a specified inventory. The content will be
generated to the specified target directories. This process is a part of the creation of a Software Distribution Annex.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property                      | Required | Explanation                                                                    |
|-------------------------------|----------|--------------------------------------------------------------------------------|
| input.inventory.file          | yes      | The input inventory for which the contents are to be aggregated.               |

### Parameters
| Property                           | Required | Explanation                                                                                                                                            |
|------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| param.reference.inventory.dir      | yes      | The input reference inventory with which the input inventory will be enriched. This is the parent directory of the license and components directories. |
| param.reference.inventory.includes | no       | A comma separated list of included filetypes for the reference inventory, default is "**/*.ser,**/*.xls,**/*.xlsx.                                     |
| param.reference.component.path     | no       | The component paht of the reference inventory, default is  "components".                                                                               |
| param.reference.license.path       | no       | The license paht of the reference inventory, default is  "licenses".                                                                                   |
| param.target.component.dir         | no       | The target directory for the components, default is "${project.build.dir}/annex/components".                                                           |
| param.target.license.dir           | no       | The target directory for the licenses default is "{project.build.dir}/annex/components".                                                               |
| param.fail.on.missing.license.file | no       | A boolean for controlling if the process fails if a license file is missing, default is "true".                                                        |


### Environment
None
