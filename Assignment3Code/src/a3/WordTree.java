package a3;

//COMP 250 - Introduction to Computer Science - Fall 2017
//Assignment #3 - Question 1

import java.util.*;

/*
 *  WordTree class.  Each node is associated with a prefix of some word 
 *  stored in the tree.   (Any string is a prefix of itself.)
 */

public class WordTree
{
	WordTreeNode root;

	// Empty tree has just a root node.  All the children are null.

	public WordTree() 
	{
		root = new WordTreeNode();
	}


	/*
	 * Insert word into the tree.  First, find the longest 
	 * prefix of word that is already in the tree (use getPrefixNode() below). 
	 * Then, add TreeNode(s) such that the word is inserted 
	 * according to the specification in PDF.
	 */
	public void insert(String word)
	{
		//  ADD YOUR CODE BELOW HERE
		WordTreeNode prefix = this.getPrefixNode(word);
		char c = 0;
		int i = 0;
		if (prefix.depth<word.length()) {
			i = prefix.depth;
			c = word.charAt(i);
			prefix.createChild(c);
			prefix = prefix.getChild(c);
			i++;
			while (i<word.length()) {
				c = word.charAt(i);
				prefix.createChild(c);
				prefix = prefix.getChild(c);
				i++;
			}
		} 
		if ((prefix.depth==word.length()) && (word.charAt(prefix.depth-1)==prefix.charInParent)) {
			prefix.endOfWord = true;
		} else if (i==word.length() && c==prefix.charInParent) {
			prefix.endOfWord = true;
		} else {
			prefix.endOfWord = false;
		}
		
		//  ADD YOUR ABOVE HERE
	}

	// insert each word in a given list 

	public void loadWords(ArrayList<String> words)
	{
		for (int i = 0; i < words.size(); i++)
		{
			insert(words.get(i));
		}
		return;
	}

	/*
	 * Given an input word, return the TreeNode corresponding the longest prefix that is found. 
	 * If no prefix is found, return the root. 
	 * In the example in the PDF, running getPrefixNode("any") should return the
	 * dashed node under "n", since "an" is the longest prefix of "any" in the tree. 
	 */
	WordTreeNode getPrefixNode(String word)
	{
		//   ADD YOUR CODE BELOW HERE
		WordTreeNode tmp = new WordTreeNode();
		char c = 0;
		int i = 0;
		tmp = root;
		if (word.equals("")) {
			return tmp;
		}
		c = word.charAt(i);
		while (tmp.getChild(c)!=null && c==tmp.getChild(c).charInParent) {
			tmp = tmp.getChild(c);
			i++;
			if (i< word.length()) {
				c = word.charAt(i);
			} else {
				break;
			}
		}
		return tmp;
		//   ADD YOUR CODE ABOVE HERE

	}

	/*
	 * Similar to getPrefixNode() but now return the prefix as a String, rather than as a TreeNode.
	 */

	public String getPrefix(String word)
	{
		return getPrefixNode(word).toString();
	}


	/*
	 *  Return true if word is contained in the tree (i.e. it was added by insert), false otherwise.
	 *  Hint:  any string is a prefix of itself, so you can use getPrefixNode().
	 */
	public boolean contains(String word)
	{  
		//   ADD YOUR CODE BELOW HERE
		WordTreeNode prefix = this.getPrefixNode(word);
		if (prefix.endOfWord==true) {
			return true;
		}
		else {
			return false;
		}
		//   ADD YOUR CODE ABOVE HERE
	}

	/*
	 *  Return a list of all words in the tree that have the given prefix. 
	 *  For example,  getListPrefixMatches("") should return all words in the tree.
	 */
	public ArrayList<String> getListPrefixMatches( String prefix )
	{
		//  ADD YOUR CODE BELOW
		ArrayList <String> words = new ArrayList<String>();
		WordTreeNode prefixNode = this.getPrefixNode(prefix);
		String word = null;
		if (prefix.equals("")) {
			prefixNode = this.root;
			word = "";
		}
		else {
			word = getPrefix(prefix);
		}
		if (prefix.equals("")) {
			if (prefixNode.getNumOfChildren()!=0) {
				for (int j=0;j<prefixNode.children.length;j++) {
					if (prefixNode.getChild((char) j)!=null) {
						word+=prefixNode.getChild((char) j).charInParent;
						ArrayList<String> tmpWords = getListPrefixMatches(word);
						words.addAll(tmpWords);
						word = getPrefix(prefix);
					} else {
						word = prefix;
						if (!(word.equals(""))) {
							words.add(word);
						}
					}
				}
			}
		} else if (prefixNode.getNumOfChildren()!=0 && prefixNode!=this.root) {
			if (prefixNode.isEndOfWord()==true) {
				words.add(prefix);
			}
			for (int i=0;i<prefixNode.children.length;i++) {
				if (prefixNode.getChild((char) i)!=null) {
					word+=prefixNode.getChild((char) i).charInParent;
					ArrayList<String> tmpWords = getListPrefixMatches(word);
					words.addAll(tmpWords);
					word = getPrefix(prefix);
				}
			}
		} else {
			if (prefixNode!=root) {
				word = prefix;
			}
			words.add(word);
		}
		return words;
		//  ADD YOUR CODE ABOVE HERE
	}


