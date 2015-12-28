package facegraph.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFacegraphInputReader 
{
	Map<String, Object> readProfile() throws IOException;
	List<String> readFriends() throws IOException;
	List<String> readLikes() throws IOException;
}
