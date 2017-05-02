import java.util.*;


public class Tree {
	
	public static class TreeNode{
		private int val;
		private TreeNode left;
		private TreeNode right;
		
		public TreeNode(int val){
			this.val = val;
			this.left = this.right = null;
		}
	}
	
	public static void main(String[] args){
		TreeNode root = new TreeNode(1);
		System.out.println(1);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		System.out.println(2+" "+3);
		root.left.left = new TreeNode(4);
		root.left.right = new TreeNode(5);
		System.out.println(4 + " " + 5);
		root.left.right.left = new TreeNode(6);
		root.right.right =  new TreeNode(0);
		root.left.right.right = new TreeNode(7);
		root.right.right.left = new TreeNode(8);
		root.right.right.right = new TreeNode(9);
		LinkedList<TreeNode> result1 = inOrderTraversal(root);
		System.out.print("In order Tree Traversal: ");
		for(TreeNode node: result1){
			System.out.print(node.val+" ");
		}
		System.out.println("!");

		
		LinkedList<TreeNode> result2 = preOrderTraversal(root);
		System.out.print("Pre order Tree Traversal: ");
		for(TreeNode node: result2){
			System.out.print(node.val+" ");
		}
		System.out.println("!");
		
		LinkedList<TreeNode> result3 = postOrderTraversal(root);
		System.out.print("Post order Tree Traversal: ");
		for(TreeNode node: result3){
			System.out.print(node.val+" ");
		}
		System.out.println("!");

	}


	public static LinkedList<TreeNode>  inOrderTraversal(TreeNode root){
		LinkedList<TreeNode>  result = new LinkedList<TreeNode>();
		if(root == null) return result;

		Stack<TreeNode> frontier = new Stack<TreeNode>();
		while(!frontier.isEmpty() || root != null){
			if(root != null){
				frontier.push(root);
				root = root.left;
			}
			else{
				root = frontier.pop();
				result.add(root);
				root = root.right;
			}
		}
		return result;
	}				
	
	public static LinkedList<TreeNode> preOrderTraversal(TreeNode root){
		LinkedList<TreeNode> result = new LinkedList<TreeNode>();
		if(root == null) return result;
		Stack<TreeNode> frontier = new Stack<TreeNode>();
		while(!frontier.empty() || root != null){
			if(root != null){
				result.add(root);
				frontier.push(root);
				root = root.left;
			}
			else{
				root = frontier.pop();
				root = root.right;
			}
		}
		return result;
		
	}
	
	public static LinkedList<TreeNode> postOrderTraversal(TreeNode root){
		LinkedList<TreeNode> result = new LinkedList<TreeNode>();
		if(root == null) return result;
		Stack<TreeNode> frontier = new Stack<TreeNode>();
		TreeNode lastVisited = null;
		
		while(!frontier.isEmpty() || root != null){
			if(root != null){
				frontier.push(root);
				root = root.left;
			}
			else{
				TreeNode peekNode = frontier.peek();
				if(peekNode.right != null && peekNode.right != lastVisited){
					root = peekNode.right;
				}
				else{
					result.add(peekNode);
					lastVisited = frontier.pop();
				}
			}
		}
		return result;
	}
	
	
	//public static void Tree commonAn(Tree a, Tree b){
	//	if(a == null || b == null) {
			
		//}
		
	//}


}
