# TODOs

## Test Resources

- [ ? ] always perform tests based on workspaces; we would like to ensure a certain common structure and align the 
  processors accordingly. Within the test workspaces we ensure that configuration is minimal and integrates with the
  provisions of the workspace (not available on kontinuum-level; here we use the tests/resources/workbench).
- [ ] on kontinuum level we do not use common correlation data. We accept false-negative and false-positive 
  vulnerability associations on this level. Yet this must be documented sufficiently to not raise false expectations.
- [ ] DISCUSS: only log processor executions from script; append processor logs to dedicate log file; enable console
  logging using debug flag on environment-level.
  - log 1: only log inputs and outputs as well as processor name/success. 
  - log 2: only log executed commands as well as processor name/success.
  - log 3: full log of maven call, commands, processor name/success. (current console log)
- [ ] add mirror download processor which downloads and builds the mirror from the different data sources instaed of
  our own server / a local instance.

