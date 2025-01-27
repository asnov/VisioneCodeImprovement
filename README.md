## Alex Novikov (alex@lpdev.pro)

Comfort level on a scale 1 to 10:
1) Native Android development - 9
2) Kotlin - 9
3) API Requests - 9
4) New Libraries - 10
5) Salesforce - 4

I found and fixed a few issues and made a few improvements, so I've divided them by skill level:

**Junior level improvements** (https://github.com/asnov/VisioneCodeImprovement/commit/b6ee057d18db30c3772b761516d2266145adfb9f):

1) Formatting. I would recommend to use default Jet Brains IDE linter before commit or setup some linter on git hook to make this automatically;
2) callGemini(): calling processCustomFields() doesn't send map parameter from the closure, so the results of the recognition are not shown to the User.
	- https://github.com/asnov/VisioneCodeImprovement/compare/main...feature/improvements#diff-acc4795f1f702d02d0fbf9a08e75fbad4f72233c1973074e341f08ba1016ae49L37-R42
3) getGeminiResult() in DataRepositoryImpl.kt : The closing curly brace was in the wrong place. This may have happened when copying the code.
	- https://github.com/asnov/VisioneCodeImprovement/compare/main...feature/improvements#diff-839b92fdd9411a1a4939d4415fcc30dbc148e57ebe5b7b4c30ab35e7d7f6718fL43-R49

**Middle level Improvements** (https://github.com/asnov/VisioneCodeImprovement/commit/db6b032b4e252fca34524abc94ad653e1cdc1ed8):

4) fetchOcrDataUsingGemini(): fileToBase64String() call could throw exception during Base64 file to string conversion so I would add some error handling to notify the User. This is the foundation of a good experience.
	- https://github.com/asnov/VisioneCodeImprovement/commit/db6b032b4e252fca34524abc94ad653e1cdc1ed8#diff-9b9b188b48e717babf147f573f121f67ac487a7f00a92a2eb31e94306ebda992R52
5) callGemini():  also calling onError() doesn't send the string error parameter from the closure, so the User would not be notified about the error reason.
	- https://github.com/asnov/VisioneCodeImprovement/commit/db6b032b4e252fca34524abc94ad653e1cdc1ed8#diff-acc4795f1f702d02d0fbf9a08e75fbad4f72233c1973074e341f08ba1016ae49L41-R42
6) callGemini(): incoming parameter onSuccess is never user. I think this is an error because the caller of callGemini() probably blocks corresponding button to avoid double clicks so it needs this callback to remove the blocking.
	- https://github.com/asnov/VisioneCodeImprovement/commit/db6b032b4e252fca34524abc94ad653e1cdc1ed8#diff-acc4795f1f702d02d0fbf9a08e75fbad4f72233c1973074e341f08ba1016ae49R39
7) fetchOcrDataUsingGemini() line 101 `onError(errorMsg.message.orEmpty())`: I would not send the empty error message to the client. I believe, something like "unspecified error during API call." will be much better.
	- https://github.com/asnov/VisioneCodeImprovement/commit/db6b032b4e252fca34524abc94ad653e1cdc1ed8#diff-9b9b188b48e717babf147f573f121f67ac487a7f00a92a2eb31e94306ebda992L101-R104

**Senior level Improvements** (didn't implemented yet):

8) There is no tests. This is the difference between junior, mid-level and senior developers: the former don't do testing, the latter may do it when pushed by their boss, and the latter start with it to get proven high-quality code.
9) There is no possibility for the User to cancel the request. For example, if a user pressed a button accidentally or by mistake and wants to immediately cancel their request, they will not be able to do so. Also, if for some reason beyond our control, the request lasts longer than necessary, the user will have to restart the application to continue working.
10) There is no visual progress indicator of the recognition process. I think this will make the user's wait more enjoyable.
11) I would increase default Retrofit network timeout from 10 seconds to 20. The request takes about 8 seconds in my environment with fiber optics. I dread to guess how long it might take in a conference with crowded cell phones.
12) To improve quality of the service that app provides, I would advise to collect some anonymous information about the unsuccessful clients requests. So the product manager could analyze it and to make improvement decisions.

All my changes are here: https://github.com/asnov/VisioneCodeImprovement/compare/main...feature/improvements

If there are other problems with the presented code from the user's point of view, I would be glad to fix them.
