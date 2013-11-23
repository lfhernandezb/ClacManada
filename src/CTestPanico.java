
public class CTestPanico extends CPanico{

	public CTestPanico(String Nombre, String Descripcion,
			CDatosServicio DatosServicio, CEvento ManejadorEvento) {
		super(Nombre, Descripcion, DatosServicio, ManejadorEvento);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Show() {
		// TODO Auto-generated method stub
		super.Show();
		System.out.println("ID=" + String.valueOf(this.m_ID));
		System.out.println("ESTADO PANICO=" + String.valueOf(this.m_EstadoPanico));
		System.out.println("MENSAJE DE PANICO ENVIADO=" + this.m_MensajePanico);
		System.out.println("MENSAJE DE RESPUESA A PANICO=" + this.m_RespuestaAPanico);
	}

	

}
