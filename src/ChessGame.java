import java.util.ArrayList;

public class ChessGame {
	char[][] board;
	boolean isRedTurn;
	Piece[] pieces = new Piece[32];
	ChessGame last = null;
	
	public ChessGame() {
		// initialize the game
		this.board = new char[10][9];
		this.isRedTurn = true;
		this.board[0] = new char[]{'C', 'H', 'E', 'A', 'G', 'A', 'E', 'H', 'C'};
		this.board[1] = new char[]{'.','.','.','.','.','.','.','.','.'};
		this.board[2] = new char[]{'.','N','.','.','.','.','.','N','.'};
		this.board[3] = new char[]{'S','.','S','.','S','.','S','.','S'};
		this.board[4] = new char[]{'.','.','.','.','.','.','.','.','.'};
		this.board[5] = new char[]{'.','.','.','.','.','.','.','.','.'};
		this.board[6] = new char[]{'s','.','s','.','s','.','s','.','s'};
		this.board[7] = new char[]{'.','n','.','.','.','.','.','n','.'};
		this.board[8] = new char[]{'.','.','.','.','.','.','.','.','.'};
		this.board[9] = new char[]{'c', 'h', 'e', 'a', 'g', 'a', 'e', 'h', 'c'};
		this.pieces = this.getPieces();
	}
	
	public ChessGame(char[][] board, boolean isRedTurn, Piece[] pieces) {
		// create a game state with given info
		this.isRedTurn = isRedTurn;
		this.board = board;
		this.pieces = pieces;
	}
	
