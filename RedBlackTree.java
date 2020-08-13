import java.util.Stack;

public class RedBlackTree {
	//This class describes the structure of Red-Black Tree.

	private static final boolean RED = false;
    private static final boolean BLACK = true;
    
    Node root;//the root of the Red-Black Tree.


    public Node lookUpNode(Node node) {
    	//Look up a certain node in the Red-Black Tree.
    	if(root != null) {
    		Node cur = root;
        	while(cur != null && cur.triplet.buildingNum != node.triplet.buildingNum) {
            	if(node.triplet.buildingNum < cur.triplet.buildingNum)
            		cur = cur.left;
            	else if(node.triplet.buildingNum > cur.triplet.buildingNum)
            		cur = cur.right;
        	}
        	return cur;
    	}
    	else
    		return null;
    }

    public void addNode(Node node) {
    	//Add a node into the Red-Black Tree.
		if(root == null)//if tree is empty, the node becomes the root.
			root = node;
		else {//else find the parent for the new node.
			Node cur = root;
			Node parent = null;
			boolean leftchild = false;
			do {
				parent = cur;
				if (node.triplet.buildingNum > cur.triplet.buildingNum) {
					cur = cur.right;
					leftchild = false;
				}
				else {
					cur = cur.left;
					leftchild = true;
				}
			}while (cur != null);
			node.parent = parent;
			if(leftchild == true) {
				parent.left = node;
			}
			else {
				parent.right = node;
			}
			insertFix(node);
		}
	}

    public void insertFix(Node node) {
    	//Rebalance the Red-Black Tree after inserting a new node.
    	node.color = RED;
    	Node p = node;
    	Node pp = p.parent;
    	Node gp = pp.parent;
    	if(p != null && pp != null && gp != null) {
    		//XYr
    		if(gp.left != null && gp.right != null && gp.left.color == RED && gp.right.color == RED && p.color == RED) {
    			gp.color = RED;
    			gp.right.color = BLACK;
    			gp.left.color = BLACK;
    			if(gp.parent != null) {
    				if(gp.parent.parent != null) {
    					insertFix(gp);
    				}
    			}
    			else {
    				gp.color = BLACK;
    			}
    		}
    		//RRb
    		else if(p != null && pp != null && gp != null && pp.color == RED && p.color == RED) {
    			if(gp.right == pp && pp.right == p) {
        			RR(pp, gp);
        			gp.color = RED;
        			pp.color = BLACK;
        		}
        		//LLb
        		else if(gp.left == pp && pp.left == p) {
        			LL(pp, gp);
        			gp.color = RED;
        			pp.color = BLACK;
        		}
        		//LRb
        		else if(gp.left == pp && pp.right == p) {
        			RR(p, pp);
        			LL(p, gp);
        			p.color = BLACK;
        			gp.color = RED;
        		}
        		//RLb
        		else if(gp.right == pp && pp.left == p) {
        			LL(p, pp);
        			RR(p, gp);
        			p.color = BLACK;
        			gp.color = RED;
        		}
    		}
    	}
    }

