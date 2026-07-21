# Util - Apply Business Case

An inventory can be evaluated in a defined business case and documentation context. This processor enables to
apply business case specific modulation of an inventory. E.g. before producing a distribution annex.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter             | Required | Description                                                                                     |
|-----------------------|----------|-------------------------------------------------------------------------------------------------|
| input.inventory.file  | yes      | The file path of the inventory which will be modulated with business case specific information. |
| output.inventory.file | yes      | The target inventory file.                                                                      |     

### Parameters
| Parameter                   | Required | Description                                                                                                                                                                       |
|-----------------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| param.language.mode         | no       | The language mode selects the languages currently `de_DE`, `en_US` are supported. `en_US` is default.                                                                             |
| param.source.mode           | no       | The source mode determines how source code is handled in the business case. One of `NONE`, `DISTRIBUTION_ANNEX`, `ON_DEMAND`. `DISTRIBUTION_ANNEX` is default.                    |
| param.notice.mode.overwrite | no       | The input inventory may already contain license notices that are not meant to be updated. With this parameter being `false`, exising notices will not updated. Default is `true`. |

NOTE: currently the business case configuration is work in progress. The definition here will be revised in the short future.

### Environment
| Parameter             | Required | Description                                                                                     |
|-----------------------|----------|-------------------------------------------------------------------------------------------------|
| env.tmd.source        | no       | The license database source. Either `ae-universe` or `ae-kosmos`. Default is `ae-kosmos`.       |
| env.tmd.userkeys.file | yes      | The license database used applying the inventory modulation requires a key file to be accessed. |
| env.tmd.password      | yes      | Along with the key file a password is required to access the key.                               |
