package facegraph.ui;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.neo4j.graphdb.Node;

import swing2swt.layout.BorderLayout;

import com.rav.common.DeleteDirectory;
import com.rav.common.progress.WatchableTask;

import facegraph.Facegraph;
import facegraph.LoadDataTask;
import facegraph.constants.Const;
import facegraph.constants.ErrMsg;
import facegraph.layout.VisConfig;
import facegraph.layout.Visualization;

public class FacegraphApp {
	private final Display display;
	private final Shell shlFacegraph;
	private final Composite composite;
	private final Button btnOpenConsol;
	private final Button btnOpenVisual;

	private final Composite composite_1;
	private final CTabFolder tabFolder;

	private Facegraph facegraph;
	private final ProgressBar progressBar;
	private Composite composite_5;
	private final Label loadLabel;

	private final FacegraphApp thiz;

	/* Id of active node -> displayed in left window */
	private String active_id;

	private final static Logger logger = Logger.getLogger( FacegraphApp.class );
	private Button btnDropDatabase;
	private Button btnOpenHarvest;

	/**
	 * Launch the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				try {
					logger.info( "Starting Facegraph UI" );
					new FacegraphApp();
					logger.info( "Facegraph UI started" );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} );
	}

	/**
	 * Create the application.
	 * 
	 */
	public FacegraphApp() {
		this.thiz = this;
		facegraph = new Facegraph( System.getProperty( Const.USER_DATABASE_LOCATION ) );

		display = new Display();
		shlFacegraph = new Shell( display );
		shlFacegraph.setText( "Facegraph 0.2" );
		composite = new Composite( shlFacegraph, SWT.BORDER );
		composite.setLayoutData( BorderLayout.WEST );
		RowLayout rl_composite = new RowLayout( SWT.VERTICAL );
		rl_composite.fill = true;
		rl_composite.center = true;
		composite.setLayout( rl_composite );
		btnOpenVisual = new Button( composite, SWT.NONE );
		btnOpenVisual.setText( "Open Visual" );

		if (facegraph.checkIfDbIsEmpty())
			btnOpenVisual.setEnabled( false );

		btnOpenConsol = new Button( composite, SWT.NONE );
		btnOpenConsol.setText( "Open Consol" );
		btnOpenConsol.setEnabled( false );

		btnOpenHarvest = new Button( composite, SWT.NONE );
		btnOpenHarvest.setText( "Open harvest" );

		Button btnGetMoreData = new Button( composite, SWT.NONE );
		btnGetMoreData.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseUp( MouseEvent e ) {
				try {
					if (active_id != null)
						Runtime.getRuntime().exec( "Facegraph-Savage.exe -p " + Const.USER_DIR + "\\harvest -d 1 -i " + active_id );
					else
						Runtime.getRuntime().exec( "Facegraph-Savage.exe" );
				} catch (IOException e1) {
					logger.error( "More data script failure", e1 );
					loadLabel.setText( "More data script failuer -> logs" );
				}

				loadLabel.setText( "More data script started." );
			}
		} );
		btnGetMoreData.setText( "Get More Data" );

		btnDropDatabase = new Button( composite, SWT.NONE );
		btnDropDatabase.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseUp( MouseEvent e ) {
				btnOpenVisual.setEnabled( false );
				logger.info( "Dropping database ..." );
				loadLabel.setText( "Dropping database ... " );
				facegraph.shutdown();
				facegraph = null;

				DeleteDirectory delDirThread = new DeleteDirectory( Paths.get( Const.USER_DATABASE_LOCATION ) );
				try {
					delDirThread.join();
				} catch (InterruptedException e1) {
					logger.error( "Error while dropping database. Delete files manulally -> db folder.", e1 );
					return;
				}
				logger.info( "Creating new database" );
				loadLabel.setText( "Creating new database ..." );
				facegraph = new Facegraph( Const.USER_DATABASE_LOCATION );
				loadLabel.setText( "Dropping database process - success" );
			}
		} );
		btnDropDatabase.setText( "Drop database" );

		Button btnExit = new Button( composite, SWT.NONE );
		btnExit.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseUp( MouseEvent e ) {
				logger.info( "Facegraph UI is about to closed" );
				shlFacegraph.close();
				System.exit( 0 );
			}
		} );
		btnExit.setText( "Exit" );
		btnOpenHarvest.addSelectionListener( new Open() );

		btnOpenVisual.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseUp( MouseEvent event ) {
				try {
					VisOptions.start( display, thiz, facegraph );
				} catch (Exception e) {
					logger.error( "Exception in open visualisation process", e );
					loadLabel.setText( ErrMsg.PRE_VIS_ERROR );
				}
			}
		} );

		composite_1 = new Composite( shlFacegraph, SWT.NONE );
		tabFolder = new CTabFolder( composite_1, SWT.CLOSE | SWT.UP );

		tabFolder.setSimple( false );
		tabFolder.setUnselectedCloseVisible( false );

		composite_1.setLayoutData( BorderLayout.CENTER );
		composite_1.setLayout( new BorderLayout( 0, 0 ) );

		composite_5 = new Composite( shlFacegraph, SWT.BORDER );
		composite_5.setLayoutData( BorderLayout.SOUTH );
		composite_5.setLayout( new FillLayout( SWT.HORIZONTAL ) );

		loadLabel = new Label( composite_5, SWT.NONE );
		loadLabel.setText( "Facegraph " + Const.version + " application loaded" );

		progressBar = new ProgressBar( composite_5, SWT.NONE );
		progressBar.setMaximum( 100 );
		progressBar.setMinimum( 0 );

		initialize();
	}

	/**
	 * Create process of visualization graph for config passed in arguments;
	 * 
	 * @param config
	 *            Configuration of visualization.
	 */
	public void visualise( final VisConfig config ) {
		final WatchableTask export = new Visualization( "view", facegraph ) {
			public void run() {
				try {
					this.configure();
					this.vis( config );
					System.gc();
				} catch (Exception e) {
					logger.error( ErrMsg.VIS_ERROR );
					return;
				}

				if (display.isDisposed())
					return;
				display.asyncExec( new Runnable() {
					@Override
					public void run() {

						if (tabFolder.isDisposed())
							return;
						viewVis();
						
						btnDropDatabase.setEnabled( true );
						btnOpenVisual.setEnabled( true );
						btnOpenHarvest.setEnabled( true );
					}
				} );
			};
		};
		export.start();

		display.asyncExec( new Runnable() {
			@Override
			public void run() {
				/* Brzydko trzeba oddzielna func na to */
				btnDropDatabase.setEnabled( false );
				btnOpenVisual.setEnabled( false );
				btnOpenHarvest.setEnabled( false );
			}
		} );

		display.timerExec( 10, new Runnable() {
			int progress = 0;
			String msg = "";
			char[] dots = { '|', '/', '-', '\\' };
			int i = 0;

			public void run() {
				if (progressBar.isDisposed() || loadLabel.isDisposed())
					return;
				if (progress != export.getProgress()) {
					progress = export.getProgress();
					msg = export.getMsg();
					i = 0;
					progressBar.setSelection( progress );
					loadLabel.setText( export.getMsg() );
				} else if (export.isAlive()) {
					loadLabel.setText( msg + ' ' + dots[i++ % 4] );
					if (i == 4)
						i = 0;
				} else {
					loadLabel.setText( ErrMsg.VIS_ERROR );
					progressBar.setSelection( 0 );
					return;
				}

				if (progressBar.getSelection() < progressBar.getMaximum())
					display.timerExec( 500, this );
				else {
					loadLabel.setText( export.getMsg() );
					progressBar.setSelection( 0 );
				}

			}
		} );
	}

	private void viewVis() {
		final CTabItem tabItem = new CTabItem( tabFolder, SWT.CLOSE );
		tabItem.setText( "Vis" );

		Browser browser = null;
		try {
			browser = new Browser( tabFolder, SWT.NONE );
		} catch (SWTError e) {
			/*
			 * The Browser widget throws an SWTError if it fails to instantiate
			 * properly. Application code should catch this SWTError and disable
			 * any feature requiring the Browser widget. Platform requirements
			 * for the SWT Browser widget are available from the SWT FAQ
			 * website.
			 */
			throw new IllegalAccessError();
		}

		if (browser != null) {
			Browser.clearSessions();
			browser.setUrl( System.getProperty( "user.dir" ) + "/vis/index.html" );
			new CustomFunction( browser, "jfunc" );
			browser.refresh();

		}
		tabItem.setControl( browser );
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		shlFacegraph.setLayout( new BorderLayout( 0, 0 ) );

		shlFacegraph.open();
		while (!shlFacegraph.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	private class Open
			implements SelectionListener {
		private LoadDataTask loadData = null;

		public void widgetSelected( SelectionEvent event ) {
			DirectoryDialog dd = new DirectoryDialog( shlFacegraph );
			dd.setText( "Open" );
			dd.setFilterPath( Const.USER_DIR + File.separatorChar + "harvested" );
			final String selected = dd.open();

			if (selected != null) {
				loadData = new LoadDataTask( facegraph ) {
					public void run() {
						this.getData( selected );
						if (display.isDisposed())
							return;
						display.asyncExec( new Runnable() {
							public void run() {
								if (btnOpenVisual.isDisposed())
									return;
								btnOpenVisual.setEnabled( true );
							}
						} );

					};
				};
				loadData.start();

				display.timerExec( 50, new Runnable() {
					int i = 0;

					public void run() {
						if (progressBar.isDisposed() || loadLabel.isDisposed())
							return;

						progressBar.setSelection( loadData.getProgress() );
						loadLabel.setText( loadData.getMsg() );

						if (!loadData.isAlive() && progressBar.getSelection() < progressBar.getMaximum()) {
							loadLabel.setText( ErrMsg.LOAD_DATA_ERROR );
							progressBar.setSelection( 0 );
						} else if (progressBar.getSelection() < progressBar.getMaximum())
							display.timerExec( 10, this );
						else {
							loadLabel.setText( loadData.getMsg() );
							progressBar.setSelection( 0 );
						}

					}
				} );
			}

		}

		@Override
		public void widgetDefaultSelected( SelectionEvent e ) {

		}
	}

	/* Function for javascript in browser */
	private class CustomFunction
			extends BrowserFunction {
		public CustomFunction( Browser browser, String name ) {
			super( browser, name );
		}

		@Override
		public Object function( Object[] arguments ) {
			/*
			 * Check if arg is String - it suppost to be String id of node -
			 * human node
			 */
			if (arguments[0] instanceof String) {
				String nid = (String) arguments[0];
				Node node = facegraph.getDB().findNodeByNid( nid );
				/* If there is node with that nid in database */
				if (node != null && node.hasProperty( Const.NODE_TYPE ) && node.getProperty( Const.NODE_TYPE ).equals( Const.HUMAN_TYPE ))
					active_id = nid;

			} else
				logger.error( "Value returned from scriptis not String id" );
			return null;
		}
	}
}
