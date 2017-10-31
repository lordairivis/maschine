// An attempt at recreating the Enigma machine for encrypting and decrypting text.
// CLI application taking switches for rotor selection and order, ring positions, plugboard connections, and starting rotor positions
// Initially designed to emulate the Wehrmacht and Kriegsmarine Enigma machines
// Information gathered from http://users.telenet.be/d.rijmenants/en/enigmatech.htm & https://en.wikipedia.org/wiki/Enigma_machine

import java.util.LinkedList;

public class Maschine {
	
	public static void main(String[] args) {
		try {
			int[] rots = rotors(args[0].toString());
			int[] ringPosition = rotorPositions(args[1].toString());
			LinkedList<Rotor> rotors = new LinkedList<Rotor>();
			for (int i = 0; i < 3; i++) {
				rotors.add(new Rotor(rots[i], ringPosition[i]));
			}
			
			String[] plugboard = plugboard(args[2]);
			
			String ref = args[3].toString();
			Rotor reflector;
			try {
				reflector = new Rotor(ref.charAt(0));
			}
			catch (IndexOutOfBoundsException ex) {
				reflector = new Rotor('b');
			}
			
			Enigma maschine = new Enigma(reflector, rotors, plugboard);
			
			System.out.println(maschine.translate(args[4]));
			
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.toString());
		}
	}
	
	/**
	 * Method to determine rotor selections
	 * @param s User provided configuration string of rotor selections
	 * @return Integer array of rotor types and order
	 */
	private static int[] rotors(String s) {
		String[] sels = s.split(",");
		for(int i = 0; i < sels.length; i++) {
			try {
				if (Integer.parseInt(sels[i]) < 1 || Integer.parseInt(sels[i]) > 5) {
					sels[i] = Integer.toString(1);
				}
			}
			catch (NumberFormatException ex) {
				sels[i] = Integer.toString(1);
			}
		}
		int rotor1 = Integer.parseInt(sels[0]);
		int rotor2 = Integer.parseInt(sels[1]);
		int rotor3 = Integer.parseInt(sels[2]);
		if (rotor1 == rotor2 || rotor2 == rotor3 || rotor1 == rotor3) return new int[] { 1, 2, 3 };
		return new int[] { rotor1, rotor2, rotor3 };
	}
	
	/**
	 * Method to parse the starting rotor positions
	 * @param s String provided at runtime
	 * @return Integer array of the starting rotor positions
	 */
	private static int[] rotorPositions(String s) {
		String[] sels = s.split(",");
		for(int i = 0; i < sels.length; i++) {
			try {
				if (Integer.parseInt(sels[i]) < 1 || Integer.parseInt(sels[i]) > 26) {
					sels[i] = "1";
				}
			}
			catch (NumberFormatException ex) {
				sels[i] = "1";
			}
		}
		int pos1 = Integer.parseInt(sels[0]);
		int pos2 = Integer.parseInt(sels[1]);
		int pos3 = Integer.parseInt(sels[2]);
		return new int[] { pos1, pos2, pos3 };
	}
	
	/**
	 * Method to determine plugboard configuration
	 * @param s User provided configuration string of plugboard selections
	 * @return String array of plugboard configuration
	 */
	private static String[] plugboard(String s) {		
		String[] sels = s.split(",");
		if (sels.length > 13) {
			return new String[13];
		}
		if (isMatch(sels)) {
			return new String[13];
		}
		else return sels;
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
}
