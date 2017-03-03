# Checklist

- try make as many fields final as possible
- filesystem should probably be a singleton and have methods that are synchronised to ensure clients do not conflict with each other's requests
- maybe system should not rely on preexisting directories? (logs & resources need to exist for the system to work, though
that is also true for the public folder)
- remove fileSystem vulnerability (should refuse paths that include '..')
- make com.andreamazzarella.http_server.request body optional?
- extract data range parser from filesystem
- refactor fakes that use inheritance to use interface instead?
- Request should maybe handle invalid requests gracefully?
- Maven test runner does not tell you if there are any failing tests?
- All the checked exceptions I simply make into unchecked ones and throw, is this ok?
- Get feedback on the fact that SocketConnection has not got tests: do I need them?
