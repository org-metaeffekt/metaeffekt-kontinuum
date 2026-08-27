# Fetch - Download Asset

This processor downloads a remote asset from a specified URL into a target directory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter        | Required | Description                                       |
|------------------|----------|---------------------------------------------------|
| output.asset.dir | yes      | The target directory to download the asset into.  |

### Parameters
| Parameter                | Required | Description                                                                                 |
|--------------------------|----------|---------------------------------------------------------------------------------------------|
| param.asset.url          | yes      | The URL of the remote asset to download.                                                    |
| param.asset.username     | no       | Username for HTTP Basic Authentication.                                                     |
| param.asset.password     | no       | Password for HTTP Basic Authentication.                                                     |
| param.asset.token        | no       | Bearer token or token value for HTTP authentication.                                        |
| param.asset.header.name  | no       | Custom HTTP header name for token authentication (e.g. `PRIVATE-TOKEN`, `JOB-TOKEN`).        |
| param.asset.header.value | no       | Custom HTTP header value when custom header name is specified.                              |

### Environment
None.
