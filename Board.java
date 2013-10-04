/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slidingblocks;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Niall Young
 */
public class Board {

    int length;  // Length of the board
    int width; //Width of the board
    int[][] board; // 2D array Representing the board, -1's = free space
    ArrayList<Block> blockInBoard = new ArrayList<>(); // Stores the blocks in the board.
    ArrayList<String> previousMoves = new ArrayList<>(); //Stores the Previous moves in the format (Row,Column,Vertical direction, horizontal direction) 

    public Board(int boardLength, int boardWidth)//Makes a board with the length and width
    {
        this.board = new int[boardLength][boardWidth];
        this.length = boardLength;//Sets the class variable
        this.width = boardWidth;//For length and width
    }

    public void printBoard() // Outputs every entry in the board, changing the -1 to "."
    {
        String outputLine;
        for (int i = 0; i < this.board.length; i++) {
            outputLine = ""; //Resets the output
            for (int j = 0; j < this.board[0].length; j++) {
                if (j != 0) {
                    outputLine += " ";
                }

                if (this.board[i][j] == -1) {
                    outputLine += ".";
                } else {
                    outputLine += this.board[i][j];
                }
            }
            System.out.println(outputLine);
        }
    }

    public ArrayList<String> getPreviousMoves() // Returns the list of Previous moves
    {
        return previousMoves;
    }

    public void setPreviousMoves(ArrayList<String> oldMoves) // Sets the list of Previous moves of the new board
    {
            previousMoves.addAll(oldMoves);
    }

    public void addPreviousMove(String move) // Adds a previous move
    {
        previousMoves.add(move);
    }

