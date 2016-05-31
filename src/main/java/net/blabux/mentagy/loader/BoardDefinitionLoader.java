package net.blabux.mentagy.loader;

import net.blabux.mentagy.domain.BoardDefinition;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by btbuxton on 5/30/16.
 */
public interface BoardDefinitionLoader {
    public BoardDefinition load(Reader reader) throws IOException;
}
