package AbstractGames;


public class MinimaxSearch<BOARD extends Board, MOVE extends Move> implements Search<BOARD,MOVE>  {
  BOARD board;
  int totalNodesSearched;
  int totalLeafNodes;

  @Override
  public MOVE findBestMove(BOARD board, int depth) {
    MOVE best_move = null;
    int runningNodeTotal = 0;
    long startTime = System.currentTimeMillis();
    long elapsedTime = 0;
    long currentPeriod;
    long previousPeriod = 0;
    int i = 1;

    this.board = board;

    // Including the iterative deepening for consistency.
    while ( i <= depth ) {
      totalNodesSearched = totalLeafNodes = 0;

      best_move = Minimax(i); // Min-Max alpha beta with transposition tables

      elapsedTime = System.currentTimeMillis()-startTime;
      currentPeriod = elapsedTime-previousPeriod;
      double rate = 0.0;
      if ( i > 3 && previousPeriod > 50 )
        rate = (currentPeriod - previousPeriod)/previousPeriod;
      previousPeriod = elapsedTime;

      runningNodeTotal += totalNodesSearched;
      System.out.println("Depth: " + i +" Time: " + elapsedTime/1000.0 + " " + currentPeriod/1000.0 + " Nodes Searched: " +
          totalNodesSearched + " Leaf Nodes: " + totalLeafNodes + " Rate: " + rate);

      // increment indexes;
      i = i + 2;
    }

    System.out.println("Nodes per Second = " + runningNodeTotal/(elapsedTime/1000.0));
    if (best_move == null ) {
      throw new Error ("No Move Available - Search Error!");
    }
    return best_move;
  }

  /**
   * TODO Write Minimax here!
   *
   * @param depth Depth to search to
   * @return best move found at this node
   */
  private MOVE Minimax(int depth) {

    //generate available moves
    Move maxminMoves = this.board.generateMoves();

    // Terminating condition
    if(this.board.endGame() == 1){ 
        maxminMoves.value += (10-depth);
        totalLeafNodes++;
        @SuppressWarnings("unchecked") MOVE m = (MOVE)maxminMoves;
        return m;
    }
    else if(this.board.endGame() == 0){
      maxminMoves.value += (-10);
      totalLeafNodes++;
      @SuppressWarnings("unchecked") MOVE m = (MOVE)maxminMoves;
       return m;
    }
    else if (this.board.endGame() == BOARD.GAME_DRAW){
      totalLeafNodes++;
      @SuppressWarnings("unchecked") MOVE m = (MOVE)maxminMoves;
      return m;
    }

    @SuppressWarnings("unchecked") MOVE bestMove = (MOVE)maxminMoves;
    

    //assign values to moves
    //this.board.moveOrdering(maxminMoves, depth);
    //sort by value
    //Util.QuickSort(maxminMoves);

    //If player is BLACK, select MAX value Move
    if(this.board.getCurrentPlayer() == 1){
      bestMove.value = Double.MIN_VALUE;
      while(maxminMoves != null){
        this.board.makeMove(maxminMoves);
        totalNodesSearched++;
        MOVE value = Minimax(depth + 1);
        bestMove = maxMove(bestMove, value);
        this.board.reverseMove(maxminMoves);
        maxminMoves = maxminMoves.next;
        return bestMove;
      }
    }
    //If player is WHITE, select MIN value Move
    else{
      bestMove.value = Double.MAX_VALUE;
      while(maxminMoves != null){
        this.board.makeMove(maxminMoves);
        totalNodesSearched++;
        MOVE value = Minimax(depth + 1);
        bestMove = minMove(bestMove, value);
        this.board.reverseMove(maxminMoves);
        maxminMoves = maxminMoves.next;
        return bestMove;
      }
    }
    return null;
}

  /**
   * maxMove
   *
   * @param m1 Move to compare
   * @param m2 Move to compare
   * @return Move with higher value
   */
  public MOVE maxMove(MOVE m1, MOVE m2){
    if(m2 == null){return m1;}
    else if(m1 == null){return m2;}
    return m1.value >= m2.value ? m1 : m2;
  }

    /**
   * minMove
   *
   * @param m1 Move to compare
   * @param m2 Move to compare
   * @return Move with lower value
   */
  public MOVE minMove(MOVE m1, MOVE m2){
    if(m2 == null){return m1;}
    else if(m1 == null){return m2;}
    return m1.value <= m2.value ? m1 : m2;
  }
}
