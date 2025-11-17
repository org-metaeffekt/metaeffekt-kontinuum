# Getting Started

To create automatically tested releases based on predefined releases of artifact-analysis, core and the
metaeffekt-workbench simply run the release workflow manually via GitHub or the GitHub cli. All necessary
configurations can be done via the available inputs for the workflow.

## Testing locally with act-runner

To ensure the release workflow will run successfully before running the workflow remotely, the workflow
can be executed locally via the act-runner. A couple of prerequisites are necessary for this to work:

### Prerequisites

If on macos install the act-runner via:
   ```bash
   brew install act
   ```
Act requires docker desktop and podman desktop to be installed to work.

To run the workflow with inputs a config file is required which can be located anywhere and
looks as follows:

   ```json
      {
      "action": "workflow_call",
      "inputs": {
        "ae-core-version": "0.147.0",
        "ae-artifact-analysis-version": "0.150.0"
      }
    }
   ```

Since the remote GitHub runner uses a full ubuntu image to run all tests, act needs to be configured to
use a similar image as some prerequisites are already installed on the full image. This is done automatically
by act when executing the workflow via the following command:

   ```bash
    act -P ubuntu-latest=catthehacker/ubuntu:full-latest -e YOUR_CONFIG_FILE.json
   ```




