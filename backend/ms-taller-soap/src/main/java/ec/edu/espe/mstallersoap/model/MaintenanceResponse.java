package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ticketId",
    "scheduledDate",
    "status"
})
@XmlRootElement(name = "MaintenanceResponse", namespace = "http://espe.edu.ec/mstallersoap")
public class MaintenanceResponse {

    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String ticketId;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String scheduledDate;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String status;

    public String getTicketId() { return ticketId; }
    public void setTicketId(String value) { this.ticketId = value; }
    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String value) { this.scheduledDate = value; }
    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}
