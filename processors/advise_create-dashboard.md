# Advise - Create Dashboard

This process takes an enriched input inventory (see [advise_enrich-inventory](advise_enrich-inventory.md)) and creates 
a vulnerability assessment dashboard from it. Additional parameters can influence the information contained in the 
resulting dashboard which are listed in the table below.

| Parameter                                       | Required | Description                                                                      |
|-------------------------------------------------|----------|----------------------------------------------------------------------------------|
| input.inventory.file                            | yes      | The input inventory from which the dashboard will be generated.                  |
| output.dashboard.file                           | yes      | The file of the resulting vulnerability assessment dashboard.                    |
| vulnerability.mirror.dir                        | yes      | The directory containing the vulnerability database/index.                       |
| security.policy.file                            | yes      | The security policy file to use.                                                 |
| maximum.vulnerabilities.per.dashboard.count     | no       | Maximum number of vulnerabilities to display in the dashboard.                   | 
| vulnerability.include.filter                    | no       | A filter expression to include only specific vulnerabilities.                    |
| maximum.cpe.for.timelines.per.vulnerability     | no       | Maximum number of CPEs shown per vulnerability timeline.                         |
| maximum.vulnerabilities.per.timeline            | no       | Maximum number of vulnerabilities to display in a single timeline.               |
| maximum.versions.per.timeline                   | no       | Maximum number of software versions to display in a timeline.                    |
| maximum.time.spent.on.timelines                 | no       | Global time limit (in seconds) for generating all vulnerability timelines.       |
| maximum.time.spent.per.timeline                 | no       | Time limit (in seconds) for generating a single vulnerability timeline.          |
| vulnerability.timelines.global.enabled          | no       | Enables or disables generation of vulnerability timelines globally.              |
| vulnerability.timeline.hide.irrelevant.versions | no       | Hides software versions in timelines that are not affected by the vulnerability. |
| fail.on.vulnerability.without.specified.risk    | no       | Fails the build if a vulnerability has no specified risk rating.                 |
| fail.on.unreviewed.advisories                   | no       | Fails the build if advisories have not been reviewed.                            |
| vulnerability.svg.chart.interpolation.method    | no       | The interpolation method used for SVG charts (e.g. LINEAR, BASE-METRICS).        |
| detail.levels                                   | no       | Controls the level of detail included.                                           |

## Future Developments

The current implementation of the vulnerability assessment dashboard is under revision to allow more dynamic interactions with 
the dashboard.


