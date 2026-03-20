# Prepare - Save Inspect Image

This process saves and inspects a docker container image via its id and version. The extracted container information is
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
| param.image.id        | yes      | The ID of the container image. Either in Format [id] or [repository/id] depending on whether the image is in the official docker repo or not. |
| param.image.version   | yes      | The image version tag.                                                                                                                        |

### Environment
None.

# FIXMEs
* provide parameter to enable implicit pull. Default to false.


