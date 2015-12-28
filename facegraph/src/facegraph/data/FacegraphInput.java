package facegraph.data;

import java.util.List;
import java.util.Map;

public interface FacegraphInput {
	Map<String, Object> getProperties();
	List<String> getIds();
}
