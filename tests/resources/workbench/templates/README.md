# Templates

In this folder documentation templates are maintained. The templates contained in this directory represent a cut-down
selection of the templates found in our metaeffekt-workbench project as not all reference templates are needed here.
The report templates provide "ready to go" report types which can be generated via the [report_create-document.md](../../../../processors/report/report_create-document.md)
processor. Depending on which report type was triggered, a fitting profile is selected which generates the relevant
report sections into a single resulting pdf. 

For an example on how to generate a report take a look at the following files:
- [report_create-document-01.sh](../../../scripts/cases/report/report_create-document-01.sh)
- [report_create-document.sh](../../../scripts/processors/report/report_create-document.sh)
- [asset-descriptor_GENERIC-vulnerability-report.yaml](../descriptors/asset-descriptor_GENERIC-vulnerability-report.yaml)

## Profiles

### Software Distribution Annex (SDA)

### License Documentation (LD)

### Initial License Documentation (ILD)

### Vulnerability Report (VR)

### Cert Report (CR)

TODO
- add profile description
- describe core-parameters (TBC)
