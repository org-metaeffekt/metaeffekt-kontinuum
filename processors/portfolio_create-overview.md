# Portfolio - Create Overview

This process creates an overview with the resources copied with the portfolio_copy-resources.xml processor.

| Parameter               | Required | Description                                                        |
|-------------------------|----------|--------------------------------------------------------------------|
| input.inventory.dir     | yes      | The directory of the input inventory for overview generation.      |
| input.inventory.path    | yes      | The path of the input inventory for overview generation.           |
| security.policy.file    | yes      | The security policy file used for the overview.                    |     
| advisor.inventories.dir | yes      | The source directory containing advisor inventory files (`.xlsx`). |     
| dashboards.dir          | yes      | The source directory containing dashboard files (`.html`).         |     
| reports.dir             | yes      | The source directory containing report files (`.pdf`).             |     
| output.file             | yes      | The name of the output overview file.                              |     
