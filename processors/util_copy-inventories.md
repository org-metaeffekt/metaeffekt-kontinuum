# Util - Copy Inventories

It copies a list of inventories to a directory. This is a utility processor used to copy different inventories from specific
locations to a shared directory which can be parameterized to the template for creating a report from an inventory directory.
Use this processor when your directory structure contains multiple versions of the same inventory within one directory, and
you want to combine them with other directories in a report.

| Property         | Required | Explanation                                                          |
|------------------|----------|----------------------------------------------------------------------|
| inventories.list | yes      | The list containing the inventories to copy to the output directory. |
| output.dir       | yes      | The output directory to which the files are copied.                  |
