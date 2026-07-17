# Util - Aggregate Sources

Checks a reference inventory for the artifacts contained within and downloads them from different configured data sources. This process is a precursor to generating an
annex-document, which requires the additional artifact archives during generation.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter            | Required | Description                                                                                |
|----------------------|----------|--------------------------------------------------------------------------------------------|
| input.inventory.file | yes      | The file path of the inventory containing the artifacts for which to download the sources. |
| output.target.dir    | yes      | The directory in which to download the source artifacts.                                   |     

### Parameters
| Parameter           | Required | Description                                                                                              |
|---------------------|----------|----------------------------------------------------------------------------------------------------------|
| param.config.file   | yes      | The file path of the configuration YAML required for the source aggregation process.                     |
| param.protocol.file | no       | The file path of the protocol file containing detailed execution information for the source aggregation. |

