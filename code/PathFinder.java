//Fatma Beyza Sever
//2023400270

import java.util.*;

public class PathFinder {
    private ArrayList<Tile> allTiles;
    private double[][] costMatrix;
    private int cols;
    private int rows;

    public PathFinder(ArrayList<Tile> allTiles, double[][] costMatrix, int cols, int rows) { //a constructor using setter methods
        setAllTiles(allTiles);
        setCostMatrix(costMatrix);
        setCols(cols);
        setRows(rows);
    }

    private Tile getTile(int col, int row) { //a method to return the tile in the given column and row
        for (Tile t : allTiles) {
            if (t.getColumn() == col && t.getRow() == row){ return t;}
        }
        return null;
    }

    private int tileIndex(int col, int row) { //a method to return the number of the given tile
        return row * cols + col;
    }

    public ArrayList<Tile> findShortestPath(Tile start, Tile goal) {

        int total = rows * cols; //total number of rows and cols
        double[] distanceArray = new double[total]; //an array to gather up distances
        boolean[] visitedOrNot = new boolean[total]; //a boolean array to help us whether the tile is visited or not
        Tile[] previousTileInfo = new Tile[total]; //an array to store info about the passed tiles

        for (int i = 0; i<distanceArray.length; i++){ //at first, we can call an arbitrary big number for all distance values
            distanceArray[i]=10000000;
        }
        distanceArray[tileIndex(start.getColumn(), start.getRow())] = 0; //distance of the starting point will be zero because we are exactly at that point

        for (int step = 0; step < total; step++) {
            int indexNumber = -1;
            double minimumDistance = 10000000;
            for (int i = 0; i < total; i++) { //check for every tile
                if (!visitedOrNot[i] && distanceArray[i] < minimumDistance) { //if the tile is not visited and the distance is less than the current minimum
                    minimumDistance = distanceArray[i]; //change the value of the minimum
                    indexNumber = i; //and the index number
                }
            }
            if (indexNumber == -1) {break;} //if the index number still hasn't change, then we can say there is no available tile for the movement, break

            visitedOrNot[indexNumber] = true; //make the tile visited in the visitedOrNot array

            int xCoordinate = indexNumber % cols; //get the x coordinate of the tile
            int yCoordinate = indexNumber / cols; //get the y coordinate of the tile
            Tile currentTile = getTile(xCoordinate, yCoordinate); //get the tile with using the coordinates
            if (currentTile == null || !currentTile.isPassable()) continue;

            for (Tile adjacent : currentTile.getAdjacentTiles()) {
                int vIndex = tileIndex(adjacent.getColumn(), adjacent.getRow());
                double cost = costMatrix[indexNumber][vIndex];
                if (cost < Double.POSITIVE_INFINITY && distanceArray[indexNumber] + cost < distanceArray[vIndex]) {
                    distanceArray[vIndex] = distanceArray[indexNumber] + cost;
                    previousTileInfo[vIndex] = currentTile;
                }
            }
        }

        ArrayList<Tile> path = new ArrayList<>(); //create an array list to store the movements of the knight caharcter
        Tile current = goal;
        while (current != null && !current.samePosition(start)) {
            path.add(0, current);
            current = previousTileInfo[tileIndex(current.getColumn(), current.getRow())];
        }
        if (current == null) {
            return null;
        }
        path.add(0, start);

        return path;
    }

    private void setAllTiles(ArrayList<Tile> allTiles){
        this.allTiles=allTiles;
    }
    private void setCostMatrix(double[][] costMatrix){
        this.costMatrix=costMatrix;
    }
    private void setCols(int cols){
        this.cols =cols;
    }
    private void setRows(int rows){
        this.rows=rows;
    }
}
