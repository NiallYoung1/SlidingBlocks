/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slidingblocks;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

/**
 *
 * @author Niall Young
 */
public class SlidingBlocks {

    /**
     * @param args the command line arguments
     */
    static boolean debug = false;
    //Boolean's for the debuging options.
    static boolean debugoptions = false;
    static boolean debugboard = false;
    static boolean debugprevious = false;
    static boolean debugboards = false;
    static boolean debugloop = false;
    static boolean debugattempts = false;
    static boolean debugsuccess = false;

    public static void main(String[] args) {


        String inputL;
        String targetL;

        if (args.length == 3 && args[0].startsWith("-o")) { // If we take in 3 arguements then we're debugging
            String debugLine = args[0];
            inputL = args[1];
            targetL = args[2];

            String options = debugLine.substring(2);

            
            for (int r = 0; r < options.length(); r++) { // For each character
                char current = options.charAt(r);
                
                if (current == 'o') {
                System.out.println("         DEBUGING OPTIONS:");
                System.out.println("*************************************");
                System.out.println("*p: Shows amount of previous Boards *");
                System.out.println("*x: Shows amount of current Boards  *");
                System.out.println("*l: Shows the loop iterations       *");
                System.out.println("*a: Shows the attempted moves       *");
                System.out.println("*s: Shows the succussful moves      *");
                System.out.println("*board: Prints each moved board     *");
                System.out.println("*************************************");
                r += 6;
            }
                
                if (current == 'b') {
                    debugboard = true;
                    r += 4; //Should skip "oard" then r++ will land it on the next char
                } else if (current == 'p') {
                    debugprevious = true;
                } else if (current == 'x') {
                    debugboards = true;
                } else if (current == 'l') {
                    debugloop = true;
                } else if (current == 'a') {
                    debugattempts = true;
                } else if (current == 's') {
                    debugsuccess = true;
                }
            }
            debug = true;
        } else if (args.length == 2) { // if its 2 arguements then we do no debugging
            inputL = args[0];
            targetL = args[1];
            debug = false;
        } else {
            throw new IllegalArgumentException("Optional debugging must begin with '-0'.");
        }

        java.io.File inputFile = new java.io.File(inputL); //Takes the given file
        java.io.File targetFile = new java.io.File(targetL); //Takes the solution file
        try {
            Scanner input = new Scanner(inputFile); //The scanner to get input
            Scanner target = new Scanner(targetFile); //The scanner for the solution
            String line = input.nextLine(); //The First line
            String targetLine; // The first line of target

            String[] boardDimensions = line.split(" "); //Gets the dimensions of the board in a String Array
            int boardLength = Integer.parseInt(boardDimensions[0]);
            int boardWidth = Integer.parseInt(boardDimensions[1]);

            ArrayList<Block> blockList = new ArrayList<>(); // The list of blocks in the given board.
            ArrayList<Block> targetBlockList = new ArrayList<>(); // The list or blocks in the target board.

            while (input.hasNext())//For all blocks on the input:
            {
                line = input.nextLine();
                String[] blockSpecs = line.split(" ");
                int l, w, r, c; //Temp ints for the dimentions
                l = Integer.parseInt(blockSpecs[0]); // Sets all the temp ints to the Line Values
                w = Integer.parseInt(blockSpecs[1]);
                r = Integer.parseInt(blockSpecs[2]);
                c = Integer.parseInt(blockSpecs[3]);

                Block newBlock = new Block(l, w, r, c); //Makes the new block with the specs                
                blockList.add(newBlock); // Adds the block to the ArrayList
            } //End the while as we've done all the blocks

            while (target.hasNext())// While the target has next block.
            {
                targetLine = target.nextLine();
                String[] blockSpecsTarget = targetLine.split(" ");
                int l, w, r, c; //Temp ints for the block attributes
                l = Integer.parseInt(blockSpecsTarget[0]);
                w = Integer.parseInt(blockSpecsTarget[1]);
                r = Integer.parseInt(blockSpecsTarget[2]);
                c = Integer.parseInt(blockSpecsTarget[3]);

                Block newTargetBlock = new Block(l, w, r, c);
                targetBlockList.add(newTargetBlock);// Adds the new block with the right specs to the list.
            } // End the while once we've done all blocks for the target

            Board inputBoard = new Board(boardLength, boardWidth); //Makes an input board instance
            inputBoard.fillBoard(blockList); // Fills the input board instance with the blocks
            inputBoard.transferBlocks(blockList);

            Board targetBoard = new Board(boardLength, boardWidth); //Makes a target board instance with the same dimensions.
            targetBoard.fillBoard(targetBlockList); // Fills the target board with blocks
            targetBoard.transferBlocks(targetBlockList);

            ArrayList<Board> boards = new ArrayList<>(); // ArrayList of all the boards
            boards.add(inputBoard); // Add the initial imput Board
            Board solutionBoard = new Board(boardLength, boardWidth); // This will be the board that will be returned.
            int boardCounter = boards.size();
            int loopCounter = 0;

            //These just allow for easy viewing of the board, to see if The program is actually working...
            //inputBoard.printBoard();
            //System.out.println("");
            //targetBoard.printBoard();
            //System.out.println("");

            if (debug) {
                debugSolver(inputBoard, targetBoard, boardLength, boardWidth);
            }

            if (!debug) {
                normalSolver(inputBoard, targetBoard, boardLength, boardWidth);
            }
            
            for (int n = 0; n < solutionBoard.previousMoves.size(); n++) // For all previous moves for the solution board.
            {
                System.out.println(solutionBoard.previousMoves.get(n)); // Print them out.
            }
        } catch (FileNotFoundException e) { // Used to catch when theres no file to run.
            System.err.format("File does not exist, Check input \n");
        }
    } // End of Main

