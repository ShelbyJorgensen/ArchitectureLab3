/*
 * Author: Shelby Jorgensen
 * Created for CS312 Computer Architecture
 */

package LabNumber3;

import java.util.Scanner;

public class LabNumber3 {

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		System.out.println("Main memory respresentation is A x 2^E");
		
		// Prompt user to provide input for A and exponent values
		System.out.print("Value A: ");
		int A = Integer.parseInt(input.nextLine());
		System.out.print("Exponent E: ");
		int E = Integer.parseInt(input.nextLine());
		
		// Calculate the main memory value in bytes, and display results
		int mainMemory = (int) (A * Math.pow(2, E));
		System.out.println("Main memory: " + A + " x 2^" + E + " = " + mainMemory + " bytes");
		
		// Prompt user to enter cache block, bytes in blocks, and k-set values
		System.out.print("Cache blocks: ");
		int blockNumber = Integer.parseInt(input.nextLine());
		checkInputBase2("Cache Blocks", blockNumber);
		System.out.print("Bytes in Cache blocks: ");
		int bytesInBlock = Integer.parseInt(input.nextLine());
		checkInputBase2("Bytes in Cache clocks", bytesInBlock);
		System.out.print("k-set associative value: ");
		int kSet = Integer.parseInt(input.nextLine());
		checkInputBase2("k-set associative values", kSet);
		
		// Prompt the user to enter the memory address in hex
		System.out.print("Memory address in Base 16: ");
		String base16Hex = input.nextLine();
		// Try and catch block to handle memory addresses that aren't base 16, decode has built in error checking for base 16
		int base16 = 0;
		try {
			base16 = Integer.decode(base16Hex);
		} catch (Exception e) {
			System.out.println("Memory address is not in Base 16. Program terminated.");
			System.exit(0);
		}
		
		// Check memory size, to ensure hex value provided isn't larger than main memory
		checkMemorySize(base16, mainMemory);
		String binaryAddress = Integer.toBinaryString(base16);
		binaryAddress = checkBinaryString(convertToPowerTwo(mainMemory), binaryAddress);
		System.out.println("Address in binary: " + binaryAddress + "\n");
		
		// Convert the provided block, byte and memory values to get the direct address subsections
		System.out.println("Direct Cache mapping of " + base16Hex + " address");
		int dirLine = convertToPowerTwo(blockNumber);
		int dirWord = convertToPowerTwo(bytesInBlock);
		int dirTag = convertToPowerTwo(mainMemory) - (dirLine + dirWord);
		
		// Break the binary representation of the address to get the correct mapping fields
		String dirTagBinary = binaryAddress.substring(0,dirTag);
		String dirLineBinary = binaryAddress.substring(dirTag, (dirTag + dirLine));
		String dirWordBinary = binaryAddress.substring((dirTag + dirLine), E);
		System.out.println("[TAG] " + dirTag + " : [LINE] " + dirLine + " : [WORD] " + dirWord);
		System.out.println("[TAG] " + dirTagBinary + " : [LINE] " + dirLineBinary + " : [WORD] " + dirWordBinary + "\n");
		
		// Convert the provided block, byte and memory values to get the associated address subsections
		System.out.println("Associative Cache mapping of " + base16Hex + " address");
		int assWord = convertToPowerTwo(bytesInBlock);
		int assTag = convertToPowerTwo(mainMemory) - (assWord);
		
		// Break the binary representation of the address to get the correct mapping fields
		String assTagBinary = binaryAddress.substring(0, assTag);
		String assWordBinary = binaryAddress.substring(assTag, (assTag + assWord));
		System.out.println("[TAG] " + assTag + " : [WORD] " + assWord);
		System.out.println("[TAG] " + assTagBinary + " : [WORD] " + assWordBinary + "\n");
		
		// Convert the provided block, byte and memory values to get the set-associated address subsections
		System.out.println("4-way Cache mapping of " + base16Hex + " address");
		int setSet = convertToPowerTwo(blockNumber) - convertToPowerTwo(kSet);
		int setWord = kSet;
		int setTag = convertToPowerTwo(mainMemory) - (setSet + setWord);
		
		// Break the binary representation of the address to get the correct mapping fields
		String setTagBinary = binaryAddress.substring(0, setTag);
		String setSetBinary = binaryAddress.substring(setTag, (setTag + setSet));
		String setWordBinary = binaryAddress.substring((setTag + setSet), E);
		System.out.println("[TAG] " + setTag + " : [SET] " + setSet + " : [WORD] " + setWord);
		System.out.println("[TAG] " + setTagBinary + " : [SET] " + setSetBinary + " : [WORD] " + setWordBinary + "\n");
		
	}
	
	/*
	 * Method to turn any provided int into the base 2 representation
	 */
	public static int convertToPowerTwo(int convNum) {
		int count = 1;
		while(convNum != 2) {
			convNum = convNum / 2;
			count++;
		}
		return count;
	}
	
	/*
	 * Method to add leading zeros onto binary strings, ensuring string size is equal to main memory in base 2
	 */
	public static String checkBinaryString(int checkNum, String binStr) {
		StringBuilder strBin = new StringBuilder(binStr);
		while(strBin.length() < checkNum) {
			strBin.insert(0, "0");
		}
		
		return strBin.toString();
	}
	
	/*
	 * Method that checks if the provided input is a base 2 number, if not program terminates
	 */
	public static void checkInputBase2(String check, int checkNum) {
		while(checkNum != 1) {
			if(checkNum % 2 != 0) {
				System.out.println(check + " is not in Base 2. Program terminated.");
				System.exit(0);
			}
			checkNum = checkNum / 2;
		}
	}
	
	/*
	 * Method that checks if the provided address fits inside the memory of the cache
	 */
	public static void checkMemorySize(int addressSize, int memorySize) {
		if(addressSize > memorySize) {
			System.out.println("Size of address is larger than main memory size. Program terminated.");
			System.exit(0);
		}
	}
}

