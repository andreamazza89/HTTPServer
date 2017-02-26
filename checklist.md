# Checklist

- extract data range parser from filesystem
- create new method in fileSystem, getStaticResource, which does not return optional
- remove duplication of CONCAteNATEDATA
- perhaps uri() could be removed from the Resource interface?
- find a better way to compare two bytearrays so that failing tests give better information (string comparison)
- refactor fakes that use inheritance to use interface instead
- Refactor Request class, also ensure it handles invalid requests (i.e. at the moment it relies on empty line to read body)
 |--- also add a parse method to separate parsing from initialising
- Look at Maven warnings when compiling.
- Maven test runner does not tell you if there are any failing tests?
- All the checked exceptions I simply make into unchecked ones and throw, is this ok?
- Get feedback on the fact that SocketConnection has not got tests: do I need them?
- Remove or edit name of SCAFFOLD_TESTs.
- HTTPServer tests are not good at all, as they use two threads and rely on timing. Also needs to test that a socket
closed after a response is received.