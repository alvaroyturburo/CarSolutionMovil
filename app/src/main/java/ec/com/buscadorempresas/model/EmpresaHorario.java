package ec.com.buscadorempresas.model;



public class EmpresaHorario {

	private int idempresa_horario;
	private String estado;
	private int id_empresa;
	private String dia;
	private String hora_inicio;
	private String hora_fin;

	public int getIdempresa_horario() {
		return idempresa_horario;
	}

	public void setIdempresa_horario(int idempresa_horario) {
		this.idempresa_horario = idempresa_horario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(int id_empresa) {
		this.id_empresa = id_empresa;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getHora_inicio() {
		return hora_inicio;
	}

	public void setHora_inicio(String hora_inicio) {
		this.hora_inicio = hora_inicio;
	}

	public String getHora_fin() {
		return hora_fin;
	}

	public void setHora_fin(String hora_fin) {
		this.hora_fin = hora_fin;
	}
}
