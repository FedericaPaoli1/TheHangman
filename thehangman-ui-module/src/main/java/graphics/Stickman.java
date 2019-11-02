package graphics;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Stickman {
	
	private Stickman() {}
	
	private static final String[] BACKING = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|", 
            "|",      
            "|",        
            "|",        
            "|",          
            "|",           
            "|",          
            "|",         
            "|",         
            "|",
            "|"
    };
	
	
	private static final String[] HEAD = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|          * *", 
            "|         *   *", 
            "|          * *",
            "|",        
            "|",          
            "|",           
            "|",          
            "|",         
            "|",         
            "|",
            "|"
    };
	
	private static final String[] BODY = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|          * *", 
            "|         *   *", 
            "|          * *",
            "|           |",
            "|           |",
            "|           |",
            "|           |",
            "|",         
            "|",         
            "|",
            "|"
    };
	
	private static final String[] RIGHT_ARM = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|          * *", 
            "|         *   *", 
            "|          * *",
            "|         \\ |",
            "|          \\|",
            "|           |",
            "|           |",
            "|",         
            "|",         
            "|",
            "|"
    };
	
	private static final String[] ARMS = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|          * *", 
            "|         *   *", 
            "|          * *",
            "|         \\ | /",
            "|          \\|/",
            "|           |",
            "|           |",
            "|",         
            "|",         
            "|",
            "|"
    };
	
	private static final String[] RIGHT_FOOT = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
			"|          * *", 
            "|         *   *", 
            "|          * *",
            "|         \\ | /",
            "|          \\|/",
            "|           |",
            "|           |",
            "|          /",
            "|         /",
            "|",
            "|"
    };
	
	private static final String[] COMPLETE_STICKMAN = {
			"_____________", 
			"|           |", 
			"|           |", 
			"|           |",
            "|          * *", 
            "|         *   *", 
            "|          * *",
            "|         \\ | /",
            "|          \\|/",
            "|           |",
            "|           |",
            "|          / \\",
            "|         /   \\",
            "|",
            "|"
    };
	
	private static final List<String[]> FIGURES = new LinkedList<>(Arrays.asList(BACKING, HEAD, BODY, RIGHT_ARM, ARMS, RIGHT_FOOT, COMPLETE_STICKMAN));
	
	public static final List<String[]> IMMUTABLE_FIGURES = Collections.unmodifiableList(FIGURES);

}
