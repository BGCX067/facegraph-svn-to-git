/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.connection;

import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author Administrator
 */
public enum FacegraphRelationship
implements RelationshipType
{
	/**
	 * KNOWS is relationship between Human nodes.<BR>
	 * KNOWS is:<BR>
	 * - symmetric<BR>
	 * - irreflexive<BR>
	 * <BR>
	 * Relationship KNOWS has direction but it is not used.<BR>
	 * KNOWS is symmetric so if A KNOWS B then B KNOWS A,
	 * the direction of relationship does not matter.
	 */
    KNOWS
    {
        @Override
        public String toString()
        {
            return "knows";
        }
    }
    , 
    /**
     * LIKES is relationship between Human and Thing.<BR>
     * LIKES is:<BR>		
     * - irreflexive<BR>
     * - asymmetric<BR>
     * <BR>
     * Relationship LIKES has direction, and the only valid directions
     * of LIKES is from Human to Thing. So Human can LIKES Thing but Thing
     * can NOT LIKES Human.<BR>
     * StartNode = Human<BR>
     * EndNode = Thing
     */
    LIKES
    {
         @Override
         public String toString()
         {
             return "likes";
         }
    }
}
