package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Author: Philippe Latour
// Student ID: 261022386
// Faculty: Arts
// Program: Joint Honours Mathematics & Computer Science, B.A.
// Course: McGill Fall 2022 - COMP250

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
		// Concept: iterate through the list using iterator, and send all of them to an array list.

		ArrayList<Cat> allCats = new ArrayList<Cat>();

		for (Cat c : this) {
			allCats.add(c);
		}

		int amount = 0;

		if (numOfCatsToHonor > allCats.size()) {
			amount = allCats.size();
		}
		else {
			amount = numOfCatsToHonor;
		}

		// Uses a lambda function to tell ArrayList.sort() to use compare cats using the return value of Integer.compare() on
		// two cats'  fur thickness.
		allCats.sort((cat1, cat2) -> { return Integer.compare(cat1.getFurThickness(), cat2.getFurThickness());});

		// New arraylist that we will return
		ArrayList<Cat> hallOfFame = new ArrayList<>();

		for (int i = allCats.size()-1; i > allCats.size()-amount-1; i--) {
			hallOfFame.add(allCats.get(i));
		}

		return hallOfFame;
	}

	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {

		double cost = 0;

		for (Cat c : this) {
			// Add the cost of this cat if it will be groomed in the next numDays
			if (c.getDaysToNextGrooming() <= numDays) {
				cost += c.getExpectedGroomingCost();
			}
		}
		return cost;
	}

	// returns a list of list of Cats. 
	// The cats in the list at index 0 need be groomed in the next week. 
	// The cats in the list at index i need to be groomed in i weeks. 
	// Cats in each sublist are listed in from most senior to most junior. 
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {

		ArrayList<ArrayList<Cat>> schedule = new ArrayList<>();

		for (Cat c : this) {

			int week = c.getDaysToNextGrooming() / 7;

			try {

				schedule.get(week).add(c);

			} catch (IndexOutOfBoundsException e) {

				while (schedule.size() != week+1) {
					ArrayList<Cat> newInner = new ArrayList<>();
					schedule.add(newInner);
				}
				schedule.get(week).add(c);
			}

		}

		return schedule;
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

			CatNode toRemove = findCat(this, c);

			boolean underThis = true;

			/* If the cat we would like to remove is not under the node on which retire() was called,
			*  we check the whole tree. If it is still not there, then the Cat we would like to remove does not exist,
			*  and we return the node on which retire() was called
			*/
			if (toRemove == null) {

				// We did not find the cat under the current node, so we look in the whole tree
				toRemove = findCat(CatCafe.this.root, c);

				if (toRemove == null) {
					// The cat does not exist anywhere in the cafe, so we return the current node
					return this;
				}
				else {
					// The node is not under the current node, but somewhere else in the tree
					underThis = false;
				}
			}

			// Step 1: Remove the node following BST properties.
			CatNode head;
			// If it is under the current node, we remove toRemove from under this node. Else, we want to search the whole tree
			if (!underThis) {
				head = bstRemove(CatCafe.this.root, toRemove); // returns root of whole tree
			}
			else {
				head = bstRemove(this, toRemove); // returns root of current subtree
			}

			boolean bHeap = isHeap(head);

			while (!bHeap) {
				CatNode broken = findHeapBreak(head);
				head = downHeap(broken);
				bHeap = isHeap(head);
			}

			return head;
		}

		// find the cat with highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			return findEldest(this);
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
			// Make a shallow copy of the current tree

			if (root == null) {
				return;
			}

			newCafe = new CatCafe(CatCafe.this);

			current = findCat(newCafe.root, findYoungest(newCafe.root));

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

			// Find the new minimum of our tree, only if there are still elements left
			if (newCafe.root != null) {
				current = findCat(newCafe.root, findYoungest(newCafe.root));;
			}

			return currentCat;
		}

		public boolean hasNext() {

			if (root == null) {
				return false;
			}

			return newCafe.root != null;
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

	// Find a Cat c in a tree rooted at CatNode root
	private static CatNode findCat(CatNode root, Cat c) {

		if (root == null) {
			return null;
		}
		else if (root.catEmployee.equals(c)) {
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
				// Depending on the position of root, we set the corresponding branch to the pivot
				if (isJunior(root)) {
					root.parent.junior = pivot;
				}
				else {
					root.parent.senior = pivot;
				}
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

				// Depending on the position of root, we set the corresponding branch to the pivot
				if (isJunior(root)) {
					root.parent.junior = pivot;
				}
				else {
					root.parent.senior = pivot;
				}

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

		int jThicc = -1;
		int sThicc = -1;
		int rootThicc;

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

		// if we had to replace the top root, return the element
		if (element.parent == null) {
			return element;
		}

		return this.root;

	}
	//--------------------------------------------------------

	// Helpers for CatNode.retire()
	//--------------------------------------------------------
	public CatNode bstRemove(CatNode root, CatNode toRemove) {

		// Base Case -> when we meet toRemove
		if (root.catEmployee.equals(toRemove.catEmployee)) {

			if (toRemove.senior == null && toRemove.junior == null) { // Case 1: the node to remove has no children

				// Set the correct branch of toRemove's parent to null
				// Only set the parent links if there is a parent (ie root isn't the head of the tree)
				if (root.parent != null) {
					if (isJunior(root)) { // toRemove is the junior of its parent
						root.parent.junior = null;
					} else { // toRemove is the senior of its parent
						root.parent.senior = null;
					}
				}

				// disconnect toRemove from its parent
				root.parent = null;

				// set root (toRemove node) to null
				root = null;

			}
			else if (toRemove.senior == null) { // Case 2: the node toRemove has 1 child -> a junior
				// Move toRemove's junior child up to toRemove's parent's corresponding branch
				CatNode parent = root.parent;

				// Only set the parent links if there is one (ie root isn't the head of the tree)
				if (parent != null) {
					if (isJunior(root)) {
						parent.junior = root.junior;
					} else {
						parent.senior = toRemove.junior;
					}
				}

				root.junior.parent = parent;

				root = root.junior;
			}
			else if (toRemove.junior == null) { // Case 3: the node to toRemove has 1 child -> a senior

				CatNode parent = root.parent;

				if (root.parent != null) {
					if (isJunior(root)) {
						parent.junior = root.senior;
					} else {
						parent.senior = root.senior;
					}
				}

				root.senior.parent = parent;

				root = root.senior;
			}
			else { // Case 4: there are two children -> one junior and one senior

				// findMostSenior in toRemove.junior. It will take toRemove's place
				CatNode oldestCat = findCat(toRemove, findEldest(toRemove.junior));

				// Keep track of toRemove's parent
				CatNode rmParent = root.parent;

				// The senior can either be the junior to toRemove or be a senior to a sub tree
				// make sure it exists
				// find if toRemove is a junior or a senior
				// Set oldestCat's parent branch
				// if it doesn't, oldestCat becomes the root
				if (oldestCat.parent.catEmployee.equals(toRemove.catEmployee)) { // Case 1: oldest cat is directly under toRemove
					// It will simply take its place, no need to rearrange the tree yet

					oldestCat.senior = toRemove.senior; // take the right branch of toRemove
					toRemove.senior.parent = oldestCat;
				}
				else { // Case 2: oldest cat is the senior to some subtree under toRemove

					// Step 1: Connect oldestCat's junior branch to the senior branch of oldestCat's parent
					if (oldestCat.junior != null) {
						oldestCat.junior.parent = oldestCat.parent; // set the parent
						oldestCat.parent.senior = oldestCat.junior; // set the child
					}
					else {
						// disconnect oldestCat from its parent, set the parent senior field to null
						oldestCat.parent.senior = null;
					}



					// Step 2: Connect oldestCat to toRemove's children
					oldestCat.junior = toRemove.junior; // junior branch
					oldestCat.senior = toRemove.senior; // senior branch
					oldestCat.junior.parent = oldestCat; // junior parent branch
					oldestCat.senior.parent = oldestCat; // senior parent branch


				}
				if (toRemove.parent != null) { // All cases: Connect toRemove's parent to oldestCat

					// We must know if toRemove was connected to the junior or senior branch of its parent
					if (isJunior(toRemove)) { // toRemove was connected to the junior node
						toRemove.parent.junior = oldestCat;
					}
					else {
						toRemove.parent.senior = oldestCat;
					}

					// Connect oldestCat to its parent
					oldestCat.parent = toRemove.parent;
				}
				else {
					// oldestCat becomes the root of the whole tree, so it has no parent
					oldestCat.parent = null;
				}

				root = oldestCat;
			}
		}
		else { // Recursive step

			if (toRemove.catEmployee.compareTo(root.catEmployee) > 0) { // toRemove has seniority, so we search right subtree
				root.senior = bstRemove(root.senior, toRemove);

				// If we haven't removed the root's senior, we want to add it back to the tree
				if (root.senior != null) {
					root.senior.parent = root;
				}

			}
			else { // root has seniority, so we search the left subtree

				root.junior = bstRemove(root.junior, toRemove);

				// If we haven't removed the root's senior, we want to add it back to the tree
				if (root.junior != null) {
					root.junior.parent = root;
				}

			}
		}

		return root;
	}

	public CatNode downHeap(CatNode element) {

		CatNode tmpNode;

		// Rotate at least once, because the tree is definitely broken
//		do {
//			rotateTree(element.parent, element);
//
//			// if our element has become the top node, we don't want to rotate anymore
//			if (element.parent == null) {
//				break;
//			}
//
//		} while (element.catEmployee.getFurThickness() > element.parent.catEmployee.getFurThickness());

		rotateTree(element.parent, element);

		tmpNode = element;

		// go back all the way up to get the new root of the whole tree
		while (tmpNode.parent != null) {
			tmpNode = tmpNode.parent;
		}
		// It will exit when tmpNode's parent is null, so tmpNode will end up being the root of the tree
		return tmpNode;
	}

	public CatNode findHeapBreak(CatNode root) {

		// Base case:
		if (root == null) {
			return null;
		}
		else if (root.parent != null && root.parent.catEmployee.getFurThickness() < root.catEmployee.getFurThickness()) {

			// We want to return the child with the greater fur thickness

			if (isJunior(root) && root.parent.senior != null) {
				// Compare against sibling (senior of parent)
				if (root.catEmployee.getFurThickness() > root.parent.senior.catEmployee.getFurThickness()) {
					return root;
				}
				else {
					return root.parent.senior;
				}

			}
			else if (root.parent.junior != null) {
				// Compare against sibling (junior of parent)
				if (root.catEmployee.getFurThickness() > root.parent.junior.catEmployee.getFurThickness()) {
					return root;
				}
				else {
					return root.parent.junior;
				}
			}

			return root;
		}
		else {
			// Recursive step

			CatNode tmp1 = findHeapBreak(root.junior);
			CatNode tmp2 = findHeapBreak(root.senior);

			// We want to pass the greater of the two


			// Make sure we aren't passing a null pointer
			if (tmp1 != null) {
				// if the senior is null, set root to junior
				root = tmp1;
			}
			else if (tmp2 != null){
				// if the junior is null, set root to senior
				root = tmp2;
			}
			else {
				// If the root has no children and its fur size isn't greater than its parent's, we return null
				root = null;
			}
		}
		return root;
	}
	//--------------------------------------------------------


	// Helper for bstRemove
	private boolean isJunior(CatNode cat) {
		if (cat.parent == null) {
			return false;
		}
		else return cat.parent.junior == cat;
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


