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

/**
 * This project was created for fun, and a challenge related to coding a set of JPanels in a grid layout within one
 * JFrame. Another challenge was cleanly coding the logic used to house multiple games in one.
 * <p>
 * The players starts with 9 Connect-4 games. 2 Players take turns playing any Connect-4 game they'd like until they tie
 * or win. When a game is finished, the winning color will be displayed in where the Connect-4 game once was. If red
 * wins, a red 'X' is displayed, and if yellow wins then a yellow 'O' is displayed. With these wins, the players then
 * try to win the Tic-Tac-Toe game. If a Connect-4 game is tied, it will display as so in the Connect-4 box. If the
 * Tic-Tac-Toe game is tied, a dialog will display as so.
 */
class Main
{
   public static void main(String[] args)
   {
      new BigBoard();
   }
}
