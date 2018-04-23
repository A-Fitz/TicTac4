/*
 * Project TicTac4, 2018-04-22T22:24-0500
 *
 * Copyright 2018 Austin FitzGerald
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package TicTac4;

import javax.swing.*;
import java.awt.*;

/**
 * This class holds the code needed to create each Connect-4 game. The panel is split into a specified number of rows and
 * columns (usually 6x7) into a grid. Each grid cell holds a Piece object, for which colors are changed when Connect-4
 * pieces are placed. This class is responsible for keeping track of each Piece object's position, and to report to
 * the JFrame class if the game has been won. There are methods to freeze the game board, to display the winner, and
 * to show various styling.
 */
class BoardPanel extends JPanel
{
   private final BigBoard bigBoard; // Used to reference the JFrame object

   // The width and height of the Connect-4 game board - used to make the JPanel.
   private final int width;
   private final int height;

   // The number of rows and columns for each Connect-4 game (usually 6x7)
   private final int rows;
   private final int columns;

   private final Piece[][] pieces; // The matrix of Piece objects needed for the game
   // If the game has been won, hold the winning color in this variable
   Color winningColor;
   // The current color variable changes for each turn
   private Color currentColor;
   // Changes if the game has been won or filled, stops new pieces from being added
   private Boolean editable;

   /**
    * Constructor that sets up each Connect-4 game. Initializes variables and styles, then runs.
    *
    * @param rows     The number of rows for the game (usually 6)
    * @param columns  The number of columns for the game (usually 7)
    * @param width    The width of the JPanel
    * @param height   The height of the JPanel
    * @param bigBoard The main JFrame parent
    */
   public BoardPanel(int rows, int columns, int width, int height, BigBoard bigBoard)
   {
      this.bigBoard = bigBoard;
      editable = true; // Editable by default

      this.rows = rows;
      this.columns = columns;
      pieces = new Piece[rows][columns]; // Initializes the matrix of Piece objects

      currentColor = Color.RED; // Sets the starting color

      this.width = width;
      this.height = height;

      setLayout(new GridLayout(rows, columns)); // Creates a grid of Pieces
      setBorder(BorderFactory.createEmptyBorder());
      set(); // Fills the board with empty spaces
   }

   /**
    * This function determines if anyone has won the game. It goes through each possible winning combination and checks
    * if the colors match for a given Piece's location.
    *
    * @param pieceRow    The Piece's row
    * @param pieceColumn The Piece's column
    * @return True if the game has been won, false otherwise
    */
   private boolean hasWon(int pieceRow, int pieceColumn)
   {

      // Get type of the piece at the given coordinates
      Piece pieceType = pieces[pieceRow][pieceColumn];

      // Check if piece won vertically
      if (pieceRow < 3 && pieces[pieceRow + 1][pieceColumn].getColor() == pieceType.getColor() &&
              pieces[pieceRow + 2][pieceColumn].getColor() == pieceType.getColor() &&
              pieces[pieceRow + 3][pieceColumn].getColor() == pieceType.getColor())
      {
         winningColor = pieceType.getColor();
         return true;
      }

      // Check if piece won horizontally
      for (int columnNum = 0; columnNum < 4; columnNum++)
      {
         if (pieces[pieceRow][columnNum].getColor() == pieceType.getColor() &&
                 pieces[pieceRow][columnNum + 1].getColor() == pieceType.getColor() &&
                 pieces[pieceRow][columnNum + 2].getColor() == pieceType.getColor() &&
                 pieces[pieceRow][columnNum + 3].getColor() == pieceType.getColor())
         {
            winningColor = pieceType.getColor();
            return true;
         }
      }

      // Check if piece won diagonally
      for (int rowNum = 0; rowNum < 3; rowNum++)
      {
         for (int columnNum = 0; columnNum < 4; columnNum++)
         {
            if (pieces[rowNum][columnNum].getColor() == pieceType.getColor() &&
                    pieces[rowNum + 1][columnNum + 1].getColor() == pieceType.getColor() &&
                    pieces[rowNum + 2][columnNum + 2].getColor() == pieceType.getColor() &&
                    pieces[rowNum + 3][columnNum + 3].getColor() == pieceType.getColor())
            {
               winningColor = pieceType.getColor();
               return true;
            }
         }
      }
      for (int rowNum = 3; rowNum < 6; rowNum++)
      {
         for (int columnNum = 0; columnNum < 4; columnNum++)
         {
            if (pieces[rowNum][columnNum].getColor() == pieceType.getColor() &&
                    pieces[rowNum - 1][columnNum + 1].getColor() == pieceType.getColor() &&
                    pieces[rowNum - 2][columnNum + 2].getColor() == pieceType.getColor() &&
                    pieces[rowNum - 3][columnNum + 3].getColor() == pieceType.getColor())
            {
               winningColor = pieceType.getColor();
               return true;
            }
         }
      }

      // Checks if the board is full, but no winner found. If so, make the winning color black
      if (isBoardFull())
      {
         winningColor = Color.BLACK;
         return true;
      }

      return false;
   }

