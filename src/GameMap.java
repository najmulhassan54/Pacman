public class GameMap {

    public static String[] getMap(Difficulty level) {
        switch (level) {

            case EASY: return new String[]{
                "XXXXXXXXXXXXXXXXXXX",
                "X        P        X",
                "X XXXXX  X  XXXXX X",
                "X XX     X     XX X",
                "X XX     X     XX X",
                "X        X        X",
                "X X      X      X X",
                "X X      X      X X",
                "X X             X X",
                "X X      X      X X",
                "X        X        X",
                "X XX     X     XX X",
                "X XX     X     XX X",
                "X XXXXX  X  XXXXX X",
                "X        bp       X",
                "XXXXXXXXXXXXXXXXXXX"
            };

            case MEDIUM: return new String[]{
                "XXXXXXXXXXXXXXXXXXX",
                "X XX   X     X   XX",
                "X XX XXX X XXX XX X",
                "X   b             X",
                "X XX X XXXXX X XX X",
                "X    X   P   X    X",
                "XXXX XXXX XXXX XXXX",
                "X    o       p    X",
                "X XX X XXXXX X XX X",
                "X        r        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "XXXXXXXXXXXXXXXXXXX"
            };

            case HARD: return new String[]{
                "XXXXXXXXXXXXXXXXXXX",
                "X XX XXX X XXX XX X",
                "X    b   X   o    X",
                "X XX X XXXXX X XX X",
                "X         P       X",
                "XXXX XXXX XXXX XXXX",
                "X  p X  X  X  X   X",
                "X XX X XXXXX X XX X",
                "X r  XXX X XXX    X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "XXXX X XXXXX X XXXX",
                "X XX XXX X XXX XX X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
            };

            default: return new String[]{};
        }
    }
}