    public static void debugSolver(Board inputBoard, Board targetBoard, int boardLength, int boardWidth) {
        ArrayList<Board> boards = new ArrayList<>(); // ArrayList of all the boards
        HashSet<String> previousBoards = new HashSet<>(); // The List of previous boardStates from export Board.

        boards.add(inputBoard); // Add the initial imput Board
        Board solutionBoard = new Board(boardLength, boardWidth); // This will be the board that will be returned.
        int loopCounter = 0;

        free:
        while (!boardSolved(boards, targetBoard)) // While theres no solution
        {
            //boardCounter = boards.size();   // Sets the count of boards to the amount of boards in the arrayList  
            for (int i = 0; i < boards.size(); i++) // For all the boards in the array Currently.
            {
                Board copyBoard = boards.get(0);
                boards.remove(0);
                String[] validMoves = copyBoard.possibleMoves(); // An array of all possible moves for the board i

                for (int m = 0; m < validMoves.length; m++) // For all possible moves for board i
                {
                    //  " x y z" is the layout for moves

                    if (debugattempts) {
                        System.out.println("Attempted Move: " + validMoves[m]);
                    }

                    String[] splitMoves = validMoves[m].split(" "); // Splits each move into the direction and block number
                    int bNo = Integer.parseInt(splitMoves[0]); // Gives the Block Number
                    int lD = Integer.parseInt(splitMoves[1]); // Gives the L Direction
                    int wD = Integer.parseInt(splitMoves[2]); // Gives the W Direction

                    Board tempBoard = new Board(boardLength, boardWidth); // Makes a temporary board which is essentially blank

                    tempBoard.populateBoardArray(copyBoard.board); // Populates the Board 2d Array with the 2D array of the Board its getting moved from
                    tempBoard.setPreviousMoves(copyBoard.previousMoves); // Adds in the previous moves that said board has had done to it.
                    tempBoard.moveBlock(bNo, lD, wD); // Moves the specific block on the new board in the direction stated.

                    if (debugsuccess) {
                        System.out.println("Successful Move: " + validMoves[m]);
                    }

                    if (!previousBoards.contains(tempBoard.exportBoard())) // If the previousBoards doesnt contain the state of the new moved board
                    {
                        previousBoards.add(tempBoard.exportBoard()); // Add the state off the new board to the previous boards list.

                        if (debugboard) {
                            tempBoard.printBoard();
                        }

                        if (debugprevious) {
                            System.out.println("Number of previous boards: " + previousBoards.size());
                        }

                        //IF add(0, temp boards) then its DFS.
                        boards.add(tempBoard); // Add the board to the boards Array.
                        if (debugboards) {
                            System.out.println("Number of Boards: " + boards.size());
                        }
                    }

                    if (boardComplete(tempBoard, targetBoard)) // If the current tempboard has solved the problem set it to the solutionBoard and break the loop.
                    {
                        solutionBoard = tempBoard; //Set the solution board
                        break free; // I WANT TO BREAK FREE!!!!
                    }
                }

                //If theres no more possible moves its impossible
                if (boards.isEmpty()) {
                    System.out.println("This is an impossible board."); // Its an impossible board
                    break free; //Give up...
                }
            }
            loopCounter++;
        }

        if (debugloop) {
            System.out.println("LoopCounter: " + loopCounter);
        }

        for (int n = 0; n < solutionBoard.previousMoves.size(); n++) // For all previous moves for the solution board.
        {
            System.out.println(solutionBoard.previousMoves.get(n)); // Print them out.
        }
    }

