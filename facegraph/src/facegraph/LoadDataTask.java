package facegraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Transaction;

import scala.util.control.Exception.Finally;

import com.rav.common.progress.WatchableTask;

import facegraph.constants.Const;
import facegraph.core.FacegraphDB;
import facegraph.data.adv.FacegraphJSONFileReader;

public class LoadDataTask
		extends WatchableTask {
	private final Logger _logger = Logger.getLogger( LoadDataTask.class );
	private final Facegraph facegraph;

	private static final String msg = "Loading data ... ";

	public LoadDataTask( Facegraph facegraph ) {
		super( 100, msg );
		this.facegraph = facegraph;
	}

	/**
	 * Loads data to database.
	 * 
	 * @param pathToSrcDir
	 *            points to directory which contains files directories. Each
	 *            directory represents node.
	 */
	public void getData( String pathToSrcDir ) {
		long start = System.currentTimeMillis();

		File mainDir = new File( pathToSrcDir );

		if (!mainDir.isDirectory()) {
			IllegalArgumentException ex = new IllegalArgumentException( "Path does NOT specify exisiting directory." );
			_logger.error( "Bad main data directory: " + mainDir.getAbsoluteFile(), ex );
			throw ex;
		}

		final String[] nodesDirs = mainDir.list();

		/*
		 * If there are no nodesDirs, we can finish. It means that there will be
		 * no nodes!
		 */
		if (nodesDirs.length == 0)
			return;
		this.resetTask( nodesDirs.length * 3, "Loading data ..." );

		try {
			getProfiles( mainDir.getAbsolutePath(), nodesDirs );

			getFriends( mainDir.getAbsolutePath(), nodesDirs );

			getLikes( mainDir.getAbsolutePath(), nodesDirs );
			this.setMsg( "Data loaded." );
		} catch (Exception e) {
			_logger.error( "Problem with downloaded files: ", e );
		}
		facegraph.checkIfDbIsEmpty();
		System.out.println( System.currentTimeMillis() - start );
	}

	private void getProfiles( String parentPath, String[] nodesDirs )
			throws FileNotFoundException, IOException {
		_logger.info( "Facegraph starts to load profiles ... " );
		this.setMsg( "Loading profiles ..." );

		FacegraphJSONFileReader reader = new FacegraphJSONFileReader( parentPath + File.separator + nodesDirs[0] );
		int i = 1;
		Transaction tx = null;
		try {
			for (String nodeFile : nodesDirs) {
				reader.changeDir( parentPath + File.separator + nodeFile );

				Map<String, Object> properties = reader.readProfile();
				if ((i - 1) % Const.TRANSACTION_RESET_INTERVAL == 0)
					tx = facegraph.getDB().getDB().beginTx();
				if (properties != null)
					facegraph.getDB().addNode( properties );
				if ((i - 1) % Const.TRANSACTION_RESET_INTERVAL == Const.TRANSACTION_RESET_INTERVAL - 1) {
					tx.success();
					tx.finish();
				}
				this.incProgress( "Loading profiles[" + i++ + "] ..." );
			}
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.finish();
		}
	}

	private void getFriends( String parentPath, String[] nodesDirs )
			throws FileNotFoundException, IOException {
		_logger.info( "Facegraph starts to load friends ... " );
		this.setMsg( "Loading friends ..." );

		FacegraphJSONFileReader reader = new FacegraphJSONFileReader( parentPath + File.separator + nodesDirs[0] );

		/* counter for made nodes */
		int j = 1;
		/* counter for made transactions */
		int counter = 0;
		Transaction tx = null;
		try {
			for (String nodeFile : nodesDirs) {
				reader.changeDir( parentPath + File.separator + nodeFile );

				List<String> friends = reader.readFriends();
				if (friends.size() != 0) {
					for (String i : friends) {
						if (counter == 0)
							tx = facegraph.getDB().getDB().beginTx();

						facegraph.getDB().makeFriends( nodeFile, i );
						counter++;

						if (counter == Const.TRANSACTION_RESET_INTERVAL_REL_LIKE) {
							tx.success();
							tx.finish();
							counter = 0;
						}
					}
				}
				this.incProgress( "Loading friends[" + j++ + "] ..." );
			}
			tx.success();
		} catch (Exception e) {
			_logger.error( "Error while loading friends", e );
		} finally {
			tx.finish();
		}
	}

	private void getLikes( String parentPath, String[] nodesDirs )
			throws FileNotFoundException, IOException {
		_logger.info( "Facegraph starts to load likes ... " );
		this.setMsg( "Loading likes ..." );
		FacegraphJSONFileReader reader = new FacegraphJSONFileReader( parentPath + File.separator + nodesDirs[0] );

		/* counter for made nodes */
		int j = 1;
		/* counter for made transactions */
		int counter = 0;
		Transaction tx = null;
		try {
			for (String nodeFile : nodesDirs) {
				reader.changeDir( parentPath + File.separator + nodeFile );

				List<String> likes = reader.readLikes();

				if (likes.size() != 0) {
					/* loop over likes of specific node */
					for (String i : likes) {
						if (counter == 0)
							tx = facegraph.getDB().getDB().beginTx();

						facegraph.getDB().makeLike( nodeFile, i );
						counter++;

						if (counter == Const.TRANSACTION_RESET_INTERVAL_REL_LIKE) {
							tx.success();
							tx.finish();
							counter = 0;
						}
					}
				}

				this.incProgress( "Loading likes[" + j++ + "] ..." );
			}
			tx.success();
		} catch (Exception e) {
			_logger.error( "Error while loading likes", e );
		} finally {
			tx.finish();
		}
	}
}
