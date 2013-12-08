#TracePanel and AlertLog

###Questions & Discussion
Please use the [issue tracker](https://github.com/usc-csci201-fall2013/simcity201/issues) for this repository to ask and discuss topics related to the team project.

###Resources:
  + [Overview & Deliverables](http://www-scf.usc.edu/~csci201/team/)
  + [Operational Concepts Description](http://www-scf.usc.edu/~csci201/team/operational-concepts-description.html)
  + [Requirements](http://www-scf.usc.edu/~csci201/team/simcity201.html)
  

#Using the new logging system
A basic demo developed by Keith is available in the DemoLauncher class.  Search through it for the word "TUTORIAL" for examples of how to use the system.
This system has also been integrated into the existing city demo.

###The Alert Log
This code contains an AlertLog that can be used to log messages with different Levels and Tags.  The existing levels are: ERROR, WARNING, INFO, MESSAGE, and DEBUG.  A few Tags (represented by the AlertTag enum) are also listed already, although you will have to add new tags as you integrate it into your code.  Tags are just a way to sort the logs besides only the AlertLevel.  The AlertLog is a statically accessible singleton, so you can access it from anywhere in your code easily using:
``` AlertLog.getInstance(); ```
    
There are methods to log different types of alerts:

```
    logError
    logWarning
    logInfo
    logMessage
    logDebug
```

The parameters for each of these are the same: 
```logXXX(AlertTag tag, String name, String message);```
Where the tag is a label that you use to group the message, the name is the name of the sender, and the message is the content of the message itself.  These will all be stored in the AlertLog, and will be accessed by the TracePanel.

###The Trace Panel
The other big feature of these classes is the TracePanel.  It takes events that have been logged in the AlertLog and displays them (much like the console in eclipse) nicely formatted with timestamps, sender names, and coloring.  You can see documentation on how to use it in the city demo, as well as the demo in the trace package.  Using it is relatively straightforward, as much of the behind-the-scenes management of the Alerts is taken care of internally.  You can simply instantiate it and then use it like any other JPanel.

The biggest feature of this panel over simply writing everything with System.out.println() is the ability to filter the content.  You can tell the TracePanel to only display ERRORs and WARNINGs, or to hide all messages tagged with BANK\_CUSTOMER, or show messages tagged with PERSON\_HOUSE\_ROLE, etc.  You can sort by Level and by Tag, presuming that you have set up more enum values in AlertTag.  

**NOTE: An alert will only be printed if its AlertLevel is enabled, AND its AlertTag is not hidden!  By default, all levels are enabled, but none of the tags are.**

The methods to do this are:

    showAlertsWithLevel(AlertLevel level)
    hideAlertsWithLevel(AlertLevel level)
    
    showAlertsWithTag(AlertTag tag)
    hideAlertsWithTag(AlertTag tag)
    
    //These are convenience methods to enable everything
    showAlertsForAllLevels()
    showAlertsForAllTags()
    
###Summary
This system make it much easier to see what is going on when you use print statements from your agents and animations.  It would be almost impossible to tell if the restaurant agents were interacting in the right order if there were print messages from the markets, banks, busses, and people all over the place getting in the way.  This should make debugging and verifying operations in your city much easier.  Good luck!


###Known Issues
 + Warnings print in yellow text, but with a black background.  The black makes it possible to read, but quite ugly.  Feel free to change that background, the text color, or the panel background color.
 + Filtering the panel becomes a somewhat costly operation when the number of Alerts gets very high, since it currently must clear out all of the text, and then go through all of the alerts to see if each one should be printed.  This operation hasn't been optimized yet.
