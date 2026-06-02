//Fatma Beyza Sever
//2023400270

import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args){

        FileReader fileReader = new FileReader(); //construct the fileReader
        fileReader.mapDataReader("mapData.txt" ); //read the mapData
        fileReader.travelCostsReader("travelCosts.txt"); //read the travelCosts
        ArrayList<Tile> objectives = fileReader.objectiveReader("objectives.txt");
        //read the objectives and create an array for it

        ArrayList<Tile> tiles = fileReader.getTiles(); //create an arrayList to store all tiles
        double[][] costArray = fileReader.getCostArray(); //cost array to use in pathFinder
        int cols = fileReader.getCols(); //number of cols
        int rows = fileReader.getRows(); //number of rows

        StdDraw.setCanvasSize(cols * 25, rows * 25); //Set the canvas size
        StdDraw.setXscale(0, cols); //set the x scale
        StdDraw.setYscale(0, rows); //Set the y scale
        StdDraw.enableDoubleBuffering();
        drawMap(tiles, cols, rows); //draw the map using tiles array and # of cols and rows
        PathFinder pathFinder = new PathFinder(tiles, costArray, cols, rows);
        //construct the pathFinder

        if (objectives.isEmpty()) { //an if statement to check whether the objectives.txt is an empty one
            return;
        }
        boolean[] objectiveReached = new boolean[objectives.size()-1]; //a boolean array to tell us if we reached the exact objective
        //it will laters help us to draw the coins if they are not reached
        Arrays.fill(objectiveReached,false);

        Tile start = objectives.get(0); //get the start tile of the knight character
        for( int f = 0; f <objectiveReached.length; f++){ //for the coins that is not reached, draw the coin
            if (!objectiveReached[f]){
                drawCoin(objectives.get(f+1), rows);
            }
        }
        drawKnight(start, rows); //draw the knight
        StdDraw.show();
        StdDraw.pause(500);
        double totalCost = 0; // to store the total cost
        int totalSteps = 0; //to count the total number of steps



        try{
            FileWriter writer = new FileWriter("output.txt"); //open the writing mode for the output.txt file

            for (int t = 1; t < objectives.size(); t++) { //get every objective one by one
                Tile next = objectives.get(t); //get the next tile to be reached
                ArrayList<Tile> visitedTiles = new ArrayList<>(); //create an arrayList to store the visitedTiles
                StdDraw.pause(400);
                ArrayList<Tile> path = pathFinder.findShortestPath(start, next); //find the shortest path

                if (path == null) { //if there is no way to reach the coin
                    int number = t;
                    writer.write("Objective " + number + " cannot be reached!\n"); //write that we cannot reach it to the output.txt
                } else { //if there is a way

                    writer.write("Starting position: " + start.toString() + "\n"); //write firstly the starting point
                    double pathCost = 0; //start to calculate the pathCost

                    for (int i = 1; i < path.size(); i++) {

                        Tile current = path.get(i); //get the current tile
                        StdDraw.clear(); //clear the canvas first
                        drawMap(tiles, cols, rows); //draw the map all over again
                        for( int f = 0; f <objectiveReached.length; f++){ //for the coins that is not reached, draw the coin
                            if (!objectiveReached[f]){
                                drawCoin(objectives.get(f+1), rows);
                            }
                        }
                        drawKnight(current, rows); //draw the knight

                        visitedTiles.add(current); //add the current tile to the visitedTiles
                        Random rand = new Random();
                        int red = rand.nextInt(256);
                        int green = rand.nextInt(256);
                        int blue = rand.nextInt(256);
                        StdDraw.setPenColor(new Color(red, green, blue)); //get a random color, it will change at every step
                        for(int m = 0; m< visitedTiles.size()-1; m++){ //for every visited tile, draw circles
                            Tile visited = visitedTiles.get(m);
                            double xForPath = visited.getColumn() + 0.5;
                            double yForPath = rows - visited.getRow() - 0.5;
                            StdDraw.filledCircle(xForPath,yForPath,0.1); //draw circles for every visited tile
                        }
                        StdDraw.show();
                        StdDraw.pause(200);

                        Tile initial = path.get(i - 1); //get the initial tile
                        int firstIndex = initial.getRow() * cols + initial.getColumn();
                        int secondIndex = current.getRow() * cols + current.getColumn();
                        double stepCost = costArray[firstIndex][secondIndex]; //and use also the current one to calculate the cost of the travel
                        pathCost += stepCost; //add the step cost to the path cost

                        writer.write("Step Count: " + i + ", move to " + current +
                                ". Total Cost: " + String.format("%.2f", pathCost) + ".\n"); //Write on the output.txt


                    }
                    objectiveReached[t-1]=true; //change the boolean variable of the objective to true to indicate thatw e reached the coin

                    totalCost += pathCost; //Add the pathCost to the totalCost
                    totalSteps += path.size() - 1; //calculate the number of steps of the path we followed
                    int number = t;
                    writer.write("Objective " + number + " reached!\n");
                    start = next;
                }
            }
            writer.write("Total Step: " + totalSteps + ", Total Cost: " + String.format("%.2f", totalCost));
            //write the number of steps and the total cost we stop looking at all the objectives
            writer.close();
        }catch(Exception e){
        }

        StdDraw.show();
    }

    public static void drawMap(ArrayList<Tile> tiles, int cols, int rows) { //a method to help us to draw the map itself
        for (Tile tile : tiles) {

            double x = tile.getColumn() + 0.5; //get the coordinates of the tile and draw it
            double y = rows - tile.getRow() - 0.5;
            if(tile.getType()==0){
                StdDraw.picture(x,y,"misc/grassTile.jpeg",1,1);
            }
            else if(tile.getType()==1){
                StdDraw.picture(x,y,"misc/sandTile.png",1,1);
            }
            else if(tile.getType()==2){
                StdDraw.picture(x,y,"misc/impassableTile.jpeg",1,1);
            }
        }
        StdDraw.show();
    }

    public static void drawCoin(Tile t, int rows){ //a method to help us to draw the coins
        double xCoin = t.getColumn()+0.5; //get the coordinates of the coin using the tile that is on it
        double yCoin = rows - t.getRow()-0.5;
        StdDraw.picture(xCoin,yCoin,"misc/coin.png",1,1);
        StdDraw.show();
    }

    public static void drawKnight(Tile start, int rows){ //a method to help us to draw the knight
        double xStart = start.getColumn()+0.5; //get the coordinates of the knight using the tile that is on it
        double yStart = rows- start.getRow() -0.5;
        StdDraw.picture(xStart, yStart, "misc/knight.png", 1, 1);
        StdDraw.show();
    }
}
