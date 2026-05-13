import type { MaintenanceOrderResult, WorkshopVehicleResult } from "../types/workshop";

const namespace = "http://espe.edu.ec/mstallersoap";

function escapeXml(value: string): string {
  return value
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&apos;");
}

function soapEnvelope(body: string): string {
  return `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tal="${namespace}">
  <soapenv:Header/>
  <soapenv:Body>${body}</soapenv:Body>
</soapenv:Envelope>`;
}

function textFromXml(xml: Document, name: string): string {
  return xml.getElementsByTagNameNS(namespace, name)[0]?.textContent ?? "";
}

async function postSoap(xml: string): Promise<Document> {
  const response = await fetch("/soap", {
    method: "POST",
    headers: { "Content-Type": "text/xml;charset=UTF-8" },
    body: xml
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  return new DOMParser().parseFromString(await response.text(), "text/xml");
}

export async function consultarVehiculo(matricula: string): Promise<WorkshopVehicleResult> {
  const xml = await postSoap(
    soapEnvelope(`
      <tal:ConsultarVehiculoRequest>
        <tal:matricula>${escapeXml(matricula)}</tal:matricula>
      </tal:ConsultarVehiculoRequest>`)
  );

  return {
    matricula: textFromXml(xml, "matricula"),
    estado: textFromXml(xml, "estado"),
    ultimoMantenimiento: textFromXml(xml, "ultimoMantenimiento"),
    observaciones: textFromXml(xml, "observaciones")
  };
}

export async function registrarOrdenMantenimiento(
  matricula: string,
  descripcion: string
): Promise<MaintenanceOrderResult> {
  const xml = await postSoap(
    soapEnvelope(`
      <tal:RegistrarOrdenMantenimientoRequest>
        <tal:matricula>${escapeXml(matricula)}</tal:matricula>
        <tal:descripcion>${escapeXml(descripcion)}</tal:descripcion>
      </tal:RegistrarOrdenMantenimientoRequest>`)
  );

  return {
    codigoOrden: textFromXml(xml, "codigoOrden"),
    fechaIngreso: textFromXml(xml, "fechaIngreso"),
    mensaje: textFromXml(xml, "mensaje")
  };
}
