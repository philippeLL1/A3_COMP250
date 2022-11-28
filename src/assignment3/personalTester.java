package assignment3;

import assignment3.CatCafe.CatNode;

public class personalTester {

    public static void main(String args[]) {

        // Declare some cats
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

        // Test CatNode.hire
        CatCafe cafe = new CatCafe();
        cafe.hire(JTO);
        Printer p1 = new Printer(cafe.root, 0);
        p1.printTree();
        //printTree(cafe.root, 0);
        //System.out.println("~~~~~~~~~~~~~~~~~~~");
        cafe.hire(C);
        Printer p2 = new Printer(cafe.root, 0);
        p2.printTree();
        //printTree(cafe.root, 0);
        //System.out.println("~~~~~~~~~~~~~~~~~~~");
        cafe.hire(JJ);
        Printer p3 = new Printer(cafe.root, 0);
        p3.printTree();
        //printTree(cafe.root, 0);
        //System.out.println("~~~~~~~~~~~~~~~~~~~");
        cafe.hire(J);
        Printer p4 = new Printer(cafe.root, 0);
        p4.printTree();
       // printTree(cafe.root, 0);
       // System.out.println("~~~~~~~~~~~~~~~~~~~");
        cafe.hire(MrsN);
        Printer p5 = new Printer(cafe.root, 0);
        p5.printTree();
        //	cafe.hire(new Cat("Blofeld’s cat", 6, 72, 18, 120.0));
        //	cafe.hire(new Cat("Lucifer", 10, 44, 20, 50.0));
       // printTree(cafe.root, 0);
       // System.out.println("~~~~~~~~~~~~~~~~~~~");

        //cafe.bstRemove(cafe.root, cafe.root.senior);



//        for (Cat m : cafe) {
//            System.out.println(m);
//        }

    }


}

