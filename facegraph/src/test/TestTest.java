package test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.rav.common.DeleteDirectory;
import com.rav.common.collections.FixedSizeMap;
import com.rav.common.progress.WatchableTask;

public class TestTest {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
/*//		new FileCollector( "C:\\Users\\ravwojdyla\\workspace\\facegraph\\harvested", "profile" ).run();
		new WatchableTask( 100, "test" ) {
			public void run() {
				this.setProgress( 20 );
				
				this.setProgress( 40 );
			};
			
		}.start();
	}*/

		
		DeleteDirectory s = new DeleteDirectory( Paths.get(System.getProperty("user.dir")+ "/test"));
		s.start();
		try {
			s.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("juz");
	}
}
