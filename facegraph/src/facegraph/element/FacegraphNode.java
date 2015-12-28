/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element;

import org.neo4j.graphdb.Node;

import facegraph.element.property.AbstractCategory;

/**
 *
 * Stands for abstract node in facegraph database.
 */
public abstract class FacegraphNode 
{
    protected Node _wrappedNode;
    
    protected FacegraphNode(Node node)
    {
        _wrappedNode = node;
    }
    
    public Object getProperty(String key)
    {
        return _wrappedNode.getProperty(key);
    }
    
    public Node asNode()
    {
        return _wrappedNode;
    }
    
    @Override
    public int hashCode()
    {
        return _wrappedNode.hashCode();
    }
    
    public static String getIndexObject(String id1, String id2)
    {
    	return Long.parseLong(id1) > Long.parseLong(id2) ? id1 + id2 : id2 + id1;
    }
   
}
