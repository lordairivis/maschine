# Maschine
### A very rudimentary simulation of the Enigma machine cypher written in Java

An attempt at recreating the Enigma machine for encrypting and decrypting text.

CLI application taking switches for rotor selection and order, ring positions, plugboard connections, and starting rotor positions.

Initially designed to emulate the Wehrmacht and Kriegsmarine Enigma machines.

Information gathered from http://users.telenet.be/d.rijmenants/en/enigmatech.htm & https://en.wikipedia.org/wiki/Enigma_machine

### Usage
`java maschine.java [comma-delimited rotor selection] [comma-delimited initial rotor positions] [reflector selection option] "string you wish to encrypt/decrypt" [optional comma-delimited list of letter swaps for plugboard]`

Rotor selections are made by choosing any three rotors, numbered 1 through 5, and delimited by commas.

Initial rotor starting positions are indicated by passing a comma-delimited list of three numbers 1 through 26.

Reflector selection options are 'B' or 'C'.

Plugboard connections are chosen by a comma-delimited listed of letter pairs, such as 'KW'. In this example, any time the message string encounters a K, it will be swapped to a W before the encryption/decryption process occurs.

Currently not in a proper working state.
