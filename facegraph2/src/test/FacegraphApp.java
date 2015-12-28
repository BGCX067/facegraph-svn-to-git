package test;

import java.awt.EventQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import swing2swt.layout.BorderLayout;
import facegraph.Facegraph;
import facegraph.constants.Const;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.TabItem;

public class FacegraphApp {
	private final Display display;
	private final Shell shell;
	private final Composite composite;
	private final Button btnOpenConsol;
	private final Button btnOpenVisual;
	
	
	private final Composite composite_1;
	private final TabFolder tabFolder;
	
	
	
	private final Facegraph facegraph;

	/**
	 * Launch the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new FacegraphApp();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public FacegraphApp() {
		display = new Display();
		shell = new Shell(display);
		composite = new Composite(shell, SWT.NONE);
		btnOpenVisual = new Button(composite, SWT.NONE);
		btnOpenConsol = new Button(composite, SWT.NONE);
		
		composite_1 = new Composite(shell, SWT.NONE);
		tabFolder = new TabFolder(composite_1, SWT.NONE);
		
		facegraph = new Facegraph(
				System.getProperty(Const.USER_DATABASE_LOCATION));

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		shell.setLayout(new BorderLayout(0, 0));

		composite.setLayoutData(BorderLayout.WEST);
		composite.setLayout(new GridLayout(1, false));

		Button btnOpenHarvest = new Button(composite, SWT.NONE);
		btnOpenHarvest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnOpenHarvest.setText("Open harvest");
		btnOpenHarvest.addSelectionListener(new Open());

		btnOpenConsol.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnOpenConsol.setText("Open Consol");
		btnOpenConsol.setEnabled(false);

		btnOpenVisual.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		btnOpenVisual.setText("Open Visual");
		btnOpenVisual.setEnabled(false);
		btnOpenVisual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event)
			{
				TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				tabItem.setText("Vis");
				
				Browser browser = null;
				try {
					browser = new Browser(tabFolder, SWT.NONE);
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
					browser.setUrl("C:/Users/ravwojdyla/workspace/sockets-workspace/Facegraph/web/1/index.html");
					
					browser.refresh();
					
				}
				tabItem.setControl(browser);
				
			}
		});

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnGetMoreData = new Button(composite, SWT.NONE);
		btnGetMoreData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		btnGetMoreData.setText("Get More Data");
		btnGetMoreData.setEnabled(false);

		Button btnExit = new Button(composite, SWT.NONE);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.close();
			}
		});
		
		
		btnExit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		btnExit.setText("Exit");
		

		composite_1.setLayoutData(BorderLayout.CENTER);
		composite_1.setLayout(new BorderLayout(0, 0));
	
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
	
	private void loadData(String pathToHarvest)
	{
		facegraph.getData(pathToHarvest);	
	}

	private class Open implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			DirectoryDialog dd = new DirectoryDialog(shell);
			dd.setText("Open");
			dd.setFilterPath("C:/Users/ravwojdyla/workspace/facegraph/harvested");
			String selected = dd.open();
			
			if(selected != null)
			{
				loadData(selected);
				btnOpenVisual.setEnabled(true);
				//btnOpenConsol.setEnabled(true);
			}
			
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}
	}
	
}