    public void deleteNode(Node node, boolean isrecursion, boolean left) {
    	//Delete a node from the Red-Black Tree. 
		if(root != null){
			Node cur = root;
			Node parent = null;
			boolean leftchild = false;
			boolean isleaf = false;
			boolean nullleaf = false;
			boolean fix = true;
			boolean recursion = false;
			do {//find the target node.
				if (node.triplet.buildingNum > cur.triplet.buildingNum) {
					parent = cur;
					cur = cur.right;
					leftchild = false;
				}
				else if (node.triplet.buildingNum < cur.triplet.buildingNum){
					parent = cur;
					cur = cur.left;
					leftchild = true;
				}
				else if(node.triplet.buildingNum == cur.triplet.buildingNum && isrecursion) {
					if(left) {
						parent = cur;
						cur = cur.left;
						leftchild = true;
					}
					else {
						parent = cur;
						cur = cur.right;
						leftchild = false;
					}
					isrecursion = false;
				}
				else
					break;
			}while (cur != null);

			if(cur != null) {
				if(cur == root && cur.left == null && cur.right == null)//if node is the only node in the tree, the root becomes null.
					root = null;
				else {
					Node cur1 = null;
					if(cur.left != null && cur.right != null) {
						cur1 = cur.left;
						while(cur1.right != null) {//find the largest node in the left subtree.
							cur1 = cur1.right;
						}
						if(cur1.color == BLACK && cur1.left != null)//if the replace node is black and not a leaf, a recursion is needed.
							recursion = true;
					}
					else if(cur.right != null) {
						cur1 = cur.right;
						if(cur1.color == RED) {//if the replace node is red, clip it to black
							cur1.color = BLACK;
							fix = false;//no fix is needed any more
						}
					}
					else if(cur.left != null) {
						cur1 = cur.left;
						if(cur1.color == RED) {//if the replace node is red, clip it to black
							cur1.color = BLACK;
							fix = false;//no fix is needed any more
						}
					}
					else {//the node is a leaf node.
						cur1 = cur;
						isleaf = true;
					}

					Node nullNode = new Node(new Triplet(cur1.triplet.buildingNum, 0, 0));
					nullNode.isnull = true;
					//A null node will be added to the tree when the deleted node is a black node.
					//which will be removed after rebalancing.
					if((recursion == true || (isleaf && cur1.color == BLACK)) && cur1.parent != null){
		   				if(cur1.parent.left == cur1)
	        				cur1.parent.left = nullNode;
	        			else if(cur1.parent.right == cur1)
	        				cur1.parent.right = nullNode;
	        			nullNode.parent = cur1.parent;
	        			if(!isleaf) {
	        				nullNode.left = cur1.left;
	        				nullNode.left.parent = nullNode;
	        				nullNode.color = cur1.color;
	        			}
	        		}
					else if(cur1.color == BLACK && cur1.left == null && cur1.right == null && cur.left != null && cur.right != null) {
		   				if(cur1.parent.left == cur1) {
		   					if(cur1.parent == cur) {
		   						cur1.left = nullNode;
		   						nullNode.parent = cur1;
		   					}
		   					else {
		   						cur1.parent.left = nullNode;
		   						nullNode.parent = cur1.parent;
		   					}
		   				}
	        			else if(cur1.parent.right == cur1) {
		   					if(cur1.parent == cur) {
		   						cur1.right = nullNode;
		   						nullNode.parent = cur1;
		   					}
		   					else {
		   						cur1.parent.right = nullNode;
		   						nullNode.parent = cur1.parent;
		   					}
	        			}
					}
	        		else if((isleaf && cur1.color == RED && cur1.parent != null) || (cur1.left == null && cur1.right == null && cur1.color == RED)) {
	        			//if the replace node is a red leaf, delete it and no fix is needed.
	        			if(cur1.parent.left == cur1)
	        				cur1.parent.left = null;
	        			else if(cur1.parent.right == cur1)
	        				cur1.parent.right = null;
	        			fix = false;
	        		}
					if(parent != null && leftchild == true && !isleaf)
						parent.left = cur1;
					else if(parent != null && leftchild != true && !isleaf)
						parent.right = cur1;
					else if(!isleaf)
						root = cur1;

					cur1.parent = parent;
				    cur1.color = cur.color;

				    if(cur1 != cur.left)
						cur1.left = cur.left;
					if(cur1 != cur.right)
						cur1.right = cur.right;
					if (cur1.left != null)
						cur1.left.parent = cur1;
					if(cur1.right != null)
						cur1.right.parent = cur1;

					if(nullNode.left == null && nullNode.right == null) {
						nullleaf = true;
					}

					Node y = null;
					if(isleaf || nullleaf)
						y = nullNode;
					else
						y = cur1;
					if(recursion)
						if(cur.triplet.buildingNum > cur1.triplet.buildingNum)
							deleteNode(nullNode, true, true);
						else
							deleteNode(nullNode, true, false);
					else if(fix) {
						deleteFix(y);
					}
				}
			}
		}
	}

