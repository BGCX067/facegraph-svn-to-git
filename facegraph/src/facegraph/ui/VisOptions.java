package facegraph.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.gephi.filters.api.Range;

import swing2swt.layout.BorderLayout;
import facegraph.Facegraph;
import facegraph.constants.Const;
import facegraph.layout.Layouts;
import facegraph.layout.VisConfig;
import facegraph.layout.VisConfig.VisConfigBuilder;

public class VisOptions {
	public final Shell shell;
	private final Shell oldShell;
	private final Display display;
	private final Facegraph facegraph;
	private final FacegraphApp parent;
	private Composite composite;
	private Spinner runs_spinner;
	private Button btnCheck_Modularity;
	private Button btnCheck_RankDegree;
	private Button btncheck_RankCentral;
	private Combo combo_layout;
	private Spinner spinner_filter_min;
	private Spinner spinner_filter_max;
	private Button btnRadio_edge_line;
	private Button btnRadio_edge_curve;
	private Button btncheck_interests;
	private Spinner spinner_extra;
	private Label lblExtraOption;

	public VisOptions( Display display, Facegraph facegraph, Shell oldShell, FacegraphApp parent) {
		this.parent = parent;
		this.display = display;
		this.facegraph = facegraph;
		this.shell = new Shell( this.display );
		this.oldShell = oldShell;
		this.initialize();
	}

