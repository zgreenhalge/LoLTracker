#LoLTracker

###THIS IS AN OUT OF DATE VERSION

######Work has frozen during the building of a functional GUI - if you're interested in a (functional) command line version, contact me and I can provide it. I'd rather not distribute it openly since the code base behind it is fragmented & gross - this project has been in the idea phase for too long and changed as Riot's API has changed

__________________________________________________________________________________________
A multi-funtional tool for tracking friends and generating statistics in League of Legends by utilising Riot Game's API.
Once you download the app, just run it once a day after you play your games (or after every game, I'm not your mother) and it will access and catalogue your games. By building up a large pool of games that you/your friends/whoever you want to track has played, you will be able to look at patterns, tendencies, and generate statistics.

##WHEN YOU ARE DONE, MAKE SURE YOU TYPE QUIT - OTHERWISE IT WILL NOT SAVE THE DATA AND YOU'LL HAVE TO RETRIEVE IT ALL OVER AGAIN

Currently only available as a text-based interface.
__________________________________________________________________________________________
<b><u>Upcoming features:</b></u>
- auto save on update (so you can just exit without typing quit)
- ability to discover statistics through a custom query engine
- current game summary
- link generator to allow you to access the raw JSON returned from Riot API calls
- ability to view statistics of items and champions, not just players
- an actual GUI!

|Current commands|Function|
|----------------|--------|
|add [summoner]| adds the specified summoner to the list of tracked summoners|
|list games -c -r| lists all catalogued games in order of retrieval, complete and ranked only flags|
|list (summoners)(players)| displays a list of all tracked summoenrs|
|summary -s [summonerName]| displays a summary of the specified summoner|
|summary -g [gameId]| displays a summary of the specified game|

______________________________________________________________________________________________________________________




> This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.


