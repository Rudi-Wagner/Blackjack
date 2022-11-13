package misc;

public class JsonObj 
{
	private String type;
	private int value;
	
	public JsonObj(String type, int value) 
	{
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public int getValue() {
		return value;
	}
}
