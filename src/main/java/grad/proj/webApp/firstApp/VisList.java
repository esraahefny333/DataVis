package grad.proj.webApp.firstApp;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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


@Path("/visList")
public class VisList { 
/*
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
	
	
	*/
	@POST
	//@Path("/create")
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<VisData> chart(
	        @FormParam("tableName") String tableName,
	        @FormParam("xAxis") String xAxis,
	        @FormParam("yAxis") String yAxis,
	        @FormParam("aggregateFunction") String aggregateFunction,
	        @FormParam("selectorOnDimension") String selectorOnDimension,
	        @FormParam("SpecificAttribute") String SpecificAttribute,
	        @FormParam("firstOperator") String firstOperator,
	        @FormParam("firstSelector") String firstSelector,
	        @FormParam("secondOperator") String secondOperator,
	        @FormParam("secondSelector") String secondSelector
	        ) throws ClassNotFoundException, SQLException {
	
		//System.out.println(firstname);
		// VisBuilder vb = new VisBuilder();
	        VisExtract extract = new VisExtract(tableName, xAxis, yAxis,
	        		aggregateFunction, selectorOnDimension,
	        		SpecificAttribute, firstOperator, firstSelector,
	        		secondOperator, secondSelector
	        );

	    
	      ArrayList<VisData> mainVisList = new ArrayList<VisData> ();
	      ArrayList<VisData> recommendationList_bruteForce = new ArrayList<VisData> ();
	      ArrayList<VisData> recommendationList_optimized=new ArrayList<VisData> ();
	        
	        
	        mainVisList= extract.getMainVis();
	        recommendationList_bruteForce=extract.getRecommendations_bruteForce();
	        recommendationList_optimized=extract.getRecommendation_optimized();
	        
	    //----------------------------------------    
	        Result result = new Result();
			Url url = new Url();
			url.setUrl("http://kokoko.com");
		    Axis axis = new Axis();
		    Cell cell = new Cell();
 			Column column = new Column();
			
			
			Config config = new Config();
			Encoding encoding = new Encoding();
			Facet facet = new Facet();
			Scale scale = new Scale();
			Transform transform = new Transform();
			Tuple tuple = new Tuple();
			result.setData(url);
			//return result;

		  return recommendationList_bruteForce;
	}
	
}
