/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element.property;

import java.util.Map;

/**
 * 
 * Represents necessary properties for Human.
 */
public enum HumanProperty implements PropertySpec {
	NID("id") {
		@Override
		public String toString() {
			return "Id";
		}
	},
	FIRST_NAME("first_name") {
		@Override
		public String toString() {
			return "First name";
		}
	},
	LAST_NAME("last_name") {
		@Override
		public String toString() {
			return "Last name";
		}
	},
	/* TODO Not sure about Gender! */
	GENDER("gender") {
		@Override
		public String toString() {
			return "Gender";
		}
	},
	NTYPE("ntype") {
		@Override
		public String toString() {
			return "Network type";
		}
	};

	private final String _raw_name;

	HumanProperty( String raw_name ) {
		_raw_name = raw_name;
	}

	@Override
	public String getRawName() {
		return _raw_name;
	}

	@Override
	public String getName() {
		return toString();
	}


}
