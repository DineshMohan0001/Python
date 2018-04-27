package tas_sp2018;

/**
 * Represents the Badge used for identification for each employee
 * 
 * @author Shauntara Green, Andrew Blair, Jacob O'Dell, Derrick Godwin, Zeth Malcom
 */
public class Badge {
           private String description;
	   private String id;
	
	   public Badge(){}
           
           
	   public Badge(String description, String id){
		this.description = description;
		this.id = id;
	}
	
	   public String getDescription() {return description;}
	   public String getId() {return id;}


	   public void setDescription(String description) { this.description = description;}
	   public void setId(String id) { this.id = id;}


	   @Override
	   public String toString(){
		return "#" + getId() + " (" + getDescription() + ")";
	}
    }

