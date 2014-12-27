import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {
	//Priyanka Pathak, COMS 3134
	//Assignment Three: File Reader
	//This file serves as the back end class for a file reader
	
	Scanner sc;
	
	public FileReader(String fileName) throws FileNotFoundException{
		sc = new Scanner(new File(fileName));
	}
	
	public String getNextWord(){
		return sc.next();
	}
	
	public boolean hasNext(){
		if (sc.hasNext() == true){
			return true;
		}else{
			return false;
		}
	}
}
