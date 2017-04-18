<!DOCTYPE html>
<html>
<body>

<form action="rest/visList" method="post">
tableName:<br>
  <input type="text" name="tableName" value="Traffic_Violations" >
  <br>
  
 xAxis:<br>
  <input type="text" name="xAxis" value="Race" >
  <br>
  
yAxis:<br>
  <input type="text" name="yAxis" value="Accident" >
  <br>
  
  aggregateFunction:<br>
  <input type="text" name="aggregateFunction" value="Count" >
  <br>
  
  selectorOnDimension:<br>
  <input type="text" name="selectorOnDimension" value="'yes'">
  <br>
  
  SpecificAttribute:<br>
  <input type="text" name="SpecificAttribute" value="Gender">
  <br>
  
firstOperator:<br>
  <input type="text" name="firstOperator" value="=">
  <br>
  
  firstSelector:<br>
  <input type="text" name="firstSelector" value="'M'">
  <br>
  
  secondOperator:<br>
  <input type="text" name="secondOperator" value="=">
  <br>
  
  secondSelector:<br>
  <input type="text" name="secondSelector" value="'F'">
  <br>
  
  <input type="submit" value="Submit">
</form> 

<p>If you click the "Submit" button, the form-data will be sent to a page called "/action_page.php".</p>

</body>
</html>