    public void deleteFix(Node node) {
    	//Rebalance the Red-Black Tree after deleting a node.
    	Node y = node;
    	Node py = node.parent;

    	if(py != null) {
        	if(y.color == BLACK) {
        		//Rbn
        		if(py.right == y && py.left.color == BLACK) {
        			Node v = py.left;
        			int redNum = 0;
        			if(v.left != null && v.left.color == RED)
        				redNum++;
        			if(v.right != null && v.right.color == RED)
        				redNum++;
        			//Rb0
        			if(redNum == 0) {
        				//case1
        				if(py.color == BLACK) {
        					v.color = RED;
        					deleteFix(py);
        				}
        				//case2
        				else if(py.color == RED) {
        					py.color = BLACK;
        					v.color = RED;
        				}

        			}
        			//Rb1
        			else if(redNum == 1) {
        				//case1
        				if(v.left != null && v.left.color == RED) {
        					LL(v, py);
                			v.left.color = BLACK;
                			if(py.color == RED)
                				v.color = RED;
                			else
                				v.color = BLACK;
                			py.color = BLACK;
                			if(v.parent == null)
                				root = v;
        				}
        				//case2
        				else if(v.right != null && v.right.color == RED) {
        					Node w = v.right;
        					RR(w, v);
        					LL(w, py);
                			if(py.color == RED)
                				w.color = RED;
                			else
                				w.color = BLACK;
                			py.color = BLACK;
                			if(w.parent == null)
                				root = w;
        				}
        			}
        			//Rb2
        			else if(redNum == 2) {
        				Node w = v.right;
        				RR(w, v);
    					LL(w, py);
            			if(py.color == RED)
            				w.color = RED;
            			else
            				w.color = BLACK;
            			py.color = BLACK;
            			if(w.parent == null)
            				root = w;
        			}
        		}
        		//Rrn
        		else if(py.right == y && py.left.color == RED) {
        			Node v = py.left;
        			Node w = v.right;
        			int redNum = 0;
        			if(w!=null) {
        			if(w.left != null && w.left.color == RED)
        				redNum++;
        			if(w.right != null && w.right.color == RED)
        				redNum++;
        			}
        			//Rr0
        			if(redNum == 0) {
        				LL(v, py);
            			v.color = BLACK;
            			if(w != null)
            				w.color = RED;
            			if(v.parent == null)
            				root = v;
        			}
        			//Rr1
        			else if(redNum == 1) {
        				//case1
        				if(w.left != null && w.left.color == RED) {
        					RR(w, v);
        					LL(w, py);
                			v.right.color = BLACK;
                			if(w.parent == null)
                				root = w;
        				}
        				//case2
        				else if(w.right != null && w.right.color == RED) {
        					Node x = w.right;
        					if(py.parent != null && py.parent.left == py)
                				py.parent.left = x;
                			else if (py.parent != null && py.parent.right == py)
                				py.parent.right = x;
                			x.parent = py.parent;
                			v.parent = x;
                			py.parent = x;
                			w.right = x.left;
                			py.left = x.right;
                			if(x.left != null) {
                				x.left.parent = w;
                			}
                			if(x.right != null) {
                				x.right.parent = py;
                			}
                			x.left = v;
                			x.right = py;
                			x.color = BLACK;
                			if(x.parent == null)
                				root = x;
        				}
        			}
        			//Rr2
        			else if(redNum == 2) {
        				Node x = w.right;
    					if(py.parent != null && py.parent.left == py)
            				py.parent.left = x;
            			else if (py.parent != null && py.parent.right == py)
            				py.parent.right = x;
            			x.parent = py.parent;
            			v.parent = x;
            			py.parent = x;
            			w.right = x.left;
            			py.left = x.right;
            			if(x.left != null) {
            				x.left.parent = w;
            			}
            			if(x.right != null) {
            				x.right.parent = py;
            			}
            			x.left = v;
            			x.right = py;
            			x.color = BLACK;
            			if(x.parent == null)
            				root = x;
        			}
        		}


        		//Lbn
        		else if(py.left == y && py.right.color == BLACK) {
        			Node v = py.right;
        			int redNum = 0;
        			if(v.left != null && v.left.color == RED)
        				redNum++;
        			if(v.right != null && v.right.color == RED)
        				redNum++;
        			//Lb0
        			if(redNum == 0) {
        				//case1
        				if(py.color == BLACK) {
        					v.color = RED;
        					deleteFix(py);
        				}
        				//case2
        				else if(py.color == RED) {
        					py.color = BLACK;
        					v.color = RED;
        				}

        			}
        			//Lb1
        			else if(redNum == 1) {
        				//case1
        				if(v.right != null && v.right.color == RED) {
        					RR(v, py);
                			v.right.color = BLACK;
                			if(py.color == RED)
                				v.color = RED;
                			else
                				v.color = BLACK;
                			py.color = BLACK;
                			if(v.parent == null)
                				root = v;
        				}
        				//case2
        				else if(v.left != null && v.left.color == RED) {
        					Node w = v.left;
        					LL(w, v);
        					RR(w, py);
                			if(py.color == RED)
                				w.color = RED;
                			else
                				w.color = BLACK;
                			py.color = BLACK;
                			if(w.parent == null)
                				root = w;
        				}
        			}
        			//Lb2
        			else if(redNum == 2) {
        				Node w = v.left;
        				LL(w, v);
    					RR(w, py);
            			if(py.color == RED)
            				w.color = RED;
            			else
            				w.color = BLACK;
            			py.color = BLACK;
            			if(w.parent == null)
            				root = w;
        			}
        		}
        		//Lrn
        		else if(py.left == y && py.right.color == RED) {
        			Node v = py.right;
        			Node w = v.left;
        			int redNum = 0;
        			if(w.left != null && w.left.color == RED)
        				redNum++;
        			if(w.right != null && w.right.color == RED)
        				redNum++;
        			//Lr0
        			if(redNum == 0) {
        				RR(v, py);
            			v.color = BLACK;
            			w.color = RED;
            			if(v.parent == null)
            				root = v;
        			}
        			//Lr1
        			else if(redNum == 1) {
        				//case1
        				if(w.right != null && w.right.color == RED) {
        					LL(w, v);
        					RR(w, py);
                			v.left.color = BLACK;
                			if(w.parent == null)
                				root = w;
        				}
        				//case2
        				else if(w.left != null && w.left.color == RED) {
        					Node x = w.left;
        					if(py.parent != null && py.parent.left == py)
                				py.parent.left = x;
                			else if (py.parent != null && py.parent.right == py)
                				py.parent.right = x;
                			x.parent = py.parent;
                			v.parent = x;
                			py.parent = x;
                			w.left = x.right;
                			py.right = x.left;
                			if(x.right != null) {
                				x.right.parent = w;
                			}
                			if(x.left != null) {
                				x.left.parent = py;
                			}
                			x.right = v;
                			x.left = py;
                			x.color = BLACK;
                			if(x.parent == null)
                				root = x;
        				}
        			}
        			//Lr2
        			else if(redNum == 2) {
        				Node x = w.left;
    					if(py.parent != null && py.parent.left == py)
            				py.parent.left = x;
            			else if (py.parent != null && py.parent.right == py)
            				py.parent.right = x;
            			x.parent = py.parent;
            			v.parent = x;
            			py.parent = x;
            			w.left = x.right;
            			py.right = x.left;
            			if(x.right != null) {
            				x.right.parent = w;
            			}
            			if(x.left != null) {
            				x.left.parent = py;
            			}
            			x.right = v;
            			x.left = py;
            			x.color = BLACK;
            			if(x.parent == null)
            				root = x;
        			}
        		}

        	}
    	}
    	if(node.parent != null && node.isnull == true) {
			if(node == node.parent.left)
				node.parent.left = null;
			else
				node.parent.right = null;
    	}
    }

