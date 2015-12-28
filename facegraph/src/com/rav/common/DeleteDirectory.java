package com.rav.common;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteDirectory 
extends Thread
{
	public DeleteDirectory(Path path)
	{
		try {
			Files.walkFileTree( path, new FileVisitor<Path>() {

				@Override
				public FileVisitResult postVisitDirectory( Path dir, IOException exc )
						throws IOException {
					Files.delete( dir );
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
					
					Files.delete( file );
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed( Path file, IOException exc )
						throws IOException {
					return FileVisitResult.TERMINATE;
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


