# Util - Portfolio Download

This process enables the download of specific assets from a running portfolio manager service.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                    | Required | Description                          |
|------------------------------|----------|--------------------------------------|
| input.file                   | yes      | The SBOM or inventory file to upload |

### Parameters
| Parameter                     | Required | Description                                                     |
|-------------------------------|----------|-----------------------------------------------------------------|
| param.portfolio.manager.url   | yes      | Url to the SwAM Portfolio Manager Endpoint                      |
| param.portfolio.manager.token | yes      | Upload user token for project; always pass as credential/secret |
| param.product.name            | yes      | The product name                                                |
| param.product.version         | yes      | The product version                                             |
| param.product.artifact.id     | yes      | The product artifact id                                         |
| param.keystore.config.file      | yes      | Path to the keystore                                            |
| param.truststore.config.file    | yes      | Path to the truststore                                          |
| param.keystore.password         | yes      | Keystore password, always pass as credential/secret             |
| param.truststore.password       | yes      | Truststore password, always pass as credential/secret           |
