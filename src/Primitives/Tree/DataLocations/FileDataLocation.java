package Primitives.Tree.DataLocations;

import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import Primitives.Tree.Nodes.FileNode;

public class FileDataLocation<Value extends Sizeofable & Parsable> extends DataLocation<FileNode<Value>>
{
    public FileDataLocation(FileNode<Value> node, int offset)
    {
        super(node, offset);
    }
}