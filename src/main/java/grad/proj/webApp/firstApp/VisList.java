package grad.proj.webApp.firstApp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import grad.proj.webApp.firstApp.recommendation.VisBuilder;
import grad.proj.webApp.firstApp.recommendation.VisData;
import grad.proj.webApp.firstApp.recommendation.VisExtract;
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


@Path("/VisList")
public class VisList { 

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<VisData> chart() throws ClassNotFoundException, SQLException
	{
		
		
		  VisBuilder vb = new VisBuilder();
	        VisExtract extract = new VisExtract(vb.getTableName(), vb.getXAxis(), vb.getYAxis(),
	                vb.getAggregateFunction(), vb.getSelectorOnDimension(),
	                vb.getSpecificAttribute(), vb.getFirstOperator(), vb.getfirstSelector(),
	                vb.getSecondOperator(), vb.getSecondSelector()
	        );

	      
		
		return extract.getMainVis();
	}
}
