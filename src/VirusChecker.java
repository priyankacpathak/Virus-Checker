import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


public class VirusChecker {

	//Priyanka Pathak, COMS 3134
	//Assignment Three: Virus Checker
	//This program checks hash tables to probabilistically check to see if a given file is a virus or benign file.
	
	static String choice;
	static BufferedReader reader;
	static HashMap malicious = new HashMap();
	static HashMap benign = new HashMap();
	static int fileCount;
	static String fileLoc;
	static boolean hasInvoked = false;
	
	public static void main(String[] args){
		//Opens program with menu of options for user. Loads previous file directory, if it exists, on first run of main method.
		
		reader = new BufferedReader(new InputStreamReader(System.in)); 
		VirusChecker vc = new VirusChecker();

		if(hasInvoked = false){
			if(fileLoc != null){
				if(vc.fileExists(fileLoc + "VirusCheckerFiles.ser")){
					vc.loadFiles();
					}
				}
			else if (vc.fileExists("VirusCheckerFiles.ser")){
				vc.loadFiles();
				}
			}
		
		System.out.println("What would you like to do? \n1. Designate the dictionary directory location. \n2. Clear all probability information" +
				"\n3. Add information to benign database. \n4. Add information to virus database. \n5. Calculate probability of virus in unknown " +
				"file. \n6. Get information about this system. \n7. Quit.");

		
		try{
			choice = reader.readLine(); 
			
			if (choice.equals("1")){
				//Designates directory to store hash files in. If not specified, will store in default location.
				
				System.out.println("What is the relative directory location?");
				fileLoc = reader.readLine();
				System.out.println("Directory location successfully stored. \n");
				hasInvoked = true;
				main(args);
			}else if (choice.equals("2")){
				//Clears malicious and benign hash tables.
				
				malicious.clear();
				benign.clear();
				System.out.println("Hash tables have been cleared. \n");
				hasInvoked = true;
				main(args);
			}else if (choice.equals("3")){
				//Adds file info to benign hash table.
				
				System.out.println("What file(s) would you like to add to the benign database?");
				choice = reader.readLine();
				vc.populateSet(benign, choice);
				System.out.println("Information successfully added.");
				hasInvoked = true;
				main(args);
			}else if (choice.equals("4")){
				//Adds file info to malicious hash table.
				
				System.out.println("What file(s) would you like to add to the virus database?");
				choice = reader.readLine();
				vc.populateSet(malicious, choice);
				System.out.println("Information successfully added.");
				hasInvoked = true;
				main(args);
			}else if (choice.equals("5")){
				//Calculate probability that file is a virus/benign and report results.
				
				System.out.println("What file would you like to check?");
				String fn = reader.readLine();
				double probM = vc.roundTwoDecimals(vc.calculateProbabilityMal(fn, malicious, benign));
				double probB = vc.roundTwoDecimals(vc.calculateProbabilityBen(fn, benign, malicious));
				System.out.println("There is a " + probM + "% chance that this file is a virus, and a " + probB + "% chance that this file is benign.");
				if((probM >= probB)){
					System.out.println("This file is probably a virus.");
				}else{
					System.out.println("This file is probably benign.");
				}
				hasInvoked = true;
				main(args);
			}else if (choice.equals("6")){
				//Give information about program and number of files added during current session.
				
				System.out.println("This top-rated, critically acclaimed virus checking software was developed by " +
						"Priyanka Pathak at Columbia University. Her award-winning, A+ grade-earning \nsoftware has been sought after by " +
						"major corporations such as Microsoft and Apple.\n\n" + fileCount + " files have been added to the system this session. ");
				hasInvoked = true;
				main(args);
			}else if (choice.equals("7")){
				//Allows user to save hash tables to directory.
				
				System.out.println("Would you like to save the virus and benign files to your current directory, y or n?");
				choice = reader.readLine();
				if(choice.equals("y")){
					vc.saveFile(malicious, benign);
					System.out.println("Files saved!");
				}else if(choice.equals("n")){
					//Do nothing.
				}else{
					System.out.println("Command unrecognized; file not saved.");
				}
				System.out.println("Goodbye!");
			}else{
				System.out.println("Please choose one of the options, 1-7.");
				hasInvoked = true;
				main(args);
			}
			
			}catch (IOException e){
				e.printStackTrace();
			}
	}
	
	public void saveFile(HashMap malicious, HashMap benign){
        //This method takes the malicious and benign hash tables and stores them in a Serializable file for future use.
		//The location can be altered by 'fileLoc', which is changed by the user if they choose option 1 in the main menu. If
		//they do not change the location, it will automatically be stored in the default location.
		
		try {

			if(fileLoc != null){
	            FileOutputStream fileOut = new FileOutputStream(fileLoc + "/" + "VirusCheckerFiles.ser");
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);

	            out.writeObject(malicious);
	            out.writeObject(benign);

	            out.close();
	            fileOut.close();	
			}else{
	            FileOutputStream fileOut = new FileOutputStream("VirusCheckerFiles.ser");
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);

	            out.writeObject(malicious);
	            out.writeObject(benign);

	            out.close();
	            fileOut.close();
			}
           
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void loadFiles(){
        //This method loads the malicious and benign hash tables and from a Serializable file. This location
		//can be altered by 'fileLoc', which is changed by the user if they choose option 1 in the main menu. If
		//they do not change the location, it will automatically be pulled from the default location.
		
        try {
           
			if(fileLoc != null){
	            FileOutputStream fileOut = new FileOutputStream(fileLoc + "VirusCheckerFiles.ser");
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);

	            out.writeObject(malicious);
	            out.writeObject(benign);

	            out.close();
	            fileOut.close();	
			}else{
	            FileOutputStream fileOut = new FileOutputStream("VirusCheckerFiles.ser");
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);

	            out.writeObject(malicious);
	            out.writeObject(benign);

	            out.close();
	            fileOut.close();
			}
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void populateSet(HashMap hashMap, String fileName){
		//This method populates the hash tables.
		
		FileReader fr;
		try {
			
			fr = new FileReader(fileName);
			fileCount++;
			String window;
			int count;
			
			while(fr.hasNext()){
				window = fr.getNextWord();
				count = 1;
				if(hashMap.containsKey(window) == true){
					int temp = (Integer) hashMap.get(window);
					count = temp++;
					hashMap.put(window, count);
				}else{
					hashMap.put(window, count);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public double calculateProbabilityMal(String fileName, HashMap malicious, HashMap benign){
		//This method calculates the probability that a given file is a malicious one.
		
		FileReader fr;
		try {
			fr = new FileReader(fileName);
			String window;
			double totalProbM = 1;
			int valueM = 1;
			int total = 1;
			double probM;
			
			while(fr.hasNext()){
				window = fr.getNextWord();
				if(malicious.containsKey(window)){
					valueM = (Integer) malicious.get(window);
					
					if(benign.containsKey(window)){
						total = (valueM + ((Integer) benign.get(window)));
					}else{
						total = valueM;
					}
					
					probM = (valueM/total);

					totalProbM = (totalProbM * probM * 100);
					return totalProbM;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
			return 0;
	}
	
	public double calculateProbabilityBen(String fileName, HashMap benign, HashMap malicious){
		//This method calculates the probability that a given file is a benign one.
		
		FileReader fr;
		try {
			fr = new FileReader(fileName);
			String window;
			double totalProbB = 1;
			int valueB = 1;
			int total = 1;
			double probB;
			
			while(fr.hasNext()){
				window = fr.getNextWord();
				if(benign.containsKey(window)){
					valueB = (Integer) benign.get(window);
					
					if(malicious.containsKey(window)){
						total = (valueB + (Integer) malicious.get(window));
					}else{
						total = valueB;
					}

					probB = (valueB/total);
					totalProbB = (totalProbB * probB * 100);
					
					return totalProbB;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return 0;

	}
	
	
	double roundTwoDecimals(double d) {
		//This method rounds a double to two decimals.
		
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
	
	public boolean fileExists(String fileName){
		//Checks to see if a file directory exists
		File file = new File(fileName);
		
		if (file.exists()){
			return true;
		}else{
			return false;
		}
	}
}
