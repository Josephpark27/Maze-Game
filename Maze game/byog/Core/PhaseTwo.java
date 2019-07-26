package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class PhaseTwo implements java.io.Serializable {

    TETile[][] worldFrame;
    WorldGeneration world;
    int[] playeronePos = new int[2];
    int[] playertwoPos = new int[2];
    int[] temponePos = new int[2];
    int[] temptwoPos = new int[2];
    int width;
    int height;
    TERenderer ter;
    Random r;
    int[] exit = new int[2];
    boolean load;
    int level = 1;
    int[][] points = new int[25][2];
    int playerone = 0;
    int playertwo = 0;
    String commands;
    Boolean inputString = false;
    Boolean end = false;
    TETile ud = Tileset.UNLOCKED_DOOR;
    TETile p = Tileset.POINT;

    public PhaseTwo(int w, int h, TERenderer t, String c, TETile[][] wf) {
        width = w;
        height = h;
        ter = t;
        worldFrame = wf;
        if (c != null) {
            commands = c.toLowerCase();
            inputString = true;
        }
    }

    public void pointlocation() {
        for (int i = 0; i < points.length; i += 1) {
            int x = RandomUtils.uniform(r, 0, width);
            int y = RandomUtils.uniform(r, 0, height);
            if (worldFrame[x][y].equals(Tileset.FLOOR) && playerDistancetwo(x, y)) {
                points[i][0] = x;
                points[i][1] = y;
                continue;
            }
            while (!(worldFrame[x][y].equals(Tileset.FLOOR) && playerDistancetwo(x, y))) {
                x = RandomUtils.uniform(r, 0, width);
                y = RandomUtils.uniform(r, 0, height);
            }
            points[i][0] = x;
            points[i][1] = y;
        }
    }

    public void pointset() {
        for (int i = 0; i < points.length; i += 1) {
            worldFrame[points[i][0]][points[i][1]] = Tileset.POINT;
        }
    }

    public boolean playerDistancetwo(int x, int y) {
        if (Math.abs(playeronePos[0] - x) < 8 && Math.abs(playeronePos[1] - y) < 8) {
            return false;
        }
        return true;
    }

    public int[] exitLocation() {
        int x = RandomUtils.uniform(r, 0, width);
        int y = RandomUtils.uniform(r, 0, height);
        boolean wall = worldFrame[x][y].equals(Tileset.WALL);
        exit = new int[2];
        if (playerDistance(x, y) && wall && notCorner(x, y)) {
            exit[0] = x;
            exit[1] = y;
            return exit;
        }
        while (!(playerDistance(x, y) && wall && notCorner(x, y))) {
            x = RandomUtils.uniform(r, 0, width);
            y = RandomUtils.uniform(r, 0, height);
            wall = worldFrame[x][y].equals(Tileset.WALL);
        }
        exit[0] = x;
        exit[1] = y;
        return exit;
    }

    public boolean playerDistance(int x, int y) {
        if (Math.abs(playeronePos[0] - x) < 10 && Math.abs(playeronePos[1] - y) < 10) {
            return false;
        }
        return true;
    }

    public boolean notCorner(int x, int y) {
        boolean doorAllowed = true;
        boolean up;
        boolean down;
        boolean left;
        boolean right;
        try {
            up = worldFrame[x][y + 1].equals(Tileset.WALL);
            right = worldFrame[x + 1][y].equals(Tileset.WALL);
            if (up && right) {
                doorAllowed = false;
            }
            up = worldFrame[x][y + 1].equals(Tileset.WALL);
            left = worldFrame[x - 1][y].equals(Tileset.WALL);
            if (up && left) {
                doorAllowed = false;
            }
            down = worldFrame[x][y - 1].equals(Tileset.WALL);
            right = worldFrame[x + 1][y].equals(Tileset.WALL);
            if (down && right) {
                doorAllowed = false;
            }
            down = worldFrame[x][y - 1].equals(Tileset.WALL);
            left = worldFrame[x - 1][y].equals(Tileset.WALL);
            if (down && left) {
                doorAllowed = false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            doorAllowed = false;
        }
        return doorAllowed;
    }

    public void loadWorld() {
        File w = new File("./world.txt");
        File wf = new File("./worldFrame.txt");
        File t = new File("./ter.txt");
        File rand = new File("./random.txt");
        File pop = new File("./playeronePos.txt");
        File l = new File("./level.txt");
        File ptp = new File("./playertwoPos.txt");
        File po = new File("./playerone.txt");
        File pt = new File("./playertwo.txt");
        if (w.exists() && wf.exists() && t.exists() && rand.exists() && pop.exists()) {
            try {
                FileInputStream ws = new FileInputStream(w);
                FileInputStream wfs = new FileInputStream(wf);
                FileInputStream wt = new FileInputStream(t);
                FileInputStream wr = new FileInputStream(rand);
                FileInputStream wp = new FileInputStream(pop);
                FileInputStream wl = new FileInputStream(l);
                FileInputStream wptp = new FileInputStream(ptp);
                FileInputStream wpo = new FileInputStream(po);
                FileInputStream wpt = new FileInputStream(pt);
                ObjectInputStream os = new ObjectInputStream(ws);
                ObjectInputStream ofs = new ObjectInputStream(wfs);
                ObjectInputStream ot = new ObjectInputStream(wt);
                ObjectInputStream or = new ObjectInputStream(wr);
                ObjectInputStream op = new ObjectInputStream(wp);
                ObjectInputStream ol = new ObjectInputStream(wl);
                ObjectInputStream optp = new ObjectInputStream(wptp);
                ObjectInputStream opo = new ObjectInputStream(wpo);
                ObjectInputStream opt = new ObjectInputStream(wpt);
                world = (WorldGeneration) os.readObject();
                worldFrame = (TETile[][]) ofs.readObject();
                ter = (TERenderer) ot.readObject();
                r = (Random) or.readObject();
                playeronePos = (int[]) op.readObject();
                level = (int) ol.readObject();
                playertwoPos = (int[]) optp.readObject();
                playerone = (int) opo.readObject();
                playertwo = (int) opt.readObject();
                load = true;
                os.close();
                ofs.close();
                ot.close();
                or.close();
                op.close();
                ol.close();
                optp.close();
                opo.close();
                opt.close();
            } catch (FileNotFoundException e) {
                if (inputString) {
                    end = true;
                    return;
                }
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                if (inputString) {
                    end = true;
                    return;
                }
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                if (inputString) {
                    end = true;
                    return;
                }
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }


    public void saveWorld() {
        try {
            File w = new File("./world.txt");
            File wf = new File("./worldFrame.txt");
            File t = new File("./ter.txt");
            File rand = new File("./random.txt");
            File pop = new File("./playeronePos.txt");
            File l = new File("./level.txt");
            File ptp = new File("./playertwoPos.txt");
            File po = new File("./playerone.txt");
            File pt = new File("./playertwo.txt");
            if (!(w.exists() && w.exists() && t.exists() && rand.exists() && pop.exists())) {
                w.createNewFile(); wf.createNewFile();
                t.createNewFile(); rand.createNewFile();
                pop.createNewFile(); l.createNewFile();
                ptp.createNewFile(); po.createNewFile();
                pt.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(w);
            FileOutputStream fileOut2 = new FileOutputStream(wf);
            FileOutputStream fileOut3 = new FileOutputStream(t);
            FileOutputStream fileOut4 = new FileOutputStream(rand);
            FileOutputStream fileOut5 = new FileOutputStream(pop);
            FileOutputStream fileOut6 = new FileOutputStream(l);
            FileOutputStream fileOut7 = new FileOutputStream(ptp);
            FileOutputStream fileOut8 = new FileOutputStream(po);
            FileOutputStream fileOut9 = new FileOutputStream(pt);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
            ObjectOutputStream out3 = new ObjectOutputStream(fileOut3);
            ObjectOutputStream out4 = new ObjectOutputStream(fileOut4);
            ObjectOutputStream out5 = new ObjectOutputStream(fileOut5);
            ObjectOutputStream out6 = new ObjectOutputStream(fileOut6);
            ObjectOutputStream out7 = new ObjectOutputStream(fileOut7);
            ObjectOutputStream out8 = new ObjectOutputStream(fileOut8);
            ObjectOutputStream out9 = new ObjectOutputStream(fileOut9);
            out.writeObject(world);
            out2.writeObject(worldFrame);
            out3.writeObject(ter);
            out4.writeObject(r);
            out5.writeObject(playeronePos);
            out6.writeObject(level);
            out7.writeObject(playertwoPos);
            out8.writeObject(playerone);
            out9.writeObject(playertwo);
            out.close(); fileOut.close();
            out2.close(); fileOut2.close();
            out3.close(); fileOut3.close();
            out4.close();
            fileOut4.close();
            out5.close();
            fileOut5.close();
            out6.close();
            fileOut6.close();
            out7.close();
            fileOut7.close();
            out8.close();
            fileOut8.close();
            out9.close();
            fileOut9.close();
            if (inputString) {
                return;
            }
            System.exit(0);
        } catch (FileNotFoundException e) {
            if (inputString) {
                end = true;
                return;
            }
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            if (inputString) {
                end = true;
                return;
            }
            System.out.println(e);
            System.exit(0);
        }
    }

    public void menu() {
        StdDraw.picture(0.5, 0.5, "world.jpg");
        StdDraw.setPenColor(StdDraw.BLUE);
        Font font = new Font("Modern No. 20", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.70, "CS 61B Project 2");
        Font fonttwo = new Font("Modern No. 20", Font.BOLD, 15);
        StdDraw.setFont(fonttwo);
        StdDraw.text(0.5, 0.65, "By Joseph Park and Jacqueline Wood");
        StdDraw.setPenColor(StdDraw.BLACK);
        Font fontone = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(fontone);
        StdDraw.text(0.5, 0.50, "New Game(N)");
        StdDraw.text(0.5, 0.45, "Load Game(L)");
        StdDraw.text(0.5, 0.40, "Quit(Q)");
        String input = "";
        boolean nPressed = false;
        load = false;
        boolean l = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char option = StdDraw.nextKeyTyped();
                if (option == 'n') {
                    StdDraw.picture(0.5, 0.5, "seed2.png");
                    StdDraw.setPenColor(StdDraw.BLUE);
                    Font fontthree = new Font("TimesRoman", Font.BOLD, 17);
                    StdDraw.setFont(fontthree);
                    String s = "\"s\"";
                    StdDraw.text(0.53, 0.70, "Enter a seed and press " + s);
                    break;
                }
                if (option == 'q') {
                    System.exit(1);
                }
                if (option == 'l') {
                    loadWorld();
                    l = true;
                    break;
                }
            }
        }
        if (!l) {
            double x = 0.35;
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char seed = StdDraw.nextKeyTyped();
                    if (seed == 's') {
                        break;
                    }
                    if (Character.isDigit(seed)) {
                        input = input + seed;
                        String p = "" + seed;
                        StdDraw.text(x, 0.50, p);
                        x = x + 0.02;
                    }
                }
            }
        }
        if (!load) {
            long w = Long.parseLong(input);
            r = new Random(w);
            world = new WorldGeneration(worldFrame, r, width, height);
            world.main();
            playeronePos[0] = world.playeronePos[0];
            playeronePos[1] = world.playeronePos[1];
            worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
            playertwoPos[0] = world.playertwoPos[0];
            playertwoPos[1] = world.playertwoPos[1];
            worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            exit = exitLocation();
            worldFrame[exit[0]][exit[1]] = Tileset.UNLOCKED_DOOR;
            pointlocation();
            pointset();
            ter.initialize(width, height, 0, 0);
            ter.renderFrame(worldFrame);
            drawFrame();
        }
        if (load) {
            world.main();
            ter.renderFrame(worldFrame);
        }
    }

    public void drawFrame() {
        Font save = StdDraw.getFont();
        double midWidth = 4;
        double midHeight = height - 1.25;

        Font bigFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, "Level : " + level);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth + 20, midHeight, "Player 1 Score : " + playerone);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth + 40, midHeight, "Player 2 Score : " + playertwo);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        try {
            String mouse = worldFrame[x][y].description();
            StdDraw.text(64, midHeight, "Current Tile: " + mouse);
        } catch (ArrayIndexOutOfBoundsException e) {
            StdDraw.text(64, midHeight, "Current Tile: ");
        }
        StdDraw.setFont(save);
        StdDraw.line(0, height - 2, width, height - 2);

        StdDraw.show();
    }


    public void gameplay() {
        char prevkey = 'b';
        while (true) {
            drawFrame(); ter.renderFrame(worldFrame);
            if (StdDraw.hasNextKeyTyped()) {
                char nextkey = StdDraw.nextKeyTyped();
                if (prevkey == ':' && nextkey == 'q') {
                    saveWorld();
                }
                if (nextkey == 'w') {
                    if (!worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(Tileset.WALL)) {
                        if (worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(ud)) {
                            playerone += 3; break;
                        }
                        if (worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(p)) {
                            playerone += 1;
                        }
                        temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                        playeronePos[1] = playeronePos[1] + 1;
                        worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                        worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                        drawFrame(); ter.renderFrame(worldFrame);
                    }
                }
                if (nextkey == 'd') {
                    if (!worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(Tileset.WALL)) {
                        if (worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(ud)) {
                            playerone += 3; break;
                        }
                        if (worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(p)) {
                            playerone += 1;
                        }
                        temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                        playeronePos[0] = playeronePos[0] + 1;
                        worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                        worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                        drawFrame(); ter.renderFrame(worldFrame);
                    }
                }
                if (nextkey == 'a') {
                    if (!worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(Tileset.WALL)) {
                        if (worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(ud)) {
                            playerone += 3;
                            break;
                        }
                        if (worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(p)) {
                            playerone += 1;
                        }
                        temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                        playeronePos[0] = playeronePos[0] - 1;
                        worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                        worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                        drawFrame(); ter.renderFrame(worldFrame);
                    }
                }
                if (nextkey == 's') {
                    if (!worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(Tileset.WALL)) {
                        if (worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(ud)) {
                            playerone += 3; break;
                        }
                        if (worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(p)) {
                            playerone += 1;
                        }
                        temponePos[0] = playeronePos[0];
                        temponePos[1] = playeronePos[1];
                        playeronePos[1] = playeronePos[1] - 1;
                        worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                        worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                        drawFrame(); ter.renderFrame(worldFrame);
                    }
                }
                if (gameplaytwo(nextkey)) {
                    break;
                }
                prevkey = nextkey;
            }
        }
    }

    public boolean gameplaytwo(char nextkey) {
        if (nextkey == 'i') {
            if (!worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[1] = playertwoPos[1] + 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
                ter.renderFrame(worldFrame);
                drawFrame();
                StdDraw.show();
            }
        }
        if (nextkey == 'k') {
            if (!worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[1] = playertwoPos[1] - 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
                ter.renderFrame(worldFrame);
                StdDraw.show();
            }
        }
        if (nextkey == 'j') {
            if (!worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[0] = playertwoPos[0] - 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
                ter.renderFrame(worldFrame);
                drawFrame();
                StdDraw.show();
            }
        }
        if (nextkey == 'l') {
            if (!worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[0] = playertwoPos[0] + 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
                ter.renderFrame(worldFrame);
                drawFrame();
                StdDraw.show();
            }
        }
        return false;
    }

    public void menuString() {
        String input = "";
        if (commands.charAt(0) == 'n') {
            commands = commands.substring(1);
            int lengthCommands = commands.length();
            for (int i = 0; i < lengthCommands; i += 1) {
                if (commands.charAt(0) == 's') {
                    commands = commands.substring(1);
                    break;
                }
                input = input + commands.charAt(0);
                commands = commands.substring(1);
            }
            long w = Long.parseLong(input);
            r = new Random(w);
            world = new WorldGeneration(worldFrame, r, width, height);
            world.main();
            playeronePos[0] = world.playeronePos[0];
            playeronePos[1] = world.playeronePos[1];
            worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
            playertwoPos[0] = world.playertwoPos[0];
            playertwoPos[1] = world.playertwoPos[1];
            worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            exit = exitLocation();
            worldFrame[exit[0]][exit[1]] = Tileset.UNLOCKED_DOOR;
            pointlocation();
            pointset();
            return;
        }
        if (commands.charAt(0) == 'l') {
            loadWorld();
            world.main();
            commands = commands.substring(1);
            return;
        }
        if (commands.charAt(0) == 'q') {
            end = true;
            return;
        }
    }

    public void gameplaystring() {
        while (true) {
            int lengthCommands = commands.length();
            if (lengthCommands == 0) {
                return;
            }
            if (commands.charAt(0) == ':' && commands.charAt(1) == 'q') {
                saveWorld(); commands = commands.substring(2);
                return;
            }
            if (commands.charAt(0) == 'w') {
                commands = commands.substring(1);
                if (!worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(Tileset.WALL)) {
                    if (worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(ud)) {
                        playerone += 3; return;
                    }
                    if (worldFrame[playeronePos[0]][playeronePos[1] + 1].equals(p)) {
                        playerone += 1;
                    }
                    temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                    playeronePos[1] = playeronePos[1] + 1;
                    worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                    worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                }
                continue;
            }
            if (commands.charAt(0) == 'd') {
                commands = commands.substring(1);
                if (!worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(Tileset.WALL)) {
                    if (worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(ud)) {
                        playerone += 3; return;
                    }
                    if (worldFrame[playeronePos[0] + 1][playeronePos[1]].equals(p)) {
                        playerone += 1;
                    }
                    temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                    playeronePos[0] = playeronePos[0] + 1;
                    worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                    worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                }
                continue;
            }
            if (commands.charAt(0) == 'a') {
                commands = commands.substring(1);
                if (!worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(Tileset.WALL)) {
                    if (worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(ud)) {
                        playerone += 3; return;
                    }
                    if (worldFrame[playeronePos[0] - 1][playeronePos[1]].equals(p)) {
                        playerone += 1;
                    }
                    temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                    playeronePos[0] = playeronePos[0] - 1;
                    worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                    worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                }
                continue;
            }
            if (commands.charAt(0) == 's') {
                commands = commands.substring(1);
                if (!worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(Tileset.WALL)) {
                    if (worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(ud)) {
                        playerone += 3; return;
                    }
                    if (worldFrame[playeronePos[0]][playeronePos[1] - 1].equals(p)) {
                        playerone += 1;
                    }
                    temponePos[0] = playeronePos[0]; temponePos[1] = playeronePos[1];
                    playeronePos[1] = playeronePos[1] - 1;
                    worldFrame[temponePos[0]][temponePos[1]] = Tileset.FLOOR;
                    worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
                }
                continue;
            }
            if (gameplaystringtwo()) {
                break;
            }
        }
    }

    public boolean gameplaystringtwo() {
        if (commands.charAt(0) == 'i') {
            commands = commands.substring(1);
            if (!worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0]][playertwoPos[1] + 1].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[1] = playertwoPos[1] + 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            }
            return false;
        }
        if (commands.charAt(0) == 'k') {
            commands = commands.substring(1);
            if (!worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0]][playertwoPos[1] - 1].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[1] = playertwoPos[1] - 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            }
            return false;
        }
        if (commands.charAt(0) == 'j') {
            commands = commands.substring(1);
            if (!worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0] - 1][playertwoPos[1]].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[0] = playertwoPos[0] - 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            }
            return false;
        }
        if (commands.charAt(0) == 'l') {
            commands = commands.substring(1);
            if (!worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(Tileset.WALL)) {
                if (worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(ud)) {
                    playertwo += 3;
                    return true;
                }
                if (worldFrame[playertwoPos[0] + 1][playertwoPos[1]].equals(p)) {
                    playertwo += 1;
                }
                temptwoPos[0] = playertwoPos[0];
                temptwoPos[1] = playertwoPos[1];
                playertwoPos[0] = playertwoPos[0] + 1;
                worldFrame[temptwoPos[0]][temptwoPos[1]] = Tileset.FLOOR;
                worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            }
            return false;
        }
        return false;
    }

    public void main() {
        menu();
        gameplay();
        String input;
        while (level < 3) {
            level += 1;
            Random check;
            input = String.valueOf(RandomUtils.uniform(r, 0, 1000));
            long seed = Long.parseLong(input);
            check = new Random(seed);
            boolean diffWorld = false;
            if (!check.equals(r)) {
                diffWorld = true;
            }
            if (!diffWorld) {
                while (check.equals(r)) {
                    input = String.valueOf(RandomUtils.uniform(r, 0, 1000));
                    seed = Long.parseLong(input);
                    check = new Random(seed);
                }
            }
            r = check;
            worldFrame = new TETile[width][height];
            world = new WorldGeneration(worldFrame, r, width, height);
            world.main();
            playeronePos[0] = world.playeronePos[0];
            playeronePos[1] = world.playeronePos[1];
            worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
            playertwoPos[0] = world.playertwoPos[0];
            playertwoPos[1] = world.playertwoPos[1];
            worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            exit = exitLocation();
            worldFrame[exit[0]][exit[1]] = Tileset.UNLOCKED_DOOR;
            pointlocation();
            pointset();
            ter.initialize(width, height, 0, 0);
            ter.renderFrame(worldFrame);
            drawFrame();
            gameplay();
        }
        System.out.println("player one: " + playerone);
        System.out.println("player two: " + playertwo);
        if (playerone > playertwo) {
            System.out.println("Player one wins!");
        }
        if (playerone < playertwo) {
            System.out.println("Player two wins!");
        }
        System.exit(1);
    }

    public TETile[][] mainString() {
        menuString();
        int lengthCommands = commands.length();
        if (end || lengthCommands == 0) {
            return worldFrame;
        }
        gameplaystring();
        lengthCommands = commands.length();
        if (end || lengthCommands == 0) {
            return worldFrame;
        }
        String input;
        while (level < 3) {
            level += 1;
            Random check;
            input = String.valueOf(RandomUtils.uniform(r, 0, 1000));
            long seed = Long.parseLong(input);
            check = new Random(seed);
            boolean diffWorld = false;
            if (!check.equals(r)) {
                diffWorld = true;
            }
            if (!diffWorld) {
                while (check.equals(r)) {
                    input = String.valueOf(RandomUtils.uniform(r, 0, 1000));
                    seed = Long.parseLong(input);
                    check = new Random(seed);
                }
            }
            r = check;
            worldFrame = new TETile[width][height];
            world = new WorldGeneration(worldFrame, r, width, height);
            world.main();
            playeronePos[0] = world.playeronePos[0];
            playeronePos[1] = world.playeronePos[1];
            worldFrame[playeronePos[0]][playeronePos[1]] = Tileset.PLAYER;
            playertwoPos[0] = world.playertwoPos[0];
            playertwoPos[1] = world.playertwoPos[1];
            worldFrame[playertwoPos[0]][playertwoPos[1]] = Tileset.PLAYERTWO;
            exit = exitLocation();
            worldFrame[exit[0]][exit[1]] = Tileset.UNLOCKED_DOOR;
            pointlocation();
            pointset();
            gameplaystring();
            lengthCommands = commands.length();
            if (end || lengthCommands == 0) {
                return worldFrame;
            }
        }
        System.out.println("player one: " + playerone);
        System.out.println("player two: " + playertwo);
        if (playerone > playertwo) {
            System.out.println("Player one wins!");
        }
        if (playerone < playertwo) {
            System.out.println("Player two wins!");
        }
        return worldFrame;
    }
}
