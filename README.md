# pcj-helper
A helper for the solitaire rounds in Pocket Card Jockey, which finds the longest chains you can make by means of a brute-force-esque algorithm.

At the present time, this helper assumes that you make the exact moves that it asks you to make. Additionally, if multiple longest chains are found, the helper will currently default to the first one it found.

This is still very much a work-in-progress, but it is functioning nonetheless. Some to-do items include:
* support for Farseer skill
* support for Jokers
* user selection of which path to take if multiple longest are found

Given that the program currently operates with every turn in a sandbox of sorts, it is mostly recommended that this is used for the first turn in a solitaire game.

## Usage
The .java files provided are the source code. Download them, compile them, and run. pcj-helper is a command line based program.

Upon starting the program, it will ask you to enter the number of columns of cards on the table. It will then ask you column-by-column to enter the cards, from the top-most (i.e. playable) card to the bottom card. As of right now, the best way I have found to do this is to make liberal use of the "Home" button between columns so that you do not lose much of your time while you enter the cards.

As an example, if the table looks like this:

``A- 10-2- 5- 6- 3``  
``9- 2- 4- 10-Q- K``  
``7- J- 8- 9- J- K``  
``---5- 7- 6- 4---``

Your inputs for the program would look like the following:

> 6  
> 7 9 A  
> 5 J 2 10  
> 7 8 4 2  
> 6 9 10 5  
> 4 J Q 6  
> K K 3

At this point, the program will run its algorithm and will tell you the cards to play, also indicating the column that the card resides in.  At the end, it will ask you what the next card is, i.e. what the next card you draw from the stock is, and from there will run the algorithm once again from its ending point with the new card.
