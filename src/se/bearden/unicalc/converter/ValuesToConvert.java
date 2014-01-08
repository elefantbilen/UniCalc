package se.bearden.unicalc.converter;

public class ValuesToConvert
{
	private String originalValue = "";
	private String asciiValue = "";
	private String hexValue ="";
	private String decValue ="";
	private String UTF8Value = "";
	
	public String getOriginalValue()
	{
		return originalValue;
	}
	public void setOriginalValue(String originalValue)
	{
		this.originalValue = originalValue;
	}
	public String getAsciiValue()
	{
		return asciiValue;
	}
	public void setAsciiValue(String asciiValue)
	{
		this.asciiValue = asciiValue;
	}
	public String getHexValue()
	{
		return hexValue;
	}
	public void setHexValue(String hexValue)
	{
		this.hexValue = hexValue;
	}
	public String getDecValue()
	{
		return decValue;
	}
	public void setDecValue(String decValue)
	{
		this.decValue = decValue;
	}
	public String getUTF8Value()
	{
		return UTF8Value;
	}
	public void setUTF8Value(String uTF8Value)
	{
		UTF8Value = uTF8Value;
	}

}
