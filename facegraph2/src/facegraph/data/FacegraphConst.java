package facegraph.data;

public enum FacegraphConst 
{
	FACEBOOKTYPE
	{
		@Override
		public String toString()
		{
			return "Facebook";
		}
	}
	,
	MALE
	{
		@Override
		public String toString()
		{
			return "male";
		}
	}
	,
	FEMALE
	{
		@Override
		public String toString()
		{
			return "female";
		}
	}
}
