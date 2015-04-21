package net.blabux.mentagy.domain;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class BoardTest {
	
	@Test
	public void testParse() {
		List<String> boardText=Arrays.asList("-----*","N-*J*-", "T--*--", "-*--*D", "*-Z*A-","---*-*");
		Board board = new Board();
		board.parse(boardText.stream());
		Stream<String> result = board.output();
		Iterator<String> iter = result.iterator();
		assertEquals(iter.next(), "-----*");
		assertEquals(iter.next(), "n-*j*-");
		assertEquals(iter.next(), "t--*--");
		assertEquals(iter.next(), "-*--*d");
		assertEquals(iter.next(), "*-z*a-");
		assertEquals(iter.next(), "---*-*");
	}

}
