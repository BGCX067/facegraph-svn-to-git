package facegraph.data.adv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileCollector extends Thread
{
	private BlockingQueue<String> input = null;
	private String base_dir = null;
	private String pattern = null;
	
	public FileCollector(String base_dir, String pattern)
	{
		this.base_dir = base_dir;
		this.pattern = pattern;
		input = new ArrayBlockingQueue<String>( 50, true );
	}
	
	public BlockingQueue<String> getInput()
	{
		return input;
	}
	
	@Override
	public void run() {
		try {
			Files.walkFileTree(Paths.get( base_dir ), new FileVisitor<Path>() {
				private String output = null;
				
				@Override
				public FileVisitResult postVisitDirectory( Path dir, IOException exc )
						throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory( Path dir,
						BasicFileAttributes attrs ) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile( Path file,
						BasicFileAttributes attrs ) throws IOException {
					if(!file.getFileName().toString().equals( pattern ))
						return FileVisitResult.CONTINUE;
					
					/*Use of exception - because, it should be thrown really seldom*/
					try
					{
						output = Files.readAllLines( file, Charset.defaultCharset() ).get( 0 );
					}catch (IndexOutOfBoundsException e) {
						return FileVisitResult.CONTINUE;
					}
					
					if(output.startsWith( "{" ))
						try {
							input.put( output );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed( Path file, IOException exc )
						throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
