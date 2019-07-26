package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class WorldGeneration implements java.io.Serializable {

    private Random RANDOM;

    private int WIDTH;
    private int HEIGHT;
    TETile[][] world;

    private int numOfRooms = 0;
    private int[] currPos;
    int[] playeronePos = new int[2];
    int[] playertwoPos = new int[2];
    private String currDirection;
    private int[][] upInitialCoords;
    private int[][] downInitialCoords;
    private int[][] leftInitialCoords;
    private int[][] rightInitialCoords;
    private int[] clearedDimensions = new int[6];
    private int nextNumOfHallways;
    private int[] chosenCoord = new int[2];
    private int[][] wallCoordsInDir;
    private int[] randHallwaySize = new int[2];
    private String[] unusedInitialDirections = {"up", "down", "right", "left"};
    private int usedIndex = 0;
    private String[] usedDirections = new String[4];
    private boolean firstRoomHallway = true;
    private int[] roomSize = new int[2];
    private int[] initialCoord = new int[2];
    private String lastThingAdded;
    private boolean hallwaytwofail = false;

    public WorldGeneration(TETile[][] t, Random r, int w, int h) {
        RANDOM = r;
        world = t;
        WIDTH = w;
        HEIGHT = h;
    }

    public int[] roomSize() {
        int[] roomSize1 = new int[2];
        roomSize1[0] = RandomUtils.uniform(RANDOM, 3, 10);
        roomSize1[1] = RandomUtils.uniform(RANDOM, 4, 10);
        return roomSize1;
    }

    public int[] roomPlacement() {
        int[] roomPlacement = new int[2];
        roomPlacement[0] = RandomUtils.uniform(RANDOM, 20, WIDTH - 20);
        roomPlacement[1] = RandomUtils.uniform(RANDOM, 20, HEIGHT - 20);
        currPos = roomPlacement;
        return roomPlacement;
    }

    public int[] floorPlacement(int[] roomPlacement) {
        int[] floorPlacement = new int[2];
        floorPlacement[0] = roomPlacement[0] + 1;
        floorPlacement[1] = roomPlacement[1] + 1;
        return floorPlacement;
    }

    public int[] floorSize(int[] rSize) {
        int[] floorSize = new int[2];
        floorSize[0] = rSize[0] - 2;
        floorSize[1] = rSize[1] - 2;
        return floorSize;
    }

    public int[][] coordsOfWallInDirection(int[] rPlacement, int[] rSize, String direction) {
        int counter;
        int[][] coordsOfWall;
        if (direction.equals("left")) {
            coordsOfWall = new int[rSize[1] - 2][2];
            counter = 0;
            while (counter < rSize[1] - 2) {
                for (int j = 1; j < rSize[1] - 1; j++) {
                    coordsOfWall[counter][0] = rPlacement[0];
                    coordsOfWall[counter][1] = rPlacement[1] + j;
                    counter += 1;
                }
            }
        } else if (direction.equals("up")) {
            coordsOfWall = new int[rSize[0] - 2][2];
            counter = 0;
            while (counter < rSize[0] - 2) {
                for (int k = 1; k < rSize[0] - 1; k++) {
                    coordsOfWall[counter][0] = rPlacement[0] + k;
                    coordsOfWall[counter][1] = rPlacement[1] + (rSize[1] - 1);
                    counter += 1;
                }
            }
        } else if (direction.equals("right")) {
            coordsOfWall = new int[rSize[1] - 2][2];
            counter = 0;
            while (counter < rSize[1] - 2) {
                for (int n = 1; n < rSize[1] - 1; n++) {
                    coordsOfWall[counter][0] = rPlacement[0] + (rSize[0] - 1);
                    coordsOfWall[counter][1] = rPlacement[1] + n;
                    counter += 1;
                }
            }
        } else {
            coordsOfWall = new int[rSize[0] - 2][2];
            counter = 0;
            while (counter < rSize[0] - 2) {
                for (int m = 1; m < rSize[0] - 1; m++) {
                    coordsOfWall[counter][0] = rPlacement[0] + m;
                    coordsOfWall[counter][1] = rPlacement[1];
                    counter += 1;
                }
            }
        }
        return coordsOfWall;
    }

    public boolean areaClearForRoom(int[] startingPos, String direction) {
        int[] copyPos = new int[2];
        copyPos[0] = startingPos[0];
        copyPos[1] = startingPos[1];
        int height;
        int width;
        int[] size = new int[2];
        boolean areaClear = true;
        if (direction.equals("up")) {
            int left = RandomUtils.uniform(RANDOM, 2, 5);
            int right = RandomUtils.uniform(RANDOM, 2, 5);
            height = RandomUtils.uniform(RANDOM, 4, 10);
            width = left + right + 1;
            size[0] = width;
            size[1] = height;
            copyPos[0] = copyPos[0] - left;
            for (int i = copyPos[0]; i < copyPos[0] + width; i++) {
                for (int j = copyPos[1]; j < copyPos[1] + height; j++) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
            if (areaClear) {
                clearedDimensions[0] = left;
                clearedDimensions[1] = right;
                clearedDimensions[2] = height;
                clearedDimensions[3] = width;
                clearedDimensions[4] = -1;
                clearedDimensions[5] = -1;
            }
        } else if (direction.equals("down")) {
            int left = RandomUtils.uniform(RANDOM, 2, 5);
            int right = RandomUtils.uniform(RANDOM, 2, 5);
            height = RandomUtils.uniform(RANDOM, 4, 10);
            width = left + right + 1;
            size[0] = width;
            size[1] = height;
            copyPos[0] = copyPos[0] - left;
            for (int i = copyPos[0]; i < copyPos[0] + width; i++) {
                for (int j = copyPos[1]; j > copyPos[1] - height - 1; j--) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
            if (areaClear) {
                clearedDimensions[0] = left;
                clearedDimensions[1] = right;
                clearedDimensions[2] = height;
                clearedDimensions[3] = width;
                clearedDimensions[4] = -1;
                clearedDimensions[5] = -1;
            }
        } else {
            areaClear = areaClearForRoomtwo(startingPos, direction);
        }
        return areaClear;
    }

    public boolean areaClearForRoomtwo(int[] startingPos, String direction) {
        int[] copyPos = new int[2];
        copyPos[0] = startingPos[0];
        copyPos[1] = startingPos[1];
        int height;
        int width;
        int[] size = new int[2];
        boolean areaClear = true;
        if (direction.equals("left")) {
            int up = RandomUtils.uniform(RANDOM, 2, 5);
            int down = RandomUtils.uniform(RANDOM, 2, 5);
            width = RandomUtils.uniform(RANDOM, 4, 10);
            height = up + down + 1;
            size[0] = width;
            size[1] = height;
            copyPos[1] = copyPos[1] - down;
            for (int i = copyPos[0]; i > copyPos[0] - width; i--) {
                for (int j = copyPos[1]; j < copyPos[1] + height; j++) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
            if (areaClear) {
                clearedDimensions[0] = -1;
                clearedDimensions[1] = -1;
                clearedDimensions[2] = height;
                clearedDimensions[3] = width;
                clearedDimensions[4] = up;
                clearedDimensions[5] = down;
            }
        } else {
            int up = RandomUtils.uniform(RANDOM, 2, 5);
            int down = RandomUtils.uniform(RANDOM, 2, 5);
            width = RandomUtils.uniform(RANDOM, 4, 10);
            height = up + down + 1;
            size[0] = width;
            size[1] = height;
            copyPos[1] = copyPos[1] - down;
            for (int i = copyPos[0]; i < copyPos[0] + width + 2; i++) {
                for (int j = copyPos[1]; j < copyPos[1] + height; j++) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
            if (areaClear) {
                clearedDimensions[0] = -1;
                clearedDimensions[1] = -1;
                clearedDimensions[2] = height;
                clearedDimensions[3] = width;
                clearedDimensions[4] = up;
                clearedDimensions[5] = down;
            }
        }
        return areaClear;
    }

    public void addRoom(int[] roomPlacement, int[] inputRoomSize, String direction) {
        if (numOfRooms == 0) {
            try {
                for (int i = roomPlacement[0]; i < roomPlacement[0] + inputRoomSize[0]; i++) {
                    for (int j = roomPlacement[1]; j < roomPlacement[1] + inputRoomSize[1]; j++) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] floorPlacement = floorPlacement(roomPlacement);
                int[] floorSize = floorSize(inputRoomSize);
                for (int i = floorPlacement[0]; i < floorPlacement[0] + floorSize[0]; i++) {
                    for (int j = floorPlacement[1]; j < floorPlacement[1] + floorSize[1]; j++) {
                        world[i][j] = Tileset.FLOOR;
                    }
                }
                upInitialCoords = coordsOfWallInDirection(roomPlacement, inputRoomSize, "up");
                downInitialCoords = coordsOfWallInDirection(roomPlacement, inputRoomSize, "down");
                leftInitialCoords = coordsOfWallInDirection(roomPlacement, inputRoomSize, "left");
                rightInitialCoords = coordsOfWallInDirection(roomPlacement, inputRoomSize, "right");
            } catch (ArrayIndexOutOfBoundsException e) {
                roomPlacement = roomPlacement();
                currPos = roomPlacement;
                addRoom(roomPlacement, inputRoomSize, direction);
            }
        } else {
            addRoomtwo(roomPlacement, direction);
        }
        numOfRooms += 1;
    }

    public void addRoomtwo(int[] roomPlacement, String direction) {
        int width; int height; int[] size = new int[2];
        if (direction.equals("up")) {
            int left = clearedDimensions[0];
            height = clearedDimensions[2]; width = clearedDimensions[3];
            size[0] = width; size[1] = height;
            int[] replace = new int[]{roomPlacement[0], roomPlacement[1]};
            roomPlacement[0] = roomPlacement[0] - left;
            for (int i = roomPlacement[0]; i < roomPlacement[0] + width; i++) {
                for (int j = roomPlacement[1]; j < roomPlacement[1] + height; j++) {
                    world[i][j] = Tileset.WALL;
                }
            }
            world[replace[0]][replace[1]] = Tileset.FLOOR;
            for (int i = roomPlacement[0] + 1; i < roomPlacement[0] + (width - 1); i++) {
                for (int j = roomPlacement[1] + 1; j < roomPlacement[1] + (height - 1); j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
            currPos[0] = roomPlacement[0]; currPos[1] = roomPlacement[1];
            roomSize = size;
        } else if (direction.equals("down")) {
            int left = clearedDimensions[0]; height = clearedDimensions[2];
            width = clearedDimensions[3]; size[0] = width; size[1] = height;
            int[] replace = new int[]{roomPlacement[0], roomPlacement[1]};
            roomPlacement[0] = roomPlacement[0] - left;
            for (int i = roomPlacement[0]; i < roomPlacement[0] + width; i++) {
                for (int j = roomPlacement[1]; j > roomPlacement[1] - height; j--) {
                    world[i][j] = Tileset.WALL;
                }
            }
            world[replace[0]][replace[1]] = Tileset.FLOOR;
            for (int i = roomPlacement[0] + 1; i < roomPlacement[0] + (width - 1); i++) {
                for (int j = roomPlacement[1] - 1; j > roomPlacement[1] - (height - 1); j--) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
            currPos[0] = roomPlacement[0]; currPos[1] = roomPlacement[1] - height + 1;
            roomSize = size;
        } else if (direction.equals("left")) {
            int down = clearedDimensions[5];
            width = clearedDimensions[3]; height = clearedDimensions[2];
            size[0] = width; size[1] = height;
            int[] replace = new int[]{roomPlacement[0], roomPlacement[1]};
            roomPlacement[1] = roomPlacement[1] - down;
            for (int i = roomPlacement[0]; i > roomPlacement[0] - width; i--) {
                for (int j = roomPlacement[1]; j < roomPlacement[1] + height; j++) {
                    world[i][j] = Tileset.WALL;
                }
            }
            world[replace[0]][replace[1]] = Tileset.FLOOR;
            for (int i = roomPlacement[0] - 1; i > (roomPlacement[0] - width) + 1; i--) {
                for (int j = roomPlacement[1] + 1; j < roomPlacement[1] + (height - 1); j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
            currPos[0] = roomPlacement[0] - width + 1; currPos[1] = roomPlacement[1];
            roomSize = size;
        } else if (direction.equals("right")) {
            int down = clearedDimensions[5];
            width = clearedDimensions[3]; height = clearedDimensions[2];
            size[0] = width; size[1] = height;
            int[] replace = new int[]{roomPlacement[0], roomPlacement[1]};
            roomPlacement[1] = roomPlacement[1] - down;
            for (int i = roomPlacement[0]; i < roomPlacement[0] + width + 2; i++) {
                for (int j = roomPlacement[1]; j < roomPlacement[1] + height; j++) {
                    world[i][j] = Tileset.WALL;
                }
            }
            world[replace[0]][replace[1]] = Tileset.FLOOR;
            for (int i = roomPlacement[0] + 1; i < roomPlacement[0] + (width + 1); i++) {
                for (int j = roomPlacement[1] + 1; j < roomPlacement[1] + (height - 1); j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
            currPos[0] = roomPlacement[0]; currPos[1] = roomPlacement[1];
            roomSize = size;
        }
    }

    public int randomNumOfHallways() {
        int randomNumOfHallways = RandomUtils.uniform(RANDOM, 1, 3);
        return randomNumOfHallways;
    }

    public boolean areaClearForHallway(int[] size, int[] wallCoord, String direction) {
        boolean areaClear = true;
        int[] newPos = new int[2];
        newPos[0] = wallCoord[0];
        newPos[1] = wallCoord[1];
        if (direction.equals("up")) {
            newPos[1] += 1;
            newPos[0] -= 1;
            for (int i = newPos[0]; i < newPos[0] + size[0]; i++) {
                for (int j = newPos[1]; j < newPos[1] + size[1]; j++) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
        } else if (direction.equals("down")) {
            newPos[1] -= 1;
            newPos[0] -= 1;
            for (int i = newPos[0]; i < newPos[0] + size[0]; i++) {
                for (int j = newPos[1]; j > newPos[1] - size[1]; j--) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
        } else if (direction.equals("right")) {
            newPos[0] += 1;
            newPos[1] += 1;
            for (int i = newPos[0]; i < newPos[0] + size[0]; i++) {
                for (int j = newPos[1]; j > newPos[1] - size[1]; j--) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
        } else {
            newPos[0] -= 1;
            newPos[1] += 1;
            for (int i = newPos[0]; i > newPos[0] - size[0]; i--) {
                for (int j = newPos[1]; j > newPos[1] - size[1]; j--) {
                    try {
                        if (world[i][j] != Tileset.NOTHING) {
                            areaClear = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        areaClear = false;
                    }
                }
            }
        }
        return areaClear;
    }

    public String randomHallwayDirection() {
        if (firstRoomHallway) {
            String[] directions = {"up", "down", "left", "right"};
            int randomIndex = RandomUtils.uniform(RANDOM, directions.length);
            String randomDirection = directions[randomIndex];
            currDirection = randomDirection;
            return randomDirection;
        } else {
            String[] directions = new String[3];
            if (currDirection.equals("up")) {
                directions[0] = "up";
                directions[1] = "left";
                directions[2] = "right";
            } else if (currDirection.equals("down")) {
                directions[0] = "down";
                directions[1] = "left";
                directions[2] = "right";
            } else if (currDirection.equals("left")) {
                directions[0] = "up";
                directions[1] = "left";
                directions[2] = "down";
            } else {
                directions[0] = "up";
                directions[1] = "down";
                directions[2] = "right";
            }
            int randomIndex = RandomUtils.uniform(RANDOM, directions.length);
            String randomDirection = directions[randomIndex];
            currDirection = randomDirection;
            return randomDirection;
        }
    }

    public int[] hallwaySize(String direction) {
        int[] hallwaySize = new int[2];
        if (direction.equals("left") || direction.equals("right")) {
            hallwaySize[0] = RandomUtils.uniform(RANDOM, 3, 10);
            hallwaySize[1] = 3;
        } else {
            hallwaySize[0] = 3;
            hallwaySize[1] = RandomUtils.uniform(RANDOM, 3, 10);
        }
        return hallwaySize;
    }

    public boolean containsDirection(String direction) {
        boolean containsDirection = false;
        for (int i = 0; i < usedDirections.length; i++) {
            if (usedDirections[i] == direction) {
                containsDirection = true;
            }
        }
        return containsDirection;
    }

    public void addHallway(int[] sPos, int[] hSize, String dir) {
        world[sPos[0]][sPos[1]] = Tileset.FLOOR;
        int[] newStartingPos = new int[2];
        newStartingPos[0] = sPos[0]; newStartingPos[1] = sPos[1];
        int[] endingPos = new int[2];
        if (firstRoomHallway && !containsDirection(dir)) {
            usedDirections[usedIndex] = dir;
            for (int j = 0; j < 4; j += 1) {
                if (unusedInitialDirections[j] == dir) {
                    unusedInitialDirections[j] = null;
                }
            }
            usedIndex += 1; firstRoomHallway = false;
        }
        if (dir.equals("up")) {
            newStartingPos[1] += 1; newStartingPos[0] -= 1;
            for (int i = newStartingPos[0]; i < newStartingPos[0] + hSize[0]; i++) {
                for (int j = newStartingPos[1]; j < newStartingPos[1] + hSize[1]; j++) {
                    world[i][j] = Tileset.WALL;
                }
            }
            int[] hallwayPlacement = new int[2];
            hallwayPlacement[0] = newStartingPos[0] + 1;
            hallwayPlacement[1] = newStartingPos[1];
            for (int j = hallwayPlacement[1]; j < hallwayPlacement[1] + hSize[1]; j++) {
                world[hallwayPlacement[0]][j] = Tileset.FLOOR;
            }
            endingPos[0] = hallwayPlacement[0]; endingPos[1] = hallwayPlacement[1] + hSize[1];
            currPos = endingPos;
        } else if (dir.equals("down")) {
            newStartingPos[1] -= 1; newStartingPos[0] -= 1;
            for (int i = newStartingPos[0]; i < newStartingPos[0] + hSize[0]; i++) {
                for (int j = newStartingPos[1]; j > newStartingPos[1] - hSize[1]; j--) {
                    world[i][j] = Tileset.WALL;
                }
            }
            int[] hallwayPlacement = new int[2];
            hallwayPlacement[0] = newStartingPos[0] + 1;
            hallwayPlacement[1] = newStartingPos[1];
            for (int j = hallwayPlacement[1]; j > hallwayPlacement[1] - hSize[1]; j--) {
                world[hallwayPlacement[0]][j] = Tileset.FLOOR;
            }
            endingPos[0] = hallwayPlacement[0];
            endingPos[1] = hallwayPlacement[1] - hSize[1];
            currPos = endingPos;
        } else if (dir.equals("right")) {
            newStartingPos[0] += 1; newStartingPos[1] += 1;
            for (int i = newStartingPos[0]; i < newStartingPos[0] + hSize[0]; i++) {
                for (int j = newStartingPos[1]; j > newStartingPos[1] - hSize[1]; j--) {
                    world[i][j] = Tileset.WALL;
                }
            }
            int[] hallwayPlacement = new int[2];
            hallwayPlacement[0] = newStartingPos[0];
            hallwayPlacement[1] = newStartingPos[1] - 1;
            for (int j = hallwayPlacement[0]; j < hallwayPlacement[0] + hSize[0]; j++) {
                world[j][hallwayPlacement[1]] = Tileset.FLOOR;
            }
            endingPos[0] = hallwayPlacement[0] + hSize[0];
            endingPos[1] = hallwayPlacement[1];
            currPos = endingPos;
        } else {
            newStartingPos[0] -= 1;
            newStartingPos[1] += 1;
            for (int i = newStartingPos[0]; i > newStartingPos[0] - hSize[0]; i--) {
                for (int j = newStartingPos[1]; j > newStartingPos[1] - hSize[1]; j--) {
                    world[i][j] = Tileset.WALL;
                }
            }
            int[] hallwayPlacement = new int[2];
            hallwayPlacement[0] = newStartingPos[0];
            hallwayPlacement[1] = newStartingPos[1] - 1;
            for (int j = hallwayPlacement[0]; j > hallwayPlacement[0] - hSize[0]; j--) {
                world[j][hallwayPlacement[1]] = Tileset.FLOOR;
            }
            endingPos[0] = hallwayPlacement[0] - hSize[0];
            endingPos[1] = hallwayPlacement[1];
            currPos = endingPos;
        }
    }

    public int[] randomInitialCoord() {
        firstRoomHallway = true;
        int randomIndex = RandomUtils.uniform(RANDOM, unusedInitialDirections.length);
        while (unusedInitialDirections[randomIndex] == null) {
            randomIndex = RandomUtils.uniform(RANDOM, unusedInitialDirections.length);
        }
        String chosenDirection = unusedInitialDirections[randomIndex];
        usedDirections[usedIndex] = chosenDirection;
        unusedInitialDirections[randomIndex] = null;
        usedIndex += 1;
        currDirection = chosenDirection;
        int[] randomInitialCoord;
        if (chosenDirection.equals("up")) {
            randomIndex = RandomUtils.uniform(RANDOM, upInitialCoords.length);
            randomInitialCoord = upInitialCoords[randomIndex];
        } else if (chosenDirection.equals("down")) {
            randomIndex = RandomUtils.uniform(RANDOM, downInitialCoords.length);
            randomInitialCoord = downInitialCoords[randomIndex];
        } else if (chosenDirection.equals("left")) {
            randomIndex = RandomUtils.uniform(RANDOM, leftInitialCoords.length);
            randomInitialCoord = leftInitialCoords[randomIndex];
        } else {
            randomIndex = RandomUtils.uniform(RANDOM, rightInitialCoords.length);
            randomInitialCoord = rightInitialCoords[randomIndex];
        }
        return randomInitialCoord;
    }


    public void connectHallways(String dir, String nDir, int[] ePos, int[] newSize) {
        int[] startingPos = {ePos[0], ePos[1]};
        if (dir.equals("up")) {
            currDirection = nDir;
            if (nDir.equals("left")) {
                startingPos[0] += 1; startingPos[1] += 2;
                for (int i = startingPos[0]; i > startingPos[0] - newSize[0]; i--) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0]; hallwayPlacement[1] = startingPos[1] - 1;
                for (int j = hallwayPlacement[0]; j > hallwayPlacement[0] - newSize[0]; j--) {
                    world[j][hallwayPlacement[1]] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0] - newSize[0]; ePos[1] = hallwayPlacement[1];
                currPos = ePos;
                world[startingPos[0]][startingPos[1] - 1] = Tileset.WALL;
                world[startingPos[0] - 1][startingPos[1] - 2] = Tileset.FLOOR;
            } else if (nDir.equals("right")) {
                startingPos[0] -= 1; startingPos[1] += 2;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0]; hallwayPlacement[1] = startingPos[1] - 1;
                for (int j = hallwayPlacement[0]; j < hallwayPlacement[0] + newSize[0]; j++) {
                    world[j][hallwayPlacement[1]] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0] + newSize[0]; ePos[1] = hallwayPlacement[1];
                currPos = ePos;
                world[startingPos[0]][startingPos[1] - 1] = Tileset.WALL;
                world[startingPos[0] + 1][startingPos[1] - 2] = Tileset.FLOOR;
            }
        } else if (dir.equals("down")) {
            currDirection = nDir;
            if (nDir.equals("left")) {
                startingPos[0] += 1;
                for (int i = startingPos[0]; i > startingPos[0] - newSize[0]; i--) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0]; hallwayPlacement[1] = startingPos[1] - 1;
                for (int j = hallwayPlacement[0]; j > hallwayPlacement[0] - newSize[0]; j--) {
                    world[j][hallwayPlacement[1]] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0] - newSize[0]; ePos[1] = hallwayPlacement[1];
                currPos = ePos;
                world[startingPos[0]][startingPos[1] - 1] = Tileset.WALL;
                world[startingPos[0] - 1][startingPos[1]] = Tileset.FLOOR;
            } else if (nDir.equals("right")) {
                startingPos[0] -= 1;
                startingPos[1] += 1;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0];
                hallwayPlacement[1] = startingPos[1] - 1;
                for (int j = hallwayPlacement[0]; j < hallwayPlacement[0] + newSize[0]; j++) {
                    world[j][hallwayPlacement[1]] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0] + newSize[0];
                ePos[1] = hallwayPlacement[1];
                currPos = ePos;
                world[startingPos[0]][startingPos[1] - 1] = Tileset.WALL;
                world[startingPos[0] + 1][startingPos[1]] = Tileset.FLOOR;
            }
        } else {
            connectHallwaystwo(dir, nDir, ePos, newSize);
        }
    }

    public void connectHallwaystwo(String dir, String nDir, int[] ePos, int[] newSize) {
        int[] startingPos = {ePos[0], ePos[1]};
        if (dir.equals("left")) {
            currDirection = nDir;
            if (nDir.equals("up")) {
                startingPos[0] -= 2; startingPos[1] -= 1;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j < startingPos[1] + newSize[1]; j++) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0] + 1;
                hallwayPlacement[1] = startingPos[1];
                for (int j = hallwayPlacement[1]; j < hallwayPlacement[1] + newSize[1]; j++) {
                    world[hallwayPlacement[0]][j] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0]; ePos[1] = hallwayPlacement[1] + newSize[1];
                currPos = ePos;
                world[startingPos[0] + 1][startingPos[1]] = Tileset.WALL;
                world[startingPos[0] + 2][startingPos[1] + 1] = Tileset.FLOOR;
            } else if (nDir.equals("down")) {
                startingPos[0] -= 2;
                startingPos[1] += 1;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0] + 1;
                hallwayPlacement[1] = startingPos[1];
                for (int j = hallwayPlacement[1]; j > hallwayPlacement[1] - newSize[1]; j--) {
                    world[hallwayPlacement[0]][j] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0]; ePos[1] = hallwayPlacement[1] - newSize[1];
                currPos = ePos;
                world[startingPos[0] + 1][startingPos[1]] = Tileset.WALL;
                world[startingPos[0] + 2][startingPos[1] - 1] = Tileset.FLOOR;
            }
        } else if (dir.equals("right")) {
            currDirection = nDir;
            if (nDir.equals("up")) {
                startingPos[1] -= 1;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j < startingPos[1] + newSize[1]; j++) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0] + 1;
                hallwayPlacement[1] = startingPos[1];
                for (int j = hallwayPlacement[1]; j < hallwayPlacement[1] + newSize[1]; j++) {
                    world[hallwayPlacement[0]][j] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0]; ePos[1] = hallwayPlacement[1] + newSize[1];
                currPos = ePos;
                world[startingPos[0] + 1][startingPos[1]] = Tileset.WALL;
                world[startingPos[0]][startingPos[1] + 1] = Tileset.FLOOR;
            }
            if (nDir.equals("down")) {
                startingPos[1] += 1;
                for (int i = startingPos[0]; i < startingPos[0] + newSize[0]; i++) {
                    for (int j = startingPos[1]; j > startingPos[1] - newSize[1]; j--) {
                        world[i][j] = Tileset.WALL;
                    }
                }
                int[] hallwayPlacement = new int[2];
                hallwayPlacement[0] = startingPos[0] + 1;
                hallwayPlacement[1] = startingPos[1];
                for (int j = hallwayPlacement[1]; j > hallwayPlacement[1] - newSize[1]; j--) {
                    world[hallwayPlacement[0]][j] = Tileset.FLOOR;
                }
                ePos[0] = hallwayPlacement[0]; ePos[1] = hallwayPlacement[1] - newSize[1];
                currPos = ePos;
                world[startingPos[0] + 1][startingPos[1]] = Tileset.WALL;
                world[startingPos[0]][startingPos[1] - 1] = Tileset.FLOOR;
            }
        }
    }

    public void makeDeadEnd(int[] endingPos, String direction) {
        if (direction.equals("up")) {
            world[endingPos[0]][endingPos[1] - 1] = Tileset.WALL;
        } else if (direction.equals("down")) {
            world[currPos[0]][currPos[1] + 1] = Tileset.WALL;
        } else if (direction.equals("left")) {
            world[currPos[0] + 1][currPos[1]] = Tileset.WALL;
        } else {
            world[currPos[0] - 1][currPos[1]] = Tileset.WALL;
        }
    }

    public void secondHallwayone() {
        if (currDirection.equals("left")) {
            String[] availableDirections = {"up", "down"};
            int randomIndex = RandomUtils.uniform(RANDOM, availableDirections.length);
            String nextDirection = availableDirections[randomIndex];
            randHallwaySize = hallwaySize(nextDirection);
            if (nextDirection.equals("up")) {
                currPos[0] -= 2;
                currPos[1] -= 1;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] += 2;
                    currPos[1] += 1;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] += 2;
                    currPos[1] += 1;
                }
            } else if (nextDirection.equals("down")) {
                currPos[0] -= 2;
                currPos[1] += 1;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] += 2;
                    currPos[1] -= 1;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] += 2;
                    currPos[1] -= 1;
                }
            }
        } else if (currDirection.equals("right")) {
            String[] availableDirections = {"up", "down"};
            int randomIndex = RandomUtils.uniform(RANDOM, availableDirections.length);
            String nextDirection = availableDirections[randomIndex];
            randHallwaySize = hallwaySize(nextDirection);
            if (nextDirection.equals("up")) {
                currPos[0] += 2;
                currPos[1] -= 1;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] -= 2;
                    currPos[1] += 1;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] -= 2;
                    currPos[1] += 1;
                }
            } else {
                currPos[0] += 2;
                currPos[1] -= 1;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] -= 2;
                    currPos[1] += 1;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] -= 2;
                    currPos[1] += 1;
                }
            }
        } else {
            secondHallwaytwo();
        }
    }

    public void secondHallwaytwo() {
        if (currDirection.equals("down")) {
            String[] availableDirections = {"left", "right"};
            int randomIndex = RandomUtils.uniform(RANDOM, availableDirections.length);
            String nextDirection = availableDirections[randomIndex];
            randHallwaySize = hallwaySize(nextDirection);
            if (nextDirection.equals("left")) {
                currPos[0] += 1;
                currPos[1] -= 2;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] -= 1;
                    currPos[1] += 2;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] -= 1;
                    currPos[1] += 2;
                    makeDeadEnd(currPos, currDirection);
                    hallwaytwofail = true;
                }
            } else {
                currPos[1] -= 1;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[1] += 1;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[1] += 1;
                    makeDeadEnd(currPos, currDirection);
                    hallwaytwofail = true;
                }
            }
        } else {
            String[] availableDirections = {"left", "right"};
            int randomIndex = RandomUtils.uniform(RANDOM, availableDirections.length);
            String nextDirection = availableDirections[randomIndex];
            randHallwaySize = hallwaySize(nextDirection);
            if (nextDirection.equals("left")) {
                currPos[0] += 1;
                currPos[1] += 2;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] -= 1;
                    currPos[1] -= 2;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] -= 1;
                    currPos[1] -= 2;
                    makeDeadEnd(currPos, currDirection);
                    hallwaytwofail = true;
                }
            } else {
                currPos[0] -= 1;
                currPos[1] += 2;
                if (areaClearForHallway(randHallwaySize, currPos, nextDirection)) {
                    currPos[0] += 1;
                    currPos[1] -= 2;
                    connectHallways(currDirection, nextDirection, currPos, randHallwaySize);
                    lastThingAdded = "hallway";
                } else {
                    currPos[0] += 1;
                    currPos[1] -= 2;
                    makeDeadEnd(currPos, currDirection);
                    hallwaytwofail = true;
                }
            }
        }
    }

    public boolean checkallnull() {
        boolean allnull = true;
        boolean end = false;
        while (!areaClearForHallway(randHallwaySize, initialCoord, currDirection)) {
            for (int i = 0; i < unusedInitialDirections.length; i += 1) {
                if (unusedInitialDirections[i] != null) {
                    allnull = false;
                }
            }
            if (allnull) {
                end = true;
                break;
            }
            initialCoord = randomInitialCoord();
            allnull = true;
        }
        return end;
    }

    public void main() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        currPos = roomPlacement();
        roomSize = roomSize();
        currDirection = randomHallwayDirection();
        playeronePos[0] = currPos[0] + 1;
        playeronePos[1] = currPos[1] + 1;
        playertwoPos[0] = currPos[0] + roomSize[0] - 2;
        playertwoPos[1] = currPos[1] + roomSize[1] - 2;
        while (usedIndex < 4) {
            if (areaClearForRoom(currPos, currDirection)) {
                addRoom(currPos, roomSize, currDirection);
                lastThingAdded = "room";
            } else {
                makeDeadEnd(currPos, currDirection);
                String save = currDirection;
                initialCoord = randomInitialCoord();
                if (usedIndex == 4) {
                    currDirection = save;
                    break;
                }
                randHallwaySize = hallwaySize(currDirection);
                if (checkallnull()) {
                    break;
                }
                addHallway(initialCoord, randHallwaySize, currDirection);
                lastThingAdded = "hallway";
                if (areaClearForRoom(currPos, currDirection)) {
                    addRoom(currPos, roomSize, currDirection);
                    lastThingAdded = "room";
                } else {
                    makeDeadEnd(currPos, currDirection);
                    continue;
                }
            }
            nextNumOfHallways = randomNumOfHallways();
            currDirection = randomHallwayDirection();
            randHallwaySize = hallwaySize(currDirection);
            wallCoordsInDir = coordsOfWallInDirection(currPos, roomSize, currDirection);
            chosenCoord = wallCoordsInDir[RandomUtils.uniform(RANDOM, wallCoordsInDir.length)];
            if (areaClearForHallway(randHallwaySize, chosenCoord, currDirection)) {
                addHallway(chosenCoord, randHallwaySize, currDirection);
                lastThingAdded = "hallway";
            } else {
                if (usedIndex == 4) {
                    break;
                }
                initialCoord = randomInitialCoord();
                if (usedIndex == 4) {
                    break;
                }
                randHallwaySize = hallwaySize(currDirection);
                if (checkallnull()) {
                    break;
                }
                randHallwaySize = hallwaySize(currDirection);
                addHallway(initialCoord, randHallwaySize, currDirection);
                lastThingAdded = "hallway";
            }
            if (nextNumOfHallways == 2) {
                secondHallwayone();
                if (hallwaytwofail) {
                    if (usedIndex == 4) {
                        break;
                    }
                    initialCoord = randomInitialCoord();
                    if (usedIndex == 4) {
                        break;
                    }
                    randHallwaySize = hallwaySize(currDirection);
                    if (checkallnull()) {
                        break;
                    }
                    addHallway(initialCoord, randHallwaySize, currDirection);
                    lastThingAdded = "hallway";
                    hallwaytwofail = false;
                }
            }
        }
        if (lastThingAdded.equals("hallway") && (!hallwaytwofail)) {
            makeDeadEnd(currPos, currDirection);
            hallwaytwofail = false;
        }
        ter.renderFrame(world);
    }
}

