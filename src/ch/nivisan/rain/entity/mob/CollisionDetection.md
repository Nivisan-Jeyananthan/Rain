# Explaination Collision detection

Credit goes to @3asdfjfj34fshasmdsds6 for the awsome explaination!
Edited slightly.

## Main

To understand how the algorithm works, it helps to see what actually what goes on with the variable c.
Here, I made a little table that shows that.

| c | c % 2 | c / 2 |
|---|-------|-------|
| 0 | 0     | 0     |
| 1 | 1     | 0     |
| 2 | 0     | 1     |
| 3 | 1     | 1     |

Now, keep these numbers in your mind, because we'll look at them later.
So, let's look at the code, shall we?

```java
int xt = ((x + xa) + (c % 2)) / 16;
int yt = ((y + ya) + (c / 2)) / 16;
```

> **x** here is the player position at coordinate **x**.   
> While **xa** refers to the players movement in the **x** direction, where it can be 1,0 or -1.   
> Depending on the movement.  
> Same applies to y and ya

(I removed the little adjustments, just look at these variables right now)

Firstly, we can see that the entire expression, `((x + xa) + (c % 2))`, is divided by 16 (same as shifted 4 right),
thereby converting the **pixel coordinates** into **tile coordinates**.  
(x + xa) is the x-position of where you would be. Now what is (c % 2)?  
If we look at the table I mentioned earlier, you can see the values of `(c % 2)` can be **0** or **1**.

1. iteration : So, if `(c % 2)` is **0**, well, nothing happens.
2. Iteration : You do the calculation as always. But if `(c % 2)` is **1**, then you add **1** to `(x + xa)`.

If you move the value by 1 pixel, it's *possible* that you are checking another tile.     
But **not always**. For example, if `(x + xa)` was 14, then we'd shift it by 1, giving us 15.  
Dividing 15 by 16 would give us 0, as would 14/16.      
There is no change.   
But if `(x + xa)` was instead 15,you would divide (15 + 1) by 16, giving us 1, instead of 0.

Now, let's talk about the adjustments (the multipliers 10 here). Something like this:

```java
int xt = ((x + xa) + (c % 2) * 10) / 16;
int yt = ((y + ya) + (c / 2) * 10) / 16;
```

In this case, we do `(c % 2)`, then multiply that value by **10**.   
So, when `(c % 2)` results into 1, instead of just shifting the pixel by 1 spot, you shift it by 10 instead. A big
difference.     
However, note that if `(c % 2)` or `(c / 2)` is **0**, the multiplier doesn't make any difference.  
0 multiplied by anything is always 0. That's important, because we also want to check (x + xa) without any changes.

Now, let's look at the code as a whole. Let's look at the big picture, and see what's really going behind the scene.

```java

for(int c = 0;
c< 4;c++){

int xt = ((x + xa) + (c % 2)) / 16;
int yt = ((y + ya) + (c / 2)) / 16;

         if(level.

getTile(xt, yt).

solid())
        return true;
        }
        return false;
```

(Again, its simplified, so we can get to the good stuff)  
So we have a for-loop that goes through the values of c; **0**, **1**, **2**, and **3**.    
When c is **0**, `(c % 2)` and `(c / 2)` are both **0**, so we just check the raw values of `(x + xa)` and
`(y + ya)`.   
Also, remember that **x** and **y** are the fields located in the `Entity` class, and stores the top-left location of
the entity on the screen.     
The next value for c is 1. This means that (c % 2) is 1. So, (x + xa) is shifted to the right by 1.      
When c is 2, `(c % 2)` is 0, but `(c / 2)` is 1. So, we add 1 to `(y + ya)`.   
The last value of c- 3, makes both (c % 2) and (c / 2) equal to 1.   
So, we add 1 to both `(x + xa)` aswell as `(y + ya)`

Now, I think that the best way to explain this is with a mental picture. Here's my take on it:

```
   ~~~~~~~~~~~~~~~~~~
        X = (x + xa);
        Y = (y + ya);
   ~~~~~~~~~~~~~~~~~~

     (X, Y)                                      (X + 1, Y)
         *  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  *
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         |                                               |
         *  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  *
(X, Y + 1)                                (X + 1, Y + 1)
```

The for-loop checks all of the tiles at these positions, and returns true if that tile is solid.  
If that tile isn't solid, then check the next point.

So, yeah, I hoped that helped you understand the collision() method more clearly.   
Writing this certainly did help me, well good luck to you, and thanks for reading.

## Filling in

So let's take the original formula for a tour now shall we.  
Our player just spawned in and wants to move to his right `(x+1)`.  
Let's see what checks would need to be done so that our player can actually move right and does not "hurt" himself.
He spawned at the center of the map (50x50) tiles or (800x800) pixels.  
When our player looks to his right from `x = 400` and `y = 400`, there is a hedge (`x = 416, y = 400`).  
So his path is blocked.   
... In his eyes it looks walkable, so how does our player know ?   
He starts at `400` and takes a step into the right direction `x = x + xMove`.

```
newPosition = 401
```

Our player has a finite amount of rocks at his disposal to throw.
Starting from this position `newPosition` our player will do the following:

The player can only throw stones of certain sizes **1**, otherwise he cannot see them.  
So first he takes a stone tries to break them in half of 2 and succeeds.  
He takes the smaller half of these rocks or intends to but ends up picking nothing.

```
stone = 0
halves = 2

stone / halves = 0
```

Then realize that he damaged too much of it in the process
... But there is no small one, so he tapes stones onto nothing.
So our player tries to throw the small stone and puts in a little more power (adjustment) and ends up doing nothing.

```
power = 10
stone = 0

combinedStone = (stone / halves) * 10
```

Because it's really windy `wind = 3` (ignore physics) they glide these rocks 3 positions further.

```
power = 10
wind = 3

stonePositionByThrow = (stone / halves) * power
stonePosition = stonePositionByThrow + wind
```

Sometimes they land on the same tile as he is and he is clumsy so he drops a few on the same spot.   
Other times they land on the other ground and he finds out that it is **solid** one corner of the hedge is at :  
(416)
int yt = ((y + ya) + (c / 2)) / 16;