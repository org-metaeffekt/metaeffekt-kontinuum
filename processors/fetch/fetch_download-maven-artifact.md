# Fetch - Download Maven Artifact

This processor downloads a remote Maven artifact specified by its group ID, artifact ID, and version from Maven Central or a custom repository URL into a target directory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter        | Required | Description                                       |
|------------------|----------|---------------------------------------------------|
| output.asset.dir | yes      | The target directory to download the asset into.  |

### Parameters
| Parameter         | Required | Description                                                    |
|-------------------|----------|----------------------------------------------------------------|
| param.group.id    | yes      | The Maven group ID of the artifact to download.                |
| param.artifact.id | yes      | The Maven artifact ID of the artifact to download.             |
| param.version     | yes      | The version of the Maven artifact to download.                 |
| param.repo.url    | no       | The URL of the repository hosting the artifact (optional).     |

### Environment
None.
