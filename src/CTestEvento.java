

public class CTestEvento extends CEvento{

	@Override
	public void NotificaEvento(String Origen, String Evento, String Message) {
		System.out.println(Origen + ":" + Evento);
		System.out.println(Message);
	}
}