	private void initialize() {
		oldShell.setEnabled( false );

		shell.setMinimumSize( new Point( 50, 50 ) );
		shell.setText( "Visualization Options" );
		shell.setLayout( new BorderLayout( 0, 0 ) );

		composite = new Composite( shell, SWT.NONE );
		composite.setLayoutData( BorderLayout.SOUTH );
		composite.setLayout( new BorderLayout( 0, 0 ) );

		Group grpOptions = new Group( shell, SWT.BORDER );
		grpOptions.setText( "Options" );
		grpOptions.setLayoutData( BorderLayout.CENTER );
		grpOptions.setLayout( new FillLayout( SWT.VERTICAL ) );

		Group grpLayout = new Group( grpOptions, SWT.NONE );
		grpLayout.setText( "Layout" );
		grpLayout.setLayout( new GridLayout( 5, false ) );

		combo_layout = new Combo( grpLayout, SWT.NONE );
		combo_layout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( Layouts.lookup( combo_layout.getText() ).equals( Layouts.YIFAN_HU ))
				{
					lblExtraOption.setVisible( true );
					spinner_extra.setVisible( true );
				} else {
					lblExtraOption.setVisible( false );
					spinner_extra.setVisible( false );
				}
			}
		});
		combo_layout.setBounds( 0, 0, 92, 21 );
		for(Layouts i : Layouts.values())
			combo_layout.add( i.toString() );
		combo_layout.select( 0 );

		Label label_1 = new Label( grpLayout, SWT.NONE );
		label_1.setText( "Runs:" );
		label_1.setBounds( 0, 0, 28, 13 );

		runs_spinner = new Spinner( grpLayout, SWT.BORDER );
		runs_spinner.setIncrement( 10 );
		runs_spinner.setMaximum( 1000000 );
		runs_spinner.setMinimum( 1 );
		runs_spinner.setSelection( 1000 );

		lblExtraOption = new Label( grpLayout, SWT.HORIZONTAL );
		lblExtraOption.setText( "Optimal distance" );
		lblExtraOption.setBounds( 0, 0, 49, 13 );
		lblExtraOption.setVisible( false );
		
		spinner_extra = new Spinner(grpLayout, SWT.BORDER);
		spinner_extra.setIncrement(10);
		spinner_extra.setMaximum(800);
		spinner_extra.setMinimum(1);
		spinner_extra.setSelection(200);
		spinner_extra.setVisible( false );
		

		btnCheck_Modularity = new Button( grpLayout, SWT.CHECK );
		btnCheck_Modularity.setText( "Modularity class" );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label(grpLayout, SWT.NONE);

		btnCheck_RankDegree = new Button( grpLayout, SWT.CHECK );
		btnCheck_RankDegree.setSelection( true );
		btnCheck_RankDegree.setText( "Rank by degree" );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label(grpLayout, SWT.NONE);

		btncheck_RankCentral = new Button( grpLayout, SWT.CHECK );
		btncheck_RankCentral.setSelection( true );
		btncheck_RankCentral.setText( "Rank by centrality" );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label(grpLayout,  SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label( grpLayout, SWT.NONE );
		new Label(grpLayout, SWT.NONE);
		new Label(grpLayout, SWT.NONE);

		Group grpNodeedge = new Group( grpOptions, SWT.NONE );
		grpNodeedge.setText( "Node/Edge" );
		grpNodeedge.setLayout( new GridLayout( 5, false ) );

		Label lblFilterByDegree = new Label( grpNodeedge, SWT.NONE );
		lblFilterByDegree.setText( "Filter by degree:" );

		Label lblNewLabel = new Label( grpNodeedge, SWT.NONE );
		lblNewLabel.setText( "Min" );

		spinner_filter_min = new Spinner( grpNodeedge, SWT.BORDER );
		spinner_filter_min.setMinimum(1);

		Label lblMax = new Label( grpNodeedge, SWT.NONE );
		lblMax.setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, false, false, 1, 1 ) );
		lblMax.setText( "Max (0 = INF)" );

		spinner_filter_max = new Spinner( grpNodeedge, SWT.BORDER );

		Label lblEdgesStyle = new Label( grpNodeedge, SWT.NONE );
		lblEdgesStyle.setText( "Edges style:" );

		btnRadio_edge_line = new Button( grpNodeedge, SWT.RADIO );
		btnRadio_edge_line.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
			}
		} );
		btnRadio_edge_line.setText( "Line " );
		new Label( grpNodeedge, SWT.NONE );

		btnRadio_edge_curve = new Button( grpNodeedge, SWT.RADIO );
		btnRadio_edge_curve.setSelection( true );
		btnRadio_edge_curve.setText( "Curve" );
		new Label( grpNodeedge, SWT.NONE );

		btncheck_interests = new Button( grpNodeedge, SWT.CHECK );
		btncheck_interests.setSelection( true );
		btncheck_interests.setText( "Show Interests" );
		new Label( grpNodeedge, SWT.NONE );
		new Label( grpNodeedge, SWT.NONE );
		new Label( grpNodeedge, SWT.NONE );
		new Label( grpNodeedge, SWT.NONE );

		Composite composite_1 = new Composite( shell, SWT.NONE );
		composite_1.setLayoutData( BorderLayout.SOUTH );
		composite_1.setLayout( new RowLayout( SWT.HORIZONTAL ) );

		/* EXIT BUTTON */
		Button btnExit = new Button( composite_1, SWT.NONE );
		btnExit.setText( "Exit" );
		btnExit.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseUp( MouseEvent e ) {
				super.mouseUp( e );
				shell.close();
				
			}
		} );
		/* and also when shell will be closed enable oldshell */
		shell.addShellListener( new ShellAdapter() {
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) 
			{
				super.shellClosed( e );
				oldShell.setEnabled( true );
			};
		} );

		/* GENERATE BUTTON */
		Button btnNewButton = new Button( composite_1, SWT.CENTER );
		btnNewButton.setText( "Generate" );
		btnNewButton.addMouseListener( new GenerateButtonListener() );

		/* FINALLY */
		shell.pack();
		shell.setActive();
		shell.setVisible( true );
		btnNewButton.setFocus();
	}

	public static void start( final Display display, final FacegraphApp parent, final Facegraph facegraph ) {
		Thread s = new Thread() {
			VisConfig result = null;
			@Override
			public void run() {
				new VisOptions( display, facegraph,  display.getActiveShell(), parent);
			}
		};
		display.syncExec( s );
	}
	
	

	private class GenerateButtonListener
			extends MouseAdapter {
		@Override
		public void mouseUp( MouseEvent e ) {
			super.mouseUp( e );
			/* Create new vis configuration */
			Layouts layout =  Layouts.lookup( combo_layout.getText());
			Object[] args = layout.equals( Layouts.YIFAN_HU ) 
					? new Object[] {runs_spinner.getSelection(), (float) spinner_extra.getSelection() }
					: new Object[] {runs_spinner.getSelection()};
			
			final VisConfig visConfig = new VisConfigBuilder()
					.setLayout( layout )
					.setArgs( args )
					.setModularityClass( btnCheck_Modularity.getSelection() )
					.setRankByDegree( btnCheck_RankDegree.getSelection() )
					.setRankByCentrality( btncheck_RankCentral.getSelection() )
					.setFilterByDegree(
							new Range( spinner_filter_min.getSelection(), spinner_filter_max.getSelection() == 0 ? Integer.MAX_VALUE
									: spinner_filter_max.getSelection() ) ).setEgdes( btnRadio_edge_curve.getSelection() ? "Curve" : "Line" )
					.setShowInterests( btncheck_interests.getSelection() )
					.build();
			
			oldShell.setEnabled( true );
			parent.visualise( visConfig );
			
			shell.setVisible( false );
			
		
		}
	}
}
