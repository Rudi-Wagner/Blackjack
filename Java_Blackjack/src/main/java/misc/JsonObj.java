package main.java.misc;

public class JsonObj
{
	private String type;
	private int value;


	/**
	 * Dies ist ein Konstruktor, der bei der Erstellung eines JsonObj-Objekts aufgerufen wird und dazu verwendet wird, den Typ und den Wert des Objekts zu initialisieren
	 * @param type
	 * @param value
	 * @author Rudi Wagner
	 */
	public JsonObj(String type, int value)
	{
		this.type = type;
		this.value = value;
	}


	/**
	 * Dies ist eine Getter-Methode, die den Typ des JsonObj-Objekts zurückgibt.
	 * @return
	 */
	public String getType() {
		return type;
	}


	/**
	 * Dies ist eine Getter-Methode, die den Wert des JsonObj-Objekts zurückgibt.
	 * @return
	 */
	public int getValue() {
		return value;
	}
}