    public void LL(Node pp, Node gp) {
    	//Perform a LL rotation.
		if(gp.parent != null && gp.parent.left == gp)
			gp.parent.left = pp;
		else if (gp.parent != null && gp.parent.right == gp)
			gp.parent.right = pp;
		else
			this.root = pp;
		pp.parent = gp.parent;
		gp.parent = pp;
		gp.left = pp.right;
		if(pp.right != null) {
			pp.right.parent = gp;
		}
		pp.right = gp;
    }

    public void RR(Node pp, Node gp) {
    	//Perform a RR rotation.
		if(gp.parent != null && gp.parent.left == gp)
			gp.parent.left = pp;
		else if (gp.parent != null && gp.parent.right == gp)
			gp.parent.right = pp;
		else
			this.root = pp;
		pp.parent = gp.parent;
		gp.parent = pp;
		gp.right = pp.left;
		if(pp.left != null) {
			pp.left.parent = gp;
		}
		pp.left = gp;
    }
    
	public String midOrder(Node node, int low, int high) {
		//Perform a middle order traversal in a certain range.
		Stack<Node> stack = new Stack<Node>();
		String str = null;
		do {
			if(node.triplet.buildingNum > high)
				node = node.left;
			else if(node.triplet.buildingNum < low)
				node = node.right;
		}while(node.triplet.buildingNum > high || node.triplet.buildingNum < low);
		while((node != null) || !stack.isEmpty())
		{
			while(node != null && node.triplet.buildingNum >= low )
			{
				if(node.triplet.buildingNum < low)
					node = node.right;
				else {
					stack.push(node);
					node = node.left;
				}
			}
			if(!stack.isEmpty())
			{
				node = stack.pop();
				if(node.triplet.buildingNum > high)
					break;
				else
					str += ",("+node.triplet.buildingNum+","+node.triplet.executed_time+","+node.triplet.total_time+")";
				node = node.right;
			}
		}
		return str;
	}
}
