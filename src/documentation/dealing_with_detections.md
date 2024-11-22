# <a href="https://www.paypal.com/ncp/payment/EVXKXBD6M5XPC">Get Java & Bedrock Edition in one jar</a>
## Below is a list of reasons you will see when checking a player using the “Player Info” inventory menu. To use this inventory menu, execute the command “/spartan info” to check yourself and “/spartan info playerName” to check someone else.

### Permission Bypass:
When a player has the permission spartan.bypass they won’t be checked by any check. Additionally, when a player has the permission spartan.bypass.checkName they won’t be checked by the specified check. Click to learn more about permissions

### Detection Missing:
Some detections are not included in the jar file and require a different jar file to be implemented. For example, the Spartan AntiCheat requires the Spartan.jar to implement detections for Java players. Join our Discord server to learn more

### XXXXX Compatibility:
A plugin/functionality the anti-cheat is aware of is deemed too complex to be supported in a meaningful way, so all checks are disabled to protect your server from instabilities. Click to learn more about compatibilities

### Cancelled:
Another plugin using Spartan’s Developer API or the anti-cheat itself has instructed a check to stop checking for a certain amount of time. This is likely to be introduced by a first-party or third-party compatibility to prevent instabilities and does not require fixing unless implemented improperly.

### Silent Checking:
The anti-cheat is normally checking the player but without applying any preventions upon detection of a certain or multiple hack modules. To change this, go to the folder “/plugins/Spartan”, open the checks.yml file and set the option “silent” to “false” under the category of the check you want. You can also disable the check using the “enabled” option.

### Checking:
The anti-cheat is normally checking the player and will apply preventions upon detection of a certain or multiple hack modules. To change this, go to the folder “/plugins/Spartan”, open the checks.yml file and set the option “silent” to “true” under the category of the check you want to disable. You can also disable the check using the “enabled” option.


## Using the Right Notifications

### Suspicion Notifications:
Notifications which are not frequent and compressed to contain only what truly matters. These notifications will periodically send a message letting you know some players are hacking and will attach their names so you can easily find them.

### Detection Notifications:
Notifications which are frequent and contain a lot of useful information. These notifications will send a message when a player is violated by a check and contain information about the check violations, the player latency, the server TPS, the detection information, e.t.c. Their frequency will change depending on how close you are to the player, allowing you to focus on what matters.

### Explanation:
When notification frequency is less than 100 ticks, which is worth 5 seconds, Spartan will pick Detection Notifications. If it is 100 ticks or greater, it will pick Detection Notifications along with Suspicion Notifications. To change the frequency, run the command /spartan notifications X, with X being the ticks-frequency of the Detection Notifications. For example, if you pick 10 you will be notified every 0.5 seconds because the server runs 20 times per second, thus “10 / 20 = 0.5”.

## Giving time for the checks to collect data

### What happens in the background
Spartan collects data per every detection of every check to measure probabilities and figure out who is using hack modules. As more data is collected, Spartan will enable preventions and then punishments for specific detections of checks until eventually all detections of all checks are fully functional. This process may take minutes, hours or even weeks depending on how many players are online in your server at once and how active each player is when they are online. Fortunately, data stored is part of profiles stored in storage and memory, meaning players who help Spartan collect data do not need to be online all the time for their data to be used, as Spartan will use the stored data of offline players just fine.

### How to be informed about this
To learn about the "approximate" (Approximate because each check is a collection of detections and not one simple algorithm) state of Spartan's checks, run the command "/spartan menu", click the bottom item named "Management" and finally hover above the colored items of the menu to view the description of each item. If a check's preventions or punishments are not yet functional due to missing data, they will appear with a yellow warning in the end of the item's description, giving you a hint on what's happening.