    public void populateBoardArray(int[][] input) // Fills the 2D array with a given 2D array
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = input[i][j];
            }
        }
    }
    
    public void transferBlocks(ArrayList<Block> blockIn)
    {
        //THIS SHOULD DO A MANUAL COPY OF EACH BLOCKIN INTO THE BLOCK IN BOARD
        blockInBoard.clear();
        blockInBoard.addAll(blockIn);
    }

    public void fillBoard(ArrayList<Block> blockList) // Fills the 2D board with an arrayList of blocks.
    {
        int length = this.length;
        int width = this.width;

        //Make the 2D array Filled with -1's for Empty slots
        for (int jl = 0; jl < length; jl++) {
            for (int jw = 0; jw < width; jw++) {
                board[jl][jw] = -1;
            }
        }
        for (int x = 0; x < blockList.size(); x++)//For all blocks in the list
        {
            //Check that the positions it needs on the board are free.

            boolean freeSpace = true;
            int loopBlockRow = blockList.get(x).blockRow;
            int loopBlockColumn = blockList.get(x).blockColumn;
            //Check for the width and length of the block that all slots inside it are -1s
            for (int y = 0; y < blockList.get(x).blockLength; y++)//For the length of the block             
            {
                for (int z = 0; z < blockList.get(x).blockWidth; z++) {
                    if (this.board[y + loopBlockRow][z + loopBlockColumn] != -1) {
                        freeSpace = false;
                    }
                }
            }

            if (freeSpace == true)//if space is free add block
            {
                for (int l = 0; l < blockList.get(x).blockLength; l++) {
                    for (int w = 0; w < blockList.get(x).blockWidth; w++) {
                        this.board[l + loopBlockRow][w + loopBlockColumn] = x;
                    }
                }
            } else {
                System.out.println("Not a valid block: block " + x);
            }
        }
    }

    public String[] possibleMoves()//Returns all Possible moves on a given board.
    {

        //Make an ArrayList of all possible moves that Can be done in format {"BoardNo LDir WDir"}

        ArrayList<String> movesList = new ArrayList<>(); // the list of moves in format "No l w"
        int maxBlockVal = 0;

        for (int i = 0; i < board.length; i++)// Goes through all blocks to find the max block Number
        {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] > maxBlockVal) {
                    maxBlockVal = board[i][j];
                }
            }
        }

        for (int i = 0; i <= maxBlockVal; i++) //For each Blocknumber
        {
            if (canMove(i, -1, 0)) //If it can move up
            {
                movesList.add("" + i + " " + -1 + " " + 0); //Add it to the possible Moves
            }
            if (canMove(i, 1, 0)) //If it can move Down
            {
                movesList.add("" + i + " " + 1 + " " + 0); //Add it to the possible Moves
            }
            if (canMove(i, 0, -1)) //If it can move Left
            {
                movesList.add("" + i + " " + 0 + " " + -1); //Add it to the possible Moves
            }
            if (canMove(i, 0, 1)) //If it can move Right
            {
                movesList.add("" + i + " " + 0 + " " + 1); //Add it to the possible Moves
            }
        }

        String[] posMoves = new String[movesList.size()]; //Sets the array that'll be returned to the same as the arrayList
        for (int i = 0; i < movesList.size(); i++) {
            posMoves[i] = movesList.get(i);
        }

        return posMoves;
    }

    public String exportBoard() // returns a string representing the board as a string
    {
        // The String will be each line of numbers split with a "," then the next line.
        String line = "";
        int maxBlockNo = 0;
        for (int i = 0; i<board.length; i++)
        {
            for(int j = 0; j<board[0].length; j++)
            {
                if(board[i][j] > maxBlockNo)
                {
                    maxBlockNo = board[i][j];
                }
            }
        }
        for(int x = 0; x<= maxBlockNo; x++)
        {
            A:for (int i = 0; i < board.length; i++) 
            {
                for (int j = 0; j < board[0].length; j++)
                {
                    if(board[i][j] == x)
                    {
                        line += "" + i + "," + j + " ";
                        break A;
                    }
                }
            }
        }
        return line;
    }

    public boolean canMove(int blockNumber, int lDir, int wDir)//Returns if the block can move in a certain direction
    {
        //METHOD WRITTEN ON NOTEPAD

        boolean canItMove = true;

        ArrayList<String> allBlocks = new ArrayList<>();

        for (int i = 0; i < board.length; i++) //Go across all rows
        {
            for (int j = 0; j < board[0].length; j++)// and across all columns
            {
                if (board[i][j] == blockNumber) //If the block is equal to the number
                {
                    allBlocks.add("" + i + " " + j); // Add the block coords to the arrayList
                }
            }
        }

        if (wDir == 0 && lDir < 0)// If we're going up
        {
            //UP
            ArrayList<Integer> movingFaceVals = new ArrayList<>();
            for (int i = 0; i < allBlocks.size(); i++) // For all the blocks with the right Number
            {

                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value
                movingFaceVals.add(l);
            }

            int faceVal = Collections.min(movingFaceVals); //FIND THE MIN VALUE

            for (int i = 0; i < allBlocks.size(); i++) // For All the blocks with right number
            {
                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value

                if (l == faceVal) //If its on the moving face 
                {
                    if (l < 1) {
                        canItMove = false;
                    }

                    if (canItMove == true) {
                        if (board[l - 1][w] > -1) // and theres a block where it wants to be.
                        {
                            canItMove = false;
                        }
                    }
                }
            }
        }

        if (wDir == 0 && lDir > 0) {
            //DOWN
            ArrayList<Integer> movingFaceVals = new ArrayList<>();
            for (int i = 0; i < allBlocks.size(); i++) // For all the blocks with the right Number
            {

                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value
                movingFaceVals.add(l);
            }

            int faceVal = Collections.max(movingFaceVals); //FIND THE MIN VALUE

            for (int i = 0; i < allBlocks.size(); i++) // For All the blocks with right number
            {
                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value

                if (l == faceVal) //If its on the moving face 
                {
                    if (l > board.length - 2) {
                        canItMove = false;
                    }
                    if (canItMove == true) {
                        if (board[l + 1][w] > -1) // and theres a block where it wants to be.
                        {
                            canItMove = false;
                        }
                    }

                }
            }
        }

        if (wDir < 0 && lDir == 0) {
            //LEFT
            ArrayList<Integer> movingFaceVals = new ArrayList<>();
            for (int i = 0; i < allBlocks.size(); i++) // For all the blocks with the right Number
            {

                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value
                movingFaceVals.add(w);
            }

            int faceVal = Collections.min(movingFaceVals); //FIND THE MIN VALUE
            //System.out.println(faceVal);

            for (int i = 0; i < allBlocks.size(); i++) // For All the blocks with right number
            {
                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value

                if (w == faceVal) //If its on the moving face 
                {
                    if (w < 1) {
                        //System.out.println("This works");
                        canItMove = false;
                    }
                    if (canItMove == true) {
                        //System.out.println("This works");
                        if (board[l][w - 1] > -1) // and theres a block where it wants to be.
                        {
                            //System.out.println("This works");
                            canItMove = false;
                        }
                    }
                }
            }
        }

        if (wDir > 0 && lDir == 0) {
            //Right

            ArrayList<Integer> movingFaceVals = new ArrayList<>();
            for (int i = 0; i < allBlocks.size(); i++) // For all the blocks with the right Number
            {

                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value
                movingFaceVals.add(w);
            }

            int faceVal = Collections.max(movingFaceVals); //FIND THE MIN VALUE

            for (int i = 0; i < allBlocks.size(); i++) // For All the blocks with right number
            {
                String[] temp = allBlocks.get(i).split(" ");
                int l = Integer.parseInt(temp[0]); // Get the L value
                int w = Integer.parseInt(temp[1]); // Get the W value

                if (w == faceVal) //If its on the moving face 
                {
                    if (w > board[0].length - 2) {
                        canItMove = false;
                    }
                    if (canItMove == true) {
                        if (board[l][w + 1] > -1) // and theres a block where it wants to be.
                        {
                            canItMove = false;
                        }
                    }
                }
            }
        }
        return canItMove;
    }

    public void moveBlock(int blockNumber, int lDir, int wDir)// Moves the block in the given direction
    {
        //Coordinates work like so:
        //(x,-1,0) Up
        //(x,1,0) Down
        //(x,0,-1) Left
        //(x,0,1) Right


        int lCoord = 0;
        int wCoord = 0;
        b:
        for (int i =board.length-1; i >=0; i--) //Go across all rows
        {
            for (int j = board[0].length - 1; j >= 0; j--)// and backwards across all columns
            {
                if (board[i][j] == blockNumber) {
                    lCoord = i;
                    wCoord = j;
                    
                }
            }

        }

        ArrayList<String> allBlocks = new ArrayList<>();

        if (!canMove(blockNumber, lDir, wDir)) {
            System.out.println("Cant move a block here!");
        }

        //UP
        if (lDir == -1 && wDir == 0 && canMove(blockNumber, lDir, wDir)) {
            for (int i = 0; i < board.length; i++) //Go across all rows
            {
                for (int j = 0; j < board[0].length; j++)// and across all columns
                {
                    if (board[i][j] == blockNumber) //If the block is equal to the number
                    {
                        board[i][j] = -1;
                        board[i - 1][j] = blockNumber;
                    }
                }
            }
        }

        //DOWN
        if (lDir == 1 && wDir == 0 && canMove(blockNumber, lDir, wDir)) {
            for (int i = board.length - 1; i >= 0; i--) //Go up all rows
            {
                for (int j = 0; j < board[0].length; j++)// and across all columns
                {
                    if (board[i][j] == blockNumber) //If the block is equal to the number
                    {
                        //make the blockNumber -1, then make the one to where its getting moved to the actual number.
                        board[i][j] = -1;
                        board[i + 1][j] = blockNumber;
                    }
                }
            }
        }

        //LEFT
        if (lDir == 0 && wDir == -1 && canMove(blockNumber, lDir, wDir)) {
            for (int i = 0; i < board.length; i++) //Go across all rows
            {
                for (int j = 0; j < board[0].length; j++)// and across all columns
                {
                    if (board[i][j] == blockNumber) //If the block is equal to the number
                    {
                        //make the blockNumber -1, then make the one to where its getting moved to the actual number.
                        board[i][j] = -1;
                        board[i][j - 1] = blockNumber;

                    }
                }
            }
        }

        //RIGHT
        if (lDir == 0 && wDir == 1 && canMove(blockNumber, lDir, wDir)) {
            for (int i = 0; i < board.length; i++) //Go across all rows
            {
                for (int j = board[0].length - 1; j >= 0; j--)// and across all columns
                {
                    if (board[i][j] == blockNumber) //If the block is equal to the number
                    {
                        //make the blockNumber -1, then make the one to where its getting moved to the actual number.
                        board[i][j] = -1;
                        board[i][j + 1] = blockNumber;

                    }
                }
            }
        }

        previousMoves.add("" + lCoord + " " + wCoord + " " + (lCoord+ lDir) + " " + (wCoord+ wDir));


    }
}
