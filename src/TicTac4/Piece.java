/*
 * Project TicTac4, 2018-04-23T10:24-0500
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class extends a regular JComponent, styled as an outer rectangle used as a border, and an inner oval used as the
 * Connect-4 game piece. It holds mouse listeners used when hovering over a column, to display a highlight, and when
 * clicked: to try and add a piece. It contains methods used to show and remove highlights, change colors, and check
 * for certain conditions.
 */
class Piece extends JComponent
{
   private final BoardPanel panel; // The parent JPanel: the game board
   // The width and height of the piece, dynamic for window size
   private final int width;
   private final int height;
   private Color color; // Declaring the color used for the piece
   private Color turnColor; // The current turn's color

   // Booleans used for remembering if the cell has a highlight, or is filled by non-white
   private Boolean highlighted = false;
   private Boolean filled = false;

   /**
    * The constructor is used to initialize values and add mouse listeners.
    *
    * @param width  The cell width
    * @param height The cell height
    * @param color  The color of the oval inside the cell
    * @param panel  The parent panel
    * @param row    Which row this piece is in
    * @param column Which column this piece is in
    */
   public Piece(int width, int height, Color color, BoardPanel panel, int row, int column)
   {
      this.width = width;
      this.height = height;
      this.color = color;
      this.panel = panel;

      addMouseListener(new MouseAdapter()
      {
         @Override
         public void mousePressed(MouseEvent e)
         {
            panel.addPiece(column);
         }

         @Override
         public void mouseEntered(MouseEvent e)
         {
            canHover();
         }

         @Override
         public void mouseExited(MouseEvent e)
         {
            stopHover();
         }
      });
   }

   /**
    * Paints the Piece's cell. Outer rectangle is blue, to match the Connect-4 board. An oval is then created of the
    * specified color, default white for this program. It also holds code to show a highlighted rectangle above
    * the cell, used when "dropping" a piece in the column.
    *
    * @param g The graphics object used to display
    */
   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g.setColor(Color.BLUE);
      int yPos = 0;
      int xPos = 0;
      g.fillRect(xPos, yPos, width + 2, height + 2);
      g.setColor(color);
      g.fillOval(xPos + 1, yPos + 1, width, height);
      if (highlighted)
      {
         g.setColor(turnColor);
         g.fillRect(xPos + 1, yPos + 1, width + 2, height / 5);
         g.setColor(color);
      }
   }

   /**
    * This function is used to change the color of a Piece
    *
    * @param color The Color object to used
    */
   public void changeColor(Color color)
   {
      this.color = color;
      filled = true;
      repaint();
   }

   /**
    * Used to show a highlighted rectangle above a column when hovering - if allowed
    *
    * @param turnColor The color to change to
    */
   public void highlightColor(Color turnColor)
   {
      this.turnColor = turnColor;
      highlighted = true;
      repaint();
   }

   /**
    * Removes the highlighting from a column when not hovered over anymore
    */
   public void unhighlightColor()
   {
      highlighted = false;
      repaint();
   }

   /**
    * Used to determine the color of the oval
    *
    * @return The Color object currently used for the oval
    */
   public Color getColor()
   {
      return this.color;
   }

   /**
    * Used to call the highlighting method of the parent panel
    */
   private void canHover()
   {
      panel.setHighlightBorder(this);
   }

   /**
    * Used to call the method that removes highlighting from the column, from the parent panel
    */
   private void stopHover()
   {
      panel.setDefaultBorder(this);
      repaint();
   }

   /**
    * Used to determine if a non-white color is contained in the oval
    *
    * @return True if filled, false otherwise
    */
   public Boolean isFilled()
   {
      return filled;
   }
}
