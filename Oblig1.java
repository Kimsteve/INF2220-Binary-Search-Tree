	
import java.util.*;
import java.io.*;
import java.util.Arrays;
import java.io.PrintWriter;
class Oblig1 {

	public static void main(String [] args  ) {
		String path = args[0]; // input file
		BinarySearchTree theTree = new BinarySearchTree();
					
		
		//Read input file and call insert method.

		try {
			Scanner br = new Scanner (new FileReader(path));

			while (br.hasNext()){
	
				theTree.insert(br.nextLine());
			
		 	} 
		
		} catch (IOException e) {
		
		}
		
		//theTree.inorderTraverse(theTree.rootnode);	

		UserInterface inter = new UserInterface(theTree);
		inter.promptLoop();
		inter.printTreeStatistics();

		System.out.println("alphabetically first word " + theTree.findfirst());
		System.out.println("alphabetically last word " + theTree.findLast());
	}
}



class BinarySearchTree {

	private Node rootnode;

	private class Node {

		Node leftChild;
		Node rightChild;
		String nodedata;

		Node(String nodedata ) { 
				 this( nodedata, null, null ); 
		}

		Node( String e, Node l, Node r ){
			   nodedata = e; leftChild = l; rightChild = r; 
		}
	}

	BinarySearchTree(){
		this.rootnode = null;
	}

	public void insert( String e ) { 
	  rootnode = insert( e, rootnode ); 
	}
	  
	public void remove(String e) {
		rootnode = remove(e, rootnode);
	}

	// 
	public boolean contains( String e ) { 
	return contains( e, rootnode ); 
	}  

	//least value
	public String findfirst(){
			return findfirst(rootnode).nodedata;
	}

	public String findLast(){
		return findLast(rootnode).nodedata;
	}

	public void inorderTraverse(Node focusNode) {

		if (focusNode != null) {
			// left node?
			inorderTraverse(focusNode.leftChild);
				// the current node
			System.out.println(focusNode.nodedata);
				// the right node
			inorderTraverse(focusNode.rightChild);

			}
	}

	private boolean contains(String check, Node rnode) {
			//check if empty
			if (rnode == null) {
				return false;
			}

			int compare = check.compareTo(rnode.nodedata);

			if (compare < 0 ) {
				//check if the node is on the left side.
				return contains(check, rnode.leftChild);
			} else if (compare > 0 ) {
				// else check if it contained on the right child
				return contains(check, rnode.rightChild);
			} else {
				return true;
			}
	}

	private Node insert(String e, Node rnode) {

		// create and initialize If no root this becomes root

		if (rnode == null) {
			//root = newNode;
			return new Node(e);
		} else {

		int compare = e.compareTo(rnode.nodedata);
		// if less -> insert on left side.
		if (compare < 0 ) {
			rnode.leftChild = insert(e, rnode.leftChild);
		} else if (compare > 0 ) {
			// else insert on right side.
			rnode.rightChild = insert(e, rnode.rightChild);
		}
		return rnode;

		
		}

	}
	// always finds the least value.
	private Node findfirst(Node n) {
			if (n != null) {
				while (n.leftChild != null) {
					n = n.leftChild;
				}
			}
			return n;
	}
	// right child
	private Node findLast(Node n) {
			if (n != null) {
				while (n.rightChild != null) {
					n = n.rightChild;
				}
			}
			return n;
	}

	private Node remove(String check, Node rootnode) {
			if (rootnode == null) {
				return rootnode;
			}

			int compare = check.compareTo(rootnode.nodedata);

			if (compare < 0 ) {
				// node is on the leftchild
				rootnode.leftChild = remove(check, rootnode.leftChild);
			} else if (compare > 0 ) {
				// on the rightchild
				rootnode.rightChild = remove(check, rootnode.rightChild);
				
				// else the the node has Two children 
			} else if (rootnode.leftChild != null && rootnode.rightChild != null) {
				
				rootnode.nodedata = findfirst(rootnode.rightChild).nodedata;
				rootnode.rightChild = remove(rootnode.nodedata, rootnode.rightChild);
			} else {
				//rootnode = (rootnode.leftChild != null) ? rootnode.leftChild : rootnode.rightChild;
				if(rootnode.leftChild != null){
					rootnode = rootnode.leftChild;

				} else{
					rootnode = rootnode.rightChild;
				}
			}
			return rootnode;
	}

	private int treeDepth(Node nroot) {
		if (nroot == null) {
			return -1;
		} else {
			return 1 + Math.max(treeDepth(nroot.leftChild ), treeDepth(nroot.rightChild ));
		}
	}

	private void nodesPerTreeDepth( Node nroot, int deps, int [] treeDep){
		if (nroot != null) {
			nodesPerTreeDepth(nroot.leftChild, deps+1, treeDep);
			treeDep[deps]++;
			nodesPerTreeDepth(nroot.rightChild,  deps+1, treeDep);
		}
	}

