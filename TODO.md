# TODOs

## Test Resources

- [ ] always perform tests based on workspaces; we would like to ensure a certain common structure and align the 
  processors accordingly. Within the test workspaces we ensure that configuration is minimal and integrates with the
  provisions of the workspace (not available on kontinuum-level; here we use the tests/resources/workbench).
- [ ] use workspace-001 example to further build unit tests around. Concat in a workspace-pipeline-001.sh script.
- [ ] minimize tests/resources/generic; considere renanimg once consolidated 
- [ ] on kontinuum level we do not use common correlation data. We accept false-negative and false-positive 
  vulnerability associations on this level. Yet this must be documented sufficiently to not raise false expectations.
- [ ] due to the complexities on template level we use a decoupled report-template on kontinuum level. This needs to
  be documented accordingly (the fact and how to use the metaeffekt-workbench default report-template).
