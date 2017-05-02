import java.util.*;
import java.io.*;



public class test {
	public static void main(String[] args) throws FileNotFoundException{
		File input = new File("input.txt");
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(input))));
		int dimension = Integer.parseInt(in.next());
		String mode = in.next();
		String player = in.next();
		int depth = Integer.parseInt(in.next());
		int[][] boardValue = new int[dimension][dimension];
		String[][] boardState = new String[dimension][dimension];
		
		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < dimension; j++){
			boardValue[i][j] = Integer.parseInt(in.next());
			}
		}
		
		for(int i = 0; i < dimension; i++){
			String line = in.next();
			for(int j = 0; j < dimension; j++){
			boardState[i][j] = Character.toString(line.charAt(j));
			}
		}
		
		in.close();
		
		
		Board board = new Board(dimension,boardValue,boardState,0,depth,player,player);		
		
		
		//graph.printGraph(); //debug for integrity of graph reading process.
		
		if(mode.equals("MINIMAX")){
			//testMove(board);
			miniMax(board);
		}
		
		else if(mode.equals("ALPHABETA")){
			//abPruning(board);
			run(board);
		}
		/*else if(mode.equals("COMPETITION")){
			competition(board);
		}*/
		
	}
	
	public static void run(Board board) {
        System.out.println("Start time:" + new Date());
        try {
        	abPruning(board);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
        System.out.println("End time:" + new Date());
    }
	
	
	
	
	public static void miniMax(Board board) throws FileNotFoundException{
		Operation result = miniMaxDecision(board);//System.out.println("----");
		//printMatrix(result.state);System.out.println("-----");
		outPut(board,result);
	}
	
	
	public static Operation miniMaxDecision(Board board){
		Queue<Operation> operationSet = getSuccessor(board);
		HashMap<Operation,Integer> valueSet = new HashMap<Operation,Integer>();

		for(Operation op:operationSet){
			valueSet.put(op, minValue(move(board,op)));
			//printMatrix(move(board,op).boardState);
			//System.out.println("level1");
		}
		
		int max = Collections.max(valueSet.values());
		Queue<Operation> maxOp = new LinkedList<Operation>();
		
		for(Operation oop: operationSet){
			for(Operation op : valueSet.keySet()){
				if(valueSet.get(op) == max && matrixEqual(op.state,oop.state)){
				 maxOp.add(op);
				}
			}
		}
		
		
		//printMatrix(maxOp.peek().state);System.out.println("-----------");
		for(Operation op:maxOp){
			if(op.moveType == "Stake"){
				return op;
			}
		}
		return maxOp.poll();
		
	}
	
	
	
	public static int maxValue(Board board) {
		Queue<Operation> operationSet = getSuccessor(board);
		if(cutOffCheck(board) || terminalCheck(board)){
			return scoreCount(board);
		}
		int v = -9999999;
		
		for(Operation op : operationSet){
		
			v =  Math.max(v,minValue(move(board,op)));
		}
		//System.out.println("-----------------");
		return v;
	}
	
	public static int minValue(Board board) {
		Queue<Operation> operationSet = getSuccessor(board);
		//System.out.println("minValue");
		
		if(cutOffCheck(board) || terminalCheck(board)){
		//	System.out.println("out");
			return scoreCount(board);
		}
		int v = 9999999;
		for(Operation op : operationSet){
		
			//printMatrix(move(board,op).boardState);
			//System.out.println("level 2");
			int a = maxValue(move(board,op));
			//System.out.println("-----"+a+"-----");
			v =  Math.min(v,maxValue(move(board,op)));

		}
		return v;
	}
	
	public static void abPruning(Board board) throws FileNotFoundException{
		Operation result = abDecision(board);
		outPut(board,result);
	}
	
	public static Operation abDecision(Board board){
		Queue<Operation> operationSet = getSuccessor(board);
		HashMap<Operation,Integer> valueSet = new HashMap<Operation,Integer>();
		int a = -999999;
		int b = 999999;

		for(Operation op:operationSet){
			valueSet.put(op, abminValue(move(board,op),a,b));
			//char move = (char)(op.move.y + 65);
			//String printMove = Character.toString(move) + Integer.toString(op.move.x+1);
			//System.out.println(printMove + " "+ op.moveType);//
			//System.out.println(abminValue(move(board,op),a,b));
			//printMatrix(move(board,op).boardState);
			//System.out.println("-------------");
		}
		int max = Collections.max(valueSet.values());
		Queue<Operation> maxOp = new LinkedList<Operation>();
		for(Operation oop: operationSet){
			for(Operation op : valueSet.keySet()){
				if(valueSet.get(op) == max && matrixEqual(op.state,oop.state)){
				 maxOp.add(op);
				}
			}
		}
		for(Operation op:maxOp){
			if(op.moveType == "Stake"){
				return op;
			}
		}
		return maxOp.poll();
		
	}
	
	
	
	public static int abmaxValue(Board board,int a,int b) {
		Queue<Operation> operationSet = getSuccessor(board);
		if(cutOffCheck(board) || terminalCheck(board)){
			return scoreCount(board);
		}
		int v = -999999;
		for(Operation op : operationSet){
			v =  Math.max(v,abminValue(move(board,op),a,b));
			if(v >= b) return v;
			a = Math.max(a, v);
		}
		return v;
	}
	
	public static int abminValue(Board board,int a, int b) {
		Queue<Operation> operationSet = getSuccessor(board);
		if(cutOffCheck(board) || terminalCheck(board)){
			return scoreCount(board);
		}
		int v = 999999;
		for(Operation op : operationSet){
			v =  Math.min(v,abmaxValue(move(board,op),a,b));
			if(v <= a) return v;
			b = Math.min(b, v);
		}
		return v;
	}
	
	
	
	public static void testMove(Board board){
		Queue<Operation> operationSet = getSuccessor(board);
		//System.out.println(operationSet.size());
		for(Operation op: operationSet){
			printMatrix(op.state);
			int opString = op.state.toString().hashCode();
			int test = opString;
			System.out.println(test);System.out.println("^^^^^^^^^^");
			System.out.println(opString);System.out.println("%%%%%%%%%%%%");
			System.out.println(op.hashCode());

			System.out.println(op.moveType);
			System.out.println(op.eval);
			System.out.println(scoreCount(move(board,op)));
			System.out.println("----------------");
			char move = (char)(op.move.y + 65);
			String printMove = Character.toString(move) + Integer.toString(op.move.x);
			
			System.out.println(op.move.y);
			System.out.println(op.move.x);
			System.out.println(printMove);
		}
		
	}
	
	public static void outPut(Board board,Operation minOp) throws FileNotFoundException{
		File outFile = new File("output.txt");
		Queue<Operation> operationSet = getSuccessor(board);
		PrintWriter outPut = new PrintWriter(outFile);
		for(Operation op: operationSet){
			if(matrixEqual(op.state,minOp.state)){
				char move = (char)(op.move.y + 65);
				String printMove = Character.toString(move) + Integer.toString(op.move.x+1);
				outPut.println(printMove + " "+ op.moveType);
				//System.out.println(printMove + " "+ op.moveType);//
				for(int i = 0; i < op.state[0].length; i++){
					StringBuffer buffer = new StringBuffer();
					for(int j = 0; j < op.state.length; j++){
						buffer.append(op.state[i][j]);
					}
					outPut.println(buffer.toString());
					//System.out.println(buffer.toString());//
				}
				outPut.close();
			}
		}
	}
	
	public static boolean cutOffCheck(Board board){
		if(board.targetDepth > board.depth){
			return false;
		}
		return true;
	}
	
	public static boolean terminalCheck(Board board){
		String[][] state = board.boardState;
		int n = board.dimension;
		for(int i = 0;i < n; i++){
			for(int j = 0; j < n;j++){
				if(state[i][j].equals(".")){

					return false;
				}
			}
		}
		return true;
	}
	
	
	
	public static boolean matrixEqual(String[][] a,String[][] b){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[0].length; j++){
				if(!a[i][j].equals(b[i][j])){
					return false;
				}
			}
		}
		return true;
	}
	
	public static void printMatrix(String[][] matrix){
		for(int i = 0; i < matrix.length; i++){
				StringBuffer buffer = new StringBuffer();
			for(int j = 0; j < matrix[0].length; j++){
				buffer.append(matrix[i][j]);
			}
				System.out.println(buffer.toString());
		}
	}
	
	public static int scoreCount(Board board){
		int selfPoints = 0;
		int oppoPoints = 0;
		
		String player = board.mySymbol;
		String oppo = opponent(player);
		//System.out.println(player);
		
		for(int i = 0; i < board.dimension; i++){
			for(int j = 0; j < board.dimension; j++){
				if(board.boardState[i][j].equals(player)){
					selfPoints+=board.boardValue[i][j];
				}
				else if(board.boardState[i][j].equals(oppo)){
					oppoPoints+=board.boardValue[i][j];
				}
			}
		}
		
		return selfPoints - oppoPoints;
	}
	
	
	
	
	public class Node{
		private Board currentBoard;
		private int eval;
		private HashMap<Board,Integer> children = new HashMap<Board,Integer>();
		public Node(Board board){
			this.currentBoard = board;
			this.eval = scoreCount(currentBoard);
		}
		
		public HashMap<Board,Integer> getChildren(){
			return this.children;
		}
	}
	
	
	public static  Queue<Operation> getSuccessor(Board board){
		Queue<Operation> successors = new LinkedList<Operation>();
		Queue<Operation> sortedSuccessors = new LinkedList<Operation>();
		HashMap<Pair, String> successors2 = new HashMap<Pair,String>();
		String[][] state = board.boardState;
		int n = board.dimension;
		String player = board.player;
		String oppo = opponent(player);
		
		//System.out.println(successors.size());

		
		//check all "." position to determine move (operation type "Raid" or "Stake")
		//could be done adding a padding but remain this way so far.
		
		successors2 = getMove(board);
		//System.out.println(successors2.size());
		

		
		//check is over now generate corresponding next state based above type information, here need to search again about the oppo 
		//this can be improved later.
		
		for(Pair move:successors2.keySet()){

			String[][] nextState = arrayCopy(state);
			if(successors2.get(move).equals("Stake")){
				nextState[move.x][move.y] = player;
				successors.add(new Operation(board,nextState,"Stake",move));

			}
			else{
				String type = "Stake";
				if(move.x == 0 && move.y == 0){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				else if(move.x == 0 && move.y == n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}//3
				else if(move.x == n-1 && move.y == n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}//4
				else if(move.x == n-1 && move.y == 0){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				//edge 1
				else if(move.x == 0 && move.y != 0 && move.y != n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				//edge 2
				else if(move.y == n-1 && move.x != 0 && move.x != n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				//edge 3
				else if(move.x == n-1 && move.y != 0 && move.y != n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				//edge 4
				else if(move.y == 0 && move.x != 0 && move.x != n-1){
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
				
				// all inner area
				else{
					nextState[move.x][move.y] = player;
					String[][] stake = arrayCopy(nextState);
					successors.add(new Operation(board,stake,type,move));
					if(nextState[move.x+1][move.y].equals(oppo)){
						nextState[move.x+1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x-1][move.y].equals(oppo)){
						nextState[move.x-1][move.y] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y+1].equals(oppo)){
						nextState[move.x][move.y+1] = player;
						type = "Raid";
					}
					if(nextState[move.x][move.y-1].equals(oppo)){
						nextState[move.x][move.y-1] = player;
						type = "Raid";
					}
					successors.add(new Operation(board,nextState,type,move));
				}
			}
			
		}
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				for(Operation op: successors){
					if(op.move.x == i && op.move.y == j && op.moveType.equals("Stake")){
						sortedSuccessors.add(op);
					}
				}
			}
		}
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				for(Operation op: successors){
					if(op.move.x == i && op.move.y == j && op.moveType.equals("Raid")){
						sortedSuccessors.add(op);
					}
				}
			}
		}
		
		
		return sortedSuccessors;
	}
	
	
	public static class Operation{
		private String[][] state;
		private String moveType;
		private int eval;
		private Pair move;
		private Board board;
		
		public Operation(Board board,String[][] state,String moveType,Pair move){
			this.state = state;
			this.moveType = moveType;
			this.board = move(board,this);
			this.move = move;
			this.eval = scoreCount(this.board);
		}
		
	
	}
	
	
	public static  HashMap<Pair,String> getMove(Board board){

		HashMap<Pair, String> successors2 = new HashMap<Pair,String>();
		String[][] state = board.boardState;
		int n = board.dimension;
		String player = board.player;
		String oppo = opponent(player);
		
		
		//check all "." position to determine move (operation type "Raid" or "Stake")
		//could be done adding a padding but remain this way so far.
		
		if(state[0][0] .equals(".")){
			if(state[0][1].equals(player) || state[1][0].equals(player)){
				successors2.put(new Pair(0,0), "Raid");
			}
			else{
				successors2.put(new Pair(0,0), "Stake");
			}
		}
		
		if(state[0][n-1].equals(".")){
			if(state[0][n-2].equals(player) || state[1][n-1].equals(player)){
				successors2.put(new Pair(0,n-1), "Raid");
			}
			else{
				successors2.put(new Pair(0,n-1), "Stake");
			}
		}
		
		if(state[n-1][n-1].equals(".")){
			if(state[n-2][n-1].equals(player) || state[n-1][n-2].equals(player)){
				successors2.put(new Pair(n-1,n-1), "Raid");
			}
			else{
				successors2.put(new Pair(n-1,n-1), "Stake");
			}
		}
		
		if(state[n-1][0].equals(".")){
			if(state[n-2][0].equals(player) || state[n-1][1].equals(player)){
				successors2.put(new Pair(n-1,0), "Raid");
			}
			else{
				successors2.put(new Pair(n-1,0), "Stake");
			}
		}
		
		
		for(int j = 1; j < n-1; j ++){
			if(state[0][j].equals(".")){
				if(state[1][j].equals(player) ||  state[0][j+1].equals(player) || state[0][j-1].equals(player)){
					successors2.put(new Pair(0,j),"Raid");
				}
				else{
					successors2.put(new Pair(0,j),"Stake");
				}
			}
		}
		
		for(int i = 1; i < n-1; i ++){
			if(state[i][n-1].equals(".")){
				if(state[i+1][n-1].equals(player) ||  state[i][n-2].equals(player) || state[i-1][n-1].equals(player)){
					successors2.put(new Pair(i,n-1),"Raid");
				}
				else{
					successors2.put(new Pair(i,n-1),"Stake");
				}
			}
		}
		
		for(int j = 1; j < n-1; j ++){
			if(state[n-1][j].equals(".")){
				if(state[n-1][j+1].equals(player) ||  state[n-1][j-1].equals(player) || state[n-2][j].equals(player)){
					successors2.put(new Pair(n-1,j),"Raid");
				}
				else{
					successors2.put(new Pair(n-1,j),"Stake");
				}
			}
		}
		
		for(int i = 1; i < n-1; i ++){
			if(state[i][0].equals(".")){
				if(state[i-1][0].equals(player) ||  state[i+1][0].equals(player) || state[i][1].equals(player)){
					successors2.put(new Pair(i,0),"Raid");
				}
				else{
					successors2.put(new Pair(i,0),"Stake");
				}
			}
		}
		
		
		
		for(int i = 1;i < n-1;i++){
			for(int j = 1; j < n-1; j++){
				if(state[i][j].equals(".")){
					if(state[i+1][j].equals(player) || state[i-1][j].equals(player) || state[i][j+1].equals(player) || state[i][j-1].equals(player)){
						successors2.put(new Pair(i,j),"Raid");
					}
					else{
						successors2.put(new Pair(i,j),"Stake");
					}
				}
			}
		}
		
		//check is over now generate corresponding next state based above type information, here need to search again about the oppo 
		//this can be improved later.
		
		return successors2;
	}
	
	
	
	
	public static String[][] arrayCopy(String[][] state){
		String[][] copy = new String[state[0].length][state.length];
		for(int i = 0; i < state[0].length;i++){
			for(int j = 0; j < state.length; j ++){
				copy[i][j] = state[i][j];
			}
		}
		return copy;
	}
	
	public static String opponent(String player){
		if(player.equals("X")){
			return "O";
		}
		else {
			return "X";
		}
	}
	
	
	public static class Pair{
		private int x;
		private int y;
		public Pair(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	
	
	public static Board move(Board board,Operation op){
		Board newBoard = new Board(board.dimension,board.boardValue,op.state,board.depth+1,board.targetDepth, opponent(board.player),board.mySymbol);
		return newBoard;
	}
	
	
	public static class Board{
		private int targetDepth;
		private int dimension;
		private int[][] boardValue;
		private String[][] boardState;
		private int depth;
		private String player;
		private String mySymbol;
		//private int eval;
		//private Queue<Operation> operationSet;
		public Board(int dimension,int[][] boardValue, String[][] boardState,int depth,int targetDepth, String player,String mySymbol){
			this.dimension = dimension;
			this.boardValue = boardValue;
			this.boardState = boardState;
			this.depth = depth;
			this.player = player;
			this.mySymbol = mySymbol;
			//this.eval = scoreCount(this);
			//System.out.println("here");
			//this.operationSet = getSuccessor(this);
			this.targetDepth = targetDepth;
		}
		

	}
	
	public static class Tuple{
		private Pair move;
		private String moveType;
		
		public Tuple(Pair move, String moveType){
			this.move = move;
			this.moveType = moveType;
		}
	}
}
