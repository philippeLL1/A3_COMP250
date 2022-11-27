package assignment3;

public class Printer {

    CatCafe.CatNode root;
    int spaceCount;

    public Printer(CatCafe.CatNode root, int spaceCount) {
        this.root = root;
        this.spaceCount = spaceCount;
    }

    public void printTree() {
        //Thanks, GeeksForGeeks!:D
        printTree(root, spaceCount);
        System.out.println("\n\n----------------------------------------------------------\n\n");

    }

    private void printTree(CatCafe.CatNode root, int spaceCount) {
        if (root == null)
            return;

        int spacing = spaceCount + 20;

        printTree(root.senior, spacing);


        System.out.println();
        for (int index = 0; index < spacing; index++)
            System.out.print(" ");
        System.out.println(root.catEmployee);

        printTree(root.junior, spacing);
    }

}
