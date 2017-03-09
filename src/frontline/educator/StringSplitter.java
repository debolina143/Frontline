package frontline.educator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringSplitter {
	/*
	 * Convert the string:
	 * "(id,created,employee(id,firstname,employeeType(id), lastname),location)"
	 * to the following output
	 * 
	 * id created employee - id - firstname - employeeType -- id - lastname
	 * location
	 */
	/*
	 * then sort created employee - employeeType -- id - firstname - id -
	 * lastname id location
	 */
	static Map<Integer, List<String>> connections = new HashMap<Integer, List<String>>();
	public static String sample = "(id,created,employee(id,firstname,employeeType(id), lastname),location)";

	public static void main(String[] args) {

		sample = sample.replaceAll(" ", "");
		Node node = new Node();
		List<Node> allNodes = new ArrayList<Node>();
		StringBuffer sampleBuffer = new StringBuffer();
		String[] stringArray = sample.split("(?<!^)(?=[,()])");
		int hyphenCount = -1;
		List<String> values = new ArrayList<String>();
		String hyphen = "";
		String finalString = "";
		int count = 0;
		values.add("");
		connections.put(-1, values);

		for (String item : stringArray) {
			if (item.startsWith("(")) {
				hyphenCount++;
				hyphen = updateHyphenString(hyphenCount);
			} else if (item.endsWith(")")) {
				hyphenCount--;
				hyphen = updateHyphenString(hyphenCount);
			}
			String cleanSampleString = item.replaceAll("[(),]", "");
			finalString = hyphen + cleanSampleString;

			if (!finalString.equals("-") && !finalString.equals(")") && !finalString.equals("")) {
				System.out.println(finalString);
				node = new Node();
				node.setWord(cleanSampleString);
				List<String> tempValueString = connections.get(hyphenCount);
				if (tempValueString == null) {
					tempValueString = new ArrayList<String>();
				}
				tempValueString.add(cleanSampleString);
				connections.put(hyphenCount, tempValueString);
				node.setDepth(hyphenCount);
				sampleBuffer.append(finalString).append(" ");
				allNodes.add(count, node);
				count++;
			}
		}
		printSortedString(allNodes);
	}

	private static void printSortedString(List<Node> allNodes) {
		System.out.println("\nBonus question\n");
		writeInOrderTree(0, allNodes);
	}

	private static String updateHyphenString(int iterations) {
		String hyphenString = "";
		for (int count = 0; count < iterations; count++) {
			hyphenString += "-";
		}
		return hyphenString;
	}

	private static void writeInOrderTree(int depth, List<Node> nodes) {
		// find all elements at that depth and sort it and create a list of sorted nodes
		List<Node> eachLevelWords = sortBy(nodes, depth);
		for (Node w : eachLevelWords) {
			System.out.println(updateHyphenString(w.getDepth()) + w.getWord());
			if (connections.containsKey(depth)) {
				for (String values : connections.get(depth)) {
					if (values.equals(w.getWord())) {
						writeInOrderTree(++depth, nodes);
					}
				}
			}
		}
	}

	private static List<Node> sortBy(List<Node> words, int depth) {
		List<Node> nodesAtDepth = new ArrayList<Node>();
		for (Node w : words) {
			if (w.getDepth() == depth) {
				nodesAtDepth.add(w);
			}
		}
		// sort the nodes at one depth and return based on value
		Collections.sort(nodesAtDepth, new Comparator<Node>() {
			public int compare(Node o1, Node o2) {
				return ((Node) o1).getWord().compareTo(((Node) o2).getWord());
			}
		});
		return nodesAtDepth;
	}
}
