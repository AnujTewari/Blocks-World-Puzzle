import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

public class BlocksWorld {

	public static int noOfBlocks;
	public static int noOfStacks;
	public static int iterations = 0;
	public static int max_queue_size = 0;
	public static int solutionSteps = 0;
	public static Node goalNode;
	public static Node startNode;
	public static Stack<Node> solutionPath = new Stack<Node>();

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Invalid arguments");
			System.out.println("Please run as \"BlocksWorld <no_of_blocks> <no_of_stacks> \" ");
			System.exit(1);
		}

		BlocksWorld bw = new BlocksWorld();
		noOfBlocks = Integer.parseInt(args[0]);
		noOfStacks = Integer.parseInt(args[1]);
		List<List<Character>> listofList = bw.problemGenerator(noOfBlocks, noOfStacks);
		startNode = new Node();
		startNode.setListofList(listofList);
		System.out.println("Original State :");
		int stackNo = 1;
		for (List<Character> list : startNode.getListofList()) {
			System.out.print(stackNo++ + " | ");
			for (Character c : list) {
				System.out.print(c + " ");
			}
			System.out.println();
		}
		bw.playGame(startNode, goalNode);
	}

	public void playGame(Node startNode, Node goalNode) {

		PriorityQueue<Node> queue = new PriorityQueue<Node>(11, new Compare());
		Map<String, Node> visitedStates = new HashMap<String, Node>();
		startNode.setDepth(0);
		startNode.setParent(null);
		startNode.calculateHeuristic(goalNode);
		// startNode.calculateSimpleHeuristic(goalNode);
		visitedStates.put(startNode.getState(), startNode);
		queue.add(startNode);
		while (!queue.isEmpty()) {
			iterations++;
			if (iterations >= 15000) {
				System.out.println("Sorry! This problem is not solvable.");
				System.exit(1);
			}
			max_queue_size = max_queue_size < queue.size() ? queue.size() : max_queue_size;
			Node node = queue.remove();
			System.out.println("iter=" + iterations + ", f=g+h=" + node.getHeur() + ", depth =" + node.getDepth());
			if (node.getListofList().get(0).equals(goalNode.getListofList().get(0))) {
				System.out.println("Success! depth=" + node.getDepth() + ", total_goal_tests=" + iterations
						+ ", max_queue_size=" + max_queue_size);

				Node.traceback(node);
				System.out.println("Solution steps : " + (solutionPath.size() - 1));
				int step = 0;

				while (!solutionPath.isEmpty()) {
					Node n = solutionPath.pop();
					System.out.println("Step " + step++ + ": ");
					int stackNo = 1;
					for (List<Character> singlelist : n.getListofList()) {
						System.out.print(stackNo++ + " | ");
						for (Character c : singlelist) {
							System.out.print(c + " ");
						}
						System.out.println();
					}
				}
				return;
			}
			List<Node> list = node.successors();
			for (Node n : list) {
				if (n.getDepth() < node.getDepth()) {
					visitedStates.remove(n.getState());
				}
				if (!visitedStates.containsKey(n.getState())) {
					n.setParent(node);
					n.setDepth(visitedStates.get(node.getState()).getDepth() + 1);
					n.calculateHeuristic(goalNode);
					// n.calculateSimpleHeuristic(goalNode);
					visitedStates.put(n.getState(), n);
					queue.add(n);
				}
			}
		}
	}

	// Mock generator to check for a known original state
	public List<List<Character>> mockProblemGenerator() {

		List<List<Character>> listofList = new ArrayList<List<Character>>();
		List<List<Character>> goalList = new ArrayList<List<Character>>();
		List<Character> singleList = new ArrayList<Character>();
		Character c = 'A';
		for (int i = 0; i < 5; i++) {
			singleList.add(c++);
		}
		goalList.add(singleList);
		goalNode = new Node();
		goalNode.setListofList(goalList);
		listofList.add(new ArrayList<Character>());
		listofList.add(new ArrayList<Character>());
		listofList.add(new ArrayList<Character>());
		listofList.add(new ArrayList<Character>());
		listofList.add(new ArrayList<Character>());
		// listofList.add(new ArrayList<Character>());
		// listofList.add(new ArrayList<Character>());
		// listofList.add(new ArrayList<Character>());
		// listofList.add(new ArrayList<Character>());
		// listofList.add(new ArrayList<Character>());

		listofList.get(4).add('A');
		listofList.get(1).add('B');
		listofList.get(0).add('C');
		listofList.get(1).add('D');
		listofList.get(3).add('E');
		// listofList.get(1).add('F');
		// listofList.get(2).add('G');
		// listofList.get(1).add('H');
		// listofList.get(0).add('I');
		// listofList.get(3).add('J');
		// listofList.get(2).add('B');
		// listofList.get(8).add('C');
		// listofList.get(9).add('G');
		// listofList.get(2).add('E');
		// listofList.get(6).add('A');
		// listofList.get(4).add('J');
		// listofList.get(6).add('K');
		// listofList.get(5).add('D');
		// listofList.get(5).add('S');
		// listofList.get(6).add('T');

		return listofList;
	}

	// to generate random generator
	public List<List<Character>> problemGenerator(int noOfBlocks, int noOfStacks) {

		if (noOfBlocks <= 0 || noOfBlocks > 26) {
			System.out.println("Invalid number of blocks. Please enter a value from 1 to 26.");
			System.exit(1);
		}
		List<List<Character>> goalList = new ArrayList<List<Character>>();
		List<Character> singleList = new ArrayList<Character>();
		Character c = 'A';
		for (int i = 0; i < noOfBlocks; i++) {
			singleList.add(c++);
		}
		goalList.add(singleList);
		goalNode = new Node();
		goalNode.setListofList(goalList);
		List<List<Character>> listofList = new ArrayList<List<Character>>();
		for (int i = 0; i < noOfStacks; i++) {
			listofList.add(new ArrayList<Character>());
		}

		c = 'A';
		Random randomInstance = new Random();
		for (int i = 0; i < noOfBlocks; i++) {
			int randomNumber = randomInstance.nextInt(noOfStacks);
			listofList.get(randomNumber).add(c++);
		}
		return listofList;
	}

	public static class Compare implements Comparator<Node> {

		public int compare(Node n1, Node n2) {
			if (n1.getHeur() < n2.getHeur()) {
				return -1;
			} else if (n1.getHeur() > n2.getHeur()) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
