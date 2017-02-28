# Checklist

- remove fileSystem vulnerability (should refuse paths that include '..')
- make request body optional?
- extract data range parser from filesystem
- refactor fakes that use inheritance to use interface instead?
- Request should maybe handle invalid requests gracefully?
- Look at Maven warnings when compiling.
- Maven test runner does not tell you if there are any failing tests?
- All the checked exceptions I simply make into unchecked ones and throw, is this ok?
- Get feedback on the fact that SocketConnection has not got tests: do I need them?
