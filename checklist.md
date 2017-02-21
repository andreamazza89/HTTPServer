# Checklist

- Some of the acceptance tests raise exceptions but do pass as they retry when the serverSocket rejects the connection; is this ok?
- All the checked exceptions I simply make into unchecked ones and throw, is this ok?
- Get feedback on the fact that SocketConnection has not got tests: do I need them?
- Remove or edit name of SCAFFOLD_TESTs.
- HTTPServer tests are not good at all, as they use two threads and rely on timing. Also needs to test that a socket
closed after a response is received.