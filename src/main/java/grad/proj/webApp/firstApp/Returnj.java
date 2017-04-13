package grad.proj.webApp.firstApp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import grad.proj.webApp.firstApp.results.Axis;
import grad.proj.webApp.firstApp.results.Cell;
import grad.proj.webApp.firstApp.results.Column;
import grad.proj.webApp.firstApp.results.Config;
import grad.proj.webApp.firstApp.results.Encoding;
import grad.proj.webApp.firstApp.results.Facet;
import grad.proj.webApp.firstApp.results.Result;
import grad.proj.webApp.firstApp.results.Scale;
import grad.proj.webApp.firstApp.results.Transform;
import grad.proj.webApp.firstApp.results.Tuple;
import grad.proj.webApp.firstApp.results.Url;


@Path("/json")
public class Returnj { 

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Result chart(@QueryParam("sel") String selector)
	{
		Result result = new Result();
		Url url = new Url();
		url.setUrl("http://kokoko.com");
//		Axis axis = new Axis();
//		Cell cell = new Cell();
//		Column column = new Column();
//		Config config = new Config();
//		Encoding encoding = new Encoding();
//		Facet facet = new Facet();
//		Scale scale = new Scale();
//		Transform transform = new Transform();
//		Tuple tuple = new Tuple();
		result.setData(url);
		return result;
	}
}
