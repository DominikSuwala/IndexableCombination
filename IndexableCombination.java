/**
 * File: IndexableCombination.java
 *   $Id$
 *   
 * Revisions:
 *   $Log$
 */

/**
 * @author Dominik Suwala <dxs9411@RiT.edu>
 * @date Jan 14, 2016
 * Implements string-based virtual indexing with reversible look-up.
 * Minimal memory footprint.
 */

import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;

public class IndexableCombination {
	
	static int[][] slots; /* Stores min, max for each slot */
	
	/**
	 * Traverses slots to determine number of combinations
	 * @param slots
	 * @return
	 */
	public static BigInteger computeSpace( int[][] slots ) {
		
		BigInteger sum = new BigInteger( "1" );
		for( int j = 0; j < slots.length; j++ ) {
			sum = sum.multiply( new BigInteger( "" + ( slots[ j ][ 1 ] - slots[ j ][ 0 ] + 1 ) ) );
		}
		
		return sum;
	}
	
	/**
	 * @param combination
	 */
	public static void printCombination( BigInteger[] combination ) {
		for( int i = 0; i < combination.length; i++ ) {
			// System.out.print( ( char ) ( Integer.parseInt( combination[ i ].add( new BigInteger( "" + ( slots[ i ][ 0 ] - 1 ) ) ).toString() ) ) );
			System.out.print( ( combination[ i ].add( new BigInteger( "" + ( slots[ i ][ 0 ] - 1 ) ) ).toString() ) );
			
			if( i != combination.length - 1 ) {
				System.out.print( " " );
			}
		}
		System.out.println();
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static int[] minMaxTuple( String str ) {
		
		String[] splits = str.split( "," );
		
		return new int[] {
				Integer.parseInt( splits[ 0 ] ),
				Integer.parseInt( splits[ 1 ] )
		};
	}
	
	/**
	 * @param combination
	 * @return
	 */
	public static BigInteger computeVirtualIndex( String combination ) {
		
		
		return null;
	}
	
	/**
	 * @param virtualIndex
	 * @return
	 */
	public static String computeCombination( BigInteger virtualIndex ) {
		
		
		
		return null;
	}
	
	/**
	 * Create a virtual index for a given combination
	 * @param args formatted with slots given as {(min, max),(min, max),...};
	 */
	public static void main( String[] args ) {
		final BigInteger BIG_INTEGER_ZERO = new BigInteger( "0" );
		final BigInteger BIG_INTEGER_ONE = new BigInteger( "1" );
		StringBuilder prettyBuilder = new StringBuilder();
		
		for( int i = 0; i < args.length; i++ ) {
			prettyBuilder.append( args[ i ] );
		}
		
		System.out.println( prettyBuilder.toString() );
		
		String pretty = prettyBuilder.toString();
		
		String[] splits = pretty.split( ";" );
		
		slots = new int[ splits.length ][ 2 ];
		int[] mins = new int[ slots.length ]; /* offsets */
		BigInteger[] maxVIs = new BigInteger[ slots.length ]; /* maximum virtual index */
		BigInteger[] utility = new BigInteger[ slots.length ]; /* utility of increasing value in slot by 1 */
		
		int i = 0;
		BigInteger curComp = BIG_INTEGER_ONE;
		
		int movingIndex = slots.length - i - 1;
		for( String str : splits ) {
			
			slots[ i ] = minMaxTuple( str );
			mins[ i ] = slots[ i ][ 0 ];
			i++;
		}
		i = 0;
		for( String str : splits ) {
			if( i == 0 ) {
				maxVIs[ movingIndex ] = new BigInteger( "" + ( slots[ movingIndex ][ 1 ] - slots[ movingIndex ][ 0 ] + 1 ) );
				utility[ movingIndex ] = BIG_INTEGER_ONE;
			}
			else if( slots[ movingIndex ][ 1 ] - slots[ movingIndex ][ 0 ] == 0  ) {
				System.out.println( movingIndex );
				maxVIs[ movingIndex ] = maxVIs[ movingIndex + 1 ].multiply( 
						new BigInteger( "" + ( slots[ movingIndex ][ 1 ] - slots[ movingIndex ][ 0 ] + 1 ) ) );
				utility[ movingIndex ] = BIG_INTEGER_ZERO;
			}
			else {
				maxVIs[ movingIndex ] = maxVIs[ movingIndex + 1 ].multiply( 
						new BigInteger( "" + ( slots[ movingIndex ][ 1 ] - slots[ movingIndex ][ 0 ] + 1 ) ) );
				utility[ movingIndex ] = maxVIs[ movingIndex + 1 ];
			}
			
			movingIndex--;
			i++;
		}
		
		for( int k = 0; k < maxVIs.length; k++ ) {
			System.out.print( maxVIs[ k ] + " " );
		}
		System.out.println();
		// min and max are populated
		System.out.println( "And now, the utility values" );
		for( int k = 0; k < maxVIs.length; k++ ) {
			System.out.print( utility[ k ] + " " );
		}
		System.out.println();
		
		final BigInteger space = computeSpace( slots );
		System.out.println( space + " combinations in any order (incl. permutations)." );
		
		BigInteger grabIndex, target;
		BigInteger leftOvers, quotient;
		Scanner systemInScanner = new Scanner( System.in );
		BigInteger[] currentCombination = new BigInteger[ slots.length ];
		for( BigInteger a = BIG_INTEGER_ONE; a.compareTo( space ) <= 0; a = a.add( BIG_INTEGER_ONE ) ) {
		/*
		while( true ) {
			try {
				grabIndex = new BigInteger( systemInScanner.nextLine() );
			}
			catch( Exception e ) {
				System.out.println( "Enter a positive integer" );
				continue;
			}*/
			/* grabIndex = new BigInteger( systemInScanner.nextLine() ); */
			grabIndex = new BigInteger( "" + a );
			int movableIndex;
			if(
			grabIndex.compareTo( space ) <= 0 && 
			grabIndex.compareTo( BIG_INTEGER_ZERO ) == 1 ) {
				
				/* Fill numbers here and to the right */
				int beginIndex = 0;
				int targetIndex = slots.length - 1;
				while( grabIndex.compareTo( maxVIs[ targetIndex ] ) == 1 ) {
					targetIndex--;
				}
				
				// System.out.println( "HERE: " + maxVIs[ targetIndex ] + ", " + targetIndex );
				currentCombination = new BigInteger[ slots.length ]; /* zero-fill */
				
				/* fill-er up with expected minimums */
				for( i = 0; i < slots.length; i++ ) {
					currentCombination[ i ] = BIG_INTEGER_ONE;
				}
				
				// currentCombination[ targetIndex ] = currentCombination[ targetIndex ].add( BIG_INTEGER_ONE ); /* Guaranteed */
				leftOvers = grabIndex;
				/*
				if( targetIndex != slots.length - 1 ) {
					leftOvers = leftOvers.subtract( maxVIs[ targetIndex + 1 ] );
					leftOvers = leftOvers.subtract( BIG_INTEGER_ONE );
				}
				*/
				leftOvers.subtract( BIG_INTEGER_ONE );
				// System.err.println( leftOvers.toString() );
				// systemInScanner.nextLine();
				
				movableIndex = targetIndex;
				while( leftOvers.compareTo( BIG_INTEGER_ZERO ) != 0 && movableIndex < slots.length - 1 ) {
					if( leftOvers.compareTo( maxVIs[ movableIndex + 1 ] ) >= 0 ) {
						BigInteger[] dAndR = leftOvers.divideAndRemainder( maxVIs[ movableIndex + 1 ] );
						quotient = dAndR[ 0 ];
						//quotient = leftOvers.divide( maxVIs[ movableIndex + 1 ] );
						
						// System.out.println( "quotient: " + quotient );
						if( dAndR[ 1 ].equals( BIG_INTEGER_ZERO ) ) {
							quotient = quotient.subtract( BIG_INTEGER_ONE );
						}
						currentCombination[ movableIndex ] = currentCombination[ movableIndex ].add( quotient );
						
						leftOvers = leftOvers.subtract( quotient.multiply( maxVIs[ movableIndex + 1 ] ) );
						if( dAndR[ 1 ].equals( BIG_INTEGER_ZERO ) ) {
							leftOvers = maxVIs[ movableIndex + 1 ];
						}
						// System.out.println( "leftOvers\tcurrentComb[moI]\n" + leftOvers + "\t" + currentCombination[ movableIndex ] );
					}
					// currentCombination[ movableIndex ] = currentCombination[ movableIndex ].add( leftOvers );
					// break;
					movableIndex++;
				}
				// System.out.println( movableIndex + " < movable " );
				if( !leftOvers.equals( BIG_INTEGER_ZERO ) )
					currentCombination[ movableIndex ] = currentCombination[ movableIndex ].multiply( leftOvers );
				
				printCombination( currentCombination );
				// System.out.println( "Number is less than " + maxVIs[ targetIndex ] );
				
			}
			else {
				System.out.println( "Index out of range." );
				systemInScanner.nextLine();
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}