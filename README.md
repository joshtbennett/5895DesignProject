# 5895DesignProject

### Project Idea

RGB Receivers(get the corresponding color light to these receivers)
 
Emitters(RGB or combinations (magenta, cyan, yellow, white))
 
RGB Mirrors(light of the same color passes through, all others are reflected)
    - all colors except red reflect off of a red mirror for example
    - stackable to create magenta, cyan, yellow, or white mirrors
    - if yellow light hits a red mirror, green is reflected and red passes through
    - dotted line can show the outgoing angle on the first mirror after an emitter
 
Walls (every color reflects) are randomly generated to be obstacles in the level
    - maybe create levels or tiers to determine how many walls are created for the level
 
Receiver's, emitter's and mirror's angles may be adjusted by the player
 
Score is calculated based on
    - how many mirrors used ( -5 points per mirror)
    - how much time the level took to be completed(-1 point per couple seconds)
    - how many attempts(or how many trys the level took, 1-5 tries = no penalty, 6-10 = -5 points, 11-15 = -10 points, etc)
            (once mirrors are set up the player presses play and the light is turned on, if a receiver doesnt receive its correct color the player must reattempt)
    - potentially add some kind of coin that light must travel through to collect (+ 10 points per coin)