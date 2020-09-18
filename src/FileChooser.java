import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// a class that choose and read files
public class FileChooser {
	JFileChooser f = new JFileChooser();
	public FileChooser() {
		this.f.setFileSelectionMode(JFileChooser.CUSTOM_DIALOG);
	}
	
    public Object chooseFile() throws FileNotFoundException{
        this.f.showSaveDialog(null);
        return f.getSelectedFile();
    }
    
    //choose a file and read it
    public ArrayList<String> readFile() throws FileNotFoundException {
    	File file = (File) this.chooseFile();
    	Scanner s = new Scanner(file);
    	ArrayList<String> re = new ArrayList<String>();
    	String fName = file.getName();
    	re.add(fName.substring(fName.indexOf('.')+1));// filter out the file type
    	String nextLine;
    	while(s.hasNextLine()) {
    		nextLine = s.nextLine();// get in all the contents in list
    		if(nextLine.length() < 1) continue;
    		if(nextLine.contains("#")) re.add(nextLine.substring(0, nextLine.indexOf("#")));
    		else re.add(nextLine);
    	}
    	s.close();// close scanner
		return re;
    }
    
    // write data into file
    public void writeFile(File w, ChessGame cg) {
        try {
            w.createNewFile(); 
            try (FileWriter writer = new FileWriter(w);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(cg.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

