//Fatma Beyza Sever
//2023400270

import java.io.*;
import java.util.*;

public class FileReader {
    private ArrayList<Tile> tiles = new ArrayList<>(); //array list for all the tiles
    private double[][] costArray; //a 2d dimensional array to determine the cost of moves from certain tiles to certain tiles
    private int cols; //number of total columns
    private int rows; //number of the total rows

    public FileReader(){}

    public void mapDataReader(String filename){ //a method that will help us to read the mapData.txt file every time we call from the main
        int total; //total number of the squares on the map
        try{
            File file = new File(filename); //open and start to read the file
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine() && reader != null) {
                String line = reader.nextLine(); //get every line step by step
                String[] lineArray = line.trim().split(" "); //split the string line according to the position of blanks,
                // make sure there is no extra blank at the beginning and at the ending of the string line
                if (lineArray.length == 2) { //if the array's length is 2, that means it is giving us the # of rows and columns
                    cols = Integer.parseInt(lineArray[0]); //get the number of columns
                    rows = Integer.parseInt(lineArray[1]); //get the number of rows
                }else if (lineArray.length == 3){ //if the array's length is 3, that means it is giving us the coordinates and the type of a tile
                    tiles.add(new Tile(Integer.parseInt(lineArray[0]), Integer.parseInt(lineArray[1]), Integer.parseInt(lineArray[2])));
                    //add the new tile to the tiles arraylist
                }
            }
            reader.close();
        }catch(Exception e){}

        total = cols * rows; //get the total number of tiles
        costArray = new double[total][total]; //create the 2d array to store the cost of moves from each tile

        for (Tile t : tiles) { //get every tile from the tiles to find the adjacent tiles for each tile
            int[][] directions = {{0,1},{0,-1},{1,0},{-1,0}}; //get the possible moves that the knight can
            for (int[] direction : directions) { //for every direction in the directions array
                Tile adjacent = getTile(t.getColumn() + direction[0], t.getRow() + direction[1]);
                if (adjacent != null && adjacent.isPassable()) { //check whether the targeted tile is valid for movement
                    t.addAdjacentTile(adjacent); //if it is, add to the tile's adjacentTiles arrayList
                }
            }
        }
    }

    public void travelCostsReader(String filename){ //a method to help us to read the travelCost.txt every time we call from the main
        for (int i = 0; i < costArray.length; i++) {
            Arrays.fill(costArray[i], Double.POSITIVE_INFINITY);
        }
        try{
            File file = new File(filename); //open and start to read the file
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine() && reader != null){
                String line = reader.nextLine(); //get every line step by step
                String[] lineArray = line.trim().split(" "); //split the string line by the position of blanks
                int startX = Integer.parseInt(lineArray[0]); //get the start x
                int startY = Integer.parseInt(lineArray[1]); //get the start y
                int endX = Integer.parseInt(lineArray[2]); //get the targeted x
                int endY = Integer.parseInt(lineArray[3]); //get the targeted y
                double costOfTheMove = Double.parseDouble(lineArray[4]); //get the cost of the movement
                //if we ever decide to call the tiles starting from 0 to total
                int startTileIndex = startY* cols + startX; //this will be startTileIndex's number
                int endTileIndex = endY * cols + endX; //and this will be endTileIndex's number
                costArray[startTileIndex][endTileIndex]=costOfTheMove; //in the cost array, assign the value
                costArray[endTileIndex][startTileIndex]=costOfTheMove; //back movement will cost the same so assign the same value
            }
            reader.close();

        }catch(Exception e){}

    }
    public ArrayList<Tile> objectiveReader(String filename){
        ArrayList<Tile> objectives = new ArrayList<>(); //create an arraylist to store the tiles at the position of the objectives
        try{
            File file = new File(filename); //open and start to read the file
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine(); //get every line in the file step by step
                String[] lineArray = line.trim().split(" "); //create an array by splitting up the string according to the position of the blanks
                Tile tile = getTile(Integer.parseInt(lineArray[0]), Integer.parseInt(lineArray[1])); //get the tile in the given coordinates
                if(tile != null){
                    objectives.add(tile); //add the tile to the objectives arrayList
                }
            }
            reader.close();
        }catch(Exception e){}

        return objectives;
    }

    private Tile getTile(int col, int row) { //returns the tile in that specific col and row
        for (Tile t : tiles) {
            if (t.getColumn() == col && t.getRow() == row) return t;
        }
        return null;
    }

    //getter methods
    public ArrayList<Tile> getTiles() { return tiles; }
    public double[][] getCostArray() { return costArray; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }
}
