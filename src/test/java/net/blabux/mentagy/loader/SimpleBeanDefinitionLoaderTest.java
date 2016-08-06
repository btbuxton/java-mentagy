package net.blabux.mentagy.loader;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.Test;

import net.blabux.mentagy.domain.Board;
import net.blabux.mentagy.domain.BoardDefinition;

/**
 * Created by btbuxton on 5/30/16.
 */
public class SimpleBeanDefinitionLoaderTest {

    public static final String BOARD_TEXT = "-----*\nN-*J*-\nT--*--\n-*--*D\n*-Z*A-\n---*-*";

    @Test
    public void testLoad() throws IOException {
        SimpleBoardDefinitionLoader loader = new SimpleBoardDefinitionLoader();
        StringReader reader = new StringReader(BOARD_TEXT);
        BoardDefinition def = loader.load(reader);
        Stream<String> result = def.toStringRows();
        Iterator<String> iter = result.iterator();
        assertThat(iter.next()).isEqualTo("+++++*");
        assertThat(iter.next()).isEqualTo("n+*j*+");
        assertThat(iter.next()).isEqualTo("t++*++");
        assertThat(iter.next()).isEqualTo("+*++*d");
        assertThat(iter.next()).isEqualTo("*+z*a+");
        assertThat(iter.next()).isEqualTo("+++*+*");
        assertThat(iter.hasNext()).isFalse();
    }

    @Test
    public void testApply() throws IOException {
        SimpleBoardDefinitionLoader loader = new SimpleBoardDefinitionLoader();
        StringReader reader = new StringReader(BOARD_TEXT);
        BoardDefinition def = loader.load(reader);
        Board board = new Board();
        def.apply(board);
        Iterator<String> iter = board.output().iterator();
        assertThat(iter.next()).isEqualTo("-----*");
        assertThat(iter.next()).isEqualTo("n-*j*-");
        assertThat(iter.next()).isEqualTo("t--*--");
        assertThat(iter.next()).isEqualTo("-*--*d");
        assertThat(iter.next()).isEqualTo("*-z*a-");
        assertThat(iter.next()).isEqualTo("---*-*");
        assertThat(iter.hasNext()).isFalse();
    }
}
