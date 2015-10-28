import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {

	private Node parent;
	private int heur;
	private int depth;
	private static final long serialVersionUID = 1L;
	private List<List<Character>> listofList = new ArrayList<List<Character>>();

	public List<Node> successors() {

		List<Node> states = new ArrayList<Node>();
		for (int i = 0; i < BlocksWorld.noOfStacks; i++) {
			for (int j = 0; j < BlocksWorld.noOfStacks; j++) {
				if (i == j) {
					continue;
				}
				List<List<Character>> list = new ArrayList<List<Character>>();
				Node copiedNode = new Node();
				try {
					copiedNode = copy();
				} catch (Exception e1) {
					System.out.println("Copy Failed : " + e1.getMessage());
					System.exit(1);
				}
				list = copiedNode.getListofList();
				List<Character> listI = list.get(i);
				if (!listI.isEmpty()) {
					Character c = listI.get(listI.size() - 1);
					listI.remove(c);
					list.get(j).add(c);
					copiedNode.setListofList(list);
					copiedNode.setParent(this);
					copiedNode.setDepth(this.getDepth() + 1);
					states.add(copiedNode);
				}
			}
		}
		return states;
	}

	public String getState() {
		String state = new String();
		int i = 0;
		for (List<Character> list : getListofList()) {
			state += i;
			for (Character c : list) {
				state += c;
			}
			i++;
		}
		return state;
	}

	public void calculateSimpleHeuristic(Node goalNode) {

		List<Character> goalList = goalNode.getListofList().get(0);
		int heur = 0;
		List<Character> heurList = new ArrayList<Character>();
		int count = goalNode.getListofList().get(0).size();
		List<Character> list = getListofList().get(0);
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if (goalList.get(i).equals(list.get(i))) {
					heurList.add(goalList.get(i));
					count--;
				} else {
					break;
				}
			}
		}
		heur = count + getDepth();
		setHeur(heur);
	}

	public void calculateHeuristic(Node goalNode) {

		List<Character> goalList = goalNode.getListofList().get(0);
		int heur = 0;
		List<Character> heurList = new ArrayList<Character>();
		int blocksOutOfPlace = goalNode.getListofList().get(0).size();
		List<Character> list = getListofList().get(0);
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if (goalList.get(i).equals(list.get(i))) {
					heurList.add(goalList.get(i));
					blocksOutOfPlace--;
				} else {
					break;
				}
			}
		}

		List<List<Character>> listOfLists = getListofList();
		for (List<Character> singleList : listOfLists) {
			if (singleList.isEmpty()) {
				continue;
			}
			for (Character c : singleList) {
				if (heurList.contains(c)) {
					continue;
				}
				int stepsToGetOutOfStack = singleList.size() - singleList.indexOf(c);
				int positionInGoalNode = goalList.indexOf(c);
				int stepsToPutItInCorrectPosn = 0;
				if (listOfLists.get(0) != null && listOfLists.get(0).size() > positionInGoalNode) {
					stepsToPutItInCorrectPosn = listOfLists.get(0).size() - positionInGoalNode + blocksOutOfPlace;
				}
				heur += stepsToGetOutOfStack + stepsToPutItInCorrectPosn;
			}
		}
		heur += heur + getDepth();
		setHeur(heur);
	}

	public static void traceback(Node node) {

		if (node != null) {
			BlocksWorld.solutionPath.push(node);
			traceback(node.getParent());
		}
	}

	public Node copy() throws Exception {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(this);
		out.flush();
		out.close();
		byte[] bytes = bos.toByteArray();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
		return (Node) ois.readObject();

	}

	public List<List<Character>> getListofList() {
		return listofList;
	}

	public void setListofList(List<List<Character>> listofList) {
		this.listofList = listofList;
	}

	public int getHeur() {
		return heur;
	}

	public void setHeur(int heur) {
		this.heur = heur;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int dist) {
		this.depth = dist;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
}
