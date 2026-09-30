# Fetch - Save Image

This process saves and inspects a docker container image via its repository URL, image ID, and version. The extracted container information is
then saved into a specified directory for further processing.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                | Required | Description                                                         |
|--------------------------|----------|---------------------------------------------------------------------|
| output.dir               | yes      | The output directory for the extracted container image information. |

### Parameters
| Parameter             | Required | Description                                                                                                                                   |
|-----------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| param.image.id        | yes      | The ID of the container image. |
| param.image.version   | yes      | The image version tag.                                                                                                                        |
| param.repo.url        | no       | The repository URL of the container image registry. Defaults to docker.io.                                                                   |

### Environment
None.
