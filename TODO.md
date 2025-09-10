# TODOs

## Test Resources

- [ ] always perform tests based on workspaces; we would like to ensure a certain common structure and align the 
  processors accordingly. Within the test workspaces we ensure that configuration is minimal and integrates with the
  provisions of the workspace (not available on kontinuum-level; here we use the tests/resources/workbench).
- [ ] use workspace-001 example to further build unit tests around. Concat in a workspace-pipeline-001.sh script.
- [ ] on kontinuum level we do not use common correlation data. We accept false-negative and false-positive 
  vulnerability associations on this level. Yet this must be documented sufficiently to not raise false expectations.
- [ ] due to the complexities on template level we use a decoupled report-template on kontinuum level. This needs to
  be documented accordingly (the fact and how to use the metaeffekt-workbench default report-template).
- [ ] in processors/util a target folder is generated, check whether this still the case; avoid uncontrolled target 
  folders.
- [ ] DISCUSS: only log processor executions from script; append processor logs to dedicate log file; enable console
  logging using debug flag on environment-level.
  - log 1: only log inputs and outputs as well as processor name/success. 
  - log 2: only log executed commands as well as processor name/success.
  - log 3: full log of maven call, commands, processor name/success. (current console log)
- [ ] cover all processors with at least one unit test.