   /**
    * This function is used to determine if the Connect-4 game board is full of pieces, no empty spots
    *
    * @return True if full, false otherwise
    */
   private Boolean isBoardFull()
   {
      int temp = 0;
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            if (pieces[i][j].getColor() != Color.WHITE) // White is the default color
               temp++;
         }
      }
      return temp == (rows * columns);
   }

   /**
    * This fills the game board with empty pieces - technically they're not empty, just white
    */
   private void set()
   {
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            pieces[i][j] = new Piece(width / columns, height / rows, Color.WHITE, this, i, j);
            add(pieces[i][j]);
         }

      }
   }

   /**
    * Used to show the column highlighting border for each Connect-4 game, shows a small rectangle with the current turn's
    * color above the hovered column
    *
    * @param hoveredPiece The piece that is currently being hovered.
    */
   public void setHighlightBorder(Piece hoveredPiece)
   {
      if (editable)
      { // Don't show the highlighting if the board isn't editable
         int[] position = findPiece(hoveredPiece);
         if (position != null)
         {
            int column = position[1]; // Row doesn't matter

            pieces[0][column].highlightColor(currentColor);
            validate();
            repaint();
         }
      }
   }

   /**
    * Used to remove the highlighted rectangle when not hovering over the column any longer.
    *
    * @param unhoveredPiece The piece to remove the highlighted rectangle from
    */
   public void setDefaultBorder(Piece unhoveredPiece)
   {
      int[] position = findPiece(unhoveredPiece);
      if (position != null)
      {
         int column = position[1];

         pieces[0][column].unhighlightColor();
         validate();
         repaint();
      }
   }

   /**
    * This function is used to find the matrix indexes of a given Piece object
    *
    * @param p The Piece object to search for
    * @return An array of integers, holding row and column, of a piece if found. Null otherwise.
    */
   private int[] findPiece(Piece p)
   {
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            if (p.equals(pieces[i][j]))
            {
               return new int[]{i, j};
            }
         }
      }
      return null;
   }

   /**
    * Called when a piece is to be added to the board. If it can be filled, do as so and switch the turns.
    * Also checks if the Connect-4 game has a winner after each added piece.
    *
    * @param column The column for a piece to be "dropped" in.
    */
   public void addPiece(int column)
   {
      if (editable)
      { // Only add when editable
         for (int i = rows - 1; i >= 0; i--)
         { // Place in the nearest spot from the bottom, not the top
            if (! pieces[i][column].isFilled())
            {
               pieces[i][column].changeColor(currentColor);
               switchTurn();

               if (hasWon(i, column))
               {
                  editable = false;
                  winner();
               }

               return;
            }
         }
      }
   }

   /**
    * Changes the editing boolean for the Connect-4 game
    *
    * @param b True or false, whether the board can be edited or not
    */
   public void setEditable(Boolean b)
   {
      this.editable = b;
   }

   /**
    * Every time a piece is added, the turn needs to be changed. This function does as so.
    */
   private void switchTurn()
   {
      if (currentColor == Color.YELLOW)
      {
         currentColor = Color.RED;
      } else if (currentColor == Color.RED)
      {
         currentColor = Color.YELLOW;
      }
   }

   /**
    * When the game has been won, or tied, reset all pieces and display the Tic-Tac-Toe symbol with the same color.
    * If the game was tied, display a sad face ):
    */
   private void winner()
   {
      //reset all to white
      resetAll();

      if (winningColor == Color.RED)
      {
         drawX();
      } else if (winningColor == Color.YELLOW)
      {
         drawY();
      } else if (winningColor == Color.BLACK)
      {
         drawSad();
      }
      bigBoard.addWinner(this); // Calls the addWinner function in the JFrame class
   }

   /**
    * Resets all pieces on the game board to white, just like the start.
    */
   private void resetAll()
   {
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            pieces[i][j].changeColor(Color.WHITE);
         }
      }
   }

   /**
    * Used when the color red has won the game, shows the Tic-Tac-Toe symbol 'X' in red.
    * |x| | | | | |x|
    * | |x| | | |x| |
    * | | |x| |x| | |
    * | | | |x| | | |
    * | | |x| |x| | |
    * | |x| | | |x| |
    */
   private void drawX()
   {
      pieces[1][1].changeColor(Color.RED);
      pieces[0][0].changeColor(Color.RED);
      pieces[2][2].changeColor(Color.RED);
      pieces[3][3].changeColor(Color.RED);
      pieces[4][4].changeColor(Color.RED);
      pieces[5][5].changeColor(Color.RED);
      pieces[5][1].changeColor(Color.RED);
      pieces[4][2].changeColor(Color.RED);
      pieces[2][4].changeColor(Color.RED);
      pieces[1][5].changeColor(Color.RED);
      pieces[0][6].changeColor(Color.RED);
   }

   /**
    * Used when the color yellow has won, displaying the Tic-Tac-Toe symbol 'O' in yellow.
    * | | |o|o|o| | |
    * | |o| | | |o| |
    * | |o| | | |o| |
    * | |o| | | |o| |
    * | |o| | | |o| |
    * | | |o|o|o| | |
    */
   private void drawY()
   {
      pieces[1][1].changeColor(Color.YELLOW);
      pieces[2][1].changeColor(Color.YELLOW);
      pieces[3][1].changeColor(Color.YELLOW);
      pieces[4][1].changeColor(Color.YELLOW);
      pieces[0][2].changeColor(Color.YELLOW);
      pieces[5][2].changeColor(Color.YELLOW);
      pieces[5][3].changeColor(Color.YELLOW);
      pieces[0][3].changeColor(Color.YELLOW);
      pieces[0][4].changeColor(Color.YELLOW);
      pieces[5][4].changeColor(Color.YELLOW);
      pieces[1][5].changeColor(Color.YELLOW);
      pieces[2][5].changeColor(Color.YELLOW);
      pieces[3][5].changeColor(Color.YELLOW);
      pieces[4][5].changeColor(Color.YELLOW);
   }

   /**
    * Used to show a sad face when the Connect-4 game results in a tie.
    * | | | | | | | |
    * | | |-| |-| | |
    * | | | | | | | |
    * | | |-|-|-| | |
    * | |-| | | |-| |
    * | | | | | | | |
    */
   private void drawSad()
   {
      pieces[1][2].changeColor(Color.BLACK);
      pieces[1][4].changeColor(Color.BLACK);
      pieces[4][1].changeColor(Color.BLACK);
      pieces[3][2].changeColor(Color.BLACK);
      pieces[3][3].changeColor(Color.BLACK);
      pieces[3][4].changeColor(Color.BLACK);
      pieces[4][5].changeColor(Color.BLACK);

   }
}
