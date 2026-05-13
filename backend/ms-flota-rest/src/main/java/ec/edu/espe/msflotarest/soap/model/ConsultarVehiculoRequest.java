package ec.edu.espe.msflotarest.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "matricula" })
@XmlRootElement(name = "ConsultarVehiculoRequest", namespace = "http://espe.edu.ec/mstallersoap")
public class ConsultarVehiculoRequest {
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String matricula;

    public String getMatricula() { return matricula; }
    public void setMatricula(String value) { this.matricula = value; }
}
