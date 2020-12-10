# Dice Blackjack (Back)

The is the Java (Spring) back-end of a version of Blackjack played with dice. It's a web socket server that handles clients playing the game. Each message (join, hit, stand, etc.) results in the sending of updated game data back to the clients for re-rendering. I built it so that I could host a working version of the app on Netlify (the back-end is hosted on Heroku).

You can see the app in action [here](https://affectionate-ride-7883fd.netlify.app/).