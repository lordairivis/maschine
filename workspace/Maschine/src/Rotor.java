// Rotor object class which defines the different rotor types and letter mappings

import java.util.Arrays;
import java.util.LinkedList;

public class Rotor {
	
	// letter mappings for each rotor and reflectors obtained from known details from captured Enigma machines
	private static final int[] TYPE_1 = { 4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9 };      // Rotor Type I: EKMFLGDQVZNTOWYHXUSPAIBRCJ
	private static final int[] TYPE_2 = { 0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4 };     // Rotor Type II: AJDKSIRUXBLHWTMCQGZNPYFVOE
	private static final int[] TYPE_3 = { 1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14 };    // Rotor Type III: BDFHJLCPRTXVZNYEIWGAKMUSQO
	private static final int[] TYPE_4 = { 4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1 };     // Rotor Type IV: ESOVPZJAYQUIRHXLNFTGKDCMWB
	private static final int[] TYPE_5 = { 21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 18, 14, 5, 4, 2, 10 };      // Rotor Type V: VZBRGITYUPSDNHLXAWMJQOFECK

	private static final int[] TYPE_B = { 24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19 };  // Reflector Type B: YRUHQSLDPXNGOKMIEBFZCWVJAT
	private static final int[] TYPE_C = { 5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11 };  // Reflector Type C: FVPJIAOYEDRZXWGCTKUQSBNMHL
	
	private final Character[] LETTERS = { 'A' ,'B' ,'C' ,'D' ,'E' ,'F' ,'G' ,'H' ,'I' ,'J' ,'K' ,'L' ,'M' ,'N' ,'O' ,'P' ,'Q' ,'R' ,'S' ,'T' ,'U' ,'V' ,'W' ,'X' ,'Y' ,'Z' };
	private static final int[][] WIRINGS = { TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5, TYPE_B, TYPE_C };
	
	private int type;  // valid values: 1-7
	private int ringSetting; // valid values: 1-26
	private LinkedList<Integer> wiring;
	private int stepCount = 0;
	private boolean stepNext = false;
	private boolean isReflector = false;
	private boolean isPlugboard = false;
	
	public int getStepCount() {
		return this.stepCount;
	}
	
	public boolean getStepNext() {
		return this.stepNext;
	}
	
	public LinkedList<Integer> getWiring() {
		return this.wiring;
	}
	
	public boolean isReflector() {
		return this.isReflector;
	}
	
	public boolean isPlugboard() {
		return this.isPlugboard;
	}
	
	private void setType(int type) {
		this.type = type;
	}
	
	private void setRingSetting(int ringSetting) {
		this.ringSetting = ringSetting;
	}
	
	private void setWiring(int type) {
		this.wiring = new LinkedList<Integer>();
		//for (int letter : WIRINGS[type - 1]) {
		for (int letter : WIRINGS[type]) {
			this.wiring.add(letter);
		}
	}
	
	private void setWiring(Integer[] wiring) {
		this.wiring = new LinkedList<Integer>();
		for (int letter : wiring) {
			this.wiring.add(letter);
		}
	}
	
	public void resetStepNext() {
		this.stepNext = false;
	}
	
	/**
	 * Constructor for the Rotor object
	 * @param type Selected type of rotor
	 * @param ringSetting selected starting ring setting
	 */
	public Rotor(int type, int ringSetting) {
		this.setType(type - 1);
		this.setWiring(type - 1);
		this.setRingSetting(ringSetting);
		this.align();
	}
	
	/**
	 * Alternate constructor for creating reflector-type rotors
	 * @param type Selected type of reflector
	 */
	public Rotor(char type) {
		if (Character.toLowerCase(type) == 'b') {
			this.setType(5);
			this.setWiring(5);
		}
		else if (Character.toLowerCase(type) == 'c') {
			this.setType(6);
			this.setWiring(6);
		}
		this.ringSetting = -1;
		this.stepCount = -1;
		this.isReflector = true;
	}
	
