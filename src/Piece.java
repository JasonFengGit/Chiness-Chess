// a class that stores information of a piece
public class Piece {
	public int x;
	public int y;
	public char name;
	public boolean red;
	public boolean dead = false;
	
	public Piece(int x, int y, char name, boolean red) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.red = red;
	}
	
	// get string representation (for debug)
	public String toString() {
		return "" + this.name + " " + Integer.toString(this.x) + "," + Integer.toString(this.y);
	}
	
	// check if a move is valid 
	public boolean isValidMove(int ny, int nx, Piece p, ChessGame cg) {
		/**
		 * get a int representation of a move to determine the pattern of a move
		 * ex. horse moves in 21 or 12; elephant moves in 22;
		 */
		int d = 10 * Math.abs(nx - x) + Math.abs(ny - y);
		switch (this.name) {
		case 'G':
			if(p != null && p.name == 'g' && ny == y && this.numOfPieceInPath(cg, p, nx, ny)==0) return true;
			if(nx > 2 || ny < 3 || ny > 5 || d != 10 && d!=1) return false;
			return true;
		case 'g':
			if(p != null && p.name == 'G' && ny == y && this.numOfPieceInPath(cg, p, nx, ny)==0) return true;
			if(nx < 7 || ny < 3 || ny > 5 || d != 10 && d!=1) return false;
			return true;
		case 'A':
			if(nx > 2 || ny < 3 || ny > 5 || d != 11) return false;
			return true;
		case 'a':
			if(nx < 7 || ny < 3 || ny > 5 || d != 11) return false;
			return true;
		case 'H':
			if(d != 21 && d!= 12) return false;
			if(d == 21 && cg.board[x+(nx-x)/2][y] != '.') return false;
			if(d == 12 && cg.board[x][y+(ny-y)/2] != '.') return false;
			return true;
		case 'h':
			if(d != 21 && d!= 12) return false;
			if(d == 21 && cg.board[x+(nx-x)/2][y] != '.') return false;
			if(d == 12 && cg.board[x][y+(ny-y)/2] != '.') return false;
			return true;
		case 'E':
			if(d != 22 || nx > 4) return false;
			if(cg.board[x+(nx-x)/2][y + (ny-y)/2] != '.') return false;
			return true;
		case 'e':
			if(d != 22 || nx < 5) return false;
			if(cg.board[x+(nx-x)/2][y + (ny-y)/2] != '.') return false;
			return true;
		case 'C':
			if (nx != x && ny != y || this.numOfPieceInPath(cg, p, nx, ny)!=0) return false;
			return true;
		case 'c':
			if (nx != x && ny != y || this.numOfPieceInPath(cg, p, nx, ny)!=0) return false;
			return true;
		case 'N':
			if (nx != x && ny != y) return false;
			if (p == null && this.numOfPieceInPath(cg, p, nx, ny)!=0) return false;
			if (p != null && this.numOfPieceInPath(cg, p, nx, ny)!=1) return false;
			return true;
		case 'n':
			if (nx != x && ny != y) return false;
			if (p == null && this.numOfPieceInPath(cg, p, nx, ny)!=0) return false;
			if (p != null && this.numOfPieceInPath(cg, p, nx, ny)!=1) return false;
			return true;
		case 'S':
			if(d != 10 && d != 1) return false;
			if(nx < x) return false;
			if(d == 1 && x < 5) return false;
			return true;
		case 's':
			if(d != 10 && d != 1) return false;
			if(nx > x) return false;
			if(d == 1 && x > 4) return false;
			return true;
		}
		return false;
		
	}
	
	// return the number of pieces along the path
	private int numOfPieceInPath(ChessGame cg, Piece p, int nx, int ny) {
		if(this.x == nx) {
			int count = 0;
			for (int i = Math.min(y, ny) + 1; i < Math.max(y, ny); i++) {
				if(cg.board[x][i] != '.') count ++;
			}
			return count;
		}
		else if(this.y == ny) {
			int count = 0;
			for (int i = Math.min(x, nx) + 1; i < Math.max(x, nx); i++) {
				if(cg.board[i][y] != '.') count ++;
			}
			return count;
		}
		return -1;
	}
}
