package facegraph.layout;

import org.gephi.filters.api.Range;

public class VisConfig 
{
	/*layout*/
	private Layouts layout;
	private boolean modularityClass;
	private boolean rankByDegree;
	private boolean rankByCentrality;
	private Object[] args;
	
	/*node/edge*/
	private Range filterByDegree;
	private String egdes;
	private boolean showInterests;
	
	
	
	private VisConfig()
	{
		//nothing here.
	}
	
	private final void setLayout( Layouts layout ) {
		this.layout = layout;
	}

	private final void setModularityClass( boolean modularityClass ) {
		this.modularityClass = modularityClass;
	}

	private final void setRankByDegree( boolean rankByDegree ) {
		this.rankByDegree = rankByDegree;
	}

	private final void setRankByCentrality( boolean rankByCentrality ) {
		this.rankByCentrality = rankByCentrality;
	}

	private final void setFilterByDegree( Range filterByDegree ) {
		this.filterByDegree = filterByDegree;
	}

	private final void setEgdes( String egdes ) {
		this.egdes = egdes;
	}

	private final void setShowInterests( boolean showInterests ) {
		this.showInterests = showInterests;
	}

	public final Layouts getLayout() {
		return layout;
	}

	public final boolean isModularityClass() {
		return modularityClass;
	}

	public final boolean isRankByDegree() {
		return rankByDegree;
	}

	public final boolean isRankByCentrality() {
		return rankByCentrality;
	}

	public final Range getFilterByDegree() {
		return filterByDegree;
	}

	public final String getEgdes() {
		return egdes;
	}

	public final boolean isShowInterests() {
		return showInterests;
	}

	public final Object[] getArgs() {
		return args;
	}

	private final void setArgs( Object[] args ) {
		this.args = args;
	}

	public final static class VisConfigBuilder
	{
		private final VisConfig visConfig;
		
		public VisConfigBuilder() {
			visConfig = new VisConfig();
		}
		
		public final VisConfigBuilder setLayout( Layouts layout ) {
			this.visConfig.layout = layout;
			return this;
		}

		public final VisConfigBuilder setModularityClass( boolean modularityClass ) {
			this.visConfig.modularityClass = modularityClass;
			return this;
		}

		public final VisConfigBuilder setRankByDegree( boolean rankByDegree ) {
			this.visConfig.rankByDegree = rankByDegree;
			return this;
		}

		public final VisConfigBuilder setRankByCentrality( boolean rankByCentrality ) {
			this.visConfig.rankByCentrality = rankByCentrality;
			return this;
		}

		public final VisConfigBuilder setFilterByDegree( Range filterByDegree ) {
			this.visConfig.filterByDegree = filterByDegree;
			return this;
		}

		public final VisConfigBuilder setEgdes( String egdes ) {
			this.visConfig.egdes = egdes;
			return this;
		}

		public final VisConfigBuilder setShowInterests( boolean showInterests ) {
			this.visConfig.showInterests = showInterests;
			return this;
		}
		
		public final VisConfigBuilder setArgs( Object[] args ) {
			this.visConfig.args = args;
			return this;
		}
		
		public final VisConfig build()
		{
			return visConfig;
		}
	}
}