	/**
	 * Alternate constructor for creating plugboard-type rotors
	 * @param connections Selected plugboard connections
	 */
	public Rotor(String connections) {
		String[] selections = connections.split(",");
		if (!isMatch(selections)) {
			Integer[] wiring = this.createConnections(selections);
			this.setWiring(wiring);
		}
		this.ringSetting = -1;
		this.stepCount = -1;
		this.isPlugboard = true;
	}
	
	/**
	 * Creates a custom wiring layout based on the plugboard selections
	 * @param selections Selections provided at runtime
	 * @return Integer[] array of paired indexes
	 */
	private Integer[] createConnections(String[] selections) {
		for (int i = 0; i < selections.length; i++) {
			selections[i] = selections[i].toUpperCase();
		}
		LinkedList<Integer> connections = new LinkedList<Integer>(Arrays.asList(new Integer[26]));
		for (int i = 0; i < connections.size(); i++) {
			connections.set(i, -1);
		}
		for (int i = 0; i < selections.length; i++) {
			// connections should be provided in pairs of letters, i.e. AW, KV, LO, etc.
			// set the character at the index of the first letter to a number representing the character at the index of the second letter according to the LETTER map
			// set the character at the index of the second letter to a number representing the character at the index of the first letter according to the LETTER map
			connections.set(Arrays.asList(this.LETTERS).indexOf(selections[i].charAt(0)), Arrays.asList(this.LETTERS).indexOf(selections[i].charAt(1)));
			connections.set(Arrays.asList(this.LETTERS).indexOf(selections[i].charAt(1)), Arrays.asList(this.LETTERS).indexOf(selections[i].charAt(0)));
		}
		return connections.toArray(new Integer[connections.size()]);
	}
	
	/**
	 * Method to step the rotor forward and flag if the next rotor should turn over
	 */
	public void step() {
		int head = this.wiring.pop();
		this.wiring.addLast(head);
		this.stepCount++;
		if (this.stepCount > 26) {
			this.stepCount = 0;
			this.stepNext = true;
		}
	}
	
	/**
	 * Method to align the starting position of the rotor with the ring setting
	 */
	private void align() {
		int head;
		for (int i = 0; i < this.ringSetting; i++) {
			head = this.wiring.pop();
			this.wiring.addLast(head);
		}
	}
	
	/**
	 * Determine if any plugboard patches match any other plugboard patches
	 * @param pairs String array of letter pairs selected by plugboard patches
	 * @return Boolean value of whether there are any letter overlaps
	 */
	private static boolean isMatch(String[] pairs) {
		for (String pair1 : pairs) {
			if (pair1.charAt(0) == pair1.charAt(1)) return true;
			for (String pair2 : pairs) {
				if (pair2.equals(pair1)) continue;
				else if (isMatch(pair1, pair2)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Compare a patch to a second patch
	 * @param pair1 First letter pair
	 * @param pair2 Second letter pair
	 * @return Boolean value whether the letter pairs match or overlap
	 */
	private static boolean isMatch(String pair1, String pair2) {
		if (pair1.charAt(0) == pair2.charAt(0)) return true;
		else if (pair1.charAt(1) == pair2.charAt(0)) return true;
		else if (pair1.charAt(0) == pair2.charAt(1)) return true;
		else if (pair1.charAt(1) == pair2.charAt(1)) return true;
		else return false;
	}
	
	/**
	 * Method to convert standard numbers to Roman numerals
	 * @param type Rotor number
	 * @return Rotor number as a Roman numeral
	 */
	private String asRoman(int type) {
		if (type == 0) return "I";
		else if (type == 1) return "II";
		else if (type == 2) return "III";
		else if (type == 3) return "IV";
		else if (type == 4) return "V";
		else return "";
	}
	
	public String toString() {
		return (!this.isReflector ? asRoman(type) : (type > 4 && type == 5 ? "B" : "C")); // Returns a Roman numeral if rotor is type 0-4, or B/C if type is > 4
	}
	
	public String toLongString() {
		return (!this.isReflector ? asRoman(type) + " " + Integer.toString(ringSetting) + "( +" + Integer.toString(stepCount) + " steps)" : "Type " + this.toString());
	}

}
