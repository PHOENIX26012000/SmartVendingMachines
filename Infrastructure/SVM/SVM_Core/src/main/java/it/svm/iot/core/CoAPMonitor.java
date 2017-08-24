package it.svm.iot.core;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.json.JSONObject;

public class CoAPMonitor extends CoapServer
{
  private static final int COAP_PORT = 5685;
  private static Mca IN_Mca;
  public String rn;
  
  void addEndpoints()
  {
    for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
      if (((addr instanceof Inet4Address)) || (addr.isLoopbackAddress()))
      {
        InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
        addEndpoint(new CoapEndpoint(bindToAddress));
      }
    }
  }
  
  public CoAPMonitor(String name) throws SocketException
  {
	rn = name;
    add(new Resource[] { new Monitor() });
  }
  
  class Monitor extends CoapResource
  {
    public Monitor()
    {
      super(rn);
      
      getAttributes().setTitle(rn);
    }
     
    public void handlePOST(CoapExchange exchange)
    {
    	exchange.respond(ResponseCode.CREATED);
    	byte[] content = exchange.getRequestPayload();
        String contentStr = new String(content);
        System.out.println(contentStr);
        
        JSONObject root = new JSONObject(contentStr);
		JSONObject m2msgn = (JSONObject) root.get("m2m:sgn");
		JSONObject nev = (JSONObject) m2msgn.get("nev");
		JSONObject rep = (JSONObject) nev.get("rep");
		
		String uri = m2msgn.getString("sur");
	
    }
  }
}
