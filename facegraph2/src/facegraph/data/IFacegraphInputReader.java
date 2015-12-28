package facegraph.data;

import java.util.List;
import java.util.Map;

public interface IFacegraphInputReader 
{
	Map<String, Object> readProfile();
	List<String> readFriends();
	List<String> readLikes();
}
