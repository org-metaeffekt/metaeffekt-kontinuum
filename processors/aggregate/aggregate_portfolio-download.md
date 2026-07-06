# Aggregate - Portfolio Download

This process enables the download of specific assets from a running portfolio manager service.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                    | Required | Description                                                |
|------------------------------|----------|------------------------------------------------------------|
| output.inventory.dir         | yes      | The directory in which to download all fitting inventories |

### Parameters
| Parameter                     | Required | Description                                                                                                  |
|-------------------------------|----------|--------------------------------------------------------------------------------------------------------------|
| param.portfolio.manager.url   | yes      | Url to the SwAM Portfolio Manager Endpoint                                                                   |
| param.portfolio.manager.token | yes      | Upload user token for project; always pass as credential/secret                                              |
| param.project.name            | yes      | The project name                                                                                             |
| param.asset.group.id          | yes      | The asset group id                                                                                           |
| param.asset.name              | yes      | The asset name                                                                                               |
| param.asset.version           | yes      | The version                                                                                                  |
| param.keystore.password       | yes      | Keystore password, always pass as credential/secret                                                          |
| param.truststore.password     | yes      | Truststore password, always pass as credential/secret                                                        |
| param.keystore.config.file    | yes      | Path to the keystore                                                                                         |
| param.truststore.config.file  | yes      | Path to the truststore                                                                                       |
| param.inventory.modifier      | yes      | Determines from which stage the inventory will be downloaded. Can either be 'initial', 'scanned' or 'report' |

