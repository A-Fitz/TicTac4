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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * This is the only JFrame for Tic-Tac-4. It holds the content JPanel, which creates a 3x3 grid of Connect-4 games. Each
 * Connect-4 game is housed inside of another JPanel, inside of said grid. This class is used to set up the JFrame and
 * create each JPanel needed. It contains functions used to determine if a user has won the game.
 */
class BigBoard extends JFrame
{
   // Tic-Tac-Toe size
   private final int rows = 3;
   private final int columns = 3;

   private final BoardPanel[][] panels; // Matrix of JPanels containing the Connect-4 game
   private final Color[][] winners; // Matrix of Colors that correspond with the winners of Connect-4

   private final JPanel content; // The JPanel that houses each Panel of Connect-4 games

   /**
    * Constructor for the JFrame. Sets the size, initializes the panels and matrices, styles, and sets up the close button
    */
   public BigBoard()
   {
      // Getting the current monitor, setting the JFrame size (square) to it's height
      GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
      int size = gd.getDisplayMode().getHeight() - 50;
      this.setSize(size, size);

      panels = new BoardPanel[rows][columns]; // Initializing matrix of JPanels that hold Connect-4 games
      winners = new Color[rows][columns]; // Initializing matrix of Connect-4 winning colors

      content = new JPanel(new GridLayout(rows, columns)); // Initializing the JPanel that holds Connect-4 Panels


      /*
         Loops through each row and column of the Tic-Tac-Toe game (content panel), adding a new Connect-4 game to each grid section
       */
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            panels[i][j] = new BoardPanel(6, 7, size / columns, size / rows, this);
            panels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Adds grid borders
            content.add(panels[i][j]);
         }
      }

      /*
         Setting close button to do nothing, display dialog asking if they're sure, then close if yes.
       */
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent we)
         {
            close_dialog("Are you sure you want to exit?");
         }
      });

      this.add(content); // Adds the JPanel holding the Connect-4 games in a grid layout
      this.setResizable(false); // Disallow resizing
      this.setVisible(true);
   }

   /**
    * Used to add a winning color, corresponding to the location on the Tic-Tac-Toe board, to the winners matrix
    *
    * @param hasWinner The Connect-4 Panel that has been won
    */
   public void addWinner(BoardPanel hasWinner)
   {
      int[] temp = findPanel(hasWinner);
      winners[Objects.requireNonNull(temp)[0]][temp[1]] = hasWinner.winningColor;
      checkWinners(); // Check if there is a Tic-Tac-Toe winner after every Connect-4 win
   }

   /**
    * This function is to check the winners matrix, seeing if there is a Tic-Tac-Toe winner or if the game is a tie.
    */
   private void checkWinners()
   {
      if (tic_tac_toe_win(winners) != null && tic_tac_toe_win(winners) != Color.BLACK) // Checks if a winning color (Tic-Tac-Toe) was returned
      {
         freezeAll(); // Freeze all Connect-4 games if there is a Tic-Tac-Toe winner
         close_dialog(getColorName(tic_tac_toe_win(winners)) + " has won! Would you like to exit?");
      } else if ((tic_tac_toe_win(winners) == null && isWinnersFull() || tic_tac_toe_win(winners) == Color.BLACK)) // Checks for ties
      {
         freezeAll(); // Freeze all Connect-4 games if each board is filled, with no possible winners
         close_dialog("That's a tie. Would you like to exit?");
      }
   }

   /**
    * This function gives a String value of the color - probably should have just overridden the Color class' toString
    *
    * @param c The color who's name should be returned
    * @return The String corresponding with the possible winning colors
    */
   private String getColorName(Color c)
   {
      if (c == Color.YELLOW)
         return "Yellow";
      else if (c == Color.RED)
         return "Red";
      else if (c == Color.BLACK)
         return "Black";
      else
         return null;
   }

   /**
    * Freezes all Connect-4 games, loops through each panel and calls the setEditable method.
    */
   private void freezeAll()
   {
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            panels[i][j].setEditable(false);
         }
      }
   }

   /**
    * This function is used to determine if the matrix that holds each Connect-4 game's winning color is full.
    *
    * @return True if it is full, false otherwise
    */
   private Boolean isWinnersFull()
   {
      int temp = 0;
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            if (winners[i][j] != null)
               temp++;
         }
      }
      return temp == (rows * columns);
   }

   /**
    * Check three values to see if they are the same. If so, we have a winner for Tic-Tac-4
    *
    * @param tic The first winning color
    * @param tac The second winning color
    * @param toe The third winning color
    * @return True if each color is equal, false otherwise
    */
   private Boolean tic_tac_toe_row(Color tic, Color tac, Color toe)
   {
      return (tic == tac) && (tic == toe) && (tic != null && tac != null && toe != null);
   }

   /**
    * Goes through each possible winning Tic-Tac-Toe combination and calls another function to check if the three possible
    * winning colors are equal.
    *
    * @param grid The matrix used to house the winning colors
    * @return The winning Tic-Tac-4 color if there is a winner, null if there is not
    */
   private Color tic_tac_toe_win(Color grid[][])
   {
      // Loop through the rows
      for (int i = 0; i < 3; i++)
      {
         if (tic_tac_toe_row(grid[i][0], grid[i][1], grid[i][2]))
         {
            return grid[i][0];
         }
      }

      // Loop through the columns
      for (int i = 0; i < 3; i++)
      {
         if (tic_tac_toe_row(grid[0][i], grid[1][i], grid[2][i]))
         {
            return grid[0][i];
         }
      }

      // Check diagonals
      if (tic_tac_toe_row(grid[0][0], grid[1][1], grid[2][2]))
      {
         return grid[0][0];
      }

      if (tic_tac_toe_row(grid[0][2], grid[1][1], grid[2][0]))
      {
         return grid[0][2];
      }

      return null;
   }

   /**
    * Used to find a panel object's indexes in the Connect-4 panel matrix
    *
    * @param panel The Connect-4 panel to search for
    * @return The indexes, inside an integer array, if the object is found. -1 if it is not
    */
   private int[] findPanel(BoardPanel panel)
   {
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            if (panel.equals(panels[i][j]))
            {
               return new int[]{i, j};
            }
         }
      }
      return null;
   }

   /**
    * Used to show a JOptionPane asking whether or not the user wants to exit the game. Contains a customizable line.
    *
    * @param caption The caption for the dialog.
    */
   private void close_dialog(String caption)
   {
      String ObjButtons[] = {"Yes", "No"};
      int PromptResult = JOptionPane.showOptionDialog(null, caption, "Tic Tac 4", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
      if (PromptResult == JOptionPane.YES_OPTION)
      {
         System.exit(0);
      }
   }

}
