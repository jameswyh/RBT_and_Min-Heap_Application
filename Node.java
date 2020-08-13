public class Node {
	//This class describes the node structure used in the Red-Black Tree.

	private static final boolean BLACK = true;
	private static final boolean RED = false;

	Node left;//the left child of the node.
	Node right;//the right child of the node.
	Node parent;//the parent of the node.
	boolean color = BLACK;//the color of the node.
	Triplet triplet;//the building record of the node.
	boolean isnull = false;//whether the node becomes a null node after delete operation.

	public Node(Triplet triplet) {
		this.triplet = triplet;
	}
}
