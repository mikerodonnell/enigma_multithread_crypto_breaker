package com.demo.crypto.enigma.util;

import com.demo.crypto.enigma.model.SteckerCable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SteckerPairTrackerTest {

	@Test
	public void testUnsteckered() {
		SteckerCombinationTracker tracker = new SteckerCombinationTracker(0);

		// with no stecker cables, the SteckerCombinationTracker should return exactly 1 List (an empty List) of stecker combinations.
		assertTrue(tracker.hasNext());
		assertTrue(tracker.next().isEmpty());
		assertFalse(tracker.hasNext());
	}

	@Test
	public void testOnePair() {
		SteckerCombinationTracker tracker = new SteckerCombinationTracker(1);

		assertTrue(tracker.hasNext());
		List<SteckerCable> steckerPairs = tracker.next();
		assertTrue(steckerPairs.isEmpty());

		long possibleCombinationCount = getPossibleCombinationCount(1);
		for (long count = 1; count <= possibleCombinationCount; count++) {
			assertTrue(tracker.hasNext());
			steckerPairs = tracker.next();

			assertEquals(1, steckerPairs.size());

			// while we're here, spot check a few steckers
			if (count == 1) { // the first pair (other than the empty set above) should be A<=>B
				assertEquals('A', steckerPairs.get(0).getInput());
				assertEquals('B', steckerPairs.get(0).getOutput());
			} else if (count == 25) { // the 25th pair should be A<=>Z
				assertEquals('A', steckerPairs.get(0).getInput());
				assertEquals('Z', steckerPairs.get(0).getOutput());
			} else if (count == 26) { // after A<=>Z, we're done checking letter A, next pair should be B<=>C
				assertEquals('B', steckerPairs.get(0).getInput());
				assertEquals('C', steckerPairs.get(0).getOutput());
			} else if (count == possibleCombinationCount) { // and the last pair should be Y<=>Z
				assertEquals('Y', steckerPairs.get(0).getInput());
				assertEquals('Z', steckerPairs.get(0).getOutput());
			}
		}

		assertFalse(tracker.hasNext());
	}

	@Test
	public void testNextNPairs() {
		int numberOfPairs = 2;

		SteckerCombinationTracker tracker = new SteckerCombinationTracker(numberOfPairs);

		assertTrue(tracker.hasNext());
		List<SteckerCable> steckerPairs = tracker.next();
		assertTrue(steckerPairs.isEmpty());

		long possibleCombinationCount = getPossibleCombinationCount(numberOfPairs);
		for (long count = 1; count <= possibleCombinationCount; count++) {
			assertTrue(tracker.hasNext());
			steckerPairs = tracker.next();

			assertEquals(numberOfPairs, steckerPairs.size());

			// while we're here, spot check a few steckers
			if (count == 1) { // the first pairs (other than the empty set above) should be A<=>B and C<=>D
				assertEquals('A', steckerPairs.get(0).getInput());
				assertEquals('B', steckerPairs.get(0).getOutput());
				assertEquals('C', steckerPairs.get(1).getInput());
				assertEquals('D', steckerPairs.get(1).getOutput());
			} else if (count == 23) { // the 23rd pairs should be A<=>B and C<=>Z
				assertEquals('A', steckerPairs.get(0).getInput());
				assertEquals('B', steckerPairs.get(0).getOutput());
				assertEquals('C', steckerPairs.get(1).getInput());
				assertEquals('Z', steckerPairs.get(1).getOutput());
			} else if (count == possibleCombinationCount) { // and the last pairs should be W<=>X and Y<=>Z
				assertEquals('W', steckerPairs.get(0).getInput());
				assertEquals('X', steckerPairs.get(0).getOutput());
				assertEquals('Y', steckerPairs.get(1).getInput());
				assertEquals('Z', steckerPairs.get(1).getOutput());
			}
		}

		assertFalse(tracker.hasNext());
	}

	private static long getPossibleCombinationCount(int steckerCableCount) {
		long possibleCombinationCount = 0;

		if (steckerCableCount == 1) {
			/* for n==1, we can calculate the Guass Sum. the stecker cable input will take 25 values (start at A, end at Y). while the input is A there are 25 possible
			 * outputs, while the input is B there are 24 possible outputs, while the input is Y there is 1 possible output, etc.
			 * so this looks like:
			 *   25 + 24 + 23 + 22 ... + 2 + 1
			 *   which is equal to the Guass summation (25 * 25+1)/2 = 325
			 */
			possibleCombinationCount = 25 * 26 / 2;
		} else if (steckerCableCount == 2) {
			possibleCombinationCount = 49743;
		}
		// TODO: fill in up through n==10, and maybe come up with a mathematical formula for 1<n<=10

		return possibleCombinationCount;
	}
}
