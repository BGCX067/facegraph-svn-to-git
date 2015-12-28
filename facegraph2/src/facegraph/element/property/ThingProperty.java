/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element.property;

/**
 *
 * Represents necessary properties for Thing.
 */
public enum ThingProperty 
implements PropertySpec
{
    NID("id")
    {
        @Override
        public String toString()
        {
            return "Id";
        }
    },
    NAME("name")
    {
        @Override
        public String toString()
        {
            return "Name";
        }
    },
    CATEGORY("category")
    {
        @Override
        public String toString()
        {
            return "Category";
        }
    },
    NTYPE("ntype")
    {
    	@Override
    	public String toString()
    	{
    		return "Network type";
    	}
    };
    
    private final String _raw_name;
    private ThingProperty(String raw_name)
    {
    	_raw_name = raw_name;
    }
    
    @Override
    public String getRawName()
    {
    	return _raw_name;
    }
    
    @Override
    public String getName() {
    	return toString();
    }
}
