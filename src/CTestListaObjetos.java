import java.util.Date;


public class CTestListaObjetos extends CListaObjetos{

	public CTestListaObjetos(CDatosServicio DatosServicio, int SegundosTimer) {
		super(DatosServicio, SegundosTimer);
	}

	@Override
	public boolean CreaObjetoPanico() {
		// TODO Auto-generated method stub
		CTestPanico Panico = new CTestPanico("PANICO", "PANICO PRUEBA", m_DatosServicio, this);
		if ( !this.m_ListaObjetos.add(Panico) ){
			return false;
		}
		Panico.DoPanico();
		return true;
	}

	@Override
	public boolean CreaObjetoAlarma(String MensajeAlarma) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CreaObjetoTema() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CreaObjetoPosicion() {
		// TODO Auto-generated method stub
		CTestPosicion Posicion = new CTestPosicion("POSICION", "Maneja Posicion", m_DatosServicio, 120, this);
		if ( !this.m_ListaObjetos.add(Posicion) ){
			return false;
		}
		return true;
	}

	@Override
	public void Show() {
		// TODO Auto-generated method stub
		System.out.println("CTestListaObjetos: TAMAÑO DE LISTA: " + String.valueOf(this.m_ListaObjetos.size()));
	}

	@Override
	protected void InformarErrorAUsuario(String Clase, String Metodo,
			int Error, String MensajeError) {
		
		Date Ahora = new Date();
		@SuppressWarnings("deprecation")
		String strAux = String.valueOf(Ahora.getHours()) + ":" + String.valueOf(Ahora.getMinutes()) + ":" + String.valueOf(Ahora.getSeconds()); 
		System.out.println(strAux + " - " + Clase + ":" + Metodo + ":" + MensajeError);
	}

	@Override
	public void NotificaEvento(String Origen, String Evento, String Message) {
		
		if ( Origen.equals("POSICION") && !Evento.equals("POSICION_ACTUALIZADA_LOCALMENTE") ) {
			ShowObject(Origen, Evento, Message);
			return;
		}

		if ( Origen.equals("PANICO")  ) {
			ShowObject(Origen, Evento, Message);
			return;
		}

		
	}

	private void ShowObject(String Origen, String Evento, String Message) {
		// TODO Auto-generated method stub
		CObjetoServidor ObjetoServidor;
		Date Ahora = new Date();
		@SuppressWarnings("deprecation")
		String strAux = String.valueOf(Ahora.getHours()) + ":" + String.valueOf(Ahora.getMinutes()) + ":" + String.valueOf(Ahora.getSeconds());
		System.out.println(strAux + " - CTestListaObjetos: NOTIFICA EVENTO - " + Origen + ":" + Evento + ":" + Message);
		ObjetoServidor = GetObjetoServidor(Message);
		if (ObjetoServidor != null ){
			ObjetoServidor.Show();
		}
		else{
			System.out.println("ERROR: NO VIENE EL ID DEL OBJETO EN EL MENSAJE DE NOTIFICACION");
		}	
	}
}
