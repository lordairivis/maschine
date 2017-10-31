// Enigma object class to create an enigma machine using specified settings for encrypting and decrypting strings

import java.util.Arrays;
import java.util.LinkedList;

public class Enigma {
	
	// letter map for translating the "electrical signal" sent through the rotors to a letter.
	private static final char[] LETTERS = { 'A' ,'B' ,'C' ,'D' ,'E' ,'F' ,'G' ,'H' ,'I' ,'J' ,'K' ,'L' ,'M' ,'N' ,'O' ,'P' ,'Q' ,'R' ,'S' ,'T' ,'U' ,'V' ,'W' ,'X' ,'Y' ,'Z' };
	
	Rotor reflector;
	LinkedList<Rotor> rotors;
	String[] plugboard;
	
	public Rotor getReflector() {
		return this.reflector;
	}
	
	public LinkedList<Rotor> getRotors() {
		return this.rotors;
	}
	
	public String[] getPlugboard() {
		return this.plugboard;
	}
	
	private void setReflector(Rotor reflector) {
		this.reflector = reflector;
	}
	
	private void setRotors(LinkedList<Rotor> rotors) {
		this.rotors = rotors;
	}
	
	private void setPlugboard(String[] plugboard) {
		this.plugboard = plugboard;
	}
	
	/**
	 * Constructor for an Enigma machine
	 * @param reflector Rotor object which does not rotate  
	 * @param rotors Set of three Rotor objects as defined by user at runtime
	 * @param plugboard Optional user-defined additional Rotor
	 */
	public Enigma(Rotor reflector, LinkedList<Rotor> rotors, String[] plugboard) {
		setReflector(reflector);
		setRotors(rotors);
		setPlugboard(plugboard);
	}

	/**
	 * Encrypt/decrypt a message
	 * @param message String value representing the message to encrypt or decrypt
	 * @return Encrypted/decrypted message
	 */
	public String translate(String message) {
		String output = new String();
		String input = message.replaceAll("[^a-zA-Z]", "").toUpperCase();
		int index;
		LinkedList<Integer> wiring;
		for (Character c : input.toCharArray()) {
			// TODO: make letter swaps according to plugboard
			
			index = Arrays.asList(LETTERS).indexOf(c);
			this.stepRotors();
			for (Rotor rot : this.rotors) {
				wiring = rot.getWiring();
				index = wiring.indexOf(index);
			}
			wiring = this.reflector.getWiring();
			index = wiring.indexOf(index);
			for (int i = 2; i >= 0; i--) {
				wiring = rotors.get(i).getWiring();
				index = wiring.indexOf(index);
			}
			index = wiring.get(index);
			output += Arrays.asList(LETTERS).get(index);
		}
		output = this.addSpaces(output);

		return output;
	}
	
	/**
	 * Method to step the rotors and pass rollovers on to the next rotor
	 */
	// Rotors step like an odometer. Every 26 rotations of the first rotor roll over to the next rotor and so on.
	private void stepRotors() {
		rotors.element().step();
		if (rotors.get(0).getStepNext()) { 
			rotors.get(1).step();
			rotors.get(0).resetStepNext();
		}
		if (rotors.get(1).getStepNext()) {
			rotors.get(2).step();
			rotors.get(1).resetStepNext();
		}
	}
	
	/**
	 * Adds spaces every fourth character to a string
	 * @param input String to modify
	 * @return String with spaces inserted every fourth character
	 */
	// Properly formatted Enigma messages are groups of 4 letters separated by whitespace
	private String addSpaces(String input) {
		String output = new String();
		int length = input.length() + (int) Math.floor(input.length() / 4);
		for (int i = 0; i < length; i++) {
			if (i % 4 == 0) {
				output += ' '; 
				i++;
			}
			output += input.charAt(i);
		}
		return output;
	}
	
	public String toString() {
		String rotors = new String();
		for (Rotor r : this.rotors) {
			rotors += r.toString() + " ";
		}
		return "Rotors: " + rotors + ", Reflector: " + this.reflector.toString() + ((plugboard != null) ? " Plugboard settings" : "");
	}
	
}
