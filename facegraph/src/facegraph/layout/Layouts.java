package facegraph.layout;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;

import facegraph.constants.Const;

public enum Layouts {
	FORCE_ATLAS_2 {
		@Override
		public void run( GraphModel graphModel, Object[] args ) {
			logger.info( "Starting " + this.toString() + " layout.");
			
			final boolean LINLOG_MODE = true;
			final boolean ADJUST_SIZES = true;
			final int THREAD_COUNT = 3;
			final double GRAVITY = 7.0;
			final boolean BARNES_HUT_OPTIMIZE = true;
			final double SCALING_RATIO = 100.0;
			final int RUNS = (Integer) args[0];

			final ForceAtlas2 f2 = new ForceAtlas2Builder().buildLayout();
			f2.setGraphModel( graphModel );
			f2.initAlgo();
			f2.resetPropertiesValues();
			f2.setScalingRatio( SCALING_RATIO );
			f2.setBarnesHutOptimize( BARNES_HUT_OPTIMIZE );
			f2.setGravity( GRAVITY );
			f2.setThreadsCount( THREAD_COUNT );
			f2.setLinLogMode( LINLOG_MODE );
			f2.setAdjustSizes( ADJUST_SIZES );

			for (int i = 0; i < RUNS && f2.canAlgo(); i++) {
				f2.goAlgo();
			}
			f2.endAlgo();
			
			logger.info( "Layout " + this.toString() + " did well :D" );
		}

		@Override
		public String toString() {
			return Const.LAYOUY_FORCE_ATLAS_2;
		}
	},
	YIFAN_HU {
		@Override
		public void run( GraphModel graphModel, Object[] args ) {
			logger.info( "Starting " + this.toString() + " layout." );
			
			YifanHuLayout layout = new YifanHuLayout( null, new StepDisplacement( 1f ) );
			layout.setGraphModel( graphModel );
			layout.resetPropertiesValues();
			layout.setOptimalDistance( (Float) args[1] );
			layout.setStepRatio( 1.0f );
			layout.initAlgo();

			for (int i = 0; i < (Integer) args[0] && layout.canAlgo(); i++) {
				layout.goAlgo();
			}
			layout.endAlgo();
			
			logger.info( "Layout " + this.toString() + " did well" );
		}

		@Override
		public String toString() {
			return Const.LAYOUT_YIFAN_HU;
		}
	};

	private static final Map<String, Layouts> cache = new HashMap<String, Layouts>();
	private static final Logger logger = Logger.getLogger( Layouts.class );
	
	public abstract void run( GraphModel graphModel, Object[] args );

	/**
	 * Lookup for layouts.
	 * 
	 * @param name
	 * @return
	 */
	public static Layouts lookup( String name ) {
		if (cache.get( name ) == null)
			rebuildCache();
		return cache.get( name );
	}

	
	/**
	 * Rebuilds cache in case lookup can't find specific name.
	 */
	private static void rebuildCache()
	{
		for(Layouts i : Layouts.values())
		{
			if(!cache.containsKey( i.toString() ))
				cache.put( i.toString(), i );
		}
	}
}
