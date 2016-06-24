package Primitives.Tree.DataLocations;

import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import Primitives.Tree.Nodes.RamFileNode;

public class RamDataLocation<Value extends Sizeofable & Parsable> extends DataLocation<RamFileNode<Value>>
{
    public RamDataLocation(RamFileNode<Value> node, int offset)
    {
        super(node, offset);
    }
}