/***************************
 * Purpose: SpriteList class to contain the master list
 * of all sprites in the game. It is recommended that
 * classes adding to the sprite list keep their own list
 * of sprites that have been added for easier removal
 * later. This class is used by the view components to
 * draw the contents of the sprite list to the screen.
 * 
 * Adding/removing sprites to/from the master list does
 * not change any local sprite lists. It is the
 * responsibility of the CALLER to update any local sprite
 * lists accordingly.
 * 
 * All operations except getList() are thread-safe. Use
 * the public lock (spriteListLock) before calling
 * getList() to make it thread-safe.
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.util.ArrayList;

public class SpriteList
{
	public static Object spriteListLock = new Object();
	private static PlayerShip playerShip = new PlayerShip(new Vector2D(50.0, 50.0));
	private static ArrayList<Sprite> masterSpriteList = new ArrayList<Sprite>();
	
	public SpriteList() { }
	
	//Add Sprite to the list
	//Returns true if successful, false if not added to list
	// (Usually due to sprite already present in the list)
	public static boolean addSprite(Sprite s)
	{
		synchronized(spriteListLock)
		{	
			return masterSpriteList.add(s);
		}
	}
	
	//Remove Sprite from the list.
	//Returns true if element was found in the list and removed
	public static boolean removeSprite(Sprite s)
	{
		synchronized (spriteListLock)
		{
			return masterSpriteList.remove(s);
		}
	}
	
	//This method is intended only for drawing the sprites.
	//Modifying the contents of the list should not be done
	//through this method.
	//It is recommended to synchronize on the public lock before
	//calling this method
	public static ArrayList<Sprite> getList()
	{
		return masterSpriteList;
	}
	
	public static void setPlayerShip(PlayerShip newShip)
	{
		if (newShip == null)
			throw new IllegalArgumentException("Error: Attempted to set SpriteList playerShip to null!");
		else
			synchronized(masterSpriteList)
			{
				masterSpriteList.remove(playerShip);
				
				playerShip = newShip;
				
				masterSpriteList.add(newShip);
			}
	}
	
	public static PlayerShip getPlayerShip()
	{
		if (playerShip == null)
		{
			synchronized(masterSpriteList)
			{
				playerShip = new PlayerShip(new Vector2D(50.0, 50.0));
				
				masterSpriteList.add(playerShip);
			}
		}
		
		return playerShip;
	}
}
