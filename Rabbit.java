public class Rabbit extends Animal {

    private boolean canSeeFoxNow = false, haveSeenBush = false;
    private int directionToFox, directionToBush, distanceToBush, currentDirection;

    public Rabbit(Model model, int row, int column) {
        super(model, row, column);
    }
  // Boolean function that returns true if the provided direction is a safe move.
  boolean isSafe(int direction) {
    // if the fox is directly infront of the rabbit
    if (look(direction) == Model.FOX && distance(direction) <= 2)
      return false;
    if (look(Model.turn(direction, -1)) == Model.FOX && distance(Model.turn(direction, -1)) == 1)
      return false;
    if (look(Model.turn(direction, 1)) == Model.FOX && distance(Model.turn(direction, 1)) == 1)
      return false;
    return true;
  }


    int decideMove() {

      distanceToBush = 100;
      canSeeFoxNow = false;
      // Look for the fox and any bushes
      for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
        if (look(i) == Model.FOX) {
            canSeeFoxNow = true;
            directionToFox = i;
        }
        else if (look(i) == Model.BUSH && distance(i) <= distanceToBush) {
            distanceToBush = distance(i);
            directionToBush = i;
            haveSeenBush = true;
        }
      }
      // Logic Tree: 
      // The fox is visable to the rabbit
        if (canSeeFoxNow) {
          // There is at least one bush visable to the rabbit
          if (haveSeenBush) {
            if (distanceToBush > 1)
              return directionToBush;
            // The bush is next to the rabbit  
            else { 
              // The rabbit is not N,E,S, or W with respect to the closest bush
              if (directionToBush == Model.NE || directionToBush == Model.SE) {
                return Model.turn(directionToBush, -2);
              } else if (directionToBush == Model.NW || directionToBush == Model.SW) {
                return Model.turn(directionToBush, 2);
              }
                // If possible, move in a diamond around the bush
                if (canMove(Model.turn(directionToBush, -1)) && isSafe(Model.turn(directionToBush, -1)))
                  return Model.turn(directionToBush, -1);
                else if (canMove(Model.turn(directionToBush, 1)) && isSafe(Model.turn(directionToBush, 1)))
                  return Model.turn(directionToBush, 1);
                // Stay still if the other moves are not possible
                else
                  return Model.STAY;
            }
          }
          // If a bush is not visable, run from the fox
          else
            return Model.turn(directionToFox, 5);
        }
        // The fox is not visable to the rabbit
        else { 
          // There is at least one bush visable to the rabbit
          if (haveSeenBush) {
            if (distanceToBush > 1)
              return directionToBush;
            // The rabbit is not N,E,S, or W with respect to the closest bush
            if (directionToBush == Model.NE || directionToBush == Model.SE) {
              return Model.turn(directionToBush, -2);
            } else if (directionToBush == Model.NW || directionToBush == Model.SW) {
              return Model.turn(directionToBush, 2);
            }
            // Stay still since no threat exists from fox
            else
              return Model.STAY;
        }
          // There is no bush visable to the rabbit
          else { 
            currentDirection = Model.random(Model.MIN_DIRECTION, Model.MAX_DIRECTION);
            for (int i = 0; i < 8; i++) {
                if (canMove(currentDirection))
                    return currentDirection;
                else
                    currentDirection = Model.turn(currentDirection, 1);
            }
          }
          // No other options available
          return Model.STAY;
        }
    }
}
