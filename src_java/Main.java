/* 
main
Kong
*/
public class Main 
{

	static Model model = new Model();

	public static void main(String args[]) 
		{

		model.loadSetting("settings.txt");

		new MainFrame().init();
	
		}
}




