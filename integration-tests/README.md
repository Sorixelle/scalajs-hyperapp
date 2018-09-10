# Integration Tests
Integration tests are written using [Gwen-web](https://github.com/gwen-interpreter/gwen-web/). Actual tests are located
in the `gwen-workspace` folder, with apps to test on located in `src`.

## Running Tests
These commands should be run in the `gwen-workspace` folder. Any other Gwen options can be used, such as `--parallel`
for parallel test execution.
### Windows
```
gwen -b features
```
### macOS and Linux Distros
```
./gwen -b features
```
