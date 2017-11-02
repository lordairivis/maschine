// Enigma object class to create an enigma machine using specified settings for encrypting and decrypting strings

import java.util.Arrays;
import java.util.LinkedList;

public class Enigma {
	
	// letter map for translating the "electrical signal" sent through the rotors to a letter.
	private final Character[] LETTERS = { 'A' ,'B' ,'C' ,'D' ,'E' ,'F' ,'G' ,'H' ,'I' ,'J' ,'K' ,'L' ,'M' ,'N' ,'O' ,'P' ,'Q' ,'R' ,'S' ,'T' ,'U' ,'V' ,'W' ,'X' ,'Y' ,'Z' };
	
	Rotor reflector;
	LinkedList<Rotor> rotors;
	Rotor plugboard;
	boolean usePlugboard;
	
	public Rotor getReflector() {
		return this.reflector;
	}
	
	public LinkedList<Rotor> getRotors() {
		return this.rotors;
	}
	
	public Rotor getPlugboard() {
		return this.plugboard;
	}
	
	private void setReflector(Rotor reflector) {
		this.reflector = reflector;
	}
	
	private void setRotors(LinkedList<Rotor> rotors) {
		this.rotors = rotors;
	}
	
	private void setPlugboard(Rotor plugboard) {
		this.plugboard = plugboard;
	}
	
	/**
	 * Constructor for an Enigma machine
	 * @param reflector Rotor object which does not step  
	 * @param rotors Set of three Rotor objects as defined by user at runtime
	 * @param plugboard User-defined additional Rotor
	 */
	public Enigma(Rotor reflector, LinkedList<Rotor> rotors, Rotor plugboard) {
		setReflector(reflector);
		setRotors(rotors);
		setPlugboard(plugboard);
		this.usePlugboard = true;
	}
	
	/**
	 * OVerload constructor for an Enigma machine
	 * @param reflector Rotor object which does not step
	 * @param rotors Set of three Rotor objects as defined by user at runtime
	 */
	public Enigma(Rotor reflector, LinkedList<Rotor> rotors) {
		setReflector(reflector);
		setRotors(rotors);
		this.usePlugboard = false;
	}

	/**
	 * Encrypt/decrypt a message
	 * @param message String value representing the message to encrypt or decrypt
	 * @return Encrypted/decrypted message
	 */
	public String translate(String message) {
		LinkedList<Character> output = new LinkedList<Character>();
		String input = message.replaceAll("[^a-zA-Z]", "").toUpperCase();
		int index;
		LinkedList<Integer> wiring;
		LinkedList<Integer> plugWiring = new LinkedList<Integer>();
		if (this.usePlugboard) {
			plugWiring = this.plugboard.getWiring();
		}
		for (Character c : input.toCharArray()) {
			
			if (this.usePlugboard && plugWiring.indexOf(Arrays.asList(this.LETTERS).indexOf(c)) != -1) {
				c = this.LETTERS[plugWiring.indexOf(Arrays.asList(this.LETTERS).indexOf(c))];
			}
			
			index = Arrays.asList(this.LETTERS).indexOf(c);
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

			if (this.usePlugboard && plugWiring.get(index) != -1) {
				index = plugWiring.get(index);
			}
			
			output.add(Arrays.asList(this.LETTERS).get(index));
		}
		//output = this.addSpaces(output);

		return this.addSpaces(output);
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
	 * @param message String to modify
	 * @return String with spaces inserted every fourth character
	 */
	// Properly formatted Enigma messages are groups of 4 letters separated by whitespace
	private String addSpaces(LinkedList<Character> message) {
		String output = new String();
		int length = message.size();
		for (int i = 0; i < length; i++) {
			if (i > 0 && i % 4 == 0) {
				output += ' ';
			}
			output += message.pop();
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
