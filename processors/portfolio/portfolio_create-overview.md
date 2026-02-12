# Portfolio - Create Overview

This process creates an overview with the resources copied with the portfolio_copy-resources.xml processor.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                     | Required | Description                                                        |
|-------------------------------|----------|--------------------------------------------------------------------|
| input.inventory.dir           | yes      | The directory of the input inventory.                              |
| input.inventory.path          | yes      | The input inventory's path relative to `input.inventory.dir`.      |
| input.advisor.inventories.dir | yes      | The source directory containing advisor inventory files (`.xlsx`). |     
| input.dashboards.dir          | yes      | The source directory containing dashboard files (`.html`).         |     
| input.reports.dir             | yes      | The source directory containing report files (`.pdf`).             |     
| output.overview.file          | yes      | The name of the output overview file.                              | 
| output.notification.file      | yes      | The name of the output notification file.                          | 

### Parameters
| Parameter                        | Required | Description                                                                       |
|----------------------------------|----------|-----------------------------------------------------------------------------------|
| param.security.policy.file       | yes      | The security policy file used for the overview.                                   |     
| param.security.policy.active.ids | no       | The activeIds of the security policy configurations to use for overview creation. |
| param.notification.config.file   | yes      | The configuration file for notification.                                          |
| param.notification.rule.file     | yes      | The rule file for notification.                                                   |

### Environment
None.

# FIXMEs
* discuss: use subpath instead of path when expecting a relative path