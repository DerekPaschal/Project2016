import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class MenuText
{
	private String text;
	private Vector2D pos;
	private Color textColor, shadowColor;
	private Font font;
	
	/*******************
	 * Constructors
	 ******************/
	 
	public MenuText()
	{
		this.text = "SAMPLE TEXT";
		this.pos = new Vector2D(200, 200);
	}
	
	//Universal constructor
	private void constrMenuText(String s, Vector2D position, Color colorText, Color colorShadow)
	{
		if (s != null)
			this.text = s;
		else
			this.text = "SAMPLE TEXT";
		
		if (position != null)
			this.pos = new Vector2D((int) position.x, (int) position.y);
		else
			this.pos = new Vector2D(200, 200);
		
		if (colorText != null)
			this.textColor = colorText;
		else
			this.textColor = Color.WHITE;
		
		
		if (shadowColor != null)
			this.shadowColor = colorShadow;
		else
			this.shadowColor = Color.BLACK;
		
		this.font = new Font("MONOSPACE", Font.BOLD, 20);
	}
	
	public MenuText(String s) { constrMenuText(s, null, null, null); }
	
	public MenuText(Vector2D position) { constrMenuText(null, new Vector2D((int) position.x, (int) position.y), null, null); }
	
	public MenuText(String s, Vector2D position) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), null, null); }
	public MenuText(String s, Vector2D position, Color colorText) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), colorText, null); }
	public MenuText(String s, Vector2D position, Color colorText, Color colorShadow) { constrMenuText(s, new Vector2D((int) position.x, (int) position.y), colorText, colorShadow); }
	
	public MenuText(String s, int posX, int posY) { constrMenuText(s, new Vector2D(posX, posY), null, null); }
	public MenuText(String s, int posX, int posY, Color colorText) { constrMenuText(s, new Vector2D(posX, posY), textColor, null); }
	public MenuText(String s, int posX, int posY, Color colorText, Color colorShadow) { constrMenuText(s, new Vector2D(posX, posY), textColor, colorShadow); }
	
	/*******************
	 * Getters/Setters
	 ******************/
	public String getText() { return this.text; }
	public void setText(String s) { this.text = s; }
	
	public Vector2D getPos() { return this.pos; }
	public void setPos(Vector2D point) {	 this.pos = new Vector2D((int) point.x, (int) point.y); }
	public void setPos(int posX, int posY) { this.pos = new Vector2D(posX, posY); }
	
	public Color getTextColor() { return this.textColor; }
	public void setTextColor(Color color) { this.textColor = color; }
	
	public Color getShadowColor() { return this.shadowColor; }
	public void setShadowColor(Color color) { this.shadowColor = color; }
	
	public Font getFont() { return this.font; }
	public void setFont(Font f) { this.font = f; }
	
	
	/*******************
	 * Drawing
	 ******************/
	
	public void draw(Graphics g)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		g.setFont(this.font);
		
		g.setColor(this.shadowColor);
		g.drawString(this.text, (int) this.pos.x + 3, (int) this.pos.y + 3);
		
		g.setColor(this.textColor);
		g.drawString(this.text, (int) this.pos.x, (int) this.pos.y);
		
		g.setFont(oldFont);
		g.setColor(oldColor);
	}
}






































