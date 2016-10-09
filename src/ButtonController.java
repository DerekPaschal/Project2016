public class ButtonController
{
	public static Model model;
	
	public ButtonController(Model m)
	{
		this.model = m;
	}
	
	public static void doAction(GUIButtonActions action, ActionButton button)
	{
		if (action == GUIButtonActions.START_GAME)
		{
			model.mv.setGameState(GameState.GAME);
		}
		return;
	}
}
