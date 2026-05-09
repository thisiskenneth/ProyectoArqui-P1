package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "matricula", "descripcion" })
@XmlRootElement(name = "RegistrarOrdenMantenimientoRequest", namespace = "http://espe.edu.ec/mstallersoap")
public class RegistrarOrdenMantenimientoRequest {
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String matricula;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String descripcion;

    public String getMatricula() { return matricula; }
    public void setMatricula(String value) { this.matricula = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
}
