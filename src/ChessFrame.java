import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

// a class that provide a GUI of the game
public class ChessFrame extends JFrame implements ActionListener,MouseListener,Runnable{
	int IR = 55;
	JLabel pieceLabels[] = new JLabel[32]; // pieces
	JLabel image;// image of the board
	Container con; 
	JToolBar mainBar;
	JButton newGame;
	JButton undo;
	JButton exit;
	JButton load;
	JButton save;
	JLabel text;
	Vector Var;
	ChessGame cg;
	Thread tmain;
	static int Man,i;
	int bg, rg;
	/**
	* pieceClicked = true blink
	* pieceClicked = false stop blink
	*/
	boolean pieceClicked;
	
	/**
	* playTurn=1 BLACK
	* playTurn=2 RED
	* playTurn=3 
	*/
	int playTurn=2;//red
	
	ChessFrame(String Title, ChessGame cg){
		this.cg = cg;
		if(!cg.isRedTurn) playTurn = 1;
		// change font
		Font font = new Font("Times", Font.PLAIN, 12);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, font);
			}
		}
		
		con = this.getContentPane();
		con.setLayout(null);
		/*
		rule = new ChessRule();
		*/
		Var = new Vector();
		
		//Create a new tool bar
		mainBar = new JToolBar();
		text = new JLabel("Chinese Chess");
		text.setToolTipText("info");
		newGame = new JButton("New");
		newGame.setToolTipText("restart a game");
		exit = new JButton("Exit");
		exit.setToolTipText("exit the chess program");
		undo = new JButton("Undo");
		undo.setToolTipText("reset to the last step");
		load = new JButton("Load");
		load.setToolTipText("load a game");
		save = new JButton("Save");
		save.setToolTipText("save a game");

		//adding items to the bar
		mainBar.setLayout(new GridLayout(0,4));
		mainBar.add(newGame);
		mainBar.add(undo);
		mainBar.add(exit);
		mainBar.add(text);
		mainBar.add(load);
		mainBar.add(save);
		mainBar.setBounds(0,0,510,40);
		con.add(mainBar);
		
		
		// adding piece labels
		drawPieces(cg.pieces);
		// adding listener
		newGame.addActionListener(this);
		undo.addActionListener(this);
		exit.addActionListener(this);		
		save.addActionListener(this);
		load.addActionListener(this);
				
		// adding piece listener
		for (int i=0;i<32;i++){
			con.add(pieceLabels[i]);
			pieceLabels[i].addMouseListener(this);
		}
		
		// adding board label
		con.add(image = new JLabel(new ImageIcon("image\\Main2.gif")));
		image.setBounds(-25,15,550,605);
		image.addMouseListener(this);
		
		// adding exit listener
		this.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we){
					System.exit(0);
				}
			}
		);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		
		if (frameSize.height > screenSize.height){
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width){
			frameSize.width = screenSize.width;
		}
		
		this.setLocation((screenSize.width - frameSize.width) / 2 - 280 ,(screenSize.height - frameSize.height ) / 2 - 350);
	
		// initialization
		this.setIconImage(new ImageIcon("image\\red_general").getImage());
		this.setResizable(false);
		this.setTitle(Title);
		this.setSize(510, 640);
		this.show();
	}
	
	// draw each of the piece
	public void drawPieces(Piece[] pieces){
		int i;
		Piece p;
		for (i=0; i < 32; i++){		
			p = pieces[i];
			pieceLabels[i] = new JLabel(getImage(p.name));
			pieceLabels[i].setBounds(IR*p.y, IR*p.x + 40,IR,IR);
			pieceLabels[i].setName(p.name + Integer.toString(i));
		}	
		
	}
	
	//returns the image Icon according to the piece 
	public Icon getImage(char name) {
		String path = "image\\";
		if(Character.isUpperCase(name)) path += "black_";
		else path += "red_";
		
		switch(name){
		case 'G':
			path += "general";
			break;
		case 'g':
			path += "general";
			break;
		case 'A':
			path += "advisor";
			break;
		case 'a':
			path += "advisor";
			break;
		case 'H':
			path += "horse";
			break;
		case 'h':
			path += "horse";
			break;
		case 'E':
			path += "elephant";
			break;
		case 'e':
			path += "elephant";
			break;
		case 'C':
			path += "chariot";
			break;
		case 'c':
			path += "chariot";
			break;
		case 'N':
			path += "cannon";
			break;
		case 'n':
			path += "cannon";
			break;
		case 'S':
			path += "soldier";
			break;
		case 's':
			path += "soldier";
			break;
		default:
			break;
		}
		path += ".png";
		return new ImageIcon(path);
	}
	
	// blink a piece
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while (true){
			//blink
			if (pieceClicked){
				pieceLabels[Man].setVisible(false);

				//time control
				try{
					tmain.sleep(200);
				}
				catch(Exception e){
				}
				
				pieceLabels[Man].setVisible(true);
			}
			
			//blink
			else {
				text.setVisible(false);
				
				//time control
				try{
					tmain.sleep(250);
				}
				catch(Exception e){
				}
				
				text.setVisible(true);
			}
			
			try{
				tmain.sleep(350);
			}
			catch (Exception e){
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		//initialize coordinates
		int Cx=0,Cy=0,Ex=0,Ey=0,Nbx=0,Nby=0;
		
		//start the thread
		if (tmain == null){
			tmain = new Thread(this);
			tmain.start();
		}
		
		//move a piece
		if (e.getSource().equals(image)){
			if (playTurn == 2 && !Character.isUpperCase(pieceLabels[Man].getName().charAt(0))){//red
				Cx = pieceLabels[Man].getX();
				Cy = pieceLabels[Man].getY();
				Ex = e.getX();
				Ey = e.getY();
				Nbx = getBXY(Ex)-1;
				Nby = getBXY(Ey-40);
				if(cg.pieces[Man].isValidMove(Nbx, Nby, null, cg)){
					pieceLabels[Man].setBounds(Nbx*IR, Nby*IR+40, IR, IR);
					cg.last = new ChessGame(deepcopy(cg.board), true, deepcopy(cg.pieces));//set last
					cg.board[Nby][Nbx] = cg.pieces[Man].name;
					cg.board[cg.pieces[Man].x][cg.pieces[Man].y] = '.';
					cg.pieces[Man].x = Nby;
					cg.pieces[Man].y = Nbx;
					
					
					text.setText("BLACK Turn");
					playTurn=1;
					cg.isRedTurn = !cg.isRedTurn;
					System.out.println(cg);
				}
				else{
					showWarningWindow();
					text.setText("RED Turn");
					playTurn=2;
				}
			}
			else if(playTurn == 1 && Character.isUpperCase(pieceLabels[Man].getName().charAt(0))) {// black
				Cx = pieceLabels[Man].getX();
				Cy = pieceLabels[Man].getY();
				Ex = e.getX();
				Ey = e.getY();
				Nbx = getBXY(Ex)-1;
				Nby = getBXY(Ey-40);
				if(cg.pieces[Man].isValidMove(Nbx, Nby, null, cg)){
					pieceLabels[Man].setBounds(Nbx*IR, Nby*IR+40, IR, IR);
					cg.last = new ChessGame(deepcopy(cg.board), false, deepcopy(cg.pieces));// set last
					cg.board[Nby][Nbx] = cg.pieces[Man].name;
					cg.board[cg.pieces[Man].x][cg.pieces[Man].y] = '.';
					cg.pieces[Man].x = Nby;
					cg.pieces[Man].y = Nbx;
					
					text.setText("RED Turn");
					playTurn=2;
					cg.isRedTurn = !cg.isRedTurn;
					System.out.println(cg);
				}
				else{
					showWarningWindow();
					text.setText("BLACK Turn");
					playTurn=1;
				}
			}
			pieceClicked=false;
			
		}
		
		//clicking a piece
		else{
			//firstTime clicked
			if (!pieceClicked){
				for (int i=0;i<32;i++){
					if (e.getSource().equals(pieceLabels[i])){
						Man=i;
						pieceClicked=true;
						break;
					}
				}
			}
			
			//Second time clicked
			else if (pieceClicked){
				//stop blinking
				pieceClicked=false;
				
				for (i=0;i<32;i++){
					//find the eaten piece
					if (e.getSource().equals(pieceLabels[i])){
						if (playTurn == 2 && !Character.isUpperCase(pieceLabels[Man].getName().charAt(0)) && 
								Character.isUpperCase(pieceLabels[i].getName().charAt(0))){// red
							if(cg.pieces[Man].isValidMove(cg.pieces[i].y,cg.pieces[i].x, cg.pieces[i], cg)){
								pieceLabels[i].setVisible(false);
								pieceLabels[Man].setBounds(pieceLabels[i].getX(), pieceLabels[i].getY(), IR, IR);
								pieceLabels[i].setBounds(99999, 99999, IR, IR);
								
								if (cg.pieces[i].name == 'G'){
									JOptionPane.showConfirmDialog(
										this,"RED Wins","RED Wins",
										JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
									playTurn=3;
									text.setText("RED Wins");
									break;
								}
								cg.last = new ChessGame(deepcopy(cg.board), true, deepcopy(cg.pieces));// set last
								cg.board[cg.pieces[i].x][cg.pieces[i].y] = cg.pieces[Man].name;
								cg.board[cg.pieces[Man].x][cg.pieces[Man].y] = '.';
								cg.pieces[Man].x = cg.pieces[i].x;
								cg.pieces[Man].y = cg.pieces[i].y;
								cg.pieces[i].x = 99999;
								cg.pieces[i].y = 99999;
								
								text.setText("BLACK Turn");
								playTurn=1;
								cg.isRedTurn = !cg.isRedTurn;
								System.out.println(cg);
								break;
							}
							else{
								showWarningWindow();
								text.setText("RED Turn");
								playTurn=2;
								break;
							}
						}
						else if(playTurn == 1 && Character.isUpperCase(pieceLabels[Man].getName().charAt(0)) &&
								!Character.isUpperCase(pieceLabels[i].getName().charAt(0))){// black
							if(cg.pieces[Man].isValidMove(cg.pieces[i].y,cg.pieces[i].x, cg.pieces[i], cg)){
								pieceLabels[i].setVisible(false);
								pieceLabels[Man].setBounds(pieceLabels[i].getX(), pieceLabels[i].getY(), IR, IR);
								pieceLabels[i].setBounds(99999, 99999, IR, IR);
								
								if (cg.pieces[i].name == 'g'){
									JOptionPane.showConfirmDialog(
										this,"BLACK Wins","BLACK Wins",
										JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
									playTurn=3;
									text.setText("BLACK Wins");
									break;
								}
								cg.last = new ChessGame(deepcopy(cg.board), false, deepcopy(cg.pieces));// set last
								cg.board[cg.pieces[i].x][cg.pieces[i].y] = cg.pieces[Man].name;
								cg.board[cg.pieces[Man].x][cg.pieces[Man].y] = '.';
								System.out.println(cg);
								System.out.println(cg.last);
								cg.pieces[Man].x = cg.pieces[i].x;
								cg.pieces[Man].y = cg.pieces[i].y;
								cg.pieces[i].x = 99999;
								cg.pieces[i].y = 99999;
								
								text.setText("RED Turn");
								playTurn=2;
								cg.isRedTurn = !cg.isRedTurn;
								System.out.println(cg);
								break;
							}
							else{
								showWarningWindow();
								text.setText("BLACK Turn");
								playTurn=1;
								break;
							}
						}
					}
				}
				
			}
		
		}
			
	}
		
		
	// copy a piece array
	private Piece[] deepcopy(Piece[] pieces) {
		Piece[] re = new Piece[32];
		for (int i = 0; i < re.length; i++) {
			re[i] = new Piece(pieces[i].x, pieces[i].y, pieces[i].name, pieces[i].red);
		}
		return re;
	}
	
	// copy a 2d char array
	private char[][] deepcopy(char[][] board) {
		char[][] re = new char[10][9];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				re[i][j] = board[i][j];
			}
		}
		return re;
	}
	
	// show invalid move warning window
	private void showWarningWindow() {
		Object[] options = { "OK", "CANCEL" };
		JOptionPane.showOptionDialog(null, "Invalid Move!", "Warning",
		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		null, options, options[0]);
		
	}
	
	// get board xy according to mouse xy
	private int getBXY(int n) {
		int re;
		double dn = n + 0.0;
		dn /= IR;
		if(dn - (int)dn >= 0.5) re = (int)dn+1;
		else re = (int)dn;

		return re;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//restart
		if (e.getSource().equals(newGame)){
			// rearrange pieces
			int i;
			Piece p;
			this.cg = new ChessGame();
			Piece[] pieces = cg.getPieces();
			for (i=0; i < 32; i++){		
				p = pieces[i];
				pieceLabels[i].setBounds(IR*p.y, IR*p.x + 40,IR,IR);
			}	
			playTurn = 2;
			text.setText("RED Turn");
			
			for (i=0;i<32;i++){
				pieceLabels[i].setVisible(true);
			}
			// clear vector
			Var.clear();
			
		}	
		
		//Undo button
		else if (e.getSource().equals(undo)){
			if(cg.last != null) {
				new ChessFrame("Chess", cg.last);
				this.dispose();
				
			}
		}
		
		// save
		else if (e.getSource().equals(save)){
			int j=JOptionPane.showConfirmDialog(
					this,"Save?","save?",
					JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (j == JOptionPane.YES_OPTION){
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showSaveDialog(null);
				if(option==JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					new FileChooser().writeFile(file, cg);
				}
			}
			
		}
		
		// load
		else if (e.getSource().equals(load)){
			int j=JOptionPane.showConfirmDialog(
					this,"Load?","load?",
					JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (j == JOptionPane.YES_OPTION){
				try {
					cg = ChessGame.toChessGame(new FileChooser().readFile());
					System.out.println(cg);
					cg.pieces = cg.getPieces();
					new ChessFrame("Chess", cg);
					this.dispose();
				} 
				catch (FileNotFoundException e1) {
					System.out.println(e1);
				}
					
				
			}
		}
			
	
		//EXIT
		else if (e.getSource().equals(exit)){
			int j=JOptionPane.showConfirmDialog(
				this,"Exiting?","Exit",
				JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if (j == JOptionPane.YES_OPTION){
				System.exit(0);
			}
		}
	}
	
}
