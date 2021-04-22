Christopher Deal
Section OL

Extra Credit:
1) All the Rooms light up as a target allowing you to select anywhere within them

2)Also Room stays lit when Human (^Player) is 'Making a Suggestion', slight mod to game rules the Human can
   cancel and change the Room and highlighting will switch to that Room.  This way you're not locked into 
   a Room if you realize you already had the Room Card.
   
3) 2 Dice were implemented because it makes the gameplay so much better which ensures the Computers can't keep
you from getting to a Room by repeatedly calling you back.  Whole map is accessible on +9 rolls if in Center of
   map (Engine Room)
   
4) Dice icons were used to implement the dice, most of the icons shown will be "doubles" but I did this
b/c this clearly conveys to the human player what is going on and doubles have no added benefit in the game
   The dice add so much to the game without the clunky RollBox.
   
5) Multithreaded implementation was added to improve the functionality of the game's initial splash screen and also
to improve when the game exits upon winning or losing.  The program will bring up a screen
   say whether you won or lost then the game control panel will change and show you the winning cards and 
   call you a winner or tell you the game is over and does slightly different for Computer win/lose and this will
   show for 5 seconds and then EXIT.
   
6) My AI logic can have the Computer lose, and the game will continue appropriately

7) Art: Stars were added that make a pretty cool effect to Clue in Space. Dynamic resizing
of window allowed and keeps ship centered. Custom coloring and effects were added to the map drawing
   such as the cool Doorways and secret passages. Background and coloring added to both panels to
   focus the game and integrates with the Space design.  Also, all seen Human (^Player) Cards also light up with the Computer's Color who shows
   Card.

8) Sound was added for game beginning, if Card is unable to be disproved, winning, & also for losing.

9) .jar was made

***The sound plays perfectly in my Windows Box, but I couldn't set my default sound card for the life of me in 
   Ubuntu (Linux Mint), and I couldn't get my .jar file to play in Ubuntu (even after modifying the executable bit) but they play fine anywhere else.
   I have tested all this thoroughly but if you happen to play it in Mint and don't have your default sound card set
   my program won't act properly and displays undefined behavior.*** <~~Probably Computer Specific 

***When I play program in Windows 10 I have to maximize the window, in Linux it scaled to screen
appropriately***