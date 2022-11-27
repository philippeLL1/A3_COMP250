package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatCafe implements Iterable<Cat> {
	public CatNode root;

	public CatCafe() {
	}


	public CatCafe(CatNode dNode) {
		this.root = dNode;
	}

	// Constructor that makes a shallow copy of a CatCafe
	// New CatNode objects, but same Cat objects
	public CatCafe(CatCafe cafe) {
		// Call helper method
		shallowCopy(cafe.root);

	}

	// add a cat to the cafe database
	public void hire(Cat c) {
		if (root == null) 
			root = new CatNode(c);
		else
			root = root.hire(c); // of type CatNode
	}

	// removes a specific cat from the cafe database
	public void retire(Cat c) {
		if (root != null)
			root = root.retire(c);
	}

	// get the oldest hire in the cafe
	public Cat findMostSenior() {
		if (root == null)
			return null;

		return root.findMostSenior();
	}

	// get the newest hire in the cafe
	public Cat findMostJunior() {
		if (root == null)
			return null;

		return root.findMostJunior();
	}

	// returns a list of cats containing the top numOfCatsToHonor cats 
	// in the cafe with the thickest fur. Cats are sorted in descending 
	// order based on their fur thickness. 
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return null;
	}

	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return 0;
	}

	// returns a list of list of Cats. 
	// The cats in the list at index 0 need be groomed in the next week. 
	// The cats in the list at index i need to be groomed in i weeks. 
	// Cats in each sublist are listed in from most senior to most junior. 
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return null;
	}


	public Iterator<Cat> iterator() {
		return new CatCafeIterator();
	}

	public class CatNode {
		public Cat catEmployee;
		public CatNode junior;
		public CatNode senior;
		public CatNode parent;

		public CatNode(Cat c) {
			this.catEmployee = c;
			this.junior = null;
			this.senior = null;
			this.parent = null;
		}

		// adds the c to the tree rooted at this node and returns the root of the resulting tree
		public CatNode hire (Cat c) {

			// Make a new node with the Cat c we've been given
			CatNode newNode = new CatNode(c);

			// Step 1: Add the Cat c to the tree based on seniority (BST property). Does not change the root.
			CatNode head = bstAdd(this, newNode); // returns root of the tree

			// Step 2: Check Heap property
			boolean isHeap = isHeap(head);

			// Step 3: If the tree is broken, rearrange it
			if (!isHeap) {
				/* CatNode.hire can only add one node to the tree at a time,
					so we assume that it is Cat c that broke the Heap property
				*/
				head = upHeap(newNode);
			}

			return head;
		}




		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			// Same strategy as hire

			// Step 1: remove the cat following BST properties

			return null;
		}

		// find the cat with highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			return findEldest(this);
		}
		// HELPER METHOD FOR findMostSenior
		private static Cat findEldest(CatNode node) {
			// Base case
			if (node.senior == null) {
				// No junior, so it is the most junior
				return node.catEmployee;
			}
			else { // recursive step
				return findEldest(node.senior);
			}
		}

		// find the cat with the lowest seniority in the tree rooted at this
		public Cat findMostJunior() {
			// Call helper method
			return findYoungest(this);
		}


		// Feel free to modify the toString() method if you'd like to see something else displayed.
		public String toString() {
			String result = this.catEmployee.toString() + "\n";
			if (this.junior != null) {
				result += "junior than " + this.catEmployee.toString() + " :\n";
				result += this.junior.toString();
			}
			if (this.senior != null) {
				result += "senior than " + this.catEmployee.toString() + " :\n";
				result += this.senior.toString();
			} /*
			if (this.parent != null) {
				result += "parent of " + this.catEmployee.toString() + " :\n";
				result += this.parent.catEmployee.toString() +"\n";
			}*/
			return result;
		}
	}


	private class CatCafeIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED
		private CatNode current;
		private CatCafe newCafe;
		private CatCafeIterator() {

			// Finds the node with the least seniority, because that is where the iterator starts
			current = findFirstNode(root);

			// Make a shallow copy of the current tree
			newCafe = new CatCafe(root);

			// Assign it to the field

		}
		// HELPER METHOD FOR CONSTRUCTOR -- CHANGE THISSSSSSSSS
		private CatNode findFirstNode(CatNode root) {
			// Base case
			if (root.junior == null) {
				// No junior, so it is the most junior
				return root;
			}
			else { // recursive step
				return findFirstNode(root.junior);
			}
		}

		public Cat next() {

			// Make sure we can actually go to the next element
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			// Store the current cat object
			Cat currentCat = current.catEmployee;

			// Remove the current node
			newCafe.retire(current.catEmployee);

			// Find the new minimum of our tree, only if there are still element
			if (newCafe.root != null){
				current = findFirstNode(newCafe.root);
			}

			return currentCat;
		}

		public boolean hasNext() {
			if (current.equals(root.findMostSenior())) {
				return false;
			}

			return true;
		}

	}

	public static void main(String[] args) {
		Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
		Cat C = new Cat("Chessur", 8, 23, 2, 250.0);
		Cat J = new Cat("Jonesy", 0, 21, 12, 30.0);	
		Cat JJ = new Cat("JIJI", 156, 17, 1, 30.0);
		Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
		Cat MrB = new Cat("Mr. Bigglesworth", 71, 0, 31, 55.0);
		Cat MrsN = new Cat("Mrs. Norris", 100, 68, 15, 115.0);
		Cat T = new Cat("Toulouse", 180, 37, 14, 25.0);


		Cat BC = new Cat("Blofeld's cat", 6, 72, 18, 120.0);
		Cat L = new Cat("Lucifer", 10, 44, 20, 50.0);


	}







	// ------------------HELPER METHODS---------------------- \\

	// Helper for findMostJunior
	private static Cat findYoungest(CatNode node) {
		// Base case
		if (node.junior == null) {
			// No junior, so it is the most junior
			return node.catEmployee;
		}
		else { // recursive step
			return findYoungest(node.junior);
		}
	}

	// Find a Cat c in a tree rooted at CatNode root
	private static CatNode findCat(CatNode root, Cat c) {

		if (root.catEmployee.equals(c)) {
			return root;
		}
		else if (c.compareTo(root.catEmployee) > 0) { // c is the root's senior
			root = findCat(root.senior, c);
		}
		else if (c.compareTo(root.catEmployee) < 0) { // root is c's senior
			root = findCat(root.junior, c);
		}

		return root;
	}

	// Helper method to rotate
	private void rotateTree(CatNode root, CatNode pivot) {

		// **The goal is to switch root and pivot**

		if (pivot.parent.junior != null && pivot.parent.junior.catEmployee.equals(pivot.catEmployee)) {
			// The pivot is the left child of its parent. We rotate right

			// Step 1:
			root.junior = pivot.senior;

			// Step 2 (if pivot.senior exists)
			if (pivot.senior != null) {
				pivot.senior.parent = root;
			}

			// Step 3:
			pivot.senior = root;

			// Step 4: if there is a root above root & pivot, we must set its pointers
			if (root.parent != null) {
				// Set its senior, because we are rotating left (?)
				root.parent.junior = pivot;
			}

		}
		else {
			// The pivot is the right child of its parent. We rotate left

			// Step 1:
			root.senior = pivot.junior;

			// Step 2 (if pivot.senior exists)
			if (pivot.junior != null) {
				pivot.junior.parent = root;
			}

			// Step 3:
			pivot.junior = root;

			// Step 4: if there is a root above root & pivot, we must set its pointers
			if (root.parent != null) {
				// Set its senior, because we are rotating left (?)
				root.parent.senior = pivot;
			}


		}

		// Steps 5 & 6 (both right and left rotations end with the same steps):
		pivot.parent = root.parent;
		root.parent = pivot;

	}

	// Helpers for CatNode.hire()
	//--------------------------------------------------------
	private CatNode bstAdd(CatNode root, CatNode toAdd) {

		if (root == null) {
			root = toAdd;
		}
		else if (root.catEmployee.compareTo(toAdd.catEmployee) > 0) { // the root has seniority
			// root is senior, so toAdd is junior
			root.junior = bstAdd(root.junior, toAdd);
			// set the parent
			root.junior.parent = root;
		}
		else if (root.catEmployee.compareTo(toAdd.catEmployee) < 0)  { // toAdd has seniority
			// root is junior, so toAdd is senior
			root.senior = bstAdd(root.senior, toAdd);
			// set the parent of the new senior node
			root.senior.parent = root;
		}

		// Returns top root
		return root;
	}
	private boolean isHeap(CatNode root) {

		int jThicc = 0;
		int sThicc = 0;
		int rootThicc = 0;

		// Base case, get fur sizes otherwise
		if (root == null) {
			return true;
		}
		else {
			rootThicc = root.catEmployee.getFurThickness();
		}

		if (root.junior != null) { jThicc = root.junior.catEmployee.getFurThickness(); }
		if (root.senior != null) { sThicc = root.senior.catEmployee.getFurThickness(); }

		if (rootThicc > jThicc && rootThicc > sThicc) {
			// Call isHeap on left subtree
			boolean left = isHeap(root.junior);
			// Call isHeap on right subtree
			boolean right = isHeap(root.senior);

			// Both must be true for the tree to be a heap
			return left && right;
		}
		else {
			return false;
		}
	}
	private CatNode upHeap(CatNode element) {

		// We know we have to rotate at least once
		do {
			rotateTree(element.parent, element);
		} while (!(element.parent == null || element.catEmployee.getFurThickness() < element.parent.catEmployee.getFurThickness()));


		if (element.parent == null) {
			return element;
		}

		return this.root;

	}
	//--------------------------------------------------------

	private CatNode bstRemove(CatNode root, CatNode toRemove) {

		// Base Case
		if (root == null) {
			return null;
		}
		else if (root.equals(toRemove)) {
			if (toRemove.senior == null && toRemove.junior == null) {
				// A leaf, just remove the parent's connection to it
				if (toRemove.parent.junior.catEmployee.equals(toRemove.catEmployee)) { // toRemove is the junior of its parent
					toRemove.parent.junior = null;
				}
				else  { // toRemove is the senior of its parent
					toRemove.parent.senior = null;
				}
			}
			else if (toRemove.senior == null) {
				// Set left node as toRemove (there is no right node)
				CatNode parent = toRemove.parent;
				parent.junior = toRemove.junior;
				toRemove.junior.parent = parent;
			}
			else if (toRemove.junior == null) {
				// Set right node as toRemove (there is no left node)
				CatNode parent = toRemove.parent;
				parent.senior = toRemove.senior;
				toRemove.senior.parent = parent;
			}
			else { // There are two children

				// findMostSenior in toRemove.junior, set that as the new toRemove
				CatNode oldestCat = findCat(toRemove, findYoungest(toRemove.junior));

				// Keep track of toRemove's parent
				CatNode rmParent = toRemove.parent;

				// Make sure toRemove isn't top node/root
				if (rmParent == null) { // oldest cat becomes the head, because toRemove is the root node of the cafe
					this.root = oldestCat;
				}
				else { // oldestCat will take on an inner node position

					// oldestCat will always be a senior node, unless it is directly under toRemove
					if (oldestCat.parent.catEmployee.equals(toRemove.catEmployee)) {
						toRemove.junior = null; // this isn't really necessary, but will free up memory (hopefully)
					}
					else { // it is some senior node in the left subtree of toRemove
						// set its parent link to oldestCat to null
						oldestCat.parent.senior = null;
					}

					// TODO: Take care of oldestCat's children

					// Set the new parent for oldestCat
					oldestCat.parent = rmParent;

					// Figure out if toRemove is the left or right node of its parent
					if (toRemove.catEmployee.equals(toRemove.parent.junior.catEmployee)) {
						// it is the junior node, so oldestCat must become the junior node of toRemove's parent
						rmParent.junior = oldestCat;
					}
					else {
						// toRemove if the senior node, oldestCat becomes senior to toRemove.parent
						rmParent.senior = oldestCat;
					}
				}
			}
		}
		else {
			if (toRemove.catEmployee.compareTo(root.catEmployee) > 0) { // toRemove has seniority, so we search right subtree
				bstRemove(root.senior, toRemove);
			}
			else { // root has seniority, so we search the left subtree
				bstRemove(root.junior, toRemove);
			}
		}


		return root;
	}


	// Helper for shallow copy constructor
	private void shallowCopy(CatNode root) {

		// The logic is to do a preorder traversal of the tree, hiring all cats present into the current tree

		// Visit root
		this.hire(root.catEmployee);

		// Traverse left tree, if possible
		if (root.junior != null) {
			shallowCopy(root.junior);
		}

		// Traverse right tree, if possible
		if (root.senior != null) {
			shallowCopy(root.senior);
		}
	}


}


