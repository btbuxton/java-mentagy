package net.blabux.mentagy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testParse() {
		List<String> boardText = Arrays.asList("-----*", "N-*J*-", "T--*--",
				"-*--*D", "*-Z*A-", "---*-*");
		Board board = new Board();
		board.parse(boardText.stream());
		Stream<String> result = board.output();
		Iterator<String> iter = result.iterator();
		assertThat(iter.next()).isEqualTo("-----*");
		assertThat(iter.next()).isEqualTo("n-*j*-");
		assertThat(iter.next()).isEqualTo("t--*--");
		assertThat(iter.next()).isEqualTo("-*--*d");
		assertThat(iter.next()).isEqualTo("*-z*a-");
		assertThat(iter.next()).isEqualTo("---*-*");
		assertThat(iter.hasNext()).isFalse();
	}

}
