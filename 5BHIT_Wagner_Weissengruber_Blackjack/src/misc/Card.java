package misc;

public class Card 
{
	private String name;
	private int value;
	
	/**
	 * Dies ist ein Konstruktor, der bei der Erstellung eines Card-Objekts aufgerufen wird und dazu verwendet wird, den Namen und den Wert der Karte zu initialisieren.
	 * @param name
	 * @param value
	 */
	public Card(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * Dies ist eine Getter-Methode, die den Namen der Karte zurückgibt.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Dies ist eine Getter-Methode, die den Wert der Karte zurückgibt.
	 * @return
	 */
	public int getValue() {
		return value;
	}
}