	// return a array of Piece in order
	public Piece[] getPieces() {
		Piece[] re = new Piece[32];
		int reI;
		char[] pis = {'s','s','s','s','s','S','S','S','S','S','n','n','N','N',
				'C', 'C', 'E', 'E', 'A', 'A', 'H', 'H', 'G','c', 'c', 'e', 'e', 'a', 'a', 'h', 'h', 'g'};
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				if(this.board[i][j] != '.') {
					reI = GetIndex(pis, this.board[i][j]);
					while(re[reI] != null) reI++;
					re[reI] = new Piece(i, j, this.board[i][j],  !java.lang.Character.isUpperCase(board[i][j]));
				}
			}
		}
		return re;
	}
	
	private int GetIndex(char[] pis, char c) {
		for (int i = 0; i < pis.length; ++i) {
			if (c == pis[i]) return i;
		}
		return -1;
	}

	// get string representation
	public String toString() {
		String result = this.isRedTurn ? "@LAST_MOVE=BLACK\n" : "@LAST_MOVE=RED\n";
		result += "@@\n\n";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(i != 3 && i != 6) result += board[i][j];
				else result += board[i][j]=='.' ? '.' : board[i][j];
			}
			result += "\n";
			if(i == 4) result += "---------\n";
		}
		
		return result;
	}
	
	// convert string info to a chess game
	public static ChessGame toChessGame(ArrayList<String> lines) {
		boolean isRedTurn;
		char[][] board = new char[10][9];
		if (lines.get(0).equals("chessboard")) {// check type 
			if(lines.get(1).equals("@LAST_MOVE=BLACK")) isRedTurn = true;
			else isRedTurn = false;
			// check dimension
			if(lines.size() != 14) { // 3 + 11(board)
				System.out.println("Invalid Dimension!");
				return null;
			} 
			for (int i = 3; i < 14; i++) {
				if(lines.get(i).length() != 9) {
					System.out.println("Invalid Dimension");
					return null;
				}
				if(i == 8 && !lines.get(i).equals("---------")) {
					System.out.println("Invalid River");
					return null;
				}
			}
			int indexL = 3;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 9; j++) {
					board[i][j] = lines.get(indexL).charAt(j);
				}
				indexL++;
				if(indexL == 8) indexL++;
			}
			
			//number of each piece 
			int[] numPiece = new int[14];// CHEAGNSsncheag
			int[] correctNumPiece = {2,2,2,2,1,2,5,5,2,2,2,2,2,1};
			String namePieces = "CHEAGNSsncheag";
			for(int col = 0; col < 10; col++) {
				for (int row = 0; row < 9; row++) {
					if(board[col][row] != '.') {
						numPiece[namePieces.indexOf(board[col][row])]++;
					}
					if(board[col][row] == 'g') {
						if(col < 7 || !(2 < row && row < 6)) {
							System.out.println("Invalid g");
							return null;
						}
					}
					if(board[col][row] == 'G') {
						if(col > 2 || !(2 < row && row < 6)) {
							System.out.println("Invalid G");
							return null;
						}
					}
					if(board[col][row] == 'a') {
						if(col < 7 || !(2 < row && row < 6) || Math.abs(col-8) != Math.abs(row-4)) {
							System.out.println("Invalid a");
							return null;
						}
					}
					if(board[col][row] == 'A') {
						if(col > 2 || !(2 < row && row < 6) || Math.abs((1-col)) != Math.abs(row-4)) {
							System.out.println("Invalid A");
							return null;
						}
					}
				}
			}
			for (int i = 0; i < numPiece.length; i++) {
				if(!(numPiece[i] == correctNumPiece[i])) {
					System.out.println("Invalid number of " + namePieces.charAt(i));
					return null;
				}
			}
			ChessGame re = new ChessGame(board, isRedTurn, new Piece[32]);
			return re;
		} 
		else if (lines.get(0).equals("chessmoveseq")) {// check type
			int numOfSteps = Integer.valueOf(lines.get(1).split("@TOTAL_STEP=")[1].split(" ")[0]);// get total step
			String[] snums;
			ChessGame re = new ChessGame();
			int[][] nums = new int[lines.size()-3][4];
			int ind = 0;
			for (int i = 0; i < numOfSteps; i++) {
				snums = lines.get(i+3).split(" ");
				for(int j = 0; j < 4; j++) nums[ind][j] = Integer.valueOf(snums[j]);
				ind++;
			}
			// end of getting the data
			
			for (int i = 0; i < nums.length; i++) {
				int y = nums[i][0], x = nums[i][1], ny = nums[i][2], nx = nums[i][3];
				
				if(i%2==0) {//red
					x=10-x;y=9-y;nx=10-nx;ny=9-ny;
					if(!(0<=x && x<10 && 0<=y && y<=9) || !(0<=nx && nx<10 && 0<=ny && ny<=9)) {
						System.out.println("Invalid Position");
						continue;
					}
					else if(re.board[x][y] == '.') {
						System.out.println("No ChessPiece in Start Position");
						continue;
					}
					else if(Character.isUpperCase(re.board[x][y])) {
						System.out.println("Wrong Side of Start ChessPiece");
						continue;
					}
					else if(!Character.isUpperCase(re.board[nx][ny])) {
						System.out.println("Wrong Side of Eaten ChessPiece");
						continue;
					}
					
				}
				else {//black
					x--;y--;nx--;ny--;
					if(!(0<=x && x<10 && 0<=y && y<=9) || !(0<=nx && nx<10 && 0<=ny && ny<=9)) {
						System.out.println("Invalid Position");
						continue;
					}
					else if(re.board[x][y] == '.') {
						System.out.println("No ChessPiece in Start Position");
						continue;
					}
					else if(!Character.isUpperCase(re.board[x][y])) {
						System.out.println("Wrong Side of Start ChessPiece");
						continue;
					}
					else if(Character.isUpperCase(re.board[nx][ny])) {
						System.out.println("Wrong Side of Eaten ChessPiece");
						continue;
					}
				}
				
				
				Piece c = new Piece(x, y, re.board[x][y], !Character.isUpperCase(re.board[x][y]));//cur
				Piece n = null;//next
				if(re.board[nx][ny] != '.') n = new Piece(nx, ny, re.board[nx][ny], !Character.isUpperCase(re.board[x][y]));
				if(c.isValidMove(ny, nx, n, re)) {
					re.board[nx][ny] = c.name;
					re.board[x][y] = '.';
				}
				else {
					System.out.println("Invalid Move!");
				}
				
			}
			System.out.println(re);
			re.pieces = re.getPieces();
			return re;
		}
		else {
			System.out.println("Invalid file type!");
			return null;
		}
	}
	
	
}
