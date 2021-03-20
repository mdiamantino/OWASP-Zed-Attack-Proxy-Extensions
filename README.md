# The project
In this repository you will find three extensions to OWASP ZAP project.

## Extensions

### Policy rule verifier
#### Description
This extension lets a developer provide a JAR file containing the programmatic implementation of a set of policy rules. When running in proxy mode, ZAP will check requests and responses against these rules. If a rule flags a request, an alert is raised. This last can be consulted by the developer from within the ZAP UI, in the « Alerts » tab.

A policy is defined as the Jar file containing the programmatic implementation of a set of policy rules. In practice, to create a Policy, you first need to implement a set of rules (each one in a separate java class) starting from an empty Java project, then create a Jar of those rules.

#### Documentation
A complete tutorial on how to use this extension is available [here](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_01/Tutorial.pdf).

#### Complementary files

If you desire to understand more on the structure of the addon, please go to **solutions/user_story_02**. There you can find:
* The [class diagram](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_01/class_diagram.pdf)
* Two sequence diagrams :
  * for the [loading process of a policy](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_01/loading-sequence-diagram%20.png).
  * for the [scan](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_01/scan-sequence-diagram.png).
* The [CodeMR analysis](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/tree/master/solutions/user_story_01/codemr) of the addon.


### Policy rule verifier with DSL
#### Description
Instead of having to code each rule and load it with a JAR, the policy rule verifier supports a domain-specific language (DSL).

#### Documentation
A complete tutorial on how to use this extension is available [here](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_02/TestScenarios.pdf).

#### Complementary files

If you desire to understand more on the structure of the addon, please go to **solutions/user_story_01**. There you can find:
* The [class diagram](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_02/class-diagram-story2.pdf).
* Three [sequence diagrams](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/tree/master/solutions/user_story_02/SequenceDiagrams).
* The [CodeMR analysis](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/tree/master/solutions/user_story_02/CodeMR_Analysis) of the addon.


### File tester
#### Idea
This extensions scan files being downloaded for file-specific characteristics. ZAP alerts when spots interesting files that need to be investigated.
#### Examples of supported rules
* PEG and PNG pictures are scanned to see if they contain valid image data, or some other type of data
* PEG pictures are scanned for EXIF metadata, and the data should be listed in the results of the scan
* ZIP files are scanned to see if they are password-protected
* ZIP files are scanned to see if they are a ZIP-bomb
* EXE files run automatically through VirusTotal.

#### Documentation
* Documentation is available [here](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_07/Extension%20Documentation.pdf).
* Scenario tests are available [here](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_07/Scenario%20Tests.pdf).

#### Complementary files

If you desire to understand more on the structure of the addon, please go to **solutions/user_story_07**. There you can find:
* The [class diagram](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_07/Class%20Diagram.pdf)
* Two sequence diagrams :
  * for the [test of the file](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_07/File%20Test%20Sequence%20Diagram.png).
  * for the [generation of a report](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/blob/master/solutions/user_story_07/Generate%20Report%20Sequence%20Diagram.png).
* The [CodeMR analysis](https://github.com/mdiamantino/OWASP-Zed-Attack-Proxy-Extensions/tree/master/solutions/user_story_07/codemr) of the addon.



