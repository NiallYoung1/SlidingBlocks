/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slidingblocks;

/**
 *
 * @author Niall Young
 */
public class Block { // All Coordinates are for the topleft of the block
    int blockLength; //The Length of the block in the grid
    int blockWidth;  //The Width of the block in the grid
    int blockRow;    //The Row of the block in the grid
    int blockColumn; //The Column of the block in the grid
    
    public Block(int l, int w, int r, int c) // Constructor for block, sets the values
    {
        blockLength = l;
        blockWidth = w;
        blockRow = r;
        blockColumn = c;
    }
 
}
