package ec.com.buscadorempresas.model;

import java.util.List;

/**
 * Created by Alvaro on 18/01/2017.
 */

public class Empresa {

    private int id_empresa;
    private String nombre;
    private String direccion;
    private String telefono;
    private String descripcion_empresa;
    private String latitud;
    private String longitud;
    private int id_parroquia;
    private String ruta_imagen;
    private String path_foto;
    private byte[] foto;
    private int idtipo_empresa;
    private List<String> empresaservicios;
    private List<EmpresaHorario> empresahorarios;

    public List<String> getEmpresaservicios() {
        return empresaservicios;
    }

    public void setEmpresaservicios(List<String> empresaservicios) {
        this.empresaservicios = empresaservicios;
    }

    public List<EmpresaHorario> getEmpresahorarios() {
        return empresahorarios;
    }

    public void setEmpresahorarios(List<EmpresaHorario> empresahorarios) {
        this.empresahorarios = empresahorarios;
    }

    public Empresa(){


    }


    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() { return direccion;  }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion_empresa() {
        return descripcion_empresa;
    }

    public void setDescripcion_empresa(String descripcion_empresa) {
        this.descripcion_empresa = descripcion_empresa;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getId_parroquia() {
        return id_parroquia;
    }

    public void setId_parroquia(int id_parroquia) {
        this.id_parroquia = id_parroquia;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public String getPath_foto() {
        return path_foto;
    }

    public void setPath_foto(String path_foto) {
        this.path_foto = path_foto;
    }

    public int getIdtipo_empresa() {
        return idtipo_empresa;
    }

    public void setIdtipo_empresa(int idtipo_empresa) {
        this.idtipo_empresa = idtipo_empresa;
    }
}