	/*
	 *  Below is the inner class defining a node in a Tree (prefix) tree.  
	 *  A node contains an array of children: one for each possible character.  
	 *  The children themselves are nodes.
	 *  The i-th slot in the array contains a reference to a child node which corresponds 
	 *  to character  (char) i, namely the character with  ASCII (and UNICODE) code value i. 
	 *  Similarly the index of character c is obtained by "casting":   (int) c.
	 *  So children[97] = children[ (int) 'a']  would reference a child node corresponding to 'a' 
	 *  since (char)97 == 'a'   and  (int)'a' == 97.
	 * 
	 *  To learn more:
	 * -For all unicode charactors, see http://unicode.org/charts
	 *  in particular, the ascii characters are listed at http://unicode.org/charts/PDF/U0000.pdf
	 * -For ascii table, see http://www.asciitable.com/
	 * -For basic idea of converting (casting) from one type to another, see 
	 *  any intro to Java book (index "primitive type conversions"), or google
	 *  that phrase.   We will cover casting of reference types when get to the
	 *  Object Oriented Design part of this course.
	 */

	public class WordTreeNode
	{
		/*  
		 *   Highest allowable character index is NUMCHILDREN-1
		 *   (assuming one-byte ASCII i.e. "extended ASCII")
		 *   
		 *   NUMCHILDREN is constant (static and final)
		 *   To access it, write "TreeNode.NUMCHILDREN"
		 *   
		 *   For simplicity,  we have given each WordTree node 256 children. 
		 *   Note that if our words only consisted of characters from {a,...,z,A,...,Z} then
		 *   we would only need 52 children.   The WordTree can represent more general words
		 *   e.g.  it could also represent many special characters often used in passwords.
		 */

		public static final int NUMCHILDREN = 256;

		WordTreeNode     parent;
		WordTreeNode[]   children;
		int              depth;            // 0 for root, 1 for root's children, 2 for their children, etc..
		
		char             charInParent;    // Character associated with the tree edge from this node's parent 
		                                  // to this node.
		// See comment above for relationship between an index in 0 to 255 and a char value.
		
		boolean endOfWord;   // Set to true if prefix associated with this node is also a word.

		
		// Constructor for new, empty node with NUMCHILDREN children.  
		//  All the children are automatically initialized to null. 

		public WordTreeNode()
		{
			children = new WordTreeNode[NUMCHILDREN];
			
			//   These assignments below are unnecessary since they are just the default values.
			
			endOfWord = false;
			depth = 0; 
			charInParent = (char)0; 
		}


		/*
		 *  Add a child to current node.  The child is associated with the character specified by
		 *  the method parameter.  Make sure you set as many fields in the child node as you can.
		 *  
		 *  To implement this method, see the comment above the inner class TreeNode declaration.  
		 *  
		 */
		
		public WordTreeNode createChild(char  c) 
		{	   
			WordTreeNode child       = new WordTreeNode();
			// ADD YOUR CODE BELOW HERE
			int i = c;
			this.children[i] = child;
			child.parent = this;
			child.depth = this.depth + 1;
			child.charInParent = c;
			// ADD YOUR CODE ABOVE HERE
			return child;
		}

		// Get the child node associated with a given character, i.e. that character is "on" 
		// the edge from this node to the child.  The child could be null.  

		public WordTreeNode getChild(char c) 
		{
			return children[ c ];
		}

		// Test whether the path from the root to this node is a word in the tree.  
		// Return true if it is, false if it is prefix but not a word.

		public boolean isEndOfWord() 
		{
			return endOfWord;
		}

		// Set to true for the node associated with the last character of an input word

		public void setEndOfWord(boolean endOfWord)
		{
			this.endOfWord = endOfWord;
		}

		/*  
		 *  Return the prefix (as a String) associated with this node.  This prefix
		 *  is defined by descending from the root to this node.  However, you will
		 *  find it is easier to implement by ascending from the node to the root,
		 *  composing the prefix string from its last character to its first.  
		 *
		 *  This overrides the default toString() method.
		 */

		public String toString()
		{
			// ADD YOUR CODE BELOW HERE
			WordTreeNode tmp = new WordTreeNode();
			String s = "";
			tmp = this;
			while (tmp!=root) {
				s += tmp.charInParent;
				tmp = tmp.parent;
			}
			char[] tmpS = s.toCharArray();
			s = "";
			for (int i=tmpS.length-1;i>=0;i--) {
				s += tmpS[i];
			}
			return s;
			// ADD YOUR CODE ABOVE HERE
		}
		
		//helper method for determining the number of children a node has
		public int getNumOfChildren() {
			int num = 0;
			for (int i=0;i<=255;i++) {
				if (this.children[i]!=null) {
					num++;
				}
			}
			return num;
		}
	}
	public static void main(String[] args) {
		/*
		ArrayList<String> tester = new ArrayList<String>();
		tester.add("hello");
		tester.add("zero");
		WordTree tree = new WordTree();
		tree.loadWords(tester);
		WordTreeNode treeNode = tree.getPrefixNode("hello");
		System.out.println(treeNode.toString());
		WordTreeNode treeNode2 = tree.getPrefixNode("zero");
		System.out.println(treeNode2.toString());
		*/
		
		/*
		ArrayList<String> tester = new ArrayList<String>();
		tester.add("a");
		tester.add("and");
		tester.add("dance");
		tester.add("dances");
		tester.add("dog");
		tester.add("dogged");
		tester.add("dollar");
		tester.add("dolly");
		WordTree tree = new WordTree();
		tree.loadWords(tester);
		System.out.println(tree.getListPrefixMatches(""));
		System.out.println(tree.getListPrefixMatches("a"));
		*/
	}
}
