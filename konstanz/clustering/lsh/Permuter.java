package de.uni.konstanz.clustering.lsh;

import java.util.Random;

import de.uni.konstanz.utils.MathUtils;

/**
 * This class represents a permuter that permutes the indices of a string signature.
 * @author rockyrock
 *
 */
public class Permuter {
	int a;
	int b;
	//The prime number should be the same as the length of the signature which 
	//should also be a prime
	int p;
	Random random;
	
	public static void main(String[] args) throws Exception {
		Permuter p = new Permuter(7);
		System.out.println( p.permute("lengths") );
		
	}
	
	public Permuter(int signatureLength) throws Exception {
		
		if ( !MathUtils.isPrime(signatureLength) ) 
			throw new Exception( "The signature length should be a prime number." );
		
		random = new Random();
		a = 1 + random.nextInt( (signatureLength-1) );
		b = 1 + random.nextInt( (signatureLength-1) );
		p = signatureLength;
	}
	
	/**
	 * Permutes the string signature.
	 * @param signature
	 * @return Permuted string signature.
	 * @throws Exception
	 */
	public String permute(String signature) throws Exception {
		if ( signature.length() != p )
			throw new Exception( "The signature length should match" +
					" the permuter's declared prime number" );
		
		String s = "";
		int[] permutedIndices = new int[ signature.length() ];
		char[] newSig = new char[signature.length()];
		
		for ( int i = 0; i < signature.length(); i++ ) {
			permutedIndices[i] = getPermutedIndex(i); 
		}
		
		for ( int i = 0; i < permutedIndices.length; i++ ) {
			s += signature.charAt( permutedIndices[i] );
		}
		return s;
	}
	
	private int getPermutedIndex( int index ) {
		int pIndex = ( (a * index) + b ) % p;
		return pIndex;
	}
	
	public int test(int x) {
		Random random = new Random();
		a = random.nextInt(100);
		b = random.nextInt(100);
		p = 103;
		int pIndex = ( (a * x) + b ) % p;
		return pIndex;
	}
	
	
	
}