	public void statistics(){
		int d = treeDepth(rootnode);
		int sumnode = 0;
		int sum = 0;
		int average  = 0;
		
		int [] treeDep = new int [d+1];
		nodesPerTreeDepth(rootnode, 0, treeDep);
		System.out.println("Nodes per depth of the tree");
		for (int k =0; k< treeDep.length; k++){
			sumnode += treeDep[k]*k;
			sum+= treeDep[k];
			average =  sumnode/sum;
			System.out.println("node  " +k  +"  is  " + treeDep[k]);
		}
		System.out.println("The average depth of all nodes  " + average);
		System.out.println("The height of the Binary Search Tree is  = " + d);


	}

}


class UserInterface {

BinarySearchTree dictionary;
ArrayList<String> possiblewords;  // arraylist to store the suggested/possible words.
int positveResultLookups; //look ups that gave +ve answer



	UserInterface(BinarySearchTree spelling){
		dictionary=spelling;
		positveResultLookups = 0; 
		

	}
	/*
	while loop promt. 
	if the input is correct -> "has been spelled correctly" is printed.
	else words with similar spellings are pritned out.
	*/
	public void promptLoop() {
		Scanner userInput = new Scanner(System.in);
		while(true) {
			System.out.print("Enter a word> ");
			String input = userInput.nextLine().toLowerCase();
			if (input.equals("") ) {
				System.out.println("an input is required, Exit loop with 'q'");
			} else if (input.equals("q")) {
				return;
			} else if (dictionary.contains(input) ) {
				System.out.println(input+" has been spelled correctly");
			} else {
				
				
				long starttime = System.currentTimeMillis(); // timing the process

				possiblewords = new ArrayList<String>();
				System.out.println(input+" has been misspelled");
				// calling the four methods.  
				similarOne(input);
				replacedLetter(input);
				removedletter(input);
				addedLetter(input);
				
				long stoptime   = System.currentTimeMillis(); // stop timing
				long diff =  stoptime - starttime;
				System.out.println("Time taken to complete the task   : " + diff+  "  millisecond");

				// check if the some possible words are generated 
				if (possiblewords.isEmpty()) {
					System.out.println("No words with similar spellings found");
				} else{
					System.out.println("generated similar words are: " + possiblewords);
					System.out.println("lookups with positive results :  " + positveResultLookups);

				}
							
			}
			positveResultLookups = 0;
		}

	}

	//Rule 1
	//identical input, except two letters next to each other switched.
	// letters on the input string are swapped and cross check with the words in the dictionary.
	private void similarOne(String word) {
		char[] word_array = word.toCharArray();
		char[] tmp;
		String[] words = new String[word_array.length-1];

		for (int i=0; i < word_array.length - 1; i++ ) {
			tmp = word_array.clone();
		
				words[i] = swap(i, i+1, tmp);
				similarSpelling(words[i]);
		}
	}

	private String swap(int a, int b, char[] word) {
		//swap the char
		char tmp = word[a];
		word[a] = word[b];
		word[b] = tmp;

		return new String(word);
	}

	private void similarSpelling(String word) {
		//check if the generated word is in the dictionary and add to the possible words tree.
		if (dictionary.contains(word) ) {
			positveResultLookups++;
			possiblewords.add(word);
		} else {
			positveResultLookups++;
		}
	}
	//Rule 2
	//Identical input, except one letter replaced with another

	private void replacedLetter(String input){
		char[] word_array = input.toCharArray(); // to char array.

		for(int i=0; i < word_array.length ; i++) {
			alphabetcheck(i, word_array);
		}
	}


	private void alphabetcheck(int i, char[] word_array) {
		char[] tmpArray;
		for (char c='a'; c <= 'z'; c++) {
			tmpArray = word_array.clone();
			tmpArray[i] = c;
			// generate new words with different char and cross check if they exist in the dictionary with call of method similarspelling.
			String stringalphabet = new String(tmpArray);
			similarSpelling(stringalphabet);
			
		}
	}


	//Rule 3
	//Identical input, except one letter removed
	// the method rearranges the input string and the different aphafabets are  added and the generated words are checked 
	//if they exist in the dictionrary.
	private void removedletter(String input){
		char[] word_array = input.toCharArray(); //to char
		int len = word_array.length+1;
		char temp [][] = new char [len][len];

		for(int i=0; i <= word_array.length; i++) {
			for (int j=0; j <= word_array.length ; j++) {
				if (i > j) {
					temp[i][j] = word_array[j];
				} else if (i < j) {
					temp[i][j] = word_array[j-1];
				}
			}
		alphabetcheck(i, temp[i]);
		}


	}
	//Rule 4
	//Identical input, one letter has been added in front, middle or at the end.
	// In this case a letter is removed at a time and the new array is sent to the similarspelling
	//to check if the new word generated is in the dictionary.

	private void addedLetter(String input){
		char[] word_array = input.toCharArray();
		char tempArray [];

		for(int i=0; i < word_array.length; i++) {
			tempArray = new char[word_array.length - 1]; // to hold the new char with a less number of characters.
			for(int j=0; j < word_array.length; j++) {
				if (i > j) {
					tempArray[j] = word_array[j];
				} else if (i < j) {
					tempArray[j-1] = word_array[j];
				}
				similarSpelling(new String(tempArray) );
			}
		}

	}

	public void printTreeStatistics(){
		System.out.println("Binary Search Tree statistics");
		dictionary.statistics();
	}
}



 	
  