import java.util.Scanner; //Import Scanner class 
import com.pi4j.util.Console; // Console import class
import swiftbot.SwiftBotAPI;
import java.io.IOException;//IOException import class
import java.lang.Math; //Import Math class 
import java.util.Random; //Generates random numbers
import java.lang.Thread; // Import Sleep class 

//Main class of task 4
public class DetectObject {
	//Declare global variables
	static String runMode = "";
	static double executionTime = 0.0;
	static double startTime;
	static int detectingObj = 0;
	
	private static int Mode1 = 0;
	//Declare a static variable of type "SwiftbotAPI" and initialise it
	static SwiftBotAPI swiftBot = new SwiftBotAPI();
    //Main method for task 4
    public static void main(String[] args) throws InterruptedException, IllegalArgumentException, IOException {
            
    	    // Declare variables
            int mode = 0;  
        	int Curious = 1;
			int Scaredy = 2;
			int Any = 3; 

        // Record execution time 
        startTime = System.currentTimeMillis();

        // Create menu for user interaction
        final Console console = new Console();
        System.out.println("Detect object with Swiftbot");
        
			// Loop main menu after each user input
			while (true) {
		        // Output mode options to the user 
			    System.out.println("Mode options:");
			    System.out.println("1. 'Curious Swiftbot' mode");
			    System.out.println("2. 'Scaredy Swiftbot' mode");
			    System.out.println("3. 'Any' mode");
			    System.out.println("Enter number:");
			  
			        // Scanner statement reads input from user
			        Scanner myobj = new Scanner(System.in); 
			        //Read input from user
			    	mode = myobj.nextInt();
			    	
			    	// Check if the user entered a valid mode
				if (mode != Curious && mode != Scaredy && mode != Any) {
			        System.out.println("Error! Incorrect numeric value entered for mode selection.");
			        System.out.println("Please enter either 1 for 'Curious Swiftbot' mode, 2 for 'Scaredy Swiftbot' mode or 3 for 'Any' mode.");
			        System.out.println("Enter number:");
			    }
				else {
					break; //Exit the loop
				}
			}            
		//Curious Swiftbot mode
	    if (mode == 1) {
    	 System.out.println("Executing 'Curious Swiftbot' mode");
    	 Curious();
    	 
             }
     //Scaredy Swiftbot mode
     else if (mode == 2) {
    	 System.out.println("Executing 'Scaredy Swiftbot' mode");
    	 Scaredy();
    	 

    	 
     }
     //Any mode
     else if (mode == 3) {
    	 int random = new Random().nextInt(2);
    	 if (random == 0) {
    		 System.out.println("Executing 'Curious Swiftbot' mode"); //Curious Swiftbot mode
    		 Curious();

    	 }
    	 else {
    		 System.out.println("Executing 'Scaredy Swiftbot' mode"); //Scaredy Swiftbot mode 
    		 Scaredy();

    	 }
     }
}
    public static void purpleUnderlights() throws IOException {
    	swiftBot.fillUnderlights(148,0,211);
    }
    
    //Method for Curious Swiftbot mode 
    public static void Curious() throws InterruptedException, IOException {
    	runMode = "The mode that was running: Curious";
   	 while (true) {
   	     // If Swiftbot encounters object within 15cm
         double distanceToObject = swiftBot.useUltrasound(); 
         if (distanceToObject < 15.0) { //If distance to object is less than 15cm
        	swiftBot.startMove(-50, -50);
        	blinkUnderLightsGreen(); //Underlights blink green
         	if (distanceToObject == 15.0) {
         		detectingObj++;
         		blinkUnderLightsGreen();
         		swiftBot.stopMove();
         		System.out.println("Object Encountered. Distance to object:" + distanceToObject); 
         		Thread.sleep(2000);  	     
         	}
         }
         else if (distanceToObject > 15.0) { //If distance to object is more than 15cm
        	 blinkUnderLightsblue();
        	 swiftBot.startMove(50, 50); //Swiftbot starts to move
        	 if (distanceToObject == 15.0) { //If distance to object is equal to 15cm
        		 detectingObj++;
        		blinkUnderLightsGreen();
          		swiftBot.stopMove(); //Swiftbot stops and waits 2 seconds 
          		Thread.sleep(2000);	
          	}
         }
         else if (distanceToObject == 15.0) { //Otherwise if distance to object is equal to 15cm
        	 detectingObj++; //Everytime swiftbot encounters object, add 1 
        	 swiftBot.stopMove();//Swiftbot waits for 2 seconds
        	 Thread.sleep(2000); 
         }
		 if (swiftBot.BUTTON_X.isLow()) { 
			 swiftBot.stopMove();
			 purpleUnderlights();
			 swiftBot.move(100, -100, 2000);
			 double endTime = System.currentTimeMillis();
			 executionTime = endTime - startTime;
			 Logs();
			 break; //Exits the loop
		 }
         }
    }
    
    //Method for Scaredy Swiftbot mode 
    public static void Scaredy() throws IOException, InterruptedException {
    	runMode = "The mode that was running: Scaredy";
   	 while (true) {
    	 
   	 double distanceToObject = swiftBot.useUltrasound(); //If distance to Object is greater than 1 metre
   	 if (distanceToObject > 100.0) {
   		 blinkUnderLightsblue(); //Underlights blink blue
   		 swiftBot.startMove(50, 50); //Swiftbot starts to move
   		 }
   	 if (distanceToObject <= 100.0) { //If distance to object is less than or equal to 1 metre
   		 detectingObj++;
   		 swiftBot.stopMove(); //Swiftbot stops moving 
   		 swiftBot.disableUnderlights(); //Swiftbot disables all underlights
   		 blinkUnderLightsblue();
   		 blinkUnderLightsRed();
   		 swiftBot.move(50, -50, 1000); //Swiftbot turns around for 1 second 
   		 swiftBot.startMove(50, 50); //Swiftbot moves in different direction
   	 }
	 if (swiftBot.BUTTON_X.isLow()) { //If button X is clicked, it terminates the program
		 swiftBot.stopMove();
		 purpleUnderlights(); //Purple underlights turn on
		 swiftBot.move(100, -100, 2000); //Swiftbot spins around for 2 seconds
		 double endTime = System.currentTimeMillis(); //Calculate time of execution
		 executionTime = endTime - startTime; //Stores in global variable
		 Logs();
		 break; //Exit the loop
	 }
   	 }
    }
    //Method for logs of execution
    public static void Logs() {
    	System.out.println(runMode); //Mode it ran
    	System.out.println("Execution time: "+ executionTime/1000); //Total execution time
    	System.out.println("Numbers Swiftbot detected object: "+ detectingObj); //Number of encounters between Swiftbot and object
    }
	                              
    //Method for Blue underlights    			                 
    private static void blinkUnderLightsblue() throws IOException, InterruptedException{
    	int[] blue = { 0, 0, 255 };
		swiftBot.fillUnderlights(blue);
		
    }
    //Method for Green underlights 
    private static void blinkUnderLightsGreen() throws InterruptedException, IOException {
    	swiftBot.fillUnderlights(0, 255, 0);
    	}
    
    //Method for red underlights
    private static void blinkUnderLightsRed()throws InterruptedException, IOException {
    	    swiftBot.fillUnderlights(255, 0, 0);
			
    	}
    }
      