    public static void normalSolver(Board inputBoard, Board targetBoard, int boardLength, int boardWidth) {
        ArrayList<Board> boards = new ArrayList<>(); // ArrayList of all the boards
        HashSet<String> previousBoards = new HashSet<>(); // The List of previous boardStates from export Board.

        boards.add(inputBoard); // Add the initial imput Board
        Board solutionBoard = new Board(boardLength, boardWidth); // This will be the board that will be returned.

        free:
        while (!boardSolved(boards, targetBoard)) // While theres no solution
        {
            for (int i = 0; i < boards.size(); i++) // For all the boards in the array Currently.
            {
                Board copyBoard = boards.get(0);
                boards.remove(0);
                String[] validMoves = copyBoard.possibleMoves(); // An array of all possible moves for the board i

                for (int m = 0; m < validMoves.length; m++) // For all possible moves for board i
                {
                    //  " x y z" is the layout for moves

                    String[] splitMoves = validMoves[m].split(" "); // Splits each move into the direction and block number
                    int bNo = Integer.parseInt(splitMoves[0]); // Gives the Block Number
                    int lD = Integer.parseInt(splitMoves[1]); // Gives the L Direction
                    int wD = Integer.parseInt(splitMoves[2]); // Gives the W Direction

                    Board tempBoard = new Board(boardLength, boardWidth); // Makes a temporary board which is essentially blank

                    tempBoard.populateBoardArray(copyBoard.board); // Populates the Board 2d Array with the 2D array of the Board its getting moved from
                    tempBoard.setPreviousMoves(copyBoard.previousMoves); // Adds in the previous moves that said board has had done to it.
                    tempBoard.moveBlock(bNo, lD, wD); // Moves the specific block on the new board in the direction stated.

                    if (!previousBoards.contains(tempBoard.exportBoard())) // If the previousBoards doesnt contain the state of the new moved board
                    {
                        previousBoards.add(tempBoard.exportBoard()); // Add the state off the new board to the previous boards list.

                        //IF add(0, temp boards) then its DFS.
                        boards.add(tempBoard); // Add the board to the boards Array.
                    }

                    if (boardComplete(tempBoard, targetBoard)) // If the current tempboard has solved the problem set it to the solutionBoard and break the loop.
                    {
                        solutionBoard = tempBoard; //Set the solution board
                        break free; // I WANT TO BREAK FREE!!!!
                    }
                }

                //If theres no more possible moves its impossible
                if (boards.isEmpty()) {
                    System.out.println("This is an impossible board."); // Its an impossible board
                    break free; //Give up...
                }
            }
        }

        for (int n = 0; n < solutionBoard.previousMoves.size(); n++) // For all previous moves for the solution board.
        {
            System.out.println(solutionBoard.previousMoves.get(n)); // Print them out.
        }
    }

    public static boolean boardSolved(ArrayList<Board> boardsIn, Board boardTarg) {
        boolean finished = false;

        for (int i = 0; i < boardsIn.size(); i++) // For all the boards being checked
        {
            if (boardComplete(boardsIn.get(i), boardTarg)) // If board i is complete against the target board
            {
                finished = true; // We're done
            }
        }

        return finished;
    }

    public static boolean boardComplete(Board boardIn, Board boardTarget) {
        boolean complete = true; // The boolean to be returned.
        ArrayList<String> targetSlots = new ArrayList<>(); // An array of LW coords with blocks in them

        for (int l = 0; l < boardTarget.board.length; l++)//For all Rows
        {
            for (int w = 0; w < boardTarget.board[0].length; w++)//For all Columns within Rows
            {
                if (boardTarget.board[l][w] != -1)//If its not an empty slot
                {
                    targetSlots.add("" + l + " " + w); // Add the lw 
                }
            }
        }


        for (int x = 0; x < boardTarget.blockInBoard.size(); x++) {

            Block currentBlock = boardTarget.blockInBoard.get(x);
            int IDno = boardIn.board[currentBlock.blockRow][currentBlock.blockColumn];

            if (IDno == -1) {
                complete = false;
            }

            for (int w = currentBlock.blockRow; w < currentBlock.blockRow + currentBlock.blockLength; w++) {
                for (int w2 = currentBlock.blockColumn; w2 < currentBlock.blockColumn + currentBlock.blockWidth; w2++) {
                    if (boardIn.board[w][w2] != IDno) {
                        complete = false;
                    }
                }
            }

            int counter = 0;
            for (int z = 0; z < boardIn.length; z++) {
                for (int z2 = 0; z2 < boardIn.width; z2++) {
                    if (boardIn.board[z][z2] == IDno) {
                        counter++;
                    }
                }
            }

            if (counter != (currentBlock.blockLength * currentBlock.blockWidth)) {
                complete = false;
            }
        }
        return complete;
    }
} // End of Class